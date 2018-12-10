package com.hz.demo.services;


import com.hz.demo.core.Page;
import com.hz.demo.core.PageData;
import com.hz.demo.core.Tree;
import com.hz.demo.dao.DaoSupportImpl;
import com.hz.demo.model.base_engage;
import com.hz.demo.model.base_judging;
import com.hz.demo.model.base_speciality;
import com.hz.demo.model.speciality_group;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service("SpecialityGroupService")
public class SpecialityGroupService {

    @Resource(name = "daoSupportImpl")
    private DaoSupportImpl daoSupport;

    //添加信息
    public void add(PageData model) {
        try {
           daoSupport.insert("confing/mappers.speciality_group.insert", model);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 根据ID更新评委专家信息
    public void update(PageData pageData) {
        try {
             daoSupport.update("confing/mappers.speciality_group.updateByPrimaryKeySelective", pageData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //
    @Transactional(propagation= Propagation.NESTED)
    public int updateGroup(PageData pageData) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.update("confing/mappers.speciality_group.updateGroup", pageData);
        } catch (Exception e) {
            throw  new RuntimeException();
        }
        return iFlag;
    }

    /**
     * 设置组长
     * @param pageData
     * @return
     */
    @Transactional(propagation= Propagation.NESTED)
    public int updateGroupTwo(PageData pageData) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.update("confing/mappers.speciality_group.updateGroupTwo", pageData);
        } catch (Exception e) {
            throw  new RuntimeException();
        }
        return iFlag;
    }

    //根据ID删除评委专家信息
    public int delete(Integer id) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.delete("confing/mappers.speciality_group.deleteByPrimaryKey", id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }

    //根据ID查询评委专家返回实体信息
    public speciality_group getModel(Integer id) {
        speciality_group specialityGroup= null;
        try {
            specialityGroup= (speciality_group) daoSupport.findForObject("confing/mappers.speciality_group.selectByPrimaryKey", id);
        } catch (Exception e) {
            e.printStackTrace();
            specialityGroup= null;
        }
        return specialityGroup;
    }

    //查询所有的信息
    public List<PageData> getlistPage(Page pageData) {
        List<PageData> specialityList = null;
        try {
            specialityList = (List<PageData>) daoSupport.findForList("confing/mappers.speciality_group.getlistPage", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            specialityList = null;
        }
        return specialityList;
    }

    //查询对应的记录总数
    public Integer getListCount(PageData pageData) {
        Integer ListCount = 0;
        try {
            ListCount = (Integer) daoSupport.findForObject("confing/mappers.speciality_group.selectCount", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            ListCount = 0;
        }
        return ListCount;
    }

    //根据条件查询某个评委专家信息
    public speciality_group getModelWhere(PageData pageData) {
        speciality_group specialityGroup= null;
        try {
            specialityGroup= (speciality_group) daoSupport.findForObject("confing/mappers.speciality_group.getModelWhere", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            specialityGroup= null;
        }
        return specialityGroup;
    }

    //获取评委会/评委会分组下专家界面列表
    public List<PageData> getSpecialitylistPage(Page pageData) {
        List<PageData> specialityList = null;
        try {
            specialityList = (List<PageData>) daoSupport.findForList("confing/mappers.speciality_group.getSpecialitylistPage", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            specialityList = null;
        }
        return specialityList;
    }

    //获取评委会/评委会分组下专家 数量
    public Integer getSpecialityListCount(PageData pageData) {
        Integer ListCount = 0;
        try {
            ListCount = (Integer) daoSupport.findForObject("confing/mappers.speciality_group.getSpecialityListCount", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            ListCount = 0;
        }
        return ListCount;
    }

    //查询学科分组下专家人数
    public Integer getSpecialityCount(Integer groupId) {
        Integer ListCount = 0;
        try {
            ListCount = (Integer) daoSupport.findForObject("confing/mappers.speciality_group.getSpecialityCount", groupId);
        } catch (Exception e) {
            e.printStackTrace();
            ListCount = 0;
        }
        return ListCount;
    }

    //查询评委会下专家总数
    public Integer getAllSpecialityCount(String judgingId) {
        Integer ListCount = 0;
        try {
            ListCount = (Integer) daoSupport.findForObject("confing/mappers.speciality_group.getAllSpecialityCount", judgingId);
        } catch (Exception e) {
            e.printStackTrace();
            ListCount = 0;
        }
        return ListCount;
    }

    //根据其他条件删除分组
    @Transactional(propagation= Propagation.NESTED)
    public Integer deleteWhere(PageData pageData) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.delete("confing/mappers.speciality_group.deleteWhere", pageData);
        } catch (Exception e) {
           throw new RuntimeException();
        }
        return iFlag;
    }

    //随机获取专家
    public List<base_engage> getSpecialityByRandom(PageData pageData) {
        List<base_engage> blist = null;
        try {
            blist = (List<base_engage>) daoSupport.findForList("confing/mappers.speciality_group.getSpecialityByRandom", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            blist = null;
        }
        return blist;
    }
        //查询某专业未被评委会聘请的人数
    public PageData selectSubjectSpecialityCount(PageData pageData) {
        PageData blist = null;
        try {
            blist = (PageData) daoSupport.findForObject("confing/mappers.speciality_group.selectSubjectSpecialityCount", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            blist = null;
        }
        return blist;
    }
    //添加评委会专家信息
    public int addEngage(PageData model) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.insert("confing/mappers.speciality_group.addEngage", model);
        } catch (Exception e) {
            e.printStackTrace();
            iFlag = 0;
        }
        return iFlag;
    }
    /***
     * 获取学科组绑定专业下拉框
     */
    public List<PageData> selectProfessial(PageData pageData) {
        List<PageData> blist = null;
        try {
            blist = (List<PageData>) daoSupport.findForList("confing/mappers.speciality_group.selectProfessial", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            blist = null;
        }
        return blist;
    }
}
