package com.brenden.cloud.test.controller;

import com.brenden.cloud.api.feign.TestFeign;
import com.brenden.cloud.base.entity.BaseEntity;
import com.brenden.cloud.base.entity.ResultEntity;
import com.brenden.cloud.core.annotation.ApiProcess;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *
 * </p>
 *
 * @author lxq
 * @since 2024/8/4
 */

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
@Tag(name = "feign 测试")
public class TestController {

    private final TestFeign testFeign;

    @Operation(summary = "feign 测试")
    @GetMapping("/feign")
    @ApiProcess
    public ResultEntity<BaseEntity> feign(BaseEntity entity) {
        return testFeign.feign(entity);
    }


}
