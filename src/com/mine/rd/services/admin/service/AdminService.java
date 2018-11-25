package com.mine.rd.services.admin.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.mail.AuthenticationFailedException;
import javax.mail.SendFailedException;

import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.mine.pub.controller.BaseController;
import com.mine.pub.service.BaseService;
import com.mine.rd.services.admin.pojo.AdminDao;
import com.mine.rd.services.agreement.pojo.AgreementDao;
import com.mine.rd.services.plan.pojo.PlanDao;
import com.mine.rd.services.transfer.pojo.TransferDao;
import com.sun.mail.smtp.SMTPAddressFailedException;

public class AdminService extends BaseService{
	private AdminDao dao = new AdminDao();
	
	private int pn = 0;
	private int ps = 0;
	
	public AdminService(BaseController controller) {
		super(controller);
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170222
	 * 方法：管理员待办任务条数
	 */
	private void adminTaskNum(){
		String userType = controller.getMySession("userType").toString();
		int num = 0;
		if("admin".equals(userType)){	//admin-区级、市级管理员
			String orgCode = controller.getMyParam("orgCode").toString();
			String ROLEID = controller.getMyParam("ROLEID").toString();
			num = dao.adminTaskNum(orgCode, ROLEID);
			if("SJSPROLE".equals(ROLEID)){
				controller.setAttr("forgetPwdNum", dao.forgetPwdNum());
			}
		}else if("epCs".equals(userType) || "epCz".equals(userType)){	//epCs-医疗单位  epCz-医疗处置单位
			String epId = controller.getMyParam("epId").toString();
			String action = controller.getMyParam("action").toString();
			num =  Integer.parseInt(dao.epTask(epId, action, 1, 10, "").get("totalRow").toString());
		}else if("epAdminCs".equals(userType)){		//epAdminCs-医疗单位管理员
			String epId = controller.getMyParam("epId").toString();
			String action = controller.getMyParam("action").toString();
			num = Integer.parseInt(dao.epAdminTask(epId, action, 1, 10, "").get("totalRow").toString());
			controller.setAttr("unSubmitPlanNum", dao.queryUnsubmitPlanNum(epId));
		}else if("epAdminCz".equals(userType)){		//epAdminCz-医疗处置单位管理员
			String epId = controller.getMyParam("epId").toString();
			controller.setAttr("agreementNum", dao.queryAgreementNum(epId));
		}
		controller.setAttr("taskNum", num);
		controller.setAttr("resFlag", "0");
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170222
	 * 方法：管理员待办/已办任务
	 */
	private void adminTask(){
		String userType = controller.getMySession("userType").toString();
		pn = Integer.parseInt(controller.getMyParam("pn").toString());
		ps = Integer.parseInt(controller.getMyParam("ps").toString());
		Object searchContent = controller.getMyParam("searchContent");
		Object sepaValue = controller.getMyParam("sepaValue");
		if("admin".equals(userType)){	//admin-区级、市级管理员
			String orgCode = controller.getMyParam("orgCode").toString();
			String action = controller.getMyParam("action").toString();
			String BTOF_ID = controller.getMyParam("BTOF_ID").toString();
			String ROLEID = controller.getMyParam("ROLEID").toString();
			controller.setAttrs(dao.adminTask(orgCode, action, BTOF_ID, pn, ps, ROLEID, searchContent, sepaValue));
			controller.setAttr("resFlag", "0");
		}else if("epCs".equals(userType) || "epCz".equals(userType)){	//epCs-医疗单位  epCz-医疗处置单位
			String epId = controller.getMyParam("epId").toString();
			String action = controller.getMyParam("action").toString();
			controller.setAttrs(dao.epTask(epId, action, pn, ps, searchContent));
			controller.setAttr("resFlag", "0");
		}else{
			String epId = controller.getMyParam("epId").toString();
			String action = controller.getMyParam("action").toString();
			controller.setAttrs(dao.epAdminTask(epId, action, pn, ps, searchContent));
			controller.setAttr("resFlag", "0");
		}
	}
	
	/**
	 * @author ouyangxu
	 * @throws Exception 
	 * @date 20170222
	 * 方法：管理员处理任务
	 */
	private void adminDealTask() throws Exception{
//		String userType = controller.getMySession("userType").toString();
		Map<String, Object> map = controller.getMyParamMap("approveInfo");
		if(dao.adminDealTask(map)){
			String bizStep = map.get("bizStep").toString();
			String checkResult = map.get("checkResult").toString();
			String applyId = map.get("applyId").toString();
			String epId = map.get("epId").toString();
			Map<String, Object> mailMap = dao.updateApplyStatus(bizStep, checkResult, applyId, epId, map);
			if(mailMap != null){
				controller.setAttr("msg", "操作成功！");
				controller.setAttr("resFlag", "0");
				controller.setAttr("mailMap", mailMap);
			}else{
				controller.setAttr("msg", "操作失败！");
				controller.setAttr("resFlag", "1");
			}
		}else{
			controller.setAttr("msg", "操作失败！");
			controller.setAttr("resFlag", "1");
		}
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170223
	 * 方法：管理员查看审批记录
	 */
	private void checkApproveDetail(){
		String applyId = controller.getMyParam("applyId").toString();
		List<Map<String, Object>> list = dao.checkApproveDetail(applyId);
		controller.setAttr("approveList", list);
		controller.setAttr("resFlag", "0");
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170223
	 * 方法：管理员查看申请详情
	 */
	private void checkApplyInfo(){
		String bizId = controller.getMyParam("bizId").toString();
        String bizIdStart = bizId.substring(0,2);
        if("EP".equals(bizIdStart)){	//EP-单位信息完善
        }else if("TP".equals(bizIdStart)){		//TP-转移计划
            PlanDao planDao = new PlanDao();
            Map<String, Object> applyInfo = planDao.queryPlan(bizId);
            controller.setAttr("applyInfo", applyInfo);
			controller.setAttr("resFlag", "0");
        }else if("TM".equals(bizIdStart)){		//TM-运行中转移计划变更中转单位
            PlanDao planDao = new PlanDao();
            String tpId = planDao.queryPlanId(bizId);
            Map<String, Object> applyInfo = planDao.queryPlanModify(tpId, bizId);
            controller.setAttr("applyInfo", applyInfo);
			controller.setAttr("resFlag", "0");
        }
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170503
	 * 方法：管理员查看忘记密码申请列表
	 */
	private void queryForgetPwdApplyList(){
		pn = Integer.parseInt(controller.getMyParam("pn").toString());
		ps = Integer.parseInt(controller.getMyParam("ps").toString());
		Object searchContent = controller.getMyParam("searchContent");
		Object statusValue = controller.getMyParam("statusValue");
		@SuppressWarnings("unchecked")
		List<Object> statusCache = (List<Object>) controller.getMyParam("statusCache");
		controller.setAttrs(dao.queryForgetPwdApplyList(pn, ps, searchContent, statusValue, statusCache));
		controller.setAttr("resFlag", "0");
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170503
	 * 方法：管理员查看单位密码
	 */
	private void queryEpPwd(){
		String epName = controller.getMyParam("epName").toString();
		String orgCode = controller.getMyParam("orgCode").toString();
		controller.setAttrs(dao.queryEpPwd(epName, orgCode));
		controller.setAttr("resFlag", "0");
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170503
	 * 方法：管理员驳回忘记密码申请
	 */
	private void disagreeSendMail(){
		String forgetPwdId = controller.getMyParam("forgetPwdId").toString();
		boolean flag = dao.updateForgetPwdInfo(forgetPwdId, "4", "");
		if(flag){
			controller.setAttr("msg", "操作成功！");
			controller.setAttr("resFlag", "0");
		}else{
			controller.setAttr("msg", "操作失败！");
			controller.setAttr("resFlag", "1");
		}
	}
	
	/**
	 * @author ouyangxu
	 * @throws Exception 
	 * @date 20170503
	 * 方法：管理员同意忘记密码申请
	 */
	private void sendMail() throws Exception{
		String forgetPwdId = controller.getMyParam("forgetPwdId").toString();
		String acceptMail = controller.getMyParam("acceptMail").toString();
		String mail = controller.getMyParam("mail").toString();
		String mailPwd = controller.getMyParam("mailPwd").toString();
		String sendContent = controller.getMyParam("sendContent").toString();
		//发送邮件
		boolean flag = dao.sendMail(acceptMail, PropKit.get("forgotPwdTitle"), sendContent, mail, mailPwd);
		if(flag){
			if(dao.updateForgetPwdInfo(forgetPwdId, "3", mail)){
				controller.setAttr("msg", "操作成功！");
				controller.setAttr("resFlag", "0");
			}else{
				controller.setAttr("msg", "操作失败！");
				controller.setAttr("resFlag", "1");
			}
		}else{
			controller.setAttr("msg", "当前输入邮箱暂不支持，请使用新浪、网易126、163等常用邮箱！");
			controller.setAttr("resFlag", "1");
		}
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170522
	 * 方法：管理员查看未提交转移计划单位
	 */
	private void queryUnsubmitPlanEp(){
		pn = Integer.parseInt(controller.getMyParam("pn").toString());
		ps = Integer.parseInt(controller.getMyParam("ps").toString());
		String ROLEID = controller.getMyParam("ROLEID").toString();
		String orgCode = controller.getMyParam("orgCode").toString();
		Object searchContent = controller.getMyParam("searchContent");
		Object sepaValue = controller.getMyParam("sepaValue");
		controller.setAttrs(dao.queryUnsubmitPlanEp(pn, ps, orgCode, ROLEID,searchContent,sepaValue));
		controller.setAttr("resFlag", "0");
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170526
	 * 方法：管理员查看单位列表
	 */
	private void queryEpList(){
		pn = Integer.parseInt(controller.getMyParam("pn").toString());
		ps = Integer.parseInt(controller.getMyParam("ps").toString());
		String ROLEID = controller.getMyParam("ROLEID").toString();
		String orgCode = controller.getMyParam("orgCode").toString();
		Object searchContent = controller.getMyParam("searchContent");
		Object statusValue = controller.getMyParam("statusValue");
		Object sepaValue = controller.getMyParam("sepaValue");
		@SuppressWarnings("unchecked")
		List<Object> statusCache = (List<Object>) controller.getMyParam("statusCache");
		controller.setAttrs(dao.queryEpList(pn, ps, orgCode, ROLEID, searchContent, statusValue, sepaValue, statusCache));
		controller.setAttr("resFlag", "0");
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170609
	 * 方法：首页查看统计数据
	 */
	private void queryData(){
		AgreementDao agreementDao = new AgreementDao();
		PlanDao planDao = new PlanDao();
		TransferDao transferDao = new TransferDao();
		String userType = controller.getMySession("userType").toString();
		pn = 1;
		ps = 10;
		//查询业务状态  （finished-已办业务  unfinished-待办业务）
		String action = "finished";
		int finishedTaskNumI = -1;
		int agreementNumI = -1;
		int planNumI = -1;
		int hualNumI = -1;
		int billNumI = -1;
		if("admin".equals(userType)){	//admin-区级、市级管理员
			String orgCode = controller.getMyParam("orgCode").toString();
			String BTOF_ID = controller.getMyParam("BTOF_ID").toString();
			String ROLEID = controller.getMyParam("ROLEID").toString();
			finishedTaskNumI = Integer.parseInt(dao.adminTask(orgCode, action, BTOF_ID, pn, ps, ROLEID, "", "").get("totalRow").toString());
			agreementNumI = Integer.parseInt(agreementDao.queryAgreementListForAdmin(pn, ps, orgCode, ROLEID, "", "", "", null).get("totalRow").toString());
			planNumI = Integer.parseInt(planDao.queryPlanListForAdmin(pn, ps, orgCode, ROLEID, "", "", "", null).get("totalRow").toString());
			if("SJSPROLE".equals(ROLEID)){	//SJSPROLE-市级管理员
				hualNumI = Integer.parseInt(transferDao.queryHaulListForAdmin(ROLEID, orgCode, pn, ps, "", "", null).get("totalRow").toString());
			}
			if(!"SJSPROLE".equals(ROLEID)){		//区级管理员
				billNumI = Integer.parseInt(transferDao.queryBillListForEpCs("", pn, ps, "", "", null, orgCode).get("totalRow").toString());
			}
		}else if("epCs".equals(userType) || "epCz".equals(userType)){	//epCs-医疗单位  epCz-医疗处置单位
			String epId = controller.getMyParam("epId").toString();
			finishedTaskNumI = Integer.parseInt(dao.epTask(epId, action, pn, ps, "").get("totalRow").toString());
		}else if("sysAdmin".equals(userType)){		//sysAdmin-系统管理员
		}else{
			String epId = controller.getMyParam("epId").toString();
			finishedTaskNumI = Integer.parseInt(dao.epAdminTask(epId, action, pn, ps, "").get("totalRow").toString());
			agreementNumI = Integer.parseInt(agreementDao.queryAgreementList(epId, pn, ps, "", "").get("totalRow").toString());
			if("epAdminCs".equals(userType)){	//epAdminCs-医疗单位管理员
				planNumI = Integer.parseInt(planDao.queryPlanList(epId, pn, ps, "", "").get("totalRow").toString());
				billNumI = Integer.parseInt(transferDao.queryBillListForEpCs(epId, pn, ps, "", "", null, "").get("totalRow").toString());
			}
			if("epAdminCz".equals(userType)){	//epAdminCz-医疗处置单位管理员
				hualNumI = Integer.parseInt(transferDao.queryHaulList(epId, pn, ps, "", "", null).get("totalRow").toString());
			}
		}
		controller.setAttr("resFlag", "0");
		controller.setAttr("finishedTaskNumI", finishedTaskNumI);
		controller.setAttr("agreementNumI", agreementNumI);
		controller.setAttr("planNumI", planNumI);
		controller.setAttr("hualNumI", hualNumI);
		controller.setAttr("billNumI", billNumI);
	}
	
	@Override
	public void doService() throws Exception {
		Db.tx(new IAtom() {
	        @Override
	        public boolean run() throws SQLException {
        		try {
	            	if("adminTaskNum".equals(getLastMethodName(7))){
	        			adminTaskNum();
	        		}else if("adminTask".equals(getLastMethodName(7))){
	        			adminTask();
	        		}else if("adminDealTask".equals(getLastMethodName(7))){
	        			adminDealTask();
	        		}else if("checkApproveDetail".equals(getLastMethodName(7))){
	        			checkApproveDetail();
	        		}else if("checkApplyInfo".equals(getLastMethodName(7))){
	        			checkApplyInfo();
	        		}else if("queryForgetPwdApplyList".equals(getLastMethodName(7))){
	        			queryForgetPwdApplyList();
	        		}else if("queryEpPwd".equals(getLastMethodName(7))){
	        			queryEpPwd();
	        		}else if("disagreeSendMail".equals(getLastMethodName(7))){
	        			disagreeSendMail();
	        		}else if("sendMail".equals(getLastMethodName(7))){
	        			sendMail();
	        		}else if("queryUnsubmitPlanEp".equals(getLastMethodName(7))){
	        			queryUnsubmitPlanEp();
	        		}else if("queryEpList".equals(getLastMethodName(7))){
	        			queryEpList();
	        		}else if("queryData".equals(getLastMethodName(7))){
	        			queryData();
	        		}
	            } catch (AuthenticationFailedException e) {
	            	controller.setAttr("msg", "发送失败，请检查邮箱地址和密码！");
	    			controller.setAttr("resFlag", "1");
	    			return false;
	            } catch (SMTPAddressFailedException e) {
	            	controller.setAttr("msg", "输入邮箱有误，请检查邮箱地址！");
	    			controller.setAttr("resFlag", "1");
	    			return false;
	            } catch (SendFailedException e) {
	            	controller.setAttr("msg", "发送失败，请检查收件邮箱地址！");
	    			controller.setAttr("resFlag", "1");
	    			return false;
	            } catch (Exception e) {
	                e.printStackTrace();
	            	controller.setAttr("msg", "系统异常，请重新登录！");
	    			controller.setAttr("resFlag", "1");
	                return false;
	            }
	            return true;
	        }
	    });
		//处理需要发送邮件的业务（如审批单位信息完善业务）
		if("adminDealTask".equals(getLastMethodName())){
			if("0".equals(controller.getAttr("resFlag"))){
				Map<String, Object> mailMap = controller.getAttr("mailMap");
				if(mailMap != null && mailMap.get("subject") != null && !"".equals(mailMap.get("subject"))){
					try{
						dao.sendMail(mailMap.get("acceptMail").toString(), mailMap.get("subject").toString(), mailMap.get("content").toString(), mailMap.get("mail").toString(), mailMap.get("mailPwd").toString());
					} catch (AuthenticationFailedException e) {
						controller.setAttr("msg", "操作成功，邮件发送失败，请检查邮箱地址和密码！");
		    			controller.setAttr("resFlag", "0");
		            } catch (SMTPAddressFailedException e) {
						controller.setAttr("msg", "操作成功，邮件发送失败，请检查邮箱地址和密码！");
		    			controller.setAttr("resFlag", "0");
		            } catch (SendFailedException e) {
						controller.setAttr("msg", "操作成功，邮件发送失败，请检查邮箱地址和密码！");
		    			controller.setAttr("resFlag", "0");
		            } catch (Exception e) {
		                e.printStackTrace();
	                	controller.setAttr("msg", "系统异常，请重新登录！");
		    			controller.setAttr("resFlag", "1");
					}
				}
			}
		}
	}


}
