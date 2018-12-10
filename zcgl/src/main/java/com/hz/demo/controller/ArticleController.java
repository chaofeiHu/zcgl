package com.hz.demo.controller;

import com.hz.demo.core.Html2Text;
import com.hz.demo.core.PageData;
import com.hz.demo.core.ResultUtils;
import com.hz.demo.core.TableReturn;
import com.hz.demo.model.Article;
import com.hz.demo.services.ArticleService;
import com.hz.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;
import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.hz.demo.core.ResultUtils.toJson;

/***
 * 注意事项
 */
@Controller
@RequestMapping("/Article")
public class ArticleController extends BaseController {
    @Autowired
    UserService userService;
    @Autowired
    ArticleService articleService;

    @RequestMapping(value = "ArticlePage")
    public String DeptPage(HttpServletRequest request, HttpServletResponse response, Model model) {
        request.setAttribute("MenuBtns",userService.GetMenuBtns(request.getParameter("menuid"),getUser().getUserId()));
        return "sys/article";
    }

    //获取注意事项列表信息
    @RequestMapping(value = "getArticleList")
    public void getDictTree(HttpServletRequest request, HttpServletResponse response) {
        PageData pd = new PageData();
        pd.put("rows", request.getParameter("rows") == null? 10: Integer.parseInt(request.getParameter("rows")));//每页显示行数
        pd.put("page", request.getParameter("page") == null ? 0 : (Integer.parseInt(request.getParameter("page"))));//每页记录开始位置
        String title = request.getParameter("title1");
        String type = request.getParameter("type1");
        pd.put("title",title);
        pd.put("type",type);
        TableReturn tablereturn = new TableReturn();
        List<PageData> item = articleService.getList(pd);
        Integer listCount = articleService.getListCount(pd);
        tablereturn.setRows(item);
        tablereturn.setTotal(listCount);
        ResultUtils.write(response, toJson(tablereturn));
    }

    //删除
    @RequestMapping(value = "Delete")
    public void Delete(HttpServletRequest request, HttpServletResponse response) {
        String ids = request.getParameter("ids") == null ? "" : request.getParameter("ids");
        try {
            if (articleService.delete(ids) == 1)
                ResultUtils.writeMessage(response, 1, "删除成功");
            else
                ResultUtils.writeMessage(response, 0, "删除失败");
        } catch (Exception ex) {
            ResultUtils.writeMessage(response, 0, ex.getMessage());
        }
    }



    //添加或更新（获取）
    @RequestMapping(value = "AddOrUpdate")
    public void AddOrUpdate(HttpServletRequest request, HttpServletResponse response) {
        logger.info("添加或更新（获取）");
        Map<String,String> map=new HashMap<String, String>();
        String tid=request.getParameter("tid") == null ? "" : request.getParameter("tid");
        String ID=request.getParameter("ID") == null ? "" : request.getParameter("ID");
        String title = request.getParameter("TITLE") == null ? "" : request.getParameter("TITLE");
        String type = request.getParameter("TYPE") == null ? "" : request.getParameter("TYPE");
        String text = request.getParameter("achievementtext") == null ? "" : request.getParameter("achievementtext");
        if (tid.equals("")&&ID.equals("")) { //新增
            StringBuilder stringHtml = new StringBuilder();
            PrintStream printStream=null;
            try{
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                String filename = sdf.format(new Date().getTime());
                String urlname= "/upload/html/"+filename+".html";
                PageData pd=new PageData();
                pd.put("type",type);
                if(type.equals("5")){
                    if(articleService.getListCount(pd)>=1){
                        map.put("message","新增失败,填表须知不能为多条!");
                        ResultUtils.write(response, toJson(map));
                        return ;
                    }
                }
                pd.put("title",title);
                pd.put("text",urlname);
                pd.put("createTime",new Date());
                pd.put("userId",getUser().getUserId());
                //打开文件
                Html2Text html = new Html2Text();
                html.saveHtml(request.getSession().getServletContext().getRealPath(urlname),title,text);
                articleService.add(pd);
                map.put("isOk","1");
                map.put("message","添加成功!");
            }catch (Exception ex){
                ex.printStackTrace();
            }
            ResultUtils.write(response, toJson(map));
            //获取更新信息
        } else if(ID.equals("")&&!tid.equals("")) {
            PageData pageData= articleService.getModel(tid);
            String tm="";
            if (pageData != null) {
                try {
                    //System.out.print());
                    Html2Text parser = new Html2Text();
                    tm= parser.getBody(parser.readfile(request.getSession().getServletContext().getRealPath(pageData.getString("TEXT"))));
                }catch (Exception e) {
                    e.printStackTrace();
                }
                pageData.put("txt",tm);
                ResultUtils.write(response, toJson(pageData));
            } else {
                ResultUtils.write(response, "记录信息不存在！");
            }
        }else{ //更新
            try {
                Html2Text html = new Html2Text();
                html.saveHtml(request.getSession().getServletContext().getRealPath(request.getParameter("TEXT")),title,text);
                PageData pd=new PageData();
                Article article=new Article();
                article.setId(new BigDecimal(ID));
                article.setTitle(title);
                article.setType(new BigDecimal(type));
                articleService.update(article);
                map.put("isOk","1");
                map.put("message","修改成功!");
            }catch (Exception e){
                e.printStackTrace();
                ResultUtils.write(response, "修改失败");
            }
            ResultUtils.write(response,toJson(map) );
        }
    }



}
