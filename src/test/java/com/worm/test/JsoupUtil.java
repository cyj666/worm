package com.worm.test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.Writer;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import redis.clients.jedis.Jedis;

public class JsoupUtil {

	private final static StringBuffer sb = new StringBuffer();
	//private static Writer writer = null;
	private final static String CLOCK = "CLOCK";
	
	public void setProxy(String IP,String port) {
		System.setProperty("http.maxRedirects", "50");
		System.getProperties().setProperty("proxySet", "true");
		// 如果不设置，只要代理IP和代理端口正确,此项不设置也可以
		System.getProperties().setProperty("http.proxyHost", IP);
		System.getProperties().setProperty("http.proxyPort", port);
	}
	public static Jedis jedis = JedisTest.getResource();

	
	// 获取一个 URL 中 所有 子URL
    public static Queue htmlUrlPerser(String url) throws Exception {
    	sb.append(url+"\n");
    	Thread.sleep(2000);
    	Queue queue = new Queue();    	   	
        Connection connection = Jsoup.connect(url);
		connection.userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0")
		.timeout(3000);
		Document doc = connection.get();	
		//getResult(doc);  //豆瓣专用
        Elements elements = doc.select("a");
        for(Element element : elements){
            String aurl = element.attr("href") ;
            if (!Pattern.matches("^(http|https)://+.*", aurl)&&!Pattern.matches(".*(www.|.com|.cn).*", aurl)) {           	
            	if ((url.lastIndexOf("/")==(url.length()-1))&&aurl.length()>2) {
            		aurl=aurl.substring(1); //去除第一个符号“/”，进行拼接。        
            		//System.out.println(aurl);
            		
				}            	
				aurl=url+aurl;				
			}
            /**
             * 在此修改URL过滤策略，只提取想要的目标网址的格式
             */
            /*if (Pattern.matches(".*start=[0-9]{0,2}$", aurl)) {
            	if(!queue.contains(aurl)){
                    queue.addQueue(aurl);
                   toUnVisitedRedis(jedis,aurl);
                }
			}*/
            
            
        }
        	
        		download(sb);
                System.out.println(sb.toString());
                toVisitedRedis(jedis,url);
                delUnVisitedRedis(jedis, url);        	
        return queue;
    }
    
    public static void main(String[] args) {
		//System.out.println("http://www.acfun.cn/".lastIndexOf("/"));
		System.out.println(Pattern.matches(".*start=[0-9]{0,2}$", "https://movie.douban.com/review/best/?start=80"));
	}
    
    public static void getResult(Document document) {
		Elements elements = document.select("a.name[property][href]");  //用户
		Elements elements2 = document.select("img[alt][title][src][rel=v:image]");  //电影名
		Elements elements3 = document.select("span[property=v:rating].main-title-rating[title]"); //评价
		Elements elements4 = document.select(".main.review-item");
		for (Element element : elements4) {
			String movie = element.select("img[alt][title][src][rel=v:image]").attr("title");
			String rat =  element.select("span[property=v:rating].main-title-rating[title]").attr("title");
			System.out.println("《"+movie+"》"+":"+rat);
		}
    }
    
    public static void delVisitedRedis(Jedis jedis,String s) {
    	String key = "visitedUrl";   
    	if (jedis.sismember(key, s)) {
			jedis.srem(key, s);
		}
    }
    
    public static void delUnVisitedRedis(Jedis jedis,String s) {
    	String key = "unVisitedUrl";   	
    	if (jedis.sismember(key, s)) {
			jedis.srem(key, s);
		}
    }
    
    public static void toVisitedRedis(Jedis jedis,String s) {
    	String key = "visitedUrl";
    	//jedis.lpush(key, s);
    	jedis.sadd(key, s);
    	//JedisTest.close(jedis,JedisTest.jedisPool);
    }
    
    public static void toUnVisitedRedis(Jedis jedis,String s) {
    	String key = "unVisitedUrl";
    	//jedis.lpush(key, s); 
    	jedis.sadd(key, s);  	
    	//JedisTest.close(jedis,JedisTest.jedisPool);
    }
    
   
    

    public static void download(StringBuffer s) throws IOException {
    	if (s!=null) {
    		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("F://temp.txt"))));
        	//Writer writer = getWriter();
        	writer.write(s.toString());
        	writer.flush();
        	//writer.close();
		}else {
			System.out.println("空值不操作");
		}
    	
    }
    
    public static void download(StringBuffer s,String pathName) throws IOException {
    	if (s!=null) {
    		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(pathName))));
        	//Writer writer = getWriter();
        	writer.write(s.toString());
        	writer.flush();
        	//writer.close();
		}else {
			System.out.println("空值不操作");
		}
    	
    }
    
}
