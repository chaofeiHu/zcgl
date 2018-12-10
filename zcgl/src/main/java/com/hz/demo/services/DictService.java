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

@Service("DictService")
public class DictService {
    @Resource(name = "daoSupportImpl")
    private DaoSupportImpl daoSupport;


    //添加信息
    public int add(sys_dict model) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.insert("confing/mappers.sys_dict.insertSelective", model);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }

    // 根据ID更新信息
    public int update(PageData pageData) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.update("confing/mappers.sys_dict.updateByPrimaryKeySelective", pageData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }

    //根据ID删除信息
    public int delete(String id) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.delete("confing/mappers.sys_dict.deleteByPrimaryKey", id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }

    //根据条件查询返回实体信息
    public sys_dict getDictWhere(PageData pageData) {
        sys_dict model = null;
        try {
            model = (sys_dict) daoSupport.findForObject("confing/mappers.sys_dict.getDictWhere", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            model = null;
        }
        return model;
    }

    //查询所有的信息
    public List<sys_dict> getList(PageData pageData) {
        List<sys_dict> modelList = null;
        try {
            modelList = (List<sys_dict>) daoSupport.findForList("confing/mappers.sys_dict.selectList", pageData);
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
            ListCount = (Integer) daoSupport.findForObject("confing/mappers.sys_dict.selectCount", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            ListCount = 0;
        }
        return ListCount;
    }

    //根据ID查询返回实体信息
    public sys_dict getModel(String id) {
        sys_dict model = null;
        try {
            model = (sys_dict) daoSupport.findForObject("confing/mappers.sys_dict.selectByPrimaryKey", id);
        } catch (Exception e) {
            e.printStackTrace();
            model = null;
        }
        return model;
    }
    public List<Tree> recProDictTree(PageData pageData) {
        List<Tree> treeList = new ArrayList<>();
        List<PageData> pditem = new ArrayList<PageData>();
        try {
            pditem = (List<PageData>) daoSupport.findForList("confing/mappers.sys_dict.recProDictTree", pageData);
            for (PageData map : pditem) {
                Tree tree = new Tree();
                tree.setId(map.getString("ID"));
                tree.setText(map.getString("TEXT"));
                if(map.containsKey("PID")){
                    tree.setPid(map.getString("PID"));
                }else {
                    tree.setPid("");
                }
                treeList.add(tree);
            }
        } catch (Exception e) {
            e.printStackTrace();
            treeList = null;
        }
        return treeList;
    }

    //信息树
    public List<Tree> getTreeList(PageData pageData) {
        List<Tree> treeList = new ArrayList<>();
        List<Map<String, String>> hashMaps = new ArrayList<>();
        try {
            hashMaps = (List<Map<String, String>>) daoSupport.findForList("confing/mappers.sys_dict.getTreeList", pageData);
            for (Map<String, String> map : hashMaps) {
                  Tree tree = new Tree();
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

    public List<sys_dict> getDictList(PageData pageData) {
        List<sys_dict> dictList = null;
        try {
            dictList = (List<sys_dict>) daoSupport.findForList("confing/mappers.sys_dict.selectByGroupName", pageData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dictList;
    }
    //查询评审系列
    public List<sys_dict> getDictSeies(PageData pageData){
        List<sys_dict> dictSeiesList = null;
        try {
            dictSeiesList = (List<sys_dict>) daoSupport.findForList("confing/mappers.sys_dict.getDictSeies", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            dictSeiesList = null;
        }
        return dictSeiesList;
    }
    //查询评审系列
    public List<sys_dict> getDictSeiesCode(PageData pageData){
        List<sys_dict> dictSeiesList = null;
        try {
            dictSeiesList = (List<sys_dict>) daoSupport.findForList("confing/mappers.sys_dict.getDictSeiesCode", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            dictSeiesList = null;
        }
        return dictSeiesList;
    }
    //查询对应的记录总数
    public Integer getSeiesListCount(PageData pageData) {
        Integer ListCount = 0;
        try {
            ListCount = (Integer) daoSupport.findForObject("confing/mappers.sys_dict.selectSeiesCount", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            ListCount = 0;
        }
        return ListCount;
    }

    //根据权限获取评审内容
    public List<sys_dict> getDictByRole(PageData pageData){
        List<sys_dict> dictList = null;
        try {
            dictList = (List<sys_dict>) daoSupport.findForList("confing/mappers.sys_dict.getDictByRole", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            dictList = null;
        }
        return dictList;
    }
    //获取所有专家需审批内容
    public List<sys_dict> getCreateSpecialityContent(){
        List<sys_dict> dictList = null;
        try {
            dictList = (List<sys_dict>) daoSupport.findForList("confing/mappers.sys_dict.getCreateSpecialityContent",null);
        } catch (Exception e) {
            e.printStackTrace();
            dictList = null;
        }
        return dictList;
    }





}
