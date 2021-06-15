package com.company.project.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.company.project.mapper.ShopBrandMapper;
import com.company.project.entity.ShopBrandEntity;
import com.company.project.service.ShopBrandService;


@Service("shopBrandService")
public class ShopBrandServiceImpl extends ServiceImpl<ShopBrandMapper, ShopBrandEntity> implements ShopBrandService {


}