package com.hz.demo.controller;

import com.hz.demo.core.*;
import com.hz.demo.model.rec_result;
import com.hz.demo.model.rec_result;
import com.hz.demo.services.EngageService;
import com.hz.demo.services.RecResultService;
import com.hz.demo.services.UserService;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.naming.ldap.PagedResultsControl;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.hz.demo.core.ResultUtils.toDateTimeJson;
//评审结果管理页面用
@Controller
@RequestMapping("/RecResult")
public class RecResultController extends BaseController{

    @Autowired
    UserService userService;
    @Autowired
    RecResultService recResultService;

   //跳到评审结果界面
    @RequestMapping(value = "RecResultPage")
    public String RecResultPage(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("MenuBtns",userService.GetMenuBtns(request.getParameter("menuid"),getUser().getUserId()));
        return "sys/recResult";
    }

    //获取界面列表
    @RequestMapping(value = "getList")
    public void getList(HttpServletRequest request, HttpServletResponse response) {
        logger.info("显示评审结果列表数据");
        PageData pd = new PageData();
        String yearNo = request.getParameter("yearNo");
        pd.put("rows", request.getParameter("rows") == null? 10: Integer.parseInt(request.getParameter("rows")));//每页显示行数
        pd.put("page", request.getParameter("page") == null ? 1 : (Integer.parseInt(request.getParameter("page"))));//每页记录开始位置
        pd.put("userName", request.getParameter("userName"));
        pd.put("yearNo", yearNo);
        pd.put("judgingId", request.getParameter("judgingCode"));
        pd.put("unitCode", request.getParameter("unitCode"));
        pd.put("reviewSeries", request.getParameter("reviewSeries"));
        pd.put("professialCode", request.getParameter("professialCode"));
        pd.put("areaCode", request.getParameter("areaCode"));
        pd.put("manageUnitCode", getUser().getUnitCode());
        TableReturn tablereturn = new TableReturn();
        List<rec_result> blist = recResultService.getList(pd);
        Integer listCount = recResultService.getListCount(pd);
        tablereturn.setRows(blist);
        tablereturn.setTotal(listCount);
        ResultUtils.write(response, toDateTimeJson(tablereturn));
    }

    //导出评审结果人员信息
    @RequestMapping("/exportExcelReviewResult.do")
    public String exportExcelReviewResult(HttpServletRequest request, HttpServletResponse response, Model model) {
        logger.info("导出评审结果人员列表数据");
        PageData pd = new PageData();
        pd.put("rows", request.getParameter("rows") == null? 10: Integer.parseInt(request.getParameter("rows")));//每页显示行数
        pd.put("page", request.getParameter("page") == null ? 1 : (Integer.parseInt(request.getParameter("page"))));//每页记录开始位置
        pd.put("reviewSeries", request.getParameter("reviewSeries"));
        pd.put("judgingId", request.getParameter("judgingCode"));
        pd.put("yearNo", request.getParameter("yearNo"));
        pd.put("userName", request.getParameter("userName"));
        pd.put("professialCode", request.getParameter("professialCode"));
        pd.put("areaCode", request.getParameter("areaCode"));
        List<rec_result> blist = recResultService.getList(pd);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<PageData> varList = new ArrayList<>();
        for (rec_result recResult:blist) {
            PageData pageData = new PageData();
            pageData.put("var1",null == recResult.getYearNo()?"":recResult.getYearNo());
            String areaCode = recResult.getAreaCode();
            String grade = recResult.getBack2();
            if ("河南省".equals(areaCode)) {
                pageData.put("var2","省直");
            } else if ("3".equals(grade)) {
                pageData.put("var2",recResult.getBack3());
            }else {
                pageData.put("var2",areaCode);
            }
            pageData.put("var3",null == recResult.getUnitCode()?"":recResult.getUnitCode());
            pageData.put("var4",null == recResult.getUserName()?"":recResult.getUserName());
            pageData.put("var5",null == recResult.getSex()?"":recResult.getSex());
            pageData.put("var6",null == recResult.getIdCardNo()?"":recResult.getIdCardNo());
            pageData.put("var7",null == recResult.getJudgingCode()?"":recResult.getJudgingCode());
            pageData.put("var8",null == recResult.getReviewSeries()?"":recResult.getReviewSeries());
            pageData.put("var9",null == recResult.getTitleLevel()?"":recResult.getTitleLevel());
            pageData.put("var10",null == recResult.getPositionalTitles()?"":recResult.getPositionalTitles());
            pageData.put("var11",null == recResult.getProfessialCode()?"":recResult.getProfessialCode());
            pageData.put("var12",null == recResult.getGroupId()?"":recResult.getGroupId());
            pageData.put("var13",null == recResult.getGettime()?"":sdf.format(recResult.getGettime()));
            pageData.put("var14",null == recResult.getGetway()?"":recResult.getGetway());
            pageData.put("var15",null == recResult.getReviewType()?"":recResult.getReviewType());
            pageData.put("var16",null == recResult.getBack1()?"":recResult.getBack1());
            varList.add(pageData);
        }
        Map dataMap = new HashMap();
        //String filename = "评审结果管理信息列表"+String.valueOf(new Date().getTime());
        List titles = new ArrayList();
        titles.add("年度");
        titles.add("地市");
        titles.add("主管单位名称");
        titles.add("姓名");
        titles.add("性别");
        titles.add("身份证号");
        titles.add("评委会名称");
        titles.add("申报系列");
        titles.add("申报级别");
        titles.add("申报职称");
        titles.add("申报专业");
        titles.add("专业组");
        titles.add("取得资格时间");
        titles.add("取得资格方式");
        titles.add("评审类型");
        titles.add("备注");
        dataMap.put("titles", titles);
        dataMap.put("varList", varList);
        HSSFWorkbook wb = new HSSFWorkbook();//创建excel工作簿
        try {
            ExportExcel exportExcel = new ExportExcel();
            exportExcel.Excel(dataMap,wb,request,response);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("exportExcelLog e=" + e.getMessage());
            //ResultUtils.writeFailed(response);
        }
        return null;
    }
}
