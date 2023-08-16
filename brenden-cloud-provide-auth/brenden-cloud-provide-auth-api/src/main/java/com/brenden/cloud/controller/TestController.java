package com.brenden.cloud.controller;

import com.brenden.cloud.redis.utils.RedisUtil;
import com.brenden.cloud.redis.utils.RedissonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
@Api("redis操作")
public class TestController {

    @Autowired

    private RedisUtil redisUtil;

    @ApiOperation(value = "写入redis")
    @RequestMapping("/set")
    public void set() {
        redisUtil.hSet("test:controller:userId", "12306", "12306", RedissonUtil.HOUR_TWO);
    }

    @RequestMapping("/get")
    public Object get() {
       return redisUtil.hGet("test:controller:userId","12306");
    }
}
