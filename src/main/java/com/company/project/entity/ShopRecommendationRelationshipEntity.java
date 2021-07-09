package com.company.project.entity;

import com.company.project.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;


import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 会员推荐关系表
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-07-09 15:40:52
 */
@Accessors(chain = true)
@NoArgsConstructor
@Data
@TableName("shop_recommendation_relationship")
public class ShopRecommendationRelationshipEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 会员关系ID
     */
    @TableId("id")
    private String id;

    /**
     * 会员ID
     */
    @TableField("member_id")
    private String memberId;

    /**
     * 推荐人ID
     */
    @TableField("recommend_id")
    private String recommendId;

    /**
     * 推荐级别
     */
    @TableField("recommend_level")
    private Integer recommendLevel;

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
