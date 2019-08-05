package com.jt.util;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectMapperUtil {
	
	private static final ObjectMapper mapper = new ObjectMapper();
	
	//1.将对象转化为JSON
	public static String toJSON(Object target) {
		String result = null;
		try {
			result = mapper.writeValueAsString(target);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return result;
	}
	
	//2.将json转化为对象
	public static <T> T toObject(String json,Class<T> targetClass) {
		T t = null;
		try {
			t = mapper.readValue(json, targetClass);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return t;
	}
}
