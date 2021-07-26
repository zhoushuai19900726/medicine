package com.company.project.common.utils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期处理
 *
 * @author wenbin
 * @version V1.0
 * @date 2020年3月18日
 */
public class DateUtils {
    /**
     * 时间格式(yyyy-MM-dd HH:mm:ss)
     */
    public final static String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    /**
     * 时间格式(yyyyMMdd)
     */
    public final static String DATEPATTERN = "yyyyMMdd";

    public static String format(Date date, String pattern) {
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            return df.format(date);
        }
        return null;
    }

    /**
     * String 日期转DATE
     *
     * @return DATE
     */
    public static Date parse(String strDate) {
        DateTimeFormatter format = DateTimeFormat.forPattern(DATE_TIME_PATTERN);
        return DateTime.parse(strDate, format).toDate();
    }
}
