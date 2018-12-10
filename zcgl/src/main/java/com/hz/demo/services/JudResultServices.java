package com.hz.demo.services;

import com.hz.demo.core.PageData;
import com.hz.demo.dao.DaoSupportImpl;
import com.hz.demo.model.shenbao.JudLog;
import com.hz.demo.model.shenbao.JudResult;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service("judResult")
public class JudResultServices {

    protected final Log logger = LogFactory.getLog(getClass());

    @Resource(name = "daoSupportImpl")
    private DaoSupportImpl daoSupport;


    /**
     * 通过申报人用户id和审批阶段 查询需要申报人的审批结果
     *
     * @param pd
     * @return
     */
    public List<JudResult> getJudResultByProIdAndJudStage(PageData pd) {
        List<JudResult> judResults = null;
        try {
            judResults = (List<JudResult>) daoSupport.findForList("JudResultMapper.getJudResultByProIdAndJudStage", pd);
        } catch (Exception e) {
            e.printStackTrace();
            judResults = null;
        }
        return judResults;
    }

    /**
     * 查询退回修改
     *
     * @param pd
     * @return
     */
    public List<Map<String,String>> getJudResultTuiHui(PageData pd) {
        List<Map<String,String>> judResults = null;
        try {
            judResults = (List<Map<String,String>>) daoSupport.findForList("JudResultMapper.getJudResultTuiHui", pd);
        } catch (Exception e) {
            e.printStackTrace();
            judResults = null;
        }
        return judResults;
    }
//添加纪录
    public int addJudResult(PageData pd){
        int flag=0;
        try {
            flag = (int) daoSupport.insert("JudResultMapper.insertSelective", pd);
        } catch (Exception e) {
            e.printStackTrace();
            flag = 0;
        }
        return flag;
    }
    //修改纪录
    public int updateJudResult(PageData pd){
        int flag=0;
        try {
            flag = (int) daoSupport.update("JudResultMapper.updateByPrimaryKeySelective", pd);
        } catch (Exception e) {
            e.printStackTrace();
            flag = 0;
        }
        return flag;
    }
    //删除纪录
    public int deleteJudResult(PageData pd){
        int flag=0;
        try {
            flag = (int) daoSupport.delete("JudResultMapper.deleteByPrimaryKey", pd);
        } catch (Exception e) {
            e.printStackTrace();
            flag = 0;
        }
        return flag;
    }
//通过id获取数据
    public JudResult getJudResultTuiHuiById(PageData pd){
        JudResult judResult=null;
        try {
            judResult = (JudResult) daoSupport.findForObject("JudResultMapper.selectByPrimaryKey", pd);
        } catch (Exception e) {
            e.printStackTrace();
            judResult =null;
        }
        return judResult;
    }




}
