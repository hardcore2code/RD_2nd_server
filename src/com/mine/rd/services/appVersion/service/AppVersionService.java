package com.mine.rd.services.appVersion.service;

import java.sql.SQLException;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.mine.pub.controller.BaseController;
import com.mine.pub.service.BaseService;
import com.mine.rd.services.appVersion.pojo.AppVersionDao;

public class AppVersionService extends BaseService {

	private AppVersionDao dao = new AppVersionDao();
	public AppVersionService(BaseController controller) {
		super(controller);
	}

	/**
	 * @author ouyangxu
	 * @date 20170906
	 * 方法：检查app是否有更新
	 */
	private void checkAppVersion(){
		int appId = Integer.parseInt(controller.getMyParam("appId").toString());
		String appVersion = controller.getMyParam("appVersion").toString();
		if(dao.checkAppVersion(appId, appVersion)){
			controller.setAttr("resFlag", "0");
			controller.setAttr("msg", "可以更新！");
		}else{
			controller.setAttr("resFlag", "1");
			controller.setAttr("msg", "已是最新版本，无需更新！");
		}
	}
	
	@Override
	public void doService() throws Exception {
		Db.tx(new IAtom() {
	        @Override
	        public boolean run() throws SQLException {
	            try {
	            	if("checkAppVersion".equals(getLastMethodName(7))){
	            		checkAppVersion();
	        		}
	            } catch (Exception e) {
	                e.printStackTrace();
	                controller.setAttr("msg", "系统异常，请重新登录！");
	    			controller.setAttr("resFlag", "1");
	                return false;
	            }
	            return true;
	        }
	    });
	}

}
