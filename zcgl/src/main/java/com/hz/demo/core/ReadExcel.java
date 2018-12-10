package com.hz.demo.core;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by hcf on 2018-10-15.
 */
public class ReadExcel {
    private int totalRows = 0;
    private int totalCells = 0;

    public ReadExcel(){

    }

    public int getTotalRows() {
        return totalRows;
    }

    public int getTotalCells() {
        return totalCells;
    }

    public List<List<String>> getExcelInfo(String path) {
        //初始化集合
        List<List<String>> lList = new ArrayList<List<String>>();
        //初始化输入流
        InputStream is = null;
        try {
            //根据文件名判断文件是2003/2007版本
            boolean isExcel2003 = true;
            if (path.matches("^.+\\.(?i)(xlsx)$")) {
                isExcel2003 = false;
            }
            String newPath = path.replace("\\","/");
            //根据新建的文件实例化输入流
            is = new FileInputStream(newPath);
            File file = new File(newPath);
            Workbook wb = null;
            try {
                wb = create(is);
            } catch (InvalidFormatException e) {
                e.printStackTrace();
            }

            //根据excel里面的内容读取客户信息
            /*if (isExcel2003) {
                wb = new HSSFWorkbook(is);
            } else {
                wb = new XSSFWorkbook(is);
            }*/
            //读取Excel信息
            lList = readExcelValue(wb);
            is.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    is = null;
                    e.printStackTrace();
                }
            }
        }
        return lList;
    }

    //获取EXCEL中数据
    private List<List<String>> readExcelValue(Workbook wb){
        //得到第一个shell  
        Sheet sheet=wb.getSheetAt(0);

        //得到Excel的行数
        this.totalRows=sheet.getPhysicalNumberOfRows();

        //得到Excel的列数(前提是有行数)
        if(totalRows>=1 && sheet.getRow(0) != null){//判断行数大于一，并且第一行必须有标题（这里有bug若文件第一行没值就完了）
            this.totalCells=sheet.getRow(0).getPhysicalNumberOfCells();
        }else{
            return null;
        }

        List<List<String>> lList=new ArrayList<>();//声明一个对象集合
        //base_speciality baseSpeciality;//声明一个对象

        //循环Excel行数,从第二行开始。标题不入库
        for(int r=1;r<totalRows;r++){
            Row row = sheet.getRow(r);
            if (row == null) continue;
            List<String> stringList = new ArrayList<>();
            //循环Excel的列
            for(int c = 0; c <this.totalCells; c++){
                Cell cell = row.getCell(c);
                if (cell == null) {
                    stringList.add("");
                    continue;
                }
                stringList.add(getValue(cell));
            }
            //添加对象到集合中
            lList.add(stringList);
        }
        return lList;
    }

    private String getValue(Cell cell) {
        DecimalFormat df = new DecimalFormat("0");//格式化number String字符串
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//日期格式化
        if (cell.getCellType() == cell.CELL_TYPE_BOOLEAN) {
            // 返回布尔类型的值
            return String.valueOf(cell.getBooleanCellValue());
        } else if (cell.getCellType() == cell.CELL_TYPE_BLANK) {
            // 返回空类型的值
            return String.valueOf("");
        }
        else if (cell.getCellType() == cell.CELL_TYPE_NUMERIC) {
           String value = "";
            if (DateUtil.isCellDateFormatted(cell)) {//日期类型
                Date date = cell.getDateCellValue();
                value = sdf.format(date);
            }else if ("General".equals(cell.getCellStyle().getDataFormatString())) {
               // value = df.format(cell.getNumericCellValue());
                BigDecimal bigDecimal = new BigDecimal(cell.getNumericCellValue());
                value = bigDecimal.toString();
                //解决1234.0  去掉后面的.0
                if(null!=value&&!"".equals(value.trim())){
                    String[] item = value.split("[.]");
                    if(1<item.length&&"0".equals(item[1])){
                        value=item[0];
                    }
                }
            } else {
                value = cell.getRichStringCellValue().getString();
            }
            // 返回数值类型的值
            return value;
        } else {
            // 返回字符串类型的值
            return String.valueOf(cell.getStringCellValue());
        }
    }

    public static Workbook create(InputStream in) throws
            IOException, InvalidFormatException {
        if (!in.markSupported()) {
            in = new PushbackInputStream(in, 8);
        }
        if (POIFSFileSystem.hasPOIFSHeader(in)) {
            return new HSSFWorkbook(in);
        }
        if (POIXMLDocument.hasOOXMLHeader(in)) {
            return new XSSFWorkbook(OPCPackage.open(in));
        }
        throw new IllegalArgumentException("你的excel版本目前poi解析不了");
    }
}
