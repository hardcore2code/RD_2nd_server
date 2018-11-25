package com.mine.rd.services.transfer.pojo;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.ehcache.CacheKit;
import com.mine.pub.pojo.BaseDao;

public class TransferDao extends BaseDao {

	//查询状态列表
	List<Record> statusList = CacheKit.get("mydict", "transfer_status");
	List<Record> boxSuttle = CacheKit.get("mydict", "box_suttle");
	
	/**
	 * @author weizanting
	 * @date 20170331
	 * 方法：查询医疗废物运程列表
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> queryHaulList(String epId, int pn,int ps, Object searchContent, Object statusValue, List<Object> statusCache){
		String sql = "from (select TG_ID,CZ_DRIVER_ID,CZ_DRIVER_NAME,PLATE_NUMBER,CI_ID,isnull(cast(UNIT_NUM as decimal(9,2)), 0.00) UNIT_NUM,STATUS,(case BEGINTIME when '' then '' else CONVERT(varchar(100), BEGINTIME, 120) end) as BEGINTIME,(case ENDTIME when '' then '' else CONVERT(varchar(100), ENDTIME, 120) end) as ENDTIME,ACTIONDATE,EN_NAME_CZ from WOBO_TRANSFER_BIG where STATUS <> '2' and EN_ID_CZ =  ?) as A where A.STATUS <> '2'";
		if(searchContent != null && !"".equals(searchContent)){
			sql = sql + " and (A.TG_ID like '%"+searchContent+"%' or A.CZ_DRIVER_NAME like '%"+searchContent+"%' or A.PLATE_NUMBER like '%"+searchContent+"%' or A.BEGINTIME like '%"+searchContent+"%' or A.ENDTIME like '%"+searchContent+"%' or A.UNIT_NUM like '%"+searchContent+"%')";
		}
		if(statusValue != null && !"".equals(statusValue)){
			sql = sql + " and A.STATUS in ("+statusValue+")";
		}
		Page<Record> page = Db.paginate(pn, ps, "select *", sql + " order by A.ACTIONDATE desc", epId);
		List<Record> recordList = page.getList();
//		List<Record> recordList = Db.find("select * from WOBO_TRANSFER_BIG where STATUS <> '2' and EN_ID_CZ = ?", epId);
		Map<String, Object> map = null;
		Map<String, Object> resMap = new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if(recordList.size() > 0){
			for(Record record : recordList){
				map = new HashMap<String, Object>();
				map.put("TG_ID", record.get("TG_ID"));
				map.put("CZ_DRIVER_ID", record.get("CZ_DRIVER_ID"));
				map.put("CZ_DRIVER_NAME", record.get("CZ_DRIVER_NAME"));
				map.put("EN_NAME_CZ", record.get("EN_NAME_CZ"));
				map.put("CI_ID", record.get("CI_ID"));
				map.put("PLATE_NUMBER", record.get("PLATE_NUMBER"));
				map.put("BEGINTIME", record.get("BEGINTIME"));
				map.put("ENDTIME", record.get("ENDTIME"));
				Map<String, Object> car = getCarInfo(record.get("CI_ID"), record.get("PLATE_NUMBER"), record.get("UNIT_NUM"), record.get("TG_ID"));
				map.putAll(car);
				if(statusList != null){
					for(int i = 0; i < statusList.size(); i ++){
						if(record.get("STATUS").equals(statusList.get(i).get("dict_id"))){
							map.put("STATUSNAME", statusList.get(i).get("dict_value"));
						}
					}
				}
				map.put("STATUS", record.get("STATUS"));
				Map<String, Object> m = queryBillList(record.get("TG_ID").toString(), 1, 10, "", "", null);
				int size = ((List<Map<String, Object>>)m.get("billList")).size();
				if(size > 0){
					map.put("ifExist", "0");
				}else{
					map.put("ifExist", "1");
				}
				list.add(map);
			}
		}
		resMap.put("haulList", list);
		if(statusCache != null && !"".equals(statusCache)){
			resMap.put("statusList", super.queryDict("transfer_status", "value", "text", statusCache));
		}else{
			resMap.put("statusList", super.queryDict("transfer_status", "value", "text"));
		}
		resMap.put("totalPage", page.getTotalPage());
		resMap.put("totalRow", page.getTotalRow());
		return resMap;
	}
	
	/**
	 * @author weizanting
	 * @date 20170628
	 * 方法：查询医疗废物运程列表
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> queryHaulList(String epId, int pn,int ps, Object searchContent, Object statusValue, List<Object> statusCache, String userId){
		String sql = "from (select TG_ID,CZ_DRIVER_ID,CZ_DRIVER_NAME,PLATE_NUMBER,CI_ID,isnull(cast(UNIT_NUM as decimal(9,2)), 0.00) UNIT_NUM,STATUS,(case BEGINTIME when '' then '' else CONVERT(varchar(100), BEGINTIME, 120) end) as BEGINTIME,(case ENDTIME when '' then '' else CONVERT(varchar(100), ENDTIME, 120) end) as ENDTIME,ACTIONDATE,EN_NAME_CZ,BOX_NUM from WOBO_TRANSFER_BIG where STATUS in ('4', '5') and EN_ID_CZ =  ?) as A where A.CZ_DRIVER_ID = '" + userId + "'";
		if(searchContent != null && !"".equals(searchContent)){
			sql = sql + " and (A.TG_ID like '%"+searchContent+"%' or A.CZ_DRIVER_NAME like '%"+searchContent+"%' or A.PLATE_NUMBER like '%"+searchContent+"%' or A.BEGINTIME like '%"+searchContent+"%' or A.ENDTIME like '%"+searchContent+"%' or A.UNIT_NUM like '%"+searchContent+"%')";
		}
		if(statusValue != null && !"".equals(statusValue)){
			sql = sql + " and A.STATUS in ("+statusValue+")";
		}
		Page<Record> page = Db.paginate(pn, ps, "select *", sql + " order by A.ACTIONDATE desc", epId);
		List<Record> recordList = page.getList();
//		List<Record> recordList = Db.find("select * from WOBO_TRANSFER_BIG where STATUS <> '2' and EN_ID_CZ = ?", epId);
		Map<String, Object> map = null;
		Map<String, Object> resMap = new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if(recordList.size() > 0){
			for(Record record : recordList){
				map = new HashMap<String, Object>();
				map.put("TG_ID", record.get("TG_ID"));
				map.put("CZ_DRIVER_ID", record.get("CZ_DRIVER_ID"));
				map.put("CZ_DRIVER_NAME", record.get("CZ_DRIVER_NAME"));
				map.put("EN_NAME_CZ", record.get("EN_NAME_CZ"));
				map.put("CI_ID", record.get("CI_ID"));
				map.put("PLATE_NUMBER", record.get("PLATE_NUMBER"));
				map.put("BOX_NUM", record.get("BOX_NUM") == null ? "0" : record.get("BOX_NUM"));
				map.put("BOX_SUTTLE", queryBoxSuttle());
				map.put("WASTE_SUTTLE", record.get("UNIT_NUM"));
				map.put("BEGINTIME", record.get("BEGINTIME"));
				map.put("ENDTIME", record.get("ENDTIME"));
				Map<String, Object> car = getCarInfo(record.get("CI_ID"), record.get("PLATE_NUMBER"), record.get("UNIT_NUM"), record.get("TG_ID"));
				map.putAll(car);
				if(statusList != null){
					for(int i = 0; i < statusList.size(); i ++){
						if(record.get("STATUS").equals(statusList.get(i).get("dict_id"))){
							map.put("STATUSNAME", statusList.get(i).get("dict_value"));
						}
					}
				}
				map.put("STATUS", record.get("STATUS"));
				Map<String, Object> m = queryBillList(record.get("TG_ID").toString(), 1, 10, "", "", null);
				int size = ((List<Map<String, Object>>)m.get("billList")).size();
				if(size > 0){
					map.put("ifExist", "0");
				}else{
					map.put("ifExist", "1");
				}
				list.add(map);
			}
		}
		resMap.put("haulList", list);
		if(statusCache != null && !"".equals(statusCache)){
			resMap.put("statusList", super.queryDict("transfer_status", "value", "text", statusCache));
		}else{
			resMap.put("statusList", super.queryDict("transfer_status", "value", "text"));
		}
		resMap.put("totalPage", page.getTotalPage());
		resMap.put("totalRow", page.getTotalRow());
		return resMap;
	}
	
	/**
	 * @author weizanting
	 * @date 20170526
	 * 方法：获取车辆信息
	 */
	public Map<String, Object> getCarInfo(Object ciId, Object plateNumber, Object unitNum, Object tgId){
		String status = Db.findFirst("select STATUS from WOBO_TRANSFER_BIG where TG_ID = ?", tgId).get("STATUS").toString();
		Map<String, Object> map = new HashMap<String, Object>();
		Record car = new Record();
		if(ciId != null && plateNumber != null){
			car= Db.findFirst("select TARE from WOBO_CAR where CI_ID=? and PLATE_NUMBER=?", ciId, plateNumber);
		}
		
		float weight = 0;
		float car_weight = 0;
		float box_weight = getBoxWeight(tgId);
		if(car != null){
			car_weight = Float.parseFloat(car.get("TARE").toString());
		}
		if(unitNum != null){
			if(Float.parseFloat(unitNum.toString()) > 0 && car_weight > 0){
				weight = Float.parseFloat(unitNum.toString()) + car_weight + box_weight;
			}
		}
		if("4".equals(status) || "5".equals(status)){
			map.put("TOTAL_WEIGHT", weight);
			map.put("WEIGHT", unitNum);
		}else{
			map.put("TOTAL_WEIGHT", 0);
			map.put("WEIGHT", 0);
		}
		map.put("TARE", car_weight);
		return map;
	}
	
	/**
	 * @author weizanting
	 * @date 20170526
	 * 方法：获取车辆信息
	 */
	public float getBoxWeight(Object tgId){
		float box_weight = 0;
		int box_num = 0;
		Record record = Db.findFirst("select BOX_NUM from WOBO_TRANSFER_BIG where TG_ID = ?", tgId);
		if(record != null && record.get("BOX_NUM") != null && !"".equals(record.get("BOX_NUM"))){
			box_num = Integer.parseInt(record.get("BOX_NUM").toString());
		}
		String str = queryBoxSuttle();
		if(!"".equals(str)){
			box_weight = Float.parseFloat(str) * box_num;
		}
		return box_weight;
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170503
	 * 方法：管理员查询医疗废物运程列表
	 */
	public Map<String, Object> queryHaulListForAdmin(String ROLEID, String area, int pn,int ps, Object searchContent, Object statusValue, List<Object> statusCache){
		String sql = "";
		if("SJSPROLE".equals(ROLEID)){		//SJSPROLE-市级管理员
			sql = "from (select a.TG_ID,a.CZ_DRIVER_ID,a.CZ_DRIVER_NAME,a.PLATE_NUMBER,a.CI_ID,isnull(cast(a.UNIT_NUM as decimal(9,2)), 0.00) UNIT_NUM,a.STATUS,(case a.BEGINTIME when '' then '' else CONVERT(varchar(100), a.BEGINTIME, 120) end) as BEGINTIME,(case a.ENDTIME when '' then '' else CONVERT(varchar(100), a.ENDTIME, 120) end) as ENDTIME,a.ACTIONDATE,a.EN_NAME_CZ from WOBO_TRANSFER_BIG a where a.STATUS <> '2') as A where A.STATUS <> '2'";
			if(searchContent != null && !"".equals(searchContent)){
				sql = sql + " and (A.TG_ID like '%"+searchContent+"%' or A.EN_NAME_CZ like '%" + searchContent + "%' or A.CZ_DRIVER_NAME like '%"+searchContent+"%' or A.PLATE_NUMBER like '%"+searchContent+"%' or A.BEGINTIME like '%"+searchContent+"%' or A.ENDTIME like '%"+searchContent+"%' or A.UNIT_NUM like '%"+searchContent+"%')";
			}
			if(statusValue != null && !"".equals(statusValue)){
				sql = sql + " and A.STATUS in ("+statusValue+")";
			}
		}else{		//区级管理员
			sql = "from (select a.TG_ID,a.CZ_DRIVER_ID,a.CZ_DRIVER_NAME,a.PLATE_NUMBER,a.CI_ID,isnull(cast(a.UNIT_NUM as decimal(9,2)), 0.00) UNIT_NUM,a.STATUS,(case a.BEGINTIME when '' then '' else CONVERT(varchar(100), a.BEGINTIME, 120) end) as BEGINTIME,(case a.ENDTIME when '' then '' else CONVERT(varchar(100), a.ENDTIME, 120) end) as ENDTIME,a.ACTIONDATE,a.EN_NAME_CZ from WOBO_TRANSFER_BIG a, WOBO_ENTERPRISE b where a.STATUS <> '2' and a.EN_ID_CZ = b.EP_ID and b.BELONGSEPA = '"+area+"') as A where A.STATUS <> '2'";
			if(searchContent != null && !"".equals(searchContent)){
				sql = sql + " and (A.TG_ID like '%"+searchContent+"%' or A.EN_NAME_CZ like '%" + searchContent + "%' or A.CZ_DRIVER_NAME like '%"+searchContent+"%' or A.PLATE_NUMBER like '%"+searchContent+"%' or A.BEGINTIME like '%"+searchContent+"%' or A.ENDTIME like '%"+searchContent+"%' or A.UNIT_NUM like '%"+searchContent+"%')";
			}
			if(statusValue != null && !"".equals(statusValue)){
				sql = sql + " and A.STATUS in ("+statusValue+")";
			}
		}
		Page<Record> page = Db.paginate(pn, ps, "select *", sql + " order by A.ACTIONDATE desc");
		List<Record> recordList = page.getList();
//		List<Record> recordList = Db.find("select * from WOBO_TRANSFER_BIG where STATUS <> '2' and EN_ID_CZ = ?", epId);
		Map<String, Object> map = null;
		Map<String, Object> resMap = new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if(recordList.size() > 0){
			for(Record record : recordList){
				map = new HashMap<String, Object>();
				map.put("TG_ID", record.get("TG_ID"));
				map.put("CZ_DRIVER_ID", record.get("CZ_DRIVER_ID"));
				map.put("CZ_DRIVER_NAME", record.get("CZ_DRIVER_NAME"));
				map.put("EN_NAME_CZ", record.get("EN_NAME_CZ"));
				map.put("PLATE_NUMBER", record.get("PLATE_NUMBER"));
				map.put("BEGINTIME", record.get("BEGINTIME"));
				map.put("ENDTIME", record.get("ENDTIME"));
				/*Record car = new Record();
				if(record.get("CI_ID") != null && record.get("PLATE_NUMBER") != null){
					car= Db.findFirst("select TARE from WOBO_CAR where CI_ID=? and PLATE_NUMBER=?", record.get("CI_ID").toString(), record.get("PLATE_NUMBER").toString());
				}
				float weight = 0;
				float car_weight = 0;
				if(car != null){
					car_weight = Float.parseFloat(car.get("TARE").toString());
				}
				if(Float.parseFloat(record.get("UNIT_NUM").toString()) > 0 && car_weight > 0){
					weight = Float.parseFloat(record.get("UNIT_NUM").toString()) - car_weight;
				}
				if("4".equals(record.get("STATUS")) || "5".equals(record.get("STATUS"))){
					map.put("TOTAL_WEIGHT", record.get("UNIT_NUM"));
					map.put("WEIGHT", weight);
				}else{
					map.put("TOTAL_WEIGHT", 0);
					map.put("WEIGHT", 0);
				}*/
				Map<String, Object> car = getCarInfo(record.get("CI_ID"), record.get("PLATE_NUMBER"), record.get("UNIT_NUM"), record.get("TG_ID"));
				map.putAll(car);
				if(statusList != null){
					for(int i = 0; i < statusList.size(); i ++){
						if(record.get("STATUS").equals(statusList.get(i).get("dict_id"))){
							map.put("STATUSNAME", statusList.get(i).get("dict_value"));
						}
					}
				}
				map.put("STATUS", record.get("STATUS"));
				list.add(map);
			}
		}
		resMap.put("haulList", list);
		if(statusCache != null && !"".equals(statusCache)){
			resMap.put("statusList", super.queryDict("transfer_status", "value", "text", statusCache));
		}else{
			resMap.put("statusList", super.queryDict("transfer_status", "value", "text"));
		}
		resMap.put("totalPage", page.getTotalPage());
		resMap.put("totalRow", page.getTotalRow());
		return resMap;
	}
	
	/**
	 * @author weizanting
	 * @date 20170331
	 * 方法：保存医疗废物运程
	 */
	public String saveHaul(Map<String, Object> map){
		Record record = new Record();
		String tgId = super.getSeqId("WOBO_TRANSFER_BIG");
		record.set("TG_ID", tgId);
		record.set("EN_ID_CZ", map.get("EN_ID_CZ"));
		record.set("EN_NAME_CZ", map.get("EN_NAME_CZ"));
		record.set("CZ_DRIVER_ID", map.get("CZ_DRIVER_ID"));
		record.set("CZ_DRIVER_NAME", map.get("CZ_DRIVER_NAME"));
		record.set("CI_ID", map.get("CI_ID"));
		record.set("PLATE_NUMBER", map.get("PLATE_NUMBER"));
		if(map.get("NUMBER") != null && !"".equals(map.get("NUMBER"))){
			record.set("BOX_NUM", map.get("NUMBER"));
		}else{
			record.set("BOX_NUM", "0");
		}
		record.set("STATUS", "3");
		Timestamp time = super.getSysdate();
		record.set("ACTIONDATE", time);
		record.set("BEGINTIME", time);
		record.set("sysdate", time);
		if(Db.save("WOBO_TRANSFER_BIG", "TG_ID", record)){
			return tgId;
		}
		return "";
	}
	
	/**
	 * @author weizanting
	 * @date 20170331
	 * 方法：查询医疗废物运单列表
	 */
	public Map<String, Object> queryBillList(String TG_ID, int pn, int ps, Object searchContent, Object statusContent, List<Object> statusCache){
		String sql = "from (select TG_ID, TB_ID, EN_ID_CS, EN_NAME_CS, (case BEGINTIME when '' then '' else CONVERT(varchar(100), BEGINTIME, 120) end) as BEGINTIME,(case ENDTIME when '' then '' else CONVERT(varchar(100), ENDTIME, 120) end) as ENDTIME, STATUS, ACTIONDATE from WOBO_TRANSFER_BILL where STATUS <> '2' and TG_ID = ?) as A where A.STATUS <> '2'";
		if(searchContent != null && !"".equals(searchContent)){
			sql = sql + " and (A.TB_ID like '%"+searchContent+"%' or A.EN_NAME_CS like '%"+searchContent+"%' or A.BEGINTIME like '%"+searchContent+"%' or A.ENDTIME like '%"+searchContent+"%')";
		}
		if(statusContent != null && !"".equals(statusContent)){
			sql = sql + " and A.STATUS in ("+statusContent+")";
		}
		Page<Record> page = Db.paginate(pn, ps, "select A.*", sql + " order by A.ACTIONDATE desc", TG_ID);
		List<Record> recordList = page.getList();
//		List<Record> recordList = Db.find("select * from WOBO_TRANSFER_BILL where STATUS <> '2' and TG_ID = ?", TG_ID);
		Map<String, Object> map = null;
		Map<String, Object> resMap = new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if(recordList.size() > 0){
			for(Record record : recordList){
				map = new HashMap<String, Object>();
				map.put("TG_ID", record.get("TG_ID"));
				map.put("TB_ID", record.get("TB_ID"));
				map.put("EN_ID_CS", record.get("EN_ID_CS"));
				map.put("EN_NAME_CS", record.get("EN_NAME_CS"));
				map.put("BEGINTIME", record.get("BEGINTIME"));
				map.put("ENDTIME", record.get("ENDTIME"));
				if(statusList != null){
					for(int i = 0; i < statusList.size(); i ++){
						if(record.get("STATUS").equals(statusList.get(i).get("dict_id"))){
							map.put("STATUSNAME", statusList.get(i).get("dict_value"));
						}
					}
				}
				map.put("STATUS", record.get("STATUS"));
				list.add(map);
			}
		}
		resMap.put("billList", list);
		if(statusCache != null && !"".equals(statusCache)){
			resMap.put("statusList", super.queryDict("transfer_status", "value", "text", statusCache));
		}else{
			resMap.put("statusList", super.queryDict("transfer_status", "value", "text"));
		}
		resMap.put("totalPage", page.getTotalPage());
		resMap.put("totalRow", page.getTotalRow());
		return resMap;
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170525
	 * 方法：产生单位查询医疗废物运单列表
	 */
	public Map<String, Object> queryBillListForEpCs(String EP_ID, int pn, int ps, Object searchContent, Object statusValue, List<Object> statusCache, Object belongSepa){
		String sql = "from (select TG_ID,TB_ID,EN_ID_CS,EN_NAME_CS,CONVERT(varchar(100), BEGINTIME, 120) BEGINTIME,CONVERT(varchar(100), ENDTIME, 120) ENDTIME,STATUS,ACTIONDATE from WOBO_TRANSFER_BILL where STATUS <> '2') as A where A.EN_ID_CS = '"+EP_ID+"'";
		if(belongSepa != null && !"".equals(belongSepa)){
			sql = "from (select a.TG_ID,a.TB_ID,a.EN_ID_CS,a.EN_NAME_CS,CONVERT(varchar(100), a.BEGINTIME, 120) BEGINTIME,CONVERT(varchar(100), a.ENDTIME, 120) ENDTIME,a.STATUS,a.ACTIONDATE from WOBO_TRANSFER_BILL a, WOBO_ENTERPRISE b where a.STATUS <> '2' and a.EN_ID_CS = b.EP_ID and b.BELONGSEPA = '"+belongSepa+"') as A where A.STATUS <> '2'";
		}
		if(searchContent != null && !"".equals(searchContent)){
			sql = sql + " and (A.TG_ID like '%"+searchContent+"%' or A.TB_ID like '%"+searchContent+"%' or A.EN_ID_CS like '%"+searchContent+"%' or A.EN_NAME_CS like '%"+searchContent+"%' or A.BEGINTIME like '%" + searchContent + "%' or A.ENDTIME like '%" + searchContent + "%')";
		}
		if(statusValue != null && !"".equals(statusValue)){
			sql = sql + " and A.STATUS in ("+statusValue+")";
		}
		Page<Record> page = Db.paginate(pn, ps, "select * ", sql + " order by A.ACTIONDATE desc");
		List<Record> recordList = page.getList();
		Map<String, Object> map = null;
		Map<String, Object> resMap = new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if(recordList.size() > 0){
			for(Record record : recordList){
				map = new HashMap<String, Object>();
				map.put("TG_ID", record.get("TG_ID"));
				map.put("TB_ID", record.get("TB_ID"));
				map.put("EN_ID_CS", record.get("EN_ID_CS"));
				map.put("EN_NAME_CS", record.get("EN_NAME_CS"));
				map.put("BEGINTIME", record.get("BEGINTIME"));
				if(record.get("ENDTIME") != null){
					map.put("ENDTIME", record.get("ENDTIME"));
				}else{
					map.put("ENDTIME", "");
				}
				if(statusList != null){
					for(int i = 0; i < statusList.size(); i ++){
						if(record.get("STATUS").equals(statusList.get(i).get("dict_id"))){
							map.put("STATUSNAME", statusList.get(i).get("dict_value"));
						}
					}
				}
				map.put("STATUS", record.get("STATUS"));
				list.add(map);
			}
		}
		resMap.put("billList", list);
		if(statusCache != null && !"".equals(statusCache)){
			resMap.put("statusList", super.queryDict("transfer_status", "value", "text", statusCache));
		}else{
			resMap.put("statusList", super.queryDict("transfer_status", "value", "text"));
		}
		resMap.put("totalPage", page.getTotalPage());
		resMap.put("totalRow", page.getTotalRow());
		return resMap;
	}
	
	/**
	 * @author weizanting
	 * @date 20170331
	 * 方法：查询医疗废物运单
	 */
	public Map<String, Object> queryBill(String TB_ID){
		Record record = Db.findFirst("select a.*,b.EN_NAME_CZ,b.CZ_DRIVER_NAME from WOBO_TRANSFER_BILL a, WOBO_TRANSFER_BIG b where a.STATUS <> '2' and a.TB_ID = ? and a.TG_ID = b.TG_ID", TB_ID);
		Map<String, Object> map = new HashMap<String, Object>();
		if(record !=null){
			map.put("EN_ID_CS", record.get("EN_ID_CS"));
			map.put("EN_NAME_CS", record.get("EN_NAME_CS"));
			map.put("CS_PERSON_ID", record.get("CS_PERSON_ID"));
			map.put("CS_PERSON_NAME", record.get("CS_PERSON_NAME"));
			map.put("EN_NAME_CZ", record.get("EN_NAME_CZ"));
			map.put("CZ_DRIVER_NAME", record.get("CZ_DRIVER_NAME"));
			map.put("COUNT", record.get("COUNT"));
			map.put("weight", record.get("UNIT_NUM"));
			map.put("boxList", queryBoxList(TB_ID));
		}
		return map;
	}
	
	/**
	 * @author weizanting
	 * @date 20170405
	 * 方法：查询医疗废物运单中的箱子列表
	 */
	public List<Map<String, Object>> queryBoxList(String TB_ID){
		List<Record> recordList = Db.find("select * from WOBO_TRANSFER_BILL_LIST where STATUS <> '2' and TB_ID = ?", TB_ID);
		Map<String, Object> map = null;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if(recordList.size() > 0){
			for(Record record: recordList){
				map = new HashMap<String, Object>();
				map.put("BOX_ID", record.get("BOX_ID"));
				map.put("STATUS", record.get("STATUS"));
				List<Record> cityList = CacheKit.get("mydict", "transfer_status");
				map.put("STATUSNAME", super.convert(cityList, record.get("STATUS")));
				list.add(map);
			}
		}
		return list;
	}
	
	/**
	 * @author weizanting
	 * @date 20170405
	 * 方法：判断该行程是否已存在该医院
	 */
	public Record ifMerge(Map<String, Object> map){
		return Db.findFirst("select * from WOBO_TRANSFER_BILL where TG_ID = ? and EN_ID_CS = ?", map.get("TG_ID"), map.get("EN_ID_CS"));
	}
	
	/**
	 * @author weizanting
	 * @date 20170405
	 * 方法：保存医疗废物运单
	 */
	@SuppressWarnings("unchecked")
	public int saveBill(Map<String, Object> map){
		Record bill = new Record();
		int COUNT = 0;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String TB_ID = super.getSeqId("WOBO_TRANSFER_BILL");
		bill.set("TB_ID", TB_ID);
		bill.set("TG_ID", map.get("TG_ID"));
		bill.set("EN_ID_CS", map.get("EN_ID_CS"));
		bill.set("EN_NAME_CS", map.get("EN_NAME_CS"));
		bill.set("CS_PERSON_ID", map.get("CS_PERSON_ID"));
		bill.set("CS_PERSON_NAME", map.get("CS_PERSON_NAME"));
		bill.set("STATUS", "3");
		Timestamp time = super.getSysdate();
		bill.set("ACTIONDATE", time);
		bill.set("BEGINTIME", time);
		bill.set("sysdate", time);
		bill.set("COUNT", map.get("COUNT"));
		bill.set("UNIT_NUM", map.get("WEIGHT"));
		bill.set("UNIT", "公斤");
		boolean flag = Db.save("WOBO_TRANSFER_BILL", "TB_ID", bill);
		if(flag){
			list = (List<Map<String, Object>>) map.get("boxList");
			if(list.size() > 0){
				for(Map<String, Object> box : list){
					Record boxs = Db.findFirst("select * from WOBO_TRANSFER_BILL_LIST where BOX_ID = ? and TG_ID = ?", box.get("BOX_ID"), map.get("TG_ID"));
					if(boxs == null){
						Record record = new Record();
						record.set("ID", super.getSeqId("WOBO_TRANSFER_BILL_LIST"));
						record.set("TB_ID", TB_ID);
						record.set("TG_ID", map.get("TG_ID"));
						record.set("BOX_ID", box.get("BOX_ID"));
						record.set("BEGINTIME", super.getSysdate());
						record.set("ACTIONDATE", super.getSysdate());
						record.set("STATUS", "3");
						record.set("sysdate", super.getSysdate());
						flag = Db.save("WOBO_TRANSFER_BILL_LIST", "ID", record);
						if(!flag){
							return -1;
						}
						COUNT = COUNT + 1;
					}
				}
			}
			if(flag){
				flag = Db.update("update WOBO_TRANSFER_BILL set COUNT = ? where TB_ID = ?", COUNT, TB_ID) > 0;
			}
		}
		if(flag){
			//检验该时间内有没有转移计划
			flag = this.checkTransferPlan(TB_ID, time);
		}
		if(flag){
			return list.size() - COUNT;
		}else{
			return -1;
		}
		
	}
	
	/**
	 * @author weizanting
	 * @date 20170405
	 * 方法：保存医疗废物运单
	 */
	@SuppressWarnings("unchecked")
	public int mergeBill(Map<String, Object> map, Record ifExist){
		int COUNT = 0;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Timestamp time = super.getSysdate();
		boolean flag = true;
		if(map.get("CS_PERSON_ID").equals(ifExist.get("CS_PERSON_ID"))){
			list = (List<Map<String, Object>>) map.get("boxList");
			if(list.size() > 0){
				for(Map<String, Object> box : list){
					Record boxs = Db.findFirst("select * from WOBO_TRANSFER_BILL_LIST where BOX_ID = ? and TG_ID = ?", box.get("BOX_ID"), map.get("TG_ID"));
					if(boxs == null){
						Record record = new Record();
						record.set("ID", super.getSeqId("WOBO_TRANSFER_BILL_LIST"));
						record.set("TB_ID", ifExist.get("TB_ID"));
						record.set("TG_ID", map.get("TG_ID"));
						record.set("BOX_ID", box.get("BOX_ID"));
						record.set("BEGINTIME", super.getSysdate());
						record.set("ACTIONDATE", super.getSysdate());
						record.set("STATUS", "3");
						record.set("sysdate", super.getSysdate());
						flag = Db.save("WOBO_TRANSFER_BILL_LIST", "ID", record);
						if(!flag){
							return -1;
						}
						COUNT = COUNT + 1;
					}
				}
			}
			if(flag){
				flag = Db.update("update WOBO_TRANSFER_BILL set COUNT = count + ?, ACTIONDATE = ?,UNIT_NUM = UNIT_NUM + ? where TB_ID = ?", COUNT, time, Integer.parseInt(map.get("WEIGHT").toString()), ifExist.get("TB_ID")) > 0;
			}
			if(flag){
				//检验该时间内有没有转移计划
				flag = this.checkTransferPlan(ifExist.get("TB_ID").toString(), time);
			}
			if(flag){
				return list.size() - COUNT;
			}else{
				return -1;
			}
		}else{
			return -2;
		}
	}
	
	/**
	 * @author weizanting
	 * @date 20170428
	 * 方法：保存医疗废物重量
	 */
	public boolean saveWeight(String TG_ID, String weight){
 		return Db.update("update WOBO_TRANSFER_BIG set UNIT_NUM = ?, UNIT = '公斤' where STATUS in ('3', '4') and TG_ID = ?", weight, TG_ID) > 0;
	}
	
	/**
	 * @author weizanting
	 * @date 20170428
	 * 方法：修改行程状态
	 */
	public boolean updateStatus(String TG_ID){
		boolean flag = false;
		flag = Db.update("update WOBO_TRANSFER_BIG set STATUS = '4' where STATUS in ('3', '4') and TG_ID = ?", TG_ID) > 0;
		if(flag){
			List<Record> recordList = Db.find("select TB_ID,UNIT_NUM,COUNT from WOBO_TRANSFER_BILL where STATUS in ('3', '4') and TG_ID = ?", TG_ID);
			if(recordList.size() > 0){
				for(Record record : recordList){
					if(flag){
						if((record.get("UNIT_NUM") != null && !"".equals(record.get("UNIT_NUM")) && Float.parseFloat(record.get("UNIT_NUM").toString()) > 0) || (record.get("COUNT") != null && !"".equals(record.get("COUNT")) && Float.parseFloat(record.get("COUNT").toString()) == 0)){
							flag = Db.update("update WOBO_TRANSFER_BILL set STATUS = '5', ENDTIME = ?, ACTIONDATE = ? where STATUS in ('3', '4') and TB_ID = ?", getSysdate(), getSysdate(), record.get("TB_ID")) > 0;
						}else{
							flag = Db.update("update WOBO_TRANSFER_BILL set STATUS = '4' where STATUS in ('3', '4') and TB_ID = ?", record.get("TB_ID").toString()) > 0;
						}
					}else{
						return false;
					}
				}
			}
			if(flag){
				Record record1 = Db.findFirst("select count(1) num from WOBO_TRANSFER_BILL where STATUS in ('0', '1', '2', '3', '4') and TG_ID = ?", TG_ID);
				if(record1.getInt("num") == 0){
					flag = Db.update("update WOBO_TRANSFER_BIG set STATUS = '5' where STATUS in ('3', '4') and TG_ID = ?", TG_ID) > 0;
				}
				Record record = Db.findFirst("select count(1) number from WOBO_TRANSFER_BILL_LIST where TG_ID = ?", TG_ID);
				if(record.getInt("number") > 0){
					flag = Db.update("update WOBO_TRANSFER_BILL_LIST set STATUS = '4' where STATUS in ('3', '4') and TG_ID = ?", TG_ID) > 0;
				}
			}else{
				flag = false;
			}
		}else{
			flag = false;
		}
		return flag;
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170519
	 * 方法：查询创建行程时间段里是否存在转移计划
	 */
	public boolean checkTransferPlan(String TB_ID, Timestamp date){
		Record record = Db.findFirst("select a.EN_ID_CZ, b.EN_ID_CS from WOBO_TRANSFER_BIG a, WOBO_TRANSFER_BILL b where b.TB_ID = ? and b.TG_ID = a.TG_ID", TB_ID);
		if(record != null){
			String epIdCs = record.getStr("EN_ID_CS");
			String epIdCz = record.getStr("EN_ID_CZ");
			Record plan = Db.findFirst("select * from WOBO_TRANSFER_PLAN where EN_ID_CS = ? and EN_ID_CZ = ? and BEGINTIME <= ? and ENDTIME > ? and STATUS != '1'", epIdCs, epIdCz, date, date);
			if(plan == null){
				Record unsubmit = Db.findFirst("select * from WOBO_CONDUITS_TRANSFER_UNSUBMIT where EP_ID_CS = ? and EP_ID_CZ = ?",epIdCs,epIdCz);
				if(unsubmit == null){
					Record conduit = new Record();
					conduit.set("EP_ID_CS", epIdCs);
					conduit.set("EP_ID_CZ", epIdCz);
					return Db.save("WOBO_CONDUITS_TRANSFER_UNSUBMIT", conduit);
				}else{
					return true;
				}
			}else{
				return true;
			}
		}
		return false;
		
	}
	
	/**
	 * @author weizanting
	 * @date 20170519
	 * 方法：删除行程
	 */
	public boolean deleteHaul(String TG_ID){
		return Db.update("update WOBO_TRANSFER_BIG set STATUS = '2' where TG_ID = ?", TG_ID) > 0;
	}
	
	/**
	 * @author weizanting
	 * @date 20170628
	 * 方法：查询医疗废物未完成行程
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> queryHaul(String EN_ID_CZ, String userId){
		Map<String, Object> map = null;
		Record record = Db.findFirst("select TG_ID,CZ_DRIVER_ID,CZ_DRIVER_NAME,CONVERT(varchar(100), BEGINTIME, 120) BEGINTIME,CONVERT(varchar(100), ENDTIME, 120) ENDTIME,STATUS,CI_ID,PLATE_NUMBER,isnull(UNIT_NUM, 0) as UNIT_NUM,BOX_NUM from WOBO_TRANSFER_BIG where STATUS = '3' and EN_ID_CZ =  ? and CZ_DRIVER_ID = ?", EN_ID_CZ, userId);
		if(record != null){
			map = new HashMap<String, Object>();
			map.put("TG_ID", record.get("TG_ID"));
			map.put("BEGINTIME", record.get("BEGINTIME"));
			map.put("ENDTIME", record.get("ENDTIME"));
			map.put("CZ_DRIVER_ID", record.get("CZ_DRIVER_ID"));
			map.put("CZ_DRIVER_NAME", record.get("CZ_DRIVER_NAME"));
			map.put("CI_ID", record.get("CI_ID"));
			map.put("PLATE_NUMBER", record.get("PLATE_NUMBER"));
			map.put("BOX_NUM", record.get("BOX_NUM") == null ? "0" : record.get("BOX_NUM"));
			map.put("BOX_SUTTLE", queryBoxSuttle());
			map.put("STATUS", record.get("STATUS"));
			Map<String, Object> m = queryBillList(record.get("TG_ID").toString(), 1, 10, "", "", null);
			int size = ((List<Map<String, Object>>)m.get("billList")).size();
			if(size > 0){//有运单
				map.put("ifExist", "0");
			}else{//无运单
				map.put("ifExist", "1");
			}
			Record count = Db.findFirst("select count(distinct EN_ID_CS) num from WOBO_TRANSFER_BILL where TG_ID = ?", record.get("TG_ID"));
			map.put("hospitalNum", count.get("num"));
			map.put("hospitalList", queryHospitalList(record.get("TG_ID").toString()));
			Record weight = Db.findFirst("select sum(isnull(cast(UNIT_NUM as decimal(9,2)), 0)) weight from WOBO_TRANSFER_BILL where TG_ID = ?", record.get("TG_ID"));
			//运单中除箱子外的总重
			map.put("weight", weight.get("weight"));
			Map<String, Object> car = getCarInfo(record.get("CI_ID"), record.get("PLATE_NUMBER"), record.get("NUIT_NUM"), record.get("TG_ID"));
			map.put("TARE", car.get("TARE").toString());
		}
		return map;
	}
	
	/**
	 * @author weizanting
	 * @date 20170628
	 * 方法：查询未完成行程的医院列表
	 */
	public List<Map<String, Object>> queryHospitalList(String TG_ID){
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = null;
		List<Record> recordList = Db.find("select EN_ID_CS,EN_NAME_CS from WOBO_TRANSFER_BILL where TG_ID = ?", TG_ID);
		if(recordList.size() > 0){
			for(Record record : recordList){
				map = new HashMap<String, Object>();
				map.put("EN_ID_CS", record.get("EN_ID_CS"));
				map.put("EN_NAME_CS", record.get("EN_NAME_CS"));
				list.add(map);
			}
		}
		return list;
	}
	
	/**
	 * @author weizanting
	 * @date 20170628
	 * 方法：查询处置单位车辆信息
	 */
	public Map<String, Object> queryCarInfo(String CI_ID){
		Record record = Db.findFirst("select a.CI_ID, a.PLATE_NUMBER, a.EP_ID, b.name from WOBO_CAR a, CarTypeCode b where a.CARTYPE = b.id and a.CI_ID = ?", CI_ID);
		Map<String, Object> map = new HashMap<String, Object>();
		if(record != null){
			map.put("CI_ID", record.get("CI_ID"));
			map.put("PLATE_NUMBER", record.get("PLATE_NUMBER"));
			map.put("EP_ID", record.get("EP_ID"));
			map.put("CarType", record.get("name"));
		}
		return map;
	}
	
	/**
	 * @author weizanting
	 * @date 20170628
	 * 方法：查询产生单位交接员信息
	 */
	public Map<String, Object> queryConnectInfo(String CS_PERSON_ID){
		Record record = Db.findFirst("select b.PI_ID,b.NAME,a.EP_NAME,a.EP_ID from WOBO_ENTERPRISE a,WOBO_PERSON_CS b where a.EP_ID = b.EP_ID and IFHANDORVE = '01' and b.PI_ID = ?", CS_PERSON_ID);
		Map<String, Object> map = new HashMap<String, Object>();
		if(record != null){
			map.put("PI_ID", record.get("PI_ID"));
			map.put("NAME", record.get("NAME"));
			map.put("EP_NAME", record.get("EP_NAME"));
			map.put("EP_ID", record.get("EP_ID"));
		}
		return map;
	}
	
	/**
	 * @author weizanting
	 * @date 20170628
	 * 方法：查询产生单位管理员信息
	 */
	public Map<String, Object> queryAdminInfo(String CS_PERSON_ID){
		Record record = Db.findFirst("select b.USER_ID,b.NAME,a.EP_NAME,a.EP_ID from WOBO_ENTERPRISE a,WOBO_PERSON_CS b where a.EP_ID = b.EP_ID and IFADMIN = '01' and b.USER_ID = ?", CS_PERSON_ID);
		Map<String, Object> map = new HashMap<String, Object>();
		if(record != null){
			map.put("PI_ID", record.get("USER_ID"));
			map.put("NAME", record.get("NAME"));
			map.put("EP_NAME", record.get("EP_NAME"));
			map.put("EP_ID", record.get("EP_ID"));
		}
		return map;
	}
	
	/**
	 * @author weizanting
	 * @date 20170630
	 * 方法：查询产生单位是否存在箱子运单
	 * @return 0--不存在；1--存在箱子运单；2--存在重量运单
	 */
	public String ifExistBill(String TG_ID, String EN_ID_CS){
		String str = "0";
		Record record = Db.findFirst("select isnull(b.COUNT, 0) COUNT,isnull(b.UNIT_NUM, 0) UNIT_NUM from WOBO_TRANSFER_BIG a,WOBO_TRANSFER_BILL b where a.TG_ID = b.TG_ID and b.TG_ID = ? and b.EN_ID_CS = ?", TG_ID, EN_ID_CS);
		if(record != null){
			if(Integer.parseInt(record.get("COUNT").toString()) >= 0){
				str = "1";
			}
			if(Integer.parseInt(record.get("UNIT_NUM").toString()) > 0){
				str = "2";
			}
		}
		return str;
	}
	
	/**
	 * @author weizanting
	 * @date 20170630
	 * 方法：先删除同行程下，同单位已存在箱子运单再新增一条重量的运单
	 */
	public boolean updateBillForWeight(Map<String, Object> map){
		boolean flag = false;
		Record record = Db.findFirst("select b.TB_ID from WOBO_TRANSFER_BIG a,WOBO_TRANSFER_BILL b where a.TG_ID = b.TG_ID and b.TG_ID = ? and b.EN_ID_CS = ?", map.get("TG_ID"), map.get("EN_ID_CS"));
		if(record != null){//先删除已存在箱子运单
			flag = Db.update("delete from WOBO_TRANSFER_BILL where TB_ID = ?", record.get("TB_ID").toString()) > 0;
			if(flag){
				Record bill = Db.findFirst("select count(1) count from WOBO_TRANSFER_BILL_LIST where TB_ID = ?", record.get("TB_ID"));
				if(Integer.parseInt(bill.get("count").toString()) > 0){
					flag = Db.update("delete from WOBO_TRANSFER_BILL_LIST where TB_ID = ?", record.get("TB_ID").toString()) > 0;
					if(flag){
						int num = saveBill(map);
						if(num >= 0){
							flag = true;
						}else{
							flag = false;
						}
					}
				}else{
					int num = saveBill(map);
					if(num >= 0){
						flag = true;
					}else{
						flag = false;
					}
				}
			}
			/*Record bill = new Record();
			String TB_ID = super.getSeqId("WOBO_TRANSFER_BILL");
			Timestamp time = super.getSysdate();
			if(flag){//保存填写重量的运单
				bill.set("TB_ID", TB_ID);
				bill.set("TG_ID", map.get("TG_ID"));
				bill.set("EN_ID_CS", map.get("EN_ID_CS"));
				bill.set("EN_NAME_CS", map.get("EN_NAME_CS"));
				bill.set("CS_PERSON_ID", map.get("CS_PERSON_ID"));
				bill.set("CS_PERSON_NAME", map.get("CS_PERSON_NAME"));
				bill.set("STATUS", "3");
				bill.set("ACTIONDATE", time);
				bill.set("BEGINTIME", time);
				bill.set("sysdate", time);
				bill.set("COUNT", map.get("COUNT"));
				bill.set("UNIT_NUM", map.get("WEIGHT"));
				bill.set("UNIT", "公斤");
				flag = Db.save("WOBO_TRANSFER_BILL", "TB_ID", bill);
			}
			if(flag){
				//检验该时间内有没有转移计划
				flag = this.checkTransferPlan(TB_ID, time);
			}*/
		}
		return flag;
	}
	
	/**
	 * @author weizanting
	 * @date 20170630
	 * 方法：先删除同行程下，同单位已存在重量运单再新增一条箱子的运单
	 */
//	@SuppressWarnings("unchecked")
	public int updateBillForBox(Map<String, Object> map){
		boolean flag = false;
		int num = 0;
//		int COUNT = 0;
//		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Record record = Db.findFirst("select b.TB_ID from WOBO_TRANSFER_BIG a,WOBO_TRANSFER_BILL b where a.TG_ID = b.TG_ID and b.TG_ID = ? and b.EN_ID_CS = ?", map.get("TG_ID"), map.get("EN_ID_CS"));
		if(record != null){//先删除已存在重量运单
			flag = Db.update("delete from WOBO_TRANSFER_BILL where TB_ID = ?", record.get("TB_ID").toString()) > 0;
			if(flag){//保存扫箱子的运单
				num = saveBill(map);
				/*if(num >= 0){
					flag = true;
				}else{
					flag = false;
				}*/
				/*Record bill = new Record();
				String TB_ID = super.getSeqId("WOBO_TRANSFER_BILL");
				bill.set("TB_ID", TB_ID);
				bill.set("TG_ID", map.get("TG_ID"));
				bill.set("EN_ID_CS", map.get("EN_ID_CS"));
				bill.set("EN_NAME_CS", map.get("EN_NAME_CS"));
				bill.set("CS_PERSON_ID", map.get("CS_PERSON_ID"));
				bill.set("CS_PERSON_NAME", map.get("CS_PERSON_NAME"));
				bill.set("STATUS", "3");
				Timestamp time = super.getSysdate();
				bill.set("ACTIONDATE", time);
				bill.set("BEGINTIME", time);
				bill.set("sysdate", time);
				bill.set("COUNT", map.get("COUNT"));
				bill.set("UNIT_NUM", map.get("WEIGHT"));
				bill.set("UNIT", "公斤");
				flag = Db.save("WOBO_TRANSFER_BILL", "TB_ID", bill);
				if(flag){
					list = (List<Map<String, Object>>) map.get("boxList");
					if(list.size() > 0){
						for(Map<String, Object> box : list){
							Record boxs = Db.findFirst("select * from WOBO_TRANSFER_BILL_LIST where BOX_ID = ? and TG_ID = ?", box.get("BOX_ID"), map.get("TG_ID"));
							if(boxs == null){
								Record record = new Record();
								record.set("ID", super.getSeqId("WOBO_TRANSFER_BILL_LIST"));
								record.set("TB_ID", TB_ID);
								record.set("TG_ID", map.get("TG_ID"));
								record.set("BOX_ID", box.get("BOX_ID"));
								record.set("BEGINTIME", super.getSysdate());
								record.set("ACTIONDATE", super.getSysdate());
								record.set("STATUS", "3");
								record.set("sysdate", super.getSysdate());
								flag = Db.save("WOBO_TRANSFER_BILL_LIST", "ID", record);
								if(!flag){
									return -1;
								}
								COUNT = COUNT + 1;
							}
						}
					}
					if(flag){
						flag = Db.update("update WOBO_TRANSFER_BILL set COUNT = ? where TB_ID = ?", COUNT, TB_ID) > 0;
					}
				}
				if(flag){
					//检验该时间内有没有转移计划
					flag = this.checkTransferPlan(TB_ID, time);
				}*/
			}
		}
		return num;
	}
	
	/**
	 * @author weizanting
	 * @date 20171016
	 * 方法：查询处置单位桶净重
	 */
	public String queryBoxSuttle(){
		String str = "0";
		if(boxSuttle.get(0).get("dict_value") != null && !"0".equals(boxSuttle.get(0).get("dict_value"))){
			str = boxSuttle.get(0).get("dict_value");
		}
		return str;
	}
	
}
