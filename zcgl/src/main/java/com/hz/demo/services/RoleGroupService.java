package com.hz.demo.services;


import com.hz.demo.core.PageData;
import com.hz.demo.dao.DaoSupportImpl;
import com.hz.demo.model.sys_rolegroup;
import com.hz.demo.model.sys_user;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service("RoleGroupService")
public class RoleGroupService {

    @Resource(name = "daoSupportImpl")
    private DaoSupportImpl daoSupport;

    //添加角色组信息
    public int add(sys_rolegroup model) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.insert("confing/mappers.sys_rolegroup.insertSelective", model);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }

    // 根据ID更新角色组信息
    public int update(PageData pageData) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.update("confing/mappers.sys_rolegroup.updateByPrimaryKeySelective", pageData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }

    //根据ID删除角色组信息
    public int delete(String id) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.delete("confing/mappers.sys_rolegroup.deleteByPrimaryKey", new BigDecimal(id));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }

    //根据ID查询角色组返回实体信息
    public sys_rolegroup getModel(BigDecimal id) {
        sys_rolegroup rolegroup = null;
        try {
            rolegroup = (sys_rolegroup) daoSupport.findForObject("confing/mappers.sys_rolegroup.selectByPrimaryKey", id);
        } catch (Exception e) {
            e.printStackTrace();
            rolegroup = null;
        }
        return rolegroup;
    }

    //查询所有的角色组信息
    public List<sys_rolegroup> getList(PageData pageData) {
        List<sys_rolegroup> rolegroupList = null;
        try {
            rolegroupList = (List<sys_rolegroup>) daoSupport.findForList("confing/mappers.sys_rolegroup.selectList", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            rolegroupList = null;
        }
        return rolegroupList;
    }

    //查询对应的记录总数
    public Integer getListCount(PageData pageData) {
        Integer ListCount = 0;
        try {
            ListCount = (Integer) daoSupport.findForObject("confing/mappers.sys_rolegroup.selectCount", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            ListCount = 0;
        }
        return ListCount;
    }

    //根据条件查询某个角色组信息
    public sys_rolegroup getUserWhere(PageData pageData) {
        sys_rolegroup rolegroup = null;
        try {
            rolegroup = (sys_rolegroup) daoSupport.findForObject("confing/mappers.sys_rolegroup.getUserWhere", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            rolegroup = null;
        }
        return rolegroup;
    }

    //根据条件查询多个角色组信息
    public List<sys_rolegroup> getUserListWhere(PageData pd) {
        List<sys_rolegroup> rolegroupList = null;
        try {
            rolegroupList = (List<sys_rolegroup>) daoSupport.findForList("confing/mappers.sys_rolegroup.getUserListWhere", pd);
        } catch (Exception e) {
            e.printStackTrace();
            rolegroupList = null;
        }
        return rolegroupList;
    }

    //根据当前角色组的角色记录
    public List<Map<String, String>> GetRoleListByGroup(PageData pageData) {
        List<Map<String, String>> hashMaps;
        BigDecimal id = new BigDecimal(pageData.get("id").toString());
        try {
            sys_rolegroup rolegroup = (sys_rolegroup) daoSupport.findForObject("confing/mappers.sys_rolegroup.selectByPrimaryKey", id);
            hashMaps = (List<Map<String, String>>) daoSupport.findForList("confing/mappers.sys_userrole.selectAllRoleList", pageData);
            if (rolegroup.getRoleid()!=null){
                String roles [] = rolegroup.getRoleid().split(",");
                if (roles.length>0&&roles!=null){
                    for (Map<String,String> hashMap :hashMaps) {
                        hashMap.get("ROLE_ID");
                        for (int i = 0; i < roles.length; i++) {
                            if (hashMap.get("ROLE_ID").equals(roles[i])){
                                hashMap.put("GROUPROLEID",roles[i]);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            hashMaps = null;
        }
        return hashMaps;
    }
    //添加角色组角色
    public int updateRoleId(PageData pageData){
        BigDecimal id = new BigDecimal(pageData.get("id").toString());
        String RoleID = pageData.get("roleid").toString();
        String GroupRoleID = pageData.get("GroupRoleID").toString();
        Integer state = null;
        try{
            sys_rolegroup rolegroup = (sys_rolegroup) daoSupport.findForObject("confing/mappers.sys_rolegroup.selectByPrimaryKey", id);
            //添加权限
            if ("".equals(GroupRoleID)){
                if ("".equals(rolegroup.getRoleid())||rolegroup.getRoleid()==null){
                    RoleID = RoleID;
                }else {
                    RoleID = rolegroup.getRoleid()+","+RoleID;
                }
                pageData.put("roleid",RoleID);
                state = this.update(pageData);
            }else {
                //删除权限
                String roleId = rolegroup.getRoleid().replace(pageData.get("GroupRoleID")+",","");
                roleId = roleId.replace(pageData.get("GroupRoleID").toString(),"");
                if (",".equals(roleId.substring(roleId.length()-1,roleId.length()))){
                    roleId = roleId.substring(0,roleId.length()-1);
                }
                if (",".equals(roleId.substring(0,1))){
                    roleId = roleId.substring(1,roleId.length());
                }
                pageData.put("roleid",roleId);
                state = this.update(pageData);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return state;
    }
}
