package com.company.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.company.project.common.utils.DataResult;
import com.company.project.entity.ShopReturnOrderEntity;

/**
 * 退款申请
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-07-24 09:52:58
 */
public interface ShopReturnOrderService extends IService<ShopReturnOrderEntity> {

    DataResult saveShopReturnOrderEntity(ShopReturnOrderEntity shopReturnOrder);

}

