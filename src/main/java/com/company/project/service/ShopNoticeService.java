package com.company.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.company.project.common.utils.DataResult;
import com.company.project.entity.ShopNoticeEntity;

import java.util.List;

/**
 * 公告
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-07-16 13:56:11
 */
public interface ShopNoticeService extends IService<ShopNoticeEntity> {

    DataResult saveShopNoticeEntity(ShopNoticeEntity shopNotice);

    DataResult removeShopNoticeEntityByIds(List<String> ids);

    DataResult updateShopNoticeEntityById(ShopNoticeEntity shopNotice);

}

