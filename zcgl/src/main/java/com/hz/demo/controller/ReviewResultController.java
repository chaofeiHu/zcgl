package com.hz.demo.controller;

import com.hz.demo.core.PageData;
import com.hz.demo.core.ResultUtils;
import com.hz.demo.core.TableReturn;
import com.hz.demo.core.Tree;
import com.hz.demo.model.*;
import com.hz.demo.services.*;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.hz.demo.core.EncodeUtil.encodeMD5;
import static com.hz.demo.core.ResultUtils.toDateTimeJson;
import static com.hz.demo.core.ResultUtils.toJson;
//评审结果审核页面用
@Controller
@RequestMapping("/ReviewResult")
public class ReviewResultController extends BaseController{

    @Autowired
    UserService userService;
    @Autowired
    ReviewResultService reviewResultService;
    @Autowired
    ResultPublicityService resultPublicityService;
    @Autowired
    ResultDissentService resultDissentService;
    @Autowired
    ResultObjectionService resultObjectionService;

   //跳到评审结果界面
    @RequestMapping(value = "ReviewResultPage")
    public String DeptPage(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("MenuBtns",userService.GetMenuBtns(request.getParameter("menuid"),getUser().getUserId()));
        return "sys/reviewResult";
    }

    //获取界面列表
    @RequestMapping(value = "getList")
    public void getList(HttpServletRequest request, HttpServletResponse response) {
        logger.info("显示评审结果列表数据");
        PageData pd = new PageData();
        String yearNo = request.getParameter("yearNo");
        pd.put("rows", request.getParameter("rows") == null? 10: Integer.parseInt(request.getParameter("rows")));//每页显示行数
        pd.put("page", request.getParameter("page") == null ? 1 : (Integer.parseInt(request.getParameter("page"))));//每页记录开始位置
        pd.put("userName", request.getParameter("userName"));
        pd.put("yearNo", yearNo);
        pd.put("judgingId", request.getParameter("judgingCode"));
        pd.put("unitCode", request.getParameter("unitCode"));
        pd.put("reviewSeries", request.getParameter("reviewSeries"));
        pd.put("professialCode", request.getParameter("professialCode"));
        pd.put("manageUnitCode", getUser().getUnitCode());
        pd.put("areaCode", request.getParameter("areaCode"));
        TableReturn tablereturn = new TableReturn();
        List<rec_reviewresult> blist = reviewResultService.getList(pd);
        Integer listCount = reviewResultService.getListCount(pd);
        tablereturn.setRows(blist);
        tablereturn.setTotal(listCount);
        ResultUtils.write(response, toDateTimeJson(tablereturn));
    }

    //确认审核
    @Transactional(propagation= Propagation.REQUIRED)
    @RequestMapping(value = "confirmResult")
    public void confirmResult(HttpServletRequest request, HttpServletResponse response) {
        String ids = request.getParameter("ids") == null ? "" : request.getParameter("ids");
        String[] strs = ids.split(",");
        int flag = 0;
        try {
            for (String id:strs) {
                Integer id1 = Integer.parseInt(id);//评审结果id

                BigDecimal bid = new BigDecimal(id1);
                rec_reviewresult recReviewresult = reviewResultService.getModel(bid);
                if (null != recReviewresult) {
                    //如果大评会评审通过 进入通过结果库  公示
                    if (recReviewresult.getReviewResult().intValue()==1) {//通过
                        rec_reviewresultpublicity resultPublicity = new rec_reviewresultpublicity();
                        resultPublicity.setAreaCode(recReviewresult.getAreaCode());
                        resultPublicity.setBack1(recReviewresult.getBack1());
                        resultPublicity.setBack2(recReviewresult.getBack1());
                        resultPublicity.setBack3(recReviewresult.getBack1());
                        resultPublicity.setGroupId(recReviewresult.getGroupId());
                        resultPublicity.setGroupResult(recReviewresult.getGroupResult());
                        resultPublicity.setGroupResultNo(recReviewresult.getGroupResultNo());
                        resultPublicity.setGroupResultYes(recReviewresult.getGroupResultYes());
                        resultPublicity.setGroupResultOpinion(recReviewresult.getGroupResultOpinion());
                        resultPublicity.setGroupResultWaive(recReviewresult.getGroupResultWaive());
                        resultPublicity.setIdCardNo(recReviewresult.getIdCardNo());
                        resultPublicity.setJudgingCode(recReviewresult.getJudgingCode());
                        resultPublicity.setOptTime(recReviewresult.getOptTime());
                        resultPublicity.setOptUserid(recReviewresult.getOptUserid());
                        resultPublicity.setPositionalTitles(recReviewresult.getPositionalTitles());
                        resultPublicity.setProfessialCode(recReviewresult.getProfessialCode());
                        resultPublicity.setReviewResult(recReviewresult.getReviewResult());
                        resultPublicity.setReviewResultNo(recReviewresult.getReviewResultNo());
                        resultPublicity.setReviewResultOpinion(recReviewresult.getReviewResultOpinion());
                        resultPublicity.setReviewResultWaive(recReviewresult.getReviewResultWaive());
                        resultPublicity.setReviewResultYes(recReviewresult.getReviewResultYes());
                        resultPublicity.setReviewSeries(recReviewresult.getReviewSeries());
                        resultPublicity.setReviewType(recReviewresult.getReviewType());
                        resultPublicity.setSex(recReviewresult.getSex());
                        resultPublicity.setTitleLevel(recReviewresult.getTitleLevel());
                        resultPublicity.setUnitCode(recReviewresult.getUnitCode());
                        resultPublicity.setUserId(recReviewresult.getUserId());
                        resultPublicity.setUserName(recReviewresult.getUserName());
                        resultPublicity.setYearNo(recReviewresult.getYearNo());
                        resultPublicity.setOptTime(new Date());
                        flag = resultPublicityService.add(resultPublicity);
                        if (flag == 0) {
                            throw new RuntimeException();
                        }else {//如果信息已录入通过库更改本条数据状态
                            PageData pageData = new PageData();
                            pageData.put("id", bid);
                            pageData.put("resultState", 1);
                            flag = reviewResultService.update(pageData);
                        }
                    }
                    //如果大评会评审不通过 进入未通过结果库
                    else{
                        rec_resultdissent resultDissent = new rec_resultdissent();
                        resultDissent.setAreaCode(recReviewresult.getAreaCode());
                        resultDissent.setBack1(recReviewresult.getBack1());
                        resultDissent.setBack2(recReviewresult.getBack1());
                        resultDissent.setBack3(recReviewresult.getBack1());
                        resultDissent.setGroupId(recReviewresult.getGroupId());
                        resultDissent.setGroupResult(recReviewresult.getGroupResult());
                        resultDissent.setGroupResultNo(recReviewresult.getGroupResultNo());
                        resultDissent.setGroupResultYes(recReviewresult.getGroupResultYes());
                        resultDissent.setGroupResultOpinion(recReviewresult.getGroupResultOpinion());
                        resultDissent.setGroupResultWaive(recReviewresult.getGroupResultWaive());
                        resultDissent.setIdCardNo(recReviewresult.getIdCardNo());
                        resultDissent.setJudgingCode(recReviewresult.getJudgingCode());
                        resultDissent.setOptTime(recReviewresult.getOptTime());
                        resultDissent.setOptUserid(recReviewresult.getOptUserid());
                        resultDissent.setPositionalTitles(recReviewresult.getPositionalTitles());
                        resultDissent.setProfessialCode(recReviewresult.getProfessialCode());
                        resultDissent.setReviewResult(recReviewresult.getReviewResult());
                        resultDissent.setReviewResultNo(recReviewresult.getReviewResultNo());
                        resultDissent.setReviewResultOpinion(recReviewresult.getReviewResultOpinion());
                        resultDissent.setReviewResultWaive(recReviewresult.getReviewResultWaive());
                        resultDissent.setReviewResultYes(recReviewresult.getReviewResultYes());
                        resultDissent.setReviewSeries(recReviewresult.getReviewSeries());
                        resultDissent.setReviewType(recReviewresult.getReviewType());
                        resultDissent.setSex(recReviewresult.getSex());
                        resultDissent.setTitleLevel(recReviewresult.getTitleLevel());
                        resultDissent.setUnitCode(recReviewresult.getUnitCode());
                        resultDissent.setUserId(recReviewresult.getUserId());
                        resultDissent.setUserName(recReviewresult.getUserName());
                        resultDissent.setYearNo(recReviewresult.getYearNo());
                        resultDissent.setOptTime(new Date());
                        flag = resultDissentService.add(resultDissent);
                        if (flag == 0) {
                            throw new RuntimeException();
                        }else {//如果信息已录入未通过库更改本条数据状态
                            PageData pageData = new PageData();
                            pageData.put("id", bid);
                            pageData.put("resultState", 2);
                            flag = reviewResultService.update(pageData);
                        }
                    }
                }
            }
            if (flag == 1){
                ResultUtils.writeMessage(response, 1, "审核成功");
            }
            else{
                ResultUtils.writeMessage(response, 0, "审核失败");
                throw new RuntimeException();
            }
        } catch (Exception ex) {
            ResultUtils.writeMessage(response, 0, "审核失败");
            throw new RuntimeException();
        }
    }

    //转异议人员库
    @Transactional(propagation= Propagation.REQUIRED)
    @RequestMapping(value = "toObjection")
    public void toObjection(HttpServletRequest request, HttpServletResponse response) {
        String ids = request.getParameter("ids") == null ? "" : request.getParameter("ids");
        String[] strs = ids.split(",");
        try {
            for (String id:strs) {
                Integer id1 = Integer.parseInt(id);//评审结果id
                BigDecimal bid = new BigDecimal(id1);
                int flag = 0;
                rec_reviewresult recReviewresult = reviewResultService.getModel(bid);
                if (null != recReviewresult) {
                    //将本条评审结果的信息赋值给异议人员库中
                    rec_resultobjection resultObjection = new rec_resultobjection();
                    resultObjection.setAreaCode(recReviewresult.getAreaCode());
                    resultObjection.setBack1(recReviewresult.getBack1());
                    resultObjection.setBack2(recReviewresult.getBack1());
                    resultObjection.setBack3(recReviewresult.getBack1());
                    resultObjection.setGroupId(recReviewresult.getGroupId());
                    resultObjection.setGroupResult(recReviewresult.getGroupResult());
                    resultObjection.setGroupResultNo(recReviewresult.getGroupResultNo());
                    resultObjection.setGroupResultYes(recReviewresult.getGroupResultYes());
                    resultObjection.setGroupResultOpinion(recReviewresult.getGroupResultOpinion());
                    resultObjection.setGroupResultWaive(recReviewresult.getGroupResultWaive());
                    resultObjection.setIdCardNo(recReviewresult.getIdCardNo());
                    resultObjection.setJudgingCode(recReviewresult.getJudgingCode());
                    resultObjection.setOptTime(recReviewresult.getOptTime());
                    resultObjection.setOptUserid(recReviewresult.getOptUserid());
                    resultObjection.setPositionalTitles(recReviewresult.getPositionalTitles());
                    resultObjection.setProfessialCode(recReviewresult.getProfessialCode());
                    resultObjection.setReviewResult(recReviewresult.getReviewResult());
                    resultObjection.setReviewResultNo(recReviewresult.getReviewResultNo());
                    resultObjection.setReviewResultOpinion(recReviewresult.getReviewResultOpinion());
                    resultObjection.setReviewResultWaive(recReviewresult.getReviewResultWaive());
                    resultObjection.setReviewResultYes(recReviewresult.getReviewResultYes());
                    resultObjection.setReviewSeries(recReviewresult.getReviewSeries());
                    resultObjection.setReviewType(recReviewresult.getReviewType());
                    resultObjection.setSex(recReviewresult.getSex());
                    resultObjection.setTitleLevel(recReviewresult.getTitleLevel());
                    resultObjection.setUnitCode(recReviewresult.getUnitCode());
                    resultObjection.setUserId(recReviewresult.getUserId());
                    resultObjection.setUserName(recReviewresult.getUserName());
                    resultObjection.setYearNo(recReviewresult.getYearNo());
                    resultObjection.setOptTime(new Date());
                    flag = resultObjectionService.add(resultObjection);
                    //更改本条数据状态
                    if (flag == 1) {
                        PageData pageData = new PageData();
                        pageData.put("id", bid);
                        pageData.put("resultState", 3);
                        flag = reviewResultService.update(pageData);
                    }
                }
                if (flag == 1){
                    ResultUtils.writeMessage(response, 1, "转异议人员库成功");
                }
                else{
                    ResultUtils.writeMessage(response, 0, "转异议人员库失败");
                    throw new RuntimeException();
                }
            }
        } catch (Exception ex) {
            ResultUtils.writeMessage(response, 0, "转异议人员库失败");
            throw new RuntimeException();
        }
    }

    //导出评审结果信息
    @RequestMapping("/exportExcelReviewResult.do")
    public String exportExcelReviewResult(HttpServletRequest request, HttpServletResponse response, Model model) {
        logger.info("获取评审结果列表数据");
        PageData pd = new PageData();
        pd.put("rows", request.getParameter("rows") == null? 10: Integer.parseInt(request.getParameter("rows")));//每页显示行数
        pd.put("page", request.getParameter("page") == null ? 1 : (Integer.parseInt(request.getParameter("page"))));//每页记录开始位置
        pd.put("reviewSeries", request.getParameter("reviewSeries"));
        pd.put("judgingId", request.getParameter("judgingCode"));
        pd.put("yearNo", request.getParameter("yearNo"));
        pd.put("userName", request.getParameter("userName"));
        pd.put("professialCode", request.getParameter("professialCode"));
        pd.put("areaCode", request.getParameter("areaCode"));
        List<rec_reviewresult> blist = reviewResultService.getList(pd);
        String filename = "评审结果审核信息列表"+String.valueOf(new Date().getTime());
        HSSFWorkbook wb = new HSSFWorkbook();//创建excel工作簿
        HSSFSheet sheet = wb.createSheet("Sheet");//在Excel工作簿中建一工作表，其名为缺省值, 也可以指定Sheet名称，例如Sheet
        HSSFCellStyle style = wb.createCellStyle();//生成一个样式
        HSSFFont font = wb.createFont(); // 生成一个字体
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平居中
        font.setFontHeightInPoints((short) 10);
        font.setFontName("宋体");//设置字体
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        HSSFFont font2 = wb.createFont(); // 生成一个字体
        font2.setFontHeightInPoints((short) 10);
        font2.setFontName("宋体");//设置字体
        style.setFont(font);// 把字体样式 应用到当前样式
        HSSFRow row = sheet.createRow(0);
        CellRangeAddress cra = new CellRangeAddress(0, 0, 0, 6);//合并
        row = sheet.createRow(0);//设置表头起始行
        mycreateCell(row,0,"年度",style);
        mycreateCell(row, 1, "地市", style);
        mycreateCell(row, 2, "主管单位名称", style);
        mycreateCell(row, 3, "姓名", style);
        mycreateCell(row, 4, "性别", style);
        mycreateCell(row, 5, "身份证号", style);
        mycreateCell(row, 6, "评委会名称", style);
        mycreateCell(row, 7, "申报系列", style);
        mycreateCell(row, 8, "申报级别", style);
        mycreateCell(row, 9, "申报职称", style);
        mycreateCell(row, 10, "申报专业", style);
        mycreateCell(row, 11, "专业组同意票数", style);
        mycreateCell(row, 12, "专业组反对票数", style);
        mycreateCell(row, 13, "专业组弃权票数", style);
        mycreateCell(row, 14, "专业组评议意见", style);
        mycreateCell(row, 15, "专业组是否通过", style);
        mycreateCell(row, 16, "大评会同意票数", style);
        mycreateCell(row, 17, "大评会反对票数", style);
        mycreateCell(row, 18, "大评会弃权票数", style);
        mycreateCell(row, 19, "大评会评议意见", style);
        mycreateCell(row, 20, "大评会是否通过", style);
        mycreateCell(row, 21, "评审类型", style);
        mycreateCell(row, 22, "备注", style);
        try {
            if (null != blist && blist.size()!= 0 ) {
                style.setFont(font2);
                for (int i = 0; i < blist.size(); i++) {
                    int j = i + 1;//设置数据起始行
                    row = sheet.createRow(j);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    //设值
                    mycreateCell(row,0,String.valueOf(blist.get(i).getYearNo()),style);
                    String areaCode = blist.get(i).getAreaCode();
                    String grade = blist.get(i).getBack2();
                    if ("河南省".equals(areaCode)) {
                        mycreateCell(row,1,String.valueOf("省直"),style);
                    } else if ("3".equals(grade)) {
                        mycreateCell(row,1,String.valueOf(blist.get(i).getBack3()),style);
                    }else {
                        mycreateCell(row,1,String.valueOf(areaCode),style);
                    }
                    mycreateCell(row,2,String.valueOf(blist.get(i).getUnitCode()),style);
                    mycreateCell(row,3,String.valueOf(blist.get(i).getUserName()),style);
                    switch (blist.get(i).getSex()) {
                        case "1":mycreateCell(row,4,String.valueOf("男"),style); break;
                        case "2":mycreateCell(row,4,String.valueOf("女"),style); break;
                        default:mycreateCell(row,4,String.valueOf("未说明性别"),style); break;
                    }
                    mycreateCell(row,5,String.valueOf(blist.get(i).getIdCardNo()),style);
                    mycreateCell(row,6,String.valueOf(blist.get(i).getJudgingCode()),style);
                    mycreateCell(row,7,String.valueOf(blist.get(i).getReviewSeries()),style);
                    mycreateCell(row,8,String.valueOf(blist.get(i).getTitleLevel()),style);
                    mycreateCell(row,9,String.valueOf(blist.get(i).getPositionalTitles()),style);
                    mycreateCell(row,10,String.valueOf(blist.get(i).getProfessialCode()),style);
                    mycreateCell(row,11,String.valueOf(blist.get(i).getGroupResultYes()),style);
                    mycreateCell(row,12,String.valueOf(blist.get(i).getGroupResultNo()),style);
                    mycreateCell(row,13,String.valueOf(blist.get(i).getGroupResultWaive()),style);
                    mycreateCell(row,14,String.valueOf(blist.get(i).getGroupResultOpinion()),style);
                    if (null != blist.get(i).getGroupResult()) {
                        switch (blist.get(i).getGroupResult().intValue()) {
                            case 0:
                                mycreateCell(row, 15, String.valueOf("未通过"), style);
                                break;
                            case 1:
                                mycreateCell(row, 15, String.valueOf("通过"), style);
                                break;
                            default:
                                mycreateCell(row, 15, String.valueOf("专业组未评审"), style);
                                break;
                        }
                    }else{
                        mycreateCell(row, 15, String.valueOf("专业组未评审"), style);
                    }
                    mycreateCell(row,16,String.valueOf(blist.get(i).getReviewResultYes()),style);
                    mycreateCell(row,17,String.valueOf(blist.get(i).getReviewResultNo()),style);
                    mycreateCell(row,18,String.valueOf(blist.get(i).getReviewResultWaive()),style);
                    mycreateCell(row,19,String.valueOf(blist.get(i).getReviewResultOpinion()),style);
                    if (null != blist.get(i).getReviewResult()) {
                        switch (blist.get(i).getReviewResult().intValue()) {
                            case 0:
                                mycreateCell(row, 20, String.valueOf("未通过"), style);
                                break;
                            case 1:
                                mycreateCell(row, 20, String.valueOf("通过"), style);
                                break;
                            default:
                                mycreateCell(row, 20, String.valueOf("大评会未评审"), style);
                                break;
                        }
                    }else{
                        mycreateCell(row, 20, String.valueOf("大评会未评审"), style);
                    }
                    mycreateCell(row,21,String.valueOf(blist.get(i).getReviewType()),style);
                    mycreateCell(row,22,String.valueOf(blist.get(i).getBack1()),style);
                    for (int k = 0;k <= 22;k++) {
                        sheet.autoSizeColumn((short) k); //调整列宽度，自适应
                    }
                }
            }
            response.setContentType("text/html;charset=UTF-8");
            response.reset();// 清空输出流
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment;filename=\""
                    + new String((filename + ".xlsx").getBytes("GBK"),
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
    //设置表格数据格式--字符串
    public static void mycreateCell(HSSFRow row, int i, String value, HSSFCellStyle style) {
        HSSFCell cell = row.createCell(i);
        if(!value.equals("null")){
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

    //数值型
    public void mycreateCell(HSSFRow row, int i, double value, HSSFCellStyle style) {
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

}
