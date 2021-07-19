package com.company.project.service.impl;

import com.company.project.common.enums.OrderStatusEnum;
import com.company.project.common.utils.DataResult;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.company.project.mapper.ShopOrderMapper;
import com.company.project.entity.ShopOrderEntity;
import com.company.project.service.ShopOrderService;

import javax.annotation.Resource;
import java.util.Date;


@Service("shopOrderService")
public class ShopOrderServiceImpl extends ServiceImpl<ShopOrderMapper, ShopOrderEntity> implements ShopOrderService {

    @Resource
    private ShopOrderMapper shopOrderMapper;

    @Override
    public DataResult updateShopOrderEntityById(ShopOrderEntity shopOrder) {
        // 订单状态
        if(OrderStatusEnum.CLOSED.getType().equals(shopOrder.getOrderStatus())){
            shopOrder.setCloseTime(new Date());
        }
        return DataResult.success(shopOrderMapper.updateById(shopOrder));
    }
}
