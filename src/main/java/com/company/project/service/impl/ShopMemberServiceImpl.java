package com.company.project.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.company.project.common.exception.code.BusinessResponseCode;
import com.company.project.common.utils.*;
import com.company.project.entity.*;
import com.company.project.mapper.*;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.company.project.service.ShopMemberService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Objects;

@Transactional
@Service("shopMemberService")
public class ShopMemberServiceImpl extends ServiceImpl<ShopMemberMapper, ShopMemberEntity> implements ShopMemberService {

    @Resource
    private ShopMemberMapper shopMemberMapper;

    @Resource
    private ShopRecommendationRelationshipMapper shopRecommendationRelationshipMapper;

    @Resource
    private ShopMemberGradeMapper shopMemberGradeMapper;

    @Resource
    private ShopMemberGrowthValueMapper shopMemberGrowthValueMapper;

    @Resource
    private ShopMemberWalletMapper shopMemberWalletMapper;

    @Override
    public ShopMemberEntity findOneByMemberName(ShopMemberEntity shopMemberEntity) {
        return shopMemberMapper.findOneByMemberName(shopMemberEntity);
    }

    @Override
    public DataResult saveShopMemberEntity(ShopMemberEntity shopMemberEntity) {
        // 账号校验
        ShopMemberEntity accountVerification = shopMemberMapper.findOneByMemberName(shopMemberEntity);
        if (Objects.nonNull(accountVerification)) {
            return DataResult.fail(BusinessResponseCode.ACCOUNT_REPEAT.getMsg());
        }
        // 默认会员等级
        ShopMemberGradeEntity shopMemberGradeEntity = shopMemberGradeMapper.selectOne(Wrappers.<ShopMemberGradeEntity>lambdaQuery().eq(ShopMemberGradeEntity::getIsDefault, NumberConstants.ONE_I));
        if (Objects.nonNull(shopMemberGradeEntity)) {
            shopMemberEntity.setMemberGradeId(shopMemberGradeEntity.getId());
            shopMemberEntity.setMemberGradeName(shopMemberGradeEntity.getName());
            shopMemberEntity.setGradeTime(new Date());
        }
        // 生成邀请码－8位字符串加数字(查询当前生成邀请码是否已存在使用)
        do {
            shopMemberEntity.setMemberInvitationCode(CommonUtils.generateShortUUID());
        } while (Objects.nonNull(shopMemberMapper.findOneByInvitationCode(shopMemberEntity)));
        // 密码加密
        if (StringUtils.isNotBlank(shopMemberEntity.getMemberPasswd())) {
            shopMemberEntity.setMemberPasswd(PasswordUtils.encode(shopMemberEntity.getMemberPasswd(), PasswordUtils.getSalt()));
        } else {
            shopMemberEntity.setMemberPasswd(PasswordUtils.encode(DelimiterConstants.INIT_PASSWORD, PasswordUtils.getSalt()));
        }
        // 默认头像
        if (StringUtils.isBlank(shopMemberEntity.getMemberAvatar())) {
            shopMemberEntity.setMemberAvatar(DelimiterConstants.DEFAULT_AVATAR);
        }
        // 推荐上级
        if (StringUtils.isBlank(shopMemberEntity.getMemberInvitationCodeFrom())) {
            shopMemberEntity.setMemberFrom(DelimiterConstants.SYS_ADMIN_ID);
            // 保存顶点一直存在
            if (Objects.isNull(shopMemberMapper.selectById(DelimiterConstants.SYS_ADMIN_ID))) {
                shopMemberMapper.insert(DelimiterConstants.INIT_MEMBER());
            }
        }
        ShopMemberEntity references = shopMemberMapper.selectById(shopMemberEntity.getMemberFrom());
        if (Objects.nonNull(references)) {
            shopMemberEntity.setMemberFrom(references.getMemberId());
            shopMemberEntity.setMemberInvitationCodeFrom(references.getMemberInvitationCode());
        } else {
            return DataResult.fail(BusinessResponseCode.INVALID_RECOMMENDATION_CODE.getMsg());
        }
        // 初始化版本号
        shopMemberEntity.setMemberVersion(NumberConstants.ONE_L);
        // 保存会员信息
        shopMemberMapper.insert(shopMemberEntity);
        // 推荐关系
        ShopRecommendationRelationshipEntity shopRecommendationRelationshipEntity = new ShopRecommendationRelationshipEntity();
        shopRecommendationRelationshipEntity.setMemberId(shopMemberEntity.getMemberId());
        shopRecommendationRelationshipEntity.setRecommendId(references.getMemberId());
        // 上级推荐关系
        ShopRecommendationRelationshipEntity referencesRelationship = shopRecommendationRelationshipMapper.selectById(references.getMemberId());
        if (Objects.nonNull(referencesRelationship)) {
            shopRecommendationRelationshipEntity.setRecommendLevel(referencesRelationship.getRecommendLevel() + NumberConstants.ONE_I);
        } else {
            referencesRelationship = new ShopRecommendationRelationshipEntity();
            referencesRelationship.setMemberId(references.getMemberId());
            referencesRelationship.setRecommendLevel(NumberConstants.ONE_I);
            shopRecommendationRelationshipMapper.insert(referencesRelationship);
            shopRecommendationRelationshipEntity.setRecommendLevel(NumberConstants.TWO_I);
        }
        // 保存推荐关系
        shopRecommendationRelationshipMapper.insert(shopRecommendationRelationshipEntity);
        // 保存成长值
        shopMemberGrowthValueMapper.insert(new ShopMemberGrowthValueEntity(shopMemberEntity.getMemberId()));
        // 保存会员钱包
        shopMemberWalletMapper.insert(new ShopMemberWalletEntity(shopMemberEntity.getMemberId()));
        return DataResult.success(shopMemberEntity);
    }
}
