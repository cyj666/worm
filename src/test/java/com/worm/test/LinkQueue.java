package com.worm.test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import redis.clients.jedis.Jedis;

public class LinkQueue {

	private static volatile Set<String> visitedUrl = new HashSet<>();
	private static volatile Queue unVisitedUrl = new Queue();
	
	static {
		Jedis jedis = JedisTest.getResource();
		if (jedis.scard("visitedUrl")==0) {
			unVisitedUrl.addQueue("https://movie.douban.com/review/best/?start=0");
		}else {		
		//初始化redis中的数据
		for (String s : jedis.sdiff("visitedUrl")) {
			System.out.println(s);
			visitedUrl.add(s);
		}
		for (String s : jedis.sdiff("visitedUrl")) {
			unVisitedUrl.addQueue(s);
		}
		}
	}
	
	
	//得到已访问的URL
	public static Queue getUnVisitedUrl() {
		return unVisitedUrl;
	}
	
	public static Set<String> getVisitedUrl() {
		Collections.synchronizedSet(visitedUrl);
		return visitedUrl;
	}
	
	//增加到已访问列表中去
	public static void addVisitedUrl(String url) {
		visitedUrl.add(url);
	}
	
	 // 移除访问过的 URL
    public static void removeVisitedUrl(String url){
        visitedUrl.remove(url);
    }
    // 未访问过的 URL 出列
    public static String unVisitedUrlDeQueue(){
        return unVisitedUrl.delQueue();
    }
    
    // 在unVisitedUrl 加入之前判断其中是否有重复的 ， 当无重复时才做添加
    public static void addUnvisitedUrl(String url){
        if((!unVisitedUrl.contains(url))&&(url!=null)&&(!visitedUrl.contains(url))){
            unVisitedUrl.addQueue(url);
        }
        
    } 
    
    // 已访问的数目
    public static int getVisitedUrlNum(){
        return visitedUrl.size();
    }
    
    // 待访问的数目
    public static int getUnVisitedUrlNum(){
        return unVisitedUrl.size();
    }
    
    // 判断 待访问队列 是否为空
    public static boolean unVisitedUrlEmpty(){
        return unVisitedUrl.isQueueEmpty();
    }
	
	
}
