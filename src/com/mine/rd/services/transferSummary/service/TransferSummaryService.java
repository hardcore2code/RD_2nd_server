package com.mine.rd.services.transferSummary.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.mine.pub.controller.BaseController;
import com.mine.pub.service.BaseService;
import com.mine.rd.services.transferSummary.pojo.TransferSummaryDao;

public class TransferSummaryService extends BaseService {

	private String BEGINTIME = ""; 
	private String ENDTIME = ""; 
	private String ROLEID = ""; 
	private String orgCode = ""; 
	private String userType = ""; 
	private String epId = ""; 
	private int pn = 0; 
	private int ps = 0; 
	private TransferSummaryDao dao = new TransferSummaryDao();
	
	public TransferSummaryService(BaseController controller) {
		super(controller);
	}
	
	/**
	 * @author weizanting
	 * @date 20170622
	 * 方法：查询转移汇总（按产生单位）
	 */
	private void queryTransferSummaryForCS(){
		Map<String, Object> map = new HashMap<String, Object>();
		BEGINTIME = controller.getMyParam("BEGINTIME").toString();
		ENDTIME = controller.getMyParam("ENDTIME").toString();
		ROLEID = controller.getMyParam("ROLEID").toString();
		userType = controller.getMyParam("userType").toString();
		orgCode = controller.getMyParam("orgCode").toString();
		epId = controller.getMyParam("epId").toString();
		Object searchContent = controller.getMyParam("searchContent").toString();
		pn = Integer.parseInt(controller.getMyParam("pn").toString());
		ps = Integer.parseInt(controller.getMyParam("ps").toString());
		if(!"".equals(BEGINTIME) && "".equals(ENDTIME)){//只选开始时间
			map = dao.queryTransferSummaryForCS(BEGINTIME, pn, ps, searchContent, ROLEID, userType, orgCode, epId);
		}else if("".equals(BEGINTIME) && !"".equals(ENDTIME)){//只选结束时间
			map = dao.queryTransferSummaryForCS(pn, ps, ENDTIME, searchContent, ROLEID, userType, orgCode, epId);
		}else if(!"".equals(BEGINTIME) && !"".equals(ENDTIME)){//开始时间和结束时间都选
			map = dao.queryTransferSummaryForCS(BEGINTIME, ENDTIME, pn, ps, searchContent, ROLEID, userType, orgCode, epId);
		}else{//开始时间和结束时间都不选
			map = dao.queryTransferSummaryForCS(pn, ps, searchContent, ROLEID, userType, orgCode, epId);
		}
		controller.setAttr("resFlag", "0");
		controller.setAttrs(map);
	}
	
	/**
	 * @author weizanting
	 * @date 20170622
	 * 方法：查询转移汇总（按产生单位）
	 */
	private void queryTransferSummaryForCZ(){
		Map<String, Object> map = new HashMap<String, Object>();
		BEGINTIME = controller.getMyParam("BEGINTIME").toString();
		ENDTIME = controller.getMyParam("ENDTIME").toString();
		ROLEID = controller.getMyParam("ROLEID").toString();
		orgCode = controller.getMyParam("orgCode").toString();
		Object searchContent = controller.getMyParam("searchContent").toString();
		pn = Integer.parseInt(controller.getMyParam("pn").toString());
		ps = Integer.parseInt(controller.getMyParam("ps").toString());
		if(!"".equals(BEGINTIME) && "".equals(ENDTIME)){//只选开始时间
			map = dao.queryTransferSummaryForCZ(BEGINTIME, pn, ps, searchContent, ROLEID, orgCode);
		}else if("".equals(BEGINTIME) && !"".equals(ENDTIME)){//只选结束时间
			map = dao.queryTransferSummaryForCZ(pn, ps, ENDTIME, searchContent, ROLEID, orgCode);
		}else if(!"".equals(BEGINTIME) && !"".equals(ENDTIME)){//开始时间和结束时间都选
			map = dao.queryTransferSummaryForCZ(BEGINTIME, ENDTIME, pn, ps, searchContent, ROLEID, orgCode);
		}else{//开始时间和结束时间都不选
			map = dao.queryTransferSummaryForCZ(pn, ps, searchContent, ROLEID, orgCode);
		}
		controller.setAttr("resFlag", "0");
		controller.setAttrs(map);
	}

	@Override
	public void doService() throws Exception {
		Db.tx(new IAtom() {
	        @Override
	        public boolean run() throws SQLException {
	            try {
					if("queryTransferSummaryForCS".equals(getLastMethodName(7))){
						queryTransferSummaryForCS();
					}else if("queryTransferSummaryForCZ".equals(getLastMethodName(7))){
						queryTransferSummaryForCZ();
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
