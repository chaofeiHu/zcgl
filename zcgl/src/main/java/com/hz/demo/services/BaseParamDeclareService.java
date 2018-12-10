package com.hz.demo.services;

import com.hz.demo.core.PageData;
import com.hz.demo.core.Tree;
import com.hz.demo.dao.DaoSupportImpl;
import com.hz.demo.model.base_param_declare;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @program: pm
 * @description:
 * @author: LiuPengbo
 * @create: 2018-09-17 14:52
 **/
@Service("BaseParamDeclareService")
public class BaseParamDeclareService {
    @Resource(name = "daoSupportImpl")
    private DaoSupportImpl daoSupport;
    //添加信息
    public int add(base_param_declare model) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.insert("confing/mappers.base_param_declare.insertSelective", model);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }

    // 根据ID更新信息
    public int update(PageData pageData) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.update("confing/mappers.base_param_declare.updateByPrimaryKeySelective", pageData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }

    //根据ID删除信息
    public int delete(String id) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.delete("confing/mappers.base_param_declare.deleteByPrimaryKey", new BigDecimal(id));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }

    //根据ID查询用户返回实体信息
    public base_param_declare getModel(String id) {
        base_param_declare declare = null;
        try {
            declare = (base_param_declare) daoSupport.findForObject("confing/mappers.base_param_declare.selectByPrimaryKey", new BigDecimal(id));
        } catch (Exception e) {
            e.printStackTrace();
            declare = null;
        }
        return declare;
    }

    //根据unitCode查询
    public base_param_declare getByUnitCode(PageData pageData) {
        base_param_declare declareList = null;
        try {
            declareList = (base_param_declare) daoSupport.findForObject("confing/mappers.base_param_declare.selectByUnitCode", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            declareList = null;
        }
        return declareList;
    }

    //查询对应的记录总数
    public Integer getListCount(PageData pageData) {
        Integer ListCount = 0;
        try {
            ListCount = (Integer) daoSupport.findForObject("confing/mappers.base_param_declare.selectCount", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            ListCount = 0;
        }
        return ListCount;
    }
    //查询所有的信息
    public List<base_param_declare> getMojorList(PageData pageData) {
        List<base_param_declare> declareList = null;
        try {
            declareList = (List<base_param_declare>) daoSupport.findForList("confing/mappers.base_param_declare.selectMojorList", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            declareList = null;
        }
        return declareList;
    }

    //查询对应的记录总数
    public Integer getMojorListCount(PageData pageData) {
        Integer ListCount = 0;
        try {
            ListCount = (Integer) daoSupport.findForObject("confing/mappers.base_param_declare.selectMojorCount", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            ListCount = 0;
        }
        return ListCount;
    }

    //组织结构树
    public List<Tree> getUnitTree(PageData pageData){
        List<Tree> treeList = new ArrayList<>();
        List<Map<String, String>> hashMaps = new ArrayList<>();
        try {
            hashMaps = (List<Map<String, String>>) daoSupport.findForList("confing/mappers.base_unit.selectListWhere", pageData);
            for (Map<String, String> map : hashMaps) {
                Tree tree = new Tree();
                tree.setId(map.get("ID"));
                tree.setText(map.get("TEXT"));
                tree.setPid(map.get("PID"));
                treeList.add(tree);
            }
        }catch (Exception e) {
            e.printStackTrace();
            treeList = null;
        }
        return treeList;

    }
}
