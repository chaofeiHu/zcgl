package com.hz.demo.controller;

import com.hz.demo.core.*;
import com.hz.demo.model.*;
import com.hz.demo.services.RecCertificateService;
import com.hz.demo.services.RecFileManagerService;
import com.hz.demo.services.RecResultService;
import com.hz.demo.services.UserService;
import freemarker.template.Template;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.hz.demo.core.ResultUtils.*;

//文件管理页面用
@Controller
@RequestMapping("/RecFileManager")
public class RecFileManagerController extends BaseController{

    @Autowired
    UserService userService;
    @Autowired
    RecFileManagerService recFileManagerService;
    @Autowired
    RecResultService recResultService;
    @Autowired
    RecCertificateService recCertificateService;

    //跳到生成任职资格文件界面
    @RequestMapping(value = "RecFileCreatePage")
    public String RecFileCreatePage(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("MenuBtns",userService.GetMenuBtns(request.getParameter("menuid"),getUser().getUserId()));
        return "sys/recFileCreate";
    }

    //跳到任职资格文件管理界面
    @RequestMapping(value = "RecFileManagerPage")
    public String RecFileManagerPage(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("MenuBtns",userService.GetMenuBtns(request.getParameter("menuid"),getUser().getUserId()));
        return "sys/recFileManager";
    }

    //获取生成任职资格界面列表
    @RequestMapping(value = "getList")
    public void getList(HttpServletRequest request, HttpServletResponse response) {
        logger.info("显示任职资格文件列表数据");
        PageData pd = new PageData();
        pd.put("rows", request.getParameter("rows") == null? 10: Integer.parseInt(request.getParameter("rows")));//每页显示行数
        pd.put("page", request.getParameter("page") == null ? 1 : (Integer.parseInt(request.getParameter("page"))));//每页记录开始位置
        pd.put("userName", request.getParameter("userName"));
        pd.put("yearNo", request.getParameter("yearNo"));
        pd.put("judgingId", request.getParameter("judgingCode"));
        pd.put("unitCode", request.getParameter("unitCode"));
        pd.put("reviewSeries", request.getParameter("reviewSeries"));
        pd.put("professialCode", request.getParameter("professialCode"));
        pd.put("manageUnitCode", getUser().getUnitCode());
        TableReturn tablereturn = new TableReturn();
        List<rec_result> blist = recFileManagerService.getList(pd);
        Integer listCount = recFileManagerService.getListCount(pd);
        tablereturn.setRows(blist);
        tablereturn.setTotal(listCount);
        ResultUtils.write(response, toDateTimeJson(tablereturn));
    }

    //获取任职资格管理界面列表
    @RequestMapping(value = "getFileList")
    public void getFileList(HttpServletRequest request, HttpServletResponse response) {
        logger.info("获取任职资格管理界面列表");
        PageData pd = new PageData();
        String yearNo = request.getParameter("yearNo");
        pd.put("rows", request.getParameter("rows") == null? 10: Integer.parseInt(request.getParameter("rows")));//每页显示行数
        pd.put("page", request.getParameter("page") == null ? 1 : (Integer.parseInt(request.getParameter("page"))));//每页记录开始位置
        pd.put("fileTitle", request.getParameter("fileTitle"));
        pd.put("yearNo", yearNo);
        pd.put("areaCode", request.getParameter("areaCode"));
        pd.put("manageUnitCode", getUser().getUnitCode());
        TableReturn tablereturn = new TableReturn();
        List<rec_fileManager> blist = recFileManagerService.getFileList(pd);
        Integer listCount = recFileManagerService.getFileListCount(pd);
        tablereturn.setRows(blist);
        tablereturn.setTotal(listCount);
        ResultUtils.write(response, toDateTimeJson(tablereturn));
    }

    //确定发文
    @Transactional(propagation= Propagation.REQUIRED)
    @RequestMapping(value = "confirmSendFile")
    public void confirmSendFile(HttpServletRequest request, HttpServletResponse response) {
        logger.info("确定发文");
        String ids = request.getParameter("ids");
        String fileCode = request.getParameter("fileCode");
        String fileTitle = request.getParameter("fileTitle");
        String addtime = request.getParameter("addtime");
        rec_fileManager recFileManager = new rec_fileManager();
        Map<String, Object> dataMap = new HashMap<>();
        List<rec_result> resultList = new ArrayList<>();
        List<Map<String, Object>> list = new ArrayList<>();
        String path =request.getSession().getServletContext().getRealPath("/");//文件路径
        String modelPath = path + "static/excelModel";
        String toFilePath = path + "upload/zcgl/pdf";
        SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sd =   new SimpleDateFormat("yyyy-MM-dd");
        String pdfName = null;
        try {
            if (ids != null && !"".equals(ids)) {
                String[] rids = ids.split(",");
                for (int i = 0;i < rids.length; i++ ) {
                    rec_result recResult = null;
                    rec_certificate recCertificate = null;
                    //更改生成文件状态
                    Map map = new HashMap();
                    PageData pageData = new PageData();
                    pageData.put("id", new BigDecimal(rids[i]));
                    pageData.put("isbuilefile",new BigDecimal(1) );
                    recResultService.update(pageData);
                    //获取评审结果信息
                    recResult = recResultService.getModelById(new BigDecimal(rids[i]));
                    recFileManager.setYearNo(recResult.getYearNo());
                    resultList.add(recResult);
                    //评审结果信息
                    map.put("areaCode",recResult.getAreaCode());
                    map.put("judgingCode",recResult.getJudgingCode());
                    map.put("groupId",recResult.getGroupId());
                    map.put("unitCode",recResult.getUnitCode());
                    map.put("reviewSeries",recResult.getReviewSeries());
                    map.put("titleLevel",recResult.getTitleLevel());
                    map.put("positionalTitles",recResult.getPositionalTitles());
                    map.put("professialCode",recResult.getProfessialCode());
                    map.put("reviewType",recResult.getReviewType());
                    map.put("userName",recResult.getUserName());
                    map.put("idCardNo",recResult.getIdCardNo());
                    map.put("sex",recResult.getSex());
                    //map.put("gettime",recResult.getGettime());
                    map.put("getway",recResult.getGetway());
                    String idCardNo = recResult.getIdCardNo();
                    String birthDate = idCardNo.substring(6, 10)+"-"+idCardNo.substring(10, 12);
                    map.put("birthDate", birthDate);
                    recCertificate = recCertificateService.getModelByResultid(new BigDecimal(rids[i]));
                    map.put("certificateNumber", recCertificate.getCertificateNumber());
                    Date getTime = recCertificate.getAddtime() == null ? new Date():recCertificate.getAddtime();
                    map.put("gettime", sd.format(getTime));
                    list.add(map);
                }
            }
            recFileManager.setRecResultList(resultList);
            if (null != addtime && !"".equals(addtime)){
                recFileManager.setAddtime(sdf.parse(addtime));
            }
            recFileManager.setFileCode(fileCode);
            recFileManager.setFileTitle(fileTitle);
            dataMap.put("fileCode", fileCode);
            dataMap.put("fileTitle", fileTitle);
            dataMap.put("addtime", addtime);
            dataMap.put("yearNo", recFileManager.getYearNo());
            dataMap.put("list", list);
            dataMap.put("count", list.size());
            //生成文件
            try {
                File file1=new File(toFilePath);
                if(!file1.exists()){//如果文件夹不存在
                    file1.mkdirs();//创建文件夹
                }
                String toFilePath1 = fileTitle + (new Date().getTime()) + ".docx";
                String tempPath = toFilePath +"/"+fileTitle + (new Date().getTime()) + ".xml";
                //模板文件路径
                String docxTemplate = modelPath + "/zcmb.docx";
                XmlToDocx.toDocx("document.xml",docxTemplate,tempPath,toFilePath +"/"+toFilePath1,dataMap);
                File file = new File(toFilePath +"/"+toFilePath1);
                if (file != null && file.exists()) {
                    String filePath = file.getPath().replace("\\","/");
                    recFileManager.setFilePath(filePath);
                    //转成pdf方便预览
                    pdfName = XMlToDoc.makePdfByXcode(filePath);
                } else {//如果文件生成失败 回滚事务
                    throw new RuntimeException();
                }
                if (pdfName != null) {
                    recFileManager.setBack2("upload/zcgl/pdf/" + toFilePath1.replace(".docx", ".pdf"));
                    //删除临时文件
                    new File(tempPath).delete();
                } else {
                    throw new RuntimeException();
                }
                recFileManagerService.add(recFileManager);
                ResultUtils.writeMessage(response,1,"发文成功!");
            } catch (Exception e) {
                e.printStackTrace();
                ResultUtils.writeMessage(response,0,"发文失败!");
                throw new RuntimeException();
            }
        } catch (ParseException e) {
            e.printStackTrace();
            ResultUtils.writeMessage(response,0,"发文失败!");
            throw new RuntimeException();
        }
        //ResultUtils.writeMessage(response,1,"发文成功!");
    }

    //下载
    @RequestMapping(value = "downLoadFile")
    public void downLoadFile(HttpServletRequest request, HttpServletResponse response) {
        logger.info("下载文件");
        String id = request.getParameter("id") == null ? "" : request.getParameter("id");
        rec_fileManager recFileManager = null;
        InputStream fin = null;
        ServletOutputStream out = null;
        String filePath = null;
        String fileTitle = null;
        try {
            if (!("").equals(id)) {
                recFileManager = recFileManagerService.getModel(new BigDecimal(id));
                filePath = recFileManager.getFilePath();
                fileTitle = recFileManager.getFileTitle();
            }
            if (filePath != null) {
                File file = new File(filePath);
                fin = new FileInputStream(file);
                /*Doc2HtmlUtil doc2HtmlUtil = new Doc2HtmlUtil();
                doc2HtmlUtil.file2pdf(fin,filePath,"doc");*/
                out = response.getOutputStream();
                response.setCharacterEncoding("utf-8");
                response.setContentType("application/msword");
                response.setHeader("Content-Disposition", "attachment;filename="
                        .concat(String.valueOf(URLEncoder.encode(fileTitle+".docx", "UTF-8"))));
                byte[] buffer = new byte[512];  // 缓冲区
                int bytesToRead = -1;
                // 通过循环将读入的Word文件的内容输出到浏览器中
                while((bytesToRead = fin.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesToRead);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if(fin != null) fin.close();
                if(out != null) out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //文件添加或更新（获取）
    @RequestMapping(value = "addOrUpdate")
    public void AddOrUpdate(HttpServletRequest request, HttpServletResponse response) {
        logger.info("文件添加或更新（获取）");
        Integer fid = Integer.parseInt(request.getParameter("id") == null ? "0" : request.getParameter("id"));
        PageData pageData = new PageData();
        rec_fileManager recFileManager = null;
        if (fid != 0 && !fid.equals("")) {
            //更新（获取）
            recFileManager = recFileManagerService.getModel(new BigDecimal(fid));
        }
        if (recFileManager != null) {
            ResultUtils.write(response, toDateTimeJson(recFileManager));
        } else {
            ResultUtils.write(response, "记录信息不存在");
        }
    }

    @RequestMapping(value = "Save")
    public void Save(HttpServletRequest request, HttpServletResponse response) {
        logger.info("保存");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sd =  new SimpleDateFormat("yyyy");
        String fid = request.getParameter("id") == null ? "" : request.getParameter("id");
        PageData pageData = new PageData();
        String fileCode = request.getParameter("fileCode");
        String yearNo = request.getParameter("yearNo");
        String fileTitle = request.getParameter("fileTitle");
        String filePath = request.getParameter("filePath");
        String addtime = request.getParameter("addtime");
        String back1 = request.getParameter("back1");
        Date date = null;
        try {
            if (null != addtime && !"".equals(addtime)){
                date = sdf.parse(addtime);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        pageData.put("fileCode", fileCode);
        pageData.put("yearNo", yearNo);
        pageData.put("fileTitle", fileTitle);
        pageData.put("filePath", filePath);
        pageData.put("addtime", date);
        pageData.put("back1", back1);
        if ("".equals(fid)) { //添加
            rec_fileManager recFileManager = new rec_fileManager();
            recFileManager.setFilePath(filePath);
            recFileManager.setFileTitle(fileTitle);
            recFileManager.setFileCode(fileCode);
            recFileManager.setYearNo(yearNo);
            recFileManager.setAddtime(date);
            recFileManager.setBack1(back1);
            if (recFileManagerService.add(recFileManager) == 1) {
                ResultUtils.writeMessage(response, 1, "添加成功");
            } else {
                ResultUtils.writeMessage(response, 0, "添加失败");
            }
        } else {//修改
            Integer id = Integer.parseInt(fid);
            pageData.put("id", id);
            if (recFileManagerService.update(pageData) == 1){
                ResultUtils.writeMessage(response, 1, "修改成功");
            }
            else{
                ResultUtils.writeMessage(response, 0, "修改失败");
                throw new RuntimeException();
            }
        }

    }

    //删除
    @Transactional(propagation= Propagation.REQUIRED)
    @RequestMapping(value = "delete")
    public void Delete(HttpServletRequest request, HttpServletResponse response) {
        String ids = request.getParameter("ids") == null ? "" : request.getParameter("ids");
        String[] strs = ids.split(",");
        try {
            for (String id:strs) {
                BigDecimal id1 = new BigDecimal(id);//文件id
                int flag = 0;
                flag = recFileManagerService.delete(id1);
                if (flag == 1){
                    ResultUtils.writeMessage(response, 1, "删除成功");
                }
                else{
                    ResultUtils.writeMessage(response, 0, "删除失败");
                    throw new RuntimeException();
                }
            }
        } catch (Exception ex) {
            ResultUtils.writeMessage(response, 0, "删除失败");
            throw new RuntimeException();
        }
    }

    //获取年度
    @RequestMapping(value = "getYear")
    public void getYear(HttpServletRequest request, HttpServletResponse response) {
        List list = new ArrayList();
        SimpleDateFormat sd =   new SimpleDateFormat("yyyy");
        Date date = new Date();
        int start = 2000;
        int end = Integer.parseInt(sd.format(date));
        for (int i = end;i >= start; i-- ) {
            PageData pageData = new PageData();
            pageData.put("yearCode", i);
            pageData.put("yearText", i);
            list.add(pageData);
        }
        ResultUtils.write(response,toDateJson(list));
    }

    //导出待发文人员列表数据
    @RequestMapping("/exportExcelReviewResult.do")
    public String exportExcelReviewResult(HttpServletRequest request, HttpServletResponse response, Model model) {
        logger.info("导出待发文人员列表数据");
        PageData pd = new PageData();
        pd.put("rows", request.getParameter("rows") == null? 10: Integer.parseInt(request.getParameter("rows")));//每页显示行数
        pd.put("page", request.getParameter("page") == null ? 1 : (Integer.parseInt(request.getParameter("page"))));//每页记录开始位置
        pd.put("reviewSeries", request.getParameter("reviewSeries"));
        pd.put("judgingId", request.getParameter("judgingCode"));
        pd.put("yearNo", request.getParameter("yearNo"));
        pd.put("userName", request.getParameter("userName"));
        pd.put("professialCode", request.getParameter("professialCode"));
        pd.put("areaCode", request.getParameter("areaCode"));
        List<rec_result> blist = recFileManagerService.getList(pd);
        String filename = "待发文人员信息列表"+String.valueOf(new Date().getTime());
        HSSFWorkbook wb = new HSSFWorkbook();//创建excel工作簿
        HSSFSheet sheet = wb.createSheet("Sheet");//在Excel工作簿中建一工作表，其名为缺省值, 也可以指定Sheet名称，例如Sheet
        HSSFCellStyle style = wb.createCellStyle();//生成一个样式
        HSSFFont font = wb.createFont(); // 生成一个字体
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平居中
        font.setFontHeightInPoints((short) 10);
        font.setFontName("宋体");//设置字体
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        HSSFFont font2 = wb.createFont(); // 生成一个字体
        font2.setFontHeightInPoints((short) 10);
        font2.setFontName("宋体");//设置字体
        style.setFont(font);// 把字体样式 应用到当前样式
        HSSFRow row = sheet.createRow(0);
        CellRangeAddress cra = new CellRangeAddress(0, 0, 0, 6);//合并
        row = sheet.createRow(0);//设置表头起始行
        mycreateCell(row,0,"年度",style);
        mycreateCell(row, 1, "地市", style);
        mycreateCell(row, 2, "主管单位名称", style);
        mycreateCell(row, 3, "姓名", style);
        mycreateCell(row, 4, "性别", style);
        mycreateCell(row, 5, "身份证号", style);
        mycreateCell(row, 6, "评委会名称", style);
        mycreateCell(row, 7, "申报系列", style);
        mycreateCell(row, 8, "申报级别", style);
        mycreateCell(row, 9, "申报职称", style);
        mycreateCell(row, 10, "申报专业", style);
        mycreateCell(row, 11, "专业组", style);
        mycreateCell(row, 12, "取得资格时间", style);
        mycreateCell(row, 13, "取得资格方式", style);
        mycreateCell(row, 14, "评审类型", style);
        mycreateCell(row, 15, "备注", style);
        try {
            if (null != blist && blist.size()!= 0 ) {
                style.setFont(font2);
                for (int i = 0; i < blist.size(); i++) {
                    int j = i + 1;//设置数据起始行
                    row = sheet.createRow(j);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    //设值
                    mycreateCell(row,0,String.valueOf(blist.get(i).getYearNo()),style);
                    String areaCode = blist.get(i).getAreaCode();
                    String grade = blist.get(i).getBack2();
                    if ("河南省".equals(areaCode)) {
                        mycreateCell(row,1,String.valueOf("省直"),style);
                    } else if ("3".equals(grade)) {
                        mycreateCell(row,1,String.valueOf(blist.get(i).getBack3()),style);
                    }else {
                        mycreateCell(row,1,String.valueOf(areaCode),style);
                    }
                    mycreateCell(row,2,String.valueOf(blist.get(i).getUnitCode()),style);
                    mycreateCell(row,3,String.valueOf(blist.get(i).getUserName()),style);
                    switch (blist.get(i).getSex()) {
                        case "1":mycreateCell(row,4,String.valueOf("男"),style); break;
                        case "2":mycreateCell(row,4,String.valueOf("女"),style); break;
                        default:mycreateCell(row,4,String.valueOf("未说明性别"),style); break;
                    }
                    mycreateCell(row,5,String.valueOf(blist.get(i).getIdCardNo()),style);
                    mycreateCell(row,6,String.valueOf(blist.get(i).getJudgingCode()),style);
                    mycreateCell(row,7,String.valueOf(blist.get(i).getReviewSeries()),style);
                    mycreateCell(row,8,String.valueOf(blist.get(i).getTitleLevel()),style);
                    mycreateCell(row,9,String.valueOf(blist.get(i).getPositionalTitles()),style);
                    mycreateCell(row,10,String.valueOf(blist.get(i).getProfessialCode()),style);
                    mycreateCell(row,11,String.valueOf(blist.get(i).getGroupId()),style);
                    if (null == blist.get(i).getGettime()) {
                        mycreateCell(row,12,String.valueOf(blist.get(i).getGettime()),style);
                    }else {
                        String data1 = sdf.format(blist.get(i).getGettime());
                        mycreateCell(row,12,String.valueOf(data1),style);
                    }
                    //mycreateCell(row,12,String.valueOf(blist.get(i).getGettime()),style);
                    mycreateCell(row,13,String.valueOf(blist.get(i).getGetway()),style);
                    mycreateCell(row,14,String.valueOf(blist.get(i).getReviewType()),style);
                    mycreateCell(row,15,String.valueOf(blist.get(i).getBack1()),style);
                    for (int k = 0;k <= 15;k++) {
                        sheet.autoSizeColumn((short) k); //调整列宽度，自适应
                    }
                }
            }
            response.setContentType("text/html;charset=UTF-8");
            response.reset();// 清空输出流
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment;filename=\""
                    + new String((filename + ".xlsx").getBytes("GBK"),
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

    //导出任职资格文件列表数据
    @RequestMapping("/exportFileManager.do")
    public String exportFileManager(HttpServletRequest request, HttpServletResponse response, Model model) {
        logger.info("导出任职资格文件列表数据");
        PageData pd = new PageData();
        pd.put("rows", request.getParameter("rows") == null? 10: Integer.parseInt(request.getParameter("rows")));//每页显示行数
        pd.put("page", request.getParameter("page") == null ? 1 : (Integer.parseInt(request.getParameter("page"))));//每页记录开始位置
        pd.put("yearNo", request.getParameter("yearNo"));
        pd.put("fileTitle", request.getParameter("fileTitle"));
        List<rec_fileManager> blist = recFileManagerService.getFileList(pd);
        String filename = "任职资格文件信息列表"+String.valueOf(new Date().getTime());
        HSSFWorkbook wb = new HSSFWorkbook();//创建excel工作簿
        HSSFSheet sheet = wb.createSheet("Sheet");//在Excel工作簿中建一工作表，其名为缺省值, 也可以指定Sheet名称，例如Sheet
        HSSFCellStyle style = wb.createCellStyle();//生成一个样式
        HSSFFont font = wb.createFont(); // 生成一个字体
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平居中
        font.setFontHeightInPoints((short) 10);
        font.setFontName("宋体");//设置字体
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        HSSFFont font2 = wb.createFont(); // 生成一个字体
        font2.setFontHeightInPoints((short) 10);
        font2.setFontName("宋体");//设置字体
        style.setFont(font);// 把字体样式 应用到当前样式
        HSSFRow row = sheet.createRow(0);
        CellRangeAddress cra = new CellRangeAddress(0, 0, 0, 6);//合并
        row = sheet.createRow(0);//设置表头起始行
        mycreateCell(row,0,"年度",style);
        mycreateCell(row, 1, "文件编号", style);
        mycreateCell(row, 2, "文件标题", style);
        mycreateCell(row, 3, "发文时间", style);
        mycreateCell(row, 4, "文件路径", style);
        mycreateCell(row, 5, "备注", style);
        try {
            if (null != blist && blist.size()!= 0 ) {
                style.setFont(font2);
                for (int i = 0; i < blist.size(); i++) {
                    int j = i + 1;//设置数据起始行
                    row = sheet.createRow(j);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    //设值
                    mycreateCell(row,0,String.valueOf(blist.get(i).getYearNo()),style);
                    mycreateCell(row,1,String.valueOf(blist.get(i).getFileCode()),style);
                    mycreateCell(row,2,String.valueOf(blist.get(i).getFileTitle()),style);
                    if (null == blist.get(i).getAddtime()) {
                        mycreateCell(row,3,String.valueOf(blist.get(i).getAddtime()),style);
                    }else {
                        String data1 = sdf.format(blist.get(i).getAddtime());
                        mycreateCell(row,3,String.valueOf(data1),style);
                    }
                    //mycreateCell(row,3,String.valueOf(blist.get(i).getAddtime()),style);
                    mycreateCell(row,4,String.valueOf(blist.get(i).getFilePath()),style);
                    mycreateCell(row,5,String.valueOf(blist.get(i).getBack1()),style);
                    for (int k = 0;k <= 5;k++) {
                        sheet.autoSizeColumn((short) k); //调整列宽度，自适应
                    }
                }
            }
            response.setContentType("text/html;charset=UTF-8");
            response.reset();// 清空输出流
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment;filename=\""
                    + new String((filename + ".xlsx").getBytes("GBK"),
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

}
