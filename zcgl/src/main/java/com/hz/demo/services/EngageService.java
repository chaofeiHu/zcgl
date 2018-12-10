package com.hz.demo.services;


import com.hz.demo.core.Page;
import com.hz.demo.core.PageData;
import com.hz.demo.core.Tree;
import com.hz.demo.dao.DaoSupportImpl;
import com.hz.demo.model.base_engage;
import com.hz.demo.model.base_judging;
import com.hz.demo.model.base_speciality;
import com.hz.demo.model.sys_user;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("EngageService")
public class EngageService {

    @Resource(name = "daoSupportImpl")
    private DaoSupportImpl daoSupport;

    //添加评委会专家信息
    public int add(base_engage model) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.insert("confing/mappers.base_engage.insert", model);
        } catch (Exception e) {
            e.printStackTrace();
            iFlag = 0;
        }
        return iFlag;
    }

    /**添加评委会专家**/
    public int addEngage(PageData model) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.insert("confing/mappers.base_engage.insertEngage", model);
        } catch (Exception e) {
            iFlag = 0;
            throw  new RuntimeException();
        }
        return iFlag;
    }


    // 根据ID更新评委专家及所在评委会信息
    public void update(PageData pageData) {
        try {
          daoSupport.update("confing/mappers.base_engage.updateByPrimaryKeySelective", pageData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //删除评委会专家
    public int deleteEngage(PageData pd) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.delete("confing/mappers.base_engage.deleteByPrimaryKey", pd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }

    //根据ID删除评委专家及所在评委会信息
    public int delete(Integer id) {
        int iFlag = 0;
        try {
            //删除评委会专家表,顺便删除专家,先查询专家id
            base_engage baseEngage = getModel(id);
            if (baseEngage != null) {
                String specialityId = baseEngage.getSpecialityId();
                iFlag = (int) daoSupport.delete("confing/mappers.base_speciality.deleteByPrimaryKey", Integer.parseInt(specialityId));
            }
            iFlag += (int) daoSupport.delete("confing/mappers.base_engage.deleteByPrimaryKey", id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }

    //根据ID查询评委专家及所在评委会返回实体信息
    public base_engage getModel(Integer id) {
        base_engage speciality = null;
        try {
            speciality = (base_engage) daoSupport.findForObject("confing/mappers.base_engage.selectByPrimaryKey", id);
        } catch (Exception e) {
            e.printStackTrace();
            speciality = null;
        }
        return speciality;
    }

    //查询所有的信息
    public List<base_engage> getList(PageData pageData) {
        List<base_engage> engageList = null;
        try {
            engageList = (List<base_engage>) daoSupport.findForList("confing/mappers.base_engage.selectList", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            engageList = null;
        }
        return engageList;
    }

    //查询对应的记录总数
    public Integer getListCount(PageData pageData) {
        Integer ListCount = 0;
        try {
            ListCount = (Integer) daoSupport.findForObject("confing/mappers.base_engage.selectCount", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            ListCount = 0;
        }
        return ListCount;
    }

    //根据条件查询某个评委专家及所在评委会信息
    public List<base_engage> getModelWhere(PageData pageData) {
        List<base_engage> blist = null;
        try {
            blist = (List<base_engage>) daoSupport.findForList("confing/mappers.base_engage.getModelWhere", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            blist = null;
        }
        return blist;
    }


    //获取评委会信息
    public List<base_judging> getEngageList(PageData pageData) {
        List<base_judging> baseEngageList = null;
        try {
            baseEngageList = (List<base_judging>) daoSupport.findForList("confing/mappers.base_engage.getEngageList", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            baseEngageList = null;
        }
        return baseEngageList;
    }

    public List<Tree> getEngageTree(PageData pageData) {
        List<Tree> treeList = new ArrayList<>();
        List<Map<String, Object>> hashMaps = new ArrayList<>();
        try {
            hashMaps = (List<Map<String, Object>>) daoSupport.findForList("confing/mappers.base_engage.getTreeList", pageData);
            for (Map<String, Object> map : hashMaps) {
                Tree tree = new Tree();
                BigDecimal id = (BigDecimal) map.get("ID");
                tree.setId(id.toString());
                tree.setText((String) map.get("TEXT"));
                tree.setPid("0");
                tree.setAttributes((String) map.get("ATTRIBUTES"));
                treeList.add(tree);
            }
            Tree ptree = new Tree();
            ptree.setId("0");
            ptree.setText("评委会名称");
            treeList.add(ptree);
        } catch (Exception e) {
            e.printStackTrace();
            treeList = null;
        }
        return treeList;
    }

    public List<PageData> selectEngageList(Page pageData) {
        List<PageData> blist = null;
        try {
            blist = (List<PageData>) daoSupport.findForList("confing/mappers.base_engage.selectEngagelistPage", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            blist = null;
        }
        return blist;
    }

    public Integer selectEngageListCount(PageData pageData) {
        Integer ListCount = 0;
        try {
            ListCount = (Integer) daoSupport.findForObject("confing/mappers.base_engage.selectEngageListCount", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            ListCount = 0;
        }
        return ListCount;
    }

    public Integer selectNoGroupSp(PageData pageData) {
        Integer ListCount = 0;
        try {
            ListCount = (Integer) daoSupport.findForObject("confing/mappers.base_engage.selectNoGroupSp", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            ListCount = 0;
        }
        return ListCount;
    }

    /***
     * 获取评委会系列绑定专业下拉框
     */
    public List<PageData> selectProfessial(PageData pageData) {
        List<PageData> blist = null;
        try {
            blist = (List<PageData>) daoSupport.findForList("confing/mappers.base_engage.selectProfessial", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            blist = null;
        }
        return blist;
    }

    /***
     * 获取随机专家
     */
    public List<PageData> selectNoGroupSpRandom(PageData pageData) {
        List<PageData> blist = null;
        try {
            blist = (List<PageData>) daoSupport.findForList("confing/mappers.base_engage.selectNoGroupSpRandom", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            blist = null;
        }
        return blist;
    }
}
