package com.brenden.cloud.auth.introspector;

import com.brenden.cloud.auth.model.SecurityUserDetails;
import com.brenden.cloud.auth.repository.RedisOAuth2AuthorizationRepository;
import com.brenden.cloud.auth.utils.SecurityJacksonUtils;
import com.brenden.cloud.base.error.GlobalCodeEnum;
import com.brenden.cloud.core.utils.SignUtil;
import io.micrometer.common.lang.NonNullApi;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.MapUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.BadOpaqueTokenException;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.security.Principal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 *
 * </p>
 *
 * @author lxq
 * @since 2024/6/6
 */
@Component
@NonNullApi
@RequiredArgsConstructor
public class CustomizedOpaqueTokenIntrospector implements OpaqueTokenIntrospector, WebMvcConfigurer {

    private final RedisOAuth2AuthorizationRepository oAuth2AuthorizationRepository;

    private final UserContextInterceptor userContextInterceptor;


    @Override
    public OAuth2AuthenticatedPrincipal introspect(String token) {
        return oAuth2AuthorizationRepository.findByAccessTokenValue(token).map(redisOAuth2Authorization -> {
            Instant nowAt = Instant.now();
            long expireTime = ChronoUnit.SECONDS.between(nowAt, redisOAuth2Authorization.getAccessTokenExpiresAt());
            if (expireTime > 3) {
                String attributes = redisOAuth2Authorization.getAttributes();
                Map<String, Object> map = SecurityJacksonUtils.toMap(attributes);
                UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) map.get(Principal.class.getName());
                SecurityUserDetails principal = (SecurityUserDetails) Objects.requireNonNull(authenticationToken).getPrincipal();
                principal.setKey(MapUtils.getString(map, SignUtil.KEY_NAME));
                return principal;
            }
            throw new BadOpaqueTokenException(GlobalCodeEnum.GC_800004.getMsg());
        }).orElseThrow(() -> new BadOpaqueTokenException(GlobalCodeEnum.GC_800004.getMsg()));
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userContextInterceptor);
    }

}
