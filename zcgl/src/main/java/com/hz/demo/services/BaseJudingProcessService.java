package com.hz.demo.services;

import com.hz.demo.core.Page;
import com.hz.demo.core.PageData;
import com.hz.demo.core.ResultUtils;
import com.hz.demo.core.Tree;
import com.hz.demo.dao.DaoSupportImpl;
import com.hz.demo.model.base_judging_process;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("BaseJudingProcessService")
public class BaseJudingProcessService {
    @Resource(name = "daoSupportImpl")
    private DaoSupportImpl daoSupport;
    //添加信息
    @Transactional(propagation= Propagation.NESTED)
    public int add(base_judging_process model) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.insert("confing/mappers.base_judging_process.insertSelective", model);
        } catch (Exception e) {
            throw new RuntimeException();
        }
        return iFlag;
    }
    // 根据ID更新信息
    @Transactional(propagation= Propagation.NESTED)
    public boolean updateJudgingProposerStage(PageData pageData) {
        try {
            daoSupport.update("confing/mappers.base_proposer.updateJudgingProposerStage", pageData);
        } catch (Exception e) {
            throw new RuntimeException();
        }
        return true;
    }


    // 根据ID更新信息
    @Transactional(propagation= Propagation.NESTED)
    public int update(base_judging_process pageData) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.update("confing/mappers.base_judging_process.updateByPrimaryKeySelective", pageData);
        } catch (Exception e) {
            throw new RuntimeException();
        }
        return iFlag;
    }

    //根据ID查询用户返回实体信息
    public base_judging_process getModel(String JUDGING_CODE) {
        base_judging_process judging = null;
        try {
            judging = (base_judging_process) daoSupport.findForObject("confing/mappers.base_judging_process.selectByPrimaryKey", JUDGING_CODE);
        } catch (Exception e) {
            e.printStackTrace();
            judging = null;
        }
        return judging;
    }
    //查询评委会当前执行的节点
    public PageData selectCurrentSpeciality(String JUDGING_CODE) {
        PageData menu = null;
        try {
            menu = (PageData) daoSupport.findForObject("confing/mappers.base_judging_process.selectCurrentSpeciality", JUDGING_CODE);
        } catch (Exception e) {
            e.printStackTrace();
            menu = null;
        }
        return menu;
    }

    //根据ID查询用户返回实体信息
    public PageData selectByPrimaryKeytwo(String JUDGING_CODE) {
        PageData judging = null;
        try {
            judging = (PageData) daoSupport.findForObject("confing/mappers.base_judging_process.selectByPrimaryKeytwo", JUDGING_CODE);
        } catch (Exception e) {
            e.printStackTrace();
            judging = null;
        }
        return judging;
    }
    //根据ID查询用户返回实体信息
    public PageData selectByPrimaryKeyThree(String JUDGING_CODE) {
        PageData judging = null;
        try {
            judging = (PageData) daoSupport.findForObject("confing/mappers.base_judging_process.selectByPrimaryKeyThree", JUDGING_CODE);
        } catch (Exception e) {
            e.printStackTrace();
            judging = null;
        }
        return judging;
    }

    //根据条件查询返回集合
    public List<Tree> getListWhere(String MANAGE_UNIT) {
        List<Tree> treeList = new ArrayList<>();
        List<Map<String, String>> hashMaps = new ArrayList<>();
        try {
            hashMaps = (List<Map<String, String>>) daoSupport.findForList("confing/mappers.base_judging_process.selectJudingCode", MANAGE_UNIT);
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

    public PageData selectJudgingProcess(PageData pageData)throws Exception{
        Integer zhi = Integer.valueOf(pageData.getString("zhi") == null ? "" : pageData.getString("zhi"));
        String PROCESS_GROUP = pageData.getString("PROCESS_GROUP") == null ? "" : pageData.getString("PROCESS_GROUP");
        Integer PROCESS_TYPE = Integer.valueOf(pageData.getString("PROCESS_TYPE") == null ? "" : pageData.getString("PROCESS_TYPE"));
        String JUDGING_CODE = pageData.getString("JUDGING_CODE") == null ? "" : pageData.getString("JUDGING_CODE");
        PageData pd=new PageData();
        pd.put("JUDGING_CODE",JUDGING_CODE);
        Integer  ListCount=0;
        if(Integer.valueOf(zhi)>PROCESS_TYPE){  //进入下一步
            switch (zhi) {
                case 2:
                    ListCount = (Integer) daoSupport.findForObject("confing/mappers.base_judging_process.selectProcessTwo", pageData);
                    if(ListCount==0){
                        pd.put("msg","尚未设置专业组！"); //无法进入下一步
                    }else{
                        pd.put("msg","yes");
                    }
                    break;
                case 3:
                    ListCount= (Integer) daoSupport.findForObject("confing/mappers.base_judging_process.selectProcessThree", JUDGING_CODE);
                    if(ListCount>0){
                        pd.put("msg","申报人员信息尚未审查完毕！");//无法进入下一步
                    }else{
                        pd.put("msg","yes");
                    }
                    break;
                case 4:
                    ListCount= (Integer) daoSupport.findForObject("confing/mappers.base_judging_process.selectProcessFour", JUDGING_CODE);
                    if(ListCount>0){
                        pd.put("msg","有申报人员尚未分组！");//无法进入下一步
                    }else{
                        pd.put("msg","yes");
                    }
                    break;
                case 5:
                    PageData pdd=(PageData) daoSupport.findForObject("confing/mappers.base_judging_process.selectProcessFive", JUDGING_CODE);
                    Integer zj=Integer.valueOf(pdd.get("ZJ").toString());
                    Integer SB=Integer.valueOf(pdd.get("SB").toString());
                    Integer SL=Integer.valueOf(pdd.get("SL").toString());
                    Integer num=zj*SB;
                    if(zj==0){
                        pd.put("msg","专业组中没有专家信息！"); //无法进入下一步
                    }else{
                        if(SL<num){
                            pd.put("msg","有专家尚未投票！"); //无法进入下一步
                        }else{
                            pd.put("msg","yes");
                        }
                    }
                    break;
                case 6:
                    if(PROCESS_GROUP.equals("2")){
                        ListCount= (Integer) daoSupport.findForObject("confing/mappers.base_judging_process.selectProcessThree", JUDGING_CODE);
                        if(ListCount>0){
                            pd.put("msg","申报人员信息尚未审查完毕！");//无法进入下一步
                        }else{
                            pd.put("msg","yes");
                        }
                    }else{
                        pd.put("msg","yes");
                    }
                    break;
                case 7:
                    PageData pageData1=(PageData) daoSupport.findForObject("confing/mappers.base_judging_process.selectProcessSeven", JUDGING_CODE);
                     zj=Integer.valueOf(pageData1.get("ZJ").toString());
                     SB=Integer.valueOf(pageData1.get("SB").toString());
                     SL=Integer.valueOf(pageData1.get("SL").toString());
                     num=zj*SB;
                    if(zj==0){
                        pd.put("msg","评委会中没有评委信息！"); //无法进入下一步
                    }else{
                        if(SL<num){
                            pd.put("msg","有评委尚未投票！"); //无法进入下一步
                        }else{
                            pd.put("msg","yes");
                        }
                    }
                    break;
                case 8:
                    pd.put("msg","yes");
                    break;
            }
        }else{
            pd.put("msg","yes");
        }
        return pd;
    }

}
