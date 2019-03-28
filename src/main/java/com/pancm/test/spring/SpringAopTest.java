package com.pancm.test.spring;

<<<<<<< HEAD
import java.lang.reflect.*;
=======
import static org.hamcrest.CoreMatchers.nullValue;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
>>>>>>> origin/master
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Objects;

<<<<<<< HEAD
/**
 * @Title: SpringAopTest
 * @Description: SpringAop 测试类
 * @Version:1.0.0
 * @author pancm
=======
import sun.reflect.Reflection;

/**
 * The type Spring aop test.
 *
 * @author pancm
 * @Title: SpringAopTest
 * @Description: SpringAop 测试类
 * @Version:1.0.0
>>>>>>> origin/master
 * @date 2019年1月18日
 */
public class SpringAopTest {

<<<<<<< HEAD
	/**
	 * @param args
	 */
	public static void main(String[] args) {
=======
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
>>>>>>> origin/master

		IHello hello = new Hello();
		ProxyHello proxyHello = new ProxyHello(hello);

		// 利用jdk动态代理机制
		IHello proxy = (IHello) Proxy.newProxyInstance(hello.getClass().getClassLoader(), // 返回目标类的类装载器，保持两个类的类装载器一样
				hello.getClass().getInterfaces(), // 返回目标类实现的接口，保证组合而成的代理类也实现这些接口
				proxyHello// 指派谁去处理方法的对象
		);
		proxy.sayHello("xuwujing");
		proxy.sayBye("xuwujing");

	}
<<<<<<< HEAD
	
	
	
	/**
	 * JDK1.8代理源码
	 * @param loader
	 * @param interfaces
	 * @param h
	 * @return
	 * @throws IllegalArgumentException
	 */
	public static Object newProxyInstance(ClassLoader loader, Class<?>[] interfaces, InvocationHandler h)
=======


    /**
     * JDK1.8代理源码
     *
     * @param loader     the loader
     * @param interfaces the interfaces
     * @param h          the h
     * @return object
     * @throws IllegalArgumentException the illegal argument exception
     */
    public static Object newProxyInstance(ClassLoader loader, Class<?>[] interfaces, InvocationHandler h)
>>>>>>> origin/master
			throws IllegalArgumentException {
		//空指针校验
		Objects.requireNonNull(h);
		
		final Class<?>[] intfs = interfaces.clone();
		//这个是进行鉴权
		final SecurityManager sm = System.getSecurityManager();
		if (sm != null) {
//			checkProxyAccess(Reflection.getCallerClass(), loader, intfs);
		}

		/*
		 * 查找或生成指定的代理类.
		 */
		Class<?> cl = getProxyClass0(loader, intfs);

		/*
		 * 使用指定的调用处理程序调用其构造函数
		 */
		try {
			if (sm != null) {
				//检查调用者的代理类是否在这个包中
//				checkNewProxyPermission(Reflection.getCallerClass(), cl);
			}
			
			//代理类构造函数的参数类型
			final Class<?>[] constructorParams ={ InvocationHandler.class };
			// 得到构造方法
			final Constructor<?> cons = cl.getConstructor(constructorParams);
			final InvocationHandler ih = h;
			//如果构造器器不是公共的，需要修改访问权限，使其可以访问
			if (!Modifier.isPublic(cl.getModifiers())) {
				AccessController.doPrivileged(new PrivilegedAction<Void>() {
					public Void run() {
						cons.setAccessible(true);
						return null;
					}
				});
			}
			//通过构造方法，创建对象，传入InvocationHandler 对象
			return cons.newInstance(new Object[] { h });
		} catch (IllegalAccessException | InstantiationException e) {
			throw new InternalError(e.toString(), e);
		} catch (InvocationTargetException e) {
			Throwable t = e.getCause();
			if (t instanceof RuntimeException) {
				throw (RuntimeException) t;
			} else {
				throw new InternalError(t.toString(), t);
			}
		} catch (NoSuchMethodException e) {
			throw new InternalError(e.toString(), e);
		}
	}

	
	/**
     * 生成代理类
     * 在调用此函数之前执行权限检查.
     */
    private static Class<?> getProxyClass0(ClassLoader loader,
                                           Class<?>... interfaces) {
        if (interfaces.length > 65535) {
            throw new IllegalArgumentException("interface limit exceeded");
        }
        
        
        
        /*
         	如果代理类由给定的加载器实现定义给定的接口存在，这只会返回缓存的副本;否则，它将通过ProxyClass创建代理类
         	
         	proxyClassCache 是个代理类的缓存
         */
//        return proxyClassCache.get(loader, interfaces);
        
        return null;
    }
	
}

/**
<<<<<<< HEAD
 * 
 * @Title: ProxyHello
 * @Description:实现代理
 * @Version:1.0.0
 * @author pancm
=======
 * The type Proxy hello.
 *
 * @author pancm
 * @Title: ProxyHello
 * @Description:实现代理
 * @Version:1.0.0
>>>>>>> origin/master
 * @date 2019年1月18日
 */
class ProxyHello implements InvocationHandler {

	private Object object;

<<<<<<< HEAD
	/**
	 * 指定
	 * 
	 * @param
	 */
	public ProxyHello(Object obj) {
=======
    /**
     * 指定
     *
     * @param obj the obj
     */
    public ProxyHello(Object obj) {
>>>>>>> origin/master
		this.object = obj;
	}

	/** 
	 * 
	 */
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) {
		Object obj = null;

		try {
			// JVM通过这条语句执行原来的方法(反射机制)
			obj = method.invoke(object, args);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

		return obj;
	}

}