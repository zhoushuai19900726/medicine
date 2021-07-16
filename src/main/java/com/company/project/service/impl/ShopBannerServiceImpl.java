package com.company.project.service.impl;

import com.company.project.common.utils.DataResult;
import com.company.project.common.utils.DictionariesKeyConstant;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.company.project.mapper.ShopBannerMapper;
import com.company.project.entity.ShopBannerEntity;
import com.company.project.service.ShopBannerService;

import javax.annotation.Resource;
import java.util.List;


@Service("shopBannerService")
public class ShopBannerServiceImpl extends ServiceImpl<ShopBannerMapper, ShopBannerEntity> implements ShopBannerService {

    @Resource
    private ShopBannerMapper shopBannerMapper;

    @Resource
    private RedisTemplate redisTemplate;


    @Override
    public DataResult saveShopBannerEntity(ShopBannerEntity shopBanner) {
        shopBannerMapper.insert(shopBanner);
        redisTemplate.boundHashOps(DictionariesKeyConstant.BANNER_KEY).put(shopBanner.getId(), shopBannerMapper.selectById(shopBanner.getId()));
        return DataResult.success();
    }

    @Override
    public DataResult removeShopBannerEntityByIds(List<String> ids) {
        shopBannerMapper.deleteBatchIds(ids);
        redisTemplate.boundHashOps(DictionariesKeyConstant.BANNER_KEY).delete(ids.toArray());
        return DataResult.success();
    }

    @Override
    public DataResult updateShopBannerEntityById(ShopBannerEntity shopBanner) {
        shopBannerMapper.updateById(shopBanner);
        redisTemplate.boundHashOps(DictionariesKeyConstant.BANNER_KEY).put(shopBanner.getId(), shopBannerMapper.selectById(shopBanner.getId()));
        return DataResult.success();
    }


}
