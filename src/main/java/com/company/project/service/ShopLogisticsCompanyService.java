package com.company.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.company.project.common.utils.DataResult;
import com.company.project.entity.ShopLogisticsCompanyEntity;

import java.util.List;

/**
 * 快递公司
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-07-19 16:10:29
 */
public interface ShopLogisticsCompanyService extends IService<ShopLogisticsCompanyEntity> {

    DataResult saveShopLogisticsCompanyEntity(ShopLogisticsCompanyEntity shopLogisticsCompany);

    DataResult removeShopLogisticsCompanyEntityByIds(List<String> ids);

    DataResult updateShopLogisticsCompanyEntityById(ShopLogisticsCompanyEntity shopLogisticsCompany);

}

