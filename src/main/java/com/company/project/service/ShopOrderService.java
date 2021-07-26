package com.company.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.company.project.common.utils.DataResult;
import com.company.project.entity.ShopOrderEntity;
import org.springframework.web.multipart.MultipartFile;

/**
 * 订单表
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-07-16 15:52:54
 */
public interface ShopOrderService extends IService<ShopOrderEntity> {

    DataResult updateShopOrderEntityById(ShopOrderEntity shopOrder);

    DataResult uploadFreeBill(MultipartFile file);

    DataResult uploadWaybill(MultipartFile file, String shippingId, String consignTime);

}

