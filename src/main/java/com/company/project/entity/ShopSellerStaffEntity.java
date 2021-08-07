package com.company.project.entity;

import com.company.project.common.utils.DelimiterConstants;
import com.company.project.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;


import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.apache.commons.lang.StringUtils;

/**
 * 商家员工
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-08-06 10:48:30
 */
@Accessors(chain = true)
@NoArgsConstructor
@Data
@TableName("shop_seller_staff")
public class ShopSellerStaffEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId("id")
    private String id;

    /**
     * 所属商家
     */
    @TableField("seller_id")
    private String sellerId;
    @TableField(exist = false)
    private String sellerName;

    /**
     * 账号
     */
    @TableField("account")
    private String account;

    /**
     * 密码
     */
    @TableField("password")
    private String password;

    @TableField("salt")
    private String salt;

    /**
     * 员工姓名
     */
    @TableField("name")
    private String name;

    /**
     * 联系电话
     */
    @TableField("contact_number")
    private String contactNumber;

    /**
     * 省
     */
    @TableField("province")
    private String province;

    /**
     * 市
     */
    @TableField("city")
    private String city;

    /**
     * 区
     */
    @TableField("county")
    private String county;

    /**
     * 详细地址
     */
    @TableField("address")
    private String address;

    /**
     * 状态  1启用 0禁用
     */
    @TableField("status")
    private Integer status;
    // 页面参数封装
    @TableField(exist = false)
    private String statusStr;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private String createId;
    @TableField(exist = false)
    private String createName;

    /**
     * 创建时间
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
     * 修改人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateId;
    @TableField(exist = false)
    private String updateName;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 删除标识
     */
    @TableField(fill = FieldFill.INSERT)
    @TableLogic // 注释后进行物理删除
    private Integer deleted;

    public void setStatusStr(String statusStr) {
        this.statusStr = statusStr;
        if (StringUtils.isNotBlank(statusStr)) {
            if (StringUtils.equals(statusStr, DelimiterConstants.ON)) {
                this.status = 1;
            } else if (StringUtils.equals(statusStr, DelimiterConstants.OFF)) {
                this.status = 0;
            }
        }
    }

}
