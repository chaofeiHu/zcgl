package com.hz.demo.controller;

import com.hz.demo.core.PageData;
import com.hz.demo.core.ResultUtils;
import com.hz.demo.core.TableReturn;
import com.hz.demo.core.Tree;
import com.hz.demo.model.*;
import com.hz.demo.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

import static com.hz.demo.core.ResultUtils.toJson;

@Controller
@RequestMapping("/Dept")
public class DeptController extends BaseController{

    @Autowired
    UserService userService;
    @Autowired
    DeptService deptService;

    @RequestMapping(value = "DeptPage")
    public String DeptPage(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("MenuBtns",userService.GetMenuBtns(request.getParameter("menuid"),getUser().getUserId()));
        return "sys/dept";
    }

    //获取部门树
    @RequestMapping(value = "getTree")
    public void getTree(HttpServletRequest request, HttpServletResponse response) {
        String ParentID = request.getParameter("parentId") == null ? "00000000-0000-0000-0000-000000000000" : request.getParameter("parentId");
        PageData pd = new PageData();
        pd.put("parentId", ParentID);
        String state = request.getParameter("state") == null ? null : request.getParameter("state");
        if (state != null)
            pd.put("state", state);
        List<Tree> treeList = deptService.getTreeList(pd);
        ResultUtils.write(response, ObjectList2TreeJson(ParentID, treeList, 1));
    }

    //获取界面列表
    @RequestMapping(value = "getList")
    public void getList(HttpServletRequest request, HttpServletResponse response) {
        logger.info("显示部门列表数据");
        PageData pd = new PageData();
        pd.put("rows", request.getParameter("rows") == null? 10: Integer.parseInt(request.getParameter("rows")));//每页显示行数
        pd.put("page", request.getParameter("page") == null ? 0 : (Integer.parseInt(request.getParameter("page"))));//每页记录开始位置
        pd.put("parentId", request.getParameter("parentId") == null ? "" : request.getParameter("parentId"));//父ID
        pd.put("deptName", request.getParameter("deptName") == null ? "" : request.getParameter("deptName"));//名称

        TableReturn tablereturn = new TableReturn();
        List<sys_dept> list = deptService.getList(pd);
        Integer listCount = deptService.getListCount(pd);
        tablereturn.setRows(list);
        tablereturn.setTotal(listCount);

        ResultUtils.write(response, toJson(tablereturn));
    }

    //更改部门状态
    @RequestMapping(value = "updateState")
    public void updateState(HttpServletRequest request, HttpServletResponse response) {
        logger.info("更改部门状态");
        PageData pd = new PageData();
        pd.put("deptId", request.getParameter("deptId") == null ? "" : request.getParameter("deptId"));//ID
        pd.put("state", request.getParameter("state") == null ? "0" : request.getParameter("state"));//记录状态
        Map<String, String> map = new HashMap<>();
        map.put("isOk", "0");
        if (deptService.update(pd) == 1)
            map.put("isOk", "1");
        ResultUtils.write(response, toJson(map));
    }

    //添加或更新（获取）
    @RequestMapping(value = "AddOrUpdate")
    public void AddOrUpdate(HttpServletRequest request, HttpServletResponse response) {
        logger.info("部门添加或更新（获取）");
        String deptId = request.getParameter("deptId") == null ? "" : request.getParameter("deptId");
        if (deptId.equals("")) {
            //添加
        } else {
            //更新（获取）
            sys_dept model = deptService.getModel(deptId);
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
        String deptId = request.getParameter("deptId") == null ? "" : request.getParameter("deptId");
        sys_dept model = null;
        if (deptId.equals("")) { //添加
            model = new sys_dept();
            model.setDeptId(UUID.randomUUID().toString());
            model.setDeptCode(request.getParameter("deptCode"));
            model.setDeptName(request.getParameter("deptName"));
            model.setAddTime(new Date());
            model.setAddUserId(getUser().getUserId());
            model.setLevelPath(request.getParameter("levelPath") + "." + model.getDeptId());
            model.setParentId(request.getParameter("parentId"));
            model.setState(Integer.valueOf(request.getParameter("state") == null ? "0" : request.getParameter("state")));
            model.setFsort(Integer.valueOf(request.getParameter("fsort")));
            model.setBackup1(request.getParameter("backup1"));
            if (deptService.add(model) == 1)
                ResultUtils.writeMessage(response, 1, "添加成功");
            else
                ResultUtils.writeMessage(response, 0, "添加失败");

        } else {//修改
            PageData pd = new PageData();
            pd.put("deptId", deptId);
            pd.put("deptName", request.getParameter("deptName"));
            pd.put("deptCode", request.getParameter("deptCode"));
            pd.put("state", request.getParameter("state") == null ? "0" : request.getParameter("state"));
            pd.put("fsort", request.getParameter("fsort"));
            pd.put("backup1", request.getParameter("backup1"));
            if (deptService.update(pd) == 1)
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
            if (deptService.delete(ids) == 1)
                ResultUtils.writeMessage(response, 1, "删除成功");
            else
                ResultUtils.writeMessage(response, 0, "删除失败");
        } catch (Exception ex) {
            ResultUtils.writeMessage(response, 0, ex.getMessage());
        }
    }
}
