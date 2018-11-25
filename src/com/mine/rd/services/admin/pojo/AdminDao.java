package com.mine.rd.services.admin.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.ehcache.CacheKit;
import com.mine.pub.kit.DateKit;
import com.mine.pub.kit.mail.EmailConst;
import com.mine.pub.kit.mail.EmailUtils;
import com.mine.pub.pojo.BaseDao;
import com.mine.rd.services.login.pojo.LoginDao;

public class AdminDao extends BaseDao {
	
	List<Record> dictList = CacheKit.get("mydict", "forgetpwd_status");
	List<Record> cityList = CacheKit.get("mydict", "city_q");

	/**
	 * @author ouyangxu
	 * @date 20170222
	 * 方法：管理员待办任务条数
	 */
	public int adminTaskNum(String area, String ROLEID){
		String sql = "";
		if("SJSPROLE".equals(ROLEID)){		//SJSPROLE-市级管理员
			sql = "select count(*) as num from WOBO_APPLY_LIST where STATUS = '02'";
		}else{
			sql = "select count(*) as num from WOBO_APPLY_LIST where BELONG_SEPA = '"+area+"' and  STATUS in ('01','05')";
		}
		Record record = Db.findFirst(sql);
		if(record != null){
			return record.getInt("num");
		}else{
			return 0;
		}
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170222
	 * 方法：管理员待处理忘记密码条数
	 */
	public int forgetPwdNum(){
		Record record = Db.findFirst("select count(*) as num from WOBO_FORGETPWD where STATUS = '1'");
		if(record != null){
			return record.get("num");
		}
		return 0;
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170222
	 * 方法：管理员任务
	 */
	public Map<String, Object> adminTask(String area, String action, String BTOF_ID, int pn, int ps, String ROLEID, Object searchContent, Object sepaValue){
		String sql = "";
		String stepName = "";
		String sqlSelect = "";
		if("SJSPROLE".equals(ROLEID)){		//SJSPROLE-市级管理员
			sqlSelect = "select * ";
			if("finished".equals(action)){		//finished-已办业务
				sql = "from (select AYL_ID,EP_ID,EP_NAME,BIZ_ID,BIZ_NAME,BELONG_SEPA,APPLY_DATE,STATUS,(case APPLY_DATE when '' then '' else CONVERT(varchar(100), APPLY_DATE, 120) end) as APPLY_DATE_S from WOBO_APPLY_LIST where STATUS in ('04','05')) as A where A.STATUS in ('04','05')";
				if(searchContent != null && !"".equals(searchContent)){
					sql = sql + " and (A.AYL_ID like '%"+searchContent+"%' or A.EP_NAME like '%"+searchContent+"%' or A.BIZ_NAME like '%"+searchContent+"%' or A.APPLY_DATE_S like '%"+searchContent+"%')";
				}
				if(sepaValue != null && !"".equals(sepaValue)){
					sql = sql + " and A.BELONG_SEPA in ("+sepaValue+")";
				}
				sql = sql + " order by A.APPLY_DATE desc";
			}else{		//待办业务
				sql = "from (select AYL_ID,EP_ID,EP_NAME,BIZ_ID,BIZ_NAME,BELONG_SEPA,APPLY_DATE,STATUS,(case APPLY_DATE when '' then '' else CONVERT(varchar(100), APPLY_DATE, 120) end) as APPLY_DATE_S from WOBO_APPLY_LIST where STATUS = '02') as A where A.STATUS = '02'";
				if(searchContent != null && !"".equals(searchContent)){
					sql = sql + " and (A.AYL_ID like '%"+searchContent+"%' or A.EP_NAME like '%"+searchContent+"%' or A.BIZ_NAME like '%"+searchContent+"%' or A.APPLY_DATE_S like '%"+searchContent+"%')";
				}
				if(sepaValue != null && !"".equals(sepaValue)){
					sql = sql + " and A.BELONG_SEPA in ("+sepaValue+")";
				}
				sql = sql + " order by A.APPLY_DATE desc";
			}
			stepName = "市级审批";
		}else{		//区级管理员
			sqlSelect = "select * ";
			if("finished".equals(action)){		//finished-已办业务
				sql = "from (select distinct a.AYL_ID,a.EP_ID,a.EP_NAME,a.BIZ_ID,a.BIZ_NAME,a.BELONG_SEPA,APPLY_DATE,STATUS,(case a.APPLY_DATE when '' then '' else CONVERT(varchar(100), a.APPLY_DATE, 120) end) as APPLY_DATE_S from WOBO_APPLY_LIST a, WOBO_APPROVE_LIST b where a.BELONG_SEPA = '"+area+"' and a.STATUS in ('02','03','04') and a.AYL_ID = b.AYL_ID and b.BTOF_ID = '"+BTOF_ID+"') as A where A.STATUS in ('02','03','04')";
				if(searchContent != null && !"".equals(searchContent)){
					sql = sql + " and (A.AYL_ID like '%"+searchContent+"%' or A.EP_NAME like '%"+searchContent+"%' or A.BIZ_NAME like '%"+searchContent+"%' or A.APPLY_DATE_S like '%"+searchContent+"%')";
				}
				sql = sql + " order by A.APPLY_DATE desc";
			}else{		//待办业务
				sql = "from (select distinct AYL_ID,EP_ID,EP_NAME,BIZ_ID,BIZ_NAME,BELONG_SEPA,APPLY_DATE,STATUS,(case APPLY_DATE when '' then '' else CONVERT(varchar(100), APPLY_DATE, 120) end) as APPLY_DATE_S from WOBO_APPLY_LIST where BELONG_SEPA = '"+area+"' and  STATUS in ('01','05')) as A where A.STATUS in ('01','05')";
				if(searchContent != null && !"".equals(searchContent)){
					sql = sql + " and (A.AYL_ID like '%"+searchContent+"%' or A.EP_NAME like '%"+searchContent+"%' or A.BIZ_NAME like '%"+searchContent+"%' or A.APPLY_DATE_S like '%"+searchContent+"%')";
				}
				sql = sql + " order by A.APPLY_DATE desc";
			}
			stepName = "区级审批";
		}
		Page<Record> page = null;
		List<Record> record = new ArrayList<Record>();
		page = Db.paginate(pn, ps, sqlSelect, sql);
		record = page.getList();
		List<Map<String, Object>> list = new ArrayList<>();
		Map<String, Object> resMap = new HashMap<>();
		if(record != null){
			for(Record apply : record){
				Map<String, Object> map = new HashMap<>();
				map.put("AYL_ID", apply.get("AYL_ID"));
				map.put("EP_ID", apply.get("EP_ID"));
				map.put("EP_NAME", apply.get("EP_NAME"));
				map.put("BIZ_ID", apply.get("BIZ_ID"));
				map.put("BIZ_NAME", apply.get("BIZ_NAME"));
				map.put("STEP_NAME", stepName);
				map.put("BELONG_SEPA", apply.get("BELONG_SEPA"));
				map.put("SEPA_NAME", convert(cityList, apply.get("BELONG_SEPA")) + "环保局");
				map.put("APPLY_DATE", apply.get("APPLY_DATE_S"));
				list.add(map);
			}
		}
		resMap.put("applys", list);
		if("SJSPROLE".equals(ROLEID)){
			//获取字典表——区信息
			resMap.put("sepaList", super.queryDict("city_q", "value", "text"));
		}
		resMap.put("totalPage", page.getTotalPage());
		resMap.put("totalRow", page.getTotalRow());
		return resMap;
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170424
	 * 方法：单位任务
	 */
	public Map<String, Object> epTask(String epId, String action, int pn, int ps, Object searchContent){
		String sql = "";
		String sqlSelect = "";
		if("finished".equals(action)){		//finished-已办业务
			sqlSelect = "select * ";
			sql = "from (select AYL_ID,EP_ID,EP_NAME,BIZ_ID,BIZ_NAME,BELONG_SEPA,APPLY_DATE,STATUS,(case APPLY_DATE when '' then '' else CONVERT(varchar(100), APPLY_DATE, 120) end) as APPLY_DATE_S,(case when STATUS = '01' or STATUS = '05' then '区级审批' when STATUS = '02' then '市级审批' when STATUS = '04' then '审批通过' else '' end) as STEP_NAME from WOBO_APPLY_LIST where EP_ID = ? and STATUS in ('01', '02', '04', '05') and (BIZ_ID like '%EP%' or BIZ_ID like '%PI%' or BIZ_ID like '%PE%')) as A where A.STATUS in ('01', '02', '04', '05')";
			if(searchContent != null && !"".equals(searchContent)){
				sql = sql + " and (A.AYL_ID like '%"+searchContent+"%' or A.EP_NAME like '%"+searchContent+"%' or A.BIZ_NAME like '%"+searchContent+"%' or A.APPLY_DATE_S like '%"+searchContent+"%' or A.STEP_NAME like '%"+searchContent+"%')";
			}
			sql = sql + " order by A.APPLY_DATE desc";
		}else{		//待办业务
			sqlSelect = "select * ";
			sql = "from (select AYL_ID,EP_ID,EP_NAME,BIZ_ID,BIZ_NAME,BELONG_SEPA,APPLY_DATE,STATUS,(case APPLY_DATE when '' then '' else CONVERT(varchar(100), APPLY_DATE, 120) end) as APPLY_DATE_S,(case when STATUS = '00' then '待提交' when STATUS = '03' then '被驳回' else '' end) as STEP_NAME from WOBO_APPLY_LIST where EP_ID = ? and STATUS in ('00', '03') and (BIZ_ID like '%EP%' or BIZ_ID like '%PI%' or BIZ_ID like '%PE%')) as A where A.STATUS in ('00', '03')";
			if(searchContent != null && !"".equals(searchContent)){
				sql = sql + " and (A.AYL_ID like '%"+searchContent+"%' or A.EP_NAME like '%"+searchContent+"%' or A.BIZ_NAME like '%"+searchContent+"%' or A.APPLY_DATE_S like '%"+searchContent+"%' or A.STEP_NAME like '%"+searchContent+"%')";
			}
			sql = sql + " order by A.APPLY_DATE desc";
		}
		Page<Record> page = null;
		List<Record> record = new ArrayList<Record>();
		page = Db.paginate(pn, ps, sqlSelect, sql, epId);
		record = page.getList();
		List<Map<String, Object>> list = new ArrayList<>();
		Map<String, Object> resMap = new HashMap<>();
		if(record != null){
			for(Record apply : record){
				Map<String, Object> map = new HashMap<>();
				map.put("AYL_ID", apply.get("AYL_ID"));
				map.put("EP_ID", apply.get("EP_ID"));
				map.put("EP_NAME", apply.get("EP_NAME"));
				map.put("BIZ_ID", apply.get("BIZ_ID"));
				map.put("BIZ_NAME", apply.get("BIZ_NAME"));
				map.put("STEP_NAME", apply.get("STEP_NAME"));
				map.put("BELONG_SEPA", apply.get("BELONG_SEPA"));
				map.put("SEPA_NAME", convert(cityList, apply.get("BELONG_SEPA")) + "环保局");
				map.put("APPLY_DATE", apply.get("APPLY_DATE_S"));
				list.add(map);
			}
		}
		resMap.put("applys", list);
		resMap.put("totalPage", page.getTotalPage());
		resMap.put("totalRow", page.getTotalRow());
		return resMap;
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170424
	 * 方法：单位管理员任务
	 */
	public Map<String, Object> epAdminTask(String epId, String action, int pn, int ps, Object searchContent){
		String sql = "";
		String sqlSelect = "";
		List<Record> record = new ArrayList<Record>();
		Page<Record> page = null;
		if("finished".equals(action)){		//finished-已办业务
			sqlSelect = "select * ";
			sql = "from (select AYL_ID,EP_ID,EP_NAME,BIZ_ID,BIZ_NAME,BELONG_SEPA,APPLY_DATE,STATUS,(case APPLY_DATE when '' then '' else CONVERT(varchar(100), APPLY_DATE, 120) end) as APPLY_DATE_S,(case when STATUS = '01' or STATUS = '05' then '区级审批' when STATUS = '02' then '市级审批' when STATUS = '04' then '审批通过' else '' end) as STEP_NAME from WOBO_APPLY_LIST where EP_ID = ? and STATUS in ('01', '02', '04', '05') and (BIZ_ID like '%TP%' or BIZ_ID like '%TM%')) as A where A.STATUS in ('01', '02', '04', '05')";
			if(searchContent != null && !"".equals(searchContent)){
				sql = sql + " and (A.AYL_ID like '%"+searchContent+"%' or A.EP_NAME like '%"+searchContent+"%' or A.BIZ_NAME like '%"+searchContent+"%' or A.APPLY_DATE_S like '%"+searchContent+"%' or A.STEP_NAME like '%"+searchContent+"%')";
			}
			sql = sql + " order by A.APPLY_DATE desc";
		}else{		//待办业务
			sqlSelect = "select * ";
			sql = "from (select AYL_ID,EP_ID,EP_NAME,BIZ_ID,BIZ_NAME,BELONG_SEPA,APPLY_DATE,STATUS,(case APPLY_DATE when '' then '' else CONVERT(varchar(100), APPLY_DATE, 120) end) as APPLY_DATE_S,(case when STATUS = '00' then '待提交' when STATUS = '03' then '被驳回' else '' end) as STEP_NAME from WOBO_APPLY_LIST where EP_ID = ? and STATUS in ('00', '03') and (BIZ_ID like '%TP%' or BIZ_ID like '%TM%')) as A where STATUS in ('00', '03')";
			if(searchContent != null && !"".equals(searchContent)){
				sql = sql + " and (A.AYL_ID like '%"+searchContent+"%' or A.EP_NAME like '%"+searchContent+"%' or A.BIZ_NAME like '%"+searchContent+"%' or A.APPLY_DATE_S like '%"+searchContent+"%' or A.STEP_NAME like '%"+searchContent+"%')";
			}
			sql = sql + " order by A.APPLY_DATE desc";
		}
		page = Db.paginate(pn, ps, sqlSelect, sql, epId);
		record = page.getList();
		List<Map<String, Object>> list = new ArrayList<>();
		Map<String, Object> resMap = new HashMap<>();
		if(record != null){
			for(Record apply : record){
				Map<String, Object> map = new HashMap<>();
				map.put("AYL_ID", apply.get("AYL_ID"));
				map.put("EP_ID", apply.get("EP_ID"));
				map.put("EP_NAME", apply.get("EP_NAME"));
				map.put("BIZ_ID", apply.get("BIZ_ID"));
				map.put("BIZ_NAME", apply.get("BIZ_NAME"));
				map.put("STEP_NAME", apply.get("STEP_NAME"));
				map.put("BELONG_SEPA", apply.get("BELONG_SEPA"));
				map.put("SEPA_NAME", convert(cityList, apply.get("BELONG_SEPA")) + "环保局");
				map.put("APPLY_DATE", apply.get("APPLY_DATE_S"));
				list.add(map);
			}
		}
		resMap.put("applys", list);
		resMap.put("totalPage", page.getTotalPage());
		resMap.put("totalRow", page.getTotalRow());
		return resMap;
	}
	
	
	/**
	 * @author ouyangxu
	 * @date 20170222
	 * 方法：管理员处理任务
	 */
	public boolean adminDealTask(Map<String, Object> map){
		Record check = Db.findFirst("select BIZ_VERSION from WOBO_APPROVE_LIST where AYL_ID = ? and BIZ_STEP = ?", map.get("applyId"), map.get("bizStep"));
		int bizVersion = 0;
		if(check != null){
			bizVersion = Integer.parseInt(check.get("BIZ_VERSION").toString()) + 1;
		}
		Record record = new Record();
		record.set("AL_ID", super.getSeqId("WOBO_APPROVE_LIST"));
		record.set("EP_NAME", map.get("epName"));
		record.set("BIZ_ID", map.get("bizId"));
		record.set("BIZ_TYPE", map.get("bizName"));
		record.set("BIZ_STEP", map.get("bizStep"));
		record.set("BIZ_VERSION", bizVersion);
		record.set("CHECK_RESULT", map.get("checkResult"));
		record.set("CHECK_ADVICE", map.get("checkAdvice"));
		record.set("CHECK_DATE", super.getSysdate());
		record.set("BTO_ID", map.get("btoId"));
		record.set("BTO_NAME", map.get("btoName"));
		record.set("BTOF_ID", map.get("btofId"));
		record.set("BTOF_NAME", map.get("btofName"));
		record.set("AYL_ID", map.get("applyId"));
		record.set("sysdate", super.getSysdate());
		return Db.save("WOBO_APPROVE_LIST", "AL_ID", record);
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170417
	 * 方法：存取单位信息过渡表
	 */
	public boolean saveTransitionInfo(String epId, String tablename, String bizIdStart){
		if("PE".equals(bizIdStart)){	//PE-医疗机构信息维护
			Record enterprise = Db.findFirst("select * from WOBO_ENTERPRISE where EP_ID = ?", epId);
			if(enterprise == null){
				return false;
			}
			Record enterpriseUpdate = Db.findFirst("select * from WOBO_ENTERPRISE_UPDATE where EP_ID = ?", epId);
			if(enterpriseUpdate == null){
				return false;
			}
			if(Db.update("delete from WOBO_ENTERPRISE where EP_ID = ?", epId) <= 0){
				return false;
			}
			enterpriseUpdate.remove("BIZ_ID");
			if(!Db.save("WOBO_ENTERPRISE", "EP_ID", enterpriseUpdate)){
				return false;
			}
			Record epRelation = Db.findFirst("select * from WOBO_EPRELATION where EP_SON_ID = ? and STATUS = '1'", epId);
			if(epRelation != null){
				if(Db.update("delete from WOBO_EPRELATION where EP_SON_ID = ? and STATUS = '1'", epId) <= 0){
					return false;
				}
			}
			Record epRelationUpdate = Db.findFirst("select * from WOBO_EPRELATION_UPDATE where EP_SON_ID = ? and STATUS = '1'", epId);
			if(epRelationUpdate != null){
				epRelationUpdate.remove("BIZ_ID");
				if(!Db.save("WOBO_EPRELATION", epRelationUpdate)){
					return false;
				}
				if(Db.update("delete from WOBO_EPRELATION_UPDATE where EP_SON_ID = ? and STATUS = '1'", epId) <= 0){
					return false;
				}
			}
			if(Db.update("delete from WOBO_ENTERPRISE_UPDATE where EP_ID = ?", epId) <= 0){
				return false;
			}
		}
		if("PI".equals(bizIdStart)){	//PI-管理员信息维护
			if(tablename != null && !"".equals(tablename)){
				String sql3 = "select * from "+tablename+" where EP_ID = ?";
				if("PI".equals(bizIdStart)){
					sql3 = "select * from "+tablename+" where EP_ID = ? and IFADMIN = '01'";
				}
				Record persons = Db.findFirst(sql3, epId);
				if(persons != null){
					String sql2 = "delete from "+tablename+" where EP_ID = ?";
					if("PI".equals(bizIdStart)){
						sql2 = "delete from "+tablename+" where EP_ID = ? and IFADMIN = '01'";
					}
					if(Db.update(sql2, epId) <= 0){
						return false;
					}
				}
				String sql1 = "select * from "+tablename+"_UPDATE where EP_ID = ?";
				if("PI".equals(bizIdStart)){
					sql1 = "select * from "+tablename+"_UPDATE where EP_ID = ? and IFADMIN = '01'";
				}
				List<Record> personUpdate = Db.find(sql1, epId);
				if(personUpdate != null){
					for(Record person : personUpdate){
						person.remove("BIZ_ID");
						if(!Db.save(tablename, "PI_ID", person)){
							return false;
						}
					}
				}
				String sql2 = "delete from "+tablename+"_UPDATE where EP_ID = ?";
				if("PI".equals(bizIdStart)){
					sql2 = "delete from "+tablename+"_UPDATE where EP_ID = ? and IFADMIN = '01'";
				}
				if(Db.update(sql2, epId) <= 0){
					return false;
				}
//				if("EP".equals(bizIdStart)){
//					if("WOBO_PERSON_CZ".equals(tablename)){
//						Record carUpdate = Db.findFirst("select * from WOBO_CAR_UPDATE where EP_ID = ?", epId);
//						if(carUpdate != null){
//							if(Db.update("delete from WOBO_CAR_UPDATE where EP_ID = ?", epId) <= 0){
//								return false;
//							}
//						}
//						List<Record> cars = Db.find("select * from WOBO_CAR where EP_ID = ?", epId);
//						if(cars != null){
//							for(Record car : cars){
//								if(!Db.save("WOBO_CAR_UPDATE", "CI_ID", car)){
//									return false;
//								}
//							}
//						}
//					}
//				}
			}
		}
		return true;
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170417
	 * 方法：存取单位信息历史表
	 */
	public boolean saveHistoryInfo(String epId, String tablename, String bizId, String bizIdStart){
		if("PE".equals(bizIdStart)){	//PE-医疗机构信息维护
			//查询单位原信息
			Record enterprise = Db.findFirst("select * from WOBO_ENTERPRISE where EP_ID = ?", epId);
			if(enterprise == null){
				return false;
			}
			Record epH = Db.findFirst("select * from WOBO_ENTERPRISE_UPDATE_H where BIZ_ID = ? and EP_ID = ?", bizId, epId);
			if(epH == null){
				Record enterpriseUpdate = Db.findFirst("select VERSION from WOBO_ENTERPRISE_UPDATE_H where EP_ID = ? order by VERSION desc", epId);
				if(enterpriseUpdate != null){
					enterprise.set("VERSION", enterpriseUpdate.getInt("VERSION")+1);
				}else{
					enterprise.set("VERSION", 1);
				}
				enterprise.set("BIZ_ID", bizId);
				if(!Db.save("WOBO_ENTERPRISE_UPDATE_H", "EP_ID", enterprise)){
					return false;
				}
			}
			Record epRH = Db.findFirst("select * from WOBO_EPRELATION_UPDATE_H where BIZ_ID = ? and EP_SON_ID = ?", bizId, epId);
			if(epRH == null){
				Record epRelationUpdate = Db.findFirst("select VERSION from WOBO_EPRELATION_UPDATE_H where EP_SON_ID = ? and STATUS = '1' order by VERSION desc", epId);
				int epRelationVersion = 1;
				if(epRelationUpdate != null){
					epRelationVersion = epRelationUpdate.getInt("VERSION")+1;
				}
				Record epRelation = Db.findFirst("select * from WOBO_EPRELATION where EP_SON_ID = ? and STATUS = '1'", epId);
				if(epRelation == null){
					epRelation = new Record();
					epRelation.set("EP_MAIN_ID", "0");
					epRelation.set("EP_SON_ID", epId);
					epRelation.set("STATUS", "1");
				}
				epRelation.set("VERSION", epRelationVersion);
				epRelation.set("BIZ_ID", bizId);
				if(!Db.save("WOBO_EPRELATION_UPDATE_H", epRelation)){
					return false;
				}
			}
		}
		if("PI".equals(bizIdStart)){	//PI-管理员信息维护
			if(tablename != null && !"".equals(tablename)){
//				String sql1 = "select * from "+tablename+" where EP_ID = ?";
//				if("PI".equals(bizIdStart)){
				String sql1 = "select * from "+tablename+" where EP_ID = ? and IFADMIN = '01'";
//				}
				List<Record> persons = Db.find(sql1, epId);
				if(persons != null){
					Record personH = Db.findFirst("select * from "+tablename+"_UPDATE_H where EP_ID = ? and IFADMIN = '01' and BIZ_ID = ?", epId, bizId);
					if(personH == null){
//						String sql2 = "select VERSION from "+tablename+"_UPDATE_H where EP_ID = ? order by VERSION desc";
//						if("PI".equals(bizIdStart)){
						String sql2 = "select VERSION from "+tablename+"_UPDATE_H where EP_ID = ? and IFADMIN = '01' order by VERSION desc";
//						}
						Record personUpdate = Db.findFirst(sql2, epId);
						int version = 1;
						if(personUpdate != null){
							version = personUpdate.getInt("VERSION")+1;
						}
						for(Record person : persons){
							person.set("VERSION", version);
							person.set("BIZ_ID", bizId);
							if(!Db.save(tablename+"_UPDATE_H", "PI_ID", person)){
								return false;
							}
						}
					}
				}
//				if("EP".equals(bizIdStart)){
//					if("WOBO_PERSON_CZ".equals(tablename)){
//						List<Record> cars = Db.find("select * from WOBO_CAR where EP_ID = ?", epId);
//						if(cars != null){
//							Record carUpdate = Db.findFirst("select VERSION from WOBO_CAR_UPDATE_H where EP_ID = ? order by VERSION desc", epId);
//							int version = 1;
//							if(carUpdate != null){
//								version = carUpdate.getInt("VERSION")+1;
//							}
//							for(Record car : cars){
//								car.set("VERSION", version);
//								car.set("BIZ_ID", bizId);
//								if(!Db.save("WOBO_CAR_UPDATE_H", "CI_ID", car)){
//									return false;
//								}
//							}
//						}
//					}
//				}
			}
		}
		return true;
	}
	
	/**
	 * @author ouyangxu
	 * @throws Exception 
	 * @date 20170222
	 * 方法：管理员处理任务(修改WOBO_APPLY_LIST表状态)
	 */
	public Map<String, Object> updateApplyStatus(String bizStep, String checkResult, String applyId, String epId, Map<String, Object> map) throws Exception{
		String status = "";
		String bizId = map.get("bizId").toString();
		String bizIdStart = bizId.substring(0,2);
		String emailContent = "";
		Map<String, Object> returnMap = new HashMap<>();
		if("sjsp".equals(bizStep)){		//sjsp-市级审批
			if("00".equals(checkResult)){
				status = "05";
				emailContent = "申请被市级驳回";
			}else if("01".equals(checkResult)){
				status = "04";
				emailContent = "申请被市级通过";
			}
		}else if("qjsp".equals(bizStep)){		//qjsp-区级审批
			if("00".equals(checkResult)){
				status = "03";
				emailContent = "申请被区级驳回";
			}else if("01".equals(checkResult)){
				status = "02";
				emailContent = "申请被区级通过";
			}
		}
		int flag = Db.update("update WOBO_APPLY_LIST set STATUS = ?, APPLY_DATE = ? where AYL_ID = ?", status, super.getSysdate(), applyId);
		if(flag < 0){
			return null;
		}
		//市级审批通过后操作    04-市级审批通过
		if("04".equals(status)){
			//EP-单位信息完善   PI-管理员信息维护
			if("EP".equals(bizIdStart) || "PI".equals(bizIdStart)){
				//更新企业状态
				if("EP".equals(bizIdStart)){
					int enterpriseFlag = Db.update("update WOBO_ENTERPRISE set STATUS = '1' where EP_ID = ? and STATUS = '0'", epId);
					if(enterpriseFlag < 0){
						return null;
					}
				}
				//更新企业登录账号状态
				Record enterprise = Db.findFirst("select * from WOBO_ENTERPRISE where EP_ID = ? and STATUS = '1'", epId);
				if("EP".equals(bizIdStart)){
					int flag2 = Db.update("update WOBO_USER set STATUS = '1' where USER_ID = ? and STATUS = '0'", enterprise.getStr("USER_ID"));
					if(flag2 < 0){
						return null;
					}
//					emailContent = "单位信息完善" + emailContent;
				}
				String tablename = "";
				if("1".equals(enterprise.get("IF_PRODUCE"))){
					tablename = "WOBO_PERSON_CS";
				}
				if("1".equals(enterprise.get("IF_HANDLE"))){
					tablename = "WOBO_PERSON_CZ";
				}
				String roleId = "";
				if("EP".equals(bizIdStart)){
					if("WOBO_PERSON_CS".equals(tablename)){
						//产生单位角色ID
						roleId = "ZCHCSDW";
					}else{
						//处置单位角色ID
						roleId = "ZCHCZDW";
					}
					//更新单位登录账号角色
					boolean finalFlag = Db.update("update WOBO_USERROLE set ROLE_ID = ? where USER_ID = ? and STATUS = '1'", roleId, enterprise.getStr("USER_ID"))>0;
					if(!finalFlag){
						return null;
					}
				}
				//更新单位管理员状态
				String sql = "select * from " + tablename + " where EP_ID = ? and IFADMIN = '01'";
				if("PI".equals(bizIdStart)){
					sql = "select * from " + tablename + "_UPDATE where EP_ID = ? and IFADMIN = '01'";
				}
				List<Record> users = Db.find(sql, epId);
				if(users != null){
					roleId = "";
					if("WOBO_PERSON_CS".equals(tablename)){
						//产生单位管理员角色ID
						roleId = "CSDWGLY";
						//中转机构产生单位管理员角色
						Record transfer = Db.findFirst("select * from WOBO_ENTERPRISE where EP_ID = ? and ORGTYPE = '0'", epId);
						if(transfer != null){
							roleId = "ZZDWGLY";
						}
					}else{
						//处置单位角色管理员ID
						roleId = "CZDWGLY";
					}
					for(Record user : users){
						int flag3 = Db.update("update WOBO_USER set STATUS = '1' where USER_ID = ? and STATUS = '0'", user.getStr("USER_ID"));
						if(flag3 < 0){
							return null;
						}
						LoginDao loginDao = new LoginDao();
						//关联管理员角色
						if(!loginDao.manageEpRole(user.getStr("USER_ID"), roleId)){
							return null;
						}
					}
				}
				//保存历史表
				if(!this.saveHistoryInfo(epId, tablename, applyId, bizIdStart)){
					return null;
				}
				//保存主表并删除中间表
				if(!this.saveTransitionInfo(epId, tablename, bizIdStart)){
					return null;
				}
			//TP-转移计划申请	
			}else if("TP".equals(bizIdStart)){
				if(Db.update("update WOBO_TRANSFER_PLAN set STATUS = '3' where TP_ID = ?", map.get("TP_ID")) <= 0){
					return null;
				}
			//PE-单位信息维护
			}else if("PE".equals(bizIdStart)){
				//保存历史表
				if(!this.saveHistoryInfo(epId, "", applyId, bizIdStart)){
					return null;
				}
				//保存主表并删除中间表
				if(!this.saveTransitionInfo(epId, "", bizIdStart)){
					return null;
				}
				//查询是否需要修改管理员角色
				Record ep = Db.findFirst("select EP_ID from WOBO_ENTERPRISE where EP_ID = ? and ORGTYPE = '0'", epId);
				if(ep != null){
					List<Record> users = Db.find("select a.USER_ID from WOBO_PERSON_CS a, WOBO_USERROLE b where a.EP_ID = ? and a.USER_ID = b.USER_ID and b.ROLE_ID = ? and b.STATUS = '1'", epId, "CSDWGLY");
					if(users.size() > 0){
						for(Record user : users){
							boolean updateFlag = Db.update("update WOBO_USERROLE set ROLE_ID = ? where USER_ID = ? and ROLE_ID = ? and STATUS = '1'", "ZZDWGLY", user.get("USER_ID"), "CSDWGLY") > 0;
							if(!updateFlag){
								return null;
							}
						}
					}
				}
				//修改单位登录名称
				if(!this.modifyEpNameForLogin(epId)){
					return null;
				}
			//ZZ-运行中转移计划变更中转单位
			}else if("TM".equals(bizIdStart)){
				boolean modifyFlag = Db.update("update WOBO_TRANSFER_PLAN_MODIFY set STATUS = '3' where TM_ID = ? and STATUS = '1'", bizId) > 0;
				if(!modifyFlag){
					return null;
				}
			}
		}
		if("EP".equals(bizIdStart)){
			//查询公司邮箱
			if("04".equals(status) || "02".equals(status) || "03".equals(status) || "05".equals(status)){
				Object email = this.queryEpInfo(epId).get("EMAIL");
				if(email != null && !"".equals(email)){
					String str = PropKit.get("approvalTitle");
					if("04".equals(status) || "02".equals(status)){
						str = str + "通过";
					}else{
						str = str + "驳回";
					}
					returnMap.put("acceptMail", email);
					returnMap.put("subject", str);
					returnMap.put("content", PropKit.get("approvalContent")+emailContent);
					returnMap.put("mail", PropKit.get("email"));
					returnMap.put("mailPwd", PropKit.get("emailPwd"));
//					this.sendMail(email.toString(), str, PropKit.get("approvalContent")+emailContent, PropKit.get("email"), PropKit.get("emailPwd"));
				}
			}
		}
		returnMap.put("status", true);
		return returnMap;
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170801
	 * 方法：修改单位登录名称
	 */
	public boolean modifyEpNameForLogin(String epId){
		Record record = Db.findFirst("select a.EP_NAME, b.NAME, b.USER_ID from WOBO_ENTERPRISE a, WOBO_USER b where a.EP_ID = ? and a.USER_ID = b.USER_ID and a.STATUS = '1' and b.STATUS = '1'", epId);
		if(record != null){
			if(record.get("EP_NAME") != record.get("NAME")){
				return Db.update("update WOBO_USER set NAME = ?, NICK_NAME = ? where USER_ID = ?", record.get("EP_NAME"), record.get("EP_NAME"), record.get("USER_ID")) > 0;
			}else{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170516
	 * 方法：查询单位信息
	 */
	public Map<String, Object> queryEpInfo(String epId){
		Record ep = Db.findFirst("select * from WOBO_ENTERPRISE where EP_ID = ?", epId);
		if(ep != null){
			return ep.getColumns();
		}
		return new HashMap<String, Object>();
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170223
	 * 方法：管理员查看审批记录
	 */
	public List<Map<String, Object>> checkApproveDetail(String applyId){
		List<Record> list = Db.find("select * from WOBO_APPROVE_LIST where AYL_ID = ? order by CHECK_DATE desc", applyId);
		List<Map<String, Object>> returnList = new ArrayList<>();
		if(list != null){
			for(Record approve : list){
				Map<String, Object> map = new HashMap<>();
				map.put("CHECK_DATE", DateKit.toStr(approve.getDate("CHECK_DATE"), "yyyy-MM-dd HH:mm:ss"));
				map.put("BIZ_TYPE", approve.get("BIZ_TYPE"));
				map.put("BTOF_NAME", approve.get("BTOF_NAME"));
				map.put("BTO_NAME", approve.get("BTO_NAME"));
				String result = "";
				if("01".equals(approve.get("CHECK_RESULT"))){
					result = "同意";
				}else{
					result = "驳回";
				}
				map.put("CHECK_RESULT", result);
				map.put("CHECK_ADVICE", approve.get("CHECK_ADVICE"));
				returnList.add(map);
			}
		}
		return returnList;
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170503
	 * 方法：管理员查看忘记密码申请列表
	 */
	public Map<String, Object> queryForgetPwdApplyList(int pn, int ps, Object searchContent, Object statusValue, List<Object> statusCache){
		String sql = "from (select EP_NAME,ORGCARD,NAME,WF_ID,MAIL,APPROVE_MAIL,STATUS,sysdate,(case sysdate when '' then '' else CONVERT(varchar(100), sysdate, 120) end) as APPLY_DATE from WOBO_FORGETPWD where STATUS not in ('0', '2')) as A where A.STATUS not in ('0', '2')";
		if(searchContent != null && !"".equals(searchContent)){
			sql = sql + " and (A.EP_NAME like '%"+searchContent+"%' or A.ORGCARD like '%"+searchContent+"%' or A.NAME like '%"+searchContent+"%' or A.APPLY_DATE like '%"+searchContent+"%' or A.WF_ID like '%"+searchContent+"%' or A.MAIL like '%"+searchContent+"%')";
		}
		if(statusValue != null && !"".equals(statusValue)){
			sql = sql + " and A.STATUS in ("+statusValue+")";
		}
		sql = sql + " order by A.sysdate desc";
		Page<Record> page = Db.paginate(pn, ps, "select *", sql);
		List<Map<String, Object>> list = new ArrayList<>();
		if(page.getList() != null){
			for(Record record : page.getList()){
				Map<String, Object> map = new HashMap<>();
				map.put("EP_NAME", record.get("EP_NAME"));
				map.put("ORGCARD", record.get("ORGCARD"));
				map.put("NAME", record.get("NAME"));
				map.put("APPLY_DATE", record.get("APPLY_DATE"));
				map.put("WF_ID", record.get("WF_ID"));
				map.put("MAIL", record.get("MAIL"));
				map.put("APPROVE_MAIL", record.get("APPROVE_MAIL"));
				map.put("STATUS", record.get("STATUS"));
				map.put("STATUSNAME", convert(dictList, record.get("STATUS")));
				list.add(map);
			}
		}
		Map<String, Object> returnMap = new HashMap<>();
		returnMap.put("forgetPwdApplyList", list);
		if(statusCache != null && !"".equals(statusCache)){
			returnMap.put("statusList", super.queryDict("forgetpwd_status", "value", "text", statusCache));
		}else{
			returnMap.put("statusList", super.queryDict("forgetpwd_status", "value", "text"));
		}
		returnMap.put("totalPage", page.getTotalPage());
		returnMap.put("totalRow", page.getTotalRow());
		return returnMap;
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170503
	 * 方法：管理员查看单位密码
	 */
	public Map<String, Object> queryEpPwd(String epName, String orgCode){
		Record record = Db.findFirst("select b.NAME,b.PWD from WOBO_ENTERPRISE a, WOBO_USER b where a.EP_NAME = ? and a.REGISTERCODE = ? and a.USER_ID = b.USER_ID and a.STATUS = '1' and b.STATUS = '1'", epName, orgCode);
		Map<String, Object> map = new HashMap<String, Object>();
		if(record != null){
			map.put("NAME", record.get("NAME"));
			map.put("PWD", record.get("PWD"));
		}
		return map;
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170503
	 * 方法：管理员修改忘记密码申请信息
	 */
	public boolean updateForgetPwdInfo(String forgetPwdId, String status, String mail){
		return Db.update("update WOBO_FORGETPWD set STATUS=?,APPROVE_MAIL=?,APPROVEDATE=? where WF_ID=?", status,mail,super.getSysdate(),forgetPwdId) > 0;
	}
	
	/**
	 * @author ouyangxu
	 * @throws Exception 
	 * @date 20170503
	 * 方法：管理员同意忘记密码申请 发送邮件
	 */
	public boolean sendMail(String acceptMail, String subject, String content, String mail, String mailPwd) throws Exception{
		String smtp = "";
		String mailFlag = "smtp."+mail.split("@")[1];
		//判断邮件发送协议地址
		if("smtp.163.com".equals(mailFlag)){
			smtp = EmailConst.SMTP_163;
		}else if("smtp.126.com".equals(mailFlag)){
			smtp = EmailConst.SMTP_126;
		}else if("smtp.sina.com".equals(mailFlag)){
			smtp = EmailConst.SMTP_SINA;
		}else if("smtp.gmail.com".equals(mailFlag)){
			smtp = EmailConst.SMTP_GMAIL;
		}else{
			return false;
		}
		//发送邮件
		EmailUtils.send(smtp, mail, mailPwd, acceptMail, subject, content);
		return true;
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170522
	 * 方法：管理员查看未提交转移计划单位
	 */
	public Map<String, Object> queryUnsubmitPlanEp(int pn, int ps, String area, String ROLEID, Object searchContent, Object sepaValue){
		String sql = "";
		if("SJSPROLE".equals(ROLEID)){		//SJSPROLE-市级管理员
			sql = "from (select A.*, B.EP_NAME as EP_NAME_CZ from (select b.EP_ID as EP_ID_CS, b.EP_NAME as EP_NAME_CS, b.BELONGSEPA as BELONGSEPA, a.EP_ID_CZ as EP_ID_CZ,b.TEL,a.EP_ID_ZZ as EP_ID_ZZ,b.STATUS from WOBO_CONDUITS_TRANSFER_UNSUBMIT a, WOBO_ENTERPRISE b where a.EP_ID_CS = b.EP_ID) as A left join WOBO_ENTERPRISE B on A.EP_ID_CZ = B.EP_ID) as C left join WOBO_ENTERPRISE D on C.EP_ID_ZZ = D.EP_ID where C.STATUS = '1'";
			if(searchContent != null && !"".equals(searchContent)){
				sql = sql + " and (C.EP_NAME_CS like '%"+searchContent+"%' or C.EP_NAME_CZ like '%"+searchContent+"%' or C.TEL like '%" + searchContent +"%' or D.EP_NAME like '%"+searchContent+"%')";
			}
			if(sepaValue != null && !"".equals(sepaValue)){
				sql = sql + " and C.BELONGSEPA in ("+sepaValue+")";
			}
		}else{		//区级管理员
			sql = "from (select A.*, B.EP_NAME as EP_NAME_CZ from (select b.EP_ID as EP_ID_CS, b.EP_NAME as EP_NAME_CS, b.BELONGSEPA as BELONGSEPA, a.EP_ID_CZ as EP_ID_CZ,b.TEL,a.EP_ID_ZZ as EP_ID_ZZ,b.STATUS from WOBO_CONDUITS_TRANSFER_UNSUBMIT a, WOBO_ENTERPRISE b where a.EP_ID_CS = b.EP_ID and b.BELONGSEPA = '"+area+"') as A left join WOBO_ENTERPRISE B on A.EP_ID_CZ = B.EP_ID) as C left join WOBO_ENTERPRISE D on C.EP_ID_ZZ = D.EP_ID where C.STATUS = '1'";
			if(searchContent != null && !"".equals(searchContent)){
				sql = sql + " and (C.EP_NAME_CS like '%"+searchContent+"%' or C.EP_NAME_CZ like '%"+searchContent+"%' or C.TEL like '%" + searchContent +"%' or D.EP_NAME like '%"+searchContent+"%')";
			}
		}
		Page<Record> page = null;
		List<Record> record = new ArrayList<Record>();
		page = Db.paginate(pn, ps, "select C.*, D.EP_NAME as EP_NAME_ZZ", sql + " order by C.EP_NAME_CS asc");
		record = page.getList();
		List<Map<String, Object>> list = new ArrayList<>();
		Map<String, Object> resMap = new HashMap<>();
		if(record != null){
			for(Record ep : record){
				Map<String, Object> map = new HashMap<>();
				map.put("EP_ID_CS", ep.get("EP_ID_CS"));
				map.put("EP_NAME_CS", ep.get("EP_NAME_CS"));
				map.put("BELONGSEPA", ep.get("BELONGSEPA"));
				map.put("SEPA_NAME", convert(cityList, ep.get("BELONGSEPA")) + "环保局");
				map.put("EP_ID_CZ", ep.get("EP_ID_CZ"));
				map.put("EP_NAME_CZ", ep.get("EP_NAME_CZ"));
				map.put("EP_ID_ZZ", ep.get("EP_ID_ZZ"));
				map.put("EP_NAME_ZZ", ep.get("EP_NAME_ZZ"));
				map.put("TEL", ep.get("TEL"));
				list.add(map);
			}
		}
		resMap.put("epList", list);
		if("SJSPROLE".equals(ROLEID)){
			resMap.put("sepaList", super.queryDict("city_q", "value", "text"));
		}
		resMap.put("totalPage", page.getTotalPage());
		resMap.put("totalRow", page.getTotalRow());
		return resMap;
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170524
	 * 方法：处置单位管理员查看待确认处置协议
	 */
	public int queryAgreementNum(String epId){
		Record record = Db.findFirst("select count(*) as num from WOBO_AGREEMENT where EN_ID_CZ = ? and STATUS = '2'", epId);
		if(record != null){
			return record.getInt("num");
		}
		return 0;
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170526
	 * 方法：管理员查看单位列表
	 */
	public Map<String, Object> queryEpList(int pn, int ps, String area, String ROLEID, Object searchContent, Object statusValue, Object sepaValue, List<Object> statusCache){
		String sql = "";
		if("SJSPROLE".equals(ROLEID)){		//SJSPROLE-市级管理员
			sql = "from (select a.EP_ID,a.EP_NAME,a.BELONGSEPA,a.sysdate,(case when a.IF_PRODUCE = 1 and a.IF_HANDLE=0 then '医疗机构' when a.IF_PRODUCE = 0 and a.IF_HANDLE=1 then '处置机构' else '' end) as EPTYPE,(case when a.ORGTYPE=0 and a.IF_PRODUCE = 1 then '是' when a.ORGTYPE=1 and a.IF_PRODUCE = 1 then '否' else '—' end) as CSEPTYPE,b.STATUS,(case b.STATUS when '01' then '待审批' when '02' then '待审批' when '05' then '待审批' else '审批通过' end) as STATUSNAME from WOBO_ENTERPRISE a, WOBO_APPLY_LIST b where a.EP_ID = b.EP_ID and b.BIZ_NAME='医疗机构信息完善' and b.STATUS in ('01', '02', '04', '05') and a.STATUS in ('0', '1') and a.EP_ID != 'sysadmin') as A where A.STATUS in ('01', '02', '04', '05')";
			if(searchContent != null && !"".equals(searchContent)){
				sql = sql + " and (A.EP_NAME like '%"+searchContent+"%' or A.EPTYPE like '%"+searchContent+"%' or A.CSEPTYPE like '%"+searchContent+"%')";
			}
			if(statusValue != null && !"".equals(statusValue)){
				if("'04'".equals(statusValue)){
					sql = sql + " and A.STATUS in ("+statusValue+")";
				}else if("'01'".equals(statusValue)){
					sql = sql + " and A.STATUS in ('01', '02', '05')";
				}
			}
			if(sepaValue != null && !"".equals(sepaValue)){
				sql = sql + " and A.BELONGSEPA in ("+sepaValue+")";
			}
			sql = sql + " order by A.sysdate desc";
		}else{		//区级管理员
			sql = "from (select a.EP_ID,a.EP_NAME,a.BELONGSEPA,a.sysdate,(case when a.IF_PRODUCE = 1 and a.IF_HANDLE=0 then '医疗机构' when a.IF_PRODUCE = 0 and a.IF_HANDLE=1 then '处置机构' else '' end) as EPTYPE,(case when a.ORGTYPE=0 and a.IF_PRODUCE = 1 then '是' when a.ORGTYPE=1 and a.IF_PRODUCE = 1 then '否' else '—' end) as CSEPTYPE,b.STATUS,(case b.STATUS when '01' then '待审批' when '02' then '待审批' when '05' then '待审批' else '审批通过' end) as STATUSNAME from WOBO_ENTERPRISE a, WOBO_APPLY_LIST b where a.EP_ID = b.EP_ID and b.BIZ_NAME='医疗机构信息完善' and b.STATUS in ('01', '02', '04', '05') and a.STATUS in ('0', '1') and a.BELONGSEPA = '"+area+"' and a.EP_ID != 'sysadmin') as A where A.STATUS in ('01', '02', '04', '05')";
			if(searchContent != null && !"".equals(searchContent)){
				sql = sql + " and (A.EP_NAME like '%"+searchContent+"%' or A.EPTYPE like '%"+searchContent+"%' or A.CSEPTYPE like '%"+searchContent+"%')";
			}
			if(statusValue != null && !"".equals(statusValue)){
				if("'04'".equals(statusValue)){
					sql = sql + " and A.STATUS in ("+statusValue+")";
				}else if("'01'".equals(statusValue)){
					sql = sql + " and A.STATUS in ('01', '02', '05')";
				}
			}
			sql = sql + " order by A.sysdate desc";
		}
		Page<Record> page = Db.paginate(pn, ps, "select A.*", sql);
		List<Record> eps = page.getList();
		List<Map<String, Object>> epList = new ArrayList<>();
		Map<String, Object> resMap = new HashMap<>();
		if(eps != null){
			for(Record record : eps){
				Map<String, Object> map = new HashMap<>();
				map.put("EP_ID", record.get("EP_ID"));
				map.put("EP_NAME", record.get("EP_NAME"));
				map.put("EPTYPE", record.get("EPTYPE"));
				map.put("CSEPTYPE", record.get("CSEPTYPE"));
				map.put("BELONG_SEPA", record.get("BELONGSEPA"));
				map.put("SEPA_NAME", convert(cityList, record.get("BELONGSEPA")) + "环保局");
				map.put("STATUS", record.get("STATUS"));
				map.put("STATUSNAME", record.get("STATUSNAME"));
				epList.add(map);
			}
		}
		resMap.put("epList", epList);
		if(statusCache != null && !"".equals(statusCache)){
			resMap.put("statusList", super.queryDict("approve_status", "value", "text", statusCache));
		}else{
			resMap.put("statusList", super.queryDict("approve_status", "value", "text"));
		}
		if("SJSPROLE".equals(ROLEID)){
			resMap.put("sepaList", super.queryDict("city_q", "value", "text"));
		}
		resMap.put("totalPage", page.getTotalPage());
		resMap.put("totalRow", page.getTotalRow());
		return resMap;
	}
	
	/**
	 * @author weizanting
	 * @date 20170620
	 * 方法：管理员查询是否有未提交的计划
	 */
	public int queryUnsubmitPlanNum(String epId){
		Record record = Db.findFirst("select count(1) unSubmitPlanNum from WOBO_CONDUITS_TRANSFER_UNSUBMIT where EP_ID_CS = ?", epId);
		if(record != null){
			return record.getInt("unSubmitPlanNum");
		}else{
			return 0;
		}
	}
	
	/**
	 * @author weizanting
	 * @date 20170620
	 * 方法：查看未提交转移计划单位列表放入列内
	 */
	public void getUnSubmitPlanExcelInfo(String[] sheetNames,List<?>[] sheetAllList,String[][] headers,String[][] columns, String area, String ROLEID, Object searchContent, Object sepaValue){
		String[] cols = new String[5];
		cols[0] = "产生单位名称";
		cols[1] = "处置单位名称";
		cols[2] = "中转单位名称";
		cols[3] = "产生单位联系方式";
		cols[4] = "所属环保局";
		for(int i = 0 ; i < 1 ; i++ )
		{
			List<Object> data = Lists.newArrayList();
			sheetNames[i] = "未提交转移计划单位列表";
			for(Record one : queryUnSubmitPlanListForSheet(sheetNames[i], area, ROLEID, searchContent, sepaValue))
			{
				data.add(one);
			}
			sheetAllList[i] = data;
			headers[i] = cols;
			columns[i] = cols;
		}
	}
	
	/**
	 * @author weizanting
	 * @date 20170620
	 * 方法：查看未提交转移计划单位列表
	 */
	public List<Record> queryUnSubmitPlanListForSheet(String dictType, String area, String ROLEID, Object searchContent, Object sepaValue){
		String sql = "";
		if("SJSPROLE".equals(ROLEID)){		//SJSPROLE-市级管理员
			sql = "from (select A.*, B.EP_NAME as EP_NAME_CZ from (select b.EP_ID as EP_ID_CS, b.EP_NAME as EP_NAME_CS, b.BELONGSEPA as BELONGSEPA, a.EP_ID_CZ as EP_ID_CZ,b.TEL,a.EP_ID_ZZ as EP_ID_ZZ,b.STATUS from WOBO_CONDUITS_TRANSFER_UNSUBMIT a, WOBO_ENTERPRISE b where a.EP_ID_CS = b.EP_ID) as A left join WOBO_ENTERPRISE B on A.EP_ID_CZ = B.EP_ID) as C left join WOBO_ENTERPRISE D on C.EP_ID_ZZ = D.EP_ID where C.STATUS = '1'";
			if(searchContent != null && !"".equals(searchContent)){
				sql = sql + " and (C.EP_NAME_CS like '%"+searchContent+"%' or C.EP_NAME_CZ like '%"+searchContent+"%' or C.TEL like '%" + searchContent +"%' or D.EP_NAME like '%"+searchContent+"%')";
			}
			if(sepaValue != null && !"".equals(sepaValue)){
				sql = sql + " and C.BELONGSEPA in ("+sepaValue+")";
			}
		}else{		//区级管理员
			sql = "from (select A.*, B.EP_NAME as EP_NAME_CZ from (select b.EP_ID as EP_ID_CS, b.EP_NAME as EP_NAME_CS, b.BELONGSEPA as BELONGSEPA, a.EP_ID_CZ as EP_ID_CZ,b.TEL,a.EP_ID_ZZ as EP_ID_ZZ,b.STATUS from WOBO_CONDUITS_TRANSFER_UNSUBMIT a, WOBO_ENTERPRISE b where a.EP_ID_CS = b.EP_ID and b.BELONGSEPA = '"+area+"') as A left join WOBO_ENTERPRISE B on A.EP_ID_CZ = B.EP_ID) as C left join WOBO_ENTERPRISE D on C.EP_ID_ZZ = D.EP_ID where C.STATUS = '1'";
			if(searchContent != null && !"".equals(searchContent)){
				sql = sql + " and (C.EP_NAME_CS like '%"+searchContent+"%' or C.EP_NAME_CZ like '%"+searchContent+"%' or C.TEL like '%" + searchContent +"%' or D.EP_NAME like '%"+searchContent+"%')";
			}
		}
		List<Record> list = new ArrayList<Record>();
		list = Db.find("select C.EP_NAME_CS as \"产生单位名称\", C.EP_NAME_CZ as \"处置单位名称\", D.EP_NAME as \"中转单位名称\", C.TEL as \"产生单位联系方式\", C.BELONGSEPA as \"所属环保局\"" + sql + " order by C.EP_NAME_CS asc");
		if(list.size() > 0){
			for(int i = 0; i < list.size(); i ++){
				list.get(i).set("所属环保局", convert(cityList, list.get(i).get("所属环保局")) + "环保局");
			}
		}
		return list;
	}
	
}
