package com.mine.rd.services.setBoxSuttle.service;

import java.sql.SQLException;
import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.ehcache.CacheKit;
import com.mine.pub.controller.BaseController;
import com.mine.pub.service.BaseService;
import com.mine.rd.services.setBoxSuttle.pojo.SetBoxSuttleDao;

public class SetBoxSuttleService extends BaseService {

	private SetBoxSuttleDao dao = new SetBoxSuttleDao();
	
	public SetBoxSuttleService(BaseController controller) {
		super(controller);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @author weizanting
	 * @date 20171016
	 * 方法：保存或修改桶净重
	 */
	private void setBoxSuttle(){
		if(controller.getMyParam("boxSuttle") != null){
			String str = controller.getMyParam("boxSuttle").toString();
			boolean flag = dao.setBoxSuttle(str);
			if(flag){
				controller.setAttr("resFlag", "0");
				controller.setAttr("msg", "保存成功");
				List<Record> boxSuttle = CacheKit.get("mydict", "box_suttle");
				boxSuttle.get(0).set("dict_value", str);
				CacheKit.put("mydict", "box_suttle", boxSuttle);
			}else{
				controller.setAttr("resFlag", "0");
				controller.setAttr("msg", "保存失败");
			}
		}else{
			controller.setAttr("resFlag", "1");
			controller.setAttr("msg", "少传参数");
		}
	}
	
	
	@Override
	public void doService() throws Exception {
		// TODO Auto-generated method stub
		Db.tx(new IAtom() {
	        @Override
	        public boolean run() throws SQLException {
	            try {
	            	if("setBoxSuttle".equals(getLastMethodName(7))){
	            		setBoxSuttle();
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
