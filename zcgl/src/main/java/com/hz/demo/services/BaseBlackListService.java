package com.hz.demo.services;

import com.hz.demo.core.PageData;
import com.hz.demo.core.Tree;
import com.hz.demo.dao.DaoSupportImpl;
import com.hz.demo.model.base_blacklist;
import com.hz.demo.model.base_judging_process;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 黑名单
 */
@Service("BaseBlackListService")
public class BaseBlackListService {
    @Resource(name = "daoSupportImpl")
    private DaoSupportImpl daoSupport;

    //添加信息
    public int add(base_blacklist model) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.insert("confing/mappers.base_blacklist.insertSelective", model);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }

    // 根据ID删除信息
    public int Delete(BigDecimal id) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.delete("confing/mappers.base_blacklist.deleteByPrimaryKey", id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }

    // 根据ID更新信息
    public int update(base_blacklist pageData) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.update("confing/mappers.base_blacklist.updateByPrimaryKeySelective", pageData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }

    //根据ID查询用户返回实体信息
    public base_blacklist getModel(String id) {
        base_blacklist judging = null;
        try {
            judging = (base_blacklist) daoSupport.findForObject("confing/mappers.base_blacklist.selectByPrimaryKey", new BigDecimal(id));
        } catch (Exception e) {
            e.printStackTrace();
            judging = null;
        }
        return judging;
    }

    //查询所有的信息
    public List<PageData> getList(PageData pageData) {
        List<PageData> modelList = null;
        try {
            modelList = (List<PageData>) daoSupport.findForList("confing/mappers.base_blacklist.selectList", pageData);
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
            ListCount = (Integer) daoSupport.findForObject("confing/mappers.base_blacklist.selectCount", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            ListCount = 0;
        }
        return ListCount;
    }

    //通过用户id获取黑名单信息
    public base_blacklist getBlackListMsgByUserId(String id) {
        base_blacklist judging = null;
        try {
            judging = (base_blacklist) daoSupport.findForObject("confing/mappers.base_blacklist.getBlackListMsgByUserId", id);
        } catch (Exception e) {
            e.printStackTrace();
            judging = null;
        }
        return judging;
    }

    //通过用户身份证号获取黑名单信息
    public base_blacklist getBlackListMsgByidCardNo(String idCardNo) {
        base_blacklist judging = null;
        try {
            judging = (base_blacklist) daoSupport.findForObject("confing/mappers.base_blacklist.getBlackListMsgByidCardNo", idCardNo);
        } catch (Exception e) {
            e.printStackTrace();
            judging = null;
        }
        return judging;
    }

    //    添加（已存在时更新）
    public int addOrUpdate(base_blacklist model) {
        int flag = 0;
        base_blacklist judging = null;
        try {
            judging = (base_blacklist) daoSupport.findForObject("confing/mappers.base_blacklist.getBlackListMsgByidCardNo", model.getIdCardNo());
            if (judging == null) {
                flag = add(model);
            } else {
                flag = update(model);
            }
        } catch (Exception e) {
            e.printStackTrace();
            flag = 0;
        }
        return flag;
    }

}
