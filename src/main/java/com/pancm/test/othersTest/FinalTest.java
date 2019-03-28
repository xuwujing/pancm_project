package com.pancm.test.othersTest;

import java.util.Date;


/**
 * Title: FinalTest
 * Description:
 * final测试
 * Version:1.0.0
 *
 * @author pancm
 * @date 2018年3月21日
 */
public class FinalTest{
    /**
     * The constant name.
     */
//定义一个final修饰的变量
    public  static final String name="xuwujing";

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
		//这句会报错  因为该变量已经被final修饰了
//		name="张三";
	}

    /**
     * The type Test 2.
     */
//类加上final之后，该类是无法被继承的
	final class Test2{
	}
	//这句会报错，因为Test2是被final修饰的类
//	class Test3 extends Test2{
//	}

    /**
     * The type Test 4.
     */
    class Test4{
        /**
         * Get time date.
         *
         * @return the date
         */
//定义一个被final修饰的方法
		 final Date getTime(){
			return new Date();
		}
	}

    /**
     * The type Test 5.
     */
    class Test5 extends Test4{
		//这句会报错，因为final方法是不能被子类修改的。
//		Date getTime(){
//			return new Date();
//		}
	}
}
