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
     * 免运费标准(分)
     */
    @TableField("free_freight_standard")
    private Integer freeFreightStandard;

    /**
     * 奖励评价(成长值)
     */
    @TableField("reward_evaluation")
    private Integer rewardEvaluation;

    /**
     * 满免运费特权
     */
    @TableField("full_freight_free_privilege")
    private Integer fullFreightFreePrivilege;

    /**
     * 签到奖励特权
     */
    @TableField("sign_in_reward_privilege")
    private Integer signInRewardPrivilege;

    /**
     * 评价奖励特权
     */
    @TableField("evaluation_reward_privilege")
    private Integer evaluationRewardPrivilege;

    /**
     * 专享活动特权
     */
    @TableField("exclusive_event_privileges")
    private Integer exclusiveEventPrivileges;

    /**
     * 会员特价特权
     */
    @TableField("special_member_privileges")
    private Integer specialMemberPrivileges;

    /**
     * 生日礼包特权
     */
    @TableField("birthday_package_privileges")
    private Integer birthdayPackagePrivileges;

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
