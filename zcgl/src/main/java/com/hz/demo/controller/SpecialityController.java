package com.hz.demo.controller;

import com.hz.demo.core.*;
import com.hz.demo.model.*;
import com.hz.demo.services.EngageService;
import com.hz.demo.services.SpecialityService;
import com.hz.demo.services.UserService;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.hz.demo.core.EncodeUtil.encodeMD5;
import static com.hz.demo.core.ResultUtils.toDateJson;
import static com.hz.demo.core.ResultUtils.toDateTimeJson;
import static com.hz.demo.core.ResultUtils.toJson;

@Controller
@RequestMapping("/Speciality")
public class SpecialityController extends BaseController{

    @Autowired
    UserService userService;
    @Autowired
    SpecialityService specialityService;
    @Autowired
    EngageService engageService;


   //跳到评委专家界面
    @RequestMapping(value = "SpecialityPage")
    public String DeptPage(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("MenuBtns",userService.GetMenuBtns(request.getParameter("menuid"),getUser().getUserId()));
        return "sys/speciality";
    }

    //获取界面列表
    @RequestMapping(value = "getList")
    public void getList(HttpServletRequest request, HttpServletResponse response) {
        logger.info("显示评委专家列表数据");
        PageData pd = new PageData();
        pd.put("rows", request.getParameter("rows") == null? 10: Integer.parseInt(request.getParameter("rows")));//每页显示行数
        pd.put("page", request.getParameter("page") == null ? 1 : (Integer.parseInt(request.getParameter("page"))));//每页记录开始位置
        pd.put("specialityName", request.getParameter("specialityName"));
        pd.put("judgingId", request.getParameter("judgingCode"));
        pd.put("administrativeDutyLevel", request.getParameter("administrativeDutyLevel"));
        pd.put("professialLevel", request.getParameter("professialLevel"));
        pd.put("professialDutyLevel", request.getParameter("professialDutyLevel"));
        //String jid = request.getParameter("judgingId");
        //pd.put("judgingId", request.getParameter("judgingId"));//评委会id
        TableReturn tablereturn = new TableReturn();
        List<base_speciality> blist = specialityService.getList(pd);
        //List<base_engage> list = engageService.getList(pd);
        Integer listCount = specialityService.getListCount(pd);
        tablereturn.setRows(blist);
        tablereturn.setTotal(listCount);
        ResultUtils.write(response, toDateTimeJson(tablereturn));
    }
    //更改评委专家状态
    @RequestMapping(value = "updateState")
    public void updateState(HttpServletRequest request, HttpServletResponse response) {
        logger.info("更改评委专家状态");
        PageData pageData  = new PageData();
        String id = request.getParameter("id") == null ? "" : request.getParameter("id");
        pageData.put("id", id);//id
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
        if (specialityService.update(pageData) == 1)
            map.put("isOk", "1");

        WriteLog("更改id为" + request.getParameter("id") + "的评委专家状态");
        ResultUtils.write(response, toJson(map));
    }

    //添加或更新（获取）
    @RequestMapping(value = "addOrUpdate")
    public void AddOrUpdate(HttpServletRequest request, HttpServletResponse response) {
        logger.info("评委专家添加或更新（获取）");
        String ids = request.getParameter("id") == null ? "" : request.getParameter("id");
        if (!"".equals(ids)) {
            //更新（获取）
            Integer id = Integer.parseInt(ids);
            base_speciality baseSpeciality = specialityService.getModel(id);
            if (baseSpeciality != null) {
                baseSpeciality.getBirthdate();
                PageData pageData = new PageData();
                try {
                    PageData pd = new PageData();
                    pageData.put("speciality", baseSpeciality);
                    String areaName1 ="";
                    String areaName2 ="";
                    String areaCode1 ="";
                    String areaCode2 ="";
                    //根据区域编码获取区域信息
                    String areacode = baseSpeciality.getAreacode();
                    if (areacode == null) {
                        areaCode1 = "410000";
                        areaCode2 = "410000";
                        areaName1 = "省直";
                        areaName2 = "省直";
                    }else {
                        pd.put("areaCode", areacode);
                        List<sys_area> areaList = areaService.getAreaWhere(pd);
                        if (null != areaList) {
                            for (sys_area sysArea : areaList) {
                                if ("2".equals(sysArea.getAreaGrade())) {
                                    areaName1 = sysArea.getAreaName();
                                    areaCode1 = sysArea.getAreaCode();
                                }
                                areaName2 = sysArea.getAreaName();
                                areaCode2 = sysArea.getAreaCode();
                            }
                        }
                        //如果结果中只有河南省一个结果
                        if ("410000".equals(areaCode2)) {
                            areaCode1 = areaCode2;
                            areaName1 = areaName2;
                        }
                    }
                    pageData.put("areaName1", areaName1);
                    pageData.put("areaName2", areaName2);
                    pageData.put("areaCode1", areaCode1);
                    pageData.put("areaCode2", areaCode2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ResultUtils.write(response, toDateJson(pageData));
            } else {
                ResultUtils.write(response, "记录信息不存在！");
            }
        }
    }

    //保存
    @Transactional(propagation= Propagation.REQUIRED)
    @RequestMapping(value = "save")
    public void Save(HttpServletRequest request, HttpServletResponse response) {
        logger.info("保存");
        String id = request.getParameter("id") == null ? "" : request.getParameter("id");
        String jid = request.getParameter("judgingId");
        /*if ("".equals(jid) || null == jid) {
            ResultUtils.writeMessage(response, 0, "请选中评委会,然后添加");
        } else */
            //base_speciality model = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        int count = 0;
        if ("".equals(id)) { //添加
            base_speciality baseSpeciality = new base_speciality();
            //model = new base_speciality();
            /*base_engage baseEngage = new base_engage();
            baseEngage.setAddtime(new Date());
            baseEngage.setAdduserid(getUser().getUserId());
            baseEngage.setJudgingId(jid);
            baseEngage.setAreacode(request.getParameter("areacode"));*/
            baseSpeciality.setAddtime(new Date());
            baseSpeciality.setAdduserid(getUser().getUserId());
            baseSpeciality.setSpecialityName(request.getParameter("specialityName"));
            baseSpeciality.setAdministrativeDutyLevel(request.getParameter("administrativeDutyLevel"));
            baseSpeciality.setProfessialDutyLevel(request.getParameter("professialDutyLevel"));
            baseSpeciality.setProfessialLevel(request.getParameter("professialLevel"));
            baseSpeciality.setProfessial(request.getParameter("professial"));
            baseSpeciality.setJobYear(request.getParameter("jobYear"));
            baseSpeciality.setPerformance(request.getParameter("performance"));
            baseSpeciality.setEducation(request.getParameter("education"));
            baseSpeciality.setDegree(request.getParameter("degree"));
            baseSpeciality.setNowunit(request.getParameter("nowunit"));
            baseSpeciality.setAreacode(request.getParameter("areacode"));
            baseSpeciality.setSex(request.getParameter("sex"));
            baseSpeciality.setIdCardNo(request.getParameter("idCardNo"));
            baseSpeciality.setGraduateSchool(request.getParameter("graduateSchool"));
            String sta = request.getParameter("state") == null ? "0" : request.getParameter("state");
           // baseEngage.setState(Integer.valueOf(sta));
            baseSpeciality.setState(Integer.valueOf(sta));
            baseSpeciality.setTel(request.getParameter("tel"));
            baseSpeciality.setMobilephone(request.getParameter("mobilephone"));
            baseSpeciality.setEmail(request.getParameter("email"));
            baseSpeciality.setPostalCode(request.getParameter("postalCode"));
            baseSpeciality.setPostalAddress(request.getParameter("postalAddress"));
            baseSpeciality.setPresentation(request.getParameter("presentation"));
            baseSpeciality.setRecommendSeries(request.getParameter("recommendSeries"));
            baseSpeciality.setRecommendMajor(request.getParameter("recommendMajor"));
            String isRandom = request.getParameter("isRandom") == null ? "1" : request.getParameter("isRandom");
            baseSpeciality.setIsRandom(Integer.parseInt(isRandom));
            try {
                String birthdate = request.getParameter("birthdate");
                String graduateDate = request.getParameter("graduateDate");
                /*String beginDate = request.getParameter("beginDate");
                String endDate = request.getParameter("endDate");*/
                if (null != birthdate && !"".equals(birthdate)) {
                    baseSpeciality.setBirthdate(sdf.parse(birthdate));
                }
                if (null != graduateDate && !"".equals(graduateDate)) {
                    baseSpeciality.setGraduateDate(sdf.parse(graduateDate));
                }
               /* if (null != beginDate && !"".equals(beginDate)) {
                    baseEngage.setBeginDate(sdf.parse(beginDate));
                }
                if (null != endDate && !"".equals(endDate)) {
                    baseEngage.setEndDate(sdf.parse(endDate));
                }*/
                baseSpeciality.setBack1(request.getParameter("back1"));
                //给新建用户(专家)赋值
                sys_user sysUser = new sys_user();
                sysUser.setUserId(UUID.randomUUID().toString());
                sysUser.setAddress(baseSpeciality.getPostalAddress());
                sysUser.setAddTime(baseSpeciality.getAddtime());
                sysUser.setAddUserId(baseSpeciality.getAdduserid());
                sysUser.setDisplayName(baseSpeciality.getSpecialityName());
                sysUser.setEmail(baseSpeciality.getEmail());
                String idCard = baseSpeciality.getIdCardNo();
                String password = idCard.substring(idCard.length() - 6);
                sysUser.setIdCardNo(idCard);
                sysUser.setLoginName(baseSpeciality.getMobilephone());
                sysUser.setMobilephone(baseSpeciality.getMobilephone());
                sysUser.setPassword(encodeMD5(password));
                sysUser.setState(1);
                sysUser.setUserType(1);//0系统管理员 1 专家
                String userId = sysUser.getUserId();
                baseSpeciality.setPersonalNumber(userId);
                //添加用户
                count = userService.add(sysUser);
                if (count == 0) {
                    throw new RuntimeException();
                }
                // 添加专家
                count = specialityService.add(baseSpeciality);
                /*//添加评委会
                baseEngage.setBaseSpeciality(baseSpeciality);
                baseEngage.setJuryDuty(request.getParameter("juryDuty"));
                baseEngage.setSpecialityId(baseSpeciality.getId().toString());
                count = engageService.add(baseEngage);*/
                if ( count == 1){
                    ResultUtils.writeMessage(response, 1, "添加成功");
                }else{
                    ResultUtils.writeMessage(response, 0, "添加失败");
                    throw new RuntimeException();
                }
            } catch (ParseException e) {
                e.printStackTrace();
                ResultUtils.writeMessage(response, 0, "添加失败");
                throw new RuntimeException();
            }
        } else {//修改
            PageData pd = new PageData();
            PageData pd1 = new PageData();
            try {
                pd.put("id", Integer.parseInt(id));
                pd.put("specialityName", request.getParameter("specialityName"));
                pd.put("state", request.getParameter("state"));
                pd.put("isRandom", request.getParameter("isRandom") == null ? "0" : request.getParameter("isRandom"));
                pd.put("administrativeDutyLevel", request.getParameter("administrativeDutyLevel"));
                pd.put("professialDutyLevel", request.getParameter("professialDutyLevel"));
                pd.put("professialLevel", request.getParameter("professialLevel"));
                pd.put("professial", request.getParameter("professial"));
                pd.put("jobYear", request.getParameter("jobYear"));
                pd.put("performance", request.getParameter("performance"));
                pd.put("education", request.getParameter("education"));
                pd.put("degree", request.getParameter("degree"));
                pd.put("nowunit", request.getParameter("nowunit"));
                pd.put("areacode", request.getParameter("areacode"));
                pd.put("sex", request.getParameter("sex"));
                pd.put("idCardNo", request.getParameter("idCardNo"));
                pd.put("graduateSchool", request.getParameter("graduateSchool"));
                pd.put("mobilephone", request.getParameter("mobilephone"));
                pd.put("email", request.getParameter("email"));
                pd.put("tel", request.getParameter("tel"));
                pd.put("postalAddress", request.getParameter("postalAddress"));
                pd.put("postalCode", request.getParameter("postalCode"));
                pd.put("presentation", request.getParameter("presentation"));
                pd.put("recommendSeries", request.getParameter("recommendSeries"));
                pd.put("recommendMajor", request.getParameter("recommendMajor"));
                String birthdate = request.getParameter("birthdate");
                String graduateDate = request.getParameter("graduateDate");
              /*  String beginDate = request.getParameter("beginDate");
                String endDate = request.getParameter("endDate");*/
                if (null != birthdate && !"".equals(birthdate)) {
                    pd.put("birthdate", sdf.parse(birthdate));
                }
                if (null != graduateDate && !"".equals(graduateDate)) {
                    pd.put("graduateDate", sdf.parse(graduateDate));
                }
                /*if (null != beginDate && !"".equals(beginDate)) {
                    pd2.put("beginDate", sdf.parse(beginDate));
                }
                if (null != endDate && !"".equals(endDate)) {
                    pd2.put("endDate", sdf.parse(endDate));
                }*/
                /*//获取评委会-专家信息
                base_engage baseEngage = engageService.getModel(Integer.parseInt(id));
                base_speciality baseSpeciality  = baseEngage.getBaseSpeciality();
                String specialityId = baseEngage.getSpecialityId();
                pd.put("id",specialityId);
                */
                base_speciality baseSpeciality = specialityService.getModel(Integer.parseInt(id));
                String userId = baseSpeciality.getPersonalNumber();
                //修改用户
                pd1.put("userId", userId);
                pd1.put("displayName", request.getParameter("specialityName"));
                pd1.put("email", request.getParameter("email"));
                pd1.put("loginName", request.getParameter("mobilephone"));
                pd1.put("mobilephone", request.getParameter("mobilephone"));
                pd1.put("sex", request.getParameter("sex"));
                String idCard = request.getParameter("idCardNo");
                String password = idCard.substring(idCard.length() - 6);
                pd1.put("idCardNo", idCard);
                pd1.put("password", encodeMD5(password));
                int flag = 0;
                flag = specialityService.update(pd);
                if (flag == 0) {
                    throw new RuntimeException();
                }
                flag = userService.update(pd1);
                //flag = engageService.update(pd2);
                if (flag == 1){
                    ResultUtils.writeMessage(response, 1, "修改成功");
                }
                else{
                    ResultUtils.writeMessage(response, 0, "修改失败");
                    throw new RuntimeException();
                }
            } catch (Exception e) {
                e.printStackTrace();
                ResultUtils.writeMessage(response, 0, "修改失败");
                throw new RuntimeException();
            }
        }

    }

    //删除
    @Transactional(propagation= Propagation.REQUIRED)
    @RequestMapping(value = "delete")
    public void Delete(HttpServletRequest request, HttpServletResponse response) {
        String ids = request.getParameter("ids") == null ? "" : request.getParameter("ids");
        String[] strs = ids.split(",");
        try {
            for (String id:strs) {
                Integer id1 = Integer.parseInt(id);//专家id

                int flag = 0;
                base_speciality baseSpeciality = specialityService.getModel(id1);
                if (null != baseSpeciality) {
                   String userId = baseSpeciality.getPersonalNumber();
                   //删用户
                    flag = userService.delete(userId);
                    if (flag == 0) {
                        throw new RuntimeException();
                    }
                    //删专家
                    flag = specialityService.delete(id1);
                    if (flag == 0) {
                        throw new RuntimeException();
                    }
                    PageData pageData = new PageData();
                    pageData.put("specialityId", id1);
                    List<base_engage> blist = engageService.getModelWhere(pageData);
                    if (blist != null && blist.size() != 0) {
                        for (base_engage baseEngage:blist) {
                            flag = engageService.delete(baseEngage.getId());
                            if (flag == 0) {
                                throw new RuntimeException();
                            }
                        }
                    }
                }
                if (flag == 1)
                    ResultUtils.writeMessage(response, 1, "删除成功");
                else
                    ResultUtils.writeMessage(response, 0, "删除失败");
            }
        } catch (Exception ex) {
            ResultUtils.writeMessage(response, 0, "删除失败");
            throw new RuntimeException();
        }
    }

    //手机号及身份证号码验证
    @RequestMapping(value = "checkCode")
    public void checkCode(HttpServletRequest request, HttpServletResponse response) {
        logger.info("手机号及身份证号码验证");
        PageData pageData = new PageData();
        pageData.put("mobilephone",request.getParameter("mobilephone"));
        pageData.put("idCardNo",request.getParameter("idCardNo"));
        String id = request.getParameter("id");
        String idCardNo=request.getParameter("idCardNo");

        pageData.put("id",id);
        List<base_speciality> blist = specialityService.getSpecialityWhere(pageData);
        if ((blist != null && blist.size() != 0)) {
            ResultUtils.write(response, false);
        } else {
            ResultUtils.write(response, true);
        }
    }

    //获取评委会列表
    @RequestMapping(value = "getJudgingList")
    public void getJudgingList(HttpServletRequest request, HttpServletResponse response) {
        String judgingName = request.getParameter("judgingName");
        String pwh = request.getParameter("pwh");
        PageData pd = new PageData();
        pd.put("judgingName", judgingName);
        if (pwh==null||"".equals(pwh)) {
            pd.put("manageUnitCode", getUser().getUnitCode());
        }
        List<base_judging> judgingList = specialityService.getJudgingList(pd);
        ResultUtils.write(response, judgingList);
    }

    //获取评委会树列表
    @RequestMapping(value = "getJudgingTree")
    public void getJudgingTree(HttpServletRequest request, HttpServletResponse response) {
        String judgingName = request.getParameter("judgingName");
        PageData pageData = new PageData();
        pageData.put("judgingName", judgingName);
        pageData.put("manageUnitCode", getUser().getUnitCode());
        if(null!=getUser().getJudgingCode()&&!getUser().getJudgingCode().equals("")){
            pageData.put("JUDGING_CODE", getUser().getJudgingCode());
        }
        List<Tree> judgingList = specialityService.getJudgingTree(pageData);
        ResultUtils.write(response, ObjectList2TreeJson("0", judgingList, 1));
    }

    //导出专家信息
    @RequestMapping("/exportExcelSpeciality.do")
    public String exportExcelSpeciality(HttpServletRequest request, HttpServletResponse response, Model model) {
        logger.info("获取评委专家列表数据");
        PageData pd = new PageData();
        pd.put("rows", request.getParameter("rows") == null? 10: Integer.parseInt(request.getParameter("rows")));//每页显示行数
        pd.put("page", request.getParameter("page") == null ? 1 : (Integer.parseInt(request.getParameter("page"))));//每页记录开始位置
        pd.put("specialityName", request.getParameter("specialityName"));
        pd.put("judgingId", request.getParameter("judgingCode"));
        pd.put("administrativeDutyLevel", request.getParameter("administrativeDutyLevel"));
        pd.put("professialLevel", request.getParameter("professialLevel"));
        pd.put("professialDutyLevel", request.getParameter("professialDutyLevel"));
        List<base_speciality> blist = specialityService.getList(pd);
        /*if ("".equals(jid) || null == jid || jid.length() == 0) {
            //下载模板
            String fileName = "评委专家信息导入模板.xlsx".toString(); // 模板的默认保存名
            String path =request.getSession().getServletContext().getRealPath("");//文件路径
            String parentpath = new File(path).getParent();//获取项目的上一级目录
            String parentpath2 = new File(parentpath).getParent();//获取项目的上一级目录
            String realPath = parentpath2 + "/src/main/webapp/static/excelModel/" +fileName;
            // 读到流中
            InputStream inStream = null;
            try {
                inStream = new FileInputStream(realPath);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            // 设置输出的格式
            response.setContentType("text/html;charset=UTF-8");
            response.reset();// 清空输出流
            response.setContentType("application/vnd.ms-excel");
            //response.addHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            try {
                response.setHeader("Content-disposition", "attachment;filename=\""
                        + new String((fileName).getBytes("GBK"),
                        "ISO8859_1") + "\"");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            // 循环取出流中的数据
            byte[] b = new byte[100];
            int len;
            try {
                while ((len = inStream.read(b)) > 0)
                    response.getOutputStream().write(b, 0, len);
                inStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }*/
        String filename = "评委专家信息列表"+String.valueOf(new Date().getTime());
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
        mycreateCell(row,0,"姓名",style);
        mycreateCell(row, 1, "性别", style);
        mycreateCell(row, 2, "出生日期", style);
        mycreateCell(row, 3, "身份证号码", style);
        mycreateCell(row, 4, "手机号码", style);
        mycreateCell(row, 5, "行政职务级别", style);
        mycreateCell(row, 6, "专业技术职务", style);
        mycreateCell(row, 7, "专业技术职务等级", style);
        mycreateCell(row, 8, "从事专业", style);
        mycreateCell(row, 9, "从事专业年限", style);
        mycreateCell(row, 10, "个人业绩表现", style);
        mycreateCell(row, 11, "学历", style);
        mycreateCell(row, 12, "学位", style);
        mycreateCell(row, 13, "所在单位", style);
        mycreateCell(row, 14, "行政区划代码", style);
        mycreateCell(row, 15, "毕业学校", style);
        mycreateCell(row, 16, "毕业时间", style);
        mycreateCell(row, 17, "办公电话", style);
        mycreateCell(row, 18, "通讯地址", style);
        mycreateCell(row, 19, "邮政编码", style);
        mycreateCell(row, 20, "电子邮箱", style);
        mycreateCell(row, 21, "个人简介", style);
        mycreateCell(row, 22, "推荐系列", style);
        mycreateCell(row, 23, "推荐专业", style);
        try {
            if (null != blist && blist.size()!= 0 ) {
                style.setFont(font2);
                for (int i = 0; i < blist.size(); i++) {
                    int j = i + 1;//设置数据起始行
                    row = sheet.createRow(j);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    //设值
                    mycreateCell(row,0,String.valueOf(blist.get(i).getSpecialityName()),style);
                    mycreateCell(row,1,String.valueOf(blist.get(i).getSex()),style);
                    mycreateCell(row,3,String.valueOf(blist.get(i).getIdCardNo()),style);
                    mycreateCell(row,4,String.valueOf(blist.get(i).getMobilephone()),style);
                    mycreateCell(row,5,String.valueOf(blist.get(i).getAdministrativeDutyLevel()),style);
                    mycreateCell(row,6,String.valueOf(blist.get(i).getProfessialDutyLevel()),style);
                    mycreateCell(row,7,String.valueOf(blist.get(i).getProfessialLevel()),style);
                    mycreateCell(row,8,String.valueOf(blist.get(i).getProfessial()),style);
                    mycreateCell(row,9,String.valueOf(blist.get(i).getJobYear()),style);
                    mycreateCell(row,10,String.valueOf(blist.get(i).getPerformance()),style);
                    mycreateCell(row,11,String.valueOf(blist.get(i).getEducation()),style);
                    mycreateCell(row,12,String.valueOf(blist.get(i).getDegree()),style);
                    mycreateCell(row,13,String.valueOf(blist.get(i).getNowunit()),style);
                    mycreateCell(row,14,String.valueOf(blist.get(i).getAreacode()),style);
                    mycreateCell(row,15,String.valueOf(blist.get(i).getGraduateSchool()),style);
                    //日期格式特殊处理
                    if (null == blist.get(i).getBirthdate()) {
                        mycreateCell(row,2,String.valueOf(blist.get(i).getBirthdate()),style);
                    }else {
                        String data1 = sdf.format(blist.get(i).getBirthdate());
                        mycreateCell(row,2,String.valueOf(data1),style);
                    }if (null == blist.get(i).getGraduateDate()) {
                        mycreateCell(row,16,String.valueOf(blist.get(i).getGraduateDate()),style);
                    }else {
                        String data2 = sdf.format(blist.get(i).getGraduateDate());
                        mycreateCell(row,16,String.valueOf(data2),style);
                    }
                    mycreateCell(row,17,String.valueOf(blist.get(i).getTel()),style);
                    mycreateCell(row,18,String.valueOf(blist.get(i).getPostalAddress()),style);
                    mycreateCell(row,19,String.valueOf(blist.get(i).getPostalCode()),style);
                    mycreateCell(row,20,String.valueOf(blist.get(i).getEmail()),style);
                    mycreateCell(row,21,String.valueOf(blist.get(i).getPresentation()),style);
                    mycreateCell(row,22,String.valueOf(blist.get(i).getRecommendSeries()),style);
                    mycreateCell(row,23,String.valueOf(blist.get(i).getRecommendMajor()),style);
                    for (int k = 0;k <= 23;k++) {
                        sheet.autoSizeColumn((short) k); //调整列宽度，自适应
                    }
                }
            }/*else {//示例数据
                style.setFont(font2);
                int i = 0;
                int j = i + 1;//设置数据起始行
                row = sheet.createRow(j);
                //设值
                mycreateCell(row,0,String.valueOf("张三"),style);
                mycreateCell(row,1,String.valueOf("填1或2;1(表示男),2(表示女)"),style);
                mycreateCell(row,2,String.valueOf("yyyy-MM-dd"),style);
                mycreateCell(row,3,String.valueOf("41242119xxxxxx2415"),style);
                mycreateCell(row,4,String.valueOf("151xxxxxxxx"),style);
                mycreateCell(row,5,String.valueOf("xx"),style);
                mycreateCell(row,6,String.valueOf("xx"),style);
                mycreateCell(row,7,String.valueOf("xx"),style);
                mycreateCell(row,8,String.valueOf("xx"),style);
                mycreateCell(row,9,String.valueOf("5"),style);
                mycreateCell(row,10,String.valueOf("xx"),style);
                mycreateCell(row,11,String.valueOf("大学本科"),style);
                mycreateCell(row,12,String.valueOf("学士学位"),style);
                mycreateCell(row,13,String.valueOf("xx公司"),style);
                mycreateCell(row,14,String.valueOf(""),style);
                mycreateCell(row,15,String.valueOf("xx大学"),style);
                mycreateCell(row,16,String.valueOf("yyyy-MM-dd"),style);
                mycreateCell(row,17,String.valueOf(""),style);
                mycreateCell(row,18,String.valueOf("xx省xx市xx区/县xx街道"),style);
                mycreateCell(row,19,String.valueOf("450000"),style);
                mycreateCell(row,20,String.valueOf("xx@163.com"),style);
                mycreateCell(row,21,String.valueOf("我叫xxx,来自xxx..."),style);
                for (int k = 0;k <= 21;k++) {
                    sheet.autoSizeColumn((short) k); //调整列宽度，自适应
                }
            }*/

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

    //导入专家库
   /* @Transactional(propagation= Propagation.REQUIRED)
    @RequestMapping("importExcelSpeciality")
    public String importExcelSpeciality(@RequestParam(name="file")MultipartFile file, HttpServletRequest request,HttpServletResponse response) {
        InputStream is = null;
        int count = 0;
        try {
            String jid = request.getParameter("judgingId");
            if ("".equals(jid) || null == jid) {
                ResultUtils.writeMessage(response, 0, "请选中评委会,然后导入");
            }else {
                is = file.getInputStream();
                String fileName = file.getOriginalFilename();
                //String filePath = "D:\\work\\src\\main\\webapp\\upload\\html\\file";
                String path1 = request.getSession().getServletContext().getRealPath("");
                String filePath = path1 + "static/excelModel/";//文件路径
                String tarFileName = FileUtils.uploadFile(is, fileName, filePath);
                ReadExcel readExcel = new ReadExcel();
                List<List<String>> lList = readExcel.getExcelInfo(filePath +"\\"+ tarFileName);
                List<base_engage> blist = new ArrayList<>();
                for (List<String> list : lList) {
                    if (list == null || list.size() > 18 ) {
                        ResultUtils.writeMessage(response, 0, "表格模板不规范,请检查后再导入!");
                        return null;
                    }
                    int flag = 0;
                    base_engage baseEngage = new base_engage();
                    base_speciality baseSpeciality = new base_speciality();
                    baseSpeciality.setSpecialityName(list.get(0));
                    baseSpeciality.setSex(list.get(1));
                    baseSpeciality.setIdCardNo(list.get(3));
                    baseSpeciality.setMobilephone(list.get(4));
                    baseSpeciality.setProfessialDutyLevel(list.get(5));
                    baseSpeciality.setProfessialLevel(list.get(6));
                    baseSpeciality.setProfessial(list.get(7));
                    baseSpeciality.setJobYear(list.get(8));
                    baseSpeciality.setPerformance(list.get(9));
                    baseSpeciality.setNowunit(list.get(10));
                    baseSpeciality.setGraduateSchool(list.get(11));
                    SimpleDateFormat sDateFormat=new SimpleDateFormat("yyyy-MM-dd");
                    if (list.get(2) != null && !"".equals(list.get(2))) {
                        baseSpeciality.setBirthdate(sDateFormat.parse(list.get(2)));
                    }if (list.get(12) != null && !"".equals(list.get(12))) {
                        baseSpeciality.setGraduateDate(sDateFormat.parse(list.get(12)));
                    }
                    baseSpeciality.setTel(list.get(13));
                    baseSpeciality.setPostalAddress(list.get(14));
                    baseSpeciality.setPostalCode(list.get(15));
                    baseSpeciality.setEmail(list.get(16));
                    baseSpeciality.setPresentation(list.get(17));
                    baseSpeciality.setAddtime(new Date());
                    baseSpeciality.setAdduserid(getUser().getUserId());
                    //给新建用户(专家)赋值
                    sys_user sysUser = new sys_user();
                    sysUser.setUserId(UUID.randomUUID().toString());
                    sysUser.setAddress(baseSpeciality.getPostalAddress());
                    sysUser.setAddTime(new Date());
                    sysUser.setAddUserId(getUser().getUserId());
                    sysUser.setDisplayName(baseSpeciality.getSpecialityName());
                    sysUser.setEmail(baseSpeciality.getEmail());
                    String idCard = baseSpeciality.getIdCardNo();
                    String password = idCard.substring(idCard.length() - 6,idCard.length());
                    sysUser.setIdCardNo(idCard);
                    sysUser.setLoginName(baseSpeciality.getMobilephone());
                    sysUser.setMobilephone(baseSpeciality.getMobilephone());
                    sysUser.setPassword(encodeMD5(password));
                    sysUser.setState(1);
                    sysUser.setUserType(1);//0系统管理员 1 专家
                    String userId = sysUser.getUserId();
                    baseSpeciality.setPersonalNumber(userId);
                    //添加用户
                    flag = userService.add(sysUser);
                    if (flag == 0) {
                        throw new RuntimeException();
                    } else {
                        flag = 0;
                    }
                    // 添加专家
                    flag = specialityService.add(baseSpeciality);
                    if (flag == 0) {
                        throw new RuntimeException();
                    } else {
                        flag = 0;
                    }
                    //添加评委会
                    baseEngage.setBaseSpeciality(baseSpeciality);
                    baseEngage.setAddtime(new Date());
                    baseEngage.setAdduserid(getUser().getUserId());
                    baseEngage.setJudgingId(jid);
                    baseEngage.setState(1);
                    //baseEngage.setJuryDuty();//评委会职务
                    baseEngage.setSpecialityId(baseSpeciality.getId().toString());
                    flag = engageService.add(baseEngage);
                    if (flag == 0) {
                        throw new RuntimeException();
                    }
                    blist.add(baseEngage);
                }
                if (blist != null && blist.size() > 0) {
                    ResultUtils.writeMessage(response, 1, "添加成功");
                }else {
                    ResultUtils.writeMessage(response, 0, "添加失败");
                    throw new RuntimeException();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            ResultUtils.writeMessage(response, 0, "添加失败");
            throw new RuntimeException();
        }
        return  null;
    }*/

}
