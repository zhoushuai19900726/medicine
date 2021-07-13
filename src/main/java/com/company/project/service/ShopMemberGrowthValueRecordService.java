package com.company.project.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.company.project.common.utils.DataResult;
import com.company.project.entity.ShopMemberGrowthValueRecordEntity;

import java.util.List;

/**
 * 会员成长值记录
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-07-09 17:10:36
 */
public interface ShopMemberGrowthValueRecordService extends IService<ShopMemberGrowthValueRecordEntity> {

    IPage<ShopMemberGrowthValueRecordEntity> listByPage(Page<ShopMemberGrowthValueRecordEntity> page, LambdaQueryWrapper<ShopMemberGrowthValueRecordEntity> wrapper);

    DataResult saveShopMemberGrowthValueRecordEntity(ShopMemberGrowthValueRecordEntity shopMemberGrowthValueRecord);

}

