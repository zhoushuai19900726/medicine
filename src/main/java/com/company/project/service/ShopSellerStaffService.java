package com.company.project.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.company.project.common.utils.DataResult;
import com.company.project.entity.ShopSellerStaffEntity;
import com.company.project.entity.SysUser;
import com.company.project.vo.resp.LoginRespVO;

/**
 * 商家员工
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-08-06 10:48:30
 */
public interface ShopSellerStaffService extends IService<ShopSellerStaffEntity> {

    IPage<ShopSellerStaffEntity> listByPage(Page<ShopSellerStaffEntity> page, LambdaQueryWrapper<ShopSellerStaffEntity> wrapper);

    DataResult saveShopSellerStaffEntity(ShopSellerStaffEntity shopSellerStaff);

    DataResult updateShopSellerStaffEntityById(ShopSellerStaffEntity shopSellerStaff);

    LoginRespVO login(SysUser vo);

}

