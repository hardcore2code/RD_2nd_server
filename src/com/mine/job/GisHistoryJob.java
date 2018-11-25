package com.mine.job;

import java.io.IOException;
import java.util.Map;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.jfinal.json.Jackson;
import com.mine.rd.controllers.arcGis.ArcGisController;
import com.mine.rd.services.arcGis.pojo.ArcGisDao;
import com.mine.rd.websocket.MyWebSocket;

/**
 * @author woody
 * @date 20170717
 * @定时接收移动设备的坐标位置
 * */
public class GisHistoryJob implements Job 
{
	private StringBuffer sb = new StringBuffer();
	private ArcGisDao agd=new ArcGisDao();
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		for(MyWebSocket item: MyWebSocket.webSocketSet){ 
			sb.setLength(0);
            try {
//            	String warningCars = agd.getWarningCars();
            	if(item.paramMap == null)
            	{
	            	sb.append("{");
	            	sb.append("'key':'6','mName':'saveGisHistory',");   //1-服务端向移动端请求位置 
	            	sb.append("'value':'"+item.IWBSESSION+"'");
	            	sb.append("}");
            	}
//            	else
//            	{
//            		if(warningCars != null)
//            		{
//            			sb.append(warningCars);
//            		}
//            	}
            	if(sb.length()>0)
            	{
            		item.sendMessage(sb.toString());
            	}
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
		}
	}
}
