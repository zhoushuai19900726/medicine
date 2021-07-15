package com.company.project.common.utils;

import com.company.project.entity.ShopMemberEntity;

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
public class DelimiterConstants {

    /**
     * 分隔符
     */
    public static final String COLON = ":";
    public static final String THREE_WELL_NUMBER = "###";
    public static final String WELL_NUMBER = "#";
    public static final String COMMA = ",";
    public static final String SEMICOLON = ";";
    public static final String POINT = "\\.";
    public static final String SIGN = "%";
    public static final String CHINA_COMMA = "，";
    public static final String UNDER_LINE = "_";
    public static final String LINE = "-";
    public static final String DOLLAR = "$";
    public static final String AND = "&";
    public static final String TWO_AND = "&&";
    public static final String PERHAPS = "|";
    public static final String SPACE_PERHAPS = " | ";
    public static final String TWO_PERHAPS = "||";
    public static final String INTERROGATION = "?";
    public static final String CHARACTERS_TYPE = "type";
    public static final String UNDERLINE_ZERO_UNDERLINE = "_0_";
    public static final String UNDERLINE_ONE_UNDERLINE = "_1_";
    public static final String LEFT_PARENTHESIS = "(";
    public static final String RIGHT_PARENTHESIS = ")";
    public static final String RIGHT_BRACKET = "]";
    public static final String GREATER = ">";
    public static final String LESS_EQUAL = "<=";
    public static final String EQUAL = "=";
    public static final String EMPTY_STR = "";
    public static final String NULL_STR = "null";

    public static final String PREFIX = "SC";

    public static final String ON = "on";
    public static final String OFF = "off";

    public static final String ENCODE = "UTF-8";
    public static final String ENCODE_GBK = "GBK";
    public static final String ENCODE_ISO = "iso-8859-1";

    public static final String NEW_LINE = "\n";
    public static final String DB_NEW_LINE = "\r\n";
    public static final String HTML_NEW_LINE = "<br/>";

    public static final String REGULAR_EXPRESSION_NUMBER = "^(\\-|\\+)?\\d+(\\.\\d+)?$";
    public static final String REGULAR_EXPRESSION_HAD_NUMBER = "\\d+";
    public static final String REGULAR_EXPRESSION_NUMBERS = "[^0-9]";
    public static final String REGULAR_EXPRESSION_SPACE = "\\s+";
    public static final String REGULAR_EXPRESSION_DOUBLE_QUOTES = "\"";
    public static final String REGULAR_EXPRESSION_SPACE_ENTER = "\\s*|\t|\r|\n";

    public static final char QUESTION_MARK = '?';

    public static final String EXCELDATAVO_SORTFIELDNAME = "text";
    public static final String SORT_MODE_DESC = "desc";
    public static final String SORT_MODE_ASC = "asc";

    public static final String INIT_PASSWORD = "123456";
    public static final String COVER_PASSWORD = "********";

    public static final String VERSION = "version";

    public static final String INDEX_TITLE = "级指标";
    public static final String CURRENCY = "通用";

    // TODO 系统配置中设置默认头像
    public static final String DEFAULT_AVATAR = "http://localhost:8080/files/20210709/ec89c38d64cc40388d5e4487c2320214.jpeg";

    public static final String SYS_ADMIN_ID = "1";
    private static final String SYS_ADMIN_NAME = "ZHOUSHUAI";
    private static final String SYS_ADMIN_NICK = "平台";
    private static final String SYS_ADMIN_MOBILE = "18615662262";
    private static final String SYS_ADMIN_PASSWORD = "ZHOUSHUAI_19900726";
    private static final String SYS_ADMIN_INVITATION_CODE = "A0000001";


    public static ShopMemberEntity INIT_MEMBER() {
        return new ShopMemberEntity(
                SYS_ADMIN_ID,
                SYS_ADMIN_NAME,
                SYS_ADMIN_NICK,
                PasswordUtils.encode(SYS_ADMIN_PASSWORD, PasswordUtils.getSalt()),
                PasswordUtils.encode(SYS_ADMIN_PASSWORD, PasswordUtils.getSalt()),
                SYS_ADMIN_NICK,
                SYS_ADMIN_MOBILE,
                SYS_ADMIN_INVITATION_CODE,
                DEFAULT_AVATAR
        );
    }


    /**
     * 加解密-秘钥
     */
    public static final String LOGIN_ENCRYPT_KEY = "ELVA-NO-LONGER-HURT";
    /**
     * 向量
     */
    public static final byte[] DESIV = new byte[]{0x12, 0x34, 0x56, 120, (byte) 0x90, (byte) 0xab, (byte) 0xcd, (byte) 0xef};


}
