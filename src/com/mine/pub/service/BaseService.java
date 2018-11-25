package com.mine.pub.service;



import com.jfinal.json.Jackson;
import com.mine.pub.controller.BaseController;

/**
 * @author woody
 * @date 20160215
 * service 基本类
 * */
public abstract class BaseService extends ServiceImp
{
	public BaseController controller;
	private Object resObj;
	/**
	 * 构造方法
	 * set controller
	 * */
	public BaseService(BaseController controller)
	{
		this.controller = controller;
	}
	@Override
	public String toJson()
	{
		return Jackson.getJson().toJson(resObj);
	}
	public String getLastMethodName()
	{
		StackTraceElement[] temp=Thread.currentThread().getStackTrace();
		return temp[3].getMethodName();
	}
	public String getLastMethodName(int level)
	{
		StackTraceElement[] temp=Thread.currentThread().getStackTrace();
		return temp[level].getMethodName();
	}
	public Object getResObj() {
		return resObj;
	}
	public void setResObj(Object resObj) {
		this.resObj = resObj;
	}
}
