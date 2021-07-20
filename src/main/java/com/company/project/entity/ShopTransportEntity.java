package com.company.project.entity;

import com.company.project.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;


import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 运费模板
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-07-20 15:42:47
 */
@Accessors(chain = true)
@NoArgsConstructor
@Data
@TableName("shop_transport")
public class ShopTransportEntity extends BaseEntity implements Serializable {

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
     * 店铺ID
     */
    @TableField("seller_id")
    private String sellerId;
    @TableField(exist = false)
    private String sellerName;

    /**
     * 是否默认
     */
    @TableField("is_default")
    private Integer isDefault;

    /**
     * 特殊地区不配送, 市级地区ID组成的串，以，隔开
     */
    @TableField("special_areas")
    private String specialAreas;

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
