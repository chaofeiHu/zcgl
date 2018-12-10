package com.hz.demo.services;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hz.demo.core.Page;
import com.hz.demo.core.PageData;
import com.hz.demo.dao.DaoSupportImpl;
import com.hz.demo.model.Article;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

@Service("ReprotService")
public class ReprotService {

    @Resource(name = "daoSupportImpl")
    private DaoSupportImpl daoSupport;

    //评审情况统计表  学科组通过率
    public List<PageData> assessmentStatisticsList(PageData pageData) {
        List<PageData> areaList = null;
        try {
            areaList = (List<PageData>) daoSupport.findForList("confing/mappers.reprot.assessmentStatisticsList", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            areaList = null;
        }
        return areaList;
    }
    public List<PageData> assessmentStatisticsLevelList(PageData pageData) {
        List<PageData> areaList = null;
        try {
            areaList = (List<PageData>) daoSupport.findForList("confing/mappers.reprot.assessmentStatisticsLevelList", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            areaList = null;
        }
        return areaList;
    }
    public List<PageData> assessmentLevelList(PageData pageData) {
        List<PageData> areaList = null;
        try {
            areaList = (List<PageData>) daoSupport.findForList("confing/mappers.reprot.assessmentLevelList", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            areaList = null;
        }
        return areaList;
    }
    /** * 合并数据 * @param item */
    public void inite(List<PageData> item,String YEAR_NO){
        for(int i=0;i<item.size();i++){
            PageData pageData=item.get(i);
            pageData.put("YEAR_NO",YEAR_NO);
            List<PageData> list=assessmentStatisticsLevelList(pageData);
            int Aum=0;
            int Az=0;
            int At=0;
            int Bum=0;
            int Bz=0;
            int Bt=0;
            int Cum=0;
            int Cz=0;
            int Ct=0;
            for(PageData pdd:list){
                if(pdd.get("BACKUP2").toString().equals("A")){
                    if(pdd.get("PINGSHENTYPE").toString().equals("11")){ //正常
                        Az+=Integer.valueOf(pdd.get("COU").toString());
                    }else if(pdd.get("PINGSHENTYPE").toString().equals("12")||pdd.get("PINGSHENTYPE").toString().equals("21")){
                        //突破
                        At+=Integer.valueOf(pdd.get("COU").toString());
                    }
                    Aum+=Integer.valueOf(pdd.get("COU").toString());
                }else if(pdd.get("BACKUP2").toString().equals("B")){
                    Bum+=Integer.valueOf(pdd.get("COU").toString());
                    if(pdd.get("PINGSHENTYPE").toString().equals("11")){ //正常
                        Bz+=Integer.valueOf(pdd.get("COU").toString());
                    }else if(pdd.get("PINGSHENTYPE").toString().equals("12")||pdd.get("PINGSHENTYPE").toString().equals("21")){
                        //突破
                        Bt+=Integer.valueOf(pdd.get("COU").toString());
                    }
                }else if(pdd.get("BACKUP2").toString().equals("C")){
                    Cum+=Integer.valueOf(pdd.get("COU").toString());
                    if(pdd.getString("PINGSHENTYPE").equals("11")){ //正常
                        Cz+=Integer.valueOf(pdd.get("COU").toString());
                    }else if(pdd.get("PINGSHENTYPE").toString().equals("12")||pdd.get("PINGSHENTYPE").toString().equals("21")){
                        //突破
                        Ct+=Integer.valueOf(pdd.get("COU").toString());
                    }
                }
            }
            List<PageData> lev=assessmentLevelList(pageData);
            int Taum=0;
            int Taz=0;
            int Tat=0;
            int Tbum=0;
            int Tbz=0;
            int Tbt=0;
            int Tcum=0;
            int Tcz=0;
            int Tct=0;
            for(PageData pdd:lev){
                if(pdd.get("BACKUP2").toString().equals("A")){
                    if(pdd.get("PINGSHENTYPE").toString().equals("11")){ //正常
                        Taz+=Integer.valueOf(pdd.get("COU").toString());
                    }else if(pdd.get("PINGSHENTYPE").toString().equals("12")||pdd.get("PINGSHENTYPE").toString().equals("21")){
                        //突破
                        Tat+=Integer.valueOf(pdd.get("COU").toString());
                    }
                    Taum+=Integer.valueOf(pdd.get("COU").toString());
                }else if(pdd.get("BACKUP2").toString().equals("B")){
                    Tbum+=Integer.valueOf(pdd.get("COU").toString());
                    if(pdd.get("PINGSHENTYPE").toString().equals("11")){ //正常
                        Tbz+=Integer.valueOf(pdd.get("COU").toString());
                    }else if(pdd.get("PINGSHENTYPE").toString().equals("12")||pdd.get("PINGSHENTYPE").toString().equals("21")){
                        //突破
                        Tbt+=Integer.valueOf(pdd.get("COU").toString());
                    }
                }else if(pdd.get("BACKUP2").toString().equals("C")){
                    Tcum+=Integer.valueOf(pdd.get("COU").toString());
                    if(pdd.getString("PINGSHENTYPE").equals("11")){ //正常
                        Tcz+=Integer.valueOf(pdd.get("COU").toString());
                    }else if(pdd.get("PINGSHENTYPE").toString().equals("12")||pdd.get("PINGSHENTYPE").toString().equals("21")){
                        //突破
                        Tct+=Integer.valueOf(pdd.get("COU").toString());
                    }
                }
            }


            item.get(i).put("AUM",Aum);
            item.get(i).put("AZ",Az);
            item.get(i).put("AT",At);
            item.get(i).put("BUM",Bum);
            item.get(i).put("BZ",Bz);
            item.get(i).put("BT",Bt);
            item.get(i).put("CUM",Cum);
            item.get(i).put("CZ",Cz);
            item.get(i).put("CT",Ct);
            item.get(i).put("TAUM",Taum);
            item.get(i).put("TAZ",Taz);
            item.get(i).put("TAT",Tat);
            item.get(i).put("TBUM",Tbum);
            item.get(i).put("TBZ",Tbz);
            item.get(i).put("TBT",Tbt);
            item.get(i).put("TCUM",Tcum);
            item.get(i).put("TCZ",Tcz);
            item.get(i).put("TCT",Tct);
        }
    }

    /*** 申报信息统计 ***/
    public JSONArray declarationInformationList(PageData pageData) throws Exception{
        JSONArray jsonArray=new JSONArray();
        if(pageData.getString("num").equals("1")){ //系列级别
            jsonArray=returnJSONArray("申报标准统计（按系列级别）",
                    "confing/mappers.reprot.selectSerieslevel", pageData);

        }else  if(pageData.getString("num").equals("2")) { //学历级别
            jsonArray=returnJSONArray("申报标准统计（按学历级别）",
                    "confing/mappers.reprot.selectEducationLevel", pageData);

        }else  if(pageData.getString("num").equals("3")) { //单位级别
            jsonArray=returnJSONArray("申报标准统计（按单位级别）",
                    "confing/mappers.reprot.selectCompanyLevel", pageData);

        }else  if(pageData.getString("num").equals("4")) { //单位系列
            jsonArray=returnJSONArray("申报标准统计（按单位系列）",
                    "confing/mappers.reprot.selectCompanySeries", pageData);

        }else  if(pageData.getString("num").equals("5")) { //年龄级别

        }


        return jsonArray;
    }
    public JSONArray returnJSONArray(String title,String jdbc,PageData pageData)throws Exception{
        JSONArray jsonArray=new JSONArray();
        List<PageData>  areaList = (List<PageData>) daoSupport.findForList(jdbc, pageData);
        List<String> xl=new ArrayList<String>();
        List<String> jb =new ArrayList<String>();
        for(PageData pd:areaList){
            xl.add(pd.get("XL").toString());
            jb.add(pd.get("JB").toString());
        }
        JSONObject object=new JSONObject();
        object.put("jb",quChong(jb));
        object.put("xl", quChong(xl));
        object.put("num",areaList);
        object.put("title",title);
        jsonArray.add(object);
        return jsonArray;
    }

    public List<String> quChong(List<String> pageData){
        LinkedHashSet<String> set = new LinkedHashSet<String>(pageData.size());
        set.addAll(pageData);
        pageData.clear();
        pageData.addAll(set);
        return pageData;
    }

    public List<PageData> assessmentInformationlistPage(Page pageData) {
        List<PageData> areaList = null;
        try {
            areaList = (List<PageData>) daoSupport.findForList("confing/mappers.reprot.assessmentInformationlistPage", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            areaList = null;
        }
        return areaList;
    }
}
