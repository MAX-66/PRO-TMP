package com.brenden.cloud.auth.introspector;

import com.brenden.cloud.auth.model.SecurityUserDetails;
import com.brenden.cloud.auth.repository.ReactiveOAuth2AuthorizationService;
import com.brenden.cloud.error.GlobalCodeEnum;
import com.brenden.cloud.utils.JacksonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.BadOpaqueTokenException;
import org.springframework.security.oauth2.server.resource.introspection.ReactiveOpaqueTokenIntrospector;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

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
@RequiredArgsConstructor
public class CustomizedOpaqueTokenIntrospector implements ReactiveOpaqueTokenIntrospector {

    private final ReactiveOAuth2AuthorizationService oAuth2AuthorizationRepository;
    @Override
    public Mono<OAuth2AuthenticatedPrincipal> introspect(String token) {
        return oAuth2AuthorizationRepository.findByAccessTokenValue(token)
                .flatMap(redisOAuth2Authorization -> {
                    Instant nowAt = Instant.now();
                    long expireTime = ChronoUnit.SECONDS.between(nowAt, redisOAuth2Authorization.getAccessTokenExpiresAt());
                    if (expireTime > 3) {
                        String attributes = redisOAuth2Authorization.getAttributes();
                        Map<String, Object> map = JacksonUtil.toMap(attributes);
                        UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) map.get(Principal.class.getName());
                        return Mono.just(((SecurityUserDetails) Objects.requireNonNull(authenticationToken).getPrincipal()));
                    }
                    return Mono.error( new BadOpaqueTokenException(GlobalCodeEnum.GC_800004.getMsg()));
                });
    }
}
