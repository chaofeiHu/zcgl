package com.hz.demo.core;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Date;

public class DeleteFile {
	public static void main(String args[]) {
		//String str="upload\\2017-12-15\\1513326499381admin.png";
		//String ss="F:\\apache-tomcat-8.0.36\\webapps\\myyy\\static\\uploadfile\\";
		//String str1=ss+str.substring(0,17);
		//String str2=str.substring(18,str.length());
		//delOneFile(str1,str2.substring(0,str2.indexOf(".")));
	}

// 删除文件夹
// param folderPath 文件夹完整绝对路径

	public static void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); // 删除完里面所有内容
			String filePath = folderPath;
			filePath = filePath.toString();
			File myFilePath = new File(filePath);
			myFilePath.delete(); // 删除空文件夹
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 删除指定文件夹下所有文件
// param path 文件夹完整绝对路径
	public static boolean delAllFile(String path) {
		boolean flag = false;
		File file = new File(path);
		if (!file.exists()) {
			return flag;
		}
		if (!file.isDirectory()) {
			return flag;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
				delFolder(path + "/" + tempList[i]);// 再删除空文件夹
				flag = true;
			}
		}
		return flag;
	}

	public static boolean delOneFile(String path,String logic_name) {
		boolean flag = false;
		File folder = new File(path);
		File[] files = folder.listFiles();
		if (files!=null) {
			for (File file : files) {
				String fileName=file.getName().substring(0,file.getName().indexOf("."));
				if (fileName.equals(logic_name)) {
					if (!file.isDirectory()) {
						flag=file.delete();
					}
				}
			}
		}
		return flag;
	}

	public void ccss() {
		System.out.print("*********************");
		System.out.print("*********************");
		System.out.print("*********************");
		System.out.print("*********************");
		System.out.print("*********************");
		System.out.print("*********************");
		System.out.print("*********************");
		System.out.print("*********************");
		System.out.print("*********************");
	}
}