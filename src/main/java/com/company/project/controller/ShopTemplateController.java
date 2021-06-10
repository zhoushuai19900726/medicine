package com.company.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.company.project.common.aop.annotation.DataScope;
import com.company.project.common.aop.annotation.LogAnnotation;
import io.swagger.annotations.Api;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.List;

import com.company.project.common.utils.DataResult;

import com.company.project.entity.ShopTemplateEntity;
import com.company.project.service.ShopTemplateService;

import javax.annotation.Resource;


/**
 * 商品模板
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-06-10 11:02:07
 */
@Api(tags = "商品模板")
@Controller
@RequestMapping("/")
public class ShopTemplateController extends BaseController {

    @Resource
    private ShopTemplateService shopTemplateService;

    @ApiOperation(value = "跳转进入列表页面")
    @GetMapping("/index/shopTemplate")
    public String shopTemplate() {
        return "template/templateList";
    }

    @ApiOperation(value = "跳转进入新增/编辑页面")
    @GetMapping("/index/shopTemplate/addOrUpdate")
    public String addOrUpdate() {
        return "template/addOrUpdate";
    }

    @ApiOperation(value = "新增")
    @PostMapping("shopTemplate/add")
    @RequiresPermissions("shopTemplate:add")
    @LogAnnotation(title = "商品模板", action = "新增模板")
    @ResponseBody
    public DataResult add(@RequestBody ShopTemplateEntity shopTemplate) {
        shopTemplateService.save(shopTemplate);
        return DataResult.success();
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("shopTemplate/delete")
    @RequiresPermissions("shopTemplate:delete")
    @LogAnnotation(title = "商品模板", action = "删除模板")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids) {
        shopTemplateService.removeByIds(ids);
        return DataResult.success();
    }

    @ApiOperation(value = "更新")
    @PutMapping("shopTemplate/update")
    @RequiresPermissions("shopTemplate:update")
    @LogAnnotation(title = "商品模板", action = "更新模板")
    @ResponseBody
    public DataResult update(@RequestBody ShopTemplateEntity shopTemplate) {
        shopTemplateService.updateById(shopTemplate);
        return DataResult.success();
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("shopTemplate/listByPage")
    @RequiresPermissions("shopTemplate:list")
    @LogAnnotation(title = "商品模板", action = "查询分页数据")
    @DataScope
    @ResponseBody
    public DataResult findListByPage(@RequestBody ShopTemplateEntity shopTemplate) {
        // 分页初始化
        Page<ShopTemplateEntity> page = new Page<>(shopTemplate.getPage(), shopTemplate.getLimit());
        // 查询条件
        LambdaQueryWrapper<ShopTemplateEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper
                .eq(StringUtils.isNotBlank(shopTemplate.getId()), ShopTemplateEntity::getId, shopTemplate.getId())
                .like(StringUtils.isNotBlank(shopTemplate.getName()), ShopTemplateEntity::getName, shopTemplate.getName())
                .apply(StringUtils.isNotBlank(shopTemplate.getCreateStartTime()), "UNIX_TIMESTAMP(create_time) >= UNIX_TIMESTAMP('" + shopTemplate.getCreateStartTime() + "')")
                .apply(StringUtils.isNotBlank(shopTemplate.getCreateEndTime()), "UNIX_TIMESTAMP(create_time) <= UNIX_TIMESTAMP('" + shopTemplate.getCreateEndTime() + "')")
                .orderByDesc(ShopTemplateEntity::getCreateTime);
        // 封装数据权限 - 执行查询 - 封装用户 - 响应前端
        return DataResult.success(encapsulationUser(shopTemplateService.page(page, encapsulationDataRights(shopTemplate, queryWrapper, ShopTemplateEntity::getCreateId))));
    }

}
