package com.company.project.service.impl;

import com.company.project.common.utils.DataResult;
import com.company.project.common.utils.DictionariesKeyConstant;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.company.project.mapper.ShopCarouselMapMapper;
import com.company.project.entity.ShopCarouselMapEntity;
import com.company.project.service.ShopCarouselMapService;

import javax.annotation.Resource;
import java.util.List;


@Service("shopCarouselMapService")
public class ShopCarouselMapServiceImpl extends ServiceImpl<ShopCarouselMapMapper, ShopCarouselMapEntity> implements ShopCarouselMapService {

    @Resource
    private ShopCarouselMapMapper shopCarouselMapMapper;

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public DataResult saveShopCarouselMapEntity(ShopCarouselMapEntity shopCarouselMap) {
        shopCarouselMapMapper.insert(shopCarouselMap);
        redisTemplate.boundHashOps(DictionariesKeyConstant.CAROUSEL_MAP_KEY).put(shopCarouselMap.getId(), shopCarouselMapMapper.selectById(shopCarouselMap.getId()));
        return DataResult.success();
    }

    @Override
    public DataResult removeShopCarouselMapEntityByIds(List<String> ids) {
        shopCarouselMapMapper.deleteBatchIds(ids);
        redisTemplate.boundHashOps(DictionariesKeyConstant.CAROUSEL_MAP_KEY).delete(ids.toArray());
        return DataResult.success();
    }

    @Override
    public DataResult updateShopCarouselMapEntityById(ShopCarouselMapEntity shopCarouselMap) {
        shopCarouselMapMapper.updateById(shopCarouselMap);
        redisTemplate.boundHashOps(DictionariesKeyConstant.CAROUSEL_MAP_KEY).put(shopCarouselMap.getId(), shopCarouselMapMapper.selectById(shopCarouselMap.getId()));
        return DataResult.success();
    }
}
