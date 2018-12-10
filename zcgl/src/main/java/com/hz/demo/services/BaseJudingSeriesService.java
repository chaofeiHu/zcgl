package com.hz.demo.services;

import com.hz.demo.core.Page;
import com.hz.demo.core.PageData;
import com.hz.demo.dao.DaoSupportImpl;
import com.hz.demo.model.base_judging_series;
import com.hz.demo.model.base_speciality;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

@Service("BaseJudingSeriesService")
public class BaseJudingSeriesService {
    @Resource(name = "daoSupportImpl")
    private DaoSupportImpl daoSupport;

    //添加信息
    public int add(base_judging_series model) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.insert("confing/mappers.base_judging_series.insertSelective", model);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }

    // 根据ID更新信息
    public int update(PageData pageData) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.update("confing/mappers.base_judging_series.updateByPrimaryKeySelective", pageData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }
    //根据ID删除信息
    public int delete(String id) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.delete("confing/mappers.base_judging_series.deleteByPrimaryKey", Integer.parseInt(id));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }
    //根据评审系列删除数据
    public int deleteSeries(PageData pageData) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.delete("confing/mappers.base_judging_series.deleteByReview", pageData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }

    //根据ID查询用户返回实体信息
    public base_judging_series getModel(String id) {
        base_judging_series judging = null;
        try {
            judging = (base_judging_series) daoSupport.findForObject("confing/mappers.base_judging_series.selectByPrimaryKey", Integer.parseInt(id));
        } catch (Exception e) {
            e.printStackTrace();
            judging = null;
        }
        return judging;
    }

    //根据judgingCode查询返回实体信息
    public base_judging_series getModelWhere(PageData pageData) {
        base_judging_series judging = null;
        try {
            judging = (base_judging_series) daoSupport.findForObject("confing/mappers.base_judging_series.selectByJudgingCode", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            judging = null;
        }
        return judging;
    }
    public String getJudingCode(PageData pageData) {
        String juryProfessionCode = null;
        try {
            juryProfessionCode = (String) daoSupport.findForObject("confing/mappers.base_judging_series.selectJudingCode", pageData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return juryProfessionCode;
    }
    //查询所有的信息
    public List<base_judging_series> getList(PageData pageData) {
        List<base_judging_series> judgingList = null;
        try {
            judgingList = (List<base_judging_series>) daoSupport.findForList("confing/mappers.base_judging_series.selectList", pageData);
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
            ListCount = (Integer) daoSupport.findForObject("confing/mappers.base_judging_series.selectCount", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            ListCount = 0;
        }
        return ListCount;
    }
    //查询所有的信息
    public List<base_judging_series> getMojorList(PageData pageData) {
        List<base_judging_series> judgingList = null;
        try {
            judgingList = (List<base_judging_series>) daoSupport.findForList("confing/mappers.base_judging_series.selectMojorList", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            judgingList = null;
        }
        return judgingList;
    }
    public base_judging_series getMojorListWhere(PageData pageData) {
        base_judging_series judgingList = null;
        try {
            judgingList = (base_judging_series) daoSupport.findForObject("confing/mappers.base_judging_series.selectMojorList", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            judgingList = null;
        }
        return judgingList;
    }

    //查询对应的记录总数
    public Integer getMojorListCount(PageData pageData) {
        Integer ListCount = 0;
        try {
            ListCount = (Integer) daoSupport.findForObject("confing/mappers.base_judging_series.selectMojorCount", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            ListCount = 0;
        }
        return ListCount;
    }


    //查询所有的信息
    public List<PageData> selectGrouplistPage(Page pageData) {
        List<PageData> judgingList = null;
        try {
            judgingList = (List<PageData>) daoSupport.findForList("confing/mappers.base_judging_series.selectGrouplistPage", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            judgingList = null;
        }
        return judgingList;
    }

    /**
     * 修改主任/副主任
     * @param pageData
     * @return
     */
    @Transactional(propagation= Propagation.NESTED)
    public Integer UpdateDirector(PageData pageData){
        Integer ListCount = 0;
        try {
            ListCount = (Integer) daoSupport.update("confing/mappers.base_judging_series.UpdateDirector", pageData);
        } catch (Exception e) {
            ListCount = 0;
            throw  new RuntimeException();
        }
        return ListCount;
    }


  @Transactional(propagation= Propagation.NESTED)
    public Integer UpdateDirectorTwo(PageData pageData) {
        Integer ListCount = 0;
        try {
            ListCount = (Integer) daoSupport.update("confing/mappers.base_judging_series.UpdateDirectorTwo", pageData);
        } catch (Exception e) {
            ListCount = 0;
            throw  new RuntimeException();
        }
        return ListCount;
    }

    //根据评委会id 获取评委会评审系列
    public base_judging_series getReviewSeries(PageData pageData) {
        base_judging_series judgingSeries = null;
        try {
            judgingSeries = (base_judging_series) daoSupport.findForObject("confing/mappers.base_judging_series.selectReviewSeries", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            judgingSeries = null;
        }
        return judgingSeries;
    }

    /***
     * 学科组手工分配专家列表
     * @param pageData
     * @return
     */
    public List<PageData> selectManualGrouplistPage(Page pageData) {
        List<PageData> judgingList = null;
        try {
            judgingList = (List<PageData>) daoSupport.findForList("confing/mappers.base_judging_series.selectManualGrouplistPage", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            judgingList = null;
        }
        return judgingList;
    }




}
