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

import com.company.project.entity.ShopMemberGrowthValueEntity;
import com.company.project.service.ShopMemberGrowthValueService;

import javax.annotation.Resource;


/**
 * 会员成长值
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-07-09 17:10:25
 */
@Api(tags = "会员成长值")
@Controller
@RequestMapping("/")
public class ShopMemberGrowthValueController extends BaseController {

    @Resource
    private ShopMemberGrowthValueService shopMemberGrowthValueService;


    @ApiOperation(value = "跳转到会员成长值列表页面")
    @GetMapping("/index/shopMemberGrowthValue")
    public String shopMemberGrowthValue() {
        return "shopmembergrowthvalue/list";
        }

    @ApiOperation(value = "跳转进入新增/编辑页面")
    @GetMapping("/index/shopMemberGrowthValue/addOrUpdate")
    public String addOrUpdate() {
        return "shopmembergrowthvalue/addOrUpdate";
    }

    @ApiOperation(value = "新增")
    @PostMapping("shopMemberGrowthValue/add")
    @RequiresPermissions("shopMemberGrowthValue:add")
    @LogAnnotation(title = "会员成长值", action = "新增")
    @ResponseBody
    public DataResult add(@RequestBody ShopMemberGrowthValueEntity shopMemberGrowthValue){
        return DataResult.success(shopMemberGrowthValueService.save(shopMemberGrowthValue));
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("shopMemberGrowthValue/delete")
    @RequiresPermissions("shopMemberGrowthValue:delete")
    @LogAnnotation(title = "会员成长值", action = "删除")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids){
        return DataResult.success(shopMemberGrowthValueService.removeByIds(ids));
    }

    @ApiOperation(value = "更新")
    @PutMapping("shopMemberGrowthValue/update")
    @RequiresPermissions("shopMemberGrowthValue:update")
    @LogAnnotation(title = "会员成长值", action = "更新")
    @ResponseBody
    public DataResult update(@RequestBody ShopMemberGrowthValueEntity shopMemberGrowthValue){
        return DataResult.success(shopMemberGrowthValueService.updateById(shopMemberGrowthValue));
    }

    @ApiOperation(value = "查询全部")
    @GetMapping("shopMemberGrowthValue/listByAll")
    @RequiresPermissions("shopMemberGrowthValue:list")
    @LogAnnotation(title = "会员成长值", action = "查询全部")
    @ResponseBody
    public DataResult findListByAll() {
        return DataResult.success(shopMemberGrowthValueService.list());
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("shopMemberGrowthValue/listByPage")
    @RequiresPermissions("shopMemberGrowthValue:list")
    @LogAnnotation(title = "会员成长值", action = "查询分页数据")
    @DataScope
    @ResponseBody
    public DataResult findListByPage(@RequestBody ShopMemberGrowthValueEntity shopMemberGrowthValue){
        // 查询条件
        LambdaQueryWrapper<ShopMemberGrowthValueEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper
                .eq(StringUtils.isNotBlank(shopMemberGrowthValue.getId()), ShopMemberGrowthValueEntity::getId, shopMemberGrowthValue.getId())
                .orderByDesc(ShopMemberGrowthValueEntity::getCreateTime);
        // 封装数据权限 - 执行查询 - 封装用户 - 响应前端
        return DataResult.success(encapsulationUser(shopMemberGrowthValueService.page(new Page<>(shopMemberGrowthValue.getPage(), shopMemberGrowthValue.getLimit()), encapsulationDataRights(shopMemberGrowthValue, queryWrapper, ShopMemberGrowthValueEntity::getCreateId))));
    }

}
