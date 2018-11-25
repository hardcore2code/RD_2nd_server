package com.mine.run;

import com.jfinal.core.JFinal;
import com.mine.run.config.InitConfig;

/**
 * @author woody
 * @date 20160205
 * @启动项目
 * */
public class Run extends InitConfig
{
	/**
	 * 建议使用 JFinal 手册推荐的方式启动项目
	 * 运行此 main 方法可以启动项目，此main方法可以放置在任意的Class类定义中，不一定要放于此
	 */
	public static void main(String[] args) {
		JFinal.start("WebContent", 9001, "/", 5);
	}
}
