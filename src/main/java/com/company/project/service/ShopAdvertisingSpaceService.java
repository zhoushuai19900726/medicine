package com.company.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.company.project.common.utils.DataResult;
import com.company.project.entity.ShopAdvertisingSpaceEntity;

import java.util.List;

/**
 * 广告位
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-07-15 09:28:30
 */
public interface ShopAdvertisingSpaceService extends IService<ShopAdvertisingSpaceEntity> {

    DataResult saveShopAdvertisingSpaceEntity(ShopAdvertisingSpaceEntity shopAdvertisingSpace);

    DataResult updateShopAdvertisingSpaceEntityById(ShopAdvertisingSpaceEntity shopAdvertisingSpace);

    DataResult removeShopAdvertisingSpaceEntityByIds(List<String> ids);

}

