package com.company.project.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.company.project.mapper.ShopSkuMapper;
import com.company.project.entity.ShopSkuEntity;
import com.company.project.service.ShopSkuService;


@Service("shopSkuService")
public class ShopSkuServiceImpl extends ServiceImpl<ShopSkuMapper, ShopSkuEntity> implements ShopSkuService {


}