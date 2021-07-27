package com.company.project.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.company.project.mapper.ShopArticleMapper;
import com.company.project.entity.ShopArticleEntity;
import com.company.project.service.ShopArticleService;


@Service("shopArticleService")
public class ShopArticleServiceImpl extends ServiceImpl<ShopArticleMapper, ShopArticleEntity> implements ShopArticleService {


}