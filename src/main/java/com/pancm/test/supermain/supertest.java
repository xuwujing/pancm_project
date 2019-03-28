package com.pancm.test.supermain;


/**
 * The type Supertest.
 *
 * @author ZERO
 * @Data 2017 -5-24 下午4:27:42
 * @Description
 */
public class supertest {
    /**
     * The type Father.
     */
    static class Father {
        /**
         * Instantiates a new Father.
         */
        Father(){
			System.out.println("father");  
			  };
		}

    /**
     * The type Son.
     */
    static	class Son {
        /**
         * Instantiates a new Son.
         */
        Son(){
			  System.out.println("son");
			 };
		}

    /**
     * Main.
     *
     * @param args the args
     */
    public static  void main(String[] args){
			Father fa= new  Father();
			Son so=new Son();
		}
		
}
