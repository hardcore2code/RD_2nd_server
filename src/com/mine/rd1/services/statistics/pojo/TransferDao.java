package com.mine.rd1.services.statistics.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.mine.pub.kit.DateKit;
import com.mine.pub.pojo.BaseDao;

public class TransferDao extends BaseDao {
	
	
	/**
	 * @author zyl
	 * @date 20170519
	 * 方法：app查询转移联单方法
	 */
	public Map<String, Object> queryTransferplanBill(String id){
		Record record = Db.findFirst("SELECT * FROM TRANSFERPLAN_BILL AS t WHERE t.TB_ID = ?", id);
		Map<String, Object> resMap = new HashMap<String, Object>();
		Map<String, Object> entityData = new HashMap<String, Object>();
		Map<String, Object> map = null;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if(record != null){
			entityData.put("tbId", record.get("TB_ID"));
			entityData.put("tpId", record.get("TP_ID"));
			entityData.put("tpMainId", record.get("TP_MAIN_ID"));
			entityData.put("enIdCs", record.get("EN_ID_CS"));
			entityData.put("enIdYs", record.get("EN_ID_YS"));
			entityData.put("enIdCz", record.get("EN_ID_CZ"));
			entityData.put("enNameCs", record.get("EN_NAME_CS"));
			entityData.put("enNameYs", record.get("EN_NAME_YS"));
			entityData.put("enNameCz", record.get("EN_NAME_CZ"));
			entityData.put("beginTime", DateKit.toStr(record.getDate("BEGINTIME"), "yyyy-MM-dd"));
			entityData.put("endTime", DateKit.toStr(record.getDate("ENDTIME"), "yyyy-MM-dd"));
			entityData.put("status", record.get("STATUS"));
			entityData.put("actionDate", DateKit.toStr(record.getDate("ACTIONDATE"), "yyyy-MM-dd"));
			entityData.put("tbDate", DateKit.toStr(record.getDate("TBDATE"), "yyyy-MM-dd"));
			entityData.put("ysDate", DateKit.toStr(record.getDate("YSDATE"), "yyyy-MM-dd"));
			entityData.put("jsDate", DateKit.toStr(record.getDate("JSDATE"), "yyyy-MM-dd"));
			entityData.put("cardNo", record.get("CARDNO"));
			entityData.put("ifCollect", record.get("IFCOLLECT"));
			entityData.put("csy", record.get("CSY"));
			entityData.put("csCard", record.get("CSCARD"));
			entityData.put("ysy", record.get("YSY"));
			entityData.put("ysCard", record.get("YSCARD"));
			entityData.put("czy", record.get("CZY"));
			entityData.put("czCard", record.get("CZCARD"));
			entityData.put("reason", record.get("REASON"));
			entityData.put("reasonDate", DateKit.toStr(record.getDate("REASONDATE"), "yyyy-MM-dd"));
			entityData.put("createPerson", record.get("CREATEPERSON"));
			entityData.put("carCard", record.get("CARCARD"));
			entityData.put("sysDate", DateKit.toStr(record.getDate("sysdate"), "yyyy-MM-dd"));
			entityData.put("processinstId", record.get("PROCESSINSTID"));
			entityData.put("reason", record.get("REASON"));
			entityData.put("linkMan", record.get("LINKMAN"));
			entityData.put("linkTel", record.get("LINKTEL"));
			entityData.put("linkPhone", record.get("LINKPHONE"));
			entityData.put("sysdate", DateKit.toStr(record.getDate("sysdate"), "yyyy-MM-dd HH:mm:ss"));
			List<Record> recordList = Db.find("select b.* from TRANSFERPLAN_BILL_LIST b where b.TB_ID = ?", id);
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
					map.put("samllCategoryId", al.get("SAMLL_CATEGORY_ID"));
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
	 * @author zyl
	 * @date 20170519
	 * 方法：app查询人员卡信息
	 */
	public List<Map<String, Object>> queryPersonInfo(String cardNo,String epId,String tableName){
		String sql="select * from "+tableName+" t where 1=1 ";
		if(cardNo != null)
		{
			sql=sql+" and t.CARDNO='"+cardNo+"' ";
		}
		if(epId != null)
		{
			sql=sql+" and t.EP_ID='"+epId+"' ";
		}
		List<Record> recordList = Db.find(sql);
		Map<String, Object> map = null;
		List<Map<String, Object>> mapList = null;
		if(recordList.size()>0){
			mapList=new ArrayList<Map<String, Object>>();
			for (Record record : recordList) {
				map = new HashMap<String, Object>();
				map.put("piId", record.get("PI_ID"));
				map.put("epId", record.get("EP_ID"));
				map.put("cardNo", record.get("CARDNO"));
				map.put("name", record.get("NAME"));
				map.put("birthday", DateKit.toStr(record.getDate("BIRTHDAY"), "yyyy-MM-dd"));
				map.put("ifAdmin", record.get("IFADMIN"));
				map.put("ifHandorve", record.get("IFHANDORVE"));
				map.put("ifDriver", record.get("IFDRIVER"));
				map.put("sysdate", DateKit.toStr(record.getDate("sysdate"), "yyyy-MM-dd HH:mm:ss"));
				map.put("identitied", record.get("identitied"));
				mapList.add(map);
			}
		}
		return mapList;
	}
	
	/**
	 * @author zyl
	 * @date 20170519
	 * 方法：app查询人员卡的人员类型
	 */
	public Map<String, Object> queryPerson(String cardNo){
		String sql="select * from USERCARDAGENT t where CARDID = '"+cardNo+"' ";
		Map<String, Object> resMap = new HashMap<String, Object>();
		Record record = Db.findFirst(sql);
		Map<String, Object> map = null;
		if(record != null){
			map = new HashMap<String, Object>();
			map.put("cardId", record.get("CARDID"));
			map.put("operatorId", record.get("OPERATORID"));
			map.put("empId", record.get("EMPID"));
			map.put("status", record.get("STATUS"));
			map.put("type", record.get("TYPE"));
		}
		resMap.put("entityData", map);
		return resMap;
	}
	
	/**
	 * @author zyl
	 * @date 20170519
	 * 方法：app查询车辆信息
	 */
	public List<Map<String, Object>> queryCarInfo(String cardNo,String epId){
		String sql="select * from CAR_INFO t where 1=1 ";
		if(cardNo != null)
		{
			sql=sql+" and t.CARDNO='"+cardNo+"' ";
		}
		if(epId != null)
		{
			sql=sql+" and t.EP_ID='"+epId+"' ";
		}
		List<Record> recordList = Db.find(sql);
		List<Map<String, Object>> mapList = null;
		Map<String, Object> map = null;
		if(recordList.size() > 0){
			mapList=new ArrayList<Map<String, Object>>();
			for (Record record : recordList) {
				map = new HashMap<String, Object>();
				map.put("ciId", record.get("CI_ID"));
				map.put("epId", record.get("EP_ID"));
				map.put("plateNumber", record.get("PLATE_NUMBER"));
				map.put("carType", record.get("CARTYPE"));
				map.put("permitNum", record.get("PERMITNUM"));
				map.put("cardNo", record.get("CARDNO"));
				map.put("sysdate", DateKit.toStr(record.getDate("sysdate"), "yyyy-MM-dd HH:mm:ss"));
				mapList.add(map);
			}
		}
		return mapList;
	}
}
