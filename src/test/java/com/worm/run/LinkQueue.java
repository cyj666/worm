package com.worm.run;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


import redis.clients.jedis.Jedis;

public class LinkQueue {

	private static Set<String> visitedUrl = new HashSet<>();
	private static Queue unVisitedUrl 
	= new Queue();
	private static Jedis jedis = JedisTest.getResource();
	private static List<Movie> movies = new ArrayList<>();
	
	static {
		//初始化redis中的数据
		for (String s : jedis.sdiff("visitedUrl")) {
			visitedUrl.add(s);
		}
		for (String s : jedis.sdiff("unVisitedUrl")) {
			unVisitedUrl.addQueue(s);
		}		
	}
	
	
	public static List<Movie> getMovies() {
		return movies;
	}
	
	public static void setMovies(Movie movies) {
		LinkQueue.movies.add(movies);
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
		JsoupUtil.toVisitedRedis(jedis, url);
	}
	
	 // 移除访问过的 URL
    public static void removeVisitedUrl(String url){
        visitedUrl.remove(url);
        JsoupUtil.delVisitedRedis(jedis, url);
    }
    // 未访问过的 URL 出列
    public static String unVisitedUrlDeQueue(){
    	String url = unVisitedUrl.delQueue();
    	JsoupUtil.delUnVisitedRedis(jedis, url);
        return url;
    }
    
    // 在unVisitedUrl 加入之前判断其中是否有重复的 ， 当无重复时才做添加
    public static void addUnvisitedUrl(String url){
        if((!unVisitedUrl.contains(url))&&(url!=null)&&(!visitedUrl.contains(url))){
            unVisitedUrl.addQueue(url);
            JsoupUtil.toUnVisitedRedis(jedis, url);
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
