package com.company.project.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.company.project.entity.ShopTemplateEntity;
import com.company.project.mapper.ShopTemplateMapper;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.company.project.mapper.ShopSpecMapper;
import com.company.project.entity.ShopSpecEntity;
import com.company.project.service.ShopSpecService;

import javax.annotation.Resource;


@Service("shopSpecService")
public class ShopSpecServiceImpl extends ServiceImpl<ShopSpecMapper, ShopSpecEntity> implements ShopSpecService {

    @Resource
    private ShopSpecMapper shopSpecMapper;

    @Resource
    private ShopTemplateMapper shopTemplateMapper;

    @Override
    public void updateSpecificationQuantityInTemplate(String templateId) {
        // 查询当前模板下规格数量
        Integer specCount = shopSpecMapper.selectCount(Wrappers.<ShopSpecEntity>lambdaQuery().eq(ShopSpecEntity::getTemplateId, templateId));
        // 更新到模板主表中
        ShopTemplateEntity shopTemplateEntity = new ShopTemplateEntity();
        shopTemplateEntity.setId(templateId);
        shopTemplateEntity.setSpecNum(specCount);
        shopTemplateMapper.updateById(shopTemplateEntity);
    }

}
