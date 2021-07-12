package com.company.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.company.project.common.aop.annotation.DataScope;
import com.company.project.common.aop.annotation.LogAnnotation;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.List;

import com.company.project.common.utils.DataResult;

import com.company.project.entity.ShopMemberBlacklistEntity;
import com.company.project.service.ShopMemberBlacklistService;

import javax.annotation.Resource;


/**
 * 会员表
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-07-12 13:58:08
 */
@Api(tags = "会员表")
@Controller
@RequestMapping("/")
public class ShopMemberBlacklistController extends BaseController {

    @Resource
    private ShopMemberBlacklistService shopMemberBlacklistService;

    @ApiOperation(value = "跳转到会员表列表页面")
    @GetMapping("/index/blacklist")
    public String shopMemberBlacklist() {
        return "member/blacklistList";
    }

    @ApiOperation(value = "新增")
    @PostMapping("shopMemberBlacklist/add")
    @RequiresPermissions("shopMemberBlacklist:add")
    @LogAnnotation(title = "会员表", action = "新增")
    @ResponseBody
    public DataResult add(@RequestBody ShopMemberBlacklistEntity shopMemberBlacklist){
        return shopMemberBlacklistService.saveShopMemberBlacklistEntity(shopMemberBlacklist);
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("shopMemberBlacklist/delete")
    @RequiresPermissions("shopMemberBlacklist:delete")
    @LogAnnotation(title = "会员表", action = "删除")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids) {
        return DataResult.success(shopMemberBlacklistService.removeByIds(ids));
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("shopMemberBlacklist/listByPage")
    @RequiresPermissions("shopMemberBlacklist:list")
    @LogAnnotation(title = "会员表", action = "查询分页数据")
    @DataScope
    @ResponseBody
    public DataResult findListByPage(@RequestBody ShopMemberBlacklistEntity shopMemberBlacklist) {
        // 查询条件
        LambdaQueryWrapper<ShopMemberBlacklistEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper
                .eq(StringUtils.isNotBlank(shopMemberBlacklist.getMemberName()), ShopMemberBlacklistEntity::getMemberName, shopMemberBlacklist.getMemberName())
                .eq(StringUtils.isNotBlank(shopMemberBlacklist.getMemberMobile()), ShopMemberBlacklistEntity::getMemberMobile, shopMemberBlacklist.getMemberMobile())
                .eq(StringUtils.isNotBlank(shopMemberBlacklist.getMemberInvitationCode()), ShopMemberBlacklistEntity::getMemberInvitationCode, shopMemberBlacklist.getMemberInvitationCode())
                .orderByDesc(ShopMemberBlacklistEntity::getCreateTime);
        return DataResult.success(shopMemberBlacklistService.listByPage(new Page<>(shopMemberBlacklist.getPage(), shopMemberBlacklist.getLimit()), queryWrapper));
    }

}
