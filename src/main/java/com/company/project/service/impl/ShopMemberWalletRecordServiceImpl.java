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

import com.company.project.mapper.ShopMemberWalletRecordMapper;
import com.company.project.entity.ShopMemberWalletRecordEntity;
import com.company.project.service.ShopMemberWalletRecordService;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;


@Service("shopMemberWalletRecordService")
public class ShopMemberWalletRecordServiceImpl extends ServiceImpl<ShopMemberWalletRecordMapper, ShopMemberWalletRecordEntity> implements ShopMemberWalletRecordService {

    @Resource
    private ShopMemberWalletRecordMapper shopMemberWalletRecordMapper;

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public IPage<ShopMemberWalletRecordEntity> listByPage(Page<ShopMemberWalletRecordEntity> page, LambdaQueryWrapper<ShopMemberWalletRecordEntity> wrapper) {
        return encapsulatingFieldName(shopMemberWalletRecordMapper.selectPage(page, wrapper));
    }

    private IPage<ShopMemberWalletRecordEntity> encapsulatingFieldName(IPage<ShopMemberWalletRecordEntity> iPage) {
        // 会员集合
        List<ShopMemberWalletRecordEntity> shopMemberWalletRecordEntityList = iPage.getRecords();
        // 封装名称
        if (CollectionUtils.isNotEmpty(shopMemberWalletRecordEntityList)) {
            // 执行封装
            shopMemberWalletRecordEntityList.forEach(shopMemberWalletRecordEntity -> shopMemberWalletRecordEntity.setTypeName(getTypeName(shopMemberWalletRecordEntity.getType())));
        }
        return iPage;
    }

    private String getTypeName(Integer type) {
        AtomicReference<String> typeName = new AtomicReference<>(DelimiterConstants.EMPTY_STR);
        redisTemplate.boundHashOps(DictionariesKeyConstant.WALLET_DETAILS_TYPE).values().forEach(obj -> {
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
