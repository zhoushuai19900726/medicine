package com.company.project.common.enums;

import com.company.project.common.utils.DelimiterConstants;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;

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
 * @desc 审核状态，0：待审核，1：审核通过，2：审核未通过
 */
public enum GoodsExamineStatusEnum {

    /**
     * 待审核
     */
    TO_BE_REVIEWED("0", "待审核"),
    /**
     * 审核通过
     */
    APPROVED("1", "审核通过"),
    /**
     * 审核未通过
     */
    AUDIT_FAILED("2", "审核未通过");

    private String type;
    private String des;

    GoodsExamineStatusEnum(String type, String des) {
        this.type = type;
        this.des = des;
    }

    public static String getDesByType(String type) {
        if (StringUtils.isBlank(type)) {
            return DelimiterConstants.EMPTY_STR;
        }
        for (GoodsExamineStatusEnum goodsExamineStatusEnum : GoodsExamineStatusEnum.values()) {
            if (goodsExamineStatusEnum.getType().equals(type)) {
                return goodsExamineStatusEnum.getDes();
            }
        }
        return DelimiterConstants.EMPTY_STR;
    }

    public String getType() {
        return type;
    }

    public String getDes() {
        return des;
    }

    public static List<Map<String, String>> toList() {
        List<Map<String, String>> list = Lists.newArrayList();
        Map<String, String> map;
        for (GoodsExamineStatusEnum goodsExamineStatusEnum : GoodsExamineStatusEnum.values()) {
            map = Maps.newHashMap();
            map.put("id", goodsExamineStatusEnum.getType());
            map.put("name", goodsExamineStatusEnum.getDes());
            list.add(map);
        }
        return list;
    }

}
