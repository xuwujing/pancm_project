package com.pancm.test.threeCharactersTest;

/**
 * 
* @Title: packagingTest
* @Description:
* 封装 
* @Version:1.0.0  
* @author pancm
* @date 2018年3月27日
 */
public class packagingTest {

	public static void main(String[] args) {
		User user=new User();
		user.setId(1);
		user.setName("张三");
		System.out.println(user.getId());
		System.out.println(user.getName());
	}

}

class User{
	private int id;
	private String name;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}