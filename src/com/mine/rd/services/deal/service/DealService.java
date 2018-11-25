package com.mine.rd.services.deal.service;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Map;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.mine.pub.controller.BaseController;
import com.mine.pub.service.BaseService;
import com.mine.rd.services.deal.pojo.DealDao;

public class DealService extends BaseService {

	private DealDao dao = new DealDao();
	
	private int pn = 0;
	private int ps = 0;
	
	public DealService(BaseController controller) {
		super(controller);
	}

	/**
	 * @author ouyangxu
	 * @throws ParseException 
	 * @date 20170331
	 * 方法：查询当天处置信息
	 */
	private void queryDealListToday() throws ParseException{
		if(controller.getMyParam("epId") != null && controller.getMyParam("pn") != null && controller.getMyParam("ps") != null){
			String epId = controller.getMyParam("epId").toString();
			pn = Integer.parseInt(controller.getMyParam("pn").toString());
			ps = Integer.parseInt(controller.getMyParam("ps").toString());
			controller.setAttrs(dao.queryDealListToday(epId, pn, ps));
			controller.setAttr("resFlag", "0");
		}else{
			controller.setAttr("resFlag", "0");
			controller.setAttr("msg", "少传参数");
		}
	}
	
	/**
	 * @author ouyangxu
	 * @throws ParseException 
	 * @date 20170405
	 * 方法：查询历史处置信息
	 */
	private void queryDealListHistory() throws ParseException{
		if(controller.getMyParam("epId") != null && controller.getMyParam("pn") != null && controller.getMyParam("ps") != null){
			String epId = controller.getMyParam("epId").toString();
			pn = Integer.parseInt(controller.getMyParam("pn").toString());
			ps = Integer.parseInt(controller.getMyParam("ps").toString());
			controller.setAttrs(dao.queryDealListHistory(epId, pn, ps));
			controller.setAttr("resFlag", "0");
		}else{
			controller.setAttr("resFlag", "0");
			controller.setAttr("msg", "少传参数");
		}
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170405
	 * 方法：处置医疗废物
	 */
	private void dealBox(){
		Map<String, Object> dealInfo = controller.getMyParamMap("dealInfo");
		int num = dao.dealBox(dealInfo);
		boolean result = true;
		if(num > 0){
			result = dao.insertDealInfo(dealInfo, num);
		}
		if(result){
			controller.setAttr("dealNum", num);
			controller.setAttr("resFlag", "0");
			controller.setAttr("msg", "处置成功"+num+"箱废物！");
		}else{
			controller.setAttr("resFlag", "1");
			controller.setAttr("msg", "处置失败！");
		}
	}
	
	@Override
	public void doService() throws Exception {
		Db.tx(new IAtom() {
	        @Override
	        public boolean run() throws SQLException {
	            try {
	            	if("queryDealListToday".equals(getLastMethodName(7))){
	        			queryDealListToday();
	        		}else if("queryDealListHistory".equals(getLastMethodName(7))){
	        			queryDealListHistory();
	        		}else if("dealBox".equals(getLastMethodName(7))){
	        			dealBox();
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
