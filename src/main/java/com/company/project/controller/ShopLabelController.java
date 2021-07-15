package com.company.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.company.project.common.aop.annotation.DataScope;
import com.company.project.common.aop.annotation.LogAnnotation;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.commons.lang.StringUtils;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.List;

import com.company.project.common.utils.DataResult;

import com.company.project.entity.ShopLabelEntity;
import com.company.project.service.ShopLabelService;

import javax.annotation.Resource;


/**
 * 标签
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-07-15 16:39:45
 */
@Api(tags = "标签")
@Controller
@RequestMapping("/")
public class ShopLabelController extends BaseController {

    @Resource
    private ShopLabelService shopLabelService;


    @ApiOperation(value = "跳转到标签列表页面")
    @GetMapping("/index/shopLabel")
    public String shopLabel() {
        return "label/labelList";
    }

    @ApiOperation(value = "跳转进入新增/编辑页面")
    @GetMapping("/index/shopLabel/addOrUpdate")
    public String addOrUpdate() {
        return "label/addOrUpdate";
    }

    @ApiOperation(value = "新增")
    @PostMapping("shopLabel/add")
    @RequiresPermissions("shopLabel:add")
    @LogAnnotation(title = "标签", action = "新增")
    @ResponseBody
    public DataResult add(@RequestBody ShopLabelEntity shopLabel) {
        return DataResult.success(shopLabelService.save(shopLabel));
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("shopLabel/delete")
    @RequiresPermissions("shopLabel:delete")
    @LogAnnotation(title = "标签", action = "删除")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids) {
        return DataResult.success(shopLabelService.removeByIds(ids));
    }

    @ApiOperation(value = "更新")
    @PutMapping("shopLabel/update")
    @RequiresPermissions("shopLabel:update")
    @LogAnnotation(title = "标签", action = "更新")
    @ResponseBody
    public DataResult update(@RequestBody ShopLabelEntity shopLabel) {
        return DataResult.success(shopLabelService.updateById(shopLabel));
    }

    @ApiOperation(value = "查询全部")
    @GetMapping("shopLabel/listByAll")
    @RequiresPermissions("shopLabel:list")
    @LogAnnotation(title = "标签", action = "查询全部")
    @ResponseBody
    public DataResult findListByAll() {
        return DataResult.success(shopLabelService.list());
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("shopLabel/listByPage")
    @RequiresPermissions("shopLabel:list")
    @LogAnnotation(title = "标签", action = "查询分页数据")
    @DataScope
    @ResponseBody
    public DataResult findListByPage(@RequestBody ShopLabelEntity shopLabel) {
        // 查询条件
        LambdaQueryWrapper<ShopLabelEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper
                .eq(StringUtils.isNotBlank(shopLabel.getId()), ShopLabelEntity::getId, shopLabel.getId())
                .like(StringUtils.isNotBlank(shopLabel.getName()), ShopLabelEntity::getName, shopLabel.getName())
                .apply(StringUtils.isNotBlank(shopLabel.getCreateStartTime()), "UNIX_TIMESTAMP(create_time) >= UNIX_TIMESTAMP('" + shopLabel.getCreateStartTime() + "')")
                .apply(StringUtils.isNotBlank(shopLabel.getCreateEndTime()), "UNIX_TIMESTAMP(create_time) <= UNIX_TIMESTAMP('" + shopLabel.getCreateEndTime() + "')")
                .orderByAsc(ShopLabelEntity::getSeq);
        // 封装数据权限 - 执行查询 - 封装用户 - 响应前端
        return DataResult.success(encapsulationUser(shopLabelService.page(new Page<>(shopLabel.getPage(), shopLabel.getLimit()), encapsulationDataRights(shopLabel, queryWrapper, ShopLabelEntity::getCreateId))));
    }

}
