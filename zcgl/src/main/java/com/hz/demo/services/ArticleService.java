package com.hz.demo.services;
import com.hz.demo.core.PageData;
import com.hz.demo.dao.DaoSupportImpl;
import com.hz.demo.model.Article;
import com.hz.demo.model.sys_area;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
@Service("ArticleService")
public class ArticleService {

    @Resource(name = "daoSupportImpl")
    private DaoSupportImpl daoSupport;

    //添加地区信息
    public int add(PageData model) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.insert("confing/mappers.Article.insertSelective", model);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }

    // 根据ID更新地区信息
    public int update(Article article) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.update("confing/mappers.Article.updateByPrimaryKeySelective", article);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }

    //根据ID删除地区信息
    public int delete(String id) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.delete("confing/mappers.Article.deleteByPrimaryKey",id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }

    //查询所有的注意事项列表信息
    public List<PageData> getList(PageData pageData) {
        List<PageData> areaList = null;
        try {
            areaList = (List<PageData>) daoSupport.findForList("confing/mappers.Article.selectList", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            areaList = null;
        }
        return areaList;
    }

    //查询对应的记录总数
    public Integer getListCount(PageData pageData) {
        Integer ListCount = 0;
        try {
            ListCount = (Integer) daoSupport.findForObject("confing/mappers.Article.selectCount", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            ListCount = 0;
        }
        return ListCount;
    }


    //根据ID查询用户返回实体信息
    public PageData getModel(String id) {
        PageData sysArea = null;
        try {
            sysArea = (PageData) daoSupport.findForObject("confing/mappers.Article.selectByPrimaryKey", id);
        } catch (Exception e) {
            e.printStackTrace();
            sysArea = null;
        }
        return sysArea;
    }



}
