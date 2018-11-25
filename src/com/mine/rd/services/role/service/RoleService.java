package com.mine.rd.services.role.service;

import java.sql.SQLException;
import java.util.Map;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.mine.pub.controller.BaseController;
import com.mine.pub.service.BaseService;
import com.mine.rd.services.role.pojo.RoleDao;

public class RoleService extends BaseService {

	private RoleDao dao = new RoleDao();
	private int pn = 0;
	private int ps = 0;
	
	public RoleService(BaseController controller) {
		super(controller);
	}

	/**
	 * @author ouyangxu
	 * @date 20170315
	 * 方法：查询角色
	 */
	private void queryRoleList(){
		controller.setAttrs(dao.queryRoleList());
		controller.setAttr("resFlag", "0");
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170315
	 * 方法：查询角色信息
	 */
	private void queryRoleInfo(){
		String roleId = controller.getMyParam("roleId").toString();
		Map<String, Object> map = dao.queryRoleInfo(roleId);
		controller.setAttr("roleInfo", map);
		controller.setAttr("resFlag", "0");
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170315
	 * 方法：新增或修改角色
	 */
	private void addOrUpdateRole(){
		String roleId = controller.getMyParam("roleId").toString();
		String roleName = controller.getMyParam("roleName").toString();
		Object status = controller.getMyParam("status");
		if(dao.checkRoleName(roleId, roleName)){
			if(status != null){
				status = Integer.parseInt(status.toString())-1;
			}
			String returnId = dao.addOrUpdateRole(roleId, roleName, status+"");
			if(!"".equals(returnId)){
				controller.setAttr("roleId", returnId);
				controller.setAttr("msg", "保存成功！");
				controller.setAttr("resFlag", "0");
			}else{
				controller.setAttr("msg", "保存失败！");
				controller.setAttr("resFlag", "1");
			}
		}else{
			controller.setAttr("msg", "角色名称已存在！");
			controller.setAttr("resFlag", "1");
		}
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170315
	 * 方法：删除角色
	 */
	private void delRole(){
		String roleId = controller.getMyParam("roleId").toString();
		if(dao.delRole(roleId)){
			controller.setAttr("msg", "删除成功！将跳转至列表页...");
			controller.setAttr("resFlag", "0");
		}else{
			controller.setAttr("msg", "删除失败！");
			controller.setAttr("resFlag", "1");
		}
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170315
	 * 方法：查询角色关联菜单情况
	 */
	private void queryRoleMenu(){
		String roleId = controller.getMyParam("roleId").toString();
		controller.setAttr("menuList", dao.queryRoleMenu(roleId));
		controller.setAttr("resFlag", "0");
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170316
	 * 方法：关联角色菜单
	 */
	private void manageRoleMenu(){
		String roleId = controller.getMyParam("roleId").toString();
		String menuIdList = controller.getMyParam("menuIds").toString();
		if(dao.manageRoleMenu(roleId, menuIdList)){
			controller.setAttr("msg", "保存成功！");
			controller.setAttr("resFlag", "0");
		}else{
			controller.setAttr("msg", "保存失败！");
			controller.setAttr("resFlag", "0");
		}
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170315
	 * 方法：查询角色关联人员情况
	 */
	private void queryRoleUser(){
		if(controller.getMyParam("pn") != null && controller.getMyParam("ps") != null){
			pn = Integer.parseInt(controller.getMyParam("pn").toString());
			ps = Integer.parseInt(controller.getMyParam("ps").toString());
			String roleId = controller.getMyParam("roleId").toString();
			Object searchContent = controller.getMyParam("searchContent");
			controller.setAttrs(dao.queryRoleUser(roleId, pn, ps, searchContent));
			controller.setAttr("resFlag", "0");
		}else{
			controller.setAttr("msg", "少传参数！");
			controller.setAttr("resFlag", "1");
		}
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170315
	 * 方法：关联角色人员
	 */
	private void manageRoleUser(){
		String roleId = controller.getMyParam("roleId").toString();
		String userIdList = controller.getMyParam("userIds").toString();
		String allUserId = controller.getMyParam("allUserId").toString();
		if(dao.manageRoleUser(roleId, userIdList, allUserId)){
			controller.setAttr("msg", "保存成功！");
			controller.setAttr("resFlag", "0");
		}else{
			controller.setAttr("msg", "保存失败！");
			controller.setAttr("resFlag", "0");
		}
	}
	
	@Override
	public void doService() throws Exception {
		Db.tx(new IAtom() {
	        @Override
	        public boolean run() throws SQLException {
	            try {
	            	if("queryRoleList".equals(getLastMethodName(7))){
	        			queryRoleList();
	        		}else if("queryRoleInfo".equals(getLastMethodName(7))){
	        			queryRoleInfo();
	        		}else if("addOrUpdateRole".equals(getLastMethodName(7))){
	        			addOrUpdateRole();
	        		}else if("delRole".equals(getLastMethodName(7))){
	        			delRole();
	        		}else if("queryRoleMenu".equals(getLastMethodName(7))){
	        			queryRoleMenu();
	        		}else if("manageRoleMenu".equals(getLastMethodName(7))){
	        			manageRoleMenu();
	        		}else if("queryRoleUser".equals(getLastMethodName(7))){
	        			queryRoleUser();
	        		}else if("manageRoleUser".equals(getLastMethodName(7))){
	        			manageRoleUser();
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
