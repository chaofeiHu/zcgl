package com.hz.demo.core;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.hz.demo.model.sys_menu;
import com.hz.demo.model.sys_user;
import com.hz.demo.services.BaseJudingProcessService;
import com.hz.demo.services.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;

/**
 * 评委会流程控制拦截器
 */
public class SpecialityProcessInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    BaseJudingProcessService baseJudingProcessService;
    @Autowired
    MenuService menuService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
       // System.out.println("---------------------评委会控制拦截器--------------------");
        HttpSession session = request.getSession();
        sys_user userName = (sys_user) session.getAttribute("User");
        if(request.getQueryString()!=null){
            String menu=request.getQueryString().toString();
            if(menu.split("menuid").length>1){
                String menuid=menu.split("menuid")[1].split("=")[1];
                sys_menu sys_menu=menuService.getMenuByFid(menuid);
                if(sys_menu.getMenuCode().substring(0,2).equals("03")){
                    if(userName.getJudgingCode()==null){
                        return true;
                    }
                    PageData base_judging_process=baseJudingProcessService.selectCurrentSpeciality(userName.getJudgingCode());
                    if(base_judging_process==null){
                        response.setContentType("application/json; charset=utf-8");
                        PrintWriter writer = response.getWriter();
                        writer.print(JSONObject.toJSONString("请添加评委会流程控制！",
                                SerializerFeature.WriteMapNullValue,SerializerFeature.WriteDateUseDateFormat));
                        writer.close();
                        response.flushBuffer();
                        return false;
                    }
                    if(sys_menu.getFsort()!=0&& sys_menu.getFsort()<=8) {
                        if (base_judging_process.getString("MENUID").equals(sys_menu.getMenuId())) {
                            //评委会进度与当前菜单相同
                            System.out.println("---------------------该菜单符合评委会当前进度--------------------");
                            return true;
                        } else {
                            System.out.println("--------------------该菜单不符合评委会当前进度--------------------");
                            String html="";
                            Integer num=Integer.valueOf(base_judging_process.get("PROCESS_TYPE").toString());
                            if(num>=8){
                                html="当前评委会进度：评审结束";
                            }else{
                                html="当前评委会进度："+base_judging_process.getString("MENU_NAME");
                            }
                            response.setContentType("application/json; charset=utf-8");
                            PrintWriter writer = response.getWriter();
                            writer.print(JSONObject.toJSONString(html,
                                    SerializerFeature.WriteMapNullValue,SerializerFeature.WriteDateUseDateFormat));
                            writer.close();
                            response.flushBuffer();
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }




}
