package com.hz.demo.controller;

import com.alibaba.fastjson.JSONArray;
import com.hz.demo.core.*;
import com.hz.demo.model.Article;
import com.hz.demo.services.ReprotService;
import com.hz.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.hz.demo.core.ResultUtils.toJson;

/***
 * 报表
 */
@Controller
@RequestMapping("/Reprot")
public class ReprotController extends BaseController {
    @Autowired
    UserService userService;
    @Autowired
    ReprotService reprotService;

    /**
     * 评审情况统计表页面
     */
    @RequestMapping(value = "assessmentStatisticsPage")
    public String DeptPage(HttpServletRequest request, HttpServletResponse response, Model model) {
        request.setAttribute("MenuBtns",userService.GetMenuBtns(request.getParameter("menuid"),getUser().getUserId()));
        return "sys/reprot/assessmentStatistics";
    }


    /**
     * 评审情况统计表
     */
    @RequestMapping(value = "assessmentStatisticsList")
    public void assessmentStatisticsList(HttpServletRequest request, HttpServletResponse response) {
        PageData pd =this.getPageData();
       /* String YEAR_NO = request.getParameter("YEAR_NO");
        String JUDGING_CODE = request.getParameter("JUDGING_CODE");
        pd.put("YEAR_NO",YEAR_NO);
        pd.put("JUDGING_CODE",JUDGING_CODE);*/
        Page page=new Page();
        //学科组通过率
        List<PageData> item=reprotService.assessmentStatisticsList(pd);
        reprotService.inite(item,pd.getString("YEAR_NO"));
        page.setData(item);
       ResultUtils.write(response, toJson(page));
    }

    /**
     * 申报信息标准 统计
     */
    @RequestMapping(value = "declarationInformationPage")
    public String declarationInformationPage(HttpServletRequest request, HttpServletResponse response, Model model) {
        request.setAttribute("MenuBtns",userService.GetMenuBtns(request.getParameter("menuid"),getUser().getUserId()));
        return "sys/reprot/declarationStandard";
    }


    /**
     * 评审情况统计表
     */
    @RequestMapping(value = "declarationInformationList")
    public void declarationInformationList(HttpServletRequest request, HttpServletResponse response) {
        JSONArray jsonArray=new JSONArray();
        try{
            PageData pd = this.getPageData();
            pd.put("UNIT_CODE",getUser().getUnitCode());
            jsonArray=reprotService.declarationInformationList(pd);
        }catch (Exception e){
            e.printStackTrace();
        }
        ResultUtils.write(response, jsonArray);
    }

    /**
     * 评审情况标准 统计
     */
    @RequestMapping(value = "assessmentInformationPage")
    public String assessmentInformationPage(HttpServletRequest request, HttpServletResponse response, Model model) {
        request.setAttribute("MenuBtns",userService.GetMenuBtns(request.getParameter("menuid"),getUser().getUserId()));
        return "sys/reprot/assessmentInformation";
    }


    /**
     * 评审情况标准统计表
     */
    @RequestMapping(value = "assessmentInformationList")
    public void assessmentInformationList(HttpServletResponse response,Page page) {
        try{
            PageData pd = this.getPageData();

            page.setPd(pd);
            List<PageData> item=reprotService.assessmentInformationlistPage(page);
            page.setData(item);
        }catch (Exception e){
            e.printStackTrace();
        }
        ResultUtils.write(response, page);
    }



}
