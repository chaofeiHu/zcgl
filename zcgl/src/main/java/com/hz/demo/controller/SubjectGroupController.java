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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.hz.demo.core.EncodeUtil.encodeMD5;
import static com.hz.demo.core.ResultUtils.toDateTimeJson;
import static com.hz.demo.core.ResultUtils.toJson;

@Controller
@RequestMapping("/SubjectGroup")
public class SubjectGroupController extends BaseController{

    @Autowired
    UserService userService;
    @Autowired
    SubjectGroupService subjectGroupService;
    @Autowired
    BaseJudgingService baseJudgingService;
    @Autowired
    BaseProfessialService baseProfessialService;
    @Autowired
    BaseJudingSeriesService baseJudingSeriesService;
    @Autowired
    BaseSeriesProfessialService baseSeriesProfessialService;
    @Autowired
    SpecialityGroupService specialityGroupService;

   //跳到学科分组界面
    @RequestMapping(value = "SubjectGroupPage")
    public String DeptPage(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("MenuBtns",userService.GetMenuBtns(request.getParameter("menuid"),getUser().getUserId()));
        return "sys/subjectGroup";
    }

    //获取界面列表
    @RequestMapping(value = "getList")
    public void getList(HttpServletResponse response,Page page) {
        logger.info("显示学科分组列表数据");
        PageData pd = getPageData();
        String judgingCode = pd.getString("judgingCode");
        TableReturn tablereturn = new TableReturn();
        List<subject_group> slist = new ArrayList<>();
        Integer listCount = 0;
        page.setPd(pd);
        List<subject_group> list = subjectGroupService.getListlistPage(page);
        if (list != null && list.size()>0) {
            for (subject_group subjectGroup:list) {
                    String profeId = "";
                    String pids = subjectGroup.getProfessialId();
                    if (pids != null && pids.length()!=0) {
                        String[] proids = pids.split(",");
                        for (String professialId:proids) {
                            PageData pageData = new PageData();
                            pageData.put("professialCode",professialId);
                            base_series_professial baseSeriesProfessial = baseSeriesProfessialService.getProfeWhere(pageData);
                            if (baseSeriesProfessial == null) {
                                profeId += "";
                                continue;
                            }
                            profeId += baseSeriesProfessial.getProfessialName()+",";
                        }
                        subjectGroup.setProfessialId(profeId);
                    }
                    slist.add(subjectGroup);
                }
            }
        page.setData(slist);

        ResultUtils.write(response,toJson(page));
    }
    
    //更新前获取数据
    @RequestMapping(value = "addOrUpdate")
    public void AddOrUpdate(HttpServletRequest request, HttpServletResponse response) {
        logger.info("学科分组更新前获取数据");
        String ids = request.getParameter("id") == null ? "" : request.getParameter("id");
        if (!"".equals(ids)) {
            //更新（获取）
            Integer id = Integer.parseInt(ids);
            subject_group subjectGroup = subjectGroupService.getModel(id);
            if (subjectGroup != null) {
               // String proid = subjectGroup.getProfessialId() ==null?"" :subjectGroup.getProfessialId();
                //subjectGroup.setProfessialId(proid);
                ResultUtils.write(response, toJson(subjectGroup));
            } else {
                ResultUtils.write(response, "记录信息不存在！");
            }
        }
    }

    //保存
    @RequestMapping(value = "save")
    public void Save(HttpServletRequest request, HttpServletResponse response) {
        logger.info("保存");
        PageData pd=this.getPageData();
        String id = pd.getString("id") == null ? "" : pd.getString("id");
       // String judgingCode = request.getParameter("judgingCode");
        try {
            if ("".equals(id)) { //添加
                if (subjectGroupService.add(pd) == 1) {
                    ResultUtils.writeMessage(response, 1, "添加成功");
                } else {
                    ResultUtils.writeMessage(response, 0, "添加失败");
                }
            }else {//修改
                if (subjectGroupService.update(pd) == 1){
                    ResultUtils.writeMessage(response, 1, "修改成功");
                }else{
                    ResultUtils.writeMessage(response, 0, "修改失败");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            ResultUtils.writeMessage(response, 0, "失败");
        }
    }

    //删除
    @Transactional(propagation= Propagation.REQUIRED)
    @RequestMapping(value = "delete")
    public void Delete(HttpServletRequest request, HttpServletResponse response) {
        String ids = request.getParameter("ids");
        if (ids == null || "".equals(ids)) {
            ResultUtils.writeMessage(response, 0, "没有数据!");
        }else {
            String[] strs = ids.split(",");
            try {
                for (String id : strs) {
                    Integer id1 = Integer.parseInt(id);
                    PageData pd = new PageData();
                    pd.put("groupId", id1);
                    specialityGroupService.deleteWhere(pd); //先删除专业组下专家信息
                    subjectGroupService.delete(id1);
                }
                ResultUtils.writeMessage(response, 1, "删除成功");
            } catch (Exception ex) {
                ResultUtils.writeMessage(response, 0, ex.getMessage());
            }
        }
    }

    //获取评委会系列 及 分组专业数据
    @RequestMapping(value = "getReviewSeries")
    public void getReviewSeries(HttpServletRequest request, HttpServletResponse response) {
        String judgingId = request.getParameter("judgingId");
        PageData pd1 = new PageData();
        pd1.put("judgingId", judgingId);
        base_judging_series judgingSeries =  baseJudingSeriesService.getReviewSeries(pd1);
        PageData pd = new PageData();
        base_judging baseJudging = baseJudgingService.getModel(judgingId);
        pd.put("judgingCode", baseJudging.getJudgingCode());//评委会code
        List<subject_group> list = subjectGroupService.selectSubjectList(pd);
        List<String> slist = new ArrayList<>();
        if (list != null && list.size()>0) {
            for (subject_group subjectGroup:list) {
                String pids = subjectGroup.getProfessialId();
                if (pids != null && pids.length()!=0) {
                    String[] strs = pids.split(",");
                    for (String proid:strs) {
                        slist.add(proid);
                    }
                }
            }
        }
        Map map = new HashMap();
        map.put("judgingSeries", judgingSeries);
        map.put("proList",slist);
        ResultUtils.write(response, toJson(map));
    }

    /*//获取分组专业数据
    @RequestMapping(value = "getProfessialIds")
    public void getProfessialIds(HttpServletRequest request, HttpServletResponse response) {
        logger.info("获取分组专业数据");
        try {
            PageData pageData = new PageData();
            String reviewSeries= request.getParameter("reviewSeries") == null ? "" : request.getParameter("reviewSeries");
            String unitCode = "000000";
            //pageData.put("PROFESSIAL_CODE",unitCode);
            pageData.put("reviewSeries",reviewSeries);
            String judgingId = request.getParameter("judgingId");
            PageData pd = new PageData();
            base_judging baseJudging = baseJudgingService.getModel(judgingId);
            pd.put("judgingCode", baseJudging.getJudgingCode());//评委会code
            List<subject_group> list = subjectGroupService.selectSubjectList(pd);
            List<String> slist = new ArrayList<>();
                if (list != null && list.size()>0) {
                    for (subject_group subjectGroup:list) {
                        String pids = subjectGroup.getProfessialId();
                        if (pids != null && pids.length()!=0) {
                            String[] strs = pids.split(",");
                            for (String proid:strs) {
                                slist.add(proid);
                            }
                        }
                    }
                    //pageData.put("proids", slist);
                }
            ResultUtils.write(response, toJson(slist));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

   /* //获取分组专业数据
    @RequestMapping(value = "getListWhereBing")
    public void getListWhereBing(HttpServletRequest request, HttpServletResponse response) {
        logger.info("根据条件获取机构集合");
        try {
            PageData pageData = new PageData();
            String reviewSeries= request.getParameter("reviewSeries") == null ? "" : request.getParameter("reviewSeries");
            String unitCode = "000000";
            //pageData.put("PROFESSIAL_CODE",unitCode);
            pageData.put("reviewSeries",reviewSeries);
            String judgingId = request.getParameter("judgingId");
            PageData pd = new PageData();
            base_judging baseJudging = baseJudgingService.getModel(judgingId);
            pd.put("judgingCode", baseJudging.getJudgingCode());//评委会code
            List<subject_group> list = subjectGroupService.selectSubjectList(pd);
            if (list != null && list.size()>0) {
                List<String> slist = new ArrayList<>();
                for (subject_group subjectGroup:list) {
                    String pids = subjectGroup.getProfessialId();
                    if (pids != null && pids.length()!=0) {
                        String[] strs = pids.split(",");
                        for (String proid:strs) {
                            slist.add(proid);
                        }
                    }
                }
                pageData.put("proids", slist);
            }
            List<Tree> treeList = baseSeriesProfessialService.getBingList(pageData);
            ResultUtils.write(response, ObjectList2TreeJson(unitCode, treeList, 0));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
   /* //获取评委会列表
    @RequestMapping(value = "getJudgingList")
    public void getJudgingList(HttpServletRequest request, HttpServletResponse response) {
        String judgingName = request.getParameter("judgingName");
        PageData pd = new PageData();
        pd.put("judgingName", judgingName);
        List<base_judging> judgingList = specialityService.getJudgingList(pd);
        ResultUtils.write(response, judgingList);
    }

    //获取评委会树列表
    @RequestMapping(value = "getJudgingTree")
    public void getJudgingTree(HttpServletRequest request, HttpServletResponse response) {
        PageData pageData = new PageData();
        List<Tree> judgingList = specialityService.getJudgingTree(pageData);
        ResultUtils.write(response, ObjectList2TreeJson("0", judgingList, 1));
    }*/

    //根据条件获取专业集合 下拉列表 排除已存在的专业
    @RequestMapping(value = "getProfessialByGroup")
    public void getProfessialByGroup(HttpServletRequest request, HttpServletResponse response) {
        logger.info("根据条件获取排除已存在的专业集合");
        try {
            PageData pageData = this.getPageData();
            /*String reviewSeries= request.getParameter("reviewSeries") == null ? "" : request.getParameter("reviewSeries");
            String judgingId = request.getParameter("judgingId");
            String groupId = request.getParameter("groupId");
            PageData pageData = new PageData();
            pageData.put("judgingId", judgingId);
            pageData.put("reviewSeries",reviewSeries);
            //查询出所有专业集合
            List<base_series_professial> list = baseSeriesProfessialService.getRevProByRevSer(pageData);
            List<String> blist = new ArrayList<>();
            List<String> strList = new ArrayList<>();
            for (base_series_professial baseSeriesProfessial:list) {
                blist.add(baseSeriesProfessial.getProfessialCode());
            }
            PageData pd = new PageData();
            base_judging baseJudging = baseJudgingService.getModel(judgingId);
            pd.put("judgingCode", baseJudging.getJudgingCode());//评委会code
            if (groupId!=null && !"undefined".equals(groupId) && !"".equals(groupId)) {
                pd.put("groupId",Integer.parseInt(groupId));
            }
            List<subject_group> slist = subjectGroupService.selectSubjectList(pd);//当前评委会已存在的专业集合
            for (subject_group subjectGroup:slist ) {
                    String pids = subjectGroup.getProfessialId();
                    if (pids != null && pids.length()!=0) {
                        String[] strs = pids.split(",");
                        for (String proid:strs) {
                            strList.add(proid);
                        }
                    }
                }
            blist.removeAll(strList);
            List proList = new ArrayList();
            for (String proCode:blist) {
                PageData pageData1 = new PageData();
                pageData1.put("professialCode", proCode);
                proList.add(baseSeriesProfessialService.getProfeWhere(pageData1));
            }
            ResultUtils.write(response, proList);*/
            List<PageData> item= baseSeriesProfessialService.getProfessialByGroup(pageData);
            ResultUtils.write(response, toJson(item));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
