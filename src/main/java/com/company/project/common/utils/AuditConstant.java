package com.company.project.common.utils;

import org.apache.commons.lang.StringUtils;

/**
 * Constant
 *
 * @author zhoushuai
 * @version V1.0
 * @date 2020年3月18日
 */
public class AuditConstant {


    public static final String INITIATE_AUDIT_APPLICATION = "发起审核申请";
    public static final String APPROVED = "审核通过";
    public static final String AUDIT_REJECTION = "审核驳回";

    public static final String AuditStatus (String goodsExamineStatus){
        String result = "";
        if(StringUtils.isNotBlank(goodsExamineStatus)){
            switch (goodsExamineStatus) {
                case "0" :
                    result = INITIATE_AUDIT_APPLICATION;
                    break;
                case "1" :
                    result = APPROVED;
                    break;
                case "2" :
                    result = AUDIT_REJECTION;
                    break;
            }
        }
        return result;
    }

}
