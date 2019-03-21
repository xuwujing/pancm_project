package com.pancm.test.springBootTest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Test 2.
 */
@RestController
public class test2 {
    /**
     * Index string.
     *
     * @return the string
     */
    @RequestMapping("/hello")
    public String index() {
        return "Hello World";
    }
}