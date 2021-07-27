package com.company.project.mapper;

import com.company.project.entity.ShopOrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * 订单表
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-07-16 15:52:54
 */
public interface ShopOrderMapper extends BaseMapper<ShopOrderEntity> {

    @Update("UPDATE shop_order set consign_time = null, end_time = null, close_time = null WHERE id = #{id}")
    void resetConsignTime(@Param("id") String id);

}
