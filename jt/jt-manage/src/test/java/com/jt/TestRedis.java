package com.jt;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.jt.mapper.ItemDescMapper;
import com.jt.pojo.ItemDesc;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

public class TestRedis {
	
	/**
	 * 1.Spring整合redis入门案例
	 */
	@Test
	public void testRedis1() {
		String host = "192.168.182.129";
		int port 	= 6379;
		Jedis jedis = new Jedis(host, port);
		jedis.set("1903","1903班下午好");
		System.out.println(jedis.get("1903"));
		//设定数据超时时间
		jedis.expire("1903", 20);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("1903key还能存活:"
		+jedis.ttl("1903"));
	}
	
	/**
	 * 2.简化操作数据超时用法
	 */
	@Test
	public void testRedis2() {
		Jedis jedis = new Jedis("192.168.182.129",6379);
		jedis.setex("abc",100,"英文字母");
		System.out.println(jedis.get("abc"));
	}
	
	/**
	 * 3.锁机制用法
	 * 	 实际用法: 保证set数据时如果这个key已经存在
	 * 			   不允许修改.
	 * 	 业务场景. 
	 * 		小明: set("jimian","8点")	
	 * 		小张: set("jimian","5点")
	 */
	@Test
	public void testRedis3() {
		Jedis jedis = new Jedis("192.168.182.129",6379);
		//jedis.set("yue", "8点xxxx地点");
		//jedis.set("yue", "5点xxxxx地点");	//更新操作
		Long flag1 = jedis.setnx("yue", "8点xxxx地点");
		jedis.del("yue");
		Long flag2 = jedis.setnx("yue", "5点xxxx地点");
		System.out.println(flag1+":::"+flag2);
		System.out.println("小丽约会时间:"+jedis.get("yue"));
	}
	
	/**
	 * 死锁
	 * 		1.setnx("yue","今晚8点") //加锁
	 * 		2.jedis.del("yue");	//减锁
	 * 		3.setnx("yue","今晚9点半") //加锁
	 * 避免死锁:添加key的超时时间
	 * 锁机制优化
	 */
	@Test
	public void testRedis4() {
		Jedis jedis = new Jedis("192.168.182.129",6379);
		String result1 = 
		jedis.set("yue", "今晚8点", "NX", "EX",20);
		//int a = 1/0;
		jedis.del("yue");
		String result2 = 
		jedis.set("yue", "今晚5点", "NX", "EX", 20);
		System.out.println(result1);
		System.out.println(result2);
	}
	
	/**
	 * 2.hash在工作中出场率低
	 */
	@Test
	public void testHash1() {
		Jedis jedis = new Jedis("192.168.182.129",6379);
		jedis.hset("user", "id", "120");
		jedis.hset("user", "name", "测试数据");
		jedis.hset("user", "age", "19");
		System.out.println(jedis.hgetAll("user"));
	}
	
	
	/**
	 * 3.list集合
	 */
	@Test
	public void testList() {
		Jedis jedis = new Jedis("192.168.182.129",6379);
		//1.当做队列
		//jedis.lpush("list","1,2,3,4,5");//注意",号"
		jedis.lpush("list","1","2","3","4");
		System.out.println
		("获取数据:"+jedis.rpop("list"));
	}
	
	
	/**
	 * 4.测试事务控制
	 */
	@Test
	public void testTx() {
		Jedis jedis = 
				new Jedis("192.168.182.129",6379);
		Transaction transaction = jedis.multi();	//1.开启事务
		try {
			transaction.set("aa", "aa");
			transaction.set("bb", "bb");
			int a =1/0; //模拟报错
			transaction.exec();	//提交事务
		} catch (Exception e) {
			e.printStackTrace();
			transaction.discard();
		}
	}
	
	
	/**
	 * 5.Springboot整合redis实际操作代码
	 * 业务需求:
	 * 	查询itemDesc数据,之后缓存处理.
	 * 步骤:
	 * 	1.先查询缓存中是否有itemDesc数据
	 * 		null	查询数据库  将数据保存到缓存中
	 * 		!null	获取数据直接返回
	 * 问题:
	 * 	  一般使用redis时都采用String类型操作.
	 * 	 但是从数据库获取的数据都是对象 Object
	 * 	 String~~~~json~~~~Object类型转化
	 */
	
	
	
	
	
	
	/*
	 * @Autowired private Jedis jedis;
	 * 
	 * @Autowired private ItemDescMapper mapper;
	 * 
	 * @Test public void testRedisItemDesc() { String key = "100"; //???????
	 * ItemDesc====String result = jedis.get(key); if(StringUtils.isEmpty(result)) {
	 * ItemDesc desc = mapper.selectById(key); jedis.set(key, desc.toString()); } }
	 */
	
	
	
	
	
	
	
	
	
	
}
