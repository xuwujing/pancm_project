package com.pancm.test.design.nullobject;

/**
 * The type Null object test.
 *
 * @author pancm
 * @Title: NullObjectTest
 * @Description: 空对象模式 一个空对象取代 NULL 对象实例的检查。Null 对象不是检查空值，而是反应一个不做任何动作的关系。 这样的               Null 对象也可以在数据不可用的时候提供默认的行为。      核心: 其主要目的是在进行调用是不返回Null，而是返回一个空对象，防止空指针异常。
 * @Version:1.0.0
 * @date 2018年8月8日
 */
public class NullObjectTest {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
		
		/*
		 * 
		 */
		
		AbstractUser au1 = UserFactory.getUser("wangwu");
		AbstractUser au2 = UserFactory.getUser("xuwujing");

		System.out.println(au1.isNull());
		System.out.println(au1.getName());
		System.out.println(au2.isNull());
		System.out.println(au2.getName());

		/*
		 * 
		 * 优点: 
		 1 .可以加强系统的稳固性，能有效防止空指针报错对整个系统的影响。
		 2 .不依赖客户端便可以保证系统的稳定性。
		 
		 缺点: 
		 1.需要编写较多的代码来实现空值的判断，从某种方面来说不划算。
		 
		 使用场景：
		 需要大量对空值进行判断的时候。
		 
		 * 
		 */
	}

}

/**
 * The interface Abstract user.
 */
//定义一个抽象类
interface AbstractUser {
    /**
     * Gets name.
     *
     * @return the name
     */
    String getName();

    /**
     * Is null boolean.
     *
     * @return the boolean
     */
    boolean isNull();
}

/**
 * The type Real user.
 */
//实际用户
class RealUser implements AbstractUser {
	private String name;

    /**
     * Instantiates a new Real user.
     *
     * @param name the name
     */
    public RealUser(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean isNull() {
		return false;
	}
}

/**
 * The type Null user.
 */
class NullUser implements AbstractUser {

	@Override
	public String getName() {
		return "user is not exist";
	}

	@Override
	public boolean isNull() {
		return true;
	}
}

/**
 * The type User factory.
 */
//定义一个工厂
class UserFactory {

    /**
     * The constant names.
     */
    public static final String[] names = { "zhangsan", "lisi", "xuwujing" };

    /**
     * Gets user.
     *
     * @param name the name
     * @return the user
     */
    public static AbstractUser getUser(String name) {
		for (int i = 0; i < names.length; i++) {
			if (names[i].equalsIgnoreCase(name)) {
				return new RealUser(name);
			}
		}
		return new NullUser();
	}
}