package com.company.project.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.project.common.enums.GoodsExamineStatusEnum;
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
    private ShopSpuOperationRecordMapper shopSpuOperationRecordMapper;

    @Resource
    private ShopCategoryService shopCategoryService;

    @Override
    public ShopSpuEntity getShopSpuEntityByUnique(String unique) {
        return shopSpuMapper.selectShopSpuEntityByUnique(unique);
    }

    @Override
    public ShopSpuEntity getShopSpuEntityById(String id) {
        ShopSpuEntity shopSpuEntity = shopSpuMapper.selectShopSpuEntityById(id);
        // 封装NAME
        if (Objects.nonNull(shopSpuEntity)) {
            // 商品分类
            List<ShopCategoryEntity> shopEntityList = shopCategoryMapper.selectBatchIds(Sets.newHashSet(shopSpuEntity.getCategory1Id(), shopSpuEntity.getCategory2Id(), shopSpuEntity.getCategory3Id()));
            if (CollectionUtils.isNotEmpty(shopEntityList)) {
                // 封装分类名称
                Map<String, String> shopCategoryEntityMap = shopEntityList.stream().collect(Collectors.toMap(ShopCategoryEntity::getId, ShopCategoryEntity::getName, (k1, k2) -> k1));
                shopSpuEntity.setCategory1Name(shopCategoryEntityMap.getOrDefault(shopSpuEntity.getCategory1Id(), DelimiterConstants.EMPTY_STR));
                shopSpuEntity.setCategory2Name(shopCategoryEntityMap.getOrDefault(shopSpuEntity.getCategory2Id(), DelimiterConstants.EMPTY_STR));
                shopSpuEntity.setCategory3Name(shopCategoryEntityMap.getOrDefault(shopSpuEntity.getCategory3Id(), DelimiterConstants.EMPTY_STR));
            }
            // 商品模板
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
        // 校验SPU商品货号
        List<ShopSpuEntity> queryShopSpuEntityListResult = shopSpuMapper.selectList(Wrappers.<ShopSpuEntity>lambdaQuery().eq(ShopSpuEntity::getSn, shopSpuEntity.getSn()));
        if (CollectionUtils.isNotEmpty(queryShopSpuEntityListResult)) {
            return DataResult.fail(BusinessResponseCode.SPU_SN_REPEATED_EXISTENCE.getMsg());
        }
        // 校验SKU商品货号
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
        // 保存SPU
        shopSpuMapper.insert(shopSpuEntity);
        // 保存SKU
        shopSkuEntityList.forEach(shopSkuEntity -> shopSkuMapper.insert(shopSkuEntity.setSpuId(shopSpuEntity.getId())));
        // 分类中商品数
        shopCategoryMapper.updateCount(Lists.newArrayList(new ShopCategoryEntity(shopSpuEntity.getCategory1Id(), NumberConstants.ONE), new ShopCategoryEntity(shopSpuEntity.getCategory2Id(), NumberConstants.ONE), new ShopCategoryEntity(shopSpuEntity.getCategory3Id(), NumberConstants.ONE)));
        long endTime = System.currentTimeMillis();
        // 操作记录
        shopSpuOperationRecordMapper.insert(new ShopSpuOperationRecordEntity(shopSpuEntity.getId(), OperationConstant.ADD, null, JSONObject.toJSONString(shopSpuEntity), (endTime - startTime)));
        return DataResult.success(shopSpuEntity);
    }

    @Override
    public DataResult updateShopSpuEntityById(ShopSpuEntity shopSpuEntity) {
        long startTime = System.currentTimeMillis();
        if (NumberConstants.ZERO_I.equals(shopSpuEntity.getDeleted())) {
            // 还原SPU
            shopSpuMapper.reductionShopSpuEntityById(shopSpuEntity.getId());
            // 还原SKU
            shopSkuMapper.reductionShopSpuEntityBySpuId(shopSpuEntity.getId());
            //  查询SPU
            shopSpuEntity = shopSpuMapper.selectById(shopSpuEntity.getId());
            if (Objects.nonNull(shopSpuEntity)) {
                // 分类中商品数
                shopCategoryMapper.updateCount(Lists.newArrayList(new ShopCategoryEntity(shopSpuEntity.getCategory1Id(), NumberConstants.ONE), new ShopCategoryEntity(shopSpuEntity.getCategory2Id(), NumberConstants.ONE), new ShopCategoryEntity(shopSpuEntity.getCategory3Id(), NumberConstants.ONE)));
            }
            long endTime = System.currentTimeMillis();
            // 操作记录
            shopSpuOperationRecordMapper.insert(new ShopSpuOperationRecordEntity(shopSpuEntity.getId(), OperationConstant.REDUCTION, null, null, (endTime - startTime)));
        } else {
            // 校验SPU商品货号
            if (StringUtils.isNotBlank(shopSpuEntity.getSn())) {
                List<ShopSpuEntity> queryShopSpuEntityListResult = shopSpuMapper.selectList(Wrappers.<ShopSpuEntity>lambdaQuery().eq(ShopSpuEntity::getSn, shopSpuEntity.getSn()).ne(ShopSpuEntity::getId, shopSpuEntity.getId()));
                if (CollectionUtils.isNotEmpty(queryShopSpuEntityListResult)) {
                    return DataResult.fail(BusinessResponseCode.SPU_SN_REPEATED_EXISTENCE.getMsg());
                }
            }
            // 查询原始对象
            ShopSpuEntity oldShopSpuEntity = shopSpuMapper.selectShopSpuEntityById(shopSpuEntity.getId());
            if (Objects.isNull(oldShopSpuEntity)) {
                return DataResult.fail(BaseResponseCode.OPERATION_ERRO.getMsg());
            }
            // 更新SPU
            shopSpuMapper.updateById(shopSpuEntity);
            // TODO 更新SKU - 及状态


            long endTime = System.currentTimeMillis();
            // 操作记录
            shopSpuOperationRecordMapper.insert(new ShopSpuOperationRecordEntity(oldShopSpuEntity.getId(), OperationConstant.UPDATE, JSONObject.toJSONString(oldShopSpuEntity), JSONObject.toJSONString(shopSpuEntity), (endTime - startTime)));
        }

        return DataResult.success(shopSpuEntity);
    }

    @Override
    public DataResult removeShopSpuEntityByIds(List<String> ids) {
        long startTime = System.currentTimeMillis();
        // 查询SPU集合
        List<ShopSpuEntity> shopSpuEntityList = shopSpuMapper.selectBatchIds(ids);
        if (CollectionUtils.isNotEmpty(shopSpuEntityList)) {
            // 逻辑删除SPU
            shopSpuMapper.deleteBatchIds(ids);
            // 逻辑删除SKU
            shopSkuMapper.delete(Wrappers.<ShopSkuEntity>lambdaQuery().in(ShopSkuEntity::getSpuId, ids));
            // 分类中商品数
            Map<String, List<ShopSpuEntity>> groupBy = Maps.newHashMap();
            groupBy.putAll(shopSpuEntityList.stream().collect(Collectors.groupingBy(ShopSpuEntity::getCategory1Id)));
            groupBy.putAll(shopSpuEntityList.stream().collect(Collectors.groupingBy(ShopSpuEntity::getCategory2Id)));
            groupBy.putAll(shopSpuEntityList.stream().collect(Collectors.groupingBy(ShopSpuEntity::getCategory3Id)));
            List<ShopCategoryEntity> shopCategoryEntityList = Lists.newArrayList();
            groupBy.forEach((k, v) -> shopCategoryEntityList.add(new ShopCategoryEntity(k, -v.size())));
            shopCategoryMapper.updateCount(shopCategoryEntityList);
        }
        long endTime = System.currentTimeMillis();
        // 操作记录
        ids.forEach(id -> shopSpuOperationRecordMapper.insert(new ShopSpuOperationRecordEntity(id, OperationConstant.DELETE, null, null, (endTime - startTime))));
        return DataResult.success();
    }

    @Override
    public DataResult absolutelyRemoveShopSpuEntityByIds(List<String> ids) {
        // 物理删除SPU
        shopSpuMapper.absolutelyDeleteByIds(ids);
        // 物理删除SKU
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
        // 查询店铺
        ShopSellerEntity shopSellerEntity = shopSellerMapper.selectById(sellerId);
        if (Objects.nonNull(shopSellerEntity)) {
            // 根据店铺id查找店铺发布了多少个商品
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
     * 封装字段name
     *
     * @param iPage
     * @return
     */
    private IPage<ShopSpuEntity> encapsulatingFieldName(IPage<ShopSpuEntity> iPage) {
        List<ShopSpuEntity> shopSpuEntityList = iPage.getRecords();
        // 封装名称
        if (CollectionUtils.isNotEmpty(shopSpuEntityList)) {
            // 查询结果封装
            Map<String, String> shopCategoryEntityMap = Maps.newHashMap();
            Map<String, String> shopSellerEntityMap = Maps.newHashMap();
            Map<String, String> shopBrandEntityMap = Maps.newHashMap();
            //  提分类ID集合
            Set<String> categoryIdSet = Sets.newHashSet();
            categoryIdSet.addAll(shopSpuEntityList.stream().map(ShopSpuEntity::getCategory1Id).collect(Collectors.toList()));
            categoryIdSet.addAll(shopSpuEntityList.stream().map(ShopSpuEntity::getCategory2Id).collect(Collectors.toList()));
            categoryIdSet.addAll(shopSpuEntityList.stream().map(ShopSpuEntity::getCategory3Id).collect(Collectors.toList()));
            if (CollectionUtils.isNotEmpty(categoryIdSet)) {
                // 查询分类集合
                List<ShopCategoryEntity> shopEntityList = shopCategoryMapper.selectBatchIds(categoryIdSet);
                if (CollectionUtils.isNotEmpty(shopEntityList)) {
                    // 封装分类名称
                    shopCategoryEntityMap.putAll(shopEntityList.stream().collect(Collectors.toMap(ShopCategoryEntity::getId, ShopCategoryEntity::getName, (k1, k2) -> k1)));
                }
            }
            // 提取商家ID集合
            List<String> sellerIdList = shopSpuEntityList.stream().map(ShopSpuEntity::getSellerId).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(sellerIdList)) {
                // 查询商家集合
                List<ShopSellerEntity> shopEntityList = shopSellerMapper.selectBatchIds(sellerIdList);
                if (CollectionUtils.isNotEmpty(shopEntityList)) {
                    // 封装商家名称
                    shopSellerEntityMap.putAll(shopEntityList.stream().collect(Collectors.toMap(ShopSellerEntity::getId, ShopSellerEntity::getSellerName, (k1, k2) -> k1)));
                }
            }
            // 提取品牌ID集合
            List<String> brandIdList = shopSpuEntityList.stream().map(ShopSpuEntity::getBrandId).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(brandIdList)) {
                // 查询品牌集合
                List<ShopBrandEntity> shopEntityList = shopBrandMapper.selectBatchIds(brandIdList);
                if (CollectionUtils.isNotEmpty(shopEntityList)) {
                    // 封装品牌名称
                    shopBrandEntityMap.putAll(shopEntityList.stream().collect(Collectors.toMap(ShopBrandEntity::getId, ShopBrandEntity::getName, (k1, k2) -> k1)));
                }
            }
            // 执行封装名称
            shopSpuEntityList.forEach(shopSpuEntity -> {
                shopSpuEntity.setCategory1Name(shopCategoryEntityMap.getOrDefault(shopSpuEntity.getCategory1Id(), DelimiterConstants.EMPTY_STR));
                shopSpuEntity.setCategory2Name(shopCategoryEntityMap.getOrDefault(shopSpuEntity.getCategory2Id(), DelimiterConstants.EMPTY_STR));
                shopSpuEntity.setCategory3Name(shopCategoryEntityMap.getOrDefault(shopSpuEntity.getCategory3Id(), DelimiterConstants.EMPTY_STR));
                shopSpuEntity.setSellerName(shopSellerEntityMap.getOrDefault(shopSpuEntity.getSellerId(), DelimiterConstants.EMPTY_STR));
                shopSpuEntity.setBrandName(shopBrandEntityMap.getOrDefault(shopSpuEntity.getBrandId(), DelimiterConstants.EMPTY_STR));
            });
        }
        return iPage;
    }

    @Override
    public ShopSpuEntity copyGoods(ShopSpuEntity param) {
        if (CollectionUtils.isNotEmpty(param.getCopyToCategory3IdList())) {
            long startTime = System.currentTimeMillis();
            // 查询商品SPU
            ShopSpuEntity shopSpuEntity = shopSpuMapper.selectById(param.getId());
            if (Objects.nonNull(shopSpuEntity)) {
                // 查询商品SKU
                List<ShopSkuEntity> shopSkuEntityList = shopSkuMapper.selectList(Wrappers.<ShopSkuEntity>lambdaQuery().eq(ShopSkuEntity::getSpuId, param.getId()));
                // 声明落库数据
                List<ShopSpuEntity> finalShopSpuEntityList = Lists.newArrayList();
                List<ShopSkuEntity> finalShopSKuEntityList = Lists.newArrayList();
                // 查询所有三级分类的上级、上级的上级
                Map<String, String> parentMap = shopCategoryService.getAllParent(param.getCopyToCategory3IdList());
                param.getCopyToCategory3IdList().forEach(category3Id -> {
                    // 复制SPU
                    ShopSpuEntity copyShopSpuEntity = new ShopSpuEntity();
                    BeanUtils.copyProperties(shopSpuEntity, copyShopSpuEntity);
                    copyShopSpuEntity.setId(CommonUtils.generateUUID());
                    copyShopSpuEntity.setSn(CommonUtils.generateShortUUID());
                    copyShopSpuEntity.setStatus(GoodsExamineStatusEnum.TO_BE_REVIEWED.getType());
                    copyShopSpuEntity.setCategory3Id(category3Id);
                    copyShopSpuEntity.setCategory2Id(parentMap.getOrDefault(copyShopSpuEntity.getCategory3Id(), DelimiterConstants.EMPTY_STR));
                    copyShopSpuEntity.setCategory1Id(parentMap.getOrDefault(copyShopSpuEntity.getCategory2Id(), DelimiterConstants.EMPTY_STR));
                    finalShopSpuEntityList.add(copyShopSpuEntity);
                    // 复制SKU
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
                // 保存SPU
                finalShopSpuEntityList.forEach(finalShopSPuEntity -> shopSpuMapper.insert(finalShopSPuEntity));
                // 保存SKU
                finalShopSKuEntityList.forEach(finalShopSKuEntity -> shopSkuMapper.insert(finalShopSKuEntity));
                // 分类中商品数
                List<ShopCategoryEntity> shopCategoryEntityList = Lists.newArrayList();
                parentMap.keySet().forEach(k -> shopCategoryEntityList.add(new ShopCategoryEntity(k, NumberConstants.ONE)));
                shopCategoryMapper.updateCount(shopCategoryEntityList);
                long endTime = System.currentTimeMillis();
                // 操作记录
                finalShopSpuEntityList.forEach(finalShopSPuEntity -> shopSpuOperationRecordMapper.insert(new ShopSpuOperationRecordEntity(finalShopSPuEntity.getId(), OperationConstant.COPY, null, JSONObject.toJSONString(finalShopSPuEntity), (endTime - startTime))));
            }
        }
        return param;
    }

    @Override
    public ShopSpuEntity tansferGoods(ShopSpuEntity param) {
        if (StringUtils.isNotBlank(param.getTransferToCategory3Id())) {
            long startTime = System.currentTimeMillis();
            // 查询商品SPU
            ShopSpuEntity shopSpuEntity = shopSpuMapper.selectById(param.getId());
            if (Objects.nonNull(shopSpuEntity)) {
                // 查询商品SKU
                List<ShopSkuEntity> shopSkuEntityList = shopSkuMapper.selectList(Wrappers.<ShopSkuEntity>lambdaQuery().eq(ShopSkuEntity::getSpuId, param.getId()));
                // 原分类中商品数 -1
                shopCategoryMapper.updateCount(Lists.newArrayList(new ShopCategoryEntity(shopSpuEntity.getCategory1Id(), NumberConstants.MINUS_ONE), new ShopCategoryEntity(shopSpuEntity.getCategory2Id(), NumberConstants.MINUS_ONE), new ShopCategoryEntity(shopSpuEntity.getCategory3Id(), NumberConstants.MINUS_ONE)));
                // 现分类
                Map<String, String> parentMap = shopCategoryService.getAllParent(Lists.newArrayList(param.getTransferToCategory3Id()));
                // 修改原SPU
                ShopSpuEntity updSpu = new ShopSpuEntity();
                updSpu.setId(shopSpuEntity.getId());
                updSpu.setCategory3Id(param.getTransferToCategory3Id());
                updSpu.setCategory2Id(parentMap.getOrDefault(updSpu.getCategory3Id(), DelimiterConstants.EMPTY_STR));
                updSpu.setCategory1Id(parentMap.getOrDefault(updSpu.getCategory2Id(), DelimiterConstants.EMPTY_STR));
                shopSpuMapper.updateById(updSpu);
                // 修改原SKU
                shopSkuEntityList.forEach(shopSkuEntity -> {
                    ShopSkuEntity updSku = new ShopSkuEntity();
                    updSku.setId(shopSkuEntity.getId());
                    updSku.setSpuId(updSpu.getId());
                    updSku.setCategoryId(updSpu.getCategory3Id());
                    updSku.setCategoryName(DelimiterConstants.EMPTY_STR);
                    shopSkuMapper.updateById(updSku);
                });
                // 现分类中商品数 +1
                shopCategoryMapper.updateCount(Lists.newArrayList(new ShopCategoryEntity(updSpu.getCategory1Id(), NumberConstants.ONE), new ShopCategoryEntity(updSpu.getCategory2Id(), NumberConstants.ONE), new ShopCategoryEntity(updSpu.getCategory3Id(), NumberConstants.ONE)));
                long endTime = System.currentTimeMillis();
                // 操作记录
                shopSpuOperationRecordMapper.insert(new ShopSpuOperationRecordEntity(shopSpuEntity.getId(), OperationConstant.TRANSFER, JSONObject.toJSONString(shopSpuEntity), JSONObject.toJSONString(updSpu), (endTime - startTime)));
            }
        }
        return param;
    }


}
