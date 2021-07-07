package com.company.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.company.project.common.aop.annotation.DataScope;
import com.company.project.common.aop.annotation.LogAnnotation;
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

import com.company.project.entity.ShopMemberGradeEntity;
import com.company.project.service.ShopMemberGradeService;

import javax.annotation.Resource;


/**
 * 会员等级
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-07-07 15:30:45
 */
@Controller
@RequestMapping("/")
public class ShopMemberGradeController extends BaseController {

    @Resource
    private ShopMemberGradeService shopMemberGradeService;

    @ApiOperation(value = "跳转到会员等级列表页面")
    @GetMapping("/index/shopMemberGrade")
    public String shopMemberGrade() {
        return "membergrade/memberGradeList";
        }

    @ApiOperation(value = "跳转进入新增/编辑页面")
    @GetMapping("/index/shopMemberGrade/addOrUpdate")
    public String addOrUpdate() {
        return "membergrade/addOrUpdate";
    }

    @ApiOperation(value = "新增")
    @PostMapping("shopMemberGrade/add")
    @RequiresPermissions("shopMemberGrade:add")
    @LogAnnotation(title = "会员等级", action = "新增")
    @ResponseBody
    public DataResult add(@RequestBody ShopMemberGradeEntity shopMemberGrade){
        return shopMemberGradeService.saveShopMemberGradeEntity(shopMemberGrade);
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("shopMemberGrade/delete")
    @RequiresPermissions("shopMemberGrade:delete")
    @LogAnnotation(title = "会员等级", action = "删除")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids){
        return DataResult.success(shopMemberGradeService.removeByIds(ids));
    }

    @ApiOperation(value = "更新")
    @PutMapping("shopMemberGrade/update")
    @RequiresPermissions("shopMemberGrade:update")
    @LogAnnotation(title = "会员等级", action = "更新")
    @ResponseBody
    public DataResult update(@RequestBody ShopMemberGradeEntity shopMemberGrade){
        return shopMemberGradeService.updateShopMemberGradeEntityById(shopMemberGrade);
    }

    @ApiOperation(value = "查询全部")
    @GetMapping("shopMemberGrade/listByAll")
    @RequiresPermissions("shopMemberGrade:list")
    @LogAnnotation(title = "会员等级", action = "查询全部")
    @ResponseBody
    public DataResult findListByAll() {
        return DataResult.success(shopMemberGradeService.list());
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("shopMemberGrade/listByPage")
    @RequiresPermissions("shopMemberGrade:list")
    @LogAnnotation(title = "会员等级", action = "查询分页数据")
    @DataScope
    @ResponseBody
    public DataResult findListByPage(@RequestBody ShopMemberGradeEntity shopMemberGrade){
        // 分页初始化
        Page<ShopMemberGradeEntity> page = new Page<>(shopMemberGrade.getPage(), shopMemberGrade.getLimit());
        // 查询条件
        LambdaQueryWrapper<ShopMemberGradeEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper
                .eq(StringUtils.isNotBlank(shopMemberGrade.getId()), ShopMemberGradeEntity::getId, shopMemberGrade.getId())
                .orderByAsc(ShopMemberGradeEntity::getIntegration);
        // 封装数据权限 - 执行查询 - 封装用户 - 响应前端
        return DataResult.success(encapsulationUser(shopMemberGradeService.page(page, encapsulationDataRights(shopMemberGrade, queryWrapper, ShopMemberGradeEntity::getCreateId))));
    }

}
