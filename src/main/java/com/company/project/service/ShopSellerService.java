package com.company.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.company.project.common.utils.DataResult;
import com.company.project.entity.ShopSellerEntity;

/**
 *
 *
 * @author wenbin
 * @version V1.0
 * @date 2020年3月18日
 */
public interface ShopSellerService extends IService<ShopSellerEntity> {

    DataResult saveShopSellerEntity(ShopSellerEntity shopSeller);

    DataResult updateShopSellerEntityById(ShopSellerEntity shopSeller);

}

