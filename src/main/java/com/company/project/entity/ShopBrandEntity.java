package com.company.project.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.company.project.entity.BaseEntity;


import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 商品品牌
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-06-15 14:15:14
 */
@Accessors(chain = true)
@Data
@TableName("shop_brand")
public class ShopBrandEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId("id")
    private String id;

    /**
     * 品牌名称
     */
    @TableField("name")
    private String name;

    /**
     * 品牌图片地址
     */
    @TableField("image")
    private String image;

    /**
     * 品牌的首字母
     */
    @TableField("letter")
    private String letter;

    /**
     * 排序
     */
    @TableField("seq")
    private Integer seq;

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


}
