package com.hz.demo.services;

import com.hz.demo.core.PageData;
import com.hz.demo.core.Tree;
import com.hz.demo.dao.DaoSupportImpl;
import com.hz.demo.model.Titles;
import com.hz.demo.model.base_unit;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("TitlesService")
public class TitlesService {

    protected final Log logger = LogFactory.getLog(getClass());

    @Resource(name = "daoSupportImpl")
    private DaoSupportImpl daoSupport;

    //新增
    public int add(PageData pageData) {
        logger.info("增加Titles");
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.insert("confing/mappers.Titles.insertSelective", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            iFlag = 0;
        }
        return iFlag;
    }
    public int InsertTitles(List<Titles> pageData) {
        logger.info("增加Titles");
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.insert("confing/mappers.Titles.InsertTitles", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            iFlag = 0;
        }
        return iFlag;
    }
    //修改
    public int update(PageData pageData) {
        logger.info("修改Titles");
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.update("confing/mappers.Titles.updateByPrimaryKeySelective", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            iFlag = 0;
        }
        return iFlag;
    }

    //删除
    public int delete(String qualificationName) {
        logger.info("删除BaseUnit");
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.delete("confing/mappers.Titles.deleteTitles", qualificationName);
        } catch (Exception e) {
            e.printStackTrace();
            iFlag = 0;
        }
        return iFlag;
    }
    //分页查询
    public List<Titles> getList(PageData pageData) {
        logger.info("分页查询BaseUnit");
        List<Titles> baseunitList = null;
        try {
            baseunitList = (List<Titles>) daoSupport.findForList("confing/mappers.Titles.getAllBaseUnit", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            baseunitList = null;
        }
        return baseunitList;
    }

    public List<Titles> findTitles(PageData pageData) {
        logger.info("查询TitlesList");
        List<Titles> baseunitList = null;
        try {
            baseunitList = (List<Titles>) daoSupport.findForList("confing/mappers.Titles.findTitles", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            baseunitList = null;
        }
        return baseunitList;
    }

    //分页查询对应记录数量
    public Integer getListCount(PageData pageData) {
        logger.info("分页查询BaseUnit数量");
        Integer count = 0;
        Integer ListCount = 0;
        try {
            ListCount = (Integer) daoSupport.findForObject("confing/mappers.Titles.selectCount", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            ListCount = 0;
        }
        return ListCount;
    }


}
