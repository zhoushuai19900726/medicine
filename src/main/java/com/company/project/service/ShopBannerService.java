package com.company.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.company.project.common.utils.DataResult;
import com.company.project.entity.ShopBannerEntity;

import java.util.List;

/**
 * Banner导航
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-07-16 09:10:18
 */
public interface ShopBannerService extends IService<ShopBannerEntity> {

    DataResult saveShopBannerEntity(ShopBannerEntity shopBanner);

    DataResult removeShopBannerEntityByIds(List<String> ids);

    DataResult updateShopBannerEntityById(ShopBannerEntity shopBanner);

}

