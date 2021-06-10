package com.company.project.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.company.project.mapper.ShopTemplateMapper;
import com.company.project.entity.ShopTemplateEntity;
import com.company.project.service.ShopTemplateService;


@Service("shopTemplateService")
public class ShopTemplateServiceImpl extends ServiceImpl<ShopTemplateMapper, ShopTemplateEntity> implements ShopTemplateService {


}