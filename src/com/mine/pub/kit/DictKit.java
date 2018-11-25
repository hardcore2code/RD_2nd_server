package com.mine.pub.kit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.mine.pub.pojo.BaseDao;

public class DictKit extends BaseDao {
	
	private static DictKit dk=null;
	private static Map<String,Object> dicts=new HashMap<String, Object>();
	
	public static DictKit getDictKit() {
		if(dk == null)
		{
			dk=new DictKit();
		}
		return dk;
	}

	public static String getEosDictName(String dictTypeId,String dictId)
	{
		return getDictName(dictTypeId,dictId,"DICTID","DICTNAME","EOS_DICT_ENTRY","DICTTYPEID");
	}

	private static String getDictName(String dictTypeId,String dictId,
			String idCol,String nameCol,String tabName,String whereCol) {
		Object object = dicts.get(dictTypeId);
		if(object == null)
		{
			getDicts(dictTypeId,idCol,nameCol,tabName,whereCol);
		}
		return ((Map<String,String>)dicts.get(dictTypeId)).get(dictId);
	}
	
	public static Map<String,String> getEosDicts(String dictTypeId){
		return getDicts(dictTypeId,"DICTID","DICTNAME","EOS_DICT_ENTRY","DICTTYPEID");
	}

	public synchronized static Map<String, String> getDicts(String dictTypeId,
			String idCol,String nameCol,String tabName,String whereCol) {
		Object object = dicts.get(dictTypeId);
		Map<String,String> result=new HashMap<String, String>();
		if(object == null)
		{
			queryDictInfos(dictTypeId,idCol,nameCol,tabName,whereCol,result);
			if(dictTypeId != null)
			{
				dicts.put(dictTypeId, result);
			}
			else
			{
				dicts.put(tabName, result);
			}
		}
		else
		{
			result=(Map<String,String>)object;
		}
		return result;
	}

	private static void queryDictInfos(String dictTypeId,
			String idCol,String nameCol,String tabName,String whereCol,
			Map<String, String> result) {
		List<Record> recordList = null;
		String sql="select t."+idCol+",t."+nameCol+" from "+tabName+" t";
		if(dictTypeId != null)
		{
			sql=sql+" where t."+whereCol+"=?";
			recordList=Db.find(sql,dictTypeId);
		}
		else
		{
			recordList=Db.find(sql);
		}
		if(recordList.size() > 0){
			for (Record record : recordList) {
				result.put(record.get(idCol).toString(), record.get(nameCol).toString());
			}
		}
	}
	
	public static void main(String[] args) {
		System.out.println(getEosDictName("ABF_APPTYPE","0"));
	}

}
