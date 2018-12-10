package com.hz.demo.services;

import com.hz.demo.core.Page;
import com.hz.demo.core.PageData;
import com.hz.demo.dao.DaoSupportImpl;
import com.hz.demo.model.rec_examresult;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@Service("RecExamResultService")
public class RecExamResultService {
    @Resource(name = "daoSupportImpl")
    private DaoSupportImpl daoSupport;
    //添加信息
    public int add(PageData pageData) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.insert("confing/mappers.rec_examresult.insertSelective", pageData);

        } catch (Exception e) {
            e.printStackTrace();
            iFlag = 0;
        }
        return iFlag;
    }
    public List<rec_examresult> getList(Page pageData) {
        List<rec_examresult> yeardeclareList = null;
        try {
            yeardeclareList = (List<rec_examresult>) daoSupport.findForList("confing/mappers.rec_examresult.selectlistPage", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            yeardeclareList = null;
        }
        return yeardeclareList;
    }
    public Integer getListCount(PageData pageData) {
        Integer ListCount = 0;
        try {
            ListCount = (Integer) daoSupport.findForObject("confing/mappers.rec_examresult.selectCount", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            ListCount = 0;
        }
        return ListCount;
    }
    public rec_examresult selectRecWhere(PageData pageData) {
        rec_examresult rec = null;
        try {
            rec = (rec_examresult) daoSupport.findForObject("confing/mappers.rec_examresult.selectRecWhere", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            rec = null;
        }
        return rec;
    }

    public rec_examresult selectByPrimaryKey(BigDecimal id) {
        rec_examresult rec = null;
        try {
            rec = (rec_examresult) daoSupport.findForObject("confing/mappers.rec_examresult.selectByPrimaryKey", id);
        } catch (Exception e) {
            e.printStackTrace();
            rec = null;
        }
        return rec;
    }
    public int update(PageData pageData) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.update("confing/mappers.rec_examresult.updateByPrimaryKeySelective", pageData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }
    public int delete(BigDecimal id) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.update("confing/mappers.rec_examresult.deleteByPrimaryKey", id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }


}
