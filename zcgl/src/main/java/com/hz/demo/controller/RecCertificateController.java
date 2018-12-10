package com.hz.demo.controller;

import com.hz.demo.core.*;
import com.hz.demo.model.BaseStamp;
import com.hz.demo.model.rec_certificate;
import com.hz.demo.model.rec_result;
import com.hz.demo.model.shenbao.ProposerMsg;
import com.hz.demo.services.*;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

import static com.hz.demo.core.ResultUtils.toDateTimeJson;

//证书管理页面用
@Controller
@RequestMapping("/RecCertificate")
public class RecCertificateController extends BaseController {

    @Autowired
    UserService userService;
    @Autowired
    RecCertificateService recCertificateService;
    @Autowired
    RecResultService recResultService;
    @Autowired
    ProposerAllMsgServices proposerAllMsgServices;
    @Autowired
    BaseUnitService baseUnitService;
    @Autowired
    BaseStampService baseStampService;

    //跳到证书编号生成界面
    @RequestMapping(value = "CreateRecCertificatePage")
    public String CreateRecCertificatePage(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("MenuBtns", userService.GetMenuBtns(request.getParameter("menuid"), getUser().getUserId()));
        return "sys/recCertificateCreateNumber";
    }

    //获取界面列表
    @RequestMapping(value = "getCreateList")
    public void getCreateList(HttpServletRequest request, HttpServletResponse response) {
        logger.info("显示评审结果列表数据");
        PageData pd = new PageData();
        pd.put("rows", request.getParameter("rows") == null ? 10 : Integer.parseInt(request.getParameter("rows")));//每页显示行数
        pd.put("page", request.getParameter("page") == null ? 1 : (Integer.parseInt(request.getParameter("page"))));//每页记录开始位置
        pd.put("userName", request.getParameter("userName"));
        pd.put("yearNo", request.getParameter("yearNo"));
        pd.put("judgingId", request.getParameter("judgingCode"));
        pd.put("unitCode", request.getParameter("unitCode"));
        pd.put("reviewSeries", request.getParameter("reviewSeries"));
        pd.put("professialCode", request.getParameter("professialCode"));
        pd.put("hasCertificate", request.getParameter("hasCertificate"));
        pd.put("manageUnitCode", getUser().getUnitCode());
        TableReturn tablereturn = new TableReturn();
        List<rec_result> blist = recResultService.getListCertificate(pd);
        Integer listCount = recResultService.getListCountCertificate(pd);
        tablereturn.setRows(blist);
        tablereturn.setTotal(listCount);
        ResultUtils.write(response, toDateTimeJson(tablereturn));
    }

    //跳到证书管理界面
    @RequestMapping(value = "RecCertificatePage")
    public String RecCertificatePage(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("MenuBtns", userService.GetMenuBtns(request.getParameter("menuid"), getUser().getUserId()));
        return "sys/recCertificateCreateFile";
    }

    //获取界面列表
    @RequestMapping(value = "getList")
    public void getList(HttpServletRequest request, HttpServletResponse response) {
        logger.info("显示评审结果列表数据");
        PageData pd = new PageData();
        pd.put("rows", request.getParameter("rows") == null ? 10 : Integer.parseInt(request.getParameter("rows")));//每页显示行数
        pd.put("page", request.getParameter("page") == null ? 1 : (Integer.parseInt(request.getParameter("page"))));//每页记录开始位置
        pd.put("userName", request.getParameter("userName"));
        pd.put("yearNo", request.getParameter("yearNo"));
        pd.put("judgingId", request.getParameter("judgingCode"));
        pd.put("unitCode", request.getParameter("unitCode"));
        pd.put("reviewSeries", request.getParameter("reviewSeries"));
        pd.put("hasCertificate", "1");
        pd.put("hasCertificatePath", request.getParameter("hasCertificatePath"));
        pd.put("professialCode", request.getParameter("professialCode"));
        pd.put("manageUnitCode", getUser().getUnitCode());
        TableReturn tablereturn = new TableReturn();
        List<rec_result> blist = recResultService.getListCertificate(pd);
        Integer listCount = recResultService.getListCountCertificate(pd);
        tablereturn.setRows(blist);
        tablereturn.setTotal(listCount);
        ResultUtils.write(response, toDateTimeJson(tablereturn));
    }

    //生成证书编号
    @Transactional(propagation = Propagation.REQUIRED)
    @RequestMapping(value = "confirmResult")
    public void confirmResult(HttpServletRequest request, HttpServletResponse response) {
        String ids = request.getParameter("ids") == null ? "" : request.getParameter("ids");
        String[] strs = ids.split(",");
        try {
            for (String id : strs) {
                Integer id1 = Integer.parseInt(id);//评审结果id
                int flag = 0;
                BigDecimal bid = new BigDecimal(id1);
                rec_result recResult = recResultService.getModel(bid);
                flag = recCertificateService.createCertificateNumber(recResult);

                if (flag == 1) {
                    ResultUtils.writeMessage(response, 1, "生成成功");
                } else {
                    ResultUtils.writeMessage(response, 0, "生成失败");
                    throw new RuntimeException();
                }
            }
        } catch (Exception ex) {
            ResultUtils.writeMessage(response, 0, "生成失败");
            throw new RuntimeException();
        }
    }

    //生成证书
    @Transactional(propagation = Propagation.REQUIRED)
    @RequestMapping(value = "shengChengCertificate")
    public void shengChengCertificate(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");

        try {
            Integer id1 = Integer.parseInt(id);//评审结果id
            int flag = 0;
            BigDecimal bid = new BigDecimal(id1);
            rec_result recResult = recResultService.getModelXiang(bid);
            rec_certificate recCertificate = recCertificateService.getModelByResultid(bid);
            String oldPdfFileName = null;
            String oldPdfPath = request.getSession().getServletContext().getRealPath("/") + "upload/zcgl/createpdf/";

            if (!StringUtils.isEmpty(recCertificate.getCertificatePath())) {
                String certificatePath = recCertificate.getCertificatePath();
                oldPdfFileName = certificatePath.substring(certificatePath.lastIndexOf("/") + 1);
            }


            //                生成二维码
            String content = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/PreviewCertificatePdf/getCertificate/" + recCertificate.getCertificateNumber() + "/" + recResult.getIdCardNo();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String codePath = request.getSession().getServletContext().getRealPath("/") + "upload/zcgl/certificate/";
            String codeFileName = recResult.getUserName() + sdf.format(new Date()) + ".png";
            String filePath = codePath + codeFileName;
            CreateCode.createCode("png", content, filePath, 300, 300);

            //                生成pdf开始
            PageData pd = new PageData();
            pd.put("userid", recResult.getUserId());
            ProposerMsg proposerMsg = proposerAllMsgServices.selectUserMgr(pd);
            String picurlPath = request.getSession().getServletContext().getRealPath("/") + proposerMsg.getUserpicurl();
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("xm", recResult.getUserName());//姓名
            data.put("xb", recResult.getSex());//性别
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月dd日");
            String nowDate = sdf1.format(new Date());
            String idCardNo = recResult.getIdCardNo();
            String brithday = idCardNo.substring(6, 10) + "年" + idCardNo.substring(10, 12) + "月" + idCardNo.substring(12, 14) + "日";


            data.put("csny", brithday);//出生日期
            data.put("zc", recResult.getPositionalTitles());//职称
            data.put("zymc", recResult.getProfessialCode());//专业名称
            data.put("zcjb", recResult.getTitleLevel());//取得职称级别
            data.put("zgdwmc", recResult.getUnitCode());//主管单位名称
            data.put("pwhmc", recResult.getJudgingCode());//评委会名称
            data.put("zsbh", recCertificate.getCertificateNumber());//证书编号
            data.put("pstgsj", sdf1.format(recResult.getAddtime()));//评审通过时间


            PageData pdUnit = new PageData();
            pdUnit.put("unitCode", getUser().getUnitCode());
            data.put("fzdw", baseUnitService.getModelWhere(pdUnit).getUnitName());//发证单位
            pdUnit.put("unitCode", proposerMsg.getBaseProposer().getDeclareUnitcode());
            data.put("gzdwmc", baseUnitService.getModelWhere(pdUnit).getUnitName());//工作单位名称

            String zp = picurlPath;//照片
            String ewm = filePath;//二维码
            String pdfurl = "upload/zcgl/createpdf/" + recResult.getUserName() + sdf.format(new Date()) + ".pdf";

            String result = "0";
            File file = new File(zp);
            if (file.exists()) {
                result = createPdf(request.getSession().getServletContext().getRealPath("/"), data, zp, ewm, pdfurl);
                DeleteFile.delOneFile(codePath, codeFileName.substring(0, codeFileName.indexOf(".")));//生成pdf后删除二维码
            } else {
                result = "缺失照片";
            }

            //            -------生成pdf结束
            if (result.equals("1")) {
                PageData ppd = new PageData();
                ppd.put("id", recCertificate.getId());
                ppd.put("certificatePath", pdfurl);
                SimpleDateFormat sss = new SimpleDateFormat("yyyy-MM-dd");
//                ppd.put("addtime", sss.format(new Date()));
                ppd.put("back1", "0");
                recCertificateService.update(ppd);
                if (oldPdfFileName != null) {
                    DeleteFile.delOneFile(oldPdfPath, oldPdfFileName.substring(0, oldPdfFileName.indexOf(".")));//更新地址后删除旧pdf
                }
                PageData pddResult = new PageData();
                pddResult.put("id", bid);
                pddResult.put("isbuildcertificate", "1");
                pddResult.put("gettime", sss.format(new Date()));
                flag = recResultService.update(pddResult);

                if (flag == 1) {
                    ResultUtils.writeMessage(response, 1, "生成成功");
                } else {
                    ResultUtils.writeMessage(response, 0, "生成失败");
                    throw new RuntimeException();
                }
            } else if (result.equals("缺失照片")) {
                ResultUtils.writeMessage(response, 0, "缺失照片文件");
            } else {
                ResultUtils.writeMessage(response, 0, "生成失败");
            }
        } catch (Exception ex) {
            ResultUtils.writeMessage(response, 0, "生成失败");
            ex.printStackTrace();
            throw new RuntimeException();
        }
    }

    /**
     * 生成pdf
     */
    public String createPdf(String path, Map<String, Object> data, String zp, String ewm, String pdfurl) {
        String pdfFilePath = path + "/static/certificateModel/证书模板.pdf";
        String result = "0";
        FileOutputStream out = null;
        PdfStamper ps = null;
        PdfReader reader = null;
        ByteArrayOutputStream bos;
        try {
            File file = new File(path + pdfurl);

            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            out = new FileOutputStream(file);
            // 2 读入pdf表单
            reader = new PdfReader(pdfFilePath);
            // 3 根据表单生成一个新的pdf
            bos = new ByteArrayOutputStream();
            ps = new PdfStamper(reader, bos);
            // 4 获取pdf表单
            AcroFields form = ps.getAcroFields();
            // 5给表单添加字体
            BaseFont bf = BaseFont.createFont(path + "static/certificateModel/微软雅黑.ttf",
                    BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            ArrayList<BaseFont> baseFonts = new ArrayList<>();
            baseFonts.add(bf);
            form.setSubstitutionFonts(baseFonts);

            // 7遍历data 给pdf表单表格赋值
           /* for (String key : data.keySet()) {
                form.setFieldProperty(key, "textfont", bf, null);
                form.setField(key, data.get(key).toString());
            }*/
            ps.setFormFlattening(true);

            //-----------------------------pdf 添加图片----------------------------------
            // 通过域名获取所在页和坐标，左下角为起点
            System.out.println("pdf 添加图片");
            //----------照片
            String imgpath = zp;
            int pageNo = form.getFieldPositions("zp").get(0).page;
            Rectangle signRect = form.getFieldPositions("zp").get(0).position;
            float x = signRect.getLeft();
            float y = signRect.getBottom();
            // 读图片
            Image image = Image.getInstance(imgpath);
            // 获取操作的页面
            PdfContentByte under = ps.getOverContent(pageNo);
            // 根据域的大小缩放图片
            image.scaleToFit(signRect.getWidth(), signRect.getHeight());
            // 添加图片
            image.setAbsolutePosition(x, y);
            under.addImage(image);
            //-------------二维码
            String imgpathewm = ewm;
            int pageNo2 = form.getFieldPositions("ewm").get(0).page;
            Rectangle signRect2 = form.getFieldPositions("ewm").get(0).position;
            float x2 = signRect2.getLeft();
            float y2 = signRect2.getBottom();
            Image image2 = Image.getInstance(imgpathewm);
            PdfContentByte under2 = ps.getOverContent(pageNo2);
            image2.scaleToFit(signRect2.getWidth(), signRect2.getHeight());
            image2.setAbsolutePosition(x2, y2+16);
            under2.addImage(image2);

//            添加文字
            under = ps.getOverContent(form.getFieldPositions("ewm").get(0).page);
            under.beginText();
//用来设置文字的位置和字体---开始
            under.setColorFill(BaseColor.BLACK);
            under.setFontAndSize(bf, 14);
            PdfGState gs = new PdfGState();
            under.setGState(gs);
//用来设置文字的位置和字体---结束
//插入文字
            under.setTextMatrix(210, 581);
            under.newlineShowText(data.get("xm") + "");//姓名
            int jianY = 34;
            under.setTextMatrix(210, 581 - jianY);
            under.newlineShowText(data.get("xb") + "");//性别
            under.setTextMatrix(210, 581 - jianY * 2);
            under.newlineShowText(data.get("csny") + "");//出生年月
            under.setTextMatrix(210, 581 - jianY * 3);
            under.newlineShowText(data.get("zc") + "");//职称名称
            under.setTextMatrix(210, 581 - jianY * 4);
            under.newlineShowText(data.get("zcjb") + "");//职称级别
            under.setTextMatrix(210, 581 - jianY * 5);
            under.newlineShowText(data.get("zymc") + "");//从事专业
            under.setTextMatrix(210, 581 - jianY * 6 + 1);
            under.newlineShowText(data.get("pstgsj") + "");//评审通过时间
            under.setTextMatrix(210, 581 - jianY * 7 + 1);
            under.newlineShowText(data.get("gzdwmc") + "");//工作单位名称
            under.setTextMatrix(210, 581 - jianY * 8 + 2);
            under.newlineShowText(data.get("pwhmc") + "");//评审单位名称
            under.setTextMatrix(210, 581 - jianY * 9 + 3);
            under.newlineShowText(data.get("zsbh") + "");//证书编号
            under.setTextMatrix(399, 159);
            under.newlineShowText(data.get("fzdw") + "");//发证单位
//插入文字--结束
            under.endText();
            ps.close();
            //-------------------------------------------------------------
            Document doc = new Document();
            PdfCopy copy = new PdfCopy(doc, out);
            doc.open();
            PdfImportedPage importPage = copy.getImportedPage(new PdfReader(bos.toByteArray()), 1);
            copy.addPage(importPage);
            doc.close();
            result = "1";
            System.out.println("===============PDF导出成功=============");
        } catch (Exception e) {
            System.out.println("===============PDF导出失败=============");
            result = "0";
            e.printStackTrace();
        }
        return result;
    }

    //为证书盖章
    @Transactional(propagation = Propagation.REQUIRED)
    @RequestMapping(value = "stampOn")
    public void stampOn(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

        try {
            PageData pd = new PageData();
            pd.put("unitCode", getUser().getUnitCode());
            Map<String, Object> model = baseStampService.selectBaseStampModel(pd);

            if(model.get("FILEURL")!=null&&!"".equals(model.get("FILEURL"))){//存在章
                Integer id1 = Integer.parseInt(id);//评审结果id
                int flag = 0;
                BigDecimal bid = new BigDecimal(id1);
                rec_result recResult = recResultService.getModelXiang(bid);
                rec_certificate recCertificate = recCertificateService.getModelByResultid(bid);
                String oldPdfFileName = null;
                String oldPdfPath = request.getSession().getServletContext().getRealPath("/") + "upload/zcgl/createpdf/";
                String certificatePath = recCertificate.getCertificatePath();//原pdf
                //                生成pdf开始

                String zhang = request.getSession().getServletContext().getRealPath(model.get("FILEURL")+"")  ;
                String pdfurl = "upload/zcgl/createpdf/" + recResult.getUserName() + sdf.format(new Date()) + ".pdf";

                String result = "0";
                result = stampOnPdf(request.getSession().getServletContext().getRealPath("/"), zhang, certificatePath, pdfurl);

                //            -------生成pdf结束
                if (result.equals("1")) {
                    PageData ppd = new PageData();
                    ppd.put("id", recCertificate.getId());
                    ppd.put("certificatePath", pdfurl);
                    ppd.put("back1", "1");
//                    SimpleDateFormat sss = new SimpleDateFormat("yyyy-MM-dd");
//                    ppd.put("addtime", sss.format(new Date()));
                    recCertificateService.update(ppd);
                    if (oldPdfFileName != null) {
                        DeleteFile.delOneFile(oldPdfPath, oldPdfFileName.substring(0, oldPdfFileName.indexOf(".")));//更新地址后删除旧pdf
                    }
                    PageData pddResult = new PageData();
                    pddResult.put("id", bid);
                    pddResult.put("isbuildcertificate", "1");
                    flag = recResultService.update(pddResult);

                    if (flag == 1) {
                        ResultUtils.writeMessage(response, 1, "生成成功");
                    } else {
                        ResultUtils.writeMessage(response, 0, "生成失败");
                        throw new RuntimeException();
                    }
                } else if (result.equals("缺失印章文件")) {
                    ResultUtils.writeMessage(response, 0, "缺失印章文件");
                } else {
                    ResultUtils.writeMessage(response, 0, "生成失败");
                }
            }else {
                ResultUtils.writeMessage(response, 0, "缺失印章文件");
            }
        } catch (Exception ex) {
            ResultUtils.writeMessage(response, 0, "生成失败");
            ex.printStackTrace();
            throw new RuntimeException();
        }
    }


    /**
     * 生成pdf 盖章
     */
    public String stampOnPdf(String path, String zhang, String oldPdfurl, String pdfurl) {
        String pdfFilePath = path + oldPdfurl;
        String result = "0";
        FileOutputStream out = null;
        PdfStamper ps = null;
        PdfReader reader = null;
        ByteArrayOutputStream bos;
        try {
            File file = new File(path + pdfurl);

            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            out = new FileOutputStream(file);
            // 2 读入pdf表单
            reader = new PdfReader(pdfFilePath);
            // 3 根据表单生成一个新的pdf
            bos = new ByteArrayOutputStream();
            ps = new PdfStamper(reader, bos);
            // 4 获取pdf表单
            AcroFields form = ps.getAcroFields();
            ps.setFormFlattening(true);



            BaseFont bf = BaseFont.createFont(path + "static/certificateModel/微软雅黑.ttf",
                    BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            PdfContentByte under = ps.getOverContent(1);
            //            添加文字
            under.beginText();
//用来设置文字的位置和字体---开始
            under.setColorFill(BaseColor.BLACK);
            under.setFontAndSize(bf, 14);
            PdfGState gs = new PdfGState();
            under.setGState(gs);
//用来设置文字的位置和字体---结束

            PageData pdUnit = new PageData();
            pdUnit.put("unitCode", getUser().getUnitCode());
            under.setTextMatrix(399, 159);
            under.newlineShowText(baseUnitService.getModelWhere(pdUnit).getUnitName());//发证单位
//插入文字--结束
            under.endText();



            //-----------------------------pdf 添加图片----------------------------------
            // 通过域名获取所在页和坐标，左下角为起点
            System.out.println("pdf 添加图片");
            //----------章
            PdfGState gs1=new PdfGState();
            gs1.setFillOpacity(0.8f);
            String imgpath = zhang;

            float x = 390;
            float y = 115;
            // 读图片
            Image image = Image.getInstance(imgpath);
            // 获取操作的页面

            under.setGState(gs1);
            // 缩放图片
            image.scaleToFit(100, 100);
            // 添加图片
            image.setAbsolutePosition(x, y);
            under.addImage(image);
//-------------------------------------------------------------
            ps.close();

            Document doc = new Document();
            PdfCopy copy = new PdfCopy(doc, out);
            doc.open();
            PdfImportedPage importPage = copy.getImportedPage(new PdfReader(bos.toByteArray()), 1);
            copy.addPage(importPage);
            doc.close();
            result = "1";
            System.out.println("===============PDF导出成功=============");
        } catch (Exception e) {
            System.out.println("===============PDF导出失败=============");
            result = "0";
            e.printStackTrace();
        }
        return result;
    }


}
