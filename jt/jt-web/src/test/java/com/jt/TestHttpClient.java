package com.jt;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

public class TestHttpClient {
	
	/**
	 * 编码思路:
	 * 	1.创建工具API对象
	 * 	2.定义远程url地址
	 *  3.定义请求类型对象
	 *  4.发起http请求,获取响应结果.
	 *  5.从响应对象中获取数据.
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	@Test
	public void testGet() throws ClientProtocolException, IOException {
		CloseableHttpClient client = 
				HttpClients.createDefault();
		String url = "https://item.jd.com/8790543.html";
		HttpGet get = new HttpGet(url);
		//HttpPost post = new HttpPost(url);
		CloseableHttpResponse response = 
		client.execute(get);
		//判断响应是否正确
		if(response.getStatusLine().getStatusCode()==200) {
			//从中获取响应数据 JSON
			String result = 
			EntityUtils.toString(response.getEntity());
			System.out.println(result);
		}else {
			System.out.println("请求失败请稍后重试");
		}
	}
}
