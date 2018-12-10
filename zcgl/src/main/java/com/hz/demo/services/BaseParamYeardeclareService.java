package com.hz.demo.services;

import com.hz.demo.core.PageData;
import com.hz.demo.dao.DaoSupportImpl;
import com.hz.demo.model.base_judging;
import com.hz.demo.model.base_param_yeardeclare;
import com.hz.demo.model.base_series_professial;
import com.hz.demo.model.sys_dict;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("BaseParamYeardeclareService")
public class BaseParamYeardeclareService {
    @Resource(name = "daoSupportImpl")
    private DaoSupportImpl daoSupport;
    //添加信息
    public int add(base_param_yeardeclare model) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.insert("confing/mappers.base_param_yeardeclare.insertSelective", model);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }
    //查询所有的信息
    public List<base_param_yeardeclare> getList(PageData pageData) {
        List<base_param_yeardeclare> yeardeclareList = null;
        try {
            yeardeclareList = (List<base_param_yeardeclare>) daoSupport.findForList("confing/mappers.base_param_yeardeclare.selectList", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            yeardeclareList = null;
        }
        return yeardeclareList;
    }
    //根据ID查询用户返回实体信息
    public base_param_yeardeclare getModel(String id) {
        base_param_yeardeclare judging = null;
        try {
            judging = (base_param_yeardeclare) daoSupport.findForObject("confing/mappers.base_param_yeardeclare.selectByPrimaryKey", Integer.parseInt(id));
        } catch (Exception e) {
            e.printStackTrace();
            judging = null;
        }
        return judging;
    }
    // 根据ID更新信息
    public int update(PageData pageData) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.update("confing/mappers.base_param_yeardeclare.updateByPrimaryKeySelective", pageData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }
    public int updateState(PageData pageData) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.update("confing/mappers.base_param_yeardeclare.updateState", pageData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }
  public int updateTime(){
      int iFlag = 0;
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
      String yearnumber=sdf.format(new Date());
      try {
          iFlag = (int) daoSupport.update("confing/mappers.base_param_yeardeclare.updateTime",yearnumber);
      } catch (Exception e) {
          e.printStackTrace();
      }
      return iFlag;

  }

    public int addList() {
        int iFlag = 0;
        List<base_param_yeardeclare> list=new ArrayList<>();
        try {
            List<sys_dict> dictl=(List<sys_dict>) daoSupport.findForList("confing/mappers.sys_dict.getDictSeiesCode",null);
            List<base_param_yeardeclare> l2=(List<base_param_yeardeclare>)daoSupport.findForList("confing/mappers.base_param_yeardeclare.getDictSeiesCode",null);
            for (int i=0;i<dictl.size();i++){
                base_param_yeardeclare model1 = new base_param_yeardeclare();
                model1.setStartState(BigDecimal.valueOf(1));
                model1.setBegintime(new Date());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
                Date date = new Date();
                model1.setYearnumber(sdf.format(date));
                model1.setEndtime(new Date());
                model1.setReviewSeries(dictl.get(i).getDictCode());
                boolean boo=true;
                for (base_param_yeardeclare b2:l2){
                    if(StringUtils.equalsIgnoreCase(b2.getReviewSeries(),dictl.get(i).getDictCode())){
                        boo=false;
                    }
                }
                if(boo){
                    list.add(model1);
                }
            }
            for (base_param_yeardeclare b3:list
                 ) {
                iFlag = (int) daoSupport.insert("confing/mappers.base_param_yeardeclare.insertSelective", b3);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }
}
