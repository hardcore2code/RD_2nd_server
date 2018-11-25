package com.mine.rd.controllers.enterprise;

import org.apache.log4j.Logger;

import com.jfinal.aop.Before;
import com.mine.pub.controller.BaseController;
import com.mine.pub.service.Service;
import com.mine.rd.interceptors.LoginInterceptor;
import com.mine.rd.services.enterprise.service.EnterpriseService;
public class EnterpriseController extends BaseController {
	private Logger logger = Logger.getLogger(EnterpriseController.class);
	/**
	 * @author ouyangxu
	 * @date 20170220
	 * 方法：查询单位信息
	 */
	public void queryEnterpriseInfo(){
		logger.info("查询单位信息");
		Service service = new EnterpriseService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("查询单位信息异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170220
	 * 方法：查询单位信息for admin
	 */
	public void queryEnterpriseInfoForAdmin(){
		logger.info("查询单位信息for admin");
		Service service = new EnterpriseService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("查询单位信息for admin异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170220
	 * 方法：更新单位信息
	 */
	@Before(LoginInterceptor.class)
	public void updateEnterpriseInfo(){
		logger.info("更新单位信息");
		Service service = new EnterpriseService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("更新单位信息异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170223
	 * 方法：提交医疗机构信息完善申请
	 */
	@Before(LoginInterceptor.class)
	public void submitEpAddInfoApply(){
		logger.info("提交医疗机构信息完善申请");
		Service service = new EnterpriseService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("提交医疗机构信息完善申请异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170303
	 * 方法：查询单位管理员信息
	 */
	public void queryAdminInfo(){
		logger.info("查询单位管理员信息");
		Service service = new EnterpriseService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("查询单位管理员信息异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170303
	 * 方法：管理单位管理员
	 */
	@Before(LoginInterceptor.class)
	public void adminManage(){
		logger.info("管理单位管理员");
		Service service = new EnterpriseService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("管理单位管理员异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170303
	 * 方法：重置密码
	 */
	@Before(LoginInterceptor.class)
	public void resetPwd(){
		logger.info("重置密码");
		Service service = new EnterpriseService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("重置密码异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170306
	 * 方法：管理交接员
	 */
	@Before(LoginInterceptor.class)
	public void connectManage(){
		logger.info("管理交接员");
		Service service = new EnterpriseService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("管理交接员异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170306
	 * 方法：管理车辆
	 */
	@Before(LoginInterceptor.class)
	public void carManage(){
		logger.info("管理车辆");
		Service service = new EnterpriseService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("管理车辆异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170306
	 * 方法：删除管理员
	 */
	@Before(LoginInterceptor.class)
	public void adminDel(){
		logger.info("删除管理员");
		Service service = new EnterpriseService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("删除管理员异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170412
	 * 方法：生成二维码信息
	 */
	@Before(LoginInterceptor.class)
	public void createCodeInfo(){
		logger.info("生成二维码信息");
		Service service = new EnterpriseService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("生成二维码信息异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170412
	 * 方法：查询二维码信息
	 */
	public void queryCodeInfo(){
		logger.info("查询二维码信息");
		Service service = new EnterpriseService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("查询二维码信息异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170412
	 * 方法：查询车辆二维码信息
	 */
	public void queryCodeInfoForCar(){
		logger.info("查询车辆二维码信息");
		Service service = new EnterpriseService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("查询车辆二维码信息异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170412
	 * 方法：查询单位过渡信息或历史信息
	 */
	public void queryEnterpriseInfoTransition(){
		logger.info("查询单位过渡信息或历史信息");
		Service service = new EnterpriseService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("查询单位过渡信息或历史信息异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170412
	 * 方法：修改单位信息
	 */
	@Before(LoginInterceptor.class)
	public void modifyEnterpriseInfo(){
		logger.info("修改单位信息");
		Service service = new EnterpriseService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("修改单位信息异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170412
	 * 方法：提交医疗机构信息修改申请
	 */
	@Before(LoginInterceptor.class)
	public void submitEpModifyInfoApply(){
		logger.info("提交医疗机构信息修改申请");
		Service service = new EnterpriseService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("提交医疗机构信息修改申请异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170426
	 * 方法：修改管理员
	 */
	@Before(LoginInterceptor.class)
	public void modifyAdminInfo(){
		logger.info("修改管理员");
		Service service = new EnterpriseService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("修改管理员异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170426
	 * 方法：提交审批管理员信息
	 */
	@Before(LoginInterceptor.class)
	public void submitModifyAdminInfo(){
		logger.info("提交审批管理员信息");
		Service service = new EnterpriseService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("提交审批管理员信息异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170426
	 * 方法：查询单位管理员修改过渡和历史信息
	 */
	public void queryAdminInfoTransition(){
		logger.info("查询单位管理员修改过渡和历史信息");
		Service service = new EnterpriseService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("查询单位管理员修改过渡和历史信息异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170428
	 * 方法：删除交接员
	 */
	@Before(LoginInterceptor.class)
	public void delConnect(){
		logger.info("删除交接员");
		Service service = new EnterpriseService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("删除交接员异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170428
	 * 方法：删除车辆
	 */
	@Before(LoginInterceptor.class)
	public void delCar(){
		logger.info("删除车辆");
		Service service = new EnterpriseService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("删除车辆异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170512
	 * 方法：查询单位过渡信息 for ep
	 */
	public void queryEpInfoTransitionForEp(){
		logger.info("查询单位过渡信息 for ep");
		Service service = new EnterpriseService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("查询单位过渡信息 for ep异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170512
	 * 方法：查询单位历史信息 for ep
	 */
	public void queryEpInfoHistoryForEp(){
		logger.info("查询单位历史信息 for ep");
		Service service = new EnterpriseService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("查询单位历史信息 for ep异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author weizanting
	 * @date 20170515
	 * 方法：查询车辆类型
	 */
	public void queryCarType(){
		logger.info("查询车辆类型");
		Service service = new EnterpriseService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("查询车辆类型异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170619
	 * 方法：根据区域获取中转单位信息
	 */
	public void queryBelongOrgBySepa(){
		logger.info("根据区域获取中转单位信息");
		Service service = new EnterpriseService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("根据区域获取中转单位信息异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
}
