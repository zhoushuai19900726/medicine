package com.company.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.company.project.common.aop.annotation.DataScope;
import com.company.project.common.aop.annotation.LogAnnotation;
import com.company.project.common.utils.DelimiterConstants;
import com.company.project.entity.ShopSpuEntity;
import com.company.project.service.ShopSpuService;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.commons.lang.StringUtils;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.company.project.common.utils.DataResult;

import com.company.project.entity.ShopLabelGoodsEntity;
import com.company.project.service.ShopLabelGoodsService;

import javax.annotation.Resource;


/**
 * 标签商品
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-07-15 17:15:30
 */
@Api(tags = "标签商品")
@Controller
@RequestMapping("/")
public class ShopLabelGoodsController extends BaseController {

    @Resource
    private ShopLabelGoodsService shopLabelGoodsService;

    @Resource
    private ShopSpuService shopSpuService;


    @ApiOperation(value = "跳转进入查看关联商品页面")
    @GetMapping("/index/shopLabelGoods/{labelId}")
    public String viewProduct(@PathVariable("labelId") String labelId, Model model) {
        model.addAttribute("labelId", labelId);
        return "label/viewProduct";
    }


    @ApiOperation(value = "跳转进入关联商品页面")
    @GetMapping("/index/shopLabelGoods/relationProduct/{labelId}")
    public String relationProduct(@PathVariable("labelId") String labelId, Model model) {
        model.addAttribute("labelId", labelId);
        return "label/relationProduct";
    }

    @ApiOperation(value = "新增")
    @PostMapping("shopLabelGoods/add")
    @RequiresPermissions("shopLabel:add")
    @LogAnnotation(title = "标签商品", action = "新增")
    @ResponseBody
    public DataResult add(@RequestBody ShopLabelGoodsEntity shopLabelGoods){
        return DataResult.success(shopLabelGoodsService.saveBatch(shopLabelGoods.getSpuIdList().stream().map(spuId ->  new ShopLabelGoodsEntity(shopLabelGoods.getLabelId(), spuId)).collect(Collectors.toList())));
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("shopLabelGoods/delete")
    @RequiresPermissions("shopLabel:delete")
    @LogAnnotation(title = "标签商品", action = "删除")
    @ResponseBody
    public DataResult delete(@RequestBody ShopLabelGoodsEntity shopLabelGoods) {
        return DataResult.success(shopLabelGoodsService.remove(Wrappers.<ShopLabelGoodsEntity>lambdaQuery().eq(ShopLabelGoodsEntity::getLabelId, shopLabelGoods.getLabelId()).in(ShopLabelGoodsEntity::getSpuId, shopLabelGoods.getSpuIdList())));
    }

    @ApiOperation(value = "查询分页数据(查询已关联商品)")
    @PostMapping("shopLabelGoods/listByPage")
    @RequiresPermissions("shopLabel:list")
    @LogAnnotation(title = "标签商品", action = "查询分页数据")
    @DataScope
    @ResponseBody
    public DataResult findListByPage(@RequestBody ShopLabelGoodsEntity shopLabelGoods) {
        // 封装查询条件
        packagingConditions(shopLabelGoods);
        // 查询条件
        LambdaQueryWrapper<ShopLabelGoodsEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper
                .eq(ShopLabelGoodsEntity::getLabelId, shopLabelGoods.getLabelId())
                .eq(StringUtils.isNotBlank(shopLabelGoods.getSpuId()), ShopLabelGoodsEntity::getSpuId, shopLabelGoods.getSpuId())
                .orderByDesc(ShopLabelGoodsEntity::getCreateTime);
        // 执行查询
        IPage<ShopLabelGoodsEntity> iPage = shopLabelGoodsService.page(new Page<>(shopLabelGoods.getPage(), shopLabelGoods.getLimit()), queryWrapper);
        List<ShopLabelGoodsEntity> shopLabelGoodsEntityList = iPage.getRecords();

        IPage<ShopSpuEntity> iPage2 = new Page<>();
        iPage2.setTotal(iPage.getTotal());

        if (CollectionUtils.isNotEmpty(shopLabelGoodsEntityList)) {
            List<String> spuIdList = shopLabelGoodsEntityList.stream().map(ShopLabelGoodsEntity::getSpuId).collect(Collectors.toList());
            List<ShopSpuEntity> shopSpuEntityList = shopSpuService.listByIdList(spuIdList);
            iPage2.setRecords(shopSpuEntityList);
        }

        return DataResult.success(iPage2);
    }

    @ApiOperation(value = "查询分页数据(所有可关联商品)")
    @PostMapping("shopLabelGoods/listByPage2")
    @RequiresPermissions("shopLabel:list")
    @LogAnnotation(title = "标签商品", action = "查询分页数据")
    @DataScope
    @ResponseBody
    public DataResult findListByPage2(@RequestBody ShopLabelGoodsEntity shopLabelGoods) {
        // 封装查询条件
        packagingConditions(shopLabelGoods);
        // 查询当前标签已关联商品
        List<ShopLabelGoodsEntity> shopLabelGoodsEntityList = shopLabelGoodsService.list(Wrappers.<ShopLabelGoodsEntity>lambdaQuery().eq(ShopLabelGoodsEntity::getLabelId, shopLabelGoods.getLabelId()));
        List<String> spuIdList = shopLabelGoodsEntityList.stream().map(ShopLabelGoodsEntity::getSpuId).collect(Collectors.toList());
        // 查询条件
        LambdaQueryWrapper<ShopSpuEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper
                .eq(StringUtils.isNotBlank(shopLabelGoods.getSpuId()), ShopSpuEntity::getId, shopLabelGoods.getSpuId())
                .notIn(CollectionUtils.isNotEmpty(spuIdList), ShopSpuEntity::getId, spuIdList)
                .orderByDesc(ShopSpuEntity::getCreateTime);
        // 执行查询
        return DataResult.success(shopSpuService.listByPage(new Page<>(shopLabelGoods.getPage(), shopLabelGoods.getLimit()), queryWrapper));
    }

    private void packagingConditions(ShopLabelGoodsEntity shopLabelGoods){
        if (StringUtils.isNotBlank(shopLabelGoods.getSn())) {
            ShopSpuEntity shopSpuEntity = new ShopSpuEntity();
            shopSpuEntity.setSn(shopLabelGoods.getSn());
            ShopSpuEntity result = shopSpuService.getShopSpuEntityByUnique(shopSpuEntity);
            if (Objects.nonNull(result)) {
                shopLabelGoods.setSpuId(result.getId());
            }
        }
    }
}
