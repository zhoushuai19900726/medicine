package com.company.project.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.company.project.common.utils.DataResult;
import com.company.project.entity.ShopMemberBlacklistEntity;

/**
 * 会员表
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-07-12 13:58:08
 */
public interface ShopMemberBlacklistService extends IService<ShopMemberBlacklistEntity> {

    IPage<ShopMemberBlacklistEntity> listByPage(Page<ShopMemberBlacklistEntity> page, LambdaQueryWrapper<ShopMemberBlacklistEntity> wrapper);

    DataResult saveShopMemberBlacklistEntity(ShopMemberBlacklistEntity shopMemberBlacklist);
}

