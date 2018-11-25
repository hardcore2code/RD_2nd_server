package com.mine.rd.controllers.transfer;

import org.apache.log4j.Logger;

import com.jfinal.aop.Before;
import com.mine.pub.controller.BaseController;
import com.mine.pub.service.Service;
import com.mine.rd.interceptors.LoginInterceptor;
import com.mine.rd.services.transfer.service.TransferService;

public class TransferController extends BaseController {
	
private Logger logger = Logger.getLogger(TransferController.class);
	
	/**
	 * @author weizanting
	 * @date 20170331
	 * 方法：查询医疗废物运程列表
	 */
	public void queryHaulList(){
		logger.info("查询医疗废物运程列表");
		Service service = new TransferService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("查询医疗废物运程列表异常===>" + e.getMessage());
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author weizanting
	 * @date 20170331
	 * 方法：新增医疗废物运程
	 */
	@Before(LoginInterceptor.class)
	public void saveHaul(){
		logger.info("查询医疗废物运程");
		Service service = new TransferService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("查询医疗废物运程异常===>" + e.getMessage());
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author weizanting
	 * @date 20170331
	 * 方法：查询医疗废物运单列表
	 */
	public void queryBillList(){
		logger.info("查询医疗废物运单列表");
		Service service = new TransferService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("查询医疗废物运单列表异常===>" + e.getMessage());
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author weizanting
	 * @date 20170331
	 * 方法：查询医疗废物运单
	 */
	public void queryBill(){
		logger.info("查询医疗废物运单");
		Service service = new TransferService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("查询医疗废物运单异常===>" + e.getMessage());
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author weizanting
	 * @date 20170331
	 * 方法：保存医疗废物运单
	 */
	@Before(LoginInterceptor.class)
	public void saveBill(){
		logger.info("保存医疗废物运单");
		Service service = new TransferService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("保存医疗废物运单异常===>" + e.getMessage());
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author weizanting
	 * @date 20170428
	 * 方法：保存医疗废物总量及修改行程、运单、箱子状态
	 */
	@Before(LoginInterceptor.class)
	public void saveWeight(){
		logger.info("保存医疗废物总量及修改行程、运单、箱子状态");
		Service service = new TransferService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("保存医疗废物总量及修改行程、运单、箱子状态异常===>" + e.getMessage());
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170503
	 * 方法：管理员查询医疗废物运程列表
	 */
	public void queryHaulListForAdmin(){
		logger.info("管理员查询医疗废物运程列表");
		Service service = new TransferService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("管理员查询医疗废物运程列表异常===>" + e.getMessage());
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170503
	 * 方法：产生单位查询医疗废物运程列表
	 */
	public void queryBillListForEpCs(){
		logger.info("产生单位查询医疗废物运程列表");
		Service service = new TransferService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("产生单位查询医疗废物运程列表异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}

	/**
	 * @author weizanting
	 * @date 20170526
	 * 方法：删除没有运单的行程
	 */
	@Before(LoginInterceptor.class)
	public void deleteHaul(){
		logger.info("删除没有运单的行程");
		Service service = new TransferService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("删除没有运单的行程异常===>" + e.getMessage());
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author weizanting
	 * @date 20170628
	 * 方法：查询医疗废物未完成行程
	 */
	public void queryHaul(){
		logger.info("查询医疗废物未完成行程");
		Service service = new TransferService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("查询医疗废物未完成行程异常===>" + e.getMessage());
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author weizanting
	 * @date 20170628
	 * 方法：查询处置单位车辆信息、查询产生单位人员信息
	 */
	public void queryInfo(){
		logger.info("查询处置单位车辆信息、查询产生单位人员信息");
		Service service = new TransferService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("查询处置单位车辆信息、查询产生单位人员信息异常===>" + e.getMessage());
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author weizanting
	 * @date 20170628
	 * 方法：先删除同行程下同单位的行程，再保存医疗废物运单
	 */
	@Before(LoginInterceptor.class)
	public void updateBill(){
		logger.info("先删除同行程下同单位的行程，再保存医疗废物运单");
		Service service = new TransferService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("先删除同行程下同单位的行程，再保存医疗废物运单异常===>" + e.getMessage());
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author weizanting
	 * @date 20170629
	 * 方法：查询医疗废物运程列表
	 */
	public void queryHaulListForUser(){
		logger.info("查询医疗废物运程列表");
		Service service = new TransferService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("查询医疗废物运程列表异常===>" + e.getMessage());
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author weizanting
	 * @date 20170629
	 * 方法：查询医疗废物桶净重
	 */
	public void queryBoxSuttle(){
		logger.info("查询医疗废物桶净重");
		Service service = new TransferService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("查询医疗废物桶净重异常===>" + e.getMessage());
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
}
