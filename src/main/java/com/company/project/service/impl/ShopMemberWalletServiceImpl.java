package com.company.project.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.company.project.mapper.ShopMemberWalletMapper;
import com.company.project.entity.ShopMemberWalletEntity;
import com.company.project.service.ShopMemberWalletService;


@Service("shopMemberWalletService")
public class ShopMemberWalletServiceImpl extends ServiceImpl<ShopMemberWalletMapper, ShopMemberWalletEntity> implements ShopMemberWalletService {


}