package com.company.project.service.impl;

import com.company.project.common.utils.DelimiterConstants;
import com.company.project.common.utils.NumberConstants;
import com.company.project.entity.ShopMemberEntity;
import com.company.project.mapper.ShopMemberMapper;
import com.company.project.vo.resp.RecommendRespVO;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.company.project.mapper.ShopRecommendationRelationshipMapper;
import com.company.project.entity.ShopRecommendationRelationshipEntity;
import com.company.project.service.ShopRecommendationRelationshipService;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;


@Service("shopRecommendationRelationshipService")
public class ShopRecommendationRelationshipServiceImpl extends ServiceImpl<ShopRecommendationRelationshipMapper, ShopRecommendationRelationshipEntity> implements ShopRecommendationRelationshipService {

    @Resource
    private ShopMemberMapper shopMemberMapper;

    @Resource
    private ShopRecommendationRelationshipMapper shopRecommendationRelationshipMapper;

    @Override
    public RecommendRespVO recommendationRelationship(String memberName, Integer direction) {
        // 返回结果
        AtomicReference<RecommendRespVO> result = new AtomicReference<>(new RecommendRespVO());
        // 根据账号查询会员信息
        ShopMemberEntity shopMemberEntity = shopMemberMapper.findOneByMemberName(new ShopMemberEntity(memberName));
        if (Objects.nonNull(shopMemberEntity)) {
            // 垂直关系(direction) 1向上  2向下
            List<ShopRecommendationRelationshipEntity> shopRecommendationRelationshipEntityList = Lists.newArrayList();
            switch (direction) {
                case NumberConstants.ONE:
                    // 向上递归 - 查询推荐关系
                    shopRecommendationRelationshipEntityList.addAll(shopRecommendationRelationshipMapper.recursionQueryDistributorRelationByMemberId(shopMemberEntity.getMemberId()));
                    break;
                case NumberConstants.TWO:
                    // 向下递归 - 查询推荐关系


                    break;
            }
            if (CollectionUtils.isNotEmpty(shopRecommendationRelationshipEntityList)) {
                // 封装NAME
                List<String> memberIdList = shopRecommendationRelationshipEntityList.stream().map(ShopRecommendationRelationshipEntity::getMemberId).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(memberIdList)) {
                    List<ShopMemberEntity> shopMemberEntityList = shopMemberMapper.findListByMemberIdList(memberIdList);
                    if (CollectionUtils.isNotEmpty(shopMemberEntityList)) {
                        Map<String, String> shopMemberEntityMap = shopMemberEntityList.stream().collect(Collectors.toMap(ShopMemberEntity::getMemberId, ShopMemberEntity::getMemberName, (k1, k2) -> k1));
                        shopRecommendationRelationshipEntityList.forEach(shopRecommendationRelationshipEntity -> shopRecommendationRelationshipEntity.setMemberName(shopMemberEntityMap.getOrDefault(shopRecommendationRelationshipEntity.getMemberId(), DelimiterConstants.LINE)));
                    }
                }
                // DO -> VO
                List<RecommendRespVO> recommendRespVOList = shopRecommendationRelationshipEntityList.stream().map(shopRecommendationRelationshipEntity -> new RecommendRespVO(shopRecommendationRelationshipEntity.getMemberId(), shopRecommendationRelationshipEntity.getMemberName(), shopRecommendationRelationshipEntity.getRecommendId())).collect(Collectors.toList());
                // 根据父级分组
                Map<String, List<RecommendRespVO>> groupBy = recommendRespVOList.stream().filter(a -> StringUtils.isNotBlank(a.getParentId())).collect(Collectors.groupingBy(RecommendRespVO::getParentId));
                recommendRespVOList.forEach(recommendRespVO -> recommendRespVO.setChildren(groupBy.getOrDefault(recommendRespVO.getId(), Lists.newArrayList())));
                result.set(recommendRespVOList.get(NumberConstants.ZERO));
            }
        }
        return result.get();
    }
}
