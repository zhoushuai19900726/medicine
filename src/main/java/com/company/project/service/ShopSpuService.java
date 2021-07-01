package com.company.project.service;

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

    DataResult saveShopSpuEntity(ShopSpuEntity shopSpuEntity);

    DataResult updateShopSpuEntityById(ShopSpuEntity shopSpuEntity);

    DataResult removeShopSpuEntityByIds(List<String> ids);
}

