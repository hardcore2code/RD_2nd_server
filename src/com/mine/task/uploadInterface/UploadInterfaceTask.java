package com.mine.task.uploadInterface;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.xfire.client.Client;
import org.json.JSONArray;
import org.json.JSONObject;

import com.jfinal.kit.LogKit;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.Record;
import com.mine.task.uploadInterface.pojo.UploadInterfaceDao;

public class UploadInterfaceTask implements Runnable 
{
	private UploadInterfaceDao dao = new UploadInterfaceDao();
	private String url = PropKit.get("uploadInterfaceUrl");
	private String username = PropKit.get("uploadInterfaceUsername");
	private String pwd = PropKit.get("uploadInterfacePwd");
	private String methodapi = PropKit.get("uploadInterfaceMethod");
	Map<String,int[]> counterMap = null;
	@Override
	public void run() {
		counterMap = new HashMap<String,int[]>();
		System.out.println("===================传输与国家对接的接口报文 start===================");
		LogKit.info("===================传输与国家对接的接口报文 start===================");
		try {
			upload() ;
			dao.saveCounterFlow(counterMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		LogKit.info("===================传输与国家对接的接口报文 end===================");
		System.out.println("===================传输与国家对接的接口报文 end===================");
	}
	private void upload() throws Exception
	{
		List<Record> list = dao.queryBillList();
		JSONObject json = null;
		JSONArray jsonArray = null;
		for(Record record : list)
		{
			json=new JSONObject();
			jsonArray = new JSONArray();
//			json.put("shengxzqh", "120000");//省行政区划代码
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
//				System.out.println("json===>>"+json);
//				System.out.println("result===>>"+result[0]);
				LogKit.info("result===>>"+result[0]+",json===>>"+json);
				this.counter(counterMap,result[0].toString());  //计数器
			} catch (Exception e) {
				e.printStackTrace();
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
	//接口举例声明 json键值对 以最终省里发的接口标准为准 该示例不代表最终的数据标准
	public static void main(String[] args) throws Exception {		
//		JSONObject json=new JSONObject();  		
//		json.put("shengxzqh", "210000");//省行政区划代码
//		json.put("zyrq", "2017-01-01");//转移日期  
//		json.put("zyldbh", "testzj000000001");//转移联单编号		
//		json.put("csdwsxzqhdm", "210100");//移出地行政区划代码
//		json.put("csdwmc", "产生单位名称");//产生单位名称
//		json.put("csdwdz", "同福里");//产生单位地址
//		json.put("csdwlxr", "徐天");//产生单位联系人
//		json.put("csdwlxrdh", "13333333333"); //产生单位联系人手机		
//		json.put("jydwsxzqhdm", "210200");  //经营单位地行政区划代码
//		json.put("wxfwjyxkzh", "xkz00000001");//经营单位许可证号
//		json.put("jydwmc", "经营单位名称");//经营单位名称
//		json.put("jydwdz", "宪兵司令部");//经营单位地址
//		json.put("jydwlxr", "硬座");//经营单位联系人
//		json.put("jydwlxrdh", "14444444444");//经营单位联系人手机		
//		json.put("ysdwmc", "运输单位名称");//运输单位名称
//		json.put("ysdwdz", "仙乐斯");//运输单位地址
//		json.put("ysdwlxr", "柳如丝");//运输单位联系人
//		json.put("ysdwlxrdh", "15555555555");//运输单位联系人手机		
//		json.put("wxfwmc", "废机油");//危险废物名称
//		json.put("wxfwlb", "HW08");  //危险废物废物类别
//		json.put("wxfwdm", "900-01-08");//危险废物代码
//		json.put("lyczfs", "R1");//利用处置方式
//		json.put("zysl", "1000");//数量
//		json.put("jldw", "吨");//单位		
//		JSONObject gps = new JSONObject();  
//		gps.put("1", "116.479583,39.98883");  //车辆行驶的百度经纬度格式
//		gps.put("2", "116.377823,39.871977");  //车辆行驶的百度经纬度格式
//		json.put("gps", gps);
//		try {
//			Client client = new Client(new URL("http://www.swmc.org.cn:8090/edpgf_csy/servicesx/TranService?wsdl"));
//			Object[] token = client.invoke("getToken",
//					new Object[] { "000000", "000000" });
////			Object[] result = client.invoke("saveJson", new Object[] {json,
////					token[0].toString() });
////			Object[] result = client.invoke("tranPaper_test_12", new Object[] {json,
////					token[0].toString() });
//			Object[] result = client.invoke("tranPaper_test_63", new Object[] {token[0].toString(),json});
//			System.out.println("result===>>"+result[0]);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}
}
