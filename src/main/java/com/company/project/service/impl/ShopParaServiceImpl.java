package com.company.project.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.company.project.entity.ShopTemplateEntity;
import com.company.project.mapper.ShopTemplateMapper;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.company.project.mapper.ShopParaMapper;
import com.company.project.entity.ShopParaEntity;
import com.company.project.service.ShopParaService;

import javax.annotation.Resource;


@Service("shopParaService")
public class ShopParaServiceImpl extends ServiceImpl<ShopParaMapper, ShopParaEntity> implements ShopParaService {

    @Resource
    private ShopParaMapper shopParaMapper;

    @Resource
    private ShopTemplateMapper shopTemplateMapper;

    @Override
    public void updateParametersQuantityInTemplate(String templateId) {
        // 查询当前模板下规格数量
        Integer paraCount = shopParaMapper.selectCount(Wrappers.<ShopParaEntity>lambdaQuery().eq(ShopParaEntity::getTemplateId, templateId));
        // 更新到模板主表中
        ShopTemplateEntity shopTemplateEntity = new ShopTemplateEntity();
        shopTemplateEntity.setId(templateId);
        shopTemplateEntity.setParaNum(paraCount);
        shopTemplateMapper.updateById(shopTemplateEntity);
    }
}
