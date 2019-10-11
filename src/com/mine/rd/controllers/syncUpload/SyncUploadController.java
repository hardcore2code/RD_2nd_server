package com.mine.rd.controllers.syncUpload;

import java.net.URL;

import org.apache.log4j.Logger;
import org.codehaus.xfire.client.Client;
import org.json.JSONObject;

import com.jfinal.aop.Clear;
import com.mine.pub.controller.BaseController;
import com.mine.pub.service.Service;
import com.mine.rd.services.syncUpload.service.SyncUploadService;

@Clear
public class SyncUploadController extends BaseController{
	private Logger logger = Logger.getLogger(SyncUploadController.class);
	public void index(){
		renderJson("sync upload start");
	}
	
	public void indexbywotimer()
	{
		logger.info("来自wotimer的请求");
		Service service = new SyncUploadService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("来自wotimer的请求异常===>" + e.getMessage());
			logger.error("来自wotimer的请求异常getLocalizedMessage===>" + e.getLocalizedMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * @author woody
	 * @date 20190417
	 * @产生单位填领数据接口
	 * */
	public void saveSnld(){
		logger.info("产生单位填领数据接口");
		Service service = new SyncUploadService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("产生单位填领数据接口异常===>" + e.getMessage());
			e.printStackTrace();
		}
		renderJson();
	}
	
	/**
	 * @author woody
	 * @date 20190417
	 * @产生单位填领数据接口
	 * */
	public void saveSnldYS(){
		logger.info("运输单位确认数据接口");
		Service service = new SyncUploadService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("运输单位确认数据接口异常===>" + e.getMessage());
			e.printStackTrace();
		}
		renderJson();
	}
	
	/**
	 * @author woody
	 * @date 20190417
	 * @产生单位填领数据接口
	 * */
	public void saveSnldJY(){
		logger.info("经营单位办结数据接口");
		Service service = new SyncUploadService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("经营单位办结数据接口异常===>" + e.getMessage());
			e.printStackTrace();
		}
		renderJson();
	}
	
	/**
	 * @author woody
	 * @date 20190522
	 * @通过许可证编号和单位名称查询经营单位
	 * */
	public void getXkz(){
		logger.info("通过许可证编号和单位名称查询经营单位");
		Service service = new SyncUploadService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("通过许可证编号和单位名称查询经营单位异常===>" + e.getMessage());
			logger.error("通过许可证编号和单位名称查询经营单位异常getLocalizedMessage===>" + e.getLocalizedMessage());
			e.printStackTrace();
		}
	}
	
	public void getXkzSheng(){
		logger.info("通过许可证编号和单位名称查询经营单位");
		Service service = new SyncUploadService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("通过许可证编号和单位名称查询经营单位异常===>" + e.getMessage());
			logger.error("通过许可证编号和单位名称查询经营单位异常getLocalizedMessage===>" + e.getLocalizedMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * @date 20190522
	 * @查询许可证正式方法
	 * @param 许可证号
	 * @return 返回许可证经营危险废物代码信息
	 * @备注 根据参数模糊查询
	 * */
	public void getXkzfw()
	{	
		logger.info("返回许可证经营危险废物代码信息");
		Service service = new SyncUploadService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("返回许可证经营危险废物代码信息异常===>" + e.getMessage());
			logger.error("返回许可证经营危险废物代码信息异常getLocalizedMessage===>" + e.getLocalizedMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * @date 20190522
	 * @上传转移联单
	 * */
	public void saveZyld(){
		logger.info("上传转移联单");
		Service service = new SyncUploadService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("上传转移联单异常===>" + e.getMessage());
			logger.error("上传转移联单异常getLocalizedMessage===>" + e.getLocalizedMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * @date 20190522
	 * @许可证编号、是否办结（0 未办结，1 已办结）查询转移联单
	 * @param json
	 * */
	public void getZyldByXkzbhAndSfbj()
	{
		logger.info("查询转移联单");
		Service service = new SyncUploadService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("查询转移联单异常===>" + e.getMessage());
			logger.error("查询转移联单异常getLocalizedMessage===>" + e.getLocalizedMessage());
			e.printStackTrace();
		}
	}
	
	public void getZyldByYcDwmcAndSfbj()
	{
		logger.info("查询转移联单");
		Service service = new SyncUploadService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("查询转移联单异常===>" + e.getMessage());
			logger.error("查询转移联单异常getLocalizedMessage===>" + e.getLocalizedMessage());
			e.printStackTrace();
		}
	}
	
	public void getZyldByzyldbh()
	{
		logger.info("查询转移联单");
		Service service = new SyncUploadService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("查询转移联单异常===>" + e.getMessage());
			logger.error("查询转移联单异常getLocalizedMessage===>" + e.getLocalizedMessage());
			e.printStackTrace();
		}
	}
	
	public void bjZyld()
	{
		logger.info("办结联单");
		Service service = new SyncUploadService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("办结联单异常===>" + e.getMessage());
			logger.error("办结联单异常getLocalizedMessage===>" + e.getLocalizedMessage());
			e.printStackTrace();
		}
	}
	
	public void getYcKsld()
	{
		logger.info("移出联单");
		Service service = new SyncUploadService(this);
		try {
			service.doService();
		} catch (Exception e) {
			logger.error("移出联单异常===>" + e.getMessage());
			logger.error("移出联单异常getLocalizedMessage===>" + e.getLocalizedMessage());
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws Exception {
		// TODO 省内转移联单
		JSONObject jsonSN=new JSONObject();
		jsonSN.put("qyid", "11111000000000000001"); // 企业平台id
		jsonSN.put("jsqyid", "11111000000000000001"); // 企业平台id
		jsonSN.put("jsqyxkz", "TJHW006");
		jsonSN.put("ysdwlxrsj", "13480868991");
		jsonSN.put("ysxzqhdm", "120104");
		jsonSN.put("wfjsdz", "广东省惠州市仲恺高新区联发大道39号");
		jsonSN.put("wfjsdwlxrsj", "13544047273");
		jsonSN.put("wfjsdwmc", "惠州市东江运输有限公司");
		jsonSN.put("wfjsdwjsrq", "2017-06-02");
		jsonSN.put("fwjsdwwxfwjyxkzh", "441302150727"); 
		jsonSN.put("yrsxzqhdm", "530100");
		jsonSN.put("wfjsdwlxr", "荣祥");
		jsonSN.put("wxfwmc", "含铜废液"); 
		jsonSN.put("wfycrq", "2017-05-27 00:00:00"); 
		jsonSN.put("fwycdwlxrsj", "13510888869"); 
		jsonSN.put("wfycdwmc", "惠州比亚迪实业有限公司"); 
		jsonSN.put("wfycdwbm", "21054618325");
		jsonSN.put("ysdwlxr", "李星国");  
		jsonSN.put("ysdwdz", "广东省惠州市仲恺高新区潼侨工业区联发大道39号"); 
		jsonSN.put("wfycdwdz", "广东省惠州市大亚湾经济技术开发区惠州市大亚湾响水河"); 
		jsonSN.put("ysdwmc", "惠州市东江环保技术有限公司"); 
		jsonSN.put("tjds", "0-直达"); 
		jsonSN.put("ycsxzqhdm", "530100"); 
		jsonSN.put("wxfwlb", "HW22"); 
		jsonSN.put("wfysdwjsrq", "2017-05-27"); 
		jsonSN.put("ldbh", "4413222017058232"); 
		jsonSN.put("wfycdwlxr", "古淑芬"); 
		jsonSN.put("wfycdwlxrsj", "123"); 
		jsonSN.put("wxfwdm", "397-005-22"); 
		jsonSN.put("lyczfs", "R4"); 
		jsonSN.put("zysl", 15); 
		jsonSN.put("jldw", "吨"); 
		String reqSNStr = jsonSN.toString();
		String key = "d06253d47cee9a4c";
		String encSNStr = "";
    	encSNStr = EncriptUtil.encrypt(reqSNStr, key);
    	try {
//			Client client = new Client(
//					new URL("http://114.251.10.14/edpgf_ws/servicesx/TranService?wsdl"));
//			Object[] result = client.invoke("saveSnld", new Object[] {key,encSNStr });
//			System.out.println(result.toString());
			Client client = new Client(new URL("http://114.251.10.14/edpgf_ws/servicesx/TranService?wsdl"));
			Object[] result = client.invoke("saveSnld", new Object[] {key, encSNStr }); 
			System.out.println("saveSnld返回码：" + result[0].toString());
			if (result[0].toString().length() > 4 && result[0].toString().length() != 2) {
				System.out.println(result.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
