package com.hz.demo.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

import static com.hz.demo.core.ResultUtils.toJson;

public class ResultUtils {

    public static String toJson(Object data) {
        return JSON.toJSONString(data,SerializerFeature.WriteMapNullValue);
    }

    public static String toDateJson(Object data) {
        JSON.DEFFAULT_DATE_FORMAT = "yyyy-MM-dd";
        return JSON.toJSONString(data, SerializerFeature.WriteDateUseDateFormat);
    }

    public static String toDateTimeJson(Object data) {
        JSON.DEFFAULT_DATE_FORMAT="yyyy-MM-dd HH:mm:ss";
        return  JSON.toJSONString(data, SerializerFeature.WriteDateUseDateFormat);
    }

    public static void writeSuccess(HttpServletResponse response) {
        write(response, "success");
    }

    public static void writeFailed(HttpServletResponse response) {
        write(response, "failed");
    }

    public static void write(HttpServletResponse response, String message) {
        PrintWriter writer = null;
        try {
            response.setHeader("Charset", "UTF-8");
            response.setCharacterEncoding("UTF-8");
            writer = response.getWriter();
            writer.write(message);
        } catch (Exception e) {

        } finally {
            if (writer != null)
                writer.close();
        }
    }

    public static void write(HttpServletResponse response, Object object) {
        PrintWriter writer = null;
        try {
            response.setHeader("Charset", "UTF-8");
            response.setCharacterEncoding("UTF-8");
            writer = response.getWriter();
            writer.write(toJson(object));
            // System.out.println(toJson(object));
        } catch (Exception e) {

        } finally {
            if (writer != null)
                writer.close();
        }
    }

    //输出带状态格式的JSON数据
    public static void writeMessage(HttpServletResponse response,int isOk, String message) {
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("{");
        jsonBuilder.append("\"isOk\":"+isOk+",\"message\":\""+message+"\"");
        jsonBuilder.append("}");
        write(response, jsonBuilder.toString());
    }


}
