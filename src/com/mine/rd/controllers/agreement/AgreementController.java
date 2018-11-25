package com.mine.rd.controllers.agreement;

import org.apache.log4j.Logger;

import com.jfinal.aop.Before;
import com.mine.pub.controller.BaseController;
import com.mine.pub.service.Service;
import com.mine.rd.interceptors.LoginInterceptor;
import com.mine.rd.services.agreement.service.AgreementService;

public class AgreementController extends BaseController {

	private Logger logger = Logger.getLogger(AgreementController.class);
	
	/**
	 * @author weizanting
	 * @date 20170227
	 * 方法：查询医疗废物处置协议列表
	 */
	public void queryAgreementList(){
		logger.info("查询医疗废物处置协议列表");
		Service service = new AgreementService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("查询医疗废物处置协议列表异常===>" + e.getMessage());
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author weizanting
	 * @date 20170227
	 * 方法：查询医疗废物处置协议列表for cz
	 */
	public void queryAgreementListForCz(){
		logger.info("查询医疗废物处置协议列表for cz");
		Service service = new AgreementService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("查询医疗废物处置协议列表for cz异常===>" + e.getMessage());
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author weizanting
	 * @date 20170303
	 * 方法：查询医疗废物处置协议
	 */
	public void queryAgreement(){
		logger.info("查询医疗废物处置协议");
		Service service = new AgreementService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("查询医疗废物处置协议异常===>" + e.getMessage());
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author weizanting
	 * @date 20170303
	 * 方法：保存医疗废物处置协议
	 */
	@Before(LoginInterceptor.class)
	public void saveAgreement(){
		logger.info("保存医疗废物处置协议");
		Service service = new AgreementService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("保存医疗废物处置协议异常===>" + e.getMessage());
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author weizanting
	 * @date 20170303
	 * 方法：提交医疗废物处置协议
	 */
	@Before(LoginInterceptor.class)
	public void submitAgreement(){
		logger.info("提交医疗废物处置协议");
		Service service = new AgreementService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("提交医疗废物处置协议异常===>" + e.getMessage());
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author weizanting
	 * @date 20170303
	 * 方法：处置单位同意医疗废物处置协议
	 */
	@Before(LoginInterceptor.class)
	public void agree(){
		logger.info("处置单位同意医疗废物处置协议");
		Service service = new AgreementService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("处置单位同意医疗废物处置协议异常===>" + e.getMessage());
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author weizanting
	 * @date 20170303
	 * 方法：处置单位驳回医疗废物处置协议
	 */
	@Before(LoginInterceptor.class)
	public void reject(){
		logger.info("处置单位驳回医疗废物处置协议");
		Service service = new AgreementService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("处置单位驳回医疗废物处置协议异常===>" + e.getMessage());
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author weizanting
	 * @date 20170306
	 * 方法：查询企业列表
	 */
	public void queryEPList(){
		logger.info("查询企业列表");
		Service service = new AgreementService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("查询企业列表异常===>" + e.getMessage());
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170526
	 * 方法：查询医疗废物处置协议列表
	 */
	public void queryAgreementListForAdmin(){
		logger.info("查询医疗废物处置协议列表");
		Service service = new AgreementService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("查询医疗废物处置协议列表异常===>" + e.getMessage());
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
}
