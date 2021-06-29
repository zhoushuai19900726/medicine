package com.company.project.entity;

import com.company.project.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;


import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 相册图片
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-06-25 16:17:09
 */
@Accessors(chain = true)
@Data
@TableName("shop_album_gallery")
public class ShopAlbumGalleryEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId("id")
    private String id;
    @TableField(exist = false)
    private List<String> idList;

    /**
     * 图片名称
     */
    @TableField("name")
    private String name;

    /**
     * 相册ID
     */
    @TableField("album_id")
    private String albumId;

    /**
     * 图片地址
     */
    @TableField("image")
    private String image;

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

    public ShopAlbumGalleryEntity() {
    }

    public ShopAlbumGalleryEntity(String id, String albumId) {
        this.id = id;
        this.albumId = albumId;
    }
}
