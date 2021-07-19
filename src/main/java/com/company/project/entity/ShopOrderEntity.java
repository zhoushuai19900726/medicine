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
 * 订单表
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-07-16 15:52:54
 */
@Accessors(chain = true)
@NoArgsConstructor
@Data
@TableName("shop_order")
public class ShopOrderEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单id
     */
    @TableId("id")
    private String id;

    /**
     * 支付单号
     */
    @TableField("transaction_id")
    private String transactionId;

    /**
     * 交易流水号
     */
    @TableField("serial_number")
    private String serialNumber;

    /**
     * 商家ID
     */
    @TableField("seller_id")
    private String sellerId;

    /**
     * 商家名称
     */
    @TableField("seller_name")
    private String sellerName;

    /**
     * 数量合计
     */
    @TableField("total_num")
    private Integer totalNum;

    /**
     * 金额合计
     */
    @TableField("total_money")
    private BigDecimal totalMoney;

    /**
     * 优惠金额
     */
    @TableField("pre_money")
    private BigDecimal preMoney;

    /**
     * 邮费
     */
    @TableField("post_fee")
    private BigDecimal postFee;

    /**
     * 实付金额
     */
    @TableField("pay_money")
    private BigDecimal payMoney;

    /**
     * 支付类型，1、在线支付、0 货到付款
     */
    @TableField("pay_type")
    private String payType;

    /**
     * 订单创建时间
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
     * 订单更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 付款时间
     */
    @TableField("pay_time")
    private Date payTime;
    // 查询条件
    @TableField(exist = false)
    private String payStartTime;
    // 查询条件
    @TableField(exist = false)
    private String payEndTime;

    /**
     * 发货时间
     */
    @TableField("consign_time")
    private Date consignTime;

    /**
     * 交易完成时间
     */
    @TableField("end_time")
    private Date endTime;

    /**
     * 交易关闭时间
     */
    @TableField(value = "close_time", updateStrategy = FieldStrategy.IGNORED)
    private Date closeTime;

    /**
     * 物流名称
     */
    @TableField("shipping_id")
    private String shippingId;

    /**
     * 物流单号
     */
    @TableField("shipping_code")
    private String shippingCode;

    /**
     * 用户ID
     */
    @TableField("buyer_id")
    private String buyerId;

    /**
     * 用户名称
     */
    @TableField("buyer_name")
    private String buyerName;

    /**
     * 买家留言
     */
    @TableField("buyer_message")
    private String buyerMessage;

    /**
     * 是否评价
     */
    @TableField("buyer_rate")
    private Integer buyerRate;

    /**
     * 收货人
     */
    @TableField("receiver_contact")
    private String receiverContact;

    /**
     * 收货人手机
     */
    @TableField("receiver_mobile")
    private String receiverMobile;

    /**
     * 收货人地址
     */
    @TableField("receiver_address")
    private String receiverAddress;

    /**
     * 订单来源：1:web，2：app，3：微信公众号，4：微信小程序  5 H5手机页面
     */
    @TableField("source_type")
    private Integer sourceType;

    /**
     * 订单状态,0:未完成,1:已完成，2：已退货
     */
    @TableField("order_status")
    private Integer orderStatus;

    /**
     * 支付状态,0:未支付，1：已支付，2：支付失败
     */
    @TableField("pay_status")
    private Integer payStatus;

    /**
     * 发货状态,0:未发货，1：已发货，2：已收货
     */
    @TableField("consign_status")
    private Integer consignStatus;

    /**
     * 紧急发货标记,0:非紧急，1：紧急
     */
    @TableField("urgent_delivery")
    private Integer urgentDelivery;

    /**
     * 是否删除
     */
    @TableField(fill = FieldFill.INSERT)
    @TableLogic // 注释后进行物理删除
    private Integer deleted;

    public ShopOrderEntity(Date closeTime, Integer orderStatus) {
        this.closeTime = closeTime;
        this.orderStatus = orderStatus;
    }
}
