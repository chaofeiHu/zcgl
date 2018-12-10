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

import static com.hz.demo.core.ResultUtils.toDateTimeJson;
import static com.hz.demo.core.ResultUtils.toJson;

@Controller
@RequestMapping("/Role")
public class RoleController extends BaseController {

    @Autowired
    UserService userService;
    @Autowired
    RoleService roleService;

    @RequestMapping(value = "RolePage")
    public String RolePage(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("MenuBtns",userService.GetMenuBtns(request.getParameter("menuid"),getUser().getUserId()));
        return "sys/role";
    }

    //获取界面列表
    @RequestMapping(value = "GetList")
    public void GetList(HttpServletRequest request, HttpServletResponse response) {
        logger.info("显示角色列表数据");
        PageData pd = new PageData();
        pd.put("rows", request.getParameter("rows") == null? 10: Integer.parseInt(request.getParameter("rows")));//每页显示行数
        pd.put("page", request.getParameter("page") == null ? 0 : (Integer.parseInt(request.getParameter("page"))));//每页记录开始位置
        pd.put("roleName", request.getParameter("roleName") == null ? "" : request.getParameter("roleName"));//名称
        TableReturn tablereturn = new TableReturn();
        List<sys_role> list = roleService.getList(pd);
        Integer listCount = roleService.getListCount(pd);
        tablereturn.setRows(list);
        tablereturn.setTotal(listCount);
        ResultUtils.write(response, toDateTimeJson(tablereturn));
    }

    //更改记录状态
    @RequestMapping(value = "UpdateState")
    public void UpdateState(HttpServletRequest request, HttpServletResponse response) {
        logger.info("更改角色状态");
        PageData pd = new PageData();
        pd.put("roleId", request.getParameter("roleId") == null ? "" : request.getParameter("roleId"));//ID
        pd.put("state", request.getParameter("state") == null ? "0" : request.getParameter("state"));//记录状态
        Map<String, String> map = new HashMap<>();
        map.put("isOk", "0");
        if (roleService.update(pd) == 1)
            map.put("isOk", "1");
        ResultUtils.write(response, toJson(map));
    }

    //添加或更新（获取）
    @RequestMapping(value = "AddOrUpdate")
    public void AddOrUpdate(HttpServletRequest request, HttpServletResponse response) {
        logger.info("角色添加或更新（获取）");
        String roleId = request.getParameter("roleId") == null ? "" : request.getParameter("roleId");
        if (roleId.equals("")) {
            //添加
        } else {
            //更新（获取）
            sys_role model = roleService.getModel(roleId);
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
        logger.info("角色保存");
        String roleId = request.getParameter("roleId") == null ? "" : request.getParameter("roleId");
        sys_role model;
        if (roleId.equals("")) { //添加
            model = new sys_role();
            model.setRoleId(UUID.randomUUID().toString());
            model.setRoleName(request.getParameter("roleName"));
            model.setFsort(Integer.valueOf(request.getParameter("fsort")));
            model.setAddTime(new Date());
            model.setAddUserId(getUser().getUserId());
            model.setState(Integer.valueOf(request.getParameter("state") == null ? "0" : request.getParameter("state")));
            model.setBackup1(request.getParameter("backup1"));
            PageData pd = new PageData();
            pd.put("roleName", request.getParameter("roleName"));

            if (roleService.getListCount(pd) == 0) {
                if (roleService.add(model) == 1)
                    ResultUtils.writeMessage(response, 1, "添加成功");
                else
                    ResultUtils.writeMessage(response, 0, "添加失败");
            } else
                ResultUtils.writeMessage(response, 0, "该角色名已经存在");
        } else {//修改
            PageData pd = new PageData();
            pd.put("roleId", roleId);
            pd.put("roleName", request.getParameter("roleName"));
            pd.put("state", request.getParameter("state") == null ? "0" : request.getParameter("state"));
            pd.put("fsort", request.getParameter("fsort"));
            pd.put("backup1", request.getParameter("backup1"));
            if (roleService.update(pd) == 1)
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
            if (roleService.delete(ids) == 1)
                ResultUtils.writeMessage(response, 1, "删除成功");
            else
                ResultUtils.writeMessage(response, 0, "删除失败");
        } catch (Exception ex) {
            ResultUtils.writeMessage(response, 0, ex.getMessage());
        }
    }

    //根据角色获取关联的所有用户
    @RequestMapping(value = "GetUserListByRole")
    public void GetUserListByRole(HttpServletRequest request, HttpServletResponse response) {
        try {
            logger.info("显示角色关联用户列表数据");
            PageData pd = new PageData();
            pd.put("rows", request.getParameter("rows") == null? 10: Integer.parseInt(request.getParameter("rows")));//每页显示行数
            pd.put("page", request.getParameter("page") == null ? 0 : (Integer.parseInt(request.getParameter("page"))));//每页记录开始位置
            pd.put("roleId", request.getParameter("roleId") == null ? "" : request.getParameter("roleId"));

            TableReturn tablereturn = new TableReturn();
            List<Map<String, String>> userList = roleService.GetUserListByRole(pd);
            Integer listCount = roleService.GetUserListByRoleCount(pd);
            tablereturn.setRows(userList);
            tablereturn.setTotal(listCount);

            ResultUtils.write(response, toJson(tablereturn));
        } catch (Exception ex) {
            ResultUtils.writeMessage(response, 0, ex.getMessage());
        }
    }

    //角色权限树
    @RequestMapping(value = "GetRoleTree")
    public void GetRoleTree(HttpServletRequest request, HttpServletResponse response) {
        PageData pd = new PageData();
        pd.put("roleId", request.getParameter("roleId") == null ? "" : request.getParameter("roleId"));
        List<Tree> treeList = roleService.GetRoleTree(pd);
        ResultUtils.write(response, ObjectList2TreeJson("00000000-0000-0000-0000-000000000000", treeList, 0));
    }

    //保存角色选中的权限
    @RequestMapping(value = "SaveRoleMenuFun")
    public void SaveRoleMenuFun(HttpServletRequest request, HttpServletResponse response) {
        PageData pd = new PageData();
        pd.put("roleId", request.getParameter("roleid") == null ? "" : request.getParameter("roleid"));//角色id
        String menufunids = request.getParameter("menufunids") == null ? "" : request.getParameter("menufunids");
        menufunids = menufunids.replace(",,,,,,,,", ",").replace(",,,,,,,", ",").replace(",,,,,,", ",").replace(",,,,,", ",").replace(",,,,", ",").replace(",,,", ",").replace(",,", ",");
        if (menufunids.substring(0, 1).equals(","))
            menufunids = menufunids.substring(1, menufunids.length() - 1);//去掉第一个逗号
        if (menufunids.substring(menufunids.length() - 1).equals(","))
            menufunids = menufunids.substring(0, menufunids.length() - 1);//去掉最后一个逗号
        String[] MenuFunIDS = menufunids.split(",");
        pd.put("MenuFunIDS", MenuFunIDS);

        if (roleService.SaveRoleMenuFun(pd) == 1)
            ResultUtils.writeMessage(response, 1, "角色授权成功");
        else
            ResultUtils.writeMessage(response, 0, "角色授权失败");
    }

    //根据用户获取关联的所有角色
    @RequestMapping(value = "GetRoleListByUser")
    public void GetRoleListByUser(HttpServletRequest request, HttpServletResponse response) {
        try {
            logger.info("显示用户关联角色列表数据");
            PageData pd = new PageData();
            pd.put("rows", request.getParameter("rows") == null? 10: Integer.parseInt(request.getParameter("rows")));//每页显示行数
            pd.put("page", request.getParameter("page") == null ? 0 : (Integer.parseInt(request.getParameter("page"))));//每页记录开始位置
            pd.put("userId", request.getParameter("userId") == null ? "" : request.getParameter("userId"));
            pd.put("roleGroupCode", getRoleGroupCode());
            TableReturn tablereturn = new TableReturn();
            List<Map<String, String>> userList = roleService.GetRoleListByUser(pd);
            Integer listCount = roleService.GetRoleListByUserCount(pd);
            tablereturn.setRows(userList);
            tablereturn.setTotal(listCount);

            ResultUtils.write(response, toJson(tablereturn));
        } catch (Exception ex) {
            ResultUtils.writeMessage(response, 0, ex.getMessage());
        }
    }

    //绑定用户对应的角色信息
    @RequestMapping(value = "SaveUserRole")
    public void SaveUserRole(HttpServletRequest request, HttpServletResponse response) {
        logger.info("绑定用户对应的角色信息");
        try
        {
            String UserRoleID = request.getParameter("UserRoleID") == null ? "" : request.getParameter("UserRoleID");
            String UserID = request.getParameter("UserID") == null ? "" : request.getParameter("UserID");
            String RoleID = request.getParameter("RoleID") == null ? "" : request.getParameter("RoleID");
            sys_userrole model;
            if (UserRoleID.equals(""))
            {
                //添加
                model = new sys_userrole();
                model.setUserRoleId(UUID.randomUUID().toString());
                model.setRoleId(RoleID);
                model.setUserId(UserID);
                roleService.addUserRole(model);
            }
            else
            {
                //删除
                roleService.deleteUserRole(UserRoleID);
            }
            ResultUtils.writeMessage(response, 1, "成功");
        }
        catch (Exception ex)
        {
            ResultUtils.writeMessage(response, 0, ex.getMessage());
        }
    }

    //根据用户查询关联的全部申报块角色
    @RequestMapping(value = "getJudgingRoleByUserId")
    public void getJudgingRoleByUserId(HttpServletRequest request, HttpServletResponse response) {
        try {
            logger.info("显示用户关联的全部申报块角色");
            PageData pd = new PageData();
            pd.put("userId", getUser().getUserId());
            List<Map<String, String>> userList = roleService.getJudgingRoleByUserId(pd);

            ResultUtils.write(response, toJson(userList));
        } catch (Exception ex) {
            ResultUtils.writeMessage(response, 0, ex.getMessage());
        }
    }

}