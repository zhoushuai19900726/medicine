package com.company.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.company.project.common.aop.annotation.DataScope;
import com.company.project.common.aop.annotation.LogAnnotation;
import com.company.project.common.utils.DelimiterConstants;
import com.company.project.entity.*;
import com.company.project.service.*;
import com.google.common.base.Splitter;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.commons.lang.StringUtils;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.List;

import com.company.project.common.utils.DataResult;

import javax.annotation.Resource;


/**
 * 商品SPU
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-06-17 10:25:25
 */
@Controller
@RequestMapping("/")
public class ShopSpuController extends BaseController {

    @Resource
    private ShopSpuService shopSpuService;

    @Resource
    private ShopSkuService shopSkuService;

    @Resource
    private ShopSpuOperationRecordService shopSpuOperationRecordService;

    @ApiOperation(value = "跳转到列表页面")
    @GetMapping("/index/shopSpu")
    public String shopSpu() {
        return "goods/goodsList";
    }

    @ApiOperation(value = "跳转到发布商品页面")
    @GetMapping("/index/shopSpu/releaseProduct")
    public String releaseProduct() {
        return "goods/releaseProduct";
    }

    @ApiOperation(value = "跳转进入商品详情页面")
    @GetMapping("/index/shopSpu/detail/{id}")
    public String detail(@PathVariable("id") String id, Model model) {
        model.addAttribute("shopSpuEntity", shopSpuService.getShopSpuEntityById(id));
        model.addAttribute("shopSkuEntityList", shopSkuService.listByCondition(Wrappers.<ShopSkuEntity>lambdaQuery().eq(ShopSkuEntity::getSpuId, id)));
        return "goods/goodsDetail";
    }


    @ApiOperation(value = "跳转进入商品操作记录页面")
    @GetMapping("/index/shopSpu/operationRecord/{id}")
    public String operationRecord(@PathVariable("id") String id, Model model) {
        model.addAttribute("shopSpuOperationRecordEntityList", encapsulationUser(shopSpuOperationRecordService.list(Wrappers.<ShopSpuOperationRecordEntity>lambdaQuery().eq(ShopSpuOperationRecordEntity::getSpuId, id).orderByDesc(ShopSpuOperationRecordEntity::getCreateTime))));
        return "goods/operationRecord";
    }

    @ApiOperation(value = "跳转进入审核页面")
    @GetMapping("/index/shopSpu/auditList")
    public String auditList() {
        return "goods/auditList";
    }

    @ApiOperation(value = "跳转进入回收站页面")
    @GetMapping("/index/shopSpu/recycleBinList")
    public String recycleBinList() {
        return "goods/recycleBinList";
    }

    @ApiOperation(value = "新增")
    @PostMapping("goods/add")
    @RequiresPermissions("goods:add")
    @LogAnnotation(title = "商品SPU", action = "新增")
    @ResponseBody
    public DataResult add(@RequestBody ShopSpuEntity shopSpu) {
        return shopSpuService.saveShopSpuEntity(shopSpu);
    }

    @ApiOperation(value = "逻辑删除")
    @DeleteMapping("goods/delete")
    @RequiresPermissions("goods:delete")
    @LogAnnotation(title = "商品SPU", action = "逻辑删除")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids) {
        return shopSpuService.removeShopSpuEntityByIds(ids);
    }

    @ApiOperation(value = "物理删除")
    @DeleteMapping("goods/absolutelyDelete")
    @RequiresPermissions("goods:delete")
    @LogAnnotation(title = "商品SPU", action = "物理删除")
    @ResponseBody
    public DataResult absolutelyDelete(@RequestBody @ApiParam(value = "id集合") List<String> ids) {
        return shopSpuService.absolutelyRemoveShopSpuEntityByIds(ids);
    }

    @ApiOperation(value = "更新")
    @PutMapping("goods/update")
    @RequiresPermissions("goods:update")
    @LogAnnotation(title = "商品SPU", action = "更新")
    @ResponseBody
    public DataResult update(@RequestBody ShopSpuEntity shopSpu) {
        return shopSpuService.updateShopSpuEntityById(shopSpu);
    }

    @ApiOperation(value = "根据唯一索引查询")
    @PutMapping("goods/findOneByUnique/{sn}")
    @RequiresPermissions("goods:update")
    @LogAnnotation(title = "根据SPU商品货号查询", action = "查询")
    @ResponseBody
    public DataResult findOneByUnique(@PathVariable("sn") String sn) {
        return DataResult.success(shopSpuService.getShopSpuEntityByUnique(sn));
    }

    @ApiOperation(value = "根据商家生成唯一商品货号")
    @GetMapping("goods/getUniqueSnBySeller/{sellerId}")
    @RequiresPermissions("goods:update")
    @LogAnnotation(title = "根据商家生成唯一商品货号", action = "查询")
    @ResponseBody
    public DataResult getUniqueSnBySeller(@PathVariable("sellerId") String sellerId) {
        return DataResult.success(shopSpuService.getUniqueSnBySeller(sellerId));
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("goods/listByPage")
    @RequiresPermissions("goods:list")
    @LogAnnotation(title = "商品SPU", action = "查询分页数据")
    @DataScope
    @ResponseBody
    public DataResult findListByPage(@RequestBody ShopSpuEntity shopSpu) {
        return DataResult.success(encapsulationUser(shopSpuService.listByPage(new Page<>(shopSpu.getPage(), shopSpu.getLimit()), encapsulationDataRights(shopSpu, encapsulatingQueryCriteria(shopSpu), ShopSpuEntity::getCreateId))));
    }

    @ApiOperation(value = "查询分页数据(回收站)")
    @PostMapping("goods/recycleBinListByPage")
    @RequiresPermissions("goods:list")
    @LogAnnotation(title = "商品SPU", action = "查询分页数据(回收站)")
    @DataScope
    @ResponseBody
    public DataResult findRecycleBinListByPage(@RequestBody ShopSpuEntity shopSpu) {
        return DataResult.success(encapsulationUser(shopSpuService.recycleBinListByPage(new Page<>(shopSpu.getPage(), shopSpu.getLimit()), encapsulationDataRights(shopSpu, encapsulatingQueryCriteria(shopSpu), ShopSpuEntity::getCreateId))));
    }

    /**
     * 封装查询条件
     *
     * @param shopSpu
     */
    private LambdaQueryWrapper<ShopSpuEntity> encapsulatingQueryCriteria(ShopSpuEntity shopSpu) {
        LambdaQueryWrapper<ShopSpuEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper
                .eq(StringUtils.isNotBlank(shopSpu.getId()), ShopSpuEntity::getId, shopSpu.getId())
                .eq(StringUtils.isNotBlank(shopSpu.getSn()), ShopSpuEntity::getSn, shopSpu.getSn())
                .like(StringUtils.isNotBlank(shopSpu.getName()), ShopSpuEntity::getName, shopSpu.getName())
                .eq(StringUtils.isNotBlank(shopSpu.getSellerId()), ShopSpuEntity::getSellerId, shopSpu.getSellerId())
                .in(StringUtils.isNotBlank(shopSpu.getSellerIdListStr()), ShopSpuEntity::getSellerId, Splitter.on(DelimiterConstants.COMMA).omitEmptyStrings().trimResults().splitToList(shopSpu.getSellerIdListStr()))
                .eq(StringUtils.isNotBlank(shopSpu.getBrandId()), ShopSpuEntity::getBrandId, shopSpu.getBrandId())
                .in(StringUtils.isNotBlank(shopSpu.getBrandIdListStr()), ShopSpuEntity::getBrandId, Splitter.on(DelimiterConstants.COMMA).omitEmptyStrings().trimResults().splitToList(shopSpu.getBrandIdListStr()))
                .eq(StringUtils.isNotBlank(shopSpu.getIsMarketable()), ShopSpuEntity::getIsMarketable, shopSpu.getIsMarketable())
                .in(StringUtils.isNotBlank(shopSpu.getIsMarketableListStr()), ShopSpuEntity::getIsMarketable, Splitter.on(DelimiterConstants.COMMA).omitEmptyStrings().trimResults().splitToList(shopSpu.getIsMarketableListStr()))
                .eq(StringUtils.isNotBlank(shopSpu.getStatus()), ShopSpuEntity::getStatus, shopSpu.getStatus())
                .in(StringUtils.isNotBlank(shopSpu.getStatusListStr()), ShopSpuEntity::getStatus, Splitter.on(DelimiterConstants.COMMA).omitEmptyStrings().trimResults().splitToList(shopSpu.getStatusListStr()))
                .eq(StringUtils.isNotBlank(shopSpu.getCategory1Id()), ShopSpuEntity::getCategory1Id, shopSpu.getCategory1Id())
                .eq(StringUtils.isNotBlank(shopSpu.getCategory2Id()), ShopSpuEntity::getCategory2Id, shopSpu.getCategory2Id())
                .eq(StringUtils.isNotBlank(shopSpu.getCategory3Id()), ShopSpuEntity::getCategory3Id, shopSpu.getCategory3Id())
                .in(StringUtils.isNotBlank(shopSpu.getCategory3IdListStr()), ShopSpuEntity::getCategory3Id, Splitter.on(DelimiterConstants.COMMA).omitEmptyStrings().trimResults().splitToList(shopSpu.getCategory3IdListStr()))
                .orderByDesc(ShopSpuEntity::getCreateTime);
        return queryWrapper;
    }



}

