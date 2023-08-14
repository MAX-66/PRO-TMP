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
 * @author brenden
 * @since 2023/8/6
 */
@SpringBootApplication
@EnableConfigurationProperties
@RequiredArgsConstructor
public class AuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }

}
