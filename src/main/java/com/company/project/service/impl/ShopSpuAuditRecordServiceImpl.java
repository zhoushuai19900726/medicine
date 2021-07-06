package com.company.project.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.company.project.mapper.ShopSpuAuditRecordMapper;
import com.company.project.entity.ShopSpuAuditRecordEntity;
import com.company.project.service.ShopSpuAuditRecordService;


@Service("shopSpuAuditRecordService")
public class ShopSpuAuditRecordServiceImpl extends ServiceImpl<ShopSpuAuditRecordMapper, ShopSpuAuditRecordEntity> implements ShopSpuAuditRecordService {


}