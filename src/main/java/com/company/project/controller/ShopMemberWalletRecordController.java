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

import com.company.project.entity.ShopMemberWalletRecordEntity;
import com.company.project.service.ShopMemberWalletRecordService;

import javax.annotation.Resource;


/**
 * 会员钱包
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-07-09 17:10:37
 */
@Api(tags = "会员钱包")
@Controller
@RequestMapping("/")
public class ShopMemberWalletRecordController extends BaseController {

    @Resource
    private ShopMemberWalletRecordService shopMemberWalletRecordService;


    @ApiOperation(value = "跳转到会员钱包列表页面")
    @GetMapping("/index/shopMemberWalletRecord")
    public String shopMemberWalletRecord() {
        return "shopmemberwalletrecord/list";
        }

    @ApiOperation(value = "跳转进入新增/编辑页面")
    @GetMapping("/index/shopMemberWalletRecord/addOrUpdate")
    public String addOrUpdate() {
        return "shopmemberwalletrecord/addOrUpdate";
    }

    @ApiOperation(value = "新增")
    @PostMapping("shopMemberWalletRecord/add")
    @RequiresPermissions("shopMemberWalletRecord:add")
    @LogAnnotation(title = "会员钱包", action = "新增")
    @ResponseBody
    public DataResult add(@RequestBody ShopMemberWalletRecordEntity shopMemberWalletRecord){
        return DataResult.success(shopMemberWalletRecordService.save(shopMemberWalletRecord));
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("shopMemberWalletRecord/delete")
    @RequiresPermissions("shopMemberWalletRecord:delete")
    @LogAnnotation(title = "会员钱包", action = "删除")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids){
        return DataResult.success(shopMemberWalletRecordService.removeByIds(ids));
    }

    @ApiOperation(value = "更新")
    @PutMapping("shopMemberWalletRecord/update")
    @RequiresPermissions("shopMemberWalletRecord:update")
    @LogAnnotation(title = "会员钱包", action = "更新")
    @ResponseBody
    public DataResult update(@RequestBody ShopMemberWalletRecordEntity shopMemberWalletRecord){
        return DataResult.success(shopMemberWalletRecordService.updateById(shopMemberWalletRecord));
    }

    @ApiOperation(value = "查询全部")
    @GetMapping("shopMemberWalletRecord/listByAll")
    @RequiresPermissions("shopMemberWalletRecord:list")
    @LogAnnotation(title = "会员钱包", action = "查询全部")
    @ResponseBody
    public DataResult findListByAll() {
        return DataResult.success(shopMemberWalletRecordService.list());
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("shopMemberWalletRecord/listByPage")
    @RequiresPermissions("shopMemberWalletRecord:list")
    @LogAnnotation(title = "会员钱包", action = "查询分页数据")
    @DataScope
    @ResponseBody
    public DataResult findListByPage(@RequestBody ShopMemberWalletRecordEntity shopMemberWalletRecord){
        // 查询条件
        LambdaQueryWrapper<ShopMemberWalletRecordEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper
                .eq(StringUtils.isNotBlank(shopMemberWalletRecord.getId()), ShopMemberWalletRecordEntity::getId, shopMemberWalletRecord.getId())
                .orderByDesc(ShopMemberWalletRecordEntity::getCreateTime);
        // 封装数据权限 - 执行查询 - 封装用户 - 响应前端
        return DataResult.success(encapsulationUser(shopMemberWalletRecordService.page(new Page<>(shopMemberWalletRecord.getPage(), shopMemberWalletRecord.getLimit()), encapsulationDataRights(shopMemberWalletRecord, queryWrapper, ShopMemberWalletRecordEntity::getCreateId))));
    }

}
