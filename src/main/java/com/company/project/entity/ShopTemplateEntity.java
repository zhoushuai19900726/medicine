package com.company.project.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.company.project.entity.BaseEntity;


import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 商品模板
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-06-10 11:02:07
 */
@Data
@TableName("shop_template")
public class ShopTemplateEntity extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@TableId("id")
	private String id;

	/**
	 * 模板名称
	 */
	@TableField("name")
	private String name;

	/**
	 * 规格数量
	 */
	@TableField("spec_num")
	private Integer specNum;

	/**
	 * 参数数量
	 */
	@TableField("para_num")
	private Integer paraNum;

	@TableField(fill = FieldFill.INSERT)
	private String createId;
	@TableField(exist = false)
	private String createName;

	@TableField(fill = FieldFill.INSERT)
	private Date createTime;
	// 查询条件
	@TableField(exist = false)
	private String createStartTime;
	// 查询条件
	@TableField(exist = false)
	private String createEndTime;

	@TableField(fill = FieldFill.INSERT_UPDATE)
	private String updateId;
	@TableField(exist = false)
	private String updateName;

	@TableField(fill = FieldFill.INSERT_UPDATE)
	private Date updateTime;

	@TableField(fill = FieldFill.INSERT)
	@TableLogic // 注释后进行物理删除
	private Integer deleted;


}
