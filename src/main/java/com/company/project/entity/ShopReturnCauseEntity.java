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
 * 退货原因
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-07-20 14:14:36
 */
@Accessors(chain = true)
@NoArgsConstructor
@Data
@TableName("shop_return_cause")
public class ShopReturnCauseEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId("id")
    private String id;

    /**
     * 原因
     */
    @TableField("cause")
    private String cause;

    /**
     * 排序
     */
    @TableField("seq")
    private Integer seq;

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
     *
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
     *
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
     *
     */
    @TableField(fill = FieldFill.INSERT)
    @TableLogic // 注释后进行物理删除
    private Integer deleted;


}
