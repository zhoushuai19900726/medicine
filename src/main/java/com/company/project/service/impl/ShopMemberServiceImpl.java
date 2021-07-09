package com.company.project.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.company.project.common.exception.code.BusinessResponseCode;
import com.company.project.common.utils.CommonUtils;
import com.company.project.common.utils.DataResult;
import com.company.project.common.utils.NumberConstants;
import com.company.project.common.utils.PasswordUtils;
import com.company.project.entity.ShopMemberGradeEntity;
import com.company.project.mapper.ShopMemberGradeMapper;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.company.project.mapper.ShopMemberMapper;
import com.company.project.entity.ShopMemberEntity;
import com.company.project.service.ShopMemberService;

import javax.annotation.Resource;
import java.util.Objects;


@Service("shopMemberService")
public class ShopMemberServiceImpl extends ServiceImpl<ShopMemberMapper, ShopMemberEntity> implements ShopMemberService {

    @Resource
    private ShopMemberMapper shopMemberMapper;

    @Resource
    private ShopMemberGradeMapper shopMemberGradeMapper;

    @Override
    public ShopMemberEntity findOneByMemberName(ShopMemberEntity shopMemberEntity) {
        return shopMemberMapper.findOneByMemberName(shopMemberEntity);
    }

    @Override
    public DataResult saveShopMemberEntity(ShopMemberEntity shopMemberEntity) {
        // TODO 账号校验

        // 密码加密
        shopMemberEntity.setMemberPasswd(PasswordUtils.encode(shopMemberEntity.getMemberPasswd(), PasswordUtils.getSalt()));
        // 默认会员等级
        ShopMemberGradeEntity shopMemberGradeEntity = shopMemberGradeMapper.selectOne(Wrappers.<ShopMemberGradeEntity>lambdaQuery().eq(ShopMemberGradeEntity::getIsDefault, NumberConstants.ONE_I));
        if (Objects.nonNull(shopMemberGradeEntity)) {
            shopMemberEntity.setMemberGradeId(shopMemberGradeEntity.getId());
            shopMemberEntity.setMemberGradeName(shopMemberGradeEntity.getName());
        }
        // 生成邀请码－8位字符串加数字
        int i = NumberConstants.ZERO;
        while (true) {
            String invitationCode = CommonUtils.generateShortUUID();
            // 查询当前生成邀请码是否已存在使用
            if (CollectionUtils.isEmpty(memberDao.memberInvitationCode(invitationCode))) {
                member.setMemberInvitationCode(invitationCode);
                break;
            }
            i++;
        }






        return DataResult.success(shopMemberEntity);
    }
}
