package com.hz.demo.services;

import com.hz.demo.core.PageData;
import com.hz.demo.dao.DaoSupportImpl;
import com.hz.demo.model.shenbao.JudLog;
import com.hz.demo.model.shenbao.ProposerMsg;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service("judLog")
public class JudLogServices {

    protected final Log logger = LogFactory.getLog(getClass());

    @Resource(name = "daoSupportImpl")
    private DaoSupportImpl daoSupport;


    /**
     * 通过userid 查询需要申报人的审批日志
     *
     * @param pd
     * @return
     */
    public List<Map<String,String>> getJudLogByProId(PageData pd) {
        List<Map<String,String>> judlogs = null;
        try {
            judlogs = (List<Map<String,String>>) daoSupport.findForList("JudLogMapper.getJudLogByProId", pd);
        } catch (Exception e) {
            e.printStackTrace();
            judlogs = null;
        }
        return judlogs;
    }






}
