package com.pancm.test.tool;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.pancm.test.pojoTest.Student;
import com.pancm.test.pojoTest.User;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author pancm
 * @Title: pancm_project
 * @Description: hutool工具类测试
 * 参考api
 * https://apidoc.gitee.com/dromara/hutool/
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2023/3/23
 */
public class HutoolTest {

    public static void main(String[] args) {
        test1();
        test2();
        test3();
        test4();
        test5();
        test6();
    }

    private static void test6() {
        String url ="https://restapi.amap.com/v3/direction/walking?origin=116.434307,39.90909&destination=116.434446,39.90816&key=44cfa7574786e7f35a7f58b545f57819";
        System.out.println(HttpUtil.get(url));
        String address = "1111";
        //脱敏
        DesensitizedUtil.desensitized(address, DesensitizedUtil.DesensitizedType.ADDRESS);

    }
      private static void test5() {
        String msg="123456";
        System.out.println(DigestUtil.md5Hex16(msg));

    }

    private static void test4() {
        System.out.println(FileUtil.readableFileSize(new File("test.properties")));

    }

    private static void test3() {
        String jsonStr= "{\"pageNum\":1,\"pageSize\":10,\"billStatus\":1,\"generationStartDate\":\"2023-03-01\",\"generationEndDate\":\"2023-03-20\"}";
        System.out.println(JSONUtil.toJsonStr(jsonStr));
    }

    private static void test2() {
        System.out.println(BeanUtil.beanToMap(User.class));
        List<User> userList = new ArrayList<User>();
        for(int i=1;i<=10;i++){
            userList.add(new User(i,"张三"+i));
        }
        List<Student> studentList = new ArrayList<Student>();

        studentList = BeanUtil.copyToList(userList,Student.class);
        System.out.println("转换后的数据:"+studentList);
    }

    private static  void test1(){
        System.out.println(DateUtil.date(new Date()));
        System.out.println(DateUtil.format(new Date(),"yyyy-MM-dd"));
        System.out.println(DateUtil.now());
        System.out.println(DateUtil.parse(DateUtil.now(),"yyyy-MM-dd"));
        System.out.println(DateUtil.endOfDay(new Date()));
    }


}
