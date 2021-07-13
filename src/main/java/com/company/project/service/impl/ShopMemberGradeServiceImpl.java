package com.company.project.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.company.project.common.utils.DataResult;
import com.company.project.common.utils.DelimiterConstants;
import com.company.project.common.utils.NumberConstants;
import com.company.project.entity.ShopMemberGrowthValueEntity;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.company.project.mapper.ShopMemberGradeMapper;
import com.company.project.entity.ShopMemberGradeEntity;
import com.company.project.service.ShopMemberGradeService;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;


@Service("shopMemberGradeService")
public class ShopMemberGradeServiceImpl extends ServiceImpl<ShopMemberGradeMapper, ShopMemberGradeEntity> implements ShopMemberGradeService {

    @Resource
    private ShopMemberGradeMapper shopMemberGradeMapper;

    @Override
    public DataResult saveShopMemberGradeEntity(ShopMemberGradeEntity shopMemberGrade) {
        // 只保留一个默认
        if (NumberConstants.ONE_I.equals(shopMemberGrade.getIsDefault())) {
            shopMemberGradeMapper.update(new ShopMemberGradeEntity(NumberConstants.ZERO_I), Wrappers.lambdaQuery());
        }
        return DataResult.success(shopMemberGradeMapper.insert(shopMemberGrade));
    }

    @Override
    public DataResult updateShopMemberGradeEntityById(ShopMemberGradeEntity shopMemberGrade) {
        // 只保留一个默认
        if (NumberConstants.ONE_I.equals(shopMemberGrade.getIsDefault())) {
            shopMemberGradeMapper.update(new ShopMemberGradeEntity(NumberConstants.ZERO_I), Wrappers.lambdaQuery());
        }
        return DataResult.success(shopMemberGradeMapper.updateById(shopMemberGrade));
    }

    @Override
    public ShopMemberGradeEntity calculationMemberGradeByGrowthValue(ShopMemberGrowthValueEntity shopMemberGrowthValueEntity) {
        // 查询所有会员等级
        List<ShopMemberGradeEntity> shopMemberGradeEntityList = shopMemberGradeMapper.selectList(Wrappers.<ShopMemberGradeEntity>lambdaQuery().orderByAsc(ShopMemberGradeEntity::getIntegration));
        // 升级后等级
        AtomicReference<ShopMemberGradeEntity> result = new AtomicReference<>(new ShopMemberGradeEntity());
        shopMemberGradeEntityList.forEach(shopMemberGradeEntity -> {
            if (shopMemberGrowthValueEntity.getGrowthValue().compareTo(new BigDecimal(shopMemberGradeEntity.getIntegration())) >= NumberConstants.ZERO) {
                result.set(shopMemberGradeEntity);
            }
        });
        return result.get();
    }
}
