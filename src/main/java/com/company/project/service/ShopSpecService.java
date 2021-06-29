package com.company.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.company.project.common.utils.DataResult;
import com.company.project.entity.ShopSpecEntity;

import java.util.List;

/**
 *
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-06-11 10:26:37
 */
public interface ShopSpecService extends IService<ShopSpecEntity> {

    DataResult saveShopSpecEntity(ShopSpecEntity shopSpecEntity);

    DataResult removeShopSpecEntityByIds(List<String> ids);

    DataResult updateShopSpecEntityById(ShopSpecEntity shopSpecEntity);
}

