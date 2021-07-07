package com.company.project.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.company.project.common.utils.DataResult;
import com.company.project.common.utils.NumberConstants;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.company.project.mapper.ShopMemberGradeMapper;
import com.company.project.entity.ShopMemberGradeEntity;
import com.company.project.service.ShopMemberGradeService;

import javax.annotation.Resource;


@Service("shopMemberGradeService")
public class ShopMemberGradeServiceImpl extends ServiceImpl<ShopMemberGradeMapper, ShopMemberGradeEntity> implements ShopMemberGradeService {

    @Resource
    private ShopMemberGradeMapper shopMemberGradeMapper;

    @Override
    public DataResult saveShopMemberGradeEntity(ShopMemberGradeEntity shopMemberGrade) {
        // 只保留一个默认
        if(NumberConstants.ONE_I.equals(shopMemberGrade.getIsDefault())){
            shopMemberGradeMapper.update(new ShopMemberGradeEntity(NumberConstants.ZERO_I), Wrappers.lambdaQuery());
        }
        return DataResult.success(shopMemberGradeMapper.insert(shopMemberGrade));
    }

    @Override
    public DataResult updateShopMemberGradeEntityById(ShopMemberGradeEntity shopMemberGrade) {
        // 只保留一个默认
        if(NumberConstants.ONE_I.equals(shopMemberGrade.getIsDefault())){
            shopMemberGradeMapper.update(new ShopMemberGradeEntity(NumberConstants.ZERO_I), Wrappers.lambdaQuery());
        }
        return DataResult.success(shopMemberGradeMapper.updateById(shopMemberGrade));
    }
}
