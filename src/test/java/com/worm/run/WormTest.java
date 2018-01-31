package com.worm.run;

import java.io.IOException;

public class WormTest implements Runnable {

	@Override
	public void run() {

		try {
			if (LinkQueue.unVisitedUrlEmpty()) {
				JsoupUtil.getAllMovieUrl();
			}

		} catch (InterruptedException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		/**
		 * 当不存在为访问的URl时，停止查找 当以访问过的URL到达一定数量时，停止查找
		 */
		while (!LinkQueue.unVisitedUrlEmpty() && LinkQueue.getVisitedUrlNum() < 10000) {
			try {
				String url = LinkQueue.unVisitedUrlDeQueue(); // 从未访问过的列中取出一个URl,并添加到已访问的列表中去
				LinkQueue.addVisitedUrl(url);
				Thread.sleep(2000);
				System.out.println("线程 : " + Thread.currentThread().getName() + "  已访问数目 ："
						+ LinkQueue.getVisitedUrlNum() + " 待访问队列数目 : " + LinkQueue.getUnVisitedUrlNum());
				System.out.println("当前地址：" + url);
				System.out.println(LinkQueue.getMovies());
				Movie movie = JsoupUtil.getResult(url);
				System.out.println(movie.toString());				
			} catch (Exception e) {
			} finally {

			}
		}
	}

	public static void main(String[] args) throws InterruptedException {
		WormTest wormTest = new WormTest();		
		Thread thread1 = new Thread(wormTest);
		thread1.start();
	}

}
