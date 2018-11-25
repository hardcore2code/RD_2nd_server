package com.mine.rd.controllers.user;

import org.apache.log4j.Logger;

import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.mine.pub.controller.BaseController;
import com.mine.pub.service.Service;
import com.mine.rd.interceptors.LoginInterceptor;
import com.mine.rd.services.user.service.UserService;

public class UserController extends BaseController {

	private Logger logger = Logger.getLogger(UserController.class);
	
	/**
	 * @author ouyangxu
	 * @date 20170221
	 * 方法：退出登录
	 */
	public void logout(){
		logger.info("退出登录");
		Service service = new UserService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("退出登录异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170221
	 * 方法：修改密码
	 */
	@Before(LoginInterceptor.class)
	public void modifyPwd(){
		logger.info("修改密码");
		Service service = new UserService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("修改密码异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170503
	 * 方法：忘记密码
	 */
	@Clear
	public void forgetPwd(){
		logger.info("忘记密码");
		Service service = new UserService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("忘记密码异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
}
