package com.jt.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jt.mapper.OrderItemMapper;
import com.jt.mapper.OrderMapper;
import com.jt.mapper.OrderShippingMapper;
import com.jt.pojo.Order;
import com.jt.pojo.OrderItem;
import com.jt.pojo.OrderShipping;

@Service
public class OrderServiceImpl implements DubboOrderService {
	
	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private OrderShippingMapper orderShippingMapper;
	@Autowired
	private OrderItemMapper orderItemMapper;
	
	
	/**
	 * 1.要求返回orderId号
	 * 2.要求同时入库
	 * 3.三张表分别入库
	 */
	@Override
	@Transactional	//进行事务控制
	public String saveOrder(Order order) {
		String orderId = 
		System.currentTimeMillis()+""+order.getUserId();
		Date now = new Date();
		//1.入库订单信息
		order.setOrderId(orderId);
		order.setStatus(1);	//表示未付款
		order.setCreated(now);
		order.setUpdated(now);
		orderMapper.insert(order);
		System.out.println("订单入库成功!!!!!");
		
		//2.订单物流入库
		OrderShipping shipping = order.getOrderShipping();
		shipping.setOrderId(orderId);
		shipping.setCreated(now);
		shipping.setUpdated(now);
		orderShippingMapper.insert(shipping);
		System.out.println("订单物流入库成功!!!!");
		
		//3.订单商品入库
		List<OrderItem> orderItems = order.getOrderItems();
		for (OrderItem orderItem : orderItems) {
			orderItem.setOrderId(orderId);
			orderItem.setCreated(now);
			orderItem.setUpdated(now);
			orderItemMapper.insert(orderItem);
		}
		System.out.println("订单入库成功!!!!!!");
		return orderId;
	}

	/**
	 * order对象中应该包含三部分数据
	 * order对象  orderShipping对象  orderItem对象
	 * 思考:
	 * 	1.3张表关联查询  where,left join    
	 * 	2. 单表查询 
	 *核心业务思路:
	 *	应该尽可能的将多表关联查询,转化为单表查询.
	 */
	
	@Override
	public Order findOrderById(String id) {
		Order order = orderMapper.selectById(id);
		OrderShipping shipping = 
					  orderShippingMapper.selectById(id);
		QueryWrapper<OrderItem> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("order_id",id);
		List<OrderItem> items = 
				orderItemMapper.selectList(queryWrapper);
		order.setOrderItems(items)
			 .setOrderShipping(shipping);
		return order;
	}
}
