package com.mine.rd1.services.statistics.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.mine.pub.controller.BaseController;
import com.mine.pub.service.BaseService;
import com.mine.rd.services.arcGis.pojo.ArcGisDao;

public class ArcGisCeService extends BaseService {

	private ArcGisDao dao = new ArcGisDao();
	
	public ArcGisCeService(BaseController controller) {
		super(controller);
	}
	
	/**
	 * @author 张玉利
	 * @date 20171122
	 * 方法：保存Wince设备的设备坐标到坐标a表中
	 */
	private void saveGisCus() throws Exception{
		if(controller.getMyParam("location") != null && controller.getMyParam("uuid") != null && controller.getMyParam("tbId") != null){
			Map<String, Object> map=new HashMap<String, Object>();
			map.put("sessionId", controller.getMyParam("sessionId").toString());
			map.put("uuid", controller.getMyParam("uuid").toString());
			map.put("tbId", controller.getMyParam("tbId").toString());
			map.put("location", controller.getMyParam("location").toString());
			dao.saveLocationConduit(map);
		}else{
			controller.setAttr("resFlag", "1");
			controller.setAttr("msg", "少传参数");
		}
	}
	
	/**
	 * @author 张玉利
	 * @date 20171122
	 * 方法：保存Wince设备的设备坐标到坐标a表中
	 */
	private void saveGisTeyCus() throws Exception{
		if(controller.getMyParam("location") != null && controller.getMyParam("uuid") != null && controller.getMyParam("tbId") != null){
			Map<String, Object> map=new HashMap<String, Object>();
			map.put("sessionId", controller.getMyParam("sessionId").toString());
			map.put("uuid", controller.getMyParam("uuid").toString());
			map.put("tbId", controller.getMyParam("tbId").toString());
			map.put("location", controller.getMyParam("location").toString());
			dao.saveGisHistory(map);
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
	            	if("saveGisCus".equals(getLastMethodName(7))){
	            		saveGisCus();
	            	}
	            	else if("saveGisTeyCus".equals(getLastMethodName(7))){
	            		saveGisTeyCus();
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
