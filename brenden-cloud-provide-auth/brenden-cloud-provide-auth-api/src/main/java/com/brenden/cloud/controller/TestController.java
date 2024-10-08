package com.brenden.cloud.controller;

import com.brenden.cloud.base.entity.BaseEntity;
import com.brenden.cloud.base.entity.ResultEntity;
import com.brenden.cloud.core.annotation.ApiProcess;
import com.brenden.cloud.redis.utils.RedisUtil;
import com.brenden.cloud.redis.utils.RedissonUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
@Tag(name = "redis操作")
public class TestController {

    private final RedisUtil redisUtil;

    @Operation(summary = "写入redis")
    @PostMapping("/set")
    public void set() {
        redisUtil.hSet("test:controller:userId", "12306", "12306", RedissonUtil.HOUR_TWO);
    }

    @Operation(summary = "读取redis")
    @GetMapping("/get")
    @PreAuthorize("hasAuthority('SCOPE_profile')")
    public Object get() {
       return redisUtil.hGet("test:controller:userId","12306");
    }

    @Operation(summary = "读取")
    @GetMapping("/get2")
    @ApiProcess
    public ResultEntity<BaseEntity> get2(BaseEntity entity) {
        return ResultEntity.success(entity);
    }

    @Operation(summary = "获取用户")
    @GetMapping("/getUser/{id}")
    public Object getUser(@PathVariable("id") Integer id) {
        return null;
    }


    public static void main(String[] args) {
        System.out.println(System.getenv("JAVA_HOME"));
    }


}
