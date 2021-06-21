package com.company.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.project.common.aop.annotation.DataScope;
import com.company.project.common.aop.annotation.LogAnnotation;
import com.company.project.common.utils.DataResult;
import com.company.project.entity.ShopSellerEntity;
import com.company.project.service.ShopSellerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


/**
 * 商家管理
 *
 * @author zhoushuai
 * @version V1.0
 * @date 2020年3月18日
 */
@Api(tags = "商家管理")
@Controller
@RequestMapping("/")
public class ShopSellerController extends BaseController {

    @Resource
    private ShopSellerService sellerService;

    @ApiOperation(value = "跳转进入列表页面")
    @GetMapping("/index/seller")
    public String shopSeller() {
        return "seller/sellerList";
    }

    @ApiOperation(value = "跳转进入新增/编辑页面")
    @GetMapping("/index/seller/addOrUpdate")
    public String addOrUpdate() {
        return "seller/addOrUpdate";
    }

    @ApiOperation(value = "新增")
    @PostMapping("/seller/add")
    @RequiresPermissions("seller:add")
    @LogAnnotation(title = "商家管理", action = "新增商家")
    @ResponseBody
    public DataResult add(@RequestBody ShopSellerEntity shopSeller) {
        return DataResult.success(sellerService.save(shopSeller));
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("/seller/delete")
    @RequiresPermissions("seller:delete")
    @LogAnnotation(title = "商家管理", action = "删除商家")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids) {
        return DataResult.success(sellerService.removeByIds(ids));
    }

    @ApiOperation(value = "更新")
    @PutMapping("/seller/update")
    @RequiresPermissions("seller:update")
    @LogAnnotation(title = "商家管理", action = "更新商家")
    @ResponseBody
    public DataResult update(@RequestBody ShopSellerEntity shopSeller) {
        return DataResult.success(sellerService.updateById(shopSeller));
    }

    @ApiOperation(value = "查询全部")
    @GetMapping("/seller/listByAll")
    @RequiresPermissions("seller:list")
    @LogAnnotation(title = "商家管理", action = "查询全部")
    @ResponseBody
    public DataResult findListByAll() {
        return DataResult.success(sellerService.list());
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("/seller/listByPage")
    @RequiresPermissions("seller:list")
    @LogAnnotation(title = "商家管理", action = "查询分页数据")
    @DataScope
    @ResponseBody
    public DataResult findListByPage(@RequestBody ShopSellerEntity shopSeller) {
        // 分页初始化
        Page<ShopSellerEntity> page = new Page<>(shopSeller.getPage(), shopSeller.getLimit());
        // 查询条件
        LambdaQueryWrapper<ShopSellerEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper
                .eq(StringUtils.isNotBlank(shopSeller.getId()), ShopSellerEntity::getId, shopSeller.getId())
                .like(StringUtils.isNotBlank(shopSeller.getSellerName()), ShopSellerEntity::getSellerName, shopSeller.getSellerName())
                .eq(StringUtils.isNotBlank(shopSeller.getContactNumber()), ShopSellerEntity::getContactNumber, shopSeller.getContactNumber())
                .eq(StringUtils.isNotBlank(shopSeller.getProvince()), ShopSellerEntity::getProvince, shopSeller.getProvince())
                .eq(StringUtils.isNotBlank(shopSeller.getCity()), ShopSellerEntity::getCity, shopSeller.getCity())
                .eq(StringUtils.isNotBlank(shopSeller.getCounty()), ShopSellerEntity::getCounty, shopSeller.getCounty())
                .in(CollectionUtils.isNotEmpty(shopSeller.getStatusList()), ShopSellerEntity::getStatus, shopSeller.getStatusList())
                .apply(StringUtils.isNotBlank(shopSeller.getCreateStartTime()), "UNIX_TIMESTAMP(create_time) >= UNIX_TIMESTAMP('" + shopSeller.getCreateStartTime() + "')")
                .apply(StringUtils.isNotBlank(shopSeller.getCreateEndTime()), "UNIX_TIMESTAMP(create_time) <= UNIX_TIMESTAMP('" + shopSeller.getCreateEndTime() + "')")
                .orderByDesc(ShopSellerEntity::getCreateTime);
        // 封装数据权限 - 执行查询 - 封装用户 - 响应前端
        return DataResult.success(encapsulationUser(sellerService.page(page, encapsulationDataRights(shopSeller, queryWrapper, ShopSellerEntity::getCreateId))));
    }


}
