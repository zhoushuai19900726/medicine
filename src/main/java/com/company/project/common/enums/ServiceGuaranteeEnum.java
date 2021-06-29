package com.company.project.common.enums;

import com.company.project.common.utils.DelimiterConstants;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;
import java.util.Objects;

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
public enum ServiceGuaranteeEnum {

    /**
     * 无忧退货
     */
    WORRY_FREE_RETURN(1, "无忧退货"),
    /**
     * 快速退款
     */
    QUICK_REFUND(2, "快速退款"),
    /**
     * 免费包邮
     */
    FREE_PACKAGE(3, "免费包邮");

    private Integer type;
    private String des;

    ServiceGuaranteeEnum(Integer type, String des) {
        this.type = type;
        this.des = des;
    }

    public static String getDesByType(Integer type) {
        if (Objects.isNull(type)) {
            return DelimiterConstants.EMPTY_STR;
        }
        for (ServiceGuaranteeEnum serviceGuaranteeEnum : ServiceGuaranteeEnum.values()) {
            if (serviceGuaranteeEnum.getType().equals(type)) {
                return serviceGuaranteeEnum.getDes();
            }
        }
        return DelimiterConstants.EMPTY_STR;
    }

    public Integer getType() {
        return type;
    }

    public String getDes() {
        return des;
    }

    public static List<Map<String, Object>> toList() {
        List<Map<String, Object>> list = Lists.newArrayList();
        Map<String, Object> map;
        for (ServiceGuaranteeEnum serviceGuaranteeEnum : ServiceGuaranteeEnum.values()) {
            map = Maps.newHashMap();
            map.put("id", serviceGuaranteeEnum.getType());
            map.put("name", serviceGuaranteeEnum.getDes());
            list.add(map);
        }
        return list;
    }

}
