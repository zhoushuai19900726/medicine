package com.company.project.service.impl;

import com.company.project.common.utils.DataResult;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.company.project.mapper.ShopMemberMapper;
import com.company.project.entity.ShopMemberEntity;
import com.company.project.service.ShopMemberService;

import javax.annotation.Resource;


@Service("shopMemberService")
public class ShopMemberServiceImpl extends ServiceImpl<ShopMemberMapper, ShopMemberEntity> implements ShopMemberService {

    @Resource
    private ShopMemberMapper shopMemberMapper;

    @Override
    public ShopMemberEntity findOneByUnique(ShopMemberEntity shopMemberEntity) {
        return shopMemberMapper.findOneByUnique(shopMemberEntity);
    }

    @Override
    public DataResult saveShopMemberEntity(ShopMemberEntity shopMember) {




        return DataResult.success(shopMember);
    }
}
