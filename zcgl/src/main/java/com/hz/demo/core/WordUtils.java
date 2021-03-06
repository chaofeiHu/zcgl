package com.hz.demo.core;

/**
 * Created by xym on 2018-11-08.
 */
import java.io.*;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class WordUtils {
    //配置信息,代码本身写的还是很可读的,就不过多注解了
    private static Configuration configuration = null;
    //这里注意的是利用WordUtils的类加载器动态获得模板文件的位置
    //private static final String templateFolder = WordUtils.class.getClassLoader().getResource("../../").getPath() + "WEB-INF/templetes/";
    private static final String templateFolder = "E:\\gzuo\\code\\src\\main\\java\\com\\hz\\demo\\core";
    private static Template freemarkerTemplate = null;
    static {
        configuration = new Configuration();
        configuration.setDefaultEncoding("utf-8");
        try {
            configuration.setDirectoryForTemplateLoading(new File(templateFolder));
            freemarkerTemplate = configuration.getTemplate("zcglxt.ftl");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private WordUtils() {
        throw new AssertionError();
    }

    public static void exportMillCertificateWord(HttpServletRequest request, HttpServletResponse response, Map map,String title) throws IOException {
        //Template freemarkerTemplate = configuration.getTemplate(ftlFile);
        File file = null;
        InputStream fin = null;
        ServletOutputStream out = null;
        //Writer out = null;
        try {
            // 调用工具类的createDoc方法生成Word文档
            file = createDoc(map,title);
            fin = new FileInputStream(file);

            response.setCharacterEncoding("utf-8");
            response.setContentType("application/msword");
            // 设置浏览器以下载的方式处理该文件名
            //String fileName = title + (new Date().getTime()) + ".doc";
            response.setHeader("Content-Disposition", "attachment;filename="
                    .concat(String.valueOf(URLEncoder.encode(file.getPath(), "UTF-8"))));
            //以utf-8的编码读取ftl文件
            //Template t =  configuration.getTemplate("a.ftl","utf-8");
            // out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "utf-8"),10240);
            out = response.getOutputStream();
            byte[] buffer = new byte[512];  // 缓冲区
            int bytesToRead = -1;
            // 通过循环将读入的Word文件的内容输出到浏览器中
            while((bytesToRead = fin.read(buffer)) != -1) {
                out.write(buffer, 0, bytesToRead);
            }
        } finally {
            if(fin != null) fin.close();
            //out.close();
            if(out != null) out.close();
            //if(file != null) file.delete(); // 删除临时文件
        }
    }

    public static File createDoc(Map<String, Object> dataMap,String newFileName) {
        //String name =  "sellPlan.doc";
        // 输出文档路径及名称
        String fileName = "D:/ces/" + newFileName + (new Date().getTime()) + ".doc";
        //File outFile = new File(fileName);
        File f = new File(fileName);
        Template t = freemarkerTemplate;
        try {
            // 这个地方不能使用FileWriter因为需要指定编码类型否则生成的Word文档会因为有无法识别的编码而无法打开
            Writer w = new OutputStreamWriter(new FileOutputStream(f), "utf-8");
            t.process(dataMap, w);
            w.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        return f;
    }
}