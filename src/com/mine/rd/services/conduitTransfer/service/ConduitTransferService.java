package com.mine.rd.services.conduitTransfer.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.jfinal.json.Json;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.mine.pub.controller.BaseController;
import com.mine.pub.kit.DESKit;
import com.mine.pub.kit.DecryptKit;
import com.mine.pub.service.BaseService;
import com.mine.rd.services.conduitTransfer.pojo.ConduitTransferDao;

public class ConduitTransferService extends BaseService {

	private ConduitTransferDao dao = new ConduitTransferDao();
	public ConduitTransferService(BaseController controller) {
		super(controller);
	}

	/**
	 * @author ouyangxu
	 * @date 20170508
	 * 方法：查询中转机构转移列表
	 */
	private void queryTransferConduit(){
		String epId = controller.getMyParam("epId").toString();
		String action = controller.getMyParam("action").toString();
		int pn = Integer.parseInt(controller.getMyParam("pn").toString());
		int ps = Integer.parseInt(controller.getMyParam("ps").toString());
		controller.setAttr("resFlag", "0");
		controller.setAttrs(dao.queryTransferConduit(epId, action, pn, ps));
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170508
	 * 方法：查询中转机构列表
	 */
	private void queryTransferEp(){
		String epId = controller.getMyParam("epId").toString();
		controller.setAttr("resFlag", "0");
		controller.setAttr("transferEpList", dao.queryTransferEp(epId));
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170508
	 * 方法：查询中转申请信息
	 */
	private void queryTransferConduitInfo(){
		String WC_ID = controller.getMyParam("WC_ID").toString();
		controller.setAttr("resFlag", "0");
		controller.setAttrs(dao.queryTransferConduitInfo(WC_ID));
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170508
	 * 方法：新增、修改中转申请
	 */
	private void saveTransferConduitInfo(){
		Map<String, Object> transferConduitInfo = controller.getMyParamMap("transferConduitInfo");
		String WC_ID = dao.saveTransferConduitInfo(transferConduitInfo);
		if(!"".equals(WC_ID)){
			controller.setAttr("WC_ID", WC_ID);
			controller.setAttr("resFlag", "0");
			controller.setAttr("msg", "保存成功！");
		}else{
			controller.setAttr("resFlag", "1");
			controller.setAttr("msg", "保存失败！");
		}
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170508
	 * 方法：交接中转申请
	 */
	private void dealTransferConduitInfo(){
		String transferEpId = controller.getMyParam("transferEpId").toString();
		String WC_ID = controller.getMyParam("WC_ID").toString();
		String PERSON_ID = controller.getMyParam("PERSON_ID").toString();
		String PERSON_NAME = controller.getMyParam("PERSON_NAME").toString();
		if(dao.checkConnect(transferEpId, PERSON_ID)){
			if(dao.dealTransferConduitInfo(WC_ID, PERSON_ID, PERSON_NAME)){
				if(dao.checkTransferPlan(WC_ID)){
					controller.setAttr("resFlag", "0");
					controller.setAttr("msg", "交接成功！");
				}else{
					controller.setAttr("resFlag", "1");
					controller.setAttr("msg", "交接失败！");
				}
			}else{
				controller.setAttr("resFlag", "1");
				controller.setAttr("msg", "交接失败！");
			}
		}else{
			controller.setAttr("resFlag", "1");
			controller.setAttr("msg", "交接人员不存在，请更换人员进行交接！");
		}
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170510
	 * 方法：删除交接中转申请
	 */
	private void delTransferConduit(){
		String WC_ID = controller.getMyParam("WC_ID").toString();
		if(dao.delTransferConduit(WC_ID)){
			controller.setAttr("resFlag", "0");
			controller.setAttr("msg", "删除成功！");
		}else{
			controller.setAttr("resFlag", "1");
			controller.setAttr("msg", "删除失败！");
		}
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170510
	 * 方法：查询申请转移列表
	 */
	private void queryApplyTransferConduitList(){
		String epId = controller.getMyParam("epId").toString();
		int pn = Integer.parseInt(controller.getMyParam("pn").toString());
		int ps = Integer.parseInt(controller.getMyParam("ps").toString());
		Object searchContent = controller.getMyParam("searchContent");
		Object statusValue = controller.getMyParam("statusValue");
		@SuppressWarnings("unchecked")
		List<Object> statusCache = (List<Object>) controller.getMyParam("statusCache");
		controller.setAttr("resFlag", "0");
		controller.setAttrs(dao.queryApplyTransferConduitList(epId, pn, ps, searchContent, statusValue, statusCache));
	}
	
	/**
	 * @author ouyangxu
	 * @throws Exception 
	 * @throws IOException 
	 * @date 20170629
	 * 方法：查询中转单位交接员信息
	 */
	private void checkTransferPersonInfo() throws IOException, Exception{
		String param = controller.getMyParam("personInfo").toString();
//		String paramUtf8 = java.net.URLDecoder.decode(param,"UTF-8");
		//base64解码成明文
		String paramUtf8 = DecryptKit.decode(param);
		String key = PropKit.get("codeKey");
		//DES根据键值进行解密
		String info = DESKit.decrypt(paramUtf8, key);
		@SuppressWarnings("unchecked")
		Map<String, Object> personInfo = Json.getJson().parse(info, Map.class);
		String roleType = personInfo.get("roleType").toString();
		String epId = controller.getMyParam("epId").toString();
		//0-现场处置人员，1-司机，2-车，3-产生管理员，4-处置管理员，5-产生交接员
		if("3".equals(roleType) || "5".equals(roleType)){
			String personId = personInfo.get("CS_PERSON_ID").toString();
			Map<String, Object> map = dao.checkTransferPersonInfo(roleType, personId, epId);
			if(map != null){
				controller.setAttr("resFlag", "0");
				controller.setAttr("userInfo", map);
			}else{
				controller.setAttr("resFlag", "1");
				controller.setAttr("msg", "交接员不属于该中转机构！");
			}
		}else{
			controller.setAttr("resFlag", "1");
			controller.setAttr("msg", "该角色没有权限!！");
		}
	}
	
	@Override
	public void doService() throws Exception {
		Db.tx(new IAtom() {
	        @Override
	        public boolean run() throws SQLException {
	            try {
	            	if("queryTransferConduit".equals(getLastMethodName(7))){
	            		queryTransferConduit();
	        		}else if("queryTransferEp".equals(getLastMethodName(7))){
	        			queryTransferEp();
	        		}else if("queryTransferConduitInfo".equals(getLastMethodName(7))){
	        			queryTransferConduitInfo();
	        		}else if("saveTransferConduitInfo".equals(getLastMethodName(7))){
	        			saveTransferConduitInfo();
	        		}else if("dealTransferConduitInfo".equals(getLastMethodName(7))){
	        			dealTransferConduitInfo();
	        		}else if("delTransferConduit".equals(getLastMethodName(7))){
	        			delTransferConduit();
	        		}else if("queryApplyTransferConduitList".equals(getLastMethodName(7))){
	        			queryApplyTransferConduitList();
	        		}else if("checkTransferPersonInfo".equals(getLastMethodName(7))){
	        			checkTransferPersonInfo();
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
