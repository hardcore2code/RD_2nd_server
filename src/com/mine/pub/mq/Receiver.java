package com.mine.pub.mq;
import java.io.File;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import com.jfinal.kit.PropKit;
import com.mine.pub.kit.PdfKit;
public class Receiver {
	private static String type;
	private static String Id;
	public static void receive(String[] args) {  
		String tcp = PropKit.get("TCP");
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
                    String upload = message.getText();
                    upload = upload.replace("//", "/");
                    transformation(Id,type,upload);                    
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
     * @author jiang
	 * @param message 
	 * @return 
     * @date 20160329
     * @接收文档并转换
     * */
	public static void transformation(String Id,String type,String upload)
	{
		try {
			
//            	共享文件夹路径\\\\ 数据库路径\\
            	String name = PropKit.get("absolute_path") + upload;
            	name = "d:\\"+upload;
            	String filename= name;
            	File file = new File(filename);
            	String getFilename = file.getAbsolutePath();
//              获取.之前的所有名称
            	System.out.println("getFilename==================>>"+getFilename);
            	//需要后面改成正式代码!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            	name = name.replaceAll("//", "\\");
            	System.out.println("name==================>>"+name);
				String uploadPath = getFilename .substring(0,getFilename .lastIndexOf("."));
				System.out.println("uploadPath==================>>"+uploadPath);
                PdfKit.convert2PDF(name, uploadPath+".pdf");
                String fileName = ".swf";
        		PdfKit.convertPDF2SWF(uploadPath+".pdf", uploadPath ,fileName);
                try {
//                	dao.adressOver(Id, type, uploadPath);
        		} catch (Exception e) {
        			// TODO: handle exception
        		}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	
}
