package com.pancm.test.extendsTest;

/**
 * The type Dervied.
 *
 * @author ZERO
 * @Data 2017 -5-31 下午5:25:01
 * @Description 继承方法测试
 */
public class Dervied extends Base {

    private String name = "dervied";

    /**
     * Instantiates a new Dervied.
     */
    public Dervied() {
        tellName();
        printName();
    }
    
    public void tellName() {
        System.out.println("Dervied tell name: " + name);
    }
    
    public void printName() {
        System.out.println("Dervied print name: " + name);
    }

    /**
     * Main.
     *
     * @param args the args
     */
    public static void main(String[] args){
        
        new Dervied();    
    }
}

/**
 * The type Base.
 */
class Base {
    
    private String name = "base";

    /**
     * Instantiates a new Base.
     */
    public Base() {
        tellName();
        printName();
    }

    /**
     * Tell name.
     */
    public void tellName() {
        System.out.println("Base tell name: " + name);
    }

    /**
     * Print name.
     */
    public void printName() {
        System.out.println("Base print name: " + name);
    }
}
