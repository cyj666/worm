package com.worm.test;

import java.util.Collections;
import java.util.LinkedList;

public class Queue {
	
	//队列
	private LinkedList<String> queue = new LinkedList<>();
	
	public Queue() {
		// TODO Auto-generated constructor stub
		Collections.synchronizedCollection(queue);//线程安全
	}
			
	//入队
	public void addQueue(String e) {
		queue.offer(e);
	}
	
	//出队
	public String delQueue() {
		return queue.poll();
	}
	
	public int size(){
        return queue.size();
    }
	
    // 是否为空     空->true
    public boolean isQueueEmpty(){
        return queue.isEmpty();
    }
    
    // 是否包含t   包含->true
    public boolean contains(String t){
        return queue.contains(t);
    } 
	
	

	
}
