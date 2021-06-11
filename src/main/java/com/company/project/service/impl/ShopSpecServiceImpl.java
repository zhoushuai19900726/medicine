package com.company.project.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.company.project.mapper.ShopSpecMapper;
import com.company.project.entity.ShopSpecEntity;
import com.company.project.service.ShopSpecService;


@Service("shopSpecService")
public class ShopSpecServiceImpl extends ServiceImpl<ShopSpecMapper, ShopSpecEntity> implements ShopSpecService {


}