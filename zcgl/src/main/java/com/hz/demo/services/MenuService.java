package com.hz.demo.services;

import com.hz.demo.core.Page;
import com.hz.demo.core.PageData;
import com.hz.demo.core.Tree;
import com.hz.demo.dao.DaoSupportImpl;
import com.hz.demo.model.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("MenuService")
public class MenuService {
    @Resource(name = "daoSupportImpl")
    private DaoSupportImpl daoSupport;


    //添加菜单信息
    public int addMenu(sys_menu menu) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.insert("confing/mappers.sys_menu.insertSelective", menu);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }

    // 根据ID更新菜单信息
    public int updateMenu(PageData pageData) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.update("confing/mappers.sys_menu.updateByPrimaryKeySelective", pageData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }

    //根据ID删除菜单信息
    public int deleteMenuByFid(String id) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.delete("confing/mappers.sys_menu.deleteByPrimaryKey", id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }

    //根据ID查询菜单返回实体信息
    public sys_menu getMenuByFid(String id) {
        sys_menu menu = null;
        try {
            menu = (sys_menu) daoSupport.findForObject("confing/mappers.sys_menu.selectByPrimaryKey", id);
        } catch (Exception e) {
            e.printStackTrace();
            menu = null;
        }
        return menu;
    }

    //查询所有的菜单信息
    public List<sys_menu> getMenuList(Page pageData) {
        List<sys_menu> menuList = null;
        try {
            menuList = (List<sys_menu>) daoSupport.findForList("confing/mappers.sys_menu.selectlistPage", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            menuList = null;
        }
        return menuList;
    }

    //查询对应的菜单记录总数
    public Integer getMenuCount(PageData pageData) {
        Integer ListCount = 0;
        try {
            ListCount = (Integer) daoSupport.findForObject("confing/mappers.sys_menu.selectCount", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            ListCount = 0;
        }
        return ListCount;
    }

    //根据条件查询某个菜单按钮授权信息
    public List<sys_menu> GetMenuByUserID(String userId) {
        List<sys_menu> menuList = null;
        try {
            menuList = (List<sys_menu>) daoSupport.findForList("confing/mappers.sys_menu.GetMenuByUserID", userId);
        } catch (Exception e) {
            e.printStackTrace();
            menuList = null;
        }
        return menuList;
    }
    //根据条件查询某个菜单按钮授权信息
    public List<sys_menu> GetMenuByUserIDTwo(sys_user user) {
        List<sys_menu> menuList = null;
        try {
            List<Integer> roleId=new ArrayList<Integer>();
            base_judging_process  judging = (base_judging_process) daoSupport.findForObject("confing/mappers.base_judging_process.selectByPrimaryKey", user.getJudgingCode()); //获取评委会信息
            PageData pageData=new PageData();
            pageData.put("JUDGING_CODE",user.getJudgingCode());
            pageData.put("SPECIALITY_ID",user.getSpecialityId());
            PageData  engage = (PageData) daoSupport.findForObject("confing/mappers.base_engage.selectEngageSpeciality", pageData); //获取专家角色
            if(engage!=null){
                if(judging.getProcessGroup()==1){  //分学科组
                    if(engage.getString("JURY_DUTY").equals("1")){
                        roleId.add(9003); }
                    else{
                        roleId.add(9002); }
                }else{
                    if(engage.getString("JURY_DUTY").equals("1")){
                        roleId.add(9007);
                    }else{
                        roleId.add(9006);
                    }
                }
            }
            List<PageData>  group = (List<PageData>) daoSupport.findForList("confing/mappers.base_engage.selectGroupSpeciality",pageData); //获取专家角色
            if(group.size()>0){
                for(PageData pd:group){
                    if(pd.getString("GROUP_LEADER").equals("0")){ //组长
                        roleId.add(9004);
                    }else{
                        roleId.add(9005);
                    }
                }
            }
            menuList = (List<sys_menu>) daoSupport.findForList("confing/mappers.sys_menu.GetMenuByUserIDTwo", roleId);
        } catch (Exception e) {
            e.printStackTrace();
            menuList = null;
        }
        return menuList;
    }



    //菜单信息树
    public List<Tree> getMenuTreeList(PageData pageData) {
        List<Tree> menuTreeList = new ArrayList<>();
        List<Map<String, String>> hashMaps = new ArrayList<>();
        try {
            hashMaps = (List<Map<String, String>>) daoSupport.findForList("confing/mappers.sys_menu.getMenuTreeList", pageData);
            for (Map<String, String> map : hashMaps) {
                Tree tree = new Tree();
                tree.setId(map.get("ID"));
                tree.setText(map.get("TEXT"));
                tree.setPid(map.get("PID"));
                tree.setAttributes(map.get("ATTRIBUTES"));
                menuTreeList.add(tree);
            }
        } catch (Exception e) {
            e.printStackTrace();
            menuTreeList = null;
        }
        return menuTreeList;
    }


    //获取菜单绑定的按钮信息
    public List<Map<String, String>> GetListByMenuID(PageData pageData) {
        List<Map<String, String>> hashMaps = null;
        try {
            hashMaps = (List<Map<String, String>>) daoSupport.findForList("confing/mappers.sys_menufun.GetListByMenuID", pageData);

        } catch (Exception e) {
            e.printStackTrace();
            hashMaps = null;
        }
        return hashMaps;
    }

    //菜单增加按钮
    public int addMenuFun(sys_menufun menuFun) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.insert("confing/mappers.sys_menufun.insertSelective", menuFun);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }
    //菜单修改按钮
    public int updateMenuFun(sys_menufun menuFun) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.insert("confing/mappers.sys_menufun.updateByPrimaryKeySelective", menuFun);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }

    //菜单删除按钮
    public int deleteMenuFunByFid(String menuFunId) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.delete("confing/mappers.sys_menufun.deleteByPrimaryKey", menuFunId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }


    //根据当前登录用户获取具体菜单按钮权限
    public String GetMenuFun(PageData pageData)
    {
        String strResult = "";
        List<Map<String, String>> hashMaps = null;
        try {
            hashMaps = (List<Map<String, String>>) daoSupport.findForList("confing/mappers.sys_menufun.GetMenuFun", pageData);
            for (Map<String, String> map : hashMaps) {
                strResult+=map.get("BTNCODE").toLowerCase()+",";
            }
        } catch (Exception e) {
            e.printStackTrace();
            strResult = "";
        }
        return strResult;
    }
    //根据ID查询按钮返回实体信息
    public sys_menufun getMenufunByFid(String id) {
        sys_menufun menufun = null;
        try {
            menufun = (sys_menufun) daoSupport.findForObject("confing/mappers.sys_menufun.selectByPrimaryKey", id);
        } catch (Exception e) {
            e.printStackTrace();
            menufun = null;
        }
        return menufun;
    }

}
