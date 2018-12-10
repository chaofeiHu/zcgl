package com.hz.demo.services;


import com.hz.demo.core.PageData;
import com.hz.demo.dao.DaoSupportImpl;
import com.hz.demo.model.rec_reviewresultpublicity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@Service("ResultPublicityService")
public class ResultPublicityService {

    @Resource(name = "daoSupportImpl")
    private DaoSupportImpl daoSupport;

    //添加信息
    public int add(rec_reviewresultpublicity model) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.insert("confing/mappers.rec_reviewresultpublicity.insertSelective", model);

        } catch (Exception e) {
            e.printStackTrace();
            iFlag = 0;
        }
        return iFlag;
    }

    // 根据ID更新通过人员信息
    public int update(PageData pageData) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.update("confing/mappers.rec_reviewresultpublicity.updateByPrimaryKeySelective", pageData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }

    //根据ID删除通过人员信息
    public int delete(BigDecimal id) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.delete("confing/mappers.rec_reviewresultpublicity.deleteByPrimaryKey", id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }

    //根据ID查询未通过人员返回实体信息
    public rec_reviewresultpublicity getModel(BigDecimal id) {
        rec_reviewresultpublicity resultPublicity = null;
        try {
            resultPublicity = (rec_reviewresultpublicity) daoSupport.findForObject("confing/mappers.rec_reviewresultpublicity.selectByPrimaryKey", id);
        } catch (Exception e) {
            e.printStackTrace();
            resultPublicity = null;
        }
        return resultPublicity;
    }

    //分页查询所有的信息
    public List<rec_reviewresultpublicity> getList(PageData pageData) {
        List<rec_reviewresultpublicity> resultPublicityList = null;
        try {
            resultPublicityList = (List<rec_reviewresultpublicity>) daoSupport.findForList("confing/mappers.rec_reviewresultpublicity.selectList", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            resultPublicityList = null;
        }
        return resultPublicityList;
    }

    //查询对应的记录总数
    public Integer getListCount(PageData pageData) {
        Integer ListCount = 0;
        try {
            ListCount = (Integer) daoSupport.findForObject("confing/mappers.rec_reviewresultpublicity.selectCount", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            ListCount = 0;
        }
        return ListCount;
    }

    //根据条件查询部分通过人员信息
    public List<rec_reviewresultpublicity> getResultPublicityWhere(PageData pageData) {
        List<rec_reviewresultpublicity> blist = null;
        try {
            blist = (List<rec_reviewresultpublicity>) daoSupport.findForList("confing/mappers.rec_reviewresultpublicity.getResultPublicityWhere", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            blist = null;
        }
        return blist;
    }

}
