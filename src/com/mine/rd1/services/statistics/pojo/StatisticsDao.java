package com.mine.rd1.services.statistics.pojo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.mine.pub.kit.DateKit;
import com.mine.pub.pojo.BaseDao;

public class StatisticsDao extends BaseDao {
	
	
	/**
	 * @author weizantig
	 * @date 20170519
	 * 方法：登录区市审批app
	 */
	public Map<String, Object> appLogin(String userId, String pwd){
		Map<String, Object> map = null;
		Record record = Db.findFirst("select ao.USERID,ao.OPERATORNAME USERNAME,oe.ORGID,oo.ORGNAME,oe.EMPNAME realName,oe.EMPID,ao.OPERATORID,oo.ORGCODE,oo.ORGSEQ	from OM_EMPLOYEE oe,AC_OPERATOR ao,OM_ORGANIZATION oo where ao.OPERATORID=oe.OPERATORID and oo.ORGID=oe.ORGID and  len(oe.ORGID) = 3 and ao.USERID=? and ao.PASSWORD=?", userId, pwd);
		if(record != null){
			map=new HashMap<String, Object>();
			map.put("userId", record.get("USERID"));
			map.put("userName", record.get("USERNAME"));
			map.put("orgId", record.get("ORGID"));
			map.put("orgName", record.get("ORGNAME"));
			map.put("realName", record.get("realName"));
			map.put("empId", record.get("EMPID"));
			map.put("operatorId", record.get("OPERATORID"));
			map.put("orgCode", record.get("ORGCODE"));
			map.put("orgSeq", record.get("ORGSEQ"));
			if(record.get("OPERATORID") != null){
				map.put("roleId", queryRole(record.get("OPERATORID").toString()));
			}else{
				map.put("roleId", "");
			}
		}
		return map;
	}
	
	/**
	 * @author weizantig
	 * @date 20170519
	 * 方法：登录区市审批app
	 */
	public String queryRole(String operatorId){
		List<Record> recordList = Db.find("select * from AC_OPERATORROLE t where t.OPERATORID=?", operatorId);
		String str = "";
		if(recordList.size() > 0){
			for(Record record : recordList){
				if(record != null){
					str = str + record.get("ROLEID") + ",";
				}
			}
		}
		str = str.substring(0, str.length() - 1);
		return str;
	}
	
	/**
	 * @author weizantig
	 * @date 20170522
	 * 方法：查询当前登录用户所在的机构id
	 * 返回：字符串str
	 */
	public String queryOrgId(String userId){
		String str = "";
		Record record = Db.findFirst("select * from OM_EMPLOYEE t where t.USERID=?", userId);
		if(record != null && record.get("ORGID") != null){
			str = record.get("ORGID").toString();
		}
		return str;
	}
	
	/**
	 * @author weizantig
	 * @date 20170519
	 * 方法：处置协议列表
	 */
	public Map<String, Object> queryAgreementList(String orgCode, String queryContext, String beginDate, String entDate, String status,int pn, int ps){
		Calendar a=Calendar.getInstance();
		int year=a.get(Calendar.YEAR);
		int month=a.get(Calendar.MONTH);
		String mStr=null;
		if(month==0)
		{
			mStr="12";
			year=year-1;
		}
		else
		{
			mStr=month < 10 ? "0"+month : month+"";
		}
		int day=a.get(Calendar.DAY_OF_MONTH);
		String dStr=day < 10 ? "0"+day : day+"";
		boolean isAdd=true;
		String sqlSelect = "select * ";
		String sqlFrom = " from (select distinct t.AM_ID amId,t.EN_NAME_CS enNameCs,t.EN_NAME_YS enNameYs,t.EN_NAME_CZ enNameCz,ede.DICTNAME statusName,CASE t.IF_ADDITIONAL WHEN '0' THEN '否' ELSE '是' END ifAdditionalName, CONVERT(varchar(100), t.BEGINTIME, 23) begintimeStr,isnull(CONVERT(varchar(100), t.ENDTIME, 23),'长期有效') endtimeStr,CONVERT(varchar(100), t.ACTIONDATE, 23) actiondateStr ,t.ACTIONDATE ";
		if("SHB".equals(orgCode)){
			sqlFrom = sqlFrom + "from AGREEMENT t,ENTERPRISE e,EOS_DICT_ENTRY ede where e.EP_ID=t.EN_ID_CS and ede.DICTID=t.STATUS and ede.DICTTYPEID='AM_STATUS' ";
		}
		else if("BHXQ".equals(orgCode)){
			sqlFrom =sqlFrom+ "from AGREEMENT t,ENTERPRISE e,EOS_DICT_ENTRY ede,B_ORGAN b where e.belong_Sepa = b.bto_id and e.EP_ID=t.EN_ID_CS and ede.DICTID=t.STATUS and ede.DICTTYPEID='AM_STATUS' and b.belong_bh = 1 ";
		}
		else{
			sqlFrom = sqlFrom + "from AGREEMENT t,ENTERPRISE e,EOS_DICT_ENTRY ede where e.EP_ID=t.EN_ID_CS and ede.DICTID=t.STATUS and ede.DICTTYPEID='AM_STATUS' and e.BELONG_SEPA='" + orgCode +"' ";
		}
		if(!"".equals(queryContext)){
			sqlFrom = sqlFrom + " and (t.AM_ID like '%" + queryContext +"%' or t.EN_NAME_CS like '%" + queryContext + "%' or t.EN_NAME_YS like '%" + queryContext + "%' or t.EN_NAME_CZ like '%" + queryContext + "%')";
			isAdd=false;
		}
		if(!"".equals(beginDate)){
			sqlFrom = sqlFrom + " and CONVERT(varchar(100), t.BEGINTIME, 23) >= '" + beginDate +"'";
			isAdd=false;
		}
		if(!"".equals(entDate)){
			sqlFrom = sqlFrom + " and (CONVERT(varchar(100), t.ENDTIME, 23) <= '" + entDate +"' or t.ENDTIME is null)";
			isAdd=false;
		}
		if(!"".equals(status)){
			sqlFrom = sqlFrom + " and t.STATUS = '" + status + "'";
			isAdd=false;
		}
		//未精确查询系统默认查询一个月的数据
		if(isAdd)
		{
			sqlFrom = sqlFrom + " and  t.sysdate >='"+year+"-"+mStr+"-"+dStr+"' ";
		}
		sqlFrom = sqlFrom + ") as A order by ACTIONDATE desc ";
		System.out.println("sql======>" + sqlSelect + sqlFrom);
		Page<Record> page = Db.paginate(pn, ps, sqlSelect, sqlFrom);
//		Page<Record> page = Db.paginateByCache("mydict", "key_agreement_cache", pn, ps, sqlSelect, sqlFrom);
		List<Record> recordList = page.getList();
		Map<String, Object> map = null;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> resMap = new HashMap<String, Object>();
		if(recordList.size() > 0){
			for(Record record : recordList){
				map = new HashMap<String, Object>();
				map.put("amId", record.get("amId"));
				map.put("enNameCs", record.get("enNameCs"));
				map.put("enNameYs", record.get("enNameYs"));
				map.put("enNameCz", record.get("enNameCz"));
				map.put("statusName", record.get("statusName"));
				map.put("ifAdditionalName", record.get("ifAdditionalName"));
				map.put("begintimeStr", record.get("begintimeStr"));
				map.put("endtimeStr", record.get("endtimeStr"));
				map.put("actiondateStr", record.get("actiondateStr"));
				list.add(map);
			}
		}
		resMap.put("list", list);
		resMap.put("totalPage", page.getTotalPage());
		resMap.put("totalRow", page.getTotalRow());
		return resMap;
	}
	
	/**
	 * @author weizantig
	 * @date 20170522
	 * 方法：根据协议id查询处置协议
	 */
	public Map<String, Object> getArgeementByAmId(String amId){
		Record record = Db.findFirst("select * from AGREEMENT a where a.AM_ID = ?", amId);
		Map<String, Object> resMap = new HashMap<String, Object>();
		Map<String, Object> entityData = new HashMap<String, Object>();
		Map<String, Object> map = null;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if(record != null){
			entityData.put("amId", record.get("AM_ID"));
			entityData.put("mainId", record.get("MAIN_ID"));
			entityData.put("enIdCs", record.get("EN_ID_CS"));
			entityData.put("enNameCs", record.get("EN_NAME_CS"));
			entityData.put("enIdYs", record.get("EN_ID_YS"));
			entityData.put("enNameYs", record.get("EN_NAME_YS"));
			entityData.put("enIdCz", record.get("EN_ID_CZ"));
			entityData.put("enNameCz", record.get("EN_NAME_CZ"));
			entityData.put("beginTime", DateKit.toStr(record.getDate("BEGINTIME"), "yyyy-MM-dd"));
			entityData.put("endTime", record.getDate("ENDTIME") == null ? "长期有效" : DateKit.toStr(record.getDate("ENDTIME"), "yyyy-MM-dd"));
			entityData.put("ifAdditional", record.get("IF_ADDITIONAL"));
			entityData.put("status", record.get("STATUS"));
			List<Record> statusList = getDict("AM_STATUS"); 
			if(record.get("STATUS") != null){
				for(Record status: statusList){
					if(record.get("STATUS").equals(status.get("DICTID"))){
						entityData.put("statusName", status.get("DICTNAME"));
						break;
					}
				}
			}else{
				entityData.put("statusName", "");
			}
			entityData.put("processinstId", record.get("PROCESSINSTID"));
			entityData.put("reason", record.get("REASON"));
			entityData.put("actionDate", DateKit.toStr(record.getDate("ACTIONDATE"), "yyyy-MM-dd"));
			entityData.put("linkMan", record.get("LINKMAN"));
			entityData.put("linkTel", record.get("LINKTEL"));
			entityData.put("linkPhone", record.get("LINKPHONE"));
			entityData.put("sysdate", DateKit.toStr(record.getDate("sysdate"), "yyyy-MM-dd HH:mm:ss"));
			List<Record> recordList = Db.find("select b.* from AGREEMENT a,AGREEMENT_LIST b where a.AM_ID=b.AM_ID and a.AM_ID = ?", amId);
			if(recordList.size() > 0){
				for(Record al : recordList){
					map = new HashMap<String, Object>();
					map.put("amId", al.get("AM_ID"));
					map.put("alId", al.get("AL_ID"));
					map.put("unit", al.get("UNIT"));
					map.put("unitNum", al.get("UNIT_NUM"));
					map.put("container", al.get("CONTAINER"));
					map.put("containerNum", al.get("CONTAINER_NUM"));
					map.put("capacity", al.get("CAPACITY"));
					map.put("bigCategoryId", al.get("BIG_CATEGORY_ID"));
					map.put("bigCategoryName", al.get("BIG_CATEGORY_NAME"));
					map.put("bigCategoryComment", al.get("BIG_CATEGORY_COMMENT"));
					map.put("smallCategoryId", al.get("SAMLL_CATEGORY_ID"));
					map.put("smallCategoryName", al.get("SAMLL_CATEGORY_NAME"));
					map.put("smallCategoryComment", al.get("SAMLL_CATEGORY_COMMENT"));
					map.put("dName", al.get("D_NAME"));
					map.put("shape", al.get("SHAPE"));
					map.put("dFrom", al.get("D_FROM"));
					map.put("meterial", al.get("METERIAL"));
					map.put("sysdate", DateKit.toStr(al.getDate("sysdate"), "yyyy-MM-dd HH:mm:ss"));
					list.add(map);
				}
			}
		}
		entityData.put("subList", list);
		resMap.put("entityData", entityData);
		return resMap;
	}
	
	/**
	 * @author weizantig
	 * @return 
	 * @date 20170522
	 * 方法：查询转移计划列表
	 */
	public Map<String, Object> queryTransferPlanList(String orgCode, String queryContext, String beginDate, String entDate, String status, int pn, int ps){
		Calendar a=Calendar.getInstance();
		int year=a.get(Calendar.YEAR);
		int month=a.get(Calendar.MONTH);
		String mStr=null;
		if(month==0)
		{
			mStr="12";
			year=year-1;
		}
		else
		{
			mStr=month < 10 ? "0"+month : month+"";
		}
		int day=a.get(Calendar.DAY_OF_MONTH);
		String dStr=day < 10 ? "0"+day : day+"";
		boolean isAdd=true;
		String sqlSelect = "select * ";
		String sqlFrom = " from (select distinct t.TP_ID tpId,t.EN_NAME_CS enNameCs,t.EN_NAME_YS enNameYs,t.EN_NAME_CZ enNameCz,ede.DICTNAME statusName,CASE t.IF_ADDITIONAL WHEN '0' THEN '否' ELSE '是' END ifAdditionalName, CONVERT(varchar(100), t.BEGINTIME, 23) begintimeStr,isnull(CONVERT(varchar(100), t.ENDTIME, 23),'长期有效') endtimeStr,CONVERT(varchar(100), t.ACTIONDATE, 23) actiondateStr ,t.ACTIONDATE ";
		if("SHB".equals(orgCode)){
			sqlFrom =sqlFrom+ "from TRANSFER_PLAN t,ENTERPRISE e,EOS_DICT_ENTRY ede where e.EP_ID=t.EN_ID_CS and ede.DICTID=t.STATUS and ede.DICTTYPEID = 'TP_STATUS' ";
		}
		else if("BHXQ".equals(orgCode)){
			sqlFrom =sqlFrom+ "from TRANSFER_PLAN t,ENTERPRISE e,EOS_DICT_ENTRY ede,B_ORGAN b where e.belong_Sepa = b.bto_id and e.EP_ID=t.EN_ID_CS and ede.DICTID=t.STATUS and ede.DICTTYPEID = 'TP_STATUS' and b.belong_bh = 1 ";
		}
		else{
			sqlFrom =sqlFrom+ "from TRANSFER_PLAN t,ENTERPRISE e,EOS_DICT_ENTRY ede where e.EP_ID=t.EN_ID_CS and e.BELONG_SEPA='" + orgCode + "' and ede.DICTID=t.STATUS and ede.DICTTYPEID = 'TP_STATUS' ";
		}
		if(!"".equals(queryContext)){
			isAdd=false;
			sqlFrom = sqlFrom + " and (t.TP_ID like '%"+queryContext+"%' or t.EN_NAME_CS like '%" + queryContext + "%' or t.EN_NAME_YS like '%" + queryContext + "%' or t.EN_NAME_CZ like '%" + queryContext + "%')";
		}
		if(!"".equals(beginDate)){
			isAdd=false;
			sqlFrom = sqlFrom + " and CONVERT(varchar(100), t.BEGINTIME, 23) >= '" + beginDate +"'";
		}
		if(!"".equals(entDate)){
			isAdd=false;
			sqlFrom = sqlFrom + "  and (CONVERT(varchar(100), t.ENDTIME, 23) <= '" + entDate +"' or t.ENDTIME is null)";
		}
		if(!"".equals(status)){
			isAdd=false;
			sqlFrom = sqlFrom + " and t.STATUS = '" + status + "'";
		}
		//未精确查询系统默认查询一个月的数据
		if(isAdd)
		{
			sqlFrom = sqlFrom + " and  t.sysdate >='"+year+"-"+mStr+"-"+dStr+"' ";
		}
		sqlFrom=sqlFrom+") as A order by ACTIONDATE desc ";
		Page<Record> page = Db.paginate(pn, ps, sqlSelect, sqlFrom);
		List<Record> recordList = page.getList();
		Map<String, Object> resMap = new HashMap<String, Object>();
		Map<String, Object> map = null;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>> ();
		if(recordList.size() > 0){
			for(Record record : recordList){
				map = new HashMap<String, Object>();
				map.put("tpId", record.get("tpId"));
				map.put("enNameCs", record.get("enNameCs"));
				map.put("enNameYs", record.get("enNameYs"));
				map.put("enNameCz", record.get("enNameCz"));
				map.put("statusName", record.get("statusName"));
				map.put("ifAdditionalName", record.get("ifAdditionalName"));
				map.put("begintimeStr", record.get("begintimeStr"));
				map.put("endtimeStr", record.get("endtimeStr"));
				map.put("actiondateStr", record.get("actiondateStr"));
				list.add(map);
			}
		}
		resMap.put("list", list);
		resMap.put("totalPage", page.getTotalPage());
		resMap.put("totalRow", page.getTotalRow());
		return resMap;
	}
	
	/**
	 * @author weizantig
	 * @return 
	 * @date 20170522
	 * 方法：根据转移计划id查询转移计划
	 */
	public Map<String, Object> getTransferPlanById(String tpId, String processinstId){
		Record record = new Record();
		//根据转移计划id或流程编号查询主表信息
		if(!"".equals(tpId)){
			record = Db.findFirst("select * from TRANSFER_PLAN where TP_ID = ?", tpId);
		}
		if(!"".equals(processinstId)){
			record = Db.findFirst("select * from TRANSFER_PLAN where PROCESSINSTID = ?", processinstId);
		}
		Map<String, Object> resMap = new HashMap<String, Object>();
		Map<String, Object> entityData = new HashMap<String, Object>();
		Map<String, Object> map = null;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if(record != null){
			entityData.put("tpId", record.get("TP_ID"));
			entityData.put("tpMainId", record.get("TP_MAIN_ID"));
			entityData.put("amId", record.get("AM_ID"));
			entityData.put("mainId", record.get("MAIN_ID"));
			entityData.put("enIdCs", record.get("EN_ID_CS"));
			entityData.put("enNameCs", record.get("EN_NAME_CS"));
			entityData.put("enIdYs", record.get("EN_ID_YS"));
			entityData.put("enNameYs", record.get("EN_NAME_YS"));
			entityData.put("enIdCz", record.get("EN_ID_CZ"));
			entityData.put("enNameCz", record.get("EN_NAME_CZ"));
			entityData.put("beginTime", DateKit.toStr(record.getDate("BEGINTIME"), "yyyy-MM-dd"));
			entityData.put("endTime", record.getDate("ENDTIME") == null ? "长期有效" : DateKit.toStr(record.getDate("ENDTIME"), "yyyy-MM-dd"));
			entityData.put("ifAdditional", record.get("IF_ADDITIONAL"));
			entityData.put("ifTpAdditional", record.get("IF_TP_ADDITIONAL"));
			entityData.put("status", record.get("STATUS"));
			List<Record> statusList = getDict("TP_STATUS"); 
			if(record.get("STATUS") != null){
				for(Record status: statusList){
					if(record.get("STATUS").equals(status.get("DICTID"))){
						entityData.put("statusName", status.get("DICTNAME"));
						break;
					}
				}
			}else{
				entityData.put("statusName", "");
			}
			entityData.put("processinstId", record.get("PROCESSINSTID"));
			entityData.put("actionDate", DateKit.toStr(record.getDate("ACTIONDATE"), "yyyy-MM-dd HH:mm:ss"));
			entityData.put("linkMan", record.get("LINKMAN"));
			entityData.put("linkTel", record.get("LINKTEL"));
			entityData.put("linkPhone", record.get("LINKPHONE"));
			entityData.put("sysdate", DateKit.toStr(record.getDate("sysdate"), "yyyy-MM-dd HH:mm:ss"));
			//子表信息查询
			List<Record> recordList = Db.find("select * from TRANSFER_PLAN_LIST where TP_ID = ?", record.get("TP_ID"));
			if(recordList.size() > 0){
				for(Record al : recordList){
					map = new HashMap<String, Object>();
					map.put("tld", al.get("TL_ID"));
					map.put("tpId", al.get("TP_ID"));
					map.put("amId", al.get("AM_ID"));
					map.put("alId", al.get("AL_ID"));
					map.put("unit", al.get("UNIT"));
					map.put("unitNum", al.get("UNIT_NUM"));
					map.put("container", al.get("CONTAINER"));
					map.put("containerNum", al.get("CONTAINER_NUM"));
					map.put("capacity", al.get("CAPACITY"));
					map.put("bigCategoryId", al.get("BIG_CATEGORY_ID"));
					map.put("bigCategoryName", al.get("BIG_CATEGORY_NAME"));
					map.put("bigCategoryComment", al.get("BIG_CATEGORY_COMMENT"));
					map.put("smallCategoryId", al.get("SAMLL_CATEGORY_ID"));
					map.put("smallCategoryName", al.get("SAMLL_CATEGORY_NAME"));
					map.put("smallCategoryComment", al.get("SAMLL_CATEGORY_COMMENT"));
					map.put("dName", al.get("D_NAME"));
					map.put("shape", al.get("SHAPE"));
					map.put("dFrom", al.get("D_FROM"));
					map.put("meterial", al.get("METERIAL"));
					map.put("unitNumYc", al.get("UNIT_NUM_YC"));
					map.put("unitNumWc", al.get("UNIT_NUM_WC"));
					map.put("containerNumYc", al.get("CONTAINER_NUM_YC"));
					map.put("containerNumWc", al.get("CONTAINER_NUM_WC"));
					map.put("sysdate", DateKit.toStr(al.getDate("sysdate"), "yyyy-MM-dd HH:mm:ss"));
					list.add(map);
					//判断处置单位是否存在相对应的处置范围
					String hStatus = "1";
					if(!"0".equals(hStatus)){
						if(!"".equals(processinstId)){
							List<Record> dataList = Db.find("select * from HANDLE_CATEGORY t where t.BIG_CATEGORY_ID=? and t.EP_ID=?", al.get("BIG_CATEGORY_ID"), record.get("EN_ID_CZ"));
							if(dataList.size() <= 0){
								hStatus = "0";
							}else{
								hStatus = "1";
							}
						}
					}
					entityData.put("hStatus", hStatus);
				}
			}
		}
		entityData.put("subList", list);
		resMap.put("entityData", entityData);
		return resMap;
	}
	
	/**
	 * @author weizantig
	 * @return 
	 * @date 20170522
	 * 方法：查询转移联单列表
	 */
	public Map<String, Object> queryTransferplanBillList(String orgCode, String queryContext, String beginDate, String entDate, String status, int pn, int ps){
		Calendar a=Calendar.getInstance();
		int year=a.get(Calendar.YEAR);
		int month=a.get(Calendar.MONTH);
		String mStr=null;
		if(month==0)
		{
			mStr="12";
			year=year-1;
		}
		else
		{
			mStr=month < 10 ? "0"+month : month+"";
		}
		int day=a.get(Calendar.DAY_OF_MONTH);
		String dStr=day < 10 ? "0"+day : day+"";
		boolean isAdd=true;
		String sqlSelect = "select * ";
		String sqlFrom = " from (select distinct t.TB_ID tbId,t.EN_NAME_CS enNameCs,t.EN_NAME_YS enNameYs,t.EN_NAME_CZ enNameCz,ede.DICTNAME statusName,CASE t.IFCOLLECT WHEN '0' THEN '否' ELSE '是' END ifCollectName, CONVERT(varchar(100), t.BEGINTIME, 23) begintimeStr,isnull(CONVERT(varchar(100), t.ENDTIME, 23),'长期有效') endtimeStr,CONVERT(varchar(100), t.ACTIONDATE, 23) actiondateStr ,t.ACTIONDATE ,b.BTO_NAME btoName ";
		if("SHB".equals(orgCode)){
			sqlFrom =sqlFrom+ "from TRANSFERPLAN_BILL t,ENTERPRISE e,EOS_DICT_ENTRY ede,B_ORGAN b where e.belong_Sepa = b.bto_id and e.EP_ID=t.EN_ID_CS and ede.DICTID=t.STATUS and ede.DICTTYPEID='TB_STATUS' ";
		}
		else if("BHXQ".equals(orgCode)){
			sqlFrom =sqlFrom+ "from TRANSFERPLAN_BILL t,ENTERPRISE e,EOS_DICT_ENTRY ede,B_ORGAN b where e.belong_Sepa = b.bto_id and e.EP_ID=t.EN_ID_CS and ede.DICTID=t.STATUS and ede.DICTTYPEID='TB_STATUS' and b.belong_bh = 1 ";
		}
		else{
			sqlFrom =sqlFrom+ "from TRANSFERPLAN_BILL t,ENTERPRISE e,EOS_DICT_ENTRY ede,B_ORGAN b where e.belong_Sepa = b.bto_id and e.EP_ID=t.EN_ID_CS and ede.DICTID=t.STATUS and e.BELONG_SEPA='" + orgCode + "' and ede.DICTTYPEID='TB_STATUS' ";
		}
		if(!"".equals(queryContext)){
			isAdd=false;
			sqlFrom = sqlFrom + " and (t.TB_ID like '%"+queryContext+"%' or t.EN_NAME_CS like '%" + queryContext + "%' or t.EN_NAME_YS like '%" + queryContext + "%' or t.EN_NAME_CZ like '%" + queryContext + "%')";
		}
		if(!"".equals(beginDate)){
			isAdd=false;
			sqlFrom = sqlFrom + " and CONVERT(varchar(100), t.BEGINTIME, 23) >='" + beginDate +"'";
		}
		if(!"".equals(entDate)){
			isAdd=false;
			sqlFrom = sqlFrom + "  and (CONVERT(varchar(100), t.ENDTIME, 23) <= '" + entDate +"' or t.ENDTIME is null)";
		}
		if(!"".equals(status)){
			isAdd=false;
			sqlFrom = sqlFrom + " and t.STATUS='" + status + "'";
		}
		//未精确查询系统默认查询一个月的数据
		if(isAdd)
		{
			sqlFrom = sqlFrom + " and  t.sysdate >='"+year+"-"+mStr+"-"+dStr+"' ";
		}
		sqlFrom=sqlFrom+") as A order by ACTIONDATE desc ";
		Page<Record> page = Db.paginate(pn, ps, sqlSelect, sqlFrom);
		List<Record> recordList = page.getList();
		Map<String, Object> resMap = new HashMap<String, Object>();
		Map<String, Object> map = null;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if(recordList.size() > 0){
			for(Record record : recordList){
				map = new HashMap<String, Object>();
				map.put("tbId", record.get("tbId"));
				map.put("enNameCs", record.get("enNameCs"));
				map.put("enNameYs", record.get("enNameYs"));
				map.put("enNameCz", record.get("enNameCz"));
				map.put("statusName", record.get("statusName"));
				map.put("ifCollectName", record.get("ifCollectName"));
				map.put("begintimeStr", record.get("begintimeStr"));
				map.put("endtimeStr", record.get("endtimeStr"));
				map.put("actiondateStr", record.get("actiondateStr"));
				map.put("btoName", record.get("btoName"));
				list.add(map);
			}
		}
		resMap.put("list", list);
		resMap.put("totalPage", page.getTotalPage());
		resMap.put("totalRow", page.getTotalRow());
		return resMap;
	}
	
	/**
	 * @author weizantig
	 * @return 
	 * @date 20170522
	 * 方法：根据转移联单id查询转移联单
	 */
	public Map<String, Object> getTransferplanBillByTbId(String tbId){
		Record record = Db.findFirst("select * from TRANSFERPLAN_BILL t LEFT   JOIN CAR_INFO ci on ci.CARDNO=t.CARCARD where tb_id= ?", tbId);
		Map<String, Object> resMap = new HashMap<String, Object>();
		Map<String, Object> entityData = new HashMap<String, Object>();
		Map<String, Object> map = null;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if(record != null){
			entityData.put("tbId", record.get("TB_ID"));
			entityData.put("tpId", record.get("TP_ID"));
			entityData.put("tpMainId", record.get("TP_MAIN_ID"));
			entityData.put("enIdCs", record.get("EN_ID_CS"));
			entityData.put("enNameCs", record.get("EN_NAME_CS"));
			entityData.put("enIdYs", record.get("EN_ID_YS"));
			entityData.put("enNameYs", record.get("EN_NAME_YS"));
			entityData.put("enIdCz", record.get("EN_ID_CZ"));
			entityData.put("enNameCz", record.get("EN_NAME_CZ"));
			entityData.put("beginTime", DateKit.toStr(record.getDate("BEGINTIME"), "yyyy-MM-dd"));
			entityData.put("endTime", record.getDate("ENDTIME") == null ? "长期有效" : DateKit.toStr(record.getDate("ENDTIME"), "yyyy-MM-dd"));
			entityData.put("status", record.get("STATUS"));
			List<Record> statusList = getDict("TB_STATUS"); 
			if(record.get("STATUS") != null){
				for(Record status: statusList){
					if(record.get("STATUS").equals(status.get("DICTID"))){
						entityData.put("statusName", status.get("DICTNAME"));
						break;
					}
				}
			}else{
				entityData.put("statusName", "");
			}
			entityData.put("actionDate", DateKit.toStr(record.getDate("ACTIONDATE"), "yyyy-MM-dd HH:mm:ss"));
			entityData.put("tbDate", DateKit.toStr(record.getDate("TBDATE"), "yyyy-MM-dd HH:mm:ss"));
			entityData.put("ysDate", DateKit.toStr(record.getDate("YSDATE"), "yyyy-MM-dd HH:mm:ss"));
			entityData.put("jsDate", DateKit.toStr(record.getDate("JSDATE"), "yyyy-MM-dd HH:mm:ss"));
			entityData.put("cardno", record.get("cardno"));
			entityData.put("ifCollect", record.get("IFCOLLECT"));
			entityData.put("csy", record.get("CSY"));
			entityData.put("csCard", record.get("CSCARD"));
			entityData.put("ysy", record.get("YSY"));
			entityData.put("ysCard", record.get("YSCARD"));
			entityData.put("czy", record.get("CZY"));
			entityData.put("czCard", record.get("CZCARD"));
			entityData.put("reason", record.get("REASON"));
			entityData.put("reasonDate", record.get("REASONDATE"));
			entityData.put("createPerson", record.get("CREATEPERSON"));
			entityData.put("carCard", record.get("CARCARD"));
			entityData.put("plateNumber", record.get("PLATE_NUMBER"));
			if(record.get("PLATE_NUMBER") != null)
			{
				Record rd = Db.findFirst("select name from CarTypeCode  where code= ?", record.get("CARTYPE")+"");
				if(rd != null)
				{
					entityData.put("carType", rd.get("name"));
				}
			}
			List<Record> recordList = Db.find("select * from TRANSFERPLAN_BILL_LIST where TB_ID = ?", tbId);
			if(recordList.size() > 0){
				for(Record al : recordList){
					map = new HashMap<String, Object>();
					map.put("blld", al.get("BL_ID"));
					map.put("tbId", al.get("TB_ID"));
					map.put("tpId", al.get("TP_ID"));
					map.put("tlId", al.get("TL_ID"));
					map.put("unit", al.get("UNIT"));
					map.put("unitNum", al.get("UNIT_NUM"));
					map.put("container", al.get("CONTAINER"));
					map.put("containerNum", al.get("CONTAINER_NUM"));
					map.put("capacity", al.get("CAPACITY"));
					map.put("bigCategoryId", al.get("BIG_CATEGORY_ID"));
					map.put("bigCategoryName", al.get("BIG_CATEGORY_NAME"));
					map.put("smallCategoryId", al.get("SAMLL_CATEGORY_ID"));
					map.put("dName", al.get("D_NAME"));
					map.put("shape", al.get("SHAPE"));
					map.put("dFrom", al.get("D_FROM"));
					map.put("meterial", al.get("METERIAL"));
					map.put("fwType", al.get("FWTYPE"));
					map.put("sysdate", DateKit.toStr(al.getDate("sysdate"), "yyyy-MM-dd HH:mm:ss"));
					list.add(map);
				}
			}
		}
		entityData.put("subList", list);
		resMap.put("entityData", entityData);
		return resMap;
	}
	
	/**
	 * @author weizantig
	 * @return 
	 * @date 20170522
	 * 方法：查询审批列表
	 */
	public Map<String, Object> queryTransferPlanCheckList(String orgCode, String checkLevel, String checkLevelName, int pn, int ps){
		String sqlSelect = "select al.EP_NAME epName,al.BIZ_NAME bizName,wi.workItemName,al.BELONG_SEPA belongSepa,wi.workItemID,bo.BTO_NAME btoName,am.AM_ID amId,CONVERT(varchar(100), al.APPLY_DATE, 23) applyDate ,al.PROCESSINSTID,am.TP_ID,wi.createTime";
		String sqlFrom = "";
		if("SHB".equals(orgCode)){
			sqlFrom = "from APPLY_LIST al,WFWorkItem wi,B_ORGAN bo,TRANSFER_PLAN am where al.PROCESSINSTID=wi.PROCESSINSTID and bo.BTO_ID=al.BELONG_SEPA and am.PROCESSINSTID=al.PROCESSINSTID and al.BIZ_NAME='危废转移计划' and wi.currentState NOT IN (-1,7,8,12,13) ";
		}else{
			sqlFrom = "from APPLY_LIST al,WFWorkItem wi,B_ORGAN bo,TRANSFER_PLAN am where al.PROCESSINSTID=wi.PROCESSINSTID and bo.BTO_ID=al.BELONG_SEPA and am.PROCESSINSTID=al.PROCESSINSTID and al.BIZ_NAME='危废转移计划' and wi.currentState NOT IN (-1,7,8,12,13) and BELONG_SEPA='"+orgCode+"'";
		}
		/*if(!"".equals(checkLevel)){
			sqlFrom = sqlFrom + " and al.STATUS='"+checkLevel+"'";
		}*/
		if(!"".equals(checkLevelName)){
			sqlFrom = sqlFrom + " and wi.workItemName='" + checkLevelName +"'";
		}
		sqlFrom=sqlFrom+" order by createTime desc";
		Page<Record> page = Db.paginate(pn, ps, sqlSelect, sqlFrom);
		List<Record> recordList = page.getList();
		Map<String, Object> resMap = new HashMap<String, Object>();
		Map<String, Object> map = null;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if(recordList.size() > 0){
			for(Record record : recordList){
				map = new HashMap<String, Object>();
				map.put("epName", record.get("epName"));
				map.put("bizName", record.get("bizName"));
				map.put("workItemName", record.get("workItemName"));
				map.put("belongSepa", record.get("belongSepa"));
				map.put("workItemID", record.get("workItemID"));
				map.put("btoName", record.get("btoName"));
				map.put("amId", record.get("amId"));
				map.put("applyDate", record.get("applyDate"));
				map.put("processinstId", record.get("PROCESSINSTID"));
				map.put("tpId", record.get("TP_ID"));
				list.add(map);
			}
		}
		resMap.put("list", list);
		resMap.put("totalPage", page.getTotalPage());
		resMap.put("totalRow", page.getTotalRow());
		return resMap;
	}
	
	/**
	 * @author weizantig
	 * @return 
	 * @date 20170522
	 * 方法：查询审批历史列表
	 */
	public Map<String, Object> appCallAuditCx(String bizId, int pn, int ps){
		Page<Record> page = Db.paginate(pn, ps, "select t.*,e.DICTNAME", "from APPROVE_LIST t,EOS_DICT_ENTRY e where e.DICTID=t.CHECK_RESULT and e.DICTTYPEID='isAgree' and t.BIZ_ID=? order by sysdate desc", bizId);
		List<Record> recordList = page.getList();
		Map<String, Object> resMap = new HashMap<String, Object>();
		Map<String, Object> map = null;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if(recordList.size() > 0){
			for(Record record : recordList){
				map = new HashMap<String, Object>();
				map.put("alId", record.get("AL_ID"));
				map.put("epName", record.get("EP_NAME"));
				map.put("bizId", record.get("BIZ_ID"));
				map.put("bizType", record.get("BIZ_TYPE"));
				map.put("bizStep", record.get("BIZ_STEP"));
				map.put("bizVersion", record.get("BIZ_VERSION"));
				map.put("checkResult", record.get("CHECK_RESULT"));
				map.put("checkAdvice", record.get("CHECK_ADVICE"));
				map.put("checkDate", DateKit.toStr(record.getDate("CHECK_DATE"), "yyyy-MM-dd"));
				map.put("btoId", record.get("BTO_ID"));
				map.put("btoName", record.get("BTO_NAME"));
				map.put("btofId", record.get("BTOF_ID"));
				map.put("btofName", record.get("BTOF_NAME"));
				map.put("processinstId", record.get("PROCESSINSTID"));
				map.put("activityinstId", record.get("ACTIVITYINSTID"));
				map.put("activitydefId", record.get("ACTIVITYDEFID"));
				map.put("activityinstName", record.get("ACTIVITYINSTNAME"));
				map.put("workItemId", record.get("WORKITEMID"));
				map.put("checkResultName", record.get("DICTNAME"));
				map.put("sysdate", DateKit.toStr(record.getDate("sysdate"), "yyyy-MM-dd HH:mm:ss"));
				list.add(map);
			}
		}
		resMap.put("list", list);
		resMap.put("totalPage", page.getTotalPage());
		return resMap;
	}
	
	/**
	 * @author weizantig
	 * @return 
	 * @date 20170522
	 * 方法：查询字典表
	 */
	public List<Record> getDict(String dictTypeId){
		return Db.find("select * from EOS_DICT_ENTRY t where t.DICTTYPEID=?", dictTypeId);
	}
	
	/**
	 * @author weizantig
	 * @return 
	 * @date 20170522
	 * 方法：查询字典表
	 */
	public List<Map<String, Object>> getDicts(String dictTypeId){
		List<Record> recordList = getDict(dictTypeId);
		Map<String, Object> map = null;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if(recordList.size() > 0){
			for(Record record : recordList){
				map = new HashMap<String, Object>();
				map.put("dictTypeId", record.get("DICTTYPEID"));
				map.put("dictId", record.get("DICTID"));
				map.put("dictName", record.get("DICTNAME"));
				map.put("status", record.get("STATUS"));
				map.put("sortNo", record.get("SORTNO"));
				map.put("rank", record.get("RANK"));
				map.put("parentId", record.get("PARENTID"));
				map.put("seqNo", record.get("SEQNO"));
				map.put("filter1", record.get("FILTER1"));
				map.put("filter2", record.get("FILTER2"));
				list.add(map);
			}
		}
		return list;
	}
	
	/**
	 * @author weizantig
	 * @return 
	 * @date 20170522
	 * 方法：查询审批需要的人员信息
	 */
	public List<Map<String, Object>> queryPresonInfo(String roleId,String btoId){
		String sql="SELECT A.USERID AS 'userId', A.OPERATORNAME AS 'userName', T.ROLEID AS 'roleId',"+
				   " M.ORGCODE AS 'btoId' FROM AC_OPERATORROLE T, OM_EMPORG E, AC_OPERATOR A,OM_EMPLOYEE O,OM_ORGANIZATION M"+
				   " WHERE O.EMPID = E.EMPID AND T.OPERATORID = A.OPERATORID AND T.OPERATORID = O.OPERATORID"+
				   " AND M.ORGID = E.ORGID AND T.ROLEID = ? AND M.ORGCODE = ?";
	    List<Record> recordList =Db.find(sql, roleId,btoId);
		Map<String, Object> map = null;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if(recordList.size() > 0){
			for(Record record : recordList){
				map = new HashMap<String, Object>();
				map.put("userId", record.get("userId"));
				map.put("userName", record.get("userName"));
				map.put("roleId", record.get("roleId"));
				map.put("btoId", record.get("btoId"));
				list.add(map);
			}
		}
		return list;
	}
}
