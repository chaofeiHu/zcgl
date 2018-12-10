package com.hz.demo.services;

import com.hz.demo.core.PageData;
import com.hz.demo.dao.DaoSupportImpl;
import com.hz.demo.model.base_speciality;
import com.hz.demo.model.base_unit;
import com.hz.demo.model.shenbao.JudResult;
import com.hz.demo.model.shenbao.ProposerMsg;
import com.hz.demo.model.shenbao.SpecialityMsg;
import com.hz.demo.model.sys_area;
import com.hz.demo.model.sys_dict;
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

@Service("createSpecialityAllMsg")
public class CreateSpecialityAllMsgServices {

    protected final Log logger = LogFactory.getLog(getClass());

    @Resource(name = "daoSupportImpl")
    private DaoSupportImpl daoSupport;


    /**
     * 通过userid 查询需要申报人的所有数据
     *
     * @param pd
     * @return
     */
    public SpecialityMsg selectUserMgr(PageData pd) {
        SpecialityMsg specialityMsg = null;
        try {
            specialityMsg = (SpecialityMsg) daoSupport.findForObject("CreateSpecialityAllMsgMapper.selectUserMgr", pd);
        } catch (Exception e) {
            e.printStackTrace();
            specialityMsg = null;
        }
        return specialityMsg;
    }


    /**
     * 保存申报结果
     *
     * @param pdd 总结果 cpd审批人id  lb 当前单位所有上级单位  userPd 更改评委会（如果有） judgingRoleByUserId当前用户审批内容
     * @return
     */
    public int saveJudging(PageData pdd, PageData cpd, List<base_unit> lb, SpecialityMsg specialityMsg, PageData userPd, List<sys_dict> speContent) {

        saveJudResult(pdd, speContent);//保存审批人审查的所有信息
        int a = addJudLog(pdd, cpd);//增加一条审批日志
        String result = checkJudResult(pdd);//检查并更新审查结果（99） 返回为总结果
        if (StringUtils.equalsIgnoreCase(result, "1") && pdd.get("judStage").equals("13")) {//省通过
        } else if (!result.isEmpty()) {
            updateSpecialityMsgState(pdd, lb, specialityMsg, userPd, result);
        }
        return a;

    }

    /**
     * 获取固定属性
     *
     * @param userid
     * @return
     */
    public Map<String, String> getGuMsg(String userid) {
        Map<String, String> map = null;
        try {
            map = (Map<String, String>) daoSupport.findForObject("CreateSpecialityAllMsgMapper.getGuMsg", userid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * @param pdd         id proId judStage judView judContent judResult
     * @param speContents
     */
    public void saveJudResult(PageData pdd, List<sys_dict> speContents) {
        PageData pd = (PageData) pdd.clone();
        if (speContents != null) {
            for (sys_dict speContent : speContents) {
                //审批结果表 增加/修改纪录
                pd.put("judContent", speContent.getDictCode());
                pd.put("id", UUID.randomUUID().toString());
                try {
                    daoSupport.delete("JudResultMapper.deleteByProIdAndJudStage", pd);//删除原审批结果信息
                    //增加审批结果信息
                    daoSupport.insert("JudResultMapper.insertSelective", pd);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //    设置总结果信息  返回总结果  不通过0 通过1 退回修改2
    public String checkJudResult(PageData pdd) {
        String result = (String) pdd.get("judResult");//判断申报人最终状态应为？
        String judView = "";//申报人退回修改意见
        if (!"1".equals(result)) {
            judView = (String) pdd.get("judView");
        }
        PageData pd = new PageData();//添加信息用
        pd.put("id", UUID.randomUUID().toString());
        pd.put("proId", pdd.get("proId"));
        pd.put("judStage", pdd.get("judStage"));
        pd.put("judView", judView);
        pd.put("judContent", "99");
        pd.put("judResult", result);
        try {
            daoSupport.delete("JudResultMapper.deleteByProIdAndJudStage", pd);//删除原审批结果信息
            //增加审批结果信息
            daoSupport.insert("JudResultMapper.insertSelective", pd);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    //    增加审批日志
    public int addJudLog(PageData pdd, PageData cpd) {
        //审批日志表 增加纪录
        PageData pd1 = new PageData();
        pd1.put("id", UUID.randomUUID().toString());
        pd1.put("proId", pdd.get("proId"));
        pd1.put("proResult", pdd.get("judResult"));
        if (!pdd.get("judResult").equals("1")) {
            pd1.put("proView", pdd.get("judView"));
        } else {
            pd1.put("proView", "");
        }
        pd1.put("judId", cpd.get("judId"));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowDate = sdf.format(new Date());
        pd1.put("judDate", nowDate);
        pd1.put("judStage", pdd.get("judStage"));
        try {
            return (int) daoSupport.insert("JudLogMapper.insertSelective", pd1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    //    修改用户状态 评委会 审批阶段
    public void updateSpecialityMsgState(PageData pdd, List<base_unit> lb, SpecialityMsg specialityMsg, PageData userPd, String result) {
        String currentJudgingStage = (String) pdd.get("judStage");
        String nextStage =currentJudgingStage;//下一阶段
        String currentState = null;//当前状态
        String proposeState = null;//审批状态
        switch (result) {//根据总结果设置当前状态 审批状态
            case "0"://不通过
                currentState = "07";//审查不通过
                proposeState = "5";//申请失败
                break;
            case "1"://通过

                switch (currentJudgingStage) {
                    case "5":
                        proposeState = "2";//审核中
                        nextStage="13";
                        currentState = "15";
                        break;
                    case "13":
                        proposeState = "4";//申请成功
                        nextStage="13";
                        currentState = "19";
                        break;
                    default:
                        break;
                }
                break;
            case "2"://退回
                currentState = "01";//退回申报人修改
                proposeState = "3";//已退回
                break;
            default:
                break;
        }
        int iFlag = 0;
        try {
            //申报信息表修改 judgingStage
            PageData pd = new PageData();
            pd.put("userid", pdd.get("proId"));
            pd.put("judgingStage", nextStage);
            pd.put("currentState", currentState);//当前状态
            pd.put("proposeState", proposeState);//申请状态
            iFlag = (int) daoSupport.update("CreateSpecialityAllMsgMapper.updateJudgingStage", pd);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //    修改推荐专家状态
    public void updateCreateSpeciality(String userid, String judgingStage, String currentState, String proposeState) {
        try {
            //申报信息表修改 judgingStage
            PageData pd = new PageData();
            pd.put("userid", userid);
            pd.put("judgingStage", judgingStage);
            pd.put("currentState", currentState);//当前状态
            pd.put("proposeState", proposeState);//申请状态
            daoSupport.update("CreateSpecialityAllMsgMapper.updateJudgingStage", pd);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
