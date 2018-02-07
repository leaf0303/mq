package com.xnccs.cn.mq;

import java.util.concurrent.atomic.AtomicInteger;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import com.xnccs.cn.common.*;

import org.apache.activemq.ActiveMQConnectionFactory;

public class Producter {
	
	AtomicInteger count = new AtomicInteger(0);
	
	//链接工厂
	ConnectionFactory connectionFactory;
	//链接对象
	Connection connection;
	//事务管理
	Session session;
	
	ThreadLocal<MessageProducer> threadLocal = new ThreadLocal<>();
	
	{
		try {
			//创建链接工厂
			connectionFactory = new ActiveMQConnectionFactory(Constants.TEST_USER,Constants.TEST_PASSWORD,Constants.TEST_BROKEN_URL);
			//从工厂中创建一个链接
			connection = connectionFactory.createConnection();
			//开启链接
			connection.start();
			//创建一个事务
			session = connection.createSession(true, Session.SESSION_TRANSACTED);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
		
	
	public void sendMessage(String disname){
		try {
			//创建一个消息队列
			Queue queue = session.createQueue(disname);
			//消息生产者
			MessageProducer messageProducer = null;
			if(threadLocal.get() != null){
				messageProducer = threadLocal.get();
			}else{
				messageProducer = session.createProducer(queue);
				threadLocal.set(messageProducer);
			}
			System.out.println("-----------------");
			while(true){
				Thread.sleep(1000);
				int num = count.getAndIncrement();
				//创建一条笑消息
				TextMessage msg = session.createTextMessage(Thread.currentThread().getName()+"我在生产内容,count:"+num);
				System.out.println(Thread.currentThread().getName()+"我在生产内容,count:"+num);
				//发送消息
				messageProducer.send(msg);
				//提交事务
				session.commit();
			}
			
			
		} catch (JMSException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
