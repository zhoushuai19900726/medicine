package com.company.project.entity;

import com.company.project.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;


import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 会员等级
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-07-07 15:30:45
 */
@Accessors(chain = true)
@NoArgsConstructor
@Data
@TableName("shop_member_grade")
public class ShopMemberGradeEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId("id")
    private String id;

    /**
     * 等级名称
     */
    @TableField("name")
    private String name;

    /**
     * 图片地址
     */
    @TableField("image")
    private String image;

    /**
     * 所需积分
     */
    @TableField("integration")
    private Integer integration;

    /**
     * 会员帐号有效期(年) -1为无限制
     */
    @TableField("deadline")
    private Integer deadline;

    /**
     * 优惠百分比
     */
    @TableField("preferential")
    private Integer preferential;

    /**
     * 是否是默认
     */
    @TableField("is_default")
    private Integer isDefault;

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

    public ShopMemberGradeEntity(Integer isDefault) {
        this.isDefault = isDefault;
    }
}
