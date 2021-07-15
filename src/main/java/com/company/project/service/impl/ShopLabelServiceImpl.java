package com.company.project.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.company.project.mapper.ShopLabelMapper;
import com.company.project.entity.ShopLabelEntity;
import com.company.project.service.ShopLabelService;


@Service("shopLabelService")
public class ShopLabelServiceImpl extends ServiceImpl<ShopLabelMapper, ShopLabelEntity> implements ShopLabelService {


}