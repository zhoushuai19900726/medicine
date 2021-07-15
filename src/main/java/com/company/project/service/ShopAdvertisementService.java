package com.company.project.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.company.project.common.utils.DataResult;
import com.company.project.entity.ShopAdvertisementEntity;

import java.util.List;

/**
 * 广告
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-07-15 09:28:29
 */
public interface ShopAdvertisementService extends IService<ShopAdvertisementEntity> {

    IPage<ShopAdvertisementEntity> listByPage(Page<ShopAdvertisementEntity> page, LambdaQueryWrapper<ShopAdvertisementEntity> wrapper);

    DataResult saveShopAdvertisementEntity(ShopAdvertisementEntity shopAdvertisement);

    DataResult removeShopAdvertisementEntityByIds(List<String> ids);

    DataResult updateShopAdvertisementEntityById(ShopAdvertisementEntity shopAdvertisement);

}

