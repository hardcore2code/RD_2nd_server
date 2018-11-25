package com.mine.pub.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class DictFilter implements Filter
{
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		System.out.println("DictFilter====>>init");
//		List<DictObject> list = DictObject.dao.find("select * from wobo_dict");
//		if(list != null)
//		{
//			for(DictObject dictObject : list)
//			{
//				CacheKit.put("mydict", dictObject.get("name"), dictObject.get("val"));
//			}
//		}
	}
	
	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		System.out.println("DictFilter====>>doFilter");
	}
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
}

