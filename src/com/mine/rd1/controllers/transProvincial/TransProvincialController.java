package com.mine.rd1.controllers.transProvincial;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Collection;

import org.codehaus.xfire.client.Client;

import com.jfinal.aop.Clear;
import com.jfinal.kit.PropKit;
import com.mine.pub.controller.BaseController;
import net.sf.json.JSONObject;
/**
 * @author woody
 * @date 2017-8-16
 * @跨省对接接口
 * */
@Clear
public class TransProvincialController extends BaseController 
{
	private String url = PropKit.get("uploadInterfaceTransProvincialUrl");
	private String tokenUser = PropKit.get("uploadInterfaceUsername");
	private String tokenPwd = PropKit.get("uploadInterfacePwd");
	
	/**
	 * @date 20170816
	 * @上传转移计划
	 * @param token  json
	 * 返回值 
	 *  正常：转移计划编号 
	 *  异常：
	 *  03  token无效
	 *  05  上传失败
	 *  06 缺少ycsxzqhdm
	 *  06-1 ycsxzqhdm无效
	 *  07 缺少 wfycdwbm
	 *  07-1 wfycdwbm为空
	 *  08 缺少 wfycdwmc
	 *  08-1 wfycdwmc为空
	 *  09 缺少 yrsxzqhdm
	 *  09-1 yrsxzqhdm无效
	 *  10 缺少ksrq
	 *  10-1 ksrq格式错误
	 *  11 缺少jsrq
	 *  11-1 jsrq格式错误
	 *  12 缺少jhqrrq
	 *  12-1 jhqrrq格式错误
	 *  13缺少jhqrjg
	 *  13-1 jhqrjg不是1或2
	 *  14 缺少jhqrsxzqh
	 *  14-1 jhqrsxzqh无效
	 *  15 缺少wxfwmc
	 *  16 缺少wxfwdm
	 *  17 缺少zysl
	 *  17-1 zysl不是数字
	 *  18 缺少jldw
	 *  18-1 jldw不是吨或桶
	 * */
	public void saveZyjhTest()
	{
//		String json = "{\"ycsxzqhdm\":\"220100\",\"yrsxzqhdm\":\"441300\",\"jhqryj\":\"同意\",\"wxfw\":[{\"wxfwdm\":\"900-09-09\",\"zysl\":\"200\",\"wxfwmc\":\"废渣0\",\"jldw\":\"吨\"},{\"wxfwdm\":\"900-09-09\",\"zysl\":\"200\",\"wxfwmc\":\"废渣1\",\"jldw\":\"吨\"}],\"wfycdwmc\":\"惠州比亚迪实业有限公司2\",\"wfycdwdz\":\"广东省惠州市大亚湾经济技术开发区惠州市大亚湾响水河\",\"jhqrsxzqh\":\"220000\",\"wfjsdwlxr\":\"荣祥\",\"wfjsdwlxrsj\":\"13544047273\",\"jhqrjg\":\"1\",\"wfjsdwmc\":\"惠州市东江运输有限公司\",\"jhqrrq\":\"2017-09-09\",\"ysdwlxr\":\"李星国\",\"wfjsdz\":\"广东省惠州市仲恺高新区联发大道39号\",\"wfycdwbm\":\"1111\",\"ksrq\":\"2019-09-09\",\"jhqrr\":\"张大军\",\"fwycdwlxrsj\":\"13510888869\",\"fwjsdwwxfwjyxkzh\":\"xkz001\",\"wfycdwlxr\":\"古淑芬\",\"ysdwlxrsj\":\"13878787878\",\"jsrq\":\"2019-09-10\",\"ysdwmc\":\"惠州市东江环保技术有限公司\",\"ysdwdz\":\"广东省惠州市仲恺高新区潼侨工业区联发大道39号\"}";
		String json =  "{}" ;
		if(getPara("jsonParam") != null && !"".equals(getPara("jsonParam"))){
			json =  getPara("jsonParam").toString() ;
		}
		String apiName = "saveZyjhTest";
		String res = sendMessage(json,apiName);
		renderJson(res);
	}
	public void saveZyjh()
	{
		String json =  "{}" ;
		if(getPara("jsonParam") != null && !"".equals(getPara("jsonParam"))){
			json =  getPara("jsonParam").toString() ;
		}
		String apiName = "saveZyjh";
		String res = sendMessage(json,apiName);
		renderJson(res);
	}
	
	/**
	 * @date 2017-8-16
	 * @查询转移计划测试方法
	 * param 移出单位编码
	 * return 计划信息、移出单位信息、运输单位信息、经营单位信息
	 * */
	public void getZyjhTest()
	{
//		String str = "{\"key\":\"1234567\",\"s\":\"1111\"}";
		String str =  "{}" ;
		if(getPara("jsonParam") != null && !"".equals(getPara("jsonParam"))){
			str =  getPara("jsonParam").toString() ;
		}
		String apiName = "getZyjhTest";
		String res = getMessage(str,apiName);
		renderJson(res);
	}
	/**
	 * @date 2017-8-16
	 * @查询转移计划测试方法
	 * param 移出省行政区划  参数格式：两位行政区划代码 参数举例：21
	 * return 计划信息、移出单位信息、运输单位信息、经营单位信息
	 * */
	public void getYcZyjhTest()
	{
//		String str = "{\"key\":\"220100\"}";
		String str =  "{}" ;
		if(getPara("jsonParam") != null && !"".equals(getPara("jsonParam"))){
			str =  getPara("jsonParam").toString() ;
		}
		String apiName = "getYcZyjhTest";
		String res = getMessage(str,apiName);
		renderJson(res);
	}
	/**
	 * @date 2017-8-16
	 * @查询转移计划测试方法
	 * param 移入省行政区划 参数格式：两位行政区划代码 参数举例：21
	 * return 计划信息、移出单位信息、运输单位信息、经营单位信息
	 * */
	public void getYrZyjhTest()
	{
//		String str = "{\"key\":\"441300\"}";
		String str =  "{}" ;
		if(getPara("jsonParam") != null && !"".equals(getPara("jsonParam"))){
			str =  getPara("jsonParam").toString() ;
		}
		String apiName = "getYrZyjhTest";
		String res = getMessage(str,apiName);
		renderJson(res);
	}
	/**
	 * @date 2017-8-16
	 * @查询转移计划测试方法
	 * @param 转移计划编号
	 * @return 危险废物信息
	 * */
	public void getZyjhfwTest()
	{
//		String str = "{\"key\":\"2017220100004242\"}";
		String str =  "{}" ;
		if(getPara("jsonParam") != null && !"".equals(getPara("jsonParam"))){
			str =  getPara("jsonParam").toString() ;
		}
		String apiName = "getZyjhfwTest";
		String res = getMessage(str,apiName);
		renderJson(res);
	}
	/**
	 * @date 2017-8-16
	 * @查询转移计划测试方法
	 * @param 转移计划编号
	 * @return 转移计划确认信息
	 * */
	public void getZyjhspTest()
	{
//		String str = "{\"key\":\"2017220100004242\"}";
		String str =  "{}" ;
		if(getPara("jsonParam") != null && !"".equals(getPara("jsonParam"))){
			str =  getPara("jsonParam").toString() ;
		}
		String apiName = "getZyjhspTest";
		String res = getMessage(str,apiName);
		renderJson(res);
	}
	/**
	 * @date 2017-8-16
	 * @查询转移计划正式方法
	 * param 移出单位编码
	 * return 计划信息、移出单位信息、运输单位信息、经营单位信息
	 * */
	public void getZyjh()
	{
//		String str = "{\"key\":\"1234567\"}";
		String str =  "{}" ;
		if(getPara("jsonParam") != null && !"".equals(getPara("jsonParam"))){
			str =  getPara("jsonParam").toString() ;
		}
		String apiName = "getZyjh";
		String res = getMessage(str,apiName);
		renderJson(res);
	}
	/**
	 * @date 2017-8-16
	 * @查询转移计划正式方法
	 * param 移出省行政区划  参数格式：两位行政区划代码 参数举例：21
	 * return 计划信息、移出单位信息、运输单位信息、经营单位信息
	 * */
	public void getYcZyjh()
	{
//		String str = "{\"key\":\"220100\"}";
		String str =  "{}" ;
		if(getPara("jsonParam") != null && !"".equals(getPara("jsonParam"))){
			str =  getPara("jsonParam").toString() ;
		}
		String apiName = "getYcZyjh";
		String res = getMessage(str,apiName);
		renderJson(res);
	}
	/**
	 * @date 2017-8-16
	 * @查询转移计划正式方法
	 * param 移入省行政区划 参数格式：两位行政区划代码 参数举例：21
	 * return 计划信息、移出单位信息、运输单位信息、经营单位信息
	 * */
	public void getYrZyjh()
	{
//		String str = "{\"key\":\"441300\"}";
		String str =  "{}" ;
		if(getPara("jsonParam") != null && !"".equals(getPara("jsonParam"))){
			str =  getPara("jsonParam").toString() ;
		}
		String apiName = "getYrZyjh";
		String res = getMessage(str,apiName);
		renderJson(res);
	}
	/**
	 * @date 2017-8-16
	 * @查询转移计划正式方法
	 * @param 转移计划编号
	 * @return 危险废物信息
	 * */
	public void getZyjhfw()
	{
//		String str = "{\"key\":\"2017220100004242\"}";
		String str =  "{}" ;
		if(getPara("jsonParam") != null && !"".equals(getPara("jsonParam"))){
			str =  getPara("jsonParam").toString() ;
		}
		String apiName = "getZyjhfw";
		String res = getMessage(str,apiName);
		renderJson(res);
	}
	/**
	 * @date 2017-8-16
	 * @查询转移计划正式方法
	 * @param 转移计划编号
	 * @return 转移计划确认信息
	 * */
	public void getZyjhsp()
	{
//		String str = "{\"key\":\"2017220100004242\"}";
		String str =  "{}" ;
		if(getPara("jsonParam") != null && !"".equals(getPara("jsonParam"))){
			str =  getPara("jsonParam").toString() ;
		}
		String apiName = "getZyjhsp";
		String res = getMessage(str,apiName);
		renderJson(res);
	}
	/**
	 * @date 2017-8-16
	 * @查询许可证正式方法
	 * @param 单位名称  许可证号
	 * @return 顺序号、许可证号、单位名称、单位地址、联系人、联系人电话、许可证有效期、市级行政区划代码
	 * @throws UnsupportedEncodingException 
	 * @备注 根据参数模糊查询、可以传null
	 * */
	public void getXkz()
	{
//		String str = "{\"key1\":\"\",\"key2\":\"赣环危废证字066号\"}";
		String str =  "{}" ;
		if(getPara("jsonParam") != null && !"".equals(getPara("jsonParam"))){
			str =  getPara("jsonParam").toString() ;
		}
		String apiName = "getXkz";
		String res = getMessage(str,apiName);
		renderJson(res);
	}
	/**
	 * @date 2017-8-16
	 * @查询许可证正式方法
	 * @param 许可证号
	 * @return 返回许可证经营危险废物代码信息
	 * @备注 根据参数模糊查询
	 * */
	public void getXkzfw()
	{
//		String str = "{\"key\":\"4406080319\"}";
		String str =  "{}" ;
		if(getPara("jsonParam") != null && !"".equals(getPara("jsonParam"))){
			str =  getPara("jsonParam").toString() ;
		}
		String apiName = "getXkzfw";
		String res = getMessage(str,apiName);
		renderJson(res);
	}
	/**
	 * @date 2017-8-17
	 * @查询许可证正式方法
	 * @param 省份代码
	 * @return 许可证信息
	 * @备注 查询一个省许可证信息
	 * */
	public void getXkzSheng()
	{
//		String str = "{\"key\":\"130000\"}";
		String str =  "{}" ;
		if(getPara("jsonParam") != null && !"".equals(getPara("jsonParam"))){
			str =  getPara("jsonParam").toString() ;
		}
		String apiName = "getXkzSheng";
		String res = getMessage(str,apiName);
		renderJson(res);
	}
	/**
	 * @date 2017-8-17
	 * @查询许可证正式方法
	 * @param 省份代码
	 * @return 许可证信息
	 * @备注 查询除了参数所在省以外的所有省许可证信息
	 * */
	public void getXkzWaiSheng()
	{
//		String str = "{\"key\":\"130000\"}";
		String str =  "{}" ;
		if(getPara("jsonParam") != null && !"".equals(getPara("jsonParam"))){
			str =  getPara("jsonParam").toString() ;
		}
		String apiName = "getXkzWaiSheng";
		String res = getMessage(str,apiName);
		renderJson(res);
	}
	/**
	 * @date 20170817
	 * @上传转移联单
	 * @param token  json
	 * 返回值 
	 *  正常：转移联单编号
	 *  异常：
	 *  03  token无效
	 *  05  上传失败
	 *  04 缺少zyjhbh
	 *  04-1 zyjhbh无效或未确认
	 *  06 缺少 wfycdwbm
	 *  07-1 wfycrq格式错误
	 *  07 缺少 wfycrq
	 *  08-1 ycsxzqhdm为空
	 *  08 缺少 ycsxzqhdm
	 *  09 缺少wfycdwmc
	 *  09-1 wfycdwmc为空
	 *  10 缺少yrsxzqhdm
	 *  10-1 yrsxzqhdm无效
	 *  11缺少wxfw
	 *  12 缺少wxfwmc
	 *  13缺少wxfwdm
	 *  14 缺少zysl
	 *  14-1 zysl不是数字
	 *  15 缺少jldw
	 *  15-1 jsdw不是吨或桶
	 * */
	public void saveZyldTest()
	{
//		String json = "{\"zyjhbh\":\"2017220100004242\",\"ycsxzqhdm\":\"220100\",\"wfycrq\":\"2019-09-09\",\"wfycdwbm\":\"1234567\",\"wfycdwmc\":\"惠州比亚迪实业有限公司\",\"wfycdwdz\":\"广东省惠州市大亚湾经济技术开发区惠州市大亚湾响水河\",\"fwycdwlxrsj\":\"13510888869\",\"wfycdwlxr\":\"古淑芬\",\"ysdwmc\":\"惠州市东江环保技术有限公司\",\"ysdwdz\":\"广东省惠州市仲恺高新区潼侨工业区联发大道39号\",\"ysdwlxr\":\"李星国\",\"ysdwlxrsj\":\"13878787878\",\"fwjsdwwxfwjyxkzh\":\"xkz001\",\"yrsxzqhdm\":\"441300\",\"wfjsdwmc\":\"惠州市东江运输有限公司\",\"wfjsdz\":\"广东省惠州市仲恺高新区联发大道39号\",\"wfjsdwlxrsj\":\"13544047273\",\"wfjsdwlxr\":\"荣祥\",\"wxfw\":[{\"wxfwmc\":\"废渣1\",\"wxfwdm\":\"900-09-09\",\"zysl\":\"200\",\"jldw\":\"吨\"}]}";
		String json =  "{}" ;
//		String json = "{\"wfjsdwlxr\":\"张世亮\",\"ysdwlxr\":\"1\",\"wfjsdwlxrsj\":\"022-28569809\",\"wfycdwdz\":\"天津市开发区洞庭一街2号\",\"wxfw\":[{\"wxfwdm\":\"321-006-48\",\"wxfwmc\":\"1\",\"zysl\":\"2\",\"jldw\":\"吨\"}],\"wfjsdz\":\"天津市市辖区津南区天津市津南区北闸口镇二八路69号\",\"wfycdwmc\":\"天津阿克苏诺贝尔过氧化物有限公司\",\"wfycdwbm\":\"EP201410281019320022\",\"ysdwlxrsj\":\"18524575956\",\"wfycrq\":\"2017-09-05\",\"fwycdwlxrsj\":\"18222593547\",\"zyjhbh\":\"2017120113004821\",\"ldbh\":\"\",\"ysdwdlyszh\":\"2353645\",\"wfycdwlxr\":\"w\",\"ysdwmc\":\"1\",\"yrsxzqhdm\":\"120100\",\"ysdwdz\":\"发的规划吧\",\"ycsxzqhdm\":\"120113\",\"fwjsdwwxfwjyxkzh\":\"TJHW004\",\"tjds\":\"\",\"wfjsdwmc\":\"天津合佳威立雅环境服务有限公司\"}";
		if(getPara("jsonParam") != null && !"".equals(getPara("jsonParam"))){
			json =  getPara("jsonParam").toString() ;
		}
		String apiName = "saveZyldTest";
		String res = sendMessage(json,apiName);
		renderJson(res);
	}
	public void saveZyld()
	{
		String json =  "{}" ;
		if(getPara("jsonParam") != null && !"".equals(getPara("jsonParam"))){
			json =  getPara("jsonParam").toString() ;
		}
		String apiName = "saveZyld";
		String res = sendMessage(json,apiName);
		renderJson(res);
	}
	/**
	 * @date 2017-8-16
	 * @企业查询转移联单测试方法
	 * @param 移出单位编码
	 * @return 联单编号、移出单位信息、运输单位信息、经营单位信息
	 * */
	public void getYcZyldTest()
	{
//		String str =  "{\"key\":\"1234567\"}" ;
		String str =  "{}" ;
		if(getPara("jsonParam") != null && !"".equals(getPara("jsonParam"))){
			str =  getPara("jsonParam").toString() ;
		}
		String apiName = "getYcZyldTest";
		String res = getMessage(str,apiName);
		renderJson(res);
	}
	/**
	 * @date 2017-8-16
	 * @企业查询转移联单测试方法
	 * @param 经营单位名称
	 * @return 联单编号、移出单位信息、运输单位信息、经营单位信息
	 * */
	public void getYrZyldTest()
	{
//		String str =  "{\"key\":\"中海油能源发展股份有限公司安全环保分公司碧海环保服务公司\"}" ;
		String str =  "{}" ;
		if(getPara("jsonParam") != null && !"".equals(getPara("jsonParam"))){
			str =  getPara("jsonParam").toString() ;
		}
		String apiName = "getYrZyldTest";
		String res = getMessage(str,apiName);
		renderJson(res);
	}
	/**
	 * @date 2017-8-16
	 * @省级环保部门查询转移联单测试方法
	 * @param 移入省行政区划
	 * @return 联单编号、移出单位信息、运输单位信息、经营单位信息
	 * @备注 参数格式：两位行政区划代码 参数举例：21
	 * */
	public void getZyldYrShengTest()
	{
//		String str =  "{\"key\":\"120000\"}" ;
		String str =  "{}" ;
		if(getPara("jsonParam") != null && !"".equals(getPara("jsonParam"))){
			str =  getPara("jsonParam").toString() ;
		}
		String apiName = "getZyldYrShengTest";
		String res = getMessage(str,apiName);
		renderJson(res);
	}
	/**
	 * @date 2017-8-16
	 * @省级环保部门查询转移联单测试方法
	 * @param 移出省行政区划
	 * @return 联单编号、移出单位信息、运输单位信息、经营单位信息
	 * @备注 参数格式：两位行政区划代码 参数举例：21
	 * */
	public void getZyldYcShengTest()
	{
//		String str =  "{\"key\":\"120000\"}" ;
		String str =  "{}" ;
		if(getPara("jsonParam") != null && !"".equals(getPara("jsonParam"))){
			str =  getPara("jsonParam").toString() ;
		}
		String apiName = "getZyldYcShengTest";
		String res = getMessage(str,apiName);
		renderJson(res);
	}
	/**
	 * @date 2017-8-16
	 * @企业查询转移联单正式方法
	 * @param 移出单位编码
	 * @return 联单编号、移出单位信息、运输单位信息、经营单位信息
	 * */
	public void getYcZyld()
	{
//		String str =  "{\"key\":\"TJHW011\"}" ;
		String str =  "{}" ;
		if(getPara("jsonParam") != null && !"".equals(getPara("jsonParam"))){
			str =  getPara("jsonParam").toString() ;
		}
		String apiName = "getYcZyld";
		String res = getMessage(str,apiName);
		renderJson(res);
	}
	/**
	 * @date 2017-8-16
	 * @企业查询转移联单正式方法
	 * @param 经营单位名称
	 * @return 联单编号、移出单位信息、运输单位信息、经营单位信息
	 * */
	public void getYrZyld()
	{
//		String str =  "{\"key\":\"中海油能源发展股份有限公司安全环保分公司碧海环保服务公司\"}" ;
		String str =  "{}" ;
		if(getPara("jsonParam") != null && !"".equals(getPara("jsonParam"))){
			str =  getPara("jsonParam").toString() ;
		}
		String apiName = "getYrZyld";
		String res = getMessage(str,apiName);
		renderJson(res);
	}
	/**
	 * @date 2017-8-16
	 * @省级环保部门查询转移联单测试方法
	 * @param 移入省行政区划
	 * @return 联单编号、移出单位信息、运输单位信息、经营单位信息
	 * @备注 参数格式：两位行政区划代码 参数举例：21
	 * */
	public void getZyldYrSheng()
	{
//		String str =  "{\"key\":\"120000\"}" ;
		String str =  "{}" ;
		if(getPara("jsonParam") != null && !"".equals(getPara("jsonParam"))){
			str =  getPara("jsonParam").toString() ;
		}
		String apiName = "getZyldYrSheng";
		String res = getMessage(str,apiName);
		renderJson(res);
	}
	/**
	 * @date 2017-8-16
	 * @省级环保部门查询转移联单正式方法
	 * @param 移出省行政区划
	 * @return 联单编号、移出单位信息、运输单位信息、经营单位信息
	 * @备注 参数格式：两位行政区划代码 参数举例：21
	 * */
	public void getZyldYcSheng()
	{
//		String str =  "{\"key\":\"120000\"}" ;
		String str =  "{}" ;
		if(getPara("jsonParam") != null && !"".equals(getPara("jsonParam"))){
			str =  getPara("jsonParam").toString() ;
		}
		String apiName = "getZyldYcSheng";
		String res = getMessage(str,apiName);
		renderJson(res);
	}
	/**
	 * @date 20170817
	 * @办结转移联单
	 * @param json
	 * 返回值 
	 *  正常：01
	 *  异常：
	 *  04  联单编号不存在或已办结
	 *  05  上传失败
	 *  06 缺少wfjsdwjsrq
	 *  06-1 wfjsdwjsrq格式无效
	 *  07 缺少 wfjsdwclyj
	 *  08 缺少 wxfw
	 *  09 缺少wxfwmc
	 *  10 缺少wxfwdm
	 *  11-1 jssl不是数字
	 *  11 缺少jssl
	 *  12  联单中不存在wxfwdm和wxfwmc无法办结
	 * */
	public void bjZyldTest()
	{
//		String json =  "{\"ldbh\":\"2017220100004242\",\"wfjsdwjsrq\":\"2018-10-19\",\"wfjsdwclyj\":\"同意接收\",\"wxfw\":[{\"wxfwmc\":\"废渣1\",\"wxfwdm\":\"900-09-09\",\"jssl\":\"666\"}]}" ;
//		String json =  "{\"ldbh\":\"2017220100000061\",\"sfczzdcy\":\"2\",\"wxfw\":[{\"wxfwdm\":\"900-09-09\",\"wxfwmc\":\"废渣1\",\"jssl\":\"200.000000\"}],\"wfjsdwclyj\":\"1\",\"wfjsdwjsrq\":\"2017-08-30\"}";
		String json =  "{}" ;
		if(getPara("jsonParam") != null && !"".equals(getPara("jsonParam"))){
			json =  getPara("jsonParam").toString() ;
		}
		String apiName = "bjZyldTest";
		String res = sendMessageWithoutToken(json,apiName);
		renderJson(res);
	}
	public void bjZyld()
	{
//		String str =  "{\"key\":\"120000\"}" ;
		String json =  "{}" ;
		if(getPara("jsonParam") != null && !"".equals(getPara("jsonParam"))){
			json =  getPara("jsonParam").toString() ;
		}
		String apiName = "bjZyld";
		String res = sendMessage(json,apiName);
		renderJson(res);
	}
	/**
	 * @date 20170905
	 * @联单编号查询转移联单
	 * @param json
	 * */
	public void getZyldByzyldbh()
	{
//		String json =  "{\"zyldbh\":\"2017220100004242\"}" ;
		String json =  "{}" ;
		if(getPara("jsonParam") != null && !"".equals(getPara("jsonParam"))){
			json =  getPara("jsonParam").toString() ;
		}
		String apiName = "getZyldByzyldbh";
		String res = getMessage(json,apiName);
		renderJson(res);
	}
	public void getZyldByzyldbhTest()
	{
//		String json =  "{\"zyldbh\":\"2017120103000174\"}" ;
		String json =  "{}" ;
		if(getPara("jsonParam") != null && !"".equals(getPara("jsonParam"))){
			json =  getPara("jsonParam").toString() ;
		}
		String apiName = "getZyldByzyldbhTest";
		String res = getMessage(json,apiName);
		renderJson(res);
	}
	/**
	 * @date 20170905
	 * @许可证编号、是否办结（0 未办结，1 已办结）查询转移联单
	 * @param json
	 * */
	public void getZyldByXkzbhAndSfbj()
	{
//		String json =  "{\"xkzbh\":\"2017220100004242\",\"sfbj\":\"0\"}" ;
		String json =  "{}" ;
		if(getPara("jsonParam") != null && !"".equals(getPara("jsonParam"))){
			json =  getPara("jsonParam").toString() ;
		}
		String apiName = "getZyldByXkzbhAndSfbj";
		String res = getMessage(json,apiName);
		renderJson(res);
	}
	public void getZyldByXkzbhAndSfbjTest()
	{
//		String json =  "{\"xkzbh\":\"\",\"sfbj\":\"0\"}" ;
		String json =  "{}" ;
		if(getPara("jsonParam") != null && !"".equals(getPara("jsonParam"))){
			json =  getPara("jsonParam").toString() ;
		}
		String apiName = "getZyldByXkzbhAndSfbjTest";
		String res = getMessage(json,apiName);
		renderJson(res);
	}
	/**
	 * @date 20170905
	 * @移出单位名称、是否办结（0 未办结，1 已办结）查询转移联单
	 * @param json
	 * */
	public void getZyldByYcDwmcAndSfbj()
	{
//		String json =  "{\"ycdwmc\":\"2017220100004242\",\"sfbj\":\"2017220100004242\"}" ;
		String json =  "{}" ;
		if(getPara("jsonParam") != null && !"".equals(getPara("jsonParam"))){
			json =  getPara("jsonParam").toString() ;
		}
		String apiName = "getZyldByXkzbhAndSfbj";
		String res = getMessage(json,apiName);
		renderJson(res);
	}
	public void getZyldByYcDwmcAndSfbjTest()
	{
//		String json =  "{\"ycdwmc\":\"2017220100004242\",\"sfbj\":\"2017220100004242\"}" ;
		String json =  "{}" ;
		if(getPara("jsonParam") != null && !"".equals(getPara("jsonParam"))){
			json =  getPara("jsonParam").toString() ;
		}
		String apiName = "getZyldByYcDwmcAndSfbjTest";
		String res = getMessage(json,apiName);
		renderJson(res);
	}
	/**
	 * @date 20170905
	 * @移入单位名称、是否办结（0 未办结，1 已办结）查询转移联单
	 * @param json
	 * */
	public void getZyldByYrDwmcAndSfbj()
	{
//		String json =  "{\"yrdwmc\":\"2017220100004242\",\"sfbj\":\"2017220100004242\"}" ;
		String json =  "{}" ;
		if(getPara("jsonParam") != null && !"".equals(getPara("jsonParam"))){
			json =  getPara("jsonParam").toString() ;
		}
		String apiName = "getZyldByYrDwmcAndSfbj";
		String res = getMessage(json,apiName);
		renderJson(res);
	}
	public void getZyldByYrDwmcAndSfbjTest()
	{
//		String json =  "{\"yrdwmc\":\"2017220100004242\",\"sfbj\":\"2017220100004242\"}" ;
		String json =  "{}" ;
		if(getPara("jsonParam") != null && !"".equals(getPara("jsonParam"))){
			json =  getPara("jsonParam").toString() ;
		}
		String apiName = "getZyldByYrDwmcAndSfbjTest";
		String res = getMessage(json,apiName);
		renderJson(res);
	}
	/**
	 * @author woody
	 * @date 20170816
	 * @上传数据共用方法
	 * */
	private String sendMessage(String json,String apiName)
	{
		String res = "";
		try {
			System.out.println("sendMessage json before=========>"+json);
			sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
			byte[] bt = decoder.decodeBuffer(json); 
			json =  new String(bt, "UTF-8");
			System.out.println("sendMessage json=========>"+json);
			Client client = new Client(new URL(url));
			Object[] token = client.invoke("getToken",new Object[] {tokenUser,tokenPwd});//获取token
			Object[] zyjhbh = client.invoke(apiName,new Object[] {token[0].toString(),json});
			res = zyjhbh[0].toString();
			System.out.println("return json=========>"+zyjhbh[0].toString());
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	/**
	 * @author woody
	 * @date 20170816
	 * @上传数据共用方法(不需要token)
	 * */
	private String sendMessageWithoutToken(String json,String apiName)
	{
		String res = "";
		try {
			System.out.println("sendMessage json before=========>"+json);
			sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
			byte[] bt = decoder.decodeBuffer(json); 
			json =  new String(bt, "UTF-8");
			System.out.println("sendMessage json=========>"+json);
			Client client = new Client(new URL(url));
			Object[] zyjhbh = client.invoke(apiName,new Object[] {json});
			res = zyjhbh[0].toString();
			System.out.println("return json=========>"+zyjhbh[0].toString());
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	
	@SuppressWarnings("unchecked")
	private String getMessage(String json,String apiName)
	{
		String res = "";
		try {
			System.out.println("sendMessage json before=========>"+json);
			sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
			byte[] bt = decoder.decodeBuffer(json); 
			json =  new String(bt, "UTF-8");
			System.out.println("getMessage json=========>"+json);
			JSONObject jsonObject=JSONObject.fromObject(json);  
			Collection<Object> arra  = jsonObject.values();
			Object[] objs = arra.toArray();
			Client client = new Client(new URL(url));
			Object[] resObj = client.invoke(apiName,objs);
			res = resObj[0].toString();
			System.out.println("return json=========>"+resObj[0].toString());
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return res;
	}
	public static void main(String args[])
	{
		
//		String url = "http://www.swmc.org.cn/edpgf_csy/servicesx/KSTranService?wsdl";
//		String apiName = "getXkzSheng";
		try {
			String s = "{\"wfycdwmc\":\"河西单位0712\",\"sfbj\":\"0\"}";
		   sun.misc.BASE64Encoder necoder = new sun.misc.BASE64Encoder();  
		   String ss = necoder.encode(s.getBytes());
		   System.out.println(ss);
		   sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
		   byte[] bt = decoder.decodeBuffer(ss); 
		   ss =  new String(bt, "UTF-8");
		   System.out.println(ss);
//			Client client = new Client(new URL(url));
////			Object[] token = client.invoke("getToken",new Object[] {tokenUser,tokenPwd});//获取token
//			Object[] zyjhbh = client.invoke(apiName,new Object[] {"120000"});
//			System.out.println(zyjhbh[0].toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
