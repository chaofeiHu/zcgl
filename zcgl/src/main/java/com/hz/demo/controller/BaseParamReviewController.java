package com.hz.demo.controller;

import com.hz.demo.core.PageData;
import com.hz.demo.core.ResultUtils;
import com.hz.demo.core.TableReturn;
import com.hz.demo.core.Tree;
import com.hz.demo.model.base_judging;
import com.hz.demo.model.base_judging_series;
import com.hz.demo.model.base_param_declare;
import com.hz.demo.model.base_param_review;
import com.hz.demo.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;

import static com.hz.demo.core.ResultUtils.toJson;

@Controller
@RequestMapping("/ParamReview")
public class BaseParamReviewController extends BaseController{
    @Autowired
    UserService userService;
    @Autowired
    BaseParamReviewService baseParamReviewService;
    @RequestMapping(value = "ParamReviewPage")
    public String ParamReviewPage(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("MenuBtns",userService.GetMenuBtns(request.getParameter("menuid"),getUser().getUserId()));
        return "sys/paramreview";
    }

    //查询
    @RequestMapping(value = "select")
    public void select(HttpServletRequest request, HttpServletResponse response) {
        logger.info("查询评审控制指标");
        String judGingCode = request.getParameter("judGingCode") == null ? "" : request.getParameter("judGingCode");
        PageData pageData = new PageData();
        pageData.put("judGingCode",judGingCode);
        base_param_review model = baseParamReviewService.getByJudGingCode(pageData);
        if (model != null) {
            ResultUtils.write(response, toJson(model));
        } else {
            ResultUtils.write(response, null);
        }
    }

    //加载组织机构tree
    @RequestMapping(value = "getTree")
    public void getUnitTree(HttpServletRequest request, HttpServletResponse response) {
        String unitCode = request.getParameter("parentUnitCode") == null ? "10000" : request.getParameter("unitCode");
        PageData pd = new PageData();
        pd.put("parentUnitCode", unitCode);
        String state = request.getParameter("state") == null ? null : request.getParameter("state");
        if (state != null)
            pd.put("state", state);
        List<Tree> baseParamDeclareTreeList = baseParamReviewService.getJudGingTree(pd);
        ResultUtils.write(response, ObjectList2TreeJson(unitCode, baseParamDeclareTreeList, 0));
    }

    //保存
    @RequestMapping(value = "Save")
    public void Save(HttpServletRequest request, HttpServletResponse response) {
        logger.info("保存");
        PageData pd = new PageData();
        pd.put("judGingCode", request.getParameter("judGingCode") == null ? "" : request.getParameter("judGingCode"));//单位编号
        String id = request.getParameter("id") == null ? "" : request.getParameter("id");
        base_param_review model = null;
        if (id.equals("")) { //添加
            model = new base_param_review();
            model.setJudgingCode(pd.get("judGingCode").toString());
            model.setZg(new BigDecimal(request.getParameter("zg")));
            model.setFg(new BigDecimal(request.getParameter("fg")));
            model.setZj(new BigDecimal(request.getParameter("zj")));
            pd.put("unitCode", request.getParameter("unitCode"));
            if (baseParamReviewService.update(pd) == 0) {
                if (baseParamReviewService.add(model) == 1){
                    ResultUtils.writeMessage(response, 1, "保存成功");
                }else {
                    ResultUtils.writeMessage(response, 0, "保存失败");
                }
            } else{
                ResultUtils.writeMessage(response, 0, "保存失败");
            }
        } else {//修改
            pd.put("id",id);
            pd.put("zg", request.getParameter("zg"));
            pd.put("fg", request.getParameter("fg"));
            pd.put("zj", request.getParameter("zj"));
            if (baseParamReviewService.update(pd) == 1)
                ResultUtils.writeMessage(response, 1, "保存成功");
            else
                ResultUtils.writeMessage(response, 0, "保存失败");
        }
    }
}