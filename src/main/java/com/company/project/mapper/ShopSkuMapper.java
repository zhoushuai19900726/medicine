package com.company.project.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.company.project.entity.ShopSkuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 商品SKU
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-06-17 10:25:18
 */
public interface ShopSkuMapper extends BaseMapper<ShopSkuEntity> {

    @Select("SELECT * FROM shop_sku ${ew.customSqlSegment}")
    List<ShopSkuEntity> listByCondition(@Param(Constants.WRAPPER) Wrapper<ShopSkuEntity> wrapper);

    @Update("UPDATE shop_sku SET deleted = 0 where spu_id = ${spuId}")
    void reductionShopSpuEntityBySpuId(@Param("spuId") String spuId);
}
