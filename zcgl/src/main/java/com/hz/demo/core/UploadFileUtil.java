package com.hz.demo.core;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Date;

public class UploadFileUtil {
    /**
     * @param file 文件
     * @param pre  前缀路径
     * @param path 全路径
     */
    public static String uploadFile(MultipartFile file, String pre, String path) {
        String fileurl = null;
        String fileName = new Date().getTime() + file.getOriginalFilename().replace(" ","");//时间+文件全名
        File targetFile = new File(path, fileName);
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }
        // 保存文件
        try {
            file.transferTo(targetFile);//存入硬盘
            fileurl = "/" + pre + "/" + fileName; //路径存入数据库
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileurl;
    }

    public static void deleteFile(String file) {
        try {
            File file1 = new File(file);
            if (file1!=null&&file1.isFile()&&file1.exists()) {
                file1.delete();
            }
        } catch (NullPointerException e) {
            System.out.println("未找到删除文件");
        }

    }

}
