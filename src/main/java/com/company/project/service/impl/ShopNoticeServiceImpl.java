package com.company.project.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.company.project.mapper.ShopNoticeMapper;
import com.company.project.entity.ShopNoticeEntity;
import com.company.project.service.ShopNoticeService;


@Service("shopNoticeService")
public class ShopNoticeServiceImpl extends ServiceImpl<ShopNoticeMapper, ShopNoticeEntity> implements ShopNoticeService {


}