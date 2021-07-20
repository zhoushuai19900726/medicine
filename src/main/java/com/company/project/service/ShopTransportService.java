package com.company.project.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.company.project.common.utils.DataResult;
import com.company.project.entity.ShopTransportEntity;

/**
 * 运费模板
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-07-20 15:42:47
 */
public interface ShopTransportService extends IService<ShopTransportEntity> {

    IPage<ShopTransportEntity> listByPage(Page<ShopTransportEntity> page, LambdaQueryWrapper<ShopTransportEntity> wrapper);

    DataResult updateShopTransportEntityById(ShopTransportEntity shopTransport);

}

