package com.mine.rd.services.demo.pojo;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.mine.pub.kit.StrKit;
import com.mine.pub.pojo.BaseDao;

public class DemoDao extends BaseDao{


	public String test()
	{
		return Db.queryStr("select name from demo ");
	}
	public Map<String, Object> testmap()
	{
		return Db.find("select name from demo ").get(0).getColumns();
	}
	public String getMenuList(String userId)
	{
		return StrKit.toDTstr(super.roleMenu(userId));
	}
	public void saveUploadFlow(String tbid){
		Record record = new Record();
		record.set("tbid", tbid);
		record.set("status", "0");
		record.set("sysdate", getSysdate());
		Db.save("UPLOAD_INTERFACE_FLOW_2018", record);
	}
	public void updateUploadFlow(String tbid,String status){
		Db.update("update UPLOAD_INTERFACE_FLOW_2018 set status = ? where tbid =? ",status,tbid);
	}
	public String getTbids()
	{
		List<Record> list = Db.find("select tbid from UPLOAD_INTERFACE_FLOW_2018 where status = '0' ");
		if(list != null && list.size() >0)
		{
			StringBuffer sb = new StringBuffer();
			for(Record record : list){
				sb.append("'");
				sb.append(record.getStr("tbid"));
				sb.append("',");
			}
			return sb.toString().substring(0,sb.toString().length()-1);
		}else{
			return "";
		}
		
	}
	public List<Record> queryBillList(String tbids)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("select tb_id,en_id_cs,en_id_cz,en_name_cs,");
		sb.append("(select '天津市'+n.dictname+sb_adress_j from ENTERPRISE m,EOS_DICT_ENTRY n where m.ep_id = t.en_id_cs and n.dicttypeid='city_q' and m.sb_adress_q = n.dictid) as 'csdz',");
		sb.append("case (select n.belong_bh from ENTERPRISE m,B_ORGAN n where m.ep_id = t.en_id_cs and m.BELONG_SEPA = n.bto_id ) ");
		sb.append("when  1 then '120116'");
		sb.append("else (select n.id from ENTERPRISE m,REGION_CODE n where m.ep_id = t.en_id_cs and m.BELONG_SEPA = n.bto_id ) ");
		sb.append("end as 'regcodecs',");
		sb.append("case when en_id_ys in ('EP2015001','EP2015002','EP2015003') ");
		sb.append("then (select '天津市'+n.dictname+sb_adress_j from ENTERPRISE m,EOS_DICT_ENTRY n where m.ep_id = t.en_id_cs and n.dicttypeid='city_q' and m.sb_adress_q = n.dictid) ");
		sb.append("else (select '天津市'+n.dictname+sb_adress_j from ENTERPRISE m,EOS_DICT_ENTRY n where m.ep_id = t.en_id_ys and n.dicttypeid='city_q' and m.sb_adress_q = n.dictid)  ");
		sb.append("end as 'ysdz', ");
		sb.append("(select '天津市'+n.dictname+sb_adress_j from ENTERPRISE m,EOS_DICT_ENTRY n where m.ep_id = t.en_id_cz and n.dicttypeid='city_q' and m.sb_adress_q = n.dictid) as 'czdz', ");
		sb.append("case (select n.belong_bh from ENTERPRISE m,B_ORGAN n where m.ep_id = t.en_id_cz and m.BELONG_SEPA = n.bto_id )  ");
		sb.append("when 1 then '120116' ");
		sb.append("else (select n.id from ENTERPRISE m,REGION_CODE n where m.ep_id = t.en_id_cz and m.BELONG_SEPA = n.bto_id )  ");
		sb.append("end as 'regcodecz', ");
		sb.append("(select m.LICENSE_NO from HANDLE_LICENSE m where m.ep_id = t.en_id_cz ) as 'licenseNo', ");
		sb.append("(select PERMITNUM from car_info m where m.cardno = t.carcard ) as 'carxkz', ");
		sb.append("en_name_ys,en_name_cz,csy,ysy,czy,CONVERT(varchar(12),sysdate, 23) 'mydate', ");
		sb.append("case when status = '04' then '不同意接收' else '同意接收' end as 'myStatus'  ");
		sb.append("from TRANSFERPLAN_BILL t   ");
		sb.append("where 1=1  ");
		sb.append("and tb_id in ("+tbids+") ");
		List<Record> list = Db.find(sb.toString());
		return list;
	}
	public List<Record> queryBillSonList(String tbId)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("select t.d_name,t.big_category_id,t.samll_category_id,t.unit_num,t.unit ");
		sb.append("from TRANSFERPLAN_BILL_LIST t  ");
		sb.append("where tb_id = '"+tbId+"' ");
		List<Record> list = Db.find(sb.toString());
		return list;
	}
	public void saveCounterFlow(Map<String,int[]> map)
	{
		Record record = null;
		Timestamp time = super.getSysdate();
		for (Map.Entry<String,int[]> entry : map.entrySet())
		{
			record = new Record();
			record.set("STATUS", entry.getKey());
			record.set("COUNT", entry.getValue()[0]);
			record.set("sysdate", time);
			Db.save("UPLOAD_INTERFACE_FLOW", record);
		}
	}
	/**
	 * @author woody
	 * @date 2017-08-23
	 * @根据处置单位，大类，小类给出 处置方式
	 * */
	public String getHandleMode(String epId,String epIdFrom,String BIG_CATEGORY_ID,String SAMLL_CATEGORY_ID,String unit)
	{
		String handleModeCode = "";
		if("个".equals(unit))
		{
			//天津市雅环再生资源回收利用有限公司 单位是个 D16
			if("EP201508141009471304".equals(epId))
			{
				handleModeCode = "D16";
			}
			else
			{
				handleModeCode = "C3";
			}
		}
		else
		{
			//天津市雅环再生资源回收利用有限公司 单位是个 D16
			if("EP201508141009471304".equals(epId))
			{
				handleModeCode = "R9";
			}
			//天津华庆百胜能源有限公司  一律为 S
			if("EP201602251339212631".equals(epId))
			{
				handleModeCode = "S";
			}
			//天津市海丰化工有限公司  一律为 R6
			if("EP201410281233290032".equals(epId))
			{
				handleModeCode = "R6";
			}
			//天津壹鸣环境污染治理有限公司 一律为 D10
			if("EP201607110955513122".equals(epId))
			{
				handleModeCode = "D10";
			}
			//中海油能源发展股份有限公司安全环保分公司碧海环保服务公司 一律为 D10
			if("EP201410280908450011".equals(epId))
			{
				handleModeCode = "D10";
			}
			//天津东邦铅资源再生有限公司 一律为 R4
			if("EP201410281131110030".equals(epId))
			{
				handleModeCode = "R4";
			}
			//泰鼎(天津）环保科技有限公司 一律为 R4
			if("EP201410291352330074".equals(epId))
			{
				handleModeCode = "R4";
			}
			//天津昱隆泰再生资源环保处理有限公司 一律为 R3
			if("EP201410280946090018".equals(epId))
			{
				handleModeCode = "R3";
			}
			//天津仁新玻璃材料有限公司 一律为 R4
			if("EP201410291732000084".equals(epId))
			{
				handleModeCode = "R4";
			}
			//天津江源环保科技有限公司 一律为 R6
			if("EP201410281429170043".equals(epId))
			{
				handleModeCode = "R6";
			}
			//天津友信材料科技有限公司 一律为 R6
			if("EP201501071338330387".equals(epId))
			{
				handleModeCode = "R6";
			}
			//天津市腾源环保科技有限公司 一律为 R6
			if("EP201606031641393014".equals(epId))
			{
				handleModeCode = "R6";
			}
			//天津莱奥西斯环保科技有限公司 一律为 R9
			if("EP201707251144524521".equals(epId))
			{
				handleModeCode = "R9";
			}
			//德鸿泰（天津）环保科技有限公司 一律为 R4
			if("EP201611221430103497".equals(epId))
			{
				handleModeCode = "R4";
			}
			//中能（天津）环保再生资源利用有限公司 
			if("EP201512021411072294".equals(epId))
			{
				if("900-045-49".equals(SAMLL_CATEGORY_ID))
				{
					handleModeCode = "S";
				}
				else
				{
					handleModeCode = "R4";
				}
			}
			//天津合佳威立雅环境服务有限公司
			if("EP201410280910450012".equals(epId))
			{
				//天津滨海合佳威立雅环境服务有限公司产生的
				if("EP201411031103580169".equals(epIdFrom))
				{
					handleModeCode = "D1";
				}else
				{
					if("HW34".equals(BIG_CATEGORY_ID) || "HW35".equals(epIdFrom))
					{
						handleModeCode = "D9";
					}
					else{
						handleModeCode = "D10";
					}
				}
				
			}
			//天津滨海合佳威立雅环境服务有限公司
			if("EP201411031103580169".equals(epId))
			{
				if("HW34".equals(BIG_CATEGORY_ID) || "HW35".equals(epIdFrom))
				{
					handleModeCode = "D9";
				}
				else{
					handleModeCode = "D10";
				}
			}
		}
		return handleModeCode;
	}
}
