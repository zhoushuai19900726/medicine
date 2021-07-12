package com.company.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.project.common.exception.code.BusinessResponseCode;
import com.company.project.common.utils.DataResult;
import com.company.project.entity.ShopMemberEntity;
import com.company.project.entity.ShopMemberGrowthValueEntity;
import com.company.project.entity.ShopMemberWalletEntity;
import com.company.project.mapper.*;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.company.project.entity.ShopMemberBlacklistEntity;
import com.company.project.service.ShopMemberBlacklistService;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


@Service("shopMemberBlacklistService")
public class ShopMemberBlacklistServiceImpl extends ServiceImpl<ShopMemberBlacklistMapper, ShopMemberBlacklistEntity> implements ShopMemberBlacklistService {

    @Resource
    private ShopMemberMapper shopMemberMapper;

    @Resource
    private ShopMemberBlacklistMapper shopMemberBlacklistMapper;

    @Resource
    private ShopMemberGrowthValueMapper shopMemberGrowthValueMapper;

    @Resource
    private ShopMemberWalletMapper shopMemberWalletMapper;

    @Override
    public IPage<ShopMemberBlacklistEntity> listByPage(Page<ShopMemberBlacklistEntity> page, LambdaQueryWrapper<ShopMemberBlacklistEntity> wrapper) {
        return encapsulatingFieldName(shopMemberBlacklistMapper.selectPage(page, wrapper));
    }

    @Override
    public DataResult saveShopMemberBlacklistEntity(ShopMemberBlacklistEntity shopMemberBlacklist) {
        ShopMemberEntity shopMemberEntity = shopMemberMapper.findOneByMemberName(new ShopMemberEntity(shopMemberBlacklist.getMemberName()));
        if(Objects.isNull(shopMemberEntity)){
            DataResult.fail(BusinessResponseCode.INVALID_ACCOUNT.getMsg());
        }
        BeanUtils.copyProperties(shopMemberEntity, shopMemberBlacklist);
        return DataResult.success(shopMemberBlacklistMapper.insert(shopMemberBlacklist));
    }

    private IPage<ShopMemberBlacklistEntity> encapsulatingFieldName(IPage<ShopMemberBlacklistEntity> iPage) {
        // 会员集合
        List<ShopMemberBlacklistEntity> shopMemberBlacklistEntityList = iPage.getRecords();
        // 封装名称
        if (CollectionUtils.isNotEmpty(shopMemberBlacklistEntityList)) {
            // 查询结果封装
            Map<String, BigDecimal> walletMap = Maps.newHashMap();
            Map<String, BigDecimal> growthMap = Maps.newHashMap();
            // 提取memberId
            List<String> memberIdList = shopMemberBlacklistEntityList.stream().map(ShopMemberBlacklistEntity::getMemberId).collect(Collectors.toList());
            // 查询钱包集合
            List<ShopMemberWalletEntity> shopMemberWalletEntityList = shopMemberWalletMapper.selectList(Wrappers.<ShopMemberWalletEntity>lambdaQuery().in(ShopMemberWalletEntity::getMemberId, memberIdList));
            if (CollectionUtils.isNotEmpty(shopMemberWalletEntityList)) {
                // 封装钱包余额
                walletMap.putAll(shopMemberWalletEntityList.stream().collect(Collectors.toMap(ShopMemberWalletEntity::getMemberId, ShopMemberWalletEntity::getBalance, (k1, k2) -> k1)));
            }
            // 查询成长值集合
            List<ShopMemberGrowthValueEntity> shopMemberGrowthValueEntityList = shopMemberGrowthValueMapper.selectList(Wrappers.<ShopMemberGrowthValueEntity>lambdaQuery().in(ShopMemberGrowthValueEntity::getMemberId, memberIdList));
            if (CollectionUtils.isNotEmpty(shopMemberGrowthValueEntityList)) {
                // 封装成长值
                growthMap.putAll(shopMemberGrowthValueEntityList.stream().collect(Collectors.toMap(ShopMemberGrowthValueEntity::getMemberId, ShopMemberGrowthValueEntity::getGrowthValue, (k1, k2) -> k1)));
            }
            // 执行封装
            shopMemberBlacklistEntityList.forEach(shopMemberBlacklistEntity -> {
                shopMemberBlacklistEntity.setWalletBalance(walletMap.getOrDefault(shopMemberBlacklistEntity.getMemberId(), BigDecimal.ZERO));
                shopMemberBlacklistEntity.setGrowthValue(growthMap.getOrDefault(shopMemberBlacklistEntity.getMemberId(), BigDecimal.ZERO));
            });
        }
        return iPage;
    }

}
