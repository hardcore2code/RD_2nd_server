package com.mine.rd.services.syncUpload.service;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.codehaus.xfire.client.Client;
import org.json.JSONArray;
import org.json.JSONObject;

import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.mine.pub.controller.BaseController;
import com.mine.pub.kit.JsonMyKit;
import com.mine.pub.service.BaseService;
import com.mine.rd.controllers.syncUpload.EncriptUtil;
import com.mine.rd.services.syncUpload.pojo.SyncUploadDao;

public class SyncUploadService  extends BaseService{
	private Logger logger = Logger.getLogger(SyncUploadService.class);
	private SyncUploadDao dao = new SyncUploadDao();
	private String url = PropKit.get("syncUploadUrl");
	private String KSTranServiceUrl = PropKit.get("syncKSTranServiceUrl");
	private String key = PropKit.get("syncUploadKey");
	
	public SyncUploadService(BaseController controller) {
		super(controller);
	}

	@Override
	public void doService() throws Exception {
		Db.tx(new IAtom() {
	        @Override
	        public boolean run() throws SQLException {
	            try {
	            	if("saveSnld".equals(getLastMethodName(7))){
	            		saveSnld();
	        		}
	            	else if("saveSnldYS".equals(getLastMethodName(7))){
	            		saveSnldYS();
	            	}
	            	else if("saveSnldJY".equals(getLastMethodName(7))){
	            		saveSnldJY();
	            	}
	            	else if("getXkz".equals(getLastMethodName(7))){
	            		getXkz();
	            	}
	            	else if("getXkzSheng".equals(getLastMethodName(7))){
	            		getXkz();
	            	}
	            	else if("getXkzfw".equals(getLastMethodName(7))){
	            		getXkzfw();
	            	}
	            	else if("saveZyld".equals(getLastMethodName(7))){
	            		saveZyld();
	            	}
	            	else if("getZyldByYcDwmcAndSfbj".equals(getLastMethodName(7))){
	            		getYrKsld();
	            	}
	            	else if("getZyldByXkzbhAndSfbj".equals(getLastMethodName(7))){
	            		getYrKsld();
	            	}
	            	else if("getZyldByzyldbh".equals(getLastMethodName(7))){
	            		getYrKsld();
	            	}
	            	else if("getYcKsld".equals(getLastMethodName(7))){
	            		getYcKsld();
	            	}
	            	else if("bjZyld".equals(getLastMethodName(7))){
	            		saveKsldJy();
	            	}
	            	else if("indexbywotimer".equals(getLastMethodName(7))){
	            		indexbywotimer();
	            	}
	            	else if("indexForCors".equals(getLastMethodName(7))){
	            		indexForCors();
	            	}
	            	else if("queryPlanByEpName".equals(getLastMethodName(7))){
	            		queryPlanByEpName();
	            	}
	            	else if("applySyncPlan".equals(getLastMethodName(7))){
	            		applySyncPlan();
	            	}
	            	else if("queryPlanById".equals(getLastMethodName(7))){
	            		queryPlanById();
	            	}
	            } catch (Exception e) {
	            	logger.error("services错误信息===>" + e.getMessage());
	                e.printStackTrace();
	                controller.setAttr("msg", "系统异常");
	    			controller.setAttr("resFlag", "1");
	                return false;
	            }
	            return true;
	        }
	    });
	}
	
	private void saveSnld() throws Exception{
		String tbId = controller.getPara("tbId") == null ? "" : controller.getPara("tbId").toString();
		if(!"".equals(tbId)){
			Map<String, Object> mapFlow = dao.getFlow(tbId, "saveSnld");
			if(mapFlow == null || (mapFlow != null && !mapFlow.get("ID").equals(mapFlow.get("result")))){
				Map<String, Object> map = dao.getBill(tbId);
				JSONObject json=new JSONObject();
				String Id = mapFlow == null ? dao.getId() : mapFlow.get("ID").toString() ;
				json.put("ldbh", Id);
//				json.put("djsbm", "");
				json.put("wfycrq", map.get("actiondate"));  //yyyy-mm-dd hh:mm:ss
				
				json.put("ycsxzqhdm", map.get("regcodecs"));
//				json.put("qyid", "");
				json.put("wfycdwmc", map.get("en_name_cs"));
				json.put("wfycdwdz", map.get("csdz"));
				json.put("wfycdwlxr", map.get("linkman"));
				json.put("wfycdwlxrsj",map.get("link_num"));
				
				json.put("ysxzqhdm", map.get("regcodeys"));
				json.put("ysdwmc", map.get("en_name_ys"));
				json.put("ysdwdz", map.get("ysdz"));
				Map<String,Object> ys = dao.getEnterprise(map.get("en_id_ys").toString());
				json.put("ysdwlxr", ys.get("linkman"));
				json.put("ysdwlxrsj", ys.get("tel") == null ? "无" : ys.get("tel"));
				json.put("ysdwdlyszh", map.get("ys_zz"));
				
				json.put("yrsxzqhdm", map.get("regcodecz"));
//				json.put("jsqyid", "");
				json.put("jsqyxkz", map.get("licenseNo"));
				json.put("wfjsdwmc", map.get("en_name_cz"));
				json.put("wfjsdz", map.get("czdz"));
				Map<String,Object> cz = dao.getEnterprise(map.get("en_id_cz").toString());
				json.put("wfjsdwlxr", cz.get("linkman"));
				json.put("wfjsdwlxrsj", cz.get("tel") == null ? "无" : cz.get("tel"));
				
				JSONArray arr = new JSONArray();
				List<Map<String,Object>> list = dao.getBillList(tbId);
				for(Map<String ,Object> item : list){
					JSONObject tmp = new JSONObject();
					tmp.put("fwmc", item.get("d_name"));
					tmp.put("fwlb", item.get("big_category_id"));
					tmp.put("fwdm", item.get("samll_category_id"));
					tmp.put("xt", item.get("w_shape"));
					tmp.put("xz", item.get("character"));
					tmp.put("bzlx", "");
					tmp.put("bzsl", "");
					tmp.put("lyczfs", item.get("handle_type").toString().substring(0,item.get("handle_type").toString().indexOf("-")));
					tmp.put("zysl", item.get("unit_num"));
					tmp.put("jldw", "个".equals(item.get("unit")) ? "桶":"吨");
					arr.put(tmp);
				}
				json.put("fwsz", arr);
				Object[] result = this.myClient(json.toString(), "saveSnld",url);
				dao.saveFlow(Id, tbId, "saveSnld", result[0].toString());
				System.out.println("saveSnld返回码：" + result[0].toString());
				controller.setAttr("result",result[0].toString());
			}
		}else{
			controller.setAttr("result","tbId为空");
		}	
	}
	
	private void saveSnldYS() throws Exception{
		String tbId = controller.getPara("tbId") == null ? "" : controller.getPara("tbId").toString();
		if(!"".equals(tbId)){
			Map<String, Object> mapFlow = dao.getFlow(tbId, "saveSnldYS");
			if(mapFlow == null || (mapFlow != null && !mapFlow.get("ID").equals(mapFlow.get("result")))){
				Map<String, Object> mapFlow1 = dao.getFlow(tbId, "saveSnld");
				String Id = mapFlow1.get("ID").toString();
				Map<String, Object> map = dao.getBillInfo(tbId);
				JSONObject json=new JSONObject();
				json.put("ldbh", Id);
//				json.put("djsbm", "");
				json.put("ysrq", map.get("ACTIONDATE").toString().substring(0, map.get("ACTIONDATE").toString().indexOf(".")));  //yyyy-mm-dd hh:mm:ss
				json.put("yscph", map.get("PLATE_NUMBER"));
				Object[] result = this.myClient(json.toString(), "saveSnldYS",url);
				dao.saveFlow(Id, tbId, "saveSnldYS", result[0].toString());
				System.out.println("saveSnld返回码：" + result[0].toString());
				controller.setAttr("result",result[0].toString());
			}
		}else{
			controller.setAttr("result","tbId为空");
		}	
	}
	
	private void saveSnldJY() throws Exception{
		String tbId = controller.getPara("tbId") == null ? "" : controller.getPara("tbId").toString();
		if(!"".equals(tbId)){
			Map<String, Object> mapFlow = dao.getFlow(tbId, "saveSnldJY");
			if(mapFlow == null || (mapFlow != null && !mapFlow.get("ID").equals(mapFlow.get("result")))){
				Map<String, Object> mapFlow1 = dao.getFlow(tbId, "saveSnld");
				String Id = mapFlow1.get("ID").toString();
				Map<String, Object> map = dao.getBill(tbId);
				JSONObject json=new JSONObject();
				json.put("ldbh", Id);
//				json.put("djsbm", "");
				json.put("wfjsdwjsrq", map.get("actiondate"));  //yyyy-mm-dd hh:mm:ss
				json.put("wfjsdwclyj", map.get("myStatus"));
//				json.put("sfczzdcy", "");
//				json.put("wfjsdwclyjbz", "");
				
				JSONArray arr = new JSONArray();
				List<Map<String,Object>> list = dao.getBillList(tbId);
				for(Map<String ,Object> item : list){
					JSONObject tmp = new JSONObject();
					tmp.put("fwmc", item.get("d_name"));
					tmp.put("fwdm", item.get("samll_category_id"));
					tmp.put("lyczfs", item.get("handle_type").toString().substring(0,item.get("handle_type").toString().indexOf("-")));
					tmp.put("jssl", item.get("unit_num"));
					arr.put(tmp);
				}
				json.put("fwsz", arr);
				Object[] result = this.myClient(json.toString(), "saveSnldJY",url);
				dao.saveFlow(Id, tbId, "saveSnldJY", result[0].toString());
				System.out.println("saveSnld返回码：" + result[0].toString());
				controller.setAttr("result",result[0].toString());
			}
		}else{
			controller.setAttr("result","tbId为空");
		}	
	}
	
	private void getXkz() throws Exception{
		String str = controller.getPara("jsonParam") == null || "".equals(controller.getPara("jsonParam")) ? "{}" : controller.getPara("jsonParam").toString();
		JSONObject json=new JSONObject();
		if(controller.getPara("version") != null && "2".equals(controller.getPara("version"))){
			net.sf.json.JSONObject jsonObject_param=net.sf.json.JSONObject.fromObject(str);
			if(jsonObject_param.get("xzqh") != null && !"".equals(jsonObject_param.get("xzqh"))){
				json.put("sxzqhdm", jsonObject_param.get("xzqh"));
			}
			json.put("dwmc", jsonObject_param.get("dwmc"));
			json.put("xkzbh", jsonObject_param.get("xkzh"));
			controller.getResponse().setHeader("Access-Control-Allow-Origin", "*");
		}else{
			str = myDecoder(str);
			net.sf.json.JSONObject jsonObject_param=net.sf.json.JSONObject.fromObject(str);
			if(jsonObject_param.get("xzqh") != null && !"".equals(jsonObject_param.get("xzqh"))){
				json.put("sxzqhdm", jsonObject_param.get("xzqh"));
			}
			json.put("dwmc", jsonObject_param.get("dwmc"));
			json.put("xkzbh", jsonObject_param.get("xkzh"));
		}
		Object[] result = this.myClient(json.toString(), "getXkz",KSTranServiceUrl);
		String res = result[0].toString();
		@SuppressWarnings("unchecked")
		List<Map<String,Object>> list = JsonMyKit.parse(res, List.class);
		JSONArray arr = new JSONArray();
		for(Map<String,Object> map : list){
			JSONObject tmp = new JSONObject();
			tmp.put("dwmc", map.get("dwmc"));
			tmp.put("dwdz", map.get("dwdz"));
			tmp.put("lxrxm", map.get("lxrxm"));
			tmp.put("lxrdh", map.get("lxrdh"));
			tmp.put("xkzbh", map.get("xkzbh"));
			tmp.put("xzqhdm",map.get("szqx"));
			arr.put(tmp);
		}
//		System.out.println(arr.toString());
		controller.renderJson(arr.toString());
	}
	
	private void getXkzfw() throws Exception{
		String str = controller.getPara("jsonParam") == null || "".equals(controller.getPara("jsonParam")) ? "{}" : controller.getPara("jsonParam").toString();
		JSONObject json=new JSONObject();	
		str = myDecoder(str);
		net.sf.json.JSONObject jsonObject_param=net.sf.json.JSONObject.fromObject(str);
		json.put("xkzbh", jsonObject_param.get("xkzh"));
		Object[] result = this.myClient(json.toString(), "getXkz",KSTranServiceUrl);
		String res = result[0].toString();
		@SuppressWarnings("unchecked")
		List<Map<String,Object>> list = JsonMyKit.parse(res, List.class);
//		System.out.println( list.get(0).get("hzjylb"));
		controller.renderJson( list.get(0).get("hzjylb"));
	}
	
	private void getYrKsld() throws Exception{
		String str = controller.getPara("jsonParam") == null || "".equals(controller.getPara("jsonParam")) ? "{}" : controller.getPara("jsonParam").toString();
		JSONObject json=new JSONObject();	
		str = myDecoder(str);
		net.sf.json.JSONObject jsonObject_param=net.sf.json.JSONObject.fromObject(str);
		if(jsonObject_param.get("zyldbh") != null && !"".equals(jsonObject_param.get("zyldbh"))){
			json.put("ksldbh", jsonObject_param.get("zyldbh"));
		}
		if(jsonObject_param.get("wfycdwmc") != null && !"".equals(jsonObject_param.get("wfycdwmc"))){
			json.put("wfycdwmc", jsonObject_param.get("wfycdwmc"));
		}
		if(jsonObject_param.get("sfbj") != null && !"".equals(jsonObject_param.get("sfbj"))){
			json.put("bjzt", jsonObject_param.get("sfbj"));
		}
		json.put("sxzqhdm", "120000");
		json.put("kssj", "2019-01-01");
		json.put("zzsj", "2025-01-01");
		Object[] result = this.myClient(json.toString(), "getYrKsld",KSTranServiceUrl);
		String res = result[0].toString();
//		System.out.println(res);
		@SuppressWarnings("unchecked")
		List<Map<String,Object>> list = JsonMyKit.parse(res, List.class);
		JSONArray arr = new JSONArray();
		for(Map<String,Object> map : list){
			JSONObject tmp = new JSONObject();
			tmp.put("ldbh", map.get("ksldbh"));
			tmp.put("wfycrq", map.get("wfycrq"));
			tmp.put("zyjhbh", map.get("zysqclid"));
			tmp.put("ycsxzqhdm", map.get("ycsxzqhdm"));
			tmp.put("wfycdwbm", map.get("qyid"));
			tmp.put("wfycdwmc", map.get("wfycdwmc"));
			tmp.put("BTO_ID_CS", map.get("ycsxzqhdm"));
			tmp.put("wfycdwdz", map.get("wfycdwdz"));
			tmp.put("fwycdwlxrsj", map.get("fwycdwlxrsj"));
			tmp.put("wfycdwlxr", map.get("wfycdwlxr"));
			
			tmp.put("ysdwmc", map.get("ysdwmc"));
			tmp.put("ysdwdz", map.get("ysdwdz"));
			tmp.put("ysdwlxr", map.get("ysdwlxr"));
			tmp.put("ysdwlxrsj", map.get("ysdwlxrsj"));
			tmp.put("ysdwdlyszh", map.get("ysdwdlyszh"));
			tmp.put("tjds", map.get("tjds"));
			
			tmp.put("fwjsdwwxfwjyxkzh", map.get("jsdwxkz"));
			tmp.put("REGISTERCODE", map.get("jsdwxkz"));
			tmp.put("yrsxzqhdm", map.get("yrsxzqhdm"));
			tmp.put("BTO_ID_CZ", map.get("yrsxzqhdm"));
			tmp.put("wfjsdwmc", map.get("wfjsdwmc"));
			tmp.put("wfjsdz", map.get("wfjsdz"));
			tmp.put("wfjsdwlxrsj", map.get("wfjsdwlxrsj"));
			tmp.put("wfjsdwlxr", map.get("wfjsdwlxr"));
			tmp.put("wfjsdwjsrq", map.get("wfjsdwjsrq"));
			tmp.put("wfjsdwclyj", map.get("wfjsdwclyj"));
			JSONArray arr2 = new JSONArray();
			@SuppressWarnings("unchecked")
			List<Map<String,Object>> fwsz = (List<Map<String, Object>>) map.get("fwsz");
			for(Map<String,Object> one : fwsz){
				JSONObject tmp2 = new JSONObject();
				tmp2.put("wxfwmc", one.get("fwmc"));
				tmp2.put("wxfwdm", one.get("fwdm"));
				tmp2.put("zysl", one.get("zysl"));
				tmp2.put("lyczfs", one.get("lyczfs"));
				tmp2.put("jldw", one.get("jldw"));
				tmp2.put("jssl",  one.get("jssl"));
				arr2.put(tmp2);
			}
			tmp.put("wxfw", arr2);	
			arr.put(tmp);
		}
//		System.out.println(arr.toString());
		controller.renderJson(arr.toString());
	}
	
	private void getYcKsld() throws Exception{
		String str = controller.getPara("jsonParam") == null || "".equals(controller.getPara("jsonParam")) ? "{}" : controller.getPara("jsonParam").toString();
		JSONObject json=new JSONObject();	
		str = myDecoder(str);
		net.sf.json.JSONObject jsonObject_param=net.sf.json.JSONObject.fromObject(str);
		if(jsonObject_param.get("kszyldbh") != null && !"".equals(jsonObject_param.get("kszyldbh"))){
			json.put("ksldbh", jsonObject_param.get("kszyldbh"));
		}
		json.put("sxzqhdm", "120000");
		json.put("kssj", "2019-01-01");
		json.put("zzsj", "2025-01-01");
		Object[] result = this.myClient(json.toString(), "getYcKsld",KSTranServiceUrl);
		String res = result[0].toString();
//		System.out.println(res);
		@SuppressWarnings("unchecked")
		List<Map<String,Object>> list = JsonMyKit.parse(res, List.class);
		JSONArray arr = new JSONArray();
		for(Map<String,Object> map : list){
			JSONObject tmp = new JSONObject();
			tmp.put("ldbh", map.get("ksldbh"));
			tmp.put("wfycrq", map.get("wfycrq"));
			tmp.put("zyjhbh", map.get("zysqclid"));
			tmp.put("ycsxzqhdm", map.get("ycsxzqhdm"));
			tmp.put("wfycdwbm", map.get("qyid"));
			tmp.put("wfycdwmc", map.get("wfycdwmc"));
			tmp.put("BTO_ID_CS", map.get("ycsxzqhdm"));
			tmp.put("wfycdwdz", map.get("wfycdwdz"));
			tmp.put("fwycdwlxrsj", map.get("fwycdwlxrsj"));
			tmp.put("wfycdwlxr", map.get("wfycdwlxr"));
			
			tmp.put("ysdwmc", map.get("ysdwmc"));
			tmp.put("ysdwdz", map.get("ysdwdz"));
			tmp.put("ysdwlxr", map.get("ysdwlxr"));
			tmp.put("ysdwlxrsj", map.get("ysdwlxrsj"));
			tmp.put("ysdwdlyszh", map.get("ysdwdlyszh"));
			tmp.put("tjds", map.get("tjds"));
			
			tmp.put("fwjsdwwxfwjyxkzh", map.get("jsdwxkz"));
			tmp.put("REGISTERCODE", map.get("jsdwxkz"));
			tmp.put("yrsxzqhdm", map.get("yrsxzqhdm"));
			tmp.put("BTO_ID_CZ", map.get("yrsxzqhdm"));
			tmp.put("wfjsdwmc", map.get("wfjsdwmc"));
			tmp.put("wfjsdz", map.get("wfjsdz"));
			tmp.put("wfjsdwlxrsj", map.get("wfjsdwlxrsj"));
			tmp.put("wfjsdwlxr", map.get("wfjsdwlxr"));
			tmp.put("wfjsdwjsrq", map.get("wfjsdwjsrq"));
			tmp.put("wfjsdwclyj", map.get("wfjsdwclyj"));
			JSONArray arr2 = new JSONArray();
			@SuppressWarnings("unchecked")
			List<Map<String,Object>> fwsz = (List<Map<String, Object>>) map.get("fwsz");
			for(Map<String,Object> one : fwsz){
				JSONObject tmp2 = new JSONObject();
				tmp2.put("wxfwmc", one.get("fwmc"));
				tmp2.put("wxfwdm", one.get("fwdm"));
				tmp2.put("zysl", one.get("zysl"));
				tmp2.put("lyczfs", one.get("lyczfs"));
				tmp2.put("jldw", one.get("jldw"));
				tmp2.put("jssl",  one.get("jssl"));
				arr2.put(tmp2);
			}
			tmp.put("wxfw", arr2);	
			arr.put(tmp);
		}
//		System.out.println(arr.toString());
		controller.renderJson(arr.toString());
	}
	
	@SuppressWarnings("unchecked")
	private void saveZyld() throws Exception{
		String str = controller.getPara("jsonParam") == null || "".equals(controller.getPara("jsonParam")) ? "{}" : controller.getPara("jsonParam").toString();
		JSONObject json=new JSONObject();	
		str = myDecoder(str);
//		net.sf.json.JSONObject jsonObject_param=net.sf.json.JSONObject.fromObject(str);
		Map<String,Object> jsonObject_param = JsonMyKit.parse(str, Map.class);
		json.put("wfycrq", jsonObject_param.get("wfycrq")+" 00:00:00");
		json.put("ycsxzqhdm", jsonObject_param.get("ycsxzqhdm"));
		json.put("wfycdwmc", jsonObject_param.get("wfycdwmc"));
		json.put("wfycdwdz", jsonObject_param.get("wfycdwdz"));
		json.put("wfycdwlxr", jsonObject_param.get("wfycdwlxr"));
		json.put("fwycdwlxrsj", jsonObject_param.get("fwycdwlxrsj"));
		json.put("ysdwmc", jsonObject_param.get("ysdwmc"));
		json.put("ysdwdz", jsonObject_param.get("ysdwdz"));
		json.put("ysdwlxr", jsonObject_param.get("ysdwlxr"));
		json.put("ysdwlxrsj", jsonObject_param.get("ysdwlxrsj"));
		json.put("ysdwdlyszh", jsonObject_param.get("ysdwdlyszh"));
		json.put("yrsxzqhdm", jsonObject_param.get("yrsxzqhdm"));
		json.put("jsdwxkz", jsonObject_param.get("fwjsdwwxfwjyxkzh"));
		json.put("wfjsdwmc", jsonObject_param.get("wfjsdwmc"));
		json.put("wfjsdz", jsonObject_param.get("wfjsdz"));
		json.put("wfjsdwlxr", jsonObject_param.get("wfjsdwlxr"));
		json.put("wfjsdwlxrsj", jsonObject_param.get("wfjsdwlxrsj"));
		List<Map<String,Object>> list = (List<Map<String, Object>>) jsonObject_param.get("wxfw");
		JSONArray arr = new JSONArray();
		for(Map<String,Object> map:list){
			JSONObject tmp = new JSONObject();
			tmp.put("zysl", map.get("zysl"));
			tmp.put("jldw", map.get("jldw"));
			tmp.put("fwmc", map.get("wxfwmc"));
			tmp.put("fwdm", map.get("wxfwdm"));
			tmp.put("fwlb", map.get("wxfwdm").toString().substring(map.get("wxfwdm").toString().length()-2));
			tmp.put("xt", "S");
			tmp.put("bz", "无");
			tmp.put("bzsl", "0");
			tmp.put("lyczfs", "无");
			arr.put(tmp);
		}
		json.put("fwsz", arr);
//		System.out.println(json.toString());
		Object[] result = this.myClient(json.toString(), "saveKsld",KSTranServiceUrl);
		String res = result[0].toString();
//		System.out.println(res);
		controller.renderJson(res);
	}
	
	private void saveKsldJy() throws Exception{
		String str =  "{}" ;
		if(controller.getPara("jsonParam") != null && !"".equals(controller.getPara("jsonParam"))){
			str =  controller.getPara("jsonParam").toString() ;
		}
		str = myDecoder(str);
//		System.out.println(str);
		if(!"".equals(str)){
			@SuppressWarnings("unchecked")
			Map<String,Object> map = JsonMyKit.parse(str, Map.class);
			JSONObject tmp = new JSONObject();
			tmp.put("ksldbh", map.get("ldbh"));
			tmp.put("wfjsdwjsrq", map.get("wfjsdwjsrq")+" 00:00:00");
			tmp.put("wfjsdwclyj", map.get("wfjsdwclyj"));
			@SuppressWarnings("unchecked")
			List<Map<String,Object>> wxfw = (List<Map<String, Object>>) map.get("wxfw");
			JSONArray arr2 = new JSONArray();
			for(Map<String,Object> map2:wxfw){
				JSONObject tmp2 = new JSONObject();
				tmp2.put("fwmc", map2.get("wxfwmc"));
				tmp2.put("fwdm", map2.get("wxfwdm"));
				tmp2.put("lyczfs", map2.get("lyczfs"));
				tmp2.put("jssl", map2.get("jssl"));
				arr2.put(tmp2);
			}
			tmp.put("fwsz", arr2);	
//			System.out.println("==============");
//			System.out.println(tmp);
			Object[] result = this.myClient(tmp.toString(), "saveKsldJy",KSTranServiceUrl);
			System.out.println("saveKsldJy返回码：" + result[0].toString());
			if(result[0].toString().equals(map.get("ldbh"))){
//				controller.setAttr("result","01");
				controller.renderJson("01");
			}else{
				controller.renderJson("2");
			}
		}else{
			controller.renderJson("3");
//			controller.setAttr("result","jsonParam为空");
		}	
	}
	
	private void indexbywotimer() throws Exception{
		String str =  "{}" ;
		String method = "";
		String wsdl = "";
		if(controller.getPara("jsonParam") != null && !"".equals(controller.getPara("jsonParam"))){
			str =  controller.getPara("jsonParam").toString() ;
		}
		if(controller.getPara("method") != null && !"".equals(controller.getPara("method"))){
			method =  controller.getPara("method").toString() ;
		}
		if(controller.getPara("url") != null && !"".equals(controller.getPara("url"))){
			wsdl =  controller.getPara("url").toString() ;
		}
		System.out.println(str);
		System.out.println(method);
		System.out.println(wsdl);
		Object[] result = this.myClient(str, method,wsdl);
		String res = result[0].toString();
		System.out.println(method+"=>返回码：" + res);
		controller.renderJson(res);
	}
	
	private void indexForCors() throws Exception{
		String str =  "{}" ;
		String method = "";
		String wsdl = "";
		String TP_ID = "";
		String EP_ID = "";
		if(controller.getMyParam("jsonParam") != null && !"".equals(controller.getMyParam("jsonParam"))){
			str =  controller.getMyParam("jsonParam").toString() ;
		}
		if(controller.getMyParam("method") != null && !"".equals(controller.getMyParam("method"))){
			method =  controller.getMyParam("method").toString() ;
		}
		if(controller.getMyParam("url") != null && !"".equals(controller.getMyParam("url"))){
			wsdl =  controller.getMyParam("url").toString() ;
		}
		System.out.println(str);
		System.out.println(method);
		System.out.println(wsdl);
		Object[] result = this.myClient(str, method,wsdl);
		String res = result[0].toString();
		System.out.println(method+"=>返回码：" + res);
		if(method.equals("saveKsldSq") || method.equals("saveKsldSqHf") || method.equals("saveKsldSqSp")){
			if(controller.getMyParam("TP_ID") != null && !"".equals(controller.getMyParam("TP_ID"))){
				TP_ID =  controller.getMyParam("TP_ID").toString() ;
			}
			if(controller.getMyParam("EP_ID") != null && !"".equals(controller.getMyParam("EP_ID"))){
				EP_ID =  controller.getMyParam("EP_ID").toString() ;
			}
			dao.saveSYNCUPLOADFLOWPT(TP_ID, EP_ID, res, method);
		}
		controller.renderJsonForCorsSimple(res);
	}
	
	private void applySyncPlan() throws Exception{
		String str =  "{}" ;
		String method = "";
		String wsdl = "";
		if(controller.getMyParam("jsonParam") != null && !"".equals(controller.getMyParam("jsonParam"))){
			str =  controller.getMyParam("jsonParam").toString() ;
		}
		if(controller.getMyParam("method") != null && !"".equals(controller.getMyParam("method"))){
			method =  controller.getMyParam("method").toString() ;
		}
		if(controller.getMyParam("url") != null && !"".equals(controller.getMyParam("url"))){
			wsdl =  controller.getMyParam("url").toString() ;
		}
		System.out.println(str);
		System.out.println(method);
		System.out.println(wsdl);
		Object[] result = this.myClient(str, method,wsdl);
		String res = result[0].toString();
		System.out.println(method+"=>返回码：" + res);
		if(controller.getMyParam("zysqclid") != null && !"".equals(controller.getMyParam("zysqclid"))){
			dao.saveSYNCUPLOADFLOWPT(res,controller.getMyParam("zysqclid").toString(),method);
		}
		if(res.length()>5){
			controller.setAttr("msg", "成功");
		}else{
			controller.setAttr("msg", "失败,错误码:"+res);
		}
	}
	
	private String myDecoder(String json) throws Exception{
//		System.out.println("sendMessage json before=========>"+json);
		sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
		byte[] bt = decoder.decodeBuffer(json); 
		json =  new String(bt, "UTF-8");
//		System.out.println("getMessage json=========>"+json);
		return json;
	}
	
	private Object[] myClient(String reqSNStr,String methodName,String purl) throws Exception{
		String encSNStr = "";
    	encSNStr = EncriptUtil.encrypt(reqSNStr, key);
		Client client = new Client(new URL(purl));
		Object[] result = client.invoke(methodName, new Object[] {key, encSNStr }); 
		return result;
	}
	
	private void queryPlanByEpName(){
//		String epName = controller.getMyParam("epName").toString();
		List<Map<String,Object>> planList = dao.queryPlanByEpName();
		controller.setAttr("planList", planList == null ? "" : planList);
	}
	
	@SuppressWarnings("unchecked")
	private void queryPlanById() throws Exception{
		String str =  "{}" ;
		String method = "";
		String wsdl = "";
		if(controller.getMyParam("jsonParam") != null && !"".equals(controller.getMyParam("jsonParam"))){
			str =  controller.getMyParam("jsonParam").toString() ;
		}
		if(controller.getMyParam("method") != null && !"".equals(controller.getMyParam("method"))){
			method =  controller.getMyParam("method").toString() ;
		}
		if(controller.getMyParam("url") != null && !"".equals(controller.getMyParam("url"))){
			wsdl =  controller.getMyParam("url").toString() ;
		}
		System.out.println(str);
		System.out.println(method);
		System.out.println(wsdl);
		Object[] result = this.myClient(str, method,wsdl);
		String res = result[0].toString();
		net.sf.json.JSONObject resJson=net.sf.json.JSONObject.fromObject(res.subSequence(1, res.length()-1));
		Map<String,Object> initPtInfo = new HashMap<String, Object>();
		Map<String,Object> initPt = new HashMap<String, Object>();
		initPt.put("zysqclid", resJson.get("zysqclid"));
		initPt.put("EN_ID_CS", "");
		initPt.put("TP_ID", "");
		initPt.put("LINKMAN", resJson.get("wfjsdwlxr"));
		initPt.put("LINKPHONE", resJson.get("wfjsdwlxrsj"));
		initPt.put("wfjsdwlxr", resJson.get("wfjsdwlxr"));
		initPt.put("wfjsdwlxrsj", resJson.get("wfjsdwlxrsj"));
		initPt.put("wfjsdwmc",  resJson.get("wfjsdwmc"));
		initPt.put("wfjsdz",  resJson.get("wfjsdz"));
		initPt.put("fwjsdwwxfwjyxkzh", resJson.get("jsdwxkzbh"));
		initPt.put("fwycdwlxrsj", resJson.get("fwycdwlxrsj"));
		initPt.put("jhqrsxzqh", "");
		initPt.put("jsrq",  resJson.get("jsrq"));
		initPt.put("ksrq",  resJson.get("ksrq"));
		initPt.put("sqrq", resJson.get("resJson"));
		
		
		initPt.put("wfycdwbm", "");
		initPt.put("wfycdwdz", "");
		initPt.put("wfycdwlxr", "");
		initPt.put("wfycdwmc", "");
		initPt.put("ycsxzqhdm", "");
		initPt.put("yrsxzqhdm", "");
		initPt.put("ysdwdlyszh", "");
		initPt.put("ysdwdz", "");
		initPt.put("ysdwlxr", "");
		initPt.put("ysdwlxrsj", "");
		initPt.put("ysdwmc", "");
		initPtInfo.put("initPt", initPt);
		List<Map<String,Object>> initPtList = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> fwsz = (List<Map<String, Object>>) resJson.get("fwsz");
		Map<String,Object> fwsz_one = new HashMap<String, Object>();
		for(Map<String,Object> one : fwsz){
			fwsz_one.clear();
			fwsz_one.put("BIG_CATEGORY_ID", "HW"+one.get("fwlb"));
			fwsz_one.put("BIG_CATEGORY_NAME", "");
			fwsz_one.put("D_NAME", one.get("fwmc"));
			fwsz_one.put("UNIT_NUM", one.get("zysl"));
			fwsz_one.put("UNIT", one.get("jldw"));
			fwsz_one.put("SAMLL_CATEGORY_ID", one.get("fwdm"));
			fwsz_one.put("wxfwmc", one.get("fwmc"));
			fwsz_one.put("zysl", one.get("zysl"));
			fwsz_one.put("jldw", one.get("jldw"));
			fwsz_one.put("wxfwdm", one.get("fwdm"));
			initPtList.add(fwsz_one);
		}
		initPtInfo.put("initPtList", initPtList);
		List<Map<String,Object>> initPtInfoList = new ArrayList<Map<String,Object>>();
		initPtInfoList.add(initPtInfo);
		controller.setAttr("initPtInfoList", initPtInfoList);
	}
}
