package com.jt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.UserMapper;
import com.jt.pojo.User;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserMapper userMapper;
	
	/**
	 * true  当前用户输入内容 已存在
	 * false 表示数据可以使用
	 * param:用户输入的数据
	 * type: 参数类型  1 username 2 phone 3 email
	 */
	@Override
	public boolean findUserCheck(String param, Integer type) {
		//1.定义查询的字段
		String column = (type==1)?"username":((type==2)?"phone":"email");
		//2.校验数据库中是否有数据???
		QueryWrapper<User> queryWrapper = new QueryWrapper();
		queryWrapper.eq(column, param);
		int count = userMapper.selectCount(queryWrapper);
		return count==0?false:true;
	}
}
