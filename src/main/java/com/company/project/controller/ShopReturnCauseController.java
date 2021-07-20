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

import com.company.project.entity.ShopReturnCauseEntity;
import com.company.project.service.ShopReturnCauseService;

import javax.annotation.Resource;


/**
 * 退货原因
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-07-20 14:14:36
 */
@Api(tags = "退货原因")
@Controller
@RequestMapping("/")
public class ShopReturnCauseController extends BaseController {

    @Resource
    private ShopReturnCauseService shopReturnCauseService;


    @ApiOperation(value = "跳转到退货原因列表页面")
    @GetMapping("/index/shopReturnCause")
    public String shopReturnCause() {
        return "returncause/returnCauseList";
    }

    @ApiOperation(value = "跳转进入新增/编辑页面")
    @GetMapping("/index/shopReturnCause/addOrUpdate")
    public String addOrUpdate() {
        return "returncause/addOrUpdate";
    }

    @ApiOperation(value = "新增")
    @PostMapping("shopReturnCause/add")
    @RequiresPermissions("shopReturnCause:add")
    @LogAnnotation(title = "退货原因", action = "新增")
    @ResponseBody
    public DataResult add(@RequestBody ShopReturnCauseEntity shopReturnCause) {
        return shopReturnCauseService.saveShopReturnCauseEntity(shopReturnCause);
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("shopReturnCause/delete")
    @RequiresPermissions("shopReturnCause:delete")
    @LogAnnotation(title = "退货原因", action = "删除")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids) {
        return shopReturnCauseService.removeShopReturnCauseEntityByIds(ids);
    }

    @ApiOperation(value = "更新")
    @PutMapping("shopReturnCause/update")
    @RequiresPermissions("shopReturnCause:update")
    @LogAnnotation(title = "退货原因", action = "更新")
    @ResponseBody
    public DataResult update(@RequestBody ShopReturnCauseEntity shopReturnCause) {
        return shopReturnCauseService.updateShopReturnCauseEntityById(shopReturnCause);
    }

    @ApiOperation(value = "查询全部")
    @GetMapping("shopReturnCause/listByAll")
    @RequiresPermissions("shopReturnCause:list")
    @LogAnnotation(title = "退货原因", action = "查询全部")
    @ResponseBody
    public DataResult findListByAll() {
        return DataResult.success(shopReturnCauseService.list());
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("shopReturnCause/listByPage")
    @RequiresPermissions("shopReturnCause:list")
    @LogAnnotation(title = "退货原因", action = "查询分页数据")
    @DataScope
    @ResponseBody
    public DataResult findListByPage(@RequestBody ShopReturnCauseEntity shopReturnCause) {
        // 查询条件
        LambdaQueryWrapper<ShopReturnCauseEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper
                .eq(StringUtils.isNotBlank(shopReturnCause.getId()), ShopReturnCauseEntity::getId, shopReturnCause.getId())
                .like(StringUtils.isNotBlank(shopReturnCause.getCause()), ShopReturnCauseEntity::getCause, shopReturnCause.getCause())
                .apply(StringUtils.isNotBlank(shopReturnCause.getCreateStartTime()), "UNIX_TIMESTAMP(create_time) >= UNIX_TIMESTAMP('" + shopReturnCause.getCreateStartTime() + "')")
                .apply(StringUtils.isNotBlank(shopReturnCause.getCreateEndTime()), "UNIX_TIMESTAMP(create_time) <= UNIX_TIMESTAMP('" + shopReturnCause.getCreateEndTime() + "')")
                .orderByAsc(ShopReturnCauseEntity::getSeq);
        // 封装数据权限 - 执行查询 - 封装用户 - 响应前端
        return DataResult.success(encapsulationUser(shopReturnCauseService.page(new Page<>(shopReturnCause.getPage(), shopReturnCause.getLimit()), encapsulationDataRights(shopReturnCause, queryWrapper, ShopReturnCauseEntity::getCreateId))));
    }

}
