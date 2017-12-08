package com.pancm.test.jsonTest;
/**
 * 
* Title: User
* Description: fastJson 测试pojo 
* Version:1.0.0  
* @author panchengming
* @date 2017年9月10日
 */
public class User {
	/** 编号*/
	private Integer id;
	/** 姓名 */
	private String name;
	/** 年龄 */
	private Integer age;
	/**  
	 * 获取编号  
	 * @return id 
	 */
	public Integer getId() {
		return id;
	}
	/**  
	 * 设置编号  
	 * @param id 
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**  
	 * 获取姓名  
	 * @return name 
	 */
	public String getName() {
		return name;
	}
	/**  
	 * 设置姓名  
	 * @param name 
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**  
	 * 获取年龄  
	 * @return age 
	 */
	public Integer getAge() {
		return age;
	}
	/**  
	 * 设置年龄  
	 * @param age 
	 */
	public void setAge(Integer age) {
		this.age = age;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", age=" + age + "]";
	}
	
	
}
