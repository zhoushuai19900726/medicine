package com.company.project.controller;

import com.company.project.common.aop.annotation.LogAnnotation;
import com.company.project.common.utils.NumberConstants;
import io.swagger.annotations.Api;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import io.swagger.annotations.ApiOperation;
import com.company.project.common.utils.DataResult;

import com.company.project.entity.ShopOrderConfigEntity;
import com.company.project.service.ShopOrderConfigService;

import javax.annotation.Resource;
import java.util.List;


/**
 * 订单设置
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-07-20 11:29:38
 */
@Api(tags = "订单设置")
@Controller
@RequestMapping("/")
public class ShopOrderConfigController extends BaseController {

    @Resource
    private ShopOrderConfigService shopOrderConfigService;

    @ApiOperation(value = "跳转进入订单配置页面")
    @GetMapping("/index/shopOrderConfig")
    public String addOrUpdate(Model model) {
        List<ShopOrderConfigEntity> shopOrderConfigEntityList = shopOrderConfigService.list();
        model.addAttribute("shopOrderConfigEntity", CollectionUtils.isNotEmpty(shopOrderConfigEntityList) ? shopOrderConfigEntityList.get(NumberConstants.ZERO) : new ShopOrderConfigEntity());
        return "order/orderConfig";
    }

    @ApiOperation(value = "新增/修改")
    @PostMapping("shopOrderConfig/addOrUpdate")
    @RequiresPermissions(value = {"shopOrderConfig:add", "shopOrderConfig:update"}, logical = Logical.OR)
    @LogAnnotation(title = "订单设置", action = "新增")
    @ResponseBody
    public DataResult addOrUpdate(@RequestBody ShopOrderConfigEntity shopOrderConfig){
        return DataResult.success(shopOrderConfigService.saveOrUpdate(shopOrderConfig));
    }

}
