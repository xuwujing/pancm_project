package com.pancm.test.threeCharactersTest;

/**
 * The type Packaging test.
 *
 * @author pancm
 * @Title: PackagingTest
 * @Description: 封装
 * @Version:1.0.0
 * @date 2018年3月27日
 */
public class PackagingTest {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        User user=new User();
  //这里会报错，因为id和name是私有的，用于保护该数据
//      user.id=10;
//      user.name="张三";
        user.setId(1);
        user.setName("张三");
        System.out.println(user.getId());
        System.out.println(user.getName());
    }

}

/**
 * The type User.
 */
class User{
    private int id;
    private String name;

    /**
     * Gets id.
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
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