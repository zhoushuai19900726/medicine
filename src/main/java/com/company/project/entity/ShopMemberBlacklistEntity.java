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
 * 会员表
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-07-12 13:58:08
 */
@Accessors(chain = true)
@NoArgsConstructor
@Data
@TableName("shop_member_blacklist")
public class ShopMemberBlacklistEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 会员id
     */
    @TableId("member_id")
    private String memberId;

    /**
     * 会员账号
     */
    @TableField("member_name")
    private String memberName;

    /**
     * 会员昵称
     */
    @TableField("member_nick")
    private String memberNick;

    /**
     * 会员密码
     */
    @TableField("member_passwd")
    private String memberPasswd;

    /**
     * 支付密码
     */
    @TableField("payment_passwd")
    private String paymentPasswd;

    /**
     * 真实姓名
     */
    @TableField("member_true_name")
    private String memberTrueName;

    /**
     * 手机号
     */
    @TableField("member_mobile")
    private String memberMobile;

    /**
     * 会员头像
     */
    @TableField("member_avatar")
    private String memberAvatar;

    /**
     * 会员性别
     */
    @TableField("member_sex")
    private Integer memberSex;

    /**
     * 生日
     */
    @TableField("member_birthday")
    private String memberBirthday;

    /**
     * 身份证号
     */
    @TableField("member_identity")
    private String memberIdentity;

    /**
     * 会员邮箱
     */
    @TableField("member_email")
    private String memberEmail;

    /**
     * qq
     */
    @TableField("member_qq")
    private String memberQq;

    /**
     * 阿里旺旺
     */
    @TableField("member_ww")
    private String memberWw;

    /**
     * 支付宝姓名
     */
    @TableField("alipay_name")
    private String alipayName;

    /**
     * 支付宝账号
     */
    @TableField("alipay_account_number")
    private String alipayAccountNumber;

    /**
     * 微信名
     */
    @TableField("wechat_name")
    private String wechatName;

    /**
     * 微信账号
     */
    @TableField("wechat_account_number")
    private String wechatAccountNumber;

    /**
     * 登录次数
     */
    @TableField("member_login_num")
    private Integer memberLoginNum;

    /**
     * 当前登录时间
     */
    @TableField("member_login_time")
    private Date memberLoginTime;

    /**
     * 上次登录时间
     */
    @TableField("member_old_login_time")
    private Date memberOldLoginTime;

    /**
     * 当前登录ip
     */
    @TableField("member_login_ip")
    private String memberLoginIp;

    /**
     * 上次登录ip
     */
    @TableField("member_old_login_ip")
    private String memberOldLoginIp;

    /**
     * 地区
     */
    @TableField("member_area")
    private String memberArea;

    /**
     * 城市
     */
    @TableField("member_city")
    private String memberCity;

    /**
     * 省份
     */
    @TableField("member_province")
    private String memberProvince;

    /**
     * 地区内容
     */
    @TableField("member_area_info")
    private String memberAreaInfo;

    /**
     * 会员等级id
     */
    @TableField("member_grade_id")
    private String memberGradeId;

    /**
     * 会员等级
     */
    @TableField("member_grade_name")
    private String memberGradeName;

    /**
     * 会员升级日期
     */
    @TableField("grade_time")
    private Date gradeTime;

    /**
     * 来自什么平台注册默认 0 pc, 1 app, 2 其他
     */
    @TableField("member_identification")
    private Integer memberIdentification;

    /**
     * 第三方默认0 本站，1微信，2QQ，3新浪
     */
    @TableField("member_third_party")
    private Integer memberThirdParty;

    /**
     * 微信OPEN_ID
     */
    @TableField("member_openid")
    private String memberOpenid;

    /**
     * 会员邀请码
     */
    @TableField("member_invitation_code")
    private String memberInvitationCode;

    /**
     * 邀请人
     */
    @TableField("member_from")
    private String memberFrom;

    /**
     * 邀请人的邀请码
     */
    @TableField("member_invitation_code_from")
    private String memberInvitationCodeFrom;

    /**
     * 会员的开启状态 1为开启 0为关闭
     */
    @TableField("member_state")
    private Integer memberState;

    /**
     * 版本号
     */
    @TableField("member_version")
    private Long memberVersion;

    // 钱包余额
    @TableField(exist = false)
    private BigDecimal walletBalance;

    // 成长值
    @TableField(exist = false)
    private BigDecimal growthValue;

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
//    @TableLogic // 注释后进行物理删除
    private Integer deleted;


}
