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

import com.company.project.entity.ShopMemberGrowthValueRecordEntity;
import com.company.project.service.ShopMemberGrowthValueRecordService;

import javax.annotation.Resource;


/**
 * 会员成长值记录
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-07-09 17:10:36
 */
@Api(tags = "会员成长值记录")
@Controller
@RequestMapping("/")
public class ShopMemberGrowthValueRecordController extends BaseController {

    @Resource
    private ShopMemberGrowthValueRecordService shopMemberGrowthValueRecordService;


    @ApiOperation(value = "跳转到会员成长值记录列表页面")
    @GetMapping("/index/shopMemberGrowthValueRecord")
    public String shopMemberGrowthValueRecord() {
        return "shopmembergrowthvaluerecord/list";
        }

    @ApiOperation(value = "跳转进入新增/编辑页面")
    @GetMapping("/index/shopMemberGrowthValueRecord/addOrUpdate")
    public String addOrUpdate() {
        return "shopmembergrowthvaluerecord/addOrUpdate";
    }

    @ApiOperation(value = "新增")
    @PostMapping("shopMemberGrowthValueRecord/add")
    @RequiresPermissions("shopMemberGrowthValueRecord:add")
    @LogAnnotation(title = "会员成长值记录", action = "新增")
    @ResponseBody
    public DataResult add(@RequestBody ShopMemberGrowthValueRecordEntity shopMemberGrowthValueRecord){
        return DataResult.success(shopMemberGrowthValueRecordService.save(shopMemberGrowthValueRecord));
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("shopMemberGrowthValueRecord/delete")
    @RequiresPermissions("shopMemberGrowthValueRecord:delete")
    @LogAnnotation(title = "会员成长值记录", action = "删除")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids){
        return DataResult.success(shopMemberGrowthValueRecordService.removeByIds(ids));
    }

    @ApiOperation(value = "更新")
    @PutMapping("shopMemberGrowthValueRecord/update")
    @RequiresPermissions("shopMemberGrowthValueRecord:update")
    @LogAnnotation(title = "会员成长值记录", action = "更新")
    @ResponseBody
    public DataResult update(@RequestBody ShopMemberGrowthValueRecordEntity shopMemberGrowthValueRecord){
        return DataResult.success(shopMemberGrowthValueRecordService.updateById(shopMemberGrowthValueRecord));
    }

    @ApiOperation(value = "查询全部")
    @GetMapping("shopMemberGrowthValueRecord/listByAll")
    @RequiresPermissions("shopMemberGrowthValueRecord:list")
    @LogAnnotation(title = "会员成长值记录", action = "查询全部")
    @ResponseBody
    public DataResult findListByAll() {
        return DataResult.success(shopMemberGrowthValueRecordService.list());
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("shopMemberGrowthValueRecord/listByPage")
    @RequiresPermissions("shopMemberGrowthValueRecord:list")
    @LogAnnotation(title = "会员成长值记录", action = "查询分页数据")
    @DataScope
    @ResponseBody
    public DataResult findListByPage(@RequestBody ShopMemberGrowthValueRecordEntity shopMemberGrowthValueRecord){
        // 查询条件
        LambdaQueryWrapper<ShopMemberGrowthValueRecordEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper
                .eq(StringUtils.isNotBlank(shopMemberGrowthValueRecord.getId()), ShopMemberGrowthValueRecordEntity::getId, shopMemberGrowthValueRecord.getId())
                .orderByDesc(ShopMemberGrowthValueRecordEntity::getCreateTime);
        // 封装数据权限 - 执行查询 - 封装用户 - 响应前端
        return DataResult.success(encapsulationUser(shopMemberGrowthValueRecordService.page(new Page<>(shopMemberGrowthValueRecord.getPage(), shopMemberGrowthValueRecord.getLimit()), encapsulationDataRights(shopMemberGrowthValueRecord, queryWrapper, ShopMemberGrowthValueRecordEntity::getCreateId))));
    }

}
