package com.jt.util;

import java.nio.charset.Charset;
import java.util.Map;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class HttpClientService {

	@Autowired
	private CloseableHttpClient httpClient;
	@Autowired
	private RequestConfig requestConfig;

	/**
	 *url://www.jt.com?id=1&name=tomcat
	 * 目的:发起请求获取服务器数据
	 * 参数说明:
	 * 	1.url地址
	 *  2.用户提交的参数使用Map封装
	 *  3.指定编码格式
	 *
	 *步骤:
	 *	1.校验字符集. 如果字符集为null 设定默认值
	 *  2.校验params是否为null
	 *  	null:表示用户get请求无需传参.
	 *  	!null:需要传参,  get请求规则 url?key=value&key2=value2...
	 *  3.发起http的get请求获取返回值结果
	 */
	public String doGet(String url,Map<String,String> params,String charshet) {

		//1.校验字符集
		if(StringUtils.isEmpty(charshet)) {

			charshet = "UTF-8"; 
		}

		/**
		 * 2.校验参数是否为null url如何拼接????
		 * url:www.jt.com?id=1&name=tomcat&....
		 * Map<entry<k,v>>
		 */
		if(params!=null) {
			url +="?";
			//2.1遍历map集合 迭代器 fore循环
			for (Map.Entry<String,String> entry : params.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				url = url+key+"="+value+"&";
			}
			//2.2经过循环遍历最终url多个&
			url = url.substring(0,url.length()-1);
		}

		//3.发起get请求
		HttpGet get = new HttpGet(url);
		get.setConfig(requestConfig);//定义请求超时时间
		String result = null;
		try {
			CloseableHttpResponse response = 
					httpClient.execute(get);

			if(response.getStatusLine().getStatusCode()==200) {
				result = EntityUtils.toString(response.getEntity(),charshet);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return result;
	}


	//重载方法.对方法进行扩充方便使用者调用
	public String doGet(String url) {

		return doGet(url, null, null);
	}

	public String doGet(String url,Map<String,String> params) {

		return doGet(url, params, null);
	}
}
