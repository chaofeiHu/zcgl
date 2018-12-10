package com.hz.demo.services;


import com.hz.demo.core.PageData;
import com.hz.demo.dao.DaoSupportImpl;
import com.hz.demo.model.rec_reviewresult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@Service("ReviewResultService")
public class ReviewResultService {

    @Resource(name = "daoSupportImpl")
    private DaoSupportImpl daoSupport;

    //添加信息
    public int add(rec_reviewresult model) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.insert("confing/mappers.rec_reviewresult.insertSelective", model);

        } catch (Exception e) {
            e.printStackTrace();
            iFlag = 0;
        }
        return iFlag;
    }

    // 根据ID更新评审结果信息
    public int update(PageData pageData) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.update("confing/mappers.rec_reviewresult.updateByPrimaryKeySelective", pageData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }

    //根据ID删除评审结果信息
    public int delete(BigDecimal id) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.delete("confing/mappers.rec_reviewresult.deleteByPrimaryKey", id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }

    //根据ID查询评审结果返回实体信息
    public rec_reviewresult getModel(BigDecimal id) {
        rec_reviewresult reviewResult = null;
        try {
            reviewResult = (rec_reviewresult) daoSupport.findForObject("confing/mappers.rec_reviewresult.selectByPrimaryKey", id);
        } catch (Exception e) {
            e.printStackTrace();
            reviewResult = null;
        }
        return reviewResult;
    }

    //分页查询所有的信息
    public List<rec_reviewresult> getList(PageData pageData) {
        List<rec_reviewresult> reviewResultList = null;
        try {
            reviewResultList = (List<rec_reviewresult>) daoSupport.findForList("confing/mappers.rec_reviewresult.selectList", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            reviewResultList = null;
        }
        return reviewResultList;
    }

    //查询对应的记录总数
    public Integer getListCount(PageData pageData) {
        Integer ListCount = 0;
        try {
            ListCount = (Integer) daoSupport.findForObject("confing/mappers.rec_reviewresult.selectCount", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            ListCount = 0;
        }
        return ListCount;
    }

    //根据条件查询部分评审结果信息
    public rec_reviewresult getReviewResultWhere(PageData pageData) {
        rec_reviewresult recReviewresult = null;
        try {
            recReviewresult = (rec_reviewresult)daoSupport.findForList("confing/mappers.rec_reviewresult.getReviewResultWhere", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            recReviewresult = null;
        }
        return recReviewresult;
    }

    //根据userid修改数据
    public int updateByUserId(PageData pageData) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.update("confing/mappers.rec_reviewresult.updateByUserId", pageData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }
}
