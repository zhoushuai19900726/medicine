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
 * 轮播图
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-07-16 14:53:02
 */
@Accessors(chain = true)
@NoArgsConstructor
@Data
@TableName("shop_carousel_map")
public class ShopCarouselMapEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId("id")
    private String id;

    /**
     * 位置类型
     */
    @TableField("space_type")
    private String spaceType;

    /**
     * 资源图片
     */
    @TableField("res_url")
    private String resUrl;

    /**
     * 背景
     */
    @TableField("background")
    private String background;

    /**
     * 跳转路径
     */
    @TableField("adv_url")
    private String advUrl;

    /**
     * 开始时间
     */
    @TableField("start_date")
    private Date startDate;

    /**
     * 结束时间
     */
    @TableField("end_date")
    private Date endDate;

    /**
     * 点击率
     */
    @TableField("click_num")
    private Integer clickNum;

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
