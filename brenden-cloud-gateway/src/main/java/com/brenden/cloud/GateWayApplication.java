package com.brenden.cloud;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * <p>
 *
 * </p>
 *
 * @author lxq
 * @since 2023/12/6
 */

@SpringBootApplication
@EnableConfigurationProperties
@RequiredArgsConstructor
public class GateWayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GateWayApplication.class, args);
    }

}
