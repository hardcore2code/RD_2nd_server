package com.mine.pub.liveshow;

import java.util.Map;

import com.jfinal.kit.HttpKit;
import com.jfinal.kit.PropKit;

public class LiveShowUtil 
{
	public String getHttp(String url , Map<String,String> map)
	{
		url = PropKit.get("liveshow_url")+url;
		map.put("loginName", PropKit.get("liveshow_loginName"));
		map.put("password", PropKit.get("liveshow_password"));
		map.put("sec", PropKit.get("liveshow_sec"));
		return HttpKit.get(url, map);
	}
}
