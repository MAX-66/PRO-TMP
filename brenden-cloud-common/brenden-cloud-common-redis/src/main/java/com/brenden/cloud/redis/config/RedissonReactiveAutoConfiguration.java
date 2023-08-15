package com.brenden.cloud.redis.config;

import org.redisson.api.RedissonClient;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.api.RedissonRxClient;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * <p>
 *
 * </p>
 *
 * @author lxq
 * @since 2023/8/14
 */

@AutoConfiguration
@ComponentScan("com.brenden.cloud.redis")
@ConditionalOnClass({RedissonClient.class, ReactiveRedisConnectionFactory.class })
public class RedissonReactiveAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean({StringRedisTemplate.class})
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

    @Bean
    @Lazy
    @ConditionalOnMissingBean({RedissonReactiveClient.class})
    public RedissonReactiveClient redissonReactive(RedissonClient redisson) {
        return redisson.reactive();
    }

    @Bean
    @Lazy
    @ConditionalOnMissingBean({RedissonRxClient.class})
    public RedissonRxClient redissonRxJava(RedissonClient redisson) {
        return redisson.rxJava();
    }


}
