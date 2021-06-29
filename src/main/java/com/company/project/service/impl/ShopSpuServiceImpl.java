package com.company.project.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.company.project.common.exception.code.BaseResponseCode;
import com.company.project.common.exception.code.BusinessResponseCode;
import com.company.project.common.utils.DataResult;
import com.company.project.common.utils.DelimiterConstants;
import com.company.project.entity.ShopSkuEntity;
import com.company.project.mapper.ShopSkuMapper;
import com.google.common.base.Joiner;
import com.google.common.collect.Sets;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.company.project.mapper.ShopSpuMapper;
import com.company.project.entity.ShopSpuEntity;
import com.company.project.service.ShopSpuService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Transactional
@Service("shopSpuService")
public class ShopSpuServiceImpl extends ServiceImpl<ShopSpuMapper, ShopSpuEntity> implements ShopSpuService {

    @Resource
    private ShopSpuMapper shopSpuMapper;

    @Resource
    private ShopSkuMapper shopSkuMapper;

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
        List<ShopSkuEntity> shopSkuEntityList = shopSpuEntity.getShopSkuEntityList();
        if (CollectionUtils.isEmpty(shopSkuEntityList)) {
            return DataResult.fail(BusinessResponseCode.SKU_NULL.getMsg());
        }
        // 校验SKU商品货号
        List<String> skuSnList = shopSkuEntityList.stream().map(ShopSkuEntity::getSn).filter(StringUtils::isNotBlank).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(skuSnList)) {
            return DataResult.fail(BusinessResponseCode.SKU_NULL.getMsg());
        }
        if(Sets.newHashSet(skuSnList).size() != skuSnList.size()){
            return DataResult.fail(BusinessResponseCode.SKU_REPEAT.getMsg());
        }
        List<ShopSkuEntity> queryResultList = shopSkuMapper.selectList(Wrappers.<ShopSkuEntity>lambdaQuery().in(ShopSkuEntity::getSn, skuSnList));
        if (CollectionUtils.isNotEmpty(queryResultList)) {
            return DataResult.fail(BusinessResponseCode.SKU_SN_REPEATED_EXISTENCE.getMsg().concat(DelimiterConstants.COLON).concat(Joiner.on(DelimiterConstants.COMMA).skipNulls().join(queryResultList.stream().map(ShopSkuEntity::getSn).collect(Collectors.toList()))));
        }
        // 保存SPU
        shopSpuMapper.insert(shopSpuEntity);
        // 保存SKU
        shopSkuEntityList.forEach(shopSkuEntity -> {
            shopSkuEntity.setSpuId(shopSpuEntity.getId());
            shopSkuMapper.insert(shopSkuEntity);
        });
        return DataResult.success(shopSpuEntity);
    }

    @Override
    public DataResult updateShopSpuEntityById(ShopSpuEntity shopSpuEntity) {
        // 校验SPU商品货号
        ShopSpuEntity queryResult = shopSpuMapper.selectOne(Wrappers.<ShopSpuEntity>lambdaQuery().eq(ShopSpuEntity::getSn, shopSpuEntity.getSn()));
        if (Objects.nonNull(queryResult) && !StringUtils.equals(queryResult.getId(), shopSpuEntity.getId())) {
            return DataResult.fail(BusinessResponseCode.SPU_SN_REPEATED_EXISTENCE.getMsg());
        }


        shopSpuMapper.updateById(shopSpuEntity);


        return DataResult.success(shopSpuEntity);
    }
}
