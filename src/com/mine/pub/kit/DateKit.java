package com.mine.pub.kit;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.jfinal.kit.StrKit;

/**
 * @author woody
 * @date 2015-3-13
 * @时间日期工具类
 * 懒汉式单例类.在第一次调用的时候实例化
 * */
public class DateKit {
	private static DateKit dateKit = null;
	public static String dateFormat = "yyyy-MM-dd";
	public static String timeFormat = "yyyy-MM-dd HH:mm:ss";
	private DateKit(){}
	public synchronized static DateKit getInstance()
	{
		if(dateKit == null)
		{
			dateKit = new DateKit();
		}
		return dateKit;
	}
	/**
	 * @author woody
	 * @date 2015-3-13
	 * @注释：格式化时间转成字符串
	 * @2015-01-01 8:15:00  -> 20150101081500
	 * */
	public static String getTimestamp(Timestamp timestamp)
	{
		String time = timestamp.toString();
		//for sqlServer
		int index = time.indexOf(".");
		if(index > 0){
			time = time.substring(0, index);
		}
		return (time).replace("-", "").replace(":", "").replace(".0", "").replace(" ", "");
	}
	public static void setDateFromat(String dateFormat) {
		if (StrKit.isBlank(dateFormat))
			throw new IllegalArgumentException("dateFormat can not be blank.");
		DateKit.dateFormat = dateFormat;
	}
	
	public static void setTimeFromat(String timeFormat) {
		if (StrKit.isBlank(timeFormat))
			throw new IllegalArgumentException("timeFormat can not be blank.");
		DateKit.timeFormat = timeFormat;
	}
	
	public static Date toDate(String dateStr) {
		throw new RuntimeException("Not finish!!!");
	}
	
	public static Date toDate(String dateStr,String format) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(format);  
	    Date date = sdf.parse(dateStr);
	    return date;
	}
	
	public static String toStr(Date date) {
		return toStr(date, DateKit.dateFormat);
	}
	
	public static String toStr(Date date, String format) {
		if(date == null){
			return "";
		}
		DateFormat df = new SimpleDateFormat(format);
		return df.format(date);
	}
	/**
	 * @author woody
	 * @date 2015-6-13
	 * @获取时间差
	 * */
	public static Map<String, Object> getTimeDiff(Date begin,Date end)
	{
		long between=(end.getTime()-begin.getTime())/1000;//除以1000是为了转换成秒
		long day=between/(24*3600);
		long hour=between%(24*3600)/3600;
		long minute=between%3600/60;
		long second=between%60/60;
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("day", day);
		map.put("hour",hour);
		map.put("minute",minute);
		map.put("second",second);
		return map;
	}

}
