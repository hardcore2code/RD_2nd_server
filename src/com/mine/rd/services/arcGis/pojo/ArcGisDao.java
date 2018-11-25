package com.mine.rd.services.arcGis.pojo;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ext.kit.CreateHtmlUtils;
import com.jfinal.core.JFinal;
import com.jfinal.json.Jackson;
import com.jfinal.kit.HttpKit;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.mine.pub.kit.DateKit;
import com.mine.pub.kit.JsonMyKit;
import com.mine.pub.kit.QRcodeKit;
import com.mine.pub.pojo.BaseDao;

public class ArcGisDao extends BaseDao {
	/**
	 * @author zyl
	 * @date 20170906
	 * 方法：查询所有车辆定位信息方法
	 * @return 
	 */
	public Map<String, Object> queryPositions(){
		Map<String, Object> result=new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String querySql="select wgc.*, (CASE WHEN LEFT(wgc .TB_ID, 2) = 'TB' THEN 1 ELSE 2 END) as soure from WOBO_GIS_CONDUITS_TRANSFER wgc ";
		List<Record> recordList = Db.find(querySql);
		Map<String, Object> map = null;
		if(recordList.size() > 0){
			for (Record record : recordList) {
				map = new HashMap<String, Object>();
				setVals(record, map);
				list.add(map);
			}
		}
		result.put("list", list);
		result.put("actionType", "0");
		return result;
	}
	
	/**
	 * @author zyl
	 * @date 20170906
	 * 方法：根据车牌照查询车辆定位信息方法
	 * @return 
	 */
	public Map<String, Object> queryPositionByLp(String pzh){
		Map<String, Object> result=new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String querySql="select TOP (1) wcx.* from(select wgc.*,1 as soure from CAR_INFO ci,TRANSFERPLAN_BILL tb,WOBO_GIS_CONDUITS_H wgc "+
						"where ci.CARDNO=tb.CARCARD "+
						"and wgc.TB_ID=tb.TB_ID "+
						"and ci.PLATE_NUMBER='"+pzh+"' "+
						"union all "+
						"select wgc2.*,2 from WOBO_GIS_CONDUITS_H wgc2,WOBO_TRANSFER_BIG wtb "+
						"where wtb.TG_ID=wgc2.TB_ID "+
						"and wtb.PLATE_NUMBER='"+pzh+"' "+
						") wcx order by wcx.sysdate desc";
		Record record = Db.findFirst(querySql);
		Map<String, Object> map = null;
		if(record != null){
				map = new HashMap<String, Object>();
				setVals(record, map);
				list.add(map);
				//生成分享页面
				String location84 = Jackson.getJson().toJson(record.get("LOCATION84"));
				location84=location84.substring(1,location84.length()-1);
				location84=location84.replaceAll("\\\\", "");
				Map xy84 = JsonMyKit.parse(location84, Map.class);
				double x84=Double.parseDouble(xy84.get("latitude").toString());
				double y84=Double.parseDouble(xy84.get("longitude").toString());
				Map<String, Object> queryBillInfo = (Map<String, Object>)((ArrayList)queryBillInfo(record.getStr("TB_ID")).get("list")).get(0);
				Map<String, Object> param=new HashMap<String, Object>();
				param.put("pzh", queryBillInfo.get("plateNumber"));
				param.put("x", x84);
				param.put("y", y84);
				param.put("tbId", queryBillInfo.get("tbId"));
				param.put("sjxm", queryBillInfo.get("enNameYs"));
				param.put("fwmc", queryBillInfo.get("dName"));
				param.put("sl", queryBillInfo.get("unitNum"));
				param.put("jldw", queryBillInfo.get("unit"));
				//类所在工程根路径
				String proClassPath = this.getClass().getResource("").getPath();
				String newHtmlPath=proClassPath.substring(0,proClassPath.indexOf("WEB-INF"))+"arcgis/shareCars/";
				String template=newHtmlPath.replaceAll("shareCars/", "")+"/template.html";
				String fileName=queryBillInfo.get("uuId")+DateKit.toStr(new Date(), "yyyyMMddHHmmss");
				//生成分享页面
				CreateHtmlUtils.MakeHtml(template, param, newHtmlPath, fileName);
				//处理成服务器地址
				String servePath = PropKit.get("servePath");
				newHtmlPath=newHtmlPath.substring(newHtmlPath.indexOf("WebContent")+11);
				String qcCodeUrl=servePath+newHtmlPath+fileName+".html";
				//生成分享二维码
				String path = makeQcCode("\\arcgis\\qcCode\\",queryBillInfo.get("uuId").toString() , qcCodeUrl);
				path=servePath+path.substring(path.indexOf("WebContent")+11);
				result.put("qcCodePath", path);
		}
		result.put("actionType", "1");
		result.put("list", list);
		return result;
	}

	public static String makeQcCode(String pathDir,String uuId,
			String info) {
		String path = JFinal.me().getServletContext().getRealPath("/");
		String pathSave = uuId+"_" + DateKit.toStr(new Date(), "yyyyMMddHHmmss")+".jpg";
		path = path +pathDir;
		File dir = new File(path);
        if (!dir.exists()) {
        	dir.mkdirs();
        }
        path=path+pathSave;
		QRcodeKit.encodePR(info, 170, 170, path);
		return path;
	}
	
	/**
	 * @author zyl
	 * @date 20170906
	 * 方法：根据产生单位名称查询车辆定位信息方法
	 * @return 
	 */
	public Map<String, Object> queryPositionByCs(String csName){
		Map<String, Object> result=new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String querySql="select wgc.*,1 as soure from CAR_INFO ci,TRANSFERPLAN_BILL tb,WOBO_GIS_CONDUITS_TRANSFER wgc "+
						"where ci.CARDNO=tb.CARCARD  "+
						"and wgc.TB_ID=tb.TB_ID  "+
						"and tb.EN_NAME_CS like '%"+csName+"%' "+
						"union all  "+
						"select wgc2.*,2 from WOBO_GIS_CONDUITS_TRANSFER wgc2,WOBO_TRANSFER_BIG wtb ,WOBO_TRANSFER_BILL wtb1 "+
						"where wtb.TG_ID=wgc2.TB_ID  "+
						"and wtb1.TG_ID=wtb.TG_ID "+
						"and wtb1.EN_NAME_CS like '%"+csName+"%' ";
		List<Record> recordList = Db.find(querySql);
		Map<String, Object> map = null;
		if(recordList.size() > 0){
			map = new HashMap<String, Object>();
			Record record = recordList.get(0);
			setVals(record, map);
			list.add(map);
		}
		result.put("list", list);
		result.put("actionType", "2");
		return result;
	}
	
	/**
	 * @author zyl
	 * @date 20170906
	 * 方法：根据运输单位名称查询车辆定位信息方法，危废二期的产生单位和运输单位是一家单位，所以使用字段相同
	 * @return 
	 */
	public Map<String, Object> queryPositionByYs(String ysName){
		Map<String, Object> result=new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String querySql="select wgc.*,1 as soure from CAR_INFO ci,TRANSFERPLAN_BILL tb,WOBO_GIS_CONDUITS_TRANSFER wgc "+
				"where ci.CARDNO=tb.CARCARD  "+
				"and wgc.TB_ID=tb.TB_ID  "+
				"and tb.EN_NAME_YS like '%"+ysName+"%' "+
				"union all  "+
				"select wgc2.*,2 from WOBO_GIS_CONDUITS_TRANSFER wgc2,WOBO_TRANSFER_BIG wtb ,WOBO_TRANSFER_BILL wtb1 "+
				"where wtb.TG_ID=wgc2.TB_ID  "+
				"and wtb1.TG_ID=wtb.TG_ID "+
				"and wtb1.EN_NAME_CS like '%"+ysName+"%' ";
		List<Record> recordList = Db.find(querySql);
		Map<String, Object> map = null;
		if(recordList.size() > 0){
			map = new HashMap<String, Object>();
			Record record = recordList.get(0);
			setVals(record, map);
			list.add(map);
			
		}
		result.put("list", list);
		result.put("actionType", "3");
		return result;
	}
	
	/**
	 * @author zyl
	 * @date 20170906
	 * 方法：根据处置单位名称查询车辆定位信息方法
	 * @return 
	 */
	public Map<String, Object> queryPositionByCz(String czName){
		Map<String, Object> result=new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String querySql="select wgc.*,1 as soure from CAR_INFO ci,TRANSFERPLAN_BILL tb,WOBO_GIS_CONDUITS_TRANSFER wgc "+
				"where ci.CARDNO=tb.CARCARD  "+
				"and wgc.TB_ID=tb.TB_ID  "+
				"and tb.EN_NAME_CZ like '%"+czName+"%' "+
				"union all  "+
				"select wgc2.*,2 from WOBO_GIS_CONDUITS_TRANSFER wgc2,WOBO_TRANSFER_BIG wtb ,WOBO_TRANSFER_BILL wtb1 "+
				"where wtb.TG_ID=wgc2.TB_ID  "+
				"and wtb1.TG_ID=wtb.TG_ID "+
				"and wtb.EN_NAME_CZ like '%"+czName+"%' ";
		List<Record> recordList = Db.find(querySql);
		Map<String, Object> map = null;
		if(recordList.size() > 0){
			map = new HashMap<String, Object>();
			Record record = recordList.get(0);
			setVals(record, map);
			list.add(map);
		}
		result.put("list", list);
		result.put("actionType", "4");
		return result;
	}
	
	/**
	 * @author zyl
	 * @date 20170906
	 * 方法：根据联单编号查询具体联单及车辆信息
	 * @return 
	 */
	public Map<String, Object> queryBillInfo(String tbId){
		Map<String, Object> result=new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String querySql="select ci.PLATE_NUMBER,tb.TB_ID,ede.DICTNAME,wgc.UUID,tb.EN_NAME_CS, "+
						"tb.EN_NAME_YS,tb.EN_NAME_CZ,tbl.SAMLL_CATEGORY_ID,tbl.D_NAME,tbl.UNIT_NUM,"+
						"tbl.UNIT,1 as soure from CAR_INFO ci,TRANSFERPLAN_BILL tb,WOBO_GIS_CONDUITS_TRANSFER wgc "+
						" ,EOS_DICT_ENTRY ede,TRANSFERPLAN_BILL_LIST tbl "+
						"where ci.CARDNO=tb.CARCARD  "+
						"and wgc.TB_ID=tb.TB_ID  "+
						"and ede.DICTID=tb.STATUS "+
						"and tbl.TB_ID=tb.TB_ID "+
						"and ede.DICTTYPEID='TB_STATUS' "+
						"and tb.TB_ID ='"+tbId+"' "+
						"union all  "+
						"select wtb.PLATE_NUMBER,wgc2.TB_ID,pd.dict_value,wgc2.UUID,wtb1.EN_NAME_CS, "+
						"'',wtb.EN_NAME_CZ,'','',wtb1.UNIT_NUM,wtb1.UNIT,2 from WOBO_GIS_CONDUITS_TRANSFER wgc2, "+
						"WOBO_TRANSFER_BIG wtb ,PUB_DICT pd,WOBO_TRANSFER_BILL wtb1 "+
						"where wtb.TG_ID=wgc2.TB_ID  "+
						"and pd.dict_id=wtb.STATUS "+
						"and wtb1.TG_ID=wtb.TG_ID "+
						"and pd.id_main='6' "+
						"and wgc2.TB_ID='"+tbId+"'";
		List<Record> recordList = Db.find(querySql);
		Map<String, Object> map = null;
		if(recordList.size() > 0){
			map = new HashMap<String, Object>();
			for (Record record : recordList) {
				map = new HashMap<String, Object>();
				setDetailVals(record, map);
				list.add(map);
			}
		}
		result.put("list", list);
		result.put("actionType", "5");
		return result;
	}
	
	/**
	 * @author zyl
	 * @date 20171116
	 * 方法：根据设备UUID查询车辆轨迹信息
	 * @return 
	 */
	public Map<String, Object> queryCartTrajectory(String uuId){
		Map<String, Object> result=new HashMap<String, Object>();
		List<double[]> list = new ArrayList<double[]>();
		String querySql="SELECT UUID, LOCATION, sysdate FROM WOBO_GIS_CONDUITS_H "+
						"WHERE (UUID = '"+uuId+"') ORDER BY sysdate";
		List<Record> recordList = Db.find(querySql);
		double[] zb = null;
		if(recordList.size() > 0){
			for (Record record : recordList) {
				zb=new double[2];
				String location = Jackson.getJson().toJson(record.get("LOCATION"));
				location=location.substring(1,location.length()-1);
				location=location.replaceAll("\\\\", "");
				Map xy = JsonMyKit.parse(location, Map.class);
				zb[0]=Double.parseDouble(xy.get("latitude").toString());
				zb[1]=Double.parseDouble(xy.get("longitude").toString());
				list.add(zb);
			}
		}
		result.put("list", list);
		result.put("actionType", "7");
		return result;
	}

	private void setVals(Record record, Map<String, Object> map) {
		map.put("gsId", record.get("GS_ID"));
		map.put("sessionId", record.get("SESSION_ID"));
		map.put("uuId", record.get("UUID"));
		map.put("tbId", record.get("TB_ID"));
		map.put("location", record.get("LOCATION"));
		map.put("status", record.get("STATUS"));
		map.put("sysdate", record.get("sysdate"));
		map.put("soure", record.get("soure"));
	}
	
	private void setDetailVals(Record record, Map<String, Object> map) {
		map.put("plateNumber", record.get("PLATE_NUMBER"));
		map.put("tbId", record.get("TB_ID"));
		map.put("statusName", record.get("DICTNAME"));
		map.put("uuId", record.get("UUID"));
		map.put("enNameCs", record.get("EN_NAME_CS"));
		map.put("enNameYs", record.get("EN_NAME_YS"));
		map.put("enNameCz", record.get("EN_NAME_CZ"));
		map.put("samllCategoryId", record.get("SAMLL_CATEGORY_ID"));
		map.put("dName", record.get("D_NAME"));
		map.put("unitNum", record.get("UNIT_NUM"));
		map.put("unit", record.get("UNIT"));
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170726
	 * 方法：PDA定位存流水表
	 */
	public boolean saveLocationConduitBak(Map<String, Object> map){
		Record record = new Record();
		record.set("GS_ID", super.getSeqId("WOBO_GIS_CONDUITS"));
    	record.set("SESSION_ID", map.get("sessionId"));
    	record.set("UUID", map.get("uuid"));
    	record.set("TB_ID", map.get("tbId"));
    	record.set("LOCATION", Jackson.getJson().toJson(map.get("location")));
    	record.set("STATUS", "1");
    	record.set("sysdate", super.getSysdate());
    	return Db.save("WOBO_GIS_CONDUITS", "GS_ID", record);
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170726
	 * 方法：PDA定位存流水表
	 */
	public boolean saveLocationConduit(Map<String, Object> map){
		String uuId = map.get("uuid").toString();
//		String qSql="select * from WOBO_GIS_CONDUITS where UUID='"+uuId+"'";
		Record record = new Record();
		record.set("SESSION_ID", map.get("sessionId"));
		record.set("UUID", map.get("uuid"));
		record.set("TB_ID", map.get("tbId"));
		record.set("LOCATION", Jackson.getJson().toJson(map.get("location")));
		record.set("STATUS", "1");
		record.set("sysdate", super.getSysdate());
//		Record find = Db.findFirst(qSql);
		boolean result=false;
//		if(find != null)
//		{
//			record.set("GS_ID", find.get("GS_ID"));
//			result=Db.update("WOBO_GIS_CONDUITS", "GS_ID", record);
//		}
//		else
//		{
			record.set("GS_ID", super.getSeqId("WOBO_GIS_CONDUITS"));
			result=Db.save("WOBO_GIS_CONDUITS", "GS_ID", record);
//		}
		return result;
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170726
	 * 方法：PDA定位存流水表
	 */
	public void saveGisHistory(Map<String, Object> map){
		String days = PropKit.get("gisSaveDays");
		String uuId = map.get("uuid").toString();
		String sql="select count(0) cut from WOBO_GIS_CONDUITS_H where DateDiff(dd,sysdate,getdate())>"+days+" and UUID='"+uuId+"'";
		Record findFirst = Db.findFirst(sql);
		if(findFirst.getInt("cut") > 0)
		{
			//删除b表中的历史记录，时间戳小于今天的记录
			String delSql="delete from WOBO_GIS_CONDUITS_H where DateDiff(dd,sysdate,getdate())>"+days+" and UUID='"+uuId+"'";
			Db.update(delSql);
		}
		String location = Jackson.getJson().toJson(map.get("location"));
		location=location.substring(1,location.length()-1);
		location=location.replaceAll("\\\\", "");
		Map xy = JsonMyKit.parse(location, Map.class);
		String arcGisXyStr = HttpKit.get("http://localhost:7080/cc/servlet/CcServlet?x="+xy.get("latitude")+"&y="+xy.get("longitude"));
		arcGisXyStr=arcGisXyStr.replaceAll("(\r\n|\r|\n|\n\r)", "");
		Record record = new Record();
		record.set("SESSION_ID", map.get("sessionId"));
		record.set("UUID", map.get("uuid"));
		record.set("TB_ID", map.get("tbId"));
		record.set("LOCATION", arcGisXyStr);
		record.set("LOCATION84", map.get("location"));
		record.set("STATUS", "1");
		record.set("sysdate", super.getSysdate());
		record.set("GS_ID", super.getSeqId("WOBO_GIS_CONDUITS"));
		Db.save("WOBO_GIS_CONDUITS_H", "GS_ID", record);
	}
	
	/**
	 * @author zyl
	 * @date 20171103
	 * 方法：删除车辆定位信息表方法
	 * @return 
	 */
	public void delCarPosition(String uuId){
		String delSql="delete from WOBO_GIS_CONDUITS where UUID='"+uuId+"'";
		String delSql1="delete from WOBO_GIS_CONDUITS_TRANSFER where UUID='"+uuId+"'";
		Db.update(delSql);
		Db.update(delSql1);
	}
	
	/**
	 * @author zyl
	 * @date 20171113
	 * 方法：查询预警车辆
	 * @return 
	 */
	public String getWarningCars(){
		Map<String, Object> rst=new HashMap<String, Object>();
		Map<String, Object> param=null;
		String result=null;
		double bx=0;
		double sx=0;
		double by=0;
		double sy=0;
		double x84=0;
		double y84=0;
		int carRange = Integer.parseInt(PropKit.get("carRange"));
		List<Map<String, Object>> warningCars=new ArrayList<Map<String, Object>>();
		String sql="SELECT UUID,TB_ID FROM WOBO_GIS_CONDUITS_H "+
				   "WHERE (DATEDIFF(hh, sysdate, GETDATE()) < "+PropKit.get("stopHours")+") "+
				   "GROUP BY UUID,TB_ID HAVING (COUNT(0) > "+PropKit.get("trajectoryNum")+")";
		List<Record> find = Db.find(sql);
		for (Record record : find) {
			String sql1="SELECT * FROM WOBO_GIS_CONDUITS_H "+
					   "WHERE (DATEDIFF(hh, sysdate, GETDATE()) < "+PropKit.get("stopHours")+") ";
			List<Record> find2 = Db.find(sql1);
			int sign=0;
			for (Record record2 : find2) {
				String location = Jackson.getJson().toJson(record2.get("LOCATION"));
				location=location.substring(1,location.length()-1);
				location=location.replaceAll("\\\\", "");
				Map xy = JsonMyKit.parse(location, Map.class);
				if(sign==0)
				{
					bx=Double.parseDouble(xy.get("latitude").toString());
					sx=bx;
					by=Double.parseDouble(xy.get("longitude").toString());
					sy=by;
					sign=1;
					String location84 = Jackson.getJson().toJson(record2.get("LOCATION84"));
					location84=location84.substring(1,location84.length()-1);
					location84=location84.replaceAll("\\\\", "");
					Map xy84 = JsonMyKit.parse(location84, Map.class);
					x84=Double.parseDouble(xy84.get("latitude").toString());
					y84=Double.parseDouble(xy84.get("longitude").toString());

				}
				else
				{
					double tmpX=Double.parseDouble(xy.get("latitude").toString());
					double tmpY=Double.parseDouble(xy.get("longitude").toString());
					if(tmpX > bx) bx=tmpX;
					if(tmpX < sx) sx=tmpX;
					if(tmpY > by) by=tmpY;
					if(tmpY < sy) sy=tmpY;
				}
			}
			if((bx-sx) <= carRange && (by-sy) <= carRange)
			{
				Map<String, Object> queryBillInfo = (Map<String, Object>)((ArrayList)queryBillInfo(record.getStr("TB_ID")).get("list")).get(0);
				queryBillInfo.put("90x", bx);
				queryBillInfo.put("90y", sy);
				queryBillInfo.put("84x", x84);
				queryBillInfo.put("84y", y84);
				warningCars.add(queryBillInfo);
				param=new HashMap<String, Object>();
				param.put("pzh", queryBillInfo.get("plateNumber"));
				param.put("x", x84);
				param.put("y", y84);
				param.put("tbId", queryBillInfo.get("tbId"));
				param.put("sjxm", queryBillInfo.get("enNameYs"));
				param.put("fwmc", queryBillInfo.get("dName"));
				param.put("sl", queryBillInfo.get("unitNum"));
				param.put("jldw", queryBillInfo.get("unit"));
				//类所在工程根路径
				String proClassPath = this.getClass().getResource("").getPath();
				String newHtmlPath=proClassPath.substring(0,proClassPath.indexOf("WEB-INF"))+"arcgis/warningCars/";
				String template=newHtmlPath.replaceAll("warningCars/", "")+"/template.html";
				CreateHtmlUtils.MakeHtml(template, param, newHtmlPath, queryBillInfo.get("uuId").toString());
			}
		}
		if(warningCars.size() > 0)
		{
			rst.put("list", warningCars);
			rst.put("actionType", "6");
			result=Jackson.getJson().toJson(rst);
		}
		return result;
	}
	
	public static void main(String[] args) {
		
//		Map xy = JsonMyKit.parse("{\"latitude\":39.096553,\"longitude\":117.152577}", Map.class);
////		HashMap xy = Jackson.getJson().parse("{latitude:39.096915,longitude:117.1524}", java.util.HashMap.class);
//		System.out.println(xy);
//		System.out.println(xy.toString());
//		System.out.println(xy.get("latitude"));
		
		
	}
}
