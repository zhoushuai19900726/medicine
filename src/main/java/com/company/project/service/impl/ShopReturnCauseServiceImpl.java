package com.company.project.service.impl;

import com.company.project.common.utils.DataResult;
import com.company.project.common.utils.DictionariesKeyConstant;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.company.project.mapper.ShopReturnCauseMapper;
import com.company.project.entity.ShopReturnCauseEntity;
import com.company.project.service.ShopReturnCauseService;

import javax.annotation.Resource;
import java.util.List;


@Service("shopReturnCauseService")
public class ShopReturnCauseServiceImpl extends ServiceImpl<ShopReturnCauseMapper, ShopReturnCauseEntity> implements ShopReturnCauseService {

    @Resource
    private ShopReturnCauseMapper shopReturnCauseMapper;

    @Resource
    private RedisTemplate redisTemplate;


    @Override
    public DataResult saveShopReturnCauseEntity(ShopReturnCauseEntity shopReturnCause) {
        shopReturnCauseMapper.insert(shopReturnCause);
        redisTemplate.boundHashOps(DictionariesKeyConstant.RETURN_REASON_KEY).put(shopReturnCause.getId(), shopReturnCauseMapper.selectById(shopReturnCause.getId()));
        return DataResult.success();
    }

    @Override
    public DataResult removeShopReturnCauseEntityByIds(List<String> ids) {
        shopReturnCauseMapper.deleteBatchIds(ids);
        redisTemplate.boundHashOps(DictionariesKeyConstant.RETURN_REASON_KEY).delete(ids.toArray());
        return DataResult.success();
    }

    @Override
    public DataResult updateShopReturnCauseEntityById(ShopReturnCauseEntity shopReturnCause) {
        shopReturnCauseMapper.updateById(shopReturnCause);
        redisTemplate.boundHashOps(DictionariesKeyConstant.RETURN_REASON_KEY).put(shopReturnCause.getId(), shopReturnCauseMapper.selectById(shopReturnCause.getId()));
        return DataResult.success();
    }
}
