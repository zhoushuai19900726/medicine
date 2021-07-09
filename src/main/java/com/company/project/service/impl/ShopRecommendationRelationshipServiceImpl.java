package com.company.project.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.company.project.mapper.ShopRecommendationRelationshipMapper;
import com.company.project.entity.ShopRecommendationRelationshipEntity;
import com.company.project.service.ShopRecommendationRelationshipService;


@Service("shopRecommendationRelationshipService")
public class ShopRecommendationRelationshipServiceImpl extends ServiceImpl<ShopRecommendationRelationshipMapper, ShopRecommendationRelationshipEntity> implements ShopRecommendationRelationshipService {


}