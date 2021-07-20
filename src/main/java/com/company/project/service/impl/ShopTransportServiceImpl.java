package com.company.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.project.common.utils.DataResult;
import com.company.project.common.utils.DelimiterConstants;
import com.company.project.common.utils.NumberConstants;
import com.company.project.entity.ShopSellerEntity;
import com.company.project.mapper.ShopSellerMapper;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.company.project.mapper.ShopTransportMapper;
import com.company.project.entity.ShopTransportEntity;
import com.company.project.service.ShopTransportService;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


@Service("shopTransportService")
public class ShopTransportServiceImpl extends ServiceImpl<ShopTransportMapper, ShopTransportEntity> implements ShopTransportService {

    @Resource
    private ShopTransportMapper shopTransportMapper;

    @Resource
    private ShopSellerMapper shopSellerMapper;

    @Override
    public IPage<ShopTransportEntity> listByPage(Page<ShopTransportEntity> page, LambdaQueryWrapper<ShopTransportEntity> wrapper) {
        return encapsulatingFieldName(shopTransportMapper.selectPage(page, wrapper));
    }

    private IPage<ShopTransportEntity> encapsulatingFieldName(IPage<ShopTransportEntity> iPage) {
        // 模板集合
        List<ShopTransportEntity> shopTransportEntityList = iPage.getRecords();
        // 封装名称
        if (CollectionUtils.isNotEmpty(shopTransportEntityList)) {
            // 查询结果封装
            Map<String, String> sellerMap = Maps.newHashMap();
            // 提取sellerId
            List<String> sellerIdList = shopTransportEntityList.stream().map(ShopTransportEntity::getSellerId).collect(Collectors.toList());
            // 查询商家集合
            List<ShopSellerEntity> shopSellerEntityList = shopSellerMapper.selectList(Wrappers.<ShopSellerEntity>lambdaQuery().in(ShopSellerEntity::getId, sellerIdList));
            if (CollectionUtils.isNotEmpty(shopSellerEntityList)) {
                // 封装商家
                sellerMap.putAll(shopSellerEntityList.stream().collect(Collectors.toMap(ShopSellerEntity::getId, ShopSellerEntity::getSellerName, (k1, k2) -> k1)));
            }
            // 执行封装
            shopTransportEntityList.forEach(shopTransportEntity -> shopTransportEntity.setSellerName(sellerMap.getOrDefault(shopTransportEntity.getSellerId(), DelimiterConstants.EMPTY_STR)));
        }
        return iPage;
    }


    @Override
    public DataResult updateShopTransportEntityById(ShopTransportEntity shopTransport) {
        // 一个店铺只保留一个默认
        if (Objects.nonNull(shopTransport.getIsDefault()) && NumberConstants.ONE_I.equals(shopTransport.getIsDefault())) {
            ShopTransportEntity result = shopTransportMapper.selectById(shopTransport.getId());
            if (Objects.nonNull(result) && StringUtils.isNotBlank(result.getSellerId())) {
                shopTransportMapper.update(new ShopTransportEntity().setIsDefault(NumberConstants.ZERO_I), Wrappers.<ShopTransportEntity>lambdaQuery().eq(ShopTransportEntity::getSellerId, result.getSellerId()));
            }
        }
        return DataResult.success(shopTransportMapper.updateById(shopTransport));
    }
}
