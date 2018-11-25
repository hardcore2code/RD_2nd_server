package com.mine.rd.controllers.transferSummary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ext.render.excel.PoiRender;
import com.jfinal.aop.Before;
import com.mine.pub.controller.BaseController;
import com.mine.pub.service.Service;
import com.mine.rd.interceptors.LoginInterceptor;
import com.mine.rd.services.transferSummary.pojo.TransferSummaryDao;
import com.mine.rd.services.transferSummary.service.TransferSummaryService;

public class TransferSummaryController extends BaseController {
	
	private Logger logger = Logger.getLogger(TransferSummaryController.class);
	
	/**
	 * @author weizanting
	 * @date 20170622
	 * 方法：查询转移汇总（按产生单位）
	 */
	public void queryTransferSummaryForCS(){
		logger.info("查询转移汇总（按产生单位）");
		Service service = new TransferSummaryService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("查询转移汇总（按产生单位）异常===>" + e.getMessage());
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author weizanting
	 * @date 20170622
	 * 方法：导出转移汇总（按产生单位）
	 */
	@Before(LoginInterceptor.class)
	public void exportTransferSummaryForCS(){
		String BEGINTIME = "";
		String ENDTIME = "";
		String ROLEID = "";
		String userType = "";
		String orgCode = "";
		String epId = "";
		Object searchContent = "";
		BEGINTIME = getMyParam("BEGINTIME").toString();
		ENDTIME = getMyParam("ENDTIME").toString();
		ROLEID = getMyParam("ROLEID").toString();
		userType = getMyParam("userType").toString();
		orgCode = getMyParam("orgCode").toString();
		epId = getMyParam("epId").toString();
		searchContent = getMyParam("searchContent");
		TransferSummaryDao dao = new TransferSummaryDao();
		String filename = "transferSummaryListToProductionUnit.xlsx";
		int num = 1;
		String[] sheetNames = new String[num];
		List<?>[] sheetAllList = new ArrayList<?>[num];
		String[][] headers = new String[num][];
		String[][] columns = new String[num][];
		dao.getTransferSummaryForCSExcelInfo(sheetNames, sheetAllList, headers, columns, BEGINTIME, ENDTIME, searchContent, ROLEID, userType, orgCode, epId);			
		PoiRender poiRender = PoiRender.me(sheetAllList).fileName(filename).sheetName(sheetNames).headers(headers).columns(columns).cellWidth(3000);
		Map<String, Object> map = new HashMap<String, Object>();
//		String url = getRequest().getServerName() + ":" + getRequest().getServerPort();
//		if(getRequest().getContextPath() != null && !"".equals(getRequest().getContextPath())){
//			url = url + getRequest().getContextPath();
//		}
		String url = "";
		map.put("path", url + poiRender.createFile());
		map.put("resFlag", "0");
		map.put("msg", "导出成功");
		setAttrs(map);
		renderJsonForCors();
	}
	
	/**
	 * @author weizanting
	 * @date 20170623
	 * 方法：查询转移汇总（按处置单位）
	 */
	public void queryTransferSummaryForCZ(){
		logger.info("查询转移汇总（按处置单位）");
		Service service = new TransferSummaryService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("查询转移汇总（按处置单位）异常===>" + e.getMessage());
			e.printStackTrace();
		}
		renderJsonForCors();
	}
	
	/**
	 * @author weizanting
	 * @date 20170623
	 * 方法：导出转移汇总（按处置单位）
	 */
	@Before(LoginInterceptor.class)
	public void exportTransferSummaryForCZ(){
		String BEGINTIME = "";
		String ENDTIME = "";
		String ROLEID = "";
		String orgCode = "";
		Object searchContent = "";
		BEGINTIME = getMyParam("BEGINTIME").toString();
		ENDTIME = getMyParam("ENDTIME").toString();
		ROLEID = getMyParam("ROLEID").toString();
		orgCode = getMyParam("orgCode").toString();
		searchContent = getMyParam("searchContent");
		TransferSummaryDao dao = new TransferSummaryDao();
		String filename = "transferSummaryListByDisposalUnit.xlsx";
		int num = 1;
		String[] sheetNames = new String[num];
		List<?>[] sheetAllList = new ArrayList<?>[num];
		String[][] headers = new String[num][];
		String[][] columns = new String[num][];
		dao.getTransferSummaryForCZExcelInfo(sheetNames, sheetAllList, headers, columns, BEGINTIME, ENDTIME, searchContent, ROLEID, orgCode);			
		PoiRender poiRender = PoiRender.me(sheetAllList).fileName(filename).sheetName(sheetNames).headers(headers).columns(columns).cellWidth(3000);
		Map<String, Object> map = new HashMap<String, Object>();
//		String url = getRequest().getServerName() + ":" + getRequest().getServerPort();
//		if(getRequest().getContextPath() != null && !"".equals(getRequest().getContextPath())){
//			url = url + getRequest().getContextPath();
//		}
		String url = "";
		map.put("path", url + poiRender.createFile());
		map.put("resFlag", "0");
		map.put("msg", "导出成功");
		setAttrs(map);
		renderJsonForCors();
	}
	
}
