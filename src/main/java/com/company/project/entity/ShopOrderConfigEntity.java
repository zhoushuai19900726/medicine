package com.company.project.entity;

import com.company.project.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;


import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 订单设置
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-07-20 11:29:38
 */
@Accessors(chain = true)
@NoArgsConstructor
@Data
@TableName("shop_order_config")
public class ShopOrderConfigEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId("id")
    private String id;

    /**
     * 正常订单超时时间（分）
     */
    @TableField("order_timeout")
    private Integer orderTimeout;

    /**
     * 秒杀订单超时时间（分）
     */
    @TableField("seckill_timeout")
    private Integer seckillTimeout;

    /**
     * 自动收货（天）
     */
    @TableField("take_timeout")
    private Integer takeTimeout;

    /**
     * 售后期限
     */
    @TableField("service_timeout")
    private Integer serviceTimeout;

    /**
     * 自动五星好评
     */
    @TableField("comment_timeout")
    private Integer commentTimeout;

    /**
     *
     */
    @TableField(fill = FieldFill.INSERT)
    private String createId;
    @TableField(exist = false)
    private String createName;

    /**
     *
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    // 查询条件
    @TableField(exist = false)
    private String createStartTime;
    // 查询条件
    @TableField(exist = false)
    private String createEndTime;

    /**
     *
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateId;
    @TableField(exist = false)
    private String updateName;

    /**
     *
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     *
     */
    @TableField(fill = FieldFill.INSERT)
    @TableLogic // 注释后进行物理删除
    private Integer deleted;


}
