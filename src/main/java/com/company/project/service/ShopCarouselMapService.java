package com.company.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.company.project.common.utils.DataResult;
import com.company.project.entity.ShopCarouselMapEntity;

import java.util.List;

/**
 * 轮播图
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-07-16 14:53:02
 */
public interface ShopCarouselMapService extends IService<ShopCarouselMapEntity> {

    DataResult saveShopCarouselMapEntity(ShopCarouselMapEntity shopCarouselMap);

    DataResult removeShopCarouselMapEntityByIds(List<String> ids);

    DataResult updateShopCarouselMapEntityById(ShopCarouselMapEntity shopCarouselMap);

}

