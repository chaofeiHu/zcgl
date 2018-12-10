package com.hz.demo.services;


import com.hz.demo.core.Page;
import com.hz.demo.core.PageData;
import com.hz.demo.core.Tree;
import com.hz.demo.dao.DaoSupportImpl;
import com.hz.demo.model.base_judging;
import com.hz.demo.model.base_speciality;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("SpecialityTicketsService")
public class SpecialityTicketsService {

    @Resource(name = "daoSupportImpl")
    private DaoSupportImpl daoSupport;

    //添加信息
    public int add(base_speciality model) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.insert("confing/mappers.speciality_tickets.insert", model);

        } catch (Exception e) {
            e.printStackTrace();
            iFlag = 0;
        }
        return iFlag;
    }

    //添加评审结果信息
    @Transactional(propagation= Propagation.NESTED)
    public int addRec(PageData model) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.insert("confing/mappers.rec_reviewresult.insertSelective", model);
        } catch (Exception e) {
            iFlag = 0;
            throw new RuntimeException();
        }
        return iFlag;
    }
    // 根据ID更新评委专家信息
    @Transactional(propagation= Propagation.NESTED)
    public int updateRec(PageData pageData) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.update("confing/mappers.rec_reviewresult.updateByPrimaryKeySelective", pageData);
        } catch (Exception e) {
            throw new RuntimeException();
        }
        return iFlag;
    }
    // 根据ID更新评委专家信息
    public int update(PageData pageData) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.update("confing/mappers.speciality_tickets.updateByPrimaryKeySelective", pageData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }

    //根据ID删除评委专家信息
    public int delete(Integer id) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.delete("confing/mappers.speciality_tickets.deleteByPrimaryKey", id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }

    //根据ID查询评委专家返回实体信息
    public base_speciality getModel(Integer id) {
        base_speciality speciality = null;
        try {
            speciality = (base_speciality) daoSupport.findForObject("confing/mappers.speciality_tickets.selectByPrimaryKey", id);
        } catch (Exception e) {
            e.printStackTrace();
            speciality = null;
        }
        return speciality;
    }

    //查询所有的信息
    public List<base_speciality> getList(PageData pageData) {
        List<base_speciality> specialityList = null;
        try {
            specialityList = (List<base_speciality>) daoSupport.findForList("confing/mappers.speciality_tickets.selectList", pageData);
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
            ListCount = (Integer) daoSupport.findForObject("confing/mappers.speciality_tickets.selectCount", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            ListCount = 0;
        }
        return ListCount;
    }


    public List<PageData> selectTicketsInfo(PageData pageData){
        List<PageData> specialityList = null;
        try {
            specialityList = (List<PageData>) daoSupport.findForList("confing/mappers.speciality_tickets.selectTicketsInfo", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            specialityList = null;
        }
        return specialityList;
    }
    public Integer selectTicketsInfoCount(PageData pageData) {
        Integer ListCount = 0;
        try {
            ListCount = (Integer) daoSupport.findForObject("confing/mappers.speciality_tickets.selectTicketsInfoCount", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            ListCount = 0;
        }
        return ListCount;
    }
    public PageData findSpliTick(PageData pageData) {
        PageData ListCount = new PageData();
        try {
            ListCount = (PageData) daoSupport.findForObject("confing/mappers.speciality_tickets.findSpliTick", pageData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ListCount;
    }

    @Transactional(propagation= Propagation.REQUIRED)
    public void updateTickets(PageData str) {
        try {
            String arrPerson=str.getString("arrPerson");
            JSONArray item=JSONArray.fromObject(arrPerson);
            for(int i=0;i<item.size();i++){
                JSONObject object=item.getJSONObject(i);
                PageData pageData=new PageData();
                pageData.put("GROUP_ID",object.get("GROUP_ID"));
                pageData.put("SPECIALITY_ID",str.getString("SPECIALITY_ID"));
                pageData.put("TICKETS",object.get("TICKETS"));
                pageData.put("REMARKS",object.get("REMARKS"));
                pageData.put("PROPOSER_ID",object.get("PROPOSER_ID"));
                pageData.put("TYPE",object.get("TYPE"));
                daoSupport.findForObject("confing/mappers.speciality_tickets.updateTickets", pageData);
            }
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    //查询所有的信息
    public List<PageData> selectTicketsRemarks(PageData pageData) {
        List<PageData> specialityList = null;
        try {
            specialityList = (List<PageData>) daoSupport.findForList("confing/mappers.speciality_tickets.selectTicketsRemarks", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            specialityList = null;
        }
        return specialityList;
    }

    /**
     * 获取申请人得票数
     * @param pageData
     * @return
     */
    public List<PageData> selectTicketsComment(PageData pageData) {
        List<PageData> specialityList = null;
        try {
            specialityList = (List<PageData>) daoSupport.findForList("confing/mappers.speciality_tickets.selectTicketsComment", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            specialityList = null;
        }
        return specialityList;
    }
    //查询学科组总评人数，查询学科组比例
    public PageData selectCommentNums(PageData pageData) {
        PageData specialityList = null;
        try {
            specialityList = (PageData) daoSupport.findForObject("confing/mappers.speciality_tickets.selectCommentNums", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            specialityList = null;
        }
        return specialityList;
    }

    //评委会评议情况汇总
    public List<PageData> getCommentList(PageData pageData) {
        List<PageData> specialityList = null;
        try {
            specialityList = (List<PageData>) daoSupport.findForList("confing/mappers.speciality_tickets.getCommentList", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            specialityList = null;
        }
        return specialityList;
    }

    public PageData selectPropser(PageData pageData) {
        PageData specialityList = null;
        try {
            specialityList = (PageData) daoSupport.findForObject("confing/mappers.speciality_tickets.selectPropser", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            specialityList = null;
        }
        return specialityList;
    }

    /***
     * 根据年度和userid查询评审结果信息
     */
    public PageData selectRec(PageData pageData) {
        PageData specialityList = null;
        try {
            specialityList = (PageData) daoSupport.findForObject("confing/mappers.rec_reviewresult.selectRec", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            specialityList = null;
        }
        return specialityList;
    }
}
