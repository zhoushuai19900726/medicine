package com.company.project.entity;

import com.company.project.common.utils.DelimiterConstants;
import com.company.project.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;


import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.apache.commons.lang.StringUtils;

/**
 * 文章
 *
 * @author zhoushuai
 * @email zhoushuai_0726@163.com
 * @date 2021-07-27 13:56:46
 */
@Accessors(chain = true)
@NoArgsConstructor
@Data
@TableName("shop_article")
public class ShopArticleEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId("id")
    private String id;

    /**
     * 栏目ID
     */
    @TableField("column_id")
    private String columnId;
    @TableField(exist = false)
    private String columnName;

    /**
     * 标题
     */
    @TableField("title")
    private String title;
    @TableField(exist = false)
    private String name;

    public void setTitle(String title) {
        this.title = title;
        this.name = title;
    }

    /**
     * 副标题
     */
    @TableField("subtitle")
    private String subtitle;

    /**
     * 封面图
     */
    @TableField("cover")
    private String cover;

    /**
     * 作者
     */
    @TableField("author")
    private String author;

    /**
     * 来源
     */
    @TableField("source")
    private String source;

    /**
     * 摘要
     */
    @TableField("digest")
    private String digest;

    /**
     * 内容
     */
    @TableField("content")
    private String content;

    /**
     * 相关推荐文章
     */
    @TableField("correlation_Id")
    private String correlationId;

    /**
     * 绑定商品
     */
    @TableField("goods")
    private String goods;

    /**
     * 是否头条
     */
    @TableField("is_headline")
    private Integer isHeadline;
    // 页面参数封装
    @TableField(exist = false)
    private String isHeadlineStr;

    public void setIsHeadlineStr(String isHeadlineStr) {
        this.isHeadlineStr = isHeadlineStr;
        if (StringUtils.isNotBlank(isHeadlineStr)) {
            if(StringUtils.equals(isHeadlineStr, DelimiterConstants.ON)){
                this.isHeadline = 1;
            } else if (StringUtils.equals(isHeadlineStr, DelimiterConstants.OFF)){
                this.isHeadline = 0;
            }
        }
    }

    /**
     * 是否置顶
     */
    @TableField("is_top")
    private Integer isTop;
    // 页面参数封装
    @TableField(exist = false)
    private String isTopStr;

    public void setIsTopStr(String isTopStr) {
        this.isTopStr = isTopStr;
        if (StringUtils.isNotBlank(isTopStr)) {
            if(StringUtils.equals(isTopStr, DelimiterConstants.ON)){
                this.isTop = 1;
            } else if (StringUtils.equals(isTopStr, DelimiterConstants.OFF)){
                this.isTop = 0;
            }
        }
    }

    /**
     * 是否显示
     */
    @TableField("is_show")
    private Integer isShow;
    // 页面参数封装
    @TableField(exist = false)
    private String isShowStr;

    public void setIsShowStr(String isShowStr) {
        this.isShowStr = isShowStr;
        if (StringUtils.isNotBlank(isShowStr)) {
            if(StringUtils.equals(isShowStr, DelimiterConstants.ON)){
                this.isShow = 1;
            } else if (StringUtils.equals(isShowStr, DelimiterConstants.OFF)){
                this.isShow = 0;
            }
        }
    }

    /**
     * 是否允许评论
     */
    @TableField("allow_comment")
    private Integer allowComment;
    // 页面参数封装
    @TableField(exist = false)
    private String allowCommentStr;

    public void setAllowCommentStr(String allowCommentStr) {
        this.allowCommentStr = allowCommentStr;
        if (StringUtils.isNotBlank(allowCommentStr)) {
            if(StringUtils.equals(allowCommentStr, DelimiterConstants.ON)){
                this.allowComment = 1;
            } else if (StringUtils.equals(allowCommentStr, DelimiterConstants.OFF)){
                this.allowComment = 0;
            }
        }
    }

    /**
     * 收藏数
     */
    @TableField("collect_num")
    private Integer collectNum;

    /**
     * 浏览数
     */
    @TableField("pv")
    private Integer pv;

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
