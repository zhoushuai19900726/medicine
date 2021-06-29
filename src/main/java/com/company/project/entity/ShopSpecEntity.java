package com.company.project.entity;

import com.baomidou.mybatisplus.annotation.*;


import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 商品规格
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-06-11 10:11:20
 */
@Accessors(chain = true)
@Data
@TableName("shop_spec")
public class ShopSpecEntity extends BaseEntity implements Serializable {
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
     * 规格选项
     */
    @TableField("options")
    private String options;

    /**
     * 排序
     */
    @TableField("seq")
    private Integer seq;

    /**
     * 模板ID
     */
    @TableField("template_id")
    private String templateId;

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
