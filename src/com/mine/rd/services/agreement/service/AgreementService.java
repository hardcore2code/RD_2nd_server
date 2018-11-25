package com.mine.rd.services.agreement.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.mine.pub.controller.BaseController;
import com.mine.pub.service.BaseService;
import com.mine.rd.services.agreement.pojo.AgreementDao;

public class AgreementService extends BaseService {

	private AgreementDao dao = new AgreementDao();
	private Map<String, Object> map = null;
	private String EP_ID = "";
	private String AM_ID = "";
	private String EN_ID_CS = "";
	private String EN_NAME_CS = "";
	private String EN_ID_CZ = "";
	private String EN_NAME_CZ = "";
	private String EN_ID_ZZ = "";
	private String EN_NAME_ZZ = "";
	private String UNIT_NUM = "";
	private String BEGINTIME = "";
	private String ENDTIME = "";
	private String LINKMAN = "";
	private String LINKTEL = "";
	private String LINKPHONE = "";
	private boolean flag = false;
	private int pn = 0;
	private int ps = 0;
	
	public AgreementService(BaseController controller) {
		super(controller);
	}
	
	/**
	 * @author weizanting
	 * @date 20170227
	 * 方法：查询医疗废物处置协议列表
	 */
	private void queryAgreementList(){
		pn = Integer.parseInt(controller.getMyParam("pn").toString());
		ps = Integer.parseInt(controller.getMyParam("ps").toString());
		Object searchContent = controller.getMyParam("searchContent");
		Object statusValue = controller.getMyParam("statusValue");
		if(controller.getMyParam("EP_ID") != null && !"".equals(controller.getMyParam("EP_ID"))){
			EP_ID = controller.getMyParam("EP_ID").toString();
			controller.setAttr("resFlag", "0");
			controller.setAttrs(dao.queryAgreementList(EP_ID, pn, ps, searchContent, statusValue));
		}else{
			controller.setAttr("resFlag", "1");
			controller.setAttr("msg", "少传参数");
		}
	}
	
	/**
	 * @author weizanting
	 * @date 20170227
	 * 方法：查询医疗废物处置协议列表for cz
	 */
	private void queryAgreementListForCz(){
		pn = Integer.parseInt(controller.getMyParam("pn").toString());
		ps = Integer.parseInt(controller.getMyParam("ps").toString());
		Object searchContent = controller.getMyParam("searchContent");
		Object statusValue = controller.getMyParam("statusValue");
		if(controller.getMyParam("EP_ID") != null && !"".equals(controller.getMyParam("EP_ID"))){
			EP_ID = controller.getMyParam("EP_ID").toString();
			controller.setAttr("resFlag", "0");
			controller.setAttrs(dao.queryAgreementList(EP_ID, pn, ps, searchContent, statusValue));
		}else{
			controller.setAttr("resFlag", "1");
			controller.setAttr("msg", "少传参数");
		}
	}
	
	/**
	 * @author weizanting
	 * @date 20170303
	 * 方法：查询医疗废物处置协议
	 */
	private void queryAgreement(){
		if(controller.getMyParam("AM_ID") != null && !"".equals(controller.getMyParam("AM_ID"))){
			AM_ID = controller.getMyParam("AM_ID").toString();
			map = dao.queryAgreement(AM_ID);
			controller.setAttr("resFlag", "0");
			controller.setAttrs(map);
		}else{
			controller.setAttr("resFlag", "1");
			controller.setAttr("msg", "少传参数");
		}
	}
	
	/**
	 * @author weizanting
	 * @date 20170303
	 * 方法：保存医疗废物处置协议
	 */
	private void saveAgreement(){
		if(getValue()){
			AM_ID = dao.saveAgreementList(map);
			if(!"".equals(AM_ID)){
				controller.setAttr("resFlag", "0");
				controller.setAttr("msg", "保存成功！");
				controller.setAttr("AM_ID", AM_ID);
			}else{
				controller.setAttr("resFlag", "1");
				controller.setAttr("msg", "保存失败！");
			}
		}else{
			controller.setAttr("resFlag", "1");
			controller.setAttr("msg", "少传参数!");
		}
	}
	
	/**
	 * @author weizanting
	 * @date 20170303
	 * 方法：提交医疗废物处置协议
	 */
	private void submitAgreement(){
		if(controller.getAttr("AM_ID") != null && !"".equals(controller.getAttr("AM_ID"))){
			AM_ID = controller.getAttr("AM_ID");
		}else{
			AM_ID = controller.getMyParam("AM_ID").toString();
		}
		flag = dao.submitAgreement(AM_ID);
		if(flag){
			controller.setAttr("resFlag", "0");
			controller.setAttr("msg", "提交成功");
		}else{
			controller.setAttr("resFlag", "1");
			controller.setAttr("msg", "提交失败");
		}
	}
	
	/**
	 * @author weizanting
	 * @date 20170303
	 * 方法：处置单位同意医疗废物处置协议
	 */
	private void agree(){
		String REASON  = "";
		if(controller.getMyParam("AM_ID") != null && !"".equals(controller.getMyParam("AM_ID"))){
			AM_ID = controller.getMyParam("AM_ID").toString();
			if(controller.getMyParam("REASON") != null && !"".equals(controller.getMyParam("REASON"))){
				REASON  = controller.getMyParam("REASON").toString();
			}
			flag = dao.agree(AM_ID, REASON, "3");
			if(flag){
				flag = dao.savePlan(AM_ID);
				if(flag){
					flag = dao.saveApplyForPlan(AM_ID);
					if(flag){
						controller.setAttr("resFlag", "0");
						controller.setAttr("msg", "操作成功");
					}else{
						controller.setAttr("resFlag", "1");
						controller.setAttr("msg", "操作失败");
					}
				}else{
					controller.setAttr("resFlag", "1");
					controller.setAttr("msg", "操作失败");
				}
			}else{
				controller.setAttr("resFlag", "1");
				controller.setAttr("msg", "操作失败");
			}
		}else{
			controller.setAttr("resFlag", "1");
			controller.setAttr("msg", "少传参数");
		}
	}
	
	/**
	 * @author weizanting
	 * @date 20170303
	 * 方法：处置单位驳回医疗废物处置协议
	 */
	private void reject(){
		String REASON  = "";
		if(controller.getMyParam("AM_ID") != null && !"".equals(controller.getMyParam("AM_ID"))){
			AM_ID = controller.getMyParam("AM_ID").toString();
			if(controller.getMyParam("REASON") != null && !"".equals(controller.getMyParam("REASON"))){
				REASON  = controller.getMyParam("REASON").toString();
			}
			flag = dao.agree(AM_ID, REASON, "4");
			if(flag){
				controller.setAttr("resFlag", "0");
				controller.setAttr("msg", "提交成功");
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
	 * 方法：查询企业列表
	 */
	private void queryEPList(){
//		String userType = controller.getMySession("userType").toString();
		if(controller.getMySession("epId") != null){
			String epId = controller.getMySession("epId").toString();
//			if("epAdminCs".equals(userType)){
			List<Map<String, Object>> datalistForCZ = dao.queryEPForCZ();
//			List<Map<String, Object>> datalistForZZ = dao.queryEPForZZ(epId);
			List<Map<String, Object>> datalist = dao.queryEP(epId);
//			if(datalist.size() > 1){
			controller.setAttr("datalistForZZ", datalist);
//			}else{
//				controller.setAttr("datalistForZZ", datalistForZZ);
//			}
			controller.setAttr("datalistForCZ", datalistForCZ);
			controller.setAttr("resFlag", "0");
//			}else{
//				controller.setAttr("msg", "当前用户不能使用该接口！");
//				controller.setAttr("resFlag", "2");
//			}
		}else{
			controller.setAttr("msg", "少传参数！");
			controller.setAttr("resFlag", "1");
		}
	}
	
	/**
	 * @author weizanting
	 * @date 20170303
	 * 方法：获取保存医疗废物处置协议的参数
	 */
	private boolean getValue(){
		if(controller.getMyParam("EN_ID_CS") != null && !"".equals(controller.getMyParam("EN_ID_CS")) && controller.getMyParam("EN_NAME_CS") != null && !"".equals(controller.getMyParam("EN_NAME_CS")) && controller.getMyParam("EN_ID_CZ") != null && !"".equals(controller.getMyParam("EN_ID_CZ")) && controller.getMyParam("EN_NAME_CZ") != null && !"".equals(controller.getMyParam("EN_NAME_CZ")) && controller.getMyParam("UNIT_NUM") != null && !"".equals(controller.getMyParam("UNIT_NUM")) && controller.getMyParam("BEGINTIME") != null && !"".equals(controller.getMyParam("BEGINTIME")) && controller.getMyParam("ENDTIME") != null && !"".equals(controller.getMyParam("ENDTIME")) && controller.getMyParam("LINKMAN") != null && !"".equals(controller.getMyParam("LINKMAN")) && controller.getMyParam("LINKTEL") != null && !"".equals(controller.getMyParam("LINKTEL")) && controller.getMyParam("LINKPHONE") != null && !"".equals(controller.getMyParam("LINKPHONE"))){
			EN_ID_CS = controller.getMyParam("EN_ID_CS").toString();
			EN_NAME_CS = controller.getMyParam("EN_NAME_CS").toString();
			EN_ID_CZ = controller.getMyParam("EN_ID_CZ").toString();
			EN_NAME_CZ = controller.getMyParam("EN_NAME_CZ").toString();
			UNIT_NUM = controller.getMyParam("UNIT_NUM").toString();
			BEGINTIME = controller.getMyParam("BEGINTIME").toString();
			ENDTIME = controller.getMyParam("ENDTIME").toString();
			LINKMAN = controller.getMyParam("LINKMAN").toString();
			LINKTEL = controller.getMyParam("LINKTEL").toString();
			LINKPHONE = controller.getMyParam("LINKPHONE").toString();
			if(controller.getMyParam("AM_ID") != null && !"".equals(controller.getMyParam("AM_ID"))){
				AM_ID = controller.getMyParam("AM_ID").toString();
			}
			if(controller.getMyParam("EN_ID_ZZ") != null && !"".equals(controller.getMyParam("EN_ID_ZZ"))){
				EN_ID_ZZ = controller.getMyParam("EN_ID_ZZ").toString();
			}
			if(controller.getMyParam("EN_NAME_ZZ") != null && !"".equals(controller.getMyParam("EN_NAME_ZZ"))){
				EN_NAME_ZZ = controller.getMyParam("EN_NAME_ZZ").toString();
			}
			map = new HashMap<String, Object>();
			map.put("AM_ID", AM_ID);
			map.put("EN_ID_CS", EN_ID_CS);
			map.put("EN_NAME_CS", EN_NAME_CS);
			map.put("EN_ID_CZ", EN_ID_CZ);
			map.put("EN_NAME_CZ", EN_NAME_CZ);
			map.put("EN_ID_ZZ", EN_ID_ZZ);
			map.put("EN_NAME_ZZ", EN_NAME_ZZ);
			map.put("UNIT_NUM", UNIT_NUM);
			map.put("BEGINTIME", BEGINTIME);
			map.put("ENDTIME", ENDTIME);
			map.put("LINKMAN", LINKMAN);
			map.put("LINKTEL", LINKTEL);
			map.put("LINKPHONE", LINKPHONE);
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170526
	 * 方法：查询医疗废物处置协议列表
	 */
	private void queryAgreementListForAdmin(){
		pn = Integer.parseInt(controller.getMyParam("pn").toString());
		ps = Integer.parseInt(controller.getMyParam("ps").toString());
		if(controller.getMyParam("ROLEID") != null && !"".equals(controller.getMyParam("ROLEID"))){
			String ROLEID = controller.getMyParam("ROLEID").toString();
			String orgCode = controller.getMyParam("orgCode").toString();
			Object searchContent = controller.getMyParam("searchContent");
			Object statusValue = controller.getMyParam("statusValue");
			Object sepaValue = controller.getMyParam("sepaValue");
			@SuppressWarnings("unchecked")
			List<Object> statusCache = (List<Object>) controller.getMyParam("statusCache");
			controller.setAttr("resFlag", "0");
			controller.setAttrs(dao.queryAgreementListForAdmin(pn, ps, orgCode, ROLEID, searchContent, statusValue, sepaValue, statusCache));
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
					if("queryAgreementList".equals(getLastMethodName(7))){
						queryAgreementList();
					}else if("queryAgreement".equals(getLastMethodName(7))){
						queryAgreement();
					}else if("saveAgreement".equals(getLastMethodName(7))){
						saveAgreement();
					}else if("submitAgreement".equals(getLastMethodName(7))){
						saveAgreement();
						//保存成功之后，进行提交操作
						if("0".equals(controller.getAttr("resFlag"))){
							submitAgreement();
						}
					}else if("agree".equals(getLastMethodName(7))){
						agree();
					}else if("reject".equals(getLastMethodName(7))){
						reject();
					}else if("queryEPList".equals(getLastMethodName(7))){
						queryEPList();
					}else if("queryAgreementListForCz".equals(getLastMethodName(7))){
						queryAgreementListForCz();
					}else if("queryAgreementListForAdmin".equals(getLastMethodName(7))){
						queryAgreementListForAdmin();
					}
	            }catch (Exception e) {
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
