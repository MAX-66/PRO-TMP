package com.brenden.cloud.auth.service;

import com.brenden.cloud.auth.constants.OauthConstants;
import com.brenden.cloud.auth.model.RedisOAuth2Authorization;
import com.brenden.cloud.auth.repository.RedisOAuth2AuthorizationRepository;
import com.brenden.cloud.auth.utils.SecurityJacksonUtils;
import com.brenden.cloud.redis.utils.RedisUtil;
import jodd.util.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2DeviceCode;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.OAuth2UserCode;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.security.Principal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import static com.brenden.cloud.base.constant.SpecialCharacters.COLON;

/**
 * <p>
 *
 * </p>
 *
 * @author lxq
 * @since 2023/9/8
 */
@RequiredArgsConstructor
@Service
public class RedisOAuth2AuthorizationService implements OAuth2AuthorizationService {

    private final RedisOAuth2AuthorizationRepository redisOAuth2AuthorizationRepository;

    private final RegisteredClientRepository registeredClientRepository;

    private final RedisUtil redisUtil;
    
    @Override
    public void save(OAuth2Authorization authorization) {
        Assert.isTrue(Objects.nonNull(authorization), "authorization is null");

        RedisOAuth2Authorization redisOAuth2Authorization = convent(authorization);
        List<Instant> expireAtList = Stream.of(redisOAuth2Authorization.getAuthorizationCodeExpiresAt(),
                        redisOAuth2Authorization.getAccessTokenExpiresAt(), redisOAuth2Authorization.getOidcIdTokenExpiresAt(),
                        redisOAuth2Authorization.getRefreshTokenExpiresAt(), redisOAuth2Authorization.getUserCodeExpiresAt(),
                        redisOAuth2Authorization.getDeviceCodeExpiresAt())
                .filter(Objects::nonNull)
                .toList();
        expireAtList.stream()
                .max(Comparator.comparing(Instant::getEpochSecond))
                .ifPresent(instant -> redisOAuth2Authorization.setTtl(ChronoUnit.SECONDS.between(Instant.now(), instant)));

        redisOAuth2AuthorizationRepository.deleteById(authorization.getId());
        redisOAuth2AuthorizationRepository.save(redisOAuth2Authorization);

        String idKey = OauthConstants.OAUTH2_AUTHORIZATION_PREFIX + COLON + authorization.getId() + OauthConstants.OAUTH2_AUTHORIZATION_ID_SUFFIX;
        redisUtil.expire(idKey, redisOAuth2Authorization.getTtl());

        String accessTokenKey = OauthConstants.OAUTH2_ACCESS_TOKEN_PREFIX + redisOAuth2Authorization.getAccessTokenValue();
        redisUtil.expire(accessTokenKey, ChronoUnit.SECONDS.between(Instant.now(), redisOAuth2Authorization.getAccessTokenExpiresAt()));

        String refreshTokenKey = OauthConstants.OAUTH2_REFRESH_TOKEN_PREFIX + redisOAuth2Authorization.getRefreshTokenValue();
        redisUtil.expire(refreshTokenKey, ChronoUnit.SECONDS.between(Instant.now(), redisOAuth2Authorization.getRefreshTokenExpiresAt()));
    }

    @Override
    public void remove(OAuth2Authorization authorization) {
        Assert.isTrue(Objects.nonNull(authorization), "authorization is null");
        redisOAuth2AuthorizationRepository.findById(authorization.getId()).ifPresent(redisOAuth2Authorization -> {
            redisOAuth2AuthorizationRepository.deleteById(redisOAuth2Authorization.getId());
            String idKey = OauthConstants.OAUTH2_AUTHORIZATION_PREFIX + authorization.getId();
            String accessTokenKey = OauthConstants.OAUTH2_ACCESS_TOKEN_PREFIX + redisOAuth2Authorization.getAccessTokenValue();
            String refreshTokenKey = OauthConstants.OAUTH2_REFRESH_TOKEN_PREFIX + redisOAuth2Authorization.getRefreshTokenValue();
            redisUtil.del(idKey, accessTokenKey, refreshTokenKey);
        });
    }

    @Override
    public OAuth2Authorization findById(String id) {
        Optional<RedisOAuth2Authorization> authorizationOptional = redisOAuth2AuthorizationRepository.findById(id);
        return authorizationOptional.map(this::convent).orElseThrow();
    }

    @Override
    public OAuth2Authorization findByToken(String token, OAuth2TokenType tokenType) {
        Assert.isTrue(StringUtils.hasLength(token), "token is empty");
        Assert.isTrue(Objects.nonNull(tokenType), "tokenType is null");
        if (OAuth2TokenType.ACCESS_TOKEN.getValue().equals(tokenType.getValue())) {
            return redisOAuth2AuthorizationRepository.findByAccessTokenValue(token).map(this::convent).orElse(null);
        }
        if (OAuth2TokenType.REFRESH_TOKEN.getValue().equals(tokenType.getValue())) {
            return redisOAuth2AuthorizationRepository.findByRefreshTokenValue(token).map(this::convent).orElse(null);
        }
        return null;
    }

    @SneakyThrows
    private RedisOAuth2Authorization convent(OAuth2Authorization authorization) {
        RedisOAuth2Authorization redisOAuth2Authorization = new RedisOAuth2Authorization();
        redisOAuth2Authorization.setId(authorization.getId());
        redisOAuth2Authorization.setRegisteredClientId(authorization.getRegisteredClientId());
        redisOAuth2Authorization.setPrincipalName(authorization.getPrincipalName());
        redisOAuth2Authorization.setAuthorizationGrantType(authorization.getAuthorizationGrantType().getValue());
        redisOAuth2Authorization.setAuthorizedScopes(StringUtils.collectionToDelimitedString(authorization.getAuthorizedScopes(), ","));
        redisOAuth2Authorization.setAttributes(SecurityJacksonUtils.toJson(authorization.getAttributes()));
        redisOAuth2Authorization.setState(authorization.getAttribute(OAuth2ParameterNames.STATE));
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
                .attributes(attributesConsumer -> attributesConsumer.putAll(SecurityJacksonUtils.toMap(authorization.getAttributes())));
        if (StringUtil.isNotEmpty(authorization.getState())) {
            authorizationBuilder.attribute(OAuth2ParameterNames.STATE, authorization.getState());
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
                    metadata -> metadata.putAll(SecurityJacksonUtils.toMap(redisOAuth2Authorization.getAccessTokenMetadata())));
        }
    }

    private void parseRefreshToken(OAuth2Authorization.Builder builder, RedisOAuth2Authorization redisOAuth2Authorization) {
        String refreshTokenValue = redisOAuth2Authorization.getRefreshTokenValue();
        if (Objects.nonNull(refreshTokenValue)) {
            OAuth2RefreshToken refreshToken = new OAuth2RefreshToken(refreshTokenValue,
                    redisOAuth2Authorization.getRefreshTokenIssuedAt(),
                    redisOAuth2Authorization.getRefreshTokenExpiresAt());
            builder.token(refreshToken,
                    metadata -> metadata.putAll(SecurityJacksonUtils.toMap(redisOAuth2Authorization.getRefreshTokenMetadata())));
        }
    }

    private void parseAuthorizationCode(OAuth2Authorization.Builder builder, RedisOAuth2Authorization redisOAuth2Authorization) {
        String authorizationCodeValue = redisOAuth2Authorization.getAuthorizationCodeValue();
        if (Objects.nonNull(authorizationCodeValue)) {
            OAuth2AuthorizationCode authorizationCode = new OAuth2AuthorizationCode(authorizationCodeValue,
                    redisOAuth2Authorization.getAuthorizationCodeIssuedAt(),
                    redisOAuth2Authorization.getAuthorizationCodeExpiresAt());
            builder.token(authorizationCode, metadata -> metadata
                    .putAll(SecurityJacksonUtils.toMap(redisOAuth2Authorization.getAuthorizationCodeMetadata())));
        }
    }

    private void parseOidcIdToken(OAuth2Authorization.Builder builder, RedisOAuth2Authorization redisOAuth2Authorization) {
        String oidcIdTokenValue = redisOAuth2Authorization.getOidcIdTokenValue();
        if (Objects.nonNull(oidcIdTokenValue)) {
            OidcIdToken oidcIdToken = new OidcIdToken(oidcIdTokenValue,
                    redisOAuth2Authorization.getOidcIdTokenIssuedAt(),
                    redisOAuth2Authorization.getOidcIdTokenExpiresAt(),
                    SecurityJacksonUtils.toMap(redisOAuth2Authorization.getOidcIdTokenClaims()));
            builder.token(oidcIdToken,
                    metadata -> metadata.putAll(SecurityJacksonUtils.toMap(redisOAuth2Authorization.getOidcIdTokenMetadata())));
        }
    }

    private void parseUserCode(OAuth2Authorization.Builder builder, RedisOAuth2Authorization redisOAuth2Authorization) {
        String userCodeValue = redisOAuth2Authorization.getUserCodeValue();
        if (Objects.nonNull(userCodeValue)) {
            OAuth2UserCode userCode = new OAuth2UserCode(userCodeValue, redisOAuth2Authorization.getUserCodeIssuedAt(),
                    redisOAuth2Authorization.getUserCodeExpiresAt());
            builder.token(userCode,
                    metadata -> metadata.putAll(SecurityJacksonUtils.toMap(redisOAuth2Authorization.getUserCodeMetadata())));
        }
    }

    private void parseDeviceCode(OAuth2Authorization.Builder builder, RedisOAuth2Authorization redisOAuth2Authorization) {
        String deviceCodeValue = redisOAuth2Authorization.getDeviceCodeValue();
        if (Objects.nonNull(deviceCodeValue)) {
            OAuth2DeviceCode deviceCode = new OAuth2DeviceCode(deviceCodeValue,
                    redisOAuth2Authorization.getDeviceCodeIssuedAt(),
                    redisOAuth2Authorization.getDeviceCodeExpiresAt());
            builder.token(deviceCode,
                    metadata -> metadata.putAll(SecurityJacksonUtils.toMap(redisOAuth2Authorization.getDeviceCodeMetadata())));
        }
    }

    @SneakyThrows
    private void setAccessToken(RedisOAuth2Authorization redisOAuth2Authorization, OAuth2Authorization.Token<OAuth2AccessToken> token) {
        if (Objects.nonNull(token)) {
            OAuth2AccessToken accessToken = token.getToken();
            redisOAuth2Authorization.setAccessTokenValue(accessToken.getTokenValue());
            redisOAuth2Authorization.setAccessTokenType(accessToken.getTokenType().getValue());
            redisOAuth2Authorization.setAccessTokenExpiresAt(accessToken.getExpiresAt());
            redisOAuth2Authorization.setAccessTokenIssuedAt(accessToken.getIssuedAt());
            redisOAuth2Authorization.setAccessTokenScopes(StringUtils.collectionToDelimitedString(accessToken.getScopes(), ","));
            redisOAuth2Authorization.setAccessTokenMetadata(SecurityJacksonUtils.toJson(token.getMetadata()));
        }
    }

    @SneakyThrows
    private void setRefreshToken(RedisOAuth2Authorization redisOAuth2Authorization, OAuth2Authorization.Token<OAuth2RefreshToken> token) {
        if (Objects.nonNull(token)) {
            OAuth2RefreshToken refreshToken = token.getToken();
            redisOAuth2Authorization.setRefreshTokenValue(refreshToken.getTokenValue());
            redisOAuth2Authorization.setRefreshTokenExpiresAt(refreshToken.getExpiresAt());
            redisOAuth2Authorization.setRefreshTokenIssuedAt(refreshToken.getIssuedAt());
            redisOAuth2Authorization.setRefreshTokenMetadata(SecurityJacksonUtils.toJson(token.getMetadata()));
        }
    }

    @SneakyThrows
    private void setOidcIdToken(RedisOAuth2Authorization redisOAuth2Authorization, OAuth2Authorization.Token<OidcIdToken> token) {
        if (Objects.nonNull(token)) {
            OidcIdToken oidcIdToken = token.getToken();
            redisOAuth2Authorization.setOidcIdTokenValue(oidcIdToken.getTokenValue());
            redisOAuth2Authorization.setOidcIdTokenExpiresAt(oidcIdToken.getExpiresAt());
            redisOAuth2Authorization.setOidcIdTokenIssuedAt(oidcIdToken.getIssuedAt());
            redisOAuth2Authorization.setOidcIdTokenClaims(SecurityJacksonUtils.toJson(oidcIdToken.getClaims()));
            redisOAuth2Authorization.setOidcIdTokenMetadata(SecurityJacksonUtils.toJson(token.getMetadata()));
        }
    }

    @SneakyThrows
    private void setAuthorizationCode(RedisOAuth2Authorization redisOAuth2Authorization, OAuth2Authorization.Token<OAuth2AuthorizationCode> token) {
        if (Objects.nonNull(token)) {
            OAuth2AuthorizationCode authorizationCode = token.getToken();
            redisOAuth2Authorization.setAuthorizationCodeValue(authorizationCode.getTokenValue());
            redisOAuth2Authorization.setAuthorizationCodeExpiresAt(authorizationCode.getExpiresAt());
            redisOAuth2Authorization.setAuthorizationCodeIssuedAt(authorizationCode.getIssuedAt());
            redisOAuth2Authorization.setAuthorizationCodeMetadata(SecurityJacksonUtils.toJson(token.getMetadata()));
        }
    }

    @SneakyThrows
    private void setUserCode(RedisOAuth2Authorization redisOAuth2Authorization, OAuth2Authorization.Token<OAuth2UserCode> token) {
        if (Objects.nonNull(token)) {
            OAuth2UserCode userCode = token.getToken();
            redisOAuth2Authorization.setUserCodeValue(userCode.getTokenValue());
            redisOAuth2Authorization.setUserCodeExpiresAt(userCode.getExpiresAt());
            redisOAuth2Authorization.setUserCodeIssuedAt(userCode.getIssuedAt());
            redisOAuth2Authorization.setUserCodeMetadata(SecurityJacksonUtils.toJson(token.getMetadata()));
        }
    }

    @SneakyThrows
    private void setDeviceCode(RedisOAuth2Authorization redisOAuth2Authorization, OAuth2Authorization.Token<OAuth2DeviceCode> token) {
        if (Objects.nonNull(token)) {
            OAuth2DeviceCode deviceCode = token.getToken();
            redisOAuth2Authorization.setDeviceCodeValue(deviceCode.getTokenValue());
            redisOAuth2Authorization.setDeviceCodeExpiresAt(deviceCode.getExpiresAt());
            redisOAuth2Authorization.setDeviceCodeIssuedAt(deviceCode.getIssuedAt());
            redisOAuth2Authorization.setDeviceCodeMetadata(SecurityJacksonUtils.toJson(token.getMetadata()));
        }
    }

}
