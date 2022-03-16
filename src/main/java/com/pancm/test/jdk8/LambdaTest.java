package com.pancm.test.jdk8;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The type Lambda test.
 *
 * @author pancm
 * @Title: lambdaTest
 * @Description: 拉姆达表达式
 * @Version:1.0.0
 * @date 2018年8月28日
 */
public class LambdaTest {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
		test1();
		test2();
	}

	private static void test1() {

		Map<String, String> map = new HashMap<>();
		map.put("a", "a");
		map.put("b", "b");
		map.put("c", "c");
		map.put("d", "d");

		System.out.println("map普通方式遍历:");
		for (String key : map.keySet()) {
			System.out.println("k=" + key + "，v=" + map.get(key));
		}

		System.out.println("map拉姆达表达式遍历:");
		map.forEach((k, v) -> {
			System.out.println("k=" + k + "，v=" + v);
		});

		/**
		 * AtomicInteger说明
		 * AtomicInteger是一个提供原子操作的Integer类，通过线程安全的方式操作加减。
		 *
		 * AtomicReference说明：
		 *
		 * 它是通过"volatile"和"Unsafe提供的CAS函数实现"原子操作。
		 * 1.value是volatile类型。这保证了：当某线程修改value的值时，其他线程看到的value值都是最新的value值，即修改之后的volatile的值。
		 * 2. 通过CAS设置value。这保证了：当某线程池通过CAS函数(如compareAndSet函数)设置value时，它的操作是原子的，即线程在操作value时不会被中断。
		 * ————————————————
		 */

		List<String> list = new ArrayList<String>();
		list.add("a");
		list.add("bb");
		list.add("ccc");
		list.add("dddd");
		System.out.println("list拉姆达表达式遍历:");
		//可以进行原则操作
		final AtomicInteger c = new AtomicInteger();
		list.forEach(v -> {
			System.out.println(v+":c"+c.incrementAndGet());
		});
		System.out.println("list双冒号运算符遍历:");
		list.forEach(System.out::println);

	}

	private static void test2() {
		List<User> list = new ArrayList<User>();
		List<User> list2 = new ArrayList<User>();
		list.add(new User(1, "张三"));
		list.add(new User(2, "李四"));
		list.add(new User(3, "王五"));
		list.add(new User(4, "赵六"));
		System.out.println("list:" + list);
		list.forEach(v -> {
			if (v.getId() > 2) {
				list2.add(v);
			}
		});
		System.out.println("list2:" + list2);
	}

    /**
     * The R 1.
     */
//使用普通的方式创建
	Runnable r1 = new Runnable() {
		@Override
		public void run() {
			System.out.println("普通方式创建!");
		}
	};

    /**
     * The R 2.
     */
//使用拉姆达方式创建
	Runnable r2 = ()-> System.out.println("拉姆达方式创建!");


}

/**
 * The type User.
 */
class User {

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
		System.out.println("姓名:" + name);
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
	 *
	 */
	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + "]";
	}

}
