package com.company.project.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.company.project.mapper.ShopOrderConfigMapper;
import com.company.project.entity.ShopOrderConfigEntity;
import com.company.project.service.ShopOrderConfigService;


@Service("shopOrderConfigService")
public class ShopOrderConfigServiceImpl extends ServiceImpl<ShopOrderConfigMapper, ShopOrderConfigEntity> implements ShopOrderConfigService {


}