package com.hz.demo.core;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.*;
import java.security.*;
import java.security.cert.CertificateException;
import java.text.ParseException;
import java.util.*;

/**
 * Created by wangyong on 2016/11/23.
 */
public class GeneratorTools {
    private String packageName = "com.hz.demo";
    private String pageData = "PageData pageData";
    private String page = "pageData";
    private String pa = ",";

    private String path = "E:\\gongzuo\\code\\src\\main\\java\\com\\hz\\demo";
/**
    private void getService(String bean) {
        String service = bean;
        String className = service;
        StringBuffer sb = new StringBuffer();
        sb.append("package " + packageName + ".services;\n\n");
        sb.append("import " + packageName + ".model." + bean + ";\n");

        sb.append("public interface " + service + "{\n\n");
        sb.append("\tpublic int add"+bean+"(" + bean + " "+bean.toLowerCase() +") ;\n");

        sb.append("\tpublic int del"+bean+"(String fid);\n");
        sb.append("\tpublic int update"+bean+"(" + bean + " "+bean.toLowerCase() +");\n");
        sb.append("\tpublic "+bean +" get"+bean+"forId(String fid);\n");
        sb.append("\t}\n");
        outputToFile(path+"\\service\\"+className+".java", sb.toString());
        getServiceImpl(bean);
    }
*/
    private void getServiceImpl(String bean) {
            String service = bean + "Service";
            String stradd=""+bean+"Mapper.add"+bean+"";
            String str1delete=""+bean+"Mapper.delete"+bean+"ByFid";
            String str1update=""+bean+"Mapper.update"+bean+"";
            String str1select=""+bean+"Mapper.getAll"+bean+"";
            String str1select1=""+bean+"Mapper.select"+bean+"List";
            String str1select2=""+bean+"Mapper.get"+bean+"ForId";
            StringBuffer sb = new StringBuffer();
            sb.append("package " + packageName + ".services;\n\n");
            sb.append("import " + packageName + ".core.PageData;\n\n");
            sb.append("import " + packageName + ".model."+bean+";\n\n");
            sb.append("import " + "javax.annotation.Resource" + ";\n");
            sb.append("import " + packageName + ".dao.DaoSupportImpl"+";\n");
            sb.append("import org.springframework.stereotype.Service;\n");
            sb.append("import org.apache.commons.logging.Log;\n");
            sb.append("import org.apache.commons.logging.LogFactory;\n");

            sb.append("import java.util.List;\n");
            sb.append("@Service(\""+bean.toLowerCase()+"\")\n");
            sb.append("public class " + service +" {\n\n");
            sb.append("protected final Log logger = LogFactory.getLog(getClass());\n\n");
            sb.append("\t@Resource(name = \"daoSupportImpl\")\n\n");
            sb.append("\tprivate DaoSupportImpl daoSupport; \n\n");

            sb.append("\tpublic int add"+bean+"(" +pageData+ ") {\n");
            sb.append("\t logger.info(\"增加"+bean+"\");\n");
            sb.append("\t int iFlag =0; \n");
            sb.append("\t try { \n");
            sb.append("\t\tiFlag = (int) daoSupport.insert(\""+stradd+"\""+pa+""+page+");\n");
            sb.append("\t }catch (Exception e){ \n");
            sb.append("\t e.printStackTrace(); \n");
            sb.append("\t iFlag=0; \n");
            sb.append("\t}\n");
            sb.append("\t return iFlag; \n");
            sb.append("\t}\n");


            sb.append("\tpublic int del"+bean+"ByFid(String fid) {\n");
            sb.append("\t logger.info(\"删除"+bean+"\");\n");
            sb.append("\t int iFlag =0; \n");
            sb.append("\t try { \n");
            sb.append("\t\tiFlag = (int) daoSupport.delete(\""+str1delete+"\""+pa+"fid);\n");
            sb.append("\t }catch (Exception e){ \n");
            sb.append("\t e.printStackTrace(); \n");
            sb.append("\t iFlag=0; \n");
            sb.append("\t}\n");
            sb.append("\t return iFlag; \n");
            sb.append("\t}\n");

            sb.append("\tpublic int update"+bean+"(" + pageData + ") {\n");
            sb.append("\t logger.info(\"修改"+bean+"\");\n");
            sb.append("\t int iFlag =0; \n");
            sb.append("\t try { \n");
            sb.append("\t\tiFlag = (int) daoSupport.update(\""+str1update+"\""+pa+""+page+");\n");
            sb.append("\t }catch (Exception e){ \n");
            sb.append("\t e.printStackTrace(); \n");
            sb.append("\t iFlag=0; \n");
            sb.append("\t}\n");
            sb.append("\t return iFlag; \n");
            sb.append("\t}\n");


            sb.append("\tpublic "+bean +" get"+bean+"forId(String fid) {\n");
            sb.append("\t logger.info(\"通过ID查询"+bean+"\");\n");
            sb.append("\t"+bean+"\t"+bean.toLowerCase()+"=null;\n");
            sb.append("\t try { \n");
            sb.append("\t\t"+bean.toLowerCase()+" = ("+bean+") daoSupport.findForObject(\""+str1select2+"\""+pa+"fid);\n");
            sb.append("\t }catch (Exception e){ \n");
            sb.append("\t e.printStackTrace(); \n");
            sb.append("\t "+bean.toLowerCase()+"=null; \n");
            sb.append("\t}\n");
            sb.append("\t return "+bean.toLowerCase()+"; \n");
            sb.append("\t}\n");


            sb.append("\tpublic List<"+bean +"> getAll"+bean+"(" + pageData + ") {\n");
            sb.append("\t logger.info(\"分页查询"+bean+"\");\n");
            sb.append("\tList<"+bean+">\t"+bean.toLowerCase()+"List=null;\n");
            sb.append("\t try { \n");
            sb.append("\t\t"+bean.toLowerCase()+"List = (List<"+bean+">) daoSupport.findForList(\""+str1select+"\""+pa+""+page+");\n");
            sb.append("\t }catch (Exception e){ \n");
            sb.append("\t e.printStackTrace(); \n");
            sb.append("\t "+bean.toLowerCase()+"List=null; \n");
            sb.append("\t}\n");
            sb.append("\t return "+bean.toLowerCase()+"List; \n");
            sb.append("\t}\n");



            sb.append("\tpublic List<"+bean +"> select"+bean+"List() {\n");
            sb.append("\t logger.info(\"查询所有"+bean+"\");\n");
            sb.append("\tList<"+bean+">\t"+bean.toLowerCase()+"List=null;\n");
            sb.append("\t try { \n");
            sb.append("\t\t"+bean.toLowerCase()+"List = (List<"+bean+">) daoSupport.findForList(\""+str1select1+"\""+pa+"null);\n");
            sb.append("\t }catch (Exception e){ \n");
            sb.append("\t e.printStackTrace(); \n");
            sb.append("\t "+bean.toLowerCase()+"List=null; \n");
            sb.append("\t}\n");
            sb.append("\t return "+bean.toLowerCase()+"List; \n");
            sb.append("\t}\n");
            sb.append("}\n");
        System.out.print(path+"\\services\\"+service+".java");
        outputToFile(path+"\\services\\"+service+".java", sb.toString());
    }


    private void outputToFile(String fileName, String content) {
        OutputStream os = null;
        try {
            os = new FileOutputStream(fileName);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
        byte[] b = content.getBytes();
        try {
            os.write(b);
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] agrs) throws ParseException, KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException, KeyManagementException {
        GeneratorTools generator= new GeneratorTools();
        generator.getServiceImpl("BaseUnit");
        String str="0|1|2";
        System.out.println(str.split("\\|")[2]);
    }
}
