package com.mine.rd1.services.statistics.service;

import java.io.IOException;
import java.sql.SQLException;

import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.mine.pub.controller.BaseController;
import com.mine.pub.kit.DESKit;
import com.mine.pub.kit.DecryptKit;
import com.mine.pub.service.BaseService;
import com.mine.rd1.services.statistics.pojo.ExecuteSqlDao;

public class ExecuteSqlService extends BaseService {

	private ExecuteSqlDao dao = new ExecuteSqlDao();
	private String sql = "";

	public ExecuteSqlService(BaseController controller) {
		super(controller);
	}

	/**
	 * @author zyl
	 * @throws Exception
	 * @date 20170519 方法：执行语句
	 */
	private void executeSql() throws Exception {
		if (controller.getMyParam("sql") != null) {
			sql=controller.getMyParam("sql").toString();
			sql = DecryptKit.decode(sql);
			sql=DESKit.decrypt(sql,"IDWTDTD!@#$%");
			sql=sql.substring(1);
			controller.setAttrs(dao.executeSql(sql));
			controller.setAttr("resFlag", "0");
		} else {
			controller.setAttr("resFlag", "1");
			controller.setAttr("msg", "少传参数");
		}
	}
	
	/**
	 * @author zyl
	 * @throws Exception
	 * @date 获取执行SQL模块的加密key值
	 */
	private void getExeSqlCodeKey() throws Exception {
		controller.setAttr("exeSqlCodeKey", PropKit.get("exeSqlCodeKey"));
		controller.setAttr("resFlag", "0");
	}
	

	@Override
	public void doService() throws Exception {
		Db.tx(new IAtom() {
			@Override
			public boolean run() throws SQLException {
				try {
					if ("executeSql".equals(getLastMethodName(7))) {
						executeSql();
					}
					if ("getExeSqlCodeKey".equals(getLastMethodName(7))) {
						getExeSqlCodeKey();
					}
				} catch (Exception e) {
					e.printStackTrace();
					controller.setAttr("msg", "系统异常");
					controller.setAttr("resFlag", "1");
					return false;
				}
				return true;
			}
		});

	}

	public static void main(String[] args) throws IOException, Exception {
		System.out.println(DESKit.decrypt("0x9bfb25ec22f2fce0","IDWTDTD"));
	}
}
