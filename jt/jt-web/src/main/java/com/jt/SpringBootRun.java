package com.jt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
//容器启动时不要加载数据源相关配置
@SpringBootApplication(exclude=DataSourceAutoConfiguration.class )
public class SpringBootRun {
	
	public static void main(String[] args) {
		
		SpringApplication.run(SpringBootRun.class,args);
	}
}
