package com.mine.rd.services.transferSummary.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.ehcache.CacheKit;
import com.mine.pub.pojo.BaseDao;

public class TransferSummaryDao extends BaseDao {

	private List<Record> cityList = CacheKit.get("mydict", "city_q");
	
	/**
	 * @author weizanting
	 * @date 20170622
	 * 方法：查询转移汇总（按产生单位）--全部
	 */
	public Map<String, Object> queryTransferSummaryForCS(int pn, int ps, Object searchContent, String ROLEID, String userType, String orgCode, String epId){
		Map<String, Object> resMap = new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = null;
		Page<Record> page = null;
		String sql = "";
		if("SJSPROLE".equals(ROLEID)){		//市级管理员
			sql = " from (select EN_ID_CS,EN_NAME_CS,BELONGSEPA,sum(WEIGHT) as WEIGHT,sum(AMOUNT) as AMOUNT from (select a.EN_ID_CS,a.EN_NAME_CS,b.BELONGSEPA,cast(a.UNIT_NUM as numeric(10,2)) as WEIGHT,a.COUNT as AMOUNT,a.ACTIONDATE,a.sysdate from WOBO_TRANSFER_BILL a left join WOBO_ENTERPRISE b on a.EN_ID_CS = b.EP_ID where b.STATUS = '1' and a.STATUS in ('4', '5') group by a.EN_ID_CS,a.EN_NAME_CS,b.BELONGSEPA,a.UNIT_NUM,a.COUNT,a.ACTIONDATE,a.sysdate) as A group by EN_ID_CS,EN_NAME_CS,BELONGSEPA) as B";
		}
		if("QJSPROLE".equals(ROLEID)){		//区级管理员
			sql = " from (select EN_ID_CS,EN_NAME_CS,BELONGSEPA,sum(WEIGHT) as WEIGHT,sum(AMOUNT) as AMOUNT from (select a.EN_ID_CS,a.EN_NAME_CS,b.BELONGSEPA,cast(a.UNIT_NUM as numeric(10,2)) as WEIGHT,a.COUNT as AMOUNT,a.ACTIONDATE,a.sysdate from WOBO_TRANSFER_BILL a left join WOBO_ENTERPRISE b on a.EN_ID_CS = b.EP_ID where b.STATUS = '1' and a.STATUS in ('4', '5') and b.BELONGSEPA = '" + orgCode + "' group by a.EN_ID_CS,a.EN_NAME_CS,b.BELONGSEPA,a.UNIT_NUM,a.COUNT,a.ACTIONDATE,a.sysdate) as A group by EN_ID_CS,EN_NAME_CS,BELONGSEPA) as B";
		}
		if("epAdminCs".equals(userType)){		//医疗单位管理员
			sql = " from (select EN_ID_CS,EN_NAME_CS,BELONGSEPA,sum(WEIGHT) as WEIGHT,sum(AMOUNT) as AMOUNT from (select a.EN_ID_CS,a.EN_NAME_CS,b.BELONGSEPA,cast(a.UNIT_NUM as numeric(10,2)) as WEIGHT,a.COUNT as AMOUNT,a.ACTIONDATE,a.sysdate from WOBO_TRANSFER_BILL a left join WOBO_ENTERPRISE b on a.EN_ID_CS = b.EP_ID where b.STATUS = '1' and a.STATUS in ('4', '5') and b.EP_ID = '" + epId + "' group by a.EN_ID_CS,a.EN_NAME_CS,b.BELONGSEPA,a.UNIT_NUM,a.COUNT,a.ACTIONDATE,a.sysdate) as A group by EN_ID_CS,EN_NAME_CS,BELONGSEPA) as B";
		}
		if("epAdminCz".equals(userType)){		//医疗处置单位管理员
			sql = " from (select EN_ID_CS,EN_NAME_CS,BELONGSEPA,sum(WEIGHT) as WEIGHT,sum(AMOUNT) as AMOUNT from (select a.EN_ID_CS,a.EN_NAME_CS,b.BELONGSEPA,cast(a.UNIT_NUM as numeric(10,2)) as WEIGHT,a.COUNT as AMOUNT,a.ACTIONDATE,a.sysdate from WOBO_TRANSFER_BILL a left join WOBO_ENTERPRISE b on a.EN_ID_CS = b.EP_ID,WOBO_TRANSFER_BIG c  where b.STATUS = '1' and a.STATUS in ('4', '5') and a.TG_ID = c.TG_ID and c.EN_ID_CZ = '" + epId + "' group by a.EN_ID_CS,a.EN_NAME_CS,b.BELONGSEPA,a.UNIT_NUM,a.COUNT,a.ACTIONDATE,a.sysdate) as A group by EN_ID_CS,EN_NAME_CS,BELONGSEPA) as B";
		}
		if(searchContent != null && !"".equals(searchContent)){
			sql = sql + " where EN_NAME_CS like '%" + searchContent + "%'";
		}
		sql = sql + " group by EN_ID_CS,EN_NAME_CS,BELONGSEPA,WEIGHT,AMOUNT";
		page = Db.paginate(pn, ps, "select EN_ID_CS,EN_NAME_CS,BELONGSEPA,isnull(WEIGHT, 0) WEIGHT, isnull(AMOUNT, 0) AMOUNT", sql);
		List<Record> recordList = page.getList();
		if(recordList.size() > 0){
			for(Record record : recordList){
				map = new HashMap<String, Object>();
				map.put("EN_NAME_CS", record.get("EN_NAME_CS"));
				if(record.get("BELONGSEPA") != null){
					map.put("SEPANAME", convert(cityList, record.get("BELONGSEPA")));
				}else{
					map.put("SEPANAME", "");
				}
				map.put("WEIGHT", record.get("WEIGHT"));
				map.put("COUNT", record.get("AMOUNT"));
				list.add(map);
			}
		}
		resMap.put("dataList", list);
		resMap.put("num", queryNumForCS(searchContent, ROLEID, userType, orgCode, epId));
		resMap.put("totalPage", page.getTotalPage());
		resMap.put("totalRow", page.getTotalRow());
		return resMap;
	}
	
	/**
	 * @author weizanting
	 * @date 20170622
	 * 方法：查询转移汇总（按产生单位）--开始时间
	 */
	public Map<String, Object> queryTransferSummaryForCS(String BEGINTIME, int pn, int ps, Object searchContent, String ROLEID, String userType, String orgCode, String epId){
		Map<String, Object> resMap = new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = null;
		Page<Record> page = null;
		String sql = "";
		if("SJSPROLE".equals(ROLEID)){		//市级管理员
			sql = " from (select EN_ID_CS,EN_NAME_CS,BELONGSEPA,sum(WEIGHT) as WEIGHT,sum(AMOUNT) as AMOUNT from (select a.EN_ID_CS,a.EN_NAME_CS,b.BELONGSEPA,cast(a.UNIT_NUM as numeric(10,2)) as WEIGHT,a.COUNT as AMOUNT,a.ACTIONDATE,a.sysdate from WOBO_TRANSFER_BILL a left join WOBO_ENTERPRISE b on a.EN_ID_CS = b.EP_ID where b.STATUS = '1' and a.STATUS in ('4', '5') and (a.ACTIONDATE >= '" + BEGINTIME + "' or a.sysdate >= '" + BEGINTIME + "') group by a.EN_ID_CS,a.EN_NAME_CS,b.BELONGSEPA,a.UNIT_NUM,a.COUNT,a.ACTIONDATE,a.sysdate) as A group by EN_ID_CS,EN_NAME_CS,BELONGSEPA) as B";
		}
		if("QJSPROLE".equals(ROLEID)){		//区级管理员
			sql = " from (select EN_ID_CS,EN_NAME_CS,BELONGSEPA,sum(WEIGHT) as WEIGHT,sum(AMOUNT) as AMOUNT from (select a.EN_ID_CS,a.EN_NAME_CS,b.BELONGSEPA,cast(a.UNIT_NUM as numeric(10,2)) as WEIGHT,a.COUNT as AMOUNT,a.ACTIONDATE,a.sysdate from WOBO_TRANSFER_BILL a left join WOBO_ENTERPRISE b on a.EN_ID_CS = b.EP_ID where b.STATUS = '1' and a.STATUS in ('4', '5') and b.BELONGSEPA = '" + orgCode + "' and (a.ACTIONDATE >= '" + BEGINTIME + "' or a.sysdate >= '" + BEGINTIME + "') group by a.EN_ID_CS,a.EN_NAME_CS,b.BELONGSEPA,a.UNIT_NUM,a.COUNT,a.ACTIONDATE,a.sysdate) as A group by EN_ID_CS,EN_NAME_CS,BELONGSEPA) as B";
		}
		if("epAdminCs".equals(userType)){		//医疗单位管理员
			sql = " from (select EN_ID_CS,EN_NAME_CS,BELONGSEPA,sum(WEIGHT) as WEIGHT,sum(AMOUNT) as AMOUNT from (select a.EN_ID_CS,a.EN_NAME_CS,b.BELONGSEPA,cast(a.UNIT_NUM as numeric(10,2)) as WEIGHT,a.COUNT as AMOUNT,a.ACTIONDATE,a.sysdate from WOBO_TRANSFER_BILL a left join WOBO_ENTERPRISE b on a.EN_ID_CS = b.EP_ID where b.STATUS = '1' and a.STATUS in ('4', '5') and b.EP_ID = '" + epId + "' and (a.ACTIONDATE >= '" + BEGINTIME + "' or a.sysdate >= '" + BEGINTIME + "') group by a.EN_ID_CS,a.EN_NAME_CS,b.BELONGSEPA,a.UNIT_NUM,a.COUNT,a.ACTIONDATE,a.sysdate) as A group by EN_ID_CS,EN_NAME_CS,BELONGSEPA) as B";
		}
		if("epAdminCz".equals(userType)){		//医疗处置单位管理员
			sql = " from (select EN_ID_CS,EN_NAME_CS,BELONGSEPA,sum(WEIGHT) as WEIGHT,sum(AMOUNT) as AMOUNT from (select a.EN_ID_CS,a.EN_NAME_CS,b.BELONGSEPA,cast(a.UNIT_NUM as numeric(10,2)) as WEIGHT,a.COUNT as AMOUNT,a.ACTIONDATE,a.sysdate from WOBO_TRANSFER_BILL a left join WOBO_ENTERPRISE b on a.EN_ID_CS = b.EP_ID,WOBO_TRANSFER_BIG c  where b.STATUS = '1' and a.STATUS in ('4', '5') and a.TG_ID = c.TG_ID and c.EN_ID_CZ = '" + epId + "' and (a.ACTIONDATE >= '" + BEGINTIME + "' or a.sysdate >= '" + BEGINTIME + "') group by a.EN_ID_CS,a.EN_NAME_CS,b.BELONGSEPA,a.UNIT_NUM,a.COUNT,a.ACTIONDATE,a.sysdate) as A group by EN_ID_CS,EN_NAME_CS,BELONGSEPA) as B";
		}
		if(searchContent != null && !"".equals(searchContent)){
			sql = sql + " where EN_NAME_CS like '%" + searchContent + "%'";
		}
		sql = sql + " group by EN_ID_CS,EN_NAME_CS,BELONGSEPA,WEIGHT,AMOUNT";
		page = Db.paginate(pn, ps, "select EN_ID_CS,EN_NAME_CS,BELONGSEPA,isnull(WEIGHT, 0) WEIGHT, isnull(AMOUNT, 0) AMOUNT", sql);
		List<Record> recordList = page.getList();
		if(recordList.size() > 0){
			for(Record record : recordList){
				map = new HashMap<String, Object>();
				map.put("EN_NAME_CS", record.get("EN_NAME_CS"));
				if(record.get("BELONGSEPA") != null){
					map.put("SEPANAME", convert(cityList, record.get("BELONGSEPA")));
				}else{
					map.put("SEPANAME", "");
				}
				map.put("WEIGHT", record.get("WEIGHT"));
				map.put("COUNT", record.get("AMOUNT"));
				list.add(map);
			}
		}
		resMap.put("dataList", list);
		resMap.put("num", queryNumForCS(BEGINTIME, searchContent, ROLEID, userType, orgCode, epId));
		resMap.put("totalPage", page.getTotalPage());
		resMap.put("totalRow", page.getTotalRow());
		return resMap;
	}
	
	/**
	 * @author weizanting
	 * @date 20170622
	 * 方法：查询转移汇总（按产生单位）--结束时间
	 */
	public Map<String, Object> queryTransferSummaryForCS(int pn, int ps, String ENDTIME, Object searchContent, String ROLEID, String userType, String orgCode, String epId){
		Map<String, Object> resMap = new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = null;
		Page<Record> page = null;
		String sql = "";
		if("SJSPROLE".equals(ROLEID)){		//市级管理员
			sql = " from (select EN_ID_CS,EN_NAME_CS,BELONGSEPA,sum(WEIGHT) as WEIGHT,sum(AMOUNT) as AMOUNT from (select a.EN_ID_CS,a.EN_NAME_CS,b.BELONGSEPA,cast(a.UNIT_NUM as numeric(10,2)) as WEIGHT,a.COUNT as AMOUNT,a.ACTIONDATE,a.sysdate from WOBO_TRANSFER_BILL a left join WOBO_ENTERPRISE b on a.EN_ID_CS = b.EP_ID where b.STATUS = '1' and a.STATUS in ('4', '5') and (a.ACTIONDATE <= '" + ENDTIME + "' or a.sysdate <= '" + ENDTIME + "') group by a.EN_ID_CS,a.EN_NAME_CS,b.BELONGSEPA,a.UNIT_NUM,a.COUNT,a.ACTIONDATE,a.sysdate) as A group by EN_ID_CS,EN_NAME_CS,BELONGSEPA) as B";
		}
		if("QJSPROLE".equals(ROLEID)){		//区级管理员
			sql = " from (select EN_ID_CS,EN_NAME_CS,BELONGSEPA,sum(WEIGHT) as WEIGHT,sum(AMOUNT) as AMOUNT from (select a.EN_ID_CS,a.EN_NAME_CS,b.BELONGSEPA,cast(a.UNIT_NUM as numeric(10,2)) as WEIGHT,a.COUNT as AMOUNT,a.ACTIONDATE,a.sysdate from WOBO_TRANSFER_BILL a left join WOBO_ENTERPRISE b on a.EN_ID_CS = b.EP_ID where b.STATUS = '1' and a.STATUS in ('4', '5') and b.BELONGSEPA = '" + orgCode + "' and (a.ACTIONDATE <= '" + ENDTIME + "' or a.sysdate <= '" + ENDTIME + "') group by a.EN_ID_CS,a.EN_NAME_CS,b.BELONGSEPA,a.UNIT_NUM,a.COUNT,a.ACTIONDATE,a.sysdate) as A group by EN_ID_CS,EN_NAME_CS,BELONGSEPA) as B";
		}
		if("epAdminCs".equals(userType)){		//医疗单位管理员
			sql = " from (select EN_ID_CS,EN_NAME_CS,BELONGSEPA,sum(WEIGHT) as WEIGHT,sum(AMOUNT) as AMOUNT from (select a.EN_ID_CS,a.EN_NAME_CS,b.BELONGSEPA,cast(a.UNIT_NUM as numeric(10,2)) as WEIGHT,a.COUNT as AMOUNT,a.ACTIONDATE,a.sysdate from WOBO_TRANSFER_BILL a left join WOBO_ENTERPRISE b on a.EN_ID_CS = b.EP_ID where b.STATUS = '1' and a.STATUS in ('4', '5') and b.EP_ID = '" + epId + "' and (a.ACTIONDATE <= '" + ENDTIME + "' or a.sysdate <= '" + ENDTIME + "') group by a.EN_ID_CS,a.EN_NAME_CS,b.BELONGSEPA,a.UNIT_NUM,a.COUNT,a.ACTIONDATE,a.sysdate) as A group by EN_ID_CS,EN_NAME_CS,BELONGSEPA) as B";
		}
		if("epAdminCz".equals(userType)){		//医疗处置单位管理员
			sql = " from (select EN_ID_CS,EN_NAME_CS,BELONGSEPA,sum(WEIGHT) as WEIGHT,sum(AMOUNT) as AMOUNT from (select a.EN_ID_CS,a.EN_NAME_CS,b.BELONGSEPA,cast(a.UNIT_NUM as numeric(10,2)) as WEIGHT,a.COUNT as AMOUNT,a.ACTIONDATE,a.sysdate from WOBO_TRANSFER_BILL a left join WOBO_ENTERPRISE b on a.EN_ID_CS = b.EP_ID,WOBO_TRANSFER_BIG c  where b.STATUS = '1' and a.STATUS in ('4', '5') and a.TG_ID = c.TG_ID and c.EN_ID_CZ = '" + epId + "' and (a.ACTIONDATE <= '" + ENDTIME + "' or a.sysdate <= '" + ENDTIME + "') group by a.EN_ID_CS,a.EN_NAME_CS,b.BELONGSEPA,a.UNIT_NUM,a.COUNT,a.ACTIONDATE,a.sysdate) as A group by EN_ID_CS,EN_NAME_CS,BELONGSEPA) as B";
		}
		if(searchContent != null && !"".equals(searchContent)){
			sql = sql + " where EN_NAME_CS like '%" + searchContent + "%'";
		}
		sql = sql + " group by EN_ID_CS,EN_NAME_CS,BELONGSEPA,WEIGHT,AMOUNT";
		page = Db.paginate(pn, ps, "select EN_ID_CS,EN_NAME_CS,BELONGSEPA,isnull(WEIGHT, 0) WEIGHT, isnull(AMOUNT, 0) AMOUNT", sql);
		List<Record> recordList = page.getList();
		if(recordList.size() > 0){
			for(Record record : recordList){
				map = new HashMap<String, Object>();
				map.put("EN_NAME_CS", record.get("EN_NAME_CS"));
				if(record.get("BELONGSEPA") != null){
					map.put("SEPANAME", convert(cityList, record.get("BELONGSEPA")));
				}else{
					map.put("SEPANAME", "");
				}
				map.put("WEIGHT", record.get("WEIGHT"));
				map.put("COUNT", record.get("AMOUNT"));
				list.add(map);
			}
		}
		resMap.put("dataList", list);
		resMap.put("num", queryNumForCS(searchContent, ENDTIME, ROLEID, userType, orgCode, epId));
		resMap.put("totalPage", page.getTotalPage());
		resMap.put("totalRow", page.getTotalRow());
		return resMap;
	}
	
	/**
	 * @author weizanting
	 * @date 20170622
	 * 方法：查询转移汇总（按产生单位）--开始时间、结束时间
	 */
	public Map<String, Object> queryTransferSummaryForCS(String BEGINTIME, String ENDTIME, int pn, int ps, Object searchContent, String ROLEID, String userType, String orgCode, String epId){
		Map<String, Object> resMap = new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = null;
		Page<Record> page = null;
		String sql = "";
		if("SJSPROLE".equals(ROLEID)){		//市级管理员
			sql = " from (select EN_ID_CS,EN_NAME_CS,BELONGSEPA,sum(WEIGHT) as WEIGHT,sum(AMOUNT) as AMOUNT from (select a.EN_ID_CS,a.EN_NAME_CS,b.BELONGSEPA,cast(a.UNIT_NUM as numeric(10,2)) as WEIGHT,a.COUNT as AMOUNT,a.ACTIONDATE,a.sysdate from WOBO_TRANSFER_BILL a left join WOBO_ENTERPRISE b on a.EN_ID_CS = b.EP_ID where b.STATUS = '1' and a.STATUS in ('4', '5') and ((a.ACTIONDATE >= '" + BEGINTIME + "' and a.ACTIONDATE <= '" + ENDTIME + "') or (a.sysdate >= '" + BEGINTIME + "' and a.sysdate <= '" + ENDTIME + "')) group by a.EN_ID_CS,a.EN_NAME_CS,b.BELONGSEPA,a.UNIT_NUM,a.COUNT,a.ACTIONDATE,a.sysdate) as A group by EN_ID_CS,EN_NAME_CS,BELONGSEPA) as B";
		}
		if("QJSPROLE".equals(ROLEID)){		//区级管理员
			sql = " from (select EN_ID_CS,EN_NAME_CS,BELONGSEPA,sum(WEIGHT) as WEIGHT,sum(AMOUNT) as AMOUNT from (select a.EN_ID_CS,a.EN_NAME_CS,b.BELONGSEPA,cast(a.UNIT_NUM as numeric(10,2)) as WEIGHT,a.COUNT as AMOUNT,a.ACTIONDATE,a.sysdate from WOBO_TRANSFER_BILL a left join WOBO_ENTERPRISE b on a.EN_ID_CS = b.EP_ID where b.STATUS = '1' and a.STATUS in ('4', '5') and ((a.ACTIONDATE >= '" + BEGINTIME + "' and a.ACTIONDATE <= '" + ENDTIME + "') or (a.sysdate >= '" + BEGINTIME + "' and a.sysdate <= '" + ENDTIME + "')) group by a.EN_ID_CS,a.EN_NAME_CS,b.BELONGSEPA,a.UNIT_NUM,a.COUNT,a.ACTIONDATE,a.sysdate) as A where BELONGSEPA = '" + orgCode + "' group by EN_ID_CS,EN_NAME_CS,BELONGSEPA) as B";
		}
		if("epAdminCs".equals(userType)){		//医疗单位管理员
			sql = " from (select EN_ID_CS,EN_NAME_CS,BELONGSEPA,sum(WEIGHT) as WEIGHT,sum(AMOUNT) as AMOUNT from (select a.EN_ID_CS,a.EN_NAME_CS,b.BELONGSEPA,cast(a.UNIT_NUM as numeric(10,2)) as WEIGHT,a.COUNT as AMOUNT,a.ACTIONDATE,a.sysdate from WOBO_TRANSFER_BILL a left join WOBO_ENTERPRISE b on a.EN_ID_CS = b.EP_ID where b.STATUS = '1' and a.STATUS in ('4', '5') and ((a.ACTIONDATE >= '" + BEGINTIME + "' and a.ACTIONDATE <= '" + ENDTIME + "') or (a.sysdate >= '" + BEGINTIME + "' and a.sysdate <= '" + ENDTIME + "')) group by a.EN_ID_CS,a.EN_NAME_CS,b.BELONGSEPA,a.UNIT_NUM,a.COUNT,a.ACTIONDATE,a.sysdate) as A  where EN_ID_CS = '" + epId + "' group by EN_ID_CS,EN_NAME_CS,BELONGSEPA) as B";
		}
		if("epAdminCz".equals(userType)){		//医疗处置单位管理员
			sql = " from (select EN_ID_CS,EN_NAME_CS,BELONGSEPA,sum(WEIGHT) as WEIGHT,sum(AMOUNT) as AMOUNT from (select a.EN_ID_CS,a.EN_NAME_CS,b.BELONGSEPA,cast(a.UNIT_NUM as numeric(10,2)) as WEIGHT,a.COUNT as AMOUNT,a.ACTIONDATE,a.sysdate from WOBO_TRANSFER_BILL a left join WOBO_ENTERPRISE b on a.EN_ID_CS = b.EP_ID,WOBO_TRANSFER_BIG c  where b.STATUS = '1' and a.STATUS in ('4', '5') and a.TG_ID = c.TG_ID and c.EN_ID_CZ = '" + epId + "' and ((a.ACTIONDATE >= '" + BEGINTIME + "' and a.ACTIONDATE <= '" + ENDTIME + "') or (a.sysdate >= '" + BEGINTIME + "' and a.sysdate <= '" + ENDTIME + "')) group by a.EN_ID_CS,a.EN_NAME_CS,b.BELONGSEPA,a.UNIT_NUM,a.COUNT,a.ACTIONDATE,a.sysdate) as A group by EN_ID_CS,EN_NAME_CS,BELONGSEPA) as B";
		}
		if(searchContent != null && !"".equals(searchContent)){
			sql = sql + " where EN_NAME_CS like '%" + searchContent + "%'";
		}
		sql = sql + " group by EN_ID_CS,EN_NAME_CS,BELONGSEPA,WEIGHT,AMOUNT";
		page = Db.paginate(pn, ps, "select EN_ID_CS,EN_NAME_CS,BELONGSEPA,isnull(WEIGHT, 0) WEIGHT, isnull(AMOUNT, 0) AMOUNT", sql);
		List<Record> recordList = page.getList();
		if(recordList.size() > 0){
			for(Record record : recordList){
				map = new HashMap<String, Object>();
				map.put("EN_NAME_CS", record.get("EN_NAME_CS"));
				if(record.get("BELONGSEPA") != null){
					map.put("SEPANAME", convert(cityList, record.get("BELONGSEPA")));
				}else{
					map.put("SEPANAME", "");
				}
				map.put("WEIGHT", record.get("WEIGHT"));
				map.put("COUNT", record.get("AMOUNT"));
				list.add(map);
			}
		}
		resMap.put("dataList", list);
		resMap.put("num", queryNumForCS(searchContent, BEGINTIME, ENDTIME, ROLEID, userType, orgCode, epId));
		resMap.put("totalPage", page.getTotalPage());
		resMap.put("totalRow", page.getTotalRow());
		return resMap;
	}
	
	/**
	 * @author weizanting
	 * @date 20170623
	 * 方法：查询转移汇总（按产生单位）--全部
	 */
	public Map<String, Object> queryNumForCS(Object searchContent, String ROLEID, String userType, String orgCode, String epId){
		String sql = "select * from (select isnull(count(distinct EN_ID_CS), 0) CS_NUM,isnull(sum(weight), 0) WEIGHT,isnull(sum(amount), 0) AMOUNT from (select a.EN_ID_CS, EN_NAME_CS, sum(cast(a.UNIT_NUM as numeric(10,2))) as weight, sum(a.COUNT) as amount from WOBO_TRANSFER_BILL a, WOBO_ENTERPRISE b where a.EN_ID_CS = b.EP_ID and a.STATUS in ('4','5')";
		if("SJSPROLE".equals(ROLEID)){		//市级管理员
			if(searchContent != null && !"".equals(searchContent)){
				sql = sql + " and a.EN_NAME_CS like '%" + searchContent + "%'";
			}
		}
		if("QJSPROLE".equals(ROLEID)){		//区级管理员
			sql = sql + " and b.BELONGSEPA = '" + orgCode + "'";
			if(searchContent != null && !"".equals(searchContent)){
				sql = sql + " and a.EN_NAME_CS like '%" + searchContent + "%'";
			}
		}
		if("epAdminCs".equals(userType)){		//医疗单位管理员
			sql = sql + " and b.EP_ID = '" + epId + "'";
			if(searchContent != null && !"".equals(searchContent)){
				sql = sql + " and a.EN_NAME_CS like '%" + searchContent + "%'";
			}
		}
		if("epAdminCz".equals(userType)){		//医疗处置单位管理员
			sql = "select * from (select isnull(count(distinct EN_ID_CS), 0) CS_NUM,isnull(sum(weight), 0) WEIGHT,isnull(sum(amount), 0) AMOUNT from (select a.EN_ID_CS, sum(cast(a.UNIT_NUM as numeric(10,2))) as weight, sum(a.COUNT) as amount from WOBO_TRANSFER_BILL a, WOBO_ENTERPRISE b,WOBO_TRANSFER_BIG c where a.EN_ID_CS = b.EP_ID and a.TG_ID = c.TG_ID and a.STATUS in ('4','5') and c.EN_ID_CZ = '" + epId + "'";
			if(searchContent != null && !"".equals(searchContent)){
				sql = sql + " and a.EN_NAME_CS like '%" + searchContent + "%'";
			}
		}
		sql = sql + " group by a.EN_ID_CS,a.EN_NAME_CS) as A) as B";
		Record record = Db.findFirst(sql);
		Map<String, Object> map = new HashMap<String, Object>();
		if(record != null){
			map.put("CS_NUM", record.get("CS_NUM"));
			map.put("WEIGHT", record.get("WEIGHT"));
			map.put("AMOUNT", record.get("AMOUNT"));
		}
		return map;
	}
	
	/**
	 * @author weizanting
	 * @date 20170623
	 * 方法：查询转移汇总（按产生单位）--开始时间
	 */
	public Map<String, Object> queryNumForCS(String BEGINTIME, Object searchContent, String ROLEID, String userType, String orgCode, String epId){
		String sql = "select * from (select isnull(count(distinct EN_ID_CS), 0) CS_NUM,isnull(sum(weight), 0) WEIGHT,isnull(sum(amount), 0) AMOUNT from (select a.EN_ID_CS,a.EN_NAME_CS, sum(cast(a.UNIT_NUM as numeric(10,2))) as weight, sum(a.COUNT) as amount from WOBO_TRANSFER_BILL a, WOBO_ENTERPRISE b where a.EN_ID_CS = b.EP_ID and a.STATUS in ('4','5') and (a.ACTIONDATE >= '" + BEGINTIME +"' or a.sysdate >= '" + BEGINTIME +"')";
		if("SJSPROLE".equals(ROLEID)){		//市级管理员
			if(searchContent != null && !"".equals(searchContent)){
				sql = sql + " and a.EN_NAME_CS like '%" + searchContent + "%'";
			}
		}
		if("QJSPROLE".equals(ROLEID)){		//区级管理员
			sql = sql + " and b.BELONGSEPA = '" + orgCode + "'";
			if(searchContent != null && !"".equals(searchContent)){
				sql = sql + " and a.EN_NAME_CS like '%" + searchContent + "%'";
			}
		}
		if("epAdminCs".equals(userType)){		//医疗单位管理员
			sql = sql + " and b.EP_ID = '" + epId + "'";
			if(searchContent != null && !"".equals(searchContent)){
				sql = sql + " and a.EN_NAME_CS like '%" + searchContent + "%'";
			}
		}
		if("epAdminCz".equals(userType)){		//医疗处置单位管理员
			sql = "select * from (select isnull(count(distinct EN_ID_CS), 0) CS_NUM,isnull(sum(weight), 0) WEIGHT,isnull(sum(amount), 0) AMOUNT from (select a.EN_ID_CS, sum(cast(a.UNIT_NUM as numeric(10,2))) as weight, sum(a.COUNT) as amount from WOBO_TRANSFER_BILL a, WOBO_ENTERPRISE b,WOBO_TRANSFER_BIG c where a.EN_ID_CS = b.EP_ID and a.TG_ID = c.TG_ID and a.STATUS in ('4','5') and c.EN_ID_CZ = '" + epId + "' and (a.ACTIONDATE >= '" + BEGINTIME +"' or a.sysdate >= '" + BEGINTIME +"')";
			if(searchContent != null && !"".equals(searchContent)){
				sql = sql + " and a.EN_NAME_CS like '%" + searchContent + "%'";
			}
		}
		sql = sql + " group by a.EN_ID_CS,a.EN_NAME_CS) as A) as B";
		Record record = Db.findFirst(sql);
		Map<String, Object> map = new HashMap<String, Object>();
		if(record != null){
			map.put("CS_NUM", record.get("CS_NUM"));
			map.put("WEIGHT", record.get("WEIGHT"));
			map.put("AMOUNT", record.get("AMOUNT"));
		}
		return map;
	}
	
	/**
	 * @author weizanting
	 * @date 20170623
	 * 方法：查询转移汇总（按产生单位）--结束时间
	 */
	public Map<String, Object> queryNumForCS(Object searchContent, String ENDTIME, String ROLEID, String userType, String orgCode, String epId){
		String sql = "select * from (select isnull(count(distinct EN_ID_CS), 0) CS_NUM,isnull(sum(weight), 0) WEIGHT,isnull(sum(amount), 0) AMOUNT from (select a.EN_ID_CS,a.EN_NAME_CS, sum(cast(a.UNIT_NUM as numeric(10,2))) as weight, sum(a.COUNT) as amount from WOBO_TRANSFER_BILL a, WOBO_ENTERPRISE b";
		if("SJSPROLE".equals(ROLEID)){		//市级管理员
			sql = sql + " where a.EN_ID_CS = b.EP_ID and a.STATUS in ('4','5') and (a.ACTIONDATE <= '" + ENDTIME +"' or a.sysdate <= '" + ENDTIME +"')";
			if(searchContent != null && !"".equals(searchContent)){
				sql = sql + " and a.EN_NAME_CS like '%" + searchContent + "%'";
			}
		}
		if("QJSPROLE".equals(ROLEID)){		//区级管理员
			sql = sql + " where a.EN_ID_CS = b.EP_ID and a.STATUS in ('4','5') and b.BELONGSEPA = '" + orgCode + "' and (a.ACTIONDATE <= '" + ENDTIME +"' or a.sysdate <= '" + ENDTIME +"')";
			if(searchContent != null && !"".equals(searchContent)){
				sql = sql + " and a.EN_NAME_CS like '%" + searchContent + "%'";
			}
		}
		if("epAdminCs".equals(userType)){		//医疗单位管理员
			sql = sql + " where a.EN_ID_CS = b.EP_ID and a.STATUS in ('4','5') and b.EP_ID = '" + epId + "' and (a.ACTIONDATE <= '" + ENDTIME +"' or a.sysdate <= '" + ENDTIME +"')";
			if(searchContent != null && !"".equals(searchContent)){
				sql = sql + " and a.EN_NAME_CS like '%" + searchContent + "%'";
			}
		}
		if("epAdminCz".equals(userType)){		//医疗处置单位管理员
			sql = sql + ",WOBO_TRANSFER_BIG c where a.EN_ID_CS = b.EP_ID and a.TG_ID = c.TG_ID and a.STATUS in ('4','5') and c.EN_ID_CZ = '" + epId + "' and ((a.ACTIONDATE <= '" + ENDTIME +"' or a.sysdate <= '" + ENDTIME +"'))";
			if(searchContent != null && !"".equals(searchContent)){
				sql = sql + " and a.EN_NAME_CS like '%" + searchContent + "%'";
			}
		}
		sql = sql + " group by a.EN_ID_CS,a.EN_NAME_CS) as A) as B";
		Record record = Db.findFirst(sql);
		Map<String, Object> map = new HashMap<String, Object>();
		if(record != null){
			map.put("CS_NUM", record.get("CS_NUM"));
			map.put("WEIGHT", record.get("WEIGHT"));
			map.put("AMOUNT", record.get("AMOUNT"));
		}
		return map;
	}
	
	/**
	 * @author weizanting
	 * @date 20170623
	 * 方法：查询转移汇总（按产生单位）--开始时间、结束时间
	 */
	public Map<String, Object> queryNumForCS(Object searchContent, String BEGINTIME, String ENDTIME, String ROLEID, String userType, String orgCode, String epId){
		String sql = "";
		if("SJSPROLE".equals(ROLEID)){		//市级管理员
			sql = "select isnull(count(CS_NUM), 0) CS_NUM,isnull(sum(WEIGHT), 0) WEIGHT,isnull(sum(AMOUNT), 0) AMOUNT from (select isnull(count(distinct EN_ID_CS), 0) CS_NUM,isnull(sum(weight), 0) WEIGHT,isnull(sum(amount), 0) AMOUNT from(select a.EN_ID_CS,a.EN_NAME_CS, sum(cast(a.UNIT_NUM as numeric(10,2))) as weight, sum(a.COUNT) as amount, b.BELONGSEPA,a.ACTIONDATE,a.sysdate from WOBO_TRANSFER_BILL a left join WOBO_ENTERPRISE b on a.EN_ID_CS = b.EP_ID where a.STATUS in ('4','5') and ((a.ACTIONDATE >= '" + BEGINTIME + "' and a.ACTIONDATE <= '" + ENDTIME + "') or (a.sysdate >= '" + BEGINTIME + "' and a.sysdate <= '" + ENDTIME + "'))";
			if(searchContent != null && !"".equals(searchContent)){
				sql = sql + " and a.EN_NAME_CS like '%" + searchContent + "%'";
			}
			sql = sql + " group by a.EN_ID_CS,a.EN_NAME_CS,b.BELONGSEPA,a.ACTIONDATE,a.sysdate) as A group by EN_ID_CS,BELONGSEPA) as B";
		}
		if("QJSPROLE".equals(ROLEID)){		//区级管理员
			sql = "select isnull(count(CS_NUM), 0) CS_NUM,isnull(sum(WEIGHT), 0) WEIGHT,isnull(sum(AMOUNT), 0) AMOUNT from (select isnull(count(distinct EN_ID_CS), 0) CS_NUM,isnull(sum(weight), 0) WEIGHT,isnull(sum(amount), 0) AMOUNT from(select a.EN_ID_CS,a.EN_NAME_CS, sum(cast(a.UNIT_NUM as numeric(10,2))) as weight, sum(a.COUNT) as amount, b.BELONGSEPA,a.ACTIONDATE,a.sysdate from WOBO_TRANSFER_BILL a left join WOBO_ENTERPRISE b on a.EN_ID_CS = b.EP_ID where a.STATUS in ('4','5') and ((a.ACTIONDATE >= '" + BEGINTIME + "' and a.ACTIONDATE <= '" + ENDTIME + "') or (a.sysdate >= '" + BEGINTIME + "' and a.sysdate <= '" + ENDTIME + "'))";
			if(searchContent != null && !"".equals(searchContent)){
				sql = sql + " and a.EN_NAME_CS like '%" + searchContent + "%'";
			}
			sql = sql + " group by a.EN_ID_CS,a.EN_NAME_CS,b.BELONGSEPA,a.ACTIONDATE,a.sysdate) as A where BELONGSEPA = '" + orgCode + "' group by EN_ID_CS,BELONGSEPA) as B";
		}
		if("epAdminCs".equals(userType)){		//医疗单位管理员
			sql = "select * from (select isnull(count(distinct EN_ID_CS), 0) CS_NUM,isnull(sum(weight), 0) WEIGHT,isnull(sum(amount), 0) AMOUNT from (select a.EN_ID_CS,a.EN_NAME_CS, cast(a.UNIT_NUM as numeric(10,2)) as weight, a.COUNT as amount,a.ACTIONDATE,a.sysdate from WOBO_TRANSFER_BILL a,WOBO_ENTERPRISE b where a.EN_ID_CS = b.EP_ID and a.STATUS in ('4','5') and ((a.ACTIONDATE >= '" + BEGINTIME + "' and a.ACTIONDATE <= '" + ENDTIME + "') or (a.sysdate > '" + BEGINTIME + "' and a.sysdate < '" + ENDTIME + "'))";
			if(searchContent != null && !"".equals(searchContent)){
				sql = sql + " and a.EN_NAME_CS like '%" + searchContent + "%'";
			}
			sql = sql + " group by a.EN_ID_CS,a.EN_NAME_CS,a.UNIT_NUM,a.COUNT,a.ACTIONDATE,a.sysdate) as A where EN_ID_CS = '" + epId + "' ) as B";
		}
		if("epAdminCz".equals(userType)){		//医疗处置单位管理员
			sql = "select * from (select isnull(count(distinct EN_ID_CS), 0) CS_NUM,isnull(sum(weight), 0) WEIGHT,isnull(sum(amount), 0) AMOUNT from (select a.EN_ID_CS,a.EN_NAME_CS, cast(a.UNIT_NUM as numeric(10,2)) as weight, a.COUNT as amount,a.ACTIONDATE,a.sysdate from WOBO_TRANSFER_BILL a, WOBO_ENTERPRISE b,WOBO_TRANSFER_BIG c where a.EN_ID_CS = b.EP_ID and a.TG_ID = c.TG_ID and a.STATUS in ('4','5') and c.EN_ID_CZ = '" + epId + "' and ((a.ACTIONDATE >= '" + BEGINTIME + "' and a.ACTIONDATE <= '" + ENDTIME + "') or (a.sysdate > '" + BEGINTIME + "' and a.sysdate < '" + ENDTIME + "'))";
			if(searchContent != null && !"".equals(searchContent)){
				sql = sql + " and a.EN_NAME_CS like '%" + searchContent + "%'";
			}
			sql = sql + " group by EN_ID_CS,a.EN_NAME_CS,a.UNIT_NUM,a.COUNT,a.ACTIONDATE,a.sysdate) as A) as B";
		}
		Record record = Db.findFirst(sql);
		Map<String, Object> map = new HashMap<String, Object>();
		if(record != null){
			map.put("CS_NUM", record.get("CS_NUM"));
			map.put("WEIGHT", record.get("WEIGHT"));
			map.put("AMOUNT", record.get("AMOUNT"));
		}
		return map;
	}
	
	/**
	 * @author weizanting
	 * @date 20170623
	 * 方法：查询转移汇总（按处置单位）--全部
	 */
	public Map<String, Object> queryTransferSummaryForCZ(int pn, int ps, Object searchContent, String ROLEID, String orgCode){
		Map<String, Object> resMap = new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = null;
		Page<Record> page = null;
		String sql = "";
		if("SJSPROLE".equals(ROLEID)){		//市级管理员
			sql = " from (select b.EN_ID_CZ,b.EN_NAME_CZ,c.BELONGSEPA,sum(a.COUNT) AMOUNT from WOBO_TRANSFER_BILL a,WOBO_TRANSFER_BIG b,WOBO_ENTERPRISE c where a.TG_ID = b.TG_ID and b.EN_ID_CZ = c.EP_ID and c.STATUS = '1' and b.STATUS in ('4', '5') group by b.EN_ID_CZ,b.EN_NAME_CZ,c.BELONGSEPA) as B LEFT JOIN (select a.EN_ID_CZ,a.EN_NAME_CZ,sum(cast(a.UNIT_NUM as numeric(10,2))) WEIGHT from WOBO_TRANSFER_BIG a,WOBO_ENTERPRISE b where a.EN_ID_CZ = b.EP_ID group by a.EN_ID_CZ,a.EN_NAME_CZ) as A on A.EN_ID_CZ = B.EN_ID_CZ";
		}else{		//区级管理员
			sql = " from (select b.EN_ID_CZ,b.EN_NAME_CZ,c.BELONGSEPA,sum(a.COUNT) AMOUNT from WOBO_TRANSFER_BILL a,WOBO_TRANSFER_BIG b,WOBO_ENTERPRISE c where a.TG_ID = b.TG_ID and a.EN_ID_CS = c.EP_ID and c.STATUS = '1' and b.STATUS in ('4', '5') and c.IF_PRODUCE = '1' and c.BELONGSEPA = '" + orgCode + "' group by b.EN_ID_CZ,b.EN_NAME_CZ,c.BELONGSEPA) as B LEFT JOIN (select EN_ID_CZ,EN_NAME_CZ,sum(cast(WEIGHT as numeric(10,2))) WEIGHT from (select a.EN_ID_CZ,a.EN_NAME_CZ,cast(c.UNIT_NUM as numeric(10,2)) WEIGHT from WOBO_TRANSFER_BIG a,WOBO_ENTERPRISE b,WOBO_TRANSFER_BILL c where a.TG_ID = c.TG_ID and c.EN_ID_CS = b.EP_ID and b.IF_PRODUCE = '1' and b.BELONGSEPA = '" + orgCode + "' group by a.EN_ID_CZ,a.EN_NAME_CZ,c.UNIT_NUM) as C group by C.EN_ID_CZ,C.EN_NAME_CZ) as A on A.EN_ID_CZ = B.EN_ID_CZ";
		}
		if(searchContent != null && !"".equals(searchContent)){
			sql = sql + " where B.EN_NAME_CZ like '%" + searchContent + "%' or BELONGSEPA like '%" + searchContent + "%'";
		}
		page = Db.paginate(pn, ps, "select B.*,isnull(A.WEIGHT, 0) as WEIGHT ", sql);
		List<Record> recordList = page.getList();
		if(recordList.size() > 0){
			for(Record record : recordList){
				map = new HashMap<String, Object>();
				map.put("EN_NAME_CS", record.get("EN_NAME_CZ"));
				if(record.get("BELONGSEPA") != null){
					map.put("SEPANAME", convert(cityList, record.get("BELONGSEPA")));
				}else{
					map.put("SEPANAME", "");
				}
				map.put("WEIGHT", record.get("WEIGHT"));
				map.put("COUNT", record.get("AMOUNT"));
				list.add(map);
			}
		}
		resMap.put("dataList", list);
		resMap.put("num", queryNumForCZ(searchContent, ROLEID, orgCode));
		resMap.put("totalPage", page.getTotalPage());
		resMap.put("totalRow", page.getTotalRow());
		return resMap;
	}
	
	/**
	 * @author weizanting
	 * @date 20170623
	 * 方法：查询转移汇总（按处置单位）--开始时间
	 */
	public Map<String, Object> queryTransferSummaryForCZ(String BEGINTIME, int pn, int ps, Object searchContent, String ROLEID, String orgCode){
		Map<String, Object> resMap = new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = null;
		Page<Record> page = null;
		String sql = "";
		if("SJSPROLE".equals(ROLEID)){		//市级管理员
			sql = " from (select b.EN_ID_CZ,b.EN_NAME_CZ,c.BELONGSEPA,sum(a.COUNT) AMOUNT from WOBO_TRANSFER_BILL a,WOBO_TRANSFER_BIG b,WOBO_ENTERPRISE c where a.TG_ID = b.TG_ID and b.EN_ID_CZ = c.EP_ID and c.STATUS = '1' and b.STATUS in ('4', '5') and (b.ACTIONDATE >= '" + BEGINTIME + "' or b.sysdate >= '" + BEGINTIME + "') group by b.EN_ID_CZ,b.EN_NAME_CZ,c.BELONGSEPA) as B LEFT JOIN (select a.EN_ID_CZ,a.EN_NAME_CZ,sum(cast(a.UNIT_NUM as numeric(10,2))) WEIGHT from WOBO_TRANSFER_BIG a,WOBO_ENTERPRISE b where a.EN_ID_CZ = b.EP_ID and (a.ACTIONDATE >= '" + BEGINTIME + "' or a.sysdate >= '" + BEGINTIME + "') group by a.EN_ID_CZ,a.EN_NAME_CZ) as A on A.EN_ID_CZ = B.EN_ID_CZ";
		}else{		//区级管理员
			sql = " from (select b.EN_ID_CZ,b.EN_NAME_CZ,c.BELONGSEPA,sum(a.COUNT) AMOUNT from WOBO_TRANSFER_BILL a,WOBO_TRANSFER_BIG b,WOBO_ENTERPRISE c where a.TG_ID = b.TG_ID and a.EN_ID_CS = c.EP_ID and c.STATUS = '1' and b.STATUS in ('4', '5') and c.IF_PRODUCE = '1' and c.BELONGSEPA = '" + orgCode + "' and (b.ACTIONDATE >= '" + BEGINTIME + "' or b.sysdate >= '" + BEGINTIME + "') group by b.EN_ID_CZ,b.EN_NAME_CZ,c.BELONGSEPA) as B LEFT JOIN (select EN_ID_CZ,EN_NAME_CZ,sum(cast(WEIGHT as numeric(10,2))) WEIGHT from (select a.EN_ID_CZ,a.EN_NAME_CZ,cast(c.UNIT_NUM as numeric(10,2)) WEIGHT from WOBO_TRANSFER_BIG a,WOBO_ENTERPRISE b,WOBO_TRANSFER_BILL c where a.TG_ID = c.TG_ID and c.EN_ID_CS = b.EP_ID and b.IF_PRODUCE = '1' and b.BELONGSEPA = '" + orgCode + "' and (a.ACTIONDATE >= '" + BEGINTIME + "' or a.sysdate >= '" + BEGINTIME + "') group by a.EN_ID_CZ,a.EN_NAME_CZ,c.UNIT_NUM) as C group by C.EN_ID_CZ,C.EN_NAME_CZ) as A on A.EN_ID_CZ = B.EN_ID_CZ";
		}
		if(searchContent != null && !"".equals(searchContent)){
			sql = sql + " where B.EN_NAME_CZ like '%" + searchContent + "%'";
		}
		page = Db.paginate(pn, ps, "select B.*,isnull(A.WEIGHT, 0) as WEIGHT ", sql);
		List<Record> recordList = page.getList();
		if(recordList.size() > 0){
			for(Record record : recordList){
				map = new HashMap<String, Object>();
				map.put("EN_NAME_CS", record.get("EN_NAME_CZ"));
				if(record.get("BELONGSEPA") != null){
					map.put("SEPANAME", convert(cityList, record.get("BELONGSEPA")));
				}else{
					map.put("SEPANAME", "");
				}
				map.put("WEIGHT", record.get("WEIGHT"));
				map.put("COUNT", record.get("AMOUNT"));
				list.add(map);
			}
		}
		resMap.put("dataList", list);
		resMap.put("num", queryNumForCZ(BEGINTIME, searchContent, ROLEID, orgCode));
		resMap.put("totalPage", page.getTotalPage());
		resMap.put("totalRow", page.getTotalRow());
		return resMap;
	}
	
	/**
	 * @author weizanting
	 * @date 20170623
	 * 方法：查询转移汇总（按处置单位）--结束时间
	 */
	public Map<String, Object> queryTransferSummaryForCZ(int pn, int ps, String ENDTIME, Object searchContent, String ROLEID, String orgCode){
		Map<String, Object> resMap = new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = null;
		Page<Record> page = null;
		String sql = "";
		if("SJSPROLE".equals(ROLEID)){	//市级管理员
			sql = " from (select b.EN_ID_CZ,b.EN_NAME_CZ,c.BELONGSEPA,sum(a.COUNT) AMOUNT from WOBO_TRANSFER_BILL a,WOBO_TRANSFER_BIG b,WOBO_ENTERPRISE c where a.TG_ID = b.TG_ID and b.EN_ID_CZ = c.EP_ID and c.STATUS = '1' and b.STATUS in ('4', '5') and (b.ACTIONDATE <= '" + ENDTIME + "' or b.sysdate <= '" + ENDTIME + "') group by b.EN_ID_CZ,b.EN_NAME_CZ,c.BELONGSEPA) as B LEFT JOIN (select a.EN_ID_CZ,a.EN_NAME_CZ,sum(cast(a.UNIT_NUM as numeric(10,2))) WEIGHT from WOBO_TRANSFER_BIG a,WOBO_ENTERPRISE b where a.EN_ID_CZ = b.EP_ID and (a.ACTIONDATE <= '" + ENDTIME + "' or a.sysdate <= '" + ENDTIME + "') group by a.EN_ID_CZ,a.EN_NAME_CZ) as A on A.EN_ID_CZ = B.EN_ID_CZ";
		}else{	//区级管理员
			sql = " from (select b.EN_ID_CZ,b.EN_NAME_CZ,c.BELONGSEPA,sum(a.COUNT) AMOUNT from WOBO_TRANSFER_BILL a,WOBO_TRANSFER_BIG b,WOBO_ENTERPRISE c where a.TG_ID = b.TG_ID and a.EN_ID_CS = c.EP_ID and c.STATUS = '1' and b.STATUS in ('4', '5') and c.IF_PRODUCE = '1' and c.BELONGSEPA = '" + orgCode + "' and (b.ACTIONDATE <= '" + ENDTIME + "' or b.sysdate <= '" + ENDTIME + "') group by b.EN_ID_CZ,b.EN_NAME_CZ,c.BELONGSEPA) as B LEFT JOIN (select EN_ID_CZ,EN_NAME_CZ,sum(cast(WEIGHT as numeric(10,2))) WEIGHT from (select a.EN_ID_CZ,a.EN_NAME_CZ,cast(c.UNIT_NUM as numeric(10,2)) WEIGHT from WOBO_TRANSFER_BIG a,WOBO_ENTERPRISE b,WOBO_TRANSFER_BILL c where a.TG_ID = c.TG_ID and c.EN_ID_CS = b.EP_ID and b.IF_PRODUCE = '1' and b.BELONGSEPA = '" + orgCode + "' and (a.ACTIONDATE <= '" + ENDTIME + "' or a.sysdate <= '" + ENDTIME + "') group by a.EN_ID_CZ,a.EN_NAME_CZ,c.UNIT_NUM) as C group by C.EN_ID_CZ,C.EN_NAME_CZ) as A on A.EN_ID_CZ = B.EN_ID_CZ";
		}
		if(searchContent != null && !"".equals(searchContent)){
			sql = sql + " where B.EN_NAME_CZ like '%" + searchContent + "%'";
		}
		page = Db.paginate(pn, ps, "select B.*,isnull(A.WEIGHT, 0) as WEIGHT ", sql);
		List<Record> recordList = page.getList();
		if(recordList.size() > 0){
			for(Record record : recordList){
				map = new HashMap<String, Object>();
				map.put("EN_NAME_CS", record.get("EN_NAME_CZ"));
				if(record.get("BELONGSEPA") != null){
					map.put("SEPANAME", convert(cityList, record.get("BELONGSEPA")));
				}else{
					map.put("SEPANAME", "");
				}
				map.put("WEIGHT", record.get("WEIGHT"));
				map.put("COUNT", record.get("AMOUNT"));
				list.add(map);
			}
		}
		resMap.put("dataList", list);
		resMap.put("num", queryNumForCZ(searchContent, ENDTIME, ROLEID, orgCode));
		resMap.put("totalPage", page.getTotalPage());
		resMap.put("totalRow", page.getTotalRow());
		return resMap;
	}
	
	/**
	 * @author weizanting
	 * @date 20170623
	 * 方法：查询转移汇总（按处置单位）--开始时间、结束时间
	 */
	public Map<String, Object> queryTransferSummaryForCZ(String BEGINTIME, String ENDTIME, int pn, int ps, Object searchContent, String ROLEID, String orgCode){
		Map<String, Object> resMap = new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = null;
		Page<Record> page = null;
		String sql = "";
		if("SJSPROLE".equals(ROLEID)){	//市级管理员
			sql = " from (select b.EN_ID_CZ,b.EN_NAME_CZ,c.BELONGSEPA,sum(a.COUNT) AMOUNT from WOBO_TRANSFER_BILL a,WOBO_TRANSFER_BIG b,WOBO_ENTERPRISE c where a.TG_ID = b.TG_ID and b.EN_ID_CZ = c.EP_ID and c.STATUS = '1' and b.STATUS in ('4', '5') and ((b.ACTIONDATE >= '" + BEGINTIME + "' and b.ACTIONDATE <= '" + ENDTIME + "') or (b.sysdate >= '" + BEGINTIME + "' and b.sysdate <= '" + ENDTIME + "')) group by b.EN_ID_CZ,b.EN_NAME_CZ,c.BELONGSEPA) as B LEFT JOIN (select a.EN_ID_CZ,a.EN_NAME_CZ,sum(cast(a.UNIT_NUM as numeric(10,2))) WEIGHT from WOBO_TRANSFER_BIG a,WOBO_ENTERPRISE b where a.EN_ID_CZ = b.EP_ID and ((a.ACTIONDATE >= '" + BEGINTIME + "' and a.ACTIONDATE <= '" + ENDTIME + "') or (a.sysdate >= '" + BEGINTIME + "' and a.sysdate <= '" + ENDTIME + "')) group by a.EN_ID_CZ,a.EN_NAME_CZ) as A on A.EN_ID_CZ = B.EN_ID_CZ";
		}else{	//区级管理员
			sql = " from (select b.EN_ID_CZ,b.EN_NAME_CZ,c.BELONGSEPA,sum(a.COUNT) AMOUNT from WOBO_TRANSFER_BILL a,WOBO_TRANSFER_BIG b,WOBO_ENTERPRISE c where a.TG_ID = b.TG_ID and a.EN_ID_CS = c.EP_ID and c.STATUS = '1' and b.STATUS in ('4', '5') and c.IF_PRODUCE = '1' and c.BELONGSEPA = '" + orgCode + "' and ((b.ACTIONDATE >= '" + BEGINTIME + "' and b.ACTIONDATE <= '" + ENDTIME + "') or (b.sysdate >= '" + BEGINTIME + "' and b.sysdate <= '" + ENDTIME + "')) group by b.EN_ID_CZ,b.EN_NAME_CZ,c.BELONGSEPA) as B LEFT JOIN (select EN_ID_CZ,EN_NAME_CZ,sum(cast(WEIGHT as numeric(10,2))) WEIGHT from (select a.EN_ID_CZ,a.EN_NAME_CZ,cast(c.UNIT_NUM as numeric(10,2)) WEIGHT from WOBO_TRANSFER_BIG a,WOBO_ENTERPRISE b,WOBO_TRANSFER_BILL c where a.TG_ID = c.TG_ID and c.EN_ID_CS = b.EP_ID and b.IF_PRODUCE = '1' and b.BELONGSEPA = '" + orgCode + "' and ((a.ACTIONDATE >= '" + BEGINTIME + "' and a.ACTIONDATE <= '" + ENDTIME + "') or (a.sysdate >= '" + BEGINTIME + "' and a.sysdate <= '" + ENDTIME + "')) group by a.EN_ID_CZ,a.EN_NAME_CZ,c.UNIT_NUM) as C group by C.EN_ID_CZ,C.EN_NAME_CZ) as A on A.EN_ID_CZ = B.EN_ID_CZ";
		}
		if(searchContent != null && !"".equals(searchContent)){
			sql = sql + " where B.EN_NAME_CZ like '%" + searchContent + "%'";
		}
		page = Db.paginate(pn, ps, "select B.*,isnull(A.WEIGHT, 0) as WEIGHT ", sql);
		List<Record> recordList = page.getList();
		if(recordList.size() > 0){
			for(Record record : recordList){
				map = new HashMap<String, Object>();
				map.put("EN_NAME_CS", record.get("EN_NAME_CZ"));
				if(record.get("BELONGSEPA") != null){
					map.put("SEPANAME", convert(cityList, record.get("BELONGSEPA")));
				}else{
					map.put("SEPANAME", "");
				}
				map.put("WEIGHT", record.get("WEIGHT"));
				map.put("COUNT", record.get("AMOUNT"));
				list.add(map);
			}
		}
		resMap.put("dataList", list);
		resMap.put("num", queryNumForCZ(BEGINTIME, ENDTIME, searchContent, ROLEID, orgCode));
		resMap.put("totalPage", page.getTotalPage());
		resMap.put("totalRow", page.getTotalRow());
		return resMap;
	}
	
	/**
	 * @author weizanting
	 * @date 20170623
	 * 方法：查询转移汇总（按处置单位）--全部
	 */
	public Map<String, Object> queryNumForCZ(Object searchContent, String ROLEID, String orgCode){
		String sql = "select count(C.CZ_NUM) CZ_NUM,sum(C.WEIGHT) as WEIGHT,sum(C.AMOUNT) as AMOUNT from (select count(B.EN_ID_CZ) CZ_NUM,A.WEIGHT,B.AMOUNT ";
		if("SJSPROLE".equals(ROLEID)){	//市级管理员
			sql = sql + " from (select b.EN_ID_CZ,b.EN_NAME_CZ,c.BELONGSEPA,sum(a.COUNT) AMOUNT from WOBO_TRANSFER_BILL a,WOBO_TRANSFER_BIG b,WOBO_ENTERPRISE c where a.TG_ID = b.TG_ID and b.EN_ID_CZ = c.EP_ID and c.STATUS = '1' and b.STATUS in ('4', '5') group by b.EN_ID_CZ,b.EN_NAME_CZ,c.BELONGSEPA) as B LEFT JOIN (select a.EN_ID_CZ,a.EN_NAME_CZ,sum(cast(a.UNIT_NUM as numeric(10,2))) WEIGHT from WOBO_TRANSFER_BIG a,WOBO_ENTERPRISE b where a.EN_ID_CZ = b.EP_ID group by a.EN_ID_CZ,a.EN_NAME_CZ) as A on A.EN_ID_CZ = B.EN_ID_CZ";
		}else{	//区级管理员
			sql = sql + " from (select b.EN_ID_CZ,b.EN_NAME_CZ,c.BELONGSEPA,sum(a.COUNT) AMOUNT from WOBO_TRANSFER_BILL a,WOBO_TRANSFER_BIG b,WOBO_ENTERPRISE c where a.TG_ID = b.TG_ID and a.EN_ID_CS = c.EP_ID and c.STATUS = '1' and b.STATUS in ('4', '5') and c.IF_PRODUCE = '1' and c.BELONGSEPA = '" + orgCode + "' group by b.EN_ID_CZ,b.EN_NAME_CZ,c.BELONGSEPA) as B LEFT JOIN (select EN_ID_CZ,EN_NAME_CZ,sum(cast(WEIGHT as numeric(10,2))) WEIGHT from (select a.EN_ID_CZ,a.EN_NAME_CZ,cast(c.UNIT_NUM as numeric(10,2)) WEIGHT from WOBO_TRANSFER_BIG a,WOBO_ENTERPRISE b,WOBO_TRANSFER_BILL c where a.TG_ID = c.TG_ID and c.EN_ID_CS = b.EP_ID and b.IF_PRODUCE = '1' and b.BELONGSEPA = '" + orgCode + "' group by a.EN_ID_CZ,a.EN_NAME_CZ,c.UNIT_NUM) as C group by C.EN_ID_CZ,C.EN_NAME_CZ) as A on A.EN_ID_CZ = B.EN_ID_CZ";
		}
		if(searchContent != null && !"".equals(searchContent)){
			sql = sql + " where B.EN_NAME_CZ like '%" + searchContent + "%'";
		}
		Record record = Db.findFirst(sql + " group by A.WEIGHT,B.AMOUNT) as C");
		Map<String, Object> map = new HashMap<String, Object>();
		if(record != null){
			map.put("CZ_NUM", record.get("CZ_NUM"));
			map.put("WEIGHT", record.get("WEIGHT"));
			map.put("AMOUNT", record.get("AMOUNT"));
		}
		return map;
	}
	
	/**
	 * @author weizanting
	 * @date 20170623
	 * 方法：查询转移汇总（按处置单位）--开始时间
	 */
	public Map<String, Object> queryNumForCZ(String BEGINTIME, Object searchContent, String ROLEID, String orgCode){
		String sql = "select count(C.CZ_NUM) CZ_NUM,sum(C.WEIGHT) as WEIGHT,sum(C.AMOUNT) as AMOUNT from (select count(B.EN_ID_CZ) CZ_NUM,A.WEIGHT,B.AMOUNT ";
		if("SJSPROLE".equals(ROLEID)){		//市级管理员
			sql = sql +  " from (select b.EN_ID_CZ,b.EN_NAME_CZ,c.BELONGSEPA,sum(a.COUNT) AMOUNT from WOBO_TRANSFER_BILL a,WOBO_TRANSFER_BIG b,WOBO_ENTERPRISE c where a.TG_ID = b.TG_ID and b.EN_ID_CZ = c.EP_ID and c.STATUS = '1' and b.STATUS in ('4', '5') and (b.ACTIONDATE >= '" + BEGINTIME + "' or b.sysdate >= '" + BEGINTIME + "') group by b.EN_ID_CZ,b.EN_NAME_CZ,c.BELONGSEPA) as B LEFT JOIN (select a.EN_ID_CZ,a.EN_NAME_CZ,sum(cast(a.UNIT_NUM as numeric(10,2))) WEIGHT from WOBO_TRANSFER_BIG a,WOBO_ENTERPRISE b where a.EN_ID_CZ = b.EP_ID and (a.ACTIONDATE >= '" + BEGINTIME + "' or a.sysdate >= '" + BEGINTIME + "') group by a.EN_ID_CZ,a.EN_NAME_CZ) as A on A.EN_ID_CZ = B.EN_ID_CZ";
		}else{		//区级管理员
			sql = sql + " from (select b.EN_ID_CZ,b.EN_NAME_CZ,c.BELONGSEPA,sum(a.COUNT) AMOUNT from WOBO_TRANSFER_BILL a,WOBO_TRANSFER_BIG b,WOBO_ENTERPRISE c where a.TG_ID = b.TG_ID and a.EN_ID_CS = c.EP_ID and c.STATUS = '1' and b.STATUS in ('4', '5') and c.IF_PRODUCE = '1' and c.BELONGSEPA = '" + orgCode + "' and (b.ACTIONDATE >= '" + BEGINTIME + "' or b.sysdate >= '" + BEGINTIME + "') group by b.EN_ID_CZ,b.EN_NAME_CZ,c.BELONGSEPA) as B LEFT JOIN (select EN_ID_CZ,EN_NAME_CZ,sum(cast(WEIGHT as numeric(10,2))) WEIGHT from (select a.EN_ID_CZ,a.EN_NAME_CZ,cast(c.UNIT_NUM as numeric(10,2)) WEIGHT from WOBO_TRANSFER_BIG a,WOBO_ENTERPRISE b,WOBO_TRANSFER_BILL c where a.TG_ID = c.TG_ID and c.EN_ID_CS = b.EP_ID and b.IF_PRODUCE = '1' and b.BELONGSEPA = '" + orgCode + "' and (a.ACTIONDATE >= '" + BEGINTIME + "'  or a.sysdate >= '" + BEGINTIME + "') group by a.EN_ID_CZ,a.EN_NAME_CZ,c.UNIT_NUM) as C group by C.EN_ID_CZ,C.EN_NAME_CZ) as A on A.EN_ID_CZ = B.EN_ID_CZ";
		}
		if(searchContent != null && !"".equals(searchContent)){
			sql = sql + " where B.EN_NAME_CZ like '%" + searchContent + "%'";
		}
		Record record = Db.findFirst(sql + " group by A.WEIGHT,B.AMOUNT) as C");
		Map<String, Object> map = new HashMap<String, Object>();
		if(record != null){
			map.put("CZ_NUM", record.get("CZ_NUM"));
			map.put("WEIGHT", record.get("WEIGHT"));
			map.put("AMOUNT", record.get("AMOUNT"));
		}
		return map;
	}
	
	/**
	 * @author weizanting
	 * @date 20170623
	 * 方法：查询转移汇总（按处置单位）--结束时间
	 */
	public Map<String, Object> queryNumForCZ(Object searchContent, String ENDTIME, String ROLEID, String orgCode){
		String sql = "select count(C.CZ_NUM) CZ_NUM,sum(C.WEIGHT) as WEIGHT,sum(C.AMOUNT) as AMOUNT from (select count(B.EN_ID_CZ) CZ_NUM,A.WEIGHT,B.AMOUNT ";
		if("SJSPROLE".equals(ROLEID)){		//市级管理员
			sql = sql +  " from (select b.EN_ID_CZ,b.EN_NAME_CZ,c.BELONGSEPA,sum(a.COUNT) AMOUNT from WOBO_TRANSFER_BILL a,WOBO_TRANSFER_BIG b,WOBO_ENTERPRISE c where a.TG_ID = b.TG_ID and b.EN_ID_CZ = c.EP_ID and c.STATUS = '1' and b.STATUS in ('4', '5') and (b.ACTIONDATE <= '" + ENDTIME + "' or b.sysdate <= '" + ENDTIME + "') group by b.EN_ID_CZ,b.EN_NAME_CZ,c.BELONGSEPA) as B LEFT JOIN (select a.EN_ID_CZ,a.EN_NAME_CZ,sum(cast(a.UNIT_NUM as numeric(10,2))) WEIGHT from WOBO_TRANSFER_BIG a,WOBO_ENTERPRISE b where a.EN_ID_CZ = b.EP_ID and (a.ACTIONDATE <= '" + ENDTIME + "' or a.sysdate <= '" + ENDTIME + "') group by a.EN_ID_CZ,a.EN_NAME_CZ) as A on A.EN_ID_CZ = B.EN_ID_CZ";
		}else{		//区级管理员
			sql = sql + " from (select b.EN_ID_CZ,b.EN_NAME_CZ,c.BELONGSEPA,sum(a.COUNT) AMOUNT from WOBO_TRANSFER_BILL a,WOBO_TRANSFER_BIG b,WOBO_ENTERPRISE c where a.TG_ID = b.TG_ID and a.EN_ID_CS = c.EP_ID and c.STATUS = '1' and b.STATUS in ('4', '5') and c.IF_PRODUCE = '1' and c.BELONGSEPA = '" + orgCode + "' and (b.ACTIONDATE <= '" + ENDTIME + "' or b.sysdate <= '" + ENDTIME + "') group by b.EN_ID_CZ,b.EN_NAME_CZ,c.BELONGSEPA) as B LEFT JOIN (select EN_ID_CZ,EN_NAME_CZ,sum(cast(WEIGHT as numeric(10,2))) WEIGHT from (select a.EN_ID_CZ,a.EN_NAME_CZ,cast(c.UNIT_NUM as numeric(10,2)) WEIGHT from WOBO_TRANSFER_BIG a,WOBO_ENTERPRISE b,WOBO_TRANSFER_BILL c where a.TG_ID = c.TG_ID and c.EN_ID_CS = b.EP_ID and b.IF_PRODUCE = '1' and b.BELONGSEPA = '" + orgCode + "' and (a.ACTIONDATE <= '" + ENDTIME + "' or a.sysdate <= '" + ENDTIME + "') group by a.EN_ID_CZ,a.EN_NAME_CZ,c.UNIT_NUM) as C group by C.EN_ID_CZ,C.EN_NAME_CZ) as A on A.EN_ID_CZ = B.EN_ID_CZ";
		}
		if(searchContent != null && !"".equals(searchContent)){
			sql = sql + " where B.EN_NAME_CZ like '%" + searchContent + "%'";
		}
		Record record = Db.findFirst(sql + " group by A.WEIGHT,B.AMOUNT) as C");
		Map<String, Object> map = new HashMap<String, Object>();
		if(record != null){
			map.put("CZ_NUM", record.get("CZ_NUM"));
			map.put("WEIGHT", record.get("WEIGHT"));
			map.put("AMOUNT", record.get("AMOUNT"));
		}
		return map;
	}
	
	/**
	 * @author weizanting
	 * @date 20170623
	 * 方法：查询转移汇总（按处置单位）--开始时间、结束时间
	 */
	public Map<String, Object> queryNumForCZ(String BEGINTIME, String ENDTIME, Object searchContent, String ROLEID, String orgCode){
		String sql = "select count(C.CZ_NUM) CZ_NUM,sum(C.WEIGHT) as WEIGHT,sum(C.AMOUNT) as AMOUNT from (select count(B.EN_ID_CZ) CZ_NUM,A.WEIGHT,B.AMOUNT ";
		if("SJSPROLE".equals(ROLEID)){		//市级管理员
			sql = sql +  " from (select b.EN_ID_CZ,b.EN_NAME_CZ,c.BELONGSEPA,sum(a.COUNT) AMOUNT from WOBO_TRANSFER_BILL a,WOBO_TRANSFER_BIG b,WOBO_ENTERPRISE c where a.TG_ID = b.TG_ID and b.EN_ID_CZ = c.EP_ID and c.STATUS = '1' and b.STATUS in ('4', '5') and ((b.ACTIONDATE >= '" + BEGINTIME + "' and b.ACTIONDATE <= '" + ENDTIME + "') or (b.sysdate >= '" + BEGINTIME + "' and b.sysdate <= '" + ENDTIME + "')) group by b.EN_ID_CZ,b.EN_NAME_CZ,c.BELONGSEPA) as B LEFT JOIN (select a.EN_ID_CZ,a.EN_NAME_CZ,sum(cast(a.UNIT_NUM as numeric(10,2))) WEIGHT from WOBO_TRANSFER_BIG a,WOBO_ENTERPRISE b where a.EN_ID_CZ = b.EP_ID and ((a.ACTIONDATE >= '" + BEGINTIME + "' and a.ACTIONDATE <= '" + ENDTIME + "') or (a.sysdate >= '" + BEGINTIME + "' and a.sysdate <= '" + ENDTIME + "')) group by a.EN_ID_CZ,a.EN_NAME_CZ) as A on A.EN_ID_CZ = B.EN_ID_CZ";
		}else{		//区级管理员
			sql = sql + " from (select b.EN_ID_CZ,b.EN_NAME_CZ,c.BELONGSEPA,sum(a.COUNT) AMOUNT from WOBO_TRANSFER_BILL a,WOBO_TRANSFER_BIG b,WOBO_ENTERPRISE c where a.TG_ID = b.TG_ID and a.EN_ID_CS = c.EP_ID and c.STATUS = '1' and b.STATUS in ('4', '5') and c.IF_PRODUCE = '1' and c.BELONGSEPA = '" + orgCode + "' and ((b.ACTIONDATE >= '" + BEGINTIME + "' and b.ACTIONDATE <= '" + ENDTIME + "') or (b.sysdate >= '" + BEGINTIME + "' and b.sysdate <= '" + ENDTIME + "')) group by b.EN_ID_CZ,b.EN_NAME_CZ,c.BELONGSEPA) as B LEFT JOIN (select EN_ID_CZ,EN_NAME_CZ,sum(cast(WEIGHT as numeric(10,2))) WEIGHT from (select a.EN_ID_CZ,a.EN_NAME_CZ,cast(c.UNIT_NUM as numeric(10,2)) WEIGHT from WOBO_TRANSFER_BIG a,WOBO_ENTERPRISE b,WOBO_TRANSFER_BILL c where a.TG_ID = c.TG_ID and c.EN_ID_CS = b.EP_ID and b.IF_PRODUCE = '1' and b.BELONGSEPA = '" + orgCode + "' and ((a.ACTIONDATE >= '" + BEGINTIME + "' and a.ACTIONDATE <= '" + ENDTIME + "') or (a.sysdate >= '" + BEGINTIME + "' and a.sysdate <= '" + ENDTIME + "')) group by a.EN_ID_CZ,a.EN_NAME_CZ,c.UNIT_NUM) as C group by C.EN_ID_CZ,C.EN_NAME_CZ) as A on A.EN_ID_CZ = B.EN_ID_CZ";
		}
		if(searchContent != null && !"".equals(searchContent)){
			sql = sql + " where B.EN_NAME_CZ like '%" + searchContent + "%'";
		}
		Record record = Db.findFirst(sql + " group by A.WEIGHT,B.AMOUNT) as C");
		Map<String, Object> map = new HashMap<String, Object>();
		if(record != null){
			map.put("CZ_NUM", record.get("CZ_NUM"));
			map.put("WEIGHT", record.get("WEIGHT"));
			map.put("AMOUNT", record.get("AMOUNT"));
		}
		return map;
	}
	
	/**
	 * @author weizanting
	 * @date 20170623
	 * 方法：查看转移汇总列表放入列内--按产生单位
	 */
	public void getTransferSummaryForCSExcelInfo(String[] sheetNames,List<?>[] sheetAllList,String[][] headers,String[][] columns, String BEGINTIME, String ENDTIME, Object searchContent, String ROLEID, String userType, String orgCode, String epId){
		String[] cols = new String[4];
		cols[0] = "医疗机构";
		cols[1] = "所属区县";
		cols[2] = "重量";
		cols[3] = "个数";
		for(int i = 0 ; i < 1 ; i++ )
		{
			List<Object> data = Lists.newArrayList();
			sheetNames[i] = "转移汇总列表（按产生单位）";
			for(Record one : queryTransferSummaryForCSForSheet(sheetNames[i], BEGINTIME, ENDTIME, searchContent, ROLEID, userType, orgCode, epId))
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
	 * @date 20170623
	 * 方法：查看转移汇总列表放入列内--按产生单位
	 */
	public List<Record> queryTransferSummaryForCSForSheet(String dictType, String BEGINTIME, String ENDTIME, Object searchContent, String ROLEID, String userType, String orgCode, String epId){
		String sql = "";
		List<Record> list = new ArrayList<Record>();
		if("SJSPROLE".equals(ROLEID)){		//市级管理员
			sql = " from (select EN_ID_CS,EN_NAME_CS,BELONGSEPA,sum(WEIGHT) as WEIGHT,sum(AMOUNT) as AMOUNT from (select a.EN_ID_CS,a.EN_NAME_CS,b.BELONGSEPA,cast(a.UNIT_NUM as numeric(10,2)) as WEIGHT,a.COUNT as AMOUNT,a.ACTIONDATE,a.sysdate from WOBO_TRANSFER_BILL a left join WOBO_ENTERPRISE b on a.EN_ID_CS = b.EP_ID where b.STATUS = '1' and a.STATUS in ('4', '5')";
		}
		if("QJSPROLE".equals(ROLEID)){		//区级管理员
			sql = " from (select EN_ID_CS,EN_NAME_CS,BELONGSEPA,sum(WEIGHT) as WEIGHT,sum(AMOUNT) as AMOUNT from (select a.EN_ID_CS,a.EN_NAME_CS,b.BELONGSEPA,cast(a.UNIT_NUM as numeric(10,2)) as WEIGHT,a.COUNT as AMOUNT,a.ACTIONDATE,a.sysdate from WOBO_TRANSFER_BILL a left join WOBO_ENTERPRISE b on a.EN_ID_CS = b.EP_ID where b.STATUS = '1' and a.STATUS in ('4', '5') and b.BELONGSEPA = '" + orgCode + "'";
		}
		if("epAdminCs".equals(userType)){		//医疗单位管理员
			sql = " from (select EN_ID_CS,EN_NAME_CS,BELONGSEPA,sum(WEIGHT) as WEIGHT,sum(AMOUNT) as AMOUNT from (select a.EN_ID_CS,a.EN_NAME_CS,b.BELONGSEPA,cast(a.UNIT_NUM as numeric(10,2)) as WEIGHT,a.COUNT as AMOUNT,a.ACTIONDATE,a.sysdate from WOBO_TRANSFER_BILL a left join WOBO_ENTERPRISE b on a.EN_ID_CS = b.EP_ID where b.STATUS = '1' and a.STATUS in ('4', '5') and b.EP_ID = '" + epId + "'";
		}
		if("epAdminCz".equals(userType)){		//医疗处置单位管理员
			sql = " from (select EN_ID_CS,EN_NAME_CS,BELONGSEPA,sum(WEIGHT) as WEIGHT,sum(AMOUNT) as AMOUNT from (select a.EN_ID_CS,a.EN_NAME_CS,b.BELONGSEPA,cast(a.UNIT_NUM as numeric(10,2)) as WEIGHT,a.COUNT as AMOUNT,a.ACTIONDATE,a.sysdate from WOBO_TRANSFER_BILL a left join WOBO_ENTERPRISE b on a.EN_ID_CS = b.EP_ID,WOBO_TRANSFER_BIG c  where b.STATUS = '1' and a.STATUS in ('4', '5') and a.TG_ID = c.TG_ID and c.EN_ID_CZ = '" + epId + "'";
		}
		if(!"".equals(BEGINTIME) && "".equals(ENDTIME)){
			sql = sql + " and ((a.ACTIONDATE >= '" + BEGINTIME + "') or (a.sysdate >= '" + BEGINTIME + "'))";
		}
		if("".equals(BEGINTIME) && !"".equals(ENDTIME)){
			sql = sql + " and ((a.ACTIONDATE <= '" + ENDTIME + "') or (a.sysdate <= '" + ENDTIME + "'))";
		}
		if(!"".equals(BEGINTIME) && !"".equals(ENDTIME)){
			sql = sql + " and ((a.ACTIONDATE >='" + BEGINTIME + "' and a.ACTIONDATE <= '" + ENDTIME + "') or (a.sysdate >= '" + BEGINTIME + "' and a.sysdate <= '" + ENDTIME + "'))";
		}
		sql = sql + " group by a.EN_ID_CS,a.EN_NAME_CS,b.BELONGSEPA,a.UNIT_NUM,a.COUNT,a.ACTIONDATE,a.sysdate) as A group by EN_ID_CS,EN_NAME_CS,BELONGSEPA) as B";
		if(searchContent != null && !"".equals(searchContent)){
			sql = sql + " where EN_NAME_CS like '%" + searchContent + "%'";
		}
		list = Db.find("select EN_ID_CS as \"医疗机构ID\",EN_NAME_CS as \"医疗机构\",BELONGSEPA as \"所属区县\",isnull(WEIGHT, 0) as \"重量\", isnull(AMOUNT, 0) as \"个数\"" + sql);
		if(list.size() > 0){
			for(int i = 0; i < list.size(); i ++){
				list.get(i).set("所属区县", convert(cityList, list.get(i).get("所属区县")));
			}
		}
		return list;
	}
	
	/**
	 * @author weizanting
	 * @date 20170623
	 * 方法：查看转移汇总列表放入列内--按处置单位
	 */
	public void getTransferSummaryForCZExcelInfo(String[] sheetNames,List<?>[] sheetAllList,String[][] headers,String[][] columns, String BEGINTIME, String ENDTIME, Object searchContent, String ROLEID, String orgCode){
		String[] cols = new String[4];
		cols[0] = "医疗机构";
		cols[1] = "所属区县";
		if("SJSPROLE".equals(ROLEID)){
			cols[2] = "行程重量";
		}else{
			cols[2] = "运单重量";
		}
		cols[3] = "个数";
		for(int i = 0 ; i < 1 ; i++ )
		{
			List<Object> data = Lists.newArrayList();
			sheetNames[i] = "转移汇总列表（按处置单位）";
			for(Record one : queryTransferSummaryForCZForSheet(sheetNames[i], BEGINTIME, ENDTIME, searchContent, ROLEID, orgCode))
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
	 * @date 20170623
	 * 方法：查看转移汇总列表放入列内--按处置单位
	 */
	public List<Record> queryTransferSummaryForCZForSheet(String dictType, String BEGINTIME, String ENDTIME, Object searchContent, String ROLEID, String orgCode){
		String sql = "";
		String sqlFrom = "";
		List<Record> list = new ArrayList<Record>();
		if(!"".equals(BEGINTIME) && "".equals(ENDTIME)){
			sqlFrom = " and ((a.ACTIONDATE >= '" + BEGINTIME + "') or (a.sysdate >= '" + BEGINTIME + "'))";
		}
		if("".equals(BEGINTIME) && !"".equals(ENDTIME)){
			sqlFrom = " and ((a.ACTIONDATE <= '" + ENDTIME + "') or (a.sysdate <= '" + ENDTIME + "'))";
		}
		if(!"".equals(BEGINTIME) && !"".equals(ENDTIME)){
			sqlFrom = " and ((a.ACTIONDATE >='" + BEGINTIME + "' and a.ACTIONDATE <= '" + ENDTIME + "') or (a.sysdate >= '" + BEGINTIME + "' and a.sysdate <= '" + ENDTIME + "'))";
		}
		if("SJSPROLE".equals(ROLEID)){
			sql = "select B.EN_ID_CZ as \"医疗机构ID\",B.EN_NAME_CZ as \"医疗机构\",B.BELONGSEPA as \"所属区县\",isnull(A.WEIGHT, 0) as \"行程重量\", isnull(AMOUNT, 0) as \"个数\" from (select b.EN_ID_CZ,b.EN_NAME_CZ,c.BELONGSEPA,sum(a.COUNT) AMOUNT from WOBO_TRANSFER_BILL a,WOBO_TRANSFER_BIG b,WOBO_ENTERPRISE c where a.TG_ID = b.TG_ID and b.EN_ID_CZ = c.EP_ID and c.STATUS = '1' and a.STATUS in ('4', '5')" + sqlFrom + " group by b.EN_ID_CZ,b.EN_NAME_CZ,c.BELONGSEPA) as B LEFT JOIN (select a.EN_ID_CZ,a.EN_NAME_CZ,sum(cast(a.UNIT_NUM as numeric(10,2))) WEIGHT from WOBO_TRANSFER_BIG a,WOBO_ENTERPRISE b where a.EN_ID_CZ = b.EP_ID" + sqlFrom + " group by a.EN_ID_CZ,a.EN_NAME_CZ) as A on A.EN_ID_CZ = B.EN_ID_CZ";
		}else{
			sql = "select B.EN_ID_CZ as \"医疗机构ID\",B.EN_NAME_CZ as \"医疗机构\",B.BELONGSEPA as \"所属区县\",isnull(A.WEIGHT, 0) as \"运单重量\", isnull(AMOUNT, 0) as \"个数\" from (select b.EN_ID_CZ,b.EN_NAME_CZ,c.BELONGSEPA,sum(a.COUNT) AMOUNT from WOBO_TRANSFER_BILL a,WOBO_TRANSFER_BIG b,WOBO_ENTERPRISE c where a.TG_ID = b.TG_ID and a.EN_ID_CS = c.EP_ID and c.STATUS = '1' and a.STATUS in ('4', '5')" + sqlFrom + " and c.BELONGSEPA = '" + orgCode + "'" + " group by b.EN_ID_CZ,b.EN_NAME_CZ,c.BELONGSEPA) as B LEFT JOIN (select EN_ID_CZ,EN_NAME_CZ,sum(cast(WEIGHT as numeric(10,2))) WEIGHT from (select a.EN_ID_CZ,a.EN_NAME_CZ,cast(c.UNIT_NUM as numeric(10,2)) WEIGHT from WOBO_TRANSFER_BIG a,WOBO_ENTERPRISE b,WOBO_TRANSFER_BILL c where a.TG_ID = c.TG_ID and c.EN_ID_CS = b.EP_ID and b.IF_PRODUCE = '1' and b.BELONGSEPA = '" + orgCode + "'" + sqlFrom + " group by a.EN_ID_CZ,a.EN_NAME_CZ,c.UNIT_NUM) as C group by C.EN_ID_CZ,C.EN_NAME_CZ) as A on A.EN_ID_CZ = B.EN_ID_CZ";
		}
		if(searchContent != null && !"".equals(searchContent)){
			sql = sql + " where B.EN_NAME_CZ like '%" + searchContent + "%'";
		}
		list = Db.find(sql);
		if(list.size() > 0){
			for(int i = 0; i < list.size(); i ++){
				/*if("SJSPROLE".equals(ROLEID)){
					list.get(i).set("运单重量", convert(cityList, list.get(i).get("运单重量")));
				}else{
					list.get(i).set("运单重量", "-");
				}*/
				list.get(i).set("所属区县", convert(cityList, list.get(i).get("所属区县")));
			}
		}
		return list;
	}
	
}
