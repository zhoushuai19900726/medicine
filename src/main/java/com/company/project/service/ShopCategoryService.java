package com.company.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.company.project.entity.ShopCategoryEntity;

import java.util.List;

/**
 * 商品分类
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-06-16 10:33:59
 */
public interface ShopCategoryService extends IService<ShopCategoryEntity> {

    List<ShopCategoryEntity> findSubordinateCategoryList(String parentId);

}

