package com.company.project.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.company.project.mapper.ShopMemberMapper;
import com.company.project.entity.ShopMemberEntity;
import com.company.project.service.ShopMemberService;


@Service("shopMemberService")
public class ShopMemberServiceImpl extends ServiceImpl<ShopMemberMapper, ShopMemberEntity> implements ShopMemberService {


}