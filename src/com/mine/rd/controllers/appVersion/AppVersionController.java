package com.mine.rd.controllers.appVersion;

import org.apache.log4j.Logger;

import com.jfinal.aop.Clear;
import com.mine.pub.controller.BaseController;
import com.mine.pub.service.Service;
import com.mine.rd.interceptors.CheckSessionInterceptor;
import com.mine.rd.services.appVersion.service.AppVersionService;

@Clear(CheckSessionInterceptor.class)
public class AppVersionController extends BaseController {
	private Logger logger = Logger.getLogger(AppVersionController.class);
	
	/**
	 * @author ouyangxu
	 * @date 20170906
	 * 方法：检查app是否有更新
	 */
	public void checkAppVersion(){
		logger.info("检查app是否有更新");
		Service service = new AppVersionService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("检查app是否有更新异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
}
