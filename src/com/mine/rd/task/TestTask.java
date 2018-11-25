package com.mine.rd.task;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

public class TestTask implements Runnable {
	int i = 0;
	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("================task============================>>");
		if(i > 0)
		{
			Record record = Db.findFirst("select * from demo");
			System.out.println("task============================>>"+record.getStr("name"));
		}
		i++;
	}

}
