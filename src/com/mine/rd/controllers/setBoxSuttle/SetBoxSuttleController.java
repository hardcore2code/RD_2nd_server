package com.mine.rd.controllers.setBoxSuttle;

import org.apache.log4j.Logger;

import com.mine.pub.controller.BaseController;
import com.mine.pub.service.Service;
import com.mine.rd.services.setBoxSuttle.service.SetBoxSuttleService;

public class SetBoxSuttleController extends BaseController {

	private Logger logger = Logger.getLogger(SetBoxSuttleController.class);
	
	/**
	 * @author weizanting
	 * @date 20171016
	 * 方法：保存或修改桶净重
	 */
	public void setBoxSuttle(){
		logger.info("保存或修改桶净重");
		Service service = new SetBoxSuttleService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("保存或修改桶净重异常===>" + e.getMessage());
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
}
