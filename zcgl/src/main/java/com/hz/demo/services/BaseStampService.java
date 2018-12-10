package com.hz.demo.services;


import com.hz.demo.core.PageData;
import com.hz.demo.core.Tree;
import com.hz.demo.dao.DaoSupportImpl;
import com.hz.demo.model.BaseStamp;
import com.hz.demo.model.sys_area;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("BaseStampService")
public class BaseStampService {

    @Resource(name = "daoSupportImpl")
    private DaoSupportImpl daoSupport;

    //添加印章
    public int add(PageData pd) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.insert("confing/mappers.BaseStamp.insertSelective", pd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }

    // 根据ID更新印章信息
    public int update(PageData pageData) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.update("confing/mappers.BaseStamp.updateByPrimaryKeySelective", pageData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }

    //根据ID删除印章信息
    public int delete(String id) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.delete("confing/mappers.BaseStamp.deleteByPrimaryKey", id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }

    //根据ID查询印章返回实体信息
    public BaseStamp getModel(String id) {
        BaseStamp baseStamp = null;
        try {
            baseStamp = (BaseStamp) daoSupport.findForObject("confing/mappers.BaseStamp.selectByPrimaryKey", id);
        } catch (Exception e) {
            e.printStackTrace();
            baseStamp = null;
        }
        return baseStamp;
    }


    //根据条件查询所有的印章信息
    public List<BaseStamp> getList(PageData pageData) {
        List<BaseStamp> baseStampList = null;
        try {
            baseStampList = (List<BaseStamp>) daoSupport.findForList("confing/mappers.BaseStamp.getBaseStampList", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            baseStampList = null;
        }
        return baseStampList;
    }

    //查询对应的记录总数
    public Integer getListCount(PageData pageData) {
        Integer ListCount = 0;
        try {
            ListCount = (Integer) daoSupport.findForObject("confing/mappers.BaseStamp.selectCount", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            ListCount = 0;
        }
        return ListCount;
    }
//根据单位编码获取印章 （含UNIT_NAME）
    public Map<String,Object> selectBaseStampModel(PageData pd){
        Map<String,Object> map=null;
        try {
            map=(Map<String,Object>)daoSupport.findForObject("confing/mappers.BaseStamp.selectBaseStampModel", pd);
        } catch (Exception e) {
            e.printStackTrace();
            map=null;
        }
        return map;
    }
}
