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

import com.company.project.entity.ShopCarouselMapEntity;
import com.company.project.service.ShopCarouselMapService;

import javax.annotation.Resource;


/**
 * 轮播图
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-07-16 14:53:02
 */
@Api(tags = "轮播图")
@Controller
@RequestMapping("/")
public class ShopCarouselMapController extends BaseController {

    @Resource
    private ShopCarouselMapService shopCarouselMapService;


    @ApiOperation(value = "跳转到轮播图列表页面")
    @GetMapping("/index/shopCarouselMap")
    public String shopCarouselMap() {
        return "carouselmap/carouselMapList";
    }

    @ApiOperation(value = "跳转进入新增/编辑页面")
    @GetMapping("/index/shopCarouselMap/addOrUpdate")
    public String addOrUpdate() {
        return "carouselmap/addOrUpdate";
    }

    @ApiOperation(value = "新增")
    @PostMapping("shopCarouselMap/add")
    @RequiresPermissions("shopCarouselMap:add")
    @LogAnnotation(title = "轮播图", action = "新增")
    @ResponseBody
    public DataResult add(@RequestBody ShopCarouselMapEntity shopCarouselMap) {
        return shopCarouselMapService.saveShopCarouselMapEntity(shopCarouselMap);
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("shopCarouselMap/delete")
    @RequiresPermissions("shopCarouselMap:delete")
    @LogAnnotation(title = "轮播图", action = "删除")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids) {
        return shopCarouselMapService.removeShopCarouselMapEntityByIds(ids);
    }

    @ApiOperation(value = "更新")
    @PutMapping("shopCarouselMap/update")
    @RequiresPermissions("shopCarouselMap:update")
    @LogAnnotation(title = "轮播图", action = "更新")
    @ResponseBody
    public DataResult update(@RequestBody ShopCarouselMapEntity shopCarouselMap) {
        return shopCarouselMapService.updateShopCarouselMapEntityById(shopCarouselMap);
    }

    @ApiOperation(value = "查询全部")
    @GetMapping("shopCarouselMap/listByAll")
    @RequiresPermissions("shopCarouselMap:list")
    @LogAnnotation(title = "轮播图", action = "查询全部")
    @ResponseBody
    public DataResult findListByAll() {
        return DataResult.success(shopCarouselMapService.list());
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("shopCarouselMap/listByPage")
    @RequiresPermissions("shopCarouselMap:list")
    @LogAnnotation(title = "轮播图", action = "查询分页数据")
    @DataScope
    @ResponseBody
    public DataResult findListByPage(@RequestBody ShopCarouselMapEntity shopCarouselMap) {
        // 查询条件
        LambdaQueryWrapper<ShopCarouselMapEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper
                .eq(StringUtils.isNotBlank(shopCarouselMap.getId()), ShopCarouselMapEntity::getId, shopCarouselMap.getId())
                .apply(StringUtils.isNotBlank(shopCarouselMap.getCreateStartTime()), "UNIX_TIMESTAMP(create_time) >= UNIX_TIMESTAMP('" + shopCarouselMap.getCreateStartTime() + "')")
                .apply(StringUtils.isNotBlank(shopCarouselMap.getCreateEndTime()), "UNIX_TIMESTAMP(create_time) <= UNIX_TIMESTAMP('" + shopCarouselMap.getCreateEndTime() + "')")
                .orderByAsc(ShopCarouselMapEntity::getSeq);
        // 封装数据权限 - 执行查询 - 封装用户 - 响应前端
        return DataResult.success(encapsulationUser(shopCarouselMapService.page(new Page<>(shopCarouselMap.getPage(), shopCarouselMap.getLimit()), encapsulationDataRights(shopCarouselMap, queryWrapper, ShopCarouselMapEntity::getCreateId))));
    }

}
