package com.company.project.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.company.project.mapper.ShopCategoryMapper;
import com.company.project.entity.ShopCategoryEntity;
import com.company.project.service.ShopCategoryService;

import javax.annotation.Resource;
import java.util.List;


@Service("shopCategoryService")
public class ShopCategoryServiceImpl extends ServiceImpl<ShopCategoryMapper, ShopCategoryEntity> implements ShopCategoryService {

    @Resource
    private ShopCategoryMapper shopCategoryMapper;

    @Override
    public List<ShopCategoryEntity> findSubordinateCategoryList(String parentId) {
        return shopCategoryMapper.selectList(Wrappers.<ShopCategoryEntity>lambdaQuery().eq(ShopCategoryEntity::getParentId, parentId).orderByAsc(ShopCategoryEntity::getSeq));
    }
}
