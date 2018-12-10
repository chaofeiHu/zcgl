package com.hz.demo.services;

import com.hz.demo.core.PageData;
import com.hz.demo.dao.DaoSupportImpl;
import com.hz.demo.model.base_judging;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service("BaseJudgingService")
public class BaseJudgingService {
    @Resource(name = "daoSupportImpl")
    private DaoSupportImpl daoSupport;
    //添加信息
    public int add(base_judging model) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.insert("confing/mappers.base_judging.insertSelective", model);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }

    // 根据ID更新信息
    @Transactional(propagation= Propagation.NESTED)
    public int update(PageData pageData) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.update("confing/mappers.base_judging.updateByPrimaryKeySelective", pageData);
        } catch (Exception e) {
            throw  new RuntimeException();
        }
        return iFlag;
    }

    /**
     * 修改评委会通过比例
     * @param pageData
     * @return
     */
    @Transactional(propagation= Propagation.NESTED)
    public int updateProportion(PageData pageData) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.update("confing/mappers.base_judging.updateProportion", pageData);
        } catch (Exception e) {
            throw  new RuntimeException();
        }
        return iFlag;
    }

    public int updateIsDel(PageData pageData) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.update("confing/mappers.base_judging.updateIsDel", pageData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }

    //根据ID删除信息
    public int delete(String id) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.delete("confing/mappers.base_judging.deleteByPrimaryKey", Integer.parseInt(id));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }
    public base_judging getModelWhere(PageData pageData) {
        base_judging baseJuding = null;
        try {
            baseJuding = (base_judging) daoSupport.findForObject("confing/mappers.base_judging.getModelWhere", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            baseJuding = null;
        }
        return baseJuding;
    }
    //根据ID查询用户返回实体信息
    public base_judging getModel(String id) {
        base_judging judging = null;
        try {
            judging = (base_judging) daoSupport.findForObject("confing/mappers.base_judging.selectByPrimaryKey", Integer.parseInt(id));
        } catch (Exception e) {
            e.printStackTrace();
            judging = null;
        }
        return judging;
    }
    public base_judging getModel2(String id) {
        base_judging judging = null;
        try {
            judging = (base_judging) daoSupport.findForObject("confing/mappers.base_judging.selectById", Integer.parseInt(id));
        } catch (Exception e) {
            e.printStackTrace();
            judging = null;
        }
        return judging;
    }

    //查询所有的信息
    public List<base_judging> getList(PageData pageData) {
        List<base_judging> judgingList = null;
        try {
            judgingList = (List<base_judging>) daoSupport.findForList("confing/mappers.base_judging.selectList", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            judgingList = null;
        }
        return judgingList;
    }

    //查询对应的记录总数
    public Integer getListCount(PageData pageData) {
        Integer ListCount = 0;
        try {
            ListCount = (Integer) daoSupport.findForObject("confing/mappers.base_judging.selectCount", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            ListCount = 0;
        }
        return ListCount;
    }

    //查询所有的信息
    public List<base_judging> selectListWhere(PageData pageData) {
        List<base_judging> judgingList = null;
        try {
            judgingList = (List<base_judging>) daoSupport.findForList("confing/mappers.base_judging.selectListWhere", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            judgingList = null;
        }
        return judgingList;
    }
    //查询本级所有评委会
    public List<base_judging> selectListWhereByUnit(PageData pageData) {
        List<base_judging> judgingList = null;
        try {
            judgingList = (List<base_judging>) daoSupport.findForList("confing/mappers.base_judging.selectListWhereByUnit", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            judgingList = null;
        }
        return judgingList;
    }
    //查询获取学科组下拉框
    public List<PageData> selectSubject(PageData pageData) {
        List<PageData> judgingList = null;
        try {
            judgingList = (List<PageData>) daoSupport.findForList("confing/mappers.base_speciality_notice.selectSubject", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            judgingList = null;
        }
        return judgingList;
    }
}
