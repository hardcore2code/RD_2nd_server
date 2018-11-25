package com.mine.pub.kit;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
/**
 * @author woody
 * @date 20160308
 * */
public class MapKit 
{
	/**
	 * @author woody
	 * @date 20160308
	 * @string to map
	 * */
	public static Map<String,Object> strToMap(String mapString)
	{
		 Map<String, Object> map = new HashMap<String, 	Object>();  
		 java.util.StringTokenizer items;  
		 for(StringTokenizer entrys = new StringTokenizer(mapString, ",");
			 entrys.hasMoreTokens();   
			 map.put(items.nextToken(), items.hasMoreTokens() ? ((Object) (items.nextToken())) : null)
		 )
		 items = new StringTokenizer(entrys.nextToken().trim(), "=");  
		 return map;  
	}
}
