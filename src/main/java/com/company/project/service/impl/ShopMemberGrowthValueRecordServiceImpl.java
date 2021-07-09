package com.company.project.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.company.project.mapper.ShopMemberGrowthValueRecordMapper;
import com.company.project.entity.ShopMemberGrowthValueRecordEntity;
import com.company.project.service.ShopMemberGrowthValueRecordService;


@Service("shopMemberGrowthValueRecordService")
public class ShopMemberGrowthValueRecordServiceImpl extends ServiceImpl<ShopMemberGrowthValueRecordMapper, ShopMemberGrowthValueRecordEntity> implements ShopMemberGrowthValueRecordService {


}