package com.company.project.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.company.project.mapper.ShopLabelGoodsMapper;
import com.company.project.entity.ShopLabelGoodsEntity;
import com.company.project.service.ShopLabelGoodsService;


@Service("shopLabelGoodsService")
public class ShopLabelGoodsServiceImpl extends ServiceImpl<ShopLabelGoodsMapper, ShopLabelGoodsEntity> implements ShopLabelGoodsService {


}