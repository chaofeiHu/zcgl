package com.hz.demo.services;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hz.demo.core.PageData;
import com.hz.demo.dao.DaoSupportImpl;
import com.hz.demo.model.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("UserService")
public class UserService {

    @Resource(name = "daoSupportImpl")
    private DaoSupportImpl daoSupport;

    //添加用户信息
    public int add(sys_user model) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.insert("confing/mappers.sys_user.insertSelective", model);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }

    // 根据ID更新用户信息
    public int update(PageData pageData) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.update("confing/mappers.sys_user.updateByPrimaryKeySelective", pageData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }

    //根据ID删除用户信息
    public int delete(String id) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.delete("confing/mappers.sys_user.deleteByPrimaryKey", id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }

    //根据ID查询用户返回实体信息
    public sys_user getModel(String id) {
        sys_user user = null;
        try {
            user = (sys_user) daoSupport.findForObject("confing/mappers.sys_user.selectByPrimaryKey", id);
        } catch (Exception e) {
            e.printStackTrace();
            user = null;
        }
        return user;
    }

    //查询所有的用户信息
    public List<sys_user> getList(PageData pageData) {
        List<sys_user> userList = null;
        try {
            userList = (List<sys_user>) daoSupport.findForList("confing/mappers.sys_user.selectList", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            userList = null;
        }
        return userList;
    }

    //查询对应的记录总数
    public Integer getListCount(PageData pageData) {
        Integer ListCount = 0;
        try {
            ListCount = (Integer) daoSupport.findForObject("confing/mappers.sys_user.selectCount", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            ListCount = 0;
        }
        return ListCount;
    }

    //根据条件查询某个用户信息
    public sys_user getUserWhere(PageData pageData) {
        sys_user user = null;
        try {
            user = (sys_user) daoSupport.findForObject("confing/mappers.sys_user.getUserWhere", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            user = null;
        }
        return user;
    }

    public String GetMenuBtns(String MenuId, String UserId) {
        PageData pd = new PageData();
        pd.put("MenuId", MenuId);//菜单id
        pd.put("UserId", UserId);//当前登录用户id
        return GetMenuFun(pd);
    }

    //根据当前登录用户获取具体菜单按钮权限
    public String GetMenuFun(PageData pageData) {
        String strResult = "";
        List<Map<String, String>> hashMaps = null;
        try {
            if(getModel(pageData.get("UserId").toString()).getUserType()==1){ //专家
                hashMaps = (List<Map<String, String>>) daoSupport.findForList("confing/mappers.sys_menufun.GetMenuFunTwo", pageData);
            }else{
                hashMaps = (List<Map<String, String>>) daoSupport.findForList("confing/mappers.sys_menufun.GetMenuFun", pageData);
            }

            for (Map<String, String> map : hashMaps) {
                strResult += map.get("BTNCODE") + ",";
            }
        } catch (Exception e) {
            e.printStackTrace();
            strResult = "";
        }
        return strResult;
    }


    //根据条件查询机构管理员用户信息
    public List<sys_user> getUserListWhere(PageData pd) {
        List<sys_user> userList = null;
        try {
            userList = (List<sys_user>) daoSupport.findForList("confing/mappers.sys_user.getUserListWhere", pd);
        } catch (Exception e) {
            e.printStackTrace();
            userList = null;
        }
        return userList;
    }

    //根据条件查询机构下所有用户信息
   /* public List<sys_user> getUserList(PageData pd) {
        List<sys_user> userList = null;
        try {
            userList = (List<sys_user>) daoSupport.findForList("confing/mappers.sys_user.getUserList", pd);
        } catch (Exception e) {
            e.printStackTrace();
            userList = null;
        }
        return userList;
    }
*/
    public Integer deleteWhere(PageData pageData) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.delete("confing/mappers.sys_user.deleteWhere", pageData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }

    public Integer updateByUnitCode(PageData pageData) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.update("confing/mappers.sys_user.updateByUnitCode", pageData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }

    public List<PageData> selectTBXL(PageData pageData) {
        List<PageData> userList = null;
        try {
            userList = (List<PageData>) daoSupport.findForList("confing/mappers.sys_user.selectTBXL", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            userList = null;
        }
        return userList;
    }
    public List<PageData> selectTBJB(PageData pageData) {
        List<PageData> userList = null;
        try {
            userList = (List<PageData>) daoSupport.findForList("confing/mappers.sys_user.selectTBJB", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            userList = null;
        }
        return userList;
    }
    public PageData selectRole(PageData pageData) {
        PageData userList = null;
        try {
            userList = (PageData) daoSupport.findForObject("confing/mappers.sys_user.selectRole", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            userList = null;
        }
        return userList;
    }
    public List<PageData> selectProposerMsg(PageData pageData) {
        List<PageData> userList = null;
        try {
            userList = (List<PageData>) daoSupport.findForList("confing/mappers.sys_user.selectProposerMsg", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            userList = null;
        }
        return userList;
    }

    /**
     * 专家待办事宜
     * @param user
     * @return
     * @throws Exception
     */
    public JSONArray selectAgencySplit(sys_user user) throws Exception{
        JSONArray jsonArray=new JSONArray();
        PageData pageData=(PageData) daoSupport.findForObject("confing/mappers.sys_user.selectSplitTickets", user.getSpecialityId());
        if(pageData!=null){
            JSONObject object1=new JSONObject();
            object1.put("item",pageData.get("COU"));
            object1.put("url","/SpecialityTickets/SpecialityPage.do?type=1&");
            object1.put("name","专业组投票");
            jsonArray.add(object1);
        }
        PageData pd=(PageData) daoSupport.findForObject("confing/mappers.sys_user.selectSplitTicketsTwo", user.getSpecialityId());
        if(pd!=null){
            JSONObject object1=new JSONObject();
            object1.put("item",pd.get("COU"));
            object1.put("url","/SpecialityTickets/SpecialityPage.do?type=2&");
            object1.put("name","评委会投票");
            jsonArray.add(object1);
        }
        return jsonArray;
    }


    /**
     * 查询代办事宜
     * @return
     */
    public JSONArray selectAgency(sys_user user) throws Exception{
        PageData pd=new PageData();
        pd.put("USER_ID",user.getUserId());
        pd=selectRole(pd);
        Integer num=Integer.valueOf(pd.get("FSORT").toString());
        switch (num){
            case 1001://省职称办系统管理员
                pd.put("JUDGING_STAGE","15");
                pd.put("JUDGING_NAME","省职改办审查");
                break;
            case 1002: //省职称办  工作人员
                pd.put("JUDGING_STAGE","15");
                pd.put("JUDGING_NAME","省职改办审查");
                break;
            case 1003: //省职称办  领导
                pd.put("JUDGING_STAGE","15");
                pd.put("JUDGING_NAME","省职改办审查");
                break;
            case 2001://市职称办 系统管理员
                pd.put("JUDGING_STAGE","11,12");
                pd.put("JUDGING_NAME","市职改办审查");
                break;
            case 2002: //市职称办  工作人员
                pd.put("JUDGING_STAGE","11");
                pd.put("JUDGING_NAME","市职改办审查");
                break;
            case 2003: //市职称办  领导
                pd.put("JUDGING_STAGE","12");
                pd.put("JUDGING_NAME","市职改办审查");
                break;
            case 3001://县职称办系统管理员
                pd.put("JUDGING_STAGE","9,10");
                pd.put("JUDGING_NAME","县职改办审查");
                break;
            case 3002: //县职称办  工作人员
                pd.put("JUDGING_STAGE","9");
                pd.put("JUDGING_NAME","县职改办审查");
                break;
            case 3003: //县职称办  领导
                pd.put("JUDGING_STAGE","10");
                pd.put("JUDGING_NAME","县职改办审查");
                break;
            case 4001://主管职称办系统管理员
                pd.put("JUDGING_STAGE","7,8");
                pd.put("JUDGING_NAME","主管部门审查");
                break;
            case 4002: //主管  工作人员
                pd.put("JUDGING_STAGE","7");
                pd.put("JUDGING_NAME","主管部门审查");
                break;
            case 4003: //主管  领导
                pd.put("JUDGING_STAGE","8");
                pd.put("JUDGING_NAME","主管部门审查");
                break;
            case 5001://单位系统管理员
                pd.put("JUDGING_STAGE","5,6");
                pd.put("JUDGING_NAME","单位审查");
                break;
            case 5002: //单位  工作人员
                pd.put("JUDGING_STAGE","5");
                pd.put("JUDGING_NAME","单位审查");
                break;
            case 5003: //单位  领导
                pd.put("JUDGING_STAGE","6");
                pd.put("JUDGING_NAME","单位审查");
                break;
            case 6001://下属系统管理员
                pd.put("JUDGING_STAGE","3,4");
                pd.put("JUDGING_NAME","下属单位审查");
                break;
            case 6002: //下属  工作人员
                pd.put("JUDGING_STAGE","3");
                pd.put("JUDGING_NAME","下属单位审查");
                break;
            case 6003: //下属  领导
                pd.put("JUDGING_STAGE","4");
                pd.put("JUDGING_NAME","下属单位审查");
                break;
            case 7001://基层系统管理员
                pd.put("JUDGING_STAGE","1,2");
                pd.put("JUDGING_NAME","基层单位审查");
                break;
            case 7002: //基层  工作人员
                pd.put("JUDGING_STAGE","1");
                pd.put("JUDGING_NAME","基层单位审查");
                break;
            case 7003: //基层  领导
                pd.put("JUDGING_STAGE","2");
                pd.put("JUDGING_NAME","基层单位审查");
                break;
        }
        List<PageData> item=selectProposerMsg(pd);
        JSONArray jsonArray=new JSONArray();
        for(int i=0;i<item.size();i++){
            if(Integer.valueOf(item.get(i).get("COU").toString())!=0){
                JSONObject object=new JSONObject();
                object.put("item",item.get(i).get("COU"));
                object.put("url","/JudgingProposer/toJudgingProposer.do?currentJudgingStage="+item.get(i).get("JUDGING_STAGE"));
                object.put("name",pd.get("JUDGING_NAME"));
                jsonArray.add(object);
            }

        }
        PageData after=(PageData) daoSupport.findForObject("confing/mappers.sys_user.selectafter",user.getUnitCode());
        if(Integer.valueOf(after.get("COU").toString())!=0){
            JSONObject object=new JSONObject();
            object.put("item",after.get("COU"));
            object.put("url","/ReviewResult/ReviewResultPage.do");
            object.put("name","评审结果审核");
            jsonArray.add(object);
        }

        //查询当前用户是否具有 专家推荐审查 权限
        PageData have=(PageData) daoSupport.findForObject("confing/mappers.sys_user.selectHave",num.toString());
        if(have!=null){
            PageData split=(PageData) daoSupport.findForObject("confing/mappers.sys_user.selectSplit",null);
            if(Integer.valueOf(split.get("COU").toString())!=0){
                JSONObject object1=new JSONObject();
                object1.put("item",split.get("COU"));
                object1.put("url","/CreateSpeciality/toJudgingSpecialityMsg.do");
                object1.put("name","专家推荐审查");
                jsonArray.add(object1);
            }
        }
        return jsonArray;
    }
}
