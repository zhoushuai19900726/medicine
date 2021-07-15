package com.company.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.project.common.utils.DataResult;
import com.company.project.common.utils.DelimiterConstants;
import com.company.project.common.utils.DictionariesKeyConstant;
import com.company.project.entity.ShopAdvertisingSpaceEntity;
import com.company.project.mapper.ShopAdvertisingSpaceMapper;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.company.project.mapper.ShopAdvertisementMapper;
import com.company.project.entity.ShopAdvertisementEntity;
import com.company.project.service.ShopAdvertisementService;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


@Service("shopAdvertisementService")
public class ShopAdvertisementServiceImpl extends ServiceImpl<ShopAdvertisementMapper, ShopAdvertisementEntity> implements ShopAdvertisementService {

    @Resource
    private ShopAdvertisementMapper shopAdvertisementMapper;

    @Resource
    private ShopAdvertisingSpaceMapper shopAdvertisingSpaceMapper;

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public IPage<ShopAdvertisementEntity> listByPage(Page<ShopAdvertisementEntity> page, LambdaQueryWrapper<ShopAdvertisementEntity> wrapper) {
        return encapsulatingFieldName(shopAdvertisementMapper.selectPage(page, wrapper));
    }

    @Override
    public DataResult saveShopAdvertisementEntity(ShopAdvertisementEntity shopAdvertisement) {
        // 保存新增
        shopAdvertisementMapper.insert(shopAdvertisement);
        // 加入缓存
        ShopAdvertisementEntity result = shopAdvertisementMapper.selectById(shopAdvertisement.getId());
        ShopAdvertisingSpaceEntity shopAdvertisingSpaceEntity = shopAdvertisingSpaceMapper.selectById(result.getSpaceId());
        if (Objects.nonNull(shopAdvertisingSpaceEntity)) {
            redisTemplate.boundHashOps(DictionariesKeyConstant.ADV_KEY_PREFIX.concat(shopAdvertisingSpaceEntity.getGetTag())).put(result.getId(), result);
        }
        return DataResult.success();
    }

    @Override
    public DataResult removeShopAdvertisementEntityByIds(List<String> ids) {
        List<ShopAdvertisementEntity> shopAdvertisementEntityList = shopAdvertisementMapper.selectBatchIds(ids);
        if (CollectionUtils.isNotEmpty(shopAdvertisementEntityList)) {
            Map<String, List<ShopAdvertisementEntity>> groupBy = shopAdvertisementEntityList.stream().collect(Collectors.groupingBy(ShopAdvertisementEntity::getSpaceId));
            List<ShopAdvertisingSpaceEntity> shopAdvertisingSpaceEntityList = shopAdvertisingSpaceMapper.selectBatchIds(groupBy.keySet());
            Map<String, String> shopAdvertisingSpaceEntityMap = shopAdvertisingSpaceEntityList.stream().collect(Collectors.toMap(ShopAdvertisingSpaceEntity::getId, ShopAdvertisingSpaceEntity::getGetTag, (k1, k2) -> k1));
            groupBy.forEach((k, v) -> redisTemplate.boundHashOps(DictionariesKeyConstant.ADV_KEY_PREFIX.concat(shopAdvertisingSpaceEntityMap.get(k))).delete(v.stream().map(ShopAdvertisementEntity::getId).toArray()));
        }
        return DataResult.success(shopAdvertisementMapper.deleteBatchIds(ids));
    }

    @Override
    public DataResult updateShopAdvertisementEntityById(ShopAdvertisementEntity shopAdvertisement) {
        // 保存更新
        shopAdvertisementMapper.updateById(shopAdvertisement);
        // 加入缓存
        ShopAdvertisementEntity result = shopAdvertisementMapper.selectById(shopAdvertisement.getId());
        ShopAdvertisingSpaceEntity shopAdvertisingSpaceEntity = shopAdvertisingSpaceMapper.selectById(result.getSpaceId());
        if (Objects.nonNull(shopAdvertisingSpaceEntity)) {
            redisTemplate.boundHashOps(DictionariesKeyConstant.ADV_KEY_PREFIX.concat(shopAdvertisingSpaceEntity.getGetTag())).put(result.getId(), result);
        }
        return DataResult.success();
    }

    private IPage<ShopAdvertisementEntity> encapsulatingFieldName(IPage<ShopAdvertisementEntity> iPage) {
        // 会员集合
        List<ShopAdvertisementEntity> shopAdvertisementEntityList = iPage.getRecords();
        // 封装名称
        if (CollectionUtils.isNotEmpty(shopAdvertisementEntityList)) {
            // 查询结果封装
            Map<String, String> advertisingSpaceMap = Maps.newHashMap();
            // 提取广告位ID
            List<String> advertisingSpaceIdList = shopAdvertisementEntityList.stream().map(ShopAdvertisementEntity::getSpaceId).collect(Collectors.toList());
            // 查询广告位
            List<ShopAdvertisingSpaceEntity> shopAdvertisingSpaceEntityList = shopAdvertisingSpaceMapper.selectBatchIds(advertisingSpaceIdList);
            if (CollectionUtils.isNotEmpty(shopAdvertisingSpaceEntityList)) {
                advertisingSpaceMap.putAll(shopAdvertisingSpaceEntityList.stream().collect(Collectors.toMap(ShopAdvertisingSpaceEntity::getId, ShopAdvertisingSpaceEntity::getName, (k1, k2) -> k1)));
            }
            // 执行封装
            shopAdvertisementEntityList.forEach(shopAdvertisementEntity -> shopAdvertisementEntity.setSpaceName(advertisingSpaceMap.getOrDefault(String.valueOf(shopAdvertisementEntity.getSpaceId()), DelimiterConstants.EMPTY_STR)));
        }
        return iPage;
    }


}
