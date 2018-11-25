package com.mine.rd1.services.statistics.pojo;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.mine.pub.pojo.BaseDao;

public class ExecuteSqlDao extends BaseDao {
	
	
	/**
	 * @author zyl
	 * @date 20170519
	 * 方法：执行语句
	 */
	public HashMap<String, Object> executeSql(String sql){
		String tableStr="";
		String titleStr="";
		String rowStr="";
		HashMap<String, Object> resultMap=new HashMap<String, Object>();
		sql=sql.toUpperCase();
		if(sql.indexOf("SELECT") > -1)
		{
			List<Record> find = Db.find(sql);
			resultMap.put("influenceRows", find.size());
			List<HashMap<String, Object>> list=null;
			if(find.size() <= 50 && find.size() > 0)
			{
				tableStr="<table class='table'><caption>查询结果列表：</caption><thead><tr>";
				list=new ArrayList<HashMap<String,Object>>();
				HashMap<String, Object> map=null;
				String[] columnNames = find.get(0).getColumnNames();
				int i=0;
				List<HashMap<String, Object>> columns=new ArrayList<HashMap<String,Object>>();
				HashMap<String, Object> column=null;
				int j=1;
				for (Record record : find) {
					rowStr=rowStr+"<tr>";
					map=new HashMap<String, Object>();
					for (String key : columnNames) {
						if(i==0)
						{
							titleStr=titleStr+"<th>"+key+"</th>";
							column=new HashMap<String, Object>();
							column.put("data", key);
							columns.add(column);
							if(j==columnNames.length)
							{
								tableStr=tableStr+titleStr+"</tr></thead><tbody>";
							}
							j++;
						}
						map.put(key, record.get(key));
						String val=record.get(key) == null ? "" : record.get(key).toString();
						rowStr=rowStr+"<td>"+val+"</td>";
					}
					rowStr=rowStr+"</tr>";
					i++;
					list.add(map);
				}
				resultMap.put("columns", columns);
			}
			tableStr=tableStr+rowStr+"</tbody></table>";
			resultMap.put("list", list);
			resultMap.put("tableHtml", tableStr);
		}
		else
		{
			resultMap.put("influenceRows", Db.update(sql));
		}
		return resultMap;
	}
	
}
