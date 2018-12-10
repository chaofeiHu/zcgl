package com.hz.demo.core;

import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;
import org.apache.poi.xwpf.converter.pdf.PdfConverter;
import org.apache.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.*;


public class XMlToDoc {





    /**
     * 生成pdf
     */
    public static String makePdfByXcode(String docx) {
//       D:\test\kexie\kexie\target\parttimejob-1.0-SNAPSHOT\/upload/work/1529388079175填写申报信息模板通用级别2222.docx
        String filename = null;
        File outFile = null;
        try {

            //    document.setParagraph(new Pa );
            if (docx.contains(".docx")) {
                XWPFDocument document=new XWPFDocument(new FileInputStream(new File(docx)));
                outFile=new File(docx.replace(".docx",".pdf"));
                filename=docx.replace(".docx",".pdf");

                outFile.getParentFile().mkdirs();
                OutputStream out=new FileOutputStream(outFile);
                //    IFontProvider fontProvider = new AbstractFontRegistry();
                PdfOptions options= PdfOptions.create();  //gb2312
                PdfConverter.getInstance().convert(document,out,options);
                /*File inputFile = new File(docx);
                outFile = new File(docx.replace(".docx", ".pdf"));
                filename = docx.replace(".docx", ".pdf");
                outFile.getParentFile().mkdirs();

                OpenOfficeConnection connection = new SocketOpenOfficeConnection(8100);
                connection.connect();

                // convert
                DocumentConverter converter = new OpenOfficeDocumentConverter(connection);
                converter.convert(inputFile, outFile);

                // close the connection
                connection.disconnect();*/
            } else {
                File inputFile = new File(docx);
                outFile = new File(docx.replace(".doc", ".pdf"));
                filename = docx.replace(".doc", ".pdf");
                outFile.getParentFile().mkdirs();

                OpenOfficeConnection connection = new SocketOpenOfficeConnection(8100);
                connection.connect();

                // convert
                DocumentConverter converter = new OpenOfficeDocumentConverter(connection);
                converter.convert(inputFile, outFile);

                // close the connection
                connection.disconnect();
            }


        }catch (IllegalArgumentException e){
            System.err.println("未知文件格式");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return filename;

//        System.out.println("Generate ooxml.pdf with " + (System.currentTimeMillis() - startTime) + " ms.");
    }


}
