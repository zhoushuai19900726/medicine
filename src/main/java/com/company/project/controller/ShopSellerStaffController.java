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

import com.company.project.entity.ShopSellerStaffEntity;
import com.company.project.service.ShopSellerStaffService;

import javax.annotation.Resource;


/**
 * 商家员工
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-08-06 10:48:30
 */
@Api(tags = "商家员工")
@Controller
@RequestMapping("/")
public class ShopSellerStaffController extends BaseController {

    @Resource
    private ShopSellerStaffService shopSellerStaffService;


    @ApiOperation(value = "跳转到商家员工列表页面")
    @GetMapping("/index/shopSellerStaff")
    public String shopSellerStaff() {
        return "sellerstaff/staffList";
    }

    @ApiOperation(value = "跳转进入新增/编辑页面")
    @GetMapping("/index/shopSellerStaff/addOrUpdate")
    public String addOrUpdate() {
        return "sellerstaff/addOrUpdate";
    }

    @ApiOperation(value = "新增")
    @PostMapping("shopSellerStaff/add")
    @RequiresPermissions("shopSellerStaff:add")
    @LogAnnotation(title = "商家员工", action = "新增")
    @ResponseBody
    public DataResult add(@RequestBody ShopSellerStaffEntity shopSellerStaff) {
        return shopSellerStaffService.saveShopSellerStaffEntity(shopSellerStaff);
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("shopSellerStaff/delete")
    @RequiresPermissions("shopSellerStaff:delete")
    @LogAnnotation(title = "商家员工", action = "删除")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids) {
        return DataResult.success(shopSellerStaffService.removeByIds(ids));
    }

    @ApiOperation(value = "更新")
    @PutMapping("shopSellerStaff/update")
    @RequiresPermissions("shopSellerStaff:update")
    @LogAnnotation(title = "商家员工", action = "更新")
    @ResponseBody
    public DataResult update(@RequestBody ShopSellerStaffEntity shopSellerStaff) {
        return shopSellerStaffService.updateShopSellerStaffEntityById(shopSellerStaff);
    }

    @ApiOperation(value = "查询全部")
    @GetMapping("shopSellerStaff/listByAll")
    @RequiresPermissions("shopSellerStaff:list")
    @LogAnnotation(title = "商家员工", action = "查询全部")
    @ResponseBody
    public DataResult findListByAll() {
        return DataResult.success(shopSellerStaffService.list());
    }

    @ApiOperation(value = "根据账号查询")
    @PostMapping("shopSellerStaff/getByAccount")
    @RequiresPermissions("shopSellerStaff:list")
    @LogAnnotation(title = "商家员工", action = "根据账号查询")
    @ResponseBody
    public DataResult getByAccount(@RequestBody ShopSellerStaffEntity shopSellerStaff) {
        return DataResult.success(shopSellerStaffService.getOne(Wrappers.<ShopSellerStaffEntity>lambdaQuery().eq(ShopSellerStaffEntity::getAccount, shopSellerStaff.getAccount()).eq(ShopSellerStaffEntity::getSellerId, shopSellerStaff.getSellerId()).ne(StringUtils.isNotBlank(shopSellerStaff.getId()), ShopSellerStaffEntity::getId, shopSellerStaff.getId())));
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("shopSellerStaff/listByPage")
    @RequiresPermissions("shopSellerStaff:list")
    @LogAnnotation(title = "商家员工", action = "查询分页数据")
    @DataScope
    @ResponseBody
    public DataResult findListByPage(@RequestBody ShopSellerStaffEntity shopSellerStaff) {
        // 查询条件
        LambdaQueryWrapper<ShopSellerStaffEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper
                .like(StringUtils.isNotBlank(shopSellerStaff.getAccount()), ShopSellerStaffEntity::getAccount, shopSellerStaff.getAccount())
                .like(StringUtils.isNotBlank(shopSellerStaff.getName()), ShopSellerStaffEntity::getName, shopSellerStaff.getName())
                .apply(StringUtils.isNotBlank(shopSellerStaff.getCreateStartTime()), "UNIX_TIMESTAMP(create_time) >= UNIX_TIMESTAMP('" + shopSellerStaff.getCreateStartTime() + "')")
                .apply(StringUtils.isNotBlank(shopSellerStaff.getCreateEndTime()), "UNIX_TIMESTAMP(create_time) <= UNIX_TIMESTAMP('" + shopSellerStaff.getCreateEndTime() + "')")
                .orderByDesc(ShopSellerStaffEntity::getCreateTime);
        // 封装数据权限 - 执行查询 - 封装用户 - 响应前端
        return DataResult.success(encapsulationUser(shopSellerStaffService.listByPage(new Page<>(shopSellerStaff.getPage(), shopSellerStaff.getLimit()), encapsulationDataRights(shopSellerStaff, queryWrapper, ShopSellerStaffEntity::getCreateId))));
    }

}
