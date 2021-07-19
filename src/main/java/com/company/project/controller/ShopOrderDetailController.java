package com.company.project.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.company.project.entity.ShopOrderDetailEntity;
import com.company.project.entity.ShopOrderEntity;
import com.company.project.service.ShopOrderService;
import io.swagger.annotations.Api;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import io.swagger.annotations.ApiOperation;

import com.company.project.service.ShopOrderDetailService;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;


/**
 * 订单明细
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-07-16 15:52:56
 */
@Api(tags = "订单明细")
@Controller
@RequestMapping("/")
public class ShopOrderDetailController extends BaseController {

    @Resource
    private ShopOrderDetailService shopOrderDetailService;

    @Resource
    private ShopOrderService shopOrderService;

    @ApiOperation(value = "跳转进入详情页面")
    @GetMapping("/index/shopOrderDetail/{orderId}")
    public String detail(@PathVariable("orderId") String orderId, Model model) {
        // 订单信息
        ShopOrderEntity shopOrderEntity = shopOrderService.getById(orderId);
        model.addAttribute("shopOrderEntity", shopOrderEntity);
        // 订单商品信息
        List<ShopOrderDetailEntity> shopOrderDetailEntityList = shopOrderDetailService.list(Wrappers.<ShopOrderDetailEntity>lambdaQuery().in(ShopOrderDetailEntity::getOrderId, orderId));
        model.addAttribute("shopOrderDetailEntityList", shopOrderDetailEntityList);
        return "order/orderDetail";
    }


}
