package com.mine.rd.services.role.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.mine.pub.pojo.BaseDao;

public class RoleDao extends BaseDao {

	/**
	 * @author ouyangxu
	 * @date 20170315
	 * 方法：查询角色
	 */
	public Map<String, Object> queryRoleList(){
		List<Record> record = Db.find("select * from WOBO_ROLE where STATUS != '2'");
		List<Map<String, Object>> list = new ArrayList<>();
		Map<String, Object> resMap = new HashMap<String, Object>();
		if(record.size() > 0){
			for(Record role : record){
				Map<String, Object> map = new HashMap<>();
				map.put("ROLE_ID", role.get("ROLE_ID"));
				map.put("NAME", role.get("NAME"));
				if("0".equals(role.get("STATUS"))){
					map.put("STATUS", "保存");
				}else{
					map.put("STATUS", "启用");
				}
				list.add(map);
			}
		}
		resMap.put("roleList", list);
		return resMap;
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170315
	 * 方法：查询角色信息
	 */
	public Map<String, Object> queryRoleInfo(String roleId){
		Record record = Db.findFirst("select * from WOBO_ROLE where STATUS != '2' and ROLE_ID = ?", roleId);
		Map<String, Object> map = new HashMap<>();
		if(record != null){
			map.put("ROLE_ID", record.get("ROLE_ID"));
			map.put("NAME", record.get("NAME"));
			map.put("STATUS", (Integer.parseInt(record.get("STATUS").toString())+1)+"");
		}
		return map;
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170315
	 * 方法：校验角色名称是否存在
	 * @return true-该名称不存在 false-该名称已存在
	 */
	public boolean checkRoleName(String roleId, String roleName){
		String sql = "";
		if("".equals(roleId)){
			sql = "select * from WOBO_ROLE where NAME = ? and STATUS != '2'";
		}else{
			sql = "select * from WOBO_ROLE where NAME = ? and STATUS != '2' and ROLE_ID != '"+roleId+"'";
		}
		Record record = Db.findFirst(sql, roleName);
		if(record != null){
			return false;
		}else{
			return true;
		}
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170315
	 * 方法：新增或修改角色
	 */
	public String addOrUpdateRole(String roleId, String roleName, String status){
		if("".equals(roleId)){
			Record record = new Record();
			String id = super.getSeqId("WOBO_ROLE");
			record.set("ROLE_ID", id);
			record.set("NAME", roleName);
			record.set("STATUS", "0");
			record.set("sysdate", super.getSysdate());
			if(Db.save("WOBO_ROLE", "ROLE_ID", record)){
				return id;
			}else{
				return "";
			}
		}else{
			boolean flag = Db.update("update WOBO_ROLE set NAME = ?, STATUS = ? where ROLE_ID = ?", roleName, status, roleId) > 0;
			if(flag){
				return roleId;
			}else{
				return "";
			}
		}
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170315
	 * 方法：删除角色
	 */
	public boolean delRole(String roleId){
		return Db.update("update WOBO_ROLE set STATUS = '2' where ROLE_ID = ?", roleId) > 0;
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170315
	 * 方法：查询角色关联菜单情况
	 */
	public List<Map<String, Object>> queryRoleMenu(String roleId){
		List<Record> record = Db.find("select a.*,b.ROLE_ID from WOBO_FUNC a left join WOBO_ROLEFUNC b on a.FUNC_ID = b.FUNC_ID and b.ROLE_ID = ? and b.STATUS = '1' where a.STATUS = '1'", roleId);
		List<Map<String, Object>> list = new ArrayList<>();
		if(record.size() > 0){
			for(Record menu : record){
				Map<String, Object> map = new HashMap<>();
				map.put("FUNC_ID", menu.get("FUNC_ID"));
				map.put("NAME", menu.get("NAME"));
				map.put("FUNC_PATH", menu.get("FUNC_PATH"));
				map.put("ACTIONPATH", menu.get("ACTIONPATH"));
				if(menu.get("ROLE_ID") != null && !"".equals(menu.get("ROLE_ID"))){
					map.put("check", "1");
				}else{
					map.put("check", "0");
				}
				list.add(map);
			}
		}
		return list;
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170315
	 * 方法：关联角色菜单
	 */
	public boolean manageRoleMenu(String roleId, String menuIdList){
		Record menu = Db.findFirst("select * from WOBO_ROLEFUNC where ROLE_ID = ?", roleId);
		if(menu != null){
			boolean flag = Db.update("delete from WOBO_ROLEFUNC where ROLE_ID = ?", roleId)>0;
			if(!flag){
				return false;
			}
		}
		if(!"".equals(menuIdList)){
			String[] menuIds = menuIdList.split(",");
			for(String menuId : menuIds){
				Record record = new Record();
				record.set("ROLE_ID", roleId);
				record.set("FUNC_ID", menuId);
				record.set("STATUS", "1");
				if(!Db.save("WOBO_ROLEFUNC", record)){
					return false;
				}
				Record func = Db.findFirst("select * from WOBO_FUNC where FUNC_ID = ? and STATUS = '1'", menuId);
				if(func != null && func.get("FUNC_PARENT") != null && !"".equals(func.get("FUNC_PARENT"))){
					Record funcRole = Db.findFirst("select * from WOBO_ROLEFUNC where FUNC_ID = ? and ROLE_ID = ? and STATUS = '1'", func.get("FUNC_PARENT"),roleId);
					if(funcRole == null){
						Record record1 = new Record();
						record1.set("ROLE_ID", roleId);
						record1.set("FUNC_ID", func.get("FUNC_PARENT"));
						record1.set("STATUS", "1");
						if(!Db.save("WOBO_ROLEFUNC", record1)){
							return false;
						}
					}
				}
			}
		}
		return true;
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170315
	 * 方法：查询角色关联人员情况
	 */
	public Map<String, Object> queryRoleUser(String roleId, int pn, int ps, Object searchContent){
		Page<Record> page = null;
		String sql = "";
		//产生单位管理员
		if("CSDWGLY".equals(roleId)){
			sql = "from (select A.*,B.ROLE_ID from (select a.USER_ID,a.NAME, c.EP_ID, c.EP_NAME from WOBO_USER a, WOBO_PERSON_CS b, WOBO_ENTERPRISE c where a.USER_ID = b.USER_ID and b.EP_ID = c.EP_ID and a.STATUS = '1' and c.STATUS = '1' and c.ORGTYPE = '1') as A left join WOBO_USERROLE B on A.USER_ID = B.USER_ID and B.ROLE_ID = ?) as AA";
//			page = Db.paginate(pn, ps, "select A.*,B.ROLE_ID ", "from (select a.USER_ID,a.NAME, c.EP_ID, c.EP_NAME from WOBO_USER a, WOBO_PERSON_CS b, WOBO_ENTERPRISE c where a.USER_ID = b.USER_ID and b.EP_ID = c.EP_ID and a.STATUS = '1' and c.STATUS = '1' and c.ORGTYPE = '1') as A left join WOBO_USERROLE B on A.USER_ID = B.USER_ID and B.ROLE_ID = ?", roleId);
		}else if("CZDWGLY".equals(roleId)){//处置单位管理员
			sql = "from (select A.*,B.ROLE_ID from (select a.USER_ID,a.NAME, c.EP_ID, c.EP_NAME from WOBO_USER a, WOBO_PERSON_CZ b, WOBO_ENTERPRISE c where a.USER_ID = b.USER_ID and b.EP_ID = c.EP_ID and a.STATUS = '1' and c.STATUS = '1') as A left join WOBO_USERROLE B on A.USER_ID = B.USER_ID and B.ROLE_ID = ?) as AA";
//			page = Db.paginate(pn, ps, "select A.*,B.ROLE_ID ", "from (select a.USER_ID,a.NAME, c.EP_ID, c.EP_NAME from WOBO_USER a, WOBO_PERSON_CZ b, WOBO_ENTERPRISE c where a.USER_ID = b.USER_ID and b.EP_ID = c.EP_ID and a.STATUS = '1' and c.STATUS = '1') as A left join WOBO_USERROLE B on A.USER_ID = B.USER_ID and B.ROLE_ID = ?", roleId);
		}else if("ZCQDW".equals(roleId)){//单位（完善信息审批前）
			sql = "from (select A.*,B.ROLE_ID from (select a.USER_ID,a.NAME, b.EP_ID, b.EP_NAME from WOBO_USER a, WOBO_ENTERPRISE b where a.USER_ID = b.USER_ID and a.STATUS = '0' and b.STATUS = '0') as A left join WOBO_USERROLE B on A.USER_ID = B.USER_ID and B.ROLE_ID = ?) as AA";
//			page = Db.paginate(pn, ps, "select A.*,B.ROLE_ID ", "from (select a.USER_ID,a.NAME, b.EP_ID, b.EP_NAME from WOBO_USER a, WOBO_ENTERPRISE b where a.USER_ID = b.USER_ID and a.STATUS = '0' and b.STATUS = '0') as A left join WOBO_USERROLE B on A.USER_ID = B.USER_ID and B.ROLE_ID = ?", roleId);
		}else if("SJGLY".equals(roleId)){//市级管理员
			sql = "from (select A.*,B.ROLE_ID from (select cast(c.OPERATORID as varchar(20)) as USER_ID, c.USERID as NAME, b.ORGCODE as EP_ID,b.ORGNAME as EP_NAME from OM_EMPLOYEE a, OM_ORGANIZATION b, AC_OPERATOR c, AC_OPERATORROLE d where a.ORGID = b.ORGID and a.OPERATORID = c.OPERATORID and c.STATUS = 'running' and c.OPERATORID = d.OPERATORID and d.ROLEID='SJSPROLE') as A left join WOBO_USERROLE B on A.USER_ID = B.USER_ID and B.ROLE_ID = ?) as AA";
//			page = Db.paginate(pn, ps, "select A.*,B.ROLE_ID ", "from (select cast(c.OPERATORID as varchar(20)) as USER_ID, c.USERID as NAME, b.ORGCODE as EP_ID,b.ORGNAME as EP_NAME from OM_EMPLOYEE a, OM_ORGANIZATION b, AC_OPERATOR c, AC_OPERATORROLE d where a.ORGID = b.ORGID and a.OPERATORID = c.OPERATORID and c.STATUS = 'running' and c.OPERATORID = d.OPERATORID and d.ROLEID='SJSPROLE') as A left join WOBO_USERROLE B on A.USER_ID = B.USER_ID and B.ROLE_ID = ?", roleId);
		}else if("QJGLY".equals(roleId)){//区级管理员
			sql = "from (select A.*,B.ROLE_ID from (select cast(c.OPERATORID as varchar(20)) as USER_ID, c.USERID as NAME, b.ORGCODE as EP_ID,b.ORGNAME as EP_NAME from OM_EMPLOYEE a, OM_ORGANIZATION b, AC_OPERATOR c, AC_OPERATORROLE d where a.ORGID = b.ORGID and a.OPERATORID = c.OPERATORID  and c.STATUS = 'running' and c.OPERATORID = d.OPERATORID and d.ROLEID='QJSPROLE') as A left join WOBO_USERROLE B on A.USER_ID = B.USER_ID  and B.ROLE_ID = ?) as AA";
//			page = Db.paginate(pn, ps, "select A.*,B.ROLE_ID ", "from (select cast(c.OPERATORID as varchar(20)) as USER_ID, c.USERID as NAME, b.ORGCODE as EP_ID,b.ORGNAME as EP_NAME from OM_EMPLOYEE a, OM_ORGANIZATION b, AC_OPERATOR c, AC_OPERATORROLE d where a.ORGID = b.ORGID and a.OPERATORID = c.OPERATORID  and c.STATUS = 'running' and c.OPERATORID = d.OPERATORID and d.ROLEID='QJSPROLE') as A left join WOBO_USERROLE B on A.USER_ID = B.USER_ID  and B.ROLE_ID = ?", roleId);
		}else if("ADMIN".equals(roleId)){//系统管理员
			sql = "from (select A.*,B.ROLE_ID from (select a.USER_ID,a.NAME, b.EP_ID, b.EP_NAME from WOBO_USER a,WOBO_ENTERPRISE b where a.USER_ID = b.USER_ID and a.STATUS= '1' and b.STATUS = '1' and b.IF_PRODUCE = '0' and b.IF_HANDLE = '0') as A left join WOBO_USERROLE B on A.USER_ID = B.USER_ID and B.ROLE_ID = ?) as AA";
//			page = Db.paginate(pn, ps, "select A.*,B.ROLE_ID ", "from (select a.USER_ID,a.NAME, b.EP_ID, b.EP_NAME from WOBO_USER a,WOBO_ENTERPRISE b where a.USER_ID = b.USER_ID and a.STATUS= '1' and b.STATUS = '1' and b.IF_PRODUCE = '0' and b.IF_HANDLE = '0') as A left join WOBO_USERROLE B on A.USER_ID = B.USER_ID and B.ROLE_ID = ?", roleId);
		}else if("ZCHCSDW".equals(roleId)){//产生单位(完善信息审批后)
			sql = "from (select A.*,B.ROLE_ID from (select a.USER_ID,a.NAME, b.EP_ID, b.EP_NAME from WOBO_USER a, WOBO_ENTERPRISE b where a.USER_ID = b.USER_ID and a.STATUS = '1' and b.STATUS = '1' and b.IF_PRODUCE = '1') as A left join WOBO_USERROLE B on A.USER_ID = B.USER_ID and B.ROLE_ID = ?) as AA";
//			page = Db.paginate(pn, ps, "select A.*,B.ROLE_ID ", "from (select a.USER_ID,a.NAME, b.EP_ID, b.EP_NAME from WOBO_USER a, WOBO_ENTERPRISE b where a.USER_ID = b.USER_ID and a.STATUS = '1' and b.STATUS = '1' and b.IF_PRODUCE = '1') as A left join WOBO_USERROLE B on A.USER_ID = B.USER_ID and B.ROLE_ID = ?", roleId);
		}else if("ZCHCZDW".equals(roleId)){//处置单位(完善信息审批后)
			sql = "from (select A.*,B.ROLE_ID from (select a.USER_ID,a.NAME, b.EP_ID, b.EP_NAME from WOBO_USER a, WOBO_ENTERPRISE b where a.USER_ID = b.USER_ID and a.STATUS = '1' and b.STATUS = '1' and b.IF_HANDLE = '1') as A left join WOBO_USERROLE B on A.USER_ID = B.USER_ID and B.ROLE_ID = ?) as AA";
//			page = Db.paginate(pn, ps, "select A.*,B.ROLE_ID ", "from (select a.USER_ID,a.NAME, b.EP_ID, b.EP_NAME from WOBO_USER a, WOBO_ENTERPRISE b where a.USER_ID = b.USER_ID and a.STATUS = '1' and b.STATUS = '1' and b.IF_HANDLE = '1') as A left join WOBO_USERROLE B on A.USER_ID = B.USER_ID and B.ROLE_ID = ?", roleId);
		}else if("ZZDWGLY".equals(roleId)){//产生单位管理员（中转单位）
			sql = "from (select A.*,B.ROLE_ID from (select a.USER_ID,a.NAME, c.EP_ID, c.EP_NAME from WOBO_USER a, WOBO_PERSON_CS b, WOBO_ENTERPRISE c where a.USER_ID = b.USER_ID and b.EP_ID = c.EP_ID and a.STATUS = '1' and c.STATUS = '1' and c.ORGTYPE = '0') as A left join WOBO_USERROLE B on A.USER_ID = B.USER_ID and B.ROLE_ID = ?) as AA";
//			page = Db.paginate(pn, ps, "select A.*,B.ROLE_ID ", "from (select a.USER_ID,a.NAME, c.EP_ID, c.EP_NAME from WOBO_USER a, WOBO_PERSON_CS b, WOBO_ENTERPRISE c where a.USER_ID = b.USER_ID and b.EP_ID = c.EP_ID and a.STATUS = '1' and c.STATUS = '1' and c.ORGTYPE = '0') as A left join WOBO_USERROLE B on A.USER_ID = B.USER_ID and B.ROLE_ID = ?", roleId);
		}else{
			sql = "from (select A.*,B.ROLE_ID from ((select a.USER_ID,a.NAME, b.EP_ID, b.EP_NAME from WOBO_USER a,WOBO_ENTERPRISE b where a.USER_ID = b.USER_ID and a.STATUS= '1' and b.STATUS = '1') union (select aa.USER_ID,aa.NAME, cc.EP_ID, cc.EP_NAME from WOBO_USER aa, WOBO_PERSON_CS bb, WOBO_ENTERPRISE cc where aa.USER_ID = bb.USER_ID and bb.EP_ID = cc.EP_ID and aa.STATUS = '1' and cc.STATUS = '1') union (select aaa.USER_ID,aaa.NAME, ccc.EP_ID, ccc.EP_NAME from WOBO_USER aaa, WOBO_PERSON_CZ bbb, WOBO_ENTERPRISE ccc where aaa.USER_ID = bbb.USER_ID and bbb.EP_ID = ccc.EP_ID and aaa.STATUS = '1' and ccc.STATUS = '1') union (select cast(cccc.OPERATORID as varchar(20)) as USER_ID, cccc.USERID as NAME, bbbb.ORGCODE as EP_ID,  bbbb.ORGNAME as EP_NAME from OM_EMPLOYEE aaaa, OM_ORGANIZATION bbbb, AC_OPERATOR cccc where aaaa.ORGID = bbbb.ORGID and aaaa.OPERATORID = cccc.OPERATORID and cccc.STATUS = 'running')) as A left join WOBO_USERROLE B on A.USER_ID = B.USER_ID and B.ROLE_ID = ?) as AA";
//			page = Db.paginate(pn, ps, "select A.*,B.ROLE_ID ", "from ((select a.USER_ID,a.NAME, b.EP_ID, b.EP_NAME from WOBO_USER a,WOBO_ENTERPRISE b where a.USER_ID = b.USER_ID and a.STATUS= '1' and b.STATUS = '1') union (select aa.USER_ID,aa.NAME, cc.EP_ID, cc.EP_NAME from WOBO_USER aa, WOBO_PERSON_CS bb, WOBO_ENTERPRISE cc where aa.USER_ID = bb.USER_ID and bb.EP_ID = cc.EP_ID and aa.STATUS = '1' and cc.STATUS = '1') union (select aaa.USER_ID,aaa.NAME, ccc.EP_ID, ccc.EP_NAME from WOBO_USER aaa, WOBO_PERSON_CZ bbb, WOBO_ENTERPRISE ccc where aaa.USER_ID = bbb.USER_ID and bbb.EP_ID = ccc.EP_ID and aaa.STATUS = '1' and ccc.STATUS = '1') union (select cast(cccc.OPERATORID as varchar(20)) as USER_ID, cccc.USERID as NAME, bbbb.ORGCODE as EP_ID,  bbbb.ORGNAME as EP_NAME from OM_EMPLOYEE aaaa, OM_ORGANIZATION bbbb, AC_OPERATOR cccc where aaaa.ORGID = bbbb.ORGID and aaaa.OPERATORID = cccc.OPERATORID and cccc.STATUS = 'running')) as A left join WOBO_USERROLE B on A.USER_ID = B.USER_ID and B.ROLE_ID = ?", roleId);
		}
		if(searchContent != null && !"".equals(searchContent)){
			sql = sql + " where (AA.USER_ID like '%"+searchContent+"%' or AA.NAME like '%"+searchContent+"%' or AA.EP_NAME like '%"+searchContent+"%')";
		}
		sql = sql + " group by AA.EP_NAME,AA.USER_ID,AA.NAME,AA.EP_ID,AA.ROLE_ID";
//		page = Db.paginate(pn, ps, "select A.*,B.ROLE_ID ", "from ((select a.USER_ID,a.NAME, b.EP_ID, b.EP_NAME from WOBO_USER a,WOBO_ENTERPRISE b where a.USER_ID = b.USER_ID and a.STATUS= '1' and b.STATUS = '1') union (select aa.USER_ID,aa.NAME, cc.EP_ID, cc.EP_NAME from WOBO_USER aa, WOBO_PERSON_CS bb, WOBO_ENTERPRISE cc where aa.USER_ID = bb.USER_ID and bb.EP_ID = cc.EP_ID and aa.STATUS = '1' and cc.STATUS = '1') union (select aaa.USER_ID,aaa.NAME, ccc.EP_ID, ccc.EP_NAME from WOBO_USER aaa, WOBO_PERSON_CZ bbb, WOBO_ENTERPRISE ccc where aaa.USER_ID = bbb.USER_ID and bbb.EP_ID = ccc.EP_ID and aaa.STATUS = '1' and ccc.STATUS = '1') union (select cast(cccc.OPERATORID as varchar(20)) as USER_ID, cccc.USERID as NAME, bbbb.ORGCODE as EP_ID,  bbbb.ORGNAME as EP_NAME from OM_EMPLOYEE aaaa, OM_ORGANIZATION bbbb, AC_OPERATOR cccc where aaaa.ORGID = bbbb.ORGID and aaaa.OPERATORID = cccc.OPERATORID and cccc.STATUS = 'running')) as A left join WOBO_USERROLE B on A.USER_ID = B.USER_ID and B.ROLE_ID = ?", roleId);
		page = Db.paginate(pn, ps, "select AA.*", sql, roleId);
		List<Record> record = page.getList();
//		List<Record> record = Db.find("select A.*,B.ROLE_ID from ((select a.USER_ID,a.NAME, b.EP_ID, b.EP_NAME from WOBO_USER a,WOBO_ENTERPRISE b where a.USER_ID = b.USER_ID and a.STATUS= '1' and b.STATUS = '1') union (select aa.USER_ID,aa.NAME, cc.EP_ID, cc.EP_NAME from WOBO_USER aa, WOBO_PERSON_CS bb, WOBO_ENTERPRISE cc where aa.USER_ID = bb.USER_ID and bb.EP_ID = cc.EP_ID and aa.STATUS = '1' and cc.STATUS = '1') union (select aaa.USER_ID,aaa.NAME, ccc.EP_ID, ccc.EP_NAME from WOBO_USER aaa, WOBO_PERSON_CZ bbb, WOBO_ENTERPRISE ccc where aaa.USER_ID = bbb.USER_ID and bbb.EP_ID = ccc.EP_ID and aaa.STATUS = '1' and ccc.STATUS = '1') union (select cast(cccc.OPERATORID as varchar(20)) as USER_ID, cccc.USERID as NAME, bbbb.ORGCODE as EP_ID,  bbbb.ORGNAME as EP_NAME from OM_EMPLOYEE aaaa, OM_ORGANIZATION bbbb, AC_OPERATOR cccc where aaaa.ORGID = bbbb.ORGID and aaaa.OPERATORID = cccc.OPERATORID and cccc.STATUS = 'running')) as A left join WOBO_USERROLE B on A.USER_ID = B.USER_ID and B.ROLE_ID = ?", roleId);
		List<Map<String, Object>> list = new ArrayList<>();
		Map<String, Object> resMap = new HashMap<String, Object>();
		if(record.size() > 0){
			for(Record menu : record){
				Map<String, Object> map = new HashMap<>();
				map.put("USER_ID", menu.get("USER_ID"));
				map.put("NAME", menu.get("NAME"));
				map.put("EP_ID", menu.get("EP_ID"));
				map.put("EP_NAME", menu.get("EP_NAME"));
				if(menu.get("ROLE_ID") != null && !"".equals(menu.get("ROLE_ID"))){
					map.put("check", "1");
				}else{
					map.put("check", "0");
				}
				list.add(map);
			}
		}
		resMap.put("userList", list);
		resMap.put("totalPage", page.getTotalPage());
		resMap.put("totalRow", page.getTotalRow());
		return resMap;
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170315
	 * 方法：关联角色人员
	 */
	public boolean manageRoleUser(String roleId, String userIdList, String allUserId){
		if(!"".equals(allUserId)){
			String[] userIds = allUserId.split(",");
			for(String userId : userIds){
				Record user = Db.findFirst("select * from WOBO_USERROLE where ROLE_ID = ? and USER_ID = ? and STATUS = '1'", roleId,userId);
				if(user != null){
					boolean flag = Db.update("delete from WOBO_USERROLE where ROLE_ID = ? and USER_ID = ?", roleId,userId)>0;
					if(!flag){
						return false;
					}
				}
			}
		}
		if(!"".equals(userIdList)){
			String[] userIds = userIdList.split(",");
			for(String userId : userIds){
				Record record = new Record();
				record.set("ROLE_ID", roleId);
				record.set("USER_ID", userId);
				record.set("STATUS", "1");
				if(!Db.save("WOBO_USERROLE", record)){
					return false;
				}
			}
		}
		return true;
	}
	
}
