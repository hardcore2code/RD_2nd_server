package com.mine.rd.services.setBoxSuttle.pojo;

import com.jfinal.plugin.activerecord.Db;
import com.mine.pub.pojo.BaseDao;

public class SetBoxSuttleDao extends BaseDao {
	
	/**
	 * @author weizanting
	 * @date 20171016
	 * 方法：保存或修改桶净重
	 */
	public boolean setBoxSuttle(String str){
		return Db.update("update PUB_DICT set dict_value = ? where dict_id = 'box_suttle'", str) > 0;
	}
	
}
