package com.jt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.util.HttpClientService;
import com.jt.util.ObjectMapperUtil;

@Service
public class ItemServiceImpl implements ItemService {
	
	@Autowired
	private HttpClientService httpClient;
	
	@Override
	public Item findItemById(Long itemId) {
		String url = "http://manage.jt.com/web/item/findItemById/"+itemId;
		String itemJSON = httpClient.doGet(url);
		//将json数据转化为对象 对象转json get方法,
		//json转对象时调用set方法!!!
		return ObjectMapperUtil.toObject(itemJSON,Item.class);
	}

	@Override
	public ItemDesc findItemDescById(Long itemId) {
		String url = "http://manage.jt.com/web/item/findItemDescById/"+itemId;
		String itemDescJSON = httpClient.doGet(url);
		
		return ObjectMapperUtil.toObject(itemDescJSON, ItemDesc.class);
	}
	
}
