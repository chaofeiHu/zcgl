package com.hz.demo.controller;

import com.hz.demo.core.*;

import com.hz.demo.model.rec_examresult;
import com.hz.demo.model.rec_fileManager;
import com.hz.demo.model.sys_dict;
import com.hz.demo.services.DictService;
import com.hz.demo.services.RecExamResultService;
import net.sf.json.JsonConfig;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.hz.demo.core.ResultUtils.toDateJson;
import static com.hz.demo.core.ResultUtils.toJson;

@Controller
@RequestMapping("/RecExamResult")
public class RecExamResultController extends BaseController{
    @Autowired
    RecExamResultService recExamResultService;
    @Autowired
    DictService dictService;
    @RequestMapping(value = "RecExamResultPage")
    public String RecExamResultPage(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("MenuBtns",userService.GetMenuBtns(request.getParameter("menuid"),getUser().getUserId()));
        return "sys/recExamResult";
    }
    @RequestMapping(value = "RecProExamManagePage")
    public String JudgingPage(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("MenuBtns",userService.GetMenuBtns(request.getParameter("menuid"),getUser().getUserId()));
        return "sys/recProExamManage";
    }
    @RequestMapping("LeadInUser.do")
    public String leadInExcel(HttpServletRequest request, HttpServletResponse response) throws Exception{
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        InputStream in =null;
        List<List<Object>> listob = null;
        TableReturn tablereturn = new TableReturn();
        MultipartFile file = multipartRequest.getFile("file");
        if(file.isEmpty()){
            throw new Exception("文件不存在！");
        }
        in = file.getInputStream();
        listob = new ImportExcelUtil().getBankListByExcel(in,file.getOriginalFilename());
        if(listob==null){
            ResultUtils.writeMessage(response, 0, "请检查是否按照模板格式填写并数据不能为空");
            return null;
        }
        List<Map<String, Object>> hashMaps = new ArrayList<>();
        rec_examresult rece = new rec_examresult();
         String yearNo = request.getParameter("yearNo");
        String gettime = request.getParameter("gettime");
        String examClass = request.getParameter("examClass");
        String examName = request.getParameter("examName1");

        //该处可调用service相应方法进行数据保存到数据库中，现只对数据输出
        for (int i = 0; i < listob.size(); i++) {
            Map<String, Object> map = new HashMap<>();
            List<Object> lo = listob.get(i);
            if(!String.valueOf(lo.get(0)).equals(yearNo)){
                ResultUtils.writeMessage(response, 0, "导入失败请检查模版数据第"+(i+2)+"行年度是否与考试年度相同或是否按照模版格式填写");
                return null;
            }
            if(!String.valueOf(lo.get(12)).equals(examName)){
                ResultUtils.writeMessage(response, 0, "导入失败请检查模版数据第"+(i+2)+"行考试名称是否与所选考试名称相同或是否按照模版格式填写");
                return null;
            }
            if(!String.valueOf(lo.get(8)).equals(gettime)){
                ResultUtils.writeMessage(response, 0, "导入失败请检查模版数据第"+(i+2)+"行资格取得时间是否与所选取得时间相同或是否按照模版格式填写");
                return null;
            }
            //String yearNo=String.valueOf(lo.get(0));
//            String bb =String.valueOf(lo.get(1));
            map.put("yearNo",lo.size()>0?String.valueOf(lo.get(0)):"");//年度
            map.put("areaCode",lo.size()>1?String.valueOf(lo.get(1)):"");//地市
            map.put("ticketNumber",lo.size()>2?String.valueOf(lo.get(2)):"");//准考证号
            map.put("userName",lo.size()>3?String.valueOf(lo.get(3)):"");//姓名
            map.put("sex",lo.size()>4?String.valueOf(lo.get(4)):"");//性别
            map.put("titleLevel",lo.size()>5?String.valueOf(lo.get(5)):"");//级别
            map.put("professialCode",lo.size()>6?String.valueOf(lo.get(6)):"");//专业
            map.put("idCardNo",lo.size()>7?String.valueOf(lo.get(7)):"");//证件号
            map.put("gettime",lo.size()>8?String.valueOf(lo.get(8)):"");//资格取得时间
            map.put("unitName",lo.size()>9?String.valueOf(lo.get(9)):"");//单位名称
            map.put("fileNumber",lo.size()>10?String.valueOf(lo.get(10)):"");//档案号
            map.put("birthday",lo.size()>11?String.valueOf(lo.get(11)):"");//出生日期
            map.put("examName",lo.size()>12?String.valueOf(lo.get(12)):"");//考试名称
            map.put("certificateNumber",lo.size()>13?String.valueOf(lo.get(13)):"");//证书编号
            map.put("managerNo",lo.size()>14?String.valueOf(lo.get(14)):"");//管理号
            map.put("positionalTitles",lo.size()>15?String.valueOf(lo.get(15)):"");//职称
            map.put("back1",lo.size()>16?String.valueOf(lo.get(16)):"");//备注
            hashMaps.add(map);
        }

        tablereturn.setRows(hashMaps);
        tablereturn.setTotal(listob.size());
        ResultUtils.write(response, toJson(hashMaps));
        PrintWriter out = null;
        response.setCharacterEncoding("utf-8");  //防止ajax接受到的中文信息乱码
        out = response.getWriter();
        out.print("文件导入成功！");
        out.flush();
        out.close();
        return  null;
    }
    @RequestMapping("save.do")
    public String  save(HttpServletRequest request, HttpServletResponse response){
        String list =request.getParameter("listdata");
        String yearNo1 = request.getParameter("yearNo");
        String gettime1 = request.getParameter("gettime");
        String examClass = request.getParameter("examClass");
        String examName1 = request.getParameter("examName");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        net.sf.json.JSONArray array = net.sf.json.JSONArray.fromObject(list);
        List<Map<String,String>> list2 =  net.sf.json.JSONArray.toList(array, new HashMap<String,String>(), new JsonConfig());//参数1为要转换的JSONArray数据，参数2为要转换的目标数据，即List盛装的数据
         int bb= 0;
        for(int i =0 ;i<list2.size();i++){
//          String userName =list2.get(i).get("userName");
            PageData pd = new PageData();
            pd.put("id","1");
            String userName=list2.get(i).get("userName");
            pd.put("userName",userName);
//            if(StringUtils.isEmpty(userName)){
//                ResultUtils.writeMessage(response, 0, "添加失败第"+(i+1)+"行姓名不能为空");
//                return null;
//            }else{
//
//            }
            String yearNo=list2.get(i).get("yearNo");
            pd.put("yearNo",yearNo);
            String areaCode=list2.get(i).get("areaCode");
            pd.put("areaCode",areaCode);
            String ticketNumber=list2.get(i).get("ticketNumber");
            pd.put("ticketNumber",ticketNumber);
            String sex=list2.get(i).get("sex");
            if(sex.equals("男")){
                pd.put("sex","1");
            }else{
                pd.put("sex","2");
            }

            String titleLevel=list2.get(i).get("titleLevel");
            pd.put("titleLevel",titleLevel);
            String professialCode=list2.get(i).get("professialCode");
            pd.put("professialCode",professialCode);
            String idCardNo=list2.get(i).get("idCardNo");
            pd.put("idCardNo",idCardNo);
//            rec_examresult model = recExamResultService.selectRecWhere(pd);
//            if (model != null) {
//                ResultUtils.writeMessage(response, 0, "第"+(i+1)+"行记录已存在");
//            }
//            String idCard_regex="^\\d{15}|^\\d{17}([0-9]|X|x)$";
//            if(StringUtils.isEmpty(idCardNo)){
//                ResultUtils.writeMessage(response, 0, "添加失败第"+(i+1)+"行准身份证号不能为空");
//                return null;
//            }else {
//
//            }


            try {
                pd.put("gettime",formatter.parse(list2.get(i).get("gettime")));
                pd.put("birthday",formatter.parse(list2.get(i).get("birthday")));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            pd.put("unitName",list2.get(i).get("unitName"));
            pd.put("fileNumber",list2.get(i).get("fileNumber"));
            pd.put("examName",examName1);//考试名称
            pd.put("certificateNumber",list2.get(i).get("certificateNumber"));
            pd.put("managerNo",list2.get(i).get("managerNo"));
            pd.put("positionalTitles",list2.get(i).get("positionalTitles"));
            pd.put("back1",list2.get(i).get("back1"));
            pd.put("addtime",new Date());
            pd.put("adduserid",getUser().getUserId());
            pd.put("examClass",examClass);
            bb+=recExamResultService.add(pd);

//            if (recExamResultService.add(pd) == 1){
//               }
        }
        ResultUtils.writeMessage(response, 1, "成功添加"+bb+"条数据");
        return null;
    }

        @RequestMapping("downExcel.do")
        public String downExcel(HttpServletRequest request, HttpServletResponse response) {
            String filename = "职称考试信息模板";
            HSSFWorkbook wb = new HSSFWorkbook();//创建excel工作簿
            HSSFSheet sheet = wb.createSheet("Sheet");//在Excel工作簿中建一工作表，其名为缺省值, 也可以指定Sheet名称，例如Sheet
            HSSFCellStyle style = wb.createCellStyle();//生成一个样式
            sheet.setColumnWidth(0, 256*10);
            sheet.setColumnWidth(1, 256*10);
            sheet.setColumnWidth(2, 256*20);
            sheet.setColumnWidth(3, 256*10);
            sheet.setColumnWidth(6, 256*20);
            sheet.setColumnWidth(7, 256*20);
            sheet.setColumnWidth(8, 256*18);
            sheet.setColumnWidth(9, 256*20);
            sheet.setColumnWidth(10, 256*20);
            sheet.setColumnWidth(11, 256*18);
            sheet.setColumnWidth(12, 256*20);
            sheet.setColumnWidth(13, 256*20);
            sheet.setColumnWidth(14, 256*20);
            sheet.setColumnWidth(15, 256*18);
            sheet.setColumnWidth(16, 256*20);
            HSSFFont font = wb.createFont(); // 生成一个字体
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平居中
            font.setFontHeightInPoints((short) 13);
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
            mycreateCell(row, 2, "准考证号", style);
            mycreateCell(row, 3, "姓名", style);
            mycreateCell(row, 4, "性别", style);
            mycreateCell(row, 5, "级别", style);
            mycreateCell(row,6,"专业",style);
            mycreateCell(row, 7, "证件号", style);
            mycreateCell(row, 8, "资格取得时间", style);
            mycreateCell(row, 9, "单位名称", style);
            mycreateCell(row, 10, "档案号", style);
            mycreateCell(row, 11, "出生日期", style);
            mycreateCell(row,12,"考试名称",style);
            mycreateCell(row, 13, "证书编号", style);
            mycreateCell(row, 14, "管理号", style);
            mycreateCell(row, 15, "职称", style);
            mycreateCell(row, 16, "备注", style);
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

    @RequestMapping("recProDictTree.do")
    public void recProDictTree(HttpServletRequest request, HttpServletResponse response) {
        String ParentID = "6683601a-4f3b-46c0-923e-7262f217df16";
        PageData pd = new PageData();
        pd.put("parentID", ParentID);
        String state = request.getParameter("id") == null ? "" : request.getParameter("id");
//        if(state!=null&&state!=""){
//            sys_dict model = dictService.getModel(state);
//            pd.put("pid", model.getDictCode());
//        }else{
//            pd.put("pid",state);
//        }
        List<Tree> treeList = dictService.recProDictTree(pd);
        ResultUtils.write(response, ObjectList2TreeJson("ksfl", treeList, 1));

    }

    @RequestMapping("getRecExamList.do")
    public void getRecExamList(HttpServletResponse response,Page page) {
//        PageData pd = new PageData();
          PageData pd = getPageData();
//        pd.put("rows", request.getParameter("rows") == null? 10: Integer.parseInt(request.getParameter("rows")));//每页显示行数
//        pd.put("page", request.getParameter("page") == null ? 0 : (Integer.parseInt(request.getParameter("page"))));//每页记录开始位置
//        pd.put("examName", request.getParameter("examName") == null ? "" : request.getParameter("examName"));
//        pd.put("yearNo", request.getParameter("yearNo") == null ? "" : request.getParameter("yearNo"));
//        pd.put("userName", request.getParameter("userName") == null ? "" : request.getParameter("userName"));
//        TableReturn tablereturn = new TableReturn();
        page.setPd(pd);
        List<Map<String, Object>> hashMaps = new ArrayList<>();
        List<rec_examresult> list = recExamResultService.getList(page);
//        Integer listCount = recExamResultService.getListCount(pd);
        for (rec_examresult rec : list) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", rec.getId());
            map.put("yearNo",rec.getYearNo());//年度
            map.put("areaCode",rec.getAreaCode());//地市
            map.put("ticketNumber",rec.getTicketNumber());//准考证号
            map.put("userName",rec.getUserName());//姓名
            map.put("sex",getDict("SEX", String.valueOf(rec.getSex())).getDictName());//性别
            map.put("titleLevel",rec.getTitleLevel());//级别
            map.put("professialCode",rec.getProfessialCode());//专业
            map.put("idCardNo",rec.getIdCardNo());//证件号
            map.put("gettime",rec.getGettime());//资格取得时间
            map.put("unitName",rec.getUnitName());//单位名称
            map.put("fileNumber",rec.getFileNumber());//档案号
            map.put("birthday",rec.getBirthday());//出生日期
            map.put("examName",getDict("EXAM_NAME", String.valueOf(rec.getExamName())).getDictName());//考试名称
            map.put("certificateNumber",rec.getCertificateNumber());//证书编号
            map.put("managerNo",rec.getManagerNo());//管理号
            map.put("positionalTitles",rec.getPositionalTitles());//职称
            map.put("back1",rec.getBack1());//备注
            hashMaps.add(map);
        }
        page.setRows(hashMaps);
//        tablereturn.setRows(hashMaps);
//        tablereturn.setTotal(listCount);
        ResultUtils.write(response, toDateJson(page));

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
    //验证身份证是否存在
    @RequestMapping(value = "checkCode")
    public void checkCode(HttpServletRequest request, HttpServletResponse response) {
        logger.info("身份证号码验证");
        PageData pageData = new PageData();
        pageData.put("idCardNo", request.getParameter("idCardNo"));
        pageData.put("id",request.getParameter("id") == null ? "" : request.getParameter("id"));
        rec_examresult model = recExamResultService.selectRecWhere(pageData);
        if (model != null) {
            ResultUtils.write(response, false);
            }else{
            ResultUtils.write(response, true);
        }
    }

    //更改信息
    @RequestMapping(value = "Update")
    public void AddOrUpdate(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id") == null ? "" : request.getParameter("id");
        BigDecimal bid = new BigDecimal(id);
        if (id.equals("")) {
        } else {
            //更新（获取）
            rec_examresult model = recExamResultService.selectByPrimaryKey(bid);
            if (model != null) {
                ResultUtils.write(response, toDateJson(model));
            } else {
                ResultUtils.write(response, "记录信息不存在！");
            }
        }
    }



    //保存
    @RequestMapping(value = "recProSave")
    public void recProSave(HttpServletRequest request, HttpServletResponse response) {
        logger.info("保存");
        String id = request.getParameter("id") == null ? "" : request.getParameter("id");
        String examName = request.getParameter("examName") == null ? "" : request.getParameter("examName");
        if ("".equals(id)) {
            try {
                PageData pd = new PageData();
                pd.put("id","1");
                pd.put("examClass", examName.substring(0,2));
                pd.put("examName", examName);
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                pd.put("yearNo", request.getParameter("yearNo"));
                pd.put("areaCode", request.getParameter("areaCode"));
                pd.put("ticketNumber", request.getParameter("ticketNumber"));
                pd.put("userName", request.getParameter("userName"));
                pd.put("sex",request.getParameter("sex"));
                pd.put("titleLevel", request.getParameter("titleLevel"));
                pd.put("professialCode", request.getParameter("professialCode"));
                pd.put("idCardNo", request.getParameter("idCardNo"));
                pd.put("unitName", request.getParameter("unitName"));
                pd.put("fileNumber", request.getParameter("fileNumber"));
                pd.put("certificateNumber", request.getParameter("certificateNumber"));
                pd.put("managerNo", request.getParameter("managerNo"));
                pd.put("positionalTitles", request.getParameter("positionalTitles"));
                pd.put("back1", request.getParameter("back1"));
                String gettime = request.getParameter("gettime");
                pd.put("gettime", formatter.parse(gettime));
                String birthday = request.getParameter("birthday");
                pd.put("birthday", formatter.parse(birthday));
                pd.put("addtime",new Date());
                pd.put("adduserid",getUser().getUserId());
                if ( recExamResultService.add(pd) == 1) {
                    ResultUtils.writeMessage(response, 1, "添加成功");
                } else {
                    ResultUtils.writeMessage(response, 0, "添加失败");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            PageData pd = new PageData();
            BigDecimal bid = new BigDecimal(id);
            pd.put("id", bid);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            pd.put("yearNo", request.getParameter("yearNo"));
            pd.put("areaCode", request.getParameter("areaCode"));
            pd.put("ticketNumber", request.getParameter("ticketNumber"));
            pd.put("userName", request.getParameter("userName"));
            pd.put("sex", request.getParameter("sex"));
            pd.put("titleLevel", request.getParameter("titleLevel"));
            pd.put("professialCode", request.getParameter("professialCode"));
            pd.put("idCardNo", request.getParameter("idCardNo"));
            pd.put("unitName", request.getParameter("unitName"));
            pd.put("fileNumber", request.getParameter("fileNumber"));
            pd.put("certificateNumber", request.getParameter("certificateNumber"));
            pd.put("managerNo", request.getParameter("managerNo"));
            pd.put("positionalTitles", request.getParameter("positionalTitles"));
            pd.put("back1", request.getParameter("back1"));
            String gettime = request.getParameter("gettime");
            try {
                pd.put("gettime", formatter.parse(gettime));
                String birthday = request.getParameter("birthday");
                pd.put("birthday", formatter.parse(birthday));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (recExamResultService.update(pd) == 1) {
                ResultUtils.writeMessage(response, 1, "修改成功");
            } else {
                ResultUtils.writeMessage(response, 0, "修改失败");
            }
        }
    }
    //删除
    @RequestMapping(value = "delete")
    public void Delete(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id") == null ? "" : request.getParameter("id");
        BigDecimal bid = new BigDecimal(id);
        if (recExamResultService.delete(bid) == 1){
            ResultUtils.writeMessage(response, 1, "删除成功");}
        else{
            ResultUtils.writeMessage(response, 0, "删除失败");}
    }
    @RequestMapping(value = "check")
    public void check(HttpServletRequest request, HttpServletResponse response) {
        String examName = request.getParameter("examName") == null ? "" : request.getParameter("examName");
        PageData pd = new PageData();
        pd.put("dictCode",examName);
        pd.put("groupName","EXAM_NAME");
        sys_dict dict = dictService.getDictWhere(pd);
        if (dict!=null){
            ResultUtils.writeMessage(response, 1, "");}
        else{
            ResultUtils.writeMessage(response, 0, "请选择考试名称!");}
    }

 }




