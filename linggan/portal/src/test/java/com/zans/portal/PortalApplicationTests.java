package com.zans.portal;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
public class PortalApplicationTests {

    @Before
    public void init() {
        System.out.println("开始测试-----------------1");
    }

    @After
    public void after() {
        System.out.println("测试结束-----------------");
    }

    @Test
    public void test1(){

    }


}
