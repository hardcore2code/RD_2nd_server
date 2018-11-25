package com.mine.job;

import java.util.List;
import java.util.Map;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.jfinal.kit.HttpKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.mine.pub.kit.JsonMyKit;
import com.mine.pub.pojo.BaseDao;
import com.mine.rd.services.arcGis.pojo.ArcGisDao;

/**
 * @author zyl
 * @date 20171031
 * @定时将数据库中的WGS84坐标系统信息转换成天津90坐标系统信息
 * */
public class CCJob  implements Job 
{
	private BaseDao bd=new ArcGisDao();
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		//删除b表中的历史记录，时间戳小于今天的记录
		String delSql="delete from WOBO_GIS_CONDUITS_TRANSFER where DateDiff(dd,sysdate,getdate())>0";
		Db.update(delSql);
		//将从手机获取来的WGS84原始坐标转换为天津90坐标
    	String sql="select * from WOBO_GIS_CONDUITS";
    	List<Record> find = Db.find(sql);
    	for (Record record : find) {
    		try {
    			String location = record.getStr("LOCATION");
    			location=location.substring(1,location.length()-1);
    			location=location.replaceAll("\\\\", "");
	    		Map xy = JsonMyKit.parse(location, Map.class);
	    		//String arcGisXyStr = HttpKit.get("http://localhost:9016/cc/servlet/CcServlet?x="+xy.get("latitude")+"&y="+xy.get("longitude"));
				String arcGisXyStr = HttpKit.get("http://localhost:7080/cc/servlet/CcServlet?x="+xy.get("latitude")+"&y="+xy.get("longitude"));
				arcGisXyStr=arcGisXyStr.replaceAll("(\r\n|\r|\n|\n\r)", "");
				
				String uuid = record.getStr("UUID");
				String qSql1="select * from WOBO_GIS_CONDUITS_TRANSFER where UUID='"+uuid+"'";
				Record record1 = new Record();
				record1.set("SESSION_ID", record.getStr("SESSION_ID"));
				record1.set("UUID", uuid);
				record1.set("TB_ID", record.getStr("TB_ID"));
				record1.set("LOCATION", arcGisXyStr);
				record1.set("STATUS", "1");
				record1.set("sysdate",bd.getSysdate());
				Record find1 = Db.findFirst(qSql1);
				if(find1 != null)
				{
					record1.set("GS_ID", find1.get("GS_ID"));
					Db.update("WOBO_GIS_CONDUITS_TRANSFER", "GS_ID", record1);
				}
				else
				{
					record1.set("GS_ID", bd.getSeqId("WOBO_GIS_CONDUITS"));
					Db.save("WOBO_GIS_CONDUITS_TRANSFER", "GS_ID", record1);
				}
				Db.deleteById("WOBO_GIS_CONDUITS", "GS_ID", record.getStr("GS_ID"));
	    	} catch (Exception e) {
		        e.printStackTrace();
		        continue;
		    }
		}
	}
}
