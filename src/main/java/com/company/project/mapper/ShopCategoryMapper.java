package com.company.project.mapper;

import com.company.project.entity.ShopCategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 商品分类
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-06-16 10:33:59
 */
public interface ShopCategoryMapper extends BaseMapper<ShopCategoryEntity> {

    @Update({"<script> update shop_category  " +
            "<trim prefix ='set' prefixOverrides=',' > " +
            "<trim prefix ='goods_num = case' suffix='end'>" +
            "<foreach collection ='shopCategoryEntityList' item ='shopCategoryEntity' index = 'index'> " +
            "when id = #{shopCategoryEntity.id} then (goods_num + #{shopCategoryEntity.goodsNum}) " +
            "</foreach>" +
            "</trim> " +
            "</trim> " +
            "where  id in " +
            "<foreach collection ='shopCategoryEntityList' item ='shopCategoryEntity' index ='index' separator=',' open='(' close=')'  > " +
            "#{shopCategoryEntity.id} " +
            "</foreach> </script>"})
    void updateCount(@Param("shopCategoryEntityList") List<ShopCategoryEntity> shopCategoryEntityList);

}
