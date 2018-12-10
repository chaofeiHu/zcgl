package com.hz.demo.services;

import com.hz.demo.core.PageData;
import com.hz.demo.core.ResultUtils;
import com.hz.demo.dao.DaoSupportImpl;
import com.hz.demo.model.base_blacklist;
import com.hz.demo.model.base_unit;
import com.hz.demo.model.shenbao.JudResult;
import com.hz.demo.model.shenbao.ProposerMsg;
import com.hz.demo.model.sys_area;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.hz.demo.core.ResultUtils.toJson;

@Service("proposerAllMsg")
public class ProposerAllMsgServices {

    protected final Log logger = LogFactory.getLog(getClass());

    @Resource(name = "daoSupportImpl")
    private DaoSupportImpl daoSupport;


    /**
     * 通过userid 查询需要申报人的所有数据
     *
     * @param pd
     * @return
     */
    public ProposerMsg selectUserMgr(PageData pd) {
        ProposerMsg proposerMsg = null;
        try {
            proposerMsg = (ProposerMsg) daoSupport.findForObject("ProposerAllMsgMapper.selectUserMgr", pd);
        } catch (Exception e) {
            e.printStackTrace();
            proposerMsg = null;
        }
        return proposerMsg;
    }


    /**
     * 保存申报结果
     *
     * @param pdd 总结果 cpd审批人id  lb 当前单位所有上级单位  userPd 更改评委会（如果有） judgingRoleByUserId当前用户审批内容
     * @return
     */
    public int saveJudging(PageData pdd, PageData cpd, List<base_unit> lb, ProposerMsg proposerMsg, PageData userPd, List<Map<String, String>> judgingRoleByUserId) {
        saveJudResult(pdd, judgingRoleByUserId);//保存审批人审查的所有信息
        int a = addJudLog(pdd, cpd);//增加一条审批日志
        String result = checkJudResult(pdd);//检查并更新审查结果（99） 返回为总结果
        if (!result.isEmpty()) {
            updateProposerMsgState(pdd, lb, proposerMsg, userPd, result);//更新申报人信息表状态
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
            map = (Map<String, String>) daoSupport.findForObject("ProposerAllMsgMapper.getGuMsg", userid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * @param pdd  id proId judStage judView judContent judResult
     * @param maps
     */
    public void saveJudResult(PageData pdd, List<Map<String, String>> maps) {

        if (maps != null) {
            if (!"2".equals(pdd.get("judResult"))) {
                for (Map<String, String> map : maps) {
                    //审批结果表 增加/修改纪录
                    PageData pd = (PageData) pdd.clone();
                    pd.put("id", UUID.randomUUID().toString());
                    pd.put("judContent", map.get("FSORT"));
                    /*if("13".equals(pd.get("judStage"))&&"1".equals(pdd.get("judResult"))){
                        pd.put("judStage",15);
                    }*/
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
    }

    //    检查审批结果信息并由结果设置总结果信息  返回总结果 如未审批完全则返回为"" 不通过0 通过1 退回修改2 加入黑名单4
    public String checkJudResult(PageData pdd) {
        List<JudResult> forList;
        PageData pdCl = (PageData) pdd.clone();
       /* if ("13".equals(pdCl.getString("judStage"))){
            pdCl.put("judStage","15");
        }*/
        try {
            forList = (List<JudResult>) daoSupport.findForList("JudResultMapper.getJudResultByProIdAndJudStage", pdCl);//查看审批用户当前阶段审批结果
        } catch (Exception e) {
            e.printStackTrace();
            forList = null;
        }

        String result = "";//申报人最终状态
        String judView = "";//审批意见
        if (forList != null) {
            for (JudResult judResult : forList) {
                if (!judResult.getJudContent().equals("99") && judResult.getJudResult().equals("4")) {
                    result = "4";//加入黑名单
                    judView = judResult.getJudView();
                    break;
                }
            }
            for (JudResult judResult : forList) {
                if (!judResult.getJudContent().equals("99") && judResult.getJudResult().equals("0")) {
                    result = "0";//不通过
                    judView = judResult.getJudView();
                    break;
                }
            }
            if (!result.equals("0")) {
                for (JudResult judResult : forList) {
                    if (!judResult.getJudContent().equals("99") && judResult.getJudResult().equals("2")) {
                        result = "2";//退回修改
                        break;
                    }
                }
            }
            if (!result.equals("2")) {
                boolean has99 = false;
                boolean tuiBoo = true;
                for (JudResult judResult : forList) {
                    if (judResult.getJudContent().equals("99")) {
                        has99 = true;//有总内容
                        break;
                    }
                }
                for (JudResult judResult : forList) {
                    if (!judResult.getJudContent().equals("99") && !judResult.getJudResult().equals("1")) {
                        tuiBoo = false;//非通过状态
                        break;
                    }
                }

                if (has99 && tuiBoo && forList.size() == 12) {//除99外都为通过
                    result = "1";//通过
                } else if (!has99 && tuiBoo && forList.size() == 11) {
                    result = "1";
                }
            }
        }
        if (!result.isEmpty()) {
            PageData pd = new PageData();//添加信息用
            pd.put("id", UUID.randomUUID().toString());
            pd.put("proId", pdCl.get("proId"));
            pd.put("judStage", pdCl.get("judStage"));
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
        if (pdd.get("judResult").equals("0")) {
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
    public void updateProposerMsgState(PageData pdd, List<base_unit> lb, ProposerMsg proposerMsg, PageData userPd, String result) {
        String currentJudgingStage = (String) pdd.get("judStage");
        //评审范围  正高  副高正高  副高 中级  初级
        String judLevel = "";
        try {
            judLevel = (String) daoSupport.findForObject("confing/mappers.base_judging.getJudLevel", proposerMsg.getBaseJudgingSeries());
        } catch (Exception e) {
            e.printStackTrace();
        }

        String nextStage = (Integer.parseInt(currentJudgingStage) + 1) + "";//下一阶段
        String currentState = null;//当前状态
        String proposeState = null;//审批状态
        String roleGroupCode = "";//上一级单位所属级别
        Boolean boo = Integer.parseInt(currentJudgingStage) % 2 == 0;//若为0  证明下一阶段要更改审批部门
        if (!StringUtils.equalsIgnoreCase(result, "1")) {//不通过  阶段不需变更
            nextStage = currentJudgingStage;
        } else {//通过  阶段需变更
            if (currentJudgingStage.equals("15")) {//

            } else {
                if (boo) {//下一阶段要更改审批部门
                    if (lb.size() > 1) {//存在上级单位
                        base_unit base_unit = lb.get(1);
                        String unitCode = base_unit.getUnitCode();

                        if (unitCode.length() == 6) {//职称办
                            PageData pdarea = new PageData();
                            pdarea.put("areaCode", unitCode);
                            List<sys_area> areaList = null;
                            try {
                                areaList = (List<sys_area>) daoSupport.findForList("confing/mappers.sys_area.selectList1", pdarea);
                            } catch (Exception e) {
                                e.printStackTrace();
                                areaList = null;
                            }
                            String areaGrade = areaList.get(0).getAreaGrade();
                            switch (areaGrade) {
                                case "1":
                                    roleGroupCode = "1";//省职称办
                                    nextStage = "13";
                                    break;
                                case "2":
                                    roleGroupCode = "2";//市职称办
                                    nextStage = "11";
                                    break;
                                case "3":
                                    roleGroupCode = "3";//县区职称办
                                    nextStage = "9";
                                    break;
                            }
                        } else if (unitCode.length() == 9) {//主管单位
                            nextStage = "7";
                            if (unitCode.substring(2, 4).equals("00"))
                                roleGroupCode = "4";//省级主管单位
                            else {
                                if (unitCode.substring(4, 6).equals("00") || unitCode.substring(2, 4).equals("90"))
                                    roleGroupCode = "5";//市级主管单位包含济源市
                                else
                                    roleGroupCode = "6";//县区级主管单位
                            }

                        } else if (unitCode.length() == 13) {
                            roleGroupCode = "7";//单位
                            nextStage = "5";
                        } else if (unitCode.length() == 15) {
                            roleGroupCode = "8";//下属单位
                            nextStage = "3";
                        } else if (unitCode.length() == 17) {
                            roleGroupCode = "9";//基层单位
                        }
                    }//获取上级单位的级别

                    switch (judLevel) {
                        case "初级":
                            break;
                        case "中级":
                            if (StringUtils.equalsIgnoreCase(roleGroupCode, "1")) {
                                nextStage = "15";
                            }
                            break;
                        case "副高":
                            break;
                        case "正高副高":
                            break;
                        case "正高":
                            break;
                        default:
                            break;
                    }
                }
            }
        }

        switch (result) {//根据总结果设置当前状态 审批状态
            case "0"://不通过
                currentState = "07";//审查不通过
                proposeState = "5";//申请失败
                break;
            case "1"://通过
                if (currentJudgingStage.equals("15")) {//参评人员审查
                    proposeState = "6";//确认参评
                } else {
                    proposeState = "2";//审核中
                }
                if (nextStage.equals("14")) {//省职称办无领导审查
                    nextStage = "15";
//                    currentState = "15";
                }

                switch (nextStage) {
                    case "1":
                        currentState = "09";
                        break;
                    case "2":
                        currentState = "09";
                        break;
                    case "3":
                        currentState = "10";
                        break;
                    case "4":
                        currentState = "10";
                        break;
                    case "5":
                        currentState = "11";
                        break;
                    case "6":
                        currentState = "11";
                        break;
                    case "7":
                        currentState = "12";
                        break;
                    case "8":
                        currentState = "12";
                        break;
                    case "9":
                        currentState = "13";
                        break;
                    case "10":
                        currentState = "13";
                        break;
                    case "11":
                        currentState = "14";
                        break;
                    case "12":
                        currentState = "14";
                        break;
                    case "13":
                        currentState = "15";
                        break;
                    case "15":
                        currentState = "16";
                        proposeState = "4";//申请成功
                        break;
                    case "16":
                        currentState = "18";//审核中
                        proposeState = "6";//确认参评
                        break;
                    default:
                        break;
                }
                break;
            case "2"://退回
                currentState = "01";//退回申报人修改
                proposeState = "3";//已退回
                break;
            case "4"://加入黑名单
                currentState = "06";//加入黑名单
                proposeState = "5";//申请失败
            default:
                break;
        }
       /* if(currentJudgingStage.equals("13")){
            nextStage="15";
        }*/
        int iFlag = 0;
        try {
            //申报信息表修改 judgingStage
            PageData pd = new PageData();
            pd.put("userid", pdd.get("proId"));
            pd.put("judgingStage", nextStage);
            pd.put("currentState", currentState);//当前状态
            pd.put("proposeState", proposeState);//申请状态
            pd.put("baseJudgingSeries", userPd.get("baseJudgingSeries"));//评委会
            iFlag = (int) daoSupport.update("ProposerAllMsgMapper.updateJudgingStage", pd);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
