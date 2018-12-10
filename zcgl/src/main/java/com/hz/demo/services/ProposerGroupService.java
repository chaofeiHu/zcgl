package com.hz.demo.services;


import com.hz.demo.core.Page;
import com.hz.demo.core.PageData;
import com.hz.demo.dao.DaoSupportImpl;
import com.hz.demo.model.proposer_group;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service("ProposerGroupService")
public class ProposerGroupService {

    @Resource(name = "daoSupportImpl")
    private DaoSupportImpl daoSupport;

    //添加信息
    public int add(proposer_group model) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.insert("confing/mappers.proposer_group.insert", model);

        } catch (Exception e) {
            e.printStackTrace();
            iFlag = 0;
        }
        return iFlag;
    }

    // 根据ID更新参评人信息
    public int update(PageData pageData) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.update("confing/mappers.proposer_group.updateByPrimaryKeySelective", pageData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }

    //根据ID删除参评人信息
    public int delete(Integer id) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.delete("confing/mappers.proposer_group.deleteByPrimaryKey", id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }

    //根据ID查询参评人返回实体信息
    public proposer_group getModel(Integer id) {
        proposer_group proposerGroup= null;
        try {
            proposerGroup= (proposer_group) daoSupport.findForObject("confing/mappers.proposer_group.selectByPrimaryKey", id);
        } catch (Exception e) {
            e.printStackTrace();
            proposerGroup= null;
        }
        return proposerGroup;
    }

    //查询所有的分组信息
    public List<PageData> selectlistPage(Page pageData) {
        List<PageData> proposerList = null;
        try {
            proposerList = (List<PageData>) daoSupport.findForList("confing/mappers.proposer_group.selectlistPage", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            proposerList = null;
        }
        return proposerList;
    }

    //查询对应的记录总数
    public Integer getListCount(PageData pageData) {
        Integer ListCount = 0;
        try {
            ListCount = (Integer) daoSupport.findForObject("confing/mappers.proposer_group.selectCount", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            ListCount = 0;
        }
        return ListCount;
    }

    //根据条件查询某个参评人信息
    public proposer_group getModelWhere(PageData pageData) {
        proposer_group proposerGroup= null;
        try {
            proposerGroup= (proposer_group) daoSupport.findForObject("confing/mappers.proposer_group.getModelWhere", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            proposerGroup= null;
        }
        return proposerGroup;
    }

    //获取评委会/评委会分组下参评人界面列表
    public List<PageData> getProposerlistPage(Page pageData) {
        List<PageData> proposerList = new ArrayList<>();
        try {
            proposerList = (List<PageData>) daoSupport.findForList("confing/mappers.proposer_group.getProposerlistPage", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            proposerList = null;
        }
        return proposerList;
    }

    //获取评委会/评委会分组下参评人 数量
    public Integer getProposerListCount(PageData pageData) {
        Integer ListCount = 0;
        try {
            ListCount = (Integer) daoSupport.findForObject("confing/mappers.proposer_group.getProposerListCount", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            ListCount = 0;
        }
        return ListCount;
    }

    //获取评委会未分组参评人 数量
    public Integer getNoGroupListCount(PageData pageData) {
        Integer ListCount = 0;
        try {
            ListCount = (Integer) daoSupport.findForObject("confing/mappers.proposer_group.getNoGroupListCount", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            ListCount = 0;
        }
        return ListCount;
    }

    //查询学科分组下参评人员人数
    public Integer getProposerCount(Integer groupId) {
        Integer ListCount = 0;
        try {
            ListCount = (Integer) daoSupport.findForObject("confing/mappers.proposer_group.getProposerCount", groupId);
        } catch (Exception e) {
            e.printStackTrace();
            ListCount = 0;
        }
        return ListCount;
    }

    public List<PageData> getNoGroupProposerlistPage(Page pd) {
        List<PageData> proposerList = new ArrayList<>();
        try {
            proposerList = (List<PageData>) daoSupport.findForList("confing/mappers.proposer_group.getNoGroupProposerlistPage", pd);
        } catch (Exception e) {
            e.printStackTrace();
            proposerList = null;
        }
        return proposerList;
    }
}
