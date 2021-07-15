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

import com.company.project.entity.ShopAdvertisementEntity;
import com.company.project.service.ShopAdvertisementService;

import javax.annotation.Resource;


/**
 * 广告
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-07-15 09:28:29
 */
@Api(tags = "广告")
@Controller
@RequestMapping("/")
public class ShopAdvertisementController extends BaseController {

    @Resource
    private ShopAdvertisementService shopAdvertisementService;


    @ApiOperation(value = "跳转到广告列表页面")
    @GetMapping("/index/shopAdvertisement")
    public String shopAdvertisement() {
        return "advertisement/advertisementList";
    }

    @ApiOperation(value = "跳转进入新增/编辑页面")
    @GetMapping("/index/shopAdvertisement/addOrUpdate")
    public String addOrUpdate() {
        return "advertisement/addOrUpdate";
    }

    @ApiOperation(value = "新增")
    @PostMapping("shopAdvertisement/add")
    @RequiresPermissions("shopAdvertisement:add")
    @LogAnnotation(title = "广告", action = "新增")
    @ResponseBody
    public DataResult add(@RequestBody ShopAdvertisementEntity shopAdvertisement) {
        return DataResult.success(shopAdvertisementService.save(shopAdvertisement));
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("shopAdvertisement/delete")
    @RequiresPermissions("shopAdvertisement:delete")
    @LogAnnotation(title = "广告", action = "删除")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids) {
        return DataResult.success(shopAdvertisementService.removeByIds(ids));
    }

    @ApiOperation(value = "更新")
    @PutMapping("shopAdvertisement/update")
    @RequiresPermissions("shopAdvertisement:update")
    @LogAnnotation(title = "广告", action = "更新")
    @ResponseBody
    public DataResult update(@RequestBody ShopAdvertisementEntity shopAdvertisement) {
        return DataResult.success(shopAdvertisementService.updateById(shopAdvertisement));
    }

    @ApiOperation(value = "查询全部")
    @GetMapping("shopAdvertisement/listByAll")
    @RequiresPermissions("shopAdvertisement:list")
    @LogAnnotation(title = "广告", action = "查询全部")
    @ResponseBody
    public DataResult findListByAll() {
        return DataResult.success(shopAdvertisementService.list());
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("shopAdvertisement/listByPage")
    @RequiresPermissions("shopAdvertisement:list")
    @LogAnnotation(title = "广告", action = "查询分页数据")
    @DataScope
    @ResponseBody
    public DataResult findListByPage(@RequestBody ShopAdvertisementEntity shopAdvertisement) {
        // 查询条件
        LambdaQueryWrapper<ShopAdvertisementEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper
                .eq(StringUtils.isNotBlank(shopAdvertisement.getId()), ShopAdvertisementEntity::getId, shopAdvertisement.getId())
                .like(StringUtils.isNotBlank(shopAdvertisement.getTitle()), ShopAdvertisementEntity::getTitle, shopAdvertisement.getTitle())
                .apply(StringUtils.isNotBlank(shopAdvertisement.getCreateStartTime()), "UNIX_TIMESTAMP(create_time) >= UNIX_TIMESTAMP('" + shopAdvertisement.getCreateStartTime() + "')")
                .apply(StringUtils.isNotBlank(shopAdvertisement.getCreateEndTime()), "UNIX_TIMESTAMP(create_time) <= UNIX_TIMESTAMP('" + shopAdvertisement.getCreateEndTime() + "')")
                .orderByAsc(ShopAdvertisementEntity::getSeq);
        // 封装数据权限 - 执行查询 - 封装用户 - 响应前端
        return DataResult.success(encapsulationUser(shopAdvertisementService.listByPage(new Page<>(shopAdvertisement.getPage(), shopAdvertisement.getLimit()), encapsulationDataRights(shopAdvertisement, queryWrapper, ShopAdvertisementEntity::getCreateId))));
    }

}
