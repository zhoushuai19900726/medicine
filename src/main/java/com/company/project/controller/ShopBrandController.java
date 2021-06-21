package com.company.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.company.project.common.aop.annotation.DataScope;
import com.company.project.common.aop.annotation.LogAnnotation;
import com.company.project.common.utils.PinYinUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.List;

import com.company.project.common.utils.DataResult;

import com.company.project.entity.ShopBrandEntity;
import com.company.project.service.ShopBrandService;

import javax.annotation.Resource;


/**
 * 商品品牌
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-06-15 14:15:14
 */
@Controller
@RequestMapping("/")
public class ShopBrandController extends BaseController {

    @Resource
    private ShopBrandService shopBrandService;

    @ApiOperation(value = "跳转到列表页面")
    @GetMapping("/index/shopBrand")
    public String shopBrand() {
        return "brand/brandList";
    }

    @ApiOperation(value = "跳转进入新增/编辑页面")
    @GetMapping("/index/shopBrand/addOrUpdate")
    public String addOrUpdate() {
        return "brand/addOrUpdate";
    }

    @ApiOperation(value = "新增")
    @PostMapping("shopBrand/add")
    @RequiresPermissions("shopBrand:add")
    @LogAnnotation(title = "商品品牌", action = "新增品牌")
    @ResponseBody
    public DataResult add(@RequestBody ShopBrandEntity shopBrand) {
        if (StringUtils.isNotBlank(shopBrand.getName()) && StringUtils.isBlank(shopBrand.getLetter())) {
            shopBrand.setLetter(PinYinUtils.getUpperStr(shopBrand.getName()));
        }
        return DataResult.success(shopBrandService.save(shopBrand));
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("shopBrand/delete")
    @RequiresPermissions("shopBrand:delete")
    @LogAnnotation(title = "商品品牌", action = "删除品牌")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids) {
        return DataResult.success(shopBrandService.removeByIds(ids));
    }

    @ApiOperation(value = "更新")
    @PutMapping("shopBrand/update")
    @RequiresPermissions("shopBrand:update")
    @LogAnnotation(title = "商品品牌", action = "更新品牌")
    @ResponseBody
    public DataResult update(@RequestBody ShopBrandEntity shopBrand) {
        if (StringUtils.isNotBlank(shopBrand.getName()) && StringUtils.isBlank(shopBrand.getLetter())) {
            shopBrand.setLetter(PinYinUtils.getFirstUpperStr(shopBrand.getName()));
        }
        return DataResult.success(shopBrandService.updateById(shopBrand));
    }

    @ApiOperation(value = "查询全部")
    @GetMapping("/shopBrand/listByAll")
    @RequiresPermissions("shopBrand:list")
    @LogAnnotation(title = "商品品牌", action = "查询全部")
    @ResponseBody
    public DataResult findListByAll() {
        return DataResult.success(shopBrandService.list());
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("shopBrand/listByPage")
    @RequiresPermissions("shopBrand:list")
    @LogAnnotation(title = "商品品牌", action = "查询分页数据")
    @DataScope
    @ResponseBody
    public DataResult findListByPage(@RequestBody ShopBrandEntity shopBrand) {
        // 分页初始化
        Page<ShopBrandEntity> page = new Page<>(shopBrand.getPage(), shopBrand.getLimit());
        // 查询条件
        LambdaQueryWrapper<ShopBrandEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper
                .eq(StringUtils.isNotBlank(shopBrand.getId()), ShopBrandEntity::getId, shopBrand.getId())
                .like(StringUtils.isNotBlank(shopBrand.getName()), ShopBrandEntity::getName, shopBrand.getName())
                .apply(StringUtils.isNotBlank(shopBrand.getCreateStartTime()), "UNIX_TIMESTAMP(create_time) >= UNIX_TIMESTAMP('" + shopBrand.getCreateStartTime() + "')")
                .apply(StringUtils.isNotBlank(shopBrand.getCreateEndTime()), "UNIX_TIMESTAMP(create_time) <= UNIX_TIMESTAMP('" + shopBrand.getCreateEndTime() + "')")
                .orderByDesc(ShopBrandEntity::getCreateTime);
        // 封装数据权限 - 执行查询 - 封装用户 - 响应前端
        return DataResult.success(encapsulationUser(shopBrandService.page(page, encapsulationDataRights(shopBrand, queryWrapper, ShopBrandEntity::getCreateId))));
    }

}
