package com.mine.run.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.jfinal.config.Routes;
import com.jfinal.core.Controller;
import com.jfinal.json.Jackson;
import com.jfinal.kit.PropKit;

/**
 * @author woody
 * @date 201602015
 * @路由配置
 * */
class RoutesSetting 
{
	
	public static Routes setRoutes(Routes me)
	{
		String json = PropKit.get("actionmap").toString();
		@SuppressWarnings("unchecked")
		List<HashMap<String,String>> list = Jackson.getJson().parse(json,ArrayList.class);
		try {
			for(HashMap<String,String> map : list)
			{
				// 第三个参数为该Controller的视图存放路径
				// 第三个参数省略时默认与第一个参数值相同，在此即为 "/blog"
				me.add(map.get("key"), ((Controller)Class.forName(map.get("value")).newInstance()).getClass(),map.get("path"));
			}
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return me;
	}
}
