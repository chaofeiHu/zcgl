package com.hz.demo.services;


import com.hz.demo.core.PageData;
import com.hz.demo.dao.DaoSupportImpl;
import com.hz.demo.model.rec_certificate;
import com.hz.demo.model.rec_result;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@Service("RecCertificateService")
public class RecCertificateService {

    @Resource(name = "daoSupportImpl")
    private DaoSupportImpl daoSupport;

    //添加信息
    public int add(rec_certificate model) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.insert("confing/mappers.rec_certificate.insertSelective", model);

        } catch (Exception e) {
            e.printStackTrace();
            iFlag = 0;
        }
        return iFlag;
    }

    // 根据ID更新通过人员信息
    public int update(PageData pageData) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.update("confing/mappers.rec_certificate.updateByPrimaryKeySelective", pageData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }

    //根据ID删除通过人员信息
    public int delete(BigDecimal id) {
        int iFlag = 0;
        try {
            iFlag = (int) daoSupport.delete("confing/mappers.rec_certificate.deleteByPrimaryKey", id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }

    //根据ID查询通过人员返回实体信息
    public rec_certificate getModel(BigDecimal id) {
        rec_certificate recResult = null;
        try {
            recResult = (rec_certificate) daoSupport.findForObject("confing/mappers.rec_certificate.selectByPrimaryKey", id);
        } catch (Exception e) {
            e.printStackTrace();
            recResult = null;
        }
        return recResult;
    }

    //分页查询所有的信息
    public List<rec_certificate> getList(PageData pageData) {
        List<rec_certificate> recResultList = null;
        try {
            recResultList = (List<rec_certificate>) daoSupport.findForList("confing/mappers.rec_certificate.selectList", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            recResultList = null;
        }
        return recResultList;
    }

    //查询对应的记录总数
    public Integer getListCount(PageData pageData) {
        Integer ListCount = 0;
        try {
            ListCount = (Integer) daoSupport.findForObject("confing/mappers.rec_certificate.selectCount", pageData);
        } catch (Exception e) {
            e.printStackTrace();
            ListCount = 0;
        }
        return ListCount;
    }

    // 根据评审结果生成证书编号并新增纪录
    public int createCertificateNumber(rec_result recResult) {
        int iFlag = 0;
        PageData pageData = new PageData();
        pageData.put("resultid", recResult.getId());
       pageData.put("certificateNumber", createNumber(recResult));
        try {
            iFlag = (int) daoSupport.insert("confing/mappers.rec_certificate.insertSelective", pageData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iFlag;
    }

    //    生成证书编号
    public String createNumber(rec_result recResult) {
        String titleLevel = recResult.getTitleLevel();
        String yearNo = recResult.getYearNo();
        String reviewSeries = recResult.getReviewSeries().substring(0, 2);
        String dishi = "";
        Integer liuShui=getSeqByLevel(titleLevel);
        String liuShuiHao="";
        switch (titleLevel) {
            case "A":
                 liuShuiHao = String.format("%04d", liuShui);
                dishi = recResult.getAreaCode().substring(2, 4);
                break;
            case "B":
                liuShuiHao = String.format("%05d", liuShui);
                dishi = recResult.getAreaCode().substring(2, 4);
                break;
            case "C":
                liuShuiHao = String.format("%05d", liuShui);
                dishi = recResult.getUnitCode().substring(2);
                break;
            case "D":
                liuShuiHao = String.format("%05d", liuShui);
                dishi = recResult.getUnitCode().substring(2);
                break;
            case "E":
                liuShuiHao = String.format("%05d", liuShui);
                dishi = recResult.getUnitCode().substring(2);
                break;
            default:
                break;
        }
        String certificateNumber=titleLevel+yearNo+reviewSeries+dishi+liuShuiHao;

        return certificateNumber;


    }


    //查询对应的序列
    public Integer getSeqByLevel(String level) {
        Integer xuLie = 0;
        try {
            switch (level) {
                case "A":
                    xuLie = (Integer) daoSupport.findForObject("confing/mappers.rec_certificate.selectSeqZhengGao", null);
                    break;
                case "B":
                    xuLie = (Integer) daoSupport.findForObject("confing/mappers.rec_certificate.selectSeqFuGao", null);
                    break;
                case "C":
                    xuLie = (Integer) daoSupport.findForObject("confing/mappers.rec_certificate.selectSeqZhong", null);
                    break;
                case "D":
                    xuLie = (Integer) daoSupport.findForObject("confing/mappers.rec_certificate.selectSeqZhuLi", null);
                    break;
                case "E":
                    xuLie = (Integer) daoSupport.findForObject("confing/mappers.rec_certificate.selectSeqYuan", null);
                    break;
                default:
                    break;

            }
        } catch (Exception e) {
            e.printStackTrace();
            xuLie = 0;
        }
        return xuLie;
    }

    //根据ID查询通过人员返回实体信息
    public rec_certificate getModelByResultid(BigDecimal id) {
        rec_certificate recResult = null;
        try {
            recResult = (rec_certificate) daoSupport.findForObject("confing/mappers.rec_certificate.getModelByResultid", id);
        } catch (Exception e) {
            e.printStackTrace();
            recResult = null;
        }
        return recResult;
    }
}
