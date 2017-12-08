package com.pancm.test.codetest;
/**
 * @author ZERO
 * @version 2017-4-17 下午6:05:37
 * 类说明 boolean源码检测
 */
public class TestBoolean {

						
	public static void main(String[] args) {
		 {
		        Boolean bool1 = Boolean.valueOf(true);       //这里均使用valueof创建对象，new创建的Boolean对象是不断的新创建一个实例对象，而valueOf则是返回Boolean类里的静态成员变量
		        Boolean bool2 = Boolean.valueOf("True");    //这里上一句代码验证使用String变量作为参数时，不区分大小写的。
		        Boolean bool3 = Boolean.valueOf("ASD");
		        boolean x1 = bool1.booleanValue();
		        boolean x2 = bool2.booleanValue();
		        System.out.println("bool1:" + x1 + ",bool2:" + x2 + ",bool3:" + bool3);
		        boolean x3 = bool1.equals(bool2);       //这个就是验证享元模式，使用的是同一个对象
		        boolean x4 = bool1.equals(bool3);       //肯定不是同一对象啦。
		        System.out.println("bool1.equals(bool2):" + x3 + ",bool1.equals(bool3):" + x4);
		        String str1 = Boolean.toString(bool1);      //可见Boolean对象是可以转换成字符的
		        String str2 = Boolean.toString(false);      
		        String str3 = bool3.toString();
		        System.out.println("bool1:" + str1 + ",str2:" + str2 + ",bool3:" + str3);
		        boolean x5 = Boolean.parseBoolean("ASD");         //源码是直接判断然后与true对比，因此打印为false
		        System.out.println(x5);
		    }

	}

}
