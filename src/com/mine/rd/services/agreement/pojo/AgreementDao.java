package com.mine.rd.services.agreement.pojo;

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
import com.mine.rd.services.plan.pojo.PlanDao;

public class AgreementDao extends BaseDao {

	private PlanDao dao = new PlanDao();
	//查询字典表数据
	List<Record> statusList = CacheKit.get("mydict", "agreement_status");
	List<Record> cityList = CacheKit.get("mydict", "city_q");
	
	/**
	 * @author weizanting
	 * @date 20170227
	 * 方法：查询医疗废物处置协议列表
	 */
	public Map<String, Object> queryAgreementList(String EP_ID, int pn, int ps, Object searchContent, Object statusValue){
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		List<Record> recordList = new ArrayList<Record>();
		//单位类型 （1-医疗单位  2-医疗处置单位）
		String str = dao.queryEPType(EP_ID);
		Page<Record> page = null;
		Map<String, Object> resMap = new HashMap<String, Object>();
		String sql = "";
		if(searchContent != null && !"".equals(searchContent)){
			sql = " and (AM_ID like '%"+searchContent+"%' or EN_NAME_CS like '%"+searchContent+"%' or EN_NAME_ZZ like'%"+searchContent+"%' or EN_NAME_CZ like'%"+searchContent+"%' or BEGINTIME like '%"+searchContent+"%' or ENDTIME like '%"+searchContent+"%')";
		}
		if(statusValue != null && !"".equals(statusValue)){
			sql = sql + " and STATUS in (" + statusValue +")";
		}
		if("1".equals(str)){
			page = Db.paginate(pn, ps, "select AM_ID,EN_NAME_CS,EN_NAME_ZZ,EN_NAME_CZ,BEGINTIME,ENDTIME,STATUS ", "from (select AM_ID,EN_NAME_CS,EN_NAME_ZZ,EN_NAME_CZ,CONVERT(varchar(100), BEGINTIME, 23) BEGINTIME,CONVERT(varchar(100), ENDTIME, 23) ENDTIME,STATUS,ACTIONDATE from WOBO_AGREEMENT where STATUS in ('0', '2', '3', '4') and EN_ID_CS = ?) as A where STATUS in ('0', '2', '3', '4')" + sql + " order by ACTIONDATE desc", EP_ID);
			recordList = page.getList();
		}
		if("2".equals(str)){
			page = Db.paginate(pn, ps, "select AM_ID,EN_NAME_CS,EN_NAME_ZZ,EN_NAME_CZ,BEGINTIME,ENDTIME,STATUS ", "from (select AM_ID,EN_NAME_CS,EN_NAME_ZZ,EN_NAME_CZ,CONVERT(varchar(100), BEGINTIME, 23) BEGINTIME,CONVERT(varchar(100), ENDTIME, 23) ENDTIME,STATUS,ACTIONDATE from WOBO_AGREEMENT where STATUS in ('2', '3', '4') and EN_ID_CZ = ?) as A where STATUS in ('2', '3', '4')" + sql + " order by ACTIONDATE desc", EP_ID);
			recordList = page.getList();
		}
		Map<String, Object> map = null;
		if(recordList.size() > 0){
			for(Record record : recordList){
				map = new HashMap<String, Object>();
				map.put("AM_ID", record.get("AM_ID"));
				map.put("EN_ID_CS", record.get("EN_ID_CS"));
				map.put("EN_NAME_CS", record.get("EN_NAME_CS"));
				map.put("EN_ID_CZ", record.get("EN_ID_CZ"));
				map.put("EN_NAME_CZ", record.get("EN_NAME_CZ"));
				map.put("EN_ID_ZZ", record.get("EN_ID_ZZ"));
				map.put("EN_NAME_ZZ", record.get("EN_NAME_ZZ"));
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
		resMap.put("statusList", queryDict("agreement_status", "value", "text"));
		resMap.put("totalPage", page.getTotalPage());
		resMap.put("totalRow", page.getTotalRow());
		return resMap;
	}
	
	/**
	 * @author weizanting
	 * @date 20170303
	 * 方法：查询医疗废物处置协议
	 */
	public Map<String, Object> queryAgreement(String AM_ID){
		Record record = Db.findFirst("select * from WOBO_AGREEMENT where AM_ID = ?", AM_ID);
		Map<String, Object> map = new HashMap<String, Object>();
		if(record != null){
			map.put("AM_ID", record.get("AM_ID"));
			map.put("EN_ID_CS", record.get("EN_ID_CS"));
			map.put("EN_NAME_CS", record.get("EN_NAME_CS"));
			map.put("EN_ID_CZ", record.get("EN_ID_CZ"));
			map.put("EN_NAME_CZ", record.get("EN_NAME_CZ"));
			map.put("EN_ID_ZZ", record.get("EN_ID_ZZ"));
			map.put("EN_NAME_ZZ", record.get("EN_NAME_ZZ"));
			map.put("UNIT_NUM", record.get("UNIT_NUM"));
			map.put("BEGINTIME", DateKit.toStr(record.getDate("BEGINTIME"), "yyyy-MM-dd"));
			map.put("ENDTIME", DateKit.toStr(record.getDate("ENDTIME"), "yyyy-MM-dd"));
			map.put("LINKMAN", record.get("LINKMAN"));
			map.put("LINKTEL", record.get("LINKTEL"));
			map.put("LINKPHONE", record.get("LINKPHONE"));
			map.put("REASON", record.get("REASON"));
			map.put("STATUS", record.get("STATUS"));
			Record ep = Db.findFirst("select * from WOBO_TRANSFER_PLAN_MODIFY where AM_ID = ? and STATUS = '3'", AM_ID);
			if(ep != null){
				map.put("transferOrgFlag", true);
			}else{
				map.put("transferOrgFlag", false);
			}
		}
		return map;
	}
	
	/**
	 * @author weizanting
	 * @date 20170303
	 * 方法：保存医疗废物处置协议
	 */
	public String saveAgreementList(Map<String, Object> map){
		String AM_ID = "";
		Record record = new Record();
		if("".equals(map.get("AM_ID"))){		//新增
			AM_ID = super.getSeqId("WOBO_AGREEMENT");
			record.set("AM_ID", AM_ID);
			record.set("EN_ID_CS", map.get("EN_ID_CS"));
			record.set("EN_NAME_CS", map.get("EN_NAME_CS"));
			record.set("EN_ID_CZ", map.get("EN_ID_CZ"));
			record.set("EN_NAME_CZ", map.get("EN_NAME_CZ"));
			record.set("EN_ID_ZZ", map.get("EN_ID_ZZ"));
			record.set("EN_NAME_ZZ", map.get("EN_NAME_ZZ"));
			record.set("UNIT_NUM", map.get("UNIT_NUM"));
			record.set("UNIT", map.get("UNIT"));
			record.set("BEGINTIME", map.get("BEGINTIME"));
			record.set("ENDTIME", map.get("ENDTIME"));
			record.set("LINKMAN", map.get("LINKMAN"));
			record.set("LINKTEL", map.get("LINKTEL"));
			record.set("LINKPHONE", map.get("LINKPHONE"));
			record.set("ACTIONDATE", super.getSysdate());
			record.set("STATUS", "0");
			record.set("sysdate", super.getSysdate());
			boolean flag = Db.save("WOBO_AGREEMENT", "AM_ID", record);
			if(!flag){
				return "";
			}
		}else{		//修改
			AM_ID = map.get("AM_ID").toString();
			String sql = "update WOBO_AGREEMENT set EN_ID_CS=?,EN_NAME_CS=?,EN_ID_CZ=?,EN_NAME_CZ=?,EN_ID_ZZ=?,EN_NAME_ZZ=?,UNIT_NUM=?,UNIT=?,BEGINTIME=?,ENDTIME=?,LINKMAN=?,LINKTEL=?,LINKPHONE=?,ACTIONDATE=? where AM_ID=?";
			boolean flag = Db.update(sql, map.get("EN_ID_CS"),map.get("EN_NAME_CS"),map.get("EN_ID_CZ"),map.get("EN_NAME_CZ"),map.get("EN_ID_ZZ"),map.get("EN_NAME_ZZ"),map.get("UNIT_NUM"),map.get("UNIT"),map.get("BEGINTIME"),map.get("ENDTIME"),map.get("LINKMAN"),map.get("LINKTEL"),map.get("LINKPHONE"),super.getSysdate(),AM_ID) > 0;
			/*Record agreement = Db.findFirst("select * from WOBO_AGREEMENT where AM_ID=?", AM_ID);
			agreement.set("EN_ID_CS", map.get("EN_ID_CS"));
			agreement.set("EN_NAME_CS", map.get("EN_NAME_CS"));
			agreement.set("EN_ID_CZ", map.get("EN_ID_CZ"));
			agreement.set("EN_NAME_CZ", map.get("EN_NAME_CZ"));
			agreement.set("EN_ID_ZZ", map.get("EN_ID_ZZ"));
			agreement.set("EN_NAME_ZZ", map.get("EN_NAME_ZZ"));
			agreement.set("UNIT_NUM", map.get("UNIT_NUM"));
			agreement.set("UNIT", map.get("UNIT"));
			agreement.set("BEGINTIME", map.get("BEGINTIME"));
			agreement.set("ENDTIME", map.get("ENDTIME"));
			agreement.set("LINKMAN", map.get("LINKMAN"));
			agreement.set("LINKTEL", map.get("LINKTEL"));
			agreement.set("LINKPHONE", map.get("LINKPHONE"));
			agreement.set("ACTIONDATE", super.getSysdate());
			boolean flag = Db.update("WOBO_AGREEMENT", "AM_ID", agreement);*/
			if(!flag){
				return "";
			}
		}
		return AM_ID;
	}
	
	/**
	 * @author weizanting
	 * @date 20170303
	 * 方法：提交医疗废物处置协议
	 */
	public boolean submitAgreement(String AM_ID){
		return Db.update("update WOBO_AGREEMENT set STATUS='2' where AM_ID=?", AM_ID) > 0;
	}
	
	/**
	 * @author weizanting
	 * @date 20170303
	 * 方法：提交医疗废物处置协议
	 */
	public boolean agree(String AM_ID, String REASON, String STATUS){
		return Db.update("update WOBO_AGREEMENT set STATUS = ?, ACTIONDATE = ?, REASON = ? where AM_ID = ?", STATUS, super.getSysdate(), REASON, AM_ID) > 0;
	}
	
	/**
	 * @author weizanting
	 * @date 20170303
	 * 方法：保存医疗废物处置协议计划
	 */
	public boolean savePlan(String AM_ID){
		Record record = Db.findFirst("select * from WOBO_AGREEMENT where AM_ID=?", AM_ID);
		record.set("TP_ID", super.getSeqId("WOBO_TRANSFER_PLAN"));
		record.set("STATUS", "0");
		record.set("ACTIONDATE", super.getSysdate());
		record.set("LINKMAN", "");
		record.set("LINKTEL", "");
		record.set("LINKPHONE", "");
		record.set("sysdate", super.getSysdate());
		if(Db.save("WOBO_TRANSFER_PLAN", "TP_ID", record)){
			String epIdCs = record.get("EN_ID_CS");
			Object epIdCz = record.get("EN_ID_CZ");
			Object epIdZz = record.get("EN_ID_ZZ");
			if(epIdZz != null && !"".equals(epIdZz)){
				Record conduit = Db.findFirst("select * from WOBO_CONDUITS_TRANSFER_UNSUBMIT where EP_ID_CS = ? and EP_ID_ZZ = ?", epIdCs, epIdZz);
				if(conduit != null){
					Record zzPlan = Db.findFirst("select count(1) as totalNum from WOBO_CONDUITS_TRANSFER where EP_SMALL_ID = ? and EP_BIG_ID = ?", epIdCs, epIdZz);
					int totalNum = 0;
					if(zzPlan != null){
						totalNum = zzPlan.getInt("totalNum");
						List<Record> transfers = Db.find("select * from WOBO_TRANSFER_PLAN where EN_ID_CS = ? and EN_ID_ZZ = ? and STATUS != '1'", epIdCs, epIdZz);
						int count = 0;
						for(Record transfer : transfers){
							Record billList = Db.findFirst("select count(1) as totalNum from WOBO_CONDUITS_TRANSFER where EP_SMALL_ID = ? and EP_BIG_ID = ? and (TBDATE >= ? or TBDATE < ?)", epIdCs, epIdZz, transfer.get("BEGINTIME"), transfer.get("ENDTIME"));
							if(billList != null){
								count = count + billList.getInt("totalNum");
							}
						}
						if(count >= totalNum){
							return Db.update("delete from WOBO_CONDUITS_TRANSFER_UNSUBMIT where EP_ID_CS = ? and EP_ID_ZZ = ?", epIdCs, epIdZz) > 0;
						}else{
							return true;
						}
					}
				}else{
					return true;
				}
			}else{
				Record conduit = Db.findFirst("select * from WOBO_CONDUITS_TRANSFER_UNSUBMIT where EP_ID_CS = ? and EP_ID_CZ = ?", epIdCs, epIdCz);
				if(conduit != null){
					Record bills = Db.findFirst("select count(b.TB_ID) as totalNum from WOBO_TRANSFER_BIG a, WOBO_TRANSFER_BILL b where a.EN_ID_CZ = ? and b.EN_ID_CS = ? and a.TG_ID = b.TG_ID and a.STATUS != '2' and b.STATUS != '2'", epIdCz, epIdCs);
					int totalNum = 0;
					if(bills != null){
						totalNum = bills.getInt("totalNum");
						List<Record> transfers = Db.find("select * from WOBO_TRANSFER_PLAN where EN_ID_CS = ? and EN_ID_CZ = ? and STATUS != '1'", epIdCs, epIdCz);
						int count = 0;
						for(Record transfer : transfers){
							Record billList = Db.findFirst("select count(b.TB_ID) as totalNum from WOBO_TRANSFER_BIG a, WOBO_TRANSFER_BILL b where a.EN_ID_CZ = ? and b.EN_ID_CS = ? and a.TG_ID = b.TG_ID and a.STATUS != '2' and b.STATUS != '2' and b.BEGINTIME >= ? and b.BEGINTIME < ?", epIdCz, epIdCs, transfer.get("BEGINTIME"), transfer.get("ENDTIME"));
							if(billList != null){
								count = count + billList.getInt("totalNum");
							}
						}
						if(count >= totalNum){
							return Db.update("delete from WOBO_CONDUITS_TRANSFER_UNSUBMIT where EP_ID_CS = ? and EP_ID_CZ = ?", epIdCs, epIdCz) > 0;
						}else{
							return true;
						}
					}else{
						return true;
					}
				}else{
					return true;
				}
			}
			
		}
		return false;
	}
	
	//保存医疗转移计划的申请
	public boolean saveApplyForPlan(String AM_ID){
		String applyId = super.getSeqId("WOBO_APPLY_LIST");
		Record map = Db.findFirst("select a.*,b.BELONGSEPA from WOBO_TRANSFER_PLAN a, WOBO_ENTERPRISE b where a.AM_ID=? and a.EN_ID_CS = b.EP_ID", AM_ID);
		if(map != null){
			Record record = new Record();
			record.set("AYL_ID", applyId);
			record.set("BIZ_ID", map.get("TP_ID"));
			record.set("BIZ_NAME", "医疗废物转移计划");
			record.set("EP_ID", map.get("EN_ID_CS"));
			record.set("EP_NAME", map.get("EN_NAME_CS"));
			record.set("BIZ_VERSION", "0");
			record.set("APPLY_DATE", super.getSysdate());
			record.set("STATUS", "00");
			record.set("BELONG_SEPA", map.get("BELONGSEPA"));
			record.set("sysdate", super.getSysdate());
			if(Db.save("WOBO_APPLY_LIST", "AYL_ID", record)){
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * @author weizanting
	 * @date 20170306
	 * 方法：查询处置单位列表
	 */
	public List<Map<String, Object>> queryEPForCZ(){
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		List<Record> recordList = Db.find("select EP_ID,EP_NAME from WOBO_ENTERPRISE where IF_HANDLE = '1' and STATUS = '1'");
		map.put("id", "0");
		map.put("name", "请选择");
		list.add(map);
		if(recordList.size() > 0){
			map = new HashMap<String, Object>();
			for(Record record:recordList){
				map = new HashMap<String, Object>();
				map.put("id", record.get("EP_ID"));
				map.put("name", record.get("EP_NAME"));
				list.add(map);
			}
		}
		return list;
	}
	
	/**
	 * @author weizanting
	 * @date 20170306
	 * 方法：查询中转单位列表
	 */
	public List<Map<String, Object>> queryEPForZZ(String epId){
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		List<Record> recordList = Db.find("select EP_ID,EP_NAME from WOBO_ENTERPRISE where ORGTYPE = '0' and STATUS = '1' and EP_ID not in (?)", epId);
		map.put("id", "0");
		map.put("name", "请选择");
		list.add(map);
		if(recordList.size() > 0){
			map = new HashMap<String, Object>();
			for(Record record:recordList){
				map = new HashMap<String, Object>();
				map.put("id", record.get("EP_ID"));
				map.put("name", record.get("EP_NAME"));
				list.add(map);
			}
		}
		return list;
	}
	
	/**
	 * @author weizanting
	 * @date 20170619
	 * 方法：查询中转单位列表
	 */
	public List<Map<String, Object>> queryEP(String epId){
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		List<Record> recordList = Db.find("select a.EP_ID,a.EP_NAME from WOBO_ENTERPRISE a,(select EP_MAIN_ID from WOBO_EPRELATION where EP_SON_ID = ?) b where a.EP_ID = b.EP_MAIN_ID", epId);
		map.put("id", "0");
		map.put("name", "请选择");
		list.add(map);
		if(recordList.size() > 0){
			map = new HashMap<String, Object>();
			for(Record record:recordList){
				map = new HashMap<String, Object>();
				map.put("id", record.get("EP_ID"));
				map.put("name", record.get("EP_NAME"));
				list.add(map);
			}
		}
		return list;
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170526
	 * 方法：管理员查询医疗废物处置协议列表
	 */
	public Map<String, Object> queryAgreementListForAdmin(int pn, int ps, String area, String ROLEID, Object searchContent, Object statusValue, Object sepaValue, List<Object> statusCache){
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> resMap = new HashMap<String, Object>();
		String sql = "";
		if("SJSPROLE".equals(ROLEID)){		//SJSPROLE-市级管理员
			sql = "from (select a.AM_ID,a.EN_ID_CS,a.EN_NAME_CS,a.EN_ID_CZ,a.EN_NAME_CZ,a.EN_ID_ZZ,a.EN_NAME_ZZ,(case a.BEGINTIME when '' then '' else CONVERT(varchar(100), a.BEGINTIME, 23) end) as BEGINTIME,(case a.ENDTIME when '' then '' else CONVERT(varchar(100), a.ENDTIME, 23) end) as ENDTIME,a.STATUS,b.BELONGSEPA,a.ACTIONDATE from WOBO_AGREEMENT a, WOBO_ENTERPRISE b where a.STATUS in ('3', '4') and a.EN_ID_CS = b.EP_ID) as A where A.STATUS in ('3', '4')";
			if(searchContent != null && !"".equals(searchContent)){
				sql = sql + " and (A.AM_ID like '%"+searchContent+"%' or A.EN_NAME_CS like '%"+searchContent+"%' or A.EN_NAME_CZ like '%"+searchContent+"%' or A.EN_NAME_ZZ like '%"+searchContent+"%' or A.BEGINTIME like '%"+searchContent+"%' or A.ENDTIME like '%"+searchContent+"%')";
			}
			if(statusValue != null && !"".equals(statusValue)){
				sql = sql + " and A.STATUS in ("+statusValue+")";
			}
			if(sepaValue != null && !"".equals(sepaValue)){
				sql = sql + " and A.BELONGSEPA in ("+sepaValue+")";
			}
			sql = sql + " order by A.ACTIONDATE desc";
		}else{		//区级管理员
			sql = "from (select a.AM_ID,a.EN_ID_CS,a.EN_NAME_CS,a.EN_ID_CZ,a.EN_NAME_CZ,a.EN_ID_ZZ,a.EN_NAME_ZZ,(case a.BEGINTIME when '' then '' else CONVERT(varchar(100), a.BEGINTIME, 23) end) as BEGINTIME,(case a.ENDTIME when '' then '' else CONVERT(varchar(100), a.ENDTIME, 23) end) as ENDTIME,a.STATUS,b.BELONGSEPA,a.ACTIONDATE from WOBO_AGREEMENT a, WOBO_ENTERPRISE b where a.STATUS in ('3', '4') and a.EN_ID_CS = b.EP_ID and b.BELONGSEPA = '"+area+"') as A where A.STATUS in ('3', '4')";
			if(searchContent != null && !"".equals(searchContent)){
				sql = sql + " and (A.AM_ID like '%"+searchContent+"%' or A.EN_NAME_CS like '%"+searchContent+"%' or A.EN_NAME_CZ like '%"+searchContent+"%' or A.EN_NAME_ZZ like '%"+searchContent+"%' or A.BEGINTIME like '%"+searchContent+"%' or A.ENDTIME like '%"+searchContent+"%')";
			}
			if(statusValue != null && !"".equals(statusValue)){
				sql = sql + " and A.STATUS in ("+statusValue+")";
			}
			sql = sql + " order by A.ACTIONDATE desc";
		}
		Page<Record> page = Db.paginate(pn, ps, "select A.*", sql);
		List<Record> recordList = page.getList();
		Map<String, Object> map = null;
		if(recordList.size() > 0){
			for(Record record : recordList){
				map = new HashMap<String, Object>();
				map.put("AM_ID", record.get("AM_ID"));
				map.put("EN_ID_CS", record.get("EN_ID_CS"));
				map.put("EN_NAME_CS", record.get("EN_NAME_CS"));
				map.put("EN_ID_CZ", record.get("EN_ID_CZ"));
				map.put("EN_NAME_CZ", record.get("EN_NAME_CZ"));
				map.put("EN_ID_ZZ", record.get("EN_ID_ZZ"));
				map.put("EN_NAME_ZZ", record.get("EN_NAME_ZZ"));
				map.put("BEGINTIME", record.get("BEGINTIME"));
				map.put("ENDTIME", record.get("ENDTIME"));
				if(statusList != null){
					for(int i = 0; i < statusList.size(); i ++){
						if(record.get("STATUS").equals(statusList.get(i).get("dict_id"))){
							map.put("STATUS", statusList.get(i).get("dict_value"));
						}
					}
				}
				map.put("BELONG_SEPA", record.get("BELONGSEPA"));
				map.put("SEPA_NAME", convert(cityList, record.get("BELONGSEPA")) + "环保局");
				list.add(map);
			}
		}
		resMap.put("dataList", list);
		if(statusCache != null && !"".equals(statusCache)){
			resMap.put("statusList", super.queryDict("agreement_status", "value", "text", statusCache));
		}else{
			resMap.put("statusList", super.queryDict("agreement_status", "value", "text"));
		}
		if("SJSPROLE".equals(ROLEID)){
			resMap.put("sepaList", super.queryDict("city_q", "value", "text"));
		}
		resMap.put("totalPage", page.getTotalPage());
		resMap.put("totalRow", page.getTotalRow());
		return resMap;
	}
	
}
