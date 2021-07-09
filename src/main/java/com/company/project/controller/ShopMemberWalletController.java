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

import com.company.project.entity.ShopMemberWalletEntity;
import com.company.project.service.ShopMemberWalletService;

import javax.annotation.Resource;


/**
 * 会员钱包
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-07-09 17:10:27
 */
@Api(tags = "会员钱包")
@Controller
@RequestMapping("/")
public class ShopMemberWalletController extends BaseController {

    @Resource
    private ShopMemberWalletService shopMemberWalletService;


    @ApiOperation(value = "跳转到会员钱包列表页面")
    @GetMapping("/index/shopMemberWallet")
    public String shopMemberWallet() {
        return "shopmemberwallet/list";
        }

    @ApiOperation(value = "跳转进入新增/编辑页面")
    @GetMapping("/index/shopMemberWallet/addOrUpdate")
    public String addOrUpdate() {
        return "shopmemberwallet/addOrUpdate";
    }

    @ApiOperation(value = "新增")
    @PostMapping("shopMemberWallet/add")
    @RequiresPermissions("shopMemberWallet:add")
    @LogAnnotation(title = "会员钱包", action = "新增")
    @ResponseBody
    public DataResult add(@RequestBody ShopMemberWalletEntity shopMemberWallet){
        return DataResult.success(shopMemberWalletService.save(shopMemberWallet));
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("shopMemberWallet/delete")
    @RequiresPermissions("shopMemberWallet:delete")
    @LogAnnotation(title = "会员钱包", action = "删除")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids){
        return DataResult.success(shopMemberWalletService.removeByIds(ids));
    }

    @ApiOperation(value = "更新")
    @PutMapping("shopMemberWallet/update")
    @RequiresPermissions("shopMemberWallet:update")
    @LogAnnotation(title = "会员钱包", action = "更新")
    @ResponseBody
    public DataResult update(@RequestBody ShopMemberWalletEntity shopMemberWallet){
        return DataResult.success(shopMemberWalletService.updateById(shopMemberWallet));
    }

    @ApiOperation(value = "查询全部")
    @GetMapping("shopMemberWallet/listByAll")
    @RequiresPermissions("shopMemberWallet:list")
    @LogAnnotation(title = "会员钱包", action = "查询全部")
    @ResponseBody
    public DataResult findListByAll() {
        return DataResult.success(shopMemberWalletService.list());
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("shopMemberWallet/listByPage")
    @RequiresPermissions("shopMemberWallet:list")
    @LogAnnotation(title = "会员钱包", action = "查询分页数据")
    @DataScope
    @ResponseBody
    public DataResult findListByPage(@RequestBody ShopMemberWalletEntity shopMemberWallet){
        // 查询条件
        LambdaQueryWrapper<ShopMemberWalletEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper
                .eq(StringUtils.isNotBlank(shopMemberWallet.getId()), ShopMemberWalletEntity::getId, shopMemberWallet.getId())
                .orderByDesc(ShopMemberWalletEntity::getCreateTime);
        // 封装数据权限 - 执行查询 - 封装用户 - 响应前端
        return DataResult.success(encapsulationUser(shopMemberWalletService.page(new Page<>(shopMemberWallet.getPage(), shopMemberWallet.getLimit()), encapsulationDataRights(shopMemberWallet, queryWrapper, ShopMemberWalletEntity::getCreateId))));
    }

}
