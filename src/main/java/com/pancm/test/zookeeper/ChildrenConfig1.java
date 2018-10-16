package com.pancm.test.zookeeper;

/**
 * 
* @Title: ChildrenConfig
* @Description:
* 子类配置 
* @Version:1.0.0  
* @author pancm
* @date 2018年5月2日
 */
public class ChildrenConfig1 {

	private String name;
	
	private String dburl;

	/**  
	 * 获取name  
	 * @return  name  
	 */
	public String getName() {
		return name;
	}

	/**  
	 * 设置name  
	 * @param String name  
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**  
	 * 获取dburl  
	 * @return  dburl  
	 */
	public String getDburl() {
		return dburl;
	}

	/**  
	 * 设置dburl  
	 * @param String dburl  
	 */
	public void setDburl(String dburl) {
		this.dburl = dburl;
	}
	
	
}

