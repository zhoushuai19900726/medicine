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

import com.company.project.entity.ShopBannerEntity;
import com.company.project.service.ShopBannerService;

import javax.annotation.Resource;


/**
 * Banner导航
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-07-16 09:10:18
 */
@Api(tags = "Banner导航")
@Controller
@RequestMapping("/")
public class ShopBannerController extends BaseController {

    @Resource
    private ShopBannerService shopBannerService;


    @ApiOperation(value = "跳转到Banner导航列表页面")
    @GetMapping("/index/shopBanner")
    public String shopBanner() {
        return "banner/bannerList";
    }

    @ApiOperation(value = "跳转进入新增/编辑页面")
    @GetMapping("/index/shopBanner/addOrUpdate")
    public String addOrUpdate() {
        return "banner/addOrUpdate";
    }

    @ApiOperation(value = "新增")
    @PostMapping("shopBanner/add")
    @RequiresPermissions("shopBanner:add")
    @LogAnnotation(title = "Banner导航", action = "新增")
    @ResponseBody
    public DataResult add(@RequestBody ShopBannerEntity shopBanner) {
        return shopBannerService.saveShopBannerEntity(shopBanner);
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("shopBanner/delete")
    @RequiresPermissions("shopBanner:delete")
    @LogAnnotation(title = "Banner导航", action = "删除")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids) {
        return shopBannerService.removeShopBannerEntityByIds(ids);
    }

    @ApiOperation(value = "更新")
    @PutMapping("shopBanner/update")
    @RequiresPermissions("shopBanner:update")
    @LogAnnotation(title = "Banner导航", action = "更新")
    @ResponseBody
    public DataResult update(@RequestBody ShopBannerEntity shopBanner) {
        return shopBannerService.updateShopBannerEntityById(shopBanner);
    }

    @ApiOperation(value = "查询全部")
    @GetMapping("shopBanner/listByAll")
    @RequiresPermissions("shopBanner:list")
    @LogAnnotation(title = "Banner导航", action = "查询全部")
    @ResponseBody
    public DataResult findListByAll() {
        return DataResult.success(shopBannerService.list());
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("shopBanner/listByPage")
    @RequiresPermissions("shopBanner:list")
    @LogAnnotation(title = "Banner导航", action = "查询分页数据")
    @DataScope
    @ResponseBody
    public DataResult findListByPage(@RequestBody ShopBannerEntity shopBanner) {
        // 查询条件
        LambdaQueryWrapper<ShopBannerEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper
                .eq(StringUtils.isNotBlank(shopBanner.getId()), ShopBannerEntity::getId, shopBanner.getId())
                .like(StringUtils.isNotBlank(shopBanner.getName()), ShopBannerEntity::getName, shopBanner.getName())
                .apply(StringUtils.isNotBlank(shopBanner.getCreateStartTime()), "UNIX_TIMESTAMP(create_time) >= UNIX_TIMESTAMP('" + shopBanner.getCreateStartTime() + "')")
                .apply(StringUtils.isNotBlank(shopBanner.getCreateEndTime()), "UNIX_TIMESTAMP(create_time) <= UNIX_TIMESTAMP('" + shopBanner.getCreateEndTime() + "')")
                .orderByAsc(ShopBannerEntity::getSeq);
        // 封装数据权限 - 执行查询 - 封装用户 - 响应前端
        return DataResult.success(encapsulationUser(shopBannerService.page(new Page<>(shopBanner.getPage(), shopBanner.getLimit()), encapsulationDataRights(shopBanner, queryWrapper, ShopBannerEntity::getCreateId))));
    }

}
