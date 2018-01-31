package com.worm.run;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import redis.clients.jedis.Jedis;

public class JsoupUtil {

	private static Movie movie = new Movie();
	public static Jedis jedis = JedisTest.getResource();

	public void setProxy(String IP, String port) {
		System.setProperty("http.maxRedirects", "50");
		System.getProperties().setProperty("proxySet", "true");
		// 如果不设置，只要代理IP和代理端口正确,此项不设置也可以
		System.getProperties().setProperty("http.proxyHost", IP);
		System.getProperties().setProperty("http.proxyPort", port);
	}

	

	
	
	/**
	 * 从URL中获取的json数据转换成电影信息类集合
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public static List<MovieInfo> getMovieInfos(String url) throws IOException {
		StringBuffer sb = new StringBuffer();

		/**
		 * 连接
		 */
		Connection connection = Jsoup.connect(url);
		connection.userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0")
				.ignoreContentType(true) // 处理json字符串
				.timeout(3000);
		Document doc = connection.get();

		/**
		 * 处理json字符串
		 */
		String json = doc.body().text();
		JSONObject object = JSON.parseObject(json);
		Object jsonArray = object.get("data");
		List<MovieInfo> movieInfos = JSON.parseArray(jsonArray + "", MovieInfo.class);
		for (MovieInfo movieInfo : movieInfos) {
			// System.out.println(movieInfo);
			sb.append(movieInfo.toString()+"\n");
		}

		//download(sb, "F://MovieInfo.txt"); // 保存在本地，可自行修改。
		return movieInfos;
	}

	/**
	 * 得到电影的具体地址,并添加到未访问过的列表中去
	 * 
	 * @throws InterruptedException
	 * @throws IOException
	 */
	//public volatile static Queue queue =LinkQueue.getUnVisitedUrl();
	

	public static synchronized void getAllMovieUrl() throws InterruptedException, IOException {
		for (int i = 0; i < 2; i++) { // 根据多线程分配任务
			Thread.sleep(1000);
			String url = Content.URL + String.valueOf(i * 20);// 根据豆瓣最受欢迎电影页面设定
			List<MovieInfo> movieInfos = getMovieInfos(url);
			for (MovieInfo movieInfo : movieInfos) {
				if (!LinkQueue.getUnVisitedUrl().contains(movieInfo.getUrl())&&!LinkQueue.getVisitedUrl().contains(movieInfo.getUrl())) { // 判断未访问过的队列中是否包含此电影网址，不存在才添加
					LinkQueue.addUnvisitedUrl(movieInfo.getUrl());
				}
			}
		}

	}

	
	public static Movie getResult(String url) throws IOException {		
		Connection connection = Jsoup.connect(url);
		connection.userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0")
		.timeout(3000);
		Document doc = connection.get();	
				
		Elements movieName = doc.select("span[property=v:itemreviewed]");
		movie.setMovieName(movieName.text());
		Elements year = doc.select("span.year");
		movie.setYear(year.text());						
		Elements directors = doc.select("span.attrs a[rel=v:directedBy]");
		movie.setDirectors(directors.text());			
		Elements rate = doc.select("div.rating_self.clearfix[typeof=v:Rating] strong.rating_num[property=v:average]");
		movie.setRate(Double.parseDouble(rate.text()));
		Elements popleNum = doc.select("span[property=v:votes]");
		movie.setPopleNum(Long.parseLong(popleNum.text()));
		Elements casts = doc.select("span.actor span.attrs a[rel=v:starring]");// 主演
		for (Element element : casts) {
			movie.setCasts(element.text());
		}		
		Elements type = doc.select("span[property=v:genre]");
		for (Element element : type) {
			movie.setType(element.text());
		}				
		LinkQueue.setMovies(movie);
		toMovieRedis(jedis, movie.toString());
		//JsoupUtil.download(movie.toString(), "F://Movie.txt");
		System.out.println(movie.toString());
		movie = null;
		return movie;
	}
	
	public static void delVisitedRedis(Jedis jedis, String s) {
		String key = "visitedUrl";
		if (jedis.sismember(key, s)) {
			jedis.srem(key, s);
		}
	}

	public static void delUnVisitedRedis(Jedis jedis, String s) {
		String key = "unVisitedUrl";
		if (jedis.sismember(key, s)) {
			jedis.srem(key, s);
		}
	}

	public static void toMovieRedis(Jedis jedis, String s) {
		String key = "movie";
		jedis.sadd(key, s);
	}
	
	public static void toVisitedRedis(Jedis jedis, String s) {
		String key = "visitedUrl";
		jedis.sadd(key, s);
	}

	public static void toUnVisitedRedis(Jedis jedis, String s) {
		String key = "unVisitedUrl";
		jedis.sadd(key, s);
	}

	/*public static void download(StringBuffer s) throws IOException {
		if (s != null) {
			BufferedWriter writer = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(new File("F://temp.txt"))));
			// Writer writer = getWriter();
			writer.write(s.toString());
			writer.flush();
			writer.close();
		} else {
			System.out.println("空值不操作");
		}

	}*/

	/*public static void download(StringBuffer s, String pathName) throws IOException {
		File file = new File(pathName);	
		StringBuffer sb =  new StringBuffer("");
		if (file.exists()) {
			String str = "";
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
			while ((str=reader.readLine())!=null) {
				sb.append(str);
			}
			reader.close();
		}
		if (s != null) {
			BufferedWriter writer = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(new File(pathName))));
			// Writer writer = getWriter();
			writer.write(sb.toString()+s.toString());
			writer.flush();
			writer.close();
		} else {
			System.out.println("空值不操作");
		}

	}*/
	
	/*public static void download(String s, String pathName) throws IOException {
		File file = new File(pathName);	
		StringBuffer sb =  new StringBuffer("");
		if (file.exists()) {
			String str = "";
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
			while ((str=reader.readLine())!=null) {
				sb.append(str);
			}
			reader.close();
		}
		if (s != null) {
			BufferedWriter writer = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(new File(pathName))));
			// Writer writer = getWriter();
			writer.write(sb.toString()+s.toString());
			writer.flush();
			writer.close();
		} else {
			System.out.println("空值不操作");
		}

	}*/

}
