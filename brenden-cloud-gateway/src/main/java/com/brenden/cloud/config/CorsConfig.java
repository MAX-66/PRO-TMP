package com.brenden.cloud.config;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

/**
 * <p>
 *
 * </p>
 *
 * @author lxq
 * @since 2024/7/31
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsWebFilter corsFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.addAllowedOrigin(CorsConfiguration.ALL);
        corsConfig.setAllowedMethods(Arrays.stream(HttpMethod.values()).map(HttpMethod::name).toList()); // 允许的HTTP方法
        corsConfig.setAllowedHeaders(Collections.singletonList(CorsConfiguration.ALL)); // 允许的请求头
        corsConfig.setAllowCredentials(true); // 是否允许发送凭证
        corsConfig.addExposedHeader(CorsConfiguration.ALL);
        corsConfig.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);
        return new CorsWebFilter(source);
    }

}
