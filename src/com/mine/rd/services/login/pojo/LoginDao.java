package com.mine.rd.services.login.pojo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.ehcache.CacheKit;
import com.mine.pub.pojo.BaseDao;

public class LoginDao extends BaseDao {
	
	List<Record> cityList = CacheKit.get("mydict", "city_q");
	
	/**
	 * 
	 * @author weizanting
	 * @date 20161221
	 * 方法：登录--是否存在该用户
	 * 
	 */
	public Map<String, Object> login(String username){
		String sql = "select a.USER_ID,b.EP_ID,b.EP_NAME,a.NAME,b.EP_ADRESS_Q,b.EP_ADRESS_S,b.REGISTERCODE,c.PORTRAIT,a.PWD,b.IF_PRODUCE,b.IF_HANDLE,b.BELONGSEPA,b.STATUS from WOBO_USER a, WOBO_ENTERPRISE b, WOBO_USER_INFO c where a.USER_ID = b.USER_ID and a.STATUS in ('0','1') and b.STATUS in ('0','1') and a.USER_ID = c.USER_ID  and a.NAME=? "+super.checkLowerAndUpper();
		Record record =  Db.findFirst(sql, username);
		Map<String, Object> map = new HashMap<>();
		if(record != null){
			map.put("userId", record.get("USER_ID"));
			map.put("epId", record.get("EP_ID"));
			map.put("epName", record.get("EP_NAME"));
			map.put("nickName", record.get("NAME"));
			map.put("ifLogin", "0");
			map.put("pwd", record.get("PWD"));
			if(record.get("EP_ADRESS_Q") != null){
				map.put("belongQ", convert(cityList, record.get("EP_ADRESS_Q")));
			}else{
				map.put("belongQ", "");
			}
			map.put("belongS", record.get("EP_ADRESS_S"));
			map.put("orgCode", record.get("REGISTERCODE"));
			map.put("userPortrait", record.get("PORTRAIT"));
			map.put("belongSepa", record.get("BELONGSEPA"));
			map.put("status", record.get("STATUS"));
			if(record.get("BELONGSEPA") != null){
				map.put("sepaName", convert(cityList, record.get("BELONGSEPA")));
			}else{
				map.put("sepaName", "");
			}
			if("1".equals(record.get("IF_PRODUCE"))){
				map.put("userType", "epCs");
			}
			if("1".equals(record.get("IF_HANDLE"))){
				map.put("userType", "epCz");
			}
			//sysadmin-系统管理员
			if("sysadmin".equals(username)){
				map.put("userType", "sysAdmin");
				map.put("orgCode", "");
			}
		}
		return map;
	}
	
	/**
	 * 
	 * @author weizanting
	 * @date 20161223
	 * 方法：查询session值
	 * 
	 */
	public Map<String , Object> querySession(String sessionId){
		List<Record> session = Db.find("select * from IWOBO_SESSION where SESSION_ID=?", sessionId);
		Map<String , Object> map = new HashMap<String , Object>();
		if(session.size() > 0){
			map.put("SESSIONID", session.get(0).get("SESSION_ID"));
			for(Record record:session){
				map.put(record.get("IWB_KEY").toString(), record.get("IWB_VALUE"));
			}
		}else{
			map.put("SESSIONID", "");
			map.put("ifLogin", "1");
			map.put("mail", "");
			map.put("nickName", "");
			map.put("tel", "");
			map.put("userId", "");
			map.put("userPortrait", "");
		}
		return map;
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170216
	 * 方法：检查单位是否存在
	 * @return true-不存在  false-存在
	 */
	public boolean checkEnterprise(String enterpriseName, String epId){
		String sql = "select * from WOBO_ENTERPRISE where EP_NAME = ? "+super.checkLowerAndUpper()+" and STATUS in ('0','1')";
		if(!"".equals(epId)){
			sql = sql + " and EP_ID <> '"+epId+"'";
		}
		Record record = Db.findFirst(sql, enterpriseName);
		if(record == null){
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @author ouyangxu
	 * @date 20170216
	 * 方法：检查组织机构代码证是否存在
	 * @return true-不存在  false-存在
	 */
	public boolean checkOrgCode(String orgCode, String epId){
		String sql = "select * from WOBO_ENTERPRISE where REGISTERCODE = ? "+super.checkLowerAndUpper()+" and STATUS in ('0','1')";
		if(!"".equals(epId)){
			sql = sql + " and EP_ID <> '"+epId+"'";
		}
		Record record = Db.findFirst(sql, orgCode);
		if(record == null){
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @author ouyangxu
	 * @date 20170216
	 * 方法：单位注册
	 * 
	 */
	public Map<String, Object> enterpriseRegister(String enterpriseType, String enterpriseName, String orgCode, String pwd){
		Record enterprise = new Record();
		String userId = super.getSeqId("WOBO_USER");
		String epId = super.getSeqId("WOBO_ENTERPRISE");
		enterprise.set("EP_ID", epId);
		enterprise.set("EP_NAME", enterpriseName);
		enterprise.set("REGISTERCODE", orgCode);
		//1-医疗处置单位，2-医疗单位
		if("1".equals(enterpriseType)){
			enterprise.set("IF_PRODUCE", "1");
			enterprise.set("IF_HANDLE", "0");
		}else if("2".equals(enterpriseType)){
			enterprise.set("IF_PRODUCE", "0");
			enterprise.set("IF_HANDLE", "1");
		}
		enterprise.set("STATUS", "0");
		enterprise.set("ORGTYPE", "1");
		enterprise.set("sysdate", super.getSysdate());
		enterprise.set("USER_ID", userId);
		if(Db.save("WOBO_ENTERPRISE", "EP_ID", enterprise)){
			Record user = new Record();
			user.set("USER_ID", userId);
			user.set("NAME", enterpriseName);
			user.set("NICK_NAME", enterpriseName);
			user.set("PWD", pwd);
			user.set("STATUS", "0");
			user.set("sysdate", super.getSysdate());
			if(Db.save("WOBO_USER", "USER_ID", user)){
				Record userInfo = new Record();
				userInfo.set("USER_ID", userId);
				userInfo.set("NAME", enterpriseName);
//				userInfo.set("PWD", pwd);
				userInfo.set("sysdate", super.getSysdate());
				if(Db.save("WOBO_USER_INFO", "USER_ID", userInfo)){
					Map<String, Object> map = new HashMap<>();
					map.put("userId", userId);
					map.put("epId", epId);
					return map;
				}
			}
		}
		return new HashMap<String, Object>();
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170216
	 * 方法：单位与角色关联
	 */
	public boolean manageEpRole(String userId, String roleId){
		Record userRole = Db.findFirst("select * from WOBO_USERROLE where USER_ID = ? and ROLE_ID = ? and STATUS = '1'", userId, roleId);
		if(userRole == null){
			Record record = new Record();
			record.set("USER_ID", userId);
			record.set("ROLE_ID", roleId);
			record.set("STATUS", "1");
			return Db.save("WOBO_USERROLE", record);
		}
		return true;
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170216
	 * 方法：二维码登录校验信息
	 */
	public Map<String, Object> checkPersonInfo(String roleType, String personId){
		String roleName = "";
		//0-现场处置人员，1-司机，2-车，3-产生管理员，4-处置管理员，5-产生交接员
		if("0".equals(roleType)){
			roleName = "IFHANDORVE";
		}else if("1".equals(roleType)){
			roleName = "IFDRIVER";
		}else if("4".equals(roleType)){
			roleName = "IFADMIN";
		}else{
			return null;
		}
		String sql = "select a.PI_ID,a.NAME,b.EP_ID,b.EP_NAME from WOBO_PERSON_CZ a, WOBO_ENTERPRISE b where a.PI_ID = ? and a.EP_ID = b.EP_ID and a."+roleName+" = '01'";
		if("4".equals(roleType)){
			sql = "select a.USER_ID as PI_ID,a.NAME,b.EP_ID,b.EP_NAME from WOBO_PERSON_CZ a, WOBO_ENTERPRISE b, WOBO_USER c where a.USER_ID = ? and a.EP_ID = b.EP_ID and a."+roleName+" = '01' and a.USER_ID = c.USER_ID and c.STATUS = '1'";
		}
		Record record = Db.findFirst(sql, personId);
		if(record != null){
			Map<String, Object> map = new HashMap<>();
			map.put("personId", record.get("PI_ID"));
			map.put("personName", record.get("NAME"));
			map.put("roleType", roleType);
			map.put("epId", record.get("EP_ID"));
			map.put("epName", record.get("EP_NAME"));
			return map;
		}
		return null;
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170216
	 * 方法：二维码登录校验信息 for 医疗
	 */
	public Map<String, Object> checkPersonInfoForYl(String roleType, String personId){
		String roleName = "";
		//0-现场处置人员，1-司机，2-车，3-产生管理员，4-处置管理员，5-产生交接员
		if("5".equals(roleType)){
			roleName = "IFHANDORVE";
		}else if("3".equals(roleType)){
			roleName = "IFADMIN";
		}else{
			return null;
		}
		String sql = "select a.PI_ID,a.NAME,b.EP_ID,b.EP_NAME from WOBO_PERSON_CS a, WOBO_ENTERPRISE b where a.PI_ID = ? and a.EP_ID = b.EP_ID and a."+roleName+" = '01'";
		if("3".equals(roleType)){
			sql = "select a.USER_ID as PI_ID,a.NAME,b.EP_ID,b.EP_NAME from WOBO_PERSON_CS a, WOBO_ENTERPRISE b, WOBO_USER c where a.USER_ID = ? and a.EP_ID = b.EP_ID and a."+roleName+" = '01' and a.USER_ID = c.USER_ID and c.STATUS = '1'";
		}
		Record record = Db.findFirst(sql, personId);
		if(record != null){
			Map<String, Object> map = new HashMap<>();
			map.put("personId", record.get("PI_ID"));
			map.put("personName", record.get("NAME"));
			map.put("roleType", roleType);
			map.put("epId", record.get("EP_ID"));
			map.put("epName", record.get("EP_NAME"));
			return map;
		}
		return null;
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170216
	 * 方法：app登录查询单位权限 by id
	 */
	public boolean checkEpPower(String epId){
		Record record = Db.findFirst("select * from WOBO_ENTERPRISE where EP_ID = ? and IF_PRODUCE = '1' and ORGTYPE = '1' and STATUS = '1'", epId);
		if(record != null){
			return true;
		}
		return false;
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170216
	 * 方法：app登录查询单位权限  by orgCode
	 */
	public boolean checkEpPowerbyOrgCode(String orgCode){
		Record record = Db.findFirst("select * from WOBO_ENTERPRISE where REGISTERCODE = ? and IF_PRODUCE = '1' and ORGTYPE = '1' and STATUS = '1'", orgCode);
		if(record != null){
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @author ouyangxu
	 * @date 20170221
	 * 方法：管理员登录
	 * 
	 */
	public Map<String, Object> adminLogin(String userId){
		String sql = "select c.OPERATORID, c.PASSWORD, b.ORGADDR, b.ORGNAME, b.ORGCODE,b.ORGID,c.USERID,c.OPERATORNAME from OM_EMPLOYEE a, OM_ORGANIZATION b ,AC_OPERATOR c where a.ORGID = b.ORGID and a.OPERATORID = c.OPERATORID and c.USERID = ? "+super.checkLowerAndUpper()+" and c.STATUS = 'running'";
		Record record = Db.findFirst(sql, userId);
		Map<String, Object> map = new HashMap<>();
		if(record != null){
			map.put("operatorId", record.get("OPERATORID"));
			map.put("pwd", record.get("PASSWORD"));
			map.put("orgAddr", record.get("ORGADDR"));
			map.put("orgArea", record.get("ORGNAME"));
			map.put("orgCode", record.get("ORGCODE"));
			map.put("btoId", record.get("ORGID"));
			map.put("btoName", record.get("ORGNAME"));
			map.put("btofId", record.get("USERID"));
			map.put("btofName", record.get("OPERATORNAME"));
			//查询管理员角色
			Record role = Db.findFirst("select ROLEID from AC_OPERATORROLE where OPERATORID = ? and ROLEID in ('QJSPROLE','SJSPROLE')", record.get("OPERATORID"));
			if(role != null){
				map.put("ROLEID", role.get("ROLEID"));
			}else{
				map.put("ROLEID", "");
			}
		}
		return map;
	}
	
	/**
	 * 
	 * @author weizanting
	 * @date 20170302
	 * 方法：医疗机构管理员登录
	 * return 0-中转 1-生产  2-处置
	 * 
	 */
	public String queryEPType(String REGISTERCODE){
		String sql = "select * from wobo_enterprise where REGISTERCODE = ? "+super.checkLowerAndUpper();
		Record record = Db.findFirst(sql, REGISTERCODE);
		String str = "";
		if(record != null && record.get("IF_PRODUCE") != null && !"".equals(record.get("IF_PRODUCE")) && record.get("IF_HANDLE") != null && !"".equals(record.get("IF_HANDLE"))){
			if("1".equals(record.get("IF_PRODUCE"))){
				str = "1";
				if("0".equals(record.get("ORGTYPE"))){
					str = "0";
				}
			}
			if("1".equals(record.get("IF_HANDLE"))){
				str = "2";
			}
		}
		return str;
	}
	
	/**
	 * 
	 * @author weizanting
	 * @date 20170302
	 * 方法：验证医疗机构管理员登录信息是否正确
	 * return 0-中转  1-产生  2-处置
	 * 
	 */
	public Map<String, Object> verifyLoginInfo(String epType, String NAME, String PWD, String REGISTERCODE){
		Record record = new Record();
		String sql = "";
		if("0".equals(epType) || "1".equals(epType)){
			sql = "select b.USER_ID,b.NAME,b.PWD,c.PORTRAIT,d.EP_ID,d.EP_NAME,d.EP_ADRESS_Q,d.EP_ADRESS_S,d.REGISTERCODE,d.BELONGSEPA from WOBO_PERSON_CS a,WOBO_USER b,WOBO_USER_INFO c,WOBO_ENTERPRISE d where a.USER_ID = b.USER_ID and b.USER_ID = c.USER_ID and a.EP_ID = d.EP_ID and b.status = '1' and d.status = '1' and b.NAME = ? and d.REGISTERCODE = ? "+super.checkLowerAndUpper();
			record = Db.findFirst(sql, NAME, REGISTERCODE);
		}else if("2".equals(epType)){
			sql = "select b.USER_ID,b.NAME,b.PWD,c.PORTRAIT,d.EP_ID,d.EP_NAME,d.EP_ADRESS_Q,d.EP_ADRESS_S,d.REGISTERCODE,d.BELONGSEPA from WOBO_PERSON_CZ a,WOBO_USER b,WOBO_USER_INFO c,WOBO_ENTERPRISE d where a.USER_ID = b.USER_ID and b.USER_ID = c.USER_ID and a.EP_ID = d.EP_ID and b.status = '1' and d.status = '1' and b.NAME = ?  and d.REGISTERCODE = ? "+super.checkLowerAndUpper();
			record = Db.findFirst(sql, NAME, REGISTERCODE);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		if(record != null){
			map.put("USER_ID", record.get("USER_ID"));
			map.put("NAME", record.get("NAME"));
			map.put("PWD", record.get("PWD"));
			map.put("EP_ADRESS_Q", convert(cityList, record.get("EP_ADRESS_Q")));
			if(record.get("EP_ADRESS_S") != null && !"".equals(record.get("EP_ADRESS_S"))){
				map.put("EP_ADRESS_S", record.get("EP_ADRESS_S"));
			}else{
				map.put("EP_ADRESS_S", "");
			}
			if(record.get("REGISTERCODE") != null && !"".equals(record.get("REGISTERCODE"))){
				map.put("REGISTERCODE", record.get("REGISTERCODE"));
			}else{
				map.put("REGISTERCODE", "");
			}
			if(record.get("PORTRAIT") != null && !"".equals(record.get("PORTRAIT"))){
				map.put("PORTRAIT", record.get("PORTRAIT"));
			}else{
				map.put("PORTRAIT", "");
			}
			map.put("EP_ID", record.get("EP_ID"));
			map.put("EP_NAME", record.get("EP_NAME"));
			map.put("belongSepa", record.get("BELONGSEPA"));
			map.put("sepaName", convert(cityList, record.get("BELONGSEPA")));
		}
		return map;
	}
	
	/**
	 * 
	 * @author weizanting
	 * @date 20170303
	 * 方法：查询医疗机构管理员信息
	 * return 0-运输  1-产生  2-处置
	 * 
	 *//*
	public Map<String, Object> queryAdmin(String REGISTERCODE, String NAME, String PWD){
		String str = this.queryEPType(REGISTERCODE);
		Record record = new Record();
		Map<String, Object> map = new HashMap<String, Object>();
		if("1".equals(str)){
			record = Db.findFirst("select b.USER_ID,b.NAME,c.PORTRAIT,d.EP_ADRESS_Q,d.EP_ADRESS_S,d.REGISTERCODE from WOBO_PERSON_CS a,wobo_user b,wobo_user_info c,WOBO_ENTERPRISE d where a.USER_ID = b.USER_ID and b.USER_ID = c.USER_ID and a.EP_ID = d.EP_ID and b.status = '1' and d.status = '1' and b.PWD = ? and b.NAME = ?", NAME, PWD);
		}
		if("2".equals(str)){
			record = Db.findFirst("select b.USER_ID,b.NAME,c.PORTRAIT,d.EP_ADRESS_Q,d.EP_ADRESS_S,d.REGISTERCODE from WOBO_PERSON_CZ a,wobo_user b,wobo_user_info c,WOBO_ENTERPRISE d where a.USER_ID = b.USER_ID and b.USER_ID = c.USER_ID and a.EP_ID = d.EP_ID and b.status = '1' and d.status = '1' and b.PWD = ? and b.NAME = ?", NAME, PWD);
		}
		if("0".equals(str)){
			record = Db.findFirst("select b.USER_ID,b.NAME,c.PORTRAIT,d.EP_ADRESS_Q,d.EP_ADRESS_S,d.REGISTERCODE from WOBO_PERSON_YS a,wobo_user b,wobo_user_info c,WOBO_ENTERPRISE d where a.USER_ID = b.USER_ID and b.USER_ID = c.USER_ID and a.EP_ID = d.EP_ID and b.status = '1' and d.status = '1' and b.PWD = ? and b.NAME = ?", NAME, PWD);
		}
		if(record != null){
			map.put("USER_ID", record.get("USER_ID"));
			map.put("NAME", record.get("NAME"));
			map.put("EP_ADRESS_Q", record.get("EP_ADRESS_Q"));
			map.put("EP_ADRESS_S", record.get("EP_ADRESS_S"));
			map.put("REGISTERCODE", record.get("REGISTERCODE"));
			map.put("PORTRAIT", record.get("PORTRAIT"));
		}
		return map;
	}*/
	
	/**
	 * 
	 * @author ouyangxu
	 * @date 20170623
	 * 方法：检验单位是否完善信息
	 * 
	 */
	public boolean checkEpIfSave(String epId){
		Record record = Db.findFirst("select * from WOBO_APPLY_LIST where EP_ID = ? and BIZ_ID = ? and BIZ_NAME = '医疗机构信息完善'", epId, epId);
		if(record != null){
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * @author weizanting
	 * @date 20170802
	 * 方法：检验单位是否完善信息
	 * 
	 */
	public String queryEP(String cs_person_id, String roleType){
		String sql = "select b.* from WOBO_PERSON_CS a,WOBO_ENTERPRISE b where a.EP_ID = b.EP_ID and b.STATUS = '1'";
		String str = "";
		//roleType:0-现场处置人员，1-司机，2-车，3-产生管理员，4-处置管理员，5-产生交接员
		if(roleType.equals("3")){
			sql = sql + " and a.USER_ID = '" + cs_person_id + "'";
		}
		if(roleType.equals("5")){
			sql = sql + " and a.PI_ID = '" + cs_person_id + "'";
		}
		Record record = Db.findFirst(sql);
		if(record != null){
			if("1".equals(record.get("IF_PRODUCE")) && "0".equals(record.get("ORGTYPE"))){
				str = "0";
			}
			if("1".equals(record.get("IF_PRODUCE")) && "1".equals(record.get("ORGTYPE"))){
				str = "1";
			}
			if("1".equals(record.get("IF_HANDLE"))){
				str = "2";
			}
		}
		return str;
	}
}
