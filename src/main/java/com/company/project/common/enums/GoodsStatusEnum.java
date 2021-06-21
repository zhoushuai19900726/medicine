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
public enum GoodsStatusEnum {

    /**
     * 上架
     */
    PUT_ON_THE_SHELVES(1, "上架"),
    /**
     * 下架
     */
    OFF_THE_SHELF(0, "下架"),
    /**
     * 停售
     */
    HALT_THE_SALES(2, "停售");

    private Integer type;
    private String des;

    GoodsStatusEnum(Integer type, String des) {
        this.type = type;
        this.des = des;
    }

    public static String getDesByType(Integer type) {
        if (Objects.isNull(type)) {
            return DelimiterConstants.EMPTY_STR;
        }
        for (GoodsStatusEnum goodsStatusEnum : GoodsStatusEnum.values()) {
            if (goodsStatusEnum.getType().equals(type)) {
                return goodsStatusEnum.getDes();
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
        for (GoodsStatusEnum goodsStatusEnum : GoodsStatusEnum.values()) {
            map = Maps.newHashMap();
            map.put("id", goodsStatusEnum.getType());
            map.put("name", goodsStatusEnum.getDes());
            list.add(map);
        }
        return list;
    }

}
