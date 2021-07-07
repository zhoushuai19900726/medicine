package com.company.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.company.project.common.aop.annotation.DataScope;
import com.company.project.common.aop.annotation.LogAnnotation;
import com.company.project.common.utils.DelimiterConstants;
import com.company.project.common.utils.NumberConstants;
import com.company.project.entity.*;
import com.company.project.service.*;
import com.google.common.base.Joiner;
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
import java.util.Objects;
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
    private ShopBrandService shopBrandService;

    @Resource
    private ShopSpecService shopSpecService;

    @Resource
    private ShopParaService shopParaService;

    @Resource
    private ShopSpuOperationRecordService shopSpuOperationRecordService;

    @Resource
    private ShopSpuAuditRecordService shopSpuAuditRecordService;

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

    @ApiOperation(value = "跳转到编辑商品页面")
    @GetMapping("/index/shopSpu/editProduct/{id}")
    public String editProduct(@PathVariable("id") String id, Model model) {
        ShopSpuEntity shopSpuEntity = shopSpuService.getShopSpuEntityById(id);
        model.addAttribute("shopSpuEntity", shopSpuEntity);
        List<ShopSkuEntity> shopSkuEntityList = shopSkuService.listByCondition(Wrappers.<ShopSkuEntity>lambdaQuery().eq(ShopSkuEntity::getSpuId, id).orderByAsc(ShopSkuEntity::getId));
        model.addAttribute("skuSnList", Joiner.on(DelimiterConstants.COMMA).skipNulls().join(shopSkuEntityList.stream().map(ShopSkuEntity::getSn).collect(Collectors.toList())));
        // 未删除SKU
        model.addAttribute("shopSkuEntityList", shopSkuEntityList.stream().filter(a -> NumberConstants.ZERO_I.equals(a.getDeleted())).collect(Collectors.toList()));
        // 已删除SKU
        model.addAttribute("removedShopSkuEntityList", shopSkuEntityList.stream().filter(a -> NumberConstants.ONE_I.equals(a.getDeleted())).collect(Collectors.toList()));
        // 规格模板
        model.addAttribute("shopSpecEntityList", shopSpecService.list(Wrappers.<ShopSpecEntity>lambdaQuery().eq(ShopSpecEntity::getTemplateId, shopSpuEntity.getTemplateId()).orderByAsc(ShopSpecEntity::getSeq)));
        // 参数模板
        model.addAttribute("shopParaEntityList", shopParaService.list(Wrappers.<ShopParaEntity>lambdaQuery().eq(ShopParaEntity::getTemplateId, shopSpuEntity.getTemplateId()).orderByAsc(ShopParaEntity::getSeq)));
        return "goods/editProduct";
    }

    @ApiOperation(value = "跳转进入商品SPU详情页面")
    @GetMapping("/index/shopSpu/detail/{id}")
    public String spuDetail(@PathVariable("id") String id, Model model) {
        model.addAttribute("shopSpuEntity", shopSpuService.getShopSpuEntityById(id));
        model.addAttribute("shopSkuEntityList", shopSkuService.list(Wrappers.<ShopSkuEntity>lambdaQuery().eq(ShopSkuEntity::getSpuId, id)));
        return "goods/goodsDetail";
    }

    @ApiOperation(value = "跳转进入商品SKU详情页面")
    @GetMapping("/index/shopSku/detail/{id}")
    public String skuDetail(@PathVariable("id") String id, Model model) {
        model.addAttribute("shopSkuEntity", shopSkuService.getShopSpuEntityById(id));
        return "goods/skuDetail";
    }


    @ApiOperation(value = "跳转进入商品操作记录页面")
    @GetMapping("/index/shopSpu/operationRecord/{id}")
    public String operationRecord(@PathVariable("id") String id, Model model) {
        model.addAttribute("shopSpuOperationRecordEntityList", encapsulationUser(shopSpuOperationRecordService.list(Wrappers.<ShopSpuOperationRecordEntity>lambdaQuery().eq(ShopSpuOperationRecordEntity::getSpuId, id).orderByDesc(ShopSpuOperationRecordEntity::getCreateTime))));
        return "goods/operationRecord";
    }

    @ApiOperation(value = "跳转进入商品审核记录页面")
    @GetMapping("/index/shopSpu/auditRecord/{id}")
    public String auditRecord(@PathVariable("id") String id, Model model) {
        model.addAttribute("shopSpuAuditRecordEntityList", encapsulationUser(shopSpuAuditRecordService.list(Wrappers.<ShopSpuAuditRecordEntity>lambdaQuery().eq(ShopSpuAuditRecordEntity::getSpuId, id).orderByDesc(ShopSpuAuditRecordEntity::getCreateTime))));
        return "goods/auditRecord";
    }

    @ApiOperation(value = "跳转进入商品库存管理页面")
    @GetMapping("/index/shopSpu/stockControl/{id}")
    public String stockControl(@PathVariable("id") String id, Model model) {
        model.addAttribute("shopSpuEntity", shopSpuService.getShopSpuEntityById(id));
        // 库存管理只对未删除规格进行库存维护
        model.addAttribute("shopSkuEntityList", shopSkuService.list(Wrappers.<ShopSkuEntity>lambdaQuery().eq(ShopSkuEntity::getSpuId, id).orderByAsc(ShopSkuEntity::getId)));
        return "goods/stockControl";
    }

    @ApiOperation(value = "跳转进入商品规格管理页面")
    @GetMapping("/index/shopSpu/specControl/{id}")
    public String specControl(@PathVariable("id") String id, Model model) {
        ShopSpuEntity shopSpuEntity = shopSpuService.getShopSpuEntityById(id);
        // 品牌
        if (StringUtils.isNotBlank(shopSpuEntity.getBrandId())) {
            ShopBrandEntity shopBrandEntity = shopBrandService.getById(shopSpuEntity.getBrandId());
            if (Objects.nonNull(shopBrandEntity)) {
                shopSpuEntity.setBrandName(shopBrandEntity.getName());
            }
        }
        model.addAttribute("shopSpuEntity", shopSpuEntity);
        List<ShopSkuEntity> shopSkuEntityList = shopSkuService.listByCondition(Wrappers.<ShopSkuEntity>lambdaQuery().eq(ShopSkuEntity::getSpuId, id).orderByAsc(ShopSkuEntity::getId));
        model.addAttribute("skuSnList", Joiner.on(DelimiterConstants.COMMA).skipNulls().join(shopSkuEntityList.stream().map(ShopSkuEntity::getSn).collect(Collectors.toList())));
        // 未删除SKU
        model.addAttribute("shopSkuEntityList", shopSkuEntityList.stream().filter(a -> NumberConstants.ZERO_I.equals(a.getDeleted())).collect(Collectors.toList()));
        // 已删除SKU
        model.addAttribute("removedShopSkuEntityList", shopSkuEntityList.stream().filter(a -> NumberConstants.ONE_I.equals(a.getDeleted())).collect(Collectors.toList()));
        // 规格模板
        model.addAttribute("shopSpecEntityList", shopSpecService.list(Wrappers.<ShopSpecEntity>lambdaQuery().eq(ShopSpecEntity::getTemplateId, shopSpuEntity.getTemplateId()).orderByAsc(ShopSpecEntity::getSeq)));
        // 参数模板
        model.addAttribute("shopParaEntityList", shopParaService.list(Wrappers.<ShopParaEntity>lambdaQuery().eq(ShopParaEntity::getTemplateId, shopSpuEntity.getTemplateId()).orderByAsc(ShopParaEntity::getSeq)));
        return "goods/specControl";
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

    @ApiOperation(value = "逻辑删除SPU")
    @DeleteMapping("goods/delete")
    @RequiresPermissions("goods:delete")
    @LogAnnotation(title = "商品SPU", action = "逻辑删除")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids) {
        return shopSpuService.removeShopSpuEntityByIds(ids);
    }

    @ApiOperation(value = "逻辑删除SKU")
    @DeleteMapping("goods/deleteSku/{id}")
    @RequiresPermissions("goods:delete")
    @LogAnnotation(title = "商品SKU", action = "逻辑删除")
    @ResponseBody
    public DataResult deleteSku(@PathVariable("id") String id) {
        return DataResult.success(shopSkuService.removeById(id));
    }

    @ApiOperation(value = "物理删除SKU")
    @DeleteMapping("goods/absolutelyDeleteSku/{id}")
    @RequiresPermissions("goods:delete")
    @LogAnnotation(title = "商品SKU", action = "逻辑删除")
    @ResponseBody
    public DataResult absolutelyDeleteSku(@PathVariable("id") String id) {
        return DataResult.success(shopSkuService.absolutelyDeleteSku(id));
    }

    @ApiOperation(value = "还原SKU")
    @DeleteMapping("goods/reductionSku/{id}")
    @RequiresPermissions("goods:delete")
    @LogAnnotation(title = "商品SKU", action = "还原")
    @ResponseBody
    public DataResult reductionSku(@PathVariable("id") String id) {
        return DataResult.success(shopSkuService.reductionSku(id));
    }

    @ApiOperation(value = "物理删除")
    @DeleteMapping("goods/absolutelyDelete")
    @RequiresPermissions("goods:delete")
    @LogAnnotation(title = "商品SPU", action = "物理删除")
    @ResponseBody
    public DataResult absolutelyDelete(@RequestBody @ApiParam(value = "id集合") List<String> ids) {
        return shopSpuService.absolutelyRemoveShopSpuEntityByIds(ids);
    }

    @ApiOperation(value = "更新SPU")
    @PutMapping("goods/update")
    @RequiresPermissions("goods:update")
    @LogAnnotation(title = "商品SPU", action = "更新")
    @ResponseBody
    public DataResult update(@RequestBody ShopSpuEntity shopSpu) {
        return shopSpuService.updateShopSpuEntityById(shopSpu);
    }

    @ApiOperation(value = "更新SKU")
    @PutMapping("goods/updateSku")
    @RequiresPermissions("goods:update")
    @LogAnnotation(title = "商品SKU", action = "更新")
    @ResponseBody
    public DataResult updateSku(@RequestBody ShopSkuEntity shopSkuEntity) {
        return DataResult.success(shopSkuService.updateShopSpuEntityById(shopSkuEntity));
    }

    @ApiOperation(value = "更新SKU状态")
    @PutMapping("goods/updateSkuStatus")
    @RequiresPermissions("goods:update")
    @LogAnnotation(title = "商品SKU", action = "更新")
    @ResponseBody
    public DataResult updateSkuStatus(@RequestBody ShopSkuEntity shopSkuEntity) {
        return DataResult.success(shopSkuService.updateShopSpuEntityStatusById(shopSkuEntity));
    }

    @ApiOperation(value = "根据唯一索引查询")
    @PutMapping("goods/findOneByUnique")
    @RequiresPermissions("goods:update")
    @LogAnnotation(title = "根据SPU商品货号查询", action = "查询")
    @ResponseBody
    public DataResult findOneByUnique(@RequestBody ShopSpuEntity shopSpuEntity) {
        return DataResult.success(shopSpuService.getShopSpuEntityByUnique(shopSpuEntity));
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

    @ApiOperation(value = "复制")
    @PutMapping("goods/copyGoods")
    @RequiresPermissions("goods:add")
    @LogAnnotation(title = "商品SPU", action = "复制")
    @ResponseBody
    public DataResult copyGoods(@RequestBody ShopSpuEntity shopSpu) {
        return DataResult.success(shopSpuService.copyGoods(shopSpu));
    }

    @ApiOperation(value = "转移")
    @PutMapping("goods/tansferGoods")
    @RequiresPermissions("goods:add")
    @LogAnnotation(title = "商品SPU", action = "转移")
    @ResponseBody
    public DataResult tansferGoods(@RequestBody ShopSpuEntity shopSpu) {
        return DataResult.success(shopSpuService.tansferGoods(shopSpu));
    }


}

