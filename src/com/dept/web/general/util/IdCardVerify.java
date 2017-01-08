package com.dept.web.general.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;

import com.alibaba.fastjson.JSONObject;

public class IdCardVerify {

	private final static String mall_id="100043";
	private final static String appkey="4831d86f244cc1b119c4c96bcfb527d4";
	private final static String url="http://121.41.42.121:8080/v2/id-server?";

	public static int idcard_verify(String realname,String idcard){
		idcard=idcard.toLowerCase();
		long tm=System.currentTimeMillis();
		String md5_param=mall_id+realname+idcard+tm+appkey;
		String sign=md5(md5_param);
		String param=new StringBuffer().append("mall_id="+mall_id)
				.append("&realname="+realname)
				.append("&idcard="+idcard)
				.append("&tm="+tm)
				.append("&sign="+sign).toString();
		String url_v=url+param;
		String jsonString=url2string(url_v);
		
		JSONObject result=JSONObject.parseObject(jsonString);

		int status=Integer.parseInt(result.getString("status"));
		int code=Integer.parseInt(result.getJSONObject("data").getString("code"));
		String message=result.getJSONObject("data").getString("message");
				
		/*客户可以根据自己的业务需求进行处理*/
		if(status==2001){
			//2001=正常服务
			
			if(code==1000){
				//一致
				
			}else if(code==1001){
				//不一致
					
			}else if(code==1002){
				//库中无此号
					
			}
					
		}
		return code;
	}
	private static String md5(String s){
		char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};       
        try {
            byte[] btInput = s.getBytes("utf-8");
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
	}
	private static String url2string(String url){
		StringBuffer sb=new StringBuffer();
		try {
			InputStream is=new URL(url).openStream();
			byte[] buf=new byte[1024*10];
			int len=0;
			while((len=is.read(buf, 0, 1024*10))>0){
				sb.append(new String(buf,0,len));
			}
			is.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

}
