package com.hz.demo.controller;

import com.hz.demo.core.PageData;
import com.hz.demo.core.ResultUtils;
import com.hz.demo.core.TableReturn;
import com.hz.demo.core.Tree;
import com.hz.demo.model.base_judging;
import com.hz.demo.model.base_series_professial;
import com.hz.demo.model.sys_dict;
import com.hz.demo.services.BaseProfessialService;
import com.hz.demo.services.BaseSeriesProfessialService;
import com.hz.demo.services.DictService;
import com.hz.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;

import static com.hz.demo.core.ResultUtils.toJson;

@Controller
@RequestMapping("/SeriesProfessial")
public class BaseSeriesProfessialController extends BaseController {
    @Autowired
    UserService userService;
    @Autowired
    BaseSeriesProfessialService baseSeriesProfessialService;
    @Autowired
    DictService dictService;
    @Autowired
    BaseProfessialService baseProfessialService;
    @RequestMapping(value = "SeriesProfessialPage")
    public String SeriesProfessialPage(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("MenuBtns",userService.GetMenuBtns(request.getParameter("menuid"),getUser().getUserId()));
        return "sys/seriesprofessial";
    }
    //获取评审系列界面列表
    @RequestMapping(value = "getDictList")
    public void getDictList(HttpServletRequest request, HttpServletResponse response) {
        logger.info("显示评审系列列表数据");
        PageData pd = new PageData();
        TableReturn tablereturn = new TableReturn();
        List<Map<String, Object>> hashMaps = new ArrayList<>();
        List<sys_dict> list = dictService.getDictSeies(pd);
        for (sys_dict dict : list) {
            Map<String, Object> map = new HashMap<>();
            map.put("dictName", dict.getDictName());

            hashMaps.add(map);
        }
        tablereturn.setRows(list);
        ResultUtils.write(response, toJson(tablereturn));
    }


    //加载对应专业列表
    @RequestMapping(value = "getJudgingSerList")
    public void getJudgingSerList(HttpServletRequest request, HttpServletResponse response) {
        logger.info("显示对应专业列表数据");
         String reviewSeries=request.getParameter("reviewSeries");
        PageData pd = new PageData();
        pd.put("rows", request.getParameter("rows") == null? 10: Integer.parseInt(request.getParameter("rows")));//每页显示行数
        pd.put("page", request.getParameter("page") == null ? 0 : (Integer.parseInt(request.getParameter("page"))));//每页记录开始位置

        pd.put("reviewSeries",request.getParameter("reviewSeries") == null ? "" : request.getParameter("reviewSeries"));
        TableReturn tablereturn = new TableReturn();
        Integer listCount = baseSeriesProfessialService.getListCount(pd);
        List<Map<String, Object>> hashMaps = new ArrayList<>();
        List<base_series_professial> list = baseSeriesProfessialService.getList(pd);
        for (base_series_professial professial : list) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", professial.getId());
            map.put("professialCode",professial.getProfessialCode());
            map.put("professialName",professial.getProfessialName());
            map.put("state",professial.getState());
            hashMaps.add(map);
        }
        tablereturn.setRows(hashMaps);
        tablereturn.setTotal(listCount);
        ResultUtils.write(response, toJson(tablereturn));
    }
    @RequestMapping(value = "AddOrUpdate")
    public void AddOrUpdate(HttpServletRequest request, HttpServletResponse response) {
        logger.info("专业的添加或更新（获取）");
        String id = request.getParameter("id") == null ? "" : request.getParameter("id");
        if (id.equals("")) {
            //添加
        } else {
            //更新（获取）
            base_series_professial model = baseSeriesProfessialService.getModel(id);
            if (model != null) {
                ResultUtils.write(response, toJson(model));
            } else {
                ResultUtils.write(response, "记录信息不存在！");
            }
        }

    }
    @RequestMapping(value = "Save")
    public void Save(HttpServletRequest request, HttpServletResponse response) {
        logger.info("保存");
        String id = request.getParameter("id") == null ? "" : request.getParameter("id");
        base_series_professial model = null;
        if (id.equals("")) { //添加
            model = new base_series_professial();
            model.setReviewSeries(request.getParameter("reviewSeries"));
            model.setProfessialCode(request.getParameter("professialCode"));
            model.setProfessialName(request.getParameter("professialName"));
            model.setState(Short.valueOf(Short.parseShort(request.getParameter("state") == null ? "0" : request.getParameter("state"))));
            if (baseSeriesProfessialService.add(model) == 1)
                    ResultUtils.writeMessage(response, 1, "添加成功");
            else
                ResultUtils.writeMessage(response, 0, "添加失败");

        } else {//修改
            PageData pd = new PageData();
            pd.put("id",id);
            pd.put("reviewSeries", request.getParameter("reviewSeries"));
            pd.put("professialCode", request.getParameter("professialCode"));
            pd.put("professialName", request.getParameter("professialName"));
            pd.put("state", request.getParameter("state") == null ? "0" : request.getParameter("state"));
            if (baseSeriesProfessialService.update(pd) == 1)
                ResultUtils.writeMessage(response, 1, "修改成功");
            else
                ResultUtils.writeMessage(response, 0, "修改失败");
        }

    }
    //验证信息是否存在
    @RequestMapping(value = "checkCode")
    public void checkCode(HttpServletRequest request, HttpServletResponse response) {
        logger.info("判断信息是否已存在");
        PageData pageData = new PageData();
        String id = request.getParameter("id");
        String reviewSeries = request.getParameter("reviewSeries");
        String professialCode = request.getParameter("professialCode");
        String professialName = request.getParameter("professialName");
        pageData.put("id",id);
        pageData.put("reviewSeries",reviewSeries);
        pageData.put("professialCode",professialCode);
        pageData.put("professialName",professialName);
        base_series_professial model =baseSeriesProfessialService.getListName1(pageData);
        if (model != null) {
            ResultUtils.write(response, false);
        } else {
            ResultUtils.write(response, true);
        }
    }


        @RequestMapping(value = "Delete")
        public void Delete(HttpServletRequest request, HttpServletResponse response) {
            String ids = request.getParameter("ids") == null ? "" : request.getParameter("ids");
            try {
                if (baseSeriesProfessialService.delete(ids) == 1)
                    ResultUtils.writeMessage(response, 1, "删除成功");
                else
                    ResultUtils.writeMessage(response, 0, "删除失败");
            } catch (Exception ex) {
                ResultUtils.writeMessage(response, 0, ex.getMessage());
            }
        }

//    //保存评审系列对应的专业
//    @RequestMapping(value = "saveReviewSeries")
//    public void SaveRoleMenuFun(HttpServletRequest request, HttpServletResponse response) {
//        logger.info("保存评审系列对应的专业");
//        PageData pd = new PageData();
//
//        pd.put("reviewSeries", request.getParameter("reviewSeries") == null ? "" : request.getParameter("reviewSeries"));//角色id
//        String menufunids = request.getParameter("menufunids") == null ? "" : request.getParameter("menufunids");
//
//        menufunids = menufunids.replace(",,,,,,,,", ",").replace(",,,,,,,", ",").replace(",,,,,,", ",").replace(",,,,,", ",").replace(",,,,", ",").replace(",,,", ",").replace(",,", ",");
//        if (menufunids.substring(0, 1).equals(","))
//            menufunids = menufunids.substring(1, menufunids.length() - 1);//去掉第一个逗号
//        if (menufunids.substring(menufunids.length() - 1, menufunids.length()).equals(","))
//            menufunids = menufunids.substring(0, menufunids.length() - 1);//去掉最后一个逗号
//        String[] MenuFunIDS = menufunids.split(",");
//        pd.put("MenuFunIDS", MenuFunIDS);
//
//        if (baseSeriesProfessialService.SaveSeriesProfessial(pd) == 1)
//            ResultUtils.writeMessage(response, 1, " 保存成功");
//        else
//            ResultUtils.writeMessage(response, 0, "保存失败");
//    }

//    //获取相关专业树
//    @RequestMapping(value = "getProfessialTree")
//    public void getDictTree(HttpServletRequest request, HttpServletResponse response) {
//        logger.info("获取相关专业树");
//        String professialParentcode = request.getParameter("professialParentcode") == null ? "000000" : request.getParameter("professialParentcode");
//        PageData pd = new PageData();
//        pd.put("professialParentcode", professialParentcode);
//        String reviewSeries = request.getParameter("reviewSeries");
//        List<base_series_professial> list=baseSeriesProfessialService.getRevProByRevSer(reviewSeries);
//        List<Tree> treeList = baseProfessialService.getTreeList(pd);
//        for(base_series_professial modules:list) {//遍历已经关联的菜单
//
//            for (Tree tre:treeList) {
//                if(tre.getId().equals(modules.getReviewProfessial())){
//                    tre.setChecked(1);
//                }
//            }
//        }
//
//
//        ResultUtils.write(response, ObjectList2TreeJson(professialParentcode, treeList, 1));
//    }



}
