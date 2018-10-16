package com.pancm.test.springBootTest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class test2 {
    @RequestMapping("/hello")
    public String index() {
        return "Hello World";
    }
}