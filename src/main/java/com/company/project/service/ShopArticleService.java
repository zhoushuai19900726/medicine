package com.company.project.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.company.project.entity.ShopArticleEntity;

/**
 * 文章
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-07-27 13:56:46
 */
public interface ShopArticleService extends IService<ShopArticleEntity> {

    IPage<ShopArticleEntity> listByPage(Page<ShopArticleEntity> page, LambdaQueryWrapper<ShopArticleEntity> wrapper);

}

