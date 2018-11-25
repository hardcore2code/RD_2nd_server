package com.mine.rd1.controllers.statistics;

import org.apache.log4j.Logger;

import com.jfinal.aop.Before;
import com.mine.pub.controller.BaseController;
import com.mine.pub.service.Service;
import com.mine.rd.interceptors.LoginInterceptor;
import com.mine.rd1.services.statistics.service.ExecuteSqlService;

public class ExecuteSqlController extends BaseController {
	
	private Logger logger = Logger.getLogger(ExecuteSqlController.class);
	
	/**
	 * @author zyl
	 * @date 20170519
	 * 方法：执行语句
	 */
	@Before(LoginInterceptor.class)
	public void executeSql(){
		logger.info("执行语句");
		Service service = new ExecuteSqlService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("执行语句异常===>" + e.getMessage());
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author zyl
	 * @date 20170519
	 * 方法：获取执行SQL模块的加密key值
	 */
	public void getExeSqlCodeKey(){
		logger.info("获取执行SQL模块的加密key值");
		Service service = new ExecuteSqlService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("执行语句异常===>" + e.getMessage());
			e.printStackTrace();
		}
		renderJsonForCors();
	}
}
