package com.hz.demo.controller;

import com.hz.demo.core.ExportExcel;
import com.hz.demo.core.PageData;
import com.hz.demo.core.ResultUtils;
import com.hz.demo.core.TableReturn;
import com.hz.demo.model.rec_result;
import com.hz.demo.model.rec_resultdissent;
import com.hz.demo.model.rec_resultobjection;
import com.hz.demo.model.rec_reviewresult;
import com.hz.demo.services.EngageService;
import com.hz.demo.services.ResultDissentService;
import com.hz.demo.services.ReviewResultService;
import com.hz.demo.services.UserService;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.hz.demo.core.ResultUtils.toDateTimeJson;
//评审未通过人员管理页面用
@Controller
@RequestMapping("/ResultDissent")
public class ResultDissentController extends BaseController{

    @Autowired
    UserService userService;
    @Autowired
    ResultDissentService resultDissentService;

   //跳到未通过人员界面
    @RequestMapping(value = "ResultDissentPage")
    public String ResultDissentPage(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("MenuBtns",userService.GetMenuBtns(request.getParameter("menuid"),getUser().getUserId()));
        return "sys/reviewResultDissent";
    }

    //获取界面列表
    @RequestMapping(value = "getList")
    public void getList(HttpServletRequest request, HttpServletResponse response) {
        logger.info("显示未通过人员列表数据");
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
        List<rec_resultdissent> blist = resultDissentService.getList(pd);
        Integer listCount = resultDissentService.getListCount(pd);
        tablereturn.setRows(blist);
        tablereturn.setTotal(listCount);
        ResultUtils.write(response, toDateTimeJson(tablereturn));
    }
    //删除数据
    @RequestMapping(value = "delete")
    public void delete(HttpServletRequest request, HttpServletResponse response) {
        String ids = request.getParameter("ids") == null ? "" : request.getParameter("ids");
        String[] strs = ids.split(",");
        try {
            int flag = 0;
            for (String id:strs) {
                Integer id1 = Integer.parseInt(id);//未通过人员id
                BigDecimal bid = new BigDecimal(id1);
                flag = resultDissentService.delete(bid);
                if (flag == 0){
                    ResultUtils.writeMessage(response, 0, "删除失败"+id);
                    break;
                }
            }
            if (flag == 0){
                ResultUtils.writeMessage(response, 0, "删除失败");
            } else{
                ResultUtils.writeMessage(response, 1, "删除成功");
                throw new RuntimeException();
            }
        } catch (Exception ex) {
            ResultUtils.writeMessage(response, 0, "删除失败");
            throw new RuntimeException();
        }
    }

    //导出未通过人员信息
    @RequestMapping("/exportExcelReviewResult.do")
    public String exportExcelReviewResult(HttpServletRequest request, HttpServletResponse response, Model model) {
        logger.info("获取未通过人员列表数据");
        PageData pd = new PageData();
        pd.put("rows", request.getParameter("rows") == null? 10: Integer.parseInt(request.getParameter("rows")));//每页显示行数
        pd.put("page", request.getParameter("page") == null ? 1 : (Integer.parseInt(request.getParameter("page"))));//每页记录开始位置
        pd.put("reviewSeries", request.getParameter("reviewSeries"));
        pd.put("judgingId", request.getParameter("judgingCode"));
        pd.put("yearNo", request.getParameter("yearNo"));
        pd.put("userName", request.getParameter("userName"));
        pd.put("areaCode", request.getParameter("areaCode"));
        pd.put("professialCode", request.getParameter("professialCode"));
        List<rec_resultdissent> blist = resultDissentService.getList(pd);
        //String filename = "未通过人员信息列表"+String.valueOf(new Date().getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<PageData> varList = new ArrayList<>();
        for (rec_resultdissent resultdissent:blist) {
            PageData pageData = new PageData();
            pageData.put("var1",null == resultdissent.getYearNo()?"":resultdissent.getYearNo());
            String areaCode = resultdissent.getAreaCode();
            String grade = resultdissent.getBack2();
            if ("河南省".equals(areaCode)) {
                pageData.put("var2","省直");
            } else if ("3".equals(grade)) {
                pageData.put("var2",resultdissent.getBack3());
            }else {
                pageData.put("var2",areaCode);
            }
            pageData.put("var3",null == resultdissent.getUnitCode()?"":resultdissent.getUnitCode());
            pageData.put("var4",null == resultdissent.getUserName()?"":resultdissent.getUserName());
            pageData.put("var5",null == resultdissent.getSex()?"":resultdissent.getSex());
            pageData.put("var6",null == resultdissent.getIdCardNo()?"":resultdissent.getIdCardNo());
            pageData.put("var7",null == resultdissent.getJudgingCode()?"":resultdissent.getJudgingCode());
            pageData.put("var8",null == resultdissent.getReviewSeries()?"":resultdissent.getReviewSeries());
            pageData.put("var9",null == resultdissent.getTitleLevel()?"":resultdissent.getTitleLevel());
            pageData.put("var10",null == resultdissent.getPositionalTitles()?"":resultdissent.getPositionalTitles());
            pageData.put("var11",null == resultdissent.getProfessialCode()?"":resultdissent.getProfessialCode());
            pageData.put("var12",null == resultdissent.getGroupResultYes()?"":resultdissent.getGroupResultYes());
            pageData.put("var13",null == resultdissent.getGroupResultNo()?"":resultdissent.getGroupResultNo());
            pageData.put("var14",null == resultdissent.getGroupResultWaive()?"":resultdissent.getGroupResultWaive());
            pageData.put("var15",null == resultdissent.getGroupResultOpinion()?"":resultdissent.getGroupResultOpinion());
            if (null!=resultdissent.getGroupResult()) {
                switch (resultdissent.getGroupResult().intValue()) {
                    case 0:
                        pageData.put("var16", "未通过");
                        break;
                    case 1:
                        pageData.put("var16", "通过");
                        break;
                    default:
                        pageData.put("var16", "专业组未评审");
                        break;
                }
            }else{
                pageData.put("var16", "专业组未评审");
            }
            pageData.put("var17",null == resultdissent.getReviewResultYes()?"":resultdissent.getReviewResultYes());
            pageData.put("var18",null == resultdissent.getReviewResultNo()?"":resultdissent.getReviewResultNo());
            pageData.put("var19",null == resultdissent.getReviewResultWaive()?"":resultdissent.getReviewResultWaive());
            pageData.put("var20",null == resultdissent.getReviewResultOpinion()?"":resultdissent.getReviewResultOpinion());
            if (null!=resultdissent.getReviewResult()) {
                switch (resultdissent.getReviewResult().intValue()) {
                    case 0:
                        pageData.put("var21", "未通过");
                        break;
                    case 1:
                        pageData.put("var21", "通过");
                        break;
                    default:
                        pageData.put("var21", "大评会未评审");
                        break;
                }
            }else{
                pageData.put("var21", "大评会未评审");
            }
            pageData.put("var22",null == resultdissent.getReviewType()?"":resultdissent.getReviewType());
            pageData.put("var23",null == resultdissent.getBack1()?"":resultdissent.getBack1());
            varList.add(pageData);
        }
        Map dataMap = new HashMap();
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
        titles.add("专业组同意票数");
        titles.add("专业组反对票数");
        titles.add("专业组弃权票数");
        titles.add("专业组评议意见");
        titles.add("专业组是否通过");
        titles.add("大评会同意票数");
        titles.add("大评会反对票数");
        titles.add("大评会弃权票数");
        titles.add("大评会评议意见");
        titles.add("大评会是否通过");
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
