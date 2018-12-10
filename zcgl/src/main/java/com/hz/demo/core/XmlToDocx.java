package com.hz.demo.core; /**
 * Created by xym on 2018-11-12.
 */

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.*;
import java.util.Enumeration;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * 其实docx属于zip的一种，这里只需要操作word/document.xml中的数据，其他的数据不用动
 *
 * @author
 *
 */
public class XmlToDocx {

   /* //配置信息,代码本身写的还是很可读的,就不过多注解了
    private static Configuration configuration = null;
    //这里注意的是利用WordUtils的类加载器动态获得模板文件的位置
    //private static final String templateFolder = WordUtils.class.getClassLoader().getResource("../../").getPath() + "WEB-INF/templetes/";
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
    }*/
    /**
     *
     *  @param xmlTemplate xml的文件名
     *  @param docxTemplate docx的路径和文件名
     * @param xmlTemp  填充完数据的临时xml
     * @param toFilePath  目标文件名
     * @param map  需要动态传入的数据
     * @throws IOException
     * @throws TemplateException
     */
    public static void toDocx(String xmlTemplate,String docxTemplate,String xmlTemp ,String  toFilePath,Map map)  {
        try {
            // 1.map是动态传入的数据
            // 这个地方不能使用FileWriter因为需要指定编码类型否则生成的Word文档会因为有无法识别的编码而无法打开
            Writer w1 = new OutputStreamWriter(new FileOutputStream(xmlTemp), "gb2312");
            // 2.把map中的数据动态由freemarker传给xml
            XmlTplUtil.process(xmlTemplate, map, w1);
            // 3.把填充完成的xml写入到docx中
            XmlToDocx xtd = new XmlToDocx();
            xtd.outDocx(new File(xmlTemp), docxTemplate, toFilePath);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     *
     * @param documentFile 动态生成数据的docunment.xml文件
     * @param docxTemplate docx的模板
     * @param toFilePath  需要导出的文件路径
     * @throws ZipException
     * @throws IOException
     */

    public void outDocx(File documentFile, String docxTemplate, String toFilePath) throws ZipException, IOException {

        try {
            File docxFile = new File(docxTemplate);
            ZipFile zipFile = new ZipFile(docxFile);
            Enumeration<? extends ZipEntry> zipEntrys = zipFile.entries();
            ZipOutputStream zipout = new ZipOutputStream(new FileOutputStream(toFilePath));
            int len = -1;
            byte[] buffer = new byte[1024];
            while (zipEntrys.hasMoreElements()) {
                ZipEntry next = zipEntrys.nextElement();
                InputStream is = zipFile.getInputStream(next);
                // 把输入流的文件传到输出流中 如果是word/document.xml由我们输入
                zipout.putNextEntry(new ZipEntry(next.toString()));
                if ("word/document.xml".equals(next.toString())) {
                    InputStream in = new FileInputStream(documentFile);
                    while ((len = in.read(buffer)) != -1) {
                        zipout.write(buffer, 0, len);
                    }
                    in.close();
                } else {
                    while ((len = is.read(buffer)) != -1) {
                        zipout.write(buffer, 0, len);
                    }
                    is.close();
                }
            }
            zipout.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
