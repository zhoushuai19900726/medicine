package com.company.project.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.company.project.entity.ShopCategoryEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品分类
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-06-16 10:33:59
 */
public interface ShopCategoryService extends IService<ShopCategoryEntity> {

    ShopCategoryEntity getShopCategoryEntityById(String id);

    List<ShopCategoryEntity> findSubordinateCategoryList(String parentId);

    List<ShopCategoryEntity> listByAll();

    IPage<ShopCategoryEntity> listByPage(Page<ShopCategoryEntity> page, LambdaQueryWrapper<ShopCategoryEntity> wrapper);

    Map<String, String> getAllParent(List<String> category3IdList);

}

