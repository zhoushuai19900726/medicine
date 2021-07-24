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

import com.company.project.entity.ShopReturnOrderDetailEntity;
import com.company.project.service.ShopReturnOrderDetailService;

import javax.annotation.Resource;


/**
 * 退款申请明细
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-07-24 09:53:00
 */
@Api(tags = "退款申请明细")
@Controller
@RequestMapping("/")
public class ShopReturnOrderDetailController extends BaseController {

    @Resource
    private ShopReturnOrderDetailService shopReturnOrderDetailService;


//    @ApiOperation(value = "跳转到退款申请明细列表页面")
//    @GetMapping("/index/shopReturnOrderDetail")
//    public String shopReturnOrderDetail() {
//        return "shopreturnorderdetail/list";
//        }
//
//    @ApiOperation(value = "跳转进入新增/编辑页面")
//    @GetMapping("/index/shopReturnOrderDetail/addOrUpdate")
//    public String addOrUpdate() {
//        return "shopreturnorderdetail/addOrUpdate";
//    }
//
//    @ApiOperation(value = "新增")
//    @PostMapping("shopReturnOrderDetail/add")
//    @RequiresPermissions("shopReturnOrderDetail:add")
//    @LogAnnotation(title = "退款申请明细", action = "新增")
//    @ResponseBody
//    public DataResult add(@RequestBody ShopReturnOrderDetailEntity shopReturnOrderDetail){
//        return DataResult.success(shopReturnOrderDetailService.save(shopReturnOrderDetail));
//    }
//
//    @ApiOperation(value = "删除")
//    @DeleteMapping("shopReturnOrderDetail/delete")
//    @RequiresPermissions("shopReturnOrderDetail:delete")
//    @LogAnnotation(title = "退款申请明细", action = "删除")
//    @ResponseBody
//    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids){
//        return DataResult.success(shopReturnOrderDetailService.removeByIds(ids));
//    }
//
//    @ApiOperation(value = "更新")
//    @PutMapping("shopReturnOrderDetail/update")
//    @RequiresPermissions("shopReturnOrderDetail:update")
//    @LogAnnotation(title = "退款申请明细", action = "更新")
//    @ResponseBody
//    public DataResult update(@RequestBody ShopReturnOrderDetailEntity shopReturnOrderDetail){
//        return DataResult.success(shopReturnOrderDetailService.updateById(shopReturnOrderDetail));
//    }
//
//    @ApiOperation(value = "查询全部")
//    @GetMapping("shopReturnOrderDetail/listByAll")
//    @RequiresPermissions("shopReturnOrderDetail:list")
//    @LogAnnotation(title = "退款申请明细", action = "查询全部")
//    @ResponseBody
//    public DataResult findListByAll() {
//        return DataResult.success(shopReturnOrderDetailService.list());
//    }
//
//    @ApiOperation(value = "查询分页数据")
//    @PostMapping("shopReturnOrderDetail/listByPage")
//    @RequiresPermissions("shopReturnOrderDetail:list")
//    @LogAnnotation(title = "退款申请明细", action = "查询分页数据")
//    @DataScope
//    @ResponseBody
//    public DataResult findListByPage(@RequestBody ShopReturnOrderDetailEntity shopReturnOrderDetail){
//        // 查询条件
//        LambdaQueryWrapper<ShopReturnOrderDetailEntity> queryWrapper = Wrappers.lambdaQuery();
//        queryWrapper
//                .eq(StringUtils.isNotBlank(shopReturnOrderDetail.getId()), ShopReturnOrderDetailEntity::getId, shopReturnOrderDetail.getId())
//        // .apply(StringUtils.isNotBlank(shopReturnOrderDetail.getCreateStartTime()), "UNIX_TIMESTAMP(create_time) >= UNIX_TIMESTAMP('" + shopReturnOrderDetail.getCreateStartTime() + "')")
//        // .apply(StringUtils.isNotBlank(shopReturnOrderDetail.getCreateEndTime()), "UNIX_TIMESTAMP(create_time) <= UNIX_TIMESTAMP('" + shopReturnOrderDetail.getCreateEndTime() + "')")
//                .orderByDesc(ShopReturnOrderDetailEntity::getCreateTime);
//        // 封装数据权限 - 执行查询 - 封装用户 - 响应前端
//        return DataResult.success(encapsulationUser(shopReturnOrderDetailService.page(new Page<>(shopReturnOrderDetail.getPage(), shopReturnOrderDetail.getLimit()), encapsulationDataRights(shopReturnOrderDetail, queryWrapper, ShopReturnOrderDetailEntity::getCreateId))));
//    }

}
