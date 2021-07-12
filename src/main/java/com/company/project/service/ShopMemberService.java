package com.company.project.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.company.project.common.utils.DataResult;
import com.company.project.entity.ShopMemberEntity;

import java.util.List;

/**
 * 会员
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-07-08 13:59:46
 */
public interface ShopMemberService extends IService<ShopMemberEntity> {

    ShopMemberEntity findOneByMemberId(String memberId);

    ShopMemberEntity findOneByMemberName(ShopMemberEntity shopMemberEntity);

    ShopMemberEntity findOneByInvitationCode(ShopMemberEntity shopMemberEntity);

    DataResult saveShopMemberEntity(ShopMemberEntity shopMember);

    IPage<ShopMemberEntity> listByPage(Page<ShopMemberEntity> page, LambdaQueryWrapper<ShopMemberEntity> wrapper);

    DataResult updateShopMemberEntityById(ShopMemberEntity shopMember);

    DataResult absolutelyDelete(List<String> memberIdList);

    DataResult removeByMemberIds(List<String> memberIdList);
}

