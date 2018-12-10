package com.hz.demo.controller;

import com.hz.demo.core.Page;
import com.hz.demo.core.PageData;
import com.hz.demo.core.ResultUtils;
import com.hz.demo.core.TableReturn;
import com.hz.demo.model.*;
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
import java.util.ArrayList;
import java.util.List;

import static com.hz.demo.core.ResultUtils.toDateTimeJson;
import static com.hz.demo.core.ResultUtils.toJson;

@Controller
@RequestMapping("/SpecialityGroup")
public class SpecialityGroupController extends BaseController{

    @Autowired
    UserService userService;
    @Autowired
    SpecialityGroupService specialityGroupService;
    @Autowired
    BaseJudgingService baseJudgingService;
    @Autowired
    BaseProfessialService baseProfessialService;
    @Autowired
    BaseJudingSeriesService baseJudingSeriesService;

   //跳到评委库专家分组界面
    @RequestMapping(value = "SpecialityGroupPage")
    public String DeptPage(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("MenuBtns",userService.GetMenuBtns(request.getParameter("menuid"),getUser().getUserId()));
        return "sys/specialityGroup";
    }

    //获取界面列表
    @RequestMapping(value = "getList")
    public void getList(HttpServletResponse response, Page page) {
        logger.info("显示评委库专家分组列表数据");
        PageData pd = this.getPageData();
        if(pd.containsKey("JUDGING_CODE")){
            page.setPd(pd);
            List<PageData> list = specialityGroupService.getlistPage(page);
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
    @RequestMapping(value = "addOrUpdate")
    public void AddOrUpdate(HttpServletRequest request, HttpServletResponse response) {
        logger.info("评委库专家分组添加或更新（获取）");
        String ids = request.getParameter("id") == null ? "" : request.getParameter("id");
        if (!"".equals(ids)) {
            //更新（获取）
            Integer id = Integer.parseInt(ids);
            speciality_group specialityGroup = specialityGroupService.getModel(id);
            if (specialityGroup != null) {
                ResultUtils.write(response, toJson(specialityGroup));
            } else {
                ResultUtils.write(response, "记录信息不存在！");
            }
        }
    }

    //保存
    @RequestMapping(value = "save")
    public void Save(HttpServletRequest request, HttpServletResponse response) {
        logger.info("保存");
        PageData pd=this.getPageData();
        try {
            specialityGroupService.add(pd);
            ResultUtils.writeMessage(response, 1, "添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            ResultUtils.writeMessage(response, 0, "添加失败");
        }
    }

    //删除
    @Transactional(propagation= Propagation.REQUIRED)
    @RequestMapping(value = "delete")
    public void Delete(HttpServletRequest request, HttpServletResponse response){
        PageData pd=this.getPageData();
        String[] strs = pd.getString("id").split(",");
        try {
            if (strs.length>0) {
                for (String id:strs) {
                    specialityGroupService.delete(Integer.valueOf(id));
                }
            }
            ResultUtils.writeMessage(response, 1, "删除成功");
        } catch (Exception ex) {
            ResultUtils.writeMessage(response, 0, ex.getMessage());
        }
    }

    //获取分组下专家界面列表
    @RequestMapping(value = "getSpecialityList")
    public void getSpecialityList(HttpServletResponse response,Page page) {
        logger.info("获取分组下专家界面列表");
        PageData pd = this.getPageData();
       /* pd.put("rows", request.getParameter("rows") == null? 10: Integer.parseInt(request.getParameter("rows")));//每页显示行数
        pd.put("page", request.getParameter("page") == null ? 1 : (Integer.parseInt(request.getParameter("page"))));//每页记录开始位置
        pd.put("groupId", request.getParameter("groupId"));
        pd.put("judgingCode", request.getParameter("judgingCode"));
        pd.put("specialityName", request.getParameter("specialityName"));
        pd.put("sta", request.getParameter("sta"));*/
        page.setPd(pd);
        List<PageData> list = specialityGroupService.getSpecialitylistPage(page);
        page.setData(list);
        ResultUtils.write(response, toJson(page));
    }

    /**
     * 查询某专业未被评委会聘请的人数
     * @param request
     * @param response
     */
    @RequestMapping(value = "selectSubjectSpecialityCount")
    public void selectSubjectSpecialityCount(HttpServletRequest request, HttpServletResponse response) {
        String groupId = request.getParameter("groupId");
        try {
            PageData pd = new PageData();
            pd.put("groupId", groupId);
            pd.put("professialId",request.getParameter("professialId"));
            pd=specialityGroupService.selectSubjectSpecialityCount(pd);
            ResultUtils.write(response, toJson(pd.get("LISTCOUNT")));
        } catch (Exception ex) {
            ResultUtils.writeMessage(response, 0, ex.getMessage());
        }
    }

    //随机获取部分专家
    @RequestMapping(value = "getSpecialityByRandom")
    public void getSpecialityByRandom(HttpServletRequest request, HttpServletResponse response) {
        logger.info("获取分组下专家界面列表");
        PageData pd = this.getPageData();
       try{
           //随机获取n个专家
           List<base_engage> blist = specialityGroupService.getSpecialityByRandom(pd);
           Integer count = 0;
           //将专家添加到分组中
           if (null != blist) {
               for (base_engage baseEngage : blist)    {
                   String specialityId = baseEngage.getSpecialityId();
                   pd.put("SPECIALITY_ID",specialityId);
                   specialityGroupService.add(pd);
               }
           }
           ResultUtils.writeMessage(response, 1, "获取专家成功");
       }catch (Exception e){
           ResultUtils.writeMessage(response, 0, "获取专家失败");
       }
    }
    @Transactional(propagation= Propagation.REQUIRED)
    @RequestMapping(value = "updateGroup")
    public void updateGroup( HttpServletResponse response) {
        PageData pd=this.getPageData();
   /*     pd.put("GROUP_ID",request.getParameter("GROUP_ID"));
        pd.put("id",request.getParameter("id"));*/
        try{
            specialityGroupService.updateGroupTwo(pd); //设置组长
            specialityGroupService.updateGroup(pd);//之前组长更改状态
            ResultUtils.writeMessage(response, 1, "修改成功");
        }catch (Exception e){
            e.printStackTrace();
            ResultUtils.writeMessage(response, 2, "修改失败!");
        }
    }

    /**
     * 学科组手工分配专家列表
     * @param response
     */
    @RequestMapping("selectManualGroup.do")
    public void selectManualGroup(HttpServletResponse response,Page page) {
        logger.info("学科组手工分配专家列表");

        PageData pd =this.getPageData();
       /* pd.put("rows", request.getParameter("rows") == null? 10: Integer.parseInt(request.getParameter("rows")));
        pd.put("page", request.getParameter("page") == null ? 0 : (Integer.parseInt(request.getParameter("page"))));
        pd.put("SPECIALITY_NAME",request.getParameter("SPECIALITY_NAME") == null ? "" : request.getParameter("SPECIALITY_NAME"));
        pd.put("NOWUNIT",request.getParameter("NOWUNIT") == null ? "" : request.getParameter("NOWUNIT"));
        pd.put("RECOMMEND_MAJOR",request.getParameter("RECOMMEND_MAJOR") == null ? "" : request.getParameter("RECOMMEND_MAJOR"));
        pd.put("GROUP_ID",request.getParameter("GROUP_ID") == null ? "" : request.getParameter("GROUP_ID"));*/

        page.setPd(pd);
        List<PageData> list = baseJudingSeriesService.selectManualGrouplistPage(page);
        page.setData(list);
        ResultUtils.write(response, toJson(page));
    }

    /***
     * 学科组确认是否出席
     * @param response
     */
    @RequestMapping(value = "updateSpecialityGroup")
    public void updateSpecialityGroup(HttpServletResponse response) {
        PageData pd=this.getPageData();
       /* pd.put("GROUP_ID",request.getParameter("GROUP_ID"));
        pd.put("id",request.getParameter("id"));*/
        try{
           /* String arrPerson=request.getParameter("arrPerson");
            JSONArray item=JSONArray.fromObject(arrPerson);*/
            /*for(int i=0;i<item.size();i++){
                JSONObject object=item.getJSONObject(i);
                PageData pageData=new PageData();
                pageData.put("id",object.get("ID"));
                pageData.put("type",object.get("TYPE"));
                pageData.put("reason",object.get("reason"));

            }*/
            specialityGroupService.update(pd);
            ResultUtils.writeMessage(response, 1, "提交成功");
        }catch (Exception e){
            e.printStackTrace();
            ResultUtils.writeMessage(response, 2, "修改失败!");
        }
    }
    /***
     * 获取学科组绑定专业下拉框
     * @param request
     * @param response
     */
    @RequestMapping(value = "selectProfessial")
    public void selectProfessial(HttpServletRequest request, HttpServletResponse response) {
        try {
            PageData pd=new PageData();
            pd.put("professialId",request.getParameter("professialId"));
            List<PageData> item=specialityGroupService.selectProfessial(pd);
            ResultUtils.write(response, item);
        } catch (Exception e) {
            e.printStackTrace();
            ResultUtils.writeMessage(response, 0, "添加失败");
        }
    }

}
