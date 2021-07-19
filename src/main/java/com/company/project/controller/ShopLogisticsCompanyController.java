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

import com.company.project.entity.ShopLogisticsCompanyEntity;
import com.company.project.service.ShopLogisticsCompanyService;

import javax.annotation.Resource;


/**
 * 快递公司
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-07-19 16:10:29
 */
@Api(tags = "快递公司")
@Controller
@RequestMapping("/")
public class ShopLogisticsCompanyController extends BaseController {

    @Resource
    private ShopLogisticsCompanyService shopLogisticsCompanyService;


    @ApiOperation(value = "跳转到快递公司列表页面")
    @GetMapping("/index/shopLogisticsCompany")
    public String shopLogisticsCompany() {
        return "logisticscompany/logisticsCompanyList";
    }

    @ApiOperation(value = "跳转进入新增/编辑页面")
    @GetMapping("/index/shopLogisticsCompany/addOrUpdate")
    public String addOrUpdate() {
        return "logisticscompany/addOrUpdate";
    }

    @ApiOperation(value = "新增")
    @PostMapping("shopLogisticsCompany/add")
    @RequiresPermissions("shopLogisticsCompany:add")
    @LogAnnotation(title = "快递公司", action = "新增")
    @ResponseBody
    public DataResult add(@RequestBody ShopLogisticsCompanyEntity shopLogisticsCompany) {
        return shopLogisticsCompanyService.saveShopLogisticsCompanyEntity(shopLogisticsCompany);
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("shopLogisticsCompany/delete")
    @RequiresPermissions("shopLogisticsCompany:delete")
    @LogAnnotation(title = "快递公司", action = "删除")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids) {
        return shopLogisticsCompanyService.removeShopLogisticsCompanyEntityByIds(ids);
    }

    @ApiOperation(value = "更新")
    @PutMapping("shopLogisticsCompany/update")
    @RequiresPermissions("shopLogisticsCompany:update")
    @LogAnnotation(title = "快递公司", action = "更新")
    @ResponseBody
    public DataResult update(@RequestBody ShopLogisticsCompanyEntity shopLogisticsCompany) {
        return shopLogisticsCompanyService.updateShopLogisticsCompanyEntityById(shopLogisticsCompany);
    }

    @ApiOperation(value = "查询全部")
    @GetMapping("shopLogisticsCompany/listByAll")
    @RequiresPermissions("shopLogisticsCompany:list")
    @LogAnnotation(title = "快递公司", action = "查询全部")
    @ResponseBody
    public DataResult findListByAll() {
        return DataResult.success(shopLogisticsCompanyService.list(Wrappers.<ShopLogisticsCompanyEntity>lambdaQuery().orderByAsc(ShopLogisticsCompanyEntity::getSeq)));
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("shopLogisticsCompany/listByPage")
    @RequiresPermissions("shopLogisticsCompany:list")
    @LogAnnotation(title = "快递公司", action = "查询分页数据")
    @DataScope
    @ResponseBody
    public DataResult findListByPage(@RequestBody ShopLogisticsCompanyEntity shopLogisticsCompany) {
        // 查询条件
        LambdaQueryWrapper<ShopLogisticsCompanyEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper
                .eq(StringUtils.isNotBlank(shopLogisticsCompany.getId()), ShopLogisticsCompanyEntity::getId, shopLogisticsCompany.getId())
                .like(StringUtils.isNotBlank(shopLogisticsCompany.getName()), ShopLogisticsCompanyEntity::getName, shopLogisticsCompany.getName())
                .apply(StringUtils.isNotBlank(shopLogisticsCompany.getCreateStartTime()), "UNIX_TIMESTAMP(create_time) >= UNIX_TIMESTAMP('" + shopLogisticsCompany.getCreateStartTime() + "')")
                .apply(StringUtils.isNotBlank(shopLogisticsCompany.getCreateEndTime()), "UNIX_TIMESTAMP(create_time) <= UNIX_TIMESTAMP('" + shopLogisticsCompany.getCreateEndTime() + "')")
                .orderByAsc(ShopLogisticsCompanyEntity::getSeq);
        // 封装数据权限 - 执行查询 - 封装用户 - 响应前端
        return DataResult.success(encapsulationUser(shopLogisticsCompanyService.page(new Page<>(shopLogisticsCompany.getPage(), shopLogisticsCompany.getLimit()), encapsulationDataRights(shopLogisticsCompany, queryWrapper, ShopLogisticsCompanyEntity::getCreateId))));
    }

}
