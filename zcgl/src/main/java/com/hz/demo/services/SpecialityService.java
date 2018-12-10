package com.hz.demo.services;


import com.hz.demo.core.PageData;
import com.hz.demo.core.Tree;
import com.hz.demo.dao.DaoSupportImpl;
import com.hz.demo.model.base_engage;
import com.hz.demo.model.base_judging;
import com.hz.demo.model.base_speciality;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("SpecialityService")
public class SpecialityService {

    @Resource(name = "daoSupportImpl")
    private DaoSupportImpl daoSupport;

    //添加信息
    public int add(base_speciality model) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.insert("confing/mappers.base_speciality.insert", model);

        } catch (Exception e) {
            e.printStackTrace();
            iFlag = 0;
        }
        return iFlag;
    }

    // 根据ID更新评委专家信息
    public int update(PageData pageData) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.update("confing/mappers.base_speciality.updateByPrimaryKeySelective", pageData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }

    //根据ID删除评委专家信息
    public int delete(Integer id) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.delete("confing/mappers.base_speciality.deleteByPrimaryKey", id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }

    /***
     * 查询专家信息
     * @param pageData
     * @return
     */
    public List<PageData> selectSpecialityInfo(PageData pageData){
        List<PageData> speciality = null;
        try {
            speciality = (List<PageData>) daoSupport.findForList("confing/mappers.base_speciality.selectSpecialityInfo", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            speciality = null;
        }
        return speciality;
    }

    //根据ID查询评委专家返回实体信息
    public base_speciality getModel(Integer id) {
        base_speciality speciality = null;
        try {
            speciality = (base_speciality) daoSupport.findForObject("confing/mappers.base_speciality.selectByPrimaryKey", id);
        } catch (Exception e) {
            e.printStackTrace();
            speciality = null;
        }
        return speciality;
    }

    //查询所有的信息
    public List<base_speciality> getList(PageData pageData) {
        List<base_speciality> specialityList = null;
        try {
            specialityList = (List<base_speciality>) daoSupport.findForList("confing/mappers.base_speciality.selectList", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            specialityList = null;
        }
        return specialityList;
    }

    //查询对应的记录总数
    public Integer getListCount(PageData pageData) {
        Integer ListCount = 0;
        try {
            ListCount = (Integer) daoSupport.findForObject("confing/mappers.base_speciality.selectCount", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            ListCount = 0;
        }
        return ListCount;
    }

    //根据条件查询部分评委专家信息
    public List<base_speciality> getSpecialityWhere(PageData pageData) {
        List<base_speciality> blist = null;
        try {
            blist = (List<base_speciality>) daoSupport.findForList("confing/mappers.base_speciality.getSpecialityWhere", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            blist = null;
        }
        return blist;
    }


    //获取评委会信息
    public List<base_judging> getJudgingList(PageData pageData) {
        List<base_judging> baseJudgingList = null;
        try {
            baseJudgingList = (List<base_judging>) daoSupport.findForList("confing/mappers.base_speciality.getJudgingList", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            baseJudgingList = null;
        }
        return baseJudgingList;
    }

    public List<Tree> getJudgingTree(PageData pageData) {
        List<Tree> treeList = new ArrayList<>();
        List<Map<String, Object>> hashMaps = new ArrayList<>();
        try {
            hashMaps = (List<Map<String, Object>>) daoSupport.findForList("confing/mappers.base_speciality.getTreeList", pageData);
            for (Map<String, Object> map : hashMaps) {
                Tree tree = new Tree();
                BigDecimal id = (BigDecimal) map.get("ID");
                tree.setId(id.toString());
                tree.setText((String) map.get("TEXT"));
                tree.setPid("0");
                tree.setAttributes((String) map.get("ATTRIBUTES"));
                treeList.add(tree);
            }
            Tree ptree = new Tree();
            ptree.setId("0");
            ptree.setText("评委会名称");
            treeList.add(ptree);
        } catch (Exception e) {
            e.printStackTrace();
            treeList = null;
        }
        return treeList;
    }

    public List<PageData> loginEngage(String userId) {
        List<PageData> baseJudgingList = null;
        try {
            baseJudgingList = (List<PageData>) daoSupport.findForList("confing/mappers.base_speciality.loginEngage", userId);
        } catch (Exception e) {
            e.printStackTrace();
            baseJudgingList = null;
        }
        return baseJudgingList;
    }

}
