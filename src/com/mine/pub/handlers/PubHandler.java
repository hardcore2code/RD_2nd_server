package com.mine.pub.handlers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jfinal.handler.Handler;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.Db;
import com.mine.pub.kit.JMKit;

/**
 * @author woody
 * @date 20160229
 * @公用 处理器
 * */
public class PubHandler extends Handler{

	@Override
	public void handle(String target, HttpServletRequest request,
			HttpServletResponse response, boolean[] isHandled) {
//		System.out.println("controller===============>>"+PropKit.get("controller"));
//		System.out.println("TEST MYDB===============>>"+Db.queryTimestamp("select current_timestamp(0)::timestamp without time zone "));
//		String mi = PropKit.get("controller");
//		System.out.println("22222222222TEST MYDB===============>>"+JMKit.getJM("2016-08-01 17:18:32"));
//		System.out.println("22222222222TEST MYDB===============>>"+JMKit.getYuan(mi));
//		System.out.println(JMKit.JM(PropKit.get("controller"), Db.queryTimestamp("select current_timestamp(0)::timestamp without time zone")));
		//postgresql
//		boolean flag = JMKit.JM(PropKit.get("controller"), Db.queryTimestamp("select now() "));
		//sqlServer
		boolean flag = JMKit.JM(PropKit.get("controller"), Db.queryTimestamp("select getdate() "));
		//对于/pub/loadDict 不予处理
		int index = target.indexOf("/pub/loadDict");
        if (index == -1 && flag) {
        	next.handle(target, request, response, isHandled);
        }
	}
}
