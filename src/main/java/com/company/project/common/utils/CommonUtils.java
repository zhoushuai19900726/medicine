package com.company.project.common.utils;

import com.alibaba.fastjson.JSONObject;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import org.apache.commons.lang.StringUtils;

import java.io.File;

/**
 * @author catalpa
 * @version V1.0
 * @date 2021年3月9日
 */
public class CommonUtils {

    public static void main(String[] args) {

        try {
            //加载待读取图片
//            File imageFile = new File("/Users/zhoushuai/Downloads/铁铸盘热加工窑炉的余热回收装置-授权.tif");
            File imageFile = new File("/Users/zhoushuai/Downloads/山东东阿长吉磨板有限责任公司.tif");
            //创建tess对象
            ITesseract instance = new Tesseract();
            //设置训练文件目录
            instance.setDatapath("/Users/zhoushuai/Downloads/tessdata");
            //设置训练语言
            instance.setLanguage("chi_sim");
            //执行转换
            String result = instance.doOCR(imageFile);
            result = result.replaceAll(" ", "");
            System.out.println(result);
        } catch (Exception e) {
            System.out.println(e);
        }


    }


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

}
