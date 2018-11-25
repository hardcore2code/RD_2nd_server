package com.mine.rd1.controllers.statistics;

import org.apache.log4j.Logger;

import com.jfinal.aop.Clear;
import com.mine.pub.controller.BaseController;
import com.mine.pub.service.Service;
import com.mine.rd1.services.statistics.service.StatisticsService;
@Clear()
public class StatisticsController extends BaseController {
	
	private Logger logger = Logger.getLogger(StatisticsController.class);
	
	/**
	 * @author weizantig
	 * @date 20170519
	 * 方法：登录区市审批app
	 */
	public void appLogin(){
		logger.info("登录区市审批app");
		Service service = new StatisticsService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("登录区市审批app异常===>" + e.getMessage());
			e.printStackTrace();
		}
		renderJsonForCorsLoginRegister();
	}
	
	/**
	 * @author weizantig
	 * @date 20170519
	 * 方法：处置协议列表
	 */
	public void queryAgreementList(){
		logger.info("处置协议列表");
		Service service = new StatisticsService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("处置协议列表异常===>" + e.getMessage());
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author weizantig
	 * @date 20170522
	 * 方法：根据协议id查询处置协议
	 */
	public void getArgeementByAmId(){
		logger.info("根据协议id查询处置协议");
		Service service = new StatisticsService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("根据协议id查询处置协议异常===>" + e.getMessage());
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author weizantig
	 * @date 20170522
	 * 方法：查询转移计划列表
	 */
	public void queryTransferPlanList(){
		logger.info("查询转移计划列表");
		Service service = new StatisticsService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("查询转移计划列表异常===>" + e.getMessage());
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author weizantig
	 * @date 20170522
	 * 方法：根据转移计划id查询转移计划
	 */
	public void getTransferPlanByTpId(){
		logger.info("根据转移计划id查询转移计划");
		Service service = new StatisticsService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("根据转移计划id查询转移计划异常===>" + e.getMessage());
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author weizantig
	 * @date 20170522
	 * 方法：查询转移联单列表
	 */
	public void queryTransferplanBillList(){
		logger.info("查询转移联单列表");
		Service service = new StatisticsService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("查询转移联单列表异常===>" + e.getMessage());
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author weizantig
	 * @date 20170522
	 * 方法：根据转移联单id查询转移联单
	 */
	public void getTransferplanBillByTbId(){
		logger.info("根据转移联单id查询转移联单");
		Service service = new StatisticsService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("根据转移联单id查询转移联单异常===>" + e.getMessage());
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author weizantig
	 * @date 20170523
	 * 方法：查询审批列表
	 */
	public void queryTransferPlanCheckList(){
		logger.info("查询审批列表");
		Service service = new StatisticsService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("查询审批列表异常===>" + e.getMessage());
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author weizantig
	 * @date 20170523
	 * 方法：查询审批历史列表
	 */
	public void appCallAuditCx(){
		logger.info("查询审批历史列表");
		Service service = new StatisticsService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("查询审批历史列表异常===>" + e.getMessage());
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author weizantig
	 * @return 
	 * @date 20170522
	 * 方法：查询字典列表
	 */
	public void getDicts(){
		logger.info("查询字典列表");
		Service service = new StatisticsService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("查询字典列表异常===>" + e.getMessage());
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	/**
	 * @author zhangyuli
	 * @return 
	 * @date 20170725
	 * 方法：查询审批需要的人员信息
	 */
	public void queryPresonInfo(){
		logger.info("查询审批需要的人员信息");
		Service service = new StatisticsService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("查询审批需要的人员信息异常===>" + e.getMessage());
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
}
