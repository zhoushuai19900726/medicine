package com.company.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.project.common.utils.DataResult;
import com.company.project.common.utils.DelimiterConstants;
import com.company.project.common.utils.NumberConstants;
import com.company.project.entity.ShopSellerEntity;
import com.company.project.entity.ShopTransportExtendEntity;
import com.company.project.mapper.ShopSellerMapper;
import com.company.project.mapper.ShopTransportExtendMapper;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.company.project.mapper.ShopTransportMapper;
import com.company.project.entity.ShopTransportEntity;
import com.company.project.service.ShopTransportService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Transactional
@Service("shopTransportService")
public class ShopTransportServiceImpl extends ServiceImpl<ShopTransportMapper, ShopTransportEntity> implements ShopTransportService {

    @Resource
    private ShopTransportMapper shopTransportMapper;
    @Resource
    private ShopTransportExtendMapper shopTransportExtendMapper;

    @Resource
    private ShopSellerMapper shopSellerMapper;

    @Override
    public IPage<ShopTransportEntity> listByPage(Page<ShopTransportEntity> page, LambdaQueryWrapper<ShopTransportEntity> wrapper) {
        return encapsulatingFieldName(shopTransportMapper.selectPage(page, wrapper));
    }

    private IPage<ShopTransportEntity> encapsulatingFieldName(IPage<ShopTransportEntity> iPage) {
        // ????????????
        List<ShopTransportEntity> shopTransportEntityList = iPage.getRecords();
        // ????????????
        if (CollectionUtils.isNotEmpty(shopTransportEntityList)) {
            // ??????????????????
            Map<String, String> sellerMap = Maps.newHashMap();
            // ??????sellerId
            List<String> sellerIdList = shopTransportEntityList.stream().map(ShopTransportEntity::getSellerId).collect(Collectors.toList());
            // ??????????????????
            List<ShopSellerEntity> shopSellerEntityList = shopSellerMapper.selectList(Wrappers.<ShopSellerEntity>lambdaQuery().in(ShopSellerEntity::getId, sellerIdList));
            if (CollectionUtils.isNotEmpty(shopSellerEntityList)) {
                // ????????????
                sellerMap.putAll(shopSellerEntityList.stream().collect(Collectors.toMap(ShopSellerEntity::getId, ShopSellerEntity::getSellerName, (k1, k2) -> k1)));
            }
            // ????????????
            shopTransportEntityList.forEach(shopTransportEntity -> shopTransportEntity.setSellerName(sellerMap.getOrDefault(shopTransportEntity.getSellerId(), DelimiterConstants.EMPTY_STR)));
        }
        return iPage;
    }


    @Override
    public DataResult updateShopTransportEntityById(ShopTransportEntity shopTransportEntity) {
        // ?????????????????????????????????
        if (Objects.nonNull(shopTransportEntity.getIsDefault()) && NumberConstants.ONE_I.equals(shopTransportEntity.getIsDefault())) {
            ShopTransportEntity result = shopTransportMapper.selectById(shopTransportEntity.getId());
            if (Objects.nonNull(result) && StringUtils.isNotBlank(result.getSellerId())) {
                shopTransportMapper.update(new ShopTransportEntity().setIsDefault(NumberConstants.ZERO_I), Wrappers.<ShopTransportEntity>lambdaQuery().eq(ShopTransportEntity::getSellerId, result.getSellerId()));
            }
        }
        // ????????????
        if(CollectionUtils.isNotEmpty(shopTransportEntity.getShopTransportExtendEntityList())){
            // ???????????????
            shopTransportExtendMapper.delete(Wrappers.<ShopTransportExtendEntity>lambdaQuery().eq(ShopTransportExtendEntity::getTransportId, shopTransportEntity.getId()));
            // ???????????????
            shopTransportEntity.getShopTransportExtendEntityList().forEach(shopTransportExtendEntity -> shopTransportExtendMapper.insert(shopTransportExtendEntity.setTransportId(shopTransportEntity.getId())));
        }
        return DataResult.success(shopTransportMapper.updateById(shopTransportEntity));
    }

    @Override
    public DataResult saveShopTransportEntity(ShopTransportEntity shopTransportEntity) {
        shopTransportMapper.insert(shopTransportEntity);
        if(CollectionUtils.isNotEmpty(shopTransportEntity.getShopTransportExtendEntityList())){
            shopTransportEntity.getShopTransportExtendEntityList().forEach(shopTransportExtendEntity -> shopTransportExtendMapper.insert(shopTransportExtendEntity.setTransportId(shopTransportEntity.getId())));
        }
        return DataResult.success();
    }

    @Override
    public DataResult removeShopTransportEntityByIds(List<String> ids) {
        shopTransportMapper.deleteBatchIds(ids);
        shopTransportExtendMapper.delete(Wrappers.<ShopTransportExtendEntity>lambdaQuery().in(ShopTransportExtendEntity::getTransportId, ids));
        return DataResult.success();
    }
}
