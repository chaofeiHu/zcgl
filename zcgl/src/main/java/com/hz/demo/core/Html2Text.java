package com.hz.demo.core;

import java.io.*;
public class Html2Text {

    public String readfile(String filePath){
        String fileContent = "";
        try {
            File f = new File(filePath);
            if (f.isFile() && f.exists()) {
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(f), "GBK");
                BufferedReader reader = new BufferedReader(read);
                String line;
                while ((line = reader.readLine()) != null) {
                    fileContent += line;
                }
                read.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.print(fileContent);
        return fileContent;
    }

    public String getBody(String val) {
        String start = "<body style='font-family: 微软雅黑;'>";
        String end = "</body>";
        int s = val.indexOf(start) + start.length();
        int e = val.indexOf(end);
        return val.substring(s, e);
    }

    /**
     * 保存HTML
     * @param fileName
     * @param title
     * @param text
     */
    public void saveHtml(String fileName,String title,String text){
        try {
            File f = new File(fileName);
            if (!f.exists()) {
                f.createNewFile();
            }
            StringBuilder stringHtml = new StringBuilder();
            OutputStreamWriter write = new OutputStreamWriter(
                    new FileOutputStream(f), "GBK");
            BufferedWriter writer = new BufferedWriter(write);
            stringHtml.append("<html><head>");
            stringHtml.append("<meta http-equiv='Content-Type' content='text/html; charset=GBK'>");
            stringHtml.append("<title>"+title+"</title>");
            stringHtml.append("</head>");
            stringHtml.append("<body style='font-family: 微软雅黑;'>"+text);
            stringHtml.append("</body></html>");
            writer.write(stringHtml.toString());
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

      /*  PrintStream printStream=null;
        StringBuilder stringHtml = new StringBuilder();
        try {
            printStream = new PrintStream(new FileOutputStream(fileName));
            //输入HTML文件内容
            stringHtml.append("<html><head>");
            stringHtml.append("<meta http-equiv='Content-Type' content='text/html; charset=GBK'>");
            stringHtml.append("<title>"+title+"</title>");
            stringHtml.append("</head>");
            stringHtml.append("<body style='font-family: 微软雅黑;'>"+text);
            stringHtml.append("</body></html>");
            printStream.println(stringHtml.toString());
        }catch (Exception e){
            e.printStackTrace();
        }*/
    }

}