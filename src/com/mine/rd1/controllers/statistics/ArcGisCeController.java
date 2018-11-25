package com.mine.rd1.controllers.statistics;

import java.util.Map;

import org.apache.log4j.Logger;

import com.jfinal.aop.Clear;
import com.mine.pub.controller.BaseController;
import com.mine.pub.kit.JsonMyKit;
import com.mine.pub.service.Service;
import com.mine.rd1.services.statistics.service.ArcGisCeService;
@Clear()
public class ArcGisCeController extends BaseController {
	
	private Logger logger = Logger.getLogger(ArcGisCeController.class);
	
	/**
	 * @author 张玉利
	 * @date 20171122
	 * 方法：保存Wince设备的设备坐标到坐标a表中
	 */
	public void saveGisCus(){
		logger.info("保存Wince设备的设备坐标到坐标a表中");
		Service service = new ArcGisCeService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("保存Wince设备的设备坐标到坐标a表中异常===>" + e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * @author 张玉利
	 * @date 20171122
	 * 方法：保存Wince设备的设备坐标到坐标a表中
	 */
	public void saveGisTeyCus(){
		logger.info("保存Wince设备的设备坐标到坐标c表中");
		System.out.println("go in fun saveGisTeyCus!!!");
		Service service = new ArcGisCeService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("保存Wince设备的设备坐标到坐标c表中异常===>" + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		String str="{\"tbId\":\"\",\"sessionId\":\"\",\"uuid\":\"696969\",\"location\":{\"latitude\":666,\"longitude\":999}}";
		Map map=JsonMyKit.parse(str, Map.class);
		System.out.println(map);
	}
}
