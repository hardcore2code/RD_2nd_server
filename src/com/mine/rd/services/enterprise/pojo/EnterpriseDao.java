package com.mine.rd.services.enterprise.pojo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.core.JFinal;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.mine.pub.kit.DateKit;
import com.mine.pub.kit.QRcodeKit;
import com.mine.pub.pojo.BaseDao;

public class EnterpriseDao extends BaseDao {

	/**
	 * @author ouyangxu
	 * @date 20170220
	 * 方法：查询单位信息
	 */
	public Map<String, Object> queryEnterpriseInfo(String epId, Object action){
		String tablename = "";
		String epType = "";
		Map<String, Object> returnMap = new HashMap<>();
		Record enterprise = Db.findFirst("select * from WOBO_ENTERPRISE where EP_ID = ? and STATUS in ('0','1')", epId);
		if(enterprise != null){
			Map<String, Object> epInfo = new HashMap<>();
			if("1".equals(enterprise.get("IF_PRODUCE")) && "0".equals(enterprise.get("IF_HANDLE"))){
				tablename = "WOBO_PERSON_CS";
				epType = "CS";
			}
			if("0".equals(enterprise.get("IF_PRODUCE")) && "1".equals(enterprise.get("IF_HANDLE"))){
				tablename = "WOBO_PERSON_CZ";
				epType = "CZ";
			}
			epInfo.put("epId", epId);
			epInfo.put("epName", enterprise.get("EP_NAME"));	
			epInfo.put("epType", epType);
			epInfo.put("orgCode", enterprise.get("REGISTERCODE"));
			epInfo.put("linkMan", enterprise.get("LINKMAN"));
			epInfo.put("belongArea", enterprise.get("EP_ADRESS_Q"));
			epInfo.put("belongCity", enterprise.get("EP_ADRESS_S"));
			epInfo.put("belongStreet", enterprise.get("EP_ADRESS_J"));
			epInfo.put("belongSepa", enterprise.get("BELONGSEPA"));
			epInfo.put("contancts", enterprise.get("CONTANCTS"));
			epInfo.put("tel", enterprise.get("TEL"));
			epInfo.put("telephone", enterprise.get("TELEPHONE"));
			epInfo.put("mobilephone", enterprise.get("MOBILEPHONE"));
			epInfo.put("postalCode", enterprise.get("POSTAL_CODE"));
			epInfo.put("eMail", enterprise.get("EMAIL"));
			epInfo.put("longitude", enterprise.get("LONGITUDE"));
			epInfo.put("latitude", enterprise.get("LATITUDE"));
			epInfo.put("step", enterprise.get("STEP"));
			epInfo.put("orgType", enterprise.get("ORGTYPE"));
			Record epRelation = Db.findFirst("select * from WOBO_EPRELATION where EP_SON_ID = ? and STATUS = '1'", enterprise.get("EP_ID"));
			String belongOrgSepa = "";
			if(epRelation != null){
				epInfo.put("belongOrg", epRelation.get("EP_MAIN_ID"));
				belongOrgSepa = this.getBelongOrgSepa(epRelation.get("EP_MAIN_ID").toString());
				epInfo.put("orgBelongSepa", belongOrgSepa);
			}else{
				epInfo.put("belongOrg", "");
			}
			String bizId = enterprise.get("EP_ID");
			if(action != null && !"".equals(action)){
				bizId = "PE"+bizId.substring(2);
			}
			Record apply = Db.findFirst("select AYL_ID from WOBO_APPLY_LIST where BIZ_ID = ? and EP_ID = ? and STATUS != '00'", bizId, enterprise.get("EP_ID"));
			if(apply != null){
				epInfo.put("applyId", apply.get("AYL_ID"));
			}else{
				epInfo.put("applyId", "");
			}
			returnMap.put("epInfo", epInfo);
			returnMap.put("transferOrgData", this.queryBelongOrgBySepa(belongOrgSepa, epId));
		}else{
			returnMap.put("epInfo", new HashMap<String, Object>());
		}
		if(action == null || "".equals(action)){
			List<Record> admins = Db.find("select a.PI_ID,b.* from "+ tablename + " a, WOBO_USER b where a.EP_ID = ? and a.USER_ID = b.USER_ID and b.STATUS in ('0','1') and a.IFADMIN = '01' order by a.PI_ID asc", epId);
			if(admins != null){
				List<Map<String, Object>> adminList = new ArrayList<>();
				for(int i=0; i<admins.size(); i++){
					Map<String, Object> administrator = new HashMap<>();
					administrator.put("adminName", admins.get(i).get("NAME"));
					administrator.put("adminPwd", admins.get(i).get("PWD"));
					administrator.put("personId", admins.get(i).get("PI_ID"));
					administrator.put("userId", admins.get(i).get("USER_ID"));
					administrator.put("sort", i+3);
					adminList.add(administrator);
				}
				returnMap.put("adminData", adminList);
			}else{
				returnMap.put("adminData", new ArrayList<Map<String, Object>>());
			}
			List<Record> connects = Db.find("select * from "+ tablename + " where EP_ID = ? and IFADMIN = '00' order by PI_ID asc", epId);
			if(connects != null){
				List<Map<String, Object>> connectList = new ArrayList<>();
				for(int i=0; i<connects.size(); i++){
					Map<String, Object> connect = new HashMap<>();
					connect.put("connectName", connects.get(i).get("NAME"));
					if("01".equals(connects.get(i).get("IFHANDORVE"))){
						connect.put("connectType", "1");
					}
					if("01".equals(connects.get(i).get("IFDRIVER"))){
						connect.put("connectType", "2");
					}
					connect.put("personId", connects.get(i).get("PI_ID"));
					connect.put("codePath", connects.get(i).get("CODE_PATH"));
					connect.put("sort", i+3);
					connectList.add(connect);
				}
				returnMap.put("connectData", connectList);
			}else{
				returnMap.put("connectData", new ArrayList<Map<String, Object>>());
			}
			List<Record> cars = Db.find("select * from WOBO_CAR where EP_ID = ? order by CI_ID asc", epId);
			if(cars != null){
				List<Map<String, Object>> carList = new ArrayList<>();
				for(int i=0; i<cars.size(); i++){
					Map<String, Object> car = new HashMap<>();
					car.put("carNum", cars.get(i).get("PLATE_NUMBER"));
					car.put("carType", cars.get(i).get("CARTYPE"));
					car.put("permit", cars.get(i).get("PERMITNUM"));
					car.put("carId", cars.get(i).get("CI_ID"));
					car.put("tare", cars.get(i).get("TARE"));
					car.put("codePath", cars.get(i).get("CODE_PATH"));
					car.put("sort", i+3);
					carList.add(car);
				}
				returnMap.put("carData", carList);
			}else{
				returnMap.put("carData", new ArrayList<Map<String, Object>>());
			}
		}
//		List<Record> transferOrgs = Db.find("select * from WOBO_ENTERPRISE where ORGTYPE = '0' and STATUS = '1'");
//		List<Map<String, Object>> transferOrgList = new ArrayList<>();
//		Map<String, Object> transferOrg1 = new HashMap<>();
//		transferOrg1.put("value", "0");
//		transferOrg1.put("orgName", "请选择");
//		transferOrgList.add(transferOrg1);
//		if(transferOrgs != null){
//			for(Record org : transferOrgs){
//				if(!epId.equals(org.get("EP_ID"))){
//					Map<String, Object> transferOrg = new HashMap<>();
//					transferOrg.put("value", org.get("EP_ID"));
//					transferOrg.put("orgName", org.get("EP_NAME"));
//					transferOrgList.add(transferOrg);
//				}
//			}
//		}
//		returnMap.put("transferOrgData", transferOrgList);
		String sql = "select * from WOBO_APPLY_LIST where EP_ID = ? and BIZ_NAME = '医疗机构信息完善' and STATUS in ('01','02','04','05')";
		if(action != null && !"".equals(action)){
			sql = "select * from WOBO_APPLY_LIST where EP_ID = ? and BIZ_NAME = '医疗机构信息维护' and STATUS in ('01','02','05')";
		}
		Record btnFlag = Db.findFirst(sql, epId);
		if(btnFlag != null){
			returnMap.put("btnFlag", true);
		}else{
			returnMap.put("btnFlag", false);
		}
		//查询区列表
		returnMap.put("areaList", super.queryDictForSelect("city_q", "value", "area"));
		//查询市列表
		returnMap.put("cityList", super.queryDictForSelect("city_s", "value", "city"));
		//查询单位等级列表
		returnMap.put("hospitalGradeList", super.queryDictForSelect("hospital_grade", "value", "grade"));
		return returnMap;
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170512
	 * 方法：查询单位过渡信息 for ep
	 */
	public Map<String, Object> queryEpInfoTransitionForEp(String epId, Object action){
		String tablename = "";
		String epType = "";
		Map<String, Object> returnMap = new HashMap<>();
		//查看过渡表是否有信息
		Record enterprise = Db.findFirst("select * from WOBO_ENTERPRISE_UPDATE where EP_ID = ? and STATUS in ('0','1')", epId);
		String addname = "";
		if(enterprise != null){
			addname = "_UPDATE";
		}else{
			//过渡表无信息则查询主表
			enterprise = Db.findFirst("select * from WOBO_ENTERPRISE where EP_ID = ? and STATUS in ('0','1')", epId);
		}
		//查询单位信息
		if(enterprise != null){
			Map<String, Object> epInfo = new HashMap<>();
			if("1".equals(enterprise.get("IF_PRODUCE")) && "0".equals(enterprise.get("IF_HANDLE"))){
				tablename = "WOBO_PERSON_CS";
				epType = "CS";
			}
			if("0".equals(enterprise.get("IF_PRODUCE")) && "1".equals(enterprise.get("IF_HANDLE"))){
				tablename = "WOBO_PERSON_CZ";
				epType = "CZ";
			}
			epInfo.put("epId", epId);
			epInfo.put("epName", enterprise.get("EP_NAME"));
			epInfo.put("epType", epType);
			epInfo.put("orgCode", enterprise.get("REGISTERCODE"));
			epInfo.put("linkMan", enterprise.get("LINKMAN"));
			epInfo.put("belongArea", enterprise.get("EP_ADRESS_Q"));
			epInfo.put("belongCity", enterprise.get("EP_ADRESS_S"));
			epInfo.put("belongStreet", enterprise.get("EP_ADRESS_J"));
			epInfo.put("belongSepa", enterprise.get("BELONGSEPA"));
			epInfo.put("contancts", enterprise.get("CONTANCTS"));
			epInfo.put("tel", enterprise.get("TEL"));
			epInfo.put("telephone", enterprise.get("TELEPHONE"));
			epInfo.put("mobilephone", enterprise.get("MOBILEPHONE"));
			epInfo.put("postalCode", enterprise.get("POSTAL_CODE"));
			epInfo.put("eMail", enterprise.get("EMAIL"));
			epInfo.put("longitude", enterprise.get("LONGITUDE"));
			epInfo.put("latitude", enterprise.get("LATITUDE"));
			epInfo.put("step", enterprise.get("STEP"));
			epInfo.put("orgType", enterprise.get("ORGTYPE"));
			Record epRelation = Db.findFirst("select * from WOBO_EPRELATION"+addname+" where EP_SON_ID = ? and STATUS = '1'", enterprise.get("EP_ID"));
			String belongOrgSepa = "";
			if(epRelation != null){
				epInfo.put("belongOrg", epRelation.get("EP_MAIN_ID"));
				belongOrgSepa = this.getBelongOrgSepa(epRelation.get("EP_MAIN_ID").toString());
				epInfo.put("orgBelongSepa", belongOrgSepa);
			}else{
				epInfo.put("belongOrg", "");
			}
			String bizId = enterprise.get("EP_ID");
			if(action != null && !"".equals(action)){
				bizId = "PE"+bizId.substring(2);
			}
			Record apply = Db.findFirst("select AYL_ID from WOBO_APPLY_LIST where BIZ_ID = ? and EP_ID = ? order by sysdate desc", bizId, enterprise.get("EP_ID"));
			if(apply != null){
				if("00".equals(apply.get("STATUS"))){
					epInfo.put("applyId", "");
				}else{
					epInfo.put("applyId", apply.get("AYL_ID"));
				}
			}else{
				epInfo.put("applyId", "");
			}
			returnMap.put("epInfo", epInfo);
			returnMap.put("transferOrgData", this.queryBelongOrgBySepa(belongOrgSepa, epId));
		}else{
			returnMap.put("epInfo", new HashMap<String, Object>());
		}
		//查询单位管理员信息
		if(action == null || "".equals(action)){
			List<Record> admins = Db.find("select a.PI_ID,b.* from "+ tablename + addname + " a, WOBO_USER b where a.EP_ID = ? and a.USER_ID = b.USER_ID and b.STATUS in ('0','1') and a.IFADMIN = '01' order by a.PI_ID asc", epId);
			if(admins != null){
				List<Map<String, Object>> adminList = new ArrayList<>();
				for(int i=0; i<admins.size(); i++){
					Map<String, Object> administrator = new HashMap<>();
					administrator.put("adminName", admins.get(i).get("NAME"));
					administrator.put("adminPwd", admins.get(i).get("PWD"));
					administrator.put("personId", admins.get(i).get("PI_ID"));
					administrator.put("userId", admins.get(i).get("USER_ID"));
					administrator.put("sort", i+3);
					adminList.add(administrator);
				}
				returnMap.put("adminData", adminList);
			}else{
				returnMap.put("adminData", new ArrayList<Map<String, Object>>());
			}
			//查询单位交接员信息
			List<Record> connects = Db.find("select * from "+ tablename + addname + " where EP_ID = ? and IFADMIN = '00' order by PI_ID asc", epId);
			if(connects != null){
				List<Map<String, Object>> connectList = new ArrayList<>();
				for(int i=0; i<connects.size(); i++){
					Map<String, Object> connect = new HashMap<>();
					connect.put("connectName", connects.get(i).get("NAME"));
					if("01".equals(connects.get(i).get("IFHANDORVE"))){
						connect.put("connectType", "1");
					}
					if("01".equals(connects.get(i).get("IFDRIVER"))){
						connect.put("connectType", "2");
					}
					connect.put("personId", connects.get(i).get("PI_ID"));
					connect.put("codePath", connects.get(i).get("CODE_PATH"));
					connect.put("sort", i+3);
					connectList.add(connect);
				}
				returnMap.put("connectData", connectList);
			}else{
				returnMap.put("connectData", new ArrayList<Map<String, Object>>());
			}
			//查询单位车辆信息
			List<Record> cars = Db.find("select * from WOBO_CAR"+addname+" where EP_ID = ? order by CI_ID asc", epId);
			if(cars != null){
				List<Map<String, Object>> carList = new ArrayList<>();
				for(int i=0; i<cars.size(); i++){
					Map<String, Object> car = new HashMap<>();
					car.put("carNum", cars.get(i).get("PLATE_NUMBER"));
					car.put("carType", cars.get(i).get("CARTYPE"));
					car.put("permit", cars.get(i).get("PERMITNUM"));
					car.put("carId", cars.get(i).get("CI_ID"));
					car.put("tare", cars.get(i).get("TARE"));
					car.put("codePath", cars.get(i).get("CODE_PATH"));
					car.put("sort", i+3);
					carList.add(car);
				}
				returnMap.put("carData", carList);
			}else{
				returnMap.put("carData", new ArrayList<Map<String, Object>>());
			}
		}
		//查询中转单位信息
//		List<Record> transferOrgs = Db.find("select * from WOBO_ENTERPRISE where ORGTYPE = '0' and STATUS = '1'");
//		List<Map<String, Object>> transferOrgList = new ArrayList<>();
//		Map<String, Object> transferOrg1 = new HashMap<>();
//		transferOrg1.put("value", "0");
//		transferOrg1.put("orgName", "请选择");
//		transferOrgList.add(transferOrg1);
//		if(transferOrgs != null){
//			for(Record org : transferOrgs){
//				if(!epId.equals(org.get("EP_ID"))){
//					Map<String, Object> transferOrg = new HashMap<>();
//					transferOrg.put("value", org.get("EP_ID"));
//					transferOrg.put("orgName", org.get("EP_NAME"));
//					transferOrgList.add(transferOrg);
//				}
//			}
//		}
//		returnMap.put("transferOrgData", transferOrgList);
		//审批记录按钮是否显示标志位
		String sql = "select * from WOBO_APPLY_LIST where EP_ID = ? and BIZ_NAME = '医疗机构信息完善' and STATUS in ('01','02','04','05')";
		if(action != null && !"".equals(action)){
			sql = "select * from WOBO_APPLY_LIST where EP_ID = ? and BIZ_NAME = '医疗机构信息维护' and STATUS in ('01','02','05')";
		}
		Record btnFlag = Db.findFirst(sql, epId);
		if(btnFlag != null){
			returnMap.put("btnFlag", true);
		}else{
			returnMap.put("btnFlag", false);
		}
		//查询区列表
		returnMap.put("areaList", super.queryDictForSelect("city_q", "value", "area"));
		//查询市列表
		returnMap.put("cityList", super.queryDictForSelect("city_s", "value", "city"));
		//查询单位等级列表
		returnMap.put("hospitalGradeList", super.queryDictForSelect("hospital_grade", "value", "grade"));
		//查看原信息按钮是否显示标志位
		if("".equals(addname)){
			returnMap.put("historyFlag", false);
		}else{
			returnMap.put("historyFlag", true);
		}
		returnMap.put("orgTypeFlag", this.queryOrgType(epId));
		return returnMap;
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170512
	 * 方法：查询单位历史信息 for ep
	 */
	public Map<String, Object> queryEpInfoHistoryForEp(String epId, Object action, String applyId){
		String epType = "";
		Map<String, Object> returnMap = new HashMap<>();
		//查看历史表是否有信息
		Record enterprise = Db.findFirst("select * from WOBO_ENTERPRISE_UPDATE_H where EP_ID = ? and BIZ_ID = ? and STATUS in ('0','1')", epId, applyId);
		String addname = "";
		if(enterprise != null){
			addname = "_UPDATE_H";
		}else{
			//历史表无信息则查询过渡表
			enterprise = Db.findFirst("select * from WOBO_ENTERPRISE_UPDATE where EP_ID = ? and BIZ_ID = ? and STATUS in ('0','1')", epId, applyId);
		}
		//查询单位信息
		if(enterprise != null){
			Map<String, Object> epInfo = new HashMap<>();
			//CS-医疗单位  CZ-医疗处置单位
			if("1".equals(enterprise.get("IF_PRODUCE")) && "0".equals(enterprise.get("IF_HANDLE"))){
				epType = "CS";
			}
			if("0".equals(enterprise.get("IF_PRODUCE")) && "1".equals(enterprise.get("IF_HANDLE"))){
				epType = "CZ";
			}
			epInfo.put("epId", epId);
			epInfo.put("epName", enterprise.get("EP_NAME"));
			epInfo.put("epType", epType);
			epInfo.put("orgCode", enterprise.get("REGISTERCODE"));
			epInfo.put("linkMan", enterprise.get("LINKMAN"));
			epInfo.put("belongArea", enterprise.get("EP_ADRESS_Q"));
			epInfo.put("belongCity", enterprise.get("EP_ADRESS_S"));
			epInfo.put("belongStreet", enterprise.get("EP_ADRESS_J"));
			epInfo.put("belongSepa", enterprise.get("BELONGSEPA"));
			epInfo.put("contancts", enterprise.get("CONTANCTS"));
			epInfo.put("tel", enterprise.get("TEL"));
			epInfo.put("telephone", enterprise.get("TELEPHONE"));
			epInfo.put("mobilephone", enterprise.get("MOBILEPHONE"));
			epInfo.put("postalCode", enterprise.get("POSTAL_CODE"));
			epInfo.put("eMail", enterprise.get("EMAIL"));
			epInfo.put("longitude", enterprise.get("LONGITUDE"));
			epInfo.put("latitude", enterprise.get("LATITUDE"));
			epInfo.put("step", enterprise.get("STEP"));
			epInfo.put("orgType", enterprise.get("ORGTYPE"));
			String sql1 = "select * from WOBO_EPRELATION_UPDATE where EP_SON_ID = ? and STATUS = '1' and BIZ_ID = ?";
			if("_UPDATE_H".equals(addname)){
				sql1 = "select * from WOBO_EPRELATION"+addname+" where EP_SON_ID = ? and STATUS = '1' and BIZ_ID = ?";
			}
			Record epRelation = Db.findFirst(sql1, enterprise.get("EP_ID"), applyId);
			String belongOrgSepa = "";
			if(epRelation != null && epRelation.get("EP_MAIN_ID") != null && !"".equals(epRelation.get("EP_MAIN_ID")) && !"0".equals(epRelation.get("EP_MAIN_ID"))){
				epInfo.put("belongOrg", epRelation.get("EP_MAIN_ID"));
				belongOrgSepa = this.getBelongOrgSepa(epRelation.get("EP_MAIN_ID").toString());
				epInfo.put("orgBelongSepa", belongOrgSepa);
			}else{
				epInfo.put("belongOrg", "");
			}
			String bizId = enterprise.get("EP_ID");
			if(action != null && !"".equals(action)){
				bizId = "PE"+bizId.substring(2);
			}
			Record apply = Db.findFirst("select AYL_ID from WOBO_APPLY_LIST where BIZ_ID = ? and EP_ID = ? and STATUS != '00'", bizId, enterprise.get("EP_ID"));
			if(apply != null){
				epInfo.put("applyId", apply.get("AYL_ID"));
			}else{
				epInfo.put("applyId", "");
			}
			returnMap.put("epInfo", epInfo);
			returnMap.put("transferOrgData", this.queryBelongOrgBySepa(belongOrgSepa, epId));
		}else{
			returnMap.put("epInfo", new HashMap<String, Object>());
		}
		//查询中转单位信息
//		List<Record> transferOrgs = Db.find("select * from WOBO_ENTERPRISE where ORGTYPE = '0' and STATUS = '1'");
//		List<Map<String, Object>> transferOrgList = new ArrayList<>();
//		Map<String, Object> transferOrg1 = new HashMap<>();
//		transferOrg1.put("value", "0");
//		transferOrg1.put("orgName", "请选择");
//		transferOrgList.add(transferOrg1);
//		if(transferOrgs != null){
//			for(Record org : transferOrgs){
//				if(!epId.equals(org.get("EP_ID"))){
//					Map<String, Object> transferOrg = new HashMap<>();
//					transferOrg.put("value", org.get("EP_ID"));
//					transferOrg.put("orgName", org.get("EP_NAME"));
//					transferOrgList.add(transferOrg);
//				}
//			}
//		}
//		returnMap.put("transferOrgData", transferOrgList);
		//审批记录按钮是否显示标志位
		String sql = "select * from WOBO_APPLY_LIST where EP_ID = ? and BIZ_NAME = '医疗机构信息完善' and STATUS in ('01','02','04','05')";
		if(action != null && !"".equals(action)){
			sql = "select * from WOBO_APPLY_LIST where EP_ID = ? and BIZ_NAME = '医疗机构信息维护' and STATUS in ('01','02','05')";
		}
		Record btnFlag = Db.findFirst(sql, epId);
		if(btnFlag != null){
			returnMap.put("btnFlag", true);
		}else{
			returnMap.put("btnFlag", false);
		}
		//查询区列表
		returnMap.put("areaList", super.queryDictForSelect("city_q", "value", "area"));
		//查询市列表
		returnMap.put("cityList", super.queryDictForSelect("city_s", "value", "city"));
		//查询单位等级列表
		returnMap.put("hospitalGradeList", super.queryDictForSelect("hospital_grade", "value", "grade"));
		//查看原信息按钮是否显示标志位
//		if("".equals(addname)){
//			returnMap.put("historyFlag", false);
//		}else{
		returnMap.put("historyFlag", true);
//		}
		returnMap.put("orgTypeFlag", this.queryOrgType(epId));
		return returnMap;
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170220
	 * 方法：查询单位中转信息或历史信息
	 */
	public Map<String, Object> queryEnterpriseInfoTransition(String epId, String bizId, Object action){
		String tablename = "";
		String epType = "";
		Map<String, Object> returnMap = new HashMap<>();
		//查询单位原信息
		String sql = "select * from WOBO_ENTERPRISE where EP_ID = ? and STATUS in ('0','1')";
		String sqlEpRelation = "select * from WOBO_EPRELATION where EP_SON_ID = ? and STATUS = '1'";
		if(!"".equals(bizId)){
			//查询单位历史信息
			sql = "select * from WOBO_ENTERPRISE_UPDATE_H where EP_ID = ? and STATUS in ('0','1') and BIZ_ID = '"+bizId+"' order by sysdate,VERSION desc";
			sqlEpRelation = "select * from WOBO_EPRELATION_UPDATE_H where EP_SON_ID = ? and STATUS = '1' and BIZ_ID = '"+bizId+"' order by VERSION desc";
//			sql = "select * from WOBO_ENTERPRISE_UPDATE_H where EP_ID = ? and STATUS in ('0','1') order by sysdate,VERSION desc";
			Record record = Db.findFirst("select * from WOBO_APPLY_LIST where AYL_ID = ? and STATUS = '04'", bizId);
			if(record != null){
				sql = "select * from WOBO_ENTERPRISE_UPDATE_H where EP_ID = '"+epId+"' and VERSION not in (select top 1 VERSION from WOBO_ENTERPRISE_UPDATE_H where EP_ID = ? and STATUS in ('0','1') and BIZ_ID = '"+bizId+"' order by sysdate,VERSION desc) order by sysdate,VERSION desc";
				sqlEpRelation = "select top 1 * from WOBO_EPRELATION_UPDATE_H where EP_SON_ID = '"+epId+"' and VERSION not in (select top 1 VERSION from WOBO_EPRELATION_UPDATE_H where EP_SON_ID = ? and STATUS = '1' and BIZ_ID = '"+bizId+"' order by VERSION desc) order by VERSION desc";
			}
		}
		Record enterprise = Db.findFirst(sql, epId);
		if(!"".equals(bizId) && enterprise == null){
			enterprise = Db.findFirst("select * from WOBO_ENTERPRISE_UPDATE_H where EP_ID = ? and STATUS in ('0','1') order by sysdate,VERSION desc", epId);
		}
		if(enterprise != null){
			Map<String, Object> epInfo = new HashMap<>();
			if("1".equals(enterprise.get("IF_PRODUCE")) && "0".equals(enterprise.get("IF_HANDLE"))){
				tablename = "WOBO_PERSON_CS";
				if(!"".equals(bizId)){
					tablename = "WOBO_PERSON_CS_UPDATE_H";
				}
				epType = "CS";
			}
			if("0".equals(enterprise.get("IF_PRODUCE")) && "1".equals(enterprise.get("IF_HANDLE"))){
				tablename = "WOBO_PERSON_CZ";
				if(!"".equals(bizId)){
					tablename = "WOBO_PERSON_CZ_UPDATE_H";
				}
				epType = "CZ";
			}
			epInfo.put("epId", epId);
			epInfo.put("epName", enterprise.get("EP_NAME"));
			epInfo.put("epType", epType);
			epInfo.put("orgCode", enterprise.get("REGISTERCODE"));
			epInfo.put("linkMan", enterprise.get("LINKMAN"));
			epInfo.put("belongArea", enterprise.get("EP_ADRESS_Q"));
			epInfo.put("belongCity", enterprise.get("EP_ADRESS_S"));
			epInfo.put("belongStreet", enterprise.get("EP_ADRESS_J"));
			epInfo.put("belongSepa", enterprise.get("BELONGSEPA"));
			epInfo.put("contancts", enterprise.get("CONTANCTS"));
			epInfo.put("tel", enterprise.get("TEL"));
			epInfo.put("telephone", enterprise.get("TELEPHONE"));
			epInfo.put("mobilephone", enterprise.get("MOBILEPHONE"));
			epInfo.put("postalCode", enterprise.get("POSTAL_CODE"));
			epInfo.put("longitude", enterprise.get("LONGITUDE"));
			epInfo.put("latitude", enterprise.get("LATITUDE"));
			epInfo.put("step", enterprise.get("STEP"));
			epInfo.put("orgType", enterprise.get("ORGTYPE"));
			//查询所属中转单位
			Record epRelation = Db.findFirst(sqlEpRelation, enterprise.get("EP_ID"));
			if(!"".equals(bizId) && epRelation == null){
				epRelation = Db.findFirst("select * from WOBO_EPRELATION_UPDATE_H where EP_SON_ID = ? and STATUS = '1' order by VERSION desc", epId);
			}
			if(epRelation != null && epRelation.get("EP_MAIN_ID") != null && !"0".equals(epRelation.get("EP_MAIN_ID"))){
				epInfo.put("belongOrg", epRelation.get("EP_MAIN_ID"));
			}else{
				epInfo.put("belongOrg", "");
			}
			returnMap.put("epInfo", epInfo);
			if(action != null && !"".equals(action)){
//				System.out.println("tablename=====>"+tablename);
				//查询管理员信息
				String sql1 = "select a.PI_ID,b.* from "+ tablename + " a, WOBO_USER b where a.EP_ID = ? and a.USER_ID = b.USER_ID and b.STATUS in ('0','1') and a.IFADMIN = '01' order by a.PI_ID asc";
				if(!"".equals(bizId)){
					Record adminH = Db.findFirst("select VERSION from "+tablename+" where EP_ID = ? and IFADMIN = '01' and BIZ_ID = ? order by VERSION desc", epId, bizId);
					if(adminH != null){
						sql1 = "select a.PI_ID,b.* from "+ tablename + " a, WOBO_USER b where a.EP_ID = ? and a.USER_ID = b.USER_ID and b.STATUS in ('0','1') and a.IFADMIN = '01' and a.BIZ_ID = '"+bizId+"' and a.VERSION = "+adminH.get("VERSION")+" order by a.PI_ID asc";
					}
				}
				List<Record> admins = Db.find(sql1, epId);
				if(admins != null){
					List<Map<String, Object>> adminList = new ArrayList<>();
					for(int i=0; i<admins.size(); i++){
						Map<String, Object> administrator = new HashMap<>();
						administrator.put("adminName", admins.get(i).get("NAME"));
						administrator.put("adminPwd", admins.get(i).get("PWD"));
						administrator.put("personId", admins.get(i).get("PI_ID"));
						administrator.put("userId", admins.get(i).get("USER_ID"));
						administrator.put("sort", i+3);
						adminList.add(administrator);
					}
					returnMap.put("adminData", adminList);
				}else{
					returnMap.put("adminData", new ArrayList<Map<String, Object>>());
				}
				//查询交接员信息
				String sql2 = "select * from "+ tablename + " where EP_ID = ? and IFADMIN = '00' order by PI_ID asc";
				if(!"".equals(bizId)){
					Record personH = Db.findFirst("select VERSION from "+tablename+" where EP_ID = ? and IFADMIN = '00' and BIZ_ID = ? order by VERSION desc", epId, bizId);
					if(personH != null){
						sql2 = "select * from "+ tablename + " where EP_ID = ? and IFADMIN = '00' and BIZ_ID = '"+bizId+"' and VERSION = "+personH.get("VERSION")+" order by PI_ID asc";
					}
				}
				List<Record> connects = Db.find(sql2, epId);
				if(connects != null){
					List<Map<String, Object>> connectList = new ArrayList<>();
					for(int i=0; i<connects.size(); i++){
						Map<String, Object> connect = new HashMap<>();
						connect.put("connectName", connects.get(i).get("NAME"));
						if("01".equals(connects.get(i).get("IFHANDORVE"))){
							connect.put("connectType", "1");
						}
						if("01".equals(connects.get(i).get("IFDRIVER"))){
							connect.put("connectType", "2");
						}
						connect.put("personId", connects.get(i).get("PI_ID"));
						connect.put("codePath", connects.get(i).get("CODE_PATH"));
						connect.put("sort", i+3);
						connectList.add(connect);
					}
					returnMap.put("connectData", connectList);
				}else{
					returnMap.put("connectData", new ArrayList<Map<String, Object>>());
				}
				//查询车辆信息
				String sql3 = "select * from WOBO_CAR where EP_ID = ? order by CI_ID asc";
				if(!"".equals(bizId)){
					Record carH = Db.findFirst("select VERSION from WOBO_CAR_UPDATE_H where EP_ID = ? and BIZ_ID = ? order by VERSION desc", epId, bizId);
					if(carH != null){
						sql3 = "select * from WOBO_CAR_UPDATE_H where EP_ID = ? and BIZ_ID = '"+bizId+"' and VERSION = "+carH.get("VERSION")+" order by CI_ID asc";
					}
				}
				List<Record> cars = Db.find(sql3, epId);
				if(cars != null){
					List<Map<String, Object>> carList = new ArrayList<>();
					for(int i=0; i<cars.size(); i++){
						Map<String, Object> car = new HashMap<>();
						car.put("carNum", cars.get(i).get("PLATE_NUMBER"));
						car.put("carType", cars.get(i).get("CARTYPE"));
						car.put("permit", cars.get(i).get("PERMITNUM"));
						car.put("carId", cars.get(i).get("CI_ID"));
						car.put("tare", cars.get(i).get("TARE"));
						car.put("codePath", cars.get(i).get("CODE_PATH"));
						car.put("sort", i+3);
						carList.add(car);
					}
					returnMap.put("carData", carList);
				}else{
					returnMap.put("carData", new ArrayList<Map<String, Object>>());
				}
			}
			//查询中转单位列表
			String sql4 = "select * from WOBO_ENTERPRISE where ORGTYPE = '0' and STATUS = '1'";
			List<Record> transferOrgs = Db.find(sql4);
			List<Map<String, Object>> transferOrgList = new ArrayList<>();
			Map<String, Object> transferOrg1 = new HashMap<>();
			transferOrg1.put("value", "0");
			transferOrg1.put("orgName", "请选择");
			transferOrgList.add(transferOrg1);
			if(transferOrgs != null){
				for(Record org : transferOrgs){
					Map<String, Object> transferOrg = new HashMap<>();
					transferOrg.put("value", org.get("EP_ID"));
					transferOrg.put("orgName", org.get("EP_NAME"));
					transferOrgList.add(transferOrg);
				}
			}
			//查询是否已提交
			Record btnFlag = Db.findFirst("select * from WOBO_APPLY_LIST where EP_ID = ? and BIZ_NAME = ? and STATUS in ('01','02','05')", epId, "医疗机构信息完善");
			if(btnFlag != null){
				returnMap.put("btnFlag", true);
			}else{
				returnMap.put("btnFlag", false);
			}
			//查询区列表
			returnMap.put("areaList", super.queryDictForSelect("city_q", "value", "area"));
			//查询市列表
			returnMap.put("cityList", super.queryDictForSelect("city_s", "value", "city"));
			//查询单位
			returnMap.put("hospitalGradeList", super.queryDictForSelect("hospital_grade", "value", "grade"));
			returnMap.put("transferOrgData", transferOrgList);
		}else{
			returnMap.put("epInfo", new HashMap<String, Object>());
		}
		return returnMap;
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170220
	 * 方法：更新单位信息
	 */
	public boolean updateEnterpriseInfo(Map<String, Object> epInfo, String epId){
		String orgType = epInfo.get("orgType").toString();
		String sql = "update WOBO_ENTERPRISE set EP_NAME=?,REGISTERCODE=?,LINKMAN=?,EP_ADRESS_Q=?,CONTANCTS=?,TEL=?,MOBILEPHONE=?,POSTAL_CODE=?,EMAIL=?,LONGITUDE=?,LATITUDE=?,STEP=?,ORGTYPE=?,EP_ADRESS_S=?,EP_ADRESS_J=?,BELONGSEPA=? where EP_ID = ?";
		int result = Db.update(sql, epInfo.get("epName"),epInfo.get("epCode"),epInfo.get("linkMan"),epInfo.get("belongArea"),epInfo.get("contancts"),epInfo.get("tel"),epInfo.get("mobilephone"),epInfo.get("postalCode"),epInfo.get("eMail"),epInfo.get("longitude"),epInfo.get("latitude"),epInfo.get("step"),orgType,epInfo.get("belongCity"),epInfo.get("belongStreet"),epInfo.get("belongSepa"),epId);
		if(result > 0){
			if(epInfo.get("belongOrg") != null && !"".equals(epInfo.get("belongOrg")) && !"0".equals(epInfo.get("belongOrg"))){
				Record record = Db.findFirst("select * from WOBO_EPRELATION where EP_SON_ID = ? and STATUS = '1'", epId);
				if(record != null){
					int flag = Db.update("update WOBO_EPRELATION set EP_MAIN_ID = ? where EP_SON_ID = ? and STATUS = '1'", epInfo.get("belongOrg"), epId);
					if(flag > 0){
						return true;
					}else{
						return false;
					}
				}else{
					Record newRecord = new Record();
					newRecord.set("EP_MAIN_ID", epInfo.get("belongOrg"));
					newRecord.set("EP_SON_ID", epId);
					newRecord.set("STATUS", "1");
					return Db.save("WOBO_EPRELATION", newRecord);
				}
			}else{
				Record record = Db.findFirst("select * from WOBO_EPRELATION where EP_SON_ID = ? and STATUS = '1'", epId);
				if(record != null){
					return Db.update("update WOBO_EPRELATION set STATUS = '2' where EP_SON_ID = ? and STATUS = '1'", epId) > 0;
				}
				return true;
			}
		}else{
			return false;
		}
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170520
	 * 方法：更新单位信息 （暂存过渡表）
	 */
	public boolean saveEpInfoTransition(Map<String, Object> epInfo, String epId, String applyId){
		Record ep = Db.findFirst("select * from WOBO_ENTERPRISE_UPDATE where EP_ID = ? and STATUS in ('0','1')", epId);
		if(ep == null){
			Record epH = Db.findFirst("select * from WOBO_ENTERPRISE where EP_ID = ? and STATUS in ('0','1')", epId);
			epH.set("BIZ_ID", applyId);
			if(!Db.save("WOBO_ENTERPRISE_UPDATE", "EP_ID", epH)){
				return false;
			}
		}
		String orgType = epInfo.get("orgType").toString();
		String sql = "update WOBO_ENTERPRISE_UPDATE set EP_NAME=?,REGISTERCODE=?,LINKMAN=?,EP_ADRESS_Q=?,CONTANCTS=?,TEL=?,MOBILEPHONE=?,POSTAL_CODE=?,EMAIL=?,LONGITUDE=?,LATITUDE=?,STEP=?,ORGTYPE=?,EP_ADRESS_S=?,EP_ADRESS_J=?,BELONGSEPA=? where EP_ID = ?";
		int result = Db.update(sql, epInfo.get("epName"),epInfo.get("epCode"),epInfo.get("linkMan"),epInfo.get("belongArea"),epInfo.get("contancts"),epInfo.get("tel"),epInfo.get("mobilephone"),epInfo.get("postalCode"),epInfo.get("eMail"),epInfo.get("longitude"),epInfo.get("latitude"),epInfo.get("step"),orgType,epInfo.get("belongCity"),epInfo.get("belongStreet"),epInfo.get("belongSepa"),epId);
		if(result > 0){
			if(epInfo.get("belongOrg") != null && !"".equals(epInfo.get("belongOrg")) && !"0".equals(epInfo.get("belongOrg"))){
				Record record = Db.findFirst("select * from WOBO_EPRELATION_UPDATE where EP_SON_ID = ? and STATUS = '1'", epId);
				if(record != null){
					int flag = Db.update("update WOBO_EPRELATION_UPDATE set EP_MAIN_ID = ? where EP_SON_ID = ? and STATUS = '1'", epInfo.get("belongOrg"), epId);
					if(flag > 0){
						return true;
					}else{
						return false;
					}
				}else{
					Record newRecord = new Record();
					newRecord.set("EP_MAIN_ID", epInfo.get("belongOrg"));
					newRecord.set("EP_SON_ID", epId);
					newRecord.set("STATUS", "1");
					newRecord.set("BIZ_ID", applyId);
					return Db.save("WOBO_EPRELATION_UPDATE", newRecord);
				}
			}else{
				Record record = Db.findFirst("select * from WOBO_EPRELATION_UPDATE where EP_SON_ID = ? and STATUS = '1'", epId);
				if(record != null){
					return Db.update("update WOBO_EPRELATION_UPDATE set STATUS = '2' where EP_SON_ID = ? and STATUS = '1'", epId) > 0;
				}
				return true;
			}
		}else{
			return false;
		}
		
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170216
	 * 方法：检查单位是否存在
	 * @return true-可修改 false-不可修改
	 */
	public boolean checkIfModifyEpInfo(Map<String, Object> epInfo, String epId){
		String sql = "select * from WOBO_ENTERPRISE where EP_NAME=? and BELONGSEPA=? and LINKMAN=? and TEL=? and POSTAL_CODE=? and REGISTERCODE=? and LONGITUDE=? and LATITUDE=? and EP_ADRESS_S=? and EP_ADRESS_Q=? and EP_ADRESS_J=? and EMAIL=? and STEP=? and ORGTYPE=? and STATUS = '1'";
		Record ep = Db.findFirst(sql, epInfo.get("epName"),epInfo.get("belongSepa"),epInfo.get("linkMan"),epInfo.get("tel"),epInfo.get("postalCode"),epInfo.get("epCode"),epInfo.get("longitude"),epInfo.get("latitude"),epInfo.get("belongCity"),epInfo.get("belongArea"),epInfo.get("belongStreet"),epInfo.get("eMail"),epInfo.get("step"),epInfo.get("orgType"));
		if(ep != null){
			if(epInfo.get("belongOrg") != null && !"".equals(epInfo.get("belongOrg")) && !"0".equals(epInfo.get("belongOrg"))){
				Record record = Db.findFirst("select * from WOBO_EPRELATION where EP_SON_ID = ? and STATUS = '1' and EP_MAIN_ID = ?", epId, epInfo.get("belongOrg"));
				if(record != null){
					return false;
				}else{
					return true;
				}
			}else{
				Record record = Db.findFirst("select * from WOBO_EPRELATION where EP_SON_ID = ? and STATUS = '1'", epId);
				if(record != null){
					return true;
				}else{
					return false;
				}
			}
		}else{
			return true;
		}
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170323
	 * 方法：查询管理员是否可删除
	 */
	public boolean checkAdmin(List<Map<String, Object>> admins, String epId, String tablename){
		List<Record> persons = Db.find("select a.NAME from "+tablename+" as a, WOBO_USER as b where a.EP_ID = ? and a.IFADMIN ='01' and a.USER_ID = b.USER_ID and b.STATUS in ('1','3')", epId);
		int i = 0;
		if(persons.size() > 0){
			for(Record person : persons){
				for(Map<String, Object> admin : admins){
					if(person.get("NAME").equals(admin.get("adminName"))){
						i++;
						break;
					}
				}
			}
			if(persons.size() == i){
				return true;
			}else{
				return false;
			}
		}else{
			return true;
		}
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170323
	 * 方法：删除未通过审核管理员
	 */
	public boolean deleteNoApproveAdmin(String epId, String tablename){
		List<Record> persons = Db.find("select a.USER_ID from "+tablename+" as a, WOBO_USER as b where a.EP_ID = ? and a.IFADMIN ='01' and a.USER_ID = b.USER_ID and b.STATUS = '0'", epId);
		if(persons.size() > 0){
			for(Record person : persons){
				if(Db.update("delete from "+tablename+" where USER_ID = ?", person.get("USER_ID").toString()) <= 0){
					return false;
				}
				if(Db.update("delete from WOBO_USER where USER_ID = ?", person.get("USER_ID").toString()) <= 0){
					return false;
				}
				if(Db.update("delete from WOBO_USER_INFO where USER_ID = ?", person.get("USER_ID").toString()) <= 0){
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170220
	 * 方法：添加管理员
	 */
	public int checkPersonNum(String epId, String tablename, String ifAdmin){
		Record record = Db.findFirst("select count(*) as num from "+tablename+" where EP_ID = ? and IFADMIN = ?", epId, ifAdmin);
		if(record != null){
			return record.getInt("num");
		}else{
			return 0;
		}
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170220
	 * 方法：添加管理员
	 */
	public List<Map<String, Object>> addAdminInfo(List<Map<String, Object>> admins, String epId, String tablename, String action, String applyId){
		boolean flag1 = false;
		if("add".equals(action)){
			flag1 = this.deleteNoApproveAdmin(epId, tablename);
		}else{
			flag1 = this.deleteNoApproveAdmin(epId, tablename+"_UPDATE");
		}
		if(!flag1){
			return new ArrayList<>();
		}
		List<Map<String, Object>> returnList = new ArrayList<>();
		for(Map<String, Object> admin : admins){
			Map<String, Object> returnMap = new HashMap<>();
			Record record = Db.findFirst("select * from "+tablename+" where NAME = ? and EP_ID = ? and IFADMIN ='01'", admin.get("adminName"), epId);
			if(record == null){
				Record newRecord1 = new Record();
				String userIdNew = this.getSeqId("WOBO_USER");
				int num = 0;
				if("add".equals(action)){
					num = this.checkPersonNum(epId, tablename, "01")+1;
				}else{
					num = this.checkPersonNum(epId, tablename+"_UPDATE", "01")+1;
				}
				String personId = "00000" + num;
				newRecord1.set("PI_ID", personId);
				newRecord1.set("EP_ID", epId);
				newRecord1.set("NAME", admin.get("adminName"));
				newRecord1.set("IFADMIN", "01");
				newRecord1.set("USER_ID", userIdNew);
				newRecord1.set("sysdate", this.getSysdate());
				boolean flag2 = false;
				if("add".equals(action)){
					flag2 = Db.save(tablename, newRecord1);
				}else{
					newRecord1.set("BIZ_ID", applyId);
					flag2 = Db.save(tablename+"_UPDATE", newRecord1);
				}
				if(flag2){
					Record newRecord2 = new Record();
					newRecord2.set("USER_ID", userIdNew);
					newRecord2.set("NAME", admin.get("adminName"));
					newRecord2.set("NICK_NAME", admin.get("adminName"));
					newRecord2.set("PWD", admin.get("adminPwd"));
					newRecord2.set("STATUS", "0");
					newRecord2.set("sysdate", this.getSysdate());
					if(Db.save("WOBO_USER", "USER_ID", newRecord2)){
						Record newRecord3 = new Record();
						newRecord3.set("USER_ID", userIdNew);
						newRecord3.set("NAME", admin.get("adminName"));
						newRecord3.set("sysdate", this.getSysdate());
						if(!Db.save("WOBO_USER_INFO", "USER_ID", newRecord3)){
							return new ArrayList<>();
						}
						returnMap.put("userId", userIdNew);
						returnMap.put("name", admin.get("adminName"));
						returnList.add(returnMap);
					}else{
						return new ArrayList<>();
					}
				}else{
					return new ArrayList<>();
				}
			}else{
				boolean flag3 = false;
				if("modify".equals(action)){
					Record recordU = Db.findFirst("select * from "+tablename+"_UPDATE where NAME = ? and EP_ID = ? and IFADMIN ='01'", admin.get("adminName"), epId);
					if(recordU == null){
						record.set("BIZ_ID", applyId);
						flag3 = Db.save(tablename+"_UPDATE", record);
					}else{
						flag3 = true;
					}
				}
				if(!flag3){
					return new ArrayList<>();
				}
				if(Db.update("update WOBO_USER set PWD = ? where USER_ID = ?", admin.get("adminPwd"), record.get("USER_ID")) <= 0){
					return new ArrayList<>();
				}
				returnMap.put("userId", record.get("USER_ID"));
				returnMap.put("name", admin.get("adminName"));
				returnList.add(returnMap);
			}
		}
		return returnList;
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170220
	 * 方法：添加交接员
	 */
	public List<Map<String, Object>> addConnectInfo(List<Map<String, Object>> connects, String epId, String tablename){
		int flag = Db.update("delete from " +tablename+" where EP_ID = ? and IFADMIN != '01'", epId);
		if(flag < 0){
			return new ArrayList<Map<String,Object>>();
		}
		List<Map<String, Object>> returnList = new ArrayList<Map<String,Object>>();
		for(Map<String, Object> connect : connects){
			String IFHANDORVE = "";
			String IFDRIVER = "";
			//00-否，01-是
			if("1".equals(connect.get("connectType"))){
				IFHANDORVE = "01";
				IFDRIVER = "00";
			}else if("2".equals(connect.get("connectType"))){
				IFHANDORVE = "00";
				IFDRIVER = "01";
			}
			Record newRecord = new Record();
			Object personId = connect.get("personId");
			if(personId == null || "".equals(personId)){
				personId = super.getSeqId(tablename);
			}
			newRecord.set("PI_ID", personId);
			newRecord.set("EP_ID", epId);
			newRecord.set("NAME", connect.get("connectName"));
			newRecord.set("IFADMIN", "00");
			newRecord.set("IFHANDORVE", IFHANDORVE);
			newRecord.set("IFDRIVER", IFDRIVER);
			newRecord.set("CODE_PATH", connect.get("connectCodePath"));
			newRecord.set("sysdate", this.getSysdate());
			Map<String, Object> returnMap = new HashMap<>();
			returnMap.put("personId", personId);
			returnMap.put("personName", connect.get("connectName"));
			returnList.add(returnMap);
			if(!Db.save(tablename, newRecord)){
				return new ArrayList<Map<String,Object>>();
			}
		}
		return returnList;
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170220
	 * 方法：添加车辆
	 */
	public List<Map<String, Object>> addCarInfo(List<Map<String, Object>> cars, String epId){
		Record record = Db.findFirst("select * from WOBO_CAR where EP_ID = ?", epId);
		if(record != null){
			int flag = Db.update("delete from WOBO_CAR where EP_ID = ?", epId);
			if(flag < 0){
				return new ArrayList<Map<String,Object>>();
			}
		}
		List<Map<String, Object>> returnList = new ArrayList<Map<String,Object>>();
		for(Map<String, Object> car : cars){
			Record newRecord = new Record();
			Object carId = car.get("carId");
			if(carId == null || "".equals(carId)){
				carId = super.getSeqId("WOBO_CAR");
			}
			newRecord.set("CI_ID", carId);
			newRecord.set("EP_ID", epId);
			newRecord.set("PLATE_NUMBER", car.get("carNum"));
			newRecord.set("CARTYPE", car.get("carType"));
			newRecord.set("PERMITNUM", car.get("permit"));
			newRecord.set("TARE", car.get("tare"));
			newRecord.set("CODE_PATH", car.get("codePath"));
			newRecord.set("sysdate", this.getSysdate());
			Map<String, Object> returnMap = new HashMap<>();
			returnMap.put("carId", carId);
			returnMap.put("carNum", car.get("carNum"));
			returnList.add(returnMap);
			if(!Db.save("WOBO_CAR", newRecord)){
				return new ArrayList<Map<String,Object>>();
			}
		}
		return returnList;
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170223
	 * 方法：保存申请
	 */
	public String saveApply(String epId, String epName, String belongHbj, String action, String tablename){
		String bizName = "医疗机构信息完善";
		String bizId = epId;
		if(action != null && !"".equals(action)){
			bizName = "医疗机构信息维护";
			bizId = "PE"+epId.substring(2);
			if("modifyAdmin".equals(action)){
				bizName = "管理员信息维护";
				bizId = "PI"+epId.substring(2);
			}
		}
		Record btnFlag = Db.findFirst("select * from WOBO_APPLY_LIST where EP_ID = ? and BIZ_NAME = ? and STATUS != '04'", epId, bizName);
		if(btnFlag != null){
			if("医疗机构信息维护".equals(bizName)){
				//查询是否需要重新在申请表插入数据
				Record ep = Db.findFirst("select * from WOBO_ENTERPRISE_UPDATE where EP_ID = ?", epId);
				if(ep != null){
					return btnFlag.getStr("AYL_ID");
				}
			}else if("管理员信息维护".equals(bizName)){
				//查询是否需要重新在申请表插入数据
				Record person = Db.findFirst("select * from "+tablename+"_UPDATE where EP_ID = ?", epId);
				if(person != null){
					return btnFlag.getStr("AYL_ID");
				}
			}else{
				return btnFlag.getStr("AYL_ID");
			}
		}
		Record record = new Record();
		String applyId = super.getSeqId("WOBO_APPLY_LIST");
		record.set("AYL_ID", applyId);
		record.set("BIZ_ID", bizId);
		record.set("BIZ_NAME", bizName);
		record.set("EP_ID", epId);
		record.set("EP_NAME", epName);
		record.set("BIZ_VERSION", 0);
		record.set("APPLY_DATE", super.getSysdate());
		record.set("STATUS", "00");
		record.set("BELONG_SEPA", belongHbj);
		record.set("sysdate", super.getSysdate());
		if(Db.save("WOBO_APPLY_LIST", "AYL_ID", record)){
			return applyId;
		}else{
			return "";
		}
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170223
	 * 方法：提交医疗机构信息完善申请
	 */
	public boolean submitEpAddInfoApply(String epId, String action){
		String bizName = "医疗机构信息完善";
		String bizId = epId;
		if(action != null && !"".equals(action)){
			bizName = "医疗机构信息维护";
			bizId = "PE"+epId.substring(2);
			if("modifyAdmin".equals(action)){
				bizName = "管理员信息维护";
				bizId = "PI"+epId.substring(2);
			}
		}
		int flag = Db.update("update WOBO_APPLY_LIST set STATUS = '01', BIZ_VERSION = BIZ_VERSION+1, APPLY_DATE = ? where BIZ_ID = ? and EP_ID = ? and BIZ_NAME = ? and STATUS != '04'", super.getSysdate(), bizId, epId, bizName);
		if(flag > 0){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170303
	 * 方法：查询单位管理员信息
	 */
	public List<Map<String, Object>> queryAdminInfo(String epId){
		String tablename = "";
		//查询单位类型
		Record enterprise = Db.findFirst("select * from WOBO_ENTERPRISE where EP_ID = ? and STATUS in ('0','1')", epId);
		if(enterprise != null){
			//0.不属于；1.属于
			if("1".equals(enterprise.get("IF_PRODUCE")) && "0".equals(enterprise.get("IF_HANDLE"))){
				tablename = "WOBO_PERSON_CS";
			}
			if("0".equals(enterprise.get("IF_PRODUCE")) && "1".equals(enterprise.get("IF_HANDLE"))){
				tablename = "WOBO_PERSON_CZ";
			}
		}
		//查询过渡表单位管理员
		List<Record> admins = Db.find("select b.*,a.PI_ID from "+ tablename + "_UPDATE a, WOBO_USER b where a.EP_ID = ? and a.USER_ID = b.USER_ID and b.STATUS in ('0','1','3') and a.IFADMIN = '01' order by a.PI_ID asc", epId);
		List<Map<String, Object>> adminList = new ArrayList<>();
		if(admins.size() <= 0){
			//查询主表单位管理员
			admins = Db.find("select b.*,a.PI_ID from "+ tablename + " a, WOBO_USER b where a.EP_ID = ? and a.USER_ID = b.USER_ID and b.STATUS in ('0','1','3') and a.IFADMIN = '01' order by a.PI_ID asc", epId);
		}
		if(admins != null){
			for(int i=0; i<admins.size(); i++){
				Map<String, Object> administrator = new HashMap<>();
				administrator.put("adminName", admins.get(i).get("NAME"));
				administrator.put("adminPwd", admins.get(i).get("PWD"));
				administrator.put("userId", admins.get(i).get("USER_ID"));
				administrator.put("personId", admins.get(i).get("PI_ID"));
				if("1".equals(admins.get(i).get("STATUS"))){
					administrator.put("btnFlag", false);
					administrator.put("btnStatus", "3");
					administrator.put("btnTitle", "禁用");
				}else{
					administrator.put("btnFlag", true);
					administrator.put("btnStatus", "1");
					administrator.put("btnTitle", "启用");
				}
				administrator.put("sort", i+3);
				if("0".equals(admins.get(i).get("STATUS"))){
					administrator.put("Flag", false);
				}else{
					administrator.put("Flag", true);
				}
				adminList.add(administrator);
			}
		}
		return adminList;
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170303
	 * 方法：查询单位当前管理员信息
	 */
	public List<Map<String, Object>> queryAdminInfoForNow(String epId){
		String tablename = "";
		//查询单位类型
		Record enterprise = Db.findFirst("select * from WOBO_ENTERPRISE where EP_ID = ? and STATUS in ('0','1')", epId);
		if(enterprise != null){
			//0.不属于；1.属于
			if("1".equals(enterprise.get("IF_PRODUCE")) && "0".equals(enterprise.get("IF_HANDLE"))){
				tablename = "WOBO_PERSON_CS";
			}
			if("0".equals(enterprise.get("IF_PRODUCE")) && "1".equals(enterprise.get("IF_HANDLE"))){
				tablename = "WOBO_PERSON_CZ";
			}
		}
		//查询过渡表单位管理员
		List<Record> admins = Db.find("select b.*,a.PI_ID from "+ tablename + " a, WOBO_USER b where a.EP_ID = ? and a.USER_ID = b.USER_ID and b.STATUS in ('0','1','3') and a.IFADMIN = '01' order by a.PI_ID asc", epId);
		List<Map<String, Object>> adminList = new ArrayList<>();
		if(admins != null){
			for(int i=0; i<admins.size(); i++){
				Map<String, Object> administrator = new HashMap<>();
				administrator.put("adminName", admins.get(i).get("NAME"));
				administrator.put("adminPwd", admins.get(i).get("PWD"));
				administrator.put("userId", admins.get(i).get("USER_ID"));
				administrator.put("personId", admins.get(i).get("PI_ID"));
				if("1".equals(admins.get(i).get("STATUS"))){
					administrator.put("btnFlag", false);
					administrator.put("btnStatus", "3");
					administrator.put("btnTitle", "禁用");
				}else{
					administrator.put("btnFlag", true);
					administrator.put("btnStatus", "1");
					administrator.put("btnTitle", "启用");
				}
				administrator.put("sort", i+3);
				if("0".equals(admins.get(i).get("STATUS"))){
					administrator.put("Flag", false);
				}else{
					administrator.put("Flag", true);
				}
				adminList.add(administrator);
			}
		}
		return adminList;
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170303
	 * 方法：查询单位管理员信息 for finishedTask
	 */
	public List<Map<String, Object>> queryAdminInfoForFinishTask(String epId, String applyId){
		String tablename = "";
		//查询单位类型
		Record enterprise = Db.findFirst("select * from WOBO_ENTERPRISE where EP_ID = ? and STATUS in ('0','1')", epId);
		if(enterprise != null){
			//0.不属于；1.属于
			if("1".equals(enterprise.get("IF_PRODUCE")) && "0".equals(enterprise.get("IF_HANDLE"))){
				tablename = "WOBO_PERSON_CS";
			}
			if("0".equals(enterprise.get("IF_PRODUCE")) && "1".equals(enterprise.get("IF_HANDLE"))){
				tablename = "WOBO_PERSON_CZ";
			}
		}
		//查询历史记录版本号
//		Record personH = Db.findFirst("select VERSION from "+ tablename + "_UPDATE_H where EP_ID = ? and BIZ_ID = ?", epId, applyId);
//		int version = 0;
//		if(personH != null){
//			version = personH.getInt("VERSION")+1;
//		}
		//查询历史表管理员
		List<Record> admins = Db.find("select b.*,a.PI_ID from "+ tablename + "_UPDATE_H a, WOBO_USER b where a.EP_ID = ? and a.USER_ID = b.USER_ID and b.STATUS in ('0','1','3') and a.IFADMIN = '01' and a.BIZ_ID = ? order by a.PI_ID asc", epId, applyId);
		List<Map<String, Object>> adminList = new ArrayList<>();
		if(admins.size() <= 0){
			//查询过渡表管理员
			admins = Db.find("select b.*,a.PI_ID from "+ tablename + "_UPDATE a, WOBO_USER b where a.EP_ID = ? and a.USER_ID = b.USER_ID and b.STATUS in ('0','1','3') and a.IFADMIN = '01' and a.BIZ_ID = ? order by a.PI_ID asc", epId, applyId);
		}
		if(admins != null){
			for(int i=0; i<admins.size(); i++){
				Map<String, Object> administrator = new HashMap<>();
				administrator.put("adminName", admins.get(i).get("NAME"));
				administrator.put("adminPwd", admins.get(i).get("PWD"));
				administrator.put("userId", admins.get(i).get("USER_ID"));
				administrator.put("personId", admins.get(i).get("PI_ID"));
				if("1".equals(admins.get(i).get("STATUS"))){
					administrator.put("btnFlag", false);
					administrator.put("btnStatus", "3");
					administrator.put("btnTitle", "禁用");
				}else{
					administrator.put("btnFlag", true);
					administrator.put("btnStatus", "1");
					administrator.put("btnTitle", "启用");
				}
				administrator.put("sort", i+3);
				if("0".equals(admins.get(i).get("STATUS"))){
					administrator.put("Flag", false);
				}else{
					administrator.put("Flag", true);
				}
				adminList.add(administrator);
			}
		}
		return adminList;
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170303
	 * 方法：查询单位管理员修改过渡和历史信息
	 */
	public List<Map<String, Object>> queryAdminInfoTransition(String epId, Object bizId){
		String tablename = "";
		Record enterprise = Db.findFirst("select * from WOBO_ENTERPRISE where EP_ID = ? and STATUS in ('0','1')", epId);
		if(enterprise != null){
			//0.不属于；1.属于
			if("1".equals(enterprise.get("IF_PRODUCE")) && "0".equals(enterprise.get("IF_HANDLE"))){
				tablename = "WOBO_PERSON_CS";
				if(!"".equals(bizId)){
					tablename = "WOBO_PERSON_CS_UPDATE_H";
				}
			}
			if("0".equals(enterprise.get("IF_PRODUCE")) && "1".equals(enterprise.get("IF_HANDLE"))){
				tablename = "WOBO_PERSON_CZ";
				if(!"".equals(bizId)){
					tablename = "WOBO_PERSON_CZ_UPDATE_H";
				}
			}
		}
		String sql = "select b.*,a.PI_ID from "+ tablename + " a, WOBO_USER b where a.EP_ID = ? and a.USER_ID = b.USER_ID and b.STATUS in ('0','1') and a.IFADMIN = '01' order by a.PI_ID asc";
		if(bizId != null && !"".equals(bizId)){
			Record adminH = Db.findFirst("select VERSION from "+tablename+" where EP_ID = ? and IFADMIN = '01' and BIZ_ID = ? order by VERSION desc", epId, bizId);
			int version = 0;
			if(adminH != null){
				version = adminH.getInt("VERSION");
			}else{
				adminH = Db.findFirst("select VERSION from "+tablename+" where EP_ID = ? and IFADMIN = '01' order by VERSION desc", epId);
				if(adminH != null){
					version = adminH.getInt("VERSION");
				}
			}
			if(adminH != null){
				sql = "select a.PI_ID,b.* from "+ tablename + " a, WOBO_USER b where a.EP_ID = ? and a.USER_ID = b.USER_ID and b.STATUS in ('0','1') and a.IFADMIN = '01' and a.BIZ_ID = '"+bizId+"' and a.VERSION = "+version+" order by a.PI_ID asc";
			}
			Record record = Db.findFirst("select * from WOBO_APPLY_LIST where AYL_ID = ? and STATUS = '04'", bizId);
			if(record != null){
				sql = "select a.PI_ID,b.* from "+ tablename + " a, WOBO_USER b where a.EP_ID = ? and a.USER_ID = b.USER_ID and b.STATUS in ('0','1') and a.IFADMIN = '01' and a.BIZ_ID = '"+bizId+"' and a.VERSION = "+(version-1)+" order by a.PI_ID asc";
			}
		}
		List<Record> admins = Db.find(sql, epId);
		if(!"".equals(bizId) && admins.size() == 0){
			admins = Db.find("select b.*,a.PI_ID from "+ tablename + " a, WOBO_USER b where a.EP_ID = ? and a.USER_ID = b.USER_ID and b.STATUS in ('0','1') and a.IFADMIN = '01' and a.BIZ_ID <> '"+bizId+"' order by a.PI_ID asc", epId);
		}
		List<Map<String, Object>> adminList = new ArrayList<>();
		if(admins != null){
			for(int i=0; i<admins.size(); i++){
				Map<String, Object> administrator = new HashMap<>();
				administrator.put("adminName", admins.get(i).get("NAME"));
				administrator.put("adminPwd", admins.get(i).get("PWD"));
				administrator.put("userId", admins.get(i).get("USER_ID"));
				administrator.put("personId", admins.get(i).get("PI_ID"));
				if("1".equals(admins.get(i).get("STATUS"))){
					administrator.put("btnFlag", false);
					administrator.put("btnStatus", "3");
					administrator.put("btnTitle", "禁用");
				}else{
					administrator.put("btnFlag", true);
					administrator.put("btnStatus", "1");
					administrator.put("btnTitle", "启用");
				}
				administrator.put("sort", i+3);
				if("0".equals(admins.get(i).get("STATUS"))){
					administrator.put("Flag", false);
				}else{
					administrator.put("Flag", true);
				}
				adminList.add(administrator);
			}
		}
		return adminList;
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170303
	 * 方法：管理单位管理员
	 */
	public boolean adminManage(String userId, String status){
		int result = Db.update("update WOBO_USER set STATUS = ? where USER_ID = ? and STATUS in ('1','3')", status, userId);
		if(result > 0){
			return true;
		}
		return false;
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170303
	 * 方法：重置密码
	 */
	public boolean resetPwd(String userId){
		int result1 = Db.update("update WOBO_USER set PWD = ? where USER_ID = ? and STATUS in ('1','3')", PropKit.get("resetPwd"), userId);
		if(result1 > 0){
			return true;
		}
		return false;
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170306
	 * 方法：查询单位类型
	 * @return 公司人员表
	 */
	public String epPersonTable(String epId){
		String tablename = "";
		Record enterprise = Db.findFirst("select * from WOBO_ENTERPRISE where EP_ID = ? and STATUS in ('0','1')", epId);
		if(enterprise != null){
			//0.不属于；1.属于
			if("1".equals(enterprise.get("IF_PRODUCE")) && "0".equals(enterprise.get("IF_HANDLE"))){
				tablename = "WOBO_PERSON_CS";
			}
			if("0".equals(enterprise.get("IF_PRODUCE")) && "1".equals(enterprise.get("IF_HANDLE"))){
				tablename = "WOBO_PERSON_CZ";
			}
		}
		return tablename;
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170303
	 * 方法：删除管理员
	 */
	public boolean adminDel(String userId, String tablename){
		if(Db.update("update WOBO_USER set STATUS = '2' where USER_ID = ? and STATUS = '0'", userId) > 0){
			return Db.update("delete from "+tablename+"_UPDATE where USER_ID = ?", userId) > 0;
		}
		return false;
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170412
	 * 方法：保存人员二维码信息
	 */
	public String savePersonCode(String epId, String personId, String tablename, String contents, String personName){
		String path = JFinal.me().getServletContext().getRealPath("/");
		String pathSave = "/upload/personCode/" + epId + "_" + DateKit.toStr(new Date(), "yyyyMMddHHmmss")+".jpg";
		path = path + pathSave;
		QRcodeKit.encodePR(contents, 170, 170, path);
		if(Db.update("update "+tablename+" set CODE_PATH = ? where PI_ID = ? and EP_ID = ? and NAME = ?", pathSave, personId, epId, personName) > 0){
			return pathSave;
		}
		return "";
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170412
	 * 方法：查询二维码信息
	 */
	public String queryCodeInfo(String epId, String personId, String tablename, String url, String personName){
		Record record = Db.findFirst("select CODE_PATH from "+tablename+" where  PI_ID = ? and EP_ID = ? and NAME = ?", personId, epId, personName);
		if(record != null && record.get("CODE_PATH") != null && !"".equals(record.get("CODE_PATH"))){
			return url+record.getStr("CODE_PATH");
		}else{
			return "";
		}
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170412
	 * 方法：查询人员是否存在
	 */
	public boolean checkPersonExist(String epId, String personId, String tablename, String roleType, String personName){
		String sql = "";
		if("0".equals(roleType) || "5".equals(roleType)){
			sql = "select * from "+tablename+" where PI_ID = ? and EP_ID = ? and IFHANDORVE = '01' and NAME = ?";
		}else{
			sql = "select * from "+tablename+" where PI_ID = ? and EP_ID = ? and IFDRIVER = '01' and NAME = ?";
		}
		Record record = Db.findFirst(sql, personId, epId, personName);
		if(record != null){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170412
	 * 方法：查询车辆是否存在
	 */
	public boolean checkCarExist(String epId, String carId, String plateNumber){
		Record record = Db.findFirst("select * from WOBO_CAR where CI_ID = ? and EP_ID = ? and PLATE_NUMBER = ?", carId, epId, plateNumber);
		if(record != null){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170412
	 * 方法：保存车辆二维码信息
	 */
	public String saveCarCode(String epId, String carId, String contents, String plateNumber){
		String path = JFinal.me().getServletContext().getRealPath("/");
		String pathSave = "/upload/carCode/" + epId + "_" + DateKit.toStr(new Date(), "yyyyMMddHHmmss")+".jpg";
		path = path + pathSave;
		QRcodeKit.encodePR(contents, 170, 170, path);
		if(Db.update("update WOBO_CAR set CODE_PATH = ? where CI_ID = ? and EP_ID = ? and PLATE_NUMBER = ?", pathSave, carId, epId, plateNumber) > 0){
			return pathSave;
		}
		return "";
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170412
	 * 方法：查询车辆二维码信息
	 */
	public String queryCodeInfoForCar(String epId, String carId, String url, String plateNumber){
		Record record = Db.findFirst("select CODE_PATH from WOBO_CAR where CI_ID = ? and EP_ID = ? and PLATE_NUMBER = ?", carId, epId, plateNumber);
		if(record != null && record.get("CODE_PATH") != null && !"".equals(record.get("CODE_PATH"))){
			return url+record.getStr("CODE_PATH");
		}else{
			return "";
		}
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170412
	 * 方法：查询提交审核信息
	 */
	public Map<String, Object> queryApplyInfo(String epId, String bizName, String bizId){
		String sql = "select * from WOBO_APPLY_LIST where EP_ID = ? and BIZ_NAME = ? and BIZ_ID = ? and STATUS not in ('00') order by sysdate desc";
		Record btnFlag = Db.findFirst(sql, epId, bizName, bizId);
		Map<String, Object> returnMap = new HashMap<>();
		if(btnFlag != null){
			if("01".equals(btnFlag.get("STATUS")) || "02".equals(btnFlag.get("STATUS")) || "05".equals(btnFlag.get("STATUS"))){
				returnMap.put("btnFlag", true);
			}else{
				returnMap.put("btnFlag", false);
			}
			returnMap.put("applyId", btnFlag.get("AYL_ID"));
			returnMap.put("epName", btnFlag.get("EP_NAME"));
		}else{
			returnMap.put("btnFlag", false);
			returnMap.put("applyId", "");
			returnMap.put("epName", "");
		}
		return returnMap;
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170428
	 * 方法：删除交接员
	 */
	public boolean delConnect(String personId, String tablename){
		return Db.update("delete from "+tablename+" where PI_ID = ?", personId) > 0;
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170428
	 * 方法：删除车辆
	 */
	public boolean delCar(String carId){
		return Db.update("delete from WOBO_CAR where CI_ID = ?", carId) > 0;
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170428
	 * 方法：检验交接员是否有未完成运程
	 */
	public boolean checkPersonTask(String personId){
		Record record = Db.findFirst("select * from WOBO_TRANSFER_BIG where STATUS = '3' and CZ_DRIVER_ID = ?", personId);
		if(record != null){
			return false;
		}else{
			return true;
		}
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170428
	 * 方法：获取交接员id
	 */
	public String getConnectId(String epId, String personName, String tablename){
		Record record = Db.findFirst("select PI_ID from "+tablename+" where EP_ID = ? and NAME = ? and IFADMIN = '00'", epId, personName);
		if(record != null){
			return record.getStr("PI_ID");
		}
		return "";
	}
	
	/**
	 * @author weizanting
	 * @date 20170515
	 * 方法：获取车辆类型
	 */
	public List<Map<String, Object>> queryCarType(){
		List<Record> recordList = Db.find("select * from CarTypeCode where bigcode='2' order by id");
		Map<String, Object> map = null;
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		if(recordList.size() > 0){
			map = new HashMap<String, Object>();
			map.put("value", "0");
			map.put("name", "请选择车辆类型");
			list.add(map);
			for(Record record:recordList){
				map = new HashMap<String, Object>();
				map.put("value", record.get("id"));
				map.put("name", record.get("name"));
				list.add(map);
			}
		}
		return list;
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170619
	 * 方法：获取所属环保局
	 */
	public String getBelongOrgSepa(String epId){
		Record belongOrg = Db.findFirst("select BELONGSEPA from WOBO_ENTERPRISE where EP_ID = ? and STATUS = '1'", epId);
		if(belongOrg != null){
			return belongOrg.get("BELONGSEPA");
		}
		return "";
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170619
	 * 方法：根据区域获取中转单位信息
	 */
	public List<Map<String, Object>> queryBelongOrgBySepa(String sepa, String epId){
		//查询中转单位信息
		List<Record> transferOrgs = Db.find("select * from WOBO_ENTERPRISE where ORGTYPE = '0' and STATUS = '1' and BELONGSEPA = ? and EP_ID <> ?", sepa, epId);
		List<Map<String, Object>> transferOrgList = new ArrayList<>();
		Map<String, Object> transferOrg = new HashMap<>();
		transferOrg.put("value", "0");
		transferOrg.put("orgName", "请选择");
		transferOrgList.add(transferOrg);
		if(transferOrgs != null){
			for(Record org : transferOrgs){
				transferOrg = new HashMap<>();
				transferOrg.put("value", org.get("EP_ID"));
				transferOrg.put("orgName", org.get("EP_NAME"));
				transferOrgList.add(transferOrg);
			}
		}
		return transferOrgList;
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170619
	 * 方法：查询单位是否是中转单位
	 */
	public String queryOrgType(String epId){
		Record ep = Db.findFirst("select ORGTYPE from WOBO_ENTERPRISE where EP_ID = ? and STATUS = '1'", epId);
		if(ep != null && ep.get("ORGTYPE") != null && !"".equals(ep.get("ORGTYPE"))){
			return ep.getStr("ORGTYPE");
		}
		return "";
	}
	
	/**
	 * @author weizanting
	 * @date 20170707
	 * 方法：查询管理员是否被修改
	 */
	public boolean checkedAdmin(List<Map<String, Object>> admins, String epId, String tablename){
		List<Record> persons = Db.find("select a.NAME from "+tablename+" as a, WOBO_USER as b where a.EP_ID = ? and a.IFADMIN ='01' and a.USER_ID = b.USER_ID and b.STATUS in ('1','3')", epId);
		int i = 0;
		if(persons.size() > 0){
			for(Record person : persons){
				for(Map<String, Object> admin : admins){
					if(person.get("NAME").equals(admin.get("adminName"))){
						i++;
					}
				}
			}
			if(persons.size() == admins.size() && persons.size() == i){
				return false;
			}else{
				return true;
			}
		}else{
			return true;
		}
	}
}
