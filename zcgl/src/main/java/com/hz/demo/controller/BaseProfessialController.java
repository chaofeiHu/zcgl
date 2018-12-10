package com.hz.demo.controller;

import com.hz.demo.core.PageData;
import com.hz.demo.core.ResultUtils;
import com.hz.demo.core.TableReturn;
import com.hz.demo.core.Tree;
import com.hz.demo.model.base_professial;
import com.hz.demo.model.base_series_professial;
import com.hz.demo.services.BaseProfessialService;
import com.hz.demo.services.BaseSeriesProfessialService;
import com.hz.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.hz.demo.core.ResultUtils.toJson;

@Controller
@RequestMapping("/Professial")
public class BaseProfessialController extends BaseController{
    @Autowired
    UserService userService;
    @Autowired
    BaseProfessialService baseProfessialService;
    @Autowired
    BaseSeriesProfessialService baseSeriesProfessialService;
    @RequestMapping(value = "Professial")
    public String DeptPage(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("MenuBtns",userService.GetMenuBtns(request.getParameter("menuid"),getUser().getUserId()));
        return "sys/professial";
    }
    //获取从事专业树
    @RequestMapping(value = "getProfessialTree")
    public void getDictTree(HttpServletRequest request, HttpServletResponse response) {
        String professialParentcode = request.getParameter("professialParentcode") == null ? "000000" : request.getParameter("professialParentcode");
        PageData pd = new PageData();
        pd.put("professialParentcode", professialParentcode);
        String state = request.getParameter("state") == null ? null : request.getParameter("state");
        if (state != null)
            pd.put("state", state);
        List<Tree> treeList = baseProfessialService.getTreeList(pd);
        ResultUtils.write(response, ObjectList2TreeJson(professialParentcode, treeList, 1));
    }

    //获取从事专业列表
    @RequestMapping(value = "getProfessialList")
    public void getDictList(HttpServletRequest request, HttpServletResponse response) {
        logger.info("显示从事专业列表数据");
        PageData pd = new PageData();
        pd.put("rows", request.getParameter("rows") == null? 10: Integer.parseInt(request.getParameter("rows")));//每页显示行数
        pd.put("page", request.getParameter("page") == null ? 0 : (Integer.parseInt(request.getParameter("page"))));//每页记录开始位置
        pd.put("professialParentcode", request.getParameter("professialParentcode") == null ? "" : request.getParameter("professialParentcode"));//父ID
        pd.put("professialName", request.getParameter("professialName") == null ? "" : request.getParameter("professialName"));//名称

        TableReturn tablereturn = new TableReturn();
        List<base_professial> list = baseProfessialService.getList(pd);
        Integer listCount = baseProfessialService.getListCount(pd);
        tablereturn.setRows(list);
        tablereturn.setTotal(listCount);
        ResultUtils.write(response, toJson(tablereturn));
    }

    //更改从事专业状态
    @RequestMapping(value = "updateState")
    public void updateState(HttpServletRequest request, HttpServletResponse response) {
        logger.info("更改从事专业状态");
        PageData pd = new PageData();
        pd.put("id", request.getParameter("id") == null ? "" : request.getParameter("id"));//ID
        pd.put("state", request.getParameter("state") == null ? "0" : request.getParameter("state"));//记录状态
        Map<String, String> map = new HashMap<>();
        map.put("isOk", "0");
        if (baseProfessialService.update(pd) == 1)
            map.put("isOk", "1");
        ResultUtils.write(response, toJson(map));
    }

    //添加或更新（获取）
    @RequestMapping(value = "AddOrUpdate")
    public void AddOrUpdate(HttpServletRequest request, HttpServletResponse response) {
        logger.info("从事专业的添加或更新（获取）");
        String id = request.getParameter("id") == null ? "" : request.getParameter("id");
        if (id.equals("")) {
            //添加
        } else {
            //更新（获取）
            base_professial model = baseProfessialService.getModel(id);
            if (model != null) {
                ResultUtils.write(response, toJson(model));
            } else {
                ResultUtils.write(response, "记录信息不存在！");
            }
        }

    }

    //保存
    @RequestMapping(value = "Save")
    public void Save(HttpServletRequest request, HttpServletResponse response) {
        logger.info("保存");
        String id = request.getParameter("id") == null ? "" : request.getParameter("id");
        base_professial model = null;
        if (id.equals("")) { //添加
            model = new base_professial();
            model.setProfessialCode(request.getParameter("professialCode"));
            model.setProfessialName(request.getParameter("professialName"));
            model.setProfessialGrade((Integer.parseInt(request.getParameter("professialGrade"))+1)+"");
            model.setAddtime(new Date());
            model.setAdduserid(getUser().getUserId());
            model.setProfessialParentcode(request.getParameter("professialParentcode"));
            model.setState(BigDecimal.valueOf(Long.parseLong(request.getParameter("state") == null ? "0" : request.getParameter("state"))));
            model.setFsort(BigDecimal.valueOf(Long.parseLong(request.getParameter("fsort"))));
            model.setBack1(request.getParameter("backup1"));

            PageData pd = new PageData();
            pd.put("professialCode", request.getParameter("professialParentcode"));
            if (baseProfessialService.update(pd) == 0) {
                if (baseProfessialService.add(model) == 1)
                    ResultUtils.writeMessage(response, 1, "添加成功");
                else
                    ResultUtils.writeMessage(response, 0, "添加失败");
            } else
                ResultUtils.writeMessage(response, 0, "添加失败");
        } else {//修改
            PageData pd = new PageData();
            pd.put("id",id);
            pd.put("professialCode", request.getParameter("professialCode"));
            pd.put("professialName", request.getParameter("professialName"));
            pd.put("state", request.getParameter("state") == null ? "0" : request.getParameter("state"));
            pd.put("fsort", request.getParameter("fsort"));
            pd.put("backup1", request.getParameter("backup1"));
            if (baseProfessialService.update(pd) == 1)
                ResultUtils.writeMessage(response, 1, "修改成功");
            else
                ResultUtils.writeMessage(response, 0, "修改失败");
        }

    }

    //删除
    @RequestMapping(value = "Delete")
    public void Delete(HttpServletRequest request, HttpServletResponse response) {
        String ids = request.getParameter("ids") == null ? "" : request.getParameter("ids");
        try {
            if (baseProfessialService.delete(ids) == 1)
                ResultUtils.writeMessage(response, 1, "删除成功");
            else
                ResultUtils.writeMessage(response, 0, "删除失败");
        } catch (Exception ex) {
            ResultUtils.writeMessage(response, 0, ex.getMessage());
        }
    }
    //根据条件获取机构集合  下拉列表树
    @RequestMapping(value = "getListWhere")
    public void getListWhere(HttpServletRequest request, HttpServletResponse response) {
        logger.info("根据条件获取机构集合");
        try {
            PageData pageData = new PageData();
            String reviewSeries= request.getParameter("reviewSeries") == null ? "" : request.getParameter("reviewSeries");
            base_professial professial=new base_professial();
            String unitCode = "000000";
            pageData.put("PROFESSIAL_CODE",unitCode);
            List<Tree> treeList = baseProfessialService.getListWhere(pageData);
            ResultUtils.write(response, ObjectList2TreeJson(unitCode, treeList, 1));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @RequestMapping(value = "getListName")
    public void getListName(HttpServletRequest request, HttpServletResponse response) {
        logger.info("根据条件获取专业集合");
        String reviewSeries= request.getParameter("reviewSeries") == null ? "" : request.getParameter("reviewSeries");
        List<base_series_professial> list= null;
        try {
            PageData pageData = new PageData();
            pageData.put("reviewSeries",reviewSeries);
           list = baseSeriesProfessialService.getListName(pageData);
            ResultUtils.write(response, list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //根据条件获取专业集合 下拉列表树
    @RequestMapping(value = "getListWhereBing")
    public void getListWhereBing(HttpServletRequest request, HttpServletResponse response) {
        logger.info("根据条件获取专业集合");
        try {
            PageData pageData = new PageData();
            String reviewSeries= request.getParameter("reviewSeries") == null ? "" : request.getParameter("reviewSeries");
            //String unitCode = "000000";
            //pageData.put("PROFESSIAL_CODE",unitCode);
            pageData.put("reviewSeries",reviewSeries);
            List<Tree> treeList = baseSeriesProfessialService.getBingList(pageData);
            ResultUtils.write(response, ObjectList2TreeJson("6f5a344a-2f9b-4f94-86f6-caa80ce4db5f", treeList, 0));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //根据条件获取专业集合 下拉列表
    @RequestMapping(value = "getProfessialListWhere")
    public void getProfessialListWhere(HttpServletRequest request, HttpServletResponse response) {
        logger.info("根据条件获取所有专业集合");
        try {
            String reviewSeries= request.getParameter("reviewSeries") == null ? "" : request.getParameter("reviewSeries");
            PageData pageData = new PageData();
            pageData.put("reviewSeries", reviewSeries);
            List<base_series_professial> list = baseSeriesProfessialService.getRevProByRevSer(pageData);
            ResultUtils.write(response, list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //根据条件获取专业集合 下拉列表 排除已存在的专业
    @RequestMapping(value = "getProfessialByGroup")
    public void getProfessialByGroup(HttpServletRequest request, HttpServletResponse response) {
        logger.info("根据条件获取排除已存在的专业集合");
        try {
            String reviewSeries= request.getParameter("reviewSeries") == null ? "" : request.getParameter("reviewSeries");
            String judgingId = request.getParameter("judgingId");
            String groupId = request.getParameter("groupId");
            PageData pageData = new PageData();
            pageData.put("judgingId", judgingId);
            pageData.put("reviewSeries",reviewSeries);
            pageData.put("groupId",groupId);
            List<base_series_professial> list = baseSeriesProfessialService.getRevProByRevSer(pageData);

            ResultUtils.write(response, list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
