package com.hz.demo.controller;

import com.hz.demo.core.PageData;
import com.hz.demo.core.ResultUtils;
import com.hz.demo.core.TableReturn;
import com.hz.demo.core.Tree;
import com.hz.demo.model.*;
import com.hz.demo.services.*;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.text.SimpleDateFormat;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.*;

import static com.hz.demo.core.EncodeUtil.decodeBase64;
import static com.hz.demo.core.EncodeUtil.encodeMD5;
import static com.hz.demo.core.ResultUtils.toDateTimeJson;
import static com.hz.demo.core.ResultUtils.toJson;

@Controller
@RequestMapping("/Log")
public class LogController extends BaseController {

    @Autowired
    UserService userService;
    @Autowired
    LogService logService;
    private String cusname;

    @RequestMapping(value = "LogPage")
    public String LogPage(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("MenuBtns",userService.GetMenuBtns(request.getParameter("menuid"),getUser().getUserId()));
        return "sys/log";
    }
    @RequestMapping(value = "Ueditor")
    public String Ueditor(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("MenuBtns",userService.GetMenuBtns(request.getParameter("menuid"),getUser().getUserId()));
        return "sys/ueditor";
    }


    //获取界面列表
    @RequestMapping(value = "getList")
    public void getList(HttpServletRequest request, HttpServletResponse response) {
        logger.info("显示日志列表数据");
        PageData pd = new PageData();
        pd.put("rows", request.getParameter("rows") == null? 10: Integer.parseInt(request.getParameter("rows")));//每页显示行数
        pd.put("page", request.getParameter("page") == null ? 0 : (Integer.parseInt(request.getParameter("page"))));//每页记录开始位置
        pd.put("message", request.getParameter("message") == null ? "" : request.getParameter("message"));//名称
        TableReturn tablereturn = new TableReturn();
        List<sys_log> list = logService.getList(pd);
        Integer listCount = logService.getListCount(pd);
        tablereturn.setRows(list);
        tablereturn.setTotal(listCount);
        ResultUtils.write(response, toDateTimeJson(tablereturn));
    }

    //删除
    @RequestMapping(value = "Delete")
    public void Delete(HttpServletRequest request, HttpServletResponse response) {
        String ids = request.getParameter("ids") == null ? "" : request.getParameter("ids");
        try {
            String[] idItem = ids.split(",");
            for (String id : idItem) {
                logService.delete(id);
            }
            ResultUtils.writeMessage(response, 1, "删除成功");
            WriteLog("删除id为" + ids + "的日志信息");
        } catch (Exception ex) {
            ResultUtils.writeMessage(response, 0, ex.getMessage());
        }
    }



    @RequestMapping("/exportExcelLog.do")
    public String exportExcel(HttpServletRequest request, HttpServletResponse response, Model model) {
        logger.info("导出日志信息");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sss = new SimpleDateFormat("yyyy-MM-dd ");
        String filename = sss.format(new Date().getTime())+ "日志信息";
       // Calendar.getInstance().get(Calendar.YEAR);
        int page = request.getParameter("page") == null ? 1 : Integer.parseInt(request.getParameter("page"));
       // System.out.println("789"+offset);
        int rows = request.getParameter("rows") == null ? 10000000 : Integer.parseInt(request.getParameter("rows"));
        //System.out.println("789456"+limit);
//        String fcode = request.getParameter("fcode") == null ? "" : request.getParameter("fcode");
//        String createtime = request.getParameter("createtime") == null ? "" : request.getParameter("createtime");
//        String createtimes = request.getParameter("createtimes") == null ? "" : request.getParameter("createtimes");
        PageData pageData = new PageData();
        pageData.put("page", page);
        pageData.put("rows",rows);

//        pageData.put("fcode", fcode);
//        pageData.put("createtime", createtime);
//        pageData.put("createtimes", createtimes);
       // List<User2> list = user2Services.getAllUser2(pageData);
        List<sys_log> list = logService.getList(pageData);
        //System.err.println("****"+list.size());
        HSSFWorkbook wb = new HSSFWorkbook();//创建excel工作簿
        HSSFSheet sheet = wb.createSheet("Sheet");//在Excel工作簿中建一工作表，其名为缺省值, 也可以指定Sheet名称，例如Sheet
        HSSFCellStyle style = wb.createCellStyle();//生成一个样式
        HSSFFont font = wb.createFont(); // 生成一个字体
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平居中
        //style.setWrapText(true); // 自动换行
        font.setFontHeightInPoints((short) 14);
        font.setFontName("宋体");//设置字体
        style.setFont(font);// 把字体样式 应用到当前样式
        HSSFRow row = sheet.createRow((int) 0);
        CellRangeAddress cra = new CellRangeAddress(0, 0, 0, 6);//合并
        sheet.addMergedRegion(cra);//合并第一行1到6
        //表头
        HSSFCell cell = row.createCell(0);
        cell.setCellValue(filename);
        cell.setCellStyle(style);
        row = sheet.createRow(1);//设置表头起始行
        mycreateCell(row,0,"序号",style);
        mycreateCell(row, 1, "日志编码", style);
        mycreateCell(row, 2, "客户端IP", style);
        mycreateCell(row, 3, "操作路径", style);
        mycreateCell(row, 4, "操作人", style);
        mycreateCell(row, 5, "操作时间", style);
        mycreateCell(row, 6, "内容", style);

        for (int i = 0; i < list.size(); i++) {
            int j = i + 2;//设置数据起始行
            row = sheet.createRow(j);
            //设值
            mycreateCell(row,0,i+1,style);
            mycreateCell(row,1, String.valueOf(list.get(i).getLogId()), style);
            mycreateCell(row, 2, String.valueOf(list.get(i).getIp()), style);
            mycreateCell(row, 3, String.valueOf(list.get(i).getPage()), style);
            mycreateCell(row, 4, String.valueOf(list.get(i).getAddUserId()), style);

//            if (!list.get(i).getAddUserId().equalsIgnoreCase("")) {
//                System.err.println("****"+list.get(i).getAddUserId());
//                sys_user user = userService.getModel(list.get(i).getAddUserId());
//                mycreateCell(row, 4, String.valueOf(user.getDisplayName()), style);
//            } else {
//                mycreateCell(row, 4, String.valueOf(""), style);
//            }

            mycreateCell(row, 5, String.valueOf(sdf.format(list.get(i).getAddTime())), style);
            mycreateCell(row, 6, String.valueOf(list.get(i).getMessage()), style);

            sheet.autoSizeColumn((short) 0); //调整第一列宽度，自适应
            sheet.autoSizeColumn((short) 1); //调整第二列宽度，自适应
            sheet.autoSizeColumn((short) 2); //调整第三列宽度，自适应
            sheet.autoSizeColumn((short) 3); //调整第四列宽度，自适应
            sheet.autoSizeColumn((short) 4); //调整第五列宽度，自适应
            sheet.autoSizeColumn((short) 5); //调整第五列宽度，自适应
            sheet.autoSizeColumn((short) 6); //调整第五列宽度，自适应

        }
        try {
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
            logger.error("exportExcelLog e=" + e.getMessage());
            ResultUtils.writeFailed(response);
        }
        return null;
    }
    //设置表格数据格式--字符串
    public static void mycreateCell(HSSFRow row, int i, String value, HSSFCellStyle style) {
        HSSFCell cell = row.createCell(i);
        cell.setCellValue(value);
        cell.setCellStyle(style);
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

}

