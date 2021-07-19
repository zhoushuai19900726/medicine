package com.company.project.common.enums;

import com.company.project.common.utils.DelimiterConstants;
import org.apache.commons.lang.StringUtils;

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
public enum OrderStatusEnum {


    CANCELLED(-1, "已取消"),
    NOT_FINISHED(0, "未完成"),
    FINISHED(1, "已完成"),
    REFUND(2, "退款"),
    REFUND_AND_RETURN(3, "退款退货"),
    CLOSED(4, "已关闭"),
   ;

    private Integer type;
    private String des;

    OrderStatusEnum(Integer type, String des) {
        this.type = type;
        this.des = des;
    }


    public Integer getType() {
        return type;
    }

    public String getDes() {
        return des;
    }

}
