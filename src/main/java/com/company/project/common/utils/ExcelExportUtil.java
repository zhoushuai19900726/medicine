package com.company.project.common.utils;

import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Excel导出数据数据操作工具类
 *
 * @author
 * CRM 1.0
 */
public class ExcelExportUtil {
    /**
     * 导出Excel文件
     *
     * @throws Exception
     */
    public static InputStream exportFile(DataModel dataModel) throws Exception {
        InputStream fileInputStream = null;
        //读取模版
        try {
            fileInputStream = dataModel.getTemplate().getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null != fileInputStream) {
            Workbook workbook;
            //实例化工作簿
            if (dataModel.getType() == 3) {
                workbook = new HSSFWorkbook(fileInputStream);
            } else {
                workbook = new XSSFWorkbook(fileInputStream);
            }
            Sheet sheet = workbook.getSheetAt(0);
            //字体居中
            CellStyle centerStyle = workbook.createCellStyle();
            centerStyle.setAlignment(CellStyle.ALIGN_LEFT);

            /*
            centerStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            short border = 1;
            centerStyle.setBorderBottom(border);
            centerStyle.setBorderLeft(border);
            centerStyle.setBorderRight(border);
            centerStyle.setBorderTop(border);
            */

            //表头样式
            CellStyle headerStyle;
            Font headerFont;

            //设置表头和字体
            if (dataModel.getHeader() != null) {
                headerStyle = workbook.createCellStyle();
                headerFont = workbook.createFont();
                headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
                headerFont.setFontName("宋体");
                headerFont.setFontHeightInPoints((short) 20);
                headerStyle.setFont(headerFont);
                headerStyle.setAlignment(CellStyle.ALIGN_CENTER);
                headerStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

                Row titleRow = sheet.createRow(0);
                titleRow.setHeight((short) 600);
                Cell cell = titleRow.createCell(dataModel.getHeaderStartColumn());
                cell.setCellValue(dataModel.getHeader());
                cell.setCellStyle(headerStyle);
            }
            //表头样式
            CellStyle explainStyle;
            Font explainFont;
            //设置说明
            if (dataModel.getExplain() != null) {
                explainStyle = workbook.createCellStyle();
                explainFont = workbook.createFont();
                explainFont.setColor(Font.COLOR_RED);
                explainFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
                explainFont.setFontName("宋体");
                explainFont.setFontHeightInPoints((short) 11);
                explainStyle.setFont(explainFont);
                Row explainRow = sheet.createRow(1);
                Cell cell = explainRow.createCell(dataModel.getExplainStartColumn());
                cell.setCellValue(dataModel.getExplain());
                cell.setCellStyle(explainStyle);
            }

            List<Object[]> data = dataModel.getData();
            int startRow = dataModel.getStartRow();
            for (int i = 0; i < data.size(); i++) {
                Object[] objArray = data.get(i);
                //创建行
                Row row = sheet.createRow(startRow);
                Cell cell[];
                if (dataModel.getType() == 3) {
                    cell = new HSSFCell[objArray.length];
                } else {
                    cell = new XSSFCell[objArray.length];
                }
                for (int j = 0; j < objArray.length; j++) {
                    //创建列
                    if (objArray[j] != null) {
                        cell[j] = row.createCell(j);
                        cell[j].setCellStyle(centerStyle);
                        try {
                            cell[j].setCellValue(String.valueOf(objArray[j]));
                        } catch (Exception e) {
                            e.printStackTrace();
                            cell[j].setCellValue("");
                        }
                    }
                }
                startRow++;
            }
            try {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                workbook.write(bos);
                return new ByteArrayInputStream(bos.toByteArray());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static List<String> readExcelTitle(InputStream is, String fileName) {
        List<String> title = Lists.newArrayList();
        Workbook wb;
        Sheet sheet;
        try {
            boolean isExcel2003 = true;
            if (fileName.matches("^.+\\.(?i)(xlsx)$")) {
                isExcel2003 = false;
            }
            if (isExcel2003) {
                wb = new HSSFWorkbook(is);
            } else {
                wb = new XSSFWorkbook(is);
            }
            sheet = wb.getSheetAt(0);
        } catch (IOException e) {
            e.printStackTrace();
            return title;
        }
        // 得到总行数
        Row headRow = sheet.getRow(0);
        int colNum = headRow.getPhysicalNumberOfCells();//获取标题列数
        int j = 0;
        while (j < colNum) {
            String value = getCellFormatValue(headRow.getCell(j)).trim();
            if (StringUtils.isBlank(value)) {
                colNum = j;
                break;
            }
            title.add(getTitle(value));
            j++;
        }
        return title;
    }

    private static String getCellFormatValue(Cell cell) {
        String cellvalue;
        if (cell != null) {
            // 判断当前Cell的Type
            switch (cell.getCellType()) {
                // 如果当前Cell的Type为NUMERIC
                case HSSFCell.CELL_TYPE_NUMERIC:
                case HSSFCell.CELL_TYPE_FORMULA: {
                    // 判断当前的cell是否为Date
                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
                        // 如果是Date类型则，转化为Data格式
                        Date date = cell.getDateCellValue();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        cellvalue = sdf.format(date);
                    }
                    // 如果是纯数字
                    else {
                        // 取得当前Cell的数值
                        BigDecimal bd = new BigDecimal(String.valueOf(cell.getNumericCellValue()));//要修改的值，需要string类型
                        cellvalue = bd.setScale(0, BigDecimal.ROUND_HALF_UP).toPlainString();
                    }
                    break;
                }
                // 如果当前Cell的Type为STRIN
                case HSSFCell.CELL_TYPE_STRING:
                    // 取得当前的Cell字符串
                    cellvalue = cell.getRichStringCellValue().getString();
                    break;
                // 默认的Cell值
                default:
                    cellvalue = " ";
            }
        } else {
            cellvalue = "";
        }
        return cellvalue;

    }

    public static String getTitle(String title) {
        if (title == null || "".equals(title)) {
            return "";
        }
//        title = title.replaceAll("\\(元\\)", "");
//        title = title.replaceAll("\\(", "");
//        title = title.replaceAll("（", "");
//        title = title.replaceAll("\\)", "");
//        title = title.replaceAll("）", "");
        title = title.replaceAll(" ", "");
        title = title.replaceAll("非必填", "");
        title = title.replaceAll("必填", "");
        title = title.replaceAll("kg", "");
        return title;
    }

    public static List<LinkedHashMap<String, Object>> readExcelContent(InputStream is, String fileName, List<String> titleCode) {
        List<LinkedHashMap<String, Object>> content = Lists.newArrayList();
        Workbook wb;
        Sheet sheet;
        Row row;
        Cell val;
        try {
            boolean isExcel2003 = true;
            if (fileName.matches("^.+\\.(?i)(xlsx)$")) {
                isExcel2003 = false;
            }
            if (isExcel2003) {
                wb = new HSSFWorkbook(is);
            } else {
                wb = new XSSFWorkbook(is);
            }
            sheet = wb.getSheetAt(0);
        } catch (IOException e) {
            e.printStackTrace();
            return content;
        }
        // 得到总行数
        int rowNum = sheet.getLastRowNum();
        // 正文内容应该从第二行开始,第一行为表头的标题
        for (int i = 1; i <= rowNum; i++) {
            row = sheet.getRow(i);
            int j = 0;
            LinkedHashMap<String, Object> data = new LinkedHashMap<String, Object>();
            int flag = 0;//判断是否是空行标记
            while (j < titleCode.size()) {
                val = row.getCell(j);
                String value = getCellFormatValue(val).trim();
                data.put(titleCode.get(j), value);
                j++;
                if (StringUtils.isNotBlank(value)) {
                    flag = 1;
                }
            }
            content.add(data);
            if (flag == 0) {//遇到空行后，不再进行循环，自动认为excel中的数据到达最后一行
                break;
            }
        }
        return content;
    }


}
