package com.mine.rd1.controllers.statistics;

import org.apache.log4j.Logger;

import com.jfinal.aop.Clear;
import com.mine.pub.controller.BaseController;
import com.mine.pub.service.Service;
import com.mine.rd1.services.statistics.service.TransferService;
@Clear
public class TransferController extends BaseController {
	
	private Logger logger = Logger.getLogger(TransferController.class);
	
	/**
	 * @author zyl
	 * @date 20170519
	 * 方法：app查询转移联单信息
	 */
	public void queryTransferplanBill(){
		logger.info("app查询转移联单信息");
		Service service = new TransferService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("app查询转移联单信息异常===>" + e.getMessage());
			e.printStackTrace();
		}
		renderJsonForCorsLoginRegister();
	}
	
	/**
	 * @author zyl
	 * @date 20170519
	 * 方法：app更正转移联单状态方法
	 */
	public void updateStatus(){
		logger.info("app更正转移联单状态方法");
		Service service = new TransferService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("app更正转移联单状态方法异常===>" + e.getMessage());
			e.printStackTrace();
		}
		renderJsonForCorsLoginRegister();
	}
	
	/**
	 * @author zyl
	 * @date 20170519
	 * 方法：app查询人员卡的人员类型
	 */
	public void queryPerson(){
		logger.info("app查询人员卡的人员类型");
		Service service = new TransferService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("app更正转移联单状态方法异常===>" + e.getMessage());
			e.printStackTrace();
		}
		renderJsonForCorsLoginRegister();
	}
	
	
}
