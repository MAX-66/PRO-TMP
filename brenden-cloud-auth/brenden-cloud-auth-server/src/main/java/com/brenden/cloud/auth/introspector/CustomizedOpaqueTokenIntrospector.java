package com.brenden.cloud.auth.introspector;

import com.brenden.cloud.auth.authentication.PasswordAuthenticationToken;
import com.brenden.cloud.auth.user.SecurityUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.DefaultOAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
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
public class CustomizedOpaqueTokenIntrospector implements OpaqueTokenIntrospector {

    private final OAuth2AuthorizationService oAuth2AuthorizationService;
    @Override
    public OAuth2AuthenticatedPrincipal introspect(String token) {
        OAuth2Authorization oAuth2Authorization = oAuth2AuthorizationService.findByToken(token, OAuth2TokenType.ACCESS_TOKEN);
        if (Objects.isNull(oAuth2Authorization)) {
            throw new OAuth2AuthenticationException("400");
        }
        OAuth2Authorization.Token<OAuth2AccessToken> accessToken = oAuth2Authorization.getAccessToken();
        Instant expiresAt = accessToken.getToken().getExpiresAt();
        Instant nowAt = Instant.now();
        long expireTime = ChronoUnit.SECONDS.between(nowAt, expiresAt);
        if (expireTime > 3) {
            Object obj = oAuth2Authorization.getAttribute(Principal.class.getName());
            UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) obj;
            return (SecurityUserDetails) Objects.requireNonNull(authenticationToken).getPrincipal();
        }
        throw new OAuth2AuthenticationException("499");
    }
}
