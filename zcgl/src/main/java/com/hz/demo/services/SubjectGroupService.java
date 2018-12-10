package com.hz.demo.services;


import com.hz.demo.core.Page;
import com.hz.demo.core.PageData;
import com.hz.demo.core.Tree;
import com.hz.demo.dao.DaoSupportImpl;
import com.hz.demo.model.base_judging;
import com.hz.demo.model.subject_group;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("SubjectGroupService")
public class SubjectGroupService {

    @Resource(name = "daoSupportImpl")
    private DaoSupportImpl daoSupport;

    //添加信息
    public int add(PageData model) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.insert("confing/mappers.subject_group.insert", model);

        } catch (Exception e) {
            e.printStackTrace();
            iFlag = 0;
        }
        return iFlag;
    }

    // 根据ID更新评委专家信息
    @Transactional(propagation= Propagation.NESTED)
    public int update(PageData pageData) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.update("confing/mappers.subject_group.updateByPrimaryKeySelective", pageData);
        } catch (Exception e) {
           throw  new RuntimeException();
        }
        return iFlag;
    }

    //根据ID删除评委专家信息
    @Transactional(propagation= Propagation.NESTED)
    public int delete(Integer id) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.delete("confing/mappers.subject_group.deleteByPrimaryKey", id);
        } catch (Exception e) {
           throw new RuntimeException();
        }
        return iFlag;
    }

    //根据ID查询评委专家返回实体信息
    public subject_group getModel(Integer id) {
        subject_group subjectGroup = null;
        try {
            subjectGroup = (subject_group) daoSupport.findForObject("confing/mappers.subject_group.selectByPrimaryKey", id);
        } catch (Exception e) {
            e.printStackTrace();
            subjectGroup = null;
        }
        return subjectGroup;
    }

    //分页查询所有的信息
    public List<subject_group> getListlistPage(Page pageData) {
        List<subject_group> subjectGroupList = null;
        try {
            subjectGroupList = (List<subject_group>) daoSupport.findForList("confing/mappers.subject_group.selectListlistPage", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            subjectGroupList = null;
        }
        return subjectGroupList;
    }

    //查询对应的记录总数
    public Integer getListCount(PageData pageData) {
        Integer ListCount = 0;
        try {
            ListCount = (Integer) daoSupport.findForObject("confing/mappers.subject_group.selectCount", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            ListCount = 0;
        }
        return ListCount;
    }

    //查询评委会下所有学科组的信息
    public List<subject_group> selectSubjectList(PageData pageData) {
        List<subject_group> subjectGroupList = null;
        try {
            subjectGroupList = (List<subject_group>) daoSupport.findForList("confing/mappers.subject_group.selectSubjectList", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            subjectGroupList = null;
        }
        return subjectGroupList;
    }

    //根据条件查询某个学科组信息
    public subject_group getSubjectGroupWhere(PageData pageData) {
        subject_group subjectGroup = null;
        try {
            subjectGroup = (subject_group) daoSupport.findForObject("confing/mappers.subject_group.getSubjectGroupWhere", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            subjectGroup = null;
        }
        return subjectGroup;
    }

}
