package com.company.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.project.common.utils.DelimiterConstants;
import com.company.project.common.utils.DictionariesKeyConstant;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.company.project.mapper.ShopMemberGrowthValueRecordMapper;
import com.company.project.entity.ShopMemberGrowthValueRecordEntity;
import com.company.project.service.ShopMemberGrowthValueRecordService;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;


@Service("shopMemberGrowthValueRecordService")
public class ShopMemberGrowthValueRecordServiceImpl extends ServiceImpl<ShopMemberGrowthValueRecordMapper, ShopMemberGrowthValueRecordEntity> implements ShopMemberGrowthValueRecordService {

    @Resource
    private ShopMemberGrowthValueRecordMapper shopMemberGrowthValueRecordMapper;

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public IPage<ShopMemberGrowthValueRecordEntity> listByPage(Page<ShopMemberGrowthValueRecordEntity> page, LambdaQueryWrapper<ShopMemberGrowthValueRecordEntity> wrapper) {
        return encapsulatingFieldName(shopMemberGrowthValueRecordMapper.selectPage(page, wrapper));
    }

    private IPage<ShopMemberGrowthValueRecordEntity> encapsulatingFieldName(IPage<ShopMemberGrowthValueRecordEntity> iPage) {
        // 会员集合
        List<ShopMemberGrowthValueRecordEntity> shopMemberGrowthValueRecordEntityList = iPage.getRecords();
        // 封装名称
        if (CollectionUtils.isNotEmpty(shopMemberGrowthValueRecordEntityList)) {
            // 执行封装
            shopMemberGrowthValueRecordEntityList.forEach(shopMemberGrowthValueRecordEntity -> shopMemberGrowthValueRecordEntity.setTypeName(getTypeName(shopMemberGrowthValueRecordEntity.getType())));
        }
        return iPage;
    }

    private String getTypeName(Integer type) {
        AtomicReference<String> typeName = new AtomicReference<>(DelimiterConstants.EMPTY_STR);
        redisTemplate.boundHashOps(DictionariesKeyConstant.GROWTH_VALUE_DETAILS_TYPE).values().forEach(obj -> {
            try {
                Map<String, String> map = BeanUtils.describe(obj);
                if (StringUtils.equals(map.get("value"), String.valueOf(type))) {
                    typeName.set(map.get("label"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return typeName.get();
    }
}
