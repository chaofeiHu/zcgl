package com.hz.demo.core;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 导出Excel
 */
public class ExportExcel {


    public void Excel(Map<String,Object> dataMap, HSSFWorkbook wb, HttpServletRequest request, HttpServletResponse response){
        HSSFSheet sheet = wb.createSheet("Sheet");//在Excel工作簿中建一工作表，其名为缺省值, 也可以指定Sheet名称，例如Sheet
        HSSFCellStyle style = wb.createCellStyle();//生成一个样式
        HSSFFont font = wb.createFont(); // 生成一个字体
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平居中
        font.setFontHeightInPoints((short) 14);
        font.setFontName("宋体");//设置字体
        style.setFont(font);// 把字体样式 应用到当前样式
        HSSFRow row = sheet.createRow(0);
        CellRangeAddress cra = new CellRangeAddress(0, 0, 0, 6);//合并
        List<String> titles = (List<String>) dataMap.get("titles");
        row = sheet.createRow(0);//设置表头起始行
        for(int w=0;w<titles.size();w++){  //设置标题
            mycreateCell(row,w,titles.get(w),style);
        }
        List<PageData> varList = (List<PageData>) dataMap.get("varList");
        for (int i =0; i <varList.size(); i++) {
            PageData pageData=varList.get(i);
            row = sheet.createRow(i+1);
            for(int j=0;j<titles.size();j++){
                mycreateCell(row,j, pageData.get("var"+(j+1)), style);
                sheet.autoSizeColumn((short) j); //调整第一列宽度，自适应
            }
        }
        try {
            SimpleDateFormat sss = new SimpleDateFormat("yyyy-MM-dd ");
            String filename = sss.format(new Date().getTime());
            response.setContentType("text/html;charset=UTF-8");
            response.reset();// 清空输出流
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment;filename=\""
                    + new String((filename + ".xls").getBytes("GBK"),
                    "ISO8859_1") + "\"");
            OutputStream ouputStream = response.getOutputStream();
            wb.write(ouputStream);
            ouputStream.flush();
            ouputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            ResultUtils.writeFailed(response);
        }
       // return wb;
    }

    //设置表格数据格式--字符串
    public static void mycreateCell(HSSFRow row, int i, String value, HSSFCellStyle style) {
        HSSFCell cell = row.createCell(i);
        if(!value.equals("null")){
            cell.setCellValue(value);
            cell.setCellStyle(style);
        }
    }
    //设置表格数据格式--字符串
    public static void mycreateCell(HSSFRow row, int i, Date value, HSSFCellStyle style) {
        HSSFCell cell = row.createCell(i);
        cell.setCellValue(value);
        cell.setCellStyle(style);
    }

    //数值型
    public void mycreateCell(HSSFRow row, int i, double value, HSSFCellStyle style) {
        HSSFCell cell = row.createCell(i);
        cell.setCellValue(value);
        cell.setCellStyle(style);
    }

    //整型
    public static void mycreateCell(HSSFRow row, int i, Integer value, HSSFCellStyle style) {
        HSSFCell cell = row.createCell(i);
        cell.setCellValue(value);
        cell.setCellStyle(style);
    }
    public static void mycreateCell(HSSFRow row, int i, Object value, HSSFCellStyle style) {
        HSSFCell cell = row.createCell(i);
        cell.setCellValue(value.toString());
        cell.setCellStyle(style);
    }

}
