package com.company.project.entity;

import com.alibaba.fastjson.JSON;
import com.company.project.common.utils.CommonUtils;
import com.company.project.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;


import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.apache.commons.lang.StringUtils;

/**
 * 运费模板扩展
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-07-22 16:41:53
 */
@Accessors(chain = true)
@NoArgsConstructor
@Data
@TableName("shop_transport_extend")
public class ShopTransportExtendEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId("id")
    private String id;

    /**
     * 运费模板ID
     */
    @TableField("transport_id")
    private String transportId;

    /**
     * 配送方式
     */
    @TableField("shipping_method")
    private String shippingMethod;

    /**
     * 市级地区信息
     */
    @TableField("area_info")
    private String areaInfo;

    @TableField(exist = false)
    private Map<String, Map<String, Object>> areaInfoMap;

    public void setAreaInfo(String areaInfo) {
        this.areaInfo = areaInfo;
        if(StringUtils.isNotBlank(areaInfo) && CommonUtils.isJson(areaInfo)){
            this.areaInfoMap = (Map<String, Map<String, Object>>) JSON.parse(areaInfo);
        }
    }

    /**
     * 首件/kg/m³数量
     */
    @TableField("snum")
    private BigDecimal snum;

    /**
     * 首件/kg/m³运费
     */
    @TableField("sprice")
    private BigDecimal sprice;

    /**
     * 续件/kg/m³数量
     */
    @TableField("xnum")
    private BigDecimal xnum;

    /**
     * 续件/kg/m³运费
     */
    @TableField("xprice")
    private BigDecimal xprice;

    /**
     * 是否默认
     */
    @TableField("is_default")
    private Integer isDefault;

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
