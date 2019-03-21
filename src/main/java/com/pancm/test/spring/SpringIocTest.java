package com.pancm.test.spring;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * The type Spring ioc test.
 *
 * @author pancm
 * @Title: SpringIocTest
 * @Description: Spring的IOC测试类
 * @Version:1.0.0
 * @date 2019年1月18日
 */
public class SpringIocTest {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
		
		SpringIocTest test =new SpringIocTest();
		IHello hello =new Hello();
		
		try {
			test.getIntance(hello.getClass(),"sayHello","xuwujing");
			test.getIntance(hello.getClass(),"sayBye","xuwujing");
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * 利用反射原理获取值
	 * @param class1
	 * @param method
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 */
	private void getIntance(Class<?> class1,String method,String name) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Object obj=class1.newInstance();
		Method[] methods = obj.getClass().getMethods();
		for(Method method2:methods) {
			if(method.equals(method2.getName().intern())) {
				method2.invoke(obj, name);
			}
		}
		
	}
	
	
}

/**
 * The interface Hello.
 */
/*
 * 定义一个Hello的接口
 */
interface IHello{

    /**
     * Say hello.
     *
     * @param name the name
     */
    void sayHello(String name);

    /**
     * Say bye.
     *
     * @param name the name
     */
    void sayBye(String name);
}

/**
 * The type Hello.
 */
/*
 * 
 */
class Hello implements IHello{

	@Override
	public void sayHello(String name) {
		System.out.println("hello "+name);
	}

	@Override
	public void sayBye(String name) {
		System.out.println("bye "+name);
	}
	
}

