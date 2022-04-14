package com.pancm.test.othersTest;


/**
 * The type Ex test.
 *
 * @author ZERO
 * @date 2017 -4-24
 * @Description 继承测试
 */
@SuppressWarnings({ "serial", "unused" })
public class exTest extends Exception{
	static{
		int x=5;
	}

    /**
     * The X.
     */
    static int x, /**
     * The Y.
     */
    y;

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws exTest the ex test
     */
    public static void main(String[] args) throws exTest {
		int i=1,l=2;
//		System.out.println(++l);
		int q=i++; //1
		int w=++l; //3
		System.out.println(q + w);//4
		x--;
		my();
		System.out.println(x+ y++ +x); //2

		String abc = "abc";
		String ab = "ab";
		System.out.println("=="+ab.contains(abc));
		System.out.println("=="+abc.contains(ab));
		System.out.println("=="+ab.indexOf(abc));
		System.out.println("=="+abc.indexOf(ab));

	}

    /**
     * My.
     */
    public static void my(){
		y=x++ + ++x;
		System.out.println("y:"+y);
	}

}
