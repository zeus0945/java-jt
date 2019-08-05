package com.jt.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.ItemCatMapper;
import com.jt.pojo.ItemCat;
import com.jt.util.ObjectMapperUtil;
import com.jt.vo.EasyUI_Tree;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.ShardedJedis;

@Service
public class ItemCatServiceImpl implements ItemCatService {
	
	@Autowired
	private JedisCluster jedisCluster;
	
	
	
	@Autowired
	private ItemCatMapper itemCatMapper;
	
	@Override
	public String findItemCatNameById(Long itemCatId) {
		
		ItemCat itemCat = 
				itemCatMapper.selectById(itemCatId);
		return itemCat.getName();
	}

	/**
	 * List<EasyUI_Tree> 返回的是VO对象集合
	 * EasyUI_Tree:id/text/state
	 * 
	 * List<ItemCat> 	  返回ItemCat集合对象
	 * ItemCat: id/name/判断是否为父级
	 */
	
	//查询数据库
	@Override
	public List<EasyUI_Tree> findItemCatByParentId(Long parentId) {
		List<EasyUI_Tree> treeList = new ArrayList<>();
		//1.获取数据库数据
		List<ItemCat> itemCatList = findItemCatList(parentId);
		for (ItemCat itemCat : itemCatList) {
			Long   id = itemCat.getId();
			String text = itemCat.getName();
			//一级二级菜单 closed 三级菜单 open
			String state = 
			itemCat.getIsParent()?"closed":"open";
			EasyUI_Tree tree = 
					new EasyUI_Tree(id, text, state);
			treeList.add(tree);
		}
		return treeList;
	}
	
	public List<ItemCat> findItemCatList(Long parentId) {
		QueryWrapper<ItemCat> queryWrapper 
						= new QueryWrapper<ItemCat>();
		queryWrapper.eq("parent_id", parentId);
		return itemCatMapper.selectList(queryWrapper);
	}
	
	/**
	 * 如何实现松耦合????  
	 * AOP实现
	 * 1.环绕通知
	 * 2.自定义注解  @Cache(key)
	 * 	 value:用户获取数据
	 */
	//该方法,查询缓存
	@SuppressWarnings("unchecked")
	@Override
	public List<EasyUI_Tree> findItemCatByCache(Long parentId) {
		List<EasyUI_Tree> treeList = new ArrayList<EasyUI_Tree>();
		String key = "ITEM_CAT_"+parentId;
		String result = jedisCluster.get(key);
		if(StringUtils.isEmpty(result)) {
			//表示缓存中没有数据,应该查询数据库
			treeList = findItemCatByParentId(parentId);
			//将对象转化为json
			String json = 
					ObjectMapperUtil.toJSON(treeList);
			//将数据保存到缓存中
			jedisCluster.set(key, json);
			System.out.println("查询数据库!!!!!");
		}else {
			//表示缓存中有数据,将json串转化为对象
			treeList = 
			ObjectMapperUtil.toObject(result, treeList.getClass());
			System.out.println("查询缓存!!!!");
		}
		return treeList;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
