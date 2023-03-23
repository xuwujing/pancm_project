package com.pancm.test.tool;


import cn.hutool.*;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.poi.excel.ExcelUtil;
import org.apache.hadoop.hbase.security.User;

import java.io.File;
import java.util.Date;

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
    }

    private static  void test1(){
        System.out.println(DateUtil.date(new Date()));
        System.out.println(DateUtil.format(new Date(),"yyyy-MM-dd"));
    }


}
