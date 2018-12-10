package com.hz.demo.services;


import com.hz.demo.core.PageData;
import com.hz.demo.dao.DaoSupportImpl;
import com.hz.demo.model.rec_resultobjection;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@Service("ResultObjectionService")
public class ResultObjectionService {

    @Resource(name = "daoSupportImpl")
    private DaoSupportImpl daoSupport;

    //添加信息
    public int add(rec_resultobjection model) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.insert("confing/mappers.rec_resultobjection.insertSelective", model);

        } catch (Exception e) {
            e.printStackTrace();
            iFlag = 0;
        }
        return iFlag;
    }

    // 根据ID更新异议人员信息
    public int update(PageData pageData) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.update("confing/mappers.rec_resultobjection.updateByPrimaryKeySelective", pageData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }

    //根据ID删除异议人员信息
    public int delete(BigDecimal id) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.delete("confing/mappers.rec_resultobjection.deleteByPrimaryKey", id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }

    //根据ID查询异议人员返回实体信息
    public rec_resultobjection getModel(BigDecimal id) {
        rec_resultobjection resultObjection = null;
        try {
            resultObjection = (rec_resultobjection) daoSupport.findForObject("confing/mappers.rec_resultobjection.selectByPrimaryKey", id);
        } catch (Exception e) {
            e.printStackTrace();
            resultObjection = null;
        }
        return resultObjection;
    }

    //分页查询所有的信息
    public List<rec_resultobjection> getList(PageData pageData) {
        List<rec_resultobjection> resultObjectionList = null;
        try {
            resultObjectionList = (List<rec_resultobjection>) daoSupport.findForList("confing/mappers.rec_resultobjection.selectList", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            resultObjectionList = null;
        }
        return resultObjectionList;
    }

    //查询对应的记录总数
    public Integer getListCount(PageData pageData) {
        Integer ListCount = 0;
        try {
            ListCount = (Integer) daoSupport.findForObject("confing/mappers.rec_resultobjection.selectCount", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            ListCount = 0;
        }
        return ListCount;
    }

    //根据条件查询部分异议人员信息
    public List<rec_resultobjection> getReviewResultWhere(PageData pageData) {
        List<rec_resultobjection> blist = null;
        try {
            blist = (List<rec_resultobjection>) daoSupport.findForList("confing/mappers.rec_resultobjection.getResultObjectionWhere", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            blist = null;
        }
        return blist;
    }

}
