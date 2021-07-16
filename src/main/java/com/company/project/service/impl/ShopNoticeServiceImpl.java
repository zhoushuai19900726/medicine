package com.company.project.service.impl;

import com.company.project.common.utils.DataResult;
import com.company.project.common.utils.DictionariesKeyConstant;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.company.project.mapper.ShopNoticeMapper;
import com.company.project.entity.ShopNoticeEntity;
import com.company.project.service.ShopNoticeService;

import javax.annotation.Resource;
import java.util.List;


@Service("shopNoticeService")
public class ShopNoticeServiceImpl extends ServiceImpl<ShopNoticeMapper, ShopNoticeEntity> implements ShopNoticeService {

    @Resource
    private ShopNoticeMapper shopNoticeMapper;

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public DataResult saveShopNoticeEntity(ShopNoticeEntity shopNotice) {
        shopNoticeMapper.insert(shopNotice);
        redisTemplate.boundHashOps(DictionariesKeyConstant.NOTICE_KEY).put(shopNotice.getId(), shopNoticeMapper.selectById(shopNotice.getId()));
        return DataResult.success();
    }

    @Override
    public DataResult removeShopNoticeEntityByIds(List<String> ids) {
        shopNoticeMapper.deleteBatchIds(ids);
        redisTemplate.boundHashOps(DictionariesKeyConstant.NOTICE_KEY).delete(ids.toArray());
        return DataResult.success();
    }

    @Override
    public DataResult updateShopNoticeEntityById(ShopNoticeEntity shopNotice) {
        shopNoticeMapper.updateById(shopNotice);
        redisTemplate.boundHashOps(DictionariesKeyConstant.NOTICE_KEY).put(shopNotice.getId(), shopNoticeMapper.selectById(shopNotice.getId()));
        return DataResult.success();
    }
}
