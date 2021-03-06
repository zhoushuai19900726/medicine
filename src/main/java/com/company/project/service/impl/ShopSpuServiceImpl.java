package com.company.project.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.project.common.enums.GoodsExamineStatusEnum;
import com.company.project.common.enums.GoodsStatusEnum;
import com.company.project.common.exception.code.BaseResponseCode;
import com.company.project.common.exception.code.BusinessResponseCode;
import com.company.project.common.utils.*;
import com.company.project.entity.*;
import com.company.project.mapper.*;
import com.company.project.service.ShopCategoryService;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.company.project.service.ShopSpuService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional
@Service("shopSpuService")
public class ShopSpuServiceImpl extends ServiceImpl<ShopSpuMapper, ShopSpuEntity> implements ShopSpuService {

    @Resource
    private ShopSpuMapper shopSpuMapper;

    @Resource
    private ShopSkuMapper shopSkuMapper;

    @Resource
    private ShopBrandMapper shopBrandMapper;

    @Resource
    private ShopSellerMapper shopSellerMapper;

    @Resource
    private ShopCategoryMapper shopCategoryMapper;

    @Resource
    private ShopTemplateMapper shopTemplateMapper;

    @Resource
    private ShopCategoryService shopCategoryService;

    @Resource
    private ShopSpuOperationRecordMapper shopSpuOperationRecordMapper;

    @Resource
    private ShopSpuAuditRecordMapper shopSpuAuditRecordMapper;

    @Override
    public ShopSpuEntity getShopSpuEntityByUnique(ShopSpuEntity shopSpuEntity) {
        return shopSpuMapper.selectShopSpuEntityByUnique(shopSpuEntity);
    }

    @Override
    public ShopSpuEntity getShopSpuEntityById(String id) {
        ShopSpuEntity shopSpuEntity = shopSpuMapper.selectShopSpuEntityById(id);
        // ??????NAME
        if (Objects.nonNull(shopSpuEntity)) {
            // ????????????
            List<ShopCategoryEntity> shopEntityList = shopCategoryMapper.selectBatchIds(Sets.newHashSet(shopSpuEntity.getCategory1Id(), shopSpuEntity.getCategory2Id(), shopSpuEntity.getCategory3Id()));
            if (CollectionUtils.isNotEmpty(shopEntityList)) {
                // ??????????????????
                Map<String, String> shopCategoryEntityMap = shopEntityList.stream().collect(Collectors.toMap(ShopCategoryEntity::getId, ShopCategoryEntity::getName, (k1, k2) -> k1));
                shopSpuEntity.setCategory1Name(shopCategoryEntityMap.getOrDefault(shopSpuEntity.getCategory1Id(), DelimiterConstants.EMPTY_STR));
                shopSpuEntity.setCategory2Name(shopCategoryEntityMap.getOrDefault(shopSpuEntity.getCategory2Id(), DelimiterConstants.EMPTY_STR));
                shopSpuEntity.setCategory3Name(shopCategoryEntityMap.getOrDefault(shopSpuEntity.getCategory3Id(), DelimiterConstants.EMPTY_STR));
            }
            // ????????????
            ShopTemplateEntity shopTemplateEntity = shopTemplateMapper.selectById(shopSpuEntity.getTemplateId());
            if (Objects.nonNull(shopTemplateEntity)) {
                shopSpuEntity.setTemplateName(shopTemplateEntity.getName());
            }
        }
        return shopSpuEntity;
    }

    @Override
    public DataResult saveShopSpuEntity(ShopSpuEntity shopSpuEntity) {
        long startTime = System.currentTimeMillis();
        // ??????SPU????????????
        List<ShopSpuEntity> queryShopSpuEntityListResult = shopSpuMapper.listByCondition(Wrappers.<ShopSpuEntity>lambdaQuery().eq(ShopSpuEntity::getSn, shopSpuEntity.getSn()));
        if (CollectionUtils.isNotEmpty(queryShopSpuEntityListResult)) {
            return DataResult.fail(BusinessResponseCode.SPU_SN_REPEATED_EXISTENCE.getMsg());
        }
        // ??????SKU????????????
        List<ShopSkuEntity> shopSkuEntityList = shopSpuEntity.getShopSkuEntityList();
        if (CollectionUtils.isEmpty(shopSkuEntityList)) {
            return DataResult.fail(BusinessResponseCode.SKU_NULL.getMsg());
        }
        List<String> skuSnList = shopSkuEntityList.stream().map(ShopSkuEntity::getSn).filter(StringUtils::isNotBlank).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(skuSnList)) {
            return DataResult.fail(BusinessResponseCode.SKU_NULL.getMsg());
        }
        if (Sets.newHashSet(skuSnList).size() != skuSnList.size()) {
            return DataResult.fail(BusinessResponseCode.SKU_REPEAT.getMsg());
        }
        List<ShopSkuEntity> queryResultList = shopSkuMapper.selectList(Wrappers.<ShopSkuEntity>lambdaQuery().in(ShopSkuEntity::getSn, skuSnList));
        if (CollectionUtils.isNotEmpty(queryResultList)) {
            return DataResult.fail(BusinessResponseCode.SKU_SN_REPEATED_EXISTENCE.getMsg().concat(DelimiterConstants.COLON).concat(Joiner.on(DelimiterConstants.COMMA).skipNulls().join(queryResultList.stream().map(ShopSkuEntity::getSn).collect(Collectors.toList()))));
        }
        // ??????SPU
        shopSpuMapper.insert(shopSpuEntity);
        // ??????SKU
        shopSkuEntityList.forEach(shopSkuEntity -> shopSkuMapper.insert(shopSkuEntity.setSpuId(shopSpuEntity.getId())));
        // ??????????????????
        shopCategoryMapper.updateCount(Lists.newArrayList(new ShopCategoryEntity(shopSpuEntity.getCategory1Id(), NumberConstants.ONE), new ShopCategoryEntity(shopSpuEntity.getCategory2Id(), NumberConstants.ONE), new ShopCategoryEntity(shopSpuEntity.getCategory3Id(), NumberConstants.ONE)));
        long endTime = System.currentTimeMillis();
        // ????????????
        shopSpuAuditRecordMapper.insert(new ShopSpuAuditRecordEntity(shopSpuEntity.getId(), AuditConstant.INITIATE_AUDIT_APPLICATION));
        // ????????????
        shopSpuOperationRecordMapper.insert(new ShopSpuOperationRecordEntity(shopSpuEntity.getId(), OperationConstant.ADD, null, JSONObject.toJSONString(shopSpuEntity), (endTime - startTime)));
        return DataResult.success(shopSpuEntity);
    }

    /**
     * ????????????
     *
     * @param shopSpuEntity
     */
    private DataResult reductionGoods(ShopSpuEntity shopSpuEntity) {
        long startTime = System.currentTimeMillis();
        // ??????SPU
        shopSpuMapper.reductionShopSpuEntityById(shopSpuEntity.getId());
        // ??????SKU
        shopSkuMapper.reductionShopSpuEntityBySpuId(shopSpuEntity.getId());
        //  ??????SPU
        shopSpuEntity = shopSpuMapper.selectById(shopSpuEntity.getId());
        if (Objects.nonNull(shopSpuEntity)) {
            // ??????????????????
            shopCategoryMapper.updateCount(Lists.newArrayList(new ShopCategoryEntity(shopSpuEntity.getCategory1Id(), NumberConstants.ONE), new ShopCategoryEntity(shopSpuEntity.getCategory2Id(), NumberConstants.ONE), new ShopCategoryEntity(shopSpuEntity.getCategory3Id(), NumberConstants.ONE)));
        }
        // ????????????
        shopSpuAuditRecordMapper.insert(new ShopSpuAuditRecordEntity(shopSpuEntity.getId(), AuditConstant.INITIATE_AUDIT_APPLICATION));
        long endTime = System.currentTimeMillis();
        // ????????????
        shopSpuOperationRecordMapper.insert(new ShopSpuOperationRecordEntity(shopSpuEntity.getId(), OperationConstant.REDUCTION, null, null, (endTime - startTime)));
        return DataResult.success(shopSpuEntity);
    }

    /**
     * ?????????SPU
     *
     * @param shopSpuEntity
     * @return
     */
    private DataResult onlyUpdateSpu(ShopSpuEntity shopSpuEntity) {
        long startTime = System.currentTimeMillis();
        // ??????SPU????????????
        if (StringUtils.isNotBlank(shopSpuEntity.getSn())) {
            List<ShopSpuEntity> queryShopSpuEntityListResult = shopSpuMapper.listByCondition(Wrappers.<ShopSpuEntity>lambdaQuery().eq(ShopSpuEntity::getSn, shopSpuEntity.getSn()).ne(ShopSpuEntity::getId, shopSpuEntity.getId()));
            if (CollectionUtils.isNotEmpty(queryShopSpuEntityListResult)) {
                return DataResult.fail(BusinessResponseCode.SPU_SN_REPEATED_EXISTENCE.getMsg());
            }
        }
        // ??????????????????
        ShopSpuEntity oldShopSpuEntity = shopSpuMapper.selectShopSpuEntityById(shopSpuEntity.getId());
        if (Objects.isNull(oldShopSpuEntity)) {
            return DataResult.fail(BaseResponseCode.OPERATION_ERRO.getMsg());
        }
        if(StringUtils.isNotBlank(shopSpuEntity.getStatus())){
            // ????????????
            shopSpuAuditRecordMapper.insert(new ShopSpuAuditRecordEntity(shopSpuEntity.getId(), AuditConstant.AuditStatus(shopSpuEntity.getStatus()), shopSpuEntity.getAuditRejectionReason()));
        } else if(StringUtils.equals(shopSpuEntity.getIsMarketable(), GoodsStatusEnum.PUT_ON_THE_SHELVES.getType())){
            shopSpuEntity.setStatus(GoodsExamineStatusEnum.TO_BE_REVIEWED.getType());
            // ????????????
            shopSpuAuditRecordMapper.insert(new ShopSpuAuditRecordEntity(shopSpuEntity.getId(), AuditConstant.INITIATE_AUDIT_APPLICATION));
        }
        // ??????SPU
        shopSpuMapper.updateById(shopSpuEntity);
        // ??????SKU - ?????????SPU????????????
        if (StringUtils.isNotBlank(shopSpuEntity.getIsMarketable())) {
            shopSkuMapper.update(new ShopSkuEntity(shopSpuEntity.getIsMarketable()), Wrappers.<ShopSkuEntity>lambdaQuery().eq(ShopSkuEntity::getSpuId, shopSpuEntity.getId()));
        }
        long endTime = System.currentTimeMillis();
        // ????????????
        shopSpuOperationRecordMapper.insert(new ShopSpuOperationRecordEntity(oldShopSpuEntity.getId(), OperationConstant.UPDATE, JSONObject.toJSONString(oldShopSpuEntity), JSONObject.toJSONString(shopSpuEntity), (endTime - startTime)));
        return DataResult.success(shopSpuEntity);
    }

    /**
     * ??????SPU???SKU
     *
     * @param shopSpuEntity
     * @return
     */
    private DataResult updateSpuAndSku(ShopSpuEntity shopSpuEntity) {
        long startTime = System.currentTimeMillis();
        // ??????SPU????????????
        if (StringUtils.isNotBlank(shopSpuEntity.getSn())) {
            List<ShopSpuEntity> queryShopSpuEntityListResult = shopSpuMapper.listByCondition(Wrappers.<ShopSpuEntity>lambdaQuery().eq(ShopSpuEntity::getSn, shopSpuEntity.getSn()).ne(ShopSpuEntity::getId, shopSpuEntity.getId()));
            if (CollectionUtils.isNotEmpty(queryShopSpuEntityListResult)) {
                return DataResult.fail(BusinessResponseCode.SPU_SN_REPEATED_EXISTENCE.getMsg());
            }
        }
        // ??????????????????
        ShopSpuEntity oldShopSpuEntity = shopSpuMapper.selectShopSpuEntityById(shopSpuEntity.getId());
        if (Objects.isNull(oldShopSpuEntity)) {
            return DataResult.fail(BaseResponseCode.OPERATION_ERRO.getMsg());
        }
        // ????????????
        shopSpuEntity.setStatus(GoodsExamineStatusEnum.TO_BE_REVIEWED.getType());
        // ??????SPU
        shopSpuMapper.updateById(shopSpuEntity);
        // ????????????
        shopSpuAuditRecordMapper.insert(new ShopSpuAuditRecordEntity(shopSpuEntity.getId(), AuditConstant.INITIATE_AUDIT_APPLICATION));
        // ?????????SKU
        shopSkuMapper.delete(Wrappers.<ShopSkuEntity>lambdaQuery().eq(ShopSkuEntity::getSpuId, shopSpuEntity.getId()));
        // ?????????SKU
        shopSpuEntity.getShopSkuEntityList().forEach(shopSkuEntity -> shopSkuMapper.insert(shopSkuEntity));
        long endTime = System.currentTimeMillis();
        // ????????????
        shopSpuOperationRecordMapper.insert(new ShopSpuOperationRecordEntity(oldShopSpuEntity.getId(), OperationConstant.UPDATE, JSONObject.toJSONString(oldShopSpuEntity), JSONObject.toJSONString(shopSpuEntity), (endTime - startTime)));
        return DataResult.success(shopSpuEntity);
    }

    @Override
    public DataResult updateShopSpuEntityById(ShopSpuEntity shopSpuEntity) {
        if (NumberConstants.ZERO_I.equals(shopSpuEntity.getDeleted())) {
            //  ????????????
            return reductionGoods(shopSpuEntity);
        } else if (CollectionUtils.isNotEmpty(shopSpuEntity.getShopSkuEntityList())) {
            // ??????SPU???SKU
            return updateSpuAndSku(shopSpuEntity);
        } else {
            // ?????????SPU
            return onlyUpdateSpu(shopSpuEntity);
        }
    }

    @Override
    public DataResult removeShopSpuEntityByIds(List<String> ids) {
        long startTime = System.currentTimeMillis();
        // ??????SPU??????
        List<ShopSpuEntity> shopSpuEntityList = shopSpuMapper.selectBatchIds(ids);
        if (CollectionUtils.isNotEmpty(shopSpuEntityList)) {
            // ????????????SPU
            shopSpuMapper.deleteBatchIds(ids);
            // ????????????SKU
            shopSkuMapper.delete(Wrappers.<ShopSkuEntity>lambdaQuery().in(ShopSkuEntity::getSpuId, ids));
            // ??????????????????
            Map<String, List<ShopSpuEntity>> groupBy = Maps.newHashMap();
            groupBy.putAll(shopSpuEntityList.stream().collect(Collectors.groupingBy(ShopSpuEntity::getCategory1Id)));
            groupBy.putAll(shopSpuEntityList.stream().collect(Collectors.groupingBy(ShopSpuEntity::getCategory2Id)));
            groupBy.putAll(shopSpuEntityList.stream().collect(Collectors.groupingBy(ShopSpuEntity::getCategory3Id)));
            List<ShopCategoryEntity> shopCategoryEntityList = Lists.newArrayList();
            groupBy.forEach((k, v) -> shopCategoryEntityList.add(new ShopCategoryEntity(k, -v.size())));
            shopCategoryMapper.updateCount(shopCategoryEntityList);
        }
        long endTime = System.currentTimeMillis();
        // ????????????
        ids.forEach(id -> shopSpuOperationRecordMapper.insert(new ShopSpuOperationRecordEntity(id, OperationConstant.DELETE, null, null, (endTime - startTime))));
        return DataResult.success();
    }

    @Override
    public DataResult absolutelyRemoveShopSpuEntityByIds(List<String> ids) {
        // ????????????SPU
        shopSpuMapper.absolutelyDeleteByIds(ids);
        // ????????????SKU
        shopSkuMapper.absolutelyDeleteBySpuIds(ids);
        return DataResult.success();
    }

    @Override
    public IPage<ShopSpuEntity> listByPage(Page<ShopSpuEntity> page, LambdaQueryWrapper<ShopSpuEntity> wrapper) {
        return encapsulatingFieldName(shopSpuMapper.selectPage(page, wrapper));
    }

    @Override
    public IPage<ShopSpuEntity> recycleBinListByPage(Page<ShopSpuEntity> page, LambdaQueryWrapper<ShopSpuEntity> wrapper) {
        wrapper.eq(ShopSpuEntity::getDeleted, NumberConstants.ONE_I);
        return encapsulatingFieldName(shopSpuMapper.recycleBinListByPage(page, wrapper));
    }

    @Override
    public String getUniqueSnBySeller(String sellerId) {
        String serialNo = DelimiterConstants.EMPTY_STR;
        // ????????????
        ShopSellerEntity shopSellerEntity = shopSellerMapper.selectById(sellerId);
        if (Objects.nonNull(shopSellerEntity)) {
            // ????????????id????????????????????????????????????
            Integer n = shopSpuMapper.countByCondition(Wrappers.<ShopSpuEntity>lambdaQuery().eq(ShopSpuEntity::getSellerId, sellerId));
            int a = NumberConstants.ZERO;
            while (true) {
                a++;
                String total = String.format("%06d", n + a);
                serialNo = (StringUtils.isNotBlank(shopSellerEntity.getShortCode()) ? shopSellerEntity.getShortCode() : DelimiterConstants.PREFIX) + total;
                ShopSpuEntity shopSpuEntity = shopSpuMapper.selectOne(Wrappers.<ShopSpuEntity>lambdaQuery().eq(ShopSpuEntity::getSn, serialNo));
                if (Objects.isNull(shopSpuEntity)) {
                    break;
                }
            }
        }
        return serialNo;
    }

    /**
     * ????????????name
     *
     * @param iPage
     * @return
     */
    private IPage<ShopSpuEntity> encapsulatingFieldName(IPage<ShopSpuEntity> iPage) {
        // SPU??????
        List<ShopSpuEntity> shopSpuEntityList = iPage.getRecords();
        // ????????????
        if (CollectionUtils.isNotEmpty(shopSpuEntityList)) {
            // ??????????????????
            Map<String, String> shopCategoryEntityMap = Maps.newHashMap();
            Map<String, String> shopSellerEntityMap = Maps.newHashMap();
            Map<String, String> shopBrandEntityMap = Maps.newHashMap();
            //  ?????????ID??????
            Set<String> categoryIdSet = Sets.newHashSet();
            categoryIdSet.addAll(shopSpuEntityList.stream().map(ShopSpuEntity::getCategory1Id).collect(Collectors.toList()));
            categoryIdSet.addAll(shopSpuEntityList.stream().map(ShopSpuEntity::getCategory2Id).collect(Collectors.toList()));
            categoryIdSet.addAll(shopSpuEntityList.stream().map(ShopSpuEntity::getCategory3Id).collect(Collectors.toList()));
            if (CollectionUtils.isNotEmpty(categoryIdSet)) {
                // ??????????????????
                List<ShopCategoryEntity> shopEntityList = shopCategoryMapper.selectBatchIds(categoryIdSet);
                if (CollectionUtils.isNotEmpty(shopEntityList)) {
                    // ??????????????????
                    shopCategoryEntityMap.putAll(shopEntityList.stream().collect(Collectors.toMap(ShopCategoryEntity::getId, ShopCategoryEntity::getName, (k1, k2) -> k1)));
                }
            }
            // ????????????ID??????
            List<String> sellerIdList = shopSpuEntityList.stream().map(ShopSpuEntity::getSellerId).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(sellerIdList)) {
                // ??????????????????
                List<ShopSellerEntity> shopEntityList = shopSellerMapper.selectBatchIds(sellerIdList);
                if (CollectionUtils.isNotEmpty(shopEntityList)) {
                    // ??????????????????
                    shopSellerEntityMap.putAll(shopEntityList.stream().collect(Collectors.toMap(ShopSellerEntity::getId, ShopSellerEntity::getSellerName, (k1, k2) -> k1)));
                }
            }
            // ????????????ID??????
            List<String> brandIdList = shopSpuEntityList.stream().map(ShopSpuEntity::getBrandId).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(brandIdList)) {
                // ??????????????????
                List<ShopBrandEntity> shopEntityList = shopBrandMapper.selectBatchIds(brandIdList);
                if (CollectionUtils.isNotEmpty(shopEntityList)) {
                    // ??????????????????
                    shopBrandEntityMap.putAll(shopEntityList.stream().collect(Collectors.toMap(ShopBrandEntity::getId, ShopBrandEntity::getName, (k1, k2) -> k1)));
                }
            }
            // ??????SKU?????? - ??????????????????
            List<ShopSkuEntity> shopSkuEntityList = shopSkuMapper.selectList(Wrappers.<ShopSkuEntity>lambdaQuery().in(ShopSkuEntity::getSpuId, shopSpuEntityList.stream().map(ShopSpuEntity::getId).collect(Collectors.toList())));
            Map<String, List<ShopSkuEntity>> groupBy = shopSkuEntityList.stream().collect(Collectors.groupingBy(ShopSkuEntity::getSpuId));
            // ??????????????????
            shopSpuEntityList.forEach(shopSpuEntity -> {
                shopSpuEntity.setCategory1Name(shopCategoryEntityMap.getOrDefault(shopSpuEntity.getCategory1Id(), DelimiterConstants.EMPTY_STR));
                shopSpuEntity.setCategory2Name(shopCategoryEntityMap.getOrDefault(shopSpuEntity.getCategory2Id(), DelimiterConstants.EMPTY_STR));
                shopSpuEntity.setCategory3Name(shopCategoryEntityMap.getOrDefault(shopSpuEntity.getCategory3Id(), DelimiterConstants.EMPTY_STR));
                shopSpuEntity.setSellerName(shopSellerEntityMap.getOrDefault(shopSpuEntity.getSellerId(), DelimiterConstants.EMPTY_STR));
                shopSpuEntity.setBrandName(shopBrandEntityMap.getOrDefault(shopSpuEntity.getBrandId(), DelimiterConstants.EMPTY_STR));
                // ??????
                shopSpuEntity.setStock(groupBy.getOrDefault(shopSpuEntity.getId(), Lists.newArrayList()).stream().mapToInt(ShopSkuEntity::getNum).sum());
            });
        }
        return iPage;
    }

    @Override
    public ShopSpuEntity copyGoods(ShopSpuEntity param) {
        if (CollectionUtils.isNotEmpty(param.getCopyToCategory3IdList())) {
            long startTime = System.currentTimeMillis();
            // ????????????SPU
            ShopSpuEntity shopSpuEntity = shopSpuMapper.selectById(param.getId());
            if (Objects.nonNull(shopSpuEntity)) {
                // ????????????SKU
                List<ShopSkuEntity> shopSkuEntityList = shopSkuMapper.selectList(Wrappers.<ShopSkuEntity>lambdaQuery().eq(ShopSkuEntity::getSpuId, param.getId()));
                // ??????????????????
                List<ShopSpuEntity> finalShopSpuEntityList = Lists.newArrayList();
                List<ShopSkuEntity> finalShopSKuEntityList = Lists.newArrayList();
                // ???????????????????????????????????????????????????
                Map<String, String> parentMap = shopCategoryService.getAllParent(param.getCopyToCategory3IdList());
                param.getCopyToCategory3IdList().forEach(category3Id -> {
                    // ??????SPU
                    ShopSpuEntity copyShopSpuEntity = new ShopSpuEntity();
                    BeanUtils.copyProperties(shopSpuEntity, copyShopSpuEntity);
                    copyShopSpuEntity.setId(CommonUtils.generateUUID());// ???????????????????????????????????? SnowflakeIdWorker idWorker = new SnowflakeIdWorker(0, 0);
                    copyShopSpuEntity.setSn(CommonUtils.generateShortUUID());
                    copyShopSpuEntity.setStatus(GoodsExamineStatusEnum.TO_BE_REVIEWED.getType());
                    copyShopSpuEntity.setCategory3Id(category3Id);
                    copyShopSpuEntity.setCategory2Id(parentMap.getOrDefault(copyShopSpuEntity.getCategory3Id(), DelimiterConstants.EMPTY_STR));
                    copyShopSpuEntity.setCategory1Id(parentMap.getOrDefault(copyShopSpuEntity.getCategory2Id(), DelimiterConstants.EMPTY_STR));
                    finalShopSpuEntityList.add(copyShopSpuEntity);
                    // ??????SKU
                    List<ShopSkuEntity> copyShopSkuEntityList = shopSkuEntityList.stream().map(shopSkuEntity -> {
                        ShopSkuEntity copyShopSkuEntity = new ShopSkuEntity();
                        BeanUtils.copyProperties(shopSkuEntity, copyShopSkuEntity);
                        copyShopSkuEntity.setId(CommonUtils.generateUUID());
                        copyShopSkuEntity.setSn(CommonUtils.generateShortUUID());
                        copyShopSkuEntity.setSpuId(copyShopSpuEntity.getId());
                        copyShopSkuEntity.setCategoryId(category3Id);
                        copyShopSkuEntity.setCategoryName(DelimiterConstants.EMPTY_STR);
                        return copyShopSkuEntity;
                    }).collect(Collectors.toList());
                    finalShopSKuEntityList.addAll(copyShopSkuEntityList);
                });
                finalShopSpuEntityList.forEach(finalShopSPuEntity -> {
                    // ??????SPU
                    shopSpuMapper.insert(finalShopSPuEntity);
                    // ????????????
                    shopSpuAuditRecordMapper.insert(new ShopSpuAuditRecordEntity(finalShopSPuEntity.getId(), AuditConstant.INITIATE_AUDIT_APPLICATION));
                });
                // ??????SKU
                finalShopSKuEntityList.forEach(finalShopSKuEntity -> shopSkuMapper.insert(finalShopSKuEntity));
                // ??????????????????
                List<ShopCategoryEntity> shopCategoryEntityList = Lists.newArrayList();
                parentMap.keySet().forEach(k -> shopCategoryEntityList.add(new ShopCategoryEntity(k, NumberConstants.ONE)));
                shopCategoryMapper.updateCount(shopCategoryEntityList);
                long endTime = System.currentTimeMillis();
                // ????????????
                finalShopSpuEntityList.forEach(finalShopSPuEntity -> shopSpuOperationRecordMapper.insert(new ShopSpuOperationRecordEntity(finalShopSPuEntity.getId(), OperationConstant.COPY, null, JSONObject.toJSONString(finalShopSPuEntity), (endTime - startTime))));
            }
        }
        return param;
    }

    @Override
    public ShopSpuEntity tansferGoods(ShopSpuEntity param) {
        if (StringUtils.isNotBlank(param.getTransferToCategory3Id())) {
            long startTime = System.currentTimeMillis();
            // ????????????SPU
            ShopSpuEntity shopSpuEntity = shopSpuMapper.selectById(param.getId());
            if (Objects.nonNull(shopSpuEntity)) {
                // ????????????????????? -1
                shopCategoryMapper.updateCount(Lists.newArrayList(new ShopCategoryEntity(shopSpuEntity.getCategory1Id(), NumberConstants.MINUS_ONE), new ShopCategoryEntity(shopSpuEntity.getCategory2Id(), NumberConstants.MINUS_ONE), new ShopCategoryEntity(shopSpuEntity.getCategory3Id(), NumberConstants.MINUS_ONE)));
                // ?????????
                Map<String, String> parentMap = shopCategoryService.getAllParent(Lists.newArrayList(param.getTransferToCategory3Id()));
                // ?????????SPU
                ShopSpuEntity updSpu = new ShopSpuEntity();
                updSpu.setId(shopSpuEntity.getId());
                updSpu.setStatus(GoodsExamineStatusEnum.TO_BE_REVIEWED.getType());
                updSpu.setCategory3Id(param.getTransferToCategory3Id());
                updSpu.setCategory2Id(parentMap.getOrDefault(updSpu.getCategory3Id(), DelimiterConstants.EMPTY_STR));
                updSpu.setCategory1Id(parentMap.getOrDefault(updSpu.getCategory2Id(), DelimiterConstants.EMPTY_STR));
                shopSpuMapper.updateById(updSpu);
                // ????????????
                shopSpuAuditRecordMapper.insert(new ShopSpuAuditRecordEntity(updSpu.getId(), AuditConstant.INITIATE_AUDIT_APPLICATION));
                // ????????????
                ShopCategoryEntity shopCategoryEntity = shopCategoryMapper.selectById(updSpu.getCategory3Id());
                // ?????????SKU
                shopSkuMapper.update(new ShopSkuEntity(updSpu.getCategory3Id(), Objects.nonNull(shopCategoryEntity) ? shopCategoryEntity.getName() : DelimiterConstants.EMPTY_STR), Wrappers.<ShopSkuEntity>lambdaQuery().eq(ShopSkuEntity::getSpuId, updSpu.getId()));
                // ????????????????????? +1
                shopCategoryMapper.updateCount(Lists.newArrayList(new ShopCategoryEntity(updSpu.getCategory1Id(), NumberConstants.ONE), new ShopCategoryEntity(updSpu.getCategory2Id(), NumberConstants.ONE), new ShopCategoryEntity(updSpu.getCategory3Id(), NumberConstants.ONE)));
                long endTime = System.currentTimeMillis();
                // ????????????
                shopSpuOperationRecordMapper.insert(new ShopSpuOperationRecordEntity(shopSpuEntity.getId(), OperationConstant.TRANSFER, JSONObject.toJSONString(shopSpuEntity), JSONObject.toJSONString(updSpu), (endTime - startTime)));
            }
        }
        return param;
    }

    @Override
    public List<ShopSpuEntity> listByIdList(List<String> spuIdList) {
        List<ShopSpuEntity> shopSpuEntityList = shopSpuMapper.listByCondition(Wrappers.<ShopSpuEntity>lambdaQuery().in(ShopSpuEntity::getId, spuIdList));
        IPage<ShopSpuEntity> iPage = new Page<>();
        iPage.setRecords(shopSpuEntityList);
        encapsulatingFieldName(iPage);
        return shopSpuEntityList;
    }


}
