package com.mine.rd.controllers.plan;

import org.apache.log4j.Logger;

import com.jfinal.aop.Before;
import com.mine.pub.controller.BaseController;
import com.mine.pub.service.Service;
import com.mine.rd.interceptors.LoginInterceptor;
import com.mine.rd.services.plan.service.PlanService;

public class PlanController extends BaseController {

	private Logger logger = Logger.getLogger(PlanController.class);
	
	/**
	 * @author weizanting
	 * @date 20170306
	 * 方法：查询医疗废物转移计划列表
	 */
	public void queryPlanList(){
		logger.info("查询医疗废物转移计划列表");
		Service service = new PlanService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("查询医疗废物转移计划列表异常===>" + e.getMessage());
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author weizanting
	 * @date 20170306
	 * 方法：查询医疗废物转移计划
	 */
	public void queryPlan(){
		logger.info("查询医疗废物转移计划");
		Service service = new PlanService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("查询医疗废物转移计划异常===>" + e.getMessage());
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author weizanting
	 * @date 20170306
	 * 方法：保存医疗废物转移计划
	 */
	@Before(LoginInterceptor.class)
	public void savePlan(){
		logger.info("保存医疗废物转移计划");
		Service service = new PlanService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("保存医疗废物转移计划异常===>" + e.getMessage());
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author weizanting
	 * @date 20170306
	 * 方法：提交医疗废物转移计划
	 */
	@Before(LoginInterceptor.class)
	public void submitPlan(){
		logger.info("提交医疗废物转移计划");
		Service service = new PlanService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("提交医疗废物转移计划异常===>" + e.getMessage());
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170526
	 * 方法：管理员查询医疗废物转移计划列表
	 */
	public void queryPlanListForAdmin(){
		logger.info("管理员查询医疗废物转移计划列表");
		Service service = new PlanService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("管理员查询医疗废物转移计划列表异常===>" + e.getMessage());
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170621
	 * 方法：查询运行中转移计划列表
	 */
	public void queryTransferRunningList(){
		logger.info("查询运行中转移计划列表");
		Service service = new PlanService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("查询运行中转移计划列表异常===>" + e.getMessage());
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170621
	 * 方法：查询运行中医疗废物转移计划详情
	 */
	public void queryTransferRunningInfo(){
		logger.info("查询运行中医疗废物转移计划详情");
		Service service = new PlanService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("查询运行中医疗废物转移计划详情异常===>" + e.getMessage());
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170621
	 * 方法：运行中转移计划修改中转单位
	 */
	@Before(LoginInterceptor.class)
	public void saveTransferRunningModify(){
		logger.info("运行中转移计划修改中转单位");
		Service service = new PlanService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("运行中转移计划修改中转单位异常===>" + e.getMessage());
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170622
	 * 方法：提交运行中转移计划修改中转单位
	 */
	@Before(LoginInterceptor.class)
	public void submitTransferRunningModify(){
		logger.info("提交运行中转移计划修改中转单位");
		Service service = new PlanService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("提交运行中转移计划修改中转单位异常===>" + e.getMessage());
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170622
	 * 方法：查询运行中转移计划修改中转单位记录
	 */
	public void queryModifyTransferOrgList(){
		logger.info("查询运行中转移计划修改中转单位记录");
		Service service = new PlanService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("查询运行中转移计划修改中转单位记录异常===>" + e.getMessage());
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170703
	 * 方法：查询医疗废物转移计划列表by applyList status
	 */
	public void queryPlanListByApply(){
		logger.info("查询医疗废物转移计划列表by applyList status");
		Service service = new PlanService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("查询医疗废物转移计划列表by applyList status异常===>" + e.getMessage());
			e.printStackTrace();
		}
		renderJsonForCors();
	}
}
