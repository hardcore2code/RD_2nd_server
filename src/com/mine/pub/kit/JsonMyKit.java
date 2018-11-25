package com.mine.pub.kit;

import com.jfinal.json.Jackson;


/**
 * @author woody
 * @date 20160308
 * @json工具类
 * */
public class JsonMyKit 
{
	public static String toJson(Object object) {
		return Jackson.getJson().toJson(object);
	}
	public static <T> T parse(String jsonString, Class<T> type) {
		return Jackson.getJson().parse(jsonString, type);
	}
}
