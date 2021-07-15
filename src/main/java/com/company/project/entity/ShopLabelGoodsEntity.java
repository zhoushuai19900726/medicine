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
 * 标签商品
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-07-15 17:15:30
 */
@Accessors(chain = true)
@NoArgsConstructor
@Data
@TableName("shop_label_goods")
public class ShopLabelGoodsEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId("id")
    private String id;

    /**
     * 标签ID
     */
    @TableField("label_id")
    private String labelId;

    /**
     * 商品ID
     */
    @TableField("spu_id")
    private String spuId;
    // 取消关联条件
    @TableField(exist = false)
    private List<String> spuIdList;
    // 查询条件
    @TableField(exist = false)
    private String sn;

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

    public ShopLabelGoodsEntity(String labelId, String spuId) {
        this.labelId = labelId;
        this.spuId = spuId;
    }
}
