package com.worm.test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WormTest implements Runnable{

	
	@Override
	public void run() {					 	
	        while(!LinkQueue.unVisitedUrlEmpty()&&LinkQueue.getVisitedUrlNum()<20) {	        	
	            try{
	                String url = LinkQueue.unVisitedUrlDeQueue();
	                LinkQueue.addVisitedUrl(url);
	                Thread.sleep(5000);
	                Queue newQ = JsoupUtil.htmlUrlPerser(url);
	                while(!newQ.isQueueEmpty()){
	                    String oneUrl = newQ.delQueue();
	                 //   System.out.println(oneUrl);
	                    LinkQueue.addUnvisitedUrl(oneUrl);
	                }
	                System.out.println("线程 : "+Thread.currentThread().getName()+"  已访问数目 ："+LinkQueue.getVisitedUrlNum()+" 待访问队列数目 : "+LinkQueue.getUnVisitedUrlNum());
	                System.out.println("当前地址："+url);
	                System.out.println();
	            }catch (Exception e){
	            }finally {

	            }
	        }
	    }
	
	public static void main(String[] args) throws InterruptedException {
		WormTest wormTest = new WormTest();
		ExecutorService pool = Executors.newFixedThreadPool(4);
		pool.submit(wormTest);
		pool.submit(wormTest);
		pool.submit(wormTest);
		pool.submit(wormTest);	
		pool.shutdown();
		/*Thread thread1 = new Thread(wormTest);
		Thread thread2= new Thread(wormTest);
		Thread thread3 = new Thread(wormTest);
		Thread thread4 = new Thread(wormTest);
		thread1.start();
		thread2.start();
		thread3.start();
		thread4.start();*/
	}
	
}
