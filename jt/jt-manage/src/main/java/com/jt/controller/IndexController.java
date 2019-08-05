package com.jt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jt.pojo.Item;

@Controller
public class IndexController {

	/**
	 * 业务需求: 通过url实现页面跳转功能
	 * /page/item-add
	 * /page/item-list
	 * /page/item-param-list
	 * 
	 * 共性:
	 * 	1.请求路径的后半部分不同
	 * 	2.请求路径与跳转页面相同
	 * 
	 *思考: 能否编辑一个方法实现页面的通用跳转??
	 *
	 *RestFul风格  必会内容
	 *	1.url中的参数必须使用"/"分割
	 *  2.服务端获取数据时.必须使用"{}"包裹参数
	 *  3.使用@PathVariable注解实现数据转化
	 *  
	 *  如果接收参数名称与url变量名称不一致.
	 *  则需要使用value属性标识
	 *  @PathVariable(value = "moduleName") String abc
	 */
	@RequestMapping("/page/{moduleName}")
	public String itemAdd(@PathVariable String moduleName) {

		return moduleName;
	}
	
	//localhost:8091/saveItem/手机/永不关机/1000000
	/*
	 * @RequestMapping("/saveItem/{title}/{sellPoint}/{price}")
	 * 
	 * @ResponseBody public Item saveItem(
	 * 
	 * @PathVariable String title,
	 * 
	 * @PathVariable String sellPoint,
	 * 
	 * @PathVariable Long price) { Item item = new Item(); item.setTitle(title)
	 * .setSellPoint(sellPoint) .setPrice(price); return item; }
	 */
	
	/**
	 * localhost:8091/saveItem/手机/永不关机/1000000
	 * 使用时需要保证参数与属性名称一致
	 * @param item
	 * @return
	 * 简化get操作
	 * 	localhost:8091/saveItem?id=1&name=xxx
	 *  restFul是springCloud调用的方式.
	 */
	@RequestMapping("/saveItem/{title}/{sellPoint}/{price}")
	@ResponseBody
	public Item saveItem(Item item) {
		
		return item;
	}
	
	
	/**
	 * 测试负载均衡
	 */
	@ResponseBody
	@RequestMapping("/getMsg")
	public String getMsg() {
		
		return "我是8093服务器!!!!!!!";
	}
	
	
	
}
