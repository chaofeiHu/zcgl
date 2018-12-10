package com.hz.demo.controller;

import net.sf.json.JSONObject;
import com.hz.demo.core.DownloadUtil;
import com.hz.demo.core.PageData;
import com.hz.demo.core.ResultUtils;
import com.hz.demo.model.*;
import com.hz.demo.model.shenbao.JudLog;
import com.hz.demo.model.shenbao.JudResult;
import com.hz.demo.model.shenbao.ProposerMsg;
import com.hz.demo.model.shenbao.SpecialityMsg;
import com.hz.demo.services.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.hz.demo.core.EncodeUtil.encodeMD5;
import static com.hz.demo.core.ResultUtils.toJson;

/**
 * @author LvZiHang
 * @date 2018/9/14 16:47
 */
@Controller
@RequestMapping("/JudgingProposer")
public class JudgingProposerController extends BaseController {
    @Autowired
    UserService userService;
    @Autowired
    ProposerAllMsgServices proposerAllMsgServices;
    @Autowired
    JudLogServices judLogServices;
    @Autowired
    JudResultServices judResultServices;
    @Autowired
    BaseJudgingService baseJudgingService;
    @Autowired
    BaseUnitService baseUnitService;
    @Autowired
    DictService dictService;
    @Autowired
    RoleService roleService;
    @Autowired
    SubjectGroupService subjectGroupService;
    @Autowired
    ProposerGroupService proposerGroupService;
    @Autowired
    CreateSpecialityAllMsgServices createSpecialityAllMsgServices;
    @Autowired
    SpecialityService specialityService;
    @Autowired
    BaseBlackListService baseBlackListService;


    @RequestMapping("/toJudgingProposer.do")
    public String toJudgingProposer(HttpServletRequest request, HttpServletResponse response, Model model) {
        request.setAttribute("MenuBtns", userService.GetMenuBtns(request.getParameter("?menuid"), getUser().getUserId()));
        request.setAttribute("currentJudgingStage", request.getParameter("currentJudgingStage"));
        return "sys/judgingproposer";
    }

    //获取用户各方面信息（需审批的）
    @RequestMapping("/getProposerAllMsg.do")
    public String getProposerAllMsg(HttpServletRequest request, HttpServletResponse response, Model model) {
        logger.info("getProposerAllMsg");
        String userid = request.getParameter("userid");
        PageData pd = new PageData();
        pd.put("userid", userid);
        ProposerMsg proposerMsg = proposerAllMsgServices.selectUserMgr(pd);

        ResultUtils.write(response, toJson(proposerMsg));
        return null;
    }

    //获取用户审批结果信息
    @RequestMapping("/getProposerJudResult.do")
    public String getProposerJudResult(HttpServletRequest request, HttpServletResponse response, Model model) {
        logger.info("getProposerJudResult");
        String userid = request.getParameter("userid");
        String currentJudgingStage = request.getParameter("currentJudgingStage");
        PageData pd = new PageData();
        pd.put("proId", userid);
        pd.put("judStage", currentJudgingStage);
        List<JudResult> judResults = judResultServices.getJudResultByProIdAndJudStage(pd);
        ResultUtils.write(response, toJson(judResults));
        return null;
    }


    /**
     * 保存评审结果
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/saveJudging.do")
    public String saveJudging(HttpServletRequest request, HttpServletResponse response) {
        logger.info("saveJudging");
        String userid = request.getParameter("userId");
        String editpwh = request.getParameter("editpwh");//重选评委会
        PageData userPd = new PageData();
        userPd.put("userid", userid);
        ProposerMsg proposerMsg = proposerAllMsgServices.selectUserMgr(userPd);//获取用户信息
        if (editpwh != null && !editpwh.equals("")) {
            userPd.put("userid", userid);
            userPd.put("baseJudgingSeries", editpwh);
        }
        String judId = request.getParameter("judId");//审批人id
        String finalResult = request.getParameter("finalResult");//最终审批结果
        String currentJudgingStage = request.getParameter("currentJudgingStage");//当前审批阶段

        PageData getJud = new PageData();
        getJud.put("userId", getUser().getUserId());
        List<Map<String, String>> judgingRoleByUserId = roleService.getJudgingRoleByUserId(getJud);//当前用户所审批的内容 （FSORT）

        PageData pdd = new PageData();//总结果
        pdd.put("id", UUID.randomUUID().toString());
        pdd.put("proId", userid);
        pdd.put("judStage", currentJudgingStage);
        pdd.put("judView", request.getParameter("judgingView") == null ? "" : request.getParameter("judgingView"));
        if("4".equals(finalResult)){
            pdd.put("judView", request.getParameter("blackView") == null ? "" : request.getParameter("blackView"));
            pdd.put("blackendtime",request.getParameter("blackendtime") == null ? "" : request.getParameter("blackendtime"));
        }
        pdd.put("judContent", "99");
        pdd.put("judResult", finalResult);
        PageData pd = new PageData();
        pd.put("judId", judId);//审批人id
        String unitCode = getUser().getUnitCode();//当前单位编号
        List<base_unit> lb = new ArrayList<>();
        baseUnitService.getAllSuperUnit(unitCode, lb);//当前单位所有上级单位
        int flag = proposerAllMsgServices.saveJudging(pdd, pd, lb, proposerMsg, userPd, judgingRoleByUserId);//保存审批结果
        if (flag > 0) {
            //自动将参评人员分组
            proposerMsg = proposerAllMsgServices.selectUserMgr(userPd);//再次获取用户信息
            String state = proposerMsg.getCurrentState();
            if ("18".equals(proposerMsg.getCurrentState())) {//参评人员处于评审中状态
                //获取用户申报专业
                String proid = proposerMsg.getShenbaomajorId();
                //获取评委会下所有学科分组
                String jid = proposerMsg.getBaseJudgingSeries();
                PageData pageData = new PageData();
                pageData.put("judgingCode", jid);
                List<subject_group> subjectList = subjectGroupService.selectSubjectList(pageData);
                if (subjectList != null && subjectList.size() > 0) {
                    for (subject_group subjectGroup : subjectList) {
                        String professialId = subjectGroup.getProfessialId();
                        if (professialId != null && professialId.length() != 0) {
                            if (professialId.contains(proid)) {//如果参评人申报专业在分组内 就将该参评人划入该分组
                                proposer_group proposerGroup = new proposer_group();
                                proposerGroup.setGroupId(subjectGroup.getId().toString());
                                proposerGroup.setProposerId(userid);
                                proposerGroupService.add(proposerGroup);
                                break;
                            }
                        }
                    }
                }
            }else if("06".equals(proposerMsg.getCurrentState())){//黑名单
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    base_blacklist base_blacklist=new base_blacklist();
                    base_blacklist.setAddtime(new Date());
                    base_blacklist.setAdduserid(getUser().getUserId());
                    base_blacklist.setIdCardNo(proposerMsg.getBaseProposer().getIdCardNo());
                    base_blacklist.setMobilephone(proposerMsg.getBaseProposer().getMobilephone());
                    base_blacklist.setProposerName(proposerMsg.getBaseProposer().getDisplayName());
                    base_blacklist.setReason(pdd.getString("judView"));
                    base_blacklist.setRelieveDate(sdf.parse(pdd.getString("blackendtime")));
                    PageData pdBaseUnit=new PageData();
                    pdBaseUnit.put("unitCode",proposerMsg.getBaseProposer().getBasicUnitcode());
                    base_blacklist.setUnitName(baseUnitService.getModelWhere(pdBaseUnit).getUnitName());
                    base_blacklist.setBack1(proposerMsg.getUserId());
                    base_blacklist.setState(new BigDecimal(1));
                    baseBlackListService.addOrUpdate(base_blacklist);
                    ResultUtils.writeMessage(response, 1, "保存成功");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            ResultUtils.writeMessage(response, 1, "保存成功");
        } else {
            ResultUtils.writeMessage(response, 0, "保存失败");
        }
        return null;
    }

    /**
     * 预览文件
     *
     * @param response
     * @param request
     * @return
     */
    @RequestMapping("/previewFile.do")
    public String previewFile(HttpServletResponse response, HttpServletRequest request) {
        DownloadUtil.previewFile(response, request);
        return null;
    }

    /**
     * 预览文件(遮挡)
     *
     * @param response
     * @param request
     * @return
     */
    @RequestMapping("/previewFileZhe.do")
    public String previewFileZhe(HttpServletResponse response, HttpServletRequest request) {
        DownloadUtil.previewFileZhe(response, request);
        return null;
    }

    //通过组名获取字典数据
    @RequestMapping("/getDict.do")
    public void getDict(HttpServletResponse response, HttpServletRequest request) {
        String groupName = request.getParameter("groupName");
        List<sys_dict> dictList = getDictList(groupName);
        ResultUtils.write(response, ResultUtils.toJson(dictList));
    }

    //获取一部分固定数据
    @RequestMapping("/getGuMsg.do")
    public void getGuMsg(HttpServletResponse response, HttpServletRequest request) {
        ResultUtils.write(response, ResultUtils.toJson(proposerAllMsgServices.getGuMsg(request.getParameter("userid"))));
    }

    //获取评审日志
    @RequestMapping("/getProposerJudLog.do")
    public void getProposerJudLog(HttpServletResponse response, HttpServletRequest request) {
        String userid = request.getParameter("userid");
        PageData pd = new PageData();
        pd.put("proId", userid);
        List<Map<String, String>> judLogByProId = judLogServices.getJudLogByProId(pd);
        ResultUtils.write(response, ResultUtils.toJson(judLogByProId));
    }

    //获取退回修改信息
    @RequestMapping("/getProposerJudResultTui.do")
    public void getProposerJudResultTui(HttpServletResponse response, HttpServletRequest request) {
        String userid = request.getParameter("userid");
        String currentJudgingStage = request.getParameter("currentJudgingStage");
        PageData pd = new PageData();
        pd.put("proId", userid);
        pd.put("judResult", "2");//退回修改
        pd.put("judStage", currentJudgingStage);
        List<Map<String, String>> judResult = judResultServices.getJudResultTuiHui(pd);
        ResultUtils.write(response, ResultUtils.toJson(judResult));
    }

    //通过id获取退回修改信息
    @RequestMapping("/getProposerJudResultTuiById.do")
    public void getProposerJudResultTuiById(HttpServletResponse response, HttpServletRequest request) {
        PageData pd = new PageData();
        pd.put("id", request.getParameter("judResultId"));
        JudResult judResult = judResultServices.getJudResultTuiHuiById(pd);
        ResultUtils.write(response, ResultUtils.toJson(judResult));
    }

    //添加退回修改表数据
    @RequestMapping("/addTuiHui.do")
    public void addTuiHui(HttpServletResponse response, HttpServletRequest request) {
        String userid = request.getParameter("userId");
        String currentJudgingStage = request.getParameter("currentJudgingStage");
        PageData pd = new PageData();
        pd.put("id", UUID.randomUUID().toString());
        pd.put("proId", userid);
        pd.put("judResult", "2");
        pd.put("judView", request.getParameter("judView"));
        pd.put("judContent", request.getParameter("judContent"));
        pd.put("judStage", currentJudgingStage);
        int flag = judResultServices.addJudResult(pd);
        if (flag > 0) {
            ResultUtils.writeMessage(response, 1, "添加成功");
        } else {
            ResultUtils.writeMessage(response, 0, "添加失败");
        }
    }

    //修改退回修改表数据
    @RequestMapping("/updateJudResult.do")
    public void updateJudResult(HttpServletResponse response, HttpServletRequest request) {
        PageData pd = new PageData();
        pd.put("id", request.getParameter("judResultId"));
        pd.put("judView", request.getParameter("judView"));
        int flag = judResultServices.updateJudResult(pd);
        if (flag > 0) {
            ResultUtils.writeMessage(response, 1, "修改成功");
        } else {
            ResultUtils.writeMessage(response, 0, "修改失败");
        }
    }

    //删除退回修改表信息
    @RequestMapping("/delteteJudResult.do")
    public void delteteJudResult(HttpServletResponse response, HttpServletRequest request) {
        PageData pd = new PageData();
        pd.put("id", request.getParameter("judResultId"));
        int flag = judResultServices.deleteJudResult(pd);
        if (flag > 0) {
            ResultUtils.writeMessage(response, 1, "删除成功");
        } else {
            ResultUtils.writeMessage(response, 0, "删除失败");
        }
    }


    //    根据权限获取评审内容
    @RequestMapping("/getDictByRole.do")
    public void getDictByRole(HttpServletRequest request, HttpServletResponse response) {
        try {
            logger.info("根据权限获取评审内容");
            PageData pd = new PageData();
            pd.put("userId", getUser().getUserId());
            List<sys_dict> dictList = dictService.getDictByRole(pd);
            ResultUtils.write(response, dictList);
        } catch (Exception ex) {
            ResultUtils.writeMessage(response, 0, ex.getMessage());
        }
    }

    //获取用户各方面信息（需审批的）
    @RequestMapping("/getCreateSpecialityAllMsg.do")
    public String getCreateSpecialityAllMsg(HttpServletRequest request, HttpServletResponse response, Model model) {
        logger.info("getCreateSpecialityAllMsg");
        String userid = request.getParameter("userid");
        PageData pd = new PageData();
        pd.put("userid", userid);
        SpecialityMsg specialityMsg = createSpecialityAllMsgServices.selectUserMgr(pd);

        ResultUtils.write(response, toJson(specialityMsg));
        return null;
    }

    //获取一部分固定数据
    @RequestMapping("/getCreateSpecialityGuMsg.do")
    public void getCreateSpecialityGuMsg(HttpServletResponse response, HttpServletRequest request) {
        ResultUtils.write(response, ResultUtils.toJson(createSpecialityAllMsgServices.getGuMsg(request.getParameter("userid"))));
    }

    /**
     * 保存评审结果(推荐专家)
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/saveJudgingSpe.do")
    public String saveJudgingSpe(HttpServletRequest request, HttpServletResponse response) {
        logger.info("saveJudgingSpe");
        String userid = request.getParameter("userId");
        PageData userPd = new PageData();
        userPd.put("userid", userid);
        SpecialityMsg specialityMsg = createSpecialityAllMsgServices.selectUserMgr(userPd);//获取用户信息
        String judId = request.getParameter("judId");//审批人id
        String finalResult = request.getParameter("finalResultSpe");//最终审批结果
        String currentJudgingStage = request.getParameter("currentJudgingStage");//当前审批阶段

        PageData getJud = new PageData();
        getJud.put("userId", getUser().getUserId());
        List<sys_dict> speContent = dictService.getCreateSpecialityContent();//所审批的内容

        PageData pdd = new PageData();//总结果
        pdd.put("id", UUID.randomUUID().toString());
        pdd.put("proId", userid);
        pdd.put("judStage", currentJudgingStage);
        String view = "";
        if (StringUtils.equalsIgnoreCase(finalResult, "2")) {//退回修改
            view = request.getParameter("TuiHuiViewSpe") == null ? "" : request.getParameter("TuiHuiViewSpe");
        } else if (StringUtils.equalsIgnoreCase(finalResult, "0")) {//不通过
            view = request.getParameter("judgingViewSpe") == null ? "" : request.getParameter("judgingViewSpe");
        }

        pdd.put("judView", view);
        pdd.put("judContent", "99");
        pdd.put("judResult", finalResult);

        PageData pd = new PageData();
        pd.put("judId", judId);//审批人id

        String unitCode = getUser().getUnitCode();//当前单位编号
        List<base_unit> lb = new ArrayList<>();
        baseUnitService.getAllSuperUnit(unitCode, lb);//当前单位所有上级单位

        int flag = 0;

        if (StringUtils.equalsIgnoreCase(pdd.getString("judResult"), "1") && pdd.get("judStage").equals("13")) {//省通过
            flag = createSpecialityAllMsgServices.saveJudging(pdd, pd, lb, specialityMsg, userPd, speContent);//保存审批结果
            createSpecialityAllMsgServices.updateCreateSpeciality(pdd.getString("proId"), "99", "19", "4");//申报结束
            //开始保存专家到专家库
            PageData pppp = new PageData();
            pppp.put("userid", userid);
            SpecialityMsg specialityMsg1 = createSpecialityAllMsgServices.selectUserMgr(pppp);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            base_speciality baseSpeciality = new base_speciality();
            baseSpeciality.setAddtime(new Date());
            baseSpeciality.setAdduserid(getUser().getUserId());
            baseSpeciality.setSpecialityName(specialityMsg1.getBaseProposer().getDisplayName());
            baseSpeciality.setAdministrativeDutyLevel(specialityMsg1.getAdministrativeDutyLevel());
            baseSpeciality.setProfessialDutyLevel(specialityMsg1.getProfessialDutyLevel());
            baseSpeciality.setProfessialLevel(specialityMsg1.getProfessialLevel());
            baseSpeciality.setProfessial(specialityMsg1.getProfessial());
            baseSpeciality.setJobYear(specialityMsg1.getJobYear());
            baseSpeciality.setPerformance(specialityMsg1.getPerformance());
            baseSpeciality.setEducation(specialityMsg1.getEducation());
            baseSpeciality.setDegree(specialityMsg1.getDegree());
            baseSpeciality.setNowunit(specialityMsg1.getNowunit());
            baseSpeciality.setAreacode(specialityMsg1.getAreacode());
            baseSpeciality.setSex(specialityMsg1.getSex());
            baseSpeciality.setIdCardNo(specialityMsg1.getBaseProposer().getIdCardNo());
            baseSpeciality.setGraduateSchool(specialityMsg1.getGraduateSchool());
            baseSpeciality.setState(1);
            baseSpeciality.setTel(specialityMsg1.getTel());
            baseSpeciality.setMobilephone(specialityMsg1.getBaseProposer().getMobilephone());
            baseSpeciality.setEmail(specialityMsg1.getEmail());
            baseSpeciality.setPostalAddress(specialityMsg1.getAddress());
            baseSpeciality.setPresentation(specialityMsg1.getPresentation());
            baseSpeciality.setRecommendSeries(specialityMsg1.getReviewSeries());
            baseSpeciality.setRecommendMajor(specialityMsg1.getShenbaomajorId());
            baseSpeciality.setIsRandom(1);
            baseSpeciality.setBack1(specialityMsg1.getUserId());
            try {
                String birthdate = specialityMsg1.getBirthday();
                String graduateDate = specialityMsg1.getGraduateDate();
                if (null != birthdate && !"".equals(birthdate)) {
                    baseSpeciality.setBirthdate(sdf.parse(birthdate));
                }
                if (null != graduateDate && !"".equals(graduateDate)) {
                    baseSpeciality.setGraduateDate(sdf.parse(graduateDate));
                }
                //给新建用户(专家)赋值
                sys_user sysUser = new sys_user();
                sysUser.setUserId(UUID.randomUUID().toString());
                sysUser.setAddress(baseSpeciality.getPostalAddress());
                sysUser.setAddTime(baseSpeciality.getAddtime());
                sysUser.setAddUserId(baseSpeciality.getAdduserid());
                sysUser.setDisplayName(baseSpeciality.getSpecialityName());
                sysUser.setEmail(baseSpeciality.getEmail());
                String idCard = baseSpeciality.getIdCardNo();
                String password = idCard.substring(idCard.length() - 6);
                sysUser.setIdCardNo(idCard);
                sysUser.setLoginName(baseSpeciality.getMobilephone());
                sysUser.setMobilephone(baseSpeciality.getMobilephone());
                sysUser.setPassword(encodeMD5(password));
                sysUser.setState(1);
                sysUser.setUserType(1);//0系统管理员 1 专家
                String userId = sysUser.getUserId();
                baseSpeciality.setPersonalNumber(userId);
                //添加用户
                flag = userService.add(sysUser);
                if (flag == 0) {
                    throw new RuntimeException();
                }
                // 添加专家
                flag = specialityService.add(baseSpeciality);
                if (flag == 1) {
                    ResultUtils.writeMessage(response, 1, "保存成功");
                } else {
                    ResultUtils.writeMessage(response, 0, "保存失败");
                    throw new RuntimeException();
                }
            } catch (ParseException e) {
                e.printStackTrace();
                ResultUtils.writeMessage(response, 0, "保存失败");
                throw new RuntimeException();
            }


        } else if (!pdd.getString("judResult").isEmpty()) {
            flag = createSpecialityAllMsgServices.saveJudging(pdd, pd, lb, specialityMsg, userPd, speContent);//保存审批结果
            if (flag > 0) {
                ResultUtils.writeMessage(response, 1, "保存成功");
            } else {
                ResultUtils.writeMessage(response, 0, "保存失败");
            }
        }
        return null;
    }




    //所调用的接口 获取路径
    @RequestMapping(value = "getPath.html", method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody
    public JSONObject getPath(HttpServletRequest request) {
        JSONObject jsonObj = new JSONObject();
        Map<String,Object> map = new HashMap<String,Object>();
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request1 = ((ServletRequestAttributes) ra).getRequest();
        String path = request1.getSession().getServletContext().getRealPath("/");
        map.put("path", path);
        jsonObj.putAll(map);
        return jsonObj;
    }
}
