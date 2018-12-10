package com.hz.demo.controller;

import com.hz.demo.core.PageData;
import com.hz.demo.core.ResultUtils;
import com.hz.demo.core.TableReturn;
import com.hz.demo.core.Tree;
import com.hz.demo.model.sys_area;
import com.hz.demo.services.AreaService;
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
@RequestMapping("/Area")
public class AreaController extends BaseController{
    @Autowired
    UserService userService;
    @Autowired
    AreaService areaService;
    @RequestMapping(value = "AreaPage")
    public String DeptPage(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("MenuBtns",userService.GetMenuBtns(request.getParameter("menuid"),getUser().getUserId()));
        return "sys/area";
    }

    //获取行政编码树
    @RequestMapping(value = "getAreaTree")
    public void getDictTree(HttpServletRequest request, HttpServletResponse response) {
        String areaParentcode = request.getParameter("areaParentcode") == null ? "410000" : request.getParameter("areaParentcode");
        PageData pd = new PageData();
        pd.put("areaParentcode", areaParentcode);
        String state = request.getParameter("state") == null ? null : request.getParameter("state");
        if (state != null)
            pd.put("state", state);
        List<Tree> treeList = areaService.getTreeList(pd);
        ResultUtils.write(response, ObjectList2TreeJson(areaParentcode, treeList, 1));
    }

    //获取行政编码界面列表
    @RequestMapping(value = "getAreaList")
    public void getDictList(HttpServletRequest request, HttpServletResponse response) {
        logger.info("显示行政区域编码列表数据");
        PageData pd = new PageData();
        pd.put("rows", request.getParameter("rows") == null? 10: Integer.parseInt(request.getParameter("rows")));//每页显示行数
        pd.put("page", request.getParameter("page") == null ? 0 : (Integer.parseInt(request.getParameter("page"))));//每页记录开始位置
        pd.put("areaParentcode", request.getParameter("areaParentcode") == null ? "" : request.getParameter("areaParentcode"));//父ID
        pd.put("areaName", request.getParameter("areaName") == null ? "" : request.getParameter("areaName"));//名称

        TableReturn tablereturn = new TableReturn();
        List<sys_area> list = areaService.getList(pd);
        Integer listCount = areaService.getListCount(pd);
        tablereturn.setRows(list);
        tablereturn.setTotal(listCount);
        ResultUtils.write(response, toJson(tablereturn));
    }

    //更改行政编码状态
    @RequestMapping(value = "updateState")
    public void updateState(HttpServletRequest request, HttpServletResponse response) {
        logger.info("更改行政编码状态");
        PageData pd = new PageData();
        pd.put("id", request.getParameter("id") == null ? "" : request.getParameter("id"));//ID
        pd.put("state", request.getParameter("state") == null ? "0" : request.getParameter("state"));//记录状态
        Map<String, String> map = new HashMap<>();
        map.put("isOk", "0");
        if (dictService.update(pd) == 1)
            map.put("isOk", "1");
        ResultUtils.write(response, toJson(map));
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
            sys_area model = areaService.getModel(id);
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
        sys_area model = null;
        if (id.equals("")) { //添加
            model = new sys_area();
           // model.setId("id");
            model.setAreaCode(request.getParameter("areaCode"));
            model.setAreaName(request.getParameter("areaName"));
            model.setAreaGrade((Integer.parseInt(request.getParameter("areaGrade"))+1)+"");
            model.setAddtime(new Date());
            model.setAdduserid(getUser().getUserId());
            model.setAreaParentcode(request.getParameter("areaParentcode"));
            model.setState(BigDecimal.valueOf(Long.parseLong(request.getParameter("state") == null ? "0" : request.getParameter("state"))));
            model.setFsort(BigDecimal.valueOf(Long.parseLong(request.getParameter("fsort"))));
            model.setBack1(request.getParameter("backup1"));

            PageData pd = new PageData();
            pd.put("areaCode", request.getParameter("areaParentcode"));
            if (areaService.update(pd) == 0) {
                if (areaService.add(model) == 1)
                    ResultUtils.writeMessage(response, 1, "添加成功");
                else
                    ResultUtils.writeMessage(response, 0, "添加失败");
            } else
                ResultUtils.writeMessage(response, 0, "添加失败");
        } else {//修改
            PageData pd = new PageData();
            pd.put("id",id);
            pd.put("areaCode", request.getParameter("areaCode"));
            pd.put("areaName", request.getParameter("areaName"));
            pd.put("state", request.getParameter("state") == null ? "0" : request.getParameter("state"));
            pd.put("fsort", request.getParameter("fsort"));
            pd.put("backup1", request.getParameter("backup1"));
            if (areaService.update(pd) == 1)
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
            if (areaService.delete(ids) == 1)
                ResultUtils.writeMessage(response, 1, "删除成功");
            else
                ResultUtils.writeMessage(response, 0, "删除失败");
        } catch (Exception ex) {
            ResultUtils.writeMessage(response, 0, ex.getMessage());
        }
    }




}
