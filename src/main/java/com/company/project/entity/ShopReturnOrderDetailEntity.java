package com.company.project.entity;

import com.company.project.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;


import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 退款申请明细
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-07-24 09:53:00
 */
@Accessors(chain = true)
@NoArgsConstructor
@Data
@TableName("shop_return_order_detail")
public class ShopReturnOrderDetailEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId("id")
    private String id;

    /**
     * 分类ID
     */
    @TableField("category_id")
    private String categoryId;

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
     * 商品名称
     */
    @TableField("name")
    private String name;

    /**
     * 订单ID
     */
    @TableField("order_id")
    private String orderId;

    /**
     * 订单明细ID
     */
    @TableField("order_detail_id")
    private String orderDetailId;

    /**
     * 退货订单ID
     */
    @TableField("return_order_id")
    private String returnOrderId;

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
     * 支付金额
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
     *
     */
    @TableField(fill = FieldFill.INSERT)
    private String createId;
    @TableField(exist = false)
    private String createName;

    /**
     * 申请时间
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
     * 处理时间
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
