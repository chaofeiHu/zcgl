package com.hz.demo.services;


import com.hz.demo.core.PageData;
import com.hz.demo.dao.DaoSupportImpl;
import com.hz.demo.model.rec_resultdissent;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@Service("ResultDissentService")
public class ResultDissentService {

    @Resource(name = "daoSupportImpl")
    private DaoSupportImpl daoSupport;

    //添加信息
    public int add(rec_resultdissent model) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.insert("confing/mappers.rec_resultdissent.insertSelective", model);

        } catch (Exception e) {
            e.printStackTrace();
            iFlag = 0;
        }
        return iFlag;
    }

    // 根据ID更新未通过人员信息
    public int update(PageData pageData) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.update("confing/mappers.rec_resultdissent.updateByPrimaryKeySelective", pageData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }

    //根据ID删除未通过人员信息
    public int delete(BigDecimal id) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.delete("confing/mappers.rec_resultdissent.deleteByPrimaryKey", id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }

    //根据ID查询未通过人员返回实体信息
    public rec_resultdissent getModel(BigDecimal id) {
        rec_resultdissent resultDissent = null;
        try {
            resultDissent = (rec_resultdissent) daoSupport.findForObject("confing/mappers.rec_resultdissent.selectByPrimaryKey", id);
        } catch (Exception e) {
            e.printStackTrace();
            resultDissent = null;
        }
        return resultDissent;
    }

    //分页查询所有的信息
    public List<rec_resultdissent> getList(PageData pageData) {
        List<rec_resultdissent> resultDissentList = null;
        try {
            resultDissentList = (List<rec_resultdissent>) daoSupport.findForList("confing/mappers.rec_resultdissent.selectList", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            resultDissentList = null;
        }
        return resultDissentList;
    }

    //查询对应的记录总数
    public Integer getListCount(PageData pageData) {
        Integer ListCount = 0;
        try {
            ListCount = (Integer) daoSupport.findForObject("confing/mappers.rec_resultdissent.selectCount", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            ListCount = 0;
        }
        return ListCount;
    }

    //根据条件查询部分未通过人员信息
    public List<rec_resultdissent> getReviewResultWhere(PageData pageData) {
        List<rec_resultdissent> blist = null;
        try {
            blist = (List<rec_resultdissent>) daoSupport.findForList("confing/mappers.rec_resultdissent.getResultDissentWhere", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            blist = null;
        }
        return blist;
    }

}
