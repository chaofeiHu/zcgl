package com.hz.demo.controller;

import com.hz.demo.core.PageData;
import com.hz.demo.core.ResultUtils;
import com.hz.demo.core.Tree;
import com.hz.demo.model.base_judging_process;
import com.hz.demo.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;
import static com.hz.demo.core.ResultUtils.toJson;

/**
 * 评审流程控制
 */
@Controller
@RequestMapping("/JudgingProcess")
public class BaseJudgingProcessController extends BaseController{
    @Autowired
    UserService userService;
    @Autowired
    BaseJudingProcessService baseJudingProcessService;
    @Autowired
    BaseProposerService baseProposerService;

    @RequestMapping(value = "JudgingProcessPage")
    public String JudgingPage(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("MenuBtns",userService.GetMenuBtns(request.getParameter("menuid"),getUser().getUserId()));
        return "sys/judgingProcess";
    }

    //添加或更新（获取）
    //@Transactional(propagation= Propagation.REQUIRED)
    @RequestMapping(value = "AddOrUpdate")
    public void AddOrUpdate(HttpServletRequest request, HttpServletResponse response) {
        logger.info("添加或更新（获取）");
        PageData pd=this.getPageData();
        String id = pd.getString("BID") == null ? "" : pd.getString("BID");
        String PROCESS_STATE=pd.getString("PROCESS_STATE") == null ? "" : pd.getString("PROCESS_STATE");
        String PROCESS_GROUP=pd.getString("PROCESS_GROUP") == null ? "" :pd.getString("PROCESS_GROUP");
        String JUDGING_CODE=pd.getString("JUDGING_CODE") == null ? "" : pd.getString("JUDGING_CODE");
        String PROCESS_TYPE=pd.getString("PROCESS_TYPE") == null ? "" : pd.getString("PROCESS_TYPE");
       try{
          if (id.equals("")) {
               //添加
               base_judging_process base_judging_process=new base_judging_process();
               base_judging_process.setJudgingCode(JUDGING_CODE);
               if(!PROCESS_STATE.equals(""))
                   base_judging_process.setProcessState(Long.valueOf(PROCESS_STATE));
               if(!PROCESS_GROUP.equals(""))
                   base_judging_process.setProcessGroup(Long.valueOf(PROCESS_GROUP));
               if(!PROCESS_TYPE.equals(""))
                   base_judging_process.setProcessType(1L);
               base_judging_process.setCreateTime(new Date());
               Calendar cale = Calendar.getInstance();
               base_judging_process.setYearNo(String.valueOf(cale.get(Calendar.YEAR)));
              base_judging_process.setProcessPattern(pd.getString("PROCESS_PATTERN"));
               baseJudingProcessService.add(base_judging_process);
               ResultUtils.writeMessage(response, 1, "添加成功");
           } else {
               base_judging_process base_judging_process=new base_judging_process();
                  base_judging_process.setProcessPattern(pd.getString("PROCESS_PATTERN"));
               if(!PROCESS_STATE.equals(""))
                   base_judging_process.setProcessState(Long.valueOf(PROCESS_STATE));
               if(!PROCESS_GROUP.equals("")){
                   if(Integer.valueOf(PROCESS_GROUP)==2){
                       if(!PROCESS_TYPE.equals("")){
                           if(Integer.valueOf(PROCESS_TYPE)==5){
                               base_judging_process.setProcessType(1L);
                           }else{
                               base_judging_process.setProcessType(Long.valueOf(PROCESS_TYPE));
                           }
                       }
                   }else{
                       if(!PROCESS_TYPE.equals("")){
                           base_judging_process.setProcessType(Long.valueOf(PROCESS_TYPE));
                       }
                   }
                   base_judging_process.setProcessGroup(Long.valueOf(PROCESS_GROUP));
               }
               base_judging_process.setId(new BigDecimal(id));
              /* if(!PROCESS_TYPE.equals(""))
                   base_judging_process.setProcessType(Long.valueOf(PROCESS_TYPE));*/
               if(PROCESS_TYPE.equals("8")){
                   PageData pageData =new PageData();
                   pageData.put("currentState",19);
                   pageData.put("baseJudgingSeries",request.getParameter("JUDGING_CODE"));
                   baseJudingProcessService.updateJudgingProposerStage(pageData);
               }
               baseJudingProcessService.update(base_judging_process);
           }
           ResultUtils.writeMessage(response, 1, "更新成功");
       }catch (Exception e){
           ResultUtils.writeMessage(response, 2, "更新失败");
       }
    }

    //添加或更新（获取）
    @RequestMapping(value = "selectProcess")
    public void selectProcess(HttpServletRequest request, HttpServletResponse response) {
        logger.info("获取评审流程控制信息");
        String JUDGING_CODE = request.getParameter("JUDGING_CODE") == null ? "" : request.getParameter("JUDGING_CODE");
        base_judging_process juding = baseJudingProcessService.getModel(JUDGING_CODE);
        if (juding != null) {
            ResultUtils.write(response, toJson(juding));
        } else {
            ResultUtils.write(response, toJson("msg"));
        }
    }

    //获取组织结构表树
    @RequestMapping(value = "getBaseJudingCodeTree")
    public void getBaseUnitTree(HttpServletRequest request, HttpServletResponse response) {
        String ParentID =request.getParameter("parentId") == null ? "410000" : request.getParameter("parentId");
        List<Tree> menuTreeList = baseJudingProcessService.getListWhere(getUser().getUnitCode());
        ResultUtils.write(response, toJson(menuTreeList));
    }
    //判断是否能够进入下一步
    @RequestMapping(value = "selectJudgingProcess")
    public void selectJudgingProcess(HttpServletRequest request, HttpServletResponse response) {
        logger.info("获取评审流程控制信息");
        PageData pageData=getPageData();
        try{
            pageData =baseJudingProcessService.selectJudgingProcess(pageData);
        }catch (Exception e){
            e.printStackTrace();
            pageData.put("msg","出错了");
        }
        ResultUtils.write(response, toJson(pageData));
    }
}
