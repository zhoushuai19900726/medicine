package com.company.project.entity;

import com.company.project.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;


import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 相册
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-06-25 14:17:03
 */
@Accessors(chain = true)
@Data
@TableName("shop_album")
public class ShopAlbumEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId("id")
    private String id;

    /**
     * 相册名称
     */
    @TableField("name")
    private String name;

    /**
     * 相册封面图片地址
     */
    @TableField("image")
    private String image;

    /**
     * 图片数量
     */
    @TableField("picture_count")
    private Integer pictureCount;

    /**
     * 排序
     */
    @TableField("seq")
    private Integer seq;

    /**
     * 描述
     */
    @TableField("remark")
    private String remark;

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
