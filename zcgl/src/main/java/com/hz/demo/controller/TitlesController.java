package com.hz.demo.controller;

import com.hz.demo.core.PageData;
import com.hz.demo.core.ResultUtils;
import com.hz.demo.core.TableReturn;
import com.hz.demo.model.Titles;
import com.hz.demo.model.base_unit;
import com.hz.demo.model.sys_dict;
import com.hz.demo.services.TitlesService;
import com.hz.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import static com.hz.demo.core.ResultUtils.toJson;

@Controller
@RequestMapping("/Titles")
public class TitlesController extends BaseController{
    @Autowired
    UserService userService;

    @Autowired
    TitlesService titlesService;


    @RequestMapping(value = "TitlesPage")
    public String DeptPage(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("MenuBtns",userService.GetMenuBtns(request.getParameter("menuid"),getUser().getUserId()));
        return "sys/Titles";
    }

    //获取界面列表
    @RequestMapping(value = "getList")
    public void getList(HttpServletRequest request, HttpServletResponse response) {
        logger.info("显示组织机构列表数据");
        PageData pd = new PageData();
        pd.put("rows", request.getParameter("rows") == null? 10: Integer.parseInt(request.getParameter("rows")));//每页显示行数
        pd.put("page", request.getParameter("page") == null ? 0 : (Integer.parseInt(request.getParameter("page"))));//每页记录开始位置
        pd.put("qualificationName", request.getParameter("qualificationName") == null ? "" : request.getParameter("qualificationName"));//部门ID
        TableReturn tablereturn = new TableReturn();
        List<Titles> list = titlesService.getList(pd);
        Integer listCount = titlesService.getListCount(pd);
        List<Titles> blist = new ArrayList<>();
        if (list != null) {
            for (Titles baseUnit:list) {
                blist.add(baseUnit);
            }
        }
        tablereturn.setRows(blist);
        tablereturn.setTotal(listCount);

        ResultUtils.write(response, toJson(tablereturn));
    }

    //更改职称申报状态
    @RequestMapping(value = "UpdateState")
    public void UpdateState(HttpServletRequest request, HttpServletResponse response) {
        logger.info("更改职称申报状态");
        PageData pageData  = new PageData();
        String type=request.getParameter("type");
        String state = request.getParameter("state") == null ? "0" : request.getParameter("state");
        Integer sta = Integer.parseInt(state);
        if (sta == 0) {
            sta = 1;
        } else {
            sta = 0;
        }
        if(type.equals("1")){
            pageData.put("isReview", sta);//id
        }else{
            pageData.put("isFirmlyBelieve", sta);//id
        }
        String bid = request.getParameter("id") == null ? "" : request.getParameter("id");
        pageData.put("id", bid);//状态
        Map<String, String> map = new HashMap<>();
        map.put("isOk", "0");
        if (titlesService.update(pageData) == 1)
            map.put("isOk", "1");
        WriteLog("更改id为" + request.getParameter("bid") + "的机构状态");
        ResultUtils.write(response, toJson(map));
    }

    /**同步职称*/
    @RequestMapping(value = "InsertTitles")
    public  void InsertTitles(HttpServletRequest request, HttpServletResponse response){
        List<sys_dict> pg=this.getDictList("POSITIONAL_TITLES");
        List<Titles> titlesList=new ArrayList<Titles>();
        List<Titles> tit=titlesService.findTitles(null);
        for(sys_dict sy:pg){
            int nu=0;
            for (Titles tis:tit){
                if(sy.getDictCode().equals(tis.getQualificationCode())){
                    nu=1;
                }
            }
            if(nu==0){
                Titles titles=new Titles();
                titles.setQualificationCode(sy.getDictCode());
                titles.setQualificationName(sy.getDictName());
                 titlesList.add(titles);
            }
        }
        Map<String, String> map = new HashMap<>();
        map.put("isOk", "0");
        map.put("message","同步失败");
        if(titlesList.size()>0){
            if ( titlesService.InsertTitles(titlesList)>1) {
                map.put("isOk", "1");
                map.put("message", "同步成功");
            }
        }else if(titlesList.size()==0){
                map.put("isOk", "1");
                map.put("message","同步成功");
        }
        WriteLog("更改id为" + request.getParameter("bid") + "的机构状态");
        ResultUtils.write(response, toJson(map));
    }





}
