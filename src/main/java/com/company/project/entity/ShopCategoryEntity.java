package com.company.project.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.company.project.common.utils.DelimiterConstants;
import com.company.project.common.utils.NumberConstants;
import com.company.project.entity.BaseEntity;


import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Data;
import org.apache.commons.lang.StringUtils;

/**
 * 商品分类
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-06-16 10:33:59
 */
@Data
@TableName("shop_category")
public class ShopCategoryEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId("id")
    private String id;

    /**
     * 分类名称
     */
    @TableField("name")
    private String name;

    /**
     * 商品数量
     */
    @TableField("goods_num")
    private Integer goodsNum;

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
     * 是否导航
     */
    @TableField("is_menu")
    private Integer isMenu;
    // 页面参数封装
    @TableField(exist = false)
    private String isMenuStr;

    public void setIsMenuStr(String isMenuStr) {
        this.isMenuStr = isMenuStr;
        if (StringUtils.isNotBlank(isMenuStr)) {
            if(StringUtils.equals(isMenuStr, DelimiterConstants.ON)){
                this.isMenu = 1;
            } else if (StringUtils.equals(isMenuStr, DelimiterConstants.OFF)){
                this.isMenu = 0;
            }
        }
    }


    /**
     * 排序
     */
    @TableField("seq")
    private Integer seq;

    /**
     * 级别
     */
    @TableField("level")
    private Integer level;


    /**
     * 上级ID
     */
    @TableField("parent_id")
    private String parentId;

    /**
     * 模板ID
     */
    @TableField("template_id")
    private String templateId;
    @TableField(exist = false)
    private String templateName;


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
    @TableLogic // 注释后进行物理删除
    private Integer deleted;

    @TableField(exist = false)
    private List<ShopCategoryEntity> children;


}
