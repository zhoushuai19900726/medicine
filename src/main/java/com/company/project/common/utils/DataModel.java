package com.company.project.common.utils;

import org.springframework.core.io.ClassPathResource;

import java.util.List;

/**
 * 导出Excel DataModel
 *
 * @author cwj
 */
public class DataModel {
    private ClassPathResource template;//Excel导出模板的路径
    private List<Object[]> data;//填充到模板的数据
    private String fileName;//浏览器下载显示的名称
    private int startRow = 1;//从哪行开始，默认为1
    private int type = 7;//excel版本，默认为07
    private String header; //Excel表头，默认第0行开始
    private int headerStartColumn = 0;//Excel表头，默认第0列开始
    private String explain;//注意说明，默认第一行开始
    private int explainStartColumn = 0;//注意说明，默认第0列开始
    private String outExcelPath;//Excel保存的位置


    public DataModel() {
        super();
    }

    public DataModel(ClassPathResource template, List<Object[]> data, String fileName,
                     int startRow, int type, String header, int headerStartColumn,
                     String explain, int explainStartColumn) {
        super();
        this.template = template;
        this.data = data;
        this.fileName = fileName;
        this.startRow = startRow;
        this.type = type;
        this.header = header;
        this.headerStartColumn = headerStartColumn;
        this.explain = explain;
        this.explainStartColumn = explainStartColumn;
    }

    public ClassPathResource getTemplate() {
        return template;
    }

    public void setTemplate(ClassPathResource template) {
        this.template = template;
    }

    public List<Object[]> getData() {
        return data;
    }

    public void setData(List<Object[]> data) {
        this.data = data;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getStartRow() {
        return startRow;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public int getHeaderStartColumn() {
        return headerStartColumn;
    }

    public void setHeaderStartColumn(int headerStartColumn) {
        this.headerStartColumn = headerStartColumn;
    }

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }

    public int getExplainStartColumn() {
        return explainStartColumn;
    }

    public void setExplainStartColumn(int explainStartColumn) {
        this.explainStartColumn = explainStartColumn;
    }

    public String getOutExcelPath() {
        return outExcelPath;
    }

    public void setOutExcelPath(String outExcelPath) {
        this.outExcelPath = outExcelPath;
    }


}
