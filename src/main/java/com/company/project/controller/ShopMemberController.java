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

import com.company.project.entity.ShopMemberEntity;
import com.company.project.service.ShopMemberService;

import javax.annotation.Resource;


/**
 * 会员
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-07-08 13:59:46
 */
@Api(tags = "会员")
@Controller
@RequestMapping("/")
public class ShopMemberController extends BaseController {

    @Resource
    private ShopMemberService shopMemberService;


    @ApiOperation(value = "跳转到会员列表页面")
    @GetMapping("/index/shopMember")
    public String shopMember() {
        return "member/memberList";
    }

    @ApiOperation(value = "跳转进入新增/编辑页面")
    @GetMapping("/index/shopMember/addOrUpdate")
    public String addOrUpdate() {
        return "member/addOrUpdate";
    }

    @ApiOperation(value = "新增")
    @PostMapping("shopMember/add")
    @RequiresPermissions("shopMember:add")
    @LogAnnotation(title = "会员", action = "新增")
    @ResponseBody
    public DataResult add(@RequestBody ShopMemberEntity shopMember) {
        return DataResult.success(shopMemberService.save(shopMember));
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("shopMember/delete")
    @RequiresPermissions("shopMember:delete")
    @LogAnnotation(title = "会员", action = "删除")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids) {
        return DataResult.success(shopMemberService.removeByIds(ids));
    }

    @ApiOperation(value = "更新")
    @PutMapping("shopMember/update")
    @RequiresPermissions("shopMember:update")
    @LogAnnotation(title = "会员", action = "更新")
    @ResponseBody
    public DataResult update(@RequestBody ShopMemberEntity shopMember) {
        return DataResult.success(shopMemberService.updateById(shopMember));
    }

    @ApiOperation(value = "查询全部")
    @GetMapping("shopMember/listByAll")
    @RequiresPermissions("shopMember:list")
    @LogAnnotation(title = "会员", action = "查询全部")
    @ResponseBody
    public DataResult findListByAll() {
        return DataResult.success(shopMemberService.list());
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("shopMember/listByPage")
    @RequiresPermissions("shopMember:list")
    @LogAnnotation(title = "会员", action = "查询分页数据")
    @DataScope
    @ResponseBody
    public DataResult findListByPage(@RequestBody ShopMemberEntity shopMember) {
        // 查询条件
        LambdaQueryWrapper<ShopMemberEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper
                .eq(StringUtils.isNotBlank(shopMember.getMemberName()), ShopMemberEntity::getMemberName, shopMember.getMemberName())
                .eq(StringUtils.isNotBlank(shopMember.getMemberMobile()), ShopMemberEntity::getMemberMobile, shopMember.getMemberMobile())
                .eq(StringUtils.isNotBlank(shopMember.getMemberInvitationCode()), ShopMemberEntity::getMemberInvitationCode, shopMember.getMemberInvitationCode())
                .orderByDesc(ShopMemberEntity::getCreateTime);
        // 封装数据权限 - 执行查询 - 封装用户 - 响应前端
//        return DataResult.success(encapsulationUser(shopMemberService.page(page, encapsulationDataRights(shopMember, queryWrapper, ShopMemberEntity::getCreateId))));
        return DataResult.success(shopMemberService.page(new Page<>(shopMember.getPage(), shopMember.getLimit()), queryWrapper));
    }

}
