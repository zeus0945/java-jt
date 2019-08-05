package com.jt.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.Module.SetupContext;
import com.jt.mapper.CartMapper;
import com.jt.pojo.Cart;

@Service(timeout = 3000)
public class DubboCartServiceImpl implements DubboCartService {
	
	@Autowired
	private CartMapper cartMapper;
	
	//根据userId查询cartList集合
	@Override
	public List<Cart> findCartListByUserId(Long userId) {
		QueryWrapper<Cart> queryWrapper = 
						new QueryWrapper<Cart>();
		queryWrapper.eq("user_id", userId);
		return cartMapper.selectList(queryWrapper);
	}
	
	/**
	 * 利用对象中不为null的属性充当where条件
	 */
	@Override
	public void deleteCart(Cart cart) {
		//delete from tb_cart where user_id=7 and item_id=562379
		QueryWrapper<Cart> queryWrapper = new QueryWrapper<>(cart);
		cartMapper.delete(queryWrapper);
		
	}

	/**
	 * 思路:
	 * 	根据user_id item_id查询数据库.
	 * 	null  表示第一次购买 新增入库
	 * 	!null 表示已经购买过 做数量的更新.
	 */
	@Override
	public void saveCart(Cart cart) {
		QueryWrapper<Cart> queryWrapper = 
				new QueryWrapper<Cart>();
		queryWrapper.eq("item_id", cart.getItemId())
					.eq("user_id",cart.getUserId());
		Cart cartDB = cartMapper.selectOne(queryWrapper);
		if(cartDB == null) {
			//表示第一次新增
			cart.setCreated(new Date())
				.setUpdated(cart.getCreated());
			cartMapper.insert(cart);
		}else {
			int num = cartDB.getNum() + cart.getNum();
			Cart cartTemp = new Cart();
			cartTemp.setId(cartDB.getId()) //设定主键
					.setNum(num)
					.setUpdated(new Date());
			//sql:update tb_cart set num=3,updated=now() where id=100
			cartMapper.updateById(cartTemp);
		}
	}

	//应该更新数量 set num = 100,updated=时间 where user_id=xx and item_id=xxx
	/**
	 * entity:要修改的数据
	 * updateWrapper:修改的where条件
	 * id 或  userId itemId
	 * num 和updated
	 */
	@Override
	public void updateCartNum(Cart cart) {
		Cart cartTemp = new Cart();
		cartTemp.setNum(cart.getNum())
				.setUpdated(new Date());
		QueryWrapper<Cart> updateWrapper = new QueryWrapper<Cart>();
		updateWrapper.eq("user_id", cart.getUserId())
					 .eq("item_id", cart.getItemId());
		cartMapper.update(cartTemp, updateWrapper);
		
	}
}
