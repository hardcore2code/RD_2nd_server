package com.mine.pub.proc;


import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.jfinal.plugin.activerecord.ICallback;


public class ProcForQuery implements ICallback{

	private String procName = "";
	private Object[] params = null;
	private List<Map<String,Object>> list = null;
	private ResultSet rs = null;
	private StringBuffer sb = null;
	public ProcForQuery(String proc,Object[] paramobjs)
	{
		this.procName = proc;
		this.params = paramobjs;
	}
	
	@Override
	public Object call(Connection conn) throws SQLException {
		CallableStatement proc = null;
		sb = new StringBuffer();
		sb.append("{ call ");
		sb.append(procName);
		sb.append("(");
		for(int i = 1 ; i <= params.length ; i++)
		{
			if(i == params.length)
			{
				sb.append("?");
			}else
			{
				sb.append("?,");
			}
		}
		sb.append(" ) }");
		proc = conn.prepareCall(sb.toString());
		for(int i = 1 ; i <= params.length ; i++)
		{
			proc.setString(i, params[i-1].toString());
		}
		if(proc.execute())
		{
			rs = proc.getResultSet();
			list = new ArrayList<Map<String,Object>>();
			while(rs.next())
			{
				 Map<String,Object> map = new HashMap<String, Object>();
				 map.put("FUNC_ID", rs.getString("FUNC_ID"));
				 map.put("FUNC_PARENT", rs.getString("FUNC_PARENT"));
				 map.put("IFPARENT", rs.getString("IFPARENT"));
				 map.put("IFROOT", rs.getString("IFROOT"));
				 map.put("NAME", rs.getString("NAME"));
				 map.put("STATUS", rs.getString("STATUS"));
				 map.put("FUNC_DESC", rs.getString("FUNC_DESC"));
				 map.put("ACTIONPATH", rs.getString("ACTIONPATH"));
				 map.put("LEVEL", rs.getInt("LEVEL"));
				 map.put("SORT", rs.getString("SORT"));
				 map.put("I_CLASS", rs.getString("I_CLASS"));
				 map.put("A_HREF", rs.getString("A_HREF"));
				 map.put("A_ONCLICK", "".equals(rs.getString("A_ONCLICK")) ? null : rs.getString("A_ONCLICK"));
				 map.put("A_ID", rs.getString("A_ID"));
				 map.put("SPAN_CLASS", rs.getString("SPAN_CLASS"));
				 map.put("UL_CLASS", rs.getString("UL_CLASS"));
				 list.add(map);
			}
			list = sortMenu(list);
		}
		return list;
	}
	//排序菜单 
	//选择排序算法
	//目前支持2级菜单
	public List<Map<String,Object>> sortMenu(List<Map<String,Object>> menulist)
	{
		List<Map<String,Object>> sortMenu = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> sortMenuNew = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> sortMenuSon = null;
		while(menulist.size() > 0)
		{
			int min = 0;
			for(int j=1;j<menulist.size();j++)
			{
				if(Integer.parseInt(menulist.get(min).get("SORT").toString()) > Integer.parseInt(menulist.get(j).get("SORT").toString()))
				{
					min = j;
				}
			}
			sortMenu.add(menulist.get(min));
			menulist.remove(min);
		}
		for(Map<String,Object> map: sortMenu)
		{
			if("00".equals(map.get("SORT").toString().substring(2))) //sort的后两位是00 ,说明是父节点
			{
				sortMenuSon = new ArrayList<Map<String,Object>>();
				//添加子菜单
				for(Map<String,Object> mapson: sortMenu)
				{
					//前两位与父节点的前两位相等，并且后两位不是00
					if((map.get("SORT").toString().substring(0, 2)).equals(mapson.get("SORT").toString().substring(0, 2)) && !"00".equals(mapson.get("SORT").toString().substring(2)))
					{
						sortMenuSon.add(mapson);
					}
				}
				map.put("son", sortMenuSon);
				sortMenuNew.add(map);
			}
		}
		return sortMenuNew;
	}
}
