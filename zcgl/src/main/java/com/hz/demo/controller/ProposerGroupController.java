package com.hz.demo.controller;

import com.hz.demo.core.Page;
import com.hz.demo.core.PageData;
import com.hz.demo.core.ResultUtils;
import com.hz.demo.core.TableReturn;
import com.hz.demo.model.*;
import com.hz.demo.services.BaseJudgingService;
import com.hz.demo.services.BaseProfessialService;
import com.hz.demo.services.ProposerGroupService;
import com.hz.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

import static com.hz.demo.core.ResultUtils.toDateTimeJson;
import static com.hz.demo.core.ResultUtils.toJson;

@Controller
@RequestMapping("/ProposerGroup")
public class ProposerGroupController extends BaseController{

    @Autowired
    UserService userService;
    @Autowired
    ProposerGroupService proposerGroupService;
    @Autowired
    BaseJudgingService baseJudgingService;
    @Autowired
    BaseProfessialService baseProfessialService;

   //跳到参评人分组界面
    @RequestMapping(value = "ProposerGroupPage")
    public String DeptPage(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("MenuBtns",userService.GetMenuBtns(request.getParameter("menuid"),getUser().getUserId()));
        return "sys/proposerGroup";
    }

    //获取界面列表
    @RequestMapping(value = "getList")
    public void getList( HttpServletResponse response,Page page) {
        logger.info("显示参评人分组列表数据");
        PageData pd =this.getPageData();
        if(pd.containsKey("JUDGING_CODE")){
            page.setPd(pd);
            List<PageData> list = proposerGroupService.selectlistPage(page);
            if(list.size()>0){
                for(int i=0;i<list.size();i++){
                    PageData pageData=list.get(i);
                    PageData baseProfessial = baseProfessialService.findProfessial(pageData);
                    list.get(i).put("PROFESSIAL_NAME",baseProfessial.getString("PROFESSIAL_NAME"));
                }
            }
            page.setData(list);
        }
        ResultUtils.write(response, toJson(page));
    }
    
    //添加或更新（获取）
    /*@RequestMapping(value = "addOrUpdate")
    public void AddOrUpdate(HttpServletRequest request, HttpServletResponse response) {
        logger.info("参评人分组添加或更新（获取）");
        String ids = request.getParameter("id") == null ? "" : request.getParameter("id");
        if (!"".equals(ids)) {
            //更新（获取）
            Integer id = Integer.parseInt(ids);
            proposer_group proposerGroup = proposerGroupService.getModel(id);
            if (proposerGroup != null) {
                ResultUtils.write(response, toJson(proposerGroup));
            } else {
                ResultUtils.write(response, "记录信息不存在！");
            }
        }
    }*/

    //保存
    @RequestMapping(value = "save")
    public void Save(HttpServletRequest request, HttpServletResponse response) {
        logger.info("保存");
       /* String ids = request.getParameter("ids") == null ? "" : request.getParameter("ids");
        String[] strs = ids.split(",");
        String groupId = request.getParameter("groupId");*/
        PageData pd=this.getPageData();
        int count = 0;
        try {
            /*for (String id:strs) {
                proposer_group proposerGroup = new proposer_group();
                proposerGroup.setGroupId(groupId);
                proposerGroup.setProposerId(id);
                PageData pageData = new PageData();
                pageData.put("groupId", groupId);
                pageData.put("proposerId", id);
                count += proposerGroupService.add(proposerGroup);
            }*/
            proposer_group proposerGroup = new proposer_group();
            proposerGroup.setGroupId(pd.getString("GROUP_ID"));
            proposerGroup.setProposerId(pd.getString("ids"));
            count = proposerGroupService.add(proposerGroup);
            if (count > 0) {
                ResultUtils.writeMessage(response, 1, "添加成功");
            } else {
                ResultUtils.writeMessage(response, 0, "添加失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ResultUtils.writeMessage(response, 0, "添加失败");
        }
    }

    //删除
    @RequestMapping(value = "delete")
    public void Delete(HttpServletRequest request, HttpServletResponse response) {
        String ids = request.getParameter("ids") == null ? "" : request.getParameter("ids");
        String[] strs = ids.split(",");
        try {
            for (String id:strs) {
                Integer id1 = Integer.parseInt(id);
                if (proposerGroupService.delete(id1) == 1)
                    ResultUtils.writeMessage(response, 1, "删除成功");
                else
                    ResultUtils.writeMessage(response, 0, "删除失败");
            }
        } catch (Exception ex) {
            ResultUtils.writeMessage(response, 0, ex.getMessage());
        }
    }

    //获取分组下或未分组参评人界面列表
    @RequestMapping(value = "getProposerList")
    public void getProposerList(HttpServletResponse response,Page page) {
        logger.info("获取分组下或未分组参评人界面列表");
        PageData pd =this.getPageData();
        List<PageData> list = new ArrayList<PageData>();
        page.setPd(pd);
        if(pd.getString("sta").equals("1")){
            list = proposerGroupService.getNoGroupProposerlistPage(page);
        }else{
            list = proposerGroupService.getProposerlistPage(page);
        }
        page.setData(list);
        ResultUtils.write(response, toJson(page));
    }
}
