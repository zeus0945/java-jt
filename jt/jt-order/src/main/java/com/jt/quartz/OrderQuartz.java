package com.jt.quartz;

import java.util.Calendar;
import java.util.Date;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jt.mapper.OrderMapper;
import com.jt.pojo.Order;

//准备订单定时任务
@Component //id=orderQuartz
public class OrderQuartz extends QuartzJobBean{

	@Autowired
	private OrderMapper orderMapper;

	
	/**
	 * 标记超时订单.
	 *修改数据: 1.status=6   2.updated=now
	 * 条件: status=1 and created < 当前时间-30分钟
	 *	
	 */
	@Override
	@Transactional
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		//格林威治时间 毫秒 秒 分 时 天 周 月 年
		//代表当前的时间
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, -30);
		Date timeOut = calendar.getTime();
		
		Order order = new Order();
		order.setStatus(6).setUpdated(new Date());
		UpdateWrapper<Order> updateWrapper = 
							new UpdateWrapper<Order>();
		updateWrapper.eq("status", 1)
					 .lt("created",timeOut);
		orderMapper.update(order, updateWrapper);
		System.out.println("定时任务完成 更新数据库!!!!!!");
	}
}
