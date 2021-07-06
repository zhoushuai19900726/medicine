package com.company.project.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.company.project.entity.ShopSpuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 商品SPU
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-06-17 10:25:25
 */
public interface ShopSpuMapper extends BaseMapper<ShopSpuEntity> {

    @Select("SELECT * FROM shop_spu ${ew.customSqlSegment}")
    List<ShopSpuEntity> listByCondition(@Param(Constants.WRAPPER) Wrapper<ShopSpuEntity> wrapper);

    @Select("SELECT COUNT(0) FROM shop_spu ${ew.customSqlSegment}")
    Integer countByCondition(@Param(Constants.WRAPPER) Wrapper<ShopSpuEntity> wrapper);

    @Select("SELECT * FROM shop_spu WHERE id = #{id}")
    ShopSpuEntity selectShopSpuEntityById(@Param("id") String id);

    @Select("SELECT * FROM shop_spu WHERE sn = #{unique}")
    ShopSpuEntity selectShopSpuEntityByUnique(@Param("unique") String unique);

    @Select("SELECT * FROM shop_spu ${ew.customSqlSegment}")
    IPage<ShopSpuEntity> recycleBinListByPage(IPage<ShopSpuEntity> page, @Param(Constants.WRAPPER) Wrapper<ShopSpuEntity> wrapper);

    @Update("UPDATE shop_spu SET deleted = 0, status = '0' WHERE id = #{id}")
    void reductionShopSpuEntityById(@Param("id") String id);

    @Delete("<script> DELETE FROM shop_spu WHERE id IN  <foreach collection ='ids' item ='id' index ='index' separator=',' open='(' close=')'  > #{id} </foreach> </script>")
    void absolutelyDeleteByIds(@Param("ids") List<String> ids);
}
