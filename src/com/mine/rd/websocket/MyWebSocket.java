package com.mine.rd.websocket;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.jfinal.json.Jackson;
import com.jfinal.plugin.ehcache.CacheKit;
import com.mine.pub.controller.BaseController;
import com.mine.rd.controllers.arcGis.ArcGisController;
import com.mine.rd.services.arcGis.pojo.ArcGisDao;

@ServerEndpoint(value="/mywebsocket")
public class MyWebSocket {
	//静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;
    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。若要实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
    public static CopyOnWriteArraySet<MyWebSocket> webSocketSet = new CopyOnWriteArraySet<MyWebSocket>();
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;
    
    public String userId;
    
    public String IWBSESSION;
    
    public Map paramMap=null;
    
    public BaseController controller;
    /**
     * 连接建立成功调用的方法
     * @param session  可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    @SuppressWarnings("static-access")
	@OnOpen
    public void onOpen(Session session){
    	this.session = session;
        Map<String, List<String>> m =session.getRequestParameterMap();
        Map<String, Object> mySession = CacheKit.get("mySession",  m.get("IWBSESSION") == null ? "" :m.get("IWBSESSION").get(0));
        if (mySession == null ) {
        	 addOnlineCount(); 
        	 String sessionId = m.get("sessionId") == null ? "" : m.get("sessionId").get(0);
        	 if(!"".equals(sessionId))
        	 {
        		paramMap=new HashMap<String, Object>();
        		paramMap.put("sessionId", sessionId);
        	 }
        	 webSocketSet.add(this); 
        	 System.out.println("连接建立成功调用的方法,"+"当前在线人数为" + getOnlineCount());
		}
        else if ("".equals(m.get("IWBSESSION").get(0))) {
        	 this.getOnlineCount();
        	 System.out.println("连接建立成功调用的方法,"+"有新连接加入！但IWBSESSION为空,当前在线人数为" + getOnlineCount());
		}else {
//			userId = mySession.get("userId")+"";
			IWBSESSION = m.get("IWBSESSION").get(0);
	        webSocketSet.add(this);     //加入set中
	        addOnlineCount();           //在线数加1
	        System.out.println("连接建立成功调用的方法,"+"有新连接加入！当前在线人数为" + getOnlineCount());
		}
        System.out.println(this);
    }
    /**
     * 连接关闭调用的方法
     */
    @SuppressWarnings("static-access")
	@OnClose
    public void onClose(Session session){
        webSocketSet.remove(this); 
        this.session = session;
        Map<String, List<String>> m =session.getRequestParameterMap();
        Map<String, Object> mySession = CacheKit.get("mySession",  m.get("IWBSESSION") ==null ? "" :m.get("IWBSESSION").get(0));
        //从set中删除
        if (mySession == null ) {
        	subOnlineCount();  
       	    System.out.println("连接关闭调用的方法,"+"当前在线人数为" + getOnlineCount()); 
		}
        else if ("".equals(m.get("IWBSESSION").get(0))) {
       	    this.getOnlineCount();
       	    System.out.println("连接关闭调用的方法,"+"有一连接关闭！但IWBSESSION为空,当前在线人数为" + getOnlineCount());
		}else {
			subOnlineCount();           //在线数减1    
	        System.out.println("连接关闭调用的方法,"+"有一连接关闭！当前在线人数为" + getOnlineCount());
		}
    }
    /**
     * 收到客户端消息后调用的方法
     * @param message 客户端发送过来的消息   key-区分交互类型（1：获取手机位置）  res-返回结果（0:成功，1:失败）
     * @param session 可选的参数
     */
    @OnMessage
    @SuppressWarnings("unchecked")
    public void onMessage(String message, Session session) {
		System.out.println("来自客户端的消息:" + message);
		if(message.equals("netWorkTest"))
		{
			try {
				sendMessage("success");
			} catch (IOException e) {
				e.printStackTrace();
			}  
			return;
		}
		Map<String, Object> map = Jackson.getJson().parse(message, Map.class);
        if(map.get("key") != null && "1".equals(map.get("key"))){
        	if(map.get("res") != null && "0".equals(map.get("res"))){
				Map<String, Object> info = (Map<String, Object>) map.get("info");
        		ArcGisDao dao = new ArcGisDao();
        		if(dao.saveLocationConduit(info)){
            		System.out.println("============>true");
            	}else{
            		System.out.println("============>false");
            	}
        	}
        	
        }
        else if(map.get("key") != null && "2".equals(map.get("key"))){
        	ArcGisController bc=new ArcGisController();
    		bc.getCarPositions(message);
    		Map<String, Object> result= (Map<String, Object>)bc.resMap.get("carPositions");
    		String json = Jackson.getJson().toJson(result);
    		String mName = map.get("mName").toString();
    		if(!"queryPositionByLp".equals(mName))
    		{
    			String qc = map.get("qc") == null ? "" : map.get("qc").toString();
        		paramMap.put("mName", mName);
        		paramMap.put("qc", qc);
    		}
    		try {
				sendMessage(json);
			} catch (IOException e) {
				e.printStackTrace();
			}  
        }
        else if(map.get("key") != null && "3".equals(map.get("key"))){
        	ArcGisController bc=new ArcGisController();
        	bc.getCarPositions(message);
        	Map<String, Object> result= (Map<String, Object>)bc.resMap.get("carPositions");
        	String json = Jackson.getJson().toJson(result);
        	try {
				sendMessage(json);
			} catch (IOException e) {
				e.printStackTrace();
			}  
        }
        else if(map.get("key") != null && "4".equals(map.get("key"))){
        	paramMap.put("mName", "");
        }
        else if(map.get("key") != null && "5".equals(map.get("key"))){
        	ArcGisController bc=new ArcGisController();
        	bc.getCarPositions(message);
        	webSocketSet.remove(this);
        	this.onClose(this.session);
        }
        else if(map.get("key") != null && "6".equals(map.get("key"))){
        	ArcGisDao dao = new ArcGisDao();
        	Map<String, Object> info = (Map<String, Object>) map.get("info");
        	dao.saveGisHistory(info);
        }
        else if(map.get("key") != null && "7".equals(map.get("key"))){
        	ArcGisController bc=new ArcGisController();
        	bc.getCarPositions(message);
        	Map<String, Object> result= (Map<String, Object>)bc.resMap.get("carPositions");
        	String json = Jackson.getJson().toJson(result);
        	try {
				sendMessage(json);
			} catch (IOException e) {
				e.printStackTrace();
			}  
        }
        System.out.println("=======================================");
   
    }
    /**
     * 发生错误时调用
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error){
        System.out.println("发生错误");
        String fillInStackTrace = error.fillInStackTrace() + "";
        if(fillInStackTrace.indexOf("java.net.SocketException") > -1){
        	System.err.println("客户端网络异常！");
        	System.err.println(fillInStackTrace);
        }else if(fillInStackTrace.indexOf("java.net.SocketTimeoutException") > -1){
//        	this.onClose(this.session);
       	 	System.err.println("socket连接超时！");
        }else if(fillInStackTrace.indexOf("java.io.EOFException") > -1){
       	 	System.err.println("客户端断开连接！");
        }else{
         	error.printStackTrace();
        }
    }


    /**
     * 这个方法与上面几个方法不一样。没有用注解，是根据自己需要添加的方法。
     * @param message
     * @throws IOException
     */
    public void sendMessage(String message) throws IOException{
        this.session.getAsyncRemote().sendText(message);
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
    	MyWebSocket.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
    	MyWebSocket.onlineCount--;
    }
}
