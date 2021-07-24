package com.company.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.company.project.common.aop.annotation.DataScope;
import com.company.project.common.aop.annotation.LogAnnotation;
import com.company.project.entity.ShopOrderDetailEntity;
import com.company.project.entity.ShopOrderEntity;
import com.company.project.entity.ShopReturnOrderDetailEntity;
import com.company.project.service.ShopOrderDetailService;
import com.company.project.service.ShopOrderService;
import com.company.project.service.ShopReturnOrderDetailService;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.commons.lang.StringUtils;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.List;

import com.company.project.common.utils.DataResult;

import com.company.project.entity.ShopReturnOrderEntity;
import com.company.project.service.ShopReturnOrderService;

import javax.annotation.Resource;


/**
 * 退款申请
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-07-24 09:52:58
 */
@Api(tags = "退款申请")
@Controller
@RequestMapping("/")
public class ShopReturnOrderController extends BaseController {

    @Resource
    private ShopReturnOrderService shopReturnOrderService;

    @Resource
    private ShopReturnOrderDetailService shopReturnOrderDetailService;

    @Resource
    private ShopOrderDetailService shopOrderDetailService;

    @Resource
    private ShopOrderService shopOrderService;


    @ApiOperation(value = "跳转进入退货退款页面")
    @GetMapping("/index/shopReturnOrder/returnOrder/{orderId}")
    public String returnOrder(@PathVariable("orderId") String orderId, Model model) {
        // 订单信息
        ShopOrderEntity shopOrderEntity = shopOrderService.getById(orderId);
        model.addAttribute("shopOrderEntity", shopOrderEntity);
        // 订单商品信息
        List<ShopOrderDetailEntity> shopOrderDetailEntityList = shopOrderDetailService.list(Wrappers.<ShopOrderDetailEntity>lambdaQuery().in(ShopOrderDetailEntity::getOrderId, orderId));
        model.addAttribute("shopOrderDetailEntityList", shopOrderDetailEntityList);
        return "returnorder/returnOrder";
    }

    @ApiOperation(value = "跳转进入退货退款详情页面")
    @GetMapping("/index/shopReturnOrder/returnOrderDetail/{returnOrderId}")
    public String returnOrderDetail(@PathVariable("returnOrderId") String returnOrderId, Model model) {
        // 退单信息
        ShopReturnOrderEntity shopReturnOrderEntity = shopReturnOrderService.getById(returnOrderId);
        model.addAttribute("shopReturnOrderEntity", shopReturnOrderEntity);
        // 订单信息
        ShopOrderEntity shopOrderEntity = shopOrderService.getById(shopReturnOrderEntity.getOrderId());
        model.addAttribute("shopOrderEntity", shopOrderEntity);
        // 退单商品信息
        List<ShopReturnOrderDetailEntity> shopReturnOrderDetailEntityList = shopReturnOrderDetailService.list(Wrappers.<ShopReturnOrderDetailEntity>lambdaQuery().in(ShopReturnOrderDetailEntity::getReturnOrderId, returnOrderId));
        model.addAttribute("shopReturnOrderDetailEntityList", shopReturnOrderDetailEntityList);
        return "returnorder/returnOrderDetail";
    }


    @ApiOperation(value = "跳转到退款申请列表页面")
    @GetMapping("/index/shopReturnOrder")
    public String shopReturnOrder() {
        return "returnorder/returnOrderList";
    }

    @ApiOperation(value = "跳转到退款退货申请列表页面")
    @GetMapping("/index/shopReturnRefundOrder")
    public String shopReturnRefundOrder() {
        return "returnorder/returnRefundOrderList";
    }

//    @ApiOperation(value = "跳转进入新增/编辑页面")
//    @GetMapping("/index/shopReturnOrder/addOrUpdate")
//    public String addOrUpdate() {
//        return "shopreturnorder/addOrUpdate";
//    }

    @ApiOperation(value = "新增")
    @PostMapping("shopReturnOrder/add")
    @RequiresPermissions("shopReturnOrder:add")
    @LogAnnotation(title = "退款申请", action = "新增")
    @ResponseBody
    public DataResult add(@RequestBody ShopReturnOrderEntity shopReturnOrder) {
        return shopReturnOrderService.saveShopReturnOrderEntity(shopReturnOrder);
    }

//    @ApiOperation(value = "删除")
//    @DeleteMapping("shopReturnOrder/delete")
//    @RequiresPermissions("shopReturnOrder:delete")
//    @LogAnnotation(title = "退款申请", action = "删除")
//    @ResponseBody
//    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids){
//        return DataResult.success(shopReturnOrderService.removeByIds(ids));
//    }
//
//    @ApiOperation(value = "更新")
//    @PutMapping("shopReturnOrder/update")
//    @RequiresPermissions("shopReturnOrder:update")
//    @LogAnnotation(title = "退款申请", action = "更新")
//    @ResponseBody
//    public DataResult update(@RequestBody ShopReturnOrderEntity shopReturnOrder){
//        return DataResult.success(shopReturnOrderService.updateById(shopReturnOrder));
//    }
//
//    @ApiOperation(value = "查询全部")
//    @GetMapping("shopReturnOrder/listByAll")
//    @RequiresPermissions("shopReturnOrder:list")
//    @LogAnnotation(title = "退款申请", action = "查询全部")
//    @ResponseBody
//    public DataResult findListByAll() {
//        return DataResult.success(shopReturnOrderService.list());
//    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("shopReturnOrder/listByPage")
    @RequiresPermissions("shopReturnOrder:list")
    @LogAnnotation(title = "退款申请", action = "查询分页数据")
    @DataScope
    @ResponseBody
    public DataResult findListByPage(@RequestBody ShopReturnOrderEntity shopReturnOrder) {
        // 查询条件
        LambdaQueryWrapper<ShopReturnOrderEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper
                .eq(StringUtils.isNotBlank(shopReturnOrder.getId()), ShopReturnOrderEntity::getId, shopReturnOrder.getId())
                .eq(ShopReturnOrderEntity::getType, shopReturnOrder.getType())
                .apply(StringUtils.isNotBlank(shopReturnOrder.getCreateStartTime()), "UNIX_TIMESTAMP(create_time) >= UNIX_TIMESTAMP('" + shopReturnOrder.getCreateStartTime() + "')")
                .apply(StringUtils.isNotBlank(shopReturnOrder.getCreateEndTime()), "UNIX_TIMESTAMP(create_time) <= UNIX_TIMESTAMP('" + shopReturnOrder.getCreateEndTime() + "')")
                .orderByDesc(ShopReturnOrderEntity::getCreateTime);
        // 封装数据权限 - 执行查询 - 封装用户 - 响应前端
        return DataResult.success(encapsulationUser(shopReturnOrderService.page(new Page<>(shopReturnOrder.getPage(), shopReturnOrder.getLimit()), encapsulationDataRights(shopReturnOrder, queryWrapper, ShopReturnOrderEntity::getCreateId))));
    }

}
