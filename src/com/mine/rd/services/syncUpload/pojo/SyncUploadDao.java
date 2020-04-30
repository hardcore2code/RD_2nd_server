package com.mine.rd.services.syncUpload.pojo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.mine.pub.kit.DateKit;
import com.mine.pub.pojo.BaseDao;

public class SyncUploadDao extends BaseDao {
	
	public Map<String,Object> getBill(String tbId){
		StringBuffer sb = new StringBuffer();
		sb.append("select t.tb_id,t.en_id_cs,t.en_id_ys,t.en_id_cz,t.en_name_cs, ");
		sb.append("(select '天津市'+n.dictname+sb_adress_j from ENTERPRISE m,EOS_DICT_ENTRY n where m.ep_id = t.en_id_cs and n.dicttypeid='city_q' and m.sb_adress_q = n.dictid) as 'csdz', ");
//		sb.append("case (select n.belong_bh from ENTERPRISE m,B_ORGAN n where m.ep_id = t.en_id_cs and m.BELONG_SEPA = n.bto_id ) ");
//		sb.append("when  1 then '120116' ");
		sb.append(" (select n.id from ENTERPRISE m,Z_SYNC_REGION_CODE n where m.ep_id = t.en_id_cs and m.BELONG_SEPA = n.bto_id ) ");
		sb.append(" as 'regcodecs', ");
		sb.append("case when t.en_id_ys in ('EP2015001','EP2015002','EP2015003') ");
		sb.append("then (select '天津市'+n.dictname+sb_adress_j from ENTERPRISE m,EOS_DICT_ENTRY n where m.ep_id = t.en_id_cs and n.dicttypeid='city_q' and m.sb_adress_q = n.dictid) ");
		sb.append("else (select '天津市'+n.dictname+sb_adress_j from ENTERPRISE m,EOS_DICT_ENTRY n where m.ep_id = t.en_id_ys and n.dicttypeid='city_q' and m.sb_adress_q = n.dictid) ");
		sb.append("end as 'ysdz', ");
//		sb.append("case (select n.belong_bh from ENTERPRISE m,B_ORGAN n where m.ep_id = t.en_id_ys and m.BELONG_SEPA = n.bto_id )  ");
//		sb.append("when 1 then '120116' ");
		sb.append(" (select n.id from ENTERPRISE m,Z_SYNC_REGION_CODE n where m.ep_id = t.en_id_ys and m.BELONG_SEPA = n.bto_id ) ");
		sb.append(" as 'regcodeys', ");
		sb.append("(select '天津市'+n.dictname+sb_adress_j from ENTERPRISE m,EOS_DICT_ENTRY n where m.ep_id = t.en_id_cz and n.dicttypeid='city_q' and m.sb_adress_q = n.dictid) as 'czdz', ");
//		sb.append("case (select n.belong_bh from ENTERPRISE m,B_ORGAN n where m.ep_id = t.en_id_cz and m.BELONG_SEPA = n.bto_id )  ");
//		sb.append("when 1 then '120116' ");
		sb.append(" (select n.id from ENTERPRISE m,Z_SYNC_REGION_CODE n where m.ep_id = t.en_id_cz and m.BELONG_SEPA = n.bto_id )  ");
		sb.append(" as 'regcodecz', ");
		sb.append("(select m.LICENSE_NO from HANDLE_LICENSE m where m.ep_id = t.en_id_cz ) as 'licenseNo', ");
		sb.append("t.en_name_ys,t.en_name_cz,t.csy,t.ysy,t.czy,CONVERT(varchar(12),t.sysdate, 23) 'mydate',CONVERT(varchar(20),t.actiondate, 20) 'actiondate', ");
		sb.append("case when t.status = '04' then '拒收' else '接收' end as 'myStatus' , ");
		sb.append("b.linkman , b.link_num,c.ys_zz ");
		sb.append("from TRANSFERPLAN_BILL t ,TRANSFER_PLAN a, Z_WOBO_EP_EXTEND b,Z_WOBO_TRANSFER_YS c ");
		sb.append("where t.TP_ID = a.TP_ID and a.MAIN_ID = b.TP_ID and a.main_id = c.tp_id and t.EN_ID_YS = c.EN_ID_YS and tb_id = '");
		sb.append(tbId);
		sb.append("'");
		Record record = Db.findFirst(sb.toString());
		return record == null ? null : record.getColumns();
	}
	
	public Map<String,Object> getBillInfo(String tbId){
		Record record = Db.findFirst("select * from TRANSFERPLAN_BILL a, CAR_INFO b where a.CARCARD=b.cardno and tb_id = ? ",tbId);
		return record == null ? null : record.getColumns();
	}
	
	public List<Map<String,Object>> getBillList(String tbId){
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		StringBuffer sb = new StringBuffer();
		sb.append("select b.d_name,b.big_category_id,b.samll_category_id,b.unit_num,b.unit, d.w_shape ,d.character,e.handle_type ");
		sb.append("from TRANSFERPLAN_BILL a , TRANSFERPLAN_BILL_LIST b , TRANSFER_PLAN c , Z_WOBO_OVERVIEWLIST d , Z_WOBO_HANDLE_LIST e ");
		sb.append("where a.TB_ID = b .TB_ID and a.TP_ID = c.TP_ID and c.MAIN_ID = d.TP_ID and b.D_NAME = d.D_NAME and b.BIG_CATEGORY_ID = d.BIG_CATEGORY_ID  ");
		sb.append("and c.MAIN_ID = e.TP_ID and a.EN_ID_CZ = e.EN_ID_CZ and b.D_NAME = e.D_NAME and b.BIG_CATEGORY_ID = e.BIG_CATEGORY_ID ");
		sb.append("and  a.tb_id = ? ");
		List<Record> records = Db.find(sb.toString(),tbId);
		for(Record record : records){
			list.add(record.getColumns());
		}
		return list;
	}
	
	public Map<String,Object> getEnterprise(String epId){
		Record record = Db.findFirst("select linkman,tel from ENTERPRISE where ep_id = ?",epId);
		return record == null ? null : record.getColumns(); 
	}
	
	public String getId(){
		String year = Db.queryStr("select DATENAME(yyyy, GETDATE())");
		return year + getId("Z_SYNC_UPLOAD_FLOW");
	}
	
	public Map<String,Object> getFlow(String tbId,String method){
		Record record = Db.findFirst("select * from Z_SYNC_UPLOAD_FLOW where tb_id = ? and method = ? ",tbId,method);
		return record == null ? null : record.getColumns();
	}
	
	public void saveFlow(String id,String tbId,String method,String result){
		Db.update("delete from Z_SYNC_UPLOAD_FLOW where tb_Id = ? and id = ? and method = ? ",tbId,id,method);
		Record record = new Record();
		record.set("TB_ID", tbId);
		record.set("ID", id);
		record.set("method", method);
		record.set("result", result);
		record.set("sysdate", getSysdate());
		Db.save("Z_SYNC_UPLOAD_FLOW", record);
	}
	
	public List<Map<String,Object>> queryPlanByEpName(){
//		List<Record> records = Db.find("select * from Z_WOBO_PLAN_MAIN where ep_name like '%"+epName+"%' ");
		List<Record> records = Db.find("select distinct a.* from Z_WOBO_PLAN_MAIN a,Z_WOBO_TRANSFER_PLAN_PT b where a.tp_id = b.tp_id and a.status = '05' ");
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for(int i = 0; i<records.size() ; i++){
			records.get(i).set("PLAN_YEAR", DateKit.toStr(records.get(i).getDate("BEGINDATE"), "yyyy"));
			list.add(records.get(i).getColumns());
		}
		return list;
	}
	
	public Map<String,Object> queryPlanById(String id){
		Record record = Db.findFirst("select * from Z_SYNC_UPLOAD_FLOW_PT where ID= ?  ",id);
		return record == null ? null : record.getColumns();
	}
	
	public void saveSYNCUPLOADFLOWPT(String tpId,String epId,String id,String method){
		Db.update("delete from Z_SYNC_UPLOAD_FLOW_PT where TP_ID = ? and id = ? and method =? ",tpId,id,method);
		Record record = new Record();
		record.set("TP_ID", tpId);
		record.set("ID", id);
		record.set("method", method);
		record.set("result", id);
		record.set("sysdate", getSysdate());
		record.set("EP_ID", epId);
		Db.save("Z_SYNC_UPLOAD_FLOW_PT", record);
	}
	
	public void saveSYNCUPLOADFLOWPT(String res,String id,String method){
		Record old = Db.findFirst("select * from Z_SYNC_UPLOAD_FLOW_PT where method = 'saveKsldSq' and id = ? ",id);
		String tpId = "";
		String epId = "";
		if(old != null && old.get("TP_ID") != null){
			tpId = old.get("TP_ID").toString();
			epId = old.get("EP_ID").toString();
		}
		Db.update("delete from Z_SYNC_UPLOAD_FLOW_PT where id = ? and method =? ",id,method);
		Record record = new Record();
		record.set("TP_ID", tpId);
		record.set("ID", id);
		record.set("method", method);
		record.set("result", res);
		record.set("sysdate", getSysdate());
		record.set("EP_ID", epId);
		Db.save("Z_SYNC_UPLOAD_FLOW_PT", record);
	}
	
	public String getQyid(String epId) {
		Record one = Db.findFirst("select qyid from Z_SYNC_EP where ep_id = ? ",epId);
		return one == null ? "" : one.getStr("qyid");
	}
	
	public String getQyidByName(String epname) {
		Record one = Db.findFirst("select b.qyid from ENTERPRISE a, Z_SYNC_EP b where a.EP_ID = b.EP_ID and a.EP_NAME = ? ",epname);
		return one == null ? "" : one.getStr("qyid");
	}
	
	public String getZysqclid (String csname,String czname){
		String str = "select b.id ";
		str += "from Z_WOBO_TRANSFER_PLAN_PT a,Z_SYNC_UPLOAD_FLOW_PT b ";
		str += "where a.TP_ID = b.TP_ID and b.method = 'saveKsldSq' and len(b.id) > 10 ";
		str += "and a.EN_NAME_CS = ? and a.EN_NAME_CZ = ? ";
		str += "order by b.sysdate desc  ";
		Record one = Db.findFirst(str,csname,czname);
		return one == null ? "" : one.getStr("id");
	}
	
	public String getXz(String bigId,String smallId){
		Record one = Db.findFirst("select character from z_wobo_overviewlist where big_category_id = ? and samll_category_id =? ",bigId,smallId);
		return one == null ? "T" : one.getStr("character").substring(0,1);
	}
}
