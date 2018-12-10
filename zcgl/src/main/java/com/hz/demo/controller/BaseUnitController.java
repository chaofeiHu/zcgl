package com.hz.demo.controller;

import com.hz.demo.core.*;
import com.hz.demo.model.*;
import com.hz.demo.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.hz.demo.core.EncodeUtil.encodeMD5;
import static com.hz.demo.core.ResultUtils.toJson;

@Controller
@RequestMapping("/BaseUnit")
public class BaseUnitController extends BaseController {

    @Autowired
    BaseUnitService baseUnitService;
    @Autowired
    UserService userService;
    @Autowired
    BaseJudgingService baseJudgingService;

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

    //跳转到单位管理页面
    @RequestMapping(value = "BaseUnitPage")
    public String UserPage(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("MenuBtns", userService.GetMenuBtns(request.getParameter("menuid"), getUser().getUserId()));
        return "sys/baseUnit";
    }

    //获取界面列表
    @RequestMapping(value = "getList")
    public void getList(HttpServletRequest request, HttpServletResponse response) {
        logger.info("显示组织机构列表数据");
        PageData pd = new PageData();
        pd.put("rows", request.getParameter("rows") == null ? 10 : Integer.parseInt(request.getParameter("rows")));//每页显示行数
        pd.put("page", request.getParameter("page") == null ? 0 : (Integer.parseInt(request.getParameter("page"))));//每页记录开始位置
        pd.put("bid", request.getParameter("bid") == null ? "" : request.getParameter("bid"));//部门ID
        pd.put("unitName", request.getParameter("displayName") == null ? "" : request.getParameter("displayName"));//用户显示名称
        //parentId系统管理-->机构职能设置--父级单位编号
        String code = request.getParameter("parentId") == null ? "" : request.getParameter("parentId");
        if (code == null || code.equals("")) {
            pd.put("unitCode", getUser().getUnitCode());
        } else {
            pd.put("unitCode", code);
        }
        TableReturn tablereturn = new TableReturn();
        List<base_unit> list = baseUnitService.getList(pd);
        Integer listCount = baseUnitService.getListCount(pd);
        List<base_unit> blist = new ArrayList<>();
        if (list != null) {
            for (base_unit baseUnit : list) {
                String unitAttach = getDictWhere(baseUnit.getUnitAttach(), "UNIT_ATTACH");
                String unitCategory = getDictWhere(baseUnit.getUnitCategory(), "UNIT_CATEGORY");
                String industryInvolved = getDictWhere(baseUnit.getIndustryInvolved(), "INDUSTRY_INVOLVED");
                String unitNature = getDictWhere(baseUnit.getUnitNature(), "UNIT_NATURE");
                String economicType = getDictWhere(baseUnit.getEconomicType(), "ECONOMIC_TYPE");
                //String areaNumber = getDictWhere(baseUnit.getAreaNumber());
                baseUnit.setUnitAttach(unitAttach);
                baseUnit.setUnitCategory(unitCategory);
                baseUnit.setIndustryInvolved(industryInvolved);
                baseUnit.setUnitNature(unitNature);
                baseUnit.setEconomicType(economicType);
                //baseUnit.setAreaNumber(areaNumber);
                blist.add(baseUnit);
            }
        }
        tablereturn.setRows(blist);
        tablereturn.setTotal(listCount);

        ResultUtils.write(response, toJson(tablereturn));
    }

    //更改机构状态
    @RequestMapping(value = "UpdateState")
    public void UpdateState(HttpServletRequest request, HttpServletResponse response) {
        logger.info("更改机构状态");
        PageData pageData = new PageData();
        String bid = request.getParameter("bid") == null ? "" : request.getParameter("bid");
        pageData.put("bid", bid);//id
        String state = request.getParameter("state") == null ? "0" : request.getParameter("state");
        Integer sta = Integer.parseInt(state);
        if (sta == 0) {
            sta = 1;
        } else {
            sta = 0;
        }
        String type = request.getParameter("type") == null ? "0" : request.getParameter("type");
        if (type.equals("1")) {
            pageData.put("state", sta);
        } else if (type.equals("2")) {
            pageData.put("isBuildjudging", sta);
        } else if (type.equals("3")) {
            pageData.put("isManageunit", sta);
        } else {
            pageData.put("isFirmlyorganization", sta);
        }
        Map<String, String> map = new HashMap<>();
        map.put("isOk", "0");
        if (baseUnitService.update(pageData) == 1)
            map.put("isOk", "1");

        WriteLog("更改id为" + request.getParameter("bid") + "的机构状态");
        ResultUtils.write(response, toJson(map));
    }

    //添加或更新（获取）
    @RequestMapping(value = "AddOrUpdate")
    public void AddOrUpdate(HttpServletRequest request, HttpServletResponse response) {
        logger.info("组织机构添加或更新（获取）");
        Integer bid = Integer.parseInt(request.getParameter("bid") == null ? "0" : request.getParameter("bid"));
        PageData pageData = new PageData();
        base_unit baseUnit = new base_unit();
        if (bid != 0 && !bid.equals("")) {
            //更新（获取）
            baseUnit = baseUnitService.getModel(bid);
        } else {
            //获取当前用户信息
            String unitCode = getUser().getUnitCode();
            PageData pageData1 = new PageData();
            pageData1.put("unitCode", unitCode);
            base_unit baseUnit1 = baseUnitService.getModelWhere(pageData1);
            baseUnit.setAreaNumber(baseUnit1.getAreaNumber());
        }

        PageData pd = new PageData();
        pageData.put("baseUnit", baseUnit);
        //根据区域编码获取区域信息
        String areaNumber = baseUnit.getAreaNumber();
        pd.put("areaCode", areaNumber);
        List<sys_area> areaList = areaService.getAreaWhere(pd);
        String areaName1 = null;
        String areaName2 = null;
        String areaCode1 = null;
        String areaCode2 = null;
        for (sys_area sysArea : areaList) {
            if ("2".equals(sysArea.getAreaGrade())) {
                areaName1 = sysArea.getAreaName();
                areaCode1 = sysArea.getAreaCode();
            }
            areaName2 = sysArea.getAreaName();
            areaCode2 = sysArea.getAreaCode();
        }
        if (areaCode1 == null) areaCode1 = "410000";
        if (areaCode2 == null) areaCode2 = "410000";
        if (areaName1 == null) areaName1 = "省直";
        if (areaName2 == null) areaName2 = "省直";
        pageData.put("areaName1", areaName1);
        pageData.put("areaName2", areaName2);
        pageData.put("areaCode1", areaCode1);
        pageData.put("areaCode2", areaCode2);
        if (baseUnit != null) {
            ResultUtils.write(response, pageData);
        } else {
            ResultUtils.write(response, "记录信息不存在！");
        }
    }

    //保存
    @Transactional(propagation= Propagation.REQUIRED)
    @RequestMapping(value = "Save")
    public void Save(HttpServletRequest request, HttpServletResponse response) {
        logger.info("保存");
        String bid = request.getParameter("bid") == null ? "" : request.getParameter("bid");
        PageData pageData = new PageData();
        pageData.put("unitName", request.getParameter("unitName"));
        pageData.put("unitAttach", request.getParameter("unitAttach"));
        pageData.put("unitCategory", request.getParameter("unitCategory"));
        pageData.put("industryInvolved", request.getParameter("industryInvolved"));
        pageData.put("unitNature", request.getParameter("unitNature"));
        pageData.put("economicType", request.getParameter("economicType"));
        pageData.put("areaNumber", request.getParameter("areaNumber"));
        if ("".equals(bid)) { //添加
            String userUnitCode = getUser().getUnitCode();
            pageData.put("addUserId", getUser().getUserId());
            pageData.put("addTime", new Date());
            pageData.put("state", 1);
            pageData.put("parentUnitCode", userUnitCode);
            pageData.put("unitCode", getUnitCode(userUnitCode, (String) pageData.get("unitCategory"), (String) pageData.get("areaNumber")));
            if (baseUnitService.add(pageData) == 1) {
                //新增组织机构之后,自动创建其机构的默认管理员账号
                sys_user model = new sys_user();
                model.setUserId(UUID.randomUUID().toString());
                model.setDisplayName("系统管理员");
                model.setLoginName((String) pageData.get("unitCode"));
                model.setPassword(encodeMD5("000000"));
                model.setAddTime(new Date());
                model.setAddUserId(getUser().getUserId());
                model.setUnitCode((String) pageData.get("unitCode"));
                model.setState(1);
                String code = (String) pageData.get("unitCode");
                if (userService.add(model) == 1) {
                    //管理员创建成功后默认分配对应的管理员角色
                    sys_userrole ur = new sys_userrole();
                    ur.setUserRoleId(UUID.randomUUID().toString());
                    ur.setUserId(model.getUserId());
                    ur.setRoleId(getUnitByRoleId(userUnitCode, (String) pageData.get("unitCategory"), (String) pageData.get("areaNumber")));
                    if (roleService.addUserRole(ur) == 1){
                        ResultUtils.writeMessage(response, 1, "添加成功");
                    }
                    else {
                        ResultUtils.writeMessage(response, 1, "添加管理员成功，分配角色失败");
                        throw new RuntimeException();
                    }
                } else {
                    ResultUtils.writeMessage(response, 1, "机构添加成功,管理员添加失败");
                    throw new RuntimeException();
                }
            } else {
                ResultUtils.writeMessage(response, 0, "添加失败");
            }
            WriteLog("添加显示名为" + request.getParameter("unitName") + "的机构信息");
        } else {//修改
            Integer id = Integer.parseInt(bid);
            pageData.put("bid", id);
            if (baseUnitService.update(pageData) == 1){
                ResultUtils.writeMessage(response, 1, "修改成功");
            }
            else{
                ResultUtils.writeMessage(response, 0, "修改失败");
                throw new RuntimeException();
            }
            WriteLog("修改id为" + request.getParameter("bid") + "的客户信息");
        }

    }

    //批量删除
    @RequestMapping(value = "Delete")
    public void Delete(HttpServletRequest request, HttpServletResponse response) {
        String ids = request.getParameter("ids") == null ? "0" : request.getParameter("ids");
        String[] strs = ids.split(",");
        int count = 0;
        int flag = 0;
        try {
            for (String bid : strs) {
                PageData pageData = new PageData();
                base_unit baseUnit = baseUnitService.getModel(Integer.parseInt(bid));
                String unitCode = baseUnit.getUnitCode();
                PageData pageData1 = new PageData();
                pageData1.put("unitCode", unitCode);
                pageData1.put("state", 0);
                flag += userService.updateByUnitCode(pageData1);
                pageData.put("bid", bid);
                pageData.put("isDelete", 1);
                count += baseUnitService.update(pageData);
            }
            if (count >= 1 && flag >= 1)
                ResultUtils.writeMessage(response, 1, "注销成功");
            else
                ResultUtils.writeMessage(response, 0, "注销失败");

            WriteLog("注销id为" + ids + "的用户信息");
        } catch (Exception ex) {
            ResultUtils.writeMessage(response, 0, ex.getMessage());
        }
    }

    //获取字典下拉列表框
    @RequestMapping(value = "getDictList")
    public void getDictList(HttpServletRequest request, HttpServletResponse response, String groupName) {
        logger.info("获取字典下拉列表框");
        String aa=request.getParameter("reviewSeries");
        try {
            List<sys_dict> dictList = getDictList(groupName);
            Integer size = dictList.size();
            if ("UNIT_CATEGORY".equals(groupName)) {
                String unitCode = getUser().getUnitCode();
                switch (unitCode.length()) {
                    case 6:
                        dictList.remove(size - 1);
                        dictList.remove(size - 2);
                        break;
                    case 9:
                        dictList.remove(0);
                        dictList.remove(0);
                        break;
                    case 13:
                        dictList.remove(0);
                        dictList.remove(0);
                        dictList.remove(0);
                        break;
                    case 15:
                        dictList.remove(0);
                        dictList.remove(0);
                        dictList.remove(0);
                        dictList.remove(0);
                        break;
                    case 17:
                        dictList.remove(0);
                        dictList.remove(0);
                        dictList.remove(0);
                        dictList.remove(0);
                        dictList.remove(0);
                        break;
                }
                ResultUtils.write(response, dictList);
            } else {
                ResultUtils.write(response, dictList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //获取所有评委会下拉列表框
    @RequestMapping(value = "selectListWhere")
    public void selectListWhere(HttpServletRequest request, HttpServletResponse response, String groupName) {
        logger.info("获取评委会下拉列表框");
        try {
            List<base_judging> dictList = baseJudgingService.selectListWhere(null);
            ResultUtils.write(response, dictList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //获取本级评委会下拉列表框
    @RequestMapping(value = "selectListWhereByUnit")
    public void selectListWhereByUnit(HttpServletRequest request, HttpServletResponse response) {
        logger.info("获取本级评委会下拉列表框");
        try {
            PageData pd=new PageData();
            pd.put("BUILD_UNIT",getUser().getUnitCode());
            List<base_judging> dictList = baseJudgingService.selectListWhereByUnit(pd);
            ResultUtils.write(response, dictList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @RequestMapping(value="getDictListWhere.do")
    public void getDictListWhere(HttpServletRequest request, HttpServletResponse response) {
        logger.info("获取本级评委会下拉列表框");
        String groupName =request.getParameter("groupName");
        String deptCode =request.getParameter("deptCode");
        List<sys_dict> dictList = null;
        try {
            PageData pd=new PageData();
           pd.put("groupName",groupName);
            pd.put("dictCode",deptCode);
            dictList = dictService.getDictList(pd);
            ResultUtils.write(response, dictList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //根据专家信息获取评委会下学科组树形
    @RequestMapping(value = "getGroupTreeList")
    public void getGroupTreeList(HttpServletRequest request, HttpServletResponse response) {
        PageData pageData = new PageData();
        pageData.put("JUDGING_CODE", getUser().getJudgingCode());
        pageData.put("JUDGING_NAME",getUser().getJudgingName());
        if(request.getParameter("type").equals("1")){
            pageData.put("SPECIALITY_ID",getUser().getSpecialityId());
        }
        List<Tree> judgingList = baseUnitService.getGroupTreeList(pageData);
        ResultUtils.write(response, ObjectList2TreeJson("0", judgingList, 1));
    }
    //获取审查状态下拉列表框
    @RequestMapping(value = "selectScxt")
    public void selectScxt(HttpServletRequest request, HttpServletResponse response, String groupName) {
        logger.info("获取审查状态下拉列表框");
        try {

            PageData pageData = new PageData();
            //PageData pageData2 = new PageData();
           // pageData2.put("id", -1);
           // pageData2.put("text", "请选择..");
            pageData.put("id", 0);
            pageData.put("text", "已审查");
            PageData pageData1 = new PageData();
            pageData1.put("id", 3);
            pageData1.put("text", "未审查");

            List<PageData> sczt = new ArrayList<PageData>();
            //sczt.add(pageData2);
            sczt.add(pageData);
            sczt.add(pageData1);

            ResultUtils.write(response, sczt);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //获取区域下拉列表框
    @RequestMapping(value = "getAreaList")
    public void getAreaList(HttpServletRequest request, HttpServletResponse response, String areaGrade, String areaCode) {
        logger.info("获取地区下拉列表框");
        try {
            List<sys_area> areaList = getAreaList(areaGrade, areaCode);
            ResultUtils.write(response, areaList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //根据条件获取机构集合  下拉列表树
    @RequestMapping(value = "getListWhere")
    public void getListWhere(HttpServletRequest request, HttpServletResponse response) {
        logger.info("根据条件获取机构集合");
        try {
            PageData pageData = new PageData();
            String unitCode = getUser().getUnitCode();
            pageData.put("unitCode", unitCode);
            List<Tree> treeList = baseUnitService.getListWhere(pageData);
            ResultUtils.write(response, ObjectList2TreeJson(unitCode, treeList, 1));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //根据单位id获取单位管理员账户
    @RequestMapping(value = "getUserWhere")
    public void getUserWhere(HttpServletRequest request, HttpServletResponse response) {
        logger.info("根据单位id获取单位管理员账户");
        PageData pd = new PageData();
        pd.put("unitCode", request.getParameter("unitCode") == null ? "" : request.getParameter("unitCode"));
        TableReturn tablereturn = new TableReturn();
        List<sys_user> userList = userService.getUserListWhere(pd);
        if (userList != null) {
            Integer listCount = userList.size();
            tablereturn.setRows(userList);
            tablereturn.setTotal(listCount);
        }
        ResultUtils.write(response, toJson(tablereturn));
    }

    //跳转到组织结构表树管理页面
    @RequestMapping(value = "BaseInstitutionalPage")
    public String BaseInstitutionalPage(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("MenuBtns", userService.GetMenuBtns(request.getParameter("menuid"), getUser().getUserId()));
        return "sys/baseInstitutional";
    }

    //获取组织结构表树
    @RequestMapping(value = "getBaseUnitTree")
    public void getBaseUnitTree(HttpServletRequest request, HttpServletResponse response) {
        String ParentID = request.getParameter("parentId") == null ? "410000" : request.getParameter("parentId");
        PageData pd = new PageData();
        pd.put("parentId", ParentID);
        String state = request.getParameter("state");
        if (state != null)
            pd.put("state", state);
        List<Tree> menuTreeList = baseUnitService.getListWhere(pd);
        ResultUtils.write(response, ObjectList2TreeJson(ParentID, menuTreeList, 1));
    }

    //本单位信息维护
    @RequestMapping(value = "UnitInformationMaintenancePage")
    public String UnitInformationMaintenancePage(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("MenuBtns", userService.GetMenuBtns(request.getParameter("menuid"), getUser().getUserId()));
        return "sys/unitMaintenance";
    }

    //添加或更新（获取）
    @RequestMapping(value = "Update")
    public void Update(HttpServletRequest request, HttpServletResponse response) {
        logger.info("组织机构更新");
        sys_user user = getUser();
        base_unit baseUnit = baseUnitService.getUnit(null, user.getUnitCode());
        //更新（获取）
        //base_unit baseUnit = baseUnitService.getModel(bid);
        PageData pageData = new PageData();
        PageData pd = new PageData();
        pageData.put("baseUnit", baseUnit);
        //根据区域编码获取区域信息
        String areaNumber = baseUnit.getAreaNumber();
        pd.put("areaCode", areaNumber);
        List<sys_area> areaList = areaService.getAreaWhere(pd);
        String areaName1 = null;
        String areaName2 = null;
        String areaCode1 = null;
        String areaCode2 = null;
        for (sys_area sysArea : areaList) {
            if ("2".equals(sysArea.getAreaGrade())) {
                areaName1 = sysArea.getAreaName();
                areaCode1 = sysArea.getAreaCode();
            }
            areaName2 = sysArea.getAreaName();
            areaCode2 = sysArea.getAreaCode();
        }
        if (areaCode1 == null) areaCode1 = "410000";
        if (areaCode2 == null) areaCode2 = "410000";
        if (areaName1 == null) areaName1 = "河南省";
        if (areaName2 == null) areaName2 = "河南省";
        pageData.put("areaName1", areaName1);
        pageData.put("areaName2", areaName2);
        pageData.put("areaCode1", areaCode1);
        pageData.put("areaCode2", areaCode2);
        if (baseUnit != null) {
            ResultUtils.write(response, pageData);
        } else {
            ResultUtils.write(response, "记录信息不存在！");
        }
    }

    //保存本单位信息
    @RequestMapping(value = "saveUnit")
    public void saveUnit(HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.info("保存");
        String bid = request.getParameter("bid") == null ? "" : request.getParameter("bid");
        PageData pageData = new PageData();
        String path = request.getParameter("creditCodePath");
        pageData.put("back3", request.getParameter("creditCodePath"));
        pageData.put("organizationCode", request.getParameter("organizationCode"));
        pageData.put("creditCode", request.getParameter("creditCode"));
        pageData.put("linkMan", request.getParameter("linkMan"));
        pageData.put("phone", request.getParameter("phone"));
        pageData.put("address", request.getParameter("address"));
        pageData.put("postalCode", request.getParameter("postalCode"));
        pageData.put("fax", request.getParameter("fax"));
        pageData.put("email", request.getParameter("email"));
        pageData.put("industryInvolved", request.getParameter("industryInvolved"));
        pageData.put("unitNature", request.getParameter("unitNature"));
        pageData.put("economicType", request.getParameter("economicType"));
       /* InputStream is = image.getInputStream();
        String fileName = image.getOriginalFilename();
        String path = request.getSession().getServletContext().getRealPath("");
        String filePath = path + "static/imageFile/";//文件路径
        String tarFileName = FileUtils.uploadFile(is, fileName, filePath);*/
        //String newfileName = new Date().getTime()+"_"+fileName;
        /*File file1=new File(filePath+fileName);
        image.transferTo(file1);
        System.out.println(newfileName);*/
        //修改
        Integer id = Integer.parseInt(bid);
        pageData.put("bid", id);
        if (baseUnitService.update(pageData) == 1)
            ResultUtils.writeMessage(response, 1, "提交成功");
        else
            ResultUtils.writeMessage(response, 0, "提交失败");
        WriteLog("修改id为" + request.getParameter("bid") + "的客户信息");
    }

    //获取组织结构表树
    @RequestMapping(value = "ybTree")
    public void ybTree(HttpServletRequest request, HttpServletResponse response) {
        String ParentID = getUser().getUnitCode();
        PageData pd = new PageData();
        pd.put("unitCode", ParentID);
        String state = request.getParameter("pid") == null ? "" : request.getParameter("pid");
        pd.put("pid", state);
        List<Tree> menuTreeList = baseUnitService.ybTree(pd);
        StringBuilder jsonBuilder = new StringBuilder();
        if (!state.equals("")) {
            ResultUtils.write(response, ybTreeJson2(ParentID, menuTreeList, 1));
        } else {
            ResultUtils.write(response, ybTreeJson(ParentID, menuTreeList, 1));
        }

    }

    //判断机构名字是否已存在
    @RequestMapping(value = "checkCode")
    public void checkCode(HttpServletRequest request, HttpServletResponse response) {
        logger.info("判断机构名字是否已存在");
        PageData pageData = new PageData();
        String unitName = request.getParameter("unitName");
        pageData.put("unitName",request.getParameter("unitName"));
        base_unit  model = baseUnitService.getModelWhere(pageData);
        if (model != null) {
            ResultUtils.write(response, false);
        } else {
            ResultUtils.write(response, true);
        }
    }

    //获取学科组下拉框
    @RequestMapping(value = "selectSubject")
    public void selectSubject(HttpServletRequest request, HttpServletResponse response) {
        logger.info("获取本级评委会下拉列表框");
        try {
            PageData pd=new PageData();
            pd.put("JUDGING_CODE",request.getParameter("JUDGING_CODE"));
            List<PageData> dictList = baseJudgingService.selectSubject(pd);
            ResultUtils.write(response, dictList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   @RequestMapping(value = "uploadImage")
   public void uploadImage(@RequestParam(name="file")MultipartFile file, HttpServletRequest request,HttpServletResponse response) {
       InputStream is = null;
       String tarFileName = null;
       Map map = new HashMap();
       map.put("isOk", 0);
       try {
           is = file.getInputStream();
           String fileName = file.getOriginalFilename();
           String path1 = request.getSession().getServletContext().getRealPath("/");
           String filePath = path1 + "/upload/unitImages/";//文件路径
           //自动建立文件夹
           File folder = new File(filePath);
           if (!folder.exists()) {
               folder.mkdirs();
           }
           tarFileName = FileUtils.uploadFile(is, fileName, filePath);
           String pathName = "/upload/unitImages/" + tarFileName;
           map.put("isOk", 1);
           map.put("realPath", pathName);
       } catch (Exception e) {
           e.printStackTrace();
           map.put("isOk", 0);
       }
       ResultUtils.write(response, map);
   }

}