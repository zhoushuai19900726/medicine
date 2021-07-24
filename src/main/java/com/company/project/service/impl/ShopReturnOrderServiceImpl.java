package com.company.project.service.impl;

import com.company.project.common.utils.DataResult;
import com.company.project.mapper.ShopReturnOrderDetailMapper;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.company.project.mapper.ShopReturnOrderMapper;
import com.company.project.entity.ShopReturnOrderEntity;
import com.company.project.service.ShopReturnOrderService;

import javax.annotation.Resource;


@Service("shopReturnOrderService")
public class ShopReturnOrderServiceImpl extends ServiceImpl<ShopReturnOrderMapper, ShopReturnOrderEntity> implements ShopReturnOrderService {

    @Resource
    private ShopReturnOrderMapper shopReturnOrderMapper;

    @Resource
    private ShopReturnOrderDetailMapper shopReturnOrderDetailMapper;


    @Override
    public DataResult saveShopReturnOrderEntity(ShopReturnOrderEntity shopReturnOrder) {




        return DataResult.success();
    }
}
