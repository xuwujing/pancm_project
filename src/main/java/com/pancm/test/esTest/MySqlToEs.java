package com.pancm.test.esTest;

import com.pancm.util.DBDataBaseUtil;

import java.util.List;
import java.util.Map;


/**
 * @author pancm
 * @Title: pancm_project
 * @Description: MySql的表结构转换成Es中Mapping
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2019/4/24
 */
public class MySqlToEs {

    private static String url="jdbc:mysql://192.169.0.23:3306/DB1000?useUnicode=true&characterEncoding=utf8";
    private static String username="root";
    private static String pwd="123456";
    private static String tableName="MT_TASK_HH";

    public static void main(String[] args) {
        DBDataBaseUtil.init(url,username,pwd);

        List<Map<String,String>> list= DBDataBaseUtil.getColumnNamesAndTypes(tableName);

       list.forEach(System.out::println);



    }

}
