package com.jt;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.jt.util.HttpClientService;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestSpringBootHttpClient {
	
	@Autowired
	private HttpClientService httpClient;
	
	@Test
	public void testGet() {
		String url = "https://item.jd.com/45703246467.html";
		String result = httpClient.doGet(url);
		System.out.println(result);
	}
}
