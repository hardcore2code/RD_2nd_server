package com.mine.rd.controllers.login;


import org.apache.log4j.Logger;

import com.jfinal.aop.Clear;
import com.mine.pub.controller.BaseController;
import com.mine.pub.service.Service;
import com.mine.rd.services.login.service.LoginService;
@Clear
public class LoginController extends BaseController {
	
	private Logger logger = Logger.getLogger(LoginController.class);
	
	/**
	 * @author ouyangxu
	 * @date 20170221
	 * 方法：单位登录
	 */
	public void epLogin(){
		logger.info("单位登录");
		Service service = new LoginService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("单位登录异常" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
//		if(!"0".equals(this.getAttr("resFlag"))){
//			this.setAttr("IWBSESSION", "");
//		}
		renderJsonForCorsLoginRegister();
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170221
	 * 方法：单位注册
	 */
	public void epRegister(){
		logger.info("单位注册");
		Service service = new LoginService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("单位注册异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
//		if(!"0".equals(this.getAttr("resFlag"))){
//			this.setAttr("IWBSESSION", "");
//		}
		renderJsonForCors();
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170221
	 * 方法：管理员登录
	 */
	public void adminLogin(){
		logger.info("管理员登录");
		Service service = new LoginService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("管理员登录异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
//		if(!"0".equals(this.getAttr("resFlag"))){
//			this.setAttr("IWBSESSION", "");
//		}
		renderJsonForCorsLoginRegister();
	}
	
	/**
	 * @author weizanting
	 * @date 20170227
	 * 方法：医院机构管理员登录
	 */
	public void epAdminLogin(){
		logger.info("医院机构管理员登录");
		Service service = new LoginService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("医院机构管理员登录异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
//		if(!"0".equals(this.getAttr("resFlag"))){
//			this.setAttr("IWBSESSION", "");
//		}
		renderJsonForCorsLoginRegister();
	}
	
	/**
	 * @author weizanting
	 * @date 20170227
	 * 方法：PDA登录
	 */
	public void loginForPDA(){
		logger.info("PDA登录");
		Service service = new LoginService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("PDA登录异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
//		if(!"0".equals(this.getAttr("resFlag"))){
//			this.setAttr("IWBSESSION", "");
//		}
		renderJsonForCorsLoginRegister();
	}
	
	/**
	 * @author weizanting
	 * @date 20170227
	 * 方法：二维码登录校验信息
	 */
	public void loginForPDAScanner(){
		logger.info("二维码登录校验信息");
		Service service = new LoginService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("二维码登录校验信息异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
//		if(!"0".equals(this.getAttr("resFlag"))){
//			this.setAttr("IWBSESSION", "");
//		}
		renderJsonForCorsLoginRegister();
	}
	
	/**
	 * @author weizanting
	 * @date 20170227
	 * 方法：二维码登录校验信息 for 医疗单位
	 */
	public void loginForPDAScannerForYl(){
		logger.info("二维码登录校验信息 for 医疗单位");
		Service service = new LoginService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("二维码登录校验信息 for 医疗单位异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
//		if(!"0".equals(this.getAttr("resFlag"))){
//			this.setAttr("IWBSESSION", "");
//		}
		renderJsonForCorsLoginRegister();
	}
	
	/**
	 * @author ouyangxu
	 * @throws Exception 
	 * @date 20170510
	 * 方法：APP账号登录
	 */
	public void loginForAPP(){
		logger.info("APP账号登录");
		Service service = new LoginService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("APP账号登录异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
//		if(!"0".equals(this.getAttr("resFlag"))){
//			this.setAttr("IWBSESSION", "");
//		}
		renderJsonForCorsLoginRegister();
	}
	
	/** 
     * 返回验证码图片
     */
	public void getCheckCode() {
		logger.info("返回验证码图片");
		try {
			super.getCheckCode();
		} catch (Exception e) {
			logger.error("返回验证码图片异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
	}
	
	/** 
     * 返回验证码图片数字
     */
	public void getCheckCodeNum() {
		logger.info("返回验证码图片数字");
		try {
			super.getCheckCodeNum();
		} catch (Exception e) {
			logger.error("返回验证码图片数字异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
	}
}
