package com.pancm.test.abstractTest;

/**
 * The type E.
 *
 * @author ZERO
 * @Data 2017 -6-1 下午3:37:23
 * @Description 抽象类
 */
abstract class E{
    /**
     * Show.
     */
    public abstract  void show();
}

/**
 * The type F.
 */
class F extends E{
    public void show(){  
        System.out.print("test all FFFF \n");  
    }  
}

/**
 * The type G.
 */
class G extends E{
    public void show(){  
        System.out.print("test all GGGG \n");  
    }  
}

/**
 * The type Main.
 */
public class Main
{
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        E p = new F();  
        p.show();  
        E q = new G();  
        q.show();
        
         /*
          * 
          1、抽象类和抽象方法都需要被abstract修饰。抽象方法一定要定义在抽象类中。

		　　2、抽象类不可以创建实例，原因：调用抽象方法没有意义。
		
		　　3、只有覆盖了抽象类中所有的抽象方法后，其子类才可以实例化。否则该子类还是一个抽象类。
		
		　　之所以继承，更多的是在思想，是面对共性类型操作会更简单。
          * 
          */
    }  
}  
