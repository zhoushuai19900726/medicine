package com.company.project.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.company.project.common.utils.DataResult;
import com.company.project.entity.ShopSpuEntity;

import java.util.List;

/**
 * 商品SPU
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-06-17 10:25:25
 */
public interface ShopSpuService extends IService<ShopSpuEntity> {

    ShopSpuEntity getShopSpuEntityByUnique(ShopSpuEntity shopSpuEntity);

    ShopSpuEntity getShopSpuEntityById(String id);

    DataResult saveShopSpuEntity(ShopSpuEntity shopSpuEntity);

    DataResult updateShopSpuEntityById(ShopSpuEntity shopSpuEntity);

    DataResult removeShopSpuEntityByIds(List<String> ids);

    DataResult absolutelyRemoveShopSpuEntityByIds(List<String> ids);

    IPage<ShopSpuEntity> listByPage(Page<ShopSpuEntity> page, LambdaQueryWrapper<ShopSpuEntity> wrapper);

    IPage<ShopSpuEntity> recycleBinListByPage(Page<ShopSpuEntity> page, LambdaQueryWrapper<ShopSpuEntity> wrapper);

    String getUniqueSnBySeller(String sellerId);

    ShopSpuEntity copyGoods(ShopSpuEntity shopSpu);

    ShopSpuEntity tansferGoods(ShopSpuEntity shopSpu);

    List<ShopSpuEntity> listByIdList(List<String> spuIdList);

}

