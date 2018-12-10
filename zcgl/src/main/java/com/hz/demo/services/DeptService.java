package com.hz.demo.services;

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

@Service("DeptService")
public class DeptService {
    @Resource(name = "daoSupportImpl")
    private DaoSupportImpl daoSupport;

    //添加信息
    public int add(sys_dept model) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.insert("confing/mappers.sys_dept.insertSelective", model);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }

    // 根据ID更新信息
    public int update(PageData pageData) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.update("confing/mappers.sys_dept.updateByPrimaryKeySelective", pageData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }

    //根据ID删除信息
    public int delete(String id) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.delete("confing/mappers.sys_dept.deleteByPrimaryKey", id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }

    //根据ID查询返回实体信息
    public sys_dept getModel(String id) {
        sys_dept model = null;
        try {
            model = (sys_dept) daoSupport.findForObject("confing/mappers.sys_dept.selectByPrimaryKey", id);
        } catch (Exception e) {
            e.printStackTrace();
            model = null;
        }
        return model;
    }

    //查询所有的信息
    public List<sys_dept> getList(PageData pageData) {
        List<sys_dept> modelList = null;
        try {
            modelList = (List<sys_dept>) daoSupport.findForList("confing/mappers.sys_dept.selectList", pageData);
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
            ListCount = (Integer) daoSupport.findForObject("confing/mappers.sys_dept.selectCount", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            ListCount = 0;
        }
        return ListCount;
    }

    //部门树
    public List<Tree> getTreeList(PageData pageData) {
        List<Tree> treeList = new ArrayList<>();
        List<Map<String, String>> hashMaps = new ArrayList<>();
        try {
            hashMaps = (List<Map<String, String>>) daoSupport.findForList("confing/mappers.sys_dept.getTreeList", pageData);
            for (Map<String, String> map : hashMaps) {
                Tree tree = new Tree();
                tree.setId(map.get("id"));
                tree.setText(map.get("text"));
                tree.setPid(map.get("pid"));
                tree.setAttributes(map.get("attributes"));
                tree.setId(map.get("ID"));
                tree.setText(map.get("TEXT"));
                tree.setPid(map.get("PID"));
                tree.setAttributes(map.get("ATTRIBUTES"));
                treeList.add(tree);
            }
        } catch (Exception e) {
            e.printStackTrace();
            treeList = null;
        }
        return treeList;
    }
}
