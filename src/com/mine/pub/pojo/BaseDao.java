package com.mine.pub.pojo;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.json.Jackson;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.ehcache.CacheKit;
import com.mine.pub.kit.DESKit;
import com.mine.pub.kit.DateKit;
import com.mine.pub.kit.DecryptKit;
import com.mine.pub.kit.StrKit;
import com.mine.pub.proc.ProcForQuery;

/**
 * @author woody
 * @date 20160214
 * @dao公用类
 * */
public abstract class BaseDao 
{
	/**
	 * @author woody
	 * @date 2015-3-26
	 * @将list转为符合datetable的字符串
	 * */
	public String toDTstr(List<Record> list)
	{
		List<Map<String,Object>> listMap = new ArrayList<Map<String,Object>>();
		for(Record record : list)
		{
			listMap.add(record.getColumns());
		}
		return StrKit.toDTstr(listMap);
	}

	/**
	 * @author woody
	 * @date 2015-3-13
	 * @param 表名
	 * @return 前缀+时间戳+seq
	 * */
	public String getSeqId(String tablename)
	{
		Record record = Db.findFirst("select * from PUB_SEQ where TABLENAME=?", tablename);
		String seq = "";
		if(record.getInt("NUM") == record.getInt("MAXNUM"))
		{
			seq = "1";
		}else
		{
			int addres = record.getInt("NUM") + record.getInt("ADDNUM");
			seq = addres + "";
		}
		Db.update("update PUB_SEQ set num = ? where TABLENAME = ?", Integer.parseInt(seq) , tablename);
		while(seq.length()<4)
		{
			seq = "0"+seq;
		}
		return record.getStr("ID") + DateKit.getTimestamp(getSysdate()) + seq;
	}

	/**
	 * @author woody
	 * @date 20160215
	 * @获取时间戳
	 * */
	public Timestamp getSysdate()
	{
		//postgresql
//		return Db.queryTimestamp("select current_timestamp(0)::timestamp without time zone ");
		//sqlServer
		return Db.queryTimestamp("select getdate()");
	}
	
	/**
	 * @date 20160330
	 * @param id---用户名或课程id
	 * @param categoryType---等级类型：0-小学，1-初中，2-高中
	 * @param type---所属类型：4-教师，5-学生，6-课程
	 * @return true-成功；false-失败
	 * @保存用户或课程的等级类型
	 */
	public boolean save(String id, String categoryType, String type){
		Record record = new Record();
		record.set("id", id);
		record.set("category_type", categoryType);
		record.set("type", type);
		return Db.save("os_user_course_type", "id", record);
	}
	
	/**
     * @author jiang
     * @date 20160407		
     * @插入日志
     * */
	public void logManagement (String userId,String content)
	{
		Record record = new Record();
		record.set("log_id", getSeqId("os_log"));
		record.set("user_id", userId);
		record.set("content", content);
		record.set("describe", "");
		record.set("sysdate", getSysdate());
		record.set("status", 1);
		Db.save("os_log", "log_id", record);
	}
	/**
     * @author weizanting
     * @date 20160615		
     * @插入消息提醒
     * */
	public void saveMessage (String userId, String content, String title, String from, String type, String url, String parameter)
	{
		Record record = new Record();
		record.set("message_id", getSeqId("os_message"));
		record.set("user_id", userId);
		record.set("message_content", content);
		record.set("receive_time", getSysdate());
		record.set("status", 1);
		record.set("sysdate", getSysdate());
		record.set("message_title", title);
		record.set("message_from", from);
		record.set("read_status", 0);
		record.set("type", type);
		record.set("url", url);
		record.set("parameter", parameter);
		Db.save("os_message", "message_id", record);
	}
	
	/**
	 * @author woody
	 * @throws UnsupportedEncodingException 
	 * @date 20151010
	 * @保存数据库的session表
	 * @先删后插
	 * @删除原有记录
	 * */
	public void updateSession(String sessionId,String key,String obj) 
	{
		Db.update("delete from iwobo_session where SESSION_ID = ? and IWB_KEY = ?", sessionId,key);
		Db.update("insert into iwobo_session values (?,?,?,?)",sessionId,key,obj,getSysdate());
	}
	
	/**
     * @author weizanting
     * @date 20160615		
     * @更新cache中的内容
     * type: 1-更新课程/直播课程  2-更新学校
     * */
	public void updateCache(String type, int pageSize)
	{
		if("1".equals(type)){
			CacheKit.remove("mydict", "course1");
			Db.paginateByCache("mydict", "course1", 1, pageSize, "select distinct occ.course_id,oc.course_name,oc.course_logo,os.school_id,os.school_name,oc.create_id,ou.user_name,ss.subject_id,ss.subject_name,ouct.category_type,oc.pub_time", "from os_course oc,os_course_catalog occ,os_school os,os_subject ss,os_user ou,os_grade og,os_school_course osc,os_user_course_type ouct where oc.course_id=occ.course_id and osc.course_id=oc.course_id and osc.school_id=os.school_id and oc.create_id=ou.user_id and oc.subject_id=ss.subject_id and og.grade_id=oc.grade_id and oc.course_id=ouct.id and oc.status='1' and occ.status='1' and osc.status='1' and os.status='1' and ou.status in ('1','3') and ss.status='1' and og.status='1' and ouct.type='6' and oc.course_status='4' and oc.online_status='1' order by oc.pub_time desc");
			CacheKit.remove("mydict", "liveCourse1");
//			Db.paginateByCache("mydict", "liveCourse1", 1, pageSize, "select distinct *", "from os_course oc,os_school_course osc,os_subject ss,os_school os,os_user ou,os_school_user osu,os_course_catalog occ,os_school_subject oss,os_grade og,os_school_grade osg,os_user_course_type ouct where oc.course_id=osc.course_id and oc.subject_id=ss.subject_id and osc.school_id=os.school_id and oc.create_id=ou.user_id and osu.school_id=os.school_id and osu.user_id=ou.user_id and occ.course_id=oc.course_id and oss.school_id=os.school_id and oss.subject_id=ss.subject_id and og.grade_id=oc.grade_id and og.grade_id=osg.grade_id and osg.school_id=os.school_id and ouct.id=oc.course_id and oc.status='1' and osc.status='1' and ss.status='1' and os.status='1' and ou.status='1' and osu.status='1' and occ.status='1' and oss.status='1' and og.status='1' and osg.status='1' and occ.type = '0' and oc.course_status='4' and oc.online_status='1' order by occ.live_time desc");
//			Db.paginateByCache("mydict", "liveCourse1", 1, pageSize, "select distinct A.course_id,A.course_name,A.course_logo,A.school_id,A.school_name,A.create_id,A.user_name,A.subject_id,A.subject_name,A.pub_time,A.category_type ", "from (select distinct occ.course_id,oc.course_name,oc.course_logo,os.school_id,os.school_name,oc.create_id,ou.user_name,ss.subject_id,ss.subject_name,oc.pub_time,ouct.category_type,og.grade_id from os_course oc,os_school_course osc,os_subject ss,os_school os,os_user ou,os_school_user osu,os_course_catalog occ,os_grade og,os_user_course_type ouct where oc.course_id=osc.course_id and oc.subject_id=ss.subject_id and osc.school_id=os.school_id and oc.create_id=ou.user_id and osu.school_id=os.school_id and osu.user_id=ou.user_id and occ.course_id=oc.course_id and og.grade_id=oc.grade_id and ouct.id=oc.course_id and oc.status='1' and osc.status='1' and ss.status='1' and os.status='1' and ou.status='1' and osu.status='1' and occ.status='1' and og.status='1' and occ.type = '0' and oc.course_status='4' and oc.online_status='1') as A,os_school_subject oss,os_school_grade osg where oss.school_id=A.school_id and oss.subject_id=A.subject_id and oss.status='1' and A.grade_id=osg.grade_id and osg.school_id=A.school_id and osg.status='1'");
//			Db.paginateByCache("mydict", "liveCourse1", 1, pageSize, "select *", "from (select distinct on(A.course_id) course_id,A.course_name,A.course_logo,A.school_id,A.school_name,A.create_id,A.user_name,A.subject_id,A.subject_name,A.pub_time,A.category_type,A.sort,A.type,A.live_time,A.live_time_end from ((select distinct on(occ.course_id) occ.course_id,oc.course_name,oc.course_logo,os.school_id,os.school_name,oc.create_id,ou.user_name,ss.subject_id,ss.subject_name,oc.pub_time,ouct.category_type,occ.live_time,og.grade_id,'0' as sort,occ.type,occ.live_time_end from os_course oc,os_school_course osc,os_subject ss,os_school os,os_user ou,os_school_user osu,os_course_catalog occ,os_grade og,os_user_course_type ouct where oc.course_id=osc.course_id and oc.subject_id=ss.subject_id and osc.school_id=os.school_id and oc.create_id=ou.user_id and osu.school_id=os.school_id and osu.user_id=ou.user_id and occ.course_id=oc.course_id and og.grade_id=oc.grade_id and ouct.id=oc.course_id and oc.status='1' and osc.status='1' and ss.status='1' and os.status='1' and ou.status='1' and osu.status='1' and occ.status='1' and og.status='1' and occ.type = '0' and oc.course_status='4' and oc.online_status='1' and occ.live_time_end > ? order by occ.course_id,occ.live_time asc) union (select distinct on(occ.course_id) occ.course_id,oc.course_name,oc.course_logo,os.school_id,os.school_name,oc.create_id,ou.user_name,ss.subject_id,ss.subject_name,oc.pub_time,ouct.category_type,occ.live_time,og.grade_id,'1' as sort,occ.type,occ.live_time_end from os_course oc,os_school_course osc,os_subject ss,os_school os,os_user ou,os_school_user osu,os_course_catalog occ,os_grade og,os_user_course_type ouct where oc.course_id=osc.course_id and oc.subject_id=ss.subject_id and osc.school_id=os.school_id and oc.create_id=ou.user_id and osu.school_id=os.school_id and osu.user_id=ou.user_id and occ.course_id=oc.course_id and og.grade_id=oc.grade_id and ouct.id=oc.course_id and oc.status='1' and osc.status='1' and ss.status='1' and os.status='1' and ou.status='1' and osu.status='1' and occ.status='1' and og.status='1' and occ.type = '0' and oc.course_status='4' and oc.online_status='1' order by occ.course_id,occ.live_time asc)) as A,os_school_subject oss,os_school_grade osg where oss.school_id=A.school_id and oss.subject_id=A.subject_id and oss.status='1' and A.grade_id=osg.grade_id and osg.school_id=A.school_id and osg.status='1' order by A.course_id,A.live_time,A.sort asc) as B order by sort,live_time asc", Timestamp.valueOf(DateKit.toStr(new Date(), "yyyy-MM-dd HH:mm:ss" + ".0"))).getList();
			Db.paginateByCache("mydict", "liveCourse1", 1, pageSize, "select B.*", "from ((select A.* from (select distinct on (occ.course_id) occ.course_id,oc.course_name,oc.course_logo,os.school_id,os.school_name,oc.create_id,ou.user_name,ss.subject_id,ss.subject_name,oc.pub_time,ouct.category_type,occ.live_time,og.grade_id,occ.type,occ.live_time_end,os.school_category,case when to_char(now(), 'yyyyMMdd HH24:MI:SS') < to_char(occ.live_time, 'yyyyMMdd HH24:MI:SS') then to_char(now(), '1') when to_char(now(), 'yyyyMMdd HH24:MI:SS') > to_char(occ.live_time_end, 'yyyyMMdd HH24:MI:SS') then to_char(now(), '2') else '0' end as live_status from os_course oc,os_school_course osc,os_subject ss,os_school os,os_user ou,os_school_user osu,os_course_catalog occ,os_grade og,os_user_course_type ouct where oc.course_id=osc.course_id and oc.subject_id=ss.subject_id and osc.school_id=os.school_id and oc.create_id=ou.user_id and osu.school_id=os.school_id and osu.user_id=ou.user_id and occ.course_id=oc.course_id and og.grade_id=oc.grade_id and ouct.id=oc.course_id and oc.status='1' and osc.status='1' and ss.status='1' and os.status='1' and ou.status in ('1','3') and osu.status='1' and occ.status='1' and og.status='1' and occ.type = '0' and oc.course_status='4' and oc.online_status='1' order by occ.course_id,occ.live_time desc) as A where A.live_status in ('0','1') order by A.live_time asc) union all (select C.* from (select distinct on (occ.course_id) occ.course_id,oc.course_name,oc.course_logo,os.school_id,os.school_name,oc.create_id,ou.user_name,ss.subject_id,ss.subject_name,oc.pub_time,ouct.category_type,occ.live_time,og.grade_id,occ.type,occ.live_time_end,os.school_category,case when to_char(now(), 'yyyyMMdd HH24:MI:SS') < to_char(occ.live_time, 'yyyyMMdd HH24:MI:SS') then to_char(now(), '1') when to_char(now(), 'yyyyMMdd HH24:MI:SS') > to_char(occ.live_time_end, 'yyyyMMdd HH24:MI:SS') then to_char(now(), '2') else '0' end as live_status from os_course oc,os_school_course osc,os_subject ss,os_school os,os_user ou,os_school_user osu,os_course_catalog occ,os_grade og,os_user_course_type ouct where oc.course_id=osc.course_id and oc.subject_id=ss.subject_id and osc.school_id=os.school_id and oc.create_id=ou.user_id and osu.school_id=os.school_id and osu.user_id=ou.user_id and occ.course_id=oc.course_id and og.grade_id=oc.grade_id and ouct.id=oc.course_id and oc.status='1' and osc.status='1' and ss.status='1' and os.status='1' and ou.status in ('1','3') and osu.status='1' and occ.status='1' and og.status='1' and occ.type = '0' and oc.course_status='4' and oc.online_status='1' order by occ.course_id,occ.live_time desc) as C where C.live_status='2' order by C.live_time desc)) as B,os_school_subject oss,os_school_grade osg where oss.school_id=B.school_id and oss.subject_id=B.subject_id and oss.status='1' and B.grade_id=osg.grade_id and osg.school_id=B.school_id and osg.status='1'").getList();
		}else if("2".equals(type)){
			CacheKit.remove("mydict", "schoolList1");
			Db.paginateByCache("mydict", "schoolList1", 1, pageSize, "select aa.*, count(bb.*) teacher_num", "from os_school aa left join (select distinct(b.user_id), b.school_id from os_school_user b, os_user c, os_course d where b.user_id = c.user_id and b.user_id = d.create_id and d.status = '1' and c.type = ? and b.status = '1' and c.status = '1' ) as bb on aa.school_id = bb.school_id where aa.status = '1' group by aa.school_id order by teacher_num desc", "4");
		}
	}
	/**
	 * @author woody
	 * @date 2015-3-18
	 * @根据权限查询菜单列表
	 * */
	@SuppressWarnings("unchecked")
	public static List<Map<String,Object>> roleMenu(String userId)
	{
		Object[] objs = new Object[1];
		objs[0] = userId;
		ProcForQuery proc = new ProcForQuery("queryFuncs", objs);
		return (List<Map<String, Object>>) Db.execute(proc);
	}
	
	/**
	 * @author ouyangxu
	 * @date 2017-4-20
	 * @数据库区分大小写
	 * */
	public String checkLowerAndUpper(){
		return "collate Chinese_PRC_CS_AI";
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170512
	 * 方法：查询字典列表 for select
	 */
	public List<Map<String, Object>> queryDictForSelect(String name, String dictId, String dictValue){
		List<Record> lists = CacheKit.get("mydict", name);
		List<Map<String, Object>> returnList = new ArrayList<>();
		Map<String, Object> map = new HashMap<>();
		map.put(dictId, "0");
		map.put(dictValue, "请选择");
		returnList.add(map);
		if(lists != null){
			for(Record list : lists){
				map = new HashMap<>();
				map.put(dictId, list.get("dict_id"));
				map.put(dictValue, list.get("dict_value"));
				returnList.add(map);
			}
		}
		return returnList;
	}
	
	/**
	 * @author weizanting
	 * @date 20170512
	 * 方法：表中数据与字典表中数据转换
	 */
	public String convert(List<Record> list, Object field){
		String str = "";
		if(field != null && !"".equals(field)){
			if(list.size() > 0){
				for(int i = 0; i < list.size(); i ++){
					if(field.equals(list.get(i).get("dict_id"))){
						str = list.get(i).get("dict_value");
						break;
					}
				}
			}
		}
		return str;
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170602
	 * 方法：查询字典列表
	 */
	public List<Map<String, Object>> queryDict(String name, String dictId, String dictValue){
		List<Record> lists = CacheKit.get("mydict", name);
		List<Map<String, Object>> returnList = new ArrayList<>();
		if(lists != null){
			for(Record list : lists){
				Map<String, Object> map = new HashMap<>();
				map.put(dictId, list.get("dict_id"));
				map.put(dictValue, list.get("dict_value"));
				returnList.add(map);
			}
		}
		return returnList;
	}
	
	//按条件筛选查询字典列表
	public List<Map<String, Object>> queryDict(String name, String dictId, String dictValue, List<Object> objs){
		List<Record> lists = CacheKit.get("mydict", name);
		List<Map<String, Object>> returnList = new ArrayList<>();
		if(lists != null){
			for(Record list : lists){
				Map<String, Object> map = new HashMap<>();
				for(Object obj : objs){
					if(obj.equals(list.get("dict_id"))){
						map.put(dictId, list.get("dict_id"));
						map.put(dictValue, list.get("dict_value"));
						returnList.add(map);
					}
				}
			}
		}
		return returnList;
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170607
	 * 方法：查询用户是否可访问该菜单
	 */
	public boolean checkUserMenu(String userId, String currentUrl){
		List<Record> urls = Db.find("select e.A_ID from WOBO_USERROLE a,WOBO_ROLE b,WOBO_ROLEFUNC c,WOBO_FUNC d,WOBO_FUNC_ATTR e where a.USER_ID=? and a.ROLE_ID=b.ROLE_ID and b.ROLE_ID=c.ROLE_ID and c.FUNC_ID=d.FUNC_ID and d.FUNC_ID=e.FUNC_ID and a.STATUS='1' and b.STATUS='1' and c.STATUS='1' and d.STATUS='1'", userId);
		if(urls.size() > 0){
			for(Record url : urls){
				Object A_ID = url.get("A_ID");
				if(A_ID != null && !"".equals(A_ID)){
					if(currentUrl.contains(url.getStr("A_ID"))){
						return true;
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * @author ouyangxu
	 * @date 20170221
	 * 方法：用户注销
	 */
	public void logout(String sessionId){
		Db.update("delete from IWOBO_SESSION where SESSION_ID = ?", sessionId);
	}
	
	/**
	 * @author ouyangxu
	 * @throws Exception 
	 * @date 20170807
	 * 方法：获取token
	 */
    public String getToken() throws Exception{
		long loginToken = (this.getSysdate()).getTime();
		Map<String, Object> map = new HashMap<>();
		map.put("loginToken", loginToken);
		String WJWT = Jackson.getJson().toJson(map);
		//des加密
		String key = PropKit.get("codeKey");
		WJWT = DESKit.encrypt(WJWT,key);
		//base64传输
		WJWT = DecryptKit.encode(WJWT);
		return WJWT;
	}
}
