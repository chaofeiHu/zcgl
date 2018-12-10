package com.hz.demo.services;


import com.hz.demo.core.PageData;
import com.hz.demo.dao.DaoSupportImpl;
import com.hz.demo.model.rec_result;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@Service("RecResultService")
public class RecResultService {

    @Resource(name = "daoSupportImpl")
    private DaoSupportImpl daoSupport;

    //添加信息
    public int add(rec_result model) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.insert("confing/mappers.rec_result.insertSelective", model);

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
            iFlag = (int) daoSupport.update("confing/mappers.rec_result.updateByPrimaryKeySelective", pageData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }

    //根据ID删除通过人员信息
    public int delete(BigDecimal id) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.delete("confing/mappers.rec_result.deleteByPrimaryKey", id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }

    //根据ID查询通过人员返回实体信息
    public rec_result getModel(BigDecimal id) {
        rec_result recResult = null;
        try {
            recResult = (rec_result) daoSupport.findForObject("confing/mappers.rec_result.selectByPrimaryKey", id);
        } catch (Exception e) {
            e.printStackTrace();
            recResult = null;
        }
        return recResult;
    }

    //根据ID查询通过人员返回实体信息 (将编码用实际代表的中文信息填充)
    public rec_result getModelById(BigDecimal id) {
        rec_result recResult = null;
        try {
            recResult = (rec_result) daoSupport.findForObject("confing/mappers.rec_result.selectOneById", id);
        } catch (Exception e) {
            e.printStackTrace();
            recResult = null;
        }
        return recResult;
    }

    //分页查询所有的信息
    public List<rec_result> getList(PageData pageData) {
        List<rec_result> recResultList = null;
        try {
            recResultList = (List<rec_result>) daoSupport.findForList("confing/mappers.rec_result.selectList", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            recResultList = null;
        }
        return recResultList;
    }

    //查询对应的记录总数
    public Integer getListCount(PageData pageData) {
        Integer ListCount = 0;
        try {
            ListCount = (Integer) daoSupport.findForObject("confing/mappers.rec_result.selectCount", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            ListCount = 0;
        }
        return ListCount;
    }

    //根据条件查询部分通过人员信息
    public List<rec_result> getRecResultWhere(PageData pageData) {
        List<rec_result> blist = null;
        try {
            blist = (List<rec_result>) daoSupport.findForList("confing/mappers.rec_result.getRecResultWhere", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            blist = null;
        }
        return blist;
    }

    //分页查询所有的信息//含结果
    public List<rec_result> getListCertificate(PageData pageData) {
        List<rec_result> recResultList = null;
        try {
            recResultList = (List<rec_result>) daoSupport.findForList("confing/mappers.rec_result.selectListCertificate", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            recResultList = null;
        }
        return recResultList;
    }

    //查询对应的记录总数//含结果
    public Integer getListCountCertificate(PageData pageData) {
        Integer ListCount = 0;
        try {
            ListCount = (Integer) daoSupport.findForObject("confing/mappers.rec_result.selectCountCertificate", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            ListCount = 0;
        }
        return ListCount;
    }
    //根据ID查询通过人员返回实体信息（中文 非代码）
    public rec_result getModelXiang(BigDecimal id) {
        rec_result recResult = null;
        try {
            recResult = (rec_result) daoSupport.findForObject("confing/mappers.rec_result.getModelXiang", id);
        } catch (Exception e) {
            e.printStackTrace();
            recResult = null;
        }
        return recResult;
    }
//根据身份证号和证书号获取结果（带证书）
    public rec_result selectResultCertificateByIdCardAndCertificateNumber(PageData pd) {
        rec_result recResult = null;
        try {
            recResult = (rec_result) daoSupport.findForObject("confing/mappers.rec_result.selectResultCertificateByIdCardAndCertificateNumber", pd);
        } catch (Exception e) {
            e.printStackTrace();
            recResult = null;
        }
        return recResult;
    }





}
