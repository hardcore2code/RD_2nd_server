package com.mine.rd.interceptors;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.json.Jackson;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.ehcache.CacheKit;
import com.mine.pub.kit.DESKit;
import com.mine.pub.kit.DecryptKit;
import com.mine.pub.kit.JsonMyKit;
import com.mine.pub.pojo.CommonDao;
/**
 * @author woody
 * @date 2015-3-15
 * @校验缓存是否存在登录信息
 * */
public class LoginInterceptor implements Interceptor{

	@Override
	public void intercept(Invocation ai) {
		String str = ai.getController().getPara("params");
		@SuppressWarnings("unchecked")
		Map<String,Object> map = JsonMyKit.parse(str, Map.class);
		Object sessionId = map.get("IWBSESSION");
		Object WJWT = map.get("WJWT");
		//sessionId不存在
		if(sessionId == null || "".equals(sessionId) || CacheKit.get("mySession", sessionId) == null || 
			"".equals(CacheKit.get("mySession", sessionId)))
		{
			System.err.println("======>>登陆时间超长或是缓存已清空，退出重新登陆！<<======");
			ai.getController().getResponse().setHeader("Access-Control-Allow-Origin", "*");
			ai.getController().setAttr("IWBSESSION", "");
			ai.getController().renderJson();
		//未传WJWT字段(登录等接口不进该interceptor)
		}else if(sessionId != null && !"".equals(sessionId) && WJWT != null && !"".equals(WJWT)){
			//base64传输
			String WJWTS = DecryptKit.decode(WJWT.toString());
			//des解密
			String key = PropKit.get("codeKey");
			try {
				WJWTS = DESKit.decrypt(WJWTS.toString(), key);
				@SuppressWarnings("unchecked")
				Map<String, Object> tokenMap = Jackson.getJson().parse(WJWTS, Map.class);
				String loginToken = tokenMap.get("loginToken").toString();
				CommonDao dao = new CommonDao();
				int result = checkToken(loginToken, dao.getSysdate());
				//超时
				if(result >= 0){
					System.err.println("======>>登陆时间超长，请重新登陆！<<======");
					dao.logout(sessionId.toString());
					CacheKit.remove("mySession", sessionId);
					ai.getController().getResponse().setHeader("Access-Control-Allow-Origin", "*");
					ai.getController().setAttr("IWBSESSION", "");
					ai.getController().renderJson();
				}else{
					String newToken = dao.getToken();
					dao.procSession(sessionId.toString(), "WJWT", newToken);
					Map<String, Object> sessionMap = CacheKit.get("mySession", sessionId.toString());
					sessionMap.put("WJWT", newToken);
					CacheKit.put("mySession", sessionId.toString(), sessionMap);
					ai.invoke();
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			ai.invoke();
		}
	}
	
	/**
	 * @author woody
	 * @date 20170906
	 * @校验token是否过期
	 * */
	private int checkToken(String token,Timestamp sysdate)
	{
		//token失效天数
		int diff = Integer.parseInt(PropKit.get("loginValidity").toString());
		//token string转时间类型
		Timestamp tokenToDatetime = new Timestamp(Long.parseLong(token));
		Calendar calendarBegin = Calendar.getInstance();
		calendarBegin.setTime(tokenToDatetime);
		//当前时间
		Calendar calendarEnd = Calendar.getInstance();
		calendarEnd.setTime(sysdate);
		//当前时间减去超时时间  >=0 表示超时
		calendarEnd.add(Calendar.MINUTE, -diff);
		int result = calendarEnd.compareTo(calendarBegin);
		return result;
	}
}

