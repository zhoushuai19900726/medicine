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

import com.company.project.entity.ShopTransportEntity;
import com.company.project.service.ShopTransportService;

import javax.annotation.Resource;


/**
 * 运费模板
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-07-20 15:42:47
 */
@Api(tags = "运费模板")
@Controller
@RequestMapping("/")
public class ShopTransportController extends BaseController {

    @Resource
    private ShopTransportService shopTransportService;


    @ApiOperation(value = "跳转到运费模板列表页面")
    @GetMapping("/index/shopTransport")
    public String shopTransport() {
        return "transport/transportList";
    }

    @ApiOperation(value = "跳转进入新增/编辑页面")
    @GetMapping("/index/shopTransport/addOrUpdate")
    public String addOrUpdate() {
        return "transport/addOrUpdate";
    }

    @ApiOperation(value = "跳转进入选择指定地区页面")
    @GetMapping("/index/shopTransport/specifiedRegion")
    public String specifiedRegion() {
        return "transport/specifiedRegion";
    }

    @ApiOperation(value = "新增")
    @PostMapping("shopTransport/add")
    @RequiresPermissions("shopTransport:add")
    @LogAnnotation(title = "运费模板", action = "新增")
    @ResponseBody
    public DataResult add(@RequestBody ShopTransportEntity shopTransport) {
        return shopTransportService.saveShopTransportEntity(shopTransport);
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("shopTransport/delete")
    @RequiresPermissions("shopTransport:delete")
    @LogAnnotation(title = "运费模板", action = "删除")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids) {
        return shopTransportService.removeShopTransportEntityByIds(ids);
    }

    @ApiOperation(value = "更新")
    @PutMapping("shopTransport/update")
    @RequiresPermissions("shopTransport:update")
    @LogAnnotation(title = "运费模板", action = "更新")
    @ResponseBody
    public DataResult update(@RequestBody ShopTransportEntity shopTransport) {
        return shopTransportService.updateShopTransportEntityById(shopTransport);
    }

    @ApiOperation(value = "查询全部")
    @GetMapping("shopTransport/listByAll")
    @RequiresPermissions("shopTransport:list")
    @LogAnnotation(title = "运费模板", action = "查询全部")
    @ResponseBody
    public DataResult findListByAll() {
        return DataResult.success(shopTransportService.list());
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("shopTransport/listByPage")
    @RequiresPermissions("shopTransport:list")
    @LogAnnotation(title = "运费模板", action = "查询分页数据")
    @DataScope
    @ResponseBody
    public DataResult findListByPage(@RequestBody ShopTransportEntity shopTransport) {
        // 查询条件
        LambdaQueryWrapper<ShopTransportEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper
                .eq(StringUtils.isNotBlank(shopTransport.getId()), ShopTransportEntity::getId, shopTransport.getId())
                .like(StringUtils.isNotBlank(shopTransport.getName()), ShopTransportEntity::getName, shopTransport.getName())
                .apply(StringUtils.isNotBlank(shopTransport.getCreateStartTime()), "UNIX_TIMESTAMP(create_time) >= UNIX_TIMESTAMP('" + shopTransport.getCreateStartTime() + "')")
                .apply(StringUtils.isNotBlank(shopTransport.getCreateEndTime()), "UNIX_TIMESTAMP(create_time) <= UNIX_TIMESTAMP('" + shopTransport.getCreateEndTime() + "')")
                .orderByDesc(ShopTransportEntity::getCreateTime);
        // 封装数据权限 - 执行查询 - 封装用户 - 响应前端
        return DataResult.success(encapsulationUser(shopTransportService.listByPage(new Page<>(shopTransport.getPage(), shopTransport.getLimit()), encapsulationDataRights(shopTransport, queryWrapper, ShopTransportEntity::getCreateId))));
    }

}
