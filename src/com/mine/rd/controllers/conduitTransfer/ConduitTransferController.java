package com.mine.rd.controllers.conduitTransfer;


import org.apache.log4j.Logger;

import com.jfinal.aop.Before;
import com.mine.pub.controller.BaseController;
import com.mine.pub.service.Service;
import com.mine.rd.interceptors.LoginInterceptor;
import com.mine.rd.services.conduitTransfer.service.ConduitTransferService;

public class ConduitTransferController extends BaseController {

	private Logger logger = Logger.getLogger(ConduitTransferController.class);
	/**
	 * @author ouyangxu
	 * @date 20170508
	 * 方法：查询中转机构转移列表
	 */
	public void queryTransferConduit(){
		logger.info("查询中转机构转移列表");
		Service service = new ConduitTransferService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("查询中转机构转移列表异常===>" + e.getMessage());
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170508
	 * 方法：查询中转机构列表
	 */
	public void queryTransferEp(){
		logger.info("查询中转机构列表");
		Service service = new ConduitTransferService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("查询中转机构列表异常===>" + e.getMessage());
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170508
	 * 方法：查询中转申请信息
	 */
	public void queryTransferConduitInfo(){
		logger.info("查询中转申请信息");
		Service service = new ConduitTransferService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("查询中转申请信息异常===>" + e.getMessage());
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170508
	 * 方法：新增、修改中转申请
	 */
	@Before(LoginInterceptor.class)
	public void saveTransferConduitInfo(){
		logger.info("新增、修改中转申请");
		Service service = new ConduitTransferService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("新增、修改中转申请异常===>" + e.getMessage());
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170508
	 * 方法：交接中转申请
	 */
	@Before(LoginInterceptor.class)
	public void dealTransferConduitInfo(){
		logger.info("交接中转申请");
		Service service = new ConduitTransferService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("交接中转申请异常===>" + e.getMessage());
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170510
	 * 方法：删除交接中转申请
	 */
	@Before(LoginInterceptor.class)
	public void delTransferConduit(){
		logger.info("删除交接中转申请");
		Service service = new ConduitTransferService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("删除交接中转申请异常===>" + e.getMessage());
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170510
	 * 方法：查询申请转移列表
	 */
	public void queryApplyTransferConduitList(){
		logger.info("查询申请转移列表");
		Service service = new ConduitTransferService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("查询申请转移列表异常===>" + e.getMessage());
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170629
	 * 方法：查询中转单位交接员信息
	 */
	public void checkTransferPersonInfo(){
		logger.info("查询中转单位交接员信息");
		Service service = new ConduitTransferService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("查询中转单位交接员信息异常===>" + e.getMessage());
			e.printStackTrace();
		}
		renderJsonForCors();
	}
}
