package com.company.project.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.company.project.entity.ShopMemberWalletRecordEntity;

/**
 * 会员钱包
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-07-09 17:10:37
 */
public interface ShopMemberWalletRecordService extends IService<ShopMemberWalletRecordEntity> {

    IPage<ShopMemberWalletRecordEntity> listByPage(Page<ShopMemberWalletRecordEntity> page, LambdaQueryWrapper<ShopMemberWalletRecordEntity> wrapper);

}

