package com.mine.rd.services.demo.service;


import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.xfire.client.Client;
import org.json.JSONArray;
import org.json.JSONObject;

import com.jfinal.kit.LogKit;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Record;
import com.mine.pub.controller.BaseController;
import com.mine.pub.kit.DateKit;
import com.mine.pub.kit.Md5kit;
import com.mine.pub.service.BaseService;
import com.mine.rd.services.demo.pojo.DemoDao;

public class DemoService extends BaseService
{
	DemoDao dao = new DemoDao();
	private String url = PropKit.get("uploadInterfaceUrl");
	private String username = PropKit.get("uploadInterfaceUsername");
	private String pwd = PropKit.get("uploadInterfacePwd");
	private String methodapi = PropKit.get("uploadInterfaceMethod");
//	Map<String,int[]> counterMap = new HashMap<String,int[]>();
	public DemoService(BaseController controller) {
		super(controller);
	}
	private void test()
	{
		if(controller.getMyParam("pwd") != null)
		{
			System.out.println("pwd 16========>>"+Md5kit.toMd5(controller.getMyParam("pwd").toString(),16));
			System.out.println("pwd 32========>>"+Md5kit.toMd5(controller.getMyParam("pwd").toString(),32));
		}
		
		System.out.println("getParamMap===>>"+controller.getParamMap());
		System.out.println("liner_name===========>>"+controller.getMyParam("liner_name"));
//		System.out.println("map name===========>>"+getMyParamMap("visitors111").get("name"));
//		System.out.println("list cardtype===========>>"+getMyParamList("visitors").get(0).get("card_type"));
		System.out.println("你的业务");
		System.out.println("获取主键====>>"+dao.getSeqId("os_user"));
		System.out.println("获取系统时间====>>"+dao.getSysdate());
		System.out.println("获取系统时间字符串====>>"+DateKit.getTimestamp(dao.getSysdate()));
		//session 处理
//		controller.setAppSession("1aaa", "2bbb");
//		Db.update("insert into demo (name) values (?) " ,"aa");
	}
	private void saveUpdateFlow() throws Exception{
		String tbId = controller.getPara("tbid").toString();
		dao.saveUploadFlow(tbId);
	}
	private void upload() throws Exception
	{
		String tbids = dao.getTbids();
		if(!"".equals(tbids)){
			List<Record> list = dao.queryBillList(tbids);
			JSONObject json = null;
			JSONArray jsonArray = null;
			for(Record record : list)
			{
				json=new JSONObject();
				jsonArray = new JSONArray();
//				json.put("shengxzqh", "120000");//省行政区划代码
				json.put("wfycrq", record.getStr("mydate") == null ? "" : record.getStr("mydate"));//转移日期  
				json.put("ldbh", record.getStr("tb_id") == null ? "" : record.getStr("tb_id"));//转移联单编号		
				json.put("wfycdwbm", record.getStr("en_id_cs") == null ? "" : record.getStr("en_id_cs"));//转移联单编号		
				json.put("ycsxzqhdm", record.getStr("regcodecs") == null ? "" : record.getStr("regcodecs"));//移出地行政区划代码
				json.put("wfycdwmc", record.getStr("en_name_cs") == null ? "" : record.getStr("en_name_cs"));//产生单位名称
				json.put("wfycdwdz", record.getStr("csdz") == null ? "" : record.getStr("csdz"));//产生单位地址
				json.put("wfycdwlxr", record.getStr("csy") == null ? "" : record.getStr("csy"));//产生单位联系人
				json.put("fwycdwlxrsj", ""); //产生单位联系人手机		
				json.put("yrsxzqhdm", record.getStr("regcodecz") == null ? "" : record.getStr("regcodecz"));  //经营单位地行政区划代码
				json.put("wfjsdwjsrq", record.getStr("mydate") == null ? "" : record.getStr("mydate"));  //危废接收单位接收日期
				json.put("fwjsdwwxfwjyxkzh", record.getStr("licenseNo") == null ? "" : record.getStr("licenseNo"));//经营单位许可证号
				json.put("wfjsdwmc", record.getStr("en_name_cz") == null ? "" : record.getStr("en_name_cz"));//经营单位名称
				json.put("wfjsdz", record.getStr("czdz") == null ? "" : record.getStr("czdz"));//经营单位地址
				json.put("wfjsdwlxr", record.getStr("czy") == null ? "" : record.getStr("czy"));//经营单位联系人
				json.put("wfjsdwlxrsj", "");//经营单位联系人手机
				json.put("wfjsdwclyj", record.getStr("myStatus") == null ? "" : record.getStr("myStatus"));//危废接收单位处理意见
				json.put("sfczzdcy", "");//是否存在重大差异
				json.put("ysdwmc", record.getStr("en_name_ys") == null ? "" : record.getStr("en_name_ys"));//运输单位名称
				json.put("ysdwdz", record.getStr("ysdz") == null ? "" : record.getStr("ysdz"));//运输单位地址
				json.put("ysdwlxr", record.getStr("ysy") == null ? "" : record.getStr("ysy"));//运输单位联系人
				json.put("ysdwlxrsj", "");//运输单位联系人手机		
				json.put("ysdwdlyszh","");//运输单位道路运输证号	
				json.put("wfysdwjsrq",record.getStr("mydate") == null ? "" : record.getStr("mydate"));//危废运输单位接收日期
				json.put("tjds","");//途径地市
				JSONObject gps = new JSONObject();  
				json.put("gps", gps);
				List<Record> sons = dao.queryBillSonList(record.getStr("tb_id"));
				Map<String,String> details = null;
				for(Record son : sons)
				{
					details = new HashMap<String,String>();  
					details.put("wxfwmc", son.getStr("d_name") == null ? "" : son.getStr("d_name"));//危险废物名称
					details.put("wxfwlb", son.getStr("big_category_id") == null ? "" : son.getStr("big_category_id").substring(2));  //危险废物废物类别
					details.put("wxfwdm", son.getStr("samll_category_id") == null ? "" : son.getStr("samll_category_id"));//危险废物代码
					details.put("lyczfs", dao.getHandleMode(record.getStr("en_id_cz"), record.getStr("en_id_cs"), son.getStr("big_category_id"), son.getStr("samll_category_id"), son.getStr("unit")));//利用处置方式
					details.put("zysl", son.getStr("unit_num") );//数量
					details.put("jldw", "个".equals(son.getStr("unit")) ? "桶" : son.getStr("unit"));//单位	
					jsonArray.put(details);
				}
				json.put("details", jsonArray);
				try {
					Client client = new Client(new URL(url));
					Object[] token = client.invoke("getToken",new Object[] { username, pwd });
					Object[] result = client.invoke(methodapi, new Object[] {token[0].toString(),json });
//					System.out.println("json===>>"+json);
//					System.out.println("result===>>"+result[0]);
					LogKit.info("result===>>"+result[0]+",json===>>"+json);
					dao.updateUploadFlow(record.getStr("tb_id"),result[0].toString());
//					this.counter(counterMap,result[0].toString());  //计数器
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void counter(Map<String,int[]> counterMap,String status)
	{
		int[] n=counterMap.get(status);
		if(n == null)
		{
			counterMap.put(status, new int[]{1});
		}
		else
		{
			n[0]++;
		}
	}
	@Override
	public void doService(){
		Db.tx(new IAtom() {
	        @Override
	        public boolean run() throws SQLException {
	            try {
	            	if("test".equals(getLastMethodName())){
	        			test();
	        			System.out.println("LastMethodName====>>" + getLastMethodName());
	        			controller.setAttr("", toJson());
	        		}
	            	if("uploadLocalTransferData".equals(getLastMethodName(7)))
	            	{
	            		saveUpdateFlow();
	            		upload();
	            		controller.setAttr("res", "upload success");
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
