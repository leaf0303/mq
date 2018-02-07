package com.xnccs.cn.mq;

import java.util.concurrent.atomic.AtomicInteger;


import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

import com.xnccs.cn.common.Constants;

public class Consumer {

	//链接工厂
	ConnectionFactory connectionFactory;
	//链接对象
	Connection connection;
	//事务管理
	Session session;
	
	ThreadLocal<MessageConsumer> threadLocal = new ThreadLocal<>();
	
	AtomicInteger count = new AtomicInteger();
	
	
	{
		try {
			//创建链接工厂
			connectionFactory = new ActiveMQConnectionFactory(Constants.TEST_USER,Constants.TEST_PASSWORD,Constants.TEST_BROKEN_URL);
			//从工厂中创建一个链接
			connection = connectionFactory.createConnection();
			//开启链接
			connection.start();
			//创建一个事务
//			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
	
	
	public void getMessage(String disname){
		try {
			Queue queue = session.createQueue(disname);
			MessageConsumer consumer = null;
			if(threadLocal.get()!=null){
				consumer = threadLocal.get();
			}else{
				consumer = session.createConsumer(queue);
				threadLocal.set(consumer);
			}
			
			while(true){
				Thread.sleep(1000);
				TextMessage msg = (TextMessage) consumer.receive();
				if(msg != null){
					msg.acknowledge();
					System.out.println(Thread.currentThread().getName()+": Consumer:我是消费者，我正在消费Msg"+msg.getText()+"--->"+count.getAndIncrement());
				}else{
					break;
				}
			}
			
			
			
		} catch (JMSException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
