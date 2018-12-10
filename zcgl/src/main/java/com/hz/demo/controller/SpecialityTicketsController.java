package com.hz.demo.controller;

import com.hz.demo.core.*;
import com.hz.demo.model.base_judging_process;
import com.hz.demo.services.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;
import static com.hz.demo.core.ResultUtils.toJson;

/**
 * 学科组/评委会  票源/投票
 */
@Controller
@RequestMapping("/SpecialityTickets")
public class SpecialityTicketsController extends BaseController{

    @Autowired
    UserService userService;
    @Autowired
    SpecialityTicketsService specialityTicketsService;
    @Autowired
    SubjectGroupService subjectGroupService;
    @Autowired
    BaseJudingProcessService baseJudingProcessService;
    @Autowired
    BaseJudgingService baseJudgingService;
    @Autowired
    ProposerAllMsgServices proposerAllMsgServices;
   //跳到投票页
    @RequestMapping(value = "SpecialityPage")
    public String DeptPage(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("MenuBtns",userService.GetMenuBtns(request.getParameter("?menuid"),getUser().getUserId()));
        request.setAttribute("type",request.getParameter("type"));
        return "sys/speciality/speciality_tickets";
    }
    //跳到申请人详细信息页面
    @RequestMapping(value = "SpecialityProPage")
    public String SpecialityProPage(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("MenuBtns",userService.GetMenuBtns(request.getParameter("?menuid"),getUser().getUserId()));
        //request.setAttribute("currentJudgingStage","1");
        request.setAttribute("PROPOSER_ID",request.getParameter("PROPOSER_ID"));
        String s = proposerAllMsgServices.getGuMsg(request.getParameter("PROPOSER_ID")).get("BASE_JUDGING_SERIES");
        String processPattern = baseJudingProcessService.selectByPrimaryKeyThree(s).getString("PROCESS_PATTERN");
        request.setAttribute("Name",request.getParameter("Name"));
        request.setAttribute("processPattern",processPattern);
        return "sys/speciality/speciality_tickets_pro";
    }
    //跳到投票统计页面
    @RequestMapping(value = "SpecialityTicketsResultPage")
    public String SpecialityTicketsResultPage(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("MenuBtns",userService.GetMenuBtns(request.getParameter("?menuid"),getUser().getUserId()));
        request.setAttribute("type",request.getParameter("type"));
        return "sys/speciality/speciality_tickets_result";
    }
    //跳到评议汇总页面
    @RequestMapping(value = "CommentResultPage")
    public String CommentResultPage(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("MenuBtns",userService.GetMenuBtns(request.getParameter("menuid"),getUser().getUserId()));
        return "sys/speciality/speciality_tickets_comment";
    }


    //获取界面列表
    @RequestMapping(value = "getList")
    public void getList(HttpServletRequest request, HttpServletResponse response) {
        logger.info("获取学科组/评委会是否开始投票列表");
        PageData pd = new PageData();
        pd.put("SPECIALITY_ID", getUser().getSpecialityId());
        pd.put("JUDGING_CODE", getUser().getJudgingCode());
        pd.put("TYPE", request.getParameter("type"));
        pd.put("GEOUPID",request.getParameter("GEOUPID"));
        List<PageData> blist = specialityTicketsService.selectTicketsInfo(pd);
        ResultUtils.write(response, toJson(blist));
    }

    /**
     * 获取当前专家是否投票
     * @param response
     */
    @RequestMapping(value = "findSpliTick")
    public void findSpliTick(HttpServletResponse response) {
        logger.info("获取当前专家是否投票");
        try {
            PageData pageData=this.getPageData();
            pageData.put("SPECIALITY_ID",getUser().getSpecialityId());
            pageData.put("JUDGING_CODE",getUser().getJudgingCode());
            pageData=specialityTicketsService.findSpliTick(pageData);
            ResultUtils.writeMessage(response, 1, pageData.getString("COU"));
        } catch (Exception e) {
            e.printStackTrace();
            ResultUtils.writeMessage(response, 0, "提交失败");
        }
    }
    //保存

    @RequestMapping(value = "save")
    public void Save(HttpServletResponse response) {
        logger.info("保存");
        try {
            PageData pageData=this.getPageData();
            pageData.put("SPECIALITY_ID",getUser().getSpecialityId());
            specialityTicketsService.updateTickets(pageData);
            ResultUtils.writeMessage(response, 1, "提交成功");
        } catch (Exception e) {
            e.printStackTrace();
            ResultUtils.writeMessage(response, 0, "提交失败");
        }
    }

    //获取界面列表
    @RequestMapping(value = "selectTicketsRemarks")
    public void selectTicketsRemarks(HttpServletRequest request, HttpServletResponse response) {
        logger.info("获取学科组投票意见");
        PageData pd = new PageData();
        String type=request.getParameter("TYPE");
        if(type.equals("2")){  //评委会查看学科组意见
            pd.put("TYPE","1" );
        }
        pd.put("GROUP_ID", request.getParameter("GROUP_ID"));
        pd.put("PROPOSER_ID", request.getParameter("PROPOSER_ID"));
        List<PageData> blist = specialityTicketsService.selectTicketsRemarks(pd);
        ResultUtils.write(response, toJson(blist));
    }

    /**
     * 获取申请人得票数
     * @param request
     * @param response
     */
    @RequestMapping(value = "selectTicketsComment")
    public void selectTicketsComment(HttpServletRequest request, HttpServletResponse response) {
        logger.info("获取申请人得票数");
        PageData pd = new PageData();
        String type=request.getParameter("TYPE");
        pd.put("TYPE",type );
        pd.put("JUDGING_CODE",getUser().getJudgingCode());
        pd.put("GROUP_ID", request.getParameter("GROUP_ID"));
        List<PageData> blist = specialityTicketsService.selectTicketsComment(pd);
        PageData pageData=specialityTicketsService.selectCommentNums(pd);
        JSONObject object=new JSONObject();
        if(pageData!=null){
            String Proportion=pageData.getString("PROPORTION");
            object.put("nus",pageData.get("NUS"));
            if(null==Proportion){
                object.put("proportion","");
            }else{
                object.put("proportion",Proportion);
            }
        }else{
            object.put("proportion","");
        }
        object.put("rows",blist);
        object.put("total",blist.size());
        ResultUtils.write(response, object);
    }

    /**
     * 保存学科组评议信息
     * @param request
     * @param response
     */
    @Transactional(propagation= Propagation.REQUIRED)
    @RequestMapping(value = "saveComment")
    public void SaveComment(HttpServletRequest request, HttpServletResponse response) {
        logger.info("保存学科组/评委会评议信息");
        try {
            String arrPerson=request.getParameter("arrPerson");
            JSONArray item=JSONArray.fromObject(arrPerson);
            for(int i=0;i<item.size();i++){
                JSONObject object=item.getJSONObject(i);
                PageData pageData=new PageData();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
                Date date = new Date();
                pageData.put("userId",object.get("PROPOSER_ID"));
                pageData.put("yearNo",sdf.format(date));
                PageData rec=specialityTicketsService.selectRec(pageData);
                if(rec==null){  //不存在
                    PageData propser=specialityTicketsService.selectPropser(pageData);
                    pageData.put("areaCode",propser.getString("AREA_NUMBER"));
                    pageData.put("judgingCode",propser.getString("BASE_JUDGING_SERIES"));
                    pageData.put("unitCode",propser.getString("BASIC_UNITCODE"));
                    pageData.put("reviewSeries",propser.getString("REVIEW_SERIES"));
                    pageData.put("positionalTitles",propser.getString("POSITION"));
                    pageData.put("professialCode",propser.getString("SHENBAOMAJOR_ID"));
                    pageData.put("reviewType",propser.getString("PINGSHENTYPE"));
                    //pageData.put("userId",object.getString("PROPOSER_ID"));
                    pageData.put("userName",propser.getString("DISPLAY_NAME"));
                    pageData.put("idCardNo",propser.getString("ID_CARD_NO"));
                    pageData.put("sex",propser.getString("SEX"));
                    pageData.put("titleLevel",propser.getString("DICT_CODE1"));
                    pageData.put("positionalTitles",propser.getString("DICT_CODE"));
                    if(object.get("TYPE").toString().equals("1")){
                        pageData.put("groupId",object.get("GROUP_ID").toString());
                        pageData.put("groupResultYes",object.get("TY"));
                        pageData.put("groupResultNo",object.get("BTY"));
                        pageData.put("groupResultWaive",object.get("QQ"));
                        pageData.put("groupResultOpinion",object.get("COMMENT"));
                        pageData.put("groupResult",object.get("STATE"));
                    }else{
                        pageData.put("reviewResultYes",object.get("TY"));
                        pageData.put("reviewResultNo",object.get("BTY"));
                        pageData.put("reviewResultWaive",object.get("QQ"));
                        pageData.put("reviewResultOpinion",object.get("COMMENT"));
                        pageData.put("reviewResult",object.get("STATE"));
                    }
                    specialityTicketsService.addRec(pageData);
                }else{
                    if(object.get("TYPE").toString().equals("1")){
                        pageData.put("groupId",object.get("GROUP_ID"));
                        pageData.put("groupResultYes",object.get("TY"));
                        pageData.put("groupResultNo",object.get("BTY"));
                        pageData.put("groupResultWaive",object.get("QQ"));
                        pageData.put("groupResultOpinion",object.get("COMMENT"));
                        pageData.put("groupResult",object.get("STATE"));
                    }else{
                        pageData.put("reviewResultYes",object.get("TY"));
                        pageData.put("reviewResultNo",object.get("BTY"));
                        pageData.put("reviewResultWaive",object.get("QQ"));
                        pageData.put("reviewResultOpinion",object.get("COMMENT"));
                        pageData.put("reviewResult",object.get("STATE"));
                    }
                    pageData.put("id",rec.get("ID"));
                    specialityTicketsService.updateRec(pageData);
                }


            }
            /*for(int i=0;i<item.size();i++){
                JSONObject object=item.getJSONObject(i);
                PageData pageData=new PageData();
                pageData.put("GROUP_ID",object.get("GROUP_ID"));
                pageData.put("COMMENTS",object.get("COMMENT"));
                pageData.put("STATES",object.get("STATE"));
                pageData.put("PROPOSER_ID",object.get("PROPOSER_ID"));
                pageData.put("TYPES",object.get("TYPE"));
                pageData.put("TY",object.get("TY"));
                pageData.put("BTY",object.get("BTY"));
                pageData.put("QQ",object.get("QQ"));
                specialityTicketsService.updateTicketsComment(pageData);
            }*/
            ResultUtils.writeMessage(response, 1, "提交成功");
        } catch (Exception e) {
            e.printStackTrace();
            ResultUtils.writeMessage(response, 0, "提交失败");
        }
    }

    @Transactional(propagation= Propagation.REQUIRED)
    @RequestMapping(value = "saveSubjectGroup")
    public void saveSubjectGroup(HttpServletRequest request, HttpServletResponse response) {
        logger.info("保存学科组通过比例");
        try {
            String PROPORTION=request.getParameter("PROPORTION");
            String GroupId=request.getParameter("GroupId");
            PageData pageData =new PageData();
            pageData.put("id",GroupId);
            if(request.getParameter("TYPE").equals("2")){
                pageData.put("JUDGING_PROPORTION",PROPORTION);
                pageData.put("JUDGING_CODE",getUser().getJudgingCode());
                baseJudgingService.updateProportion(pageData);
            }else{
                pageData.put("proportion",PROPORTION);
                subjectGroupService.update(pageData);
            }
            ResultUtils.writeMessage(response, 1, "提交成功");
        } catch (Exception e) {
            e.printStackTrace();
            ResultUtils.writeMessage(response, 0, "提交失败");
        }
    }

    //评委会评议情况汇总
    @RequestMapping(value = "getCommentList")
    public void getCommentList(HttpServletRequest request, HttpServletResponse response) {
        logger.info("评委会评议情况汇总");
        PageData pd = new PageData();
        JSONObject object=new JSONObject();
        Page page=new Page();
        try{
            pd.put("REVIEW_RESULT", request.getParameter("STATES"));
            if(!request.getParameter("GROUP_ID").equals("")){
                pd.put("GROUP_ID",request.getParameter("GROUP_ID"));
                if(request.getParameter("GROUP_ID").equals("0")){
                    pd.put("JUDGING_CODE",getUser().getJudgingCode());
                }
                page.setPd(pd);
                List<PageData> blist = specialityTicketsService.getCommentList(pd);
                base_judging_process base_judging_process=baseJudingProcessService.getModel(getUser().getJudgingCode());
                page.setData(blist);
                if(base_judging_process.getProcessGroup()==1){
                   page.setBack1("1");
                    // object.put("nus","1");
                }else{
                    page.setBack1("2");
                }
               // object.put("data",blist);
                //object.put("total",blist.size());
                System.out.println(object);
            }
           /* object.put("msg","sdf");
            object.put("code",0);*/
            ResultUtils.write(response, toJson(page));
        }catch (Exception e){
           /* object.put("msg","sdf");
            object.put("count",1000);
            object.put("code",0);*/
            ResultUtils.write(response, toJson(page));
        }

    }

}
