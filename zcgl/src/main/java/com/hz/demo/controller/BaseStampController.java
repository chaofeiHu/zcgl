package com.hz.demo.controller;

import com.hz.demo.core.*;
import com.hz.demo.model.BaseStamp;
import com.hz.demo.services.BaseStampService;
import com.hz.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.hz.demo.core.ResultUtils.toJson;

@Controller
@RequestMapping("/BaseStamp")
public class BaseStampController extends BaseController {
    @Autowired
    UserService userService;
    @Autowired
    BaseStampService baseStampService;

    @RequestMapping(value = "/BaseStampPage.do")
    public String BaseStampPage(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("MenuBtns", userService.GetMenuBtns(request.getParameter("menuid"), getUser().getUserId()));
        return "sys/baseStamp";
    }

    //获取数据
    @RequestMapping(value = "/getData.do")
    public void getData(HttpServletRequest request, HttpServletResponse response) {
        logger.info("（获取数据）");
        //更新（获取）
        PageData pd = new PageData();
        pd.put("unitCode", getUser().getUnitCode());
        Map<String, Object> model = baseStampService.selectBaseStampModel(pd);
        if (model != null) {
            ResultUtils.write(response,  toJson(model));
        } else {
            model=new HashMap<>();
            model.put("UNIT_NAME_NAME",baseUnitService.getUnitWhere(pd).getUnitName());
            model.put("NAME","");
            model.put("ID","");
            ResultUtils.write(response,  toJson(model));
        }

    }

    //保存
    @RequestMapping(value = "/Save.do")
    public void Save(HttpServletRequest request, HttpServletResponse response, @RequestParam("file") MultipartFile file) {
        logger.info("保存");
        String id = request.getParameter("id") == null ? "" : request.getParameter("id");
        String name = request.getParameter("name") == null ? "" : request.getParameter("name");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        String pre = "upload/zcgl/stamp";//前一层路径
        String path = request.getSession().getServletContext().getRealPath("/") + pre;
        if (id.equals("")) { //添加
            PageData pd = new PageData();
            pd.put("id", UUID.randomUUID().toString());
            pd.put("userId", getUser().getUserId());
            pd.put("name", name);
            pd.put("addtime", sdf1.format(new Date()));
            pd.put("unitCode", getUser().getUnitCode());
            //上传文件
            if (file != null && !file.isEmpty()) {
                pd.put("fileurl", UploadFileUtil.uploadFile(file, pre, path));
            }
            if (baseStampService.add(pd) == 1) {
                ResultUtils.writeMessage(response, 1, "添加成功");
            }else {
                UploadFileUtil.deleteFile(request.getSession().getServletContext().getRealPath(pre+path));
                ResultUtils.writeMessage(response, 0, "添加失败");
            }
        } else {//修改
            PageData pd = new PageData();
            pd.put("id", id);
            pd.put("userId", getUser().getUserId());
            pd.put("name", name);
            pd.put("addtime", sdf1.format(new Date()));
//            pd.put("unitCode", getUser().getUnitCode());

            //上传文件
            if (file != null && !file.isEmpty()) {
                pd.put("fileurl", UploadFileUtil.uploadFile(file, pre, path));
                if (baseStampService.getModel(id).getFileurl() != null) {
                    UploadFileUtil.deleteFile(request.getSession().getServletContext().getRealPath(baseStampService.getModel(id).getFileurl()));
                }
            }
            if (baseStampService.update(pd) == 1)
                ResultUtils.writeMessage(response, 1, "修改成功");
            else
                ResultUtils.writeMessage(response, 0, "修改失败");
        }

    }

    //删除图片
    @RequestMapping(value = "/DeleteImg.do")
    public void DeleteImg(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id") == null ? "" : request.getParameter("id");
        try {
            PageData pdmo = new PageData();
            pdmo.put("unitCode", getUser().getUnitCode());
            Map<String, Object> model = baseStampService.selectBaseStampModel(pdmo);

            PageData pd = new PageData();
            pd.put("id", id);
            pd.put("userId", getUser().getUserId());
            pd.put("fileurl","");
            if (baseStampService.update(pd) == 1) {
                UploadFileUtil.deleteFile(request.getSession().getServletContext().getRealPath(model.get("FILEURL")+""));
                ResultUtils.writeMessage(response, 1, "删除成功");
            }else {
                ResultUtils.writeMessage(response, 0, "删除失败");
            }
        } catch (Exception ex) {
            ResultUtils.writeMessage(response, 0, ex.getMessage());
        }
    }


}
