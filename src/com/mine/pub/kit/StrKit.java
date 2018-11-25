package com.mine.pub.kit;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
public class StrKit {
	/**
	 * 首字母变小写
	 */
	public static String firstCharToLowerCase(String str) {
		char firstChar = str.charAt(0);
		if (firstChar >= 'A' && firstChar <= 'Z') {
			char[] arr = str.toCharArray();
			arr[0] += ('a' - 'A');
			return new String(arr);
		}
		return str;
	}
	
	/**
	 * 首字母变大写
	 */
	public static String firstCharToUpperCase(String str) {
		char firstChar = str.charAt(0);
		if (firstChar >= 'a' && firstChar <= 'z') {
			char[] arr = str.toCharArray();
			arr[0] -= ('a' - 'A');
			return new String(arr);
		}
		return str;
	}
	
	/**
	 * 字符串为 null 或者为  "" 时返回 true
	 */
	public static boolean isBlank(String str) {
		return str == null || "".equals(str.trim()) ? true : false;
	}
	
	/**
	 * 字符串不为 null 而且不为  "" 时返回 true
	 */
	public static boolean notBlank(String str) {
		return str == null || "".equals(str.trim()) ? false : true;
	}
	
	public static boolean notBlank(String... strings) {
		if (strings == null)
			return false;
		for (String str : strings)
			if (str == null || "".equals(str.trim()))
				return false;
		return true;
	}
	
	public static boolean notNull(Object... paras) {
		if (paras == null)
			return false;
		for (Object obj : paras)
			if (obj == null)
				return false;
		return true;
	}
	
	/**
	 * @author woody
	 * @date 2015-3-26
	 * @将map转为符合datatable的字符串
	 * */
	public static String toDTstr(List<Map<String , Object>> list)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		for(Map<String , Object> map : list)
		{
			sb.append(toDTstr(map)).append(",");
		}
		if(sb.length() > 1)
		{
			sb.replace(sb.length()-1, sb.length(), "]");
		}
		else
		{
			sb.append("]");
		}
		return sb.toString();
	}
	@SuppressWarnings("unchecked")
	public static String toDTstr(Map<String, Object> map)
	{
		Iterator<Entry<String, Object>> iterator = map.entrySet().iterator();
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		while(iterator.hasNext())
		{
			Map.Entry<String, Object> entry = (Entry<String, Object>) iterator.next();
			String key = entry.getKey();
			Object value = entry.getValue() == null ? "" : entry.getValue();
			if("son".equals(key))
			{
				List<Map<String , Object>> list_son = (List<Map<String, Object>>) value;
				value = toDTstr(list_son);
				sb.append(key).append(":").append("\"").append(value).append("\"").append(",");
			}
			else
			{
				sb.append(key).append(":").append("'").append(value).append("'").append(",");
			}
		}
		sb.replace(sb.length()-1, sb.length(), "}");
		return sb.toString();
	}
	/**
	 * @author woody
	 * @date 20150613
	 * @格式化时间差
	 * */
	public static String formatTimeDiff(Map<String,Object> map)
	{
		String res = "";
		if(map.get("day") != null && !"".equals(map.get("day")) && Integer.parseInt(map.get("day").toString()) > 0 )
		{
			res = map.get("day")+"天";
			return res;
		}
		if(map.get("hour") != null && !"".equals(map.get("hour")) && Integer.parseInt(map.get("hour").toString()) > 0 )
		{
			res = map.get("hour")+"小时";
			return res;
		}
		if(map.get("minute") != null && !"".equals(map.get("minute")) && Integer.parseInt(map.get("minute").toString()) > 0 )
		{
			res = map.get("minute")+"分钟";
			return res;
		}
		if(map.get("second") != null && !"".equals(map.get("second")) && Integer.parseInt(map.get("second").toString()) > 0 )
		{
			res = map.get("day")+"刚刚";
			return res;
		}
		return res;
	}
	public static String getYuan(String val)
	{
		char[] stringArr = val.toCharArray(); //注意返回值是char数组
		StringBuffer sb = new StringBuffer();
		for(char temp : stringArr)
		{
			switch (temp) {
			case 'a':
				sb.append("1");
				break;
			case 'b':
				sb.append("2");
				break;
			case 'c':
				sb.append("3");
				break;
			case 'd':
				sb.append("4");
				break;
			case 'e':
				sb.append("5");
				break;
			case 'f':
				sb.append("6");
				break;
			case 'g':
				sb.append("7");
				break;
			case 'h':
				sb.append("8");
				break;
			case 'i':
				sb.append("9");
				break;
			case 'j':
				sb.append("0");
				break;
			case 'A':
				sb.append("1");
				break;
			case 'B':
				sb.append("2");
				break;
			case 'C':
				sb.append("3");
				break;
			case 'D':
				sb.append("4");
				break;
			case 'E':
				sb.append("5");
				break;
			case 'F':
				sb.append("6");
				break;
			case 'G':
				sb.append("7");
				break;
			case 'H':
				sb.append("8");
				break;
			case 'I':
				sb.append("9");
				break;
			case 'J':
				sb.append("0");
				break;
			default:
				break;
			}
		}
		return sb.toString();
	}
	public static String getMi(String val)
	{
		char[] stringArr = val.toCharArray(); //注意返回值是char数组
		StringBuffer sb = new StringBuffer();
		for(char temp : stringArr)
		{
			switch (temp) {
			case '1':
				sb.append("a");
				break;
			case '2':
				sb.append("b");
				break;
			case '3':
				sb.append("c");
				break;
			case '4':
				sb.append("d");
				break;
			case '5':
				sb.append("e");
				break;
			case '6':
				sb.append("f");
				break;
			case '7':
				sb.append("g");
				break;
			case '8':
				sb.append("h");
				break;
			case '9':
				sb.append("i");
				break;
			case '0':
				sb.append("j");
				break;
			default:
				break;
			}
		}
		return sb.toString();
	}
}
