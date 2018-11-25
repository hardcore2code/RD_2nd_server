package com.mine.rd.interceptors;

import java.util.Map;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.plugin.ehcache.CacheKit;
import com.mine.pub.kit.JsonMyKit;
import com.mine.pub.pojo.CommonDao;
/**
 * @author woody
 * @date 2015-3-15
 * @校验缓存是否存在登录信息
 * */
public class CheckRoleMenuInterceptor implements Interceptor{

	@Override
	public void intercept(Invocation ai) {
		String str = ai.getController().getPara("params");
		@SuppressWarnings("unchecked")
		Map<String,Object> map = JsonMyKit.parse(str, Map.class);
		boolean flag = true;
    	Object sessionId = map.get("IWBSESSION");
    	Map<String, Object> userInfo = CacheKit.get("mySession", sessionId);;
    	//判断是否是手机端访问
    	if(sessionId != null && !"".equals(sessionId) && sessionId.toString().indexOf("BROWSER") >= 0){
    		CommonDao dao = new CommonDao();
    		Object currentUrl = map.get("CURRENT_URL");
    		Object userId = map.get("USER_ID");
    		if(currentUrl != null && !"".equals(currentUrl) && userId != null && !"".equals(userId)){
    			//修改密码功能没有存入角色和功能勾稽表，该请求直接放行
    			if("/dashboard/modifyPwd".equals(currentUrl)){
    				flag = true;
    			}else{
    				flag = dao.checkUserMenu(userId.toString(), currentUrl.toString());
    			}
    		}else{
    			flag = false;
    		}
    	}
		if(!flag)
		{
			ai.getController().getResponse().setHeader("Access-Control-Allow-Origin", "*");
			ai.getController().setAttr("msg", "当前用户不能使用该功能！");
			ai.getController().setAttr("resFlag", "2");
			ai.getController().setAttr("IWBSESSION", sessionId);
			ai.getController().setAttrs(userInfo);
			ai.getController().renderJson();
		}else
		{
			ai.invoke();
		}
	}
}

