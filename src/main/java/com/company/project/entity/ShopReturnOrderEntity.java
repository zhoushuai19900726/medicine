package com.company.project.entity;

import com.company.project.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;


import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 退款申请
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-07-24 09:52:58
 */
@Accessors(chain = true)
@NoArgsConstructor
@Data
@TableName("shop_return_order")
public class ShopReturnOrderEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId("id")
    private String id;

    /**
     * 订单号
     */
    @TableField("order_id")
    private String orderId;

    /**
     * 用户ID
     */
    @TableField("member_id")
    private String memberId;

    /**
     * 用户账号
     */
    @TableField("member_name")
    private String memberName;

    /**
     * 联系人
     */
    @TableField("linkman")
    private String linkman;

    /**
     * 联系人手机
     */
    @TableField("linkman_mobile")
    private String linkmanMobile;

    /**
     * 类型
     */
    @TableField("type")
    private Integer type;

    /**
     * 退款金额
     */
    @TableField("return_money")
    private Integer returnMoney;

    /**
     * 是否退运费
     */
    @TableField("is_return_freight")
    private Integer isReturnFreight;

    /**
     * 申请状态
     */
    @TableField("status")
    private Integer status;

    /**
     * 退货退款原因
     */
    @TableField("return_cause")
    private String returnCause;

    /**
     * 凭证图片
     */
    @TableField("evidence")
    private String evidence;

    /**
     * 问题描述
     */
    @TableField("description")
    private String description;

    /**
     * 处理备注
     */
    @TableField("remark")
    private String remark;

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

    @TableField(exist = false)
    private List<ShopReturnOrderDetailEntity> shopReturnOrderDetailEntityList;


}
