package com.hz.demo.services;

import com.hz.demo.core.PageData;
import com.hz.demo.core.Tree;
import com.hz.demo.dao.DaoSupportImpl;
import com.hz.demo.model.base_professial;
import com.hz.demo.model.base_unit;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("BaseProfessialService")
public class BaseProfessialService {

    @Resource(name = "daoSupportImpl")
    private DaoSupportImpl daoSupport;

    //添加信息
    public int add(base_professial model) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.insert("confing/mappers.base_professial.insertSelective", model);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }

    // 根据ID更新信息
    public int update(PageData pageData) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.update("confing/mappers.base_professial.updateByPrimaryKeySelective", pageData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }

    //根据ID删除信息
    public int delete(String id) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.delete("confing/mappers.base_professial.deleteByPrimaryKey", Integer.parseInt(id));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }

    //根据ID查询用户返回实体信息
    public base_professial getModel(String id) {
        base_professial professial = null;
        try {
            professial = (base_professial) daoSupport.findForObject("confing/mappers.base_professial.selectByPrimaryKey", Integer.parseInt(id));
        } catch (Exception e) {
            e.printStackTrace();
            professial = null;
        }
        return professial;
    }

    //查询所有的信息
    public List<base_professial> getList(PageData pageData) {
        List<base_professial> professialList = null;
        try {
            professialList = (List<base_professial>) daoSupport.findForList("confing/mappers.base_professial.selectList", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            professialList = null;
        }
        return professialList;
    }

    //查询对应的记录总数
    public Integer getListCount(PageData pageData) {
        Integer ListCount = 0;
        try {
            ListCount = (Integer) daoSupport.findForObject("confing/mappers.base_professial.selectCount", pageData);
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
            hashMaps = (List<Map<String, String>>) daoSupport.findForList("confing/mappers.base_professial.getTreeList", pageData);
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
    public base_professial getProfe(String base_professial, String professialCode) {
        PageData pageData = new PageData();
        pageData.put("professialCode", professialCode);
        base_professial unit = getProfeWhere(pageData);
        return unit;
    }
    //根据条件查询返回实体信息
    public base_professial getProfeWhere(PageData pageData) {
        base_professial model = null;
        try {
            model = (base_professial) daoSupport.findForObject("confing/mappers.base_professial.getProfeWhere", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            model = null;
        }
        return model;
    }
    //根据条件查询返回实体信息
    public PageData getProfeWhereTwo(PageData pageData) {
        PageData model = null;
        try {
            model = (PageData) daoSupport.findForObject("confing/mappers.base_professial.getProfeWhereTwo", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            model = null;
        }
        return model;
    }
    //根据条件查询返回实体信息
    public PageData findProfessial(PageData pageData) {
        PageData model = null;
        try {
            model = (PageData) daoSupport.findForObject("confing/mappers.base_professial.findProfessial", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            model = null;
        }
        return model;
    }

    //根据条件查询返回集合
    public List<Tree> getListWhere(PageData pageData) {
        List<Tree> treeList = new ArrayList<>();
        List<Map<String, String>> hashMaps = new ArrayList<>();
        try {
            hashMaps = (List<Map<String, String>>) daoSupport.findForList("confing/mappers.base_professial.selectListWhere", pageData);
            for (Map<String, String> map : hashMaps) {
                Tree tree = new Tree();
                tree.setId(map.get("ID"));
                tree.setText(map.get("TEXT"));
                tree.setPid(map.get("PID"));
                treeList.add(tree);
            }
        } catch (Exception e) {
            e.printStackTrace();
            treeList = null;
        }
        return treeList;
    }
}
