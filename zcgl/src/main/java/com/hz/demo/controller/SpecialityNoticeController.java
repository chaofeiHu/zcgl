package com.hz.demo.controller;

import com.hz.demo.core.ExportExcel;
import com.hz.demo.core.PageData;
import com.hz.demo.core.ResultUtils;
import com.hz.demo.core.TableReturn;
import com.hz.demo.model.base_judging;
import com.hz.demo.model.base_speciality_notice;
import com.hz.demo.model.speciality_group;
import com.hz.demo.services.SpecialityNoticeService;
import com.hz.demo.services.UserService;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.hz.demo.core.EncodeUtil.encodeMD5;
import static com.hz.demo.core.ResultUtils.toDateTimeJson;
import static com.hz.demo.core.ResultUtils.toJson;

/***
 * 通知评委   未使用
 */
@Controller
@RequestMapping("/SpecialityNotice")
public class SpecialityNoticeController extends BaseController {
    @Autowired
    UserService userService;
    @Autowired
    SpecialityNoticeService specialityNoticeService;

    @RequestMapping(value = "SpecialityNoticePage")
    public String DeptPage(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("MenuBtns",userService.GetMenuBtns(request.getParameter("menuid"),getUser().getUserId()));
        return "sys/specialityNotice";
    }
    //获取界面列表
    @RequestMapping(value = "getList")
    public void getList(HttpServletRequest request, HttpServletResponse response) {
        logger.info("显示评委库专家分组列表数据");
        PageData pd = new PageData();
        pd.put("rows", request.getParameter("rows") == null? 10: Integer.parseInt(request.getParameter("rows")));//每页显示行数
        pd.put("page", request.getParameter("page") == null ? 1 : (Integer.parseInt(request.getParameter("page"))));//每页记录开始位置
        String jid = request.getParameter("JUDGING_CODE");
        if ("".equals(jid) || null == jid) {
            ResultUtils.writeMessage(response, 0, "请选中评委会,然后添加");
        } else {
            pd.put("JUDGING_CODE",jid);
            TableReturn tablereturn = new TableReturn();
            List<PageData> list = specialityNoticeService.getList(pd);
            for(int i=0;i<list.size();i++){
                PageData pageData=list.get(i);
                pageData.put("JUDGING_CODE",jid);
                List<PageData> Senioritem=specialityNoticeService.selectListSenior(pageData);
                for(int k=0;k<Senioritem.size();k++){
                    PageData pdd=Senioritem.get(k);
                    if(pdd.getString("DICTNAME").equals("高级")){
                        list.get(i).put("gj","高级");
                        list.get(i).put("gjsl",pdd.get("SHU").toString());
                    }else if(pdd.getString("DICTNAME").equals("副高级")){
                        list.get(i).put("fg","副高");
                        list.get(i).put("fgsl",pdd.get("SHU").toString());
                    }else if(pdd.getString("DICTNAME").equals("中级")){
                        list.get(i).put("zj","中级");
                        list.get(i).put("zgsl",pdd.get("SHU").toString());
                    }
                }
            }
            Integer listCount = specialityNoticeService.getListCount(pd);
            tablereturn.setRows(list);
            tablereturn.setTotal(listCount);
            ResultUtils.write(response, toDateTimeJson(tablereturn));
        }
    }


    //获取专家信息,用于设置评委会主任
    @RequestMapping(value = "findSpeciality")
    public void findSpeciality(HttpServletRequest request, HttpServletResponse response) {
        logger.info("显示评委库专家列表数据");
        PageData pd = new PageData();
        pd.put("rows", request.getParameter("rows") == null? 10: Integer.parseInt(request.getParameter("rows")));//每页显示行数
        pd.put("page", request.getParameter("page") == null ? 1 : (Integer.parseInt(request.getParameter("page"))));//每页记录开始位置
        String jid = request.getParameter("JUDGING_CODE");
        pd.put("SPECIALITY_NAME", request.getParameter("SPECIALITY_NAME") == null ? "" : request.getParameter("SPECIALITY_NAME"));//每页记录开始位置
        pd.put("NOWUNIT", request.getParameter("NOWUNIT") == null ? "" :request.getParameter("NOWUNIT"));//每页记录开始位置
        pd.put("xkz", request.getParameter("xkz") == null ? "" : request.getParameter("xkz"));//每页记录开始位置
        TableReturn tablereturn = new TableReturn();
        if(jid!=null){
            pd.put("JUDGING_CODE",jid);
            List<PageData> list = specialityNoticeService.findSpeciality(pd);
            Integer listCount = specialityNoticeService.findSpecialityCount(pd);
            tablereturn.setRows(list);
            tablereturn.setTotal(listCount);
        }
        ResultUtils.write(response, toDateTimeJson(tablereturn));
    }
    //添加或更新（获取）主任信息
    @RequestMapping(value = "AddOrUpdate")
    public void AddOrUpdate(HttpServletRequest request, HttpServletResponse response) {
        logger.info("添加或更新（获取）");
        String PERSONAL_NUMBER = request.getParameter("PERSONAL_NUMBER") == null ? "" : request.getParameter("PERSONAL_NUMBER");
        String GROUP_ID = request.getParameter("GROUP_ID") == null ? "" : request.getParameter("GROUP_ID");
        String DIRECTOR = request.getParameter("DIRECTOR") == null ? "" : request.getParameter("DIRECTOR");
        PageData  pd=new PageData();
        pd.put("GROUP_ID",GROUP_ID);
        pd.put("PERSONAL_NUMBER",PERSONAL_NUMBER);
        base_speciality_notice base_speciality_notice = specialityNoticeService.getModel(pd);
        if (base_speciality_notice==null) {
            //添加
            base_speciality_notice=new base_speciality_notice();
            base_speciality_notice.setPersonalNumber(PERSONAL_NUMBER);
            base_speciality_notice.setGroupId(GROUP_ID);
            if(DIRECTOR.equals("0")){
                base_speciality_notice.setDirector(Short.parseShort("0"));
                specialityNoticeService.updateNotice(pd);
            }
            specialityNoticeService.add(base_speciality_notice);
        } else {
            specialityNoticeService.updateNotice(pd);
            specialityNoticeService.updateNoticetwo(pd);
        }
    }

    /**
     * 获取专家信息,确定是否出席
     * @param request
     * @param response
     */
    @RequestMapping(value = "findSpecialityNotice")
    public void findSpecialityNotice(HttpServletRequest request, HttpServletResponse response) {
        PageData pd = new PageData();
        pd.put("rows", request.getParameter("rows") == null? 10: Integer.parseInt(request.getParameter("rows")));//每页显示行数
        pd.put("page", request.getParameter("page") == null ? 1 : (Integer.parseInt(request.getParameter("page"))));//每页记录开始位置
        String jid = request.getParameter("JUDGING_CODE");
        pd.put("GROUP_ID", request.getParameter("GROUP_ID") == null ? "" : request.getParameter("GROUP_ID"));//每页记录开始位置
        pd.put("SPECIALITY_NAME", request.getParameter("SPECIALITY_NAME") == null ? "" : request.getParameter("SPECIALITY_NAME"));//每页记录开始位置
        pd.put("NOWUNIT", request.getParameter("NOWUNIT") == null ? "" : request.getParameter("NOWUNIT"));//每页记录开始位置
        pd.put("TYPE", request.getParameter("sf") == null ? "" : request.getParameter("sf"));//每页记录开始位置
        TableReturn tablereturn = new TableReturn();
        if(jid!=null){
            pd.put("JUDGING_CODE",jid);
            List<PageData> list = specialityNoticeService.findSpecialityNotice(pd);
            Integer listCount = specialityNoticeService.findSpecialityNoticeCount(pd);
            tablereturn.setRows(list);
            tablereturn.setTotal(listCount);
        }
        ResultUtils.write(response, toDateTimeJson(tablereturn));
    }

    //添加或更新专家是否出席信息
    @RequestMapping(value = "UpdateSpecialityNotice")
    public void UpdateSpecialityNotice(HttpServletRequest request, HttpServletResponse response) {
        logger.info("添加或更新专家是否出席信息");
        JSONArray jsonArray=new JSONArray();
        String rows=request.getParameter("rows");
        jsonArray=JSONArray.fromObject(rows) ;
        try{
            for(int i=0;i<jsonArray.size();i++){
                JSONObject object =jsonArray.getJSONObject(i);
                PageData  pd=new PageData();
                pd.put("GROUP_ID",object.get("GROUP_ID"));
                pd.put("PERSONAL_NUMBER",object.get("PERSONAL_NUMBER"));
                if(object.containsKey("REASON")){
                    pd.put("REASON",object.get("REASON"));
                }else{
                    pd.put("REASON","");
                }
                pd.put("TYPE",object.get("TYPE"));
                specialityNoticeService.UpdateOrAdd(pd);
            }
            ResultUtils.write(response, toJson("更新成功!"));
        }catch (Exception e){
            e.printStackTrace();
            ResultUtils.write(response, toJson("更新失败,请重新获取!"));
        }
    }
    /***导出需审批的申报人列表数据***/
    @RequestMapping("/exportExcelProposer.do")
    public String exportExcelProposer(HttpServletRequest request, HttpServletResponse response, Model model) {
        logger.info("导出需审批的申报人列表数据");
        Map<String,Object> dataMap = new HashMap<String,Object>();
        String JUDGING_CODE = request.getParameter("JUDGING_CODE");
        PageData pd=new PageData();
        pd.put("JUDGING_CODE",JUDGING_CODE);
        List<PageData> list=specialityNoticeService.ExportExcelNotice(pd);
        List<PageData> listPd = new ArrayList<PageData>();
        List<String> titles=new ArrayList<String>();
        titles.add("序号");
        titles.add("专家名称");
        titles.add("组名");
        titles.add("专家角色");
        titles.add("是否出席");
        titles.add("不出席理由");
        dataMap.put("titles",titles);
        for(int i=0;i<list.size();i++){
            PageData vpd=list.get(i);
            PageData pageData=new PageData();
            pageData.put("var1",i+1);
            pageData.put("var2",vpd.getString("SPECIALITY_NAME"));
            pageData.put("var3",vpd.getString("GROUP_NAME"));
            System.out.print(vpd.get("DIRECTOR").toString());
            if(vpd.get("DIRECTOR").toString().equals("0")){
                pageData.put("var4","主任专家");
            }else{
                pageData.put("var4","普通专家");
            }
            if(vpd.get("TYPE").toString().equals("0")){
                pageData.put("var5","出席");
            }else{
                pageData.put("var5","不出席");
            }
            if(vpd.containsKey("REASON")){
                pageData.put("var6",vpd.getString("REASON"));
            }else{
                pageData.put("var6","");
            }
            listPd.add(pageData);
        }
        dataMap.put("varList", listPd);
        ExportExcel erv = new ExportExcel();
        erv.Excel(dataMap,new HSSFWorkbook(),request,response);
        return null;
    }
    //专家初始化密码
    @RequestMapping(value = "initNoticePassword")
    public void initNoticePassword(HttpServletRequest request, HttpServletResponse response) {
        logger.info("初始化密码");
        PageData pageData = new PageData();
        pageData.put("userId", request.getParameter("PERSONAL_NUMBER"));
        String idCard=request.getParameter("idCard");//身份证号后六位
        idCard=idCard.substring(idCard.length()-6,idCard.length());
        System.out.println(idCard);
        pageData.put("password",encodeMD5(idCard));
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
}
