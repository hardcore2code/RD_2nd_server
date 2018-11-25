package com.mine.rd.services.deal.pojo;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.mine.pub.kit.DateKit;
import com.mine.pub.pojo.BaseDao;

public class DealDao extends BaseDao {

	/**
	 * @author ouyangxu
	 * @throws ParseException 
	 * @date 20170331
	 * 方法：查询当天处置信息
	 */
	public Map<String, Object> queryDealListToday(String epId, int pn, int ps) throws ParseException{
		String startTime = DateKit.toStr(super.getSysdate(), "yyyy-MM-dd") + " 00:00:00";
		java.sql.Date startDate = null;
		startDate = new java.sql.Date (DateKit.toDate(startTime, "yyyy-MM-dd HH:mm:ss").getTime());
		Page<Record> page = Db.paginate(pn, ps, "select * ", "from WOBO_DEAL_BILL where sysdate >= ? and sysdate <= ? and EN_ID_CZ = ? order by sysdate desc", startDate, super.getSysdate(),epId);
		List<Record> billList = page.getList();
//		List<Record> billList = Db.find("select * from WOBO_DEAL_BILL where sysdate >= ? and sysdate <= ? and EN_ID_CZ = ? order by sysdate desc", startDate, super.getSysdate(),epId);
		Map<String, Object> resMap = new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<>();
		if(billList.size() > 0){
			for(Record bill : billList){
				Map<String, Object> map = new HashMap<>();
				map.put("id", bill.get("DB_ID"));
				map.put("username", bill.get("CZ_PERSON_NAME"));
				map.put("boxNum", bill.get("COUNT"));
				map.put("dealTime", DateKit.toStr(bill.getDate("sysdate"), "yyyy-MM-dd HH:mm"));
				list.add(map);
			}
		}
		resMap.put("dealList", list);
		resMap.put("totalPage", page.getTotalPage());
		resMap.put("totalRow", page.getTotalRow());
		return resMap;
	}
	
	/**
	 * @author ouyangxu
	 * @throws ParseException 
	 * @date 20170405
	 * 方法：查询历史处置信息
	 */
	public Map<String, Object> queryDealListHistory(String epId, int pn, int ps) throws ParseException{
		String endTime = DateKit.toStr(super.getSysdate(), "yyyy-MM-dd") + " 00:00:00";
		java.sql.Date endDate = null;
		endDate = new java.sql.Date (DateKit.toDate(endTime, "yyyy-MM-dd HH:mm:ss").getTime());
		Page<Record> page = Db.paginate(pn, ps, "select * ", "from WOBO_DEAL_BILL where sysdate < ? and EN_ID_CZ = ? order by sysdate desc", endDate, epId);
		List<Record> billList = page.getList();
//		List<Record> billList = Db.find("select * from WOBO_DEAL_BILL where sysdate < ? and EN_ID_CZ = ? order by sysdate desc", endDate, epId);
		Map<String, Object> resMap = new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<>();
		if(billList.size() > 0){
			for(Record bill : billList){
				Map<String, Object> map = new HashMap<>();
				map.put("id", bill.get("DB_ID"));
				map.put("username", bill.get("CZ_PERSON_NAME"));
				map.put("boxNum", bill.get("COUNT"));
				map.put("dealTime", DateKit.toStr(bill.getDate("sysdate"), "yyyy-MM-dd HH:mm"));
				list.add(map);
			}
		}
		resMap.put("dealList", list);
		resMap.put("totalPage", page.getTotalPage());
		resMap.put("totalRow", page.getTotalRow());
		return resMap;
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170405
	 * 方法：处置医疗废物
	 */
	public int dealBox(Map<String, Object> dealInfo){
		int i = 0;
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> boxIds = (List<Map<String, Object>>) dealInfo.get("boxIds");
		Timestamp time = super.getSysdate();
		for(Map<String, Object> boxId : boxIds){
			//查询该箱子信息
			List<Record> record1 = Db.find("select * from WOBO_TRANSFER_BILL_LIST where BOX_ID = ? and STATUS = '4'", boxId.get("id"));
			if(record1.size() > 0){
				//修改箱子状态为已处置
				boolean flag1 = Db.update("update WOBO_TRANSFER_BILL_LIST set STATUS = '5',ACTIONDATE = ?,CZ_PERSON_NAME = ?,CZ_PERSON_ID=?,ENDTIME=? where BOX_ID = ? and STATUS = '4'", time,dealInfo.get("czPersonName"),dealInfo.get("czPersonId"),time,boxId.get("id")) > 0;
				if(flag1){
					for(Record record: record1){
						String tbId = record.getStr("TB_ID");
						String tgId = record.getStr("TG_ID");
						//查询该箱子同一运单下箱子是否都已处置
						Record record2 = Db.findFirst("select * from WOBO_TRANSFER_BILL_LIST where TB_ID = ? and TG_ID = ? and STATUS in ('3','4')", tbId, tgId);
						if(record2 == null){
							//该运单下箱子全部已处置，将该运单改为已处置
							boolean flag2 = Db.update("update WOBO_TRANSFER_BILL set STATUS = '5',ACTIONDATE = ?,ENDTIME = ? where TB_ID = ? and STATUS = '4'", time,time,tbId) > 0;
							if(flag2){
								//查询该行程下运单是否都已处置
								Record record3 = Db.findFirst("select * from WOBO_TRANSFER_BILL where TG_ID = ? and STATUS in ('3','4')", tgId);
								if(record3 == null){
									//该行程下所有运单已处置，将该行程改为已处置
									boolean flag3 = Db.update("update WOBO_TRANSFER_BIG set STATUS = '5',ACTIONDATE=?,ENDTIME=? where TG_ID = ? and STATUS = '4'", time,time,tgId) > 0;
									if(!flag3){
										continue;
									}
								}
							}else{
								continue;
							}
						}
						i = i+1;
					}
				}
			}
		}
		return i;
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170405
	 * 方法：存处置医疗废物记录
	 */
	public boolean insertDealInfo(Map<String, Object> dealInfo, int count){
		Record record = new Record();
		record.set("DB_ID", super.getSeqId("WOBO_DEAL_BILL"));
		record.set("CZ_PERSON_ID", dealInfo.get("czPersonId"));
		record.set("CZ_PERSON_NAME", dealInfo.get("czPersonName"));
		record.set("EN_ID_CZ", dealInfo.get("czEpId"));
		record.set("EN_NAME_CZ", dealInfo.get("czEpName"));
		record.set("COUNT", count);
		record.set("sysdate", super.getSysdate());
		return Db.save("WOBO_DEAL_BILL", "DB_ID", record);
	}
}
