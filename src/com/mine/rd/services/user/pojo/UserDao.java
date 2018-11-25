package com.mine.rd.services.user.pojo;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.mine.pub.kit.Md5kit;
import com.mine.pub.kit.StrKit;
import com.mine.pub.pojo.BaseDao;

public class UserDao extends BaseDao {

	/**
	 * 
	 * @author weizanting
	 * @date 20161221
	 * 方法：修改密码
	 * 
	 */
	public boolean modifyPwd(String userId, String newPwd, String nickName, String userType){
		if("admin".equals(userType)){		//区市级管理员
			try {
				//密码md5加密
				newPwd = Md5kit.MD5_64bit(newPwd);
			} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return Db.update("update AC_OPERATOR set PASSWORD = ? where USERID = ? and OPERATORID = ?", newPwd, nickName, userId) > 0;
		}else{		//医废用户
			return Db.update("update WOBO_USER set PWD=? where USER_ID=?", newPwd, userId) > 0;
		}
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170221
	 * 方法：校验原密码
	 */
	public boolean checkPwd(String userId, String oldPwd, String nickName, String userType){
		Record record = new Record();
		if("admin".equals(userType)){		//区市级管理员
			try {
				//MD5加密
				oldPwd = Md5kit.MD5_64bit(oldPwd);
			} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			record = Db.findFirst("select * from AC_OPERATOR where USERID = ? and OPERATORID = ? and PASSWORD = ?", nickName, userId, oldPwd);
		}else{		//医废用户
			record = Db.findFirst("select * from WOBO_USER where USER_ID = ? and PWD = ?", userId, oldPwd);
		}
		if(record != null){
			return true;
		}
		return false;
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170503
	 * 方法：忘记密码，提交申请
	 */
	public boolean forgetPwd(String epName, String orgCode, String mail, String name){
		Record record = new Record();
		record.set("WF_ID", super.getSeqId("WOBO_FORGETPWD"));
		record.set("EP_NAME", epName);
		record.set("ORGCARD", orgCode);
		record.set("STATUS", "1");
		record.set("NAME", name);
		record.set("MAIL", mail);
		record.set("sysdate", super.getSysdate());
		return Db.save("WOBO_FORGETPWD", "WF_ID", record);
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170503
	 * 方法：校验是否已申请
	 */
	public boolean checkApply(String epName, String orgCode){
		Record record = Db.findFirst("select * from WOBO_FORGETPWD where EP_NAME = ? and ORGCARD = ? and STATUS = '1'", epName, orgCode);
		if(record != null){
			return false;
		}else{
			return true;
		}
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170503
	 * 方法：校验公司信息是否存在
	 */
	public boolean checkEpInfo(String epName, String orgCode){
		Record record = Db.findFirst("select * from WOBO_ENTERPRISE where EP_NAME = ? and REGISTERCODE = ? and STATUS = '1'", epName, orgCode);
		if(record != null){
			return true;
		}else{
			return false;
		}
	}
	
	//获取角色菜单
	public String getMenuList(String userId)
	{
		return StrKit.toDTstr(super.roleMenu(userId));
	}
}
