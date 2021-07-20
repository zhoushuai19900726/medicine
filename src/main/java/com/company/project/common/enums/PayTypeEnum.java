package com.company.project.common.enums;

/***
 *       ___      _        _
 *      / __\__ _| |_ __ _| |_ __   __ _
 *     / /  / _` | __/ _` | | '_ \ / _` |
 *    / /__| (_| | || (_| | | |_) | (_| |
 *    \____/\__,_|\__\__,_|_| .__/ \__,_|
 *                          |_|
 * @author
 * @date
 * @desc
 */
public enum PayTypeEnum {


    CASH_ON_DELIVERY("0", "货到付款"),
    WECHAT_PAYMENT("1", "微信支付"),
    ALIPAY_PAYMENT("2", "支付宝支付"),
    WALLET_PAYMENT("3", "钱包支付"),
    SYSTEM_GIFT("4", "系统赠送"),
   ;

    private String type;
    private String des;

    PayTypeEnum(String type, String des) {
        this.type = type;
        this.des = des;
    }


    public String getType() {
        return type;
    }

    public String getDes() {
        return des;
    }

}
