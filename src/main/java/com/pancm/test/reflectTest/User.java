package com.pancm.test.reflectTest;

/**
 * Title: User
 * Description:
 * 反射测试实体类
 * Version:1.0.0
 *
 * @author pancm
 * @date 2018年2月9日
 */
public class User {
	private String name;

    /**
     * Instantiates a new User.
     */
//构造方法1（默认构造方法）***********************
    public User(){  
          
    }

    /**
     * Instantiates a new User.
     *
     * @param name the name
     */
//构造方法2
    public User(String name){  
        this.name=name;  
    }

    /**
     * Get message.
     */
//******自定义方法*************
    public void getMessage(){  
        System.out.print("反射测试");  
    }

    /**
     * Get message 2 string.
     *
     * @param num the num
     * @return the string
     */
//******自定义方法2*************
    public String getMessage2(int num){  
    	String str=num+"反射测试!";
        System.out.print(str);
		return str;  
    }  
    
    //******自定义方法3*************  
    private String getMessage3(){  
    	String str="这是一个私有的方法!";
        System.out.print(str);
		return str;  
    }


    /**
     * Get message 4 string.
     *
     * @param s the s
     * @return the string
     */
//******自定义方法4*************
    public static String getMessage4(String s){  
    	String str=s+"这是一个静态的方法!";
        System.out.print(str);
		return str;  
    }  
    
    //******重写toString方法，在测试的时候会用到*****  
    @Override  
    public String toString() {  
        return "name:"+this.name;  
    }

    /**
     * Gets name.
     *
     * @return the name
     */
//**************************
    public String getName() {  
        return name;  
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;  
    } 
}
