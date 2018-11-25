package com.mine.rd.services.conduitTransfer.pojo;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.mine.pub.kit.DateKit;
import com.mine.pub.pojo.BaseDao;

public class ConduitTransferDao extends BaseDao {

	/**
	 * @author ouyangxu
	 * @date 20170508
	 * 方法：查询中转机构转移列表
	 */
	public Map<String, Object> queryTransferConduit(String epId, String action, int pn, int ps){
		String status = "";
		Page<Record> conduitsPage = null;
		List<Map<String, Object>> list = new ArrayList<>();
		List<Record> conduits = null;
		if("finished".equals(action)){
			status = "3";
		}else{
			status = "1";
		}
		String orgType = Db.findFirst("select ORGTYPE from WOBO_ENTERPRISE where EP_ID = ?", epId).get("ORGTYPE");
		if("1".equals(orgType)){
			conduitsPage = Db.paginate(pn, ps, "select b.EP_ID as EP_BIG_ID,b.EP_NAME as EP_BIG_NAME,a.WC_ID,a.PERSON_ID,a.PERSON_NAME,a.UNIT_NUM,a.TBDATE,a.M_PERSON_ID,a.M_PERSON_NAME", "from WOBO_CONDUITS_TRANSFER a,WOBO_ENTERPRISE b where a.EP_BIG_ID = b.EP_ID and a.EP_SMALL_ID = ? and a.STATUS = ? order by a.sysdate desc", epId, status);
			conduits = conduitsPage.getList();
		}
		if(conduits != null){
			for(Record record : conduits){
				Map<String, Object> map = new HashMap<>();
				map.put("EP_BIG_ID", record.get("EP_BIG_ID"));
				map.put("EP_BIG_NAME", record.get("EP_BIG_NAME"));
				map.put("WC_ID", record.get("WC_ID"));
				map.put("PERSON_ID", record.get("PERSON_ID"));
				map.put("PERSON_NAME", record.get("PERSON_NAME"));
				map.put("M_PERSON_ID", record.get("M_PERSON_ID"));
				map.put("M_PERSON_NAME", record.get("M_PERSON_NAME"));
				map.put("UNIT_NUM", record.get("UNIT_NUM"));
				if(record.get("TBDATE") != null && !"".equals(record.get("TBDATE"))){
					map.put("TBDATE", DateKit.toStr(record.getDate("TBDATE"), "yyyy-MM-dd"));
				}else{
					map.put("TBDATE", "");
				}
				if(record.get("ACCEPTDATE") != null && !"".equals(record.get("ACCEPTDATE"))){
					map.put("ACCEPTDATE", DateKit.toStr(record.getDate("ACCEPTDATE"), "yyyy-MM-dd"));
				}else{
					map.put("ACCEPTDATE", "");
				}
				list.add(map);
			}
		}
		Map<String, Object> returnMap = new HashMap<>();
		returnMap.put("transferList", list);
		if(conduitsPage == null){
			returnMap.put("totalRow", 0);
			returnMap.put("totalPage", 0);
		}else{
			returnMap.put("totalRow", conduitsPage.getTotalRow());
			returnMap.put("totalPage", conduitsPage.getTotalPage());
		}
		return returnMap;
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170508
	 * 方法：查询中转机构列表
	 */
	public List<Map<String, Object>> queryTransferEp(String epId){
		List<Record> transferOrgs = Db.find("select a.EP_ID,a.EP_NAME from WOBO_ENTERPRISE a,(select EP_MAIN_ID from WOBO_EPRELATION where EP_SON_ID = ?) b where a.EP_ID = b.EP_MAIN_ID", epId);
		List<Map<String, Object>> transferOrgList = new ArrayList<>();
		Map<String, Object> transferOrg = new HashMap<>();
		transferOrg.put("epId", "0");
		transferOrg.put("epName", "请选择中转机构");
		transferOrgList.add(transferOrg);
		if(transferOrgs != null){
			for(Record org : transferOrgs){
				transferOrg = new HashMap<>();
				transferOrg.put("epId", org.get("EP_ID"));
				transferOrg.put("epName", org.get("EP_NAME"));
				transferOrgList.add(transferOrg);
			}
		}
		return transferOrgList;
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170508
	 * 方法：查询中转申请信息
	 */
	public Map<String, Object> queryTransferConduitInfo(String WC_ID){
		Record record = Db.findFirst("select * from WOBO_CONDUITS_TRANSFER where WC_ID = ?", WC_ID);
		Map<String, Object> map = new HashMap<>();
		if(record != null){
			map.put("EP_BIG_ID", record.get("EP_BIG_ID"));
			map.put("EP_BIG_NAME", record.get("EP_BIG_NAME"));
			map.put("EP_SMALL_ID", record.get("EP_SMALL_ID"));
			map.put("EP_SMALL_NAME", record.get("EP_SMALL_NAME"));
			if(record.get("TBDATE") != null && !"".equals(record.get("TBDATE"))){
				map.put("TBDATE", DateKit.toStr(record.getDate("TBDATE"), "yyyy-MM-dd"));
			}else{
				map.put("TBDATE", "");
			}
			if(record.get("ACCEPTDATE") != null && !"".equals(record.get("ACCEPTDATE"))){
				map.put("ACCEPTDATE", DateKit.toStr(record.getDate("ACCEPTDATE"), "yyyy-MM-dd"));
			}else{
				map.put("ACCEPTDATE", "");
			}
			map.put("UNIT_NUM", record.get("UNIT_NUM"));
			map.put("UNIT", record.get("UNIT"));
			map.put("CONTENT", record.get("CONTENT"));
			map.put("PERSON_ID", record.get("PERSON_ID"));
			map.put("PERSON_NAME", record.get("PERSON_NAME"));
			map.put("M_PERSON_NAME", record.get("M_PERSON_NAME"));
		}
		return map;
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170508
	 * 方法：新增、修改中转申请
	 */
	public String saveTransferConduitInfo(Map<String, Object> transferConduitInfo){
		if(transferConduitInfo.get("WC_ID") != null && !"".equals(transferConduitInfo.get("WC_ID"))){
			int flag = Db.update("update WOBO_CONDUITS_TRANSFER set EP_BIG_ID=?,EP_BIG_NAME=?,TBDATE=?,UNIT_NUM=?,CONTENT=?,M_PERSON_ID=?,M_PERSON_NAME=? where WC_ID=?", transferConduitInfo.get("EP_BIG_ID"),transferConduitInfo.get("EP_BIG_NAME"),transferConduitInfo.get("TBDATE"),transferConduitInfo.get("UNIT_NUM"),transferConduitInfo.get("CONTENT"),transferConduitInfo.get("M_PERSON_ID"),transferConduitInfo.get("M_PERSON_NAME"),transferConduitInfo.get("WC_ID"));
			if(flag > 0){
				return transferConduitInfo.get("WC_ID").toString();
			}else{
				return "";
			}
		}else{
			Record record = new Record();
			String id = super.getSeqId("WOBO_CONDUITS_TRANSFER");
			record.set("WC_ID", id);
			record.set("EP_SMALL_ID", transferConduitInfo.get("EP_SMALL_ID"));
			record.set("EP_SMALL_NAME", transferConduitInfo.get("EP_SMALL_NAME"));
			record.set("EP_BIG_ID", transferConduitInfo.get("EP_BIG_ID"));
			record.set("EP_BIG_NAME", transferConduitInfo.get("EP_BIG_NAME"));
			record.set("TBDATE", transferConduitInfo.get("TBDATE"));
			record.set("UNIT_NUM", transferConduitInfo.get("UNIT_NUM"));
			record.set("UNIT", "公斤");
			record.set("CONTENT", transferConduitInfo.get("CONTENT"));
			record.set("M_PERSON_ID", transferConduitInfo.get("M_PERSON_ID"));
			record.set("M_PERSON_NAME", transferConduitInfo.get("M_PERSON_NAME"));
			record.set("STATUS", "1");
			record.set("sysdate", super.getSysdate());
			if(Db.save("WOBO_CONDUITS_TRANSFER", "WC_ID", record)){
				return id;
			}else{
				return "";
			}
		}
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170508
	 * 方法：校验交接员是否存在
	 */
	public boolean checkConnect(String epId, String personId){
		Record record = Db.findFirst("select * from WOBO_PERSON_CS where PI_ID = ? and EP_ID = ? and IFHANDORVE = '01'", personId, epId);
		if(record != null){
			return true;
		}else{
			record = Db.findFirst("select * from WOBO_PERSON_CS where USER_ID = ? and EP_ID = ? and IFADMIN = '01'", personId, epId);
			if(record != null){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170508
	 * 方法：交接中转申请
	 */
	public boolean dealTransferConduitInfo(String WC_ID, String PERSON_ID, String PERSON_NAME){
		return Db.update("update WOBO_CONDUITS_TRANSFER set STATUS='3',ACCEPTDATE=?,PERSON_ID=?,PERSON_NAME=? where WC_ID=?", this.getSysdate(),PERSON_ID,PERSON_NAME,WC_ID) > 0;
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170804
	 * 方法：校验是否有转移计划
	 */
	public boolean checkTransferPlan(String WC_ID){
		Record WC = Db.findFirst("select EP_SMALL_ID,EP_BIG_ID from WOBO_CONDUITS_TRANSFER where WC_ID = ? and STATUS = '3'", WC_ID);
		if(WC != null){
			String epIdCs = WC.get("EP_SMALL_ID");
			String epIdZz = WC.get("EP_BIG_ID");
			Timestamp date = super.getSysdate();
			Record plan = Db.findFirst("select * from WOBO_TRANSFER_PLAN where EN_ID_CS = ? and EN_ID_ZZ = ? and BEGINTIME <= ? and ENDTIME > ? and STATUS != '1'", epIdCs, epIdZz, date, date);
			if(plan == null){
				Record unsubmit = Db.findFirst("select * from WOBO_CONDUITS_TRANSFER_UNSUBMIT where EP_ID_CS = ? and EP_ID_ZZ = ?",epIdCs,epIdZz);
				if(unsubmit == null){
					Record conduit = new Record();
					conduit.set("EP_ID_CS", epIdCs);
					conduit.set("EP_ID_ZZ", epIdZz);
					return Db.save("WOBO_CONDUITS_TRANSFER_UNSUBMIT", conduit);
				}else{
					return true;
				}
			}else{
				return true;
			}
		}else{
			return false;
		}
		
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170510
	 * 方法：删除交接中转申请
	 */
	public boolean delTransferConduit(String WC_ID){
		return Db.update("update WOBO_CONDUITS_TRANSFER set STATUS='2' where WC_ID=?", WC_ID) > 0;
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170508
	 * 方法：查询中转机构转移列表
	 */
	public Map<String, Object> queryApplyTransferConduitList(String epId, int pn, int ps, Object searchContent, Object statusValue, List<Object> statusCache){
		String sql = "";
		Page<Record> conduitsPage = null;
		Map<String, Object> returnMap = new HashMap<>();
		String orgType = Db.findFirst("select ORGTYPE from WOBO_ENTERPRISE where EP_ID = ?", epId).get("ORGTYPE");
		if(searchContent != null && !"".equals(searchContent)){
//			sql = sql + " and (EP_BIG_NAME like '%"+searchContent+"%' or EP_SMALL_NAME like '%"+searchContent+"%' or WC_ID like '%"+searchContent+"%' or PERSON_NAME like '%"+searchContent+"%' or M_PERSON_NAME like '%"+searchContent+"%' or UNIT_NUM like '%"+searchContent+"%' or TBDATE like '%"+searchContent+"%')";
			sql = sql + " and (EP_BIG_NAME like '%"+searchContent+"%' or EP_SMALL_NAME like '%"+searchContent+"%' or WC_ID like '%"+searchContent+"%' or PERSON_NAME like '%"+searchContent+"%' or M_PERSON_NAME like '%"+searchContent+"%' or UNIT_NUM like '%"+searchContent+"%' or CONVERT(varchar,TBDATE,101) like '%"+searchContent+"%')";
		}
		if(statusValue != null && !"".equals(statusValue)){
			sql = sql + " and a.STATUS in ("+statusValue+")";
		}
		if("0".equals(orgType)){
//			conduitsPage = Db.paginate(pn, ps, "select EP_SMALL_ID,EP_SMALL_NAME,WC_ID,M_PERSON_ID,M_PERSON_NAME,UNIT_NUM,TBDATE,STATUS ", "from WOBO_CONDUITS_TRANSFER where EP_BIG_ID = ? and STATUS in ('1','3') " + sql + " order by sysdate desc", epId);
			conduitsPage = Db.paginate(pn, ps, "select b.EP_ID EP_SMALL_ID,b.EP_NAME EP_SMALL_NAME,a.WC_ID,a.M_PERSON_ID,a.M_PERSON_NAME,a.UNIT_NUM,a.TBDATE,a.STATUS,a.EP_BIG_NAME,a.PERSON_NAME ", "from WOBO_CONDUITS_TRANSFER a,WOBO_ENTERPRISE b where a.EP_SMALL_ID = b.EP_ID and a.EP_BIG_ID = ? and a.STATUS in ('1','3') " + sql + " order by a.sysdate desc", epId);
			returnMap.put("btnFlag", true);
		}else{
//			conduitsPage = Db.paginate(pn, ps, "select EP_SMALL_ID,EP_SMALL_NAME,WC_ID,M_PERSON_ID,M_PERSON_NAME,UNIT_NUM,TBDATE,STATUS ", "from WOBO_CONDUITS_TRANSFER where EP_SMALL_ID = ? and STATUS in ('1','3') " + sql + " order by sysdate desc", epId);
			conduitsPage = Db.paginate(pn, ps, "select b.EP_ID EP_SMALL_ID,b.EP_NAME EP_SMALL_NAME,a.WC_ID,a.M_PERSON_ID,a.M_PERSON_NAME,a.UNIT_NUM,a.TBDATE,a.STATUS,a.EP_BIG_NAME,a.PERSON_NAME ", "from WOBO_CONDUITS_TRANSFER a,WOBO_ENTERPRISE b where a.EP_SMALL_ID = b.EP_ID and a.EP_SMALL_ID = ? and a.STATUS in ('1','3') " + sql + " order by a.sysdate desc", epId);
			returnMap.put("btnFlag", false);
		}
//		Page<Record> conduitsPage = Db.paginate(pn, ps, "select EP_SMALL_ID,EP_SMALL_NAME,WC_ID,M_PERSON_ID,M_PERSON_NAME,UNIT_NUM,TBDATE,STATUS ", "from WOBO_CONDUITS_TRANSFER where EP_BIG_ID = ? and STATUS in ('1','3') " + sql + " order by sysdate desc", epId);
		List<Map<String, Object>> list = new ArrayList<>();
		List<Record> conduits = conduitsPage.getList();
		if(conduits != null){
			for(Record record : conduits){
				Map<String, Object> map = new HashMap<>();
				map.put("EP_SMALL_ID", record.get("EP_SMALL_ID"));
				map.put("EP_SMALL_NAME", record.get("EP_SMALL_NAME"));
				map.put("WC_ID", record.get("WC_ID"));
				map.put("M_PERSON_ID", record.get("M_PERSON_ID"));
				map.put("M_PERSON_NAME", record.get("M_PERSON_NAME"));
				map.put("UNIT_NUM", record.get("UNIT_NUM"));
				map.put("EP_BIG_NAME", record.get("EP_BIG_NAME"));
				map.put("PERSON_NAME", record.get("PERSON_NAME"));
				if(record.get("TBDATE") != null && !"".equals(record.get("TBDATE"))){
					map.put("TBDATE", DateKit.toStr(record.getDate("TBDATE"), "yyyy-MM-dd"));
				}else{
					map.put("TBDATE", "");
				}
				if("1".equals(record.get("STATUS"))){
					map.put("STATUS", "待交接");
				}else{
					map.put("STATUS", "交接完成");
				}
				list.add(map);
			}
		}
		returnMap.put("transferList", list);
		returnMap.put("statusList", queryDict("conduits_status", "value", "text", statusCache));
		returnMap.put("totalRow", conduitsPage.getTotalRow());
		returnMap.put("totalPage", conduitsPage.getTotalPage());
		return returnMap;
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170629
	 * 方法：查询中转单位交接员信息
	 */
	public Map<String, Object> checkTransferPersonInfo(String roleType, String personId, String epId){
		String sql = "select a.PI_ID,a.NAME,b.EP_ID,b.EP_NAME from WOBO_PERSON_CS a, WOBO_ENTERPRISE b where a.PI_ID = ? and a.EP_ID = ? and a.EP_ID = b.EP_ID and b.STATUS = '1'";
		if("3".equals(roleType)){
			sql = "select a.USER_ID PI_ID,a.NAME,c.EP_ID,c.EP_NAME from WOBO_PERSON_CS a, WOBO_USER b, WOBO_ENTERPRISE c where a.USER_ID = ? and a.EP_ID = ? and a.USER_ID = b.USER_ID and b.STATUS = '1' and a.EP_ID = c.EP_ID and c.STATUS = '1'";
		}
		Record record = Db.findFirst(sql, personId, epId);
		if(record != null){
			Map<String, Object> map = new HashMap<>();
			map.put("CS_PERSON_ID", record.get("PI_ID"));
			map.put("CS_PERSON_NAME", record.get("NAME"));
			map.put("EN_ID_CS", record.get("EP_ID"));
			map.put("EN_NAME_CS", record.get("EP_NAME"));
			return map;
		}
		return null;
	}
}
