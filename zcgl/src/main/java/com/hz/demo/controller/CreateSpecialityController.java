package com.hz.demo.controller;

import com.hz.demo.core.PageData;
import com.hz.demo.core.ResultUtils;
import com.hz.demo.core.TableReturn;
import com.hz.demo.model.base_proposer;
import com.hz.demo.services.CreateSpecialityAllMsgServices;
import com.hz.demo.services.ProposerService;
import com.hz.demo.services.UserService;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.hz.demo.core.EncodeUtil.encodeMD5;
import static com.hz.demo.core.ResultUtils.toJson;

@Controller
@RequestMapping("/CreateSpeciality")
public class CreateSpecialityController extends BaseController{

    @Autowired
    UserService userService;
    @Autowired
    ProposerService proposerService;
    @Autowired
    CreateSpecialityAllMsgServices createSpecialityAllMsgServices;

    //跳到推荐专家页
    @RequestMapping(value = "CreateSpecialityPage.do")
    public String CreateSpecialityPage(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("MenuBtns",userService.GetMenuBtns(request.getParameter("menuid"),getUser().getUserId()));
        return "sys/createSpeciality";
    }

    //跳到推荐专家审查页
    @RequestMapping(value = "toJudgingSpecialityMsg.do")
    public String toSpecialityMsgList(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("MenuBtns",userService.GetMenuBtns(request.getParameter("menuid"),getUser().getUserId()));
        return "sys/judgingSpecialityMsg";
    }

    //获取界面列表
    @RequestMapping(value = "GetList.do")
    public void getList(HttpServletRequest request, HttpServletResponse response) {
        logger.info("显示推荐专家列表数据");
        PageData pd = new PageData();
        pd.put("rows", request.getParameter("rows") == null? 10: Integer.parseInt(request.getParameter("rows")));//每页显示行数
        pd.put("page", request.getParameter("page") == null ? 0 : (Integer.parseInt(request.getParameter("page"))));//每页记录开始位置
        pd.put("declareUnitcode",getUser().getUnitCode() );//当前登录人的单位编码
        pd.put("backup2","1" );
        pd.put("displayName", request.getParameter("displayName") == null ? "" : request.getParameter("displayName"));//申报人显示名称
        TableReturn tablereturn = new TableReturn();
        List<base_proposer> list = proposerService.getList(pd);
        Integer listCount = proposerService.getListCount(pd);
        tablereturn.setRows(list);
        tablereturn.setTotal(listCount);

        ResultUtils.write(response, toJson(tablereturn));
    }

    //更改申报人状态
    @RequestMapping(value = "UpdateState")
    public void updateState(HttpServletRequest request, HttpServletResponse response) {
        logger.info("更改申报专家状态");
        PageData pageData  = new PageData();
        String id = request.getParameter("id") == null ? "" : request.getParameter("id");
        pageData.put("id", new BigDecimal(id));//id
        String state = request.getParameter("state") == null ? "0" : request.getParameter("state");
        Integer sta = Integer.parseInt(state);
        if (sta == 0) {
            sta = 1;
        } else {
            sta = 0;
        }
        pageData.put("state", sta);//状态
        Map<String, String> map = new HashMap<>();
        map.put("isOk", "0");
        if (proposerService.update(pageData) == 1)
            map.put("isOk", "1");

        WriteLog("更改id为" + request.getParameter("id") + "的申报人状态");
        ResultUtils.write(response, toJson(map));
    }

    //添加或更新（获取）
    @RequestMapping(value = "AddOrUpdate")
    public void AddOrUpdate(HttpServletRequest request, HttpServletResponse response) {
        logger.info("申报人添加或更新（获取）");
        String id = request.getParameter("proposerId") == null ? "" : request.getParameter("proposerId");
        if (!"".equals(id)) {
            //更新（获取）
            base_proposer model = proposerService.getModel(new BigDecimal(id));
            if (model != null) {
                ResultUtils.write(response, toJson(model));
            } else {
                ResultUtils.write(response, "记录信息不存在！");
            }
        }else {
            ResultUtils.write(response, "记录信息不存在！");
        }

    }

    //保存
    @RequestMapping(value = "Save")
    public void Save(HttpServletRequest request, HttpServletResponse response) {
        logger.info("保存");
        String id = request.getParameter("id") == null ? "" : request.getParameter("id");
        //String declareUnitcode = request.getParameter("declareUnitcode") == null ? (getUser().getUnitCode()) : request.getParameter("declareUnitcode");
        base_proposer model = null;
        if ("".equals(id)) { //添加
            model = new base_proposer();
            model.setDeclareUnitcode(getUser().getUnitCode());
            model.setBasicUnitcode(getUser().getUnitCode());
            model.setAddTime(new Date());
            model.setAddUserId(getUser().getUserId());
            model.setDisplayName(request.getParameter("displayName"));
            model.setLoginName(request.getParameter("mobilephone"));
            model.setState(Integer.valueOf(request.getParameter("state") == null ? "1" : request.getParameter("state")));
            model.setLoginPassword(encodeMD5("000000"));
            model.setMobilephone(request.getParameter("mobilephone"));
            model.setEmail(request.getParameter("email"));
            model.setIdCardNo(request.getParameter("idCardNo"));
            model.setBackup1(request.getParameter("backup1"));
            model.setBackup2("1");
            if (proposerService.add(model) == 1)
                ResultUtils.writeMessage(response, 1, "添加成功");
            else
                ResultUtils.writeMessage(response, 0, "添加失败");
        } else {//修改
            PageData pd = new PageData();
            pd.put("id", new BigDecimal(id));
            pd.put("basicUnitcode", request.getParameter("basicUnitcode"));
            pd.put("displayName", request.getParameter("displayName"));
            pd.put("loginName", request.getParameter("mobilephone"));
            pd.put("loginPassword", request.getParameter("loginPassword"));
            pd.put("mobilephone", request.getParameter("mobilephone"));
            pd.put("email", request.getParameter("email"));
            pd.put("idCardNo", request.getParameter("idCardNo"));
            pd.put("backup1", request.getParameter("backup1"));
            pd.put("backup2", request.getParameter("backup2"));
            if (proposerService.update(pd) == 1)
                ResultUtils.writeMessage(response, 1, "修改成功");
            else
                ResultUtils.writeMessage(response, 0, "修改失败");
        }

    }

    //删除
    @RequestMapping(value = "Delete")
    public void Delete(HttpServletRequest request, HttpServletResponse response) {
        String ids = request.getParameter("ids") == null ? "" : request.getParameter("ids");
        String[] strs = ids.split(",");
        try {
            for (String id:strs) {
                BigDecimal bigDecimal = new BigDecimal(id);
                if (proposerService.delete(bigDecimal) == 1)
                    ResultUtils.writeMessage(response, 1, "删除成功");
                else
                    ResultUtils.writeMessage(response, 0, "删除失败");
            }
        } catch (Exception ex) {
            ResultUtils.writeMessage(response, 0, ex.getMessage());
        }
    }

    //手机号及身份证号码验证
    @RequestMapping(value = "checkCode")
    public void checkCode(HttpServletRequest request, HttpServletResponse response) {
        logger.info("手机号及身份证号码验证");
        PageData pageData = new PageData();
        pageData.put("mobilephone",request.getParameter("mobilephone"));
        pageData.put("idCardNo",request.getParameter("idCardNo"));
        String id = request.getParameter("id");
        pageData.put("id",id);
        List<base_proposer> blist = proposerService.getProposerWhere(pageData);
        if (blist != null && blist.size() != 0) {
            ResultUtils.write(response, false);
        } else {
            ResultUtils.write(response, true);
        }
    }

    //获取界面列表
    @RequestMapping(value = "GetJudgingSpecialityMsgList.do")
    public void GetJudgingProposerList(HttpServletRequest request, HttpServletResponse response) {
        logger.info("显示需审批的申报人列表数据");
        PageData pd = new PageData();
        pd.put("rows", request.getParameter("rows") == null? 10: Integer.parseInt(request.getParameter("rows")));//每页显示行数
        pd.put("page", request.getParameter("page") == null ? 0 : (Integer.parseInt(request.getParameter("page"))));//每页记录开始位置
        pd.put("declareUnitcode",getUser().getUnitCode());//当前登录人的单位编码
        //申报人姓名
        pd.put("displayName", request.getParameter("displayName2") == null ? "" : request.getParameter("displayName2"));//申报人显示名称
        //管理机构
        pd.put("gljg", request.getParameter("gljg") == null ? "" : request.getParameter("gljg"));
        pd.put("nian", request.getParameter("nian") == null ? "" : request.getParameter("nian"));
        pd.put("xilie", request.getParameter("xl") == null ? "" : request.getParameter("xl"));
        pd.put("sczt", request.getParameter("sczt") == null ? "" : request.getParameter("sczt"));
        pd.put("dqzt", request.getParameter("dqzt") == null ? "" : request.getParameter("dqzt"));
        pd.put("judgingStage", request.getParameter("judgingStage") == null ? "" : request.getParameter("judgingStage"));//申报人申报阶段
        TableReturn tablereturn = new TableReturn();
        List<Map<String,String>> list = proposerService.getJudgingSpecialityMsgList(pd);
        Integer listCount = proposerService.getJudgingSpecialityMsgListCount(pd);
        tablereturn.setRows(list);
        tablereturn.setTotal(listCount);
        ResultUtils.write(response, toJson(tablereturn));
    }
    /***导出需审批的专家列表数据***/
    @RequestMapping("/exportExcelSpecialityMsg.do")
    public String exportExcelSpecialityMsg(HttpServletRequest request, HttpServletResponse response, Model model) {
        logger.info("导出需审批的申报人列表数据");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sss = new SimpleDateFormat("yyyy-MM-dd ");
        String filename = sss.format(new Date().getTime());

        int page = request.getParameter("page") == null ? 1 : Integer.parseInt(request.getParameter("page"));
        int rows = request.getParameter("rows") == null ? 10000000 : Integer.parseInt(request.getParameter("rows"));

        PageData pd = new PageData();
        pd.put("rows", rows);//每页显示行数
        pd.put("page", page);//每页记录开始位置

        pd.put("declareUnitcode",getUser().getUnitCode());//当前登录人的单位编码
        //申报人姓名
        pd.put("displayName", request.getParameter("displayName2") == null ? "" : request.getParameter("displayName2"));//申报人显示名称
        //管理机构
        pd.put("gljg", request.getParameter("gljg") == null ? "" : request.getParameter("gljg"));
        pd.put("nian", request.getParameter("nian") == null ? "" : request.getParameter("nian"));
        pd.put("xilie", request.getParameter("xl") == null ? "" : request.getParameter("xl"));
        pd.put("sczt", request.getParameter("sczt") == null ? "" : request.getParameter("sczt"));
        pd.put("dqzt", request.getParameter("dqzt") == null ? "" : request.getParameter("dqzt"));
        pd.put("judgingStage", request.getParameter("judgingStage") == null ? "" : request.getParameter("judgingStage"));//申报人申报阶段
        List<Map<String,String>> list = proposerService.getJudgingSpecialityMsgList(pd);
        HSSFWorkbook wb = new HSSFWorkbook();//创建excel工作簿
        HSSFSheet sheet = wb.createSheet("Sheet");//在Excel工作簿中建一工作表，其名为缺省值, 也可以指定Sheet名称，例如Sheet
        HSSFCellStyle style = wb.createCellStyle();//生成一个样式
        HSSFFont font = wb.createFont(); // 生成一个字体
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平居中
        font.setFontHeightInPoints((short) 14);
        font.setFontName("宋体");//设置字体
        style.setFont(font);// 把字体样式 应用到当前样式
        HSSFRow row = sheet.createRow(0);
        CellRangeAddress cra = new CellRangeAddress(0, 0, 0, 6);//合并
        row = sheet.createRow(0);//设置表头起始行
        mycreateCell(row,0,"序号",style);
        mycreateCell(row, 1, "姓名", style);
        mycreateCell(row, 2, "推荐单位", style);
        mycreateCell(row, 3, "单位", style);
        mycreateCell(row, 4, "推荐系列", style);
        mycreateCell(row, 5, "推荐专业", style);
        mycreateCell(row, 6, "当前状态", style);

        for (int i = 0; i < list.size(); i++) {
            int j = i + 1;//设置数据起始行
            row = sheet.createRow(j);
            //设值
            mycreateCell(row,0,i+1,style);
            mycreateCell(row,1, String.valueOf(list.get(i).get("DISPLAY_NAME")), style);
            mycreateCell(row, 2, String.valueOf(list.get(i).get("UNIT_NAME")), style);
            mycreateCell(row, 3, String.valueOf(list.get(i).get("UNIT_NAME")), style);
            mycreateCell(row, 4, String.valueOf(list.get(i).get("XILIE")), style);
            mycreateCell(row, 5, String.valueOf(list.get(i).get("PROFESSIAL_NAME")), style);
            mycreateCell(row, 6, String.valueOf(list.get(i).get("CURRENT_STATE")), style);
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

    //跳到申报人状态查询界面
    @RequestMapping(value = "CreateSpecialityType")
    public String ProposerTypePage(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("MenuBtns",userService.GetMenuBtns(request.getParameter("menuid"),getUser().getUserId()));
        return "sys/createSpecialitytype";
    }
    //专家状态查询
    @RequestMapping(value = "selectCreateSpecialityTypeList.do")
    public void selectCreateSpecialityTypeList(HttpServletRequest request, HttpServletResponse response) {
        logger.info("显示专家状态列表数据");
        PageData pd = new PageData();
        pd.put("UNIT_CODE",this.getUser().getUnitCode());
        pd.put("nian",request.getParameter("nian")==null?"":request.getParameter("nian"));
        pd.put("rows", request.getParameter("rows") == null? 10: Integer.parseInt(request.getParameter("rows")));//每页显示行数
        pd.put("page", request.getParameter("page") == null ? 0 : (Integer.parseInt(request.getParameter("page"))));//每页记录开始位置
        //pd.put("basicUnitcode",request.getParameter("basicUnitcode")==null?"":request.getParameter("basicUnitcode"));
        pd.put("dqzt",request.getParameter("dqzt")==null?"":request.getParameter("dqzt"));
        pd.put("DISPLAY_NAME", request.getParameter("displayName1") == null ? "" : request.getParameter("displayName1"));
        pd.put("backup2","1");
        TableReturn tablereturn = new TableReturn();
        List<Map<String,Object>> list = proposerService.selectCreateSpecialityTypeList(pd);
        for(Map<String,Object> pageData :list){
            PageData pda=new PageData();
            pda.put("UNIT_CODE",getUser().getUnitCode());
            pda.put("JUDGING_CODE",pageData.get("JUDGING_CODE"));
            pda.put("JUDGING_STAGE",pageData.get("JUDGING_STAGE").equals("13"));
            if("13".equals(pageData.get("JUDGING_STAGE"))){
                pageData.put("UNIT_NAME3","河南省职称办");
            } else if("5".equals(pageData.get("JUDGING_STAGE"))){
                pda.putAll(pageData);
                pageData.put("UNIT_NAME3",pageData.get("UNIT_NAME"));
            }else {
                pageData.put("UNIT_NAME3","");
            }

        }
        Integer listCount = proposerService.selectCreateSpecialityTypeCount(pd);
        tablereturn.setRows(list);
        tablereturn.setTotal(listCount);

        ResultUtils.write(response, toJson(tablereturn));
    }
//获取待审机构
    public PageData selectCode(PageData pageData){
        PageData pd =new PageData();

        pd.put("PARENTUNIT_CODE",pageData.getString("UNIT_CODE"));
        List<PageData> item=new ArrayList<PageData>();
        if(pageData.getString("JUDGING_STAGE").equals("1")||pageData.getString("JUDGING_STAGE").equals("2")){
            pd.put("state",1);
            item=proposerService.selectUnitCode(pd);
        }else if(pageData.getString("JUDGING_STAGE").equals("3")||pageData.getString("JUDGING_STAGE").equals("4")){
            pd.put("state",3);
            item=proposerService.selectUnitCode(pd);
        }else if(pageData.getString("JUDGING_STAGE").equals("5")||pageData.getString("JUDGING_STAGE").equals("6")){
            pd.put("state",5);
            item=proposerService.selectUnitCode(pd);
            //pd.put("UNIT_NAME3",pageData.getString("UNIT_NAME"));
        }else if(pageData.getString("JUDGING_STAGE").equals("7")||pageData.getString("JUDGING_STAGE").equals("8")){
            pd.put("state",7);
            item=proposerService.selectUnitCode(pd);
        }else if(pageData.getString("JUDGING_STAGE").equals("9")||pageData.getString("JUDGING_STAGE").equals("10")){
            pd.put("UNIT_ATTACH",40);
            item=proposerService.selectUnitCode(pd);
        }else if(pageData.getString("JUDGING_STAGE").equals("11")||pageData.getString("JUDGING_STAGE").equals("12")){
            pd.put("UNIT_ATTACH",20);
             item=proposerService.selectUnitCode(pd);
        }else if(pageData.getString("JUDGING_STAGE").equals("13")||pageData.getString("JUDGING_STAGE").equals("14")){
            pd.put("UNIT_ATTACH",10);
             item=proposerService.selectUnitCode(pd);
        }else if(pageData.getString("JUDGING_STAGE").equals("15")){
            pd.put("JUDGING_CODE",pageData.getString("JUDGING_CODE"));
            item=proposerService.selectUnitByJudgingCode(pd);
        }
        if(item.size()==1){
            pd.put("UNIT_NAME3",item.get(0).getString("UNIT_NAME"));
            return pd;
        }
        return pd;
    }
    /***导出推荐专家信息（本单位审查）***/
    @RequestMapping("/exportExcelSpecialityMsgType.do")
    public String exportExcelSpecialityMsgType(HttpServletRequest request, HttpServletResponse response, Model model) {
        logger.info("导出申报人员状态信息");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sss = new SimpleDateFormat("yyyy-MM-dd ");
        String filename = sss.format(new Date().getTime());
        PageData pd = new PageData();
        pd.put("UNIT_CODE",this.getUser().getUnitCode());
        pd.put("nian",request.getParameter("nian")==null?"":request.getParameter("nian"));
        pd.put("rows", request.getParameter("rows") == null? 10: Integer.parseInt(request.getParameter("rows")));//每页显示行数
        pd.put("page", request.getParameter("page") == null ? 0 : (Integer.parseInt(request.getParameter("page"))));//每页记录开始位置
        //pd.put("basicUnitcode",request.getParameter("basicUnitcode")==null?"":request.getParameter("basicUnitcode"));
        pd.put("dqzt",request.getParameter("dqzt")==null?"":request.getParameter("dqzt"));
        pd.put("DISPLAY_NAME", request.getParameter("displayName1") == null ? "" : request.getParameter("displayName1"));
        pd.put("backup2","1");
        TableReturn tablereturn = new TableReturn();
        List<Map<String,Object>> list = proposerService.selectCreateSpecialityTypeList(pd);

        for(Map map :list){
            PageData ppd=new PageData();
            ppd.putAll(map);
            if("13".equals(ppd.get("JUDGING_STAGE"))){
                ppd.put("UNIT_NAME3","河南省职称办");
            } else if("5".equals(ppd.get("JUDGING_STAGE"))){
                ppd.put("UNIT_NAME3",ppd.get("UNIT_NAME"));
            }else {
                ppd.put("UNIT_NAME3","");
            }

            map.put("UNIT_NAME3",ppd.getString("UNIT_NAME3"));
        }
        HSSFWorkbook wb = new HSSFWorkbook();//创建excel工作簿
        HSSFSheet sheet = wb.createSheet("Sheet");//在Excel工作簿中建一工作表，其名为缺省值, 也可以指定Sheet名称，例如Sheet
        HSSFCellStyle style = wb.createCellStyle();//生成一个样式
        HSSFFont font = wb.createFont(); // 生成一个字体
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平居中
        font.setFontHeightInPoints((short) 14);
        font.setFontName("宋体");//设置字体
        style.setFont(font);// 把字体样式 应用到当前样式
        HSSFRow row = sheet.createRow(0);
        CellRangeAddress cra = new CellRangeAddress(0, 0, 0, 6);//合并
        row = sheet.createRow(0);//设置表头起始行
        mycreateCell(row,0,"序号",style);
        mycreateCell(row, 1, "姓名", style);
        mycreateCell(row, 2, "身份证", style);
        mycreateCell(row, 3, "推荐单位名称", style);
        mycreateCell(row, 4, "学科专业", style);
        mycreateCell(row, 5, "待审机构", style);
        mycreateCell(row, 6, "当前状态", style);

        for (int i = 0; i < list.size(); i++) {
            int j = i + 1;//设置数据起始行
            row = sheet.createRow(j);
            //设值
            mycreateCell(row,0,i+1,style);
            mycreateCell(row,1, String.valueOf(list.get(i).get("DISPLAY_NAME")), style);
            mycreateCell(row, 2, String.valueOf(list.get(i).get("ID_CARD_NO")), style);
            mycreateCell(row, 3, String.valueOf(list.get(i).get("UNIT_NAME")), style);
            mycreateCell(row, 4, String.valueOf(list.get(i).get("PROFESSIAL_NAME")), style);
            mycreateCell(row, 5, String.valueOf(list.get(i).get("UNIT_NAME3")), style);
            System.out.print(list.get(i).get("UNIT_NAME3"));
            mycreateCell(row, 6, String.valueOf(list.get(i).get("CURRENT_STATE")), style);
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
