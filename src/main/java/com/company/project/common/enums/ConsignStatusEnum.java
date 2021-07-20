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
public enum ConsignStatusEnum {


    UNDELIVERED(0, "未发货"),
    DELIVERED(1, "已发货"),
    RECEIVED(2, "已收货"),
   ;

    private Integer type;
    private String des;

    ConsignStatusEnum(Integer type, String des) {
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
