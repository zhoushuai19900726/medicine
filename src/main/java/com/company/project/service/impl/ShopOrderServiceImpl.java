package com.company.project.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.company.project.mapper.ShopOrderMapper;
import com.company.project.entity.ShopOrderEntity;
import com.company.project.service.ShopOrderService;


@Service("shopOrderService")
public class ShopOrderServiceImpl extends ServiceImpl<ShopOrderMapper, ShopOrderEntity> implements ShopOrderService {


}