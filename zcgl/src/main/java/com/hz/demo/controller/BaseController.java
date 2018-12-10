package com.hz.demo.controller;

import com.hz.demo.core.PageData;
import com.hz.demo.core.Tree;
import com.hz.demo.model.*;
import com.hz.demo.services.*;
import net.sourceforge.pinyin4j.PinyinHelper;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Created by wangyong on 2016/11/7.
 */
public class BaseController {
    protected final Log logger = LogFactory.getLog(getClass());
    @Autowired
    UserService userService;
    @Autowired
    TokenService tokenService;
    @Autowired
    LogService logService;
    @Autowired
    DictService dictService;
    @Autowired
    AreaService areaService;
    @Autowired
    BaseUnitService baseUnitService;
    @Autowired
    RoleService roleService;
    @Autowired
    BaseJudingProcessService baseJudingProcessService;
    @Autowired
    SpecialityService specialityService;
    @Autowired
    EngageService engageService;

    /**
     * 获取Session
     *
     * @return
     */
    public HttpSession getSession() {
        return getRequest().getSession();
    }
    /**
     * 得到request对象
     */
    public HttpServletRequest getRequest() {
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        return request;
    }
    /**
     * 得到PageData
     */
    public PageData getPageData(){
        return new PageData(this.getRequest());
    }
    /**
     * 获取用户
     */
    public sys_user getUser() {
        HttpServletRequest request = getRequest();
        sys_user user = (sys_user) request.getSession().getAttribute("User");
        if (user == null) {
            String toKenId = request.getParameter("toKenId");
            if (toKenId == null) {
                return null;
            } else {
                try {
                    String userid = tokenService.getTokenByFid(toKenId).getUserId();
                    user = userService.getModel(userid);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return user;
    }


    /**
     * 根据用户获取用户菜单权限
     * parentID :顶级父节点id
     * menuList:菜单集合
     * RootFlag：是否显示顶级节点
     */
    public static String getMenuJson(String parentID, List<sys_menu> menuList, int RootFlag) {
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("{menus:");
        if (RootFlag == 1) {
            jsonBuilder.append("[{");
            List<sys_menu> newMenuList = new ArrayList<>();
            for (sys_menu menu : menuList) {
                if (menu.getParentId().equals(parentID))
                    newMenuList.add(menu);
            }

            if (newMenuList.size() > 0) {
                for (sys_menu newMenu : newMenuList) {
                    if (newMenu.getParentId().equals(parentID)) {
                        jsonBuilder.append("\"menuid\":\"" + newMenu.getMenuId() + "\",");
                        jsonBuilder.append("\"menuname\":\"" + newMenu.getMenuName() + "\",");
                        jsonBuilder.append("\"url\":\"" + newMenu.getMenuUrl() + "\",");
                        if (newMenu.getHasChild() == 0) {
                            jsonBuilder.append("\"icon\":\"icon-sys\",");
                            jsonBuilder.append("\"menus\":");
                        } else {
                            jsonBuilder.append("\"icon\":\"icon-nav\",");
                        }
                    }
                }
            } else {
                jsonBuilder.append("\"menuid\":0,\"menuname\":\"根\",\"menus\":");
            }
        } else {
            GetMenuChildNodes(parentID, menuList, jsonBuilder);
            if (RootFlag == 1) {
                jsonBuilder.append("}]");
            }
            jsonBuilder.append("}");
        }
        return jsonBuilder.toString();
    }

    public static void GetMenuChildNodes(String parentID, List<sys_menu> menuList, StringBuilder jsonBuilder) {
        List<sys_menu> newMenuList = new ArrayList<>();
        for (sys_menu menu : menuList) {
            if (menu.getParentId() != null && menu.getParentId().equals(parentID))
                newMenuList.add(menu);
        }
        jsonBuilder.append("[");
        for (int i = 0; i < newMenuList.size(); i++) {
            sys_menu newMenu = newMenuList.get(i);
            jsonBuilder.append("{");
            jsonBuilder.append("\"menuid\":\"" + newMenu.getMenuId() + "\",");
            jsonBuilder.append("\"menuname\":\"" + newMenu.getMenuName() + "\",");
            jsonBuilder.append("\"url\":\"" + newMenu.getMenuUrl() + "\",");
            if (newMenu.getHasChild() == 0) {
                jsonBuilder.append("\"icon\":\"icon-sys\",");
                jsonBuilder.append("\"menus\":");
                GetMenuChildNodes(newMenu.getMenuId(), menuList, jsonBuilder);
            } else {
                jsonBuilder.append("\"icon\":\"icon-nav\"");
            }
            jsonBuilder.append("}");
            if (i != newMenuList.size() - 1) {
                jsonBuilder.append(",");
            }
        }
        jsonBuilder.append("]");
    }


    /**
     * 树对象集合转成json树对象
     *
     * @param parentID 顶级父节点id
     * @param treeList 树对象集合
     * @param RootFlag 是否显示顶级节点
     * @return josn字符串
     */
    public static String ObjectList2TreeJson(String parentID, List<Tree> treeList, int RootFlag) {
        StringBuilder jsonBuilder = new StringBuilder();
        if (RootFlag == 1) {
            jsonBuilder.append("[{");
            List<Tree> newtTreeList = new ArrayList<>();
            List<Tree> childrentTreeList = new ArrayList<>();
            for (Tree tree : treeList) {
                if (tree.getId() != null && tree.getId().equals(parentID)) {
                    newtTreeList.add(tree);
                    jsonBuilder.append("\"id\":\"" + tree.getId());
                    jsonBuilder.append("\",\"checked\":" + tree.getChecked());
                    jsonBuilder.append(",\"text\":\"" + tree.getText());
                    jsonBuilder.append("\",\"attributes\":\"" + tree.getAttributes() + "\"");

                    for (Tree childrentTree : treeList) {
                        if (childrentTree.getPid() != null)
                            if (childrentTree.getPid().equals(tree.getId())) {
                                childrentTreeList.add(childrentTree);
                            }
                    }
                }
            }
            if (childrentTreeList.size() > 0)
                jsonBuilder.append(",\"children\":");
            GetTreeChildNodes(parentID, treeList, jsonBuilder);
            jsonBuilder.append("}]");
        } else {
            GetTreeChildNodes(parentID, treeList, jsonBuilder);
        }

        return jsonBuilder.toString();
    }

    public static void GetTreeChildNodes(String parentID, List<Tree> treeList, StringBuilder jsonBuilder) {

        List<Tree> newTreeList = new ArrayList<>();
        for (Tree tree : treeList) {//得到父键等于parentID的集合
            if (tree.getPid() != null && tree.getPid().equals(parentID))
                newTreeList.add(tree);
        }
        if(newTreeList.size()==0){  //如果没有父级
            jsonBuilder.append(",\"children\":[");
        }else{
            jsonBuilder.append("[");
        }
        for (int i = 0; i < newTreeList.size(); i++) {//循环当前父节点
            Tree tree = newTreeList.get(i);
            jsonBuilder.append("{");
            jsonBuilder.append("\"id\":\"" + tree.getId());
            jsonBuilder.append("\",\"text\":\"" + tree.getText() + "\"");
            jsonBuilder.append(",\"checked\":" + tree.getChecked());
            jsonBuilder.append(",\"attributes\":\"" + tree.getAttributes() + "\"");

            List<Tree> childrentTreeList = new ArrayList<>();
            for (Tree childrentTree : treeList) {//根据当前父节点的id作为子节点的父键，得到下属子节点
                if (StringUtils.equalsIgnoreCase(childrentTree.getPid(), tree.getId()))
                    childrentTreeList.add(childrentTree);
            }

            if (childrentTreeList.size() > 0) {
                jsonBuilder.append(",\"children\":");
                GetTreeChildNodes(tree.getId(), treeList, jsonBuilder);
            }
            jsonBuilder.append("}");
            if (i != newTreeList.size() - 1) {
                jsonBuilder.append(",");
            }
        }
        jsonBuilder.append("]");
    }

    /**
     * 根据分组名获取其分组下所有字典数据
     */
    public List<sys_dict> getDictList(String group_name) {
        PageData pageData = new PageData();
        pageData.put("groupName", group_name);
        List<sys_dict> dictList = null;
        try {
            dictList = dictService.getDictList(pageData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dictList;
    }

    public sys_dict getDict(String groupName, String dictCode) {
        PageData pageData = new PageData();
        pageData.put("groupName", groupName);
        pageData.put("dictCode", dictCode);
        sys_dict dict = dictService.getDictWhere(pageData);
        return dict;
    }

    /**
     * 根据字典编码获取字典名
     */
    public String getDictWhere(String dictCode, String groupName) {
        PageData pageData = new PageData();
        sys_dict sysDict = null;
        pageData.put("dictCode", dictCode);
        pageData.put("groupName", groupName);
        String dictName = null;
        try {
            sysDict = dictService.getDictWhere(pageData);
            dictName = sysDict.getDictName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dictName;
    }


    /**
     * 汉字转换拼音
     */
    public String getChineseToPinYin(String str) {
        // ==== 全拼音 ====
        StringBuffer full_pinyin = new StringBuffer();
        try {
            if (StringUtils.isNotEmpty(str) && StringUtils.isNotBlank(str)) {// 拆分字符串中的每个【文字】
                char[] charArray = str.toCharArray();
                for (char c : charArray) {
                    String[] pinyins = PinyinHelper.toHanyuPinyinStringArray(c);
                    if (pinyins != null && pinyins.length > 0) {
                        /*
                         *
                         * 由于当【文字】为多音字时返回的数组元素为多个，如"间",既可读为jian1,又可以读为jian4,
                         * 而我们在匹配搜索时，只会用到jian作匹配，那么这里直接舍去音调。
                         */
                        String pinyinStr = pinyins[0];
                        pinyinStr = pinyinStr.replaceAll("\\d*", "");
                        full_pinyin.append(pinyinStr);
                    }
                }
            }
            return full_pinyin.toString();
        } catch (Exception e) {
            return "";
        }
    }


    /**
     * 字符串转二进制
     */
    public byte[] hex2byte(String str) { // 字符串转二进制
        if (str == null)
            return null;
        str = str.trim();
        int len = str.length();
        if (len == 0 || len % 2 == 1)
            return null;
        byte[] b = new byte[len / 2];
        try {
            for (int i = 0; i < str.length(); i += 2) {
                b[i / 2] = (byte) Integer
                        .decode("0X" + str.substring(i, i + 2)).intValue();
            }
            return b;
        } catch (Exception e) {
            return null;
        }
    }


    /// <summary>
    /// 输入日志
    /// </summary>
    /// <param name="Message"></param>
    public void WriteLog(String Message) {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) ra).getRequest();

        sys_log modelLog = new sys_log();
       // modelLog.setLogId(UUID.randomUUID().toString());
        modelLog.setAddTime(new Date());
        modelLog.setAddUserId(getUser().getUserId());
        modelLog.setIp(getIpAdrress(request));
        modelLog.setMessage(String.format("%s" + Message, getUser().getDisplayName()));
        modelLog.setPage(request.getRequestURI());
        logService.add(modelLog);
    }

    /**
     * 获取Ip地址
     *
     * @param request
     * @return
     */
    private static String getIpAdrress(HttpServletRequest request) {
        String Xip = request.getHeader("X-Real-IP");
        String XFor = request.getHeader("X-Forwarded-For");
        if (StringUtils.isNotEmpty(XFor) && !"unKnown".equalsIgnoreCase(XFor)) {
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = XFor.indexOf(",");
            if (index != -1) {
                return XFor.substring(0, index);
            } else {
                return XFor;
            }
        }
        XFor = Xip;
        if (StringUtils.isNotEmpty(XFor) && !"unKnown".equalsIgnoreCase(XFor)) {
            return XFor;
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getHeader("HTTP_CLIENT_IP");
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getRemoteAddr();
        }
        return XFor;
    }

    /**
     * 根据区域等级获取该等级下所有区域信息
     */
    public List<sys_area> getAreaList(String areaGrade, String areaCode) {
        sys_area sysArea = new sys_area();
        sysArea.setAreaGrade(areaGrade);
        sysArea.setAreaCode(areaCode);
        List<sys_area> areaList = null;
        try {
            areaList = areaService.getAreaList(sysArea);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return areaList;
    }

    /**
     * 根据当前用户unitcode获取要新建的单位的unitcode
     */
    public String getUnitCode(String unitCode, String unitCategory, String areaNumber) {
        PageData pd = new PageData();
        pd.put("unitCode", unitCode);
        pd.put("unitCategory", unitCategory);
        pd.put("areaNumber", areaNumber);
        String newUnitCode = null;
        try {
            switch (unitCategory) {
                case "1"://如果要创建的是职称办 地区编码即为单位编码
                    newUnitCode = areaNumber;
                    break;
                case "2"://主管部门  编码长度是9位
                    pd.put("unitCodelength", 9);
                    newUnitCode = baseUnitService.getUnitCode(pd);
                    if (newUnitCode == null) {//如果第一次添加
                        newUnitCode = unitCode + "001";
                    } else {
                        Long unitCode1 = Long.parseLong(newUnitCode) + 1;
                        newUnitCode = Long.toString(unitCode1);
                    }
                    break;
                case "4"://单位  编码长度是13位
                    pd.put("unitCodelength", 13);
                    newUnitCode = baseUnitService.getUnitCode(pd);
                    //如果是职称办添加
                    if (unitCode.length() == 6) {
                        if (newUnitCode == null) {//如果第一次添加
                            newUnitCode = unitCode + "0000001";
                        } else {
                            Long unitCode1 = Long.parseLong(newUnitCode) + 1;
                            newUnitCode = Long.toString(unitCode1);
                        }
                    } else {
                        if (newUnitCode == null) {//如果第一次添加
                            newUnitCode = unitCode + "0001";
                        } else {
                            Long unitCode1 = Long.parseLong(newUnitCode) + 1;
                            newUnitCode = Long.toString(unitCode1);
                        }
                    }
                    break;
                case "5"://下属单位 编码长度是15位
                    pd.put("unitCodelength", 15);
                    newUnitCode = baseUnitService.getUnitCode(pd);
                    if (newUnitCode == null) {//如果第一次添加
                        newUnitCode = unitCode + "01";
                    } else {
                        Long unitCode1 = Long.parseLong(newUnitCode) + 1;
                        newUnitCode = Long.toString(unitCode1);
                    }
                    break;
                case "6"://基层单位 编码长度是17位
                    pd.put("unitCodelength", 17);
                    newUnitCode = baseUnitService.getUnitCode(pd);
                    if (newUnitCode == null) {//如果第一次添加
                        newUnitCode = unitCode + "01";
                    } else {
                        Long unitCode1 = Long.parseLong(newUnitCode) + 1;
                        newUnitCode = Long.toString(unitCode1);
                    }
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return newUnitCode;
    }

    //根据单位级别初始化管理员角色
    public String getUnitByRoleId(String unitCode, String unitCategory, String areaNumber) {
        String roleid = "";
        PageData pd = new PageData();
        switch (unitCategory) {
            case "1"://职称管理部门
                pd.put("areaCode", areaNumber);
                String areaGrade = areaService.getListWhere(pd).get(0).getAreaGrade();
                PageData pd1 = new PageData();
                if (areaGrade.equals("1")) {
                    pd1.put("fsort", "1001");//省职称办管理员角色
                    roleid = roleService.getListWhere(pd1).get(0).getRoleId();
                } else if (areaGrade.equals("2")) {
                    pd1.put("fsort", "2001");//市职称办管理员角色
                    roleid = roleService.getListWhere(pd1).get(0).getRoleId();
                } else if (areaGrade.equals("3")) {
                    pd1.put("fsort", "3001");//县区职称办管理员角色
                    roleid = roleService.getListWhere(pd1).get(0).getRoleId();
                }
                break;
            case "2"://主管部门
                pd.put("fsort", "4001");//主管部门管理员角色
                roleid = roleService.getListWhere(pd).get(0).getRoleId();
                break;
            case "4"://单位
                pd.put("fsort", "5001");//单位管理员角色
                roleid = roleService.getListWhere(pd).get(0).getRoleId();
                break;
            case "5"://下属单位
                pd.put("fsort", "6001");//下属单位管理员角色
                roleid = roleService.getListWhere(pd).get(0).getRoleId();
                break;
            case "6"://基层单位
                pd.put("fsort", "7001");//基层单位管理员角色
                roleid = roleService.getListWhere(pd).get(0).getRoleId();
                break;
        }
        return roleid;
    }


    //当前登录用户根据所属单位获取对应角色组
    public String getRoleGroupCode() {
        String roleGroupCode = "";
        String unitCode = getUser().getUnitCode();
        if (unitCode.length() == 6) {//职称办
            PageData pd = new PageData();
            pd.put("areaCode", unitCode);
            String areaGrade = areaService.getListWhere(pd).get(0).getAreaGrade();
            switch (areaGrade) {
                case "1":
                    roleGroupCode = "1";//省职称办
                    break;
                case "2":
                    roleGroupCode = "2";//市职称办
                    break;
                case "3":
                    roleGroupCode = "3";//县区职称办
                    break;
            }
        } else if (unitCode.length() == 9) {//主管单位
            if (unitCode.substring(2, 4).equals("00"))
                roleGroupCode = "4";//省级主管单位
            else {
                if (unitCode.substring(4, 6).equals("00") || unitCode.substring(2, 4).equals("90"))
                    roleGroupCode = "5";//市级主管单位包含济源市
                else
                    roleGroupCode = "6";//县区级主管单位
            }

        } else if (unitCode.length() == 13) {
            roleGroupCode = "7";//单位
        } else if (unitCode.length() == 15) {
            roleGroupCode = "8";//下属单位
        } else if (unitCode.length() == 17) {
            roleGroupCode = "9";//基层单位
        }
        return roleGroupCode;
    }

    /**
     * 延迟加载树形
     * 树对象集合转成json树对象
     *
     * @param parentID 顶级父节点id
     * @param treeList 树对象集合
     * @param RootFlag 是否显示顶级节点
     * @return josn字符串
     */
    public  String ybTreeJson(String parentID, List<Tree> treeList, int RootFlag) {
        StringBuilder jsonBuilder = new StringBuilder();
        if (RootFlag == 1) {
            jsonBuilder.append("[{");
            List<Tree> newtTreeList = new ArrayList<>();
            List<Tree> childrentTreeList = new ArrayList<>();
            for (Tree tree : treeList) {
                if (tree.getId() != null && tree.getId().equals(parentID)) {
                    newtTreeList.add(tree);
                    jsonBuilder.append("\"id\":\"" + tree.getId());
                    jsonBuilder.append("\",\"checked\":" + tree.getChecked());
                    jsonBuilder.append(",\"text\":\"" + tree.getText());
                    jsonBuilder.append("\",\"attributes\":\"" + tree.getAttributes() + "\"");

                    for (Tree childrentTree : treeList) {
                        if (childrentTree.getPid() != null)
                            if (childrentTree.getPid().equals(tree.getId())) {
                                childrentTreeList.add(childrentTree);
                            }
                    }
                }
            }
            if (childrentTreeList.size() > 0)
                jsonBuilder.append(",\"children\":");
            ybTreeChildNodes(parentID, treeList, jsonBuilder);
            jsonBuilder.append("}]");
        } else {
            ybTreeChildNodes(parentID, treeList, jsonBuilder);
        }
        return jsonBuilder.toString();
    }

    /**
     * * 延迟加载树形
     * @param parentID
     * @param treeList
     * @param jsonBuilder
     */
    public  void ybTreeChildNodes(String parentID, List<Tree> treeList, StringBuilder jsonBuilder) {
        List<Tree> newTreeList = new ArrayList<>();
        for (Tree tree : treeList) {//得到父键等于parentID的集合
            if (tree.getPid() != null && tree.getPid().equals(parentID))
                newTreeList.add(tree);
        }
        if(newTreeList.size()==0){  //如果没有父级
            jsonBuilder.append(",\"children\":[");
        }else{
            jsonBuilder.append("[");
        }
        for (int i = 0; i < newTreeList.size(); i++) {//循环当前父节点
            Tree tree = newTreeList.get(i);
            jsonBuilder.append("{");
            jsonBuilder.append("\"id\":\"" + tree.getId());
            jsonBuilder.append("\",\"text\":\"" + tree.getText() + "\"");
            jsonBuilder.append(",\"checked\":false");
            Integer num = baseUnitService.ybTreeSize(tree.id);
            if(num>0){
                jsonBuilder.append(",\"state\":\"closed\"");
            }
            jsonBuilder.append(",\"attributes\":\"" + tree.getAttributes() + "\"");
            jsonBuilder.append("}");
            if (i != newTreeList.size() - 1) {
                jsonBuilder.append(",");
            }
        }
        jsonBuilder.append("]");
    }

    /**
     * * 延迟加载树形
     * @param parentID
     * @param treeList
     * @param RootFlag
     * @return
     */
    public  String ybTreeJson2(String parentID, List<Tree> treeList, int RootFlag) {
        StringBuilder jsonBuilder = new StringBuilder();
        if (RootFlag == 1) {
            jsonBuilder.append("[");
            List<Tree> childrentTreeList = new ArrayList<>();
            for (int i = 0; i < treeList.size(); i++) {//循环当前父节点
                        Tree tree = treeList.get(i);
                        jsonBuilder.append("{");
                        jsonBuilder.append("\"id\":\"" + tree.getId());
                        jsonBuilder.append("\",\"text\":\"" + tree.getText() + "\"");
                        jsonBuilder.append(",\"checked\":false");
                        Integer num = baseUnitService.ybTreeSize(tree.id);
                        if(num>0){
                            jsonBuilder.append(",\"state\":\"closed\"");
                        }
                        jsonBuilder.append(",\"attributes\":\"" + tree.getAttributes() + "\"");
                        jsonBuilder.append(",\"children\":[]");
                        jsonBuilder.append("}");
                        if (i != treeList.size() - 1) {
                            jsonBuilder.append(",");
                }
            }
            jsonBuilder.append("]");
        }
        return jsonBuilder.toString();
    }

}
