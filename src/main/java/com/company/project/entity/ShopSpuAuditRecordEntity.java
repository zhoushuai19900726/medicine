package com.company.project.entity;

import com.company.project.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;


import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 商品SPU审核记录
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-07-06 16:59:44
 */
@Accessors(chain = true)
@NoArgsConstructor
@Data
@TableName("shop_spu_audit_record")
public class ShopSpuAuditRecordEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

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
     * 备注
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
     * 是否删除,0:未删除，1：已删除
     */
    @TableField(fill = FieldFill.INSERT)
    @TableLogic // 注释后进行物理删除
    private Integer deleted;

    public ShopSpuAuditRecordEntity(String spuId, String operation) {
        this.spuId = spuId;
        this.operation = operation;
    }

    public ShopSpuAuditRecordEntity(String spuId, String operation, String remark) {
        this.spuId = spuId;
        this.operation = operation;
        this.remark = remark;
    }
}
