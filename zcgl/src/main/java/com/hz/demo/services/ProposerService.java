package com.hz.demo.services;


import com.hz.demo.core.PageData;
import com.hz.demo.dao.DaoSupportImpl;
import com.hz.demo.model.base_proposer;
import com.hz.demo.model.base_unit;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("ProposerService")
public class ProposerService {

    @Resource(name = "daoSupportImpl")
    private DaoSupportImpl daoSupport;

    //添加申报人信息
    public int add(base_proposer model) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.insert("confing/mappers.base_proposer.insertSelective", model);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }

    // 根据ID更新申报人信息
    public int update(PageData pageData) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.update("confing/mappers.base_proposer.updateByPrimaryKeySelective", pageData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }

    //根据ID删除申报人信息
    public int delete(BigDecimal id) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.delete("confing/mappers.base_proposer.deleteByPrimaryKey", id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }

    //根据ID查询申报人返回实体信息
    public base_proposer getModel(BigDecimal id) {
        base_proposer user = null;
        try {
            user = (base_proposer) daoSupport.findForObject("confing/mappers.base_proposer.selectByPrimaryKey", id);
        } catch (Exception e) {
            e.printStackTrace();
            user = null;
        }
        return user;
    }

    //查询所有的申报人信息
    public List<base_proposer> getList(PageData pageData) {
        List<base_proposer> userList = null;
        try {
            userList = (List<base_proposer>) daoSupport.findForList("confing/mappers.base_proposer.selectList", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            userList = null;
        }
        return userList;
    }

    //查询对应的记录总数
    public Integer getListCount(PageData pageData) {
        Integer ListCount = 0;
        try {
            ListCount = (Integer) daoSupport.findForObject("confing/mappers.base_proposer.selectCount", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            ListCount = 0;
        }
        return ListCount;
    }

    //根据手机号或身份证号查询用户 验证是否存在
    public List<base_proposer> getProposerWhere(PageData pageData) {
        List<base_proposer> blist = null;
        try {
            blist = (List<base_proposer>) daoSupport.findForList("confing/mappers.base_proposer.getProposerWhere", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            blist = null;
        }
        return blist;
    }

    //查询所有的申报人信息
    public List<Map<String, String>> getJudgingProposerList(PageData pageData) {
        List<Map<String, String>> userList = null;
        try {
            userList = (List<Map<String, String>>) daoSupport.findForList("confing/mappers.base_proposer.getJudgingProposerList", translateUnitCode(pageData, "declareUnitcode"));
        } catch (Exception e) {
            e.printStackTrace();
            userList = null;
        }
        return userList;
    }

    //查询对应的记录总数
    public Integer getJudgingProposerListCount(PageData pageData) {
        Integer ListCount = 0;
        try {
            ListCount = (Integer) daoSupport.findForObject("confing/mappers.base_proposer.getJudgingProposerListCount", translateUnitCode(pageData, "declareUnitcode"));
        } catch (Exception e) {
            e.printStackTrace();
            ListCount = 0;
        }
        return ListCount;
    }

    //根据条件查询 申报人员状态信息
    public List<PageData> selectProposerTypeList(PageData pageData) {
        List<PageData> userList = null;
        try {
            userList = (List<PageData>) daoSupport.findForList("confing/mappers.base_proposer.selectProposerTypeList", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            userList = null;
        }
        return userList;
    }

    //根据条件查询 申报人员状态信息记录总数
    public Integer selectProposerTypeCount(PageData pageData) {
        Integer ListCount = 0;
        try {

            ListCount = (Integer) daoSupport.findForObject("confing/mappers.base_proposer.selectProposerTypeCount", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            ListCount = 0;
        }
        return ListCount;
    }

    public List<PageData> selectUnitCode(PageData pdd) {
        List<PageData> pd = null;
        try {
            pd = (List<PageData>) daoSupport.findForList("confing/mappers.base_proposer.selectUnitCode", pdd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pd;
    }

    //单位编码截取
    public PageData translateUnitCode(PageData pageData, String name) {
        PageData pageData1 = (PageData) pageData.clone();
        PageData pd = new PageData();
        pd.put("unitCode", pageData1.getString(name));
        base_unit baseUnit = null;
        try {
            baseUnit = (base_unit) daoSupport.findForObject("confing/mappers.base_unit.getModelWhere", pd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if ("1".equals(baseUnit.getUnitCategory())) {
            if (baseUnit.getUnitAttach().equals("20")) {//市级单位 隶属于省
                pageData1.put(name, pageData1.getString(name).substring(0, 4));
            } else if (baseUnit.getUnitAttach().equals("10")) {//省单位
                pageData1.put(name, pageData1.getString(name).substring(0, 2));
            }
        }
        return pageData1;
    }

    //查询所有的申报人信息
    public List<Map<String, String>> getJudgingProposerListCan(PageData pageData) {
        List<Map<String, String>> userList = null;
        try {
            userList = (List<Map<String, String>>) daoSupport.findForList("confing/mappers.base_proposer.getJudgingProposerListCan", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            userList = null;
        }
        return userList;
    }

    //查询对应的记录总数
    public Integer getJudgingProposerListCountCan(PageData pageData) {
        Integer ListCount = 0;
        try {

            ListCount = (Integer) daoSupport.findForObject("confing/mappers.base_proposer.getJudgingProposerListCountCan", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            ListCount = 0;
        }
        return ListCount;
    }

    //通过评委会编码获取管理单位
    public List<PageData> selectUnitByJudgingCode(PageData pdd) {
        List<PageData> pd = null;
        try {
            pd = (List<PageData>) daoSupport.findForList("confing/mappers.base_proposer.selectUnitByJudgingCode", pdd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pd;
    }
//查询推荐专家信息
    public List<Map<String, Object>> selectCreateSpecialityTypeList(PageData pd) {
        List<Map<String, Object>> map = null;
        if(!StringUtils.isEmpty(pd.getString("basicUnitcode"))){
            translateUnitCode(pd,"basicUnitcode");
        }
        try {
            map = (List<Map<String, Object>>) daoSupport.findForList("confing/mappers.base_proposer.selectCreateSpecialityTypeList", pd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
    //查询推荐专家信息总数
    public Integer selectCreateSpecialityTypeCount(PageData pd) {
        Integer pageData = null;
        if(!StringUtils.isEmpty(pd.getString("basicUnitcode"))){
            translateUnitCode(pd,"basicUnitcode");
        }
        try {
            pageData = (Integer) daoSupport.findForObject("confing/mappers.base_proposer.selectCreateSpecialityTypeCount", pd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pageData;
    }

    //查询所有的推荐专家审查信息
    public List<Map<String, String>> getJudgingSpecialityMsgList(PageData pageData) {
        List<Map<String, String>> userList = null;
        try {
            userList = (List<Map<String, String>>) daoSupport.findForList("confing/mappers.base_proposer.getJudgingSpecialityMsgList", translateUnitCode(pageData, "declareUnitcode"));
        } catch (Exception e) {
            e.printStackTrace();
            userList = null;
        }
        return userList;
    }

    //查询对应的推荐专家审查总数
    public Integer getJudgingSpecialityMsgListCount(PageData pageData) {
        Integer ListCount = 0;
        try {
            ListCount = (Integer) daoSupport.findForObject("confing/mappers.base_proposer.getJudgingSpecialityMsgListCount", translateUnitCode(pageData, "declareUnitcode"));
        } catch (Exception e) {
            e.printStackTrace();
            ListCount = 0;
        }
        return ListCount;
    }

}
