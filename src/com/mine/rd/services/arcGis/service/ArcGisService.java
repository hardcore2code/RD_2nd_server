package com.mine.rd.services.arcGis.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.jfinal.json.Jackson;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.mine.pub.controller.BaseController;
import com.mine.pub.service.BaseService;
import com.mine.rd.services.arcGis.pojo.ArcGisDao;

public class ArcGisService extends BaseService {
	private String mess;

	private ArcGisDao dao = new ArcGisDao();
	public ArcGisService(BaseController controller) {
		super(controller);
	}
	
	public ArcGisService(BaseController controller,String mess) {
		super(controller);
		this.mess=mess;
	}

	/**
	 * @author zyl
	 * @date 20170906
	 * 方法：根据车辆牌照获取车辆位置
	 */
	private Map<String, Object> queryPositionByLp(){
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> map = Jackson.getJson().parse(mess, Map.class);
		Object qc = map.get("qc");
		if(qc != null){
			result=dao.queryPositionByLp(qc.toString());
		}
		return result;
	}
	/**
	 * @author zyl
	 * @date 20170906
	 * 方法：根据产生单位名称获取车辆位置
	 */
	private Map<String, Object> queryPositionByCs(){
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> map = Jackson.getJson().parse(mess, Map.class);
		Object qc = map.get("qc");
		if(qc != null){
			result=dao.queryPositionByCs(qc.toString());
		}
		return result;
	}
	
	/**
	 * @author zyl
	 * @date 20170906
	 * 方法：根据运输单位名称获取车辆位置
	 */
	private Map<String, Object> queryPositionByYs(){
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> map = Jackson.getJson().parse(mess, Map.class);
		Object qc = map.get("qc");
		if(qc != null){
			result=dao.queryPositionByYs(qc.toString());
		}
		return result;
	}
	
	/**
	 * @author zyl
	 * @date 20170906
	 * 方法：根据处置单位名称获取车辆位置
	 */
	private Map<String, Object> queryPositionByCz(){
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> map = Jackson.getJson().parse(mess, Map.class);
		Object qc = map.get("qc");
		if(qc != null){
			result=dao.queryPositionByCz(qc.toString());
		}
		return result;
	}
	
	/**
	 * @author zyl
	 * @date 20170906
	 * 方法：根据联单编号查询具体联单及车辆信息
	 */
	private Map<String, Object> queryBillInfo(){
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> map = Jackson.getJson().parse(mess, Map.class);
		Object tbId = map.get("tbId");
		if(tbId != null){
			result=dao.queryBillInfo(tbId.toString());
		}
		return result;
	}
	
	/**
	 * @author zyl
	 * @date 20170906
	 * 方法：查询所有车辆定位信息
	 */
	private Map<String, Object> queryPositions(){
		Map<String, Object> result =dao.queryPositions();
		return result;
	}
	
	/**
	 * @author zyl
	 * @date 20170906
	 * 方法：查询车辆轨迹
	 */
	private Map<String, Object> queryCartTrajectory(){
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> map = Jackson.getJson().parse(mess, Map.class);
		Object qc = map.get("qc");
		if(qc != null){
			result=dao.queryCartTrajectory(qc.toString());
		}
		return result;
	}
	
	/**
	 * @author zyl
	 * @date 20170906
	 * 方法：查询所有车辆定位信息
	 */
	private void delCarPosition(){
		Map<String, Object> map = Jackson.getJson().parse(mess, Map.class);
		Object uuId = map.get("uuId");
		dao.delCarPosition(uuId.toString());
	}
	
	/**
	 * @author zyl
	 * @date 20170906
	 * 方法：查询所有车辆定位信息
	 */
	private void saveGisHistory(){
		Map<String, Object> map = Jackson.getJson().parse(mess, Map.class);
		dao.saveGisHistory(map);
	}
	
	@Override
	public void doService() throws Exception {
		Db.tx(new IAtom() {
	        @Override
	        public boolean run() throws SQLException {
	        	Map<String, Object> map = Jackson.getJson().parse(mess, Map.class);
	        	Map<String, Object> result=null;
	            try {
	            	String mn = map.get("mName").toString();
	            	if(controller.resMap == null)
	            	{
	            		controller.resMap=new HashMap<String, Object>();
	            	}
	            	if("queryPositions".equals(mn)){
	            		result=queryPositions();
	            	}
	            	else if("queryPositionByLp".equals(mn)){
	            		result=queryPositionByLp();
	            	}
	            	else if("queryPositionByCs".equals(mn)){
	            		result=queryPositionByCs();
	            	}
	            	else if("queryPositionByYs".equals(mn)){
	            		result=queryPositionByYs();
	            	}
	            	else if("queryPositionByCz".equals(mn)){
	            		result=queryPositionByCz();
	            	}
	            	else if("queryBillInfo".equals(mn)){
	            		result=queryBillInfo();
	            	}
	            	else if("queryCartTrajectory".equals(mn)){
	            		result=queryCartTrajectory();
	            	}
	            	else if("delCarPosition".equals(mn)){
	            		delCarPosition();
	            	}
	            	else if("saveGisHistory".equals(mn)){
	            		saveGisHistory();
	            	}
	            	controller.resMap.put("carPositions", result);
	            } catch (Exception e) {
	                e.printStackTrace();
	                return false;
	            }
	            return true;
	        }
	    });
	}

}
