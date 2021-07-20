package com.company.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.company.project.common.aop.annotation.DataScope;
import com.company.project.common.aop.annotation.LogAnnotation;
import com.company.project.common.enums.OrderStatusEnum;
import com.company.project.common.exception.code.BusinessResponseCode;
import com.company.project.common.utils.DownFileUtil;
import com.company.project.common.utils.NumberConstants;
import com.company.project.common.utils.SystemConstants;
import com.company.project.entity.ShopOrderDetailEntity;
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

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.company.project.common.utils.DataResult;

import com.company.project.entity.ShopOrderEntity;
import com.company.project.service.ShopOrderService;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;


/**
 * 订单表
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-07-16 15:52:54
 */
@Api(tags = "订单表")
@Controller
@RequestMapping("/")
public class ShopOrderController extends BaseController {

    @Resource
    private ShopOrderService shopOrderService;

    @ApiOperation(value = "跳转到订单表列表页面")
    @GetMapping("/index/shopOrder")
    public String shopOrder() {
        return "order/orderList";
    }

    @ApiOperation(value = "跳转进入订单变更页面")
    @GetMapping("/index/shopOrder/orderChange/{orderId}")
    public String orderChange(@PathVariable("orderId") String orderId, Model model) {
        // 订单信息
        ShopOrderEntity shopOrderEntity = shopOrderService.getById(orderId);
        model.addAttribute("shopOrderEntity", shopOrderEntity);
        return "order/orderChange";
    }


//    @ApiOperation(value = "跳转进入新增/编辑页面")
//    @GetMapping("/index/shopOrder/addOrUpdate")
//    public String addOrUpdate() {
//        return "shoporder/addOrUpdate";
//    }

//    @ApiOperation(value = "新增")
//    @PostMapping("shopOrder/add")
//    @RequiresPermissions("shopOrder:add")
//    @LogAnnotation(title = "订单", action = "新增")
//    @ResponseBody
//    public DataResult add(@RequestBody ShopOrderEntity shopOrder){
//        return DataResult.success(shopOrderService.save(shopOrder));
//    }

    @ApiOperation(value = "删除")
    @DeleteMapping("shopOrder/delete")
    @RequiresPermissions("shopOrder:delete")
    @LogAnnotation(title = "订单", action = "删除")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids) {
        return DataResult.success(shopOrderService.removeByIds(ids));
    }

    @ApiOperation(value = "关闭")
    @DeleteMapping("shopOrder/close")
    @RequiresPermissions("shopOrder:update")
    @LogAnnotation(title = "订单", action = "关闭")
    @ResponseBody
    public DataResult close(@RequestBody @ApiParam(value = "id集合") List<String> ids) {
        List<ShopOrderEntity> shopOrderEntityList = shopOrderService.list(Wrappers.<ShopOrderEntity>lambdaQuery().in(ShopOrderEntity::getId, ids).ne(ShopOrderEntity::getOrderStatus, OrderStatusEnum.CLOSED.getType()));
        if(CollectionUtils.isNotEmpty(shopOrderEntityList)){
            return DataResult.success(shopOrderService.update(new ShopOrderEntity(new Date(), OrderStatusEnum.CLOSED.getType()), Wrappers.<ShopOrderEntity>lambdaQuery().in(ShopOrderEntity::getId, shopOrderEntityList.stream().map(ShopOrderEntity::getId).collect(Collectors.toList()))));
        }
        return DataResult.fail(BusinessResponseCode.NO_ORDERS_TO_CLOSE.getMsg());
    }

    @ApiOperation(value = "更新")
    @PutMapping("shopOrder/update")
    @RequiresPermissions("shopOrder:update")
    @LogAnnotation(title = "订单", action = "更新")
    @ResponseBody
    public DataResult update(@RequestBody ShopOrderEntity shopOrder) {
        return shopOrderService.updateShopOrderEntityById(shopOrder);
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("shopOrder/listByPage")
    @RequiresPermissions("shopOrder:list")
    @LogAnnotation(title = "订单", action = "查询分页数据")
    @DataScope
    @ResponseBody
    public DataResult findListByPage(@RequestBody ShopOrderEntity shopOrder) {
        // 查询条件
        LambdaQueryWrapper<ShopOrderEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper
                .eq(StringUtils.isNotBlank(shopOrder.getId()), ShopOrderEntity::getId, shopOrder.getId())
                .eq(StringUtils.isNotBlank(shopOrder.getTransactionId()), ShopOrderEntity::getTransactionId, shopOrder.getTransactionId())
                .eq(StringUtils.isNotBlank(shopOrder.getBuyerName()), ShopOrderEntity::getBuyerName, shopOrder.getBuyerName())
//                .apply(StringUtils.isNotBlank(shopOrder.getCreateStartTime()), "UNIX_TIMESTAMP(create_time) >= UNIX_TIMESTAMP('" + shopOrder.getCreateStartTime() + "')")
//                .apply(StringUtils.isNotBlank(shopOrder.getCreateEndTime()), "UNIX_TIMESTAMP(create_time) <= UNIX_TIMESTAMP('" + shopOrder.getCreateEndTime() + "')")
                .apply(StringUtils.isNotBlank(shopOrder.getPayStartTime()), "UNIX_TIMESTAMP(pay_time) >= UNIX_TIMESTAMP('" + shopOrder.getPayStartTime() + "')")
                .apply(StringUtils.isNotBlank(shopOrder.getPayEndTime()), "UNIX_TIMESTAMP(pay_time) <= UNIX_TIMESTAMP('" + shopOrder.getPayEndTime() + "')")
                .orderByDesc(ShopOrderEntity::getCreateTime);
        return DataResult.success(shopOrderService.page(new Page<>(shopOrder.getPage(), shopOrder.getLimit()), queryWrapper));
    }

    @ApiOperation(value = "下载赠单模板")
    @GetMapping(value = "shopOrder/downloadFreeBillTemplate")
    @LogAnnotation(title = "订单", action = "下载赠单模板")
    public void downloadImportTemplate(HttpServletResponse response) {
        DownFileUtil.downFileByPath(response, SystemConstants.importExport + "/赠单模板.xlsx");
    }

    @ApiOperation(value = "导入赠单")
    @PostMapping("shopOrder/uploadFreeBill")
    @RequiresPermissions("shopOrder:add")
    @LogAnnotation(title = "订单", action = "导入赠单")
    @ResponseBody
    public DataResult uploadFreeBill(@RequestParam(value = "file") MultipartFile file) {
        return shopOrderService.uploadFreeBill(file);
    }


}
