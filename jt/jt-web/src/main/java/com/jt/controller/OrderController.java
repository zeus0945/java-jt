package com.jt.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.Cart;
import com.jt.pojo.Order;
import com.jt.pojo.User;
import com.jt.service.DubboCartService;
import com.jt.service.DubboOrderService;
import com.jt.util.UserThreadLocal;
import com.jt.vo.SysResult;

@Controller
@RequestMapping("order")
public class OrderController {
	
	@Reference(timeout = 3000,check = false)
	private DubboCartService cartService;
	@Reference(timeout = 3000,check = false)
	private DubboOrderService orderService;
	
	@RequestMapping("/create")
	public String create(Model model) {
		//获取用户的购物车信息
		Long userId = UserThreadLocal.get().getId();
		List<Cart> cartList = cartService.findCartListByUserId(userId);
		model.addAttribute("carts", cartList);
		return "order-cart";	//跳转页面
	}
	
	
	/**
	 * 
	 * <input name=userId value=123/>
	 * 	接收流程是什么???  
	 * 	怎么取值  怎么赋值
	 * 	提示: 
	 * 		1.无参构造
	 * 		2.get/set方法??
	 * 	
	 * 	取值过程:
	 * 		1.利用反射机制实例化对象(必须有无参构造)
	 * 		2.利用对象的getXXXX()方法获取对象的属性名称
	 * 		  getUserId()~~~~~~将get去掉之后首字母小写 userId;
	 * 		3.利用request的getParamiter(userId).获取对象的属性的值.
	 * 			如果获取的数据为null.则属性的值为null.
	 * 		4.调用对象的setUserId(userId)完成赋值操作.
	 * 		5.最终形成一个已经实例化成功的user对象.
	 * 		
	 * @param user
	 * @return
	 */
	/*
	 * public User mvcTest(User user) {
	 * 
	 * return new User(); }
	 */
	
	//<input name="小河马.河马名称" type="text"  value="沙皮" />
	//<input name="小河马.河马品种" type="text" value="壮的很">
	
	/*
	 * class User { int userId; String username; 河马 小河马; }
	 * 
	 * class 河马 { String 河马名称; String 河马品种; }
	 */
	
	
	/**
	 * 实现订单入库操作
	 * url地址:http://www.jt.com/order/submit
	 */
	@RequestMapping("/submit")
	@ResponseBody
	public SysResult saveOrder(Order order) {
		Long userId = UserThreadLocal.get().getId();
		order.setUserId(userId);
		//1.业务要求返回 orderId号
		String orderId = 
				orderService.saveOrder(order);
		//2.校验orderId是否有值
		if(StringUtils.isEmpty(orderId)) {
			return SysResult.fail();
		}
		return SysResult.success(orderId);
	}
	
	/**
	 * ${order.orderId}
	 * @param id
	 * @return
	 */
	@RequestMapping("success")
	public String findOrderById(String id,Model model) {
		
		Order order = orderService.findOrderById(id);
		model.addAttribute("order", order);
		return "success";
	}
	
	
	
	
	
	
}
