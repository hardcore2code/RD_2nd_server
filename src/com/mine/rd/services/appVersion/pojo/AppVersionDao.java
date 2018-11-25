package com.mine.rd.services.appVersion.pojo;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.mine.pub.pojo.BaseDao;

public class AppVersionDao extends BaseDao {
	/**
	 * @author ouyangxu
	 * @date 20170906
	 * 方法：检查app是否有更新
	 * @return true-有更新 false-无更新
	 */
	public boolean checkAppVersion(int appId, String appVersion){
		Record record = Db.findFirst("select * from IWOBO_APP where ID = ?", appId);
		if(record != null){
			int version = Integer.parseInt(record.get("VERSION").toString());
			if(version > Integer.parseInt(appVersion)){
				return true;
			}
		}
		return false;
	}
}
