package com.hz.demo.core;


import java.io.File;

/**
 * @author:change
 * @description:获取绝对路径
 * @date:2018/6/28 0028
 */
public class PathUtil {
    static String path1;
    static String download;

    public static String getRootPath() {
        String line = File.separator;
        String path = Thread.currentThread().getContextClassLoader().getResource("").toString();
        //windows下
        if ("\\".equals(line)) {
            /*            path = path.replace("/", "\\");  // 将/换成\\*/
            path1 = path;
        }
        //linux下
        if ("/".equals(line)) {
            path = path.replace("\\", "/");
            path1 = path;
        }
        return path1;
    }

    public static void main(String[] args) {
        System.out.println(getRootPath());
        System.out.println(PathUtil.class.getClassLoader().getResource("pdf").getPath());

    }
}
