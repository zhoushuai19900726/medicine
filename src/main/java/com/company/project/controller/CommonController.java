package com.company.project.controller;

import com.company.project.common.enums.GoodsExamineStatusEnum;
import com.company.project.common.enums.GoodsStatusEnum;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 公共Controller
 *
 * @author zhoushuai
 * @version V1.0
 * @date 2020年3月18日
 */
@Controller
@RequestMapping("/common")
public class CommonController {

    @ApiOperation(value = "查询所有商品上下架状态")
    @GetMapping("findAllGoodsStatus")
    @RequiresPermissions("goods:list")
    @ResponseBody
    public List<GoodsStatusEnum> findAllGoodsStatus() {
        return Lists.newArrayList(GoodsStatusEnum.values());
    }
    @ApiOperation(value = "查询所有商品审核状态")
    @GetMapping("findAllGoodsExamineStatus")
    @RequiresPermissions("goods:list")
    @ResponseBody
    public List<GoodsExamineStatusEnum> findAllGoodsExamineStatus() {
        return Lists.newArrayList(GoodsExamineStatusEnum.values());
    }

}
