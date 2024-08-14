package com.brenden.cloud.config;

import com.alibaba.csp.sentinel.adapter.spring.webflux.SentinelWebFluxFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 *
 * </p>
 *
 * @author lxq
 * @since 2024/8/13
 */
@Configuration
public class SentinelConfiguration {

    @Bean
    public SentinelWebFluxFilter sentinelWebFluxFilter() {
        return new SentinelWebFluxFilter();
    }
}
