package com.company.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.company.project.common.aop.annotation.DataScope;
import com.company.project.common.aop.annotation.LogAnnotation;
import com.company.project.common.utils.DelimiterConstants;
import com.company.project.common.utils.NumberConstants;
import com.company.project.entity.*;
import com.company.project.service.*;
import com.google.common.base.Splitter;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.collections.CollectionUtils;
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
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

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
    private ShopCategoryService shopCategoryService;

    @Resource
    private ShopSellerService shopSellerService;

    @Resource
    private ShopBrandService shopBrandService;

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
    @GetMapping("/index/shopSpu/detail")
    public String detail() {
        return "goods/goodsDetail";
    }

    @ApiOperation(value = "跳转进入审核页面")
    @GetMapping("/index/shopSpu/auditList")
    public String addOrUpdate() {
        return "goods/auditList";
    }

    @ApiOperation(value = "新增")
    @PostMapping("goods/add")
    @RequiresPermissions("goods:add")
    @LogAnnotation(title = "商品SPU", action = "新增")
    @ResponseBody
    public DataResult add(@RequestBody ShopSpuEntity shopSpu) {
        return shopSpuService.saveShopSpuEntity(shopSpu);
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("goods/delete")
    @RequiresPermissions("goods:delete")
    @LogAnnotation(title = "商品SPU", action = "删除")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids) {
        return DataResult.success(shopSpuService.removeByIds(ids));
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
        return DataResult.success(shopSpuService.getOne(Wrappers.<ShopSpuEntity>lambdaQuery().eq(ShopSpuEntity::getSn, sn)));
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("goods/listByPage")
    @RequiresPermissions("goods:list")
    @LogAnnotation(title = "商品SPU", action = "查询分页数据")
    @DataScope
    @ResponseBody
    public DataResult findListByPage(@RequestBody ShopSpuEntity shopSpu) {
        // 分页初始化
        Page<ShopSpuEntity> page = new Page<>(shopSpu.getPage(), shopSpu.getLimit());
        // 查询条件
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
        // 封装数据权限 - 执行查询
        IPage<ShopSpuEntity> iPage = shopSpuService.page(page, encapsulationDataRights(shopSpu, queryWrapper, ShopSpuEntity::getCreateId));
        // 封装名称
        List<ShopSpuEntity> shopSpuEntityList = iPage.getRecords();
        if (CollectionUtils.isNotEmpty(shopSpuEntityList)) {
            // 查询结果封装
            Map<String, String> shopCategoryEntityMap = Maps.newHashMap();
            Map<String, String> shopSellerEntityMap = Maps.newHashMap();
            Map<String, String> shopBrandEntityMap = Maps.newHashMap();
            //  提分类ID集合
            Set<String> categoryIdSet = Sets.newHashSet();
            categoryIdSet.addAll(shopSpuEntityList.stream().map(ShopSpuEntity::getCategory1Id).collect(Collectors.toList()));
            categoryIdSet.addAll(shopSpuEntityList.stream().map(ShopSpuEntity::getCategory2Id).collect(Collectors.toList()));
            categoryIdSet.addAll(shopSpuEntityList.stream().map(ShopSpuEntity::getCategory3Id).collect(Collectors.toList()));
            if (CollectionUtils.isNotEmpty(categoryIdSet)) {
                // 查询分类集合
                List<ShopCategoryEntity> shopEntityList = shopCategoryService.listByIds(categoryIdSet);
                if (CollectionUtils.isNotEmpty(shopEntityList)) {
                    // 封装模板名称
                    shopCategoryEntityMap.putAll(shopEntityList.stream().collect(Collectors.toMap(ShopCategoryEntity::getId, ShopCategoryEntity::getName, (k1, k2) -> k1)));
                }
            }
            // 提取商家ID集合
            List<String> sellerIdList = shopSpuEntityList.stream().map(ShopSpuEntity::getSellerId).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(sellerIdList)) {
                // 查询商家集合
                List<ShopSellerEntity> shopEntityList = shopSellerService.listByIds(sellerIdList);
                if (CollectionUtils.isNotEmpty(shopEntityList)) {
                    // 封装商家名称
                    shopSellerEntityMap.putAll(shopEntityList.stream().collect(Collectors.toMap(ShopSellerEntity::getId, ShopSellerEntity::getSellerName, (k1, k2) -> k1)));
                }
            }
            // 提取品牌ID集合
            List<String> brandIdList = shopSpuEntityList.stream().map(ShopSpuEntity::getBrandId).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(brandIdList)) {
                // 查询品牌集合
                List<ShopBrandEntity> shopEntityList = shopBrandService.listByIds(brandIdList);
                if (CollectionUtils.isNotEmpty(shopEntityList)) {
                    // 封装品牌名称
                    shopBrandEntityMap.putAll(shopEntityList.stream().collect(Collectors.toMap(ShopBrandEntity::getId, ShopBrandEntity::getName, (k1, k2) -> k1)));
                }
            }
            // 执行封装名称
            shopSpuEntityList.forEach(shopSpuEntity -> {
                shopSpuEntity.setCategory1Name(shopCategoryEntityMap.getOrDefault(shopSpuEntity.getCategory1Id(), DelimiterConstants.EMPTY_STR));
                shopSpuEntity.setCategory2Name(shopCategoryEntityMap.getOrDefault(shopSpuEntity.getCategory2Id(), DelimiterConstants.EMPTY_STR));
                shopSpuEntity.setCategory3Name(shopCategoryEntityMap.getOrDefault(shopSpuEntity.getCategory3Id(), DelimiterConstants.EMPTY_STR));
                shopSpuEntity.setSellerName(shopSellerEntityMap.getOrDefault(shopSpuEntity.getSellerId(), DelimiterConstants.EMPTY_STR));
                shopSpuEntity.setBrandName(shopBrandEntityMap.getOrDefault(shopSpuEntity.getBrandId(), DelimiterConstants.EMPTY_STR));
            });
        }
        // 封装数据权限 - 执行查询 - 封装用户 - 响应前端
        return DataResult.success(encapsulationUser(iPage));
    }

    @ApiOperation(value = "根据商家生成唯一商品货号")
    @GetMapping("goods/getUniqueSnBySeller/{sellerId}")
    @RequiresPermissions("goods:update")
    @LogAnnotation(title = "根据商家生成唯一商品货号", action = "查询")
    @ResponseBody
    public DataResult getUniqueSnBySeller(@PathVariable("sellerId") String sellerId) {
        // 根据店铺id查找店铺发布了多少个商品
        Integer n = shopSpuService.getBaseMapper().selectCount(Wrappers.<ShopSpuEntity>lambdaQuery().eq(ShopSpuEntity::getSellerId, sellerId));
        int a = NumberConstants.ZERO;
        String serialNo;
        while(true){
            a++;
            String total = String.format("%04d", n + a);
            serialNo = DelimiterConstants.PREFIX + total;
            ShopSpuEntity shopSpuEntity = shopSpuService.getOne(Wrappers.<ShopSpuEntity>lambdaQuery().eq(ShopSpuEntity::getSn, serialNo));
            if(Objects.isNull(shopSpuEntity)){
                break;
            }
        }
        return DataResult.success(serialNo);
    }

}
