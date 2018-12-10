package com.hz.demo.core;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class ChineseUtil {

    public static boolean isChinese(char c) {
        boolean result = false;
        if (c >= 19968 && c <= 171941) {// 汉字范围 \u4e00-\u9fa5 (文)
            result = true;
        }
        return result;
    }

    //判断这个字符是否是汉字，返回boolean
    public static boolean isChinese(char[] c) {
        boolean result = false;
        for (char cc : c) {
            if (cc >= 19968 && cc <= 171941) {// 汉字范围 \u4e00-\u9fa5 (文)
                result = true;
            }
        }

        return result;
    }

    /**
     * 将str中的汉字部分进行url编码，并且将这个编码转换为小写
     *
     * @param str
     * @return
     */
    public static String opChinese(String str) {
        String restr = "";
        String encode = null;
        char[] charArray = str.toCharArray();
        for (char c : charArray) {
            if (c >= 19968 && c <= 171941) {// 汉字范围 \u4e00-\u9fa5 (文)
                try {
                    encode = URLEncoder.encode(String.valueOf(c), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                String lowerCase = encode.toLowerCase();
                restr = restr + lowerCase;
            } else {
                restr = restr + c;
            }
        }
        return restr;
    }

    /**
     * 将编码后的字符串重新解码，如：将字符串 "sd%E6%B5%8B%E8%AF%95%E4%BB%A5%E4%B8%8B " 解码为 "sd测试以下"
     *
     * @param str
     * @return
     */
    public static String deChinese(String str) {
        String decode = null;
        try {
            decode = URLDecoder.decode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return decode;
    }

    public static boolean hasFullChar(String str) {
        if (str.getBytes().length == str.length()) {
            return false;
        }
        return true;
    }
}
