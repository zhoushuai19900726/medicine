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
     * 地址库相关
     */
    // key: parentId value: List<地址库子集>
    public static final String ADDRESS_LIBRARY_KEY_PREFIX = "address-library-key-";
    // key: id value: 当前地址库对象
    public static final String ADDRESS_LIBRARY_KEY2_PREFIX = "address-library-key2-";
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
    // 钱包明细类型
    public static final String WALLET_DETAILS_TYPE = DICT_KEY_PREFIX.concat("wallet_details_type");
    // 成长值明细类型
    public static final String GROWTH_VALUE_DETAILS_TYPE = DICT_KEY_PREFIX.concat("growth_value_details_type");
    // 广告类别
    public static final String ADVERTISING_CATEGORY = DICT_KEY_PREFIX.concat("advertising_category");
    // 广告展示方式
    public static final String ADVERTISING_DISPLAY = DICT_KEY_PREFIX.concat("advertising_display");
    // 跳转类型
    public static final String JUMP_TYPE = DICT_KEY_PREFIX.concat("jump_type");

}
