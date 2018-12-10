package com.hz.demo.controller;

import com.hz.demo.core.PageData;
import com.hz.demo.core.ResultUtils;
import com.hz.demo.core.TableReturn;
import com.hz.demo.core.Tree;
import com.hz.demo.model.base_blacklist;
import com.hz.demo.model.base_judging_process;
import com.hz.demo.services.BaseBlackListService;
import com.hz.demo.services.BaseJudingProcessService;
import com.hz.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.hz.demo.core.ResultUtils.toDateTimeJson;
import static com.hz.demo.core.ResultUtils.toJson;

/**
 * 黑名单
 */
@Controller
@RequestMapping("/BaseBlackList")
public class BaseBlackListController extends BaseController{
    @Autowired
    UserService userService;
    @Autowired
    BaseBlackListService baseBlackListService;

    @RequestMapping(value = "BaseBlackListPage")
    public String BaseBlackListPage(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("MenuBtns",userService.GetMenuBtns(request.getParameter("menuid"),getUser().getUserId()));
        return "sys/baseblacklist";
    }

    //获取界面列表
    @RequestMapping(value = "getList")
    public void getList(HttpServletRequest request, HttpServletResponse response) {
        logger.info("显示黑名单数据");
        PageData pd = new PageData();
        pd.put("rows", request.getParameter("rows") == null? 10: Integer.parseInt(request.getParameter("rows")));//每页显示行数
        pd.put("page", request.getParameter("page") == null ? 1 : (Integer.parseInt(request.getParameter("page"))));//每页记录开始位置
        pd.put("PROPOSER_NAME", request.getParameter("PROPOSER_NAME"));
        String unitName = request.getParameter("UNIT_NAME");
        pd.put("UNIT_NAME", request.getParameter("UNIT_NAME") );
        TableReturn tablereturn = new TableReturn();
        List<PageData> list = baseBlackListService.getList(pd);
        Integer listCount = baseBlackListService.getListCount(pd);
        tablereturn.setRows(list);
        tablereturn.setTotal(listCount);
        ResultUtils.write(response, toDateTimeJson(tablereturn));
    }


    //添加或更新（获取）
    @RequestMapping(value = "AddOrUpdate")
    public void AddOrUpdate(HttpServletRequest request, HttpServletResponse response) {
        logger.info("添加或更新（获取）");
        String id = request.getParameter("id") == null ? "" : request.getParameter("id");
        String PROPOSER_NAME=request.getParameter("proposerName") == null ? "" : request.getParameter("proposerName");
        String ID_CARD_NO=request.getParameter("idCardNo") == null ? "" : request.getParameter("idCardNo");
        String MOBILEPHONE=request.getParameter("mobilephone") == null ? "" : request.getParameter("mobilephone");
        String RELIEVE_DATE=request.getParameter("relieveDate") == null ? "" : request.getParameter("relieveDate");
        String REASON=request.getParameter("reason") == null ? "" : request.getParameter("reason");
        String UNIT_NAME=request.getParameter("unitName") == null ? "" : request.getParameter("unitName");
        try{
            if (id.equals("")) {
                //添加
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                base_blacklist base_blacklist=new base_blacklist();
                base_blacklist.setAddtime(new Date());
                base_blacklist.setAdduserid(getUser().getUserId());
                base_blacklist.setIdCardNo(ID_CARD_NO);
                base_blacklist.setMobilephone(MOBILEPHONE);
                base_blacklist.setProposerName(PROPOSER_NAME);
                base_blacklist.setReason(REASON);
                base_blacklist.setRelieveDate(sdf.parse(RELIEVE_DATE));
                base_blacklist.setUnitName(UNIT_NAME);
                baseBlackListService.add(base_blacklist);
                ResultUtils.write(response, toJson("添加成功！"));
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                base_blacklist base_blacklist=new base_blacklist();
                base_blacklist.setIdCardNo(ID_CARD_NO);
                base_blacklist.setMobilephone(MOBILEPHONE);
                base_blacklist.setProposerName(PROPOSER_NAME);
                base_blacklist.setReason(REASON);
                base_blacklist.setRelieveDate(sdf.parse(RELIEVE_DATE));
                base_blacklist.setUnitName(UNIT_NAME);
                base_blacklist.setId(new BigDecimal(id));
                baseBlackListService.update(base_blacklist);
                ResultUtils.write(response, toJson("更新成功！"));
            }
        }catch (Exception e){
            ResultUtils.write(response, toJson("失败！"));
        }
    }

    //添加或更新（获取）
    @RequestMapping(value = "selectBlackList")
    public void selectBlackList(HttpServletRequest request, HttpServletResponse response) {
        logger.info("获取评审流程控制信息");
        String id = request.getParameter("ID") == null ? "" : request.getParameter("ID");
        base_blacklist juding = baseBlackListService.getModel(id);
        if (juding != null) {
            ResultUtils.write(response, toJson(juding));
        } else {
            ResultUtils.write(response, toJson("msg"));
        }
    }
    //添加或更新（获取）
    @RequestMapping(value = "Delete")
    public void Delete(HttpServletRequest request, HttpServletResponse response) {
        logger.info("删除黑名单信息");
        String id = request.getParameter("ID") == null ? "" : request.getParameter("ID");
        int juding = baseBlackListService.Delete(new BigDecimal(id));
        if (juding >0) {
            ResultUtils.write(response, toJson("删除成功"));
        } else {
            ResultUtils.write(response, toJson("删除失败"));
        }
    }


    //通过用户id获取黑名单信息
    @RequestMapping(value = "getBlackListMsgByUserId")
    public void getBlackListMsgByUserId(HttpServletRequest request, HttpServletResponse response) {
        logger.info("通过用户id获取黑名单信息");
        String id = request.getParameter("id") == null ? "" : request.getParameter("id");
        base_blacklist blacklist = baseBlackListService.getBlackListMsgByUserId(id);
        if (blacklist !=null) {
            ResultUtils.write(response, toJson(blacklist)) ;
        } else {
            ResultUtils.write(response, toJson("获取失败"));
        }
    }

}
