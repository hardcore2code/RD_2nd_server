package com.mine.run.config;

import cn.dreampie.quartz.QuartzPlugin;

import com.ext.plugin.scheduler.SchedulerPlugin;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.json.JacksonFactory;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.dialect.AnsiSqlDialect;
import com.jfinal.plugin.c3p0.C3p0Plugin;
import com.jfinal.plugin.ehcache.EhCachePlugin;
import com.jfinal.render.ViewType;
import com.mine.pub.handlers.PubHandler;
import com.mine.pub.handlers.websocket.WebSocketHandler;
import com.mine.rd.interceptors.CheckRoleMenuInterceptor;
import com.mine.rd.interceptors.CheckSessionInterceptor;
import com.mine.task.gis.CCTask;
import com.mine.task.gis.GisTask;
import com.mine.task.uploadInterface.UploadInterfaceTask;
/**
 * @author woody
 * @date 20160205
 * @初始化项目配置信息
 * */
public class InitConfig extends JFinalConfig
{
	/**
	 * 配置常量
	 */
	@Override
	public void configConstant(Constants me) {
		// 加载少量必要配置，随后可用PropKit.get(...)获取值
		PropKit.use("init.properties");
		me.setJsonFactory(new JacksonFactory());
		me.setDevMode(PropKit.getBoolean("devMode", false));
		me.setViewType(ViewType.JSP); 							// 设置视图类型为Jsp，否则默认为FreeMarker
		me.setEncoding("UTF-8");
	}
	/**
	 * 配置路由
	 */
	public void configRoute(Routes me) {
		me = RoutesSetting.setRoutes(me);
	}
	/**
	 * 配置处理器
	 */
	public void configHandler(Handlers me) {
		me.add(new PubHandler());
		me.add(new WebSocketHandler());
	}
	/**
	 * 配置全局拦截器
	 */
	@Override
	public void configInterceptor(Interceptors me) {
		//添加查询用户是否登录拦截器
		me.add(new CheckSessionInterceptor());
		//添加查询用户角色是否能使用对应菜单拦截器
		me.add(new CheckRoleMenuInterceptor());
//		me.add(new LoginInterceptor());
		//添加控制层全局拦截器
//		me.addGlobalActionInterceptor(new ExceptionIntoLogInterceptor());
//		me.addGlobalActionInterceptor(new OperationLogsInterceptor());
		//添加业务层全局拦截器
//		me.addGlobalServiceInterceptor(new ExceptionIntoLogInterceptor());
	}
	/**
	 * @author woody
	 * @date 20160229
	 * @启动后 to do
	 * */
	@Override
	public void afterJFinalStart(){
		if(PropKit.getBoolean("ifdb"))
		{
			InitCache.init();
		}
	};
	/**
	 * 配置插件
	 */
//  mysql
//	@Override
//	public void configPlugin(Plugins me) {
//		if(PropKit.getBoolean("ifdb"))
//		{
//			// 配置C3p0数据库连接池插件
//			C3p0Plugin C3p0Plugin = new C3p0Plugin(PropKit.get("jdbcUrl"), PropKit.get("user"), PropKit.get("password").trim());
//			me.add(C3p0Plugin);
//			me.add(new EhCachePlugin(PathKit.getRootClassPath()+"/wobocache.xml"));  //加载ehcache缓存
//			// 配置ActiveRecord插件
//			ActiveRecordPlugin arp = new ActiveRecordPlugin(C3p0Plugin);
//			me.add(arp);
//		}
//	}
	
	//sqlServer
	@Override
	public void configPlugin(Plugins me) {
		// 配置C3p0数据库连接池插件
		C3p0Plugin c3p0Plugin = new C3p0Plugin(PropKit.get("jdbcUrl"), PropKit.get("user").trim(), PropKit.get("password").trim(),PropKit.get("driver"));
		me.add(c3p0Plugin);
		me.add(new EhCachePlugin(PathKit.getRootClassPath()+"/wobocache.xml"));  //加载ehcache缓存
		// 配置ActiveRecord插件
		ActiveRecordPlugin arp = new ActiveRecordPlugin(c3p0Plugin);
		arp.setDialect(new AnsiSqlDialect());
		me.add(arp);
		SchedulerPlugin sp = new SchedulerPlugin();
		Runnable task = new UploadInterfaceTask();
//		Runnable taskGis = new GisTask();
//		Runnable ccTask = new CCTask();
//		sp.fixedDelaySchedule(task, 5);
//		sp.fixedRateSchedule(task, 10);
		sp.cronSchedule(task, PropKit.get("taskRule"));
//		sp.cronSchedule(taskGis, PropKit.get("taskGisRule"));
//		sp.cronSchedule(ccTask, PropKit.get("taskGisRule"));
		me.add(sp);
		QuartzPlugin quartzPlugin = new QuartzPlugin();
		quartzPlugin.setJobs("system-quartz.properties");
		me.add(quartzPlugin);
	}

}
