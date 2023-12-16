package com.brenden.cloud.redis.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * <p>
 *
 * </p>
 *
 * @author lxq
 * @since 2023/8/10
 */
@AutoConfiguration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@EnableConfigurationProperties(RedisProperties.class)
public class RedissonConfiguration {

    @Bean(destroyMethod = "shutdown", name = "redissonClient")
    @ConditionalOnMissingBean(RedissonClient.class)
    public RedissonClient redissonClient(RedisProperties properties) {
        int timeout = (int) properties.getTimeout().toMillis();
        int connectTimeout = (int) properties.getConnectTimeout().toMillis();
        Config config = new Config();
        config.useSingleServer().setDatabase(properties.getDatabase())
                .setAddress("redis://" + properties.getHost() + ":" + properties.getPort())
                .setPassword(properties.getPassword())
                .setTimeout(timeout).setConnectTimeout(connectTimeout);
       return Redisson.create(config);
    }

}
