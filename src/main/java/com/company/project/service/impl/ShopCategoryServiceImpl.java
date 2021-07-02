package com.company.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.project.common.utils.DelimiterConstants;
import com.company.project.common.utils.NumberConstants;
import com.company.project.entity.ShopTemplateEntity;
import com.company.project.mapper.ShopTemplateMapper;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.company.project.mapper.ShopCategoryMapper;
import com.company.project.entity.ShopCategoryEntity;
import com.company.project.service.ShopCategoryService;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


@Service("shopCategoryService")
public class ShopCategoryServiceImpl extends ServiceImpl<ShopCategoryMapper, ShopCategoryEntity> implements ShopCategoryService {

    @Resource
    private ShopCategoryMapper shopCategoryMapper;

    @Resource
    private ShopTemplateMapper shopTemplateMapper;

    @Override
    public ShopCategoryEntity getShopCategoryEntityById(String id) {
        ShopCategoryEntity shopCategoryEntity = shopCategoryMapper.selectById(id);
        if(Objects.nonNull(shopCategoryEntity)){
            ShopTemplateEntity shopTemplateEntity = shopTemplateMapper.selectById(shopCategoryEntity.getTemplateId());
            if(Objects.nonNull(shopTemplateEntity)){
                shopCategoryEntity.setTemplateName(shopTemplateEntity.getName());
            }
        }
        return shopCategoryEntity;
    }

    @Override
    public List<ShopCategoryEntity> findSubordinateCategoryList(String parentId) {
        return shopCategoryMapper.selectList(Wrappers.<ShopCategoryEntity>lambdaQuery().eq(ShopCategoryEntity::getParentId, parentId).orderByAsc(ShopCategoryEntity::getSeq));
    }

    @Override
    public List<ShopCategoryEntity> listByAll() {
        List<ShopCategoryEntity> shopCategoryEntityList = shopCategoryMapper.selectList(Wrappers.<ShopCategoryEntity>lambdaQuery().orderByAsc(ShopCategoryEntity::getSeq));
        if (CollectionUtils.isNotEmpty(shopCategoryEntityList)) {
            Map<String, List<ShopCategoryEntity>> groupBy = shopCategoryEntityList.stream().collect(Collectors.groupingBy(ShopCategoryEntity::getParentId));
            shopCategoryEntityList.forEach(shopCategoryEntity -> shopCategoryEntity.setChildren(groupBy.get(shopCategoryEntity.getId())));
        }
        return shopCategoryEntityList.stream().filter(a -> a.getParentId().equals(NumberConstants.ZERO_STR)).collect(Collectors.toList());
    }

    @Override
    public IPage<ShopCategoryEntity> listByPage(Page<ShopCategoryEntity> page, LambdaQueryWrapper<ShopCategoryEntity> wrapper) {
        IPage<ShopCategoryEntity> iPage = shopCategoryMapper.selectPage(page, wrapper);
        // 封装模板名称
        List<ShopCategoryEntity> shopCategoryEntityList = iPage.getRecords();
        if (CollectionUtils.isNotEmpty(shopCategoryEntityList)) {
            //  提取模板ID集合
            List<String> templateIdList = shopCategoryEntityList.stream().map(ShopCategoryEntity::getTemplateId).collect(Collectors.toList());
            // 查询模板集合
            List<ShopTemplateEntity> shopTemplateEntityList = shopTemplateMapper.selectBatchIds(templateIdList);
            if (CollectionUtils.isNotEmpty(shopTemplateEntityList)) {
                // 封装模板名称
                Map<String, String> shopTemplateEntityMap = shopTemplateEntityList.stream().collect(Collectors.toMap(ShopTemplateEntity::getId, ShopTemplateEntity::getName, (k1, k2) -> k1));
                shopCategoryEntityList.forEach(shopCategoryEntity -> shopCategoryEntity.setTemplateName(shopTemplateEntityMap.getOrDefault(shopCategoryEntity.getTemplateId(), DelimiterConstants.EMPTY_STR)));
            }
        }
        return iPage;
    }
}
