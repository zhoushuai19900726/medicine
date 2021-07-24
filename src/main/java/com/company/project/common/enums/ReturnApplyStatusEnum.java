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
public enum ReturnApplyStatusEnum {


    REVIEWED(0, "待审核"),
    APPROVED(1, "审核通过"),
    REJECT(2, "审核拒绝"),
   ;

    private Integer type;
    private String des;

    ReturnApplyStatusEnum(Integer type, String des) {
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
