package com.mine.rd.controllers.menu;

import java.util.List;

import org.apache.log4j.Logger;

import com.jfinal.aop.Before;
import com.jfinal.plugin.ehcache.CacheKit;
import com.mine.pub.controller.BaseController;
import com.mine.pub.service.Service;
import com.mine.rd.interceptors.LoginInterceptor;
import com.mine.rd.services.menu.service.MenuService;
import com.mine.rd.services.user.pojo.UserDao;

public class MenuController extends BaseController {
	private Logger logger = Logger.getLogger(MenuController.class);
	/**
	 * @author ouyangxu
	 * @date 20170316
	 * 方法：查询菜单
	 */
	public void queryMenu(){
		logger.info("查询菜单");
		Service service = new MenuService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("查询菜单" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170316
	 * 方法：查询单个菜单信息
	 */
	public void queryMenuInfo(){
		logger.info("查询单个菜单信息");
		Service service = new MenuService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("查询单个菜单信息" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170316
	 * 方法：修改菜单状态
	 */
	@Before(LoginInterceptor.class)
	public void updateMenu(){
		logger.info("修改菜单状态");
		Service service = new MenuService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("修改菜单状态" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	//获取角色菜单
	public void getMenuList()
	{
		UserDao dao = new UserDao();
		@SuppressWarnings({ "rawtypes", "unused" })
		List list = CacheKit.getKeys("mySession");
		String userId = this.getMySession("userId").toString();
		setAttr("menulist", dao.getMenuList(userId));
		renderJsonForCors();
	}
}
