package com.hz.demo.controller;

import com.hz.demo.core.PageData;
import com.hz.demo.core.ResultUtils;
import com.hz.demo.core.TableReturn;
import com.hz.demo.model.*;
import com.hz.demo.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;

import static com.hz.demo.core.EncodeUtil.encodeMD5;
import static com.hz.demo.core.ResultUtils.toJson;

@Controller
@RequestMapping("/RoleGroup")
public class RoleGroupController extends BaseController {

   @Autowired
    BaseUnitService baseUnitService;
    @Autowired
    TokenService tokenService;
    @Autowired
    MenuService menuService;
    @Autowired
    UserService userService;
    @Autowired
    RoleGroupService roleGroupService;
    @Autowired
    RoleService roleService;

    //跳转到主页
    @RequestMapping("/index.do")
    public String baseIndex() {
        return "index";
    }

    //跳转到登陆页面
    @RequestMapping("/baseLogin.do")
    public String baseLogin() {
        return "login";
    }

    //跳转到角色组页面
    @RequestMapping(value = "RoleGroupPage")
    public String UserPage(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("MenuBtns",userService.GetMenuBtns(request.getParameter("menuid"),getUser().getUserId()));
        return "sys/rolegroup";
    }

    //获取界面列表
    @RequestMapping(value = "getList")
    public void getList(HttpServletRequest request, HttpServletResponse response) {
        logger.info("显示角色组列表数据");
        PageData pd = new PageData();
        pd.put("rows", request.getParameter("rows") == null? 10: Integer.parseInt(request.getParameter("rows")));//每页显示行数
        pd.put("page", request.getParameter("page") == null ? 0 : (Integer.parseInt(request.getParameter("page"))));//每页记录开始位置
        pd.put("roleGroupname", request.getParameter("roleGroupname") == null ? "" : request.getParameter("roleGroupname"));//角色组名称

        TableReturn tablereturn = new TableReturn();
        List<sys_rolegroup> list = roleGroupService.getList(pd);
        Integer listCount = roleGroupService.getListCount(pd);
        tablereturn.setRows(list);
        tablereturn.setTotal(listCount);
        ResultUtils.write(response, toJson(tablereturn));
    }


    //添加或更新（获取）
    @RequestMapping(value = "AddOrUpdate")
    public void AddOrUpdate(HttpServletRequest request, HttpServletResponse response) {
        logger.info("角色组添加或更新（获取）");
        String id = request.getParameter("id") == null ? "" : request.getParameter("id");
        if (id.equals("")) {
            //添加
        } else {
            //更新（获取）
            sys_rolegroup model = roleGroupService.getModel(new BigDecimal(id));
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
        sys_rolegroup model = null;
        if (id.equals("")) { //添加
            model = new sys_rolegroup();
            model.setRoleGroupcode(request.getParameter("roleGroupcode"));
            model.setRoleGroupname(request.getParameter("roleGroupname"));
            model.setBack1(request.getParameter("back1"));
            if (roleGroupService.add(model) == 1)
                ResultUtils.writeMessage(response, 1, "添加成功");
            else
                ResultUtils.writeMessage(response, 0, "添加失败");
        } else {//修改
            PageData pd = new PageData();
            pd.put("id",new BigDecimal(id));
            pd.put("roleGroupcode", request.getParameter("roleGroupcode"));
            pd.put("roleGroupname", request.getParameter("roleGroupname"));
            pd.put("back1", request.getParameter("back1"));
            if (roleGroupService.update(pd) == 1)
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
            if (roleGroupService.delete(ids) == 1)
                ResultUtils.writeMessage(response, 1, "删除成功");
            else
                ResultUtils.writeMessage(response, 0, "删除失败");

        } catch (Exception ex) {
            ResultUtils.writeMessage(response, 0, ex.getMessage());
        }
    }


    //获取角色组关联的所有角色
    @RequestMapping(value = "GetRoleListByGroup")
    public void GetRoleListByGroup(HttpServletRequest request, HttpServletResponse response) {
        try {
            logger.info("显示角色组关联角色列表数据");
            PageData pd = new PageData();
            pd.put("rows", request.getParameter("rows") == null? 10: Integer.parseInt(request.getParameter("rows")));//每页显示行数
            pd.put("page", request.getParameter("page") == null ? 0 : (Integer.parseInt(request.getParameter("page"))));//每页记录开始位置
            pd.put("id", request.getParameter("id") == null ? "" : request.getParameter("id"));

            TableReturn tablereturn = new TableReturn();
            List<Map<String, String>> roleList = roleGroupService.GetRoleListByGroup(pd);
            Integer listCount = roleService.getListCount(pd);
            tablereturn.setRows(roleList);
            tablereturn.setTotal(listCount);
            ResultUtils.write(response, toJson(tablereturn));
        } catch (Exception ex) {
            ResultUtils.writeMessage(response, 0, ex.getMessage());
        }
    }

    //绑定角色组对应的角色信息
    @RequestMapping(value = "SaveGroupRole")
    public void SaveGroupRole(HttpServletRequest request, HttpServletResponse response) {
        logger.info("绑定角色组对应的角色信息");
        try
        {
            String GroupRoleID = request.getParameter("GroupRoleID") == null ? "" : request.getParameter("GroupRoleID");
            String RoleID = request.getParameter("RoleID") == null ? "" : request.getParameter("RoleID");
            String id = request.getParameter("id") == null ? "" : request.getParameter("id");
            sys_userrole model;
            PageData pageData = new PageData();
            pageData.put("id",id);
            pageData.put("roleid",RoleID);
            pageData.put("GroupRoleID",GroupRoleID);
            roleGroupService.updateRoleId(pageData);
            ResultUtils.writeMessage(response, 1, "成功");
        }
        catch (Exception ex)
        {
            ResultUtils.writeMessage(response, 0, ex.getMessage());
        }
    }
}