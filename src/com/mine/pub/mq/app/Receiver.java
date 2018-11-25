package com.mine.pub.mq.app;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import com.baidu.yun.core.log.YunLogEvent;
import com.baidu.yun.core.log.YunLogHandler;
import com.baidu.yun.push.auth.PushKeyPair;
import com.baidu.yun.push.client.BaiduPushClient;
import com.baidu.yun.push.constants.BaiduPushConstants;
import com.baidu.yun.push.exception.PushClientException;
import com.baidu.yun.push.exception.PushServerException;
import com.baidu.yun.push.model.PushBatchUniMsgRequest;
import com.baidu.yun.push.model.PushBatchUniMsgResponse;
import com.baidu.yun.push.model.PushMsgToSingleDeviceRequest;
import com.baidu.yun.push.model.PushMsgToSingleDeviceResponse;
import com.jfinal.json.Jackson;
import com.jfinal.kit.PropKit;
import net.sf.json.JSONObject;
public class Receiver {
	private static List<Map<String,Object>> list = new ArrayList<>();
	private static String code;
	public static void receive(String[] args) {  
		String tcp = PropKit.get("TCP_PUSH_APP");
        // ConnectionFactory ：连接工厂，JMS 用它创建连接  
        ConnectionFactory connectionFactory;  
        // Connection ：JMS 客户端到JMS Provider 的连接  
        Connection connection = null;  
        // Session： 一个发送或接收消息的线程  
        Session session;  
        // Destination ：消息的目的地;消息发送给谁.  
        Destination destination;  
        // 消费者，消息接收者  
        MessageConsumer consumer;  
        connectionFactory = new ActiveMQConnectionFactory(  
                ActiveMQConnection.DEFAULT_USER,  
//                ActiveMQConnection.DEFAULT_PASSWORD, "tcp://localhost:61616"); 
                ActiveMQConnection.DEFAULT_PASSWORD, tcp); 
        
        try {  
            // 构造从工厂得到连接对象  
            connection = connectionFactory.createConnection();  
            // 启动  
            connection.start();  
            // 获取操作连接  
            session = connection.createSession(Boolean.FALSE,Session.AUTO_ACKNOWLEDGE); 
            // 获取session注意参数值xingbo.xu-queue是一个服务器的queue，须在在ActiveMq的console配置  
            destination = session.createQueue("FirstQueue");  
            consumer = session.createConsumer(destination);  
            while (true) {  
                // 设置接收者接收消息的时间，为了便于测试，这里谁定为100s  
//                TextMessage message = (TextMessage) consumer.receive(100000);  
                TextMessage message = (TextMessage) consumer.receive();  
                if (null != message) {  
                    System.out.println("收到消息" + message.getText()); 
                    System.out.println("收到消息" + list.toString()); 
                    String pushMessge = message.getText();
                    @SuppressWarnings("unchecked")
					Map<String, Object> course = Jackson.getJson().parse(pushMessge, Map.class);
                    list.add(course);
                    for(int i=0; i<list.size(); i++){
                    	//单个发送
                    	androidPushMsgToSingle(list.get(i),i);
                    	//批量发送
//                    	androidPushMsgToBatch(list.get(i),i);
                    }
                } else {  
                    break;  
                }  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            try {  
                if (null != connection)  
                    connection.close();  
            } catch (Throwable ignore) {  
            }  
        }  
    }
	
	 /**
     * @author ouyangxu
     * @date 20160825
     * @单个推送消息
     * */
	public static void androidPushMsgToSingle(Map<String, Object> course, int i) throws PushClientException,PushServerException{
		System.out.println("=========start======");

		// 1. get apiKey and secretKey from developer console
		String apiKey = "";
		String secretKey = "";
		if("3".equals(course.get("deviceType"))){
			apiKey = PropKit.get("androidApiKey");
			secretKey = PropKit.get("androidSecretKey");
		}else if("4".equals(course.get("deviceType"))){
			apiKey = PropKit.get("iosApiKey");
			secretKey = PropKit.get("iosSecretKey");
		}
		PushKeyPair pair = new PushKeyPair(apiKey, secretKey);

		// 2. build a BaidupushClient object to access released interfaces
		BaiduPushClient pushClient = new BaiduPushClient(pair,
				BaiduPushConstants.CHANNEL_REST_URL);

		// 3. register a YunLogHandler to get detail interacting information
		// in this request.
		pushClient.setChannelLogHandler(new YunLogHandler() {
			@Override
			public void onHandle(YunLogEvent event) {
				System.out.println(event.getMessage());
				code = event.getMessage().split("HttpStatusCode:\\[")[1].split("\\]")[0];
				System.out.println("code==========================>"+code);
			}
		});

		try {
			// 4. specify request arguments
			//创建 Android的通知
			JSONObject notification = new JSONObject();
			notification.put("title", course.get("title"));
			notification.put("description","课程《"+course.get("courseName")+"》将于"+course.get("startTime")+"开课");
			notification.put("notification_builder_id", 0);
			notification.put("notification_basic_style", 4);
			notification.put("open_type", 2);
//					notification.put("url", "http://push.baidu.com");
			JSONObject jsonCustormCont = new JSONObject();
			jsonCustormCont.put("key", "尊敬的用户，您参加的直播课程《"+course.get("courseName")+"》将在"+course.get("startTime")+"开始，请做好准备！"); //自定义内容，key-value
			notification.put("custom_content", jsonCustormCont);
			System.out.println("channelId================>"+course.get("channelId"));
			PushMsgToSingleDeviceRequest request = new PushMsgToSingleDeviceRequest()
					.addChannelId(course.get("channelId").toString())
					.addMsgExpires(new Integer(3600)). // message有效时间
					addMessageType(1).// 1：通知,0:透传消息. 默认为0 注：IOS只有通知.
					addMessage(notification.toString()).
					addDeviceType(Integer.parseInt(course.get("deviceType").toString()));
			// 5. http request
			PushMsgToSingleDeviceResponse response = pushClient
					.pushMsgToSingleDevice(request);
			// Http请求结果解析打印
			System.out.println("msgId: " + response.getMsgId() + ",sendTime: "
					+ response.getSendTime());
			if("200".equals(code)){
				list.remove(i);
			}
		} catch (PushClientException e) {
			/*
			 * ERROROPTTYPE 用于设置异常的处理方式 -- 抛出异常和捕获异常,'true' 表示抛出, 'false' 表示捕获。
			 */
			if (BaiduPushConstants.ERROROPTTYPE) {
				throw e;
			} else {
				e.printStackTrace();
			}
		} catch (PushServerException e) {
			if (BaiduPushConstants.ERROROPTTYPE) {
				throw e;
			} else {
				System.out.println(String.format(
						"requestId: %d, errorCode: %d, errorMessage: %s",
						e.getRequestId(), e.getErrorCode(), e.getErrorMsg()));
			}
		}
	}
	
	/**
     * @author ouyangxu
     * @date 20160825
     * @批量推送消息
     * */
	public static void androidPushMsgToBatch(Map<String, Object> course, int i) throws PushClientException,PushServerException{
		System.out.println("=========start======");
		String apiKey = "";
		String secretKey = "";
		String channelIdList = "";
		int type = 0;
		if(course.get("channelIds3") != null && !"".equals(course.get("channelIds3"))){
			apiKey = PropKit.get("androidApiKey");
			secretKey = PropKit.get("androidSecretKey");
			channelIdList = course.get("channelIds3").toString();
			type = 3;
		}
		if(course.get("channelIds4") != null && !"".equals(course.get("channelIds4"))){
			apiKey = PropKit.get("iosApiKey");
			secretKey = PropKit.get("iosSecretKey");
			channelIdList = course.get("channelIds4").toString();
			type = 4;
		}
		batch(course, i, apiKey, secretKey, channelIdList, type);
	}
	
	public static void batch(Map<String, Object> course, int i, String apiKey, String secretKey, String channelIdList, int type) throws PushClientException,PushServerException{
		
		// 1. get apiKey and secretKey from developer console
				PushKeyPair pair = new PushKeyPair(apiKey, secretKey);
		// 2. build a BaidupushClient object to access released interfaces
				BaiduPushClient pushClient = new BaiduPushClient(pair,
						BaiduPushConstants.CHANNEL_REST_URL);

				// 3. register a YunLogHandler to get detail interacting information
				// in this request.
				pushClient.setChannelLogHandler(new YunLogHandler() {
					@Override
					public void onHandle(YunLogEvent event) {
						System.out.println(event.getMessage());
						code = event.getMessage().split("HttpStatusCode:\\[")[1].split("\\]")[0];
						System.out.println("code==========================>"+code);
					}
				});

				try {
					// 4. specify request arguments
					//创建Android通知
					JSONObject notification = new JSONObject();
					notification.put("title", course.get("title"));
					notification.put("description","课程《"+course.get("courseName")+"》将于"+course.get("startTime")+"开课");
					notification.put("notification_builder_id", 0);
					notification.put("notification_basic_style", 4);
					notification.put("open_type", 2);
					JSONObject jsonCustormCont = new JSONObject();
					jsonCustormCont.put("key", "尊敬的用户，您参加的直播课程《"+course.get("courseName")+"》将在"+course.get("startTime")+"开始，请做好准备！"); //自定义内容，key-value
					notification.put("custom_content", jsonCustormCont);
//					System.out.println("channelIds================>"+course.get("channelIds"));
					String[] channelIds = channelIdList.split(",");
					PushBatchUniMsgRequest request = new PushBatchUniMsgRequest()
							.addChannelIds(channelIds)
							.addMsgExpires(new Integer(3600))
							.addMessageType(1)
							.addMessage(notification.toString())
							.addDeviceType(type);// 设置类别主题
					// 5. http request
					PushBatchUniMsgResponse response = pushClient
							.pushBatchUniMsg(request);
					// Http请求结果解析打印
					System.out.println(String.format("msgId: %s, sendTime: %d",
							response.getMsgId(), response.getSendTime()));
					if("200".equals(code)){
						list.remove(i);
					}
				} catch (PushClientException e) {
					if (BaiduPushConstants.ERROROPTTYPE) {
						throw e;
					} else {
						e.printStackTrace();
					}
				} catch (PushServerException e) {
					if (BaiduPushConstants.ERROROPTTYPE) {
						throw e;
					} else {
						System.out.println(String.format(
								"requestId: %d, errorCode: %d, errorMessage: %s",
								e.getRequestId(), e.getErrorCode(), e.getErrorMsg()));
					}
				}
	}
}
