package com.company.project.entity;

import com.company.project.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;


import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 广告
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-07-15 09:28:29
 */
@Accessors(chain = true)
@NoArgsConstructor
@Data
@TableName("shop_advertisement")
public class ShopAdvertisementEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId("id")
    private String id;

    /**
     * 广告位ID
     */
    @TableField("space_id")
    private String spaceId;
    @TableField(exist = false)
    private String spaceName;

    /**
     * 标题
     */
    @TableField("title")
    private String title;

    /**
     * 资源图片路径
     */
    @TableField("res_url")
    private String resUrl;

    /**
     * 广告内容(跳转路径)
     */
    @TableField("adv_url")
    private String advUrl;

    /**
     * 广告开始时间
     */
    @TableField("start_date")
    private Date startDate;

    /**
     * 广告结束时间
     */
    @TableField("end_date")
    private Date endDate;

    /**
     * 广告点击率
     */
    @TableField("click_num")
    private Integer clickNum;

    /**
     * 是否显示
     */
    @TableField("is_show")
    private String isShow;

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


}
