package com.company.project.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.company.project.common.exception.code.BusinessResponseCode;
import com.company.project.common.utils.DataResult;
import com.company.project.common.utils.DictionariesKeyConstant;
import com.company.project.entity.ShopAdvertisementEntity;
import com.company.project.mapper.ShopAdvertisementMapper;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.company.project.mapper.ShopAdvertisingSpaceMapper;
import com.company.project.entity.ShopAdvertisingSpaceEntity;
import com.company.project.service.ShopAdvertisingSpaceService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Transactional
@Service("shopAdvertisingSpaceService")
public class ShopAdvertisingSpaceServiceImpl extends ServiceImpl<ShopAdvertisingSpaceMapper, ShopAdvertisingSpaceEntity> implements ShopAdvertisingSpaceService {

    @Resource
    private ShopAdvertisingSpaceMapper shopAdvertisingSpaceMapper;

    @Resource
    private ShopAdvertisementMapper shopAdvertisementMapper;

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public DataResult saveShopAdvertisingSpaceEntity(ShopAdvertisingSpaceEntity shopAdvertisingSpaceEntity) {
        List<ShopAdvertisingSpaceEntity> shopAdvertisingSpaceEntityList = shopAdvertisingSpaceMapper.selectList(Wrappers.<ShopAdvertisingSpaceEntity>lambdaQuery().eq(ShopAdvertisingSpaceEntity::getGetTag, shopAdvertisingSpaceEntity.getGetTag()));
        if (CollectionUtils.isNotEmpty(shopAdvertisingSpaceEntityList)) {
            return DataResult.fail(BusinessResponseCode.GET_TAG_REPEAT.getMsg());
        }
        return DataResult.success(shopAdvertisingSpaceMapper.insert(shopAdvertisingSpaceEntity));
    }

    @Override
    public DataResult updateShopAdvertisingSpaceEntityById(ShopAdvertisingSpaceEntity shopAdvertisingSpaceEntity) {
        List<ShopAdvertisingSpaceEntity> shopAdvertisingSpaceEntityList = shopAdvertisingSpaceMapper.selectList(Wrappers.<ShopAdvertisingSpaceEntity>lambdaQuery().eq(ShopAdvertisingSpaceEntity::getGetTag, shopAdvertisingSpaceEntity.getGetTag()).ne(ShopAdvertisingSpaceEntity::getId, shopAdvertisingSpaceEntity.getId()));
        if (CollectionUtils.isNotEmpty(shopAdvertisingSpaceEntityList)) {
            return DataResult.fail(BusinessResponseCode.GET_TAG_REPEAT.getMsg());
        }
        ShopAdvertisingSpaceEntity result = shopAdvertisingSpaceMapper.selectById(shopAdvertisingSpaceEntity.getId());
        if (Objects.nonNull(result)) {
            // 保存更新
            shopAdvertisingSpaceMapper.updateById(shopAdvertisingSpaceEntity);
            // 更新缓存
            List<ShopAdvertisementEntity> shopAdvertisementEntityList = redisTemplate.boundHashOps(DictionariesKeyConstant.ADV_KEY_PREFIX.concat(shopAdvertisingSpaceEntity.getGetTag())).values();
            Map<String, ShopAdvertisementEntity> shopAdvertisementEntityMap = shopAdvertisementEntityList.stream().collect(Collectors.toMap(ShopAdvertisementEntity::getId, a -> a, (k1, k2) -> k1));
            redisTemplate.delete(DictionariesKeyConstant.ADV_KEY_PREFIX.concat(shopAdvertisingSpaceEntity.getGetTag()));
            redisTemplate.boundHashOps(DictionariesKeyConstant.DICT_KEY_PREFIX.concat(result.getGetTag())).putAll(shopAdvertisementEntityMap);
        }
        return DataResult.success();
    }

    @Override
    public DataResult removeShopAdvertisingSpaceEntityByIds(List<String> ids) {
        List<ShopAdvertisingSpaceEntity> shopAdvertisingSpaceEntityList = shopAdvertisingSpaceMapper.selectBatchIds(ids);
        if (CollectionUtils.isNotEmpty(shopAdvertisingSpaceEntityList)) {
            // 删除广告位
            shopAdvertisingSpaceMapper.deleteBatchIds(ids);
            // 删除广告
            shopAdvertisementMapper.delete(Wrappers.<ShopAdvertisementEntity>lambdaQuery().in(ShopAdvertisementEntity::getSpaceId, ids));
            // 删除缓存
            shopAdvertisingSpaceEntityList.forEach(shopAdvertisingSpaceEntity -> redisTemplate.delete(DictionariesKeyConstant.ADV_KEY_PREFIX.concat(shopAdvertisingSpaceEntity.getGetTag())));
        }
        return DataResult.success(ids);
    }

}
