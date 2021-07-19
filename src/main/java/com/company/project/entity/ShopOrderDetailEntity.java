package com.company.project.entity;

import com.company.project.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 订单明细
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-07-16 15:52:56
 */
@Accessors(chain = true)
@NoArgsConstructor
@Data
@TableName("shop_order_detail")
public class ShopOrderDetailEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId("id")
    private String id;

    /**
     * 1级分类
     */
    @TableField("category_id1")
    private String categoryId1;

    /**
     * 2级分类
     */
    @TableField("category_id2")
    private String categoryId2;

    /**
     * 3级分类
     */
    @TableField("category_id3")
    private String categoryId3;

    /**
     * SPU_ID
     */
    @TableField("spu_id")
    private String spuId;

    /**
     * SKU_ID
     */
    @TableField("sku_id")
    private String skuId;

    /**
     * 订单ID
     */
    @TableField("order_id")
    private String orderId;

    /**
     * 商品名称
     */
    @TableField("name")
    private String name;

    /**
     * 单价
     */
    @TableField("price")
    private BigDecimal price;

    /**
     * 数量
     */
    @TableField("num")
    private Integer num;

    /**
     * 总金额
     */
    @TableField("money")
    private BigDecimal money;

    /**
     * 实付金额
     */
    @TableField("pay_money")
    private BigDecimal payMoney;

    /**
     * 图片地址
     */
    @TableField("image")
    private String image;

    /**
     * 重量
     */
    @TableField("weight")
    private BigDecimal weight;

    /**
     * 运费
     */
    @TableField("post_fee")
    private BigDecimal postFee;

    /**
     * 是否退货,0:未退货，1：已退货
     */
    @TableField("is_return")
    private Integer isReturn;


}
