package com.company.project.entity;

import com.company.project.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;


import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 商品SPU操作记录
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-07-01 14:20:15
 */
@Accessors(chain = true)
@NoArgsConstructor
@Data
@TableName("shop_spu_operation_record")
public class ShopSpuOperationRecordEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    public ShopSpuOperationRecordEntity(String spuId, String operation, String oldObject, String newObject, Long timeConsuming) {
        this.spuId = spuId;
        this.operation = operation;
        this.oldObject = oldObject;
        this.newObject = newObject;
        this.timeConsuming = timeConsuming;
    }

    /**
     * 主键
     */
    @TableId("id")
    private String id;

    /**
     * 商品ID
     */
    @TableField("spu_id")
    private String spuId;

    /**
     * 操作
     */
    @TableField("operation")
    private String operation;

    /**
     * 原对象
     */
    @TableField("old_object")
    private String oldObject;

    /**
     * 新对象
     */
    @TableField("new_object")
    private String newObject;

    /**
     * 耗时
     */
    @TableField("time_consuming")
    private Long timeConsuming;

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
     * 是否删除,0:未删除，1：已删除
     */
    @TableField(fill = FieldFill.INSERT)
    @TableLogic // 注释后进行物理删除
    private Integer deleted;


}
