package com.company.project.entity;

import com.company.project.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;


import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 任务奖励设置
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-07-13 17:59:56
 */
@Accessors(chain = true)
@NoArgsConstructor
@Data
@TableName("task_reward_settings")
public class TaskRewardSettingsEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId("id")
    private String id;

    /**
     * 新手欢迎奖励
     */
    @TableField("novice_welcome_award")
    private String noviceWelcomeAward;

    /**
     * 设置头像
     */
    @TableField("set_up_avatar")
    private String setUpAvatar;

    /**
     * 设置头像
     */
    @TableField("set_up_nickname")
    private String setUpNickname;

    /**
     * 完善个人信息
     */
    @TableField("improve_personal_information")
    private String improvePersonalInformation;

    /**
     * 成功关注一位达人
     */
    @TableField("focus_on_talent")
    private String focusOnTalent;

    /**
     * 首次分享商品或专题
     */
    @TableField("first_share_products")
    private String firstShareProducts;

    /**
     * 首次收藏商品或专题
     */
    @TableField("first_collection_products")
    private String firstCollectionProducts;

    /**
     * 首次购物成功
     */
    @TableField("first_shopping")
    private String firstShopping;

    /**
     * 首次完成评价
     */
    @TableField("first_evaluation")
    private String firstEvaluation;

    /**
     * 每日登录
     */
    @TableField("daily_login")
    private String dailyLogin;

    /**
     * 每日签到
     */
    @TableField("daily_sign")
    private String dailySign;

    /**
     * 分享商品或专题
     */
    @TableField("share_products")
    private String shareProducts;

    /**
     * 邀请好友
     */
    @TableField("invite_friends")
    private String inviteFriends;

    /**
     * 好友首次下单成功
     */
    @TableField("friends_first_shopping")
    private String friendsFirstShopping;

    /**
     * 购买达标天数
     */
    @TableField("standard_days")
    private String standardDays;

    /**
     * 购买天数达标奖励
     */
    @TableField("standard_reward")
    private String standardReward;

    /**
     * 每连续签到天数
     */
    @TableField("continuity_sign_days")
    private String continuitySignDays;

    /**
     * 每连续签到达标奖励
     */
    @TableField("continuity_sign_reward")
    private String continuitySignReward;

    /**
     * 购物每消费该金额时奖励1分成长值（不含运费）
     */
    @TableField("shopping_consumption")
    private String shoppingConsumption;

    /**
     * 商品单价低于该金额时评价不送成长值
     */
    @TableField("item_pricing")
    private String itemPricing;

    /**
     * 订单实际支付金额低于该金额时不送成长值
     */
    @TableField("actual_payment")
    private String actualPayment;

    /**
     * 单件商品最高可获得成长值
     */
    @TableField("highest_commodity")
    private String highestCommodity;

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
