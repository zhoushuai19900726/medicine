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

    /**
     * 错误码 - **模块
     */

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
