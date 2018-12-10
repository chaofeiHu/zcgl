package com.hz.demo.controller;

import com.hz.demo.core.PageData;
import com.hz.demo.core.ResultUtils;
import com.hz.demo.core.TableReturn;
import com.hz.demo.core.Tree;
import com.hz.demo.model.*;
import com.hz.demo.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

import static com.hz.demo.core.ResultUtils.toJson;

@Controller
@RequestMapping("/Dict")
public class DictController extends BaseController {

    @Autowired
    UserService userService;
    @Autowired
    DictService dictService;
    @Autowired
    TitlesService titlesService;
    @RequestMapping(value = "DictPage")
    public String DictPage(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("MenuBtns",userService.GetMenuBtns(request.getParameter("menuid"),getUser().getUserId()));
        return "sys/dict";
    }

    //获取字典树
    @RequestMapping(value = "getDictTree")
    public void getDictTree(HttpServletRequest request, HttpServletResponse response) {
        String ParentID = request.getParameter("parentId") == null ? "00000000-0000-0000-0000-000000000000" : request.getParameter("parentId");
        PageData pd = new PageData();
        pd.put("parentId", ParentID);
        String state = request.getParameter("state") == null ? null : request.getParameter("state");
        if (state != null)
            pd.put("state", state);
        List<Tree> treeList = dictService.getTreeList(pd);
        ResultUtils.write(response, ObjectList2TreeJson(ParentID, treeList, 1));
    }

    //获取字典界面列表
    @RequestMapping(value = "getDictList")
    public void getDictList(HttpServletRequest request, HttpServletResponse response) {
        logger.info("显示字典列表数据");
        PageData pd = new PageData();
        pd.put("rows", request.getParameter("rows") == null? 10: Integer.parseInt(request.getParameter("rows")));//每页显示行数
        pd.put("page", request.getParameter("page") == null ? 0 : (Integer.parseInt(request.getParameter("page"))));//每页记录开始位置
        pd.put("parentId", request.getParameter("parentId") == null ? "" : request.getParameter("parentId"));//父ID
        pd.put("dictName", request.getParameter("dictName") == null ? "" : request.getParameter("dictName"));//名称

        TableReturn tablereturn = new TableReturn();
        List<sys_dict> list = dictService.getList(pd);
        Integer listCount = dictService.getListCount(pd);
        tablereturn.setRows(list);
        tablereturn.setTotal(listCount);

        ResultUtils.write(response, toJson(tablereturn));
    }

    //更改字典状态
    @RequestMapping(value = "updateState")
    public void updateState(HttpServletRequest request, HttpServletResponse response) {
        logger.info("更改字典状态");
        PageData pageData  = new PageData();
        String dictId = request.getParameter("dictId") == null ? "" : request.getParameter("dictId");
        pageData.put("dictId", dictId);//id
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
        if (dictService.update(pageData) == 1)
            map.put("isOk", "1");
        ResultUtils.write(response, toJson(map));
    }

    //添加或更新（获取）
    @RequestMapping(value = "AddOrUpdate")
    public void AddOrUpdate(HttpServletRequest request, HttpServletResponse response) {
        logger.info("字典添加或更新（获取）");
        String dictId = request.getParameter("dictId") == null ? "" : request.getParameter("dictId");
        if (dictId.equals("")) {
            //添加
        } else {
            //更新（获取）
            sys_dict model = dictService.getModel(dictId);
            if (model != null) {
                ResultUtils.write(response, toJson(model));
            } else {
                ResultUtils.write(response, "记录信息不存在！");
            }
        }

    }

    //保存
    @RequestMapping(value = "Save")
    public void Save(HttpServletRequest request, HttpServletResponse response) {
        logger.info("保存");
        String dictId = request.getParameter("dictId") == null ? "" : request.getParameter("dictId");
        sys_dict model = null;
        if (dictId.equals("")) { //添加
            model = new sys_dict();
            model.setDictId(UUID.randomUUID().toString());
            model.setDictCode(request.getParameter("dictCode"));
            model.setDictName(request.getParameter("dictName"));
            model.setGroupName(request.getParameter("groupName"));
            model.setBackup2(request.getParameter("backup2"));
            model.setAddTime(new Date());
            model.setAddUserId(getUser().getUserId());
            model.setHasChild(1);
            model.setParentId(request.getParameter("parentId"));
            model.setState(Integer.valueOf(request.getParameter("state") == null ? "0" : request.getParameter("state")));
            model.setFsort(Integer.valueOf(request.getParameter("fsort")));
            model.setBackup1(request.getParameter("backup1"));
            PageData pd = new PageData();
            pd.put("dictId", request.getParameter("parentId"));
            pd.put("hasChild", 0);
            if (dictService.update(pd) == 1) {
                if (dictService.add(model) == 1){
                    ResultUtils.writeMessage(response, 1, "添加成功");
                    if(model.getGroupName().equals("POSITIONAL_TITLES")){  //如果分组状态为POSITIONAL_TITLES，则同步添加职称表
                        pd.put("qualificationName",model.getDictName());
                        pd.put("qualificationCode",model.getDictCode());
                        titlesService.add(pd);
                    }
                }else{
                    ResultUtils.writeMessage(response, 0, "添加失败");
                }
            } else
                ResultUtils.writeMessage(response, 0, "添加失败");
        } else {//修改
            PageData pd = new PageData();
            pd.put("dictId", dictId);
            pd.put("groupName", request.getParameter("groupName"));
            pd.put("dictCode", request.getParameter("dictCode"));
            pd.put("dictName", request.getParameter("dictName"));
            pd.put("state", request.getParameter("state") == null ? "0" : request.getParameter("state"));
            pd.put("fsort", request.getParameter("fsort"));
            pd.put("backup1", request.getParameter("backup1"));
            pd.put("backup2", request.getParameter("backup2"));
            if (dictService.update(pd) == 1)
                ResultUtils.writeMessage(response, 1, "修改成功");
            else
                ResultUtils.writeMessage(response, 0, "修改失败");
        }

    }

    //删除
    @RequestMapping(value = "Delete")
    public void Delete(HttpServletRequest request, HttpServletResponse response) {
        String ids = request.getParameter("ids") == null ? "" : request.getParameter("ids");
        try {
            if (dictService.delete(ids) == 1){
                String names=request.getParameter("names") == null ? "" : request.getParameter("names");
                titlesService.delete(names);
                ResultUtils.writeMessage(response, 1, "删除成功");
            }else
                ResultUtils.writeMessage(response, 0, "删除失败");
        } catch (Exception ex) {
            ResultUtils.writeMessage(response, 0, ex.getMessage());
        }
    }


}
