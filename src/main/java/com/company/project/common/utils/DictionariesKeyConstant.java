package com.company.project.common.utils;

/**
 * 字典相关Constant
 *
 * @author zhoushuai
 * @version V1.0
 * @date 2020年3月18日
 */
public class DictionariesKeyConstant {

    /**
     * 字典相关
     */
    public static final String DICT_KEY_PREFIX = "dict-key-";
    /**
     * 字典KEY
     */
    // 性别
    public static final String SEX = DICT_KEY_PREFIX.concat("sex");
    // 商品服务保证
    public static final String SERVICE_GUARANTEE = DICT_KEY_PREFIX.concat("service_guarantee");
    // 商品上下架状态
    public static final String GOODS_STATUS = DICT_KEY_PREFIX.concat("goods_status");
    // 商品审核状态
    public static final String GOODS_EXAMINE_STATUS = DICT_KEY_PREFIX.concat("goods_examine_status");

}
