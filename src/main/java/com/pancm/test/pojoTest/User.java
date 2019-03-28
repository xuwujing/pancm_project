package com.pancm.test.pojoTest;

import java.util.Map;

import com.pancm.util.MyTools;

/**
 * Title: User Description:用户pojo类 Version:1.0.0
 *
 * @author pancm
 * @date 2017年9月26日
 */
public class User {

	/** 编号 */
	private int id;
	/** 姓名 */
	private String name;

    /**
     * Instantiates a new User.
     */
    public User() {
	}

    /**
     * 构造方法
     *
     * @param id   编号
     * @param name 姓名
     */
    public User(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

    /**
     * 获取编号
     *
     * @return id id
     */
    public int getId() {
		return id;
	}

    /**
     * 设置编号
     *
     * @param id the id
     */
    public void setId(int id) {
		this.id = id;
	}

    /**
     * 获取姓名
     *
     * @return name name
     */
    public String getName() {
		System.out.println("姓名:"+name);
		return name;
	}

    /**
     * 设置姓名
     *
     * @param name the name
     */
    public void setName(String name) {
		this.name = name;
	}


    /**
     * To map map.
     *
     * @return the map
     */
    public Map toMap() {
		return MyTools.toMap(toString());
	}
	/** 
	 * 
	 */
	@Override
	public String toString() {
		return MyTools.toString(this);
	}

}
