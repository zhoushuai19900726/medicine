package com.company.project.common.enums;


import com.google.common.collect.Lists;

import java.util.List;

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
public enum TitleAndCodeEnum {

    /**
     * 基础人员

    GOVERN_REAL_POPULATION(
            Lists.newArrayList( "所属组织", "公民身份证号码", "姓名", "曾用名",
                    "性别", "出生日期", "民族", "籍贯(省)", "籍贯(市)",
                    "籍贯(区)", "籍贯", "婚姻状况",
                    "政治面貌","学历", "宗教信仰", "职业类别", "职业",
                    "服务处所", "联系类型", "联系方式","户籍地(省)", "户籍地(市)", "户籍地(区)", "户籍地", "户籍地详址",
                    "现住地(省)",
                    "现住地(市)", "现住地(区)", "现住地(街道)", "现住地(社区)","现住地", "现住地详址","人户一致标识", "户号", "户主公民身份证号码",
                    "户主姓名", "户主性别", "与户主关系", "户主联系类型", "户主联系方式"
            ),
            Lists.newArrayList( "orgId", "idCard", "fullName", "nameUsedBefore",
                    "genderStr", "birthday", "nationStr", "nativePlaceProvinceStr", "nativePlaceCityStr",
                    "nativePlaceRegionStr", "nativePlace", "maritalStr",
                    "politicalOutlookStr", "educationStr", "religiousBeliefStr", "occupationCategoryStr", "occupationStr",
                    "serviceSpace", "contactTypeStr","contactInformation", "placeDomicileProvinceStr", "placeDomicileCityStr", "placeDomicileRegionStr","placeDomicile", "placeDomicileAddress",
                    "currentResidenceProvinceStr",
                    "currentResidenceCityStr", "currentResidenceRegionStr", "currentResidenceStreetStr", "currentResidenceCommunityStr","currentResidence", "currentResidenceAddress","householdIdentityStr", "accountNumber", "householderIdCard",
                    "householderName", "householderGenderStr", "householderRelationshipStr","householderContactTypeStr", "householderContactInformation")
    ),*/

    /**
     * 导入会员
     * 会员账号
     */
    IMPORT_MEMBER(
            Lists.newArrayList( "会员账号"),
            Lists.newArrayList( "memberName")
    );


    private List<String> title;
    private List<String> code;

    TitleAndCodeEnum(List<String> title, List<String> code) {
        this.title = title;
        this.code = code;
    }

    public List<String> getTitle() {
        return title;
    }

    public void setTitle(List<String> title) {
        this.title = title;
    }

    public List<String> getCode() {
        return code;
    }

    public void setCode(List<String> code) {
        this.code = code;
    }
}
