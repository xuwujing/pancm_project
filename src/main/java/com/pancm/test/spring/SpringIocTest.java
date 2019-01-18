package com.pancm.test.spring;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
* @Title: SpringIocTest
* @Description: Spring的IOC测试类
* @Version:1.0.0  
* @author pancm
* @date 2019年1月18日
*/
public class SpringIocTest {

	/**
	 * @param args
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

/*
 * 定义一个Hello的接口
 */
interface IHello{
	
	void sayHello(String name);
	
	void sayBye(String name);
}

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

