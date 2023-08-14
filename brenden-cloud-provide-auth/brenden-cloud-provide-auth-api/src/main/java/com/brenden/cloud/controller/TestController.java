package com.brenden.cloud.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *
 * </p>
 *
 * @author brenden
 * @since 2023/8/7
 */
@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

  /*  @Autowired(required = false)
    private  RedisUtil redisUtil;

    @RequestMapping("/set")
    public void set() {
        redisUtil.set("test", "test");
    }

    @RequestMapping("/get")
    public Object get() {
       return redisUtil.get("test");
    }*/
}
