package com.hz.demo.core;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 电话服务
 */

public class PhoneUtil {

	private static final String account = "N00000018474";//账户信息
	private static final String secret = "04d64f00-f6a5-11e7-8fb2-d57952859f2c";//api密码
	private static final String host = "http://apis.7moor.com";
	private static Log logger = LogFactory.getLog(PhoneUtil.class);
	public static void main(String[] args) {
		toPrivacyPhone("17603889885","15638533785");
	}
	//隐私电话
	public static String toPrivacyPhone(String phone1,String phone2) {
		String phone3="phoneNum:"+phone2;
		String time = getDateTime();
		String sig = md5(account + secret + time);
		//接口
		String interfacePath = "/v20160818/webCall/webCall/";
		String url = host + interfacePath + account + "?sig=" + sig;
		String auth = base64(account + ":" + time);
		HttpClientBuilder builder = HttpClientBuilder.create();
		CloseableHttpClient client = builder.build();
		HttpPost post = new HttpPost(url);
		post.addHeader("Accept", "application/json");
		post.addHeader("Content-Type","application/json;charset=utf-8");
		post.addHeader("Authorization",auth);
		post.addHeader("Access-Control-Allow-Origin", "*");
		StringEntity requestEntity = null;
		//根据需要发送的数据做相应替换
		requestEntity = new StringEntity("{\"Action\":\"Webcall\",\"ServiceNo\":\"01000001198\",\"Exten\":\""+phone1+"\",\"Variable\":\""+phone3+"\"}","UTF-8");
		post.setEntity(requestEntity);
		CloseableHttpResponse response = null;
		String str="";
		try {
			response = client.execute(post);
			HttpEntity entity = response.getEntity();
			str=EntityUtils.toString(entity,"utf8");
			//logger.info("the response is : " + EntityUtils.toString(entity,"utf8"));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (response != null){
				try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return str;
	}
	//获取短信模板
	public static String getSmsTemplate () {
		String time = getDateTime();
		String sig = md5(account + secret + time);
		//接口
		String interfacePath = "/v20160818/sms/getSmsTemplate/";
		String url = host + interfacePath + account + "?sig=" + sig;
		String auth = base64(account + ":" + time);
		HttpClientBuilder builder = HttpClientBuilder.create();
		CloseableHttpClient client = builder.build();
		HttpPost post = new HttpPost(url);
		post.addHeader("Accept", "application/json");
		post.addHeader("Content-Type","application/json;charset=utf-8");
		post.addHeader("Authorization",auth);
		StringEntity requestEntity = null;
		post.setEntity(requestEntity);
		CloseableHttpResponse response = null;
		try {
			response = client.execute(post);
			HttpEntity entity = response.getEntity();
			logger.info("the response is : " + EntityUtils.toString(entity,"utf8"));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (response != null){
				try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return "";
	}
	public static String md5 (String text) {
		return DigestUtils.md5Hex(text).toUpperCase();
	}
	public static String base64 (String text) {
		byte[] b = text.getBytes();
		Base64 base64 = new Base64();
		b = base64.encode(b);
		String s = new String(b);
		return s;
	}
	public static String getDateTime(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.format(new Date());
	}

}
