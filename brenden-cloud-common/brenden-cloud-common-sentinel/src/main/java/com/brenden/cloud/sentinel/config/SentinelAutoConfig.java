package com.brenden.cloud.sentinel.config;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.brenden.cloud.sentinel.handler.SentinelBlockExceptionHandler;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * <p>
 *
 * </p>
 *
 * @author lxq
 * @since 2024/8/14
 */
@AutoConfiguration
public class SentinelAutoConfig {

    @Bean
    @ConditionalOnMissingBean(BlockExceptionHandler.class)
    public BlockExceptionHandler blockExceptionHandler() {
        return new SentinelBlockExceptionHandler();
    }
}
