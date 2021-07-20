package com.company.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.company.project.common.utils.DataResult;
import com.company.project.entity.ShopReturnCauseEntity;

import java.util.List;

/**
 * 退货原因
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-07-20 14:14:36
 */
public interface ShopReturnCauseService extends IService<ShopReturnCauseEntity> {

    DataResult saveShopReturnCauseEntity(ShopReturnCauseEntity shopReturnCause);

    DataResult removeShopReturnCauseEntityByIds(List<String> ids);

    DataResult updateShopReturnCauseEntityById(ShopReturnCauseEntity shopReturnCause);

}

