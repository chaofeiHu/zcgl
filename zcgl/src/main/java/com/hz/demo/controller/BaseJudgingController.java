package com.hz.demo.controller;

import com.hz.demo.core.*;
import com.hz.demo.model.*;
import com.hz.demo.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;

import static com.hz.demo.core.ResultUtils.toJson;

@Controller
@RequestMapping("/Judging")
public class BaseJudgingController extends BaseController{
    @Autowired
    UserService userService;
    @Autowired
    BaseJudgingService baseJudgingService;
    @Autowired
    BaseUnitService baseUnitService;
    @Autowired
    BaseJudingSeriesService baseJudingSeriesService;
    @Autowired
    BaseProfessialService baseProfessialService;
    @Autowired
    BaseSeriesProfessialService baseSeriesProfessialService;

    @RequestMapping(value = "JudgingPage")
    public String JudgingPage(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("MenuBtns",userService.GetMenuBtns(request.getParameter("menuid"),getUser().getUserId()));
        base_unit baseUnit = baseUnitService.getUnit(null,getUser().getUnitCode());
        request.setAttribute("manname",baseUnit.getUnitName());
        return "sys/judging";
    }
    //加载评委会列表
    @RequestMapping(value = "getJudgingList")
    public void getJudgingList(HttpServletRequest request, HttpServletResponse response) {
        logger.info("显示评委会列表数据");
        PageData pd = new PageData();
        pd.put("rows", request.getParameter("rows") == null? 10: Integer.parseInt(request.getParameter("rows")));//每页显示行数
        pd.put("page", request.getParameter("page") == null ? 0 : (Integer.parseInt(request.getParameter("page"))));//每页记录开始位置
        pd.put("judgingName", request.getParameter("judgingName") == null ? "" : request.getParameter("judgingName"));//名称
        pd.put("manageUnit",getUser().getUnitCode());
        TableReturn tablereturn = new TableReturn();
        List<Map<String, Object>> hashMaps = new ArrayList<>();
        List<base_judging> list = baseJudgingService.getList(pd);
        Integer listCount = baseJudgingService.getListCount(pd);
            for (base_judging juding : list) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", juding.getId());
                map.put("judgingCode", juding.getJudgingCode());
                map.put("judgingName", juding.getJudgingName());
                map.put("reviewJurisdiction", getDict("REVIEW_JURISDICTION", String.valueOf(juding.getReviewJurisdiction())).getDictName());
                map.put("reviewScope", getDict("REVIEW_SCOPE", String.valueOf(juding.getReviewScope())).getDictName());
                map.put("buildUnit",juding.getBuildUnit());
                map.put("manageUnit",juding.getManageUnit());
                map.put("state", juding.getState());
                map.put("linkman",juding.getLinkman());
                map.put("phone",juding.getPhone());
                map.put("juryLevel",getDict("JURY_LEVEL", String.valueOf(juding.getJuryLevel())).getDictName());
                map.put("back2",juding.getBack2());

               hashMaps.add(map);
            }

        tablereturn.setRows(hashMaps);
        tablereturn.setTotal(listCount);
        ResultUtils.write(response, toJson(tablereturn));
    }
    //添加或更新（获取）
    @RequestMapping(value = "AddOrUpdate")
    public void AddOrUpdate(HttpServletRequest request, HttpServletResponse response) {
        logger.info("添加或更新（获取）");
        String id = request.getParameter("id") == null ? "" : request.getParameter("id");
        if (id.equals("")) {
            //添加

        } else {
            //更新（获取）
            base_judging juding = baseJudgingService.getModel2(id);
            if (juding != null) {
                ResultUtils.write(response, toJson(juding));
            } else {
                ResultUtils.write(response, "记录信息不存在！");
            }
        }

    }
    //判断评委会编号是否已存在
    @RequestMapping(value = "checkCodeJuding")
    public void checkCodeJuding(HttpServletRequest request, HttpServletResponse response) {
        logger.info("判断评委会编号是否已存在");
        PageData pageData = new PageData();
        String judgingCode = request.getParameter("judgingCode");
        String id = request.getParameter("id");
        pageData.put("judgingCode",judgingCode);
        pageData.put("id",id);
        base_judging  model = baseJudgingService.getModelWhere(pageData);
        if (model != null) {
            ResultUtils.write(response, false);
        } else {
            ResultUtils.write(response, true);
        }
    }
    //判断评委会名称是否已存在
    @RequestMapping(value = "checkCodeJudingName")
    public void checkCodeJudingName(HttpServletRequest request, HttpServletResponse response) {
        logger.info("判断评委会名称是否已存在");
        PageData pageData = new PageData();
        String judgingName = request.getParameter("judgingName");
        String id = request.getParameter("id");
        pageData.put("judgingName",judgingName);
        pageData.put("id",id);
        base_judging  model = baseJudgingService.getModelWhere(pageData);
        if (model != null) {
            ResultUtils.write(response, false);
        } else {
            ResultUtils.write(response, true);
        }
    }
    //评委系列保存
    @RequestMapping(value = "Save")
    public void Save(HttpServletRequest request, HttpServletResponse response) {
        logger.info("评委系列保存");
        String id = request.getParameter("id") == null ? "" : request.getParameter("id");
        base_judging model = null;
        if (id.equals("")) { //添加评委系列
            model = new base_judging();
            model.setJudgingCode(request.getParameter("judgingCode"));
            model.setJudgingName(request.getParameter("judgingName"));
            model.setReviewJurisdiction(request.getParameter("reviewJurisdiction"));
            model.setAddtime(new Date());
            model.setAdduserid(getUser().getUserId());
            model.setReviewScope(request.getParameter("reviewScope"));
            model.setBuildUnit(request.getParameter("buildUnit"));
            model.setManageUnit(getUser().getUnitCode());
            model.setJuryLevel(request.getParameter("juryLevel"));
            model.setLinkman(request.getParameter("linkman"));
            model.setPhone(request.getParameter("phone"));
            model.setBack3("1");
            model.setState(BigDecimal.valueOf(Long.parseLong(request.getParameter("state") == null ? "0" : request.getParameter("state"))));
            model.setBack1(request.getParameter("backup1"));
                if (baseJudgingService.add(model) == 1)
                    ResultUtils.writeMessage(response, 1, "添加成功");
                else
                    ResultUtils.writeMessage(response, 0, "添加失败");
        } else {//修改评委系列
            PageData pd = new PageData();
            pd.put("id",id);
            pd.put("judgingCode", request.getParameter("judgingCode"));
            pd.put("judgingName", request.getParameter("judgingName"));
            pd.put("reviewJurisdiction", request.getParameter("reviewJurisdiction"));
            pd.put("reviewScope", request.getParameter("reviewScope"));
            String buildUnit=request.getParameter("buildUnit");
            if(!ChineseUtil.hasFullChar(buildUnit)){
                 pd.put("buildUnit", buildUnit);
             }
            pd.put("juryLevel", request.getParameter("juryLevel"));
            pd.put("linkman", request.getParameter("linkman"));
            pd.put("phone", request.getParameter("phone"));
            pd.put("state", request.getParameter("state") == null ? "0" : request.getParameter("state"));
            pd.put("backup1", request.getParameter("backup1"));
            if (baseJudgingService.update(pd) == 1)
                ResultUtils.writeMessage(response, 1, "修改成功");
            else
                ResultUtils.writeMessage(response, 0, "修改失败");
        }

    }
    //删除评委系列
    @RequestMapping(value = "Delete")
    public void Delete(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("ids") == null ? "" : request.getParameter("ids");
       Integer id1= Integer.valueOf(id);
        String back3="0";
        PageData pd = new PageData();
        pd.put("id",id1);
        pd.put("back3",back3);
        if (baseJudgingService.updateIsDel(pd) == 1)
            ResultUtils.writeMessage(response, 1, "删除成功");
        else
            ResultUtils.writeMessage(response, 0, "删除失败");

    }

    //加载评审系列列表
    @RequestMapping(value = "getJudgingSerList")
    public void getJudgingSerList(HttpServletRequest request, HttpServletResponse response) {
        logger.info("显示评审系列列表数据");
        String judgingCode=request.getParameter("judgingCode");
        PageData pd = new PageData();
        pd.put("judgingCode",request.getParameter("judgingCode") == null ? "" : request.getParameter("judgingCode"));

        TableReturn tablereturn = new TableReturn();
        List<Map<String, Object>> hashMaps = new ArrayList<>();
        List<base_judging_series> list = baseJudingSeriesService.getList(pd);
        for (base_judging_series juding : list) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", juding.getId());
            map.put("judgingCode", judgingCode);
            map.put("reviewSeries",juding.getReviewSeries());
            map.put("back3",getDict("REVIEW_SERIES",String.valueOf(juding.getReviewSeries())).getDictName());
            hashMaps.add(map);
        }
        tablereturn.setRows(hashMaps);
        ResultUtils.write(response, toJson(tablereturn));
    }
    //删除评审系列数据
    @RequestMapping(value = "Delete2")
    public void Delete2(HttpServletRequest request, HttpServletResponse response) {
        logger.info("显示删除评委会数据");
        PageData pd = new PageData();
        String ids = request.getParameter("ids") == null ? "" : request.getParameter("ids");
        String jc =request.getParameter("jc") == null ? "" : request.getParameter("jc");
       // base_judging_series list = new base_judging_series();
        pd.put("judgingCode",jc);
        pd.put("reviewSeries",ids);
        try {
            if (baseJudingSeriesService.deleteSeries(pd) >= 1)
                ResultUtils.writeMessage(response, 1, "删除成功");
            else
                ResultUtils.writeMessage(response, 0, "删除失败");
        } catch (Exception ex) {
            ResultUtils.writeMessage(response, 0, ex.getMessage());
        }
    }
    //保存评审系列数据
    @RequestMapping(value = "Save2")
    public void Save2(HttpServletRequest request, HttpServletResponse response) {
        logger.info("评审系列保存");
        base_judging_series model = null;
            model = new base_judging_series();
            model.setJudgingCode(request.getParameter("judgingCode"));
            model.setAddtime(new Date());
            model.setAdduserid(getUser().getUserId());
            model.setReviewSeries(request.getParameter("reviewSeries"));
            if (baseJudingSeriesService.add(model) == 1)
                ResultUtils.writeMessage(response, 1, "添加成功");
            else
                ResultUtils.writeMessage(response, 0, "添加失败");

    }
    //加载评审专业数据
    @RequestMapping("getJudgingMojorList.do")
    public void getJudgingMojorList(HttpServletRequest request, HttpServletResponse response) {
        logger.info("显示评审专业数据");
        String judgingCode=request.getParameter("judgingCode");
        PageData pd = new PageData();
        pd.put("rows", request.getParameter("rows") == null? 10: Integer.parseInt(request.getParameter("rows")));//每页显示行数
        pd.put("page", request.getParameter("page") == null ? 0 : (Integer.parseInt(request.getParameter("page"))));//每页记录开始位置
        pd.put("judgingCode",request.getParameter("judgingCode") == null ? "" : request.getParameter("judgingCode"));
        pd.put("reviewSeries",request.getParameter("reviewSeries") == null ? "" : request.getParameter("reviewSeries"));
        TableReturn tablereturn = new TableReturn();
        List<Map<String, Object>> hashMaps = new ArrayList<>();
        List<base_judging_series> list = baseJudingSeriesService.getMojorList(pd);
        Integer listCount = baseJudingSeriesService.getMojorListCount(pd);
        for (base_judging_series juding : list) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", juding.getId());
            map.put("judgingCode", judgingCode);
            map.put("juryProfessionCode",juding.getJuryProfessionCode());
            if(juding.getReviewProfessial()!=null&&juding.getReviewProfessial()!=""){
                PageData pd1 = new PageData();
                pd1.put("professialCode",juding.getReviewProfessial());
                base_series_professial model1 =baseSeriesProfessialService.getListName1(pd1);
                map.put("reviewProfessial",model1.getProfessialName());
                hashMaps.add(map);
            }

        }
        tablereturn.setRows(hashMaps);
        tablereturn.setTotal(listCount);
        ResultUtils.write(response, toJson(tablereturn));
    }
    @RequestMapping(value = "Save3")
    public void Save3(HttpServletRequest request, HttpServletResponse response) {
        logger.info("评审专业保存");
        String id = request.getParameter("reviewProfessial");
        base_series_professial model1 = baseSeriesProfessialService.getModel(id);
        String judgingCode = request.getParameter("judgingCode");
        PageData pd = new PageData();
        pd.put("judgingCode", judgingCode);
        pd.put("judgingCodelength",6);
        String juryProfessionCode = baseJudingSeriesService.getJudingCode(pd);
        if (juryProfessionCode == null) {//如果第一次添加
            juryProfessionCode = judgingCode + "01";
        } else {
            String judgingCode1= juryProfessionCode.substring(juryProfessionCode.length()-2,juryProfessionCode.length());
            Long judgingCode2 = Long.parseLong(judgingCode1) +1;
         String a = Long.toString(judgingCode2);
         if(a.length()==1){
             String  b ="0"+a;
             juryProfessionCode = judgingCode+b;
         }else{
             juryProfessionCode = judgingCode+a;
         }
        }
        String  reviewProfessial = model1.getProfessialCode();
        base_judging_series model = null;
        model = new base_judging_series();
        model.setJuryProfessionCode(juryProfessionCode);
        model.setJudgingCode(judgingCode);
        model.setAddtime(new Date());
        model.setAdduserid(getUser().getUserId());
        model.setReviewSeries(request.getParameter("reviewSeries"));
        model.setReviewProfessial(reviewProfessial);
        if (baseJudingSeriesService.add(model) == 1)
            ResultUtils.writeMessage(response, 1, "添加成功");
        else
            ResultUtils.writeMessage(response, 0, "添加失败");

    }
    @RequestMapping(value = "Delete3")
    public void Delete3(HttpServletRequest request, HttpServletResponse response) {
        String ids = request.getParameter("ids") == null ? "" : request.getParameter("ids");
        try {
            if (baseJudingSeriesService.delete(ids) == 1)
                ResultUtils.writeMessage(response, 1, "删除成功");
            else
                ResultUtils.writeMessage(response, 0, "删除失败");
        } catch (Exception ex) {
            ResultUtils.writeMessage(response, 0, ex.getMessage());
        }
    }
    public  boolean isNumeric(String str){
        for (int i = str.length();--i>=0;){
            if (!Character.isDigit(str.charAt(i))){
                return false;
            }
        }
        return true;
    }

    //加载专家库信息
    @RequestMapping("getJudgingGroupList.do")
    public void getJudgingGroupList(HttpServletResponse response, Page page) {
        logger.info("显示评审专业数据");
        //String judgingCode=request.getParameter("judgingCode");
        PageData pd = this.getPageData();
       /* pd.put("rows", request.getParameter("rows") == null? 10: Integer.parseInt(request.getParameter("rows")));//每页显示行数
        pd.put("page", request.getParameter("page") == null ? 0 : (Integer.parseInt(request.getParameter("page"))));//每页记录开始位置
        pd.put("SPECIALITY_NAME",request.getParameter("SPECIALITY_NAME") == null ? "" : request.getParameter("SPECIALITY_NAME"));
        pd.put("NOWUNIT",request.getParameter("NOWUNIT") == null ? "" : request.getParameter("NOWUNIT"));
        pd.put("RECOMMEND_MAJOR",request.getParameter("RECOMMEND_MAJOR") == null ? "" : request.getParameter("RECOMMEND_MAJOR"));
        pd.put("NOTJUDGINGID",request.getParameter("NOTJUDGINGID") == null ? "" : request.getParameter("NOTJUDGINGID"));*/
        //TableReturn tablereturn = new TableReturn();
        page.setPd(pd);
        List<PageData> list = baseJudingSeriesService.selectGrouplistPage(page);
        page.setData(list);
        ResultUtils.write(response, toJson(page));
    }

    /**
     * 修改主任/副主任
     * @param request
     * @param response
     */
    @Transactional(propagation= Propagation.REQUIRED)
    @RequestMapping(value = "UpdateDirector")
    public void UpdateDirector(HttpServletRequest request, HttpServletResponse response) {
        try {
            PageData pageData=new PageData();
            String  DIRECTOR=request.getParameter("JURY_DUTY");
            pageData.put("JUDGING_ID",request.getParameter("JUDGING_ID"));
            pageData.put("SPECIALITY_ID",request.getParameter("SPECIALITY_ID"));
            pageData.put("JURY_DUTY",DIRECTOR);
            pageData.put("ADDUSERID",getUser().getUserId());
            pageData.put("STATE","1");
            if(DIRECTOR.equals("1")){  //设置主任
                baseJudingSeriesService.UpdateDirector(pageData);
                pageData.put("JURY_DUTY","3");
                baseJudingSeriesService.UpdateDirectorTwo(pageData);
            }else{//取消副主任或设置委员
                baseJudingSeriesService.UpdateDirector(pageData);
            }
            ResultUtils.writeMessage(response, 1, "设置成功");
        } catch (Exception ex) {
            ex.printStackTrace();
            ResultUtils.writeMessage(response, 0, "设置失败");
        }
    }


    @RequestMapping(value = "checkCode")
    public void checkCode(HttpServletRequest request, HttpServletResponse response) {
        logger.info("判断专业名字是否已存在");
        PageData pageData = new PageData();
        String id = request.getParameter("reviewProfessial");
        base_series_professial model1 = baseSeriesProfessialService.getModel(id);
        String judgingCode = request.getParameter("judgingCode");
        String  reviewProfessial = model1.getProfessialCode();
        String reviewSeries=request.getParameter("reviewSeries");
//        //String professialName = request.getParameter("reviewProfessial");
//        String reviewSeries = request.getParameter("reviewSeries1");
//        pageData.put("professialName",professialName);
//        pageData.put("reviewSeries",reviewSeries);
//        base_series_professial model1 = baseSeriesProfessialService.getListName1(pageData);
//        //base_professial baseProfessial=  baseProfessialService.getProfeWhere(pageData);
//         String  reviewProfessial=model1.getProfessialCode();
//        String judgingCode = request.getParameter("judgingCode1");
        pageData.put("reviewProfessial",reviewProfessial);
        pageData.put("reviewSeries",reviewSeries);
        pageData.put("judgingCode",judgingCode);
        base_judging_series model = baseJudingSeriesService.getMojorListWhere(pageData);
        if (model != null) {
            ResultUtils.writeMessage(response, 0, "专业已存在");
            ResultUtils.write(response, false);
        } else {
            ResultUtils.writeMessage(response, 1, "");
            ResultUtils.write(response, true);
        }
    }




    //根据judgingCode 获取评审系列
    @RequestMapping(value = "getReviewSeries")
    public void getReviewSeries(HttpServletRequest request, HttpServletResponse response) {
        logger.info("根据judgingCode 获取评审系列");
        String judgingCode = request.getParameter("judgingCode");
        PageData pageData = new PageData();
        pageData.put("judgingCode", judgingCode);
        base_judging_series baseJudgingSeries = baseJudingSeriesService.getModelWhere(pageData);
        ResultUtils.write(response,toJson(baseJudgingSeries));
    }


}
