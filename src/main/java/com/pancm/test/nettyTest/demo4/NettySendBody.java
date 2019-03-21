package com.pancm.test.nettyTest.demo4;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description: Netty 传输对象
 * Version:1.0.0
 *
 * @author pancm
 * @date 2017年9月24日
 */
public class NettySendBody  implements Serializable{
	
	/**
	 * 序列化
	 */
	private static final long serialVersionUID = 1L;
	
	
	/** 创建集合  */
	private ConcurrentHashMap<String, String> data;
	/** 时间戳 */
	private long timestamp;

    /**
     * Instantiates a new Netty send body.
     */
    public NettySendBody() {
		data = new ConcurrentHashMap<String, String>();
		timestamp = System.currentTimeMillis();  //取当前时间
	}


    /**
     * Gets data.
     *
     * @return the data
     */
    public ConcurrentHashMap<String, String> getData() {
		return data;
	}

    /**
     * Sets data.
     *
     * @param data the data
     */
    public void setData(ConcurrentHashMap<String, String> data) {
		this.data = data;
	}

    /**
     * Gets timestamp.
     *
     * @return the timestamp
     */
    public long getTimestamp() {
		return timestamp;
	}

    /**
     * Sets timestamp.
     *
     * @param timestamp the timestamp
     */
    public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

    /**
     * Get string.
     *
     * @param k the k
     * @return the string
     */
    public String get(String k) {
		return data.get(k);
	}

    /**
     * Put.
     *
     * @param k the k
     * @param v the v
     */
    public void put(String k, String v) {
		data.put(k, v);
	}


	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		buffer.append("<sent>");
		buffer.append("<timestamp>").append(timestamp).append("</timestamp>");
		buffer.append("<data>");
		for (String key : data.keySet()) {
			buffer.append("<" + key + ">").append(data.get(key)).append(
					"</" + key + ">");
		}
		buffer.append("</data>");
		buffer.append("</sent>");
		return buffer.toString();
	}
	

}
