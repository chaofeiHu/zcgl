package com.hz.demo.core;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyStore;

public class ClientCustomSSL {
        public static String doRefund(String url, String data) throws Exception {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            FileInputStream is = new FileInputStream(new File("F://apiclient_cert.p12"));
            try {
                keyStore.load(is, "5JGvbHuuImC50Zky1sbhYju28dghM0ZG".toCharArray());// 这里写密码..默认是你的MCHID
                //keyStore.load(null, null);
                //keyStore.store(new FileOutputStream("F:/apiclient_cert.p12"), "5JGvbHuuImC50Zky1sbhYju28dghM0ZG".toCharArray());
            } finally {
                is.close();
            }
// Trust own CA and all self-signed certs
            SSLContext sslcontext = SSLContexts.custom()
                    .loadKeyMaterial(keyStore, "5JGvbHuuImC50Zky1sbhYju28dghM0ZG".toCharArray())// 这里也是写密码的
                    .build();
// Allow TLSv1 protocol only
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                    sslcontext, new String[] { "TLSv1" }, null,
                    SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
            CloseableHttpClient httpclient = HttpClients.custom()
                    .setSSLSocketFactory(sslsf).build();
            try {
                HttpPost httpost = new HttpPost(url); // 设置响应头信息
                httpost.addHeader("Connection", "keep-alive");
                httpost.addHeader("Accept", "*/*");
                httpost.addHeader("Content-Type",
                        "application/x-www-form-urlencoded; charset=UTF-8");
                httpost.addHeader("Host", "api.mch.weixin.qq.com");
                //httpost.addHeader("Host", "qyapi.weixin.qq.com");
                httpost.addHeader("X-Requested-With", "XMLHttpRequest");
                httpost.addHeader("Cache-Control", "max-age=0");
                httpost.addHeader("User-Agent",
                        "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ");
                httpost.setEntity(new StringEntity(data, "UTF-8"));
                CloseableHttpResponse response = httpclient.execute(httpost);
                try {
                    HttpEntity entity = response.getEntity();

                    String jsonStr = EntityUtils.toString(response.getEntity(),
                            "UTF-8");
                    EntityUtils.consume(entity);
                    return jsonStr;
                } finally {
                    response.close();
                }
            } finally {
                httpclient.close();
            }
        }
	}
