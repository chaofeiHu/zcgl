package com.hz.demo.services;

import com.hz.demo.core.PageData;
import com.hz.demo.core.Tree;
import com.hz.demo.dao.DaoSupportImpl;
import com.hz.demo.model.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service("RoleService")
public class RoleService {
    @Resource(name = "daoSupportImpl")
    private DaoSupportImpl daoSupport;

    //添加信息
    public int add(sys_role model) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.insert("confing/mappers.sys_role.insertSelective", model);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }

    // 根据ID更新信息
    public int update(PageData pageData) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.update("confing/mappers.sys_role.updateByPrimaryKeySelective", pageData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }

    //根据ID删除信息
    public int delete(String id) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.delete("confing/mappers.sys_role.deleteByPrimaryKey", id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }

    //根据ID查询返回实体信息
    public sys_role getModel(String id) {
        sys_role model;
        try {
            model = (sys_role) daoSupport.findForObject("confing/mappers.sys_role.selectByPrimaryKey", id);
        } catch (Exception e) {
            e.printStackTrace();
            model = null;
        }
        return model;
    }

    //查询所有的信息
    public List<sys_role> getList(PageData pageData) {
        List<sys_role> modelList;
        try {
            modelList = (List<sys_role>) daoSupport.findForList("confing/mappers.sys_role.selectList", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            modelList = null;
        }
        return modelList;
    }

    //查询所有的信息
    public List<sys_role> getListWhere(PageData pageData) {
        List<sys_role> modelList;
        try {
            modelList = (List<sys_role>) daoSupport.findForList("confing/mappers.sys_role.selectList1", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            modelList = null;
        }
        return modelList;
    }

    //查询对应的记录总数
    public Integer getListCount(PageData pageData) {
        Integer ListCount = 0;
        try {
            ListCount = (Integer) daoSupport.findForObject("confing/mappers.sys_role.selectCount", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            ListCount = 0;
        }
        return ListCount;
    }

    //根据角色查询关联的全部用户记录
    public List<Map<String, String>> GetUserListByRole(PageData pageData) {
        List<Map<String, String>> hashMaps;
        try {
            hashMaps = (List<Map<String, String>>) daoSupport.findForList("confing/mappers.sys_userrole.GetUserListByRole", pageData);

        } catch (Exception e) {
            e.printStackTrace();
            hashMaps = null;
        }
        return hashMaps;
    }

    //根据角色查询关联的全部用户记录个数
    public Integer GetUserListByRoleCount(PageData pageData) {
        Integer ListCount = 0;
        try {
            ListCount = (Integer) daoSupport.findForObject("confing/mappers.sys_userrole.GetUserListByRoleCount", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            ListCount = 0;
        }
        return ListCount;
    }

    //获取角色权限树
    public List<Tree> GetRoleTree(PageData pageData) {
        List<Tree> treeList = new ArrayList<>();
        List<Map<String, String>> hashMaps = new ArrayList<>();
        try {
            hashMaps = (List<Map<String, String>>) daoSupport.findForList("confing/mappers.sys_menufun.GetRoleTree", pageData);
            for (Map<String, String> map : hashMaps) {
                Tree tree = new Tree();
                tree.setId(map.get("ID"));
                tree.setText(map.get("TEXT"));
                tree.setPid(map.get("PID"));
                tree.setChecked(Integer.parseInt(map.get("CHECKED")));
                tree.setAttributes(map.get("ATTRIBUTES"));
                treeList.add(tree);
            }
        } catch (Exception e) {
            e.printStackTrace();
            treeList = null;
        }
        return treeList;
    }

    //保存角色权限
    public int SaveRoleMenuFun(PageData pageData) {
        int iFlag = 0;
        try {
            //清除当前角色的所有权限
            daoSupport.delete("confing/mappers.sys_rolemenufun.deleteRoleByMenuFun", pageData);
            for (String MenuFunID : (String[]) pageData.get("MenuFunIDS")) {//选中集合
                sys_rolemenufun model = new sys_rolemenufun();
                model.setRoleMenuFunId(UUID.randomUUID().toString());
                model.setRoleId(pageData.get("roleId").toString());
                model.setMenuFunId(MenuFunID);
                daoSupport.insert("confing/mappers.sys_rolemenufun.insertSelective", model);
            }
            iFlag = 1;
        } catch (Exception e) {
            e.printStackTrace();
            iFlag = 0;
        }
        return iFlag;
    }

    //根据用户查询关联的全部角色记录 userId page rows
    public List<Map<String, String>> GetRoleListByUser(PageData pageData) {
        List<Map<String, String>> hashMaps;
        try {
            hashMaps = (List<Map<String, String>>) daoSupport.findForList("confing/mappers.sys_userrole.GetRoleListByUser", pageData);

        } catch (Exception e) {
            e.printStackTrace();
            hashMaps = null;
        }
        return hashMaps;
    }

    //根据用户查询关联的全部角色记录个数 userId
    public Integer GetRoleListByUserCount(PageData pageData) {
        Integer ListCount = 0;
        try {
            ListCount = (Integer) daoSupport.findForObject("confing/mappers.sys_userrole.GetRoleListByUserCount", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            ListCount = 0;
        }
        return ListCount;
    }


    //绑定用户角色信息
    public int addUserRole(sys_userrole model) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.insert("confing/mappers.sys_userrole.insertSelective", model);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }

    //删除用户所属角色信息
    public int deleteUserRole(String id) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.delete("confing/mappers.sys_userrole.deleteByPrimaryKey", id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }


    //根据用户查询关联的全部申报块角色 userId
    public List<Map<String, String>> getJudgingRoleByUserId(PageData pageData) {
        List<Map<String, String>> hashMaps;
        try {
            hashMaps = (List<Map<String, String>>) daoSupport.findForList("confing/mappers.sys_userrole.getJudgingRoleByUserId", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            hashMaps = null;
        }
        return hashMaps;
    }


}


