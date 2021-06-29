package com.company.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.company.project.common.utils.DataResult;
import com.company.project.entity.ShopParaEntity;

import java.util.List;

/**
 * 商品参数
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-06-15 11:26:57
 */
public interface ShopParaService extends IService<ShopParaEntity> {

    DataResult saveShopParaEntity(ShopParaEntity shopParaEntity);

    DataResult removeShopParaEntityByIds(List<String> ids);

    DataResult updateShopParaEntityById(ShopParaEntity shopParaEntity);
}

