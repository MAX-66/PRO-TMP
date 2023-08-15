package com.brenden.cloud.redis.config;

import com.brenden.cloud.redis.utils.RedisUtil;
import com.brenden.cloud.redis.utils.RedissonUtil;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

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
@ConditionalOnClass(LettuceConnectionFactory.class)
@ComponentScan("com.brenden.cloud.redis")
public class RedisAutoConfiguration {


    @Bean
    @ConditionalOnMissingBean(RedisTemplate.class)
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(lettuceConnectionFactory);
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = getJsonRedisSerializer();
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    private Jackson2JsonRedisSerializer<Object> getJsonRedisSerializer() {
        // Json序列化配置
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.PROPERTY);
        return new Jackson2JsonRedisSerializer<>(om, Object.class);
    }

    @Bean
    public RedisUtil redisUtil(RedisTemplate<String, Object> redisTemplate) {
        return new RedisUtil(redisTemplate);
    }

    @Bean
    public RedissonUtil redissonUtils(RedissonClient redissonClient) {
        return new RedissonUtil(redissonClient);
    }

}
