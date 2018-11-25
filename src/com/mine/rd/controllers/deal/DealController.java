package com.mine.rd.controllers.deal;

import org.apache.log4j.Logger;

import com.jfinal.aop.Before;
import com.mine.pub.controller.BaseController;
import com.mine.pub.service.Service;
import com.mine.rd.interceptors.LoginInterceptor;
import com.mine.rd.services.deal.service.DealService;

public class DealController extends BaseController {
	private Logger logger = Logger.getLogger(DealController.class);
	
	/**
	 * @author weizanting
	 * @date 20170227
	 * 方法：查询当天处置信息
	 */
	public void queryDealListToday(){
		logger.info("查询当天处置信息");
		Service service = new DealService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("查询当天处置信息异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170405
	 * 方法：查询历史处置信息
	 */
	public void queryDealListHistory(){
		logger.info("查询历史处置信息");
		Service service = new DealService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("查询历史处置信息异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170405
	 * 方法：处置医疗废物
	 */
	@Before(LoginInterceptor.class)
	public void dealBox(){
		logger.info("处置医疗废物");
		Service service = new DealService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("处置医疗废物异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
}
