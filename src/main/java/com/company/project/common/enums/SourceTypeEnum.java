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
public enum SourceTypeEnum {


    WEB(1, "WEB商城"),
    APP(2, "APP商城"),
    FREE_BILL(3, "管理员赠单"),
   ;

    private Integer type;
    private String des;

    SourceTypeEnum(Integer type, String des) {
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
