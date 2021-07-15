package com.company.project.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.company.project.mapper.ShopAdvertisementMapper;
import com.company.project.entity.ShopAdvertisementEntity;
import com.company.project.service.ShopAdvertisementService;


@Service("shopAdvertisementService")
public class ShopAdvertisementServiceImpl extends ServiceImpl<ShopAdvertisementMapper, ShopAdvertisementEntity> implements ShopAdvertisementService {


}