package com.company.project.common.filter;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.company.project.common.utils.Constant;
import com.company.project.common.utils.DictionariesKeyConstant;
import com.company.project.common.utils.NumberConstants;
import com.company.project.entity.AddressLibraryEntity;
import com.company.project.entity.SysDictDetailEntity;
import com.company.project.entity.SysDictEntity;
import com.company.project.service.AddressLibraryService;
import com.company.project.service.SysDictDetailService;
import com.company.project.service.SysDictService;
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
        // 获取【数据字典】加载结束时间
        long addressLibraryEndTime = System.currentTimeMillis();
        log.info("缓存管理-->缓存初始化:【地址库】加载完成, 用时" + (addressLibraryEndTime - dictionaryGroupEndTime) + "ms!");
        log.info("缓存管理-->缓存初始化:服务结束!");
    }


}
