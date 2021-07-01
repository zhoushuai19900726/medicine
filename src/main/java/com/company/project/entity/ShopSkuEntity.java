package com.company.project.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.baomidou.mybatisplus.annotation.*;
import com.company.project.common.utils.CommonUtils;
import com.company.project.entity.BaseEntity;


import java.io.Serializable;
import java.util.Date;
import java.util.LinkedHashMap;

import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang.StringUtils;

/**
 * 商品SKU
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-06-17 10:25:18
 */
@Accessors(chain = true)
@Data
@TableName("shop_sku")
public class ShopSkuEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 商品id
     */
    @TableId("id")
    private String id;

    /**
     * 商品条码
     */
    @TableField("sn")
    private String sn;

    /**
     * 商家
     */
    @TableField("seller_id")
    private String sellerId;

    /**
     * SKU名称
     */
    @TableField("name")
    private String name;

    /**
     * 价格（分）
     */
    @TableField("price")
    private Integer price;

    /**
     * 库存数量
     */
    @TableField("num")
    private Integer num;

    /**
     * 库存预警数量
     */
    @TableField("alert_num")
    private Integer alertNum;

    /**
     * 商品图片
     */
    @TableField("image")
    private String image;

    /**
     * 商品图片列表
     */
    @TableField("images")
    private String images;

    /**
     * 重量（克）
     */
    @TableField("weight")
    private Integer weight;

    /**
     * SPUID
     */
    @TableField("spu_id")
    private String spuId;

    /**
     * 类目ID
     */
    @TableField("category_id")
    private String categoryId;

    /**
     * 类目名称
     */
    @TableField("category_name")
    private String categoryName;

    /**
     * 品牌ID
     */
    @TableField("brand_id")
    private String brandId;

    /**
     * 品牌名称
     */
    @TableField("brand_name")
    private String brandName;

    /**
     * 规格
     */
    @TableField("spec")
    private String spec;
    @TableField(exist = false)
    private LinkedHashMap specMap = new LinkedHashMap<>();

    public void setSpec(String spec) {
        this.spec = spec;
        if (StringUtils.isNotBlank(spec) && CommonUtils.isJson(spec)) {
            this.specMap = JSON.parseObject(spec, LinkedHashMap.class, Feature.OrderedField);
        }
    }

    /**
     * 销量
     */
    @TableField("sale_num")
    private Integer saleNum;

    /**
     * 评论数
     */
    @TableField("comment_num")
    private Integer commentNum;

    /**
     * 商品状态 1-正常，2-下架，3-删除
     */
    @TableField("status")
    private String status;

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
