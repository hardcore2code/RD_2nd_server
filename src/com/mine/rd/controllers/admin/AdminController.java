package com.mine.rd.controllers.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ext.render.excel.PoiRender;
import com.jfinal.aop.Before;
import com.mine.pub.controller.BaseController;
import com.mine.pub.service.Service;
import com.mine.rd.interceptors.LoginInterceptor;
import com.mine.rd.services.admin.pojo.AdminDao;
import com.mine.rd.services.admin.service.AdminService;
public class AdminController extends BaseController {

	private Logger logger = Logger.getLogger(AdminController.class);
	
	/**
	 * @author ouyangxu
	 * @date 20170222
	 * 方法：管理员待办任务条数
	 */
	public void adminTaskNum(){
		logger.info("管理员待办任务条数");
		Service service = new AdminService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("管理员待办任务条数异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170222
	 * 方法：管理员待办任务
	 */
	public void adminTask(){
		logger.info("管理员待办任务");
		Service service = new AdminService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("管理员待办任务异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170222
	 * 方法：管理员处理任务
	 */
	@Before(LoginInterceptor.class)
	public void adminDealTask(){
		logger.info("管理员处理任务");
		Service service = new AdminService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("管理员处理任务异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170223
	 * 方法：管理员查看审批记录
	 */
	public void checkApproveDetail(){
		logger.info("管理员查看审批记录");
		Service service = new AdminService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("管理员查看审批记录异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170223
	 * 方法：管理员查看申请详情
	 */
	public void checkApplyInfo(){
		logger.info("管理员查看申请详情记录");
		Service service = new AdminService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("管理员查看申请详情异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170503
	 * 方法：管理员查看忘记密码申请列表
	 */
	public void queryForgetPwdApplyList(){
		logger.info("管理员查看忘记密码申请列表");
		Service service = new AdminService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("管理员查看忘记密码申请列表异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170503
	 * 方法：管理员查看单位密码
	 */
	public void queryEpPwd(){
		logger.info("管理员查看单位密码");
		Service service = new AdminService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("管理员查看单位密码异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170503
	 * 方法：管理员驳回忘记密码申请
	 */
	@Before(LoginInterceptor.class)
	public void disagreeSendMail(){
		logger.info("管理员驳回忘记密码申请");
		Service service = new AdminService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("管理员驳回忘记密码申请异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170503
	 * 方法：管理员同意忘记密码申请
	 */
	@Before(LoginInterceptor.class)
	public void sendMail(){
		logger.info("管理员同意忘记密码申请");
		Service service = new AdminService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("管理员同意忘记密码申请异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170522
	 * 方法：管理员查看未提交转移计划单位
	 */
	public void queryUnsubmitPlanEp(){
		logger.info("管理员查看未提交转移计划单位");
		Service service = new AdminService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("管理员查看未提交转移计划单位异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170526
	 * 方法：管理员查看单位列表
	 */
	public void queryEpList(){
		logger.info("管理员查看单位列表");
		Service service = new AdminService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("管理员查看单位列表异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170609
	 * 方法：首页查看统计数据
	 */
	public void queryData(){
		logger.info("首页查看统计数据");
		Service service = new AdminService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("首页查看统计数据异常===>" + e.getMessage());
			this.setAttr("msg", "系统异常！");
			this.setAttr("resFlag", "1");
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author weizanting
	 * @date 20170609
	 * 方法：导出excle--未提交转移计划单位列表
	 */
	@Before(LoginInterceptor.class)
	public void exportUnSubmitPlan(){
		String area = "";
		String ROLEID = "";
		Object searchContent = "";
		Object sepaValue = "";
		if(getMyParam("ROLEID") != null && getMyParam("orgCode") != null){
			ROLEID = getMyParam("ROLEID").toString();
			area = getMyParam("orgCode").toString();
			searchContent = getMyParam("searchContent");
			sepaValue = getMyParam("sepaValue");
		}
		AdminDao dao = new AdminDao();
		String filename = "uncommittedTransferPlanList.xlsx";
		int num = 1;
		String[] sheetNames = new String[num];
		List<?>[] sheetAllList = new ArrayList<?>[num];
		String[][] headers = new String[num][];
		String[][] columns = new String[num][];
		dao.getUnSubmitPlanExcelInfo(sheetNames, sheetAllList, headers, columns, area, ROLEID, searchContent, sepaValue);			
		PoiRender poiRender = PoiRender.me(sheetAllList).fileName(filename).sheetName(sheetNames).headers(headers).columns(columns).cellWidth(3000);
		Map<String, Object> map = new HashMap<String, Object>();
//		String url = getRequest().getServerName() + ":" + getRequest().getServerPort();
//		if(getRequest().getContextPath() != null && !"".equals(getRequest().getContextPath())){
//			url = url + getRequest().getContextPath();
//		}
		String url = "";
		System.out.println("path3333=====>" + url + poiRender.createFile());
		map.put("path", url + poiRender.createFile());
		map.put("resFlag", "0");
		setAttrs(map);
		renderJsonForCors();
	}
}
