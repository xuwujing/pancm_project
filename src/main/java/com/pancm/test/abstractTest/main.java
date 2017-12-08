package com.pancm.test.abstractTest;
/**
 * @author ZERO
 * @Data 2017-6-1 下午3:37:23
 * @Description 抽象类
 */
abstract class E{  
    public abstract  void show();  
}  
  
class F extends E{  
    public void show(){  
        System.out.print("test all FFFF \n");  
    }  
}  
  
class G extends E{  
    public void show(){  
        System.out.print("test all GGGG \n");  
    }  
}  
public class main   
{  
    public static void main(String[] args)throws InterruptedException {  
        E p = new F();  
        p.show();  
        E q = new G();  
        q.show();  
    }  
}  
