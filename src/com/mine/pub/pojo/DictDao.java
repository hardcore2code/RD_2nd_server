package com.mine.pub.pojo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.ehcache.CacheKit;

/**
 * @author woody
 * @date 20160229
 * @字典表
 * */
public class DictDao 
{
	/**
	 * @查询字典主表
	 * */
	public static List<Record> queryDictMain()
	{
		return Db.find("select * from pub_dict_main a where  a.status = '1'");
//		return null;
	}
	/**
	 * @查询字典子表
	 * */
	public static List<Record> queryDict(int mainId)
	{
		return Db.find("select * from pub_dict a where a.id_main = ?  and  a.status = '1' order by dict_id asc" ,mainId);
//		return null;
	}
	/**
	 * @author woody
	 * @date 20160229
	 * @初始化cache
	 * */
	public static void initCache()
	{
		List<Record> list = queryDictMain();
		if(list != null)
		{
			CacheKit.removeAll("mydict");
			for(Record record : list)
			{
				CacheKit.put("mydict", record.getStr("name"), queryDict(record.getInt("id")));
			}
			initSession();
		}
	}
	/**
	 * @author woody
	 * @date 20160705
	 * @初始化cache session
	 * */
	public static void initSession()
	{
		System.out.println("=========================initSession - start=============================");
		List<Record> list = Db.find("select * from IWOBO_SESSION");
		Map<String, Object> map = null;
		for(Record recordForSessionId : list)
		{
			String sessionId = recordForSessionId.getStr("SESSION_ID");
			map = new HashMap<String, Object>();
			for(Record record : list)
			{
				if(sessionId.equals(record.getStr("SESSION_ID")) && CacheKit.get("mySession", sessionId) == null)
				{
					map.put(record.getStr("IWB_KEY"), record.getStr("IWB_VALUE"));
				}
			}
			if(CacheKit.get("mySession", sessionId) == null)
			{
				CacheKit.put("mySession", sessionId, map);
			}
		}
		System.out.println("=========================initSession - end=============================");
	}

}
