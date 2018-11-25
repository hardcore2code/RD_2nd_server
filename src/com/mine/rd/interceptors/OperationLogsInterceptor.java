package com.mine.rd.interceptors;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
/**
 * @author woody
 * @date 20160323
 * @业务日志拦截器
 * */
public class OperationLogsInterceptor implements Interceptor
{
//	private static final Log log = Log.getLog(OperationLogsInterceptor.class);
	@Override
	public void intercept(Invocation inv) {
		// TODO Auto-generated method stub
		System.out.println("inv.getMethodName=======>>"+inv.getMethodName());
		inv.invoke();
	}
}
