package com.brenden.cloud.auth.repository;

import com.brenden.cloud.auth.model.RedisOAuth2Authorization;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static com.brenden.cloud.auth.constants.OauthConstants.OAUTH2_ACCESS_TOKEN_PREFIX;

@Service
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
@RequiredArgsConstructor
public class ReactiveOAuth2AuthorizationService{

    private final ReactiveRedisTemplate<String, Object> reactiveRedisTemplate;


    /**
     * 根据access token获取认证信息
     *
     * @param token access token
     * @return 认证信息
     */
    public Mono<RedisOAuth2Authorization> findByAccessTokenValue(String token) {
        String indexKey = OAUTH2_ACCESS_TOKEN_PREFIX + token;
        return reactiveRedisTemplate.keys(indexKey+ ":*").next()
                .flatMap(key -> reactiveRedisTemplate.opsForValue().get(key).cast(RedisOAuth2Authorization.class));
    }

}
