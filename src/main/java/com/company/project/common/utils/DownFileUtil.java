package com.company.project.common.utils;

import org.springframework.core.io.ClassPathResource;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

/**
 * 下载文件
 *
 * @ClassName 类名：DownFileUtil
 * @Author作者:
 * @Date时间：2020/9/7 14:31
 **/
public class DownFileUtil {

    /**
     * 通过传入文件路径 下载文件
     *
     * @param path 文件路径
     */
    public static void downFileByPath(HttpServletResponse response, String path) {
        ClassPathResource resource = new ClassPathResource(path);
        InputStream inputStream = null;
        ServletOutputStream outputStream = null;
        try {
            //获取文件输入流
            inputStream = resource.getInputStream();

            //创建数据缓冲区
            byte[] buffers = new byte[1024];
            //设置下载excel
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(resource.getFilename().getBytes(DelimiterConstants.ENCODE), DelimiterConstants.ENCODE_ISO));
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            //通过response中获取ServletOutputStream输出流
            outputStream = response.getOutputStream();
            int length;
            while ((length = inputStream.read(buffers)) > 0) {
                //写入到输出流中
                outputStream.write(buffers, 0, length);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
                outputStream.close();
            } catch (IOException e) {
                // e.printStackTrace();
            }
        }
    }



}
