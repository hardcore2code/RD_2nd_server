package com.mine.rd.services.transfer.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.crypto.IllegalBlockSizeException;

import com.jfinal.json.Json;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Record;
import com.mine.pub.controller.BaseController;
import com.mine.pub.kit.DESKit;
import com.mine.pub.kit.DecryptKit;
import com.mine.pub.service.BaseService;
import com.mine.rd.services.transfer.pojo.TransferDao;

public class TransferService extends BaseService {

	private TransferDao dao = new TransferDao();
	
	private String TG_ID = "";
	private String weight = "";
	private String epId = "";
	private String userId = "";
	private String ID = "";
	private int pn = 0;
	private int ps = 0;
	
	public TransferService(BaseController controller) {
		super(controller);
	}

	/**
	 * @author weizanting
	 * @date 20170331
	 * 方法：查询医疗废物运程列表
	 */
	private void queryHaulList(){
		if(controller.getMyParam("epId") != null && controller.getMyParam("pn") != null && controller.getMyParam("ps") != null){
			epId = controller.getMyParam("epId").toString();
			pn = Integer.parseInt(controller.getMyParam("pn").toString());
			ps = Integer.parseInt(controller.getMyParam("ps").toString());
			Object searchContent = controller.getMyParam("searchContent");
			Object statusValue = controller.getMyParam("statusValue");
			@SuppressWarnings("unchecked")
			List<Object> statusCache = (List<Object>) controller.getMyParam("statusCache");
			controller.setAttrs(dao.queryHaulList(epId, pn, ps, searchContent, statusValue, statusCache));
			controller.setAttr("resFlag", "0");
		}else{
			controller.setAttr("resFlag", "1");
			controller.setAttr("msg", "少传参数");
		}
	}
	
	/**
	 * @author weizanting
	 * @date 20170629
	 * 方法：查询医疗废物运程列表
	 */
	private void queryHaulListForUser(){
		if(controller.getMyParam("epId") != null && controller.getMyParam("userId") != null && controller.getMyParam("pn") != null && controller.getMyParam("ps") != null){
			epId = controller.getMyParam("epId").toString();
			userId = controller.getMyParam("userId").toString();
			pn = Integer.parseInt(controller.getMyParam("pn").toString());
			ps = Integer.parseInt(controller.getMyParam("ps").toString());
			Object searchContent = controller.getMyParam("searchContent");
			Object statusValue = controller.getMyParam("statusValue");
			@SuppressWarnings("unchecked")
			List<Object> statusCache = (List<Object>) controller.getMyParam("statusCache");
			controller.setAttrs(dao.queryHaulList(epId, pn, ps, searchContent, statusValue, statusCache, userId));
			controller.setAttr("resFlag", "0");
		}else{
			controller.setAttr("resFlag", "1");
			controller.setAttr("msg", "少传参数");
		}
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170503
	 * 方法：管理员查询医疗废物运程列表
	 */
	private void queryHaulListForAdmin(){
		if(controller.getMyParam("orgCode") != null && controller.getMyParam("ROLEID") != null && controller.getMyParam("pn") != null && controller.getMyParam("ps") != null){
			String orgCode = controller.getMyParam("orgCode").toString();
			String ROLEID = controller.getMyParam("ROLEID").toString();
			pn = Integer.parseInt(controller.getMyParam("pn").toString());
			ps = Integer.parseInt(controller.getMyParam("ps").toString());
			Object searchContent = controller.getMyParam("searchContent");
			Object statusValue = controller.getMyParam("statusValue");
			@SuppressWarnings("unchecked")
			List<Object> statusCache = (List<Object>) controller.getMyParam("statusCache");
			controller.setAttrs(dao.queryHaulListForAdmin(ROLEID, orgCode, pn, ps, searchContent, statusValue, statusCache));
			controller.setAttr("resFlag", "0");
		}else{
			controller.setAttr("resFlag", "1");
			controller.setAttr("msg", "少传参数");
		}
	}
	
	/**
	 * @author weizanting
	 * @date 20170331
	 * 方法：新增医疗废物运程
	 */
	private void saveHaul(){
		if(controller.getMyParamMap("haul") != null){
			Map<String, Object> haul = controller.getMyParamMap("haul");
			String tgId = dao.saveHaul(haul);
			if(!"".equals(tgId)){
				controller.setAttr("resFlag", "0");
				controller.setAttr("tgId", tgId);
				controller.setAttr("msg", "保存成功");
			}else{
				controller.setAttr("resFlag", "0");
				controller.setAttr("msg", "保存失败");
			}
		}else{
			controller.setAttr("resFlag", "1");
			controller.setAttr("msg", "少传参数");
		}
	}
	
	/**
	 * @author weizanting
	 * @date 20170331
	 * 方法：查询医疗废物运单列表
	 */
	private void queryBillList(){
		if(controller.getMyParam("TG_ID") != null && controller.getMyParam("pn") != null && controller.getMyParam("ps") != null){
			TG_ID = controller.getMyParam("TG_ID").toString();
			pn = Integer.parseInt(controller.getMyParam("pn").toString());
			ps = Integer.parseInt(controller.getMyParam("ps").toString());
			Object searchContent = controller.getMyParam("searchContent");
			Object statusValue = controller.getMyParam("statusValue");
			@SuppressWarnings("unchecked")
			List<Object> statusCache = (List<Object>) controller.getMyParam("statusCache");
			controller.setAttrs(dao.queryBillList(TG_ID, pn, ps, searchContent, statusValue, statusCache));
			controller.setAttr("resFlag", "0");
		}else{
			controller.setAttr("resFlag", "1");
			controller.setAttr("msg", "少传参数");
		}
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170525
	 * 方法：产生单位查询医疗废物运单列表
	 */
	private void queryBillListForEpCs(){
		if(controller.getMyParam("EP_ID") != null && controller.getMyParam("pn") != null && controller.getMyParam("ps") != null){
			String EP_ID = controller.getMyParam("EP_ID").toString();
			pn = Integer.parseInt(controller.getMyParam("pn").toString());
			ps = Integer.parseInt(controller.getMyParam("ps").toString());
			Object searchContent = controller.getMyParam("searchContent");
			Object statusValue = controller.getMyParam("statusValue");
			@SuppressWarnings("unchecked")
			List<Object> statusCache = (List<Object>) controller.getMyParam("statusCache");
			Object belongSepa = controller.getMyParam("belongSepa");
			controller.setAttrs(dao.queryBillListForEpCs(EP_ID, pn, ps, searchContent, statusValue, statusCache, belongSepa));
			controller.setAttr("resFlag", "0");
		}else{
			controller.setAttr("resFlag", "1");
			controller.setAttr("msg", "少传参数");
		}
	}
	
	/**
	 * @author weizanting
	 * @date 20170331
	 * 方法：查询医疗废物运单
	 */
	private void queryBill(){
		if(controller.getMyParam("TB_ID") != null){
			String TB_ID = controller.getMyParam("TB_ID").toString();
			Map<String, Object> map = dao.queryBill(TB_ID);
			controller.setAttr("bill", map);
			controller.setAttr("resFlag", "0");
		}else{
			controller.setAttr("resFlag", "1");
			controller.setAttr("msg", "少传参数");
		}
	}
	
	/**
	 * @author weizanting
	 * @date 20170405
	 * 方法：保存医疗废物运单
	 */
	private void saveBill(){
		if(controller.getMyParamMap("bill") != null){
			Map<String, Object> bill = controller.getMyParamMap("bill");
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> boxList = (List<Map<String, Object>>) bill.get("boxList");
			int boxNum = boxList.size();
			Record ifExist = dao.ifMerge(bill);
			if(ifExist == null){//不存在一样的医疗机构
				int count = dao.saveBill(bill);
				if(count == 0){
					controller.setAttr("resFlag", "0");
					controller.setAttr("msg", "保存成功");
				}else if(count > 0){
					if(count == boxNum){
						controller.setAttr("resFlag", "0");
						controller.setAttr("msg", "保存成功，该运单中所有箱子在该行程下已接收，箱子已屏蔽");
					}else{
						controller.setAttr("resFlag", "0");
						controller.setAttr("msg", "保存成功，该运单中有该行程下已接收的箱子已屏蔽");
					}
				}else{
					controller.setAttr("resFlag", "1");
					controller.setAttr("msg", "保存失败");
				}
			}else{//存在一样的医疗机构
				//先判断医疗机构是否存在有箱子的运单
				String str = dao.ifExistBill(bill.get("TG_ID").toString(), bill.get("EN_ID_CS").toString());
				if((Integer.parseInt((bill.get("COUNT").toString())) > 0 && "1".equals(str)) || (Integer.parseInt((bill.get("WEIGHT").toString())) > 0 && "2".equals(str))){
					int count = dao.mergeBill(bill, ifExist);
					if(count == 0){
						controller.setAttr("resFlag", "0");
						controller.setAttr("msg", "保存成功，该运单合并到" + ifExist.get("TB_ID"));
					}else if(count > 0){
						if(count == boxNum){
							controller.setAttr("resFlag", "0");
							controller.setAttr("msg", "保存成功，该运单中所有箱子在该行程下已接收，箱子已屏蔽");
						}else{
							controller.setAttr("resFlag", "0");
							controller.setAttr("msg", "保存成功，该运单合并到" + ifExist.get("TB_ID") + "并且该运单中有该行程下已接收的箱子已屏蔽");
						}
					}else if(count == -2){
						controller.setAttr("resFlag", "1");
						controller.setAttr("msg", "保存失败，该交接员与上个运单交接员不符");
					}else{
						controller.setAttr("resFlag", "1");
						controller.setAttr("msg", "保存失败");
					}
				}else{
					controller.setAttr("resFlag", "3");
					controller.setAttr("str", str);
					controller.setAttr("msg", "本次行程中已在“" + bill.get("EN_NAME_CS") + "”创建过运单，如果点击确定则覆盖之前的运单，如果点击取消则放弃本次操作");
				}
			}
			
		}else{
			controller.setAttr("resFlag", "1");
			controller.setAttr("msg", "少传参数");
		}
	}
	
	/**
	 * @author weizanting
	 * @date 20170405
	 * 方法：保存医疗废物运单
	 * str: 0--不存在；1--存在箱子运单；2--存在重量运单
	 */
	private void updateBill(){
		if(controller.getMyParamMap("bill") != null && controller.getMyParam("str") != null){
			Map<String, Object> bill = controller.getMyParamMap("bill");
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> boxList = (List<Map<String, Object>>) bill.get("boxList");
			int boxNum = boxList.size();
			String str = controller.getMyParam("str").toString();
			Record ifExist = dao.ifMerge(bill);
			if(!bill.get("CS_PERSON_ID").equals(ifExist.get("CS_PERSON_ID"))){
				controller.setAttr("resFlag", "1");
				controller.setAttr("msg", "保存失败，该交接员与上个运单交接员不符");
			}else{
				if("1".equals(str)){ 
					//先删除同行程下，同单位已存在箱子运单再新增一条重量的运单
					boolean flag = dao.updateBillForWeight(bill);
					if(flag){
						controller.setAttr("resFlag", "0");
						controller.setAttr("msg", "保存成功");
					}else{
						controller.setAttr("resFlag", "1");
						controller.setAttr("msg", "保存失败");
					}
				}
				if("2".equals(str)){
					//先删除同行程下，同单位已存在重量运单再新增一条箱子的运单
					int num = dao.updateBillForBox(bill);
					if(num == 0){
						controller.setAttr("resFlag", "0");
						controller.setAttr("msg", "保存成功");
					}else if(num > 0){
						if(num == boxNum){
							controller.setAttr("resFlag", "0");
							controller.setAttr("msg", "保存成功，该运单中所有箱子在该行程下已接收，箱子已屏蔽");
						}else{
							controller.setAttr("resFlag", "0");
							controller.setAttr("msg", "保存成功，该运单中有该行程下已接收的箱子已屏蔽");
						}
					}else{
						controller.setAttr("resFlag", "1");
						controller.setAttr("msg", "保存失败");
					}
				}
			}
		}else{
			controller.setAttr("resFlag", "1");
			controller.setAttr("msg", "少传参数");
		}
	}
	
	/**
	 * @author weizanting
	 * @throws Exception 
	 * @date 20170428
	 * 方法：保存医疗废物总量及修改行程、运单、箱子状态
	 */
	private void saveWeight() throws Exception{
		if(controller.getMyParamMap("haul") != null && controller.getMyParam("weight") != null){
			Map<String, Object> haul = controller.getMyParamMap("haul");
			weight = controller.getMyParam("weight").toString();
			boolean flag = dao.saveWeight(haul.get("TG_ID").toString(), weight);
			//保存成功重量
			if(flag){
				//修改状态
				flag = dao.updateStatus(haul.get("TG_ID").toString());
				if(flag){
					controller.setAttr("resFlag", "0");
					controller.setAttr("msg", "保存成功");
					controller.setAttrs(dao.getCarInfo(haul.get("CI_ID"), haul.get("PLATE_NUMBER"), weight, haul.get("TG_ID")));
				}else{
					controller.setAttr("resFlag", "1");
					controller.setAttr("msg", "保存失败");
				}
			}else{
				controller.setAttr("resFlag", "1");
				controller.setAttr("msg", "保存失败");
			}
		}else{
			controller.setAttr("resFlag", "1");
			controller.setAttr("msg", "少传参数");
		}
	}
	
	/**
	 * @author weizanting
	 * @date 20170526
	 * 方法：删除行程
	 */
	private void deleteHaul(){
		if(controller.getMyParam("TG_ID") != null){
			TG_ID = controller.getMyParam("TG_ID").toString();
			boolean flag = dao.deleteHaul(TG_ID);
			if(flag){
				controller.setAttr("resFlag", "0");
				controller.setAttr("msg", "删除成功");
			}else{
				controller.setAttr("resFlag", "1");
				controller.setAttr("msg", "删除失败");
			}
		}else{
			controller.setAttr("resFlag", "1");
			controller.setAttr("msg", "少传参数");
		}
	}
	
	/**
	 * @author weizanting
	 * @date 20170628
	 * 方法：查询医疗废物未完成行程
	 */
	private void queryHaul(){
		if(controller.getMyParam("epId") != null && controller.getMyParam("userId") != null){
			String EN_ID_CZ = controller.getMyParam("epId").toString();
			userId = controller.getMyParam("userId").toString();
			controller.setAttr("resFlag", "0");
			controller.setAttr("haul", dao.queryHaul(EN_ID_CZ, userId));
		}else{
			controller.setAttr("resFlag", "1");
			controller.setAttr("msg", "少传参数");
		}
	}
	/**
	 * @author weizanting
	 * @throws Exception 
	 * @throws IOException 
	 * @date 20170628
	 * 参数：type：0-人员，1-车辆；param：扫码信息
	 * 方法：查询处置单位车辆信息、查询产生单位人员信息
	 */
	@SuppressWarnings("unchecked")
	private void queryInfo() throws IOException, Exception{
		if(controller.getMyParam("param") != null && controller.getMyParam("type") != null){
			String type = controller.getMyParam("type").toString();
			String param = controller.getMyParam("param").toString();
			//base64解码成明文
			String paramUtf8 = DecryptKit.decode(param);
			String key = PropKit.get("codeKey");
			//des解密
			String info = DESKit.decrypt(paramUtf8, key);
			Map<String, Object> personInfo = Json.getJson().parse(info, Map.class);
			String roleType = personInfo.get("roleType").toString();
			controller.setAttr("resFlag", "0");
			if("1".equals(type)){//车辆信息
				if("2".equals(roleType)){
					ID = personInfo.get("CI_ID").toString();
					controller.setAttr("car", dao.queryCarInfo(ID));
				}else{
					controller.setAttr("resFlag", "1");
					controller.setAttr("msg", "扫描的不是车辆二维码");
				}
			}else{
				if("3".equals(roleType)){//产生单位管理员信息
					ID = personInfo.get("CS_PERSON_ID").toString();
					controller.setAttr("user", dao.queryAdminInfo(ID));
				}else if("5".equals(roleType)){//产生单位交接员信息
					ID = personInfo.get("CS_PERSON_ID").toString();
					controller.setAttr("user", dao.queryConnectInfo(ID));
				}else{
					controller.setAttr("resFlag", "1");
					controller.setAttr("msg", "该人员类型不符");
				}
			}
		}else{
			controller.setAttr("resFlag", "1");
			controller.setAttr("msg", "少传参数");
		}
	}
	
	/**
	 * @author weizanting
	 * @date 20171016
	 * 方法：查询处置单位桶净重
	 */
	private void queryBoxSuttle(){
		controller.setAttr("resFlag", "0");
		controller.setAttr("BOX_SUTTLE", dao.queryBoxSuttle());
	}
	
	
	
	@Override
	public void doService() throws Exception {
		Db.tx(new IAtom() {
	        @Override
	        public boolean run() throws SQLException {
	            try {
	            	if("queryHaulList".equals(getLastMethodName(7))){
	        			queryHaulList();
	        		}else if("queryBillList".equals(getLastMethodName(7))){
	        			queryBillList();
	        		}else if("saveHaul".equals(getLastMethodName(7))){
	        			saveHaul();
	        		}else if("queryBill".equals(getLastMethodName(7))){
	        			queryBill();
	        		}else if("saveBill".equals(getLastMethodName(7))){
	        			saveBill();
	        		}else if("saveWeight".equals(getLastMethodName(7))){
	        			saveWeight();
	        		}else if("queryHaulListForAdmin".equals(getLastMethodName(7))){
	        			queryHaulListForAdmin();
	        		}else if("deleteHaul".equals(getLastMethodName(7))){
	        			deleteHaul();
	        		}else if("queryBillListForEpCs".equals(getLastMethodName(7))){
	        			queryBillListForEpCs();
	        		}else if("queryHaul".equals(getLastMethodName(7))){
	        			queryHaul();
	        		}else if("queryInfo".equals(getLastMethodName(7))){
	        			queryInfo();
	        		}else if("updateBill".equals(getLastMethodName(7))){
	        			updateBill();
	        		}else if("queryHaulListForUser".equals(getLastMethodName(7))){
	        			queryHaulListForUser();
	        		}else if("queryBoxSuttle".equals(getLastMethodName(7))){
	        			queryBoxSuttle();
	        		}
	            } catch (IllegalBlockSizeException e) {
	                controller.setAttr("msg", "二维码信息不正确，请重新生成二维码！");
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
	}
	
}
