package com.company.project.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.company.project.mapper.ShopMemberWalletRecordMapper;
import com.company.project.entity.ShopMemberWalletRecordEntity;
import com.company.project.service.ShopMemberWalletRecordService;


@Service("shopMemberWalletRecordService")
public class ShopMemberWalletRecordServiceImpl extends ServiceImpl<ShopMemberWalletRecordMapper, ShopMemberWalletRecordEntity> implements ShopMemberWalletRecordService {


}