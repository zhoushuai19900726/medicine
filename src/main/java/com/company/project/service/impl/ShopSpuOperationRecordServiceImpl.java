package com.company.project.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.company.project.mapper.ShopSpuOperationRecordMapper;
import com.company.project.entity.ShopSpuOperationRecordEntity;
import com.company.project.service.ShopSpuOperationRecordService;


@Service("shopSpuOperationRecordService")
public class ShopSpuOperationRecordServiceImpl extends ServiceImpl<ShopSpuOperationRecordMapper, ShopSpuOperationRecordEntity> implements ShopSpuOperationRecordService {


}