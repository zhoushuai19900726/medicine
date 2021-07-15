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
 * 广告位
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-07-15 09:28:30
 */
@Accessors(chain = true)
@NoArgsConstructor
@Data
@TableName("shop_advertising_space")
public class ShopAdvertisingSpaceEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId("id")
    private String id;

    /**
     * 名称
     */
    @TableField("name")
    private String name;

    /**
     * 简介
     */
    @TableField("introduction")
    private String introduction;

    /**
     * 广告类别：0图片
     */
    @TableField("type")
    private Integer type;
    @TableField(exist = false)
    private String typeName;

    /**
     * 广告展示方式：1多广告展示2单广告展示
     */
    @TableField("display")
    private Integer display;
    @TableField(exist = false)
    private String displayName;


    @TableField("province")
    private String province;
    @TableField(exist = false)
    private String provinceName;
    @TableField("city")
    private String city;
    @TableField(exist = false)
    private String cityName;
    @TableField("county")
    private String county;
    @TableField(exist = false)
    private String countyName;


    /**
     * 是否显示
     */
    @TableField("is_show")
    private Integer isShow;
    // 页面参数封装
    @TableField(exist = false)
    private String isShowStr;

    public void setIsShowStr(String isShowStr) {
        this.isShowStr = isShowStr;
        if (StringUtils.isNotBlank(isShowStr)) {
            if(StringUtils.equals(isShowStr, DelimiterConstants.ON)){
                this.isShow = 1;
            } else if (StringUtils.equals(isShowStr, DelimiterConstants.OFF)){
                this.isShow = 0;
            }
        }
    }

    /**
     * 排序
     */
    @TableField("seq")
    private Integer seq;

    /**
     * 获取标记
     */
    @TableField("get_tag")
    private String getTag;

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
