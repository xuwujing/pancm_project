package com.pancm.test.extendsTest;

/**
 * The type Ss.
 *
 * @author ZERO
 * @Data 2017 -5-31 下午4:54:55
 * @Description 继承构造块 测试
 */
class ss{
    /**
     * Instantiates a new Ss.
     */
    ss(){
		System.out.println("one");
	}

    /**
     * Instantiates a new Ss.
     *
     * @param str the str
     */
    ss(String str){
      System.out.println("four");		
	}
}

/**
 * The type Gzff test.
 */
public class gzffTest extends ss{

    /**
     * Instantiates a new Gzff test.
     */
    gzffTest(){
		System.out.println("two");
	}

    /**
     * Instantiates a new Gzff test.
     *
     * @param str the str
     */
    gzffTest(String str){
		this();
		System.out.println("three");
	}

    /**
     * Main.
     *
     * @param args the args
     */
    public static void main(String[] args){
	//	new gzff("1"); //one two three
		new gzffTest(); //one two
	}
}
