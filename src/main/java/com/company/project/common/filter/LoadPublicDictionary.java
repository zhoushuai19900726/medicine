package com.company.project.common.filter;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.company.project.common.utils.DictionariesKeyConstant;
import com.company.project.common.utils.NumberConstants;
import com.company.project.entity.*;
import com.company.project.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/***
 *       ___      _        _
 *      / __\__ _| |_ __ _| |_ __   __ _
 *     / /  / _` | __/ _` | | '_ \ / _` |
 *    / /__| (_| | || (_| | | |_) | (_| |
 *    \____/\__,_|\__\__,_|_| .__/ \__,_|
 *                          |_|
 * @author
 * @date
 * @desc 公共字典数据加载进缓存
 */
@Order(1)
@Component
@Slf4j
public class LoadPublicDictionary implements CommandLineRunner {

    @Resource
    private SysDictService sysDictService;

    @Resource
    private SysDictDetailService sysDictDetailService;

    @Resource
    private ShopAdvertisementService shopAdvertisementService;

    @Resource
    private ShopAdvertisingSpaceService shopAdvertisingSpaceService;

    @Resource
    private ShopBannerService shopBannerService;

    @Resource
    private ShopNoticeService shopNoticeService;

    @Resource
    private ShopCarouselMapService shopCarouselMapService;

    @Resource
    private ShopLogisticsCompanyService shopLogisticsCompanyService;

    @Resource
    private AddressLibraryService addressLibraryService;

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public void run(String... var1) {
        log.info("缓存管理-->缓存初始化:服务启动!");
        log.info("/** ==============================  【数据字典】 ============================== **/");
        // 获取【数据字典】加载开始时间
        long dictionaryGroupStartTime = System.currentTimeMillis();
        List<SysDictDetailEntity> sysDictDetailEntityList = sysDictDetailService.list();
        if (CollectionUtils.isNotEmpty(sysDictDetailEntityList)) {
            Map<String, List<SysDictDetailEntity>> groupBy = sysDictDetailEntityList.stream().collect(Collectors.groupingBy(SysDictDetailEntity::getDictId));
            List<SysDictEntity> sysDictEntityList = sysDictService.listByIds(groupBy.keySet());
            Map<String, String> sysDictEntityMMap = sysDictEntityList.stream().collect(Collectors.toMap(SysDictEntity::getId, SysDictEntity::getName, (k1, k2) -> k1));
            groupBy.forEach((k, v) -> redisTemplate.boundHashOps(DictionariesKeyConstant.DICT_KEY_PREFIX.concat(sysDictEntityMMap.get(k))).putAll(v.stream().collect(Collectors.toMap(SysDictDetailEntity::getId, a -> a, (k1, k2) -> k1))));
        }
        // 获取【数据字典】加载结束时间
        long dictionaryGroupEndTime = System.currentTimeMillis();
        log.info("缓存管理-->缓存初始化:【数据字典】加载完成, 用时" + (dictionaryGroupEndTime - dictionaryGroupStartTime) + "ms!");
        log.info("/** ==============================  【地址库】 ============================== **/");
        List<AddressLibraryEntity> addressLibraryEntityList = addressLibraryService.list(Wrappers.<AddressLibraryEntity>lambdaQuery().eq(AddressLibraryEntity::getIsShow, NumberConstants.ONE_STR));
        if (CollectionUtils.isNotEmpty(addressLibraryEntityList)) {
            addressLibraryEntityList.forEach(addressLibraryEntity -> redisTemplate.boundValueOps(DictionariesKeyConstant.ADDRESS_LIBRARY_KEY2_PREFIX.concat(addressLibraryEntity.getId())).set(addressLibraryEntity));
            Map<String, List<AddressLibraryEntity>> groupBy = addressLibraryEntityList.stream().collect(Collectors.groupingBy(AddressLibraryEntity::getParentId));
            groupBy.forEach((k, v) -> redisTemplate.boundHashOps(DictionariesKeyConstant.ADDRESS_LIBRARY_KEY_PREFIX.concat(k)).putAll(v.stream().collect(Collectors.toMap(AddressLibraryEntity::getId, a -> a, (k1, k2) -> k1))));
        }
        // 获取【地址库】加载结束时间
        long addressLibraryEndTime = System.currentTimeMillis();
        log.info("缓存管理-->缓存初始化:【地址库】加载完成, 用时" + (addressLibraryEndTime - dictionaryGroupEndTime) + "ms!");
        log.info("/** ==============================  【广告】 ============================== **/");
        List<ShopAdvertisementEntity> shopAdvertisementEntityList = shopAdvertisementService.list();
        if (CollectionUtils.isNotEmpty(shopAdvertisementEntityList)) {
            Map<String, List<ShopAdvertisementEntity>> groupBy = shopAdvertisementEntityList.stream().collect(Collectors.groupingBy(ShopAdvertisementEntity::getSpaceId));
            List<ShopAdvertisingSpaceEntity> shopAdvertisingSpaceEntityList = shopAdvertisingSpaceService.listByIds(groupBy.keySet());
            Map<String, String> shopAdvertisingSpaceEntityMap = shopAdvertisingSpaceEntityList.stream().collect(Collectors.toMap(ShopAdvertisingSpaceEntity::getId, ShopAdvertisingSpaceEntity::getGetTag, (k1, k2) -> k1));
            groupBy.forEach((k, v) -> redisTemplate.boundHashOps(DictionariesKeyConstant.ADV_KEY_PREFIX.concat(shopAdvertisingSpaceEntityMap.get(k))).putAll(v.stream().collect(Collectors.toMap(ShopAdvertisementEntity::getId, a -> a, (k1, k2) -> k1))));
        }
        // 获取【广告】加载结束时间
        long shopAdvertisementEndTime = System.currentTimeMillis();
        log.info("缓存管理-->缓存初始化:【广告】加载完成, 用时" + (shopAdvertisementEndTime - addressLibraryEndTime) + "ms!");
        log.info("/** ==============================  【Banner导航】 ============================== **/");
        List<ShopBannerEntity> shopBannerEntityList = shopBannerService.list();
        if(CollectionUtils.isNotEmpty(shopBannerEntityList)){
            redisTemplate.boundHashOps(DictionariesKeyConstant.BANNER_KEY).putAll(shopBannerEntityList.stream().collect(Collectors.toMap(ShopBannerEntity::getId, a -> a, (k1, k2) -> k1)));
        }
        // 获取【Banner导航】加载结束时间
        long shopBannerEndTime = System.currentTimeMillis();
        log.info("缓存管理-->缓存初始化:【Banner导航】加载完成, 用时" + (shopBannerEndTime - shopAdvertisementEndTime) + "ms!");
        log.info("/** ==============================  【公告】 ============================== **/");
        List<ShopNoticeEntity> shopNoticeEntityList = shopNoticeService.list();
        if(CollectionUtils.isNotEmpty(shopNoticeEntityList)){
            redisTemplate.boundHashOps(DictionariesKeyConstant.NOTICE_KEY).putAll(shopNoticeEntityList.stream().collect(Collectors.toMap(ShopNoticeEntity::getId, a -> a, (k1, k2) -> k1)));
        }
        // 获取【公告】加载结束时间
        long shopNoticeEndTime = System.currentTimeMillis();
        log.info("缓存管理-->缓存初始化:【公告】加载完成, 用时" + (shopNoticeEndTime - shopBannerEndTime) + "ms!");
        log.info("/** ==============================  【轮播图】 ============================== **/");
        List<ShopCarouselMapEntity> shopCarouselMapEntityList = shopCarouselMapService.list();
        if(CollectionUtils.isNotEmpty(shopCarouselMapEntityList)){
            redisTemplate.boundHashOps(DictionariesKeyConstant.CAROUSEL_MAP_KEY).putAll(shopCarouselMapEntityList.stream().collect(Collectors.toMap(ShopCarouselMapEntity::getId, a -> a, (k1, k2) -> k1)));
        }
        // 获取【轮播图】加载结束时间
        long shopCarouselMapEndTime = System.currentTimeMillis();
        log.info("缓存管理-->缓存初始化:【轮播图】加载完成, 用时" + (shopCarouselMapEndTime - shopNoticeEndTime) + "ms!");
        log.info("/** ==============================  【物流公司】 ============================== **/");
        List<ShopLogisticsCompanyEntity> shopLogisticsCompanyEntityList = shopLogisticsCompanyService.list();
        if(CollectionUtils.isNotEmpty(shopLogisticsCompanyEntityList)){
            redisTemplate.boundHashOps(DictionariesKeyConstant.LOGISTICS_COMPANY_KEY).putAll(shopLogisticsCompanyEntityList.stream().collect(Collectors.toMap(ShopLogisticsCompanyEntity::getId, a -> a, (k1, k2) -> k1)));
        }
        // 获取【物流公司】加载结束时间
        long shopLogisticsCompanyEndTime = System.currentTimeMillis();
        log.info("缓存管理-->缓存初始化:【物流公司】加载完成, 用时" + (shopLogisticsCompanyEndTime - shopCarouselMapEndTime) + "ms!");
        log.info("缓存管理-->缓存初始化:服务结束!");
    }


}
