package com.mine.rd.services.menu.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.mine.pub.pojo.BaseDao;

public class MenuDao extends BaseDao {

	/**
	 * @author ouyangxu
	 * @date 20170316
	 * 方法：查询菜单（支持六级）
	 */
	public List<Map<String, Object>> queryMenu() {
		String sql = "select * from WOBO_FUNC where STATUS in ('0','1') order by SORT asc";
		List<Record> list = Db.find(sql);
		String level = "FUNC_ID";
		String belongId = "FUNC_PARENT";
		String sort = "SORT";
		String name = "NAME";
		String grade = "LEVEL";
		List<Map<String, Object>> returnList = new ArrayList<>();
		if (list.size() > 0) {
			int a = 0;
			int position = 0;
			for (Record record1 : list) {
				if ("1".equals(record1.get(grade).toString())) {
					Map<String, Object> level1 = new HashMap<>();
					level1.put("level", record1.get(level));
					level1.put("belongId", record1.get(belongId));
					level1.put("sort", record1.get(sort));
					level1.put("name", record1.get(name));
					level1.put("type", this.querySonMenu(record1.get(level).toString()));
					level1.put("grade", record1.get(grade));
					level1.put("index", a+"");
					level1.put("position", position);
					position++;
					List<Map<String, Object>> list2 = new ArrayList<>();
					int b = 0;
					for (Record record2 : list) {
						if ("2".equals(record2.get(grade).toString()) && record2.get(belongId).equals(record1.get(level))) {
							Map<String, Object> level2 = new HashMap<>();
							level2.put("level", record2.get(level));
							level2.put("belongId", record2.get(belongId));
							level2.put("sort", record2.get(sort));
							level2.put("name", record2.get(name));
							level2.put("type", this.querySonMenu(record2.get(level).toString()));
							level2.put("grade", record2.get(grade));
							level2.put("index", a+","+b);
							level2.put("position", position);
							position++;
							List<Map<String, Object>> list3 = new ArrayList<>();
							int c=0;
							for (Record record3 : list) {
								if ("3".equals(record3.get(grade).toString()) && record3.get(belongId).equals(record2.get(level))) {
									Map<String, Object> level3 = new HashMap<>();
									level3.put("level", record3.get(level));
									level3.put("belongId", record3.get(belongId));
									level3.put("sort", record3.get(sort));
									level3.put("name", record3.get(name));
									level3.put("type", this.querySonMenu(record3.get(level).toString()));
									level3.put("grade", record3.get(grade));
									level3.put("index", a+","+b+","+c);
									level3.put("position", position);
									position++;
									List<Map<String, Object>> list4 = new ArrayList<>();
									int d=0;
									for (Record record4 : list) {
										if ("4".equals(record4.get(grade).toString()) && record4.get(belongId).equals(record3.get(level))) {
											Map<String, Object> level4 = new HashMap<>();
											level4.put("level", record4.get(level));
											level4.put("belongId", record4.get(belongId));
											level4.put("sort", record4.get(sort));
											level4.put("name", record4.get(name));
											level4.put("type", this.querySonMenu(record4.get(level).toString()));
											level4.put("grade", record4.get(grade));
											level4.put("index", a+","+b+","+c+","+d);
											level4.put("position", position);
											position++;
											List<Map<String, Object>> list5 = new ArrayList<>();
											int e = 0;
											for (Record record5 : list) {
												if ("5".equals(record5.get(grade).toString())&& record5.get(belongId).equals(record4.get(level))) {
													Map<String, Object> level5 = new HashMap<>();
													level5.put("level", record5.get(level));
													level5.put("belongId", record5.get(belongId));
													level5.put("sort", record5.get(sort));
													level5.put("name", record5.get(name));
													level5.put("type", this.querySonMenu(record5.get(level).toString()));
													level5.put("grade", record5.get(grade));
													level5.put("index", a+","+b+","+c+","+d+","+e);
													level5.put("position", position);
													position++;
													List<Map<String, Object>> list6 = new ArrayList<>();
													int f = 0;
													for (Record record6 : list) {
														if ("6".equals(record6.get(grade).toString())&& record6.get(belongId).equals(record5.get(level))) {
															Map<String, Object> level6 = new HashMap<>();
															level6.put("level", record6.get(level));
															level6.put("belongId", record6.get(belongId));
															level6.put("sort", record6.get(sort));
															level6.put("name", record6.get(name));
															level6.put("type", this.querySonMenu(record6.get(level).toString()));
															level6.put("grade", record6.get(grade));
															level6.put("index", a+","+b+","+c+","+d+","+e+","+f);
															level6.put("position", position);
															position++;
															List<Map<String, Object>> list7 = new ArrayList<>();
															level6.put("children", list7);
															list6.add(level6);
															f++;
														}
													}
													level5.put("children", list6);
													list5.add(level5);
													e++;
												}
											}
											level4.put("children", list5);
											list4.add(level4);
											d++;
										}
									}
									level3.put("children", list4);
									list3.add(level3);
									c++;
								}
							}
							level2.put("children", list3);
							list2.add(level2);
							b++;
						}
					}
					level1.put("children", list2);
					returnList.add(level1);
					a++;
				}
			}
		}
		return returnList;
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170316
	 * 方法：查询菜单显示图标
	 */
	public String querySonMenu(String menuId){
		List<Record> list = Db.find("select * from WOBO_FUNC where FUNC_PARENT = ? and STATUS in ('0','1')", menuId);
		if(list.size()>0){
			return "folder";
		}else{
			return "doc";
		}
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170316
	 * 方法：查询单个菜单信息
	 */
	public Map<String, Object> queryMenuInfo(String menuId){
		Record record = Db.findFirst("select * from WOBO_FUNC where FUNC_ID = ? and STATUS in ('0','1')", menuId);
		Map<String, Object> map = new HashMap<>();
		if(record != null){
			map.put("menuName", record.get("NAME"));
			map.put("status", record.get("STATUS"));
		}
		return map;
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170316
	 * 方法：修改菜单状态
	 */
	public boolean updateMenu(String menuId, String status, String menuName){
		String sql1 = "update WOBO_FUNC set STATUS = ?, NAME=? where FUNC_ID = ?";
		String sql2 = "update WOBO_FUNC_ATTR set STATUS = ? where FUNC_ID = ?";
		boolean flag = Db.update(sql1, status, menuName, menuId) > 0;
		if(!flag){
			return false;
		}
		flag = Db.update(sql2, status, menuId) > 0;
		if(!flag){
			return false;
		}
		//查询是否有子菜单
		List<Record> list = Db.find("select * from WOBO_FUNC where FUNC_PARENT = ? and STATUS in ('0','1')", menuId);
		if(list.size()<0){
			return true;
		}
		for(Record func : list){
			flag = Db.update("update WOBO_FUNC set STATUS = ? where FUNC_ID = ?", status, func.get("FUNC_ID").toString()) > 0;
			if(!flag){
				return false;
			}
			flag = Db.update(sql2, status, func.get("FUNC_ID").toString()) > 0;
			if(!flag){
				return false;
			}
		}
		return true;
	}
}
