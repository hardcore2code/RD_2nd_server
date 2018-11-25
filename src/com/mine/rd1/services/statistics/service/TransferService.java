package com.mine.rd1.services.statistics.service;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.mine.pub.controller.BaseController;
import com.mine.pub.kit.DateKit;
import com.mine.pub.kit.DecryptKit;
import com.mine.pub.kit.DictKit;
import com.mine.pub.service.BaseService;
import com.mine.rd1.services.statistics.pojo.TransferDao;

public class TransferService extends BaseService {

	private TransferDao dao = new TransferDao();
	private String id = "";

	public TransferService(BaseController controller) {
		super(controller);
	}

	/**
	 * @author zyl
	 * @throws Exception
	 * @date 20170519 方法：app查询转移联单信息 参数：id-转移联单编号 返回：entityData
	 */
	private void queryTransferplanBill() throws Exception {
		controller.doIWBSESSION();
		if (controller.getMyParam("idByCt") != null) {
			id = DecryptKit.decode(DecryptKit.convertHexToString(controller
					.getMyParam("idByCt").toString()));
			id = id.substring(10, 30);
			System.out.println("id==========" + id);
			controller.setAttrs(dao.queryTransferplanBill(id));
			controller.setAttr("resFlag", "0");
		} else {
			controller.setAttr("resFlag", "1");
			controller.setAttr("msg", "少传参数");
		}
	}
	
	/**
	 * @author zyl
	 * @throws Exception
	 * @date 20170519 方法：app查询人员卡的人员类型
	 *  参数：交接人员卡编号 
	 *  返回：entityData
	 */
	private void queryPerson() throws Exception {
		controller.doIWBSESSION();
		if (controller.getMyParam("cardNo") != null) {
			id = controller.getMyParam("cardNo").toString();
			System.out.println("id==========" + id);
			controller.setAttrs(dao.queryPerson(id));
			controller.setAttr("resFlag", "0");
		} else {
			controller.setAttr("resFlag", "1");
			controller.setAttr("msg", "少传参数");
		}
	}

	/**
	 * @author zyl
	 * @date 20170519 方法：app修改转移联单状态
	 */
	private void updateStatus() {
		controller.doIWBSESSION();
		try {
			if (controller.getMyParam("id") != null
					&& controller.getMyParam("ysy") != null
					&& controller.getMyParam("ysc") != null) {
				id = controller.getMyParam("id").toString();
				Map<String, Object> queryTransferplanBill = dao
						.queryTransferplanBill(id);
				queryTransferplanBill=(Map<String, Object>)queryTransferplanBill.get("entityData");
				String res = "";
				if (queryTransferplanBill == null) {
					controller.setAttr("res1", "02");
					return;
				}
				String csy = controller.getMyParam("csy") == null ? ""
						: controller.getMyParam("csy").toString();
				String ysy = controller.getMyParam("ysy") == null ? ""
						: controller.getMyParam("ysy").toString();
				String czy = controller.getMyParam("czy") == null ? ""
						: controller.getMyParam("czy").toString();
				String ysc = controller.getMyParam("ysc") == null ? ""
						: controller.getMyParam("ysc").toString();
				String reason = controller.getMyParam("reason") == null ? ""
						: controller.getMyParam("reason").toString();
				String ysd = controller.getMyParam("ysd") == null ? ""
						: controller.getMyParam("ysd").toString();
				String czd = controller.getMyParam("czd") == null ? ""
						: controller.getMyParam("czd").toString();
				String action = controller.getMyParam("action") == null ? ""
						: controller.getMyParam("action").toString();
				Map<String, Object> csPersonInfo = null;
				Map<String, Object> ysPersonInfo = null;
				Map<String, Object> czPersonInfo = null;
				Map<String, Object> carInfo = null;
				controller.setAttr("res1", id);
				if (!"".equals(csy)) {
					csPersonInfo = dao.queryPersonInfo(csy, null,
							"PERSON_INFO_CS").get(0);
					if (csPersonInfo == null) {
						controller.setAttr("res1", "03");
						return;
					}
				}
				if (!"".equals(ysy)) {
					ysPersonInfo = dao.queryPersonInfo(ysy, null,
							"PERSON_INFO_YS").get(0);
					if (ysPersonInfo == null) {
						controller.setAttr("res1", "03");
						return;
					}
				}
				if (!"".equals(czy)) {
					czPersonInfo = dao.queryPersonInfo(czy, null,
							"PERSON_INFO_CZ").get(0);
					if (czPersonInfo == null) {
						controller.setAttr("res1", "03");
						return;
					}
				}
				String reasonInfo = null;
				if (!"".equals(reason)) {
					Map<String, String> eosDicts = DictKit
							.getEosDicts("BILL_REASON");
					String[] split = reason.split(";");
					for (String string : split) {
						if (reasonInfo == null) {
							reasonInfo = eosDicts.get(string);
						} else {
							reasonInfo = eosDicts.get(string) + ";"
									+ reasonInfo;
						}
					}
				}
				if (!"".equals(ysc)) {
					carInfo = dao.queryCarInfo(ysc, null).get(0);
					if (carInfo == null) {
						controller.setAttr("res1", "10");
						return;
					}
				}
				Timestamp sysdate = dao.getSysdate();
				Date ysDate = null;
				Date czDate = null;
				if ("00".equals(action) || "01".equals(action)
						|| "02".equals(action)) {
					ysDate = disDate(ysd, sysdate);
				} else if ("03".equals(action) || "04".equals(action)
						|| "05".equals(action)) {
					czDate = disDate(czd, sysdate);
				}
				String status = "";
				Date ysdate = null;
				Date reasondate = null;
				Date jsdate = null;
				if ("00".equals(action)) {
					status = "09";
				}
				else if ("01".equals(action)) {
					status = "02";
					ysdate = ysDate;
				}
				else if ("02".equals(action)) {
					status = "05";
					reasondate = ysDate;
				}
				else if ("03".equals(action)) {
					status = "06";
				}
				else if ("04".equals(action)) {
					status = "03";
					jsdate = czDate;
				}
				else if ("05".equals(action)) {
					status = "04";
					reasondate = czDate;
				}
				res = checkStatus(queryTransferplanBill.get("status")
						.toString(), action);
				if ("".equals(res)) {
					res = checkYsUser(queryTransferplanBill, ysy, res);
					res = checkYsCar(queryTransferplanBill, ysc, res);
				}
				String upSql = "update TRANSFERPLAN_BILL  set ACTIONDATE='"
						+ sysdate + "' ";
				if ("".equals(res)) {
					if (!"".equals(csy)) {
						upSql = upSql + ",CSY='" + csPersonInfo.get("name")
								+ "',CSCARD='" + csy + "' ";
					}
					if (!"".equals(ysy)) {
						upSql = upSql + ",YSY='" + ysPersonInfo.get("name")
								+ "',YSCARD='" + ysy + "' ";
					}
					if (!"".equals(czy)) {
						upSql = upSql + ",CZY='" + czPersonInfo.get("name")
								+ "',CZCARD='" + czy + "' ";
					}
					if (!"".equals(status)) {
						upSql = upSql + ",STATUS='" + status + "' ";
					}
					if (jsdate != null) {
						upSql = upSql + ",JSDATE='" + DateKit.toStr(jsdate,"yyyy-MM-dd HH:mm:ss") + "' ";
					}
					if (ysdate != null) {
						upSql = upSql + ",YSDATE='" + DateKit.toStr(ysdate,"yyyy-MM-dd HH:mm:ss") + "' ";
					}
					if (!"".equals(reason)) {
						upSql = upSql + ",REASON='" + reasonInfo + "' ";
					}
					if (reasondate != null) {
						upSql = upSql + ",REASONDATE='" + DateKit.toStr(reasondate,"yyyy-MM-dd HH:mm:ss") + "' ";
					}
					if (!"".equals(ysc)) {
						upSql = upSql + ",CARCARD='" + ysc + "' ";
					}
					upSql=upSql+" where TB_ID='"+id+"'";
					Db.update(upSql);
					controller.setAttr("res1", "01");
				}
				else
				{
					controller.setAttr("res1", res);
				}
				controller.setAttr("resFlag", 0);
			} else {
				controller.setAttr("resFlag", "1");
				controller.setAttr("msg", "少传参数");
			}
		} catch (Exception e) {
			e.printStackTrace();
			controller.setAttr("msg", "系统异常");
			controller.setAttr("resFlag", "1");
		}

	}

	private String checkYsUser(Map<String, Object> tpb, String cardno,
			String resStr) {
		String result = "";
		int ysFlag = 0;
		if ("".equals(resStr)) {
			List<Map<String, Object>> queryPersonInfo = dao.queryPersonInfo(
					null, tpb.get("enIdYs").toString(), "PERSON_INFO_YS");
			for (Map<String, Object> map : queryPersonInfo) {
				if (cardno.equals(map.get("cardNo")+"")) {
					ysFlag = 1;
					break;
				}
			}
			if (ysFlag == 0) {
				result = "03";
			}
		}
		return result;
	}

	private String checkYsCar(Map<String, Object> tpb, String cardno,
			String resStr) {
		String result = "";
		int ysFlag = 0;
		if ("".equals(resStr)) {
			List<Map<String, Object>> queryPersonInfo = dao.queryCarInfo(null,
					tpb.get("enIdYs").toString());
			for (Map<String, Object> map : queryPersonInfo) {
				if (cardno.equals(map.get("cardNo")+"")) {
					ysFlag = 1;
					break;
				}
			}
			if (ysFlag == 0) {
				result = "10";
			}
		}
		return result;
	}

	private String checkStatus(String tbStatus, String gsonStatus)
			throws ParseException {
		String result = "";
		if ("09".equals(tbStatus)) {
			if ("03".equals(gsonStatus) || "04".equals(gsonStatus)
					|| "05".equals(gsonStatus)) {
				result = "05";
			}
		} else if ("05".equals(tbStatus)) {
			result = "07";
		} else if ("04".equals(tbStatus)) {
			result = "09";
		} else if ("03".equals(tbStatus) || "10".equals(tbStatus)
				|| "11".equals(tbStatus) || "12".equals(tbStatus)) {
			result = "08";
		} else if ("02".equals(tbStatus) || "06".equals(tbStatus)) {
			if ("00".equals(gsonStatus) || "01".equals(gsonStatus)
					|| "02".equals(gsonStatus)) {
				result = "06";
			}
		}
		return result;
	}

	private Date disDate(String ysd, Timestamp sysdate) throws ParseException {
		Date result = DateKit.toDate(ysd, "yyyy-MM-dd HH:mm:ss");
		if ("".equals(ysd)) {
			result = sysdate;
		}
		long ysLong = result.getTime();
		long sysLong = sysdate.getTime();
		if (sysLong < ysLong) {
			result = sysdate;
		} else {
			long differ = sysLong - ysLong;
			long days = differ / (1000 * 60 * 60 * 24);
			if (days >= 365) {
				result = sysdate;
			}
		}
		return result;
	}

	@Override
	public void doService() throws Exception {
		Db.tx(new IAtom() {
			@Override
			public boolean run() throws SQLException {
				try {
					if ("queryTransferplanBill".equals(getLastMethodName(7))) {
						queryTransferplanBill();
					} else if ("updateStatus".equals(getLastMethodName(7))) {
						updateStatus();
					} else if ("queryPerson".equals(getLastMethodName(7))) {
						queryPerson();
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

}
