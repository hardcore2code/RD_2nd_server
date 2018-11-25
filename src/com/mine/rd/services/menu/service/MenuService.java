package com.mine.rd.services.menu.service;

import java.sql.SQLException;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.mine.pub.controller.BaseController;
import com.mine.pub.service.BaseService;
import com.mine.rd.services.menu.pojo.MenuDao;

public class MenuService extends BaseService {

	private MenuDao dao = new MenuDao();
	public MenuService(BaseController controller) {
		super(controller);
	}

	/**
	 * @author ouyangxu
	 * @date 20170316
	 * 方法：查询菜单
	 */
	private void queryMenu(){
		controller.setAttr("menuList", dao.queryMenu());
		controller.setAttr("resFlag", "0");
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170316
	 * 方法：查询单个菜单信息
	 */
	private void queryMenuInfo(){
		String menuId = controller.getMyParam("menuId").toString();
		controller.setAttr("menuInfo", dao.queryMenuInfo(menuId));
		controller.setAttr("resFlag", "0");
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170316
	 * 方法：修改菜单状态
	 */
	private void updateMenu(){
		String menuId = controller.getMyParam("menuId").toString();
		String menuName = controller.getMyParam("menuName").toString();
		String status = controller.getMyParam("status").toString();
		if(dao.updateMenu(menuId, status, menuName)){
			controller.setAttr("msg", "保存成功！");
			controller.setAttr("resFlag", "0");
		}else{
			controller.setAttr("msg", "保存失败！");
			controller.setAttr("resFlag", "1");
		}
	}
	
	@Override
	public void doService() throws Exception {
		Db.tx(new IAtom() {
	        @Override
	        public boolean run() throws SQLException {
	            try {
	            	if("queryMenu".equals(getLastMethodName(7))){
	        			queryMenu();
	        		}else if("queryMenuInfo".equals(getLastMethodName(7))){
	        			queryMenuInfo();
	        		}else if("updateMenu".equals(getLastMethodName(7))){
	        			updateMenu();
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
