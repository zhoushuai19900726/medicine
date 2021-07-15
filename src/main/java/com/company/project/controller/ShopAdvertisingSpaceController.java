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

import com.company.project.entity.ShopAdvertisingSpaceEntity;
import com.company.project.service.ShopAdvertisingSpaceService;

import javax.annotation.Resource;


/**
 * 广告位
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-07-15 09:28:30
 */
@Api(tags = "广告位")
@Controller
@RequestMapping("/")
public class ShopAdvertisingSpaceController extends BaseController {

    @Resource
    private ShopAdvertisingSpaceService shopAdvertisingSpaceService;


    @ApiOperation(value = "跳转到广告位列表页面")
    @GetMapping("/index/shopAdvertisingSpace")
    public String shopAdvertisingSpace() {
        return "advertisingspace/advertisingSpaceList";
    }

    @ApiOperation(value = "跳转进入新增/编辑页面")
    @GetMapping("/index/shopAdvertisingSpace/addOrUpdate")
    public String addOrUpdate() {
        return "advertisingspace/addOrUpdate";
    }

    @ApiOperation(value = "新增")
    @PostMapping("shopAdvertisingSpace/add")
    @RequiresPermissions("shopAdvertisingSpace:add")
    @LogAnnotation(title = "广告位", action = "新增")
    @ResponseBody
    public DataResult add(@RequestBody ShopAdvertisingSpaceEntity shopAdvertisingSpace) {
        return DataResult.success(shopAdvertisingSpaceService.save(shopAdvertisingSpace));
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("shopAdvertisingSpace/delete")
    @RequiresPermissions("shopAdvertisingSpace:delete")
    @LogAnnotation(title = "广告位", action = "删除")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids) {
        return DataResult.success(shopAdvertisingSpaceService.removeByIds(ids));
    }

    @ApiOperation(value = "更新")
    @PutMapping("shopAdvertisingSpace/update")
    @RequiresPermissions("shopAdvertisingSpace:update")
    @LogAnnotation(title = "广告位", action = "更新")
    @ResponseBody
    public DataResult update(@RequestBody ShopAdvertisingSpaceEntity shopAdvertisingSpace) {
        return DataResult.success(shopAdvertisingSpaceService.updateById(shopAdvertisingSpace));
    }

    @ApiOperation(value = "查询全部")
    @GetMapping("shopAdvertisingSpace/listByAll")
    @RequiresPermissions("shopAdvertisingSpace:list")
    @LogAnnotation(title = "广告位", action = "查询全部")
    @ResponseBody
    public DataResult findListByAll() {
        return DataResult.success(shopAdvertisingSpaceService.list());
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("shopAdvertisingSpace/listByPage")
    @RequiresPermissions("shopAdvertisingSpace:list")
    @LogAnnotation(title = "广告位", action = "查询分页数据")
    @DataScope
    @ResponseBody
    public DataResult findListByPage(@RequestBody ShopAdvertisingSpaceEntity shopAdvertisingSpace) {
        // 查询条件
        LambdaQueryWrapper<ShopAdvertisingSpaceEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper
                .eq(StringUtils.isNotBlank(shopAdvertisingSpace.getId()), ShopAdvertisingSpaceEntity::getId, shopAdvertisingSpace.getId())
                .like(StringUtils.isNotBlank(shopAdvertisingSpace.getName()), ShopAdvertisingSpaceEntity::getName, shopAdvertisingSpace.getName())
                .apply(StringUtils.isNotBlank(shopAdvertisingSpace.getCreateStartTime()), "UNIX_TIMESTAMP(create_time) >= UNIX_TIMESTAMP('" + shopAdvertisingSpace.getCreateStartTime() + "')")
                .apply(StringUtils.isNotBlank(shopAdvertisingSpace.getCreateEndTime()), "UNIX_TIMESTAMP(create_time) <= UNIX_TIMESTAMP('" + shopAdvertisingSpace.getCreateEndTime() + "')")
                .orderByAsc(ShopAdvertisingSpaceEntity::getSeq);
        // 封装数据权限 - 执行查询 - 封装用户 - 响应前端
        return DataResult.success(encapsulationUser(shopAdvertisingSpaceService.listByPage(new Page<>(shopAdvertisingSpace.getPage(), shopAdvertisingSpace.getLimit()), encapsulationDataRights(shopAdvertisingSpace, queryWrapper, ShopAdvertisingSpaceEntity::getCreateId))));
    }

}

