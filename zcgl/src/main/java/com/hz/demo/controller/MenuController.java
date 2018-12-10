package com.hz.demo.controller;

import com.hz.demo.core.*;
import com.hz.demo.model.*;
import com.hz.demo.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

import static com.hz.demo.core.ResultUtils.toJson;

@Controller
@RequestMapping("/Menu")
public class MenuController extends BaseController {
    @Autowired
    UserService userService;
    @Autowired
    TokenService tokenService;
    @Autowired
    MenuService menuService;

    @RequestMapping("/MenuPage.do")
    public String MenuPage(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("MenuBtns",userService.GetMenuBtns(request.getParameter("menuid"),getUser().getUserId()));
        return "sys/menu";
    }

    //根据当前登录用户获取权限菜单
    @RequestMapping(value = "GetMenuByUserID")
    public String GetMenuByUserID(HttpServletRequest request, HttpServletResponse response, Page page) {
        List<sys_menu> menuList = new ArrayList<>();
        if(getUser().getUserType()==1){  //专家用户
            menuList = menuService.GetMenuByUserIDTwo(getUser());
        }else{  //管理员
            menuList = menuService.GetMenuByUserID(getUser().getUserId());
        }
        //page.setData();
        ResultUtils.write(response,getMenuJson("00000000-0000-0000-0000-000000000000", menuList, 0));
        return null;
    }

    //获取菜单树
    @RequestMapping(value = "getMenuTree")
    public void getMenuTree(HttpServletRequest request, HttpServletResponse response) {
        String ParentID = request.getParameter("parentId") == null ? "00000000-0000-0000-0000-000000000000" : request.getParameter("parentId");
        PageData pd = new PageData();
        pd.put("parentId", ParentID);
        String state = request.getParameter("state") == null ? null : request.getParameter("state");
        if (state != null)
            pd.put("state", state);
        List<Tree> menuTreeList = menuService.getMenuTreeList(pd);
        ResultUtils.write(response, ObjectList2TreeJson(ParentID, menuTreeList, 1));
    }

    //获取菜单界面列表
    @RequestMapping(value = "getMenuList")
    public void getMenuList(HttpServletResponse response,Page page) {
        logger.info("显示菜单列表数据");
        PageData pd = getPageData();
        /*pd.put("rows", request.getParameter("rows") == null? 10: Integer.parseInt(request.getParameter("rows")));//每页显示行数
        pd.put("page", request.getParameter("page") == null ? 0 : (Integer.parseInt(request.getParameter("page"))));//每页记录开始位置
        pd.put("parentId", request.getParameter("parentId") == null ? "" : request.getParameter("parentId"));//父ID
        pd.put("menuName", request.getParameter("menuName") == null ? "" : request.getParameter("menuName"));//菜单名称
*/      page.setPd(pd);
        //TableReturn tablereturn = new TableReturn();
        List<sys_menu> menuList = menuService.getMenuList(page);
        //Integer menuListCount = menuService.getMenuCount(pd);
        //page.setTotal(page.getCount());
        page.setRows(menuList);
        //tablereturn.setRows(menuList);
        //tablereturn.setTotal(menuListCount);

        ResultUtils.write(response, toJson(page));
    }

    //更改菜单状态
    @RequestMapping(value = "updateMenuState")
    public void updateMenuState(HttpServletRequest request, HttpServletResponse response) {
        logger.info("更改菜单状态");
        PageData pd = new PageData();
        pd.put("menuId", request.getParameter("menuId") == null ? "" : request.getParameter("menuId"));//菜单ID
        pd.put("state", request.getParameter("state") == null ? "" : request.getParameter("state"));//菜单名称
        Map<String, String> map = new HashMap<>();
        map.put("isOk", "0");
        if (menuService.updateMenu(pd) == 1)
            map.put("isOk", "1");
        ResultUtils.write(response, toJson(map));
    }

    //添加或更新（获取）
    @RequestMapping(value = "AddOrUpdate")
    public void AddOrUpdate(HttpServletRequest request, HttpServletResponse response) {
        logger.info("添加或更新（获取）");
        String menuId = request.getParameter("menuId") == null ? "" : request.getParameter("menuId");
        if (menuId.equals("")) {
            //添加
        } else {
            //更新（获取）
            sys_menu menu = menuService.getMenuByFid(menuId);
            if (menu != null) {
                ResultUtils.write(response, toJson(menu));
            } else {
                ResultUtils.write(response, "记录信息不存在！");
            }
        }

    }

    //保存
    @RequestMapping(value = "Save")
    public void Save(HttpServletRequest request, HttpServletResponse response) {
        logger.info("保存");
        String menuId = request.getParameter("menuId") == null ? "" : request.getParameter("menuId");
        sys_menu menu;
        if (menuId.equals("")) { //添加
            menu = new sys_menu();
            menu.setMenuId(UUID.randomUUID().toString());
            menu.setMenuUrl(request.getParameter("menuUrl"));
            menu.setMenuCode(request.getParameter("menuCode"));
            menu.setMenuName(request.getParameter("menuName"));
            menu.setAddTime(new Date());
            menu.setAddUserId(getUser().getUserId());
            menu.setHasChild(1);
            menu.setLevelPath(request.getParameter("levelPath") + "." + menu.getMenuId());
            menu.setParentId(request.getParameter("parentId"));
            menu.setState(Integer.valueOf(request.getParameter("state") == null ? "0" : request.getParameter("state")));
            menu.setFsort(Integer.valueOf(request.getParameter("fsort")));
            menu.setBackup1(request.getParameter("backup1"));
            PageData pd = new PageData();
            pd.put("menuId", request.getParameter("parentId"));
            pd.put("hasChild", 0);
            if (menuService.updateMenu(pd) == 1) {
                if (menuService.addMenu(menu) == 1)
                    ResultUtils.writeMessage(response, 1, "添加成功");
                else
                    ResultUtils.writeMessage(response, 0, "添加失败");
            } else
                ResultUtils.writeMessage(response, 0, "添加失败");
        } else {//修改
            PageData pd = new PageData();
            pd.put("menuId", menuId);
            pd.put("menuUrl", request.getParameter("menuUrl"));
            pd.put("menuCode", request.getParameter("menuCode"));
            pd.put("menuName", request.getParameter("menuName"));
            pd.put("state", request.getParameter("state") == null ? "0" : request.getParameter("state"));
            pd.put("fsort", request.getParameter("fsort"));
            pd.put("backup1", request.getParameter("backup1"));
            if (menuService.updateMenu(pd) == 1)
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
            if (menuService.deleteMenuByFid(ids) == 1)
                ResultUtils.writeMessage(response, 1, "删除成功");
            else
                ResultUtils.writeMessage(response, 0, "删除失败");
        } catch (Exception ex) {
            ResultUtils.writeMessage(response, 0, ex.getMessage());
        }
    }

    //获取菜单按钮列表
    @RequestMapping(value = "GetListByMenuID")
    public void  GetListByMenuID(HttpServletRequest request, HttpServletResponse response) {
        try {
            logger.info("显示菜单按钮列表数据");
            PageData pd = new PageData();
//            pd.put("rows", request.getParameter("rows") == null ? 10 : Integer.parseInt(request.getParameter("rows")));//每页显示行数
//            pd.put("page", request.getParameter("page") == null ? 0 : (Integer.parseInt(request.getParameter("page")) - 1) * Integer.parseInt(request.getParameter("rows")));//每页记录开始位置
            pd.put("menuId", request.getParameter("menuId") == null ? "" : request.getParameter("menuId"));//菜单ID
            pd.put("state", request.getParameter("state") == null ? "" : request.getParameter("state"));//字典状态
            TableReturn tablereturn = new TableReturn();
            List<Map<String, String>> btnList = menuService.GetListByMenuID(pd);
            ResultUtils.write(response, toJson(btnList));
        } catch (Exception ex) {
            ResultUtils.writeMessage(response, 0, ex.getMessage());
        }
    }

    //菜单绑定按钮
    @RequestMapping(value = "menuBindBtn")
    public void menuBindBtn(HttpServletRequest request, HttpServletResponse response) {
        try {
            String MenuFunID = request.getParameter("MenuFunID").equals("undefined" )? "" : request.getParameter("MenuFunID");
            String State = request.getParameter("state") == null ? "0" : request.getParameter("state");

            sys_menufun menufun =new sys_menufun();
            menufun.setMenuFunId(MenuFunID);
            menufun.setState(Integer.parseInt(State));
            if (State.equals("1")) {
                //绑定
                if (menuService.updateMenuFun(menufun) == 1)
                    ResultUtils.writeMessage(response, 1, "绑定成功");
                else
                    ResultUtils.writeMessage(response, 0, "绑定失败");
            } else {
                //解绑
                if (menuService.updateMenuFun(menufun) == 1)
                    ResultUtils.writeMessage(response, 1, "解绑成功");
                else
                    ResultUtils.writeMessage(response, 0, "解绑失败");
            }

        } catch (Exception ex) {
            ResultUtils.writeMessage(response, 0, ex.getMessage());
        }
    }
    //保存
    @RequestMapping(value = "SaveBtn")
    public void SaveBtn(HttpServletRequest request, HttpServletResponse response) {
        logger.info("保存按钮");
        String menuId = request.getParameter("menuId") == null ? "" : request.getParameter("menuId");
        String menuFunId = request.getParameter("menuFunId") == null ? "" : request.getParameter("menuFunId");
        sys_menufun menufun=new sys_menufun();
        menufun.setMenuId(menuId);
        menufun.setFsort(Integer.parseInt(request.getParameter("fsort")==null?"0":request.getParameter("fsort")));
        menufun.setState(Integer.parseInt(request.getParameter("state")==null?"0":request.getParameter("state")));
        menufun.setBtnName(request.getParameter("btnName"));
        menufun.setBtnCode(request.getParameter("btnCode"));
        if (menuFunId.equals("")) { //添加
            menufun.setMenuFunId(UUID.randomUUID().toString());
                if (menuService.addMenuFun(menufun) == 1)
                    ResultUtils.writeMessage(response, 1, "添加成功");
                else
                    ResultUtils.writeMessage(response, 0, "添加失败");
        } else {//修改
            menufun.setMenuFunId(menuFunId);
            if (menuService.updateMenuFun(menufun) == 1)
                ResultUtils.writeMessage(response, 1, "修改成功");
            else
                ResultUtils.writeMessage(response, 0, "修改失败");
        }

    }
    //添加或更新（获取）_按钮
    @RequestMapping(value = "AddOrUpdateBtn")
    public void AddOrUpdateBtn(HttpServletRequest request, HttpServletResponse response) {
        logger.info("添加或更新（获取）");
        String menuFunId = request.getParameter("menuFunId") == null ? "" : request.getParameter("menuFunId");
        if (menuFunId.equals("")) {
            //添加
        } else {
            //更新（获取）
            sys_menufun menufun = menuService.getMenufunByFid(menuFunId);
            if (menufun != null) {
                ResultUtils.write(response, toJson(menufun));
            } else {
                ResultUtils.write(response, "记录信息不存在！");
            }
        }

    }
    //删除按钮
    @RequestMapping(value = "DeleteBtn")
    public void DeleteBtn(HttpServletRequest request, HttpServletResponse response) {
        String ids = request.getParameter("ids") == null ? "" : request.getParameter("ids");
        try {
            if (menuService.deleteMenuFunByFid(ids) == 1)
                ResultUtils.writeMessage(response, 1, "删除成功");
            else
                ResultUtils.writeMessage(response, 0, "删除失败");
        } catch (Exception ex) {
            ResultUtils.writeMessage(response, 0, ex.getMessage());
        }
    }
}
