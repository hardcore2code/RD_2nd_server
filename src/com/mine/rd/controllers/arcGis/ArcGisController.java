package com.mine.rd.controllers.arcGis;

import org.apache.log4j.Logger;

import com.jfinal.aop.Clear;
import com.mine.pub.controller.BaseController;
import com.mine.pub.service.Service;
import com.mine.rd.services.arcGis.service.ArcGisService;

@Clear()
public class ArcGisController extends BaseController {
	private Logger logger = Logger.getLogger(ArcGisController.class);
	
	/**
	 * @author zyl
	 * @date 20170906
	 * 方法：根据车辆牌照获取车辆位置
	 */
	
	public void getCarPositions(String mess){
		logger.info("查询车辆位置");
		Service service = new ArcGisService(this,mess);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("查询车辆位置异常===>" + e.getMessage());
			e.printStackTrace();
		}
	}
}
