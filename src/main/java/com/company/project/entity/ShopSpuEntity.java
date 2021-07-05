package com.company.project.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.baomidou.mybatisplus.annotation.*;
import com.company.project.common.utils.CommonUtils;
import com.company.project.common.utils.DelimiterConstants;
import com.company.project.entity.BaseEntity;


import java.io.Serializable;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.apache.commons.lang.StringUtils;

/**
 * 商品SPU
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-06-17 10:25:25
 */
@Accessors(chain = true)
@NoArgsConstructor
@Data
@TableName("shop_spu")
public class ShopSpuEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId("id")
    private String id;

    /**
     * 货号
     */
    @TableField("sn")
    private String sn;

    /**
     * 商家
     */
    @TableField("seller_id")
    private String sellerId;
    @TableField(exist = false)
    private String sellerIdListStr = DelimiterConstants.EMPTY_STR;
    @TableField(exist = false)
    private String sellerName;

    /**
     * SPU名
     */
    @TableField("name")
    private String name;

    /**
     * 副标题
     */
    @TableField("caption")
    private String caption;

    /**
     * 品牌ID
     */
    @TableField("brand_id")
    private String brandId;
    @TableField(exist = false)
    private String brandIdListStr = DelimiterConstants.EMPTY_STR;
    @TableField(exist = false)
    private String brandName;

    /**
     * 一级分类
     */
    @TableField("category1_id")
    private String category1Id;
    @TableField(exist = false)
    private String category1Name;

    /**
     * 二级分类
     */
    @TableField("category2_id")
    private String category2Id;
    @TableField(exist = false)
    private String category2Name;

    /**
     * 三级分类
     */
    @TableField("category3_id")
    private String category3Id;
    @TableField(exist = false)
    private String category3IdListStr = DelimiterConstants.EMPTY_STR;
    @TableField(exist = false)
    private String category3Name;

    /**
     * 模板ID
     */
    @TableField("template_id")
    private String templateId;
    @TableField(exist = false)
    private String templateName;

    /**
     * 运费模板id
     */
    @TableField("freight_id")
    private String freightId;

    /**
     * 图片
     */
    @TableField("image")
    private String image;

    /**
     * 图片列表
     */
    @TableField("images")
    private String images;

    /**
     * 售后服务
     */
    @TableField("sale_service")
    private String saleService;

    /**
     * 介绍
     */
    @TableField("introduction")
    private String introduction;

    /**
     * 规格列表
     */
    @TableField("spec_items")
    private String specItems;
    @TableField(exist = false)
    private LinkedHashMap specItemsMap = new LinkedHashMap<>();

    public void setSpecItems(String specItems) {
        this.specItems = specItems;
        if (StringUtils.isNotBlank(specItems) && CommonUtils.isJson(specItems)) {
            this.specItemsMap = JSON.parseObject(specItems, LinkedHashMap.class, Feature.OrderedField);
        }
    }

    /**
     * 参数列表
     */
    @TableField("para_items")
    private String paraItems;

    @TableField(exist = false)
    private LinkedHashMap paraItemsMap = new LinkedHashMap<>();

    public void setParaItems(String paraItems) {
        this.paraItems = paraItems;
        if (StringUtils.isNotBlank(paraItems) && CommonUtils.isJson(paraItems)) {
            this.paraItemsMap = JSON.parseObject(paraItems, LinkedHashMap.class, Feature.OrderedField);
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
     * 是否上架,0已下架，1已上架 2停售
     */
    @TableField("is_marketable")
    private String isMarketable;
    @TableField(exist = false)
    private String isMarketableListStr = DelimiterConstants.EMPTY_STR;

    /**
     * 是否启用规格
     */
    @TableField("is_enable_spec")
    private String isEnableSpec;

    /**
     * 是否推荐
     */
    @TableField("is_recommend")
    private String isRecommend;

    /**
     * 审核状态，0：未审核，1：已审核，2：审核不通过
     */
    @TableField("status")
    private String status;
    @TableField(exist = false)
    private String statusListStr = DelimiterConstants.EMPTY_STR;


    /**
     * 停售原因
     */
    @TableField("halt_sales_reason")
    private String haltSalesReason;

    /**
     * 审核拒绝原因
     */
    @TableField("audit_rejection_reason")
    private String auditRejectionReason;

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

    /**
     * sku集合
     */
    @TableField(exist = false)
    private List<ShopSkuEntity> shopSkuEntityList;

    /**
     * 库存
     */
    @TableField(exist = false)
    private Integer stock;

    /**
     * COPY至目标三级分类ID集合
     */
    @TableField(exist = false)
    private List<String> copyToCategory3IdList;

    /**
     * 转移至目标三级分类ID
     */
    @TableField(exist = false)
    private String transferToCategory3Id;

}
