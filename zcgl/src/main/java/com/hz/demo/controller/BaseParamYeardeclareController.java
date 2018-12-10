package com.hz.demo.controller;

import com.hz.demo.core.PageData;
import com.hz.demo.core.ResultUtils;
import com.hz.demo.core.TableReturn;
import com.hz.demo.model.base_judging;
import com.hz.demo.model.base_param_yeardeclare;
import com.hz.demo.services.BaseParamYeardeclareService;
import com.hz.demo.services.DictService;
import com.hz.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.hz.demo.core.ResultUtils.toDateTimeJson;
import static com.hz.demo.core.ResultUtils.toJson;

@Controller
@RequestMapping("/ParamYeardeclare")
public class BaseParamYeardeclareController extends BaseController{
    @Autowired
    UserService userService;
    @Autowired
    DictService dictService;
    @Autowired
    BaseParamYeardeclareService baseParamYeardeclareService;
    @RequestMapping(value = "ParamYeardeclarePage")
    public String ParamYeardeclarePage(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("MenuBtns",userService.GetMenuBtns(request.getParameter("menuid"),getUser().getUserId()));
        return "sys/paramyeardeclare";
    }
    //加载申报开关列表
    @RequestMapping(value = "getParamYeardeclareList")
    public void getParamYeardeclareList(HttpServletRequest request, HttpServletResponse response) {
       logger.info("显示申报开关数据");
        PageData pd = new PageData();
       TableReturn tablereturn = new TableReturn();
       List<Map<String, Object>> hashMaps = new ArrayList<>();
        List<base_param_yeardeclare> list = baseParamYeardeclareService.getList(pd);
          for (base_param_yeardeclare yeardeclare : list) {
            Map<String, Object> map = new HashMap<>();
              map.put("id",yeardeclare.getId());
              map.put("reviewSeries", getDict("REVIEW_SERIES",String.valueOf(yeardeclare.getReviewSeries())).getDictName());
              SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
              String begintime = formatter.format(yeardeclare.getBegintime());
              String ending = formatter.format(yeardeclare.getEndtime());
              map.put("begintime", begintime);
              map.put("endtime",ending);
              map.put("startState", yeardeclare.getStartState());
             hashMaps.add(map);
        }
        tablereturn.setRows(hashMaps);
        ResultUtils.write(response, toDateTimeJson(tablereturn));
  }
    @RequestMapping(value = "Update")
    public void AddOrUpdate(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id") == null ? "" : request.getParameter("id");
        if (id.equals("")) {
        } else {
            //更新（获取）
            base_param_yeardeclare model = baseParamYeardeclareService.getModel(id);
            if (model != null) {
                ResultUtils.write(response, toDateTimeJson(model));
            } else {
                ResultUtils.write(response, "记录信息不存在！");
            }
        }
     }
    //申报保存
    @RequestMapping(value = "Save")
    public void Save(HttpServletRequest request, HttpServletResponse response) {
        logger.info("申报保存");
        String id = request.getParameter("id") == null ? "" : request.getParameter("id");
            PageData pd = new PageData();

         try {
                  pd.put("id",id);
                  SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    String begintime = request.getParameter("begintime");
                    String endtime = request.getParameter("endtime");
                     if (null != begintime && !"".equals(begintime)) {
                         pd.put("begintime", formatter.parse(begintime));
                     }
                     if (null != endtime && !"".equals(endtime)) {
                         pd.put("endtime", formatter.parse(endtime));
                     }
                     if(formatter.parse(begintime).getTime()>=formatter.parse(endtime).getTime()){

                         ResultUtils.writeMessage(response, 0, "结束日期大于开始日期");
                         return;
                     }

                 if (baseParamYeardeclareService.update(pd) == 1){
                     ResultUtils.writeMessage(response, 1, "修改成功");}
                 else{
                     ResultUtils.writeMessage(response, 0, "修改失败");}
                 } catch (Exception e) {
                    e.printStackTrace();
                }
    }
   //更改年度申报总状态
    @RequestMapping(value = "UpdateAllState")
    public void UpdateAllState(HttpServletRequest request, HttpServletResponse response) {
        logger.info("更改年度申报总状态");
        PageData pageData  = new PageData();
        String startState = request.getParameter("startState") == null ? "0" : request.getParameter("startState");
        Integer sta = Integer.parseInt(startState);
        if (sta == 0) {
            sta = 1;
        } else {
            sta = 0;
        }
        pageData.put("startState",sta);//状态
        Map<String, String> map = new HashMap<>();
        map.put("isOk", "0");
       // map.put("startState", String.valueOf(sta));
        if (baseParamYeardeclareService.updateState(pageData)>=1)
            map.put("isOk", "1");
        WriteLog("更改年度申报总状态");
        ResultUtils.write(response, toJson(map));
    }

    @RequestMapping(value = "Sync")
    public void Sync(HttpServletRequest request, HttpServletResponse response) {
        logger.info("评委系列更新保存");
            if (baseParamYeardeclareService.addList() >= 1){
                ResultUtils.writeMessage(response, 1, "更新成功");}
            else{
                ResultUtils.writeMessage(response, 0, "没有数据更新");}

           baseParamYeardeclareService.updateTime();

       }
    //更改年度申报单个状态
    @RequestMapping(value = "UpdateState")
    public void UpdateState(HttpServletRequest request, HttpServletResponse response) {
        logger.info("更改年度申报单个状态");
        PageData pageData  = new PageData();
        String state = request.getParameter("state") == null ? "0" : request.getParameter("state");
        Integer sta = Integer.parseInt(state);
        if (sta == 0) {
            sta = 1;
        } else {
            sta = 0;
        }
        pageData.put("startState", sta);//id
        String bid = request.getParameter("id") == null ? "" : request.getParameter("id");
        String seriesNmae = request.getParameter("seriesNmae") == null ? "" : request.getParameter("seriesNmae");
        pageData.put("id", bid);//状态
        Map<String, String> map = new HashMap<>();
        map.put("isOk", "0");
        if (baseParamYeardeclareService.update(pageData) == 1)
            map.put("isOk", "1");
        WriteLog("更改" + seriesNmae + "的年度申报状态");
        ResultUtils.write(response, toJson(map));
    }
}
