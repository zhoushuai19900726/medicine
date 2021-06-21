package com.company.project.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.company.project.common.utils.NumberConstants;
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
import java.util.stream.Collectors;


@Service("shopCategoryService")
public class ShopCategoryServiceImpl extends ServiceImpl<ShopCategoryMapper, ShopCategoryEntity> implements ShopCategoryService {

    @Resource
    private ShopCategoryMapper shopCategoryMapper;

    @Override
    public List<ShopCategoryEntity> findSubordinateCategoryList(String parentId) {
        return shopCategoryMapper.selectList(Wrappers.<ShopCategoryEntity>lambdaQuery().eq(ShopCategoryEntity::getParentId, parentId).orderByAsc(ShopCategoryEntity::getSeq));
    }

    @Override
    public List<ShopCategoryEntity> listByAll() {
        List<ShopCategoryEntity> shopCategoryEntityList = shopCategoryMapper.selectList(Wrappers.<ShopCategoryEntity>lambdaQuery().orderByAsc(ShopCategoryEntity::getSeq));
        if(CollectionUtils.isNotEmpty(shopCategoryEntityList)){
            Map<String, List<ShopCategoryEntity>> groupBy = shopCategoryEntityList.stream().collect(Collectors.groupingBy(ShopCategoryEntity::getParentId));
            shopCategoryEntityList.forEach(shopCategoryEntity -> shopCategoryEntity.setChildren(groupBy.get(shopCategoryEntity.getId())));
        }
        return shopCategoryEntityList.stream().filter(a -> a.getParentId().equals(NumberConstants.ZERO_STR)).collect(Collectors.toList());
    }
}
