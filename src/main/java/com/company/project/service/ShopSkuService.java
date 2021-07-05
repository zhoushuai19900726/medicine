package com.company.project.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.company.project.common.utils.DataResult;
import com.company.project.entity.ShopSkuEntity;

import java.util.List;

/**
 * 商品SKU
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-06-17 10:25:18
 */
public interface ShopSkuService extends IService<ShopSkuEntity> {

    List<ShopSkuEntity> listByCondition(LambdaQueryWrapper<ShopSkuEntity> wrapper);

    ShopSkuEntity getShopSpuEntityById(String id);

    Integer updateShopSpuEntityById(ShopSkuEntity shopSkuEntity);
}

