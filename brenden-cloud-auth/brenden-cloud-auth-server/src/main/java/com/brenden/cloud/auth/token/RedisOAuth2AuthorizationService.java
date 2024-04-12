package com.brenden.cloud.auth.token;

import com.brenden.cloud.auth.model.RedisOAuth2Authorization;
import com.brenden.cloud.auth.repository.RedisOAuth2AuthorizationRepository;
import com.brenden.cloud.utils.JacksonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2DeviceCode;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.OAuth2UserCode;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

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

    @Override
    public void save(OAuth2Authorization authorization) {
        Assert.isTrue(Objects.nonNull(authorization), "authorization is null");
        RedisOAuth2Authorization redisOAuth2Authorization = convent(authorization);
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
        return null;
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
        return null;
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
