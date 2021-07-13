package com.company.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.company.project.entity.ShopRecommendationRelationshipEntity;
import com.company.project.vo.resp.RecommendRespVO;

import java.util.List;
import java.util.Map;

/**
 * 会员推荐关系表
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-07-09 15:40:52
 */
public interface ShopRecommendationRelationshipService extends IService<ShopRecommendationRelationshipEntity> {

    RecommendRespVO recommendationRelationship(String memberName, Integer direction);

}

