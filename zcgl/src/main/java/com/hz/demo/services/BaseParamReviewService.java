package com.hz.demo.services;

import com.hz.demo.core.PageData;
import com.hz.demo.core.Tree;
import com.hz.demo.dao.DaoSupportImpl;
import com.hz.demo.model.base_param_declare;
import com.hz.demo.model.base_param_review;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @program: pm
 * @description:
 * @author: LiuPengbo
 * @create: 2018-09-17 14:53
 **/
@Service("BaseParamReviewService")
public class BaseParamReviewService {
    @Resource(name = "daoSupportImpl")
    private DaoSupportImpl daoSupport;
    //添加信息
    public int add(base_param_review model) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.insert("confing/mappers.base_param_review.insertSelective", model);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }

    // 根据ID更新信息
    public int update(PageData pageData) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.update("confing/mappers.base_param_review.updateByPrimaryKeySelective", pageData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }

    //根据ID删除信息
    public int delete(String id) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.delete("confing/mappers.base_param_review.deleteByPrimaryKey", Integer.parseInt(id));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }

    //根据JudGing查询
    public base_param_review getByJudGingCode(PageData pageData) {
        base_param_review review = null;
        try {
            review = (base_param_review) daoSupport.findForObject("confing/mappers.base_param_review.selectByJudGingCode", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            review = null;
        }
        return review;
    }

    //查询所有的信息
    public List<base_param_review> getList(PageData pageData) {
        List<base_param_review> reviewList = null;
        try {
            reviewList = (List<base_param_review>) daoSupport.findForList("confing/mappers.base_param_review.selectList", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            reviewList = null;
        }
        return reviewList;
    }

    //查询对应的记录总数
    public Integer getListCount(PageData pageData) {
        Integer ListCount = 0;
        try {
            ListCount = (Integer) daoSupport.findForObject("confing/mappers.base_param_review.selectCount", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            ListCount = 0;
        }
        return ListCount;
    }
    //查询所有的信息
    public List<base_param_review> getMojorList(PageData pageData) {
        List<base_param_review> reviewList = null;
        try {
            reviewList = (List<base_param_review>) daoSupport.findForList("confing/mappers.base_param_review.selectMojorList", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            reviewList = null;
        }
        return reviewList;
    }

    //查询对应的记录总数
    public Integer getMojorListCount(PageData pageData) {
        Integer ListCount = 0;
        try {
            ListCount = (Integer) daoSupport.findForObject("confing/mappers.base_param_review.selectMojorCount", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            ListCount = 0;
        }
        return ListCount;
    }

    //组织结构树
    public List<Tree> getJudGingTree(PageData pageData){
        List<Tree> treeList = new ArrayList<>();
        List<Map<String, String>> hashMaps = new ArrayList<>();
        try {
            hashMaps = (List<Map<String, String>>) daoSupport.findForList("confing/mappers.base_judging.selectListWhere", pageData);
            for (Map<String, String> map : hashMaps) {
                Tree tree = new Tree();
                tree.setId(map.get("ID"));
                tree.setText(map.get("TEXT"));
//                tree.setPid(map.get("PID"));
                tree.setPid("10000");
                treeList.add(tree);
            }
        }catch (Exception e) {
            e.printStackTrace();
            treeList = null;
        }
        return treeList;

    }
}
