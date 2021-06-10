package com.company.project.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 商家管理
 *
 * @author zhoushuai
 * @version V1.0
 * @date 2020年3月18日
 */
@Data
@TableName("shop_seller")
public class ShopSellerEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private String id;

    @TableField("seller_name")
    private String sellerName;

    @TableField("seller_address")
    private String sellerAddress;

    @TableField("province")
    private String province;

    @TableField("city")
    private String city;

    @TableField("county")
    private String county;

    @TableField("address")
    private String address;

    @TableField("contact_number")
    private String contactNumber;

    @TableField("status")
    private Integer status = 0;
    // 页面参数封装
    @TableField(exist = false)
    private String statusStr;
    // 查询条件
    @TableField(exist = false)
    private List<String> statusList;

    @TableField(fill = FieldFill.INSERT)
    private String createId;
    @TableField(exist = false)
    private String createName;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    // 查询条件
    @TableField(exist = false)
    private String createStartTime;
    // 查询条件
    @TableField(exist = false)
    private String createEndTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateId;
    @TableField(exist = false)
    private String updateName;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @TableField(fill = FieldFill.INSERT)
    // @TableLogic // 打开注释后进行物理删除
    private Integer deleted;

    public void setStatusStr(String statusStr) {
        this.statusStr = statusStr;
        if (StringUtils.isNotBlank(statusStr) && StringUtils.equals(statusStr, "on")) {
            this.status = 1;
        }
    }

    public void setSellerAddress(String sellerAddress) {
        this.sellerAddress = StringUtils.isNotBlank(sellerAddress) ? sellerAddress :
                ((StringUtils.isNotBlank(this.province) ? this.province : StringUtils.EMPTY) +
                        (StringUtils.isNotBlank(this.city) ? this.city : StringUtils.EMPTY) +
                        (StringUtils.isNotBlank(this.county) ? this.county : StringUtils.EMPTY) +
                        (StringUtils.isNotBlank(this.address) ? this.address : StringUtils.EMPTY));
    }
}
