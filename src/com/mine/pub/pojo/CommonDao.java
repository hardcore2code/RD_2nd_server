package com.mine.pub.pojo;

import com.jfinal.plugin.activerecord.Db;

public class CommonDao extends BaseDao{
	/**
	 * @author woody
	 * @throws UnsupportedEncodingException 
	 * @date 20151010
	 * @保存数据库的session表
	 * @先删后插
	 * @删除原有记录
	 * */
	public void procSession(String sessionId,String key,String obj) throws Exception
	{
		Db.update("delete from IWOBO_SESSION where SESSION_ID = ? and IWB_KEY = ?", sessionId,key);
		Db.update("insert into IWOBO_SESSION values (?,?,?,?)",sessionId,key,obj,getSysdate());
	}

}
