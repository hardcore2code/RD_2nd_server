package com.mine.pub.pojo;

import java.util.List;

import com.google.common.collect.Lists;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.ehcache.CacheKit;

/**
 * @author woody
 * @date 20160325
 * excel dao
 * */
public class ExcelDemoDao extends BaseDao
{
	/**
	 * @author woody
	 * @date 20150919
	 * @查询单元list
	 * */
	@SuppressWarnings("unchecked")
	public List<String> querySheets()
	{
		return CacheKit.getKeys("mydict");
	}
	/**
	 * @author woody
	 * @date 20150919
	 * @查询每个单元的明细
	 * */
	public List<Record> queryListForSheet(String dictType)
	{
		return CacheKit.get("mydict", dictType);
	}
	/**
	 * @author woody
	 * @date 20150919
	 * @获取 sheet页和data headers columns
	 * */
	@SuppressWarnings("rawtypes")
	public void getExcelInfo(String[] sheetNames,List<?>[] sheetAllList,String[][] headers,String[][] columns)
	{
		List sheetlist = querySheets();
		String[] cols = new String[5];
		cols[0] = "id";
		cols[1] = "id_main";
		cols[2] = "dict_id";
		cols[3] = "dict_value";
		cols[4] = "status";
		for(int i = 0 ; i < sheetlist.size() ; i++ )
		{
			List<Object> data = Lists.newArrayList();
			sheetNames[i] = (String) sheetlist.get(i);
			for(Record one : queryListForSheet((String) sheetlist.get(i)))
			{
				data.add(one);
			}
			sheetAllList[i] = data;
			headers[i] = cols;
			columns[i] = cols;
		}
	}

}
