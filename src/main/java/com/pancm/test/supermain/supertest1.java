package com.pancm.test.supermain;

/**
 * The type Supertest 1.
 *
 * @author ZERO
 * @Data 2017 -5-24 下午4:29:22
 * @Description
 */
public class supertest1 {
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

        /**
         * Instantiates a new Father.
         *
         * @param age the age
         */
        Father(int age){
			System.out.println("father is "+age); 
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
			  super();
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

