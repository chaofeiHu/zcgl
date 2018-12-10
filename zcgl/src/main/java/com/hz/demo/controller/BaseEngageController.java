package com.hz.demo.controller;

import com.hz.demo.core.Page;
import com.hz.demo.core.PageData;
import com.hz.demo.core.ResultUtils;
import com.hz.demo.core.TableReturn;
import com.hz.demo.model.base_judging_process;
import com.hz.demo.model.base_speciality;
import com.hz.demo.services.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

import static com.hz.demo.core.ResultUtils.toJson;

;

/**
 * 评委会专家库
 */
@Controller
@RequestMapping("/BaseEngage")
public class BaseEngageController extends BaseController{

    @Autowired
    UserService userService;
    @Autowired
    SpecialityTicketsService specialityTicketsService;
    @Autowired
    SubjectGroupService subjectGroupService;
    @Autowired
    EngageService engageService;

   //跳到评委会专家界面
    @RequestMapping(value = "BaseEngagePage")
    public String DeptPage(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("MenuBtns",userService.GetMenuBtns(request.getParameter("menuid"),getUser().getUserId()));
        return "sys/baseengage";
    }


    //获取界面列表
    @RequestMapping(value = "selectEngagelistPage")
    public void selectEngagelistPage(HttpServletResponse response, Page page) {
        logger.info("获取评委会专家库列表");
        PageData pd = this.getPageData();
        if(pd.containsKey("JUDGING_CODE")){
            page.setPd(pd);
            List<PageData> list = engageService.selectEngageList(page);
            page.setData(list);
        }
        ResultUtils.write(response, toJson(page));
    }

    //删除专家
    @RequestMapping(value = "DeleteEngage")
    public void DeleteEngage(HttpServletRequest request, HttpServletResponse response) {
        try {
            PageData pd=this.getPageData();
            Integer num=engageService.deleteEngage(pd);
            if(num>0)
                ResultUtils.writeMessage(response, 1, "删除成功");
            else
                ResultUtils.writeMessage(response, 0, "删除失败");
        } catch (Exception e) {
            e.printStackTrace();
            ResultUtils.writeMessage(response, 0, "删除失败");
        }
    }

    /**
     * 添加专家
     * @param request
     * @param response
     */
    @RequestMapping(value = "addEngage")
    public void addEngage(HttpServletRequest request, HttpServletResponse response) {
        try {
            //String id=request.getParameter("ID");
            PageData pd=this.getPageData();
            //pd.put("JUDGING_ID",request.getParameter("JUDGING_ID"));
            pd.put("JUDGING_CODE",request.getParameter("JUDGING_CODE"));
            pd.put("SPECIALITY_ID",request.getParameter("SPECIALITY_ID"));
            pd.put("JURY_DUTY","3");
            pd.put("BEGIN_DATE",new Date());
            pd.put("ADDUSERID",getUser().getUserId());
            pd.put("STATE",1);
            Integer num=engageService.addEngage(pd);
            if(num>0)
                ResultUtils.writeMessage(response, 1, "添加成功");
            else
                ResultUtils.writeMessage(response, 0, "添加失败");
        } catch (Exception e) {
            e.printStackTrace();
            ResultUtils.writeMessage(response, 0, "添加失败");
        }
    }

    /**
     * 获取可随机专家数
     * @param request
     * @param response
     */
    @RequestMapping(value = "selectNoGroupSp")
    public void selectNoGroupSp(HttpServletRequest request, HttpServletResponse response) {
        try {
            PageData pd=new PageData();
            pd.put("JUDGING_CODE",request.getParameter("JUDGING_CODE"));
            //pd.put("JUDGING_ID",request.getParameter("JUDGING_ID"));
            String zhuanye=request.getParameter("zhuanye")==null ? "" :request.getParameter("zhuanye");
            pd.put("zhuanye",zhuanye);
            Integer num=engageService.selectNoGroupSp(pd);
            ResultUtils.writeMessage(response, 0, num.toString());
        } catch (Exception e) {
            e.printStackTrace();
            ResultUtils.writeMessage(response, 0, "添加失败");
        }
    }

    /***
     * 获取评委会系列绑定专业下拉框
     * @param request
     * @param response
     */
    @RequestMapping(value = "selectProfessial")
    public void selectProfessial(HttpServletRequest request, HttpServletResponse response) {
        try {
            PageData pd=new PageData();
            pd.put("JUDGING_CODE",request.getParameter("JUDGING_CODE"));
            List<PageData> item=engageService.selectProfessial(pd);
          /*  pd=new PageData();
            pd.put("PROFESSIAL_CODE","");
            pd.put("PROFESSIAL_NAME","全部");
            item.add(pd);*/
            ResultUtils.write(response, item);
        } catch (Exception e) {
            e.printStackTrace();
            ResultUtils.writeMessage(response, 0, "添加失败");
        }
    }

    /**
     * 随机添加专家
     * @param request
     * @param response
     */
    @Transactional(propagation= Propagation.REQUIRED)
    @RequestMapping(value = "addEngageRandom")
    public void addEngageRandom(HttpServletRequest request, HttpServletResponse response) {
        try {
            PageData pd=new PageData();
            pd.put("JUDGING_CODE",request.getParameter("JUDGING_CODE"));
            //pd.put("JUDGING_ID",request.getParameter("JUDGING_ID"));
            pd.put("specialityCount",request.getParameter("specialityCount"));
            String zhuanye=request.getParameter("zhuanye")==null ? "" :request.getParameter("zhuanye");
            pd.put("zhuanye",zhuanye);
            List<PageData> item=engageService.selectNoGroupSpRandom(pd);
            if(item.size()>0){
                int num=0;
                for (int i=0;i<item.size();i++){
                    //pd.put("judgingId",request.getParameter("JUDGING_ID"));
                    pd.put("SPECIALITY_ID",item.get(i));
                    //pd.put("judgingCode",request.getParameter("JUDGING_CODE"));
                    pd.put("JURY_DUTY","3");
                    pd.put("BEGIN_DATE",new Date());
                    pd.put("ADDUSERID",getUser().getUserId());
                    pd.put("STATE",1);
                    num+=engageService.addEngage(pd);
                }
                if(num==item.size()){
                    ResultUtils.writeMessage(response, 1, "添加成功");
                }else{
                    ResultUtils.writeMessage(response, 0, "添加失败");
                }
            }else{
                ResultUtils.writeMessage(response, 0, "添加失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ResultUtils.writeMessage(response, 0, "添加失败");
        }
    }

    /**
     * 修改出席 不出席理由
     * @param request
     * @param response
     */
   /* @Transactional(propagation= Propagation.REQUIRED)
    @RequestMapping(value = "saveEngageReason")
    public void saveEngageReason(HttpServletRequest request, HttpServletResponse response){
        logger.info("保存");
        try {
            String arrPerson=request.getParameter("arrPerson");
            JSONArray item=JSONArray.fromObject(arrPerson);
            for(int i=0;i<item.size();i++){
                JSONObject object=item.getJSONObject(i);
                PageData pageData=new PageData();
                pageData.put("id",object.get("ID"));
                pageData.put("type",object.get("TYPE"));
                pageData.put("reason",object.get("REASON"));
                pageData.put("back1","0");
                engageService.update(pageData);
            }
            ResultUtils.writeMessage(response, 1, "提交成功");
        } catch (Exception e) {
            e.printStackTrace();
            ResultUtils.writeMessage(response, 0, "提交失败");
        }
    }*/

    /**
     * 修改出席 不出席理由
     * @param request
     * @param response
     */
    @RequestMapping(value = "updateEngage")
    public void updateEngage(HttpServletRequest request, HttpServletResponse response){
        logger.info("修改");
        try {
             PageData pd=this.getPageData();
             engageService.update(pd);
             ResultUtils.writeMessage(response, 1, "提交成功");
        } catch (Exception e) {
            e.printStackTrace();
            ResultUtils.writeMessage(response, 0, "提交失败");
        }
    }

}
