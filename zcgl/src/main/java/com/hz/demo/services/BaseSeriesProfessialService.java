package com.hz.demo.services;

import com.hz.demo.core.PageData;
import com.hz.demo.core.Tree;
import com.hz.demo.dao.DaoSupportImpl;
import com.hz.demo.model.base_series_professial;
import com.hz.demo.model.sys_dict;
import com.hz.demo.model.sys_rolemenufun;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service("BaseSeriesProfessialService")
public class BaseSeriesProfessialService {

    @Resource(name = "daoSupportImpl")
    private DaoSupportImpl daoSupport;
    //添加信息
    public int add(base_series_professial model) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.insert("confing/mappers.base_series_professial.insertSelective", model);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }
    public int delete(String id) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.delete("confing/mappers.base_series_professial.deleteByPrimaryKey", Integer.parseInt(id));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }
//    //保存评审系列对应的专业
//    public int SaveSeriesProfessial(PageData pageData) {
//        int iFlag = 0;
//        try {
//            //清除当前角色的所有权限
//            daoSupport.delete("confing/mappers.base_series_professial.deleteRoleByMenuFun", pageData);
//
//            for (String MenuFunID : (String[]) pageData.get("MenuFunIDS")) {//选中集合
//                base_series_professial model = new base_series_professial();
//                model.setReviewSeries(pageData.get("reviewSeries").toString());
//                model.setReviewProfessial(MenuFunID);
//                daoSupport.insert("confing/mappers.base_series_professial.insertSelective", model);
//            }
//            iFlag = 1;
//        } catch (Exception e) {
//            e.printStackTrace();
//            iFlag = 0;
//        }
//        return iFlag;
//    }

   //根据评审系列查询该系列所有绑定的专业
   public List<base_series_professial> getRevProByRevSer(PageData pageData) {
       List<base_series_professial> basSerProList =null;
       try {
           basSerProList = (List<base_series_professial>) daoSupport.findForList("confing/mappers.base_series_professial.getRevProByRevSer", pageData);
       }catch (Exception e){
           e.printStackTrace();
           basSerProList =null;
       }
       return basSerProList;
   }
    public List<Tree> getBingList(PageData pageData) {
        List<Tree> treeList = new ArrayList<>();
        List<Map<String, String>> hashMaps = new ArrayList<>();
        try {
            hashMaps = (List<Map<String, String>>) daoSupport.findForList("confing/mappers.base_series_professial.getBingList", pageData);
            for (Map<String, String> map : hashMaps) {
                Tree tree = new Tree();
                tree.setId(map.get("ID"));
                tree.setText(map.get("TEXT"));
                tree.setPid(map.get("PID"));
                treeList.add(tree);
            }
        } catch (Exception e) {
            e.printStackTrace();
            treeList = null;
        }
        return treeList;
    }
    public base_series_professial getModel(String id) {
        base_series_professial model = null;
        try {
            model = (base_series_professial) daoSupport.findForObject("confing/mappers.base_series_professial.selectByPrimaryKey",Integer.parseInt(id));
        } catch (Exception e) {
            e.printStackTrace();
            model = null;
        }
        return model;
    }
    public base_series_professial getProfe(String base_series_professial, String professialCode) {
        PageData pageData = new PageData();
        pageData.put("professialCode", professialCode);
        base_series_professial unit = getProfeWhere(pageData);
        return unit;
    }
    public List<PageData> getProfessialByGroup(PageData pageData){
        List<PageData> model = null;
        try {
            List<PageData> item=(List<PageData>) daoSupport.findForList("confing/mappers.base_series_professial.selectProfessialByJudging", pageData);
            StringBuilder sb=new StringBuilder();
            for(PageData pd:item){
                sb.append(pd.getString("PROFESSIAL_ID")+",");
            }
            String str=String.join(",",sb.toString().split(",+"));
            pageData.put("professial_id",str);
            model = (List<PageData>) daoSupport.findForList("confing/mappers.base_series_professial.getProfessialByGroup", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            model = null;
        }
        return model;
    }

    public base_series_professial getProfeWhere(PageData pageData) {
        base_series_professial model = null;
        try {
            model = (base_series_professial) daoSupport.findForObject("confing/mappers.base_series_professial.getProfeWhere", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            model = null;
        }
        return model;
    }
    public int update(PageData pageData) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.update("confing/mappers.base_series_professial.updateByPrimaryKeySelective", pageData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }

    //查询所有的信息
    public List<base_series_professial> getList(PageData pageData) {
        List<base_series_professial> professialList = null;
        try {
            professialList = (List<base_series_professial>) daoSupport.findForList("confing/mappers.base_series_professial.selectList", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            professialList = null;
        }
        return professialList;
    }
    public List<base_series_professial> getListName(PageData pageData) {
        List<base_series_professial> professialList = null;
        try {
            professialList = (List<base_series_professial>) daoSupport.findForList("confing/mappers.base_series_professial.selectListName", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            professialList = null;
        }
        return professialList;
    }
    public base_series_professial getListName1(PageData pageData) {
       base_series_professial professialList = null;
        try {
            professialList = (base_series_professial) daoSupport.findForObject("confing/mappers.base_series_professial.selectListName", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            professialList = null;
        }
        return professialList;
    }

    //查询对应的记录总数
    public Integer getListCount(PageData pageData) {
        Integer ListCount = 0;
        try {
            ListCount = (Integer) daoSupport.findForObject("confing/mappers.base_series_professial.selectCount", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            ListCount = 0;
        }
        return ListCount;
    }
}
