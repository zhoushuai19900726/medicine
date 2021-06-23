package com.company.project.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.company.project.common.exception.code.BaseResponseCode;
import com.company.project.common.exception.code.BusinessResponseCode;
import com.company.project.common.utils.DataResult;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.company.project.mapper.ShopSpuMapper;
import com.company.project.entity.ShopSpuEntity;
import com.company.project.service.ShopSpuService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Objects;

@Transactional
@Service("shopSpuService")
public class ShopSpuServiceImpl extends ServiceImpl<ShopSpuMapper, ShopSpuEntity> implements ShopSpuService {

    @Resource
    private ShopSpuMapper shopSpuMapper;

    /**
     * 保存商品
     *
     * @param shopSpuEntity
     * @return
     */
    @Override
    public DataResult saveShopSpuEntity(ShopSpuEntity shopSpuEntity) {
        // 校验SPU商品货号
        ShopSpuEntity queryResult = shopSpuMapper.selectOne(Wrappers.<ShopSpuEntity>lambdaQuery().eq(ShopSpuEntity::getSn, shopSpuEntity.getSn()));
        if (Objects.nonNull(queryResult)) {
            return DataResult.fail(BusinessResponseCode.SPU_SN_REPEATED_EXISTENCE.getMsg());
        }


        shopSpuMapper.insert(shopSpuEntity);




        return DataResult.success(shopSpuEntity);
    }

    @Override
    public DataResult updateShopSpuEntityById(ShopSpuEntity shopSpuEntity) {
        // 校验SPU商品货号
        ShopSpuEntity queryResult = shopSpuMapper.selectOne(Wrappers.<ShopSpuEntity>lambdaQuery().eq(ShopSpuEntity::getSn, shopSpuEntity.getSn()));
        if (Objects.nonNull(queryResult)) {
            return DataResult.fail(BusinessResponseCode.SPU_SN_REPEATED_EXISTENCE.getMsg());
        }


        shopSpuMapper.updateById(shopSpuEntity);



        return DataResult.success(shopSpuEntity);
    }
}
