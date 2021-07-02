package com.company.project.common.utils;

import com.alibaba.fastjson.JSONObject;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.util.UUID;

/**
 * @author catalpa
 * @version V1.0
 * @date 2021年3月9日
 */
public class CommonUtils {

    public static void main(String[] args) {

//        try {
//            //加载待读取图片
////            File imageFile = new File("/Users/zhoushuai/Downloads/铁铸盘热加工窑炉的余热回收装置-授权.tif");
//            File imageFile = new File("/Users/zhoushuai/Downloads/山东东阿长吉磨板有限责任公司.tif");
//            //创建tess对象
//            ITesseract instance = new Tesseract();
//            //设置训练文件目录
//            instance.setDatapath("/Users/zhoushuai/Downloads/tessdata");
//            //设置训练语言
//            instance.setLanguage("chi_sim");
//            //执行转换
//            String result = instance.doOCR(imageFile);
//            result = result.replaceAll(" ", "");
//            System.out.println(result);
//        } catch (Exception e) {
//            System.out.println(e);
//        }

        System.out.println(CommonUtils.generateShortUUID());


    }

    private static String[] chars = new String[]{"a", "b", "c", "d", "e", "f",
            "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
            "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z"};


    public static Boolean isJson(String content) {
        if (StringUtils.isEmpty(content)) {
            return false;
        }
        boolean isJsonObject = true;
        boolean isJsonArray = true;
        try {
            JSONObject.parseObject(content);
        } catch (Exception e) {
            isJsonObject = false;
        }
        try {
            JSONObject.parseArray(content);
        } catch (Exception e) {
            isJsonArray = false;
        }
        return isJsonObject || isJsonArray;
    }

    public static String generateShortUUID() {
        StringBuffer shortBuffer = new StringBuffer();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        for (int i = 0; i < 8; i++) {
            String str = uuid.substring(i * 4, i * 4 + 4);
            int x = Integer.parseInt(str, 16);
            shortBuffer.append(chars[x % 0x3E]);
        }
        return shortBuffer.toString().toUpperCase();
    }

    /**
     * 封装JDK自带的UUID, 通过Random数字生成, 中间无-分割.
     */
    public static String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

}
