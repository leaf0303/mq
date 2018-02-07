package com.xnccs.cn.mq;

public class TestMq {
	
	public static void main(String[] args) {
		Producter producter = new Producter();
		TestMq mq = new TestMq();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		new Thread(mq.new ProductorMq(producter)).start();
		new Thread(mq.new ProductorMq(producter)).start();
		new Thread(mq.new ProductorMq(producter)).start();
		new Thread(mq.new ProductorMq(producter)).start();
		new Thread(mq.new ProductorMq(producter)).start();
		new Thread(mq.new ProductorMq(producter)).start();
	}

	
	private class ProductorMq implements Runnable{
		Producter producter;
		public ProductorMq(Producter producter){
			this.producter = producter;
		}
		@Override
		public void run() {
			while(true){
				try {
					producter.sendMessage("j_nan-MQ");
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
