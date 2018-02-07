package com.xnccs.cn.mq;

public class TestConsumer {
	
	public static void main(String[] args) {
		Consumer consumer = new Consumer();
		TestConsumer testConsumer = new TestConsumer();
		new Thread(testConsumer.new ConsumerMq(consumer)).start();
		new Thread(testConsumer.new ConsumerMq(consumer)).start();
		new Thread(testConsumer.new ConsumerMq(consumer)).start();
		new Thread(testConsumer.new ConsumerMq(consumer)).start();
		new Thread(testConsumer.new ConsumerMq(consumer)).start();
	}

	private class ConsumerMq implements Runnable{
		Consumer consumer;
		public ConsumerMq(Consumer consumer){
			this.consumer = consumer;
		}
		@Override
		public void run() {
			while(true){
				try {
					consumer.getMessage("j_nan-MQ");
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
	
	}
}
