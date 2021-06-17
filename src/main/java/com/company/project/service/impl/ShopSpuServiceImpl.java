package com.company.project.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.company.project.mapper.ShopSpuMapper;
import com.company.project.entity.ShopSpuEntity;
import com.company.project.service.ShopSpuService;


@Service("shopSpuService")
public class ShopSpuServiceImpl extends ServiceImpl<ShopSpuMapper, ShopSpuEntity> implements ShopSpuService {


}