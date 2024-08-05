package com.brenden.cloud.auth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

import static org.springframework.data.redis.core.RedisKeyValueAdapter.EnableKeyspaceEvents.OFF;

/**
 * <p>
 *
 * </p>
 *
 * @author lxq
 * @since 2024/8/5
 */
@Configuration
@EnableRedisRepositories(enableKeyspaceEvents = OFF,  basePackages = { "com.brenden.cloud.auth.repository"})
public class RepositoryConfig {
}
