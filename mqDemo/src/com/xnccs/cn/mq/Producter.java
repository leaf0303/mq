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
	
	//���ӹ���
	ConnectionFactory connectionFactory;
	//���Ӷ���
	Connection connection;
	//�������
	Session session;
	
	ThreadLocal<MessageProducer> threadLocal = new ThreadLocal<>();
	
	{
		try {
			//�������ӹ���
			connectionFactory = new ActiveMQConnectionFactory(Constants.TEST_USER,Constants.TEST_PASSWORD,Constants.TEST_BROKEN_URL);
			//�ӹ����д���һ������
			connection = connectionFactory.createConnection();
			//��������
			connection.start();
			//����һ������
			session = connection.createSession(true, Session.SESSION_TRANSACTED);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
		
	
	public void sendMessage(String disname){
		try {
			//����һ����Ϣ����
			Queue queue = session.createQueue(disname);
			//��Ϣ������
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
				//����һ��Ц��Ϣ
				TextMessage msg = session.createTextMessage(Thread.currentThread().getName()+"������������,count:"+num);
				System.out.println(Thread.currentThread().getName()+"������������,count:"+num);
				//������Ϣ
				messageProducer.send(msg);
				//�ύ����
				session.commit();
			}
			
			
		} catch (JMSException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
