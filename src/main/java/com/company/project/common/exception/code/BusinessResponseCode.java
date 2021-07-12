package com.company.project.common.exception.code;


/**
 * 错误码
 *
 * @author wenbin
 * @version V1.0
 * @date 2020年3月18日
 */
public enum BusinessResponseCode implements ResponseCodeInterface {
    /**
     * 错误码 - 商品模块
     */
    SPU_SN_REPEATED_EXISTENCE(900001, "SPU商品货号已存在"),
    SKU_SN_REPEATED_EXISTENCE(900002, "SKU商品货号已存在"),
    SKU_NULL(900003, "请至少维护一个SKU"),
    SKU_REPEAT(900004, "SKU商品货号出现重复"),

    /**
     * 错误码 - 规格参数模块
     */
    SPEC_REPEAT(910001, "商品规格名称重复"),
    PARA_REPEAT(910002, "商品参数名称重复"),

    /**
     * 错误码 - 会员模块
     */
    ACCOUNT_REPEAT(920001, "账号已存在"),
    INVITATION_CODE_REPEAT(920002, "邀请码已存在"),
    INVALID_RECOMMENDATION_CODE(920003, "无效的推荐码"),
    INVALID_ACCOUNT(920004, "无效的账号"),

    /**
     * 错误码 - 文件模块
     */
    FILE_EMPTY(930001, "文件为空"),
    FILE_FORMAT_ERROR(930002, "文件格式不正确！请使用.xls或.xlsx后缀文档"),
    FILE_TITLE_ERROR(930003, "文件模板错误,文件列数与系统不一致"),
    FILE_TITLE_CONTENT_ERROR_FRONT(930004, "表头名字和系统不对应,第一行第"),
    FILE_TITLE_CONTENT_ERROR_AFTER(930005, "个单元格应为:"),
    FILE_DATA_EMPTY(930006, "文件数据为空"),
    ;

    /**
     * 错误码
     */
    private final int code;
    /**
     * 错误消息
     */
    private final String msg;

    BusinessResponseCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }
}
