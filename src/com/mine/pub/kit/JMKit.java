package com.mine.pub.kit;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JMKit 
{
	public static String getJM(String str)
	{
		str = StrKit.getMi(str);
		char[] stringArr = str.toCharArray(); //注意返回值是char数组
		StringBuffer sb = new StringBuffer();
		java.util.Random random=new java.util.Random();
		for(char temp : stringArr)
		{
			int result=random.nextInt(10);
			if(result > 5)
			{
				sb.append(String.valueOf(temp).toUpperCase());
			}
			else
			{
				sb.append(temp);
			}
		}
		return sb.toString();
	}
	public static String getYuan(String val)
	{
		val = val.substring(16);
		return StrKit.getYuan(val);
	}
	public static boolean JM(String val,Timestamp ts)
	{
		String str = getYuan(val);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		boolean res = false;
		try {
			Date date = sdf.parse(str);
			Date sysdate = ts;
			if(sysdate.getTime() <= date.getTime())
			{	
				res = true;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return res;
	}
}
