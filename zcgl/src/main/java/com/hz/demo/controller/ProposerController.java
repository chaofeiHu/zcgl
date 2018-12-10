package com.hz.demo.controller;

import com.hz.demo.core.PageData;
import com.hz.demo.core.ResultUtils;
import com.hz.demo.core.TableReturn;
import com.hz.demo.model.base_proposer;
import com.hz.demo.services.CreateSpecialityAllMsgServices;
import com.hz.demo.services.ProposerService;
import com.hz.demo.services.UserService;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.hz.demo.core.EncodeUtil.encodeMD5;
import static com.hz.demo.core.ResultUtils.toJson;

@Controller
@RequestMapping("/Proposer")
public class ProposerController extends BaseController {

    @Autowired
    UserService userService;
    @Autowired
    ProposerService proposerService;
    @Autowired
    CreateSpecialityAllMsgServices createSpecialityAllMsgServices;

    @RequestMapping(value = "Examination")
    public String RolePage(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("MenuBtns",userService.GetMenuBtns(request.getParameter("menuid"),getUser().getUserId()));
        request.setAttribute("currentJudgingStage", request.getParameter("currentJudgingStage"));
        return "sys/speciality/speciality_exam_pro";
    }


    //设置表格数据格式--字符串
    public static void mycreateCell(HSSFRow row, int i, String value, HSSFCellStyle style) {
        HSSFCell cell = row.createCell(i);
        if (!value.equals("null")) {
            cell.setCellValue(value);
            cell.setCellStyle(style);
        }
    }

    //设置表格数据格式--字符串
    public static void mycreateCell(HSSFRow row, int i, Date value, HSSFCellStyle style) {
        HSSFCell cell = row.createCell(i);
        cell.setCellValue(value);
        cell.setCellStyle(style);
    }

    //整型
    public static void mycreateCell(HSSFRow row, int i, Integer value, HSSFCellStyle style) {
        HSSFCell cell = row.createCell(i);
        cell.setCellValue(value);
        cell.setCellStyle(style);
    }

    //跳到申报人界面
    @RequestMapping(value = "ProposerPage")
    public String DeptPage(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("MenuBtns", userService.GetMenuBtns(request.getParameter("menuid"), getUser().getUserId()));
        return "sys/proposer";
    }

    //获取界面列表
    @RequestMapping(value = "GetList")
    public void getList(HttpServletRequest request, HttpServletResponse response) {
        logger.info("显示申报人列表数据");
        PageData pd = new PageData();
        pd.put("rows", request.getParameter("rows") == null ? 10 : Integer.parseInt(request.getParameter("rows")));//每页显示行数
        pd.put("page", request.getParameter("page") == null ? 0 : (Integer.parseInt(request.getParameter("page"))));//每页记录开始位置
        pd.put("declareUnitcode", getUser().getUnitCode());//当前登录人的单位编码
        pd.put("displayName", request.getParameter("displayName") == null ? "" : request.getParameter("displayName"));//申报人显示名称
        pd.put("backup2", "0");
        TableReturn tablereturn = new TableReturn();
        List<base_proposer> list = proposerService.getList(pd);
        Integer listCount = proposerService.getListCount(pd);
        tablereturn.setRows(list);
        tablereturn.setTotal(listCount);

        ResultUtils.write(response, toJson(tablereturn));
    }

    //更改申报人状态
    @RequestMapping(value = "UpdateState")
    public void updateState(HttpServletRequest request, HttpServletResponse response) {
        logger.info("更改申报人状态");
        PageData pageData = new PageData();
        String id = request.getParameter("id") == null ? "" : request.getParameter("id");
        pageData.put("id", new BigDecimal(id));//id
        String state = request.getParameter("state") == null ? "0" : request.getParameter("state");
        Integer sta = Integer.parseInt(state);
        if (sta == 0) {
            sta = 1;
        } else {
            sta = 0;
        }
        pageData.put("state", sta);//状态
        Map<String, String> map = new HashMap<>();
        map.put("isOk", "0");
        if (proposerService.update(pageData) == 1)
            map.put("isOk", "1");

        WriteLog("更改id为" + request.getParameter("id") + "的申报人状态");
        ResultUtils.write(response, toJson(map));
    }

    //添加或更新（获取）
    @RequestMapping(value = "AddOrUpdate")
    public void AddOrUpdate(HttpServletRequest request, HttpServletResponse response) {
        logger.info("申报人添加或更新（获取）");
        String id = request.getParameter("proposerId") == null ? "" : request.getParameter("proposerId");
        if (!"".equals(id)) {
            //更新（获取）
            base_proposer model = proposerService.getModel(new BigDecimal(id));
            if (model != null) {
                ResultUtils.write(response, toJson(model));
            } else {
                ResultUtils.write(response, "记录信息不存在！");
            }
        } else {
            ResultUtils.write(response, "记录信息不存在！");
        }

    }

    //保存
    @RequestMapping(value = "Save")
    public void Save(HttpServletRequest request, HttpServletResponse response) {
        logger.info("保存");
        String id = request.getParameter("id") == null ? "" : request.getParameter("id");
        //String declareUnitcode = request.getParameter("declareUnitcode") == null ? (getUser().getUnitCode()) : request.getParameter("declareUnitcode");
        base_proposer model = null;
        if ("".equals(id)) { //添加
            model = new base_proposer();
            model.setDeclareUnitcode(getUser().getUnitCode());
            String basicUnitcode=request.getParameter("basicUnitcode");
            if(StringUtils.isEmpty(basicUnitcode)||"河南省职称办".equals(basicUnitcode)){
                model.setBasicUnitcode(getUser().getUnitCode());
            }else {
                model.setBasicUnitcode(basicUnitcode);
            }
            model.setAddTime(new Date());
            model.setAddUserId(getUser().getUserId());
            model.setDisplayName(request.getParameter("displayName"));
            model.setLoginName(request.getParameter("mobilephone"));
            model.setState(Integer.valueOf(request.getParameter("state") == null ? "1" : request.getParameter("state")));
            model.setLoginPassword(encodeMD5("000000"));
            model.setMobilephone(request.getParameter("mobilephone"));
            model.setEmail(request.getParameter("email"));
            model.setIdCardNo(request.getParameter("idCardNo"));
            model.setBackup1(request.getParameter("backup1"));
            model.setBackup2(request.getParameter("backup2"));
            if (proposerService.add(model) == 1)
                ResultUtils.writeMessage(response, 1, "添加成功");
            else
                ResultUtils.writeMessage(response, 0, "添加失败");

        } else {//修改
            PageData pd = new PageData();
            pd.put("id", new BigDecimal(id));
            String basicUnitcode=request.getParameter("basicUnitcode");
            if(StringUtils.isEmpty(basicUnitcode)||"河南省职称办".equals(basicUnitcode)){
                pd.put("basicUnitcode", getUser().getUnitCode());
            }else {
                pd.put("basicUnitcode", basicUnitcode);
            }
            pd.put("displayName", request.getParameter("displayName"));
            pd.put("loginName", request.getParameter("mobilephone"));
            pd.put("loginPassword", request.getParameter("loginPassword"));
            pd.put("mobilephone", request.getParameter("mobilephone"));
            pd.put("email", request.getParameter("email"));
            pd.put("idCardNo", request.getParameter("idCardNo"));
            pd.put("backup1", request.getParameter("backup1"));
            pd.put("backup2", request.getParameter("backup2"));
            if (proposerService.update(pd) == 1)
                ResultUtils.writeMessage(response, 1, "修改成功");
            else
                ResultUtils.writeMessage(response, 0, "修改失败");
        }

    }

    //删除
    @RequestMapping(value = "Delete")
    public void Delete(HttpServletRequest request, HttpServletResponse response) {
        String ids = request.getParameter("ids") == null ? "" : request.getParameter("ids");
        String[] strs = ids.split(",");
        try {
            for (String id : strs) {
                BigDecimal bigDecimal = new BigDecimal(id);
                if (proposerService.delete(bigDecimal) == 1)
                    ResultUtils.writeMessage(response, 1, "删除成功");
                else
                    ResultUtils.writeMessage(response, 0, "删除失败");
            }
        } catch (Exception ex) {
            ResultUtils.writeMessage(response, 0, ex.getMessage());
        }
    }

    //手机号及身份证号码验证
    @RequestMapping(value = "checkCode")
    public void checkCode(HttpServletRequest request, HttpServletResponse response) {
        logger.info("手机号及身份证号码验证");
        PageData pageData = new PageData();
        pageData.put("mobilephone", request.getParameter("mobilephone"));
        pageData.put("idCardNo", request.getParameter("idCardNo"));
        String id = request.getParameter("id");
        pageData.put("id", id);
        List<base_proposer> blist = proposerService.getProposerWhere(pageData);
        if ((blist != null && blist.size() != 0)) {
            ResultUtils.write(response, false);
        } else {
            ResultUtils.write(response, true);
        }
    }

    //获取界面列表
    @RequestMapping(value = "GetJudgingProposerList.do")
    public void GetJudgingProposerList(HttpServletRequest request, HttpServletResponse response) {
        logger.info("显示需审批的申报人/推荐专家列表数据");
        PageData pd = new PageData();
        pd.put("rows", request.getParameter("rows") == null ? 10 : Integer.parseInt(request.getParameter("rows")));//每页显示行数
        pd.put("page", request.getParameter("page") == null ? 0 : (Integer.parseInt(request.getParameter("page"))));//每页记录开始位置
        pd.put("declareUnitcode", getUser().getUnitCode());//当前登录人的单位编码
        //申报人姓名
        pd.put("displayName", request.getParameter("displayName2") == null ? "" : request.getParameter("displayName2"));//申报人显示名称
        pd.put("pwh", request.getParameter("pwh") == null ? "" : request.getParameter("pwh"));
        pd.put("nian", request.getParameter("nian") == null ? "" : request.getParameter("nian"));
        //管理机构
        pd.put("gljg", request.getParameter("gljg") == null ? "" : request.getParameter("gljg"));
        pd.put("xilie", request.getParameter("xl") == null ? "" : request.getParameter("xl"));
        pd.put("sbzg", request.getParameter("sbzg") == null ? "" : request.getParameter("sbzg"));
        pd.put("sczt", request.getParameter("sczt") == null ? "" : request.getParameter("sczt"));
        pd.put("dqzt", request.getParameter("dqzt") == null ? "" : request.getParameter("dqzt"));
        pd.put("judgingStage", request.getParameter("judgingStage") == null ? "" : request.getParameter("judgingStage"));//申报人申报阶段
        TableReturn tablereturn = new TableReturn();
        List<Map<String, String>> list = proposerService.getJudgingProposerList(pd);
        Integer listCount = proposerService.getJudgingProposerListCount(pd);
        tablereturn.setRows(list);
        tablereturn.setTotal(listCount);
        ResultUtils.write(response, toJson(tablereturn));
    }

    /***导出需审批的申报人列表数据***/
    @RequestMapping("/exportExcelProposer.do")
    public String exportExcelProposer(HttpServletRequest request, HttpServletResponse response, Model model) {
        logger.info("导出需审批的申报人列表数据");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sss = new SimpleDateFormat("yyyy-MM-dd ");
        String filename = sss.format(new Date().getTime());
        int page = request.getParameter("page") == null ? 1 : Integer.parseInt(request.getParameter("page"));
        int rows = request.getParameter("rows") == null ? 10000000 : Integer.parseInt(request.getParameter("rows"));
        PageData pageData = new PageData();
        pageData.put("page", page);
        pageData.put("rows", rows);
        pageData.put("declareUnitcode", getUser().getUnitCode());//当前登录人的单位编码
        //申报人姓名
        pageData.put("displayName", request.getParameter("displayName2") == null ? "" : request.getParameter("displayName2"));//申报人显示名称
        pageData.put("pwh", request.getParameter("pwh") == null ? "" : request.getParameter("pwh"));
        //管理机构
        pageData.put("gljg", request.getParameter("gljg") == null ? "" : request.getParameter("gljg"));
        pageData.put("nian", request.getParameter("nian") == null ? "" : request.getParameter("nian"));
        pageData.put("xilie", request.getParameter("xl") == null ? "" : request.getParameter("xl"));
        pageData.put("sbzg", request.getParameter("sbzg") == null ? "" : request.getParameter("sbzg"));
        pageData.put("sczt", request.getParameter("sczt") == null ? "" : request.getParameter("sczt"));
        pageData.put("dqzt", request.getParameter("dqzt") == null ? "" : request.getParameter("dqzt"));
        pageData.put("backup2","0");
        pageData.put("judgingStage", request.getParameter("judgingStage") == null ? "" : request.getParameter("judgingStage"));//申报人申报阶段
        List<Map<String, String>> list = proposerService.getJudgingProposerList(pageData);
        HSSFWorkbook wb = new HSSFWorkbook();//创建excel工作簿
        HSSFSheet sheet = wb.createSheet("Sheet");//在Excel工作簿中建一工作表，其名为缺省值, 也可以指定Sheet名称，例如Sheet
        HSSFCellStyle style = wb.createCellStyle();//生成一个样式
        HSSFFont font = wb.createFont(); // 生成一个字体
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平居中
        font.setFontHeightInPoints((short) 14);
        font.setFontName("宋体");//设置字体
        style.setFont(font);// 把字体样式 应用到当前样式
        HSSFRow row = sheet.createRow(0);
        CellRangeAddress cra = new CellRangeAddress(0, 0, 0, 6);//合并
        row = sheet.createRow(0);//设置表头起始行
        mycreateCell(row, 0, "序号", style);
        mycreateCell(row, 1, "姓名", style);
        mycreateCell(row, 2, "推荐单位", style);
        mycreateCell(row, 3, "单位", style);
        mycreateCell(row, 4, "申报系列", style);
        mycreateCell(row, 5, "申报资格", style);
        mycreateCell(row, 6, "申报专业", style);
        mycreateCell(row, 7, "申报类型", style);
        mycreateCell(row, 8, "评委会", style);
        mycreateCell(row, 9, "当前状态", style);

        for (int i = 0; i < list.size(); i++) {
            int j = i + 1;//设置数据起始行
            row = sheet.createRow(j);
            //设值
            mycreateCell(row, 0, i + 1, style);
            mycreateCell(row, 1, String.valueOf(list.get(i).get("DISPLAY_NAME")), style);
            mycreateCell(row, 2, String.valueOf(list.get(i).get("UNIT_NAME")), style);
            mycreateCell(row, 3, String.valueOf(list.get(i).get("UNIT_NAME")), style);
            mycreateCell(row, 4, String.valueOf(list.get(i).get("XILIE")), style);
            mycreateCell(row, 5, String.valueOf(list.get(i).get("ZIGE")), style);
            mycreateCell(row, 6, String.valueOf(list.get(i).get("PROFESSIAL_NAME")), style);
            mycreateCell(row, 7, String.valueOf(list.get(i).get("LEIXING")), style);
            mycreateCell(row, 8, String.valueOf(list.get(i).get("JUDGING_NAME")), style);
            mycreateCell(row, 9, String.valueOf(list.get(i).get("CURRENT_STATE")), style);
            sheet.autoSizeColumn((short) 0); //调整第一列宽度，自适应
            sheet.autoSizeColumn((short) 1); //调整第二列宽度，自适应
            sheet.autoSizeColumn((short) 2); //调整第三列宽度，自适应
            sheet.autoSizeColumn((short) 3); //调整第四列宽度，自适应
            sheet.autoSizeColumn((short) 4); //调整第五列宽度，自适应
            sheet.autoSizeColumn((short) 5); //调整第五列宽度，自适应
            sheet.autoSizeColumn((short) 6); //调整第五列宽度，自适应
            sheet.autoSizeColumn((short) 7); //调整第五列宽度，自适应
            sheet.autoSizeColumn((short) 8); //调整第五列宽度，自适应
            sheet.autoSizeColumn((short) 9); //调整第五列宽度，自适应

        }
        try {
            response.setContentType("text/html;charset=UTF-8");
            response.reset();// 清空输出流
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment;filename=\""
                    + new String((filename + ".xls").getBytes("GBK"),
                    "ISO8859_1") + "\"");
            OutputStream ouputStream = response.getOutputStream();
            wb.write(ouputStream);
            ouputStream.flush();
            ouputStream.close();
        } catch (Exception e) {
            logger.error("exportExcelLog e=" + e.getMessage());
            ResultUtils.writeFailed(response);
        }
        return null;
    }

    //跳到申报人状态查询界面
    @RequestMapping(value = "ProposerTypePage")
    public String ProposerTypePage(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("MenuBtns", userService.GetMenuBtns(request.getParameter("menuid"), getUser().getUserId()));
        return "sys/proposertype";
    }

    //申报人状态查询
    @RequestMapping(value = "selectProposerTypeList.do")
    public void selectProposerTypeList(HttpServletRequest request, HttpServletResponse response) {
        logger.info("显示申报人状态列表数据");
        PageData pd = new PageData();
        pd.put("UNIT_CODE", this.getUser().getUnitCode());
        pd.put("nian", request.getParameter("nian") == null ? "" : request.getParameter("nian"));
        pd.put("rows", request.getParameter("rows") == null ? 10 : Integer.parseInt(request.getParameter("rows")));//每页显示行数
        pd.put("page", request.getParameter("page") == null ? 0 : (Integer.parseInt(request.getParameter("page"))));//每页记录开始位置
        pd.put("basicUnitcode", request.getParameter("basicUnitcode") == null ? "" : request.getParameter("basicUnitcode"));
        pd.put("DISPLAY_NAME", request.getParameter("displayName1") == null ? "" : request.getParameter("displayName1"));//申报人申报阶段
        pd.put("backup2","0");
        TableReturn tablereturn = new TableReturn();
        List<PageData> list = proposerService.selectProposerTypeList(pd);
        if (null!=list) {
            for (PageData pageData : list) {
                PageData pda = new PageData();
                pda.put("UNIT_CODE", getUser().getUnitCode());
                pda.put("JUDGING_CODE", pageData.get("JUDGING_CODE"));
                pda.put("JUDGING_STAGE", "16".equals(pageData.get("JUDGING_STAGE")) ? "15" : pageData.get("JUDGING_STAGE"));
                if ("16".equals(pageData.get("JUDGING_STAGE")) || "15".equals(pageData.get("JUDGING_STAGE"))) {
                    pageData.put("UNIT_NAME3", selectCode(pda).getString("UNIT_NAME3"));
                } else {
                    pageData.put("UNIT_NAME3", selectCode(pageData).get("UNIT_NAME3").toString());
                }

            }
        }else {
            list = new ArrayList<>();
        }
        Integer listCount = proposerService.selectProposerTypeCount(pd);
        tablereturn.setRows(list);
        tablereturn.setTotal(listCount);

        ResultUtils.write(response, toJson(tablereturn));
    }

    //获取待审机构
    public PageData selectCode(PageData pageData) {
        PageData pd = new PageData();
        pd.put("PARENTUNIT_CODE", pageData.getString("UNIT_CODE"));
        List<PageData> item = new ArrayList<PageData>();

        if ("1".equals(pageData.getString("JUDGING_STAGE")) || "2".equals(pageData.getString("JUDGING_STAGE"))) {
            pd.put("state", 1);
            item = proposerService.selectUnitCode(pd);
        } else if ("3".equals(pageData.getString("JUDGING_STAGE")) || "4".equals(pageData.getString("JUDGING_STAGE"))) {
            pd.put("state", 3);
            item = proposerService.selectUnitCode(pd);
        } else if ("5".equals(pageData.getString("JUDGING_STAGE")) || "6".equals(pageData.getString("JUDGING_STAGE"))) {
            pd.put("state", 5);
            item = proposerService.selectUnitCode(pd);
            //pd.put("UNIT_NAME3",pageData.getString("UNIT_NAME"));
        } else if ("7".equals(pageData.getString("JUDGING_STAGE")) || "8".equals(pageData.getString("JUDGING_STAGE"))) {
            pd.put("state", 7);
            item = proposerService.selectUnitCode(pd);
        } else if ("9".equals(pageData.getString("JUDGING_STAGE")) || "10".equals(pageData.getString("JUDGING_STAGE"))) {
            pd.put("UNIT_ATTACH", 40);
            item = proposerService.selectUnitCode(pd);
        } else if ("11".equals(pageData.getString("JUDGING_STAGE")) || "12".equals(pageData.getString("JUDGING_STAGE"))) {
            pd.put("UNIT_ATTACH", 20);
            item = proposerService.selectUnitCode(pd);
        } else if ("13".equals(pageData.getString("JUDGING_STAGE")) || "14".equals(pageData.getString("JUDGING_STAGE"))) {
            pd.put("UNIT_ATTACH", 10);
            item = proposerService.selectUnitCode(pd);
        } else if ("15".equals(pageData.getString("JUDGING_STAGE"))) {
            pd.put("JUDGING_CODE", pageData.getString("JUDGING_CODE"));
            item = proposerService.selectUnitByJudgingCode(pd);
        }
        if (item.size() == 1) {
            pd.put("UNIT_NAME3", item.get(0).getString("UNIT_NAME"));
            return pd;
        }else {
            pd.put("UNIT_NAME3","");
        }
        return pd;
    }

    /***导出申报人员状态信息***/
    @RequestMapping("/exportExcelProposerType.do")
    public String exportExcelProposerType(HttpServletRequest request, HttpServletResponse response, Model model) {
        logger.info("导出申报人员状态信息");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sss = new SimpleDateFormat("yyyy-MM-dd ");
        String filename = sss.format(new Date().getTime());
        int page = request.getParameter("page") == null ? 1 : Integer.parseInt(request.getParameter("page"));
        int rows = request.getParameter("rows") == null ? 10 : Integer.parseInt(request.getParameter("rows"));
        PageData pageData = new PageData();
        pageData.put("UNIT_CODE", this.getUser().getUnitCode());
        pageData.put("nian", request.getParameter("nian") == null ? "" : request.getParameter("nian"));
        pageData.put("basicUnitcode", request.getParameter("basicUnitcode") == null ? "" : request.getParameter("basicUnitcode"));
        pageData.put("DISPLAY_NAME", request.getParameter("displayName1") == null ? "" : request.getParameter("displayName1"));//申报人申报阶段
        pageData.put("page", page);
        pageData.put("rows", rows);
        List<PageData> list = proposerService.selectProposerTypeList(pageData);
        for (PageData pd : list) {
            pd.put("UNIT_NAME3", selectCode(pd).getString("UNIT_NAME3"));
        }
        HSSFWorkbook wb = new HSSFWorkbook();//创建excel工作簿
        HSSFSheet sheet = wb.createSheet("Sheet");//在Excel工作簿中建一工作表，其名为缺省值, 也可以指定Sheet名称，例如Sheet
        HSSFCellStyle style = wb.createCellStyle();//生成一个样式
        HSSFFont font = wb.createFont(); // 生成一个字体
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平居中
        font.setFontHeightInPoints((short) 14);
        font.setFontName("宋体");//设置字体
        style.setFont(font);// 把字体样式 应用到当前样式
        HSSFRow row = sheet.createRow(0);
        CellRangeAddress cra = new CellRangeAddress(0, 0, 0, 6);//合并
        row = sheet.createRow(0);//设置表头起始行
        mycreateCell(row, 0, "序号", style);
        mycreateCell(row, 1, "姓名", style);
        mycreateCell(row, 2, "身份证", style);
        mycreateCell(row, 3, "申报单位名称", style);
        mycreateCell(row, 4, "申报职称", style);
        mycreateCell(row, 5, "学科专业", style);
        mycreateCell(row, 6, "评委会", style);
        mycreateCell(row, 7, "待审机构", style);
        mycreateCell(row, 8, "审查状态", style);

        for (int i = 0; i < list.size(); i++) {
            int j = i + 1;//设置数据起始行
            row = sheet.createRow(j);
            //设值
            mycreateCell(row, 0, i + 1, style);
            mycreateCell(row, 1, String.valueOf(list.get(i).getString("DISPLAY_NAME")), style);
            mycreateCell(row, 2, String.valueOf(list.get(i).getString("ID_CARD_NO")), style);
            mycreateCell(row, 3, String.valueOf(list.get(i).getString("UNIT_NAME")), style);
            mycreateCell(row, 4, String.valueOf(list.get(i).getString("DICT_NAME")), style);
            mycreateCell(row, 5, String.valueOf(list.get(i).getString("PROFESSIAL_NAME")), style);
            System.out.print(list.get(i).getString("UNIT_NAME3"));
            mycreateCell(row, 6, String.valueOf(list.get(i).getString("JUDGING_NAME")), style);
            mycreateCell(row, 7, String.valueOf(list.get(i).getString("UNIT_NAME")), style);
            mycreateCell(row, 8, String.valueOf(list.get(i).getString("CURRENT_STATE")), style);
            sheet.autoSizeColumn((short) 0); //调整第一列宽度，自适应
            sheet.autoSizeColumn((short) 1); //调整第二列宽度，自适应
            sheet.autoSizeColumn((short) 2); //调整第三列宽度，自适应
            sheet.autoSizeColumn((short) 3); //调整第四列宽度，自适应
            sheet.autoSizeColumn((short) 4); //调整第五列宽度，自适应
            sheet.autoSizeColumn((short) 5); //调整第五列宽度，自适应
            sheet.autoSizeColumn((short) 6); //调整第五列宽度，自适应
            sheet.autoSizeColumn((short) 7); //调整第五列宽度，自适应
            sheet.autoSizeColumn((short) 8); //调整第五列宽度，自适应
        }
        try {
            response.setContentType("text/html;charset=UTF-8");
            response.reset();// 清空输出流
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment;filename=\""
                    + new String((filename + ".xls").getBytes("GBK"),
                    "ISO8859_1") + "\"");
            OutputStream ouputStream = response.getOutputStream();
            wb.write(ouputStream);
            ouputStream.flush();
            ouputStream.close();
        } catch (Exception e) {
            logger.error("exportExcelLog e=" + e.getMessage());
            ResultUtils.writeFailed(response);
        }
        return null;
    }

    //数值型
    public void mycreateCell(HSSFRow row, int i, double value, HSSFCellStyle style) {
        HSSFCell cell = row.createCell(i);
        cell.setCellValue(value);
        cell.setCellStyle(style);
    }

    //获取界面列表参评人员审查
    @RequestMapping(value = "GetJudgingProposerListCan.do")
    public void GetJudgingProposerListCan(HttpServletRequest request, HttpServletResponse response) {
        logger.info("显示需审批的申报人列表数据(参评人员)");
        PageData pd = new PageData();
        pd.put("rows", request.getParameter("rows") == null ? 10 : Integer.parseInt(request.getParameter("rows")));//每页显示行数
        pd.put("page", request.getParameter("page") == null ? 0 : (Integer.parseInt(request.getParameter("page"))));//每页记录开始位置
        pd.put("declareUnitcode", getUser().getUnitCode());//当前登录人的单位编码
        if(null!=getUser().getJudgingCode()){
            pd.put("JUDGING_CODE", getUser().getJudgingCode());//当前评委会
        }
        //申报人姓名
        pd.put("displayName", request.getParameter("displayName2") == null ? "" : request.getParameter("displayName2"));//申报人显示名称
        pd.put("pwh", request.getParameter("pwh") == null ? "" : request.getParameter("pwh"));
        //管理机构
        pd.put("gljg", request.getParameter("gljg") == null ? "" : request.getParameter("gljg"));
        pd.put("nian", request.getParameter("nian") == null ? "" : request.getParameter("nian"));
        pd.put("xilie", request.getParameter("xl") == null ? "" : request.getParameter("xl"));
        pd.put("sbzg", request.getParameter("sbzg") == null ? "" : request.getParameter("sbzg"));
        pd.put("sczt", request.getParameter("sczt") == null ? "" : request.getParameter("sczt"));
        pd.put("dqzt", request.getParameter("dqzt") == null ? "" : request.getParameter("dqzt"));
        pd.put("judgingStage", request.getParameter("judgingStage") == null ? "" : request.getParameter("judgingStage"));//申报人申报阶段
        TableReturn tablereturn = new TableReturn();
        List<Map<String, String>> list = proposerService.getJudgingProposerListCan(pd);
        Integer listCount = proposerService.getJudgingProposerListCountCan(pd);
        if (list != null) {
            for (Map<String, String> map : list) {
                PageData pda = new PageData();
                pda.put("UNIT_CODE", getUser().getUnitCode());
                pda.put("JUDGING_CODE", map.get("JUDGING_CODE"));
                pda.put("JUDGING_STAGE", pd.get("judgingStage"));
                map.put("UNIT_NAME3", selectCode(pda).getString("UNIT_NAME3"));//待审机构
            }
        }
        tablereturn.setRows(list);
        tablereturn.setTotal(listCount);
        ResultUtils.write(response, toJson(tablereturn));
    }

    /***导出参评的申报人列表数据***/
    @RequestMapping("/exportExcelProposerCan.do")
    public String exportExcelProposerCan(HttpServletRequest request, HttpServletResponse response, Model model) {
        logger.info("导出参评的申报人列表数据");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sss = new SimpleDateFormat("yyyy-MM-dd ");
        String filename = sss.format(new Date().getTime());
        int page = request.getParameter("page") == null ? 1 : Integer.parseInt(request.getParameter("page"));
        int rows = request.getParameter("rows") == null ? 10000000 : Integer.parseInt(request.getParameter("rows"));
        PageData pageData = new PageData();
        pageData.put("page", page);
        pageData.put("rows", rows);
        pageData.put("declareUnitcode", getUser().getUnitCode());//当前登录人的单位编码
        //申报人姓名
        pageData.put("displayName", request.getParameter("displayName2") == null ? "" : request.getParameter("displayName2"));//申报人显示名称
        pageData.put("pwh", request.getParameter("pwh") == null ? "" : request.getParameter("pwh"));
        //管理机构
        pageData.put("gljg", request.getParameter("gljg") == null ? "" : request.getParameter("gljg"));
        pageData.put("nian", request.getParameter("nian") == null ? "" : request.getParameter("nian"));
        pageData.put("xilie", request.getParameter("xl") == null ? "" : request.getParameter("xl"));
        pageData.put("sbzg", request.getParameter("sbzg") == null ? "" : request.getParameter("sbzg"));
        pageData.put("sczt", request.getParameter("sczt") == null ? "" : request.getParameter("sczt"));
        pageData.put("dqzt", request.getParameter("dqzt") == null ? "" : request.getParameter("dqzt"));
        pageData.put("judgingStage", request.getParameter("judgingStage") == null ? "" : request.getParameter("judgingStage"));//申报人申报阶段

        List<Map<String, String>> list = proposerService.getJudgingProposerListCan(pageData);
        if (list != null) {
            for (Map<String, String> map : list) {
                PageData pda = new PageData();
                pda.put("UNIT_CODE", getUser().getUnitCode());
                pda.put("JUDGING_CODE", map.get("JUDGING_CODE"));
                pda.put("JUDGING_STAGE", pageData.get("judgingStage"));
                map.put("UNIT_NAME3", selectCode(pda).getString("UNIT_NAME3"));//待审机构
            }
        }

        HSSFWorkbook wb = new HSSFWorkbook();//创建excel工作簿
        HSSFSheet sheet = wb.createSheet("Sheet");//在Excel工作簿中建一工作表，其名为缺省值, 也可以指定Sheet名称，例如Sheet
        HSSFCellStyle style = wb.createCellStyle();//生成一个样式
        HSSFFont font = wb.createFont(); // 生成一个字体
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平居中
        font.setFontHeightInPoints((short) 14);
        font.setFontName("宋体");//设置字体
        style.setFont(font);// 把字体样式 应用到当前样式
        HSSFRow row = sheet.createRow(0);
        CellRangeAddress cra = new CellRangeAddress(0, 0, 0, 6);//合并
        row = sheet.createRow(0);//设置表头起始行
        mycreateCell(row, 0, "序号", style);
        mycreateCell(row, 1, "姓名", style);
        mycreateCell(row, 2, "推荐单位", style);
        mycreateCell(row, 3, "单位", style);
        mycreateCell(row, 4, "申报系列", style);
        mycreateCell(row, 5, "申报资格", style);
        mycreateCell(row, 6, "申报专业", style);
        mycreateCell(row, 7, "申报类型", style);
        mycreateCell(row, 8, "评委会", style);
        mycreateCell(row, 9, "待审机构", style);
        mycreateCell(row, 10, "当前状态", style);

        for (int i = 0; i < list.size(); i++) {
            int j = i + 1;//设置数据起始行
            row = sheet.createRow(j);
            //设值
            mycreateCell(row, 0, i + 1, style);
            mycreateCell(row, 1, String.valueOf(list.get(i).get("DISPLAY_NAME")), style);
            mycreateCell(row, 2, String.valueOf(list.get(i).get("UNIT_NAME")), style);
            mycreateCell(row, 3, String.valueOf(list.get(i).get("UNIT_NAME")), style);
            mycreateCell(row, 4, String.valueOf(list.get(i).get("XILIE")), style);
            mycreateCell(row, 5, String.valueOf(list.get(i).get("ZIGE")), style);
            mycreateCell(row, 6, String.valueOf(list.get(i).get("PROFESSIAL_NAME")), style);
            mycreateCell(row, 7, String.valueOf(list.get(i).get("LEIXING")), style);
            mycreateCell(row, 8, String.valueOf(list.get(i).get("JUDGING_NAME")), style);
            mycreateCell(row, 9, String.valueOf(list.get(i).get("UNIT_NAME3")), style);
            mycreateCell(row, 10, String.valueOf(list.get(i).get("CURRENT_STATE")), style);
            sheet.autoSizeColumn((short) 0); //调整第一列宽度，自适应
            sheet.autoSizeColumn((short) 1); //调整第二列宽度，自适应
            sheet.autoSizeColumn((short) 2); //调整第三列宽度，自适应
            sheet.autoSizeColumn((short) 3); //调整第四列宽度，自适应
            sheet.autoSizeColumn((short) 4); //调整第五列宽度，自适应
            sheet.autoSizeColumn((short) 5); //调整第五列宽度，自适应
            sheet.autoSizeColumn((short) 6); //调整第五列宽度，自适应
            sheet.autoSizeColumn((short) 7); //调整第五列宽度，自适应
            sheet.autoSizeColumn((short) 8); //调整第五列宽度，自适应
            sheet.autoSizeColumn((short) 9); //调整第五列宽度，自适应
            sheet.autoSizeColumn((short) 10); //调整第五列宽度，自适应

        }
        try {
            response.setContentType("text/html;charset=UTF-8");
            response.reset();// 清空输出流
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment;filename=\""
                    + new String((filename + ".xls").getBytes("GBK"),
                    "ISO8859_1") + "\"");
            OutputStream ouputStream = response.getOutputStream();
            wb.write(ouputStream);
            ouputStream.flush();
            ouputStream.close();
        } catch (Exception e) {
            logger.error("exportExcelLog e=" + e.getMessage());
            ResultUtils.writeFailed(response);
        }
        return null;
    }
}
