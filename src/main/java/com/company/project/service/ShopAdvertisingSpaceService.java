package com.company.project.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.company.project.entity.ShopAdvertisingSpaceEntity;

/**
 * 广告位
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-07-15 09:28:30
 */
public interface ShopAdvertisingSpaceService extends IService<ShopAdvertisingSpaceEntity> {

    IPage<ShopAdvertisingSpaceEntity> listByPage(Page<ShopAdvertisingSpaceEntity> page, LambdaQueryWrapper<ShopAdvertisingSpaceEntity> wrapper);

}

