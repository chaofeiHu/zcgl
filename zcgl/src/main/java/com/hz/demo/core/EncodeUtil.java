package com.hz.demo.core;


import java.math.BigInteger;
import java.security.MessageDigest;


public class EncodeUtil {

    private static final int fillchar = '=';

    private static final String cvt = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
            + "abcdefghijklmnopqrstuvwxyz" + "0123456789+/";

    /**
     * Base64加密
     * @param data
     * @return
     */
    public static String encodeBase64(String data) {
        return encodeBase64(data.getBytes());
    }

    /**
     * Base64加密
     * @param data
     * @return
     */
    public static String encodeBase64(byte[] data) {
        int c;
        int len = data.length;
        StringBuffer ret = new StringBuffer(((len / 3) + 1) * 4);
        for (int i = 0; i < len; ++i) {
            c = (data[i] >> 2) & 0x3f;
            ret.append(cvt.charAt(c));
            c = (data[i] << 4) & 0x3f;
            if (++i < len)
                c |= (data[i] >> 4) & 0x0f;

            ret.append(cvt.charAt(c));
            if (i < len) {
                c = (data[i] << 2) & 0x3f;
                if (++i < len)
                    c |= (data[i] >> 6) & 0x03;

                ret.append(cvt.charAt(c));
            } else {
                ++i;
                ret.append((char) fillchar);
            }

            if (i < len) {
                c = data[i] & 0x3f;
                ret.append(cvt.charAt(c));
            } else {
                ret.append((char) fillchar);
            }
        }
        return ret.toString();
    }

    /**
     * Base64解密
     * @param data
     * @return
     */
    public static String decodeBase64(String data) {
        return decodeBase64(data.getBytes());
    }

    /**
     * Base64解密
     * @param data
     * @return
     */
    public static String decodeBase64(byte[] data) {
        int c, c1;
        int len = data.length;
        StringBuffer ret = new StringBuffer((len * 3) / 4);
        for (int i = 0; i < len; ++i) {
            c = cvt.indexOf(data[i]);
            ++i;
            c1 = cvt.indexOf(data[i]);
            c = ((c << 2) | ((c1 >> 4) & 0x3));
            ret.append((char) c);
            if (++i < len) {
                c = data[i];
                if (fillchar == c)
                    break;

                c = cvt.indexOf((char) c);
                c1 = ((c1 << 4) & 0xf0) | ((c >> 2) & 0xf);
                ret.append((char) c1);
            }

            if (++i < len) {
                c1 = data[i];
                if (fillchar == c1)
                    break;

                c1 = cvt.indexOf((char) c1);
                c = ((c << 6) & 0xc0) | c1;
                ret.append((char) c);
            }
        }
        return ret.toString();
    }

    /**
     * MD5加密
     * @param inputStr
     * @return
     */
    public static String encodeMD5(String inputStr) {
        BigInteger bigInteger=null;

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] inputData = inputStr.getBytes();
            md.update(inputData);
            bigInteger = new BigInteger(md.digest());
        } catch (Exception e) {e.printStackTrace();}
        return bigInteger.toString(16);
    }

    /**
     * SHA加密
     * @param inputStr
     * @return
     */
    public static  String  decodeSHA(String inputStr)  {
        BigInteger sha =null;
        byte[] inputData = inputStr.getBytes();
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA");
            messageDigest.update(inputData);
            sha = new BigInteger(messageDigest.digest());
        } catch (Exception e) {e.printStackTrace();}
        return sha.toString(32);
    }

    public static void main(String... args) {
        //b0baee9d279d34fa1dfd71aadb908c3f -30bc9df7bfd99392e1a7dc0dca5fc9aa encxMTExMTE=
//        -1ef523c6b645a65441a91fa80df077c2
        //System.out.println(decodeBase64("MDAwMDAw"));
        System.out.println(encodeMD5("0000"));

    }

}
