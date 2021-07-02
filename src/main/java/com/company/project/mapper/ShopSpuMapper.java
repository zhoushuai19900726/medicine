package com.company.project.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.project.entity.ShopSpuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 商品SPU
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-06-17 10:25:25
 */
public interface ShopSpuMapper extends BaseMapper<ShopSpuEntity> {

    @Select("SELECT * FROM shop_spu where id = ${id}")
    ShopSpuEntity selectShopSpuEntityById(@Param("id") String id);

    @Select("SELECT * FROM shop_spu ${ew.customSqlSegment}")
    IPage<ShopSpuEntity> recycleBinListByPage(IPage<ShopSpuEntity> page, @Param(Constants.WRAPPER) Wrapper<ShopSpuEntity> wrapper);

    @Update("UPDATE shop_spu SET deleted = 0 where id = ${id}")
    void reductionShopSpuEntityById(@Param("id") String id);
}
