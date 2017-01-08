package com.dept.web.general.util.beifu_Pay;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


	public class SendMess {
		public String transferData(String xml, String encoding, String urlAddress){
			HttpURLConnection connection = null;
			String result="";	
			try {
				//配置连接
				URL url = new URL(urlAddress);
				
				connection = (HttpURLConnection)url.openConnection();
				connection.setRequestProperty("Content-type", "text/xml");
				connection.setConnectTimeout(200000);
				connection.setReadTimeout(200000);
				connection.setDoInput(true);
				connection.setDoOutput(true);
				connection.setRequestMethod("POST");
				//发送请求
				connection.getOutputStream().write(xml.getBytes(encoding));
				connection.getOutputStream().flush();
				connection.getOutputStream().close();
				//读取响应
				BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(),encoding));
				String str;
				while((str = reader.readLine()) != null){
					result += str;
				}
				reader.close();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				if(connection != null)
					connection.disconnect();
			}
			return result;
		}
		

}
