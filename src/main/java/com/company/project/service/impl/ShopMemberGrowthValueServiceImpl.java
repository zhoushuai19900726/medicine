package com.company.project.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.company.project.mapper.ShopMemberGrowthValueMapper;
import com.company.project.entity.ShopMemberGrowthValueEntity;
import com.company.project.service.ShopMemberGrowthValueService;


@Service("shopMemberGrowthValueService")
public class ShopMemberGrowthValueServiceImpl extends ServiceImpl<ShopMemberGrowthValueMapper, ShopMemberGrowthValueEntity> implements ShopMemberGrowthValueService {


}