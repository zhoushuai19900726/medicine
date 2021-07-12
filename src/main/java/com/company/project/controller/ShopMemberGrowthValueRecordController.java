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

    @ApiOperation(value = "删除")
    @DeleteMapping("shopMemberGrowthValueRecord/delete")
    @RequiresPermissions("shopMember:delete")
    @LogAnnotation(title = "会员成长值记录", action = "删除")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids){
        return DataResult.success(shopMemberGrowthValueRecordService.removeByIds(ids));
    }

    @ApiOperation(value = "更新")
    @PutMapping("shopMemberGrowthValueRecord/update")
    @RequiresPermissions("shopMember:update")
    @LogAnnotation(title = "会员成长值记录", action = "更新")
    @ResponseBody
    public DataResult update(@RequestBody ShopMemberGrowthValueRecordEntity shopMemberGrowthValueRecord){
        return DataResult.success(shopMemberGrowthValueRecordService.updateById(shopMemberGrowthValueRecord));
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("shopMemberGrowthValueRecord/listByPage")
    @RequiresPermissions("shopMember:list")
    @LogAnnotation(title = "会员成长值记录", action = "查询分页数据")
    @DataScope
    @ResponseBody
    public DataResult findListByPage(@RequestBody ShopMemberGrowthValueRecordEntity shopMemberGrowthValueRecord){
        // 查询条件
        LambdaQueryWrapper<ShopMemberGrowthValueRecordEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper
                .eq(ShopMemberGrowthValueRecordEntity::getMemberId, shopMemberGrowthValueRecord.getMemberId())
                .like(StringUtils.isNotBlank(shopMemberGrowthValueRecord.getRemark()), ShopMemberGrowthValueRecordEntity::getRemark, shopMemberGrowthValueRecord.getRemark())
                .orderByDesc(ShopMemberGrowthValueRecordEntity::getCreateTime);
        return DataResult.success(shopMemberGrowthValueRecordService.listByPage(new Page<>(shopMemberGrowthValueRecord.getPage(), shopMemberGrowthValueRecord.getLimit()), queryWrapper));
    }

}
