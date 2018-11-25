package com.mine.rd.services.enterprise.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.json.Json;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.mine.pub.controller.BaseController;
import com.mine.pub.kit.DESKit;
import com.mine.pub.service.BaseService;
import com.mine.rd.services.enterprise.pojo.EnterpriseDao;
import com.mine.rd.services.login.pojo.LoginDao;

public class EnterpriseService extends BaseService {

	private EnterpriseDao dao = new EnterpriseDao();
	private LoginDao loginDao = new LoginDao();
	public EnterpriseService(BaseController controller) {
		super(controller);
	}

	/**
	 * @author ouyangxu
	 * @date 20170220
	 * 方法：查询单位信息
	 */
	private void queryEnterpriseInfo(){
		Map<String, Object> returnMap = new HashMap<>();
		String epId = controller.getMyParam("epId").toString();
		Object action = controller.getMyParam("action");
		returnMap = dao.queryEnterpriseInfo(epId, action);
		controller.setAttrs(returnMap);
		controller.setAttr("resFlag", "0");
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170512
	 * 方法：查询单位过渡信息 for ep
	 */
	private void queryEpInfoTransitionForEp(){
		Map<String, Object> returnMap = new HashMap<>();
		String epId = controller.getMyParam("epId").toString();
		Object action = controller.getMyParam("action");
		returnMap = dao.queryEpInfoTransitionForEp(epId, action);
		controller.setAttrs(returnMap);
		controller.setAttr("resFlag", "0");
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170512
	 * 方法：查询单位历史信息 for ep
	 */
	private void queryEpInfoHistoryForEp(){
		Map<String, Object> returnMap = new HashMap<>();
		String epId = controller.getMyParam("epId").toString();
		Object action = controller.getMyParam("action");
		String applyId = controller.getMyParam("applyId").toString();
		returnMap = dao.queryEpInfoHistoryForEp(epId, action, applyId);
		controller.setAttrs(returnMap);
		controller.setAttr("resFlag", "0");
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170417
	 * 方法：查询单位过渡信息或历史信息
	 */
	private void queryEnterpriseInfoTransition(){
		Map<String, Object> returnMap = new HashMap<>();
		String epId = controller.getMyParam("epId").toString();
		String bizId = controller.getMyParam("bizId").toString();
		Object action = controller.getMyParam("action");
		returnMap = dao.queryEnterpriseInfoTransition(epId,bizId,action);
		controller.setAttrs(returnMap);
		controller.setAttr("resFlag", "0");
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170220
	 * 方法：查询单位信息for admin
	 */
	private void queryEnterpriseInfoForAdmin(){
		Map<String, Object> returnMap = new HashMap<>();
		String epId = controller.getMyParam("epId").toString();
		Object action = controller.getMyParam("action");
		returnMap = dao.queryEpInfoTransitionForEp(epId,action);
		controller.setAttrs(returnMap);
		controller.setAttr("resFlag", "0");
	}
	
	/**
	 * @author ouyangxu
	 * @throws Exception 
	 * @date 20170220
	 * 方法：更新单位信息
	 */
	private void updateEnterpriseInfo() throws Exception{
		Map<String, Object> epInfo = controller.getMyParamMap("epInfo");
		List<Map<String, Object>> admins = controller.getMyParamList("admins");
		List<Map<String, Object>> connects = controller.getMyParamList("connects");
		List<Map<String, Object>> cars = controller.getMyParamList("cars");
		String epId = controller.getMyParam("epId").toString();
		String epType = controller.getMyParam("epType").toString();
		String epName = controller.getMyParam("epName").toString();
		String tablename = "";
		//CS-医疗单位 CZ-医疗处置单位
		if("CS".equals(epType)){
			tablename = "WOBO_PERSON_CS";
		}else if("CZ".equals(epType)){
			tablename = "WOBO_PERSON_CZ";
		}
		if(loginDao.checkEnterprise(epInfo.get("epName").toString(), epId)){
			if(loginDao.checkOrgCode(epInfo.get("epCode").toString(), epId)){
				if(dao.checkAdmin(admins, epId, tablename)){
					if(dao.updateEnterpriseInfo(epInfo, epId)){
						int adminSize = dao.addAdminInfo(admins, epId, tablename,"add","").size();
						if(adminSize > 0 || (adminSize <= 0 && admins.size() == 0)){
							int connectSize = dao.addConnectInfo(connects, epId, tablename).size();
							if(connectSize > 0 || (connectSize <= 0 && connects.size() == 0)){
								int carSize = dao.addCarInfo(cars, epId).size();
								if(carSize > 0 || (carSize <= 0 && cars.size() == 0)){
									if(!"".equals(dao.saveApply(epId, epName, epInfo.get("belongSepa").toString(), "", tablename))){
										controller.setMySession("newGuideFlag", false);
										controller.setAttr("resFlag", "0");
										controller.setAttr("msg", "更新成功，也可在待办任务菜单中提交信息！");
									}else{
										controller.setAttr("resFlag", "1");
										controller.setAttr("msg", "更新失败！");
									}
								}else{
									controller.setAttr("resFlag", "1");
									controller.setAttr("msg", "更新失败！");
								}
							}else{
								controller.setAttr("resFlag", "1");
								controller.setAttr("msg", "更新失败！");
							}
						}else{
							controller.setAttr("resFlag", "1");
							controller.setAttr("msg", "更新失败！");
						}
					}else{
						controller.setAttr("resFlag", "1");
						controller.setAttr("msg", "更新失败！");
					}
				}else{
					controller.setAttr("resFlag", "1");
					controller.setAttr("msg", "不能删除已通过审核管理员，可至【管理员信息维护】菜单对该用户进行禁用操作！");
				}
			}else{
				controller.setAttr("resFlag", "1");
				controller.setAttr("msg", "该组织机构代码证已存在！");
			}
		}else{
			controller.setAttr("resFlag", "1");
			controller.setAttr("msg", "该名称单位已存在！");
		}
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170220
	 * 方法：修改单位信息
	 */
	private void modifyEnterpriseInfo(){
		Map<String, Object> epInfo = controller.getMyParamMap("epInfo");
		String epId = controller.getMyParam("epId").toString();
		String epName = controller.getMyParam("epName").toString();
		String belongSepa = controller.getMyParam("belongSepa").toString();
		if(dao.checkIfModifyEpInfo(epInfo, epId)){
			if(loginDao.checkEnterprise(epInfo.get("epName").toString(), epId)){
				if(loginDao.checkOrgCode(epInfo.get("epCode").toString(), epId)){
					String applyId = dao.saveApply(epId, epName, belongSepa, "modify", "");
					if(!"".equals(applyId)){
						if(dao.saveEpInfoTransition(epInfo, epId, applyId)){
	//								AdminDao adminDao = new AdminDao();
	//								if(adminDao.saveHistoryInfo(epId, "", applyId, "PE")){
							controller.setAttr("resFlag", "0");
							controller.setAttr("applyId", applyId);
							controller.setAttr("msg", "保存成功，也可在待办任务菜单中提交信息！");
	//								}else{
	//									controller.setAttr("resFlag", "1");
	//									controller.setAttr("msg", "保存失败！");
	//								}
						}else{
							controller.setAttr("resFlag", "1");
							controller.setAttr("msg", "保存失败！");
						}
					}else{
						controller.setAttr("resFlag", "1");
						controller.setAttr("msg", "保存失败！");
					}
				}else{
					controller.setAttr("resFlag", "1");
					controller.setAttr("msg", "该组织机构代码证已存在！");
				}
			}else{
				controller.setAttr("resFlag", "1");
				controller.setAttr("msg", "该名称单位已存在！");
			}
		}else{
			controller.setAttr("resFlag", "1");
			controller.setAttr("msg", "未对单位信息作修改，不能保存或提交！");
		}
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170223
	 * 方法：提交医疗机构信息完善申请
	 */
	private void submitEpAddInfoApply(){
		String epId = controller.getMyParam("epId").toString();
		if(dao.submitEpAddInfoApply(epId, "")){
			controller.setAttr("resFlag", "0");
			controller.setAttr("msg", "提交成功！");
		}else{
			controller.setAttr("resFlag", "1");
			controller.setAttr("msg", "提交失败！");
		}
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170223
	 * 方法：提交医疗机构信息修改申请
	 */
	private void submitEpModifyInfoApply(){
		String epId = controller.getMyParam("epId").toString();
//		Map<String, Object> epInfo = controller.getMyParamMap("epInfo");
//		if(dao.checkIfModifyEpInfo(epInfo, epId)){
		if(dao.submitEpAddInfoApply(epId, "modify")){
			controller.setAttr("resFlag", "0");
			controller.setAttr("msg", "提交成功！");
		}else{
			controller.setAttr("resFlag", "1");
			controller.setAttr("msg", "提交失败！");
		}
//		}else{
//			controller.setAttr("resFlag", "1");
//			controller.setAttr("msg", "未对单位信息作修改，不能提交！");
//		}
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170303
	 * 方法：查询单位管理员信息
	 */
	private void queryAdminInfo(){
		String epId = controller.getMyParam("epId").toString();
		String action = controller.getMyParam("action").toString();
		if("finished".equals(action)){
			String applyId = controller.getMyParam("applyId").toString();
			controller.setAttr("adminData", dao.queryAdminInfoForFinishTask(epId, applyId));
		}else{
			controller.setAttr("adminData", dao.queryAdminInfo(epId));
		}
		controller.setAttr("adminInfo", dao.queryApplyInfo(epId, "管理员信息维护", "PI"+epId.substring(2)));
		controller.setAttr("resFlag", "0");
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170303
	 * 方法：查询单位管理员修改过渡和历史信息
	 */
	private void queryAdminInfoTransition(){
		String epId = controller.getMyParam("epId").toString();
//			String bizId = controller.getMyParam("bizId").toString();
//			controller.setAttr("adminData", dao.queryAdminInfoTransition(epId, bizId));
		controller.setAttr("adminData", dao.queryAdminInfoForNow(epId));
		controller.setAttr("resFlag", "0");
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170303
	 * 方法：管理单位管理员
	 */
	private void adminManage(){
		String userId = controller.getMyParam("userId").toString();
		String status = controller.getMyParam("status").toString();
		if(dao.adminManage(userId, status)){
			controller.setAttr("resFlag", "0");
			controller.setAttr("msg", "操作成功！");
		}else{
			controller.setAttr("resFlag", "1");
			controller.setAttr("msg", "操作失败！");
		}
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170303
	 * 方法：重置密码
	 */
	private void resetPwd(){
		String userId = controller.getMyParam("userId").toString();
		if(dao.resetPwd(userId)){
			controller.setAttr("resFlag", "0");
			controller.setAttr("msg", "重置成功！");
		}else{
			controller.setAttr("resFlag", "1");
			controller.setAttr("msg", "重置失败！");
		}
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170306
	 * 方法：管理交接员
	 */
	private void connectManage(){
		List<Map<String, Object>> connects = controller.getMyParamList("connects");
		String epId = controller.getMyParam("epId").toString();
		String tablename = dao.epPersonTable(epId);
		List<Map<String, Object>> returnList = dao.addConnectInfo(connects, epId, tablename);
		if(returnList.size() > 0){
			controller.setAttr("resFlag", "0");
			controller.setAttr("connectList", returnList);
			controller.setAttr("msg", "操作成功！");
		}else{
			controller.setAttr("resFlag", "1");
			controller.setAttr("msg", "操作失败！");
		}
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170306
	 * 方法：管理车辆
	 */
	private void carManage(){
		List<Map<String, Object>> cars = controller.getMyParamList("cars");
		String epId = controller.getMyParam("epId").toString();
		List<Map<String, Object>> returnList = dao.addCarInfo(cars, epId);
		if(returnList.size() > 0){
			controller.setAttr("resFlag", "0");
			controller.setAttr("carList", returnList);
			controller.setAttr("msg", "操作成功！");
		}else{
			controller.setAttr("resFlag", "1");
			controller.setAttr("msg", "操作失败！");
		}
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170306
	 * 方法：删除管理员
	 */
	private void adminDel(){
		String userType = controller.getMySession("userType").toString();
		Object userId = controller.getMyParam("userId");
		String tablename = "WOBO_PERSON_CS";
		//epCz-医疗处置单位
		if("epCz".equals(userType)){
			tablename = "WOBO_PERSON_CZ";
		}
		if(userId != null && !"".equals(userId)){
			if(dao.adminDel(userId.toString(),tablename)){
				controller.setAttr("resFlag", "0");
				controller.setAttr("msg", "删除成功！");
			}else{
				controller.setAttr("resFlag", "1");
				controller.setAttr("msg", "删除失败！");
			}
		}else{
			controller.setAttr("resFlag", "3");
			controller.setAttr("msg", "用户不存在，请先保存人员信息！");
		}
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170426
	 * 方法：修改管理员
	 */
	private void modifyAdminInfo(){
		String userType = controller.getMySession("userType").toString();
		List<Map<String, Object>> admins = controller.getMyParamList("admins");
		String epId = controller.getMyParam("epId").toString();
		String epName = controller.getMyParam("epName").toString();
		String belongHbj = controller.getMyParam("belongHbj").toString();
		String action = controller.getMyParam("action").toString();
		String tablename = "";
		//epCs-医疗单位 epCz-医疗处置单位
		if("epCs".equals(userType)){
			tablename = "WOBO_PERSON_CS";
		}else if("epCz".equals(userType)){
			tablename = "WOBO_PERSON_CZ";
		}
		//判断是否修改用户信息，若未修改，则提示“未修改，无需保存”，若修改则进行保存
		if(dao.checkedAdmin(admins, epId, tablename)){
			if(dao.checkAdmin(admins, epId, tablename)){
				String applyId = dao.saveApply(epId, epName, belongHbj, action,tablename);
				if(!"".equals(applyId)){
					List<Map<String, Object>> returnList = dao.addAdminInfo(admins, epId, tablename,"modify",applyId);
					if(returnList.size() > 0){
//						AdminDao adminDao = new AdminDao();
//						if(adminDao.saveHistoryInfo(epId, tablename, applyId, "PI")){
						controller.setAttr("resFlag", "0");
						controller.setAttr("adminList", returnList);
						controller.setAttr("applyId", applyId);
						controller.setAttr("msg", "保存成功，也可在待办任务菜单中提交信息！");
//						}else{
//							controller.setAttr("resFlag", "1");
//							controller.setAttr("msg", "保存失败！");
//						}
					}else{
						controller.setAttr("resFlag", "1");
						controller.setAttr("msg", "保存失败！");
					}
				}else{
					controller.setAttr("resFlag", "1");
					controller.setAttr("msg", "保存失败！");
				}
			}else{
				controller.setAttr("resFlag", "1");
				controller.setAttr("msg", "不能删除已通过审核管理员，可对该用户进行禁用操作！");
			}
		}else{
			controller.setAttr("resFlag", "1");
			controller.setAttr("msg", "您未进行用户修改，无需保存！");
		}
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170426
	 * 方法：提交审批管理员信息
	 */
	private void submitModifyAdminInfo(){
		String epId = controller.getMyParam("epId").toString();
		String action = controller.getMyParam("action").toString();
		if(dao.submitEpAddInfoApply(epId, action)){
			controller.setAttr("resFlag", "0");
			controller.setAttr("msg", "提交成功！");
		}else{
			controller.setAttr("resFlag", "1");
			controller.setAttr("msg", "提交失败！");
		}
	}
	
	/**
	 * @author ouyangxu
	 * @throws Exception 
	 * @date 20170412
	 * 方法：生成二维码信息
	 */
	private void createCodeInfo() throws Exception{
		String roleType = controller.getMyParam("roleType").toString();
		String path = "";
		boolean existFlag = true;
		String contents = "";
		Map<String, Object> userInfo = new HashMap<>();
		String key = PropKit.get("codeKey");
		//0-现场处置人员，1-司机，2-车，3-产生管理员，4-处置管理员，5-产生交接员
		if("3".equals(roleType)){
			String epId = controller.getMyParam("EN_ID_CS").toString();
			String personId = controller.getMyParam("CS_PERSON_ID").toString();
			String personName = controller.getMyParam("CS_PERSON_NAME").toString();
			String userId = controller.getMyParam("USERID").toString();
			String tablename = "WOBO_PERSON_CS";
			userInfo.put("CS_PERSON_ID", userId);
			userInfo.put("roleType", controller.getMyParam("roleType"));
			contents = Json.getJson().toJson(userInfo);
			path = dao.savePersonCode(epId, personId, tablename, DESKit.encrypt(contents,key), personName);
		}else if("4".equals(roleType)){
			String epId = controller.getMyParam("EN_ID_CZ").toString();
			String personId = controller.getMyParam("CZ_PERSON_ID").toString();
			String personName = controller.getMyParam("CZ_PERSON_NAME").toString();
			String userId = controller.getMyParam("USERID").toString();
			String tablename = "WOBO_PERSON_CZ";
			userInfo.put("CZ_PERSON_ID", userId);
			userInfo.put("roleType", controller.getMyParam("roleType"));
			contents = Json.getJson().toJson(userInfo);
			path = dao.savePersonCode(epId, personId, tablename, DESKit.encrypt(contents,key), personName);
		}else if("5".equals(roleType)){
			String epId = controller.getMyParam("EN_ID_CS").toString();
			String personId = controller.getMyParam("CS_PERSON_ID").toString();
			String personName = controller.getMyParam("CS_PERSON_NAME").toString();
			String tablename = "WOBO_PERSON_CS";
			existFlag = dao.checkPersonExist(epId, personId, tablename, roleType, personName);
			if(existFlag){
				userInfo.put("CS_PERSON_ID", personId);
				userInfo.put("roleType", controller.getMyParam("roleType"));
				contents = Json.getJson().toJson(userInfo);
				path = dao.savePersonCode(epId, personId, tablename, DESKit.encrypt(contents,key), personName);
			}
		}else if("0".equals(roleType)){
			String epId = controller.getMyParam("EN_ID_CZ").toString();
			String personId = controller.getMyParam("CZ_PERSON_ID").toString();
			String personName = controller.getMyParam("CZ_PERSON_NAME").toString();
			String tablename = "WOBO_PERSON_CZ";
			existFlag = dao.checkPersonExist(epId, personId, tablename, roleType, personName);
			if(existFlag){
				userInfo.put("CZ_PERSON_ID", personId);
				userInfo.put("roleType", controller.getMyParam("roleType"));
				contents = Json.getJson().toJson(userInfo);
				path = dao.savePersonCode(epId, personId, tablename, DESKit.encrypt(contents,key), personName);
			}
		}else if("1".equals(roleType)){
			String epId = controller.getMyParam("EN_ID_CZ").toString();
			String personId = controller.getMyParam("CZ_DRIVER_ID").toString();
			String tablename = "WOBO_PERSON_CZ";
			String personName = controller.getMyParam("CZ_DRIVER_NAME").toString();
			existFlag = dao.checkPersonExist(epId, personId, tablename, roleType, personName);
			if(existFlag){
				userInfo.put("CZ_DRIVER_ID", personId);
				userInfo.put("roleType", controller.getMyParam("roleType"));
				contents = Json.getJson().toJson(userInfo);
				path = dao.savePersonCode(epId, personId, tablename, DESKit.encrypt(contents,key), personName);
			}
		}else if("2".equals(roleType)){
			String epId = controller.getMyParam("EN_ID_CZ").toString();
			String plateNumber = controller.getMyParam("PLATE_NUMBER").toString();
			String carId = controller.getMyParam("CI_ID").toString();
			existFlag = dao.checkCarExist(epId, carId, plateNumber);
			if(existFlag){
				userInfo.put("CI_ID", carId);
				userInfo.put("roleType", controller.getMyParam("roleType"));
				contents = Json.getJson().toJson(userInfo);
				path = dao.saveCarCode(epId, carId, DESKit.encrypt(contents,key), plateNumber);
			}
		}
		if(existFlag){
			if(!"".equals(path)){
				controller.setAttr("codePath", path);
				controller.setAttr("resFlag", "0");
				controller.setAttr("msg", "生成二维码成功！");
			}else{
				controller.setAttr("resFlag", "1");
				controller.setAttr("msg", "生成二维码失败！");
			}
		}else{
			controller.setAttr("resFlag", "1");
			if("2".equals(roleType)){
				controller.setAttr("msg", "车辆不存在，请先保存车辆信息再生成二维码！");
			}else{
				controller.setAttr("msg", "用户不存在，请先保存人员信息再生成二维码！");
			}
		}
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170412
	 * 方法：查询二维码信息
	 */
	private void queryCodeInfo(){
		String tablename = controller.getMyParam("tablename").toString();
		String epId = controller.getMyParam("epId").toString();
		String personId = controller.getMyParam("personId").toString();
		String personName = controller.getMyParam("personName").toString();
//		String url = controller.getRequest().getServerName() + ":" + controller.getRequest().getServerPort();
		String url = "";
//		if(controller.getRequest().getContextPath() != null && !"".equals(controller.getRequest().getContextPath())){
//			url = url + "/" + controller.getRequest().getContextPath();
//		}
		String path = dao.queryCodeInfo(epId, personId, tablename, url, personName);
		controller.setAttr("resFlag", "0");
		controller.setAttr("path", path);
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170412
	 * 方法：查询车辆二维码信息
	 */
	private void queryCodeInfoForCar(){
		String epId = controller.getMyParam("epId").toString();
		String carId = controller.getMyParam("carId").toString();
		String plateNumber = controller.getMyParam("plateNumber").toString();
//		String url = controller.getRequest().getServerName() + ":" + controller.getRequest().getServerPort();
		String url = "";
//		if(controller.getRequest().getContextPath() != null && !"".equals(controller.getRequest().getContextPath())){
//			url = url + "/" + controller.getRequest().getContextPath();
//		}
		String path = dao.queryCodeInfoForCar(epId, carId, url, plateNumber);
		controller.setAttr("resFlag", "0");
		controller.setAttr("path", path);
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170428
	 * 方法：删除交接员
	 */
	private void delConnect(){
		String personId = controller.getMyParam("personId").toString();
//		String personName = controller.getMyParam("personName").toString();
		String epId = controller.getMyParam("epId").toString();
		String tablename = dao.epPersonTable(epId);
//		String personId = dao.getConnectId(epId, personName, tablename);
		if(personId != null && !"".equals(personId)){
			if(dao.checkPersonTask(personId)){
				if(dao.delConnect(personId, tablename)){
					controller.setAttr("resFlag", "0");
					controller.setAttr("msg", "删除成功！");
				}else{
					controller.setAttr("resFlag", "1");
					controller.setAttr("msg", "删除失败！");
				}
			}else{
				controller.setAttr("resFlag", "1");
				controller.setAttr("msg", "该用户有未完成行程，不能删除！");
			}
		}else{
			controller.setAttr("resFlag", "3");
			controller.setAttr("msg", "用户不存在，请先保存人员信息！");
		}
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170428
	 * 方法：删除车辆
	 */
	private void delCar(){
		String carId = controller.getMyParam("carId").toString();
		if(carId != null && !"".equals(carId)){
			if(dao.delCar(carId)){
				controller.setAttr("resFlag", "0");
				controller.setAttr("msg", "删除成功！");
			}else{
				controller.setAttr("resFlag", "1");
				controller.setAttr("msg", "删除失败！");
			}
		}else{
			controller.setAttr("resFlag", "3");
			controller.setAttr("msg", "车辆不存在，请先保存车辆信息！");
		}
	}
	
	/**
	 *@author weizanting
	 * @date 20170515
	 * 方法：车辆类型
	 */
	private void queryCarType(){
		controller.setAttr("carTypeList", dao.queryCarType());
		controller.setAttr("resFlag", "0");
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170619
	 * 方法：根据区域获取中转单位信息
	 */
	private void queryBelongOrgBySepa(){
		String sepa = controller.getMyParam("sepa").toString();
		String epId = controller.getMyParam("epId").toString();
		controller.setAttr("transferOrgData", dao.queryBelongOrgBySepa(sepa, epId));
		controller.setAttr("resFlag", "0");
	}
	
	@Override
	public void doService() throws Exception {
		Db.tx(new IAtom() {
	        @Override
	        public boolean run() throws SQLException {
	            try {
	            	if("queryEnterpriseInfo".equals(getLastMethodName(7))){
	        			queryEnterpriseInfo();
	        		}else if("updateEnterpriseInfo".equals(getLastMethodName(7))){
	        			updateEnterpriseInfo();
	        		}else if("submitEpAddInfoApply".equals(getLastMethodName(7))){
	        			updateEnterpriseInfo();
	        			if("0".equals(controller.getAttr("resFlag"))){
	        				submitEpAddInfoApply();
	        			}
	        		}else if("queryAdminInfo".equals(getLastMethodName(7))){
	        			queryAdminInfo();
	        		}else if("adminManage".equals(getLastMethodName(7))){
	        			adminManage();
	        		}else if("resetPwd".equals(getLastMethodName(7))){
	        			resetPwd();
	        		}else if("connectManage".equals(getLastMethodName(7))){
	        			connectManage();
	        		}else if("carManage".equals(getLastMethodName(7))){
	        			carManage();
	        		}else if("queryEnterpriseInfoForAdmin".equals(getLastMethodName(7))){
	        			queryEnterpriseInfoForAdmin();
	        		}else if("adminDel".equals(getLastMethodName(7))){
	        			adminDel();
	        		}else if("createCodeInfo".equals(getLastMethodName(7))){
	        			createCodeInfo();
	        		}else if("queryCodeInfo".equals(getLastMethodName(7))){
	        			queryCodeInfo();
	        		}else if("queryCodeInfoForCar".equals(getLastMethodName(7))){
	        			queryCodeInfoForCar();
	        		}else if("queryEnterpriseInfoTransition".equals(getLastMethodName(7))){
	        			queryEnterpriseInfoTransition();
	        		}else if("modifyEnterpriseInfo".equals(getLastMethodName(7))){
	        			modifyEnterpriseInfo();
	        		}else if("submitEpModifyInfoApply".equals(getLastMethodName(7))){
	        			modifyEnterpriseInfo();
	        			if("0".equals(controller.getAttr("resFlag"))){
	        				submitEpModifyInfoApply();
	        			}
	        		}else if("modifyAdminInfo".equals(getLastMethodName(7))){
	        			modifyAdminInfo();
	        		}else if("submitModifyAdminInfo".equals(getLastMethodName(7))){
	        			modifyAdminInfo();
	        			if("0".equals(controller.getAttr("resFlag"))){
	        				submitModifyAdminInfo();
	        			}
	        		}else if("queryAdminInfoTransition".equals(getLastMethodName(7))){
	        			queryAdminInfoTransition();
	        		}else if("delConnect".equals(getLastMethodName(7))){
	        			delConnect();
	        		}else if("delCar".equals(getLastMethodName(7))){
	        			delCar();
	        		}else if("queryEpInfoTransitionForEp".equals(getLastMethodName(7))){
	        			queryEpInfoTransitionForEp();
	        		}else if("queryEpInfoHistoryForEp".equals(getLastMethodName(7))){
	        			queryEpInfoHistoryForEp();
	        		}else if("queryCarType".equals(getLastMethodName(7))){
	        			queryCarType();
	        		}else if("queryBelongOrgBySepa".equals(getLastMethodName(7))){
	        			queryBelongOrgBySepa();
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
