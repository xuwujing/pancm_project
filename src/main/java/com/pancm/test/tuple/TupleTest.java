package com.pancm.test.tuple;

import org.javatuples.Pair;

/**
 * @author pancm
 * @Title: pancm_project
 * @Description: Tuple 是 python的元组，使用此包可以返回多个返回类型数据
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2020/9/2
 */
public class TupleTest {

    public static void main(String[] args) {
        Pair<Boolean, String> pair1 = test1();
        System.out.println(pair1.getValue0()+"  "+ pair1.getValue1());
    }

    /**
     * @Author pancm
     * @Description 两个类型
     * @Date  2020/9/2
     * @Param []
     * @return org.javatuples.Pair<java.lang.Boolean,java.lang.String>
     **/
    private static Pair<Boolean, String> test1(){
        boolean flag = true;
        String name = "xuwujing";
        System.out.println(" 状态:"+ flag + "name" + name);
        return new Pair<Boolean, String>(flag, name);
    }


}


