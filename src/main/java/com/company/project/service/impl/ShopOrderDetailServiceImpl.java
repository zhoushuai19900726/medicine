package com.company.project.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.company.project.mapper.ShopOrderDetailMapper;
import com.company.project.entity.ShopOrderDetailEntity;
import com.company.project.service.ShopOrderDetailService;


@Service("shopOrderDetailService")
public class ShopOrderDetailServiceImpl extends ServiceImpl<ShopOrderDetailMapper, ShopOrderDetailEntity> implements ShopOrderDetailService {


}