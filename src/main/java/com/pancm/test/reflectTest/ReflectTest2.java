package com.pancm.test.reflectTest;

import com.pancm.util.GetSpringBean;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

/**
 * Title: ReflectTest2
 * Description:
 * 反射测试
 * 　反射的基本步骤：
 * 1、获得class对象，就是获取到指定的名称的字节码文件对象。
 * 2、实例化对象，获得类的属性、方法或构造函数。
 * 3、访问属性、调用方法、调用构造函数创建对象。
 * Version:1.0.0
 *
 * @author pancm
 * @date 2018年2月28日
 */
public class ReflectTest2 {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws ReflectiveOperationException the reflective operation exception
     */
    public static void main(String[] args) throws ReflectiveOperationException {
		method_1();
		method_2();
		method_3();
		method_4();
		User user = new User();
		BeanUtils.setProperty(user,"name","张三");
		System.out.println(user);
	}

	/**
	 * 获取该类中的所有方法
	 */
	private static void method_1() throws ReflectiveOperationException {
		//指定类和路径
		Class clazz=Class.forName("com.pancm.test.reflectTest.User");
//		Class clazz=User.class;
		//获取的是该类中的公有方法和父类中的公有方法。
		Method[] methods=clazz.getMethods();
		//获取本类中的方法，包含私有方法。
		methods=clazz.getDeclaredMethods();
		for(Method me:methods){
			System.out.println("方法:"+me);
		}
	}

	/**
	 * 获取指定方法
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static void method_2() throws ReflectiveOperationException {
		//指定类和路径
		Class clazz=Class.forName("com.pancm.test.reflectTest.User");
		//获取的指定名称的方法
		//如果带有入参，则指定入参类型
		Method method=clazz.getMethod("getMessage2",int.class);
		//初始化
		Object obj=clazz.newInstance();
		//执行该方法
		method.invoke(obj, 11);
	}


	/**
	 * 获取私有的方法
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static void method_3() throws ReflectiveOperationException {
		//指定类和路径
		Class clazz=Class.forName("com.pancm.test.reflectTest.User");
		//获取私有的方法，必须要使用getDeclaredMethod
		Method method=clazz.getDeclaredMethod("getMessage3", null);
		//私有方法不能直接访问，因为权限不够。非要访问，可以通过暴力的方式。
		method.setAccessible(true);
	}


	/**
	 * 获取静态方法
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static void method_4() throws ReflectiveOperationException {
		//指定类和路径
		Class clazz=Class.forName("com.pancm.test.reflectTest.User");
		//获取私有的方法，必须要使用getDeclaredMethod
		Method method=clazz.getMethod("getMessage4",String.class);
		System.out.println();
		Object o =method.invoke(null, "测试");
		System.out.println("返回值:"+o);
	}


	private Object getMethodValue(String className, String methodName,String args) throws Exception {
		Method  mh = ReflectionUtils.findMethod(GetSpringBean.getBean(className).getClass(), methodName,new Class[]{String.class} );
		Object obj = ReflectionUtils.invokeMethod(mh,  GetSpringBean.getBean(className),args);
		return obj;
	}
}
