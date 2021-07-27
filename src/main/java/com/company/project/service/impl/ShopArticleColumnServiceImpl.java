package com.company.project.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.company.project.mapper.ShopArticleColumnMapper;
import com.company.project.entity.ShopArticleColumnEntity;
import com.company.project.service.ShopArticleColumnService;


@Service("shopArticleColumnService")
public class ShopArticleColumnServiceImpl extends ServiceImpl<ShopArticleColumnMapper, ShopArticleColumnEntity> implements ShopArticleColumnService {


}