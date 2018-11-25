package com.mine.rd.controllers.role;

import org.apache.log4j.Logger;

import com.jfinal.aop.Before;
import com.mine.pub.controller.BaseController;
import com.mine.pub.service.Service;
import com.mine.rd.interceptors.LoginInterceptor;
import com.mine.rd.services.role.service.RoleService;

public class RoleController extends BaseController {
	private Logger logger = Logger.getLogger(RoleController.class);
	/**
	 * @author ouyangxu
	 * @date 20170315
	 * 方法：查询角色
	 */
	public void queryRoleList(){
		logger.info("查询角色");
		Service service = new RoleService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("查询角色异常" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170315
	 * 方法：查询角色信息
	 */
	public void queryRoleInfo(){
		logger.info("查询角色信息");
		Service service = new RoleService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("查询角色信息异常" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170315
	 * 方法：新增或修改角色
	 */
	@Before(LoginInterceptor.class)
	public void addOrUpdateRole(){
		logger.info("新增或修改角色");
		Service service = new RoleService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("新增或修改角色异常" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170315
	 * 方法：删除角色
	 */
	@Before(LoginInterceptor.class)
	public void delRole(){
		logger.info("删除角色");
		Service service = new RoleService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("删除角色异常" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170315
	 * 方法：查询角色关联菜单情况
	 */
	public void queryRoleMenu(){
		logger.info("查询角色关联菜单情况");
		Service service = new RoleService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("查询角色关联菜单情况异常" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170316
	 * 方法：关联角色菜单
	 */
	@Before(LoginInterceptor.class)
	public void manageRoleMenu(){
		logger.info("关联角色菜单");
		Service service = new RoleService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("关联角色菜单异常" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170315
	 * 方法：查询角色关联人员情况
	 */
	public void queryRoleUser(){
		logger.info("查询角色关联人员情况");
		Service service = new RoleService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("查询角色关联人员情况异常" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170315
	 * 方法：关联角色人员
	 */
	@Before(LoginInterceptor.class)
	public void manageRoleUser(){
		logger.info("关联角色人员");
		Service service = new RoleService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("关联角色人员异常" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
}
