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
public enum GrowthValueDetailsTypeEnum {


    CONSUMPTION(1, "消费"),
    SIGN_IN(2, "签到"),
    TASK(3, "任务"),
    ADMIN_UPDATE(999, "管理员修改"),
   ;

    private Integer type;
    private String des;

    GrowthValueDetailsTypeEnum(Integer type, String des) {
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
