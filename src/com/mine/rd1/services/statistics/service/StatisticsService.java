package com.mine.rd1.services.statistics.service;

import java.sql.SQLException;
import java.util.Map;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.mine.pub.controller.BaseController;
import com.mine.pub.kit.Md5kit;
import com.mine.pub.service.BaseService;
import com.mine.rd1.services.statistics.pojo.StatisticsDao;

public class StatisticsService extends BaseService {

	private StatisticsDao dao = new StatisticsDao();
	private int pn = 0;
	private int ps = 0;
	private String userId = "";
	private String queryContext = "";
	private String beginDate = "";
	private String entDate = "";
	private String status = "";
	private String orgCode = "";
	
	public StatisticsService(BaseController controller) {
		super(controller);
	}
	
	/**
	 * @author weizantig
	 * @throws Exception 
	 * @date 20170519
	 * 方法：处置协议列表
	 * 参数：userId--用户名，pwd--密码
	 * 返回：userData
	 */
	private void appLogin() throws Exception{
		controller.doIWBSESSION();
		if(controller.getMyParam("userId") != null && controller.getMyParam("pwd") != null){
			userId = controller.getMyParam("userId").toString();
			String pwd = Md5kit.MD5_64bit(controller.getMyParam("pwd").toString());
			Map<String, Object> map = dao.appLogin(userId, pwd);
			if(map != null){
				controller.setMySession("epId", map.get("orgId"));
				controller.setMySession("epName", map.get("orgName"));
				controller.setMySession("orgCode", map.get("orgCode"));
				controller.setMySession("operatorId", map.get("operatorId"));
				controller.setMySession("empId", map.get("empId"));
				controller.setMySession("userId", map.get("userId"));
				controller.setMySession("userName", map.get("userName"));
				controller.setMySession("realName", map.get("realName"));
				controller.setMySession("roleId", map.get("roleId"));
				controller.setMySession("orgSeq", map.get("orgSeq"));
				controller.setMySession("ifLogin","0");
				controller.setMySession("WJWT",dao.getToken());
				controller.setAttr("resFlag", "0");
				controller.setAttr("msg", "登录成功！");
			}else{
				controller.setAttr("resFlag", "1");
				controller.setAttr("msg", "用户信息有误，请核对后再登录！");
			}
		}else{
			controller.setAttr("resFlag", "1");
			controller.setAttr("msg", "少传参数");
		}
	}
	
	/**
	 * @author weizantig
	 * @date 20170519
	 * 方法：处置协议列表
	 */
	private void queryAgreementList(){
		if(controller.getMyParam("userId") != null && controller.getMyParam("pn") != null && controller.getMyParam("ps") != null && controller.getMyParam("orgCode") != null){
			orgCode = controller.getMyParam("orgCode").toString(); 
			queryContext = controller.getMyParam("queryContext").toString(); 
			beginDate = controller.getMyParam("beginDate").toString(); 
			entDate = controller.getMyParam("entDate").toString(); 
			status = controller.getMyParam("status").toString();
			pn = Integer.parseInt(controller.getMyParam("pn").toString()); 
			ps = Integer.parseInt(controller.getMyParam("ps").toString());
			controller.setAttrs(dao.queryAgreementList(orgCode, queryContext, beginDate, entDate, status, pn, ps));
			controller.setAttr("resFlag", 0);
		}else{
			controller.setAttr("resFlag", "1");
			controller.setAttr("msg", "少传参数");
		}
	}
	
	/**
	 * @author weizantig
	 * @date 20170522
	 * 方法：根据协议id查询处置协议
	 */
	private void getArgeementByAmId(){
		if(controller.getMyParam("amId") != null){
			String amId = controller.getMyParam("amId").toString();
			controller.setAttrs(dao.getArgeementByAmId(amId));
			controller.setAttr("resFlag", "0");
		}else{
			controller.setAttr("resFlag", "1");
			controller.setAttr("msg", "少传参数");
		}
	}

	/**
	 * @author weizantig
	 * @date 20170522
	 * 方法：查询转移计划列表
	 */
	private void queryTransferPlanList(){
		if(controller.getMyParam("userId") != null && controller.getMyParam("pn") != null && controller.getMyParam("ps") != null && controller.getMyParam("orgCode").toString() != null){
			userId = controller.getMyParam("userId").toString(); 
			orgCode = controller.getMyParam("orgCode").toString(); 
			queryContext = controller.getMyParam("queryContext").toString(); 
			beginDate = controller.getMyParam("beginDate").toString(); 
			entDate = controller.getMyParam("entDate").toString(); 
			status = controller.getMyParam("status").toString(); 
			pn = Integer.parseInt(controller.getMyParam("pn").toString()); 
			ps = Integer.parseInt(controller.getMyParam("ps").toString()); 
			controller.setAttrs(dao.queryTransferPlanList(orgCode, queryContext, beginDate, entDate, status, pn, ps));
			controller.setAttr("resFlag", "0");
		}else{
			controller.setAttr("resFlag", "1");
			controller.setAttr("msg", "少传参数");
		}
	}
	
	/**
	 * @author weizantig
	 * @date 20170522
	 * 方法：根据转移计划id查询转移计划,根据流程编号查询审批详细信息
	 */
	private void getTransferPlanByTpId(){
		if(controller.getMyParam("tpId") != null || controller.getMyParam("processinstId") != null){
			String tpId = controller.getMyParam("tpId").toString();
			String processinstId = controller.getMyParam("processinstId").toString();
			controller.setAttrs(dao.getTransferPlanById(tpId, processinstId));
			controller.setAttr("resFlag", "0");
		}else{
			controller.setAttr("resFlag", "1");
			controller.setAttr("msg", "少传参数");
		}
	}
	
	/**
	 * @author weizantig
	 * @date 20170522
	 * 方法：查询转移联单列表
	 */
	private void queryTransferplanBillList(){
		if(controller.getMyParam("userId") != null && controller.getMyParam("pn") != null && controller.getMyParam("ps") != null && controller.getMyParam("orgCode") != null){
			userId = controller.getMyParam("userId").toString();
			orgCode = controller.getMyParam("orgCode").toString();
			queryContext = controller.getMyParam("queryContext").toString();
			beginDate = controller.getMyParam("beginDate").toString();
			entDate = controller.getMyParam("entDate").toString();
			status = controller.getMyParam("status").toString();
			pn = Integer.parseInt(controller.getMyParam("pn").toString());
			ps = Integer.parseInt(controller.getMyParam("ps").toString());
			controller.setAttrs(dao.queryTransferplanBillList(orgCode, queryContext, beginDate, entDate, status, pn, ps));
			controller.setAttr("resFlag", "0");
		}else{
			controller.setAttr("resFlag", "1");
			controller.setAttr("msg", "少传参数");
		}
	}
	
	/**
	 * @author weizantig
	 * @date 20170522
	 * 方法：根据转移联单id查询转移联单
	 */
	private void getTransferplanBillByTbId(){
		if(controller.getMyParam("tbId") != null){
			String tbId = controller.getMyParam("tbId").toString();
			controller.setAttrs(dao.getTransferplanBillByTbId(tbId));
			controller.setAttr("resFlag", "0");
		}else{
			controller.setAttr("resFlag", "1");
			controller.setAttr("msg", "少传参数");
		}
	}
	
	/**
	 * @author weizantig
	 * @date 20170523
	 * 方法：查询审批列表
	 */
	private void queryTransferPlanCheckList(){
		if(controller.getMyParam("userId") != null && controller.getMyParam("pn") != null && controller.getMyParam("ps") != null && controller.getMyParam("orgCode") != null){
			userId = controller.getMyParam("userId").toString();
			orgCode = controller.getMyParam("orgCode").toString();
			String checkLevel = "";
			String checkLevelName = controller.getMyParam("checkLevelName").toString();
			pn = Integer.parseInt(controller.getMyParam("pn").toString());
			ps = Integer.parseInt(controller.getMyParam("ps").toString());
			controller.setAttrs(dao.queryTransferPlanCheckList(orgCode, checkLevel, checkLevelName, pn, ps));
			controller.setAttr("resFlag", "0");
		}else{
			controller.setAttr("resFlag", "1");
			controller.setAttr("msg", "少传参数");
		}
	}
	
	/**
	 * @author weizantig
	 * @date 20170523
	 * 方法：查询审批历史列表
	 */
	private void appCallAuditCx(){
		if(controller.getMyParam("bizId") != null && controller.getMyParam("pn") != null && controller.getMyParam("ps") != null){
			String bizId = controller.getMyParam("bizId").toString();
			pn = Integer.parseInt(controller.getMyParam("pn").toString());
			ps = Integer.parseInt(controller.getMyParam("ps").toString());
			controller.setAttrs(dao.appCallAuditCx(bizId, pn, ps));
			controller.setAttr("resFlag", "0");
		}else{
			controller.setAttr("resFlag", "1");
			controller.setAttr("msg", "少传参数");
		}
	}
	
	/**
	 * @author weizantig
	 * @return 
	 * @date 20170522
	 * 方法：查询字典表
	 */
	private void getDicts(){
		if(controller.getMyParam("dictTypeId") != null){
			String dictTypeId = controller.getMyParam("dictTypeId").toString();
			controller.setAttr("list", dao.getDicts(dictTypeId));
			controller.setAttr("resFlag", "0");
		}else{
			controller.setAttr("resFlag", "1");
			controller.setAttr("msg", "少传参数");
		}
	}
	
	/**
	 * @author zhangyuli
	 * @return 
	 * @date 20170725
	 * 方法：查询审批需要的人员信息
	 */
	private void queryPresonInfo(){
		if(controller.getMyParam("roleId1") != null && controller.getMyParam("roleId2") != null &&
		controller.getMyParam("btoId1") != null && controller.getMyParam("btoId2") != null){
			String roleId1 = controller.getMyParam("roleId1").toString();
			String roleId2 = controller.getMyParam("roleId2").toString();
			String btoId1 = controller.getMyParam("btoId1").toString();
			String btoId2 = controller.getMyParam("btoId2").toString();
			controller.setAttr("list1", dao.queryPresonInfo(roleId1,btoId1));
			controller.setAttr("list2", dao.queryPresonInfo(roleId2,btoId2));
			controller.setAttr("resFlag", "0");
		}else{
			controller.setAttr("resFlag", "1");
			controller.setAttr("msg", "少传参数");
		}
	}
	
	@Override
	public void doService() throws Exception {
		Db.tx(new IAtom() {
	        @Override
	        public boolean run() throws SQLException {
	            try {
	            	if("appLogin".equals(getLastMethodName(7))){
	            		appLogin();
	            	}else if("queryAgreementList".equals(getLastMethodName(7))){
	            		queryAgreementList();
	            	}else if("getArgeementByAmId".equals(getLastMethodName(7))){
	            		getArgeementByAmId();
	            	}else if("queryTransferPlanList".equals(getLastMethodName(7))){
	            		queryTransferPlanList();
	            	}else if("getTransferPlanByTpId".equals(getLastMethodName(7))){
	            		getTransferPlanByTpId();
	            	}else if("queryTransferplanBillList".equals(getLastMethodName(7))){
	            		queryTransferplanBillList();
	            	}else if("getTransferplanBillByTbId".equals(getLastMethodName(7))){
	            		getTransferplanBillByTbId();
	            	}else if("queryTransferPlanCheckList".equals(getLastMethodName(7))){
	            		queryTransferPlanCheckList();
	            	}else if("appCallAuditCx".equals(getLastMethodName(7))){
	            		appCallAuditCx();
	            	}else if("getDicts".equals(getLastMethodName(7))){
	            		getDicts();
	            	}else if("queryPresonInfo".equals(getLastMethodName(7))){
	            		queryPresonInfo();
	            	}
	            } catch (Exception e) {
	                e.printStackTrace();
	                controller.setAttr("msg", "系统异常");
	    			controller.setAttr("resFlag", "1");
	                return false;
	            }
	            return true;
	        }
	    });
		
		
	}

}
