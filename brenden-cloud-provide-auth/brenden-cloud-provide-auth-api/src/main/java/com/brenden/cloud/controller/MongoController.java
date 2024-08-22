package com.brenden.cloud.controller;

import com.brenden.cloud.mongodb.entity.MongoPageEntity;
import com.brenden.cloud.test.MongodbServiceImpl;
import com.brenden.cloud.test.persistence.model.User;
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
 * @since 2024/8/22
 */
@RestController
@RequestMapping("/mongo")
@RequiredArgsConstructor
@Tag(name = "mongo")
public class MongoController {

    private final MongodbServiceImpl mongodbService;

    @Operation(summary = "save")
    @GetMapping("/test")
    public void get() {
         mongodbService.save();
    }

    @Operation(summary = "page")
    @GetMapping("/page")
    public MongoPageEntity<User> page() {
        return mongodbService.query();
    }

}
