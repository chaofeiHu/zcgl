package com.hz.demo.services;


import com.hz.demo.core.PageData;
import com.hz.demo.dao.DaoSupportImpl;
import com.hz.demo.model.rec_fileManager;
import com.hz.demo.model.rec_result;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@Service("RecFileManagerService")
public class RecFileManagerService {

    @Resource(name = "daoSupportImpl")
    private DaoSupportImpl daoSupport;

    //添加信息
    public int add(rec_fileManager model) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.insert("confing/mappers.rec_fileManager.insertSelective", model);

        } catch (Exception e) {
            e.printStackTrace();
            iFlag = 0;
        }
        return iFlag;
    }

    // 根据ID更新生成文件信息
    public int update(PageData pageData) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.update("confing/mappers.rec_fileManager.updateByPrimaryKeySelective", pageData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }

    //根据ID删除生成文件信息
    public int delete(BigDecimal id) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.delete("confing/mappers.rec_fileManager.deleteByPrimaryKey", id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }

    //根据ID查询生成文件返回实体信息
    public rec_fileManager getModel(BigDecimal id) {
        rec_fileManager fileManager = null;
        try {
            fileManager = (rec_fileManager) daoSupport.findForObject("confing/mappers.rec_fileManager.selectByPrimaryKey", id);
        } catch (Exception e) {
            e.printStackTrace();
            fileManager = null;
        }
        return fileManager;
    }

    //分页查询所有待发文人员的信息
    public List<rec_result> getList(PageData pageData) {
        List<rec_result> resultList = null;
        try {
            resultList = (List<rec_result>) daoSupport.findForList("confing/mappers.rec_fileManager.selectList", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            resultList = null;
        }
        return resultList;
    }

    //查询对应的记录总数
    public Integer getListCount(PageData pageData) {
        Integer ListCount = 0;
        try {
            ListCount = (Integer) daoSupport.findForObject("confing/mappers.rec_fileManager.selectCount", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            ListCount = 0;
        }
        return ListCount;
    }

    //根据条件查询部分生成文件信息
    public List<rec_fileManager> getRecResultWhere(PageData pageData) {
        List<rec_fileManager> blist = null;
        try {
            blist = (List<rec_fileManager>) daoSupport.findForList("confing/mappers.rec_fileManager.getRecResultWhere", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            blist = null;
        }
        return blist;
    }

   // 分页查询所有文件信息
    public List<rec_fileManager> getFileList(PageData pd) {
        List<rec_fileManager> fileManagerList = null;
        try {
            fileManagerList = (List<rec_fileManager>) daoSupport.findForList("confing/mappers.rec_fileManager.selectFileList", pd);
        } catch (Exception e) {
            e.printStackTrace();
            fileManagerList = null;
        }
        return fileManagerList;
    }

    //查询所有文件总数
    public Integer getFileListCount(PageData pageData) {
        Integer ListCount = 0;
        try {
            ListCount = (Integer) daoSupport.findForObject("confing/mappers.rec_fileManager.selectFileListCount", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            ListCount = 0;
        }
        return ListCount;
    }
}
