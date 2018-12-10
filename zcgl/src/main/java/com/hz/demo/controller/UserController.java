package com.hz.demo.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hz.demo.core.*;
import com.hz.demo.model.*;
import com.hz.demo.services.*;
import net.sf.json.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import static com.hz.demo.core.EncodeUtil.encodeMD5;
import static com.hz.demo.core.ResultUtils.toJson;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
@RequestMapping("/User")
public class UserController extends BaseController {

    @Autowired
    UserService userService;
    @Autowired
    TokenService tokenService;
    @Autowired
    MenuService menuService;
    @Autowired
    BaseUnitService baseUnitService;
    @Autowired
    SpecialityService specialityService;


    //跳转到主页
    @RequestMapping("/index.do")
    public String baseIndex() {
        return "index";
    }

    //跳转到登陆页面
    @RequestMapping("/baseLogin.do")
    public String baseLogin() {
        return "login";
    }

    //跳转到用户管理页面
    @RequestMapping(value = "UserPage")
    public String UserPage(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("MenuBtns",userService.GetMenuBtns(request.getParameter("menuid"),getUser().getUserId()));
        return "sys/user";
    }

    //系统用户登录
    @RequestMapping(value = "login")
    public String login(HttpSession session, HttpServletRequest request, HttpServletResponse response) {
        logger.info("用户登录");
        try {
            Map<String, String> map = new HashMap<>();
            String username = request.getParameter("username") == null ? "" : request.getParameter("username");
            String password = encodeMD5(request.getParameter("password") == null ? "" : request.getParameter("password"));
            String code1 = (request.getParameter("code") == null ? "" : request.getParameter("code"));
            String code2 = (String) session.getAttribute("kaptcha");
            //用户类型
            String achiresult=request.getParameter("achiresult") == null ? "" : request.getParameter("achiresult");
            PageData pageData = new PageData();
            pageData.put("loginName", username);
            pageData.put("achiresult",achiresult);
            sys_user user = userService.getUserWhere(pageData);
            if (user != null) {
                List<PageData> item = new ArrayList<PageData>();
                if (achiresult.equals("1")) {
                    item = specialityService.loginEngage(user.getUserId());
                }
                map.put("isOk", "0");
                if (!code1.equals(code2)) {
                    map.put("message", "验证码错误!");
                } else if (item.size() == 0 && achiresult.equals("1")) {
                    map.put("message", "未找到所属评委会！");
                } else {
                    if (user.getState() == 0)
                        map.put("message", "您的账户已被锁定，请联系管理员!");
                    else if (!user.getPassword().equals(password))
                        map.put("message", "您输入的密码不正确，请确认后重新输入!");
                    else {
                                //设置用户登录Token
                        tokenService.deleteTokenByUserId(user.getUserId());
                        PageData tokenPageData = new PageData();
                        tokenPageData.put("toKenId", UUID.randomUUID().toString());
                        tokenPageData.put("userId", user.getUserId());
                        tokenPageData.put("createDate", new Date());
                        tokenPageData.put("state", 1);
                        tokenService.addToken(tokenPageData);
                        //设置用户Session
                        this.getSession().setAttribute("User", user);
                        map.put("isOk", "1");
                        map.put("message", "登录成功");
                        if (item.size()>=1) {
                            getUser().setJudgingName(item.get(0).getString("JUDGING_NAME"));
                            getUser().setJudgingCode(item.get(0).getString("JUDGING_CODE"));
                            getUser().setSpecialityId(item.get(0).get("SPECIALITY_ID").toString());
                        }
                    }
                    ResultUtils.write(response, map);
                    //记录数据库操作日志
                    WriteLog("登录成功");
                }
            } else {
                map.put("isOk", "0");
                map.put("message", "用户不存在");
            }
            ResultUtils.write(response, map);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    //安全退出
    @RequestMapping(value = "loginOut")
    public void loginOut(HttpServletRequest request, HttpServletResponse response) {
        logger.info("安全退出");
        try {
            WriteLog("安全退出");
            this.getSession().setAttribute("User",null);
            ResultUtils.writeMessage(response, 1, "");
        } catch (Exception e) {
            logger.error(e.getMessage());
            ResultUtils.writeMessage(response, 0, "");
        }
    }

    //修改密码
    @RequestMapping(value = "ChangePassword")
    public void ChangePassword(HttpServletRequest request, HttpServletResponse response) {
        logger.info("修改密码");
        String OldPassword = encodeMD5(request.getParameter("OldPassword") == null ? "" : request.getParameter("OldPassword"));
        String NewPassword = encodeMD5(request.getParameter("NewPassword") == null ? "" : request.getParameter("NewPassword"));
        PageData pageData = new PageData();
        pageData.put("password", NewPassword);
        pageData.put("userId", getUser().getUserId());
        try {
            if (!getUser().getPassword().equals(OldPassword))
                ResultUtils.writeMessage(response, 0, "旧密码错误");
                else if (userService.update(pageData) == 1) {
                    getUser().setPassword(NewPassword);
                this.getSession().setAttribute("User", getUser());
                WriteLog("修改密码");
                ResultUtils.writeMessage(response, 1, "密码修改成功");
            } else {
                ResultUtils.writeMessage(response, 0, "密码修改失败");
            }
        } catch (Exception ex) {
            ResultUtils.writeMessage(response, 0, ex.getMessage());
        }
    }


    //获取界面列表
    @RequestMapping(value = "getList")
    public void getList(HttpServletRequest request, HttpServletResponse response) {
        logger.info("显示单位用户列表数据");
        PageData pd = new PageData();
        pd.put("rows", request.getParameter("rows") == null? 10: Integer.parseInt(request.getParameter("rows")));//每页显示行数
        pd.put("page", request.getParameter("page") == null ? 0 : (Integer.parseInt(request.getParameter("page"))));//每页记录开始位置
        pd.put("unitCode",getUser().getUnitCode() );//单位编码
        pd.put("displayName", request.getParameter("displayName") == null ? "" : request.getParameter("displayName"));//用户显示名称

        TableReturn tablereturn = new TableReturn();
        List<sys_user> list = userService.getList(pd);
        Integer listCount = userService.getListCount(pd);
        tablereturn.setRows(list);
        tablereturn.setTotal(listCount);

        ResultUtils.write(response, toJson(tablereturn));
    }

    //更改用户状态
    @RequestMapping(value = "UpdateState")
    public void UpdateState(HttpServletRequest request, HttpServletResponse response) {
        logger.info("更改用户状态");
        PageData pd = new PageData();
        try {
            pd.put("userId", request.getParameter("userId") == null ? "" : request.getParameter("userId"));//ID
            String state = request.getParameter("state") == null ? "0" : request.getParameter("state");
            Integer sta = Integer.parseInt(state);
            if (sta == 0) {
                sta = 1;
            } else {
                sta = 0;
            }
            pd.put("state", sta);//状态
            Map<String, String> map = new HashMap<>();
            map.put("isOk", "0");
            if (userService.update(pd) == 1)
                map.put("isOk", "1");
            WriteLog("更改id为" + request.getParameter("userId") + "的用户状态");
            ResultUtils.write(response, toJson(map));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    //添加或更新（获取）
    @RequestMapping(value = "AddOrUpdate")
    public void AddOrUpdate(HttpServletRequest request, HttpServletResponse response) {
        logger.info("部门添加或更新（获取）");
        String userId = request.getParameter("userId") == null ? "" : request.getParameter("userId");
        if (userId.equals("")) {
            //添加
        } else {
            //更新（获取）
            sys_user model = userService.getModel(userId);
            if (model != null) {
                ResultUtils.write(response, toJson(model));
            } else {
                ResultUtils.write(response, "记录信息不存在！");
            }
        }

    }

    //保存
    @RequestMapping(value = "Save")
    public void Save(HttpServletRequest request, HttpServletResponse response,String loginName) {
        logger.info("保存");
        String userId = request.getParameter("userId") == null ? "" : request.getParameter("userId");
        sys_user model = null;
        if (userId.equals("")) { //添加
            model = new sys_user();
            model.setUserId(UUID.randomUUID().toString());
            model.setDisplayName(request.getParameter("displayName"));
            model.setLoginName(request.getParameter("loginName"));
            model.setPassword(encodeMD5("000000"));
            model.setEmail(request.getParameter("email"));
            model.setMobilephone(request.getParameter("mobilephone"));
            String mobilephone = request.getParameter("mobilephone");
            String loginName1 = request.getParameter("loginName");
            String loginName2 = loginName;
            model.setSex(Integer.valueOf(request.getParameter("sex")));
            model.setIdCardNo(request.getParameter("idCardNo"));
            model.setAddTime(new Date());
            model.setAddUserId(getUser().getUserId());
            model.setUnitCode(getUser().getUnitCode());
            model.setState(Integer.valueOf((request.getParameter("state") == null ? "0" : request.getParameter("state"))));
            model.setBackup1(request.getParameter("backup1"));
            if (userService.add(model) == 1)
                ResultUtils.writeMessage(response, 1, "添加成功");
            else
                ResultUtils.writeMessage(response, 0, "添加失败");

            WriteLog("添加显示名为" + request.getParameter("displayName") + "的用户信息");
        } else {//修改
            PageData pd = new PageData();
            pd.put("userId", request.getParameter("userId"));
            pd.put("displayName", request.getParameter("displayName"));
            pd.put("loginName", request.getParameter("loginName"));
            pd.put("state", request.getParameter("state"));
            pd.put("email", request.getParameter("email"));
            pd.put("mobilephone", request.getParameter("mobilephone"));
            pd.put("sex", request.getParameter("sex"));
            pd.put("idCardNo", request.getParameter("idCardNo"));
            pd.put("fsort", request.getParameter("fsort"));
            pd.put("backup1", request.getParameter("backup1"));
            if (userService.update(pd) == 1)
                ResultUtils.writeMessage(response, 1, "修改成功");
            else
                ResultUtils.writeMessage(response, 0, "修改失败");

            WriteLog("修改id为" + request.getParameter("userId") + "的用户信息");
        }

    }

    //删除
    @RequestMapping(value = "Delete")
    public void Delete(HttpServletRequest request, HttpServletResponse response) {
        String ids = request.getParameter("ids") == null ? "" : request.getParameter("ids");
        try {
            if (userService.delete(ids) == 1)
                ResultUtils.writeMessage(response, 1, "删除成功");
            else
                ResultUtils.writeMessage(response, 0, "删除失败");

            WriteLog("删除id为"+ids+"的用户信息");
        } catch (Exception ex) {
            ResultUtils.writeMessage(response, 0, ex.getMessage());
        }
    }


    //获取个人信息
    @RequestMapping(value = "getUserInfo")
    public void getUserInfo(HttpServletRequest request, HttpServletResponse response) {
        logger.info("获取个人信息");
        try {
            PageData pageData = new PageData();
            pageData.put("unitCode", getUser().getUnitCode());
            pageData.put("userType",getUser().getUserType());
            if(getUser().getUserType()==1){
                pageData.put("PERSONAL_NUMBER",getUser().getUserId());
                String username=getUser().getIdCardNo();
                String uName=username.substring(username.length()-6,username.length());
                Map<String, String> map = new HashMap<>();
                map.put("displayName","<a href='javascript:void(0);'style='font-size:14px;color:#b8ceda!important' title='选择评委会'>"+getUser().getJudgingName()+"</a>:"+getUser().getDisplayName());
                map.put("userThemes", getUser().getUserThemes());
                map.put("headportraitImg", "0");
                map.put("JUDGING_CODE",getUser().getJudgingCode());
                map.put("NotMsgCount", "0");
                map.put("type",getUser().getUserType().toString());
                if(encodeMD5(uName).equals(getUser().getPassword())){  //与初始密码相同，提示修改密码
                    map.put("isOk","-1");
                }else {
                    map.put("isOk", "1");
                }
                ResultUtils.write(response, map);
            }else{
                base_unit unit = baseUnitService.getModelWhere(pageData);
                Map<String, String> map = new HashMap<>();
                map.put("displayName", unit.getUnitName()+":"+getUser().getDisplayName());
                map.put("userThemes", getUser().getUserThemes());
                map.put("headportraitImg", "0");
                map.put("NotMsgCount", "0");
                map.put("isOk","1");
                map.put("type",getUser().getUserType().toString());
                ResultUtils.write(response, map);
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    //更改专家所在评委会
    @RequestMapping(value = "UpdateJudging")
    public void UpdateJudging(HttpServletRequest request, HttpServletResponse response) {
        logger.info("更改专家所在评委会");
        PageData pd = new PageData();
        Map<String, String> map = new HashMap<>();
        try {
            String JUDGING_CODE=request.getParameter("JUDGING_CODE");
            String JUDGING_NAME=request.getParameter("JUDGING_NAME");
            getUser().setJudgingName(JUDGING_NAME);
            getUser().setJudgingCode(JUDGING_CODE);
            map.put("isOk","1");
        } catch (NumberFormatException e) {
            map.put("isOk","2");
            e.printStackTrace();
        }
        ResultUtils.write(response, toJson(map));
    }
    //查询当前专家所有评委会
    @RequestMapping(value = "findJudging")
    public void findJudging(HttpServletRequest request, HttpServletResponse response) {
        logger.info("查询当前专家所有评委会");
        PageData pd = new PageData();
        try {
            //Map<String, String> map = new HashMap<>();
            List<PageData> item = specialityService.loginEngage(getUser().getUserId());
            ResultUtils.write(response, toJson(item));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    //更改用户主题
    @RequestMapping(value = "UpdateUserThemes")
    public void UpdateUserThemes(HttpServletRequest request, HttpServletResponse response) {
        logger.info("更改用户主题");
        PageData pd = new PageData();
        pd.put("userId", getUser().getUserId());
        pd.put("userThemes", request.getParameter("userThemes") == null ? "" : request.getParameter("userThemes"));//记录状态
        Map<String, String> map = new HashMap<>();
        map.put("isOk", "0");
        if (userService.update(pd) == 1)
            map.put("isOk", "1");
        ResultUtils.write(response, toJson(map));
    }



    //初始化密码
    @RequestMapping(value = "initPassword")
    public void initPassword(HttpServletRequest request, HttpServletResponse response) {
        logger.info("初始化密码");
        PageData pageData = new PageData();
        pageData.put("userId", request.getParameter("userId"));
        pageData.put("password",encodeMD5("000000"));
        try {
            if (userService.update(pageData) == 1) {
                ResultUtils.writeMessage(response, 1, "密码初始化成功");
            } else {
                ResultUtils.writeMessage(response, 0, "密码初始化失败");
            }
        } catch (Exception ex) {
            ResultUtils.writeMessage(response, 0, ex.getMessage());
        }
    }
    //查询今年系列申报人数-
    @RequestMapping(value = "selectTBXL")
    public void selectTBXL(HttpServletRequest request, HttpServletResponse response) {
        logger.info("查询今年系列申报人数");
        PageData pageData = new PageData();
        try {
            pageData.put("UNIT_CODE",getUser().getUnitCode());
            pageData.put("JUDGING_CODE",getUser().getJudgingCode());
            List<PageData> item=userService.selectTBXL(pageData);
            List<String> str=new ArrayList<String>();
            List<Integer> cou=new ArrayList<Integer>();
            for(PageData pd:item){
                str.add(pd.getString("DICT_NAME"));
                cou.add(Integer.valueOf(pd.get("COU").toString()));
            }
            JSONObject obj=new JSONObject();
            obj.put("name",str);
            obj.put("cou",cou);
            ResultUtils.write(response, obj);
        } catch (Exception ex) {
            ResultUtils.write(response, toJson(""));
        }
    }

    //查询今年申请职称级别人数
    @RequestMapping(value = "selectTBJB")
    public void selectTBJB(HttpServletRequest request, HttpServletResponse response) {
        logger.info("查询今年级别申报人数");
        PageData pageData = new PageData();
        try {
            pageData.put("UNIT_CODE",getUser().getUnitCode());
            pageData.put("JUDGING_CODE",getUser().getJudgingCode());
            List<PageData> item=userService.selectTBJB(pageData);
            JSONArray jsonArray=new JSONArray();
            for(PageData pd:item){
                JSONObject obj=new JSONObject();
                obj.put("name",pd.getString("DICT_NAME"));
                obj.put("y",Integer.valueOf(pd.get("COU").toString()));
                jsonArray.add(obj);
            }
            ResultUtils.write(response,jsonArray);
        } catch (Exception ex) {
            ResultUtils.write(response, toJson(""));
        }
    }
    //代办事项
    @RequestMapping(value = "selectAgency")
    public void selectAgency(HttpServletRequest request, HttpServletResponse response) {
        logger.info("查询当前人员代办事项");
        PageData pageData = new PageData();
        try {
            JSONArray object=new JSONArray();
            if(null==getUser().getJudgingCode()){
                object=userService.selectAgency(getUser());
            }else{ //专家
                object=userService.selectAgencySplit(getUser());
            }
            ResultUtils.write(response,object);
        } catch (Exception ex) {
            ex.printStackTrace();
            ResultUtils.write(response, toJson(""));
        }
    }

}