package com.mine.task.gis;

import java.io.IOException;
import java.util.Map;

import com.jfinal.json.Jackson;
import com.mine.rd.controllers.arcGis.ArcGisController;
import com.mine.rd.websocket.MyWebSocket;

/**
 * @author woody
 * @date 20170717
 * @定时接收移动设备的坐标位置
 * */
public class GisTask implements Runnable 
{
	private StringBuffer sb = new StringBuffer();
	@Override
	public void run() {
//		System.out.println("==============11111111");
		for(MyWebSocket item: MyWebSocket.webSocketSet){ 
			sb.setLength(0);
			System.out.println("item==============>"+item.toString());
    		System.out.println("item.iwbsession==============>"+item.IWBSESSION);
            try {
            	if(item.paramMap == null)
            	{
	            	sb.append("{");
	            	sb.append("'key':'1',");   //1-服务端向移动端请求位置 
	            	sb.append("'value':'"+item.IWBSESSION+"'");
	            	sb.append("}");
            	}
            	else
            	{
            		String mName = item.paramMap.get("mName") == null ? "" : item.paramMap.get("mName").toString();
            		if(!"".equals(mName))
            		{
            			String qc = item.paramMap.get("qc").toString();
                		ArcGisController bc=new ArcGisController();
                		String message="{\"mName\":\""+mName+"\",\"qc\":\""+qc+"\"}";
                		bc.getCarPositions(message);
                		Map<String, Object> result= (Map<String, Object>)bc.resMap.get("carPositions");
                		String json = Jackson.getJson().toJson(result);
                		sb.append(json);
            		}
            	}
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
