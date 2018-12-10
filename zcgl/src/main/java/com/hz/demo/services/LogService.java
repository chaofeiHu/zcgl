package com.hz.demo.services;

import com.hz.demo.core.PageData;
import com.hz.demo.core.Tree;
import com.hz.demo.dao.DaoSupportImpl;
import com.hz.demo.model.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("LogService")
public class LogService {
    @Resource(name = "daoSupportImpl")
    private DaoSupportImpl daoSupport;

    //添加信息
    public int add(sys_log model) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.insert("confing/mappers.sys_log.insertSelective", model);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }

    // 根据ID更新信息
    public int update(PageData pageData) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.update("confing/mappers.sys_log.updateByPrimaryKeySelective", pageData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }

    //根据ID删除信息
    public int delete(String id) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.delete("confing/mappers.sys_log.deleteByPrimaryKey", id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }

    //根据ID查询返回实体信息
    public sys_log getModel(String id) {
        sys_log model = null;
        try {
            model = (sys_log) daoSupport.findForObject("confing/mappers.sys_log.selectByPrimaryKey", id);
        } catch (Exception e) {
            e.printStackTrace();
            model = null;
        }
        return model;
    }

    //查询所有的信息
    public List<sys_log> getList(PageData pageData) {
        List<sys_log> modelList = null;
        try {
            modelList = (List<sys_log>) daoSupport.findForList("confing/mappers.sys_log.selectList", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            modelList = null;
        }
        return modelList;
    }


    //查询对应的记录总数
    public Integer getListCount(PageData pageData) {
        Integer ListCount = 0;
        try {
            ListCount = (Integer) daoSupport.findForObject("confing/mappers.sys_log.selectCount", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            ListCount = 0;
        }
        return ListCount;
    }

}
