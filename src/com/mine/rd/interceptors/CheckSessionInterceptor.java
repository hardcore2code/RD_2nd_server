package com.mine.rd.interceptors;

import java.util.Map;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.plugin.ehcache.CacheKit;
import com.mine.pub.kit.JsonMyKit;
/**
 * @author woody
 * @date 2015-3-15
 * @校验缓存是否存在登录信息
 * */
public class CheckSessionInterceptor implements Interceptor{
	
	@Override
	public void intercept(Invocation ai) {
		String str = ai.getController().getPara("params");
		@SuppressWarnings("unchecked")
		Map<String,Object> map = JsonMyKit.parse(str, Map.class);
		Object sessionId = map.get("IWBSESSION");
		//sessionId不存在
		if(sessionId == null || "".equals(sessionId) || CacheKit.get("mySession", sessionId) == null || 
			"".equals(CacheKit.get("mySession", sessionId)))
		{
			System.err.println("======>>登陆时间超长或是缓存已清空，退出重新登陆！<<======");
			ai.getController().getResponse().setHeader("Access-Control-Allow-Origin", "*");
			ai.getController().setAttr("IWBSESSION", "");
			ai.getController().renderJson();
		}else{
			ai.invoke();
		}
	}
}

