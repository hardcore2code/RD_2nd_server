package com.mine.rd.services.user.service;


import java.sql.SQLException;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.ehcache.CacheKit;
import com.mine.pub.controller.BaseController;
import com.mine.pub.service.BaseService;
import com.mine.rd.services.user.pojo.UserDao;

public class UserService extends BaseService {

	private UserDao dao = new UserDao();
	public UserService(BaseController controller) {
		super(controller);
	}

	/**
	 * 
	 * @author weizanting
	 * @date 20161221
	 * 方法：退出登录
	 * 
	 */
	private void logout(){
		String sessionId = controller.getMyParam("IWBSESSION").toString();
		dao.logout(sessionId);
		CacheKit.remove("mySession", sessionId);
		controller.setAttr("resFlag","0");
		controller.setAttr("msg","注销成功！");
	}
	
	/**
	 * 
	 * @author weizanting
	 * @date 20161221
	 * 方法：修改密码
	 * 
	 */
	private void modifyPwd(){
		String userId = controller.getMyParam("userId").toString();
		String newPwd = controller.getMyParam("newPwd").toString();
		String oldPwd = controller.getMyParam("oldPwd").toString();
		String userType = controller.getMyParam("userType").toString();
		String nickName = controller.getMyParam("nickName").toString();
		if(dao.checkPwd(userId, oldPwd, nickName, userType)){
			if(dao.modifyPwd(userId, newPwd, nickName, userType)){
				controller.setAttr("resFlag", "0");
				controller.setAttr("msg", "修改成功！");
			}else{
				controller.setAttr("resFlag", "1");
				controller.setAttr("msg", "修改失败！");
			}
		}else{
			controller.setAttr("resFlag", "1");
			controller.setAttr("msg", "原密码错误！");
		}
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170503
	 * 方法：忘记密码
	 */
	private void forgetPwd(){
		String epName = controller.getMyParam("epName").toString();
		String orgCode = controller.getMyParam("orgCode").toString();
		String mail = controller.getMyParam("mail").toString();
		String name = controller.getMyParam("name").toString();
		if(dao.checkEpInfo(epName, orgCode)){
			if(dao.checkApply(epName, orgCode)){
				if(dao.forgetPwd(epName, orgCode, mail, name)){
					controller.setAttr("resFlag", "0");
					controller.setAttr("msg", "提交申请成功！");
				}else{
					controller.setAttr("resFlag", "1");
					controller.setAttr("msg", "提交申请失败！");
				}
			}else{
				controller.setAttr("resFlag", "1");
				controller.setAttr("msg", "您已提交过申请，请勿重复提交！");
			}
		}else{
			controller.setAttr("resFlag", "1");
			controller.setAttr("msg", "公司不存在，请重新填写！");
		}
	}
	
	@Override
	public void doService() throws Exception {
		Db.tx(new IAtom() {
	        @Override
	        public boolean run() throws SQLException {
	            try {
	            	if("logout".equals(getLastMethodName(7))){
	        			logout();
	        		}else if("modifyPwd".equals(getLastMethodName(7))){
	        			modifyPwd();
	        		}else if("forgetPwd".equals(getLastMethodName(7))){
	        			forgetPwd();
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
