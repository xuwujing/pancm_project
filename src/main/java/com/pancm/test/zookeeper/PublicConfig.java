package com.pancm.test.zookeeper;

/**
 * 
* @Title: PublicConfig
* @Description:
* 公共的配置 
* @Version:1.0.0  
* @author pancm
* @date 2018年5月2日
 */
public class PublicConfig {

	private String url;
	
	private Integer port;

	/**  
	 * 获取url  
	 * @return  url  
	 */
	public String getUrl() {
		return url;
	}

	/**  
	 * 设置url  
	 * @param String url  
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**  
	 * 获取port  
	 * @return  port  
	 */
	public Integer getPort() {
		return port;
	}

	/**  
	 * 设置port  
	 * @param Integer port  
	 */
	public void setPort(Integer port) {
		this.port = port;
	}
	
	
	
}
