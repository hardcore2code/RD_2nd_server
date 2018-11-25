package com.mine.rd.services.plan.service;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.mine.pub.controller.BaseController;
import com.mine.pub.service.BaseService;
import com.mine.rd.services.plan.pojo.PlanDao;

public class PlanService extends BaseService {

	private PlanDao dao = new PlanDao();
	private Map<String, Object> map = new HashMap<String, Object>();
	private boolean flag = false;
	private int pn = 0;
	private int ps = 0;
	
	public PlanService(BaseController controller) {
		super(controller);
	}
	
	/**
	 * @author weizanting
	 * @date 20170306
	 * 方法：查询医疗废物转移计划列表
	 */
	private void queryPlanList(){
		pn = Integer.parseInt(controller.getMyParam("pn").toString());
		ps = Integer.parseInt(controller.getMyParam("ps").toString());
		Object searchContent = controller.getMyParam("searchContent");
		Object statusValue = controller.getMyParam("statusValue");
		if(controller.getMyParam("EP_ID") != null && !"".equals(controller.getMyParam("EP_ID"))){
			controller.setAttrs(dao.queryPlanList(controller.getMyParam("EP_ID").toString(), pn, ps, searchContent, statusValue));
			controller.setAttr("resFlag", "0");
		}else{
			controller.setAttr("resFlag", "1");
			controller.setAttr("msg", "少传参数");
		}
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170703
	 * 方法：查询医疗废物转移计划列表by applyList status
	 */
	private void queryPlanListByApply(){
		pn = Integer.parseInt(controller.getMyParam("pn").toString());
		ps = Integer.parseInt(controller.getMyParam("ps").toString());
		Object searchContent = controller.getMyParam("searchContent");
		Object statusValue = controller.getMyParam("statusValue");
		if(controller.getMyParam("EP_ID") != null && !"".equals(controller.getMyParam("EP_ID"))){
			controller.setAttrs(dao.queryPlanListByApply(controller.getMyParam("EP_ID").toString(), pn, ps, searchContent, statusValue));
			controller.setAttr("resFlag", "0");
		}else{
			controller.setAttr("resFlag", "1");
			controller.setAttr("msg", "少传参数");
		}
	}
	
	/**
	 * @author weizanting
	 * @date 20170306
	 * 方法：查询医疗废物转移计划
	 */
	private void queryPlan(){
		if(controller.getMyParam("TP_ID") != null && !"".equals(controller.getMyParam("TP_ID"))){
			map = dao.queryPlan(controller.getMyParam("TP_ID").toString());
			controller.setAttrs(map);
			controller.setAttr("resFlag", "0");
		}else{
			controller.setAttr("resFlag", "1");
			controller.setAttr("msg", "少传参数");
		}
	}
	
	/**
	 * @author weizanting
	 * @date 20170303
	 * 方法：保存医疗废物转移计划
	 */
	private void savePlan(){
		if(controller.getMyParam("LINKMAN") != null && !"".equals(controller.getMyParam("LINKMAN")) && controller.getMyParam("LINKTEL") != null && !"".equals(controller.getMyParam("LINKTEl")) && controller.getMyParam("LINKPHONE") != null && !"".equals(controller.getMyParam("LINKPHONE")) && this.getValues()){
			map.put("LINKMAN", controller.getMyParam("LINKMAN"));
			map.put("LINKTEL", controller.getMyParam("LINKTEL"));
			map.put("LINKPHONE", controller.getMyParam("LINKPHONE"));
			String applyId = dao.savePlan(map);
			if(!"".equals(applyId)){
				controller.setAttr("APPLY_ID", applyId);
				controller.setAttr("resFlag", "0");
				controller.setAttr("msg", "保存成功，也可在待办任务菜单中提交信息！");
			}else{
				controller.setAttr("resFlag", "1");
				controller.setAttr("msg", "保存失败！");
			}
		}else{
			controller.setAttr("resFlag", "1");
			controller.setAttr("msg", "少传参数！");
		}
	}
	
	/**
	 * @author weizanting
	 * @date 20170303
	 * 方法：提交医疗废物转移计划
	 */
	private void submitPlan(){
		if(getValues()){
			map.put("LINKMAN", controller.getMyParam("LINKMAN"));
			map.put("LINKTEL", controller.getMyParam("LINKTEL"));
			map.put("LINKPHONE", controller.getMyParam("LINKPHONE"));
			String applyId = dao.savePlan(map);
			if(!"".equals(applyId)){
				map.put("APPLY_ID", applyId);
				flag = dao.sumitPlan(map);
				if(flag){
					controller.setAttr("resFlag", "0");
					controller.setAttr("msg", "提交成功");
				}else{
					controller.setAttr("resFlag", "1");
					controller.setAttr("msg", "提交失败");
				}
			}else{
				controller.setAttr("resFlag", "1");
				controller.setAttr("msg", "提交失败");
			}
		}else{
			controller.setAttr("resFlag", "1");
			controller.setAttr("msg", "少传参数");
		}
	}
	
	/**
	 * @author weizanting
	 * @date 20170303
	 * 方法：获取提交医疗废物转移计划参数
	 */
	private boolean getValues(){
		if(controller.getMyParam("TP_ID") != null && !"".equals(controller.getMyParam("TP_ID")) && controller.getMyParam("BIZ_NAME") != null && !"".equals(controller.getMyParam("BIZ_NAME")) && controller.getMyParam("EP_ID") != null && !"".equals(controller.getMyParam("EP_ID")) && controller.getMyParam("EP_NAME") != null && !"".equals(controller.getMyParam("EP_NAME")) && controller.getMyParam("BELONG_SEPA") != null && !"".equals(controller.getMyParam("BELONG_SEPA")) && controller.getMyParam("APPLY_ID") != null){
			map.put("TP_ID", controller.getMyParam("TP_ID").toString());
			map.put("BIZ_NAME", controller.getMyParam("BIZ_NAME").toString());
			map.put("EP_ID", controller.getMyParam("EP_ID").toString());
			map.put("EP_NAME", controller.getMyParam("EP_NAME").toString());
			map.put("BELONG_SEPA", controller.getMyParam("BELONG_SEPA").toString());
			map.put("APPLY_ID", controller.getMyParam("APPLY_ID").toString());
			if(controller.getMyParam("PROCESSINSYID") != null && !"".equals(controller.getMyParam("PROCESSINSYID"))){
				map.put("PROCESSINSYID", controller.getMyParam("PROCESSINSYID").toString());
			}else{
				map.put("PROCESSINSYID", "");
			}
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170526
	 * 方法：管理员查询医疗废物转移计划列表
	 */
	private void queryPlanListForAdmin(){
		pn = Integer.parseInt(controller.getMyParam("pn").toString());
		ps = Integer.parseInt(controller.getMyParam("ps").toString());
		if(controller.getMyParam("orgCode") != null && !"".equals(controller.getMyParam("orgCode"))){
			String orgCode = controller.getMyParam("orgCode").toString();
			String ROLEID = controller.getMyParam("ROLEID").toString();
			Object searchContent = controller.getMyParam("searchContent");
			Object statusValue = controller.getMyParam("statusValue");
			Object sepaValue = controller.getMyParam("sepaValue");
			@SuppressWarnings("unchecked")
			List<Object> statusCache = (List<Object>) controller.getMyParam("statusCache");
			controller.setAttrs(dao.queryPlanListForAdmin(pn, ps, orgCode, ROLEID, searchContent, statusValue, sepaValue, statusCache));
			controller.setAttr("resFlag", "0");
		}else{
			controller.setAttr("resFlag", "1");
			controller.setAttr("msg", "少传参数");
		}
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170621
	 * 方法：查询运行中转移计划列表
	 */
	private void queryTransferRunningList(){
		pn = Integer.parseInt(controller.getMyParam("pn").toString());
		ps = Integer.parseInt(controller.getMyParam("ps").toString());
		Object searchContent = controller.getMyParam("searchContent");
		if(controller.getMyParam("EP_ID") != null && !"".equals(controller.getMyParam("EP_ID"))){
			controller.setAttrs(dao.queryTransferRunningList(controller.getMyParam("EP_ID").toString(), pn, ps, searchContent));
			controller.setAttr("resFlag", "0");
		}else{
			controller.setAttr("resFlag", "1");
			controller.setAttr("msg", "少传参数");
		}
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170621
	 * 方法：查询运行中医疗废物转移计划详情
	 */
	private void queryTransferRunningInfo(){
		if(controller.getMyParam("TP_ID") != null && !"".equals(controller.getMyParam("TP_ID"))){
			String TP_ID = controller.getMyParam("TP_ID").toString();
			Object bizId = controller.getMyParam("bizId");
			if(bizId != null && !"".equals(bizId)){
				TP_ID = dao.queryPlanId(bizId.toString());
			}
			map = dao.queryTransferRunningInfo(TP_ID, bizId);
			controller.setAttrs(map);
			controller.setAttr("resFlag", "0");
		}else{
			controller.setAttr("resFlag", "1");
			controller.setAttr("msg", "少传参数");
		}
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170621
	 * 方法：运行中转移计划修改中转单位
	 */
	private void saveTransferRunningModify(){
		Map<String, Object> map = controller.getMyParamMap("par");
		String TM_ID = dao.saveTransferRunningModify(map);
		if(TM_ID != null && !"".equals(TM_ID)){
			String applyId = dao.saveApply(map.get("EP_ID").toString(), map.get("EP_NAME").toString(), map.get("BELONG_SEPA").toString(), TM_ID, map.get("BIZ_NAME").toString());
			if(!"".equals(applyId)){
				controller.setAttr("APPLY_ID", applyId);
				controller.setAttr("TM_ID", TM_ID);
				controller.setAttr("resFlag", "0");
				controller.setAttr("msg", "保存成功，也可在待办任务菜单中提交信息！");
			}else{
				controller.setAttr("resFlag", "1");
				controller.setAttr("msg", "保存失败！");
			}
		}else{
			controller.setAttr("resFlag", "1");
			controller.setAttr("msg", "保存失败！");
		}
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170622
	 * 方法：提交运行中转移计划修改中转单位
	 */
	private void submitTransferRunningModify(){
		Map<String, Object> map = controller.getMyParamMap("par");
		String belongOrgId = dao.belongOrg(map.get("TP_ID").toString());
		String newBlongOrgId = "";
		if(map.get("EN_ID_ZZ") != null && !"".equals(map.get("EN_ID_ZZ")) && !"0".equals(map.get("EN_ID_ZZ"))){
			newBlongOrgId = map.get("EN_ID_ZZ").toString();
		}
		if(!belongOrgId.equals(newBlongOrgId)){
			boolean submitFlag = dao.submitTransferRunningModify(map);
			if(submitFlag){
				boolean submitApplyFlag = dao.submitApply(map.get("EP_ID").toString(), controller.getAttr("TM_ID").toString(), map.get("BIZ_NAME").toString());
				if(submitApplyFlag){
					controller.setAttr("resFlag", "0");
					controller.setAttr("msg", "提交成功！");
				}else{
					controller.setAttr("resFlag", "1");
					controller.setAttr("msg", "提交失败！");
				}
			}else{
				controller.setAttr("resFlag", "1");
				controller.setAttr("msg", "提交失败！");
			}
		}else{
			controller.setAttr("resFlag", "1");
			controller.setAttr("msg", "未对中转单位作修改，不能提交！");
		}
	}

	/**
	 * @author ouyangxu
	 * @date 20170622
	 * 方法：查询运行中转移计划修改中转单位记录
	 */
	private void queryModifyTransferOrgList() throws ParseException{
		Object tpId = controller.getMyParam("TP_ID");
		Object amId = controller.getMyParam("AM_ID");
		controller.setAttr("orgList", dao.queryModifyTransferOrgList(tpId, amId));
		controller.setAttr("resFlag", "0");
	}
	
	@Override
	public void doService() throws Exception {
		Db.tx(new IAtom() {
	        @Override
	        public boolean run() throws SQLException {
	            try {
	            	if("queryPlanList".equals(getLastMethodName(7))){
	        			queryPlanList();
	        		}else if("queryPlan".equals(getLastMethodName(7))){
	        			queryPlan();
	        		}else if("savePlan".equals(getLastMethodName(7))){
	        			savePlan();
	        		}else if("submitPlan".equals(getLastMethodName(7))){
						submitPlan();
	        		}else if("queryPlanListForAdmin".equals(getLastMethodName(7))){
	        			queryPlanListForAdmin();
	        		}else if("queryTransferRunningList".equals(getLastMethodName(7))){
	        			queryTransferRunningList();
	        		}else if("queryTransferRunningInfo".equals(getLastMethodName(7))){
	        			queryTransferRunningInfo();
	        		}else if("saveTransferRunningModify".equals(getLastMethodName(7))){
	        			saveTransferRunningModify();
	        		}else if("submitTransferRunningModify".equals(getLastMethodName(7))){
	        			saveTransferRunningModify();
	        			if("0".equals(controller.getAttr("resFlag"))){
	        				submitTransferRunningModify();
						}
	        		}else if("queryModifyTransferOrgList".equals(getLastMethodName(7))){
	        			queryModifyTransferOrgList();
	        		}else if("queryPlanListByApply".equals(getLastMethodName(7))){
	        			queryPlanListByApply();
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
