package com.hz.demo.core;



import net.sf.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 隐私电话
 */
public class HttpUtil {
    public static void main(String[] args) throws Exception {
        String str="{\"Command\":\"Response\",\"Succeed\":true,\"Message\":\"4\",\"ActionID\":\"Webcall3499937647288319382\",\"Response\":\"Webcall\"}";
        JSONObject jsonObj  = JSONObject.fromObject(str);
        System.out.println(jsonObj.get("Command"));    //输出为：22
        /*
        请求第三方接口
        try {
            URL url = new URL("http://120.26.96.9/command?Action=Webcall&Account=N00000018474&PBX=bj.ali.9.8&Timeout=20&ServiceNo=01000001198&Exten=15638533785&Variable=phoneNum:17603889885");
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setDoOutput(true); // 使用 URL 连接进行输出
            httpConn.setDoInput(true); // 使用 URL 连接进行输入
            httpConn.setUseCaches(false); // 忽略缓存
            httpConn.setRequestMethod("POST"); // 设置URL请求方法
            OutputStream outputStream = httpConn.getOutputStream();
            outputStream.close();
            BufferedReader responseReader = new BufferedReader(
                    new InputStreamReader(httpConn.getInputStream(), "UTF-8"));
            String readLine;
            StringBuffer responseSb = new StringBuffer();
            while ((readLine = responseReader.readLine()) != null) {
                responseSb.append(readLine);
            }
            System.out.println(responseSb.toString());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("ERROR");
        }
        */
        /*
                 //请求的webservice的url
                 URL url = new URL("http://120.26.96.9/command?Action=Webcall&Account=N00000018474&PBX=bj.ali.9.8&Timeout=20&ServiceNo=01000001198&Exten=15638533785&Variable=phoneNum:17603889885");
                 //创建http链接
                 HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                 //设置请求的方法类型
                 httpURLConnection.setRequestMethod("POST");
                 //设置请求的内容类型
                 httpURLConnection.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
                 //设置发送数据
                 //httpURLConnection.setDoOutput(true);
                 //设置接受数据
                 httpURLConnection.setDoInput(true);
                 //发送数据,使用输出流
                 //OutputStream outputStream = httpURLConnection.getOutputStream();
                 //String content = "user_id="+ URLEncoder.encode("13846", "gbk");
                 //发送数据
                 //outputStream.write(content.getBytes());
                 //接收数据
                 InputStream inputStream = httpURLConnection.getInputStream();
                 //定义字节数组
                 byte[] b = new byte[1024];
                 //定义一个输出流存储接收到的数据
                 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                 //开始接收数据
                 int len = 0;
                 while (true) {
                         len = inputStream.read(b);
                         if (len == -1) {
                                 //数据读完
                                 break;
                             }
                         byteArrayOutputStream.write(b, 0, len);
                     }
                 //从输出流中获取读取到数据(服务端返回的)
                 String response = byteArrayOutputStream.toString();
                 System.out.println(response);
                 */
             }
}


