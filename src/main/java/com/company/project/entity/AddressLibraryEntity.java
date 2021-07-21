package com.company.project.entity;

import com.company.project.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;


import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 地址库
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-07-14 11:56:12
 */
@Accessors(chain = true)
@NoArgsConstructor
@Data
@TableName("address_library")
public class AddressLibraryEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId("id")
    private String id;

    /**
     * 地区名称
     */
    @TableField("name")
    private String name;

    /**
     * 是否显示
     */
    @TableField("is_show")
    private String isShow;

    /**
     * 级别
     */
    @TableField("level")
    private Integer level;

    /**
     * 排序
     */
    @TableField("seq")
    private Integer seq;

    /**
     * 上级ID
     */
    @TableField("parent_id")
    private String parentId;

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
