package com.hz.demo.services;

import com.hz.demo.core.PageData;
import com.hz.demo.dao.DaoSupportImpl;
import com.hz.demo.model.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("TokenService")
public class TokenService {

    @Resource(name = "daoSupportImpl")
    private DaoSupportImpl daoSupport;


    //添加信息
    public int addToken(PageData pageData) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.insert("confing/mappers.sys_token.insertSelective", pageData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }

    // 根据ID更新用户信息
    public int updateToken(PageData pageData) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.update("confing/mappers.sys_token.updateByPrimaryKeySelective", pageData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }

    //根据ID删除用户信息
    public int deleteTokenByid(String id) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.delete("confing/mappers.sys_token.deleteByPrimaryKey", id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }

    //根据UserId删除用户信息
    public int deleteTokenByUserId(String userId) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.delete("confing/mappers.sys_token.deleteTokenByUserId", userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }

    //根据ID查询信息
    public sys_token getTokenByFid(String id) {
        sys_token token = null;
        try {
            token = (sys_token) daoSupport.findForObject("confing/mappers.sys_token.selectByPrimaryKey", id);
        } catch (Exception e) {
            e.printStackTrace();
            token = null;
        }
        return token;
    }


    //根据条件查询某个信息
    public sys_token getTokenWhere(PageData pageData) {
        sys_token token = null;
        try {
            token = (sys_token) daoSupport.findForObject("confing/mappers.sys_token.getTokenWhere", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            token = null;
        }
        return token;
    }

}
