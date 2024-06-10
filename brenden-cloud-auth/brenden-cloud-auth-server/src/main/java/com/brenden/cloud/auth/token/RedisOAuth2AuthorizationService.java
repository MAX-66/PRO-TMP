package com.brenden.cloud.auth.token;

import com.brenden.cloud.auth.model.RedisOAuth2Authorization;
import com.brenden.cloud.auth.repository.RedisOAuth2AuthorizationRepository;
import com.brenden.cloud.utils.JacksonUtil;
import com.fasterxml.jackson.databind.type.MapType;
import jodd.util.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2DeviceCode;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.OAuth2UserCode;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.security.Principal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.Optional;

import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.STATE;

/**
 * <p>
 *
 * </p>
 *
 * @author lxq
 * @since 2023/9/8
 */
@RequiredArgsConstructor
public class RedisOAuth2AuthorizationService implements OAuth2AuthorizationService {

    private final RedisOAuth2AuthorizationRepository redisOAuth2AuthorizationRepository;

    private final RegisteredClientRepository registeredClientRepository;


    static {
        JacksonUtil.getObjectMapper().registerModule(new OAuth2AuthorizationServerJackson2Module())
                .registerModules(SecurityJackson2Modules.getModules(RedisOAuth2AuthorizationService.class.getClassLoader()));
    }

    @Override
    public void save(OAuth2Authorization authorization) {
        Assert.isTrue(Objects.nonNull(authorization), "authorization is null");
        RedisOAuth2Authorization redisOAuth2Authorization = convent(authorization);
        redisOAuth2Authorization.setTtl(ChronoUnit.SECONDS.between(Instant.now(), redisOAuth2Authorization.getAccessTokenExpiresAt()));
        redisOAuth2AuthorizationRepository.deleteById(authorization.getId());
        redisOAuth2AuthorizationRepository.save(redisOAuth2Authorization);
    }

    @Override
    public void remove(OAuth2Authorization authorization) {
        Assert.isTrue(Objects.nonNull(authorization), "authorization is null");
        redisOAuth2AuthorizationRepository.deleteById(authorization.getId());
    }

    @Override
    public OAuth2Authorization findById(String id) {
        Optional<RedisOAuth2Authorization> authorizationOptional = redisOAuth2AuthorizationRepository.findById(id);
        return authorizationOptional.map(this::convent).orElseThrow();
    }

    @Override
    public OAuth2Authorization findByToken(String token, OAuth2TokenType tokenType) {
        return redisOAuth2AuthorizationRepository.findByAccessTokenValue(token).map(this::convent).orElse(null);
    }

    private RedisOAuth2Authorization convent(OAuth2Authorization authorization) {
        RedisOAuth2Authorization redisOAuth2Authorization = new RedisOAuth2Authorization();
        redisOAuth2Authorization.setId(authorization.getId());
        redisOAuth2Authorization.setRegisteredClientId(authorization.getRegisteredClientId());
        redisOAuth2Authorization.setPrincipalName(authorization.getPrincipalName());
        redisOAuth2Authorization.setAuthorizationGrantType(authorization.getAuthorizationGrantType().getValue());
        redisOAuth2Authorization.setAuthorizedScopes(StringUtils.collectionToDelimitedString(authorization.getAuthorizedScopes(), ","));
        redisOAuth2Authorization.setAttributes(JacksonUtil.toJson(authorization.getAttributes()));
        redisOAuth2Authorization.setState(authorization.getAttribute(STATE));
        // 添加 access token 信息
        setAccessToken(redisOAuth2Authorization, authorization.getAccessToken());
        // 添加 refresh token 信息
        setRefreshToken(redisOAuth2Authorization, authorization.getRefreshToken());
        // 添加 oidc token 信息
        setOidcIdToken(redisOAuth2Authorization, authorization.getToken(OidcIdToken.class));
        // 添加授权码信息
        setAuthorizationCode(redisOAuth2Authorization, authorization.getToken(OAuth2AuthorizationCode.class));
        // 添加用户码信息
        setUserCode(redisOAuth2Authorization, authorization.getToken(OAuth2UserCode.class));
        // 添加设备码信息
        setDeviceCode(redisOAuth2Authorization, authorization.getToken(OAuth2DeviceCode.class));
        return redisOAuth2Authorization;
    }

    private OAuth2Authorization convent(RedisOAuth2Authorization authorization) {
        RegisteredClient registeredClient = registeredClientRepository.findById(authorization.getRegisteredClientId());
        Assert.isTrue(Objects.nonNull(registeredClient), "registeredClient is null");
        OAuth2Authorization.Builder authorizationBuilder = OAuth2Authorization.withRegisteredClient(registeredClient)
                .id(authorization.getId())
                .principalName(authorization.getPrincipalName())
                .authorizedScopes(registeredClient.getScopes())
                .attribute(Principal.class.getName(), authorization.getAttributes())
                .authorizationGrantType(new AuthorizationGrantType(authorization.getAuthorizationGrantType()))
                .authorizedScopes(StringUtils.commaDelimitedListToSet(authorization.getAuthorizedScopes()))
                .attributes(attributesConsumer -> attributesConsumer.putAll(JacksonUtil.toMap(authorization.getAttributes())));
        if (StringUtil.isNotEmpty(authorization.getState())) {
            authorizationBuilder.attribute(STATE, authorization.getState());
        }
        // access token
        parseAccessToken(authorizationBuilder, authorization);
        // refresh token
        parseRefreshToken(authorizationBuilder, authorization);
        // authorization code
        parseAuthorizationCode(authorizationBuilder, authorization);
        // oidc id token
        parseOidcIdToken(authorizationBuilder, authorization);
        // user code
        parseUserCode(authorizationBuilder, authorization);
        // device code
        parseDeviceCode(authorizationBuilder, authorization);
        return authorizationBuilder.build();
    }


    private void parseAccessToken(OAuth2Authorization.Builder builder, RedisOAuth2Authorization redisOAuth2Authorization) {
        String accessTokenValue = redisOAuth2Authorization.getAccessTokenValue();
        if (Objects.nonNull(accessTokenValue)) {
            OAuth2AccessToken accessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER, accessTokenValue,
                    redisOAuth2Authorization.getAccessTokenIssuedAt(),
                    redisOAuth2Authorization.getAccessTokenExpiresAt(),
                    StringUtils.commaDelimitedListToSet(redisOAuth2Authorization.getAccessTokenScopes()));
            builder.token(accessToken,
                    metadata -> metadata.putAll(JacksonUtil.toMap(redisOAuth2Authorization.getAccessTokenMetadata())));
        }
    }

    private void parseRefreshToken(OAuth2Authorization.Builder builder, RedisOAuth2Authorization redisOAuth2Authorization) {
        String refreshTokenValue = redisOAuth2Authorization.getRefreshTokenValue();
        if (Objects.nonNull(refreshTokenValue)) {
            OAuth2RefreshToken refreshToken = new OAuth2RefreshToken(refreshTokenValue,
                    redisOAuth2Authorization.getRefreshTokenIssuedAt(),
                    redisOAuth2Authorization.getRefreshTokenExpiresAt());
            builder.token(refreshToken,
                    metadata -> metadata.putAll(JacksonUtil.toMap(redisOAuth2Authorization.getRefreshTokenMetadata())));
        }
    }

    private void parseAuthorizationCode(OAuth2Authorization.Builder builder, RedisOAuth2Authorization redisOAuth2Authorization) {
        String authorizationCodeValue = redisOAuth2Authorization.getAuthorizationCodeValue();
        if (Objects.nonNull(authorizationCodeValue)) {
            OAuth2AuthorizationCode authorizationCode = new OAuth2AuthorizationCode(authorizationCodeValue,
                    redisOAuth2Authorization.getAuthorizationCodeIssuedAt(),
                    redisOAuth2Authorization.getAuthorizationCodeExpiresAt());
            builder.token(authorizationCode, metadata -> metadata
                    .putAll(JacksonUtil.toMap(redisOAuth2Authorization.getAuthorizationCodeMetadata())));
        }
    }

    private void parseOidcIdToken(OAuth2Authorization.Builder builder, RedisOAuth2Authorization redisOAuth2Authorization) {
        String oidcIdTokenValue = redisOAuth2Authorization.getOidcIdTokenValue();
        if (Objects.nonNull(oidcIdTokenValue)) {
            OidcIdToken oidcIdToken = new OidcIdToken(oidcIdTokenValue,
                    redisOAuth2Authorization.getOidcIdTokenIssuedAt(),
                    redisOAuth2Authorization.getOidcIdTokenExpiresAt(),
                    JacksonUtil.toMap(redisOAuth2Authorization.getOidcIdTokenClaims()));
            builder.token(oidcIdToken,
                    metadata -> metadata.putAll(JacksonUtil.toMap(redisOAuth2Authorization.getOidcIdTokenMetadata())));
        }
    }

    private void parseUserCode(OAuth2Authorization.Builder builder, RedisOAuth2Authorization redisOAuth2Authorization) {
        String userCodeValue = redisOAuth2Authorization.getUserCodeValue();
        if (Objects.nonNull(userCodeValue)) {
            OAuth2UserCode userCode = new OAuth2UserCode(userCodeValue, redisOAuth2Authorization.getUserCodeIssuedAt(),
                    redisOAuth2Authorization.getUserCodeExpiresAt());
            builder.token(userCode,
                    metadata -> metadata.putAll(JacksonUtil.toMap(redisOAuth2Authorization.getUserCodeMetadata())));
        }
    }

    private void parseDeviceCode(OAuth2Authorization.Builder builder, RedisOAuth2Authorization redisOAuth2Authorization) {
        String deviceCodeValue = redisOAuth2Authorization.getDeviceCodeValue();
        if (Objects.nonNull(deviceCodeValue)) {
            OAuth2DeviceCode deviceCode = new OAuth2DeviceCode(deviceCodeValue,
                    redisOAuth2Authorization.getDeviceCodeIssuedAt(),
                    redisOAuth2Authorization.getDeviceCodeExpiresAt());
            builder.token(deviceCode,
                    metadata -> metadata.putAll(JacksonUtil.toMap(redisOAuth2Authorization.getDeviceCodeMetadata())));
        }
    }
    
    private void setAccessToken(RedisOAuth2Authorization redisOAuth2Authorization, OAuth2Authorization.Token<OAuth2AccessToken> token) {
        if (Objects.nonNull(token)) {
            OAuth2AccessToken accessToken = token.getToken();
            redisOAuth2Authorization.setAccessTokenValue(accessToken.getTokenValue());
            redisOAuth2Authorization.setAccessTokenType(accessToken.getTokenType().getValue());
            redisOAuth2Authorization.setAccessTokenExpiresAt(accessToken.getExpiresAt());
            redisOAuth2Authorization.setAccessTokenIssuedAt(accessToken.getIssuedAt());
            redisOAuth2Authorization.setAccessTokenScopes(StringUtils.collectionToDelimitedString(accessToken.getScopes(), ","));
            redisOAuth2Authorization.setAccessTokenMetadata(JacksonUtil.toJson(token.getMetadata()));
        }
    }

    private void setRefreshToken(RedisOAuth2Authorization redisOAuth2Authorization, OAuth2Authorization.Token<OAuth2RefreshToken> token) {
        if (Objects.nonNull(token)) {
            OAuth2RefreshToken refreshToken = token.getToken();
            redisOAuth2Authorization.setRefreshTokenValue(refreshToken.getTokenValue());
            redisOAuth2Authorization.setRefreshTokenExpiresAt(refreshToken.getExpiresAt());
            redisOAuth2Authorization.setRefreshTokenIssuedAt(refreshToken.getIssuedAt());
            redisOAuth2Authorization.setRefreshTokenMetadata(JacksonUtil.toJson(token.getMetadata()));
        }
    }

    private void setOidcIdToken(RedisOAuth2Authorization redisOAuth2Authorization, OAuth2Authorization.Token<OidcIdToken> token) {
        if (Objects.nonNull(token)) {
            OidcIdToken oidcIdToken = token.getToken();
            redisOAuth2Authorization.setOidcIdTokenValue(oidcIdToken.getTokenValue());
            redisOAuth2Authorization.setOidcIdTokenExpiresAt(oidcIdToken.getExpiresAt());
            redisOAuth2Authorization.setOidcIdTokenIssuedAt(oidcIdToken.getIssuedAt());
            redisOAuth2Authorization.setOidcIdTokenClaims(JacksonUtil.toJson(oidcIdToken.getClaims()));
            redisOAuth2Authorization.setOidcIdTokenMetadata(JacksonUtil.toJson(token.getMetadata()));
        }
    }

    private void setAuthorizationCode(RedisOAuth2Authorization redisOAuth2Authorization, OAuth2Authorization.Token<OAuth2AuthorizationCode> token) {
        if (Objects.nonNull(token)) {
            OAuth2AuthorizationCode authorizationCode = token.getToken();
            redisOAuth2Authorization.setAuthorizationCodeValue(authorizationCode.getTokenValue());
            redisOAuth2Authorization.setAuthorizationCodeExpiresAt(authorizationCode.getExpiresAt());
            redisOAuth2Authorization.setAuthorizationCodeIssuedAt(authorizationCode.getIssuedAt());
            redisOAuth2Authorization.setAuthorizationCodeMetadata(JacksonUtil.toJson(token.getMetadata()));
        }
    }

    private void setUserCode(RedisOAuth2Authorization redisOAuth2Authorization, OAuth2Authorization.Token<OAuth2UserCode> token) {
        if (Objects.nonNull(token)) {
            OAuth2UserCode userCode = token.getToken();
            redisOAuth2Authorization.setUserCodeValue(userCode.getTokenValue());
            redisOAuth2Authorization.setUserCodeExpiresAt(userCode.getExpiresAt());
            redisOAuth2Authorization.setUserCodeIssuedAt(userCode.getIssuedAt());
            redisOAuth2Authorization.setUserCodeMetadata(JacksonUtil.toJson(token.getMetadata()));
        }
    }

    private void setDeviceCode(RedisOAuth2Authorization redisOAuth2Authorization, OAuth2Authorization.Token<OAuth2DeviceCode> token) {
        if (Objects.nonNull(token)) {
            OAuth2DeviceCode deviceCode = token.getToken();
            redisOAuth2Authorization.setDeviceCodeValue(deviceCode.getTokenValue());
            redisOAuth2Authorization.setDeviceCodeExpiresAt(deviceCode.getExpiresAt());
            redisOAuth2Authorization.setDeviceCodeIssuedAt(deviceCode.getIssuedAt());
            redisOAuth2Authorization.setDeviceCodeMetadata(JacksonUtil.toJson(token.getMetadata()));
        }
    }

}
