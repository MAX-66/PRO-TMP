package com.brenden.cloud.core.config;

import com.brenden.cloud.core.utils.JacksonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

/**
 * <p>
 *
 * </p>
 *
 * @author lxq
 * @since 2024/7/18
 */
@Configuration
@Slf4j
public class HttpMessageConverterConfig {

    @Bean("jackson2HttpMessageConverter")
    @Order(Ordered.LOWEST_PRECEDENCE - 10000)
    public MappingJackson2HttpMessageConverter jackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(JacksonUtil.getObjectMapper());
        log.info("jackson init end");
        return converter;
    }
}
