package com.company.project.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.company.project.mapper.ShopReturnOrderDetailMapper;
import com.company.project.entity.ShopReturnOrderDetailEntity;
import com.company.project.service.ShopReturnOrderDetailService;


@Service("shopReturnOrderDetailService")
public class ShopReturnOrderDetailServiceImpl extends ServiceImpl<ShopReturnOrderDetailMapper, ShopReturnOrderDetailEntity> implements ShopReturnOrderDetailService {


}