package com.mine.rd.services.plan.pojo;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.ehcache.CacheKit;
import com.mine.pub.kit.DateKit;
import com.mine.pub.pojo.BaseDao;
import com.mine.rd.services.agreement.pojo.AgreementDao;

public class PlanDao extends BaseDao {
	
	//查询状态列表
	List<Record> statusList = CacheKit.get("mydict", "plan_status");
	//查询审批状态列表
	List<Record> approveStatusList = CacheKit.get("mydict", "approve_status");
	//查询区列表
	List<Record> cityList = CacheKit.get("mydict", "city_q");
	
	/**
	 * @author weizanting
	 * @date 20170306
	 * 方法：查询医疗废物转移计划列表
	 */
	public Map<String, Object> queryPlanList(String EP_ID, int pn, int ps, Object searchContent, Object statusValue){
		//1-生产  2-处置
		String str = this.queryEPType(EP_ID);
		List<Record> recordList = new ArrayList<Record>();
		Map<String, Object> map = null;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Page<Record> page = null;
		Map<String, Object> resMap = new HashMap<String, Object>();
		String sql = "";
		if(searchContent != null && !"".equals(searchContent)){
			sql = " where (TP_ID like '%"+searchContent+"%' or AM_ID like '%"+searchContent+"%' or EN_NAME_CS like'%"+searchContent+"%' or EN_NAME_ZZ like'%"+searchContent+"%' or EN_NAME_CZ like '%"+searchContent+"%' or BEGINTIME like '%" + searchContent + "%' or ENDTIME like '%"+searchContent+"%')";
		}
		if(statusValue != null && !"".equals(statusValue)){
			if(searchContent != null && !"".equals(searchContent)){
				sql = sql + " and STATUS in (" + statusValue +")";
			}else{
				sql = " where STATUS in (" + statusValue +")";
			}
		}
		if("1".equals(str)){
			page = Db.paginate(pn, ps, "select * ", "from (select TP_ID,AM_ID,EN_ID_CS,EN_NAME_CS,EN_ID_ZZ,EN_NAME_ZZ,EN_ID_CZ,EN_NAME_CZ,CONVERT(varchar(100), BEGINTIME, 23) as BEGINTIME,CONVERT(varchar(100), ENDTIME, 23) as ENDTIME,STATUS,ACTIONDATE from WOBO_TRANSFER_PLAN where STATUS in ('0', '2', '3') and EN_ID_CS = ?) as A" + sql + " order by A.ACTIONDATE desc", EP_ID);
			recordList = page.getList();
		}
		if("2".equals(str)){
			page = Db.paginate(pn, ps, "select * ", "from (select TP_ID,AM_ID,EN_ID_CS,EN_NAME_CS,EN_ID_ZZ,EN_NAME_ZZ,EN_ID_CZ,EN_NAME_CZ,CONVERT(varchar(100), BEGINTIME, 23) as BEGINTIME,CONVERT(varchar(100), ENDTIME, 23) as ENDTIME,STATUS,ACTIONDATE from WOBO_TRANSFER_PLAN where STATUS in ('0', '2', '3') and EN_ID_CZ = ?) as A" + sql + " order by A.ACTIONDATE desc", EP_ID);
			recordList = page.getList();
		}
		if(recordList.size() > 0){
			for(Record record:recordList){
				map = new HashMap<String, Object>();
				map.put("TP_ID", record.get("TP_ID"));
				map.put("AM_ID", record.get("AM_ID"));
				map.put("EN_ID_CS", record.get("EN_ID_CS"));
				map.put("EN_NAME_CS", record.get("EN_NAME_CS"));
				map.put("EN_ID_ZZ", record.get("EN_ID_ZZ"));
				map.put("EN_NAME_ZZ", record.get("EN_NAME_ZZ"));
				map.put("EN_ID_CZ", record.get("EN_ID_CZ"));
				map.put("EN_NAME_CZ", record.get("EN_NAME_CZ"));
				map.put("BEGINTIME", record.get("BEGINTIME"));
				map.put("ENDTIME", record.get("ENDTIME"));
				map.put("STATUS", record.get("STATUS"));
				if(statusList != null){
					for(int i = 0; i < statusList.size(); i ++){
						if(record.get("STATUS").equals(statusList.get(i).get("dict_id"))){
							map.put("STATUSNAME", statusList.get(i).get("dict_value"));
						}
					}
				}
				list.add(map);
			}
		}
		resMap.put("dataList", list);
		//查询计划状态列表
		resMap.put("statusList", queryDict("plan_status", "value", "text"));
		resMap.put("totalPage", page.getTotalPage());
		resMap.put("totalRow", page.getTotalRow());
		return resMap;
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170703
	 * 方法：查询医疗废物转移计划列表by applyList status
	 */
	public Map<String, Object> queryPlanListByApply(String EP_ID, int pn, int ps, Object searchContent, Object statusValue){
		List<Record> recordList = new ArrayList<Record>();
		Map<String, Object> map = null;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> resMap = new HashMap<String, Object>();
		String sql = "from (select a.TP_ID,a.AM_ID,a.EN_ID_CS,a.EN_NAME_CS,a.EN_ID_ZZ,a.EN_NAME_ZZ,a.EN_ID_CZ,a.EN_NAME_CZ,CONVERT(varchar(100), a.BEGINTIME, 23) as BEGINTIME,CONVERT(varchar(100), a.ENDTIME, 23) as ENDTIME,b.STATUS,a.ACTIONDATE from WOBO_TRANSFER_PLAN a, WOBO_APPLY_LIST b where a.STATUS in ('0', '2', '3') and a.EN_ID_CS = ?  and a.TP_ID = b.BIZ_ID) as A";
		if(searchContent != null && !"".equals(searchContent)){
			sql = sql + " where (TP_ID like '%"+searchContent+"%' or AM_ID like '%"+searchContent+"%' or EN_NAME_CS like'%"+searchContent+"%' or EN_NAME_ZZ like'%"+searchContent+"%' or EN_NAME_CZ like '%"+searchContent+"%' or BEGINTIME like '%" + searchContent + "%' or ENDTIME like '%"+searchContent+"%')";
		}
		if(statusValue != null && !"".equals(statusValue)){
			if(searchContent != null && !"".equals(searchContent)){
				sql = sql + " and STATUS in (" + statusValue +")";
			}else{
				sql = sql + " where STATUS in (" + statusValue +")";
			}
		}
		Page<Record> page = Db.paginate(pn, ps, "select * ", sql + " order by A.ACTIONDATE desc", EP_ID);
		recordList = page.getList();
		if(recordList.size() > 0){
			for(Record record:recordList){
				map = new HashMap<String, Object>();
				map.put("TP_ID", record.get("TP_ID"));
				map.put("AM_ID", record.get("AM_ID"));
				map.put("EN_ID_CS", record.get("EN_ID_CS"));
				map.put("EN_NAME_CS", record.get("EN_NAME_CS"));
				map.put("EN_ID_ZZ", record.get("EN_ID_ZZ"));
				map.put("EN_NAME_ZZ", record.get("EN_NAME_ZZ"));
				map.put("EN_ID_CZ", record.get("EN_ID_CZ"));
				map.put("EN_NAME_CZ", record.get("EN_NAME_CZ"));
				map.put("BEGINTIME", record.get("BEGINTIME"));
				map.put("ENDTIME", record.get("ENDTIME"));
				map.put("STATUS", record.get("STATUS"));
				if(approveStatusList != null){
					for(int i = 0; i < approveStatusList.size(); i ++){
						if(record.get("STATUS").equals(approveStatusList.get(i).get("dict_id"))){
							map.put("STATUSNAME", approveStatusList.get(i).get("dict_value"));
						}
					}
				}
				list.add(map);
			}
		}
		resMap.put("dataList", list);
		resMap.put("statusList", queryDict("approve_status", "value", "text"));
		resMap.put("totalPage", page.getTotalPage());
		resMap.put("totalRow", page.getTotalRow());
		return resMap;
	}
	
	/**
	 * @author weizanting
	 * @date 20170306
	 * 方法：查询医疗废物转移计划
	 */
	public Map<String, Object> queryPlan(String TP_ID){
		Map<String, Object> map = new HashMap<String, Object>();
		Record record = Db.findFirst("select * from WOBO_TRANSFER_PLAN where STATUS in ('0', '2', '3') and TP_ID = ?", TP_ID);
		if(record != null){
			map.put("TP_ID", record.get("TP_ID"));
			map.put("AM_ID", record.get("AM_ID"));
			map.put("EN_ID_CS", record.get("EN_ID_CS"));
			map.put("EN_NAME_CS", record.get("EN_NAME_CS"));
			map.put("EN_ID_ZZ", record.get("EN_ID_ZZ"));
			map.put("EN_NAME_ZZ", record.get("EN_NAME_ZZ"));
			map.put("EN_ID_CZ", record.get("EN_ID_CZ"));
			map.put("EN_NAME_CZ", record.get("EN_NAME_CZ"));
			map.put("UNIT_NUM", record.get("UNIT_NUM"));
			map.put("UNIT", record.get("UNIT"));
			map.put("LINKMAN", record.get("LINKMAN"));
			map.put("LINKTEL", record.get("LINKTEL"));
			map.put("LINKPHONE", record.get("LINKPHONE"));
			map.put("BEGINTIME", DateKit.toStr(record.getDate("BEGINTIME"), "yyyy-MM-dd"));
			map.put("ENDTIME", DateKit.toStr(record.getDate("ENDTIME"), "yyyy-MM-dd"));
			map.put("STATUS", record.get("STATUS"));
			Record AYL = Db.findFirst("select * from WOBO_APPLY_LIST where BIZ_ID = ? and BIZ_NAME = ? and EP_ID = ? and EP_NAME = ?", TP_ID, "医疗废物转移计划", record.get("EN_ID_CS"), record.get("EN_NAME_CS"));
			if(AYL != null){
				map.put("APPLY_ID", AYL.get("AYL_ID"));
				map.put("APPLY_STATUS", AYL.get("STATUS"));
				if("01".equals(AYL.get("STATUS")) || "02".equals(AYL.get("STATUS")) || "04".equals(AYL.get("STATUS")) || "05".equals(AYL.get("STATUS"))){
					map.put("btnFlag", true);
				}else{
					map.put("btnFlag", false);
				}
			}else{
				map.put("APPLY_ID", "");
				map.put("btnFlag", false);
			}
			Record ep = Db.findFirst("select * from WOBO_TRANSFER_PLAN_MODIFY where TP_ID = ? and STATUS = '3'", TP_ID);
			if(ep != null){
				map.put("transferOrgFlag", true);
			}else{
				map.put("transferOrgFlag", false);
			}
		}
		return map;
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170622
	 * 方法：查询修改中转单位后医疗废物转移计划
	 */
	public Map<String, Object> queryPlanModify(String TP_ID, String bizId){
		Map<String, Object> map = new HashMap<String, Object>();
		Record record = Db.findFirst("select * from WOBO_TRANSFER_PLAN where STATUS = '3' and TP_ID = ?", TP_ID);
		if(record != null){
			map.put("TP_ID", record.get("TP_ID"));
			map.put("AM_ID", record.get("AM_ID"));
			map.put("EN_ID_CS", record.get("EN_ID_CS"));
			map.put("EN_NAME_CS", record.get("EN_NAME_CS"));
//			Record old = Db.findFirst("select * from WOBO_TRANSFER_PLAN_MODIFY where TP_ID = ? and AM_ID = ? and STATUS = '3' order by sysdate desc",  record.get("TP_ID"), record.get("AM_ID"));
//			if(old != null){
//				map.put("EN_ID_ZZ_OLD", old.get("EN_ID_ZZ"));
//				map.put("EN_NAME_ZZ_OLD", old.get("EN_NAME_ZZ"));
//			}else{
			map.put("EN_ID_ZZ_OLD", record.get("EN_ID_ZZ"));
			map.put("EN_NAME_ZZ_OLD", record.get("EN_NAME_ZZ"));
//			}
			Record modify = Db.findFirst("select * from WOBO_TRANSFER_PLAN_MODIFY where TM_ID = ? and AM_ID = ? and STATUS in ('0','1','3') order by sysdate desc",  bizId, record.get("AM_ID"));
			if(modify != null){
				map.put("EN_ID_ZZ", modify.get("EN_ID_ZZ"));
				map.put("EN_NAME_ZZ", modify.get("EN_NAME_ZZ"));
			}else{
				map.put("EN_ID_ZZ", record.get("EN_ID_ZZ"));
				map.put("EN_NAME_ZZ", record.get("EN_NAME_ZZ"));
			}
			map.put("EN_ID_CZ", record.get("EN_ID_CZ"));
			map.put("EN_NAME_CZ", record.get("EN_NAME_CZ"));
			map.put("UNIT_NUM", record.get("UNIT_NUM"));
			map.put("UNIT", record.get("UNIT"));
			map.put("LINKMAN", record.get("LINKMAN"));
			map.put("LINKTEL", record.get("LINKTEL"));
			map.put("LINKPHONE", record.get("LINKPHONE"));
			map.put("BEGINTIME", DateKit.toStr(record.getDate("BEGINTIME"), "yyyy-MM-dd"));
			map.put("ENDTIME", DateKit.toStr(record.getDate("ENDTIME"), "yyyy-MM-dd"));
			map.put("STATUS", record.get("STATUS"));
//			Record AYL = Db.findFirst("select * from WOBO_APPLY_LIST where BIZ_ID = ? and BIZ_NAME = ? and EP_ID = ? and EP_NAME = ?", TP_ID, "医疗废物转移计划", record.get("EN_ID_CS"), record.get("EN_NAME_CS"));
//			if(AYL != null){
//				map.put("APPLY_ID", AYL.get("AYL_ID"));
//				if("01".equals(AYL.get("STATUS")) || "02".equals(AYL.get("STATUS")) || "04".equals(AYL.get("STATUS")) || "05".equals(AYL.get("STATUS"))){
//					map.put("btnFlag", true);
//				}else{
//					map.put("btnFlag", false);
//				}
//			}else{
//				map.put("APPLY_ID", "");
//				map.put("btnFlag", false);
//			}
		}
		return map;
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170720
	 * 方法：查询修改中转单位的医疗废物转移计划id
	 */
	public String queryPlanId(String bizId){
		Record record = Db.findFirst("select TP_ID from WOBO_TRANSFER_PLAN_MODIFY where TM_ID = ?", bizId);
		if(record != null){
			return record.getStr("TP_ID");
		}
		return "";
	}
	
	/**
	 * @author weizanting
	 * @date 20170306
	 * 方法：保存医疗废物转移计划
	 */
	public String savePlan(Map<String, Object> map){
//		String applyId = super.getSeqId("WOBO_APPLY_LIST");
		if(Db.update("update WOBO_TRANSFER_PLAN set LINKMAN = ?, LINKTEL = ?, LINKPHONE = ?, ACTIONDATE = ? where TP_ID = ?", map.get("LINKMAN"), map.get("LINKTEL"), map.get("LINKPHONE"), super.getSysdate(), map.get("TP_ID")) > 0){
//			if(map.get("APPLY_ID") == null || "".equals(map.get("APPLY_ID"))){
//				Record record = new Record();
//				record.set("AYL_ID", applyId);
//				record.set("BIZ_ID", map.get("TP_ID"));
//				record.set("BIZ_NAME", map.get("BIZ_NAME"));
//				record.set("EP_ID", map.get("EP_ID"));
//				record.set("EP_NAME", map.get("EP_NAME"));
//				record.set("BIZ_VERSION", "0");
//				record.set("PROCESSINSTID", map.get("PROCESSINSTID"));
//				record.set("APPLY_DATE", super.getSysdate());
//				record.set("STATUS", "00");
//				record.set("BELONG_SEPA", map.get("BELONG_SEPA"));
//				record.set("sysdate", super.getSysdate());
//				if(Db.save("WOBO_APPLY_LIST", "AYL_ID", record)){
//					return applyId;
//				}
//			}else{
			return map.get("APPLY_ID").toString();
//			}
		}
		return "";
	}
	
	/**
	 * @author weizanting
	 * @date 20170306
	 * 方法：提交医疗废物转移计划
	 */
	public boolean sumitPlan(Map<String, Object> map){
		boolean flag = Db.update("update WOBO_TRANSFER_PLAN set STATUS = '2', ACTIONDATE = ? where TP_ID = ?", super.getSysdate(), map.get("TP_ID")) > 0;
		if(flag){
			return Db.update("update WOBO_APPLY_LIST set STATUS = '01', BIZ_VERSION = BIZ_VERSION+1, APPLY_DATE = ? where AYL_ID = ?", super.getSysdate(), map.get("APPLY_ID")) > 0;
//			Record AYL = Db.findFirst("select * from WOBO_APPLY_LIST where BIZ_ID = ? and BIZ_NAME = ? and EP_ID = ? and EP_NAME = ?", map.get("TP_ID").toString(), map.get("BIZ_NAME").toString(), map.get("EP_ID").toString(), map.get("EP_NAME").toString());
//			if(AYL != null){
//				return Db.update("update WOBO_APPLY_LIST set BIZ_ID = ?,BIZ_NAME = ?,EP_ID = ?,EP_NAME = ?,BIZ_VERSION = cast(isnull(BIZ_VERSION, 0) as int) + 1,PROCESSINSYID = ?,BELONG_SEPA = ? where AYL_ID = ?", map.get("TP_ID").toString(), map.get("BIZ_NAME").toString(), map.get("EP_ID").toString(), map.get("EP_NAME").toString(), map.get("PROCESSINSYID").toString(), map.get("BELONG_SEPA").toString(), AYL.get("AYL_ID").toString()) > 0;
//			}else{
//				Record record = new Record();
//				record.set("AYL_ID", super.getSeqId("WOBO_APPLY_LIST"));
//				record.set("BIZ_ID", map.get("TP_ID"));
//				record.set("BIZ_NAME", map.get("BIZ_NAME"));
//				record.set("EP_ID", map.get("EP_ID"));
//				record.set("EP_NAME", map.get("EP_NAME"));
//				record.set("BIZ_VERSION", "1");
//				record.set("PROCESSINSTID", map.get("PROCESSINSTID"));
//				record.set("APPLY_DATE", super.getSysdate());
//				record.set("STATUS", "01");
//				record.set("BELONG_SEPA", map.get("BELONG_SEPA"));
//				record.set("sysdate", super.getSysdate());
//				return Db.save("WOBO_APPLY_LIST", "AYL_ID", record);
//			}
		}else{
			return flag;
		}
	}
	
	/**
	 * 
	 * @author weizanting
	 * @date 20170306
	 * 方法：医疗机构管理员登录
	 * return 1-生产  2-处置
	 * 
	 */
	public String queryEPType(String EP_ID){
		Record record = Db.findFirst("select * from wobo_enterprise where EP_ID = ?", EP_ID);
		String str = "";
		if(record != null && record.get("IF_PRODUCE") != null && !"".equals(record.get("IF_PRODUCE")) && record.get("IF_HANDLE") != null && !"".equals(record.get("IF_HANDLE"))){
			if("1".equals(record.get("IF_PRODUCE"))){
				str = "1";
			}
			if("1".equals(record.get("IF_HANDLE"))){
				str = "2";
			}
		}
		return str;
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170526
	 * 方法：管理员查询医疗废物转移计划列表
	 */
	public Map<String, Object> queryPlanListForAdmin(int pn, int ps, String area, String ROLEID, Object searchContent, Object statusValue, Object sepaValue, List<Object> statusCache){
		Map<String, Object> map = null;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> resMap = new HashMap<String, Object>();
		String sql = "";
		if("SJSPROLE".equals(ROLEID)){		//SJSPROLE-市级管理员
			sql = "from (select a.TP_ID,a.AM_ID,a.EN_ID_CS,a.EN_NAME_CS,a.EN_ID_ZZ,a.EN_NAME_ZZ,a.EN_ID_CZ,a.EN_NAME_CZ,a.STATUS as STATUSA,c.STATUS,a.sysdate,(case a.BEGINTIME when '' then '' else CONVERT(varchar(100), a.BEGINTIME, 23) end) as BEGINTIME,(case a.ENDTIME when '' then '' else CONVERT(varchar(100), a.ENDTIME, 23) end) as ENDTIME,b.BELONGSEPA from WOBO_TRANSFER_PLAN a, WOBO_ENTERPRISE b, WOBO_APPLY_LIST c where a.STATUS not in ('0', '1') and a.EN_ID_CS = b.EP_ID and a.TP_ID = c.BIZ_ID) as A where A.STATUSA not in ('0', '1')";
			if(searchContent != null && !"".equals(searchContent)){
				sql = sql + " and (A.TP_ID like '%"+searchContent+"%' or A.AM_ID like '%"+searchContent+"%' or A.EN_NAME_CS like '%"+searchContent+"%' or A.EN_NAME_ZZ like '%"+searchContent+"%' or A.EN_NAME_CZ like '%"+searchContent+"%' or A.BEGINTIME like '%"+searchContent+"%' or A.ENDTIME like '%"+searchContent+"%')";
			}
			if(statusValue != null && !"".equals(statusValue)){
				sql = sql + " and A.STATUS in ("+statusValue+")";
			}
			if(sepaValue != null && !"".equals(sepaValue)){
				sql = sql + " and A.BELONGSEPA in ("+sepaValue+")";
			}
			sql = sql + " order by A.sysdate desc";
		}else{		//区级管理员
			sql = "from (select a.TP_ID,a.AM_ID,a.EN_ID_CS,a.EN_NAME_CS,a.EN_ID_ZZ,a.EN_NAME_ZZ,a.EN_ID_CZ,a.EN_NAME_CZ,a.STATUS as STATUSA,c.STATUS,a.sysdate,(case a.BEGINTIME when '' then '' else CONVERT(varchar(100), a.BEGINTIME, 23) end) as BEGINTIME,(case a.ENDTIME when '' then '' else CONVERT(varchar(100), a.ENDTIME, 23) end) as ENDTIME,b.BELONGSEPA from WOBO_TRANSFER_PLAN a, WOBO_ENTERPRISE b, WOBO_APPLY_LIST c where a.STATUS not in ('0', '1') and a.EN_ID_CS = b.EP_ID and b.BELONGSEPA = '"+area+"' and a.TP_ID = c.BIZ_ID) as A where A.STATUSA not in ('0', '1')";
			if(searchContent != null && !"".equals(searchContent)){
				sql = sql + " and (A.TP_ID like '%"+searchContent+"%' or A.AM_ID like '%"+searchContent+"%' or A.EN_NAME_CS like '%"+searchContent+"%' or A.EN_NAME_ZZ like '%"+searchContent+"%' or A.EN_NAME_CZ like '%"+searchContent+"%' or A.BEGINTIME like '%"+searchContent+"%' or A.ENDTIME like '%"+searchContent+"%')";
			}
			if(statusValue != null && !"".equals(statusValue)){
				sql = sql + " and A.STATUS in ("+statusValue+")";
			}
			sql = sql + " order by A.sysdate desc";
		}
		Page<Record> page = Db.paginate(pn, ps, "select *", sql);
		List<Record> recordList = page.getList();
		if(recordList.size() > 0){
			for(Record record:recordList){
				map = new HashMap<String, Object>();
				map.put("TP_ID", record.get("TP_ID"));
				map.put("AM_ID", record.get("AM_ID"));
				map.put("EN_ID_CS", record.get("EN_ID_CS"));
				map.put("EN_NAME_CS", record.get("EN_NAME_CS"));
				map.put("EN_ID_ZZ", record.get("EN_ID_ZZ"));
				map.put("EN_NAME_ZZ", record.get("EN_NAME_ZZ"));
				map.put("EN_ID_CZ", record.get("EN_ID_CZ"));
				map.put("EN_NAME_CZ", record.get("EN_NAME_CZ"));
				map.put("BEGINTIME", record.get("BEGINTIME"));
				map.put("ENDTIME", record.get("ENDTIME"));
				if(approveStatusList != null){
					for(int i = 0; i < approveStatusList.size(); i ++){
						if(record.get("STATUS").equals(approveStatusList.get(i).get("dict_id"))){
							map.put("STATUSNAME", approveStatusList.get(i).get("dict_value"));
						}
					}
				}
				map.put("STATUS", record.get("STATUS"));
				map.put("BELONG_SEPA", record.get("BELONGSEPA"));
				map.put("SEPA_NAME", convert(cityList, record.get("BELONGSEPA")) + "环保局");
				list.add(map);
			}
		}
		resMap.put("dataList", list);
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
	 * @author ouyangxu
	 * @date 20170621
	 * 方法：查询运行中转移计划列表
	 */
	public Map<String, Object> queryTransferRunningList(String EP_ID, int pn, int ps, Object searchContent){
		Map<String, Object> map = null;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> resMap = new HashMap<String, Object>();
		String sql = "from (select TP_ID,AM_ID,EN_ID_CS,EN_NAME_CS,EN_ID_ZZ,EN_NAME_ZZ,EN_ID_CZ,EN_NAME_CZ,CONVERT(varchar(100), BEGINTIME, 23) as BEGINTIME,CONVERT(varchar(100), ENDTIME, 23) as ENDTIME,STATUS,ACTIONDATE from WOBO_TRANSFER_PLAN where STATUS = '3' and EN_ID_CS = ? and BEGINTIME < ? and ENDTIME > ?) as A";
		if(searchContent != null && !"".equals(searchContent)){
			sql = sql + " where (TP_ID like '%"+searchContent+"%' or AM_ID like '%"+searchContent+"%' or EN_NAME_CS like'%"+searchContent+"%' or EN_NAME_ZZ like'%"+searchContent+"%' or EN_NAME_CZ like '%"+searchContent+"%' or BEGINTIME like '%" + searchContent + "%' or ENDTIME like '%"+searchContent+"%')";
		}
		sql = sql + " order by A.ACTIONDATE desc";
		Timestamp now = super.getSysdate();
		Page<Record> page = Db.paginate(pn, ps, "select *", sql, EP_ID, now, now);
		List<Record> recordList = page.getList();
		if(recordList.size() > 0){
			for(Record record:recordList){
				map = new HashMap<String, Object>();
				map.put("TP_ID", record.get("TP_ID"));
				map.put("AM_ID", record.get("AM_ID"));
				map.put("EN_ID_CS", record.get("EN_ID_CS"));
				map.put("EN_NAME_CS", record.get("EN_NAME_CS"));
				map.put("EN_ID_ZZ", record.get("EN_ID_ZZ"));
				map.put("EN_NAME_ZZ", record.get("EN_NAME_ZZ"));
				map.put("EN_ID_CZ", record.get("EN_ID_CZ"));
				map.put("EN_NAME_CZ", record.get("EN_NAME_CZ"));
				map.put("BEGINTIME", record.get("BEGINTIME"));
				map.put("ENDTIME", record.get("ENDTIME"));
				list.add(map);
			}
		}
		resMap.put("dataList", list);
		resMap.put("totalPage", page.getTotalPage());
		resMap.put("totalRow", page.getTotalRow());
		return resMap;
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170621
	 * 方法：查询运行中医疗废物转移计划详情
	 */
	public Map<String, Object> queryTransferRunningInfo(String TP_ID, Object bizId){
		Map<String, Object> map = new HashMap<String, Object>();
		Record record = Db.findFirst("select * from WOBO_TRANSFER_PLAN where STATUS = '3' and TP_ID = ?", TP_ID);
		if(record != null){
			map.put("TP_ID", record.get("TP_ID"));
			map.put("AM_ID", record.get("AM_ID"));
			map.put("EN_ID_CS", record.get("EN_ID_CS"));
			map.put("EN_NAME_CS", record.get("EN_NAME_CS"));
//			Record old = Db.findFirst("select * from WOBO_TRANSFER_PLAN_MODIFY where TP_ID = ? and AM_ID = ? and STATUS = '3' order by sysdate desc",  record.get("TP_ID"), record.get("AM_ID"));
//			if(old != null){
//				map.put("EN_ID_ZZ_OLD", old.get("EN_ID_ZZ"));
//				map.put("EN_NAME_ZZ_OLD", old.get("EN_NAME_ZZ"));
//			}else{
			map.put("EN_ID_ZZ_OLD", record.get("EN_ID_ZZ"));
			map.put("EN_NAME_ZZ_OLD", record.get("EN_NAME_ZZ"));
//			}
			if(bizId != null && !"".equals(bizId)){
				Record modify = Db.findFirst("select * from WOBO_TRANSFER_PLAN_MODIFY where TM_ID = ? and AM_ID = ? and STATUS in ('0','1','3') order by sysdate desc",  bizId, record.get("AM_ID"));
				if(modify != null){
					map.put("EN_ID_ZZ", modify.get("EN_ID_ZZ"));
					map.put("EN_NAME_ZZ", modify.get("EN_NAME_ZZ"));
				}else{
					map.put("EN_ID_ZZ", record.get("EN_ID_ZZ"));
					map.put("EN_NAME_ZZ", record.get("EN_NAME_ZZ"));
				}
			}else{
				Record modify = Db.findFirst("select * from WOBO_TRANSFER_PLAN_MODIFY where TP_ID = ? and AM_ID = ? and STATUS in ('0','1','3') order by sysdate desc",  record.get("TP_ID"), record.get("AM_ID"));
				if(modify != null){
					map.put("EN_ID_ZZ", modify.get("EN_ID_ZZ"));
					map.put("EN_NAME_ZZ", modify.get("EN_NAME_ZZ"));
				}else{
					map.put("EN_ID_ZZ", record.get("EN_ID_ZZ"));
					map.put("EN_NAME_ZZ", record.get("EN_NAME_ZZ"));
				}
			}
			map.put("EN_ID_CZ", record.get("EN_ID_CZ"));
			map.put("EN_NAME_CZ", record.get("EN_NAME_CZ"));
			map.put("UNIT_NUM", record.get("UNIT_NUM"));
			map.put("UNIT", record.get("UNIT"));
			map.put("LINKMAN", record.get("LINKMAN"));
			map.put("LINKTEL", record.get("LINKTEL"));
			map.put("LINKPHONE", record.get("LINKPHONE"));
			map.put("BEGINTIME", DateKit.toStr(record.getDate("BEGINTIME"), "yyyy-MM-dd"));
			map.put("ENDTIME", DateKit.toStr(record.getDate("ENDTIME"), "yyyy-MM-dd"));
			map.put("STATUS", record.get("STATUS"));
//			Record AYL = Db.findFirst("select * from WOBO_APPLY_LIST where BIZ_NAME = ? and EP_ID = ? and EP_NAME = ? order by sysdate desc", "运行中转移计划变更中转单位", record.get("EN_ID_CS"), record.get("EN_NAME_CS"));
			Record AYL = Db.findFirst("select a.AYL_ID,a.STATUS,b.TP_ID from WOBO_APPLY_LIST a,WOBO_TRANSFER_PLAN_MODIFY b where a.BIZ_ID = b.TM_ID and a.BIZ_NAME = ? and a.EP_ID = ? and a.EP_NAME = ? order by a.sysdate desc", "运行中转移计划变更中转单位", record.get("EN_ID_CS"), record.get("EN_NAME_CS"));
			if(AYL != null){
				map.put("APPLY_ID", AYL.get("AYL_ID"));
				if(TP_ID.equals(AYL.get("TP_ID"))){
					if("00".equals(AYL.get("STATUS"))){
						map.put("checkFlag", false);
					}else{
						map.put("checkFlag", true);
					}
				}else{
					map.put("checkFlag", false);
				}
				if("01".equals(AYL.get("STATUS")) || "02".equals(AYL.get("STATUS")) || "05".equals(AYL.get("STATUS"))){
					map.put("btnFlag", true);
				}else{
					map.put("btnFlag", false);
				}
			}else{
				map.put("APPLY_ID", "");
				map.put("btnFlag", false);
			}
			AgreementDao agreementDao = new AgreementDao();
			map.put("zzs", agreementDao.queryEP(record.get("EN_ID_CS").toString()));
		}
		return map;
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170621
	 * 方法：运行中转移计划修改中转单位
	 */
	public String saveTransferRunningModify(Map<String, Object> map){
		Record record = Db.findFirst("select * from WOBO_TRANSFER_PLAN_MODIFY where TP_ID = ? and AM_ID = ? and STATUS in ('0','1')", map.get("TP_ID"), map.get("AM_ID"));
		if(record != null){
			if(Db.update("update WOBO_TRANSFER_PLAN_MODIFY set EN_ID_ZZ = ?, EN_NAME_ZZ = ? where  TP_ID = ? and AM_ID = ? and STATUS in ('0','1')", map.get("EN_ID_ZZ"), map.get("EN_NAME_ZZ"), map.get("TP_ID"), map.get("AM_ID")) > 0){
				return record.get("TM_ID");
			}
		}else{
			Record newRecord = new Record();
			String id = super.getSeqId("WOBO_TRANSFER_PLAN_MODIFY");
			newRecord.set("TM_ID", id);
			newRecord.set("TP_ID", map.get("TP_ID"));
			newRecord.set("AM_ID", map.get("AM_ID"));
			newRecord.set("EN_ID_ZZ", map.get("EN_ID_ZZ"));
			newRecord.set("EN_NAME_ZZ", map.get("EN_NAME_ZZ"));
			newRecord.set("STATUS", "0");
			newRecord.set("sysdate", super.getSysdate());
			if(Db.save("WOBO_TRANSFER_PLAN_MODIFY", newRecord)){
				return id;
			}
		}
		return "";
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170622
	 * 方法：保存申请
	 */
	public String saveApply(String epId, String epName, String belongHbj, String bizId, String bizName){
		Record btnFlag = Db.findFirst("select AYL_ID from WOBO_APPLY_LIST where EP_ID = ? and BIZ_NAME = ? and STATUS != '04' and BIZ_ID = ?", epId, bizName, bizId);
		if(btnFlag != null){
			return btnFlag.getStr("AYL_ID");
		}
		Record record = new Record();
		String applyId = super.getSeqId("WOBO_APPLY_LIST");
		Timestamp now = super.getSysdate();
		record.set("AYL_ID", applyId);
		record.set("BIZ_ID", bizId);
		record.set("BIZ_NAME", bizName);
		record.set("EP_ID", epId);
		record.set("EP_NAME", epName);
		record.set("BIZ_VERSION", 0);
		record.set("APPLY_DATE", now);
		record.set("STATUS", "00");
		record.set("BELONG_SEPA", belongHbj);
		record.set("sysdate", now);
		if(Db.save("WOBO_APPLY_LIST", "AYL_ID", record)){
			return applyId;
		}else{
			return "";
		}
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170621
	 * 方法：提交运行中转移计划修改中转单位
	 */
	public boolean submitTransferRunningModify(Map<String, Object> map){
		return Db.update("update WOBO_TRANSFER_PLAN_MODIFY set STATUS = '1' where  TP_ID = ? and AM_ID = ? and STATUS in ('0','1')", map.get("TP_ID"), map.get("AM_ID")) > 0;
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170622
	 * 方法：提交申请
	 */
	public boolean submitApply(String epId, String bizId, String bizName){
		return Db.update("update WOBO_APPLY_LIST set STATUS = '01', BIZ_VERSION = BIZ_VERSION+1, APPLY_DATE = ? where BIZ_ID = ? and EP_ID = ? and BIZ_NAME = ? and STATUS != '04'", super.getSysdate(), bizId, epId, bizName) > 0;
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170622
	 * 方法：查询所属中转单位
	 */
	public String belongOrg(String tpId){
		Record epZz = Db.findFirst("select EN_ID_ZZ from WOBO_TRANSFER_PLAN_MODIFY where TP_ID = ? and STATUS = '3' order by sysdate desc", tpId);
		if(epZz != null){
			if(epZz.get("EN_ID_ZZ") != null && !"".equals(epZz.get("EN_ID_ZZ"))){
				return epZz.get("EN_ID_ZZ");
			}else{
				return "";
			}
		}
		Record record = Db.findFirst("select EN_ID_ZZ from WOBO_TRANSFER_PLAN where TP_ID = ? and STATUS = '3'", tpId);
		if(record != null){
			return record.getStr("EN_ID_ZZ");
		}
		return "";
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170622
	 * 方法：查询运行中转移计划修改中转单位记录
	 */
	public List<Map<String, Object>> queryModifyTransferOrgList(Object tpId, Object amId) throws ParseException{
		String sql = "";
		if(tpId != null && !"".equals(tpId)){
			sql = "select a.EN_ID_ZZ,a.EN_NAME_ZZ,b.BELONGSEPA,a.sysdate from WOBO_TRANSFER_PLAN_MODIFY a left join WOBO_ENTERPRISE b on a.EN_ID_ZZ = b.EP_ID and b.STATUS = '1' where a.TP_ID = '"+tpId+"' and a.STATUS = '3' order by a.sysdate desc";
		}
		if(amId != null && !"".equals(amId)){
			sql = "select a.EN_ID_ZZ,a.EN_NAME_ZZ,b.BELONGSEPA,a.sysdate from WOBO_TRANSFER_PLAN_MODIFY a left join WOBO_ENTERPRISE b on a.EN_ID_ZZ = b.EP_ID and b.STATUS = '1' where a.AM_ID = '"+amId+"' and a.STATUS = '3' order by a.sysdate desc";
		}
		List<Map<String, Object>> list = new ArrayList<>();
		List<Record> epZzs = Db.find(sql);
		if(epZzs.size() > 0){
			for(Record epZz : epZzs){
				Map<String, Object> map = new HashMap<>();
				map.put("EN_ID_ZZ", epZz.get("EN_ID_ZZ"));
				map.put("EN_NAME_ZZ", epZz.get("EN_NAME_ZZ"));
				map.put("sysdate", DateKit.toStr(epZz.getDate("sysdate"), "yyyy-MM-dd HH:mm:ss"));
				if(epZz.get("BELONGSEPA") != null && !"".equals(epZz.get("BELONGSEPA"))){
					map.put("SEPA_NAME", convert(cityList, epZz.get("BELONGSEPA")) + "环保局");
				}else{
					map.put("SEPA_NAME", "");
				}
				list.add(map);
			}
		}
		return list;
	}
}
