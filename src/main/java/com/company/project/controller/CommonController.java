package com.company.project.controller;

import com.company.project.common.utils.DataResult;
import com.company.project.common.utils.DelimiterConstants;
import com.company.project.common.utils.DictionariesKeyConstant;
import com.company.project.entity.AddressLibraryEntity;
import com.company.project.entity.ShopLogisticsCompanyEntity;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;


/**
 * 公共Controller
 *
 * @author zhoushuai
 * @version V1.0
 * @date 2020年3月18日
 */
@Api(tags = "公共Controller")
@Controller
@RequestMapping("/common")
public class CommonController {

    @Resource
    private RedisTemplate redisTemplate;

    @ApiOperation(value = "查询所有性别")
    @GetMapping("findAllSex")
    @ResponseBody
    public DataResult findAllSex() {
        return DataResult.success(analysisRedisData(DictionariesKeyConstant.SEX));
    }

    @ApiOperation(value = "查询所有商品上下架状态")
    @GetMapping("findAllGoodsStatus")
    @ResponseBody
    public DataResult findAllGoodsStatus() {
        return DataResult.success(analysisRedisData(DictionariesKeyConstant.GOODS_STATUS));
    }

    @ApiOperation(value = "查询所有商品审核状态")
    @GetMapping("findAllGoodsExamineStatus")
    @ResponseBody
    public DataResult findAllGoodsExamineStatus() {
        return DataResult.success(analysisRedisData(DictionariesKeyConstant.GOODS_EXAMINE_STATUS));
    }

    @ApiOperation(value = "查询所有商品服务保证")
    @GetMapping("findAllServiceGuarantee")
    @ResponseBody
    public DataResult findAllServiceGuarantee() {
        return DataResult.success(analysisRedisData(DictionariesKeyConstant.SERVICE_GUARANTEE));
    }

    @ApiOperation(value = "查询所有广告类别")
    @GetMapping("findAllAdvertisingCategory")
    @ResponseBody
    public DataResult findAllAdvertisingCategory() {
        return DataResult.success(analysisRedisData(DictionariesKeyConstant.ADVERTISING_CATEGORY));
    }

    @ApiOperation(value = "查询所有广告展示方式")
    @GetMapping("findAllAdvertisingDisplay")
    @ResponseBody
    public DataResult findAllAdvertisingDisplay() {
        return DataResult.success(analysisRedisData(DictionariesKeyConstant.ADVERTISING_DISPLAY));
    }

    @ApiOperation(value = "查询所有跳转方式")
    @GetMapping("findAllJumpType")
    @ResponseBody
    public DataResult findAllJumpType() {
        return DataResult.success(analysisRedisData(DictionariesKeyConstant.JUMP_TYPE));
    }

    @ApiOperation(value = "查询所有导航类型")
    @GetMapping("findAllBannerType")
    @ResponseBody
    public DataResult findAllBannerType() {
        return DataResult.success(analysisRedisData(DictionariesKeyConstant.BANNER_TYPE));
    }

    @ApiOperation(value = "查询所有展示途径")
    @GetMapping("findAllShowWays")
    @ResponseBody
    public DataResult findAllShowWays() {
        return DataResult.success(analysisRedisData(DictionariesKeyConstant.SHOW_WAYS));
    }

    @ApiOperation(value = "查询所有轮播图位置")
    @GetMapping("findAllCarouselMapLocation")
    @ResponseBody
    public DataResult findAllCarouselMapLocation() {
        return DataResult.success(analysisRedisData(DictionariesKeyConstant.CAROUSEL_MAP_LOCATION));
    }

    @ApiOperation(value = "查询所有支付类型")
    @GetMapping("findAllPayType")
    @ResponseBody
    public DataResult findAllPayType() {
        return DataResult.success(analysisRedisData(DictionariesKeyConstant.PAY_TYPE));
    }


    @ApiOperation(value = "查询所有订单状态")
    @GetMapping("findAllOrderStatus")
    @ResponseBody
    public DataResult findAllOrderStatus() {
        return DataResult.success(analysisRedisData(DictionariesKeyConstant.ORDER_STATUS));
    }

    @ApiOperation(value = "查询所有支付状态")
    @GetMapping("findAllPayStatus")
    @ResponseBody
    public DataResult findAllPayStatus() {
        return DataResult.success(analysisRedisData(DictionariesKeyConstant.PAY_STATUS));
    }

    @ApiOperation(value = "查询所有发货状态")
    @GetMapping("findAllConsignStatus")
    @ResponseBody
    public DataResult findAllConsignStatus() {
        return DataResult.success(analysisRedisData(DictionariesKeyConstant.CONSIGN_STATUS));
    }

    @ApiOperation(value = "查询所有计价方式")
    @GetMapping("findAllPricingMethod")
    @ResponseBody
    public DataResult findAllPricingMethod() {
        return DataResult.success(analysisRedisData(DictionariesKeyConstant.PRICING_METHOD));
    }

    @ApiOperation(value = "查询所有配送方式")
    @GetMapping("findAllShippingMethod")
    @ResponseBody
    public DataResult findAllShippingMethod() {
        return DataResult.success(analysisRedisData(DictionariesKeyConstant.SHIPPING_METHOD));
    }

    @ApiOperation(value = "查询所有下级地址库")
    @GetMapping("findAllSubordinateAddressLibrary/{parentId}")
    @ResponseBody
    public DataResult findAllSubordinateAddressLibrary(@PathVariable("parentId") String parentId) {
        List<AddressLibraryEntity> addressLibraryEntityList = redisTemplate.boundHashOps(DictionariesKeyConstant.ADDRESS_LIBRARY_KEY_PREFIX.concat(parentId)).values();
        if (Objects.isNull(addressLibraryEntityList)) addressLibraryEntityList = Lists.newArrayList();
        addressLibraryEntityList.sort(Comparator.comparing(AddressLibraryEntity::getSeq));
        return DataResult.success(addressLibraryEntityList);
    }

    @ApiOperation(value = "根据字典值获取字典名称")
    @GetMapping("getLabelByValue")
    @ResponseBody
    public DataResult getLabelByValue(@RequestParam(value = "dictionariesKey") String dictionariesKey, @RequestParam(value = "value") String value) {
        AtomicReference<String> label = new AtomicReference<>(DelimiterConstants.EMPTY_STR);
        if(StringUtils.isNotBlank(dictionariesKey) && StringUtils.isNotBlank(value)){
            redisTemplate.boundHashOps(dictionariesKey).values().forEach(obj -> {
                try {
                    Map<String, String> map = BeanUtils.describe(obj);
                    if (StringUtils.equals(map.get("value"), value)) {
                        label.set(map.get("label"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        return DataResult.success(label.get());
    }

    @ApiOperation(value = "根据地区ID获取地区名称")
    @GetMapping("getNameById")
    @ResponseBody
    public DataResult getNameById(@RequestParam(value = "id") String id, @RequestParam(value = "parentId") String parentId) {
        AtomicReference<String> name = new AtomicReference<>(DelimiterConstants.EMPTY_STR);
        if(StringUtils.isNotBlank(id) && StringUtils.isNotBlank(parentId)){
            redisTemplate.boundHashOps(DictionariesKeyConstant.ADDRESS_LIBRARY_KEY_PREFIX.concat(parentId)).values().forEach(obj -> {
                AddressLibraryEntity addressLibraryEntity = (AddressLibraryEntity) obj;
                if(StringUtils.equals(addressLibraryEntity.getId(), id)){
                    name.set(addressLibraryEntity.getName());
                }
            });
        }
        return DataResult.success(name.get());
    }

    @ApiOperation(value = "根据地区ID获取地区名称")
    @GetMapping("getNameById2")
    @ResponseBody
    public DataResult getNameById2(@RequestParam(value = "id") String id) {
        AtomicReference<String> name = new AtomicReference<>(DelimiterConstants.EMPTY_STR);
        if(StringUtils.isNotBlank(id)){
            AddressLibraryEntity addressLibraryEntity = (AddressLibraryEntity) redisTemplate.boundValueOps(DictionariesKeyConstant.ADDRESS_LIBRARY_KEY2_PREFIX.concat(id)).get();
            if(Objects.nonNull(addressLibraryEntity)){
                name.set(addressLibraryEntity.getName());
            }
        }
        return DataResult.success(name.get());
    }

    @ApiOperation(value = "根据物流公司ID获取物流公司名称")
    @GetMapping("getLogisticsNameById")
    @ResponseBody
    public DataResult getLogisticsNameById(@RequestParam(value = "id") String id) {
        AtomicReference<String> name = new AtomicReference<>(DelimiterConstants.EMPTY_STR);
        if(StringUtils.isNotBlank(id)){
            ShopLogisticsCompanyEntity shopLogisticsCompanyEntity = (ShopLogisticsCompanyEntity) redisTemplate.boundHashOps(DictionariesKeyConstant.LOGISTICS_COMPANY_KEY).get(id);
            if(Objects.nonNull(shopLogisticsCompanyEntity)){
                name.set(shopLogisticsCompanyEntity.getName());
            }
        }
        return DataResult.success(name.get());
    }

    private List<Map<String, String>> analysisRedisData(String key) {
        List<Map<String, String>> result = Lists.newArrayList();
        redisTemplate.boundHashOps(key).values().forEach(obj -> {
            try {
                Map<String, String> map = BeanUtils.describe(obj);
                map.put("id", map.get("value"));
                map.put("name", map.get("label"));
                result.add(map);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        // 排序
        result.sort(Comparator.comparing(a -> a.get("sort")));
        return result;
    }

}
