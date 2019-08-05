package com.jt.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jt.mapper.ItemDescMapper;
import com.jt.mapper.ItemMapper;
import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.vo.EasyUI_Table;

@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private ItemMapper itemMapper;
	@Autowired
	private ItemDescMapper itemDescMapper;

	/*查询第一页   起始位置,显示条数*/
	//SELECT * FROM tb_item LIMIT 0,20
	/*查询第二页*/
	//SELECT * FROM tb_item LIMIT 20,20
	/*查询第三页*/
	//SELECT * FROM tb_item LIMIT 40,20
	/*查询第N页*/
	//SELECT * FROM tb_item LIMIT (n-1)*20,20
	@Override
	public EasyUI_Table findItemByPage(Integer page, Integer rows) {
		//1.获取总记录数
		int total = itemMapper.selectCount(null);
		//2.分页查询List集合
		int start = (page-1)*rows;
		List<Item> itemList = 
				itemMapper.findItemByPage(start,rows);
		return new EasyUI_Table(total, itemList);
	}

	/**
	 * 由于主键自增,每次入库后返回id值
		INSERT INTO USER VALUES (NULL,"xxxx",19,"其他");
		查询最后一个入库id信息 不会有线程安全性问题
		SELECT LAST_INSERT_ID();
	 */
	@Transactional//添加事务控制
	@Override
	public void saveItem(Item item,ItemDesc itemDesc) {
		item.setStatus(1)  //1.表示状态正常
		.setCreated(new Date())
		.setUpdated(item.getCreated());
		itemMapper.insert(item);
		//因为商品详情与商品是id一致.但是item数据是主键
		//自增只有入库之后才能获取主键信息.
		//答案:入库时返回Id值
		itemDesc.setItemId(item.getId())
		        .setCreated(item.getCreated())
				.setUpdated(item.getCreated());
		itemDescMapper.insert(itemDesc);
	}

	/**
	 * rollbackFor 指定异常类型回滚
	 * rollbackFor=Exception.class
	 * noRollbackFor=指定异常不回滚
	 */
	@Transactional//添加事务控制
	@Override
	public void updateItem(Item item,ItemDesc itemDesc) {
		item.setUpdated(new Date());
		//sql: xxxx where id = 主键值
		itemMapper.updateById(item);
		
		itemDesc.setItemId(item.getId());
		itemDesc.setUpdated(item.getUpdated());
		itemDescMapper.updateById(itemDesc);
	}

	/**
	 * 1.自己手写sql
	 * 	delete from tb_item where id in (1,2,3,4)
	 * 2.使用mybatisplus
	 */
	@Override
	@Transactional//添加事务控制
	public void deleteItems(Long[] ids) {

		//1.手写sql 复习mybatis用法
		//itemMapper.deleteItems(ids);

		//2.利用mybatisMapper操作
		//将数组转化为集合
		List<Long> idList = Arrays.asList(ids);
		itemMapper.deleteBatchIds(idList);
		itemDescMapper.deleteBatchIds(idList);
	}

	@Override
	@Transactional//添加事务控制
	public void updateStatus(Long[] ids, int status) {
		/*
		 * Item item = new Item(); item.setStatus(status) .setUpdated(new Date());
		 * List<Long> longList = Arrays.asList(ids); UpdateWrapper<Item>
		 * upteUpdateWrapper = new UpdateWrapper<Item>(); upteUpdateWrapper.in("id",
		 * longList); itemMapper.update(item, upteUpdateWrapper);
		 */
		
		itemMapper.updateStatus(ids,status);
	}

	@Override
	public ItemDesc findItemDescById(Long itemId) {
		
		return itemDescMapper.selectById(itemId);
	}

	@Override
	public Item findItemById(Long itemId) {
		
		return itemMapper.selectById(itemId);
	}






}
