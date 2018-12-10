package com.hz.demo.services;


import com.hz.demo.core.PageData;
import com.hz.demo.core.Tree;
import com.hz.demo.dao.DaoSupportImpl;
import com.hz.demo.model.sys_area;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("AreaService")
public class AreaService {

    @Resource(name = "daoSupportImpl")
    private DaoSupportImpl daoSupport;

    //添加地区信息
    public int add(sys_area model) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.insert("confing/mappers.sys_area.insertSelective", model);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }

    // 根据ID更新地区信息
    public int update(PageData pageData) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.update("confing/mappers.sys_area.updateByPrimaryKeySelective", pageData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }

    //根据ID删除地区信息
    public int delete(String id) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.delete("confing/mappers.sys_area.deleteByPrimaryKey", Integer.parseInt(id));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }

    //根据ID查询用户返回实体信息
    public sys_area getModel(String id) {
        sys_area sysArea = null;
        try {
            sysArea = (sys_area) daoSupport.findForObject("confing/mappers.sys_area.selectByPrimaryKey", Integer.parseInt(id));
        } catch (Exception e) {
            e.printStackTrace();
            sysArea = null;
        }
        return sysArea;
    }

    //查询所有的地区信息不分页
    public List<sys_area> getListWhere(PageData pageData) {
        List<sys_area> areaList = null;
        try {
            areaList = (List<sys_area>) daoSupport.findForList("confing/mappers.sys_area.selectList1", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            areaList = null;
        }
        return areaList;
    }

    //查询所有的地区信息
    public List<sys_area> getList(PageData pageData) {
        List<sys_area> areaList = null;
        try {
            areaList = (List<sys_area>) daoSupport.findForList("confing/mappers.sys_area.selectList", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            areaList = null;
        }
        return areaList;
    }

    //查询对应的记录总数
    public Integer getListCount(PageData pageData) {
        Integer ListCount = 0;
        try {
            ListCount = (Integer) daoSupport.findForObject("confing/mappers.sys_area.selectCount", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            ListCount = 0;
        }
        return ListCount;
    }
    

    //信息树
    public List<Tree> getTreeList(PageData pageData) {
        List<Tree> treeList = new ArrayList<>();
        List<Map<String, String>> hashMaps = new ArrayList<>();
        try {
            hashMaps = (List<Map<String, String>>) daoSupport.findForList("confing/mappers.sys_area.getTreeList", pageData);
            for (Map<String, String> map : hashMaps) {
                Tree tree = new Tree();
//                tree.setId(map.get("id"));
//                tree.setText(map.get("text"));
//                tree.setPid(map.get("pid"));
//                tree.setAttributes(map.get("attributes"));
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

    //根据条件查询某个地区信息
    public List<sys_area> getAreaWhere(PageData pageData) {
        List<sys_area> areaList = null;
        try {
            areaList = (List<sys_area>) daoSupport.findForList("confing/mappers.sys_area.getAreaWhere", pageData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return areaList;
    }

    //根据区域等级查询出该等级下所有地区数据
    public List<sys_area> getAreaList(sys_area sys_area) {
        List<sys_area> areaList = null;
        try {
            areaList = (List<sys_area>) daoSupport.findForList("confing/mappers.sys_area.selectAreaList", sys_area);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return areaList;
    }
}
