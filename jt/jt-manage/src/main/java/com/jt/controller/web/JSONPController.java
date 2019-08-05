package com.jt.controller.web;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.jt.pojo.Item;
import com.jt.pojo.ItemCat;
import com.jt.util.ObjectMapperUtil;

@RestController	//要求返回json数据
public class JSONPController {
	
	/**
	 * 参数说明:
	 * 	function:代表回调函数名称
	 * 	value:	 代表服务器返回的数据
	 * 
	 * @param callback
	 * @return
	 */
	@RequestMapping("/web/testJSONP")
	public JSONPObject testJSONP(String callback) {
		ItemCat itemCat = new ItemCat();
		itemCat.setId(2000L).setName("JSONP简化写法!!!");
		JSONPObject object = new JSONPObject(callback, itemCat);
		return object;
	}
	
	
	/*
	 * jsonp返回值结果,必须经过特殊格式封装.
	 * 调用者::回调函数获取
	 * 数据返回:封装数据   callback(JSON串)
	 * http://manage.jt.com/web/testJSONP
	 * ?callback=jQuery111108050409315062621_1563263312902&_=1563263312903
	 */
	/*
	 * @RequestMapping("/web/testJSONP") public String testJSONP(@PathVariable
	 * String callback) { ItemCat itemCat = new ItemCat();
	 * itemCat.setId(1000L).setName("jsonp测试!!!"); String json =
	 * ObjectMapperUtil.toJSON(itemCat); return callback+"("+json+")"; }
	 */
	
	
	
	
	
	
	
}
