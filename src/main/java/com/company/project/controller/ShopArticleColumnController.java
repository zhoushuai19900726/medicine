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

import com.company.project.entity.ShopArticleColumnEntity;
import com.company.project.service.ShopArticleColumnService;

import javax.annotation.Resource;


/**
 * 文章栏目
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-07-27 11:07:43
 */
@Api(tags = "文章栏目")
@Controller
@RequestMapping("/")
public class ShopArticleColumnController extends BaseController {

    @Resource
    private ShopArticleColumnService shopArticleColumnService;


    @ApiOperation(value = "跳转到文章栏目列表页面")
    @GetMapping("/index/shopArticleColumn")
    public String shopArticleColumn() {
        return "articlecolumn/articleColumnList";
    }

    @ApiOperation(value = "跳转进入新增/编辑页面")
    @GetMapping("/index/shopArticleColumn/addOrUpdate")
    public String addOrUpdate() {
        return "articlecolumn/addOrUpdate";
    }

    @ApiOperation(value = "新增")
    @PostMapping("shopArticleColumn/add")
    @RequiresPermissions("shopArticleColumn:add")
    @LogAnnotation(title = "文章栏目", action = "新增")
    @ResponseBody
    public DataResult add(@RequestBody ShopArticleColumnEntity shopArticleColumn) {
        return DataResult.success(shopArticleColumnService.save(shopArticleColumn));
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("shopArticleColumn/delete")
    @RequiresPermissions("shopArticleColumn:delete")
    @LogAnnotation(title = "文章栏目", action = "删除")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids) {
        return DataResult.success(shopArticleColumnService.removeByIds(ids));
    }

    @ApiOperation(value = "更新")
    @PutMapping("shopArticleColumn/update")
    @RequiresPermissions("shopArticleColumn:update")
    @LogAnnotation(title = "文章栏目", action = "更新")
    @ResponseBody
    public DataResult update(@RequestBody ShopArticleColumnEntity shopArticleColumn) {
        return DataResult.success(shopArticleColumnService.updateById(shopArticleColumn));
    }

    @ApiOperation(value = "查询全部")
    @GetMapping("shopArticleColumn/listByAll")
    @RequiresPermissions("shopArticleColumn:list")
    @LogAnnotation(title = "文章栏目", action = "查询全部")
    @ResponseBody
    public DataResult findListByAll() {
        return DataResult.success(shopArticleColumnService.list());
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("shopArticleColumn/listByPage")
    @RequiresPermissions("shopArticleColumn:list")
    @LogAnnotation(title = "文章栏目", action = "查询分页数据")
    @DataScope
    @ResponseBody
    public DataResult findListByPage(@RequestBody ShopArticleColumnEntity shopArticleColumn) {
        // 查询条件
        LambdaQueryWrapper<ShopArticleColumnEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper
                .eq(StringUtils.isNotBlank(shopArticleColumn.getId()), ShopArticleColumnEntity::getId, shopArticleColumn.getId())
                .like(StringUtils.isNotBlank(shopArticleColumn.getName()), ShopArticleColumnEntity::getName, shopArticleColumn.getName())
                .apply(StringUtils.isNotBlank(shopArticleColumn.getCreateStartTime()), "UNIX_TIMESTAMP(create_time) >= UNIX_TIMESTAMP('" + shopArticleColumn.getCreateStartTime() + "')")
                .apply(StringUtils.isNotBlank(shopArticleColumn.getCreateEndTime()), "UNIX_TIMESTAMP(create_time) <= UNIX_TIMESTAMP('" + shopArticleColumn.getCreateEndTime() + "')")
                .orderByAsc(ShopArticleColumnEntity::getSeq);
        // 封装数据权限 - 执行查询 - 封装用户 - 响应前端
        return DataResult.success(encapsulationUser(shopArticleColumnService.page(new Page<>(shopArticleColumn.getPage(), shopArticleColumn.getLimit()), encapsulationDataRights(shopArticleColumn, queryWrapper, ShopArticleColumnEntity::getCreateId))));
    }

}
