package com.hz.demo.core;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hcf on 2018-10-15.
 */
public class FileUtils {
    public static String uploadFile(InputStream is, String fileName, String path) {
        BufferedInputStream bis = null;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        String tarFileName = null;
        try {
            //构建输入缓冲区，提高读取文件速度
            bis = new BufferedInputStream(is);
            //自动建立文件夹
            File folder = new File(path);
            if(!folder.exists()) {
                folder.mkdirs();
            }
            //为保证上传文件的唯一性，可以通过uuid来解决
            //为避免中文乱码问题则新生成的文件名由uuid+原来的文件后缀组成
            //tarFileName = UUID.randomUUID().toString() + getSuffix(fileName);
            tarFileName = (fileName).substring(0,fileName.lastIndexOf(".")) + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + (fileName).substring(fileName.lastIndexOf("."));
            //构建文件输出流
            fos = new FileOutputStream(new File(path + "\\" + tarFileName));
            //构建输出缓冲区，提高写文件的性能
            bos = new BufferedOutputStream(fos);
            //通过输入流读取数据并将数据通过输出流写到硬盘文件夹
            byte[] buffer = new byte[4096]; //构建4k的缓冲区
            int s = 0;
            while((s = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, s);
                bos.flush();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(bos != null) {
                try {
                    bos.close();
                    bos = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(fos != null) {
                try {
                    fos.close();
                    fos = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(bis != null) {
                try {
                    bis.close();
                    bis = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(is != null) {
                try {
                    is.close();
                    is = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return tarFileName;
    }
}
