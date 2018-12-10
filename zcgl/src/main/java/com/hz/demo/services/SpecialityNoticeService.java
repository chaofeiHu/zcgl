package com.hz.demo.services;

import com.hz.demo.core.PageData;
import com.hz.demo.core.Tree;
import com.hz.demo.dao.DaoSupportImpl;
import com.hz.demo.model.base_proposer;
import com.hz.demo.model.base_speciality_notice;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/***
 * 通知评委   未使用
 */
@Service("SpecialityNoticeService")
public class SpecialityNoticeService {
    @Resource(name = "daoSupportImpl")
    private DaoSupportImpl daoSupport;
    //添加信息
    public int add(base_speciality_notice model) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.insert("confing/mappers.base_speciality_notice.insertSelective", model);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }

    // 根据ID更新信息
    public int update(PageData pageData) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.update("confing/mappers.base_speciality_notice.updateByPrimaryKeySelective", pageData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }
    //更新主任信息
    public int updateNotice(PageData pageData) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.update("confing/mappers.base_speciality_notice.updateNotice", pageData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }

    public int updateNoticetwo(PageData pageData) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.update("confing/mappers.base_speciality_notice.updateNoticetwo", pageData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }
    public int UpdateOrAdd(PageData pageData) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.update("confing/mappers.base_speciality_notice.UpdateOrAdd", pageData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }
    //根据ID查询返回实体信息
    public base_speciality_notice getModel(PageData id) {
        base_speciality_notice model = null;
        try {
            model = (base_speciality_notice) daoSupport.findForObject("confing/mappers.base_speciality_notice.selectByPrimaryKey", id);
        } catch (Exception e) {
            e.printStackTrace();
            model = null;
        }
        return model;
    }

    //获取各个评委会学科组各个职称人数
    public List<PageData> selectListSenior(PageData pageData) {
        List<PageData> modelList = null;
        try {
            modelList = (List<PageData>) daoSupport.findForList("confing/mappers.base_speciality_notice.selectListSenior", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            modelList = null;
        }
        return modelList;
    }

    //查询所有的信息
    public List<PageData> getList(PageData pageData) {
        List<PageData> modelList = null;
        try {
            modelList = (List<PageData>) daoSupport.findForList("confing/mappers.base_speciality_notice.selectSpeciality", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            modelList = null;
        }
        return modelList;
    }

    //查询对应的记录总数
    public Integer getListCount(PageData pageData) {
        Integer ListCount = 0;
        try {
            ListCount = (Integer) daoSupport.findForObject("confing/mappers.base_speciality_notice.selectCount", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            ListCount = 0;
        }
        return ListCount;
    }
    //查询专家信息
    public List<PageData> findSpeciality(PageData pageData) {
        List<PageData> modelList = null;
        try {
            modelList = (List<PageData>) daoSupport.findForList("confing/mappers.base_speciality_notice.findSpeciality", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            modelList = null;
        }
        return modelList;
    }
    public Integer findSpecialityCount(PageData pageData) {
        Integer ListCount = 0;
        try {
            ListCount = (Integer) daoSupport.findForObject("confing/mappers.base_speciality_notice.findSpecialityCount", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            ListCount = 0;
        }
        return ListCount;
    }

    //评委会专家确认信息列表
    public List<PageData> findSpecialityNotice(PageData pageData) {
        List<PageData> modelList = null;
        try {
            modelList = (List<PageData>) daoSupport.findForList("confing/mappers.base_speciality_notice.findSpecialityNotice", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            modelList = null;
        }
        return modelList;
    }
    public Integer findSpecialityNoticeCount(PageData pageData) {
        Integer ListCount = 0;
        try {
            ListCount = (Integer) daoSupport.findForObject("confing/mappers.base_speciality_notice.findSpecialityNoticeCount", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            ListCount = 0;
        }
        return ListCount;
    }
    public List<PageData> ExportExcelNotice(PageData pageData) {
        List<PageData> modelList = null;
        try {
            modelList = (List<PageData>) daoSupport.findForList("confing/mappers.base_speciality_notice.ExportExcelNotice", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            modelList = null;
        }
        return modelList;
    }
}
