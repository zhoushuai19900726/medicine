package com.company.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.company.project.common.utils.DataResult;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.company.project.mapper.ShopSkuMapper;
import com.company.project.entity.ShopSkuEntity;
import com.company.project.service.ShopSkuService;

import javax.annotation.Resource;
import java.util.List;


@Service("shopSkuService")
public class ShopSkuServiceImpl extends ServiceImpl<ShopSkuMapper, ShopSkuEntity> implements ShopSkuService {

    @Resource
    private ShopSkuMapper shopSkuMapper;

    @Override
    public List<ShopSkuEntity> listByCondition(LambdaQueryWrapper<ShopSkuEntity> wrapper) {
        return shopSkuMapper.listByCondition(wrapper);
    }

    @Override
    public ShopSkuEntity getShopSpuEntityById(String id) {
        return shopSkuMapper.getShopSpuEntityById(id);
    }

    @Override
    public Integer updateShopSpuEntityById(ShopSkuEntity shopSkuEntity) {
        return shopSkuMapper.updateShopSpuEntityById(shopSkuEntity);
    }
}
