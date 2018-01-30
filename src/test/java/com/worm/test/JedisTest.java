package com.worm.test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisTest {
	
	public static JedisPool jedisPool;
	public static JedisPoolConfig config;
	
	static {
		// 初始化连接池配置对象  
        config = new JedisPoolConfig(); 
        config.setMaxIdle(10);// 控制一个Pool最多有多少个状态为idle(空闲的)jedis实例,默认值也是8
        config.setMaxTotal(30); // 可用连接实例的最大数目,如果赋值为-1,表示不限制.
        config.setMaxWaitMillis(3*1000); // 等待可用连接的最大时间,单位毫秒,默认值为-1,表示永不超时/如果超过等待时间,则直接抛出异常
        config.setTestOnBorrow(true); // 在borrow一个jedis实例时,是否提前进行validate操作,如果为true,则得到的jedis实例均是可用的
        // 实例化连接池  
        jedisPool=new JedisPool(config, "127.0.0.1", 6379);  
	}

	@SuppressWarnings({ "deprecation", "unused" })
	public static void close(Jedis jedisConn,JedisPool pool){  
	        if(jedisConn!=null &&pool!=null){  
	           // jedisConn.close();	 
	            pool.returnResource(jedisConn); 
	        }  
	        if(pool!=null){  
	        	//jedisPool.close();
	            jedisPool.destroy();  
	        }  
	    }  
	  
	public static Jedis getResource() {
		if (jedisPool != null) {
			return jedisPool.getResource();
		}
		return null;
		
	}
	 
	
	public static void main(String[] args) {
		Jedis jedis = jedisPool.getResource();
		jedis.lpush("list", "a");
		jedis.lpush("list", "b");
		jedis.lpush("list", "c");
		jedis.lpush("list", "d");
		
		System.out.println(jedis.lrange("list", 0, -1));
	}
}
