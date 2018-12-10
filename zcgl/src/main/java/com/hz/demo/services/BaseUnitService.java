package com.hz.demo.services;

import com.hz.demo.core.PageData;
import com.hz.demo.core.Tree;
import com.hz.demo.model.base_unit;

import javax.annotation.Resource;

import com.hz.demo.dao.DaoSupportImpl;
import com.hz.demo.model.sys_dict;
import org.springframework.stereotype.Service;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("BaseUnitService")
public class BaseUnitService {

    protected final Log logger = LogFactory.getLog(getClass());

    @Resource(name = "daoSupportImpl")
    private DaoSupportImpl daoSupport;

    //新增
    public int add(PageData pageData) {
        logger.info("增加BaseUnit");
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.insert("confing/mappers.base_unit.insert", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            iFlag = 0;
        }
        return iFlag;
    }

    //修改
    public int update(PageData pageData) {
        logger.info("修改BaseUnit");
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.update("confing/mappers.base_unit.updateByPrimaryKeySelective", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            iFlag = 0;
        }
        return iFlag;
    }

    //删除
    public int delete(Integer id) {
        logger.info("删除BaseUnit");
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.delete("confing/mappers.base_unit.deleteBaseUnitByFid", id);
        } catch (Exception e) {
            e.printStackTrace();
            iFlag = 0;
        }
        return iFlag;
    }

    //根据主键获取实体
    public base_unit getModel(Integer id) {
        logger.info("通过ID查询BaseUnit");
        base_unit baseunit = null;
        try {
            baseunit = (base_unit) daoSupport.findForObject("confing/mappers.base_unit.getBaseUnitForId", id);
        } catch (Exception e) {
            e.printStackTrace();
            baseunit = null;
        }
        return baseunit;
    }

    //根据条件查询返回实体
    public base_unit getModelWhere(PageData pageData) {
        base_unit baseunit = null;
        try {
            baseunit = (base_unit) daoSupport.findForObject("confing/mappers.base_unit.getModelWhere", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            baseunit = null;
        }
        return baseunit;
    }

    //分页查询
    public List<base_unit> getList(PageData pageData) {
        logger.info("分页查询BaseUnit");
        List<base_unit> baseunitList = null;
        try {
            baseunitList = (List<base_unit>) daoSupport.findForList("confing/mappers.base_unit.getAllBaseUnit", pageData);
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
            ListCount = (Integer) daoSupport.findForObject("confing/mappers.base_unit.selectCount", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            ListCount = 0;
        }
        return ListCount;
    }

    //根据机构类别,行政区化代码生成新的机构unitcode
    //首先获取当前条件下最大编码
    public String getUnitCode(PageData pageData) {
        logger.info("根据机构类别,行政区化代码生成新的机构unitcode");
        String newUnitCode = null;
        try {
            newUnitCode = (String) daoSupport.findForObject("confing/mappers.base_unit.selectUnitCode", pageData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newUnitCode;
    }

    /* //根据条件查询返回集合
     public List<Tree> getListWhere(PageData pageData) {
         List<Tree> blist = null;
         try {
             blist = (List<Tree>) daoSupport.findForList("confing/mappers.base_unit.selectListWhere", pageData);
         } catch (Exception e) {
             e.printStackTrace();
         }
         return blist;
     }
 */
    public base_unit getUnit(String build_unit, String unitCode) {
        PageData pageData = new PageData();
        pageData.put("unitCode", unitCode);
        base_unit unit = getUnitWhere(pageData);
        return unit;
    }

    //根据条件查询返回实体信息
    public base_unit getUnitWhere(PageData pageData) {
        base_unit model = null;
        try {
            model = (base_unit) daoSupport.findForObject("confing/mappers.base_unit.getUnitWhere", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            model = null;
        }
        return model;
    }

    //根据条件查询返回集合
    public List<Tree> getListWhere(PageData pageData) {
        List<Tree> treeList = new ArrayList<>();
        List<Map<String, String>> hashMaps = new ArrayList<>();
        try {
            hashMaps = (List<Map<String, String>>) daoSupport.findForList("confing/mappers.base_unit.selectListWhere", pageData);
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
    //根据条件查询返回集合
    public List<Tree> ybTree(PageData pageData) {
        List<Tree> treeList = new ArrayList<>();
        List<PageData> pditem = new ArrayList<PageData>();
        try {
            pditem = (List<PageData>) daoSupport.findForList("confing/mappers.base_unit.ybTree", pageData);
            for (PageData map : pditem) {
                Tree tree = new Tree();
                tree.setId(map.getString("ID"));
                tree.setText(map.getString("TEXT"));
                tree.setPid(map.getString("PID"));
                treeList.add(tree);
            }
        } catch (Exception e) {
            e.printStackTrace();
            treeList = null;
        }
        return treeList;
    }

    //根据当前单位父编号获取所有上级单位
    public void getAllSuperUnit(String parentunitCode,List<base_unit> list){
        if(parentunitCode!=null){
            try {
                base_unit bu=(base_unit)daoSupport.findForObject("confing/mappers.base_unit.getSuperUnit", parentunitCode);
                if(bu!=null){
                    parentunitCode=bu.getParentUnitCode();
                    list.add(bu);
                }else {
                    parentunitCode=null;
                }

                getAllSuperUnit(parentunitCode,list);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return;
    }


    public Integer ybTreeSize(String pid){
        Integer pditem=0;
        try {
            pditem = Integer.valueOf(String.valueOf(daoSupport.findForObject("confing/mappers.base_unit.ybTreeSize", pid)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pditem;
    }
    //获取评委会下学科组树形
    public List<Tree> getGroupTreeList(PageData pageData) {
        List<Tree> treeList = new ArrayList<>();
        List<Map<String, Object>> hashMaps = new ArrayList<>();
        try {
            hashMaps = (List<Map<String, Object>>) daoSupport.findForList("confing/mappers.base_unit.getGroupTreeList", pageData);
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
            ptree.setText(pageData.getString("JUDGING_NAME"));
            treeList.add(ptree);
        } catch (Exception e) {
            e.printStackTrace();
            treeList = null;
        }
        return treeList;
    }

}
