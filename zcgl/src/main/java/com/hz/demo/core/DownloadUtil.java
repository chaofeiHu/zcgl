package com.hz.demo.core;


import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.StreamOpenOfficeDocumentConverter;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.ConnectException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

import static com.hz.demo.core.EncodeUtil.encodeMD5;
import static com.hz.demo.core.ResultUtils.toJson;


public class DownloadUtil {

    /**
     * 从服务器中下载图片
     *
     * @param request
     * @param response
     * @return
     */
    public static void downloadMedia(HttpServletResponse response, HttpServletRequest request) {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            //处理中文乱码
            request.setCharacterEncoding("UTF-8");
            String fileName = request.getParameter("fileName");
//            fileName = new String(fileName.getBytes("iso8859-1"),"UTF-8");
            //处理浏览器兼容
            response.setContentType("application/msexcel;charset=utf-8");//定义输出类型
            Enumeration enumeration = request.getHeaders("User-Agent");
            String browserName = (String) enumeration.nextElement();
            boolean isMSIE = browserName.contains("MSIE");
            if (isMSIE) {
                String f = fileName.substring(fileName.lastIndexOf("/") + 14);
                response.addHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(f, "UTF8"));
            } else {
                //设置下载名
                String f = fileName.substring(fileName.lastIndexOf("/") + 14);
                response.addHeader("Content-Disposition", "attachment;fileName=" + new String(f.getBytes("gb2312"), "ISO8859-1"));
            }
            //url地址如果存在空格，会导致报错！  解决方法为：用+或者%20代替url参数中的空格。
            fileName = fileName.replace(" ", "%20");
            //解决url有中文情况
            fileName = ChineseUtil.opChinese(fileName);
            //图片下载（需是完整协议端口地址如http://localhost:8080/upload/work/152826909948494cad1c8a786c9179e80a80cc23d70cf3bc75700.jpg）
            URL url = new URL(fileName);
            URLConnection conn = url.openConnection();
            outputStream = response.getOutputStream();
            inputStream = conn.getInputStream();
            IOUtils.copy(inputStream, outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(outputStream);
        }
    }

    /**
     * 预览图片及word文档
     * @param response
     * @param request
     */
    public static void previewFile(HttpServletResponse response, HttpServletRequest request) {
        String fileName = request.getParameter("fileName");

        if(fileName!=null) {
            String hou = fileName.substring(fileName.lastIndexOf(".") + 1);
            if ("jpg".equals(hou) || "jpeg".equals(hou) || "gif".equals(hou) || "png".equals(hou) || "bmp".equals(hou)|| "pdf".equals(hou)) {
                PageData pd = new PageData();
                pd.put("previewFile", fileName);
                ResultUtils.write(response, toJson(pd));
            } else if ("doc".equals(hou) || "docx".equals(hou)) {
                /*String path = request.getSession().getServletContext().getRealPath("");
                String fileurl = path + fileName;
                String s=null;
                try {
                     s = new Doc2HtmlUtil().file2pdf(new FileInputStream(fileurl), fileurl.substring(0, fileurl.lastIndexOf("/")), hou);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                response.reset();*/
                PageData pd = new PageData();
                pd.put("previewFile", fileName.substring(0, fileName.lastIndexOf(".doc")) + ".pdf");
                ResultUtils.write(response, toJson(pd));
                /*PageData pd=new PageData();
                pd.put("previewFile",fileName.substring(0,fileName.lastIndexOf("/")+1)+s);
                ResultUtils.write(response, toJson(pd));*/
            }

        }

    }

    /**
     * 预览图片及word文档(遮挡)
     * @param response
     * @param request
     */
    public static void previewFileZhe(HttpServletResponse response, HttpServletRequest request) {
        String fileName = request.getParameter("fileName");
        String idCardNo = request.getParameter("idCardNo");
        String jiaMi=encodeMD5(idCardNo).substring(0,5);
        if(fileName!=null) {
            String hou = fileName.substring(fileName.lastIndexOf(".") + 1);
            if ("jpg".equals(hou) || "jpeg".equals(hou) || "gif".equals(hou) || "png".equals(hou) || "bmp".equals(hou)|| "pdf".equals(hou)) {
                PageData pd = new PageData();
                pd.put("previewFile", fileName.substring(0,fileName.lastIndexOf("."+hou))+jiaMi+"."+hou);
                ResultUtils.write(response, toJson(pd));
            } else if ("doc".equals(hou) || "docx".equals(hou)) {
                PageData pd = new PageData();
                pd.put("previewFile", fileName.substring(0, fileName.lastIndexOf(".doc")) +jiaMi+ ".pdf");
                ResultUtils.write(response, toJson(pd));
            }

        }

    }
}
