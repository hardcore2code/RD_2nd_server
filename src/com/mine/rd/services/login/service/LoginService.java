package com.mine.rd.services.login.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import com.jfinal.json.Json;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.mine.pub.controller.BaseController;
import com.mine.pub.kit.DESKit;
import com.mine.pub.kit.DecryptKit;
import com.mine.pub.kit.Md5kit;
import com.mine.pub.service.BaseService;
import com.mine.rd.services.login.pojo.LoginDao;

public class LoginService extends BaseService {

	private LoginDao dao = new LoginDao();
	
	public LoginService(BaseController controller) {
		super(controller);
	}
	
	/**
	 * 
	 * @author weizanting
	 * @throws Exception 
	 * @date 20161221
	 * 方法：单位登录
	 * 
	 */
	private void epLogin() throws Exception{
		controller.doIWBSESSION();
		if(controller.getMyParam("username") != null && !"".equals(controller.getMyParam("username")) && controller.getMyParam("pwd") != null && !"".equals(controller.getMyParam("pwd"))){
			String username = controller.getMyParam("username").toString().trim();
			String pwd = controller.getMyParam("pwd").toString().trim();
			//sysadmin-系统管理员
			if("sysadmin".equals(username)){
				controller.setAttr("resFlag", "1");
				controller.setAttr("msg", "用户不存在！");
			}else{
				Map<String , Object> user = dao.login(username);
				if(user != null && user.get("userId") != null && !"".equals(user.get("userId"))){
					if(pwd.equals(user.get("pwd"))){
						String belongS = "";
						String belongQ = "";
						String userPortrait = "";
						if(user.get("belongS") != null && !"".equals(user.get("belongS"))){
							belongS = user.get("belongS").toString();
						}
						if(user.get("belongQ") != null && !"".equals(user.get("belongQ"))){
							belongQ = user.get("belongQ").toString();
						}
						if(user.get("userPortrait") != null && !"".equals(user.get("userPortrait"))){
							userPortrait = user.get("userPortrait").toString();
						}
						controller.setMySession("userId",user.get("userId"));
						controller.setMySession("userType",user.get("userType"));
						controller.setMySession("epId",user.get("epId"));
						controller.setMySession("epName",user.get("epName"));
						controller.setMySession("nickName",user.get("nickName"));
						controller.setMySession("ifLogin","0");
						controller.setMySession("belongQ",belongQ);
						controller.setMySession("belongS",belongS);
						controller.setMySession("status", user.get("status"));
						if(user.get("belongSepa") != null && !"".equals(user.get("belongSepa"))){
							controller.setMySession("belongSepa",user.get("belongSepa"));
						}else{
							controller.setMySession("belongSepa", "");
						}
						controller.setMySession("sepaName",user.get("sepaName"));
						controller.setMySession("orgCode",user.get("orgCode"));
						controller.setMySession("userPortrait",userPortrait);
						//查询是否需要显示新手指引内容
						controller.setMySession("newGuideFlag", dao.checkEpIfSave(user.get("epId").toString()));
						//token值
						controller.setMySession("WJWT", dao.getToken());
						controller.setAttr("resFlag", "0");
						controller.setAttr("msg", "登录成功！");
					}else{
						controller.setAttr("resFlag", "1");
						controller.setAttr("msg", "密码错误！");
					}
				}else{   // 查询不到用户信息
					controller.setAttr("resFlag", "1");
					controller.setAttr("msg", "用户不存在！");
				}
			}
		}else{
			controller.setAttr("resFlag", "1");
			controller.setAttr("msg", "少传参数！");
		}
	}
	
	/**
	 * 
	 * @author ouyangxu
	 * @throws Exception 
	 * @date 20170216
	 * 方法：单位注册
	 * 
	 */
	private void epRegister() throws Exception{
		String checkCodeService = controller.getMySession("checkCode").toString();
		String checkCode = controller.getMyParam("checkCode").toString().trim();
		String enterpriseType = controller.getMyParam("enterpriseType").toString().trim();
		String enterpriseName = controller.getMyParam("enterpriseName").toString().trim();
		String orgCode = controller.getMyParam("orgCode").toString().trim();
		String pwd = controller.getMyParam("pwd").toString().trim();
		String repwd = controller.getMyParam("repwd").toString().trim();
		if(checkCodeService.equals(checkCode)){
			if(pwd.equals(repwd)){
				if(dao.checkEnterprise(enterpriseName,"")){
					if(dao.checkOrgCode(orgCode,"")){
						Map<String, Object> ids = dao.enterpriseRegister(enterpriseType, enterpriseName, orgCode, pwd);
						if(ids.size()>0){
							if(dao.manageEpRole(ids.get("userId").toString(),"ZCQDW")){
								controller.setMySession("userId",ids.get("userId"));
								if("1".equals(enterpriseType)){
									controller.setMySession("userType","epCs");
								}else if("2".equals(enterpriseType)){
									controller.setMySession("userType","epCz");
								}
								controller.setMySession("epId",ids.get("epId"));
								controller.setMySession("epName",enterpriseName);
								controller.setMySession("nickName",enterpriseName);
								controller.setMySession("ifLogin","0");
								controller.setMySession("belongQ","");
								controller.setMySession("belongS","");
								controller.setMySession("orgCode",orgCode);
								controller.setMySession("belongSepa","");
								controller.setMySession("userPortrait","");
								controller.setMySession("status","0");
								controller.setMySession("newGuideFlag", true);
								controller.setMySession("WJWT", dao.getToken());
								controller.setAttr("resFlag", "0");
								controller.setAttr("msg", "注册成功！");
							}else{
								controller.setAttr("resFlag", "1");
								controller.setAttr("msg", "注册失败！");
							}
						}else{
							controller.setAttr("resFlag", "1");
							controller.setAttr("msg", "注册失败！");
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
				controller.setAttr("msg", "两次密码不一致！");
			}
		}else{
			controller.setAttr("resFlag", "1");
			controller.setAttr("msg", "验证码有误！");
		}
	}
	
	/**
	 * @author ouyangxu
	 * @throws Exception 
	 * @date 20170221
	 * 方法：管理员登录
	 */
	private void adminLogin() throws Exception{
		controller.doIWBSESSION();
		String username = controller.getMyParam("adminName").toString();
		String pwd = controller.getMyParam("adminPwd").toString();
		//sysadmin-系统管理员
		if("sysadmin".equals(username)){
			Map<String , Object> user = dao.login(username);
			if(user != null && user.get("userId") != null && !"".equals(user.get("userId"))){
				if(pwd.equals(user.get("pwd"))){
					String belongS = "";
					String belongQ = "";
					String userPortrait = "";
					if(user.get("belongS") != null && !"".equals(user.get("belongS"))){
						belongS = user.get("belongS").toString();
					}
					if(user.get("belongQ") != null && !"".equals(user.get("belongQ"))){
						belongQ = user.get("belongQ").toString();
					}
					if(user.get("userPortrait") != null && !"".equals(user.get("userPortrait"))){
						userPortrait = user.get("userPortrait").toString();
					}
					controller.setMySession("userId",user.get("userId"));
					controller.setMySession("userType",user.get("userType"));
					controller.setMySession("epId",user.get("epId"));
					controller.setMySession("epName",user.get("epName"));
					controller.setMySession("nickName",user.get("nickName"));
					controller.setMySession("ifLogin","0");
					controller.setMySession("belongQ",belongQ);
					controller.setMySession("belongS",belongS);
					controller.setMySession("belongSepa","");
					controller.setMySession("sepaName","");
					controller.setMySession("orgCode",user.get("orgCode"));
					controller.setMySession("userPortrait",userPortrait);
					//token值
					controller.setMySession("WJWT", dao.getToken());
					controller.setAttr("resFlag", "0");
					controller.setAttr("msg", "登录成功！");
				}else{
					controller.setAttr("resFlag", "1");
					controller.setAttr("msg", "密码错误！");
				}
			}else{   // 查询不到用户信息
				controller.setAttr("resFlag", "1");
				controller.setAttr("msg", "用户不存在！");
			}
		}else{
			Map<String, Object> user = dao.adminLogin(username);
			if(user.size() > 0){
				//md5加密
				if(Md5kit.MD5_64bit(pwd).equals(user.get("pwd"))){
					String belongS = "";
					String belongQ = "";
					if(user.get("orgAddr") != null && !"".equals(user.get("orgAddr"))){
						belongS = user.get("orgAddr").toString();
					}
					if(user.get("orgArea") != null && !"".equals(user.get("orgArea"))){
						belongQ = user.get("orgArea").toString();
					}
					controller.setMySession("userId",user.get("operatorId").toString());
					controller.setMySession("nickName",username);
					controller.setMySession("userType", "admin");
					controller.setMySession("ifLogin","0");
					controller.setMySession("belongQ",belongQ);
					controller.setMySession("belongS",belongS);
					controller.setMySession("orgCode",user.get("orgCode").toString());
					controller.setMySession("userPortrait","");
					controller.setMySession("belongSepa", "");
					controller.setMySession("sepaName", belongQ);
					controller.setAttr("btoId", user.get("btoId"));
					controller.setAttr("btoName", user.get("btoName"));
					controller.setAttr("btofId", user.get("btofId"));
					controller.setAttr("btofName", user.get("btofName"));
					controller.setAttr("ROLEID", user.get("ROLEID"));
					//token值
					controller.setMySession("WJWT", dao.getToken());
					controller.setAttr("resFlag", "0");
					controller.setAttr("msg", "登录成功！");
				}else{
					controller.setAttr("resFlag", "1");
					controller.setAttr("msg", "密码错误！");
				}
			}else{
				controller.setAttr("resFlag", "1");
				controller.setAttr("msg", "用户不存在！");
			}
		}
	}
	
	/**
	 * @author weizanting
	 * @throws Exception 
	 * @date 20170227
	 * 方法：医院机构管理员登录
	 */
	private void epAdminLogin() throws Exception{
		controller.doIWBSESSION();
		if(controller.getMyParam("epCode") != null && !"".equals(controller.getMyParam("epCode")) && controller.getMyParam("epAdminName") != null && !"".equals(controller.getMyParam("epAdminName")) && controller.getMyParam("epAdminPwd") != null && !"".equals(controller.getMyParam("epAdminPwd"))){
			String REGISTERCODE = controller.getMyParam("epCode").toString();
			String NAME = controller.getMyParam("epAdminName").toString();
			String PWD = controller.getMyParam("epAdminPwd").toString();
			//0-中转 1-生产  2-处置
			String epType = dao.queryEPType(REGISTERCODE);
			if(!"".equals(epType)){
				Map<String, Object> admin = dao.verifyLoginInfo(epType, NAME, PWD, REGISTERCODE);
				if(admin.size() > 0){
					if(PWD.equals(admin.get("PWD"))){
						controller.setMySession("userId", admin.get("USER_ID"));
						controller.setMySession("nickName", admin.get("NAME"));
						if("0".equals(epType) || "1".equals(epType)){
							controller.setMySession("userType", "epAdminCs");
						}else if("2".equals(epType)){
							controller.setMySession("userType", "epAdminCz");
						}
						controller.setMySession("ifLogin","0");
						controller.setMySession("belongQ", admin.get("EP_ADRESS_Q"));
						controller.setMySession("belongS", admin.get("EP_ADRESS_S"));
						controller.setMySession("orgCode", admin.get("REGISTERCODE"));
						controller.setMySession("userPortrait", admin.get("PORTRAIT"));
						controller.setMySession("epId", admin.get("EP_ID"));
						controller.setMySession("epName", admin.get("EP_NAME"));
						controller.setMySession("belongSepa",admin.get("belongSepa"));
						controller.setMySession("sepaName",admin.get("sepaName"));
						//token值
						controller.setMySession("WJWT", dao.getToken());
						controller.setAttr("resFlag", "0");
						controller.setAttr("msg", "登录成功！");
					}else{
						controller.setAttr("resFlag", "1");
						controller.setAttr("msg", "密码有误！");
					}
				}else{
					controller.setAttr("resFlag", "1");
					controller.setAttr("msg", "用户名不存在！");
				}
			}else{
				controller.setAttr("resFlag", "1");
				controller.setAttr("msg", "统一社会信用代码不存在！");
			}
		}else{
			controller.setAttr("resFlag", "1");
			controller.setAttr("msg", "少传参数");
		}
	}
	
	/**
	 * @author weizanting
	 * @throws Exception 
	 * @date 20170227
	 * 方法：PDA登录
	 */
	private void loginForPDA() throws Exception{
		controller.doIWBSESSION();
		String username = controller.getMyParam("username").toString();
		String pwd = controller.getMyParam("pwd").toString();
		String REGISTERCODE = controller.getMyParam("REGISTERCODE").toString();
		//0-中转 1-生产  2-处置
		String epType = dao.queryEPType(REGISTERCODE);
		if(!"".equals(epType)){
			if("2".equals(epType)){
				Map<String, Object> admin = dao.verifyLoginInfo(epType, username, pwd, REGISTERCODE);
				if(admin.size() > 0){
					if(pwd.equals(admin.get("PWD"))){
						controller.setMySession("userId", admin.get("USER_ID"));
						controller.setMySession("nickName", admin.get("NAME"));
						controller.setMySession("userType", "4");
						controller.setMySession("ifLogin","0");
						controller.setMySession("belongQ", admin.get("EP_ADRESS_Q"));
						controller.setMySession("belongS", admin.get("EP_ADRESS_S"));
						controller.setMySession("orgCode", admin.get("REGISTERCODE"));
						controller.setMySession("userPortrait", admin.get("PORTRAIT"));
						controller.setMySession("epId", admin.get("EP_ID"));
						controller.setMySession("epName", admin.get("EP_NAME"));
						controller.setMySession("belongSepa",admin.get("belongSepa"));
						//token值
						controller.setMySession("WJWT", dao.getToken());
						controller.setAttr("resFlag", "0");
						controller.setAttr("msg", "登录成功！");
					}else{
						controller.setAttr("resFlag", "1");
						controller.setAttr("msg", "密码有误！");
					}
				}else{
					controller.setAttr("resFlag", "1");
					controller.setAttr("msg", "该用户不存在！");
				}
			}else{
				controller.setAttr("resFlag", "1");
				controller.setAttr("msg", "该单位用户无权限！");
			}
		}else{
			controller.setAttr("resFlag", "1");
			controller.setAttr("msg", "统一社会信用代码不存在！");
		}
	}
	/**
	 * @author weizanting
	 * @throws Exception 
	 * @throws IOException 
	 * @date 20170227
	 * 方法：二维码登录校验信息
	 */
	private void loginForPDAScanner() throws IOException, Exception{
		controller.doIWBSESSION();
		String param = controller.getMyParam("personInfo").toString();
		//base64解码成明文
		String paramUtf8 = DecryptKit.decode(param);
		String key = PropKit.get("codeKey");
		//DES解密
		String info = DESKit.decrypt(paramUtf8, key);
		@SuppressWarnings("unchecked")
		Map<String, Object> personInfo = Json.getJson().parse(info, Map.class);
		//0-现场处置人员，1-司机，2-车，3-产生管理员，4-处置管理员，5-产生交接员
		String roleType = personInfo.get("roleType").toString();
		String personId = "";
		if("0".equals(roleType) || "1".equals(roleType) || "4".equals(roleType)){
			if("1".equals(roleType)){
				personId = personInfo.get("CZ_DRIVER_ID").toString();
			}else{
				personId = personInfo.get("CZ_PERSON_ID").toString();
			}
			Map<String, Object> person = dao.checkPersonInfo(roleType,personId);
			if(person != null){
				controller.setAttr("resFlag", "0");
				try {
					controller.setMySession("userId", person.get("personId"));
					controller.setMySession("nickName", person.get("personName"));
					controller.setMySession("userType", person.get("roleType"));
					controller.setMySession("ifLogin","0");
					controller.setMySession("orgCode", "");
					controller.setMySession("userPortrait", "");
					controller.setMySession("epId", person.get("epId"));
					controller.setMySession("epName", person.get("epName"));
					//token值
					controller.setMySession("WJWT", dao.getToken());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else{
				controller.setAttr("resFlag", "1");
				controller.setAttr("msg", "用户不存在！");
			}
		}else{
			controller.setAttr("resFlag", "1");
			controller.setAttr("msg", "该角色没有权限!！");
		}
	}
	
	/**
	 * @author weizanting
	 * @throws Exception 
	 * @throws IOException 
	 * @date 20170227
	 * 方法：二维码登录校验信息 for 医疗单位
	 */
	private void loginForPDAScannerForYl() throws IOException, Exception{
		controller.doIWBSESSION();
		String param = controller.getMyParam("personInfo").toString();
		//base64解码成明文
		String paramUtf8 = DecryptKit.decode(param);
		String key = PropKit.get("codeKey");
		//DES解密
		String info = DESKit.decrypt(paramUtf8, key);
		@SuppressWarnings("unchecked")
		Map<String, Object> personInfo = Json.getJson().parse(info, Map.class);
		//0-现场处置人员，1-司机，2-车，3-产生管理员，4-处置管理员，5-产生交接员
		String roleType = personInfo.get("roleType").toString();
		String epType = dao.queryEP(personInfo.get("CS_PERSON_ID").toString(), roleType);
		if("1".equals(epType)){
			if("3".equals(roleType) || "5".equals(roleType)){
				String personId = personInfo.get("CS_PERSON_ID").toString();
				Map<String, Object> person = dao.checkPersonInfoForYl(roleType,personId);
				if(person != null){
					controller.setAttr("resFlag", "0");
					try {
						controller.setMySession("userId", person.get("personId"));
						controller.setMySession("nickName", person.get("personName"));
						controller.setMySession("userType", person.get("roleType"));
						controller.setMySession("ifLogin","0");
						controller.setMySession("orgCode", "");
						controller.setMySession("userPortrait", "");
						controller.setMySession("epId", person.get("epId"));
						controller.setMySession("epName", person.get("epName"));
						controller.setMySession("WJWT", dao.getToken());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}else{
					controller.setAttr("resFlag", "1");
					controller.setAttr("msg", "用户不存在！");
				}
			}else{
				controller.setAttr("resFlag", "1");
				controller.setAttr("msg", "该角色没有权限!！");
			}
		}else{
			controller.setAttr("resFlag", "1");
			controller.setAttr("msg", "该角色没有权限！");
		}
	}
	
	/**
	 * @author ouyangxu
	 * @throws Exception 
	 * @date 20170510
	 * 方法：APP账号登录
	 */
	private void loginForAPP() throws Exception{
		controller.doIWBSESSION();
		String username = controller.getMyParam("username").toString();
		String pwd = controller.getMyParam("pwd").toString();
		String REGISTERCODE = controller.getMyParam("REGISTERCODE").toString();
		//0-中转 1-生产  2-处置
		String epType = dao.queryEPType(REGISTERCODE);
		if(!"".equals(epType)){
			if("1".equals(epType)){
				if(dao.checkEpPowerbyOrgCode(REGISTERCODE)){
					Map<String, Object> admin = dao.verifyLoginInfo(epType, username, pwd, REGISTERCODE);
					if(admin.size() > 0){
						if(pwd.equals(admin.get("PWD"))){
							controller.setMySession("userId", admin.get("USER_ID"));
							controller.setMySession("nickName", admin.get("NAME"));
							if("0".equals(epType) || "1".equals(epType)){
								controller.setMySession("userType", "epAdminCs");
							}else if("2".equals(epType)){
								controller.setMySession("userType", "epAdminCz");
							}
							controller.setMySession("ifLogin","0");
							controller.setMySession("belongQ", admin.get("EP_ADRESS_Q"));
							controller.setMySession("belongS", admin.get("EP_ADRESS_S"));
							controller.setMySession("orgCode", admin.get("REGISTERCODE"));
							controller.setMySession("userPortrait", admin.get("PORTRAIT"));
							controller.setMySession("epId", admin.get("EP_ID"));
							controller.setMySession("epName", admin.get("EP_NAME"));
							controller.setMySession("belongSepa",admin.get("belongSepa"));
							//token值
							controller.setMySession("WJWT", dao.getToken());
							controller.setAttr("resFlag", "0");
							controller.setAttr("msg", "登录成功！");
						}else{
							controller.setAttr("resFlag", "1");
							controller.setAttr("msg", "密码有误！");
						}
					}else{
						controller.setAttr("resFlag", "1");
						controller.setAttr("msg", "该用户不存在！");
					}
				}else{
					controller.setAttr("resFlag", "1");
					controller.setAttr("msg", "该单位用户无权限！");
				}
			}else{
				controller.setAttr("resFlag", "1");
				controller.setAttr("msg", "该单位用户无权限！");
			}
		}else{
			controller.setAttr("resFlag", "1");
			controller.setAttr("msg", "统一社会信用代码不存在！");
		}
	}
	
	@Override
	public void doService() throws Exception {
		Db.tx(new IAtom() {
	        @Override
	        public boolean run() throws SQLException {
	            try {
	            	if("epLogin".equals(getLastMethodName(7))){
	        			epLogin();
	        		}else if("epRegister".equals(getLastMethodName(7))){
	        			epRegister();
	        		}else if("adminLogin".equals(getLastMethodName(7))){
	        			adminLogin();
	        		}else if("epAdminLogin".equals(getLastMethodName(7))){
	        			epAdminLogin();
	        		}else if("loginForPDA".equals(getLastMethodName(7))){
	        			loginForPDA();
	        		}else if("loginForPDAScanner".equals(getLastMethodName(7))){
	        			loginForPDAScanner();
	        		}else if("loginForPDAScannerForYl".equals(getLastMethodName(7))){
	        			loginForPDAScannerForYl();
	        		}else if("loginForAPP".equals(getLastMethodName(7))){
	        			loginForAPP();
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
