package com.brenden.cloud.auth.authentication;

import com.brenden.cloud.auth.constants.OauthConstants;
import com.brenden.cloud.base.constant.SpecialCharacters;
import com.brenden.cloud.base.error.GlobalCodeEnum;
import com.brenden.cloud.base.error.GlobalException;
import com.brenden.cloud.core.utils.EncryptionUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClaimAccessor;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.util.StringUtils;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.security.oauth2.server.authorization.OAuth2TokenType.ACCESS_TOKEN;
import static org.springframework.security.oauth2.server.authorization.OAuth2TokenType.REFRESH_TOKEN;

/**
 * <p>
 *
 * </p>
 *
 * @author lxq
 * @since 2024/3/8
 */

public class PasswordAuthenticationProvider extends DaoAuthenticationProvider {

    private static final String ERROR_URI = "https://datatracker.ietf.org/doc/html/rfc6749#section-5.2";

    private final OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator;

    private final OAuth2AuthorizationService authorizationService;

    private static final OAuth2TokenType ID_TOKEN_TOKEN_TYPE = new OAuth2TokenType(OidcParameterNames.ID_TOKEN);


    public PasswordAuthenticationProvider(PasswordEncoder passwordEncoder, UserDetailsService userDetailsService,
                                          OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator,
                                          OAuth2AuthorizationService authorizationService) {
        super(passwordEncoder);
        super.setUserDetailsService(userDetailsService);
        this.tokenGenerator = tokenGenerator;
        this.authorizationService = authorizationService;
    }


    @Override
    protected Authentication createSuccessAuthentication(Object principal, Authentication authentication, UserDetails user) {
        OAuth2ClientAuthenticationToken oAuth2ClientAuthenticationToken = getOAuth2ClientAuthenticationToken(authentication);
        // RegisteredClient
        RegisteredClient registeredClient = oAuth2ClientAuthenticationToken.getRegisteredClient();
        if (Objects.isNull(registeredClient)) {
            throw new GlobalException(GlobalCodeEnum.GC_800003);
        }
        // Authentication

        // OAuth2AccessToken
        AuthorizationGrantType grantType = getGrantType();
        DefaultOAuth2TokenContext.Builder tokenBuild = DefaultOAuth2TokenContext.builder()
                .registeredClient(registeredClient)
                .principal(authentication)
                .authorizedScopes(registeredClient.getScopes())
                .authorizationServerContext(AuthorizationServerContextHolder.getContext())
                .authorizationGrantType(grantType)
                .authorizationGrant(oAuth2ClientAuthenticationToken)
                .tokenType(ACCESS_TOKEN);
        DefaultOAuth2TokenContext tokenContext = tokenBuild.build();
        OAuth2Token generatedAccessToken = Optional.ofNullable(tokenGenerator.generate(tokenContext))
                .orElseThrow(() -> new GlobalException(GlobalCodeEnum.GC_800003));
        OAuth2AccessToken accessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER,
                generatedAccessToken.getTokenValue(), generatedAccessToken.getIssuedAt(),
                generatedAccessToken.getExpiresAt(), tokenContext.getAuthorizedScopes());

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(user, user.getUsername(), user.getAuthorities());
        String key = createKey();
        OAuth2Authorization.Builder authorizationBuilder = OAuth2Authorization.withRegisteredClient(registeredClient)
                // 使用client id 与 登录名 md5算法作为id
                .id(generateKey(registeredClient.getClientId(), user.getUsername()))
                .principalName(authentication.getName())
                .authorizedScopes(registeredClient.getScopes())
                .attribute(Principal.class.getName(), usernamePasswordAuthenticationToken)
                .attribute("key", key)
                .authorizationGrantType(grantType);
        if (generatedAccessToken instanceof ClaimAccessor claimAccessor) {
            authorizationBuilder.token(accessToken, (metadata) ->
                    metadata.put(OAuth2Authorization.Token.CLAIMS_METADATA_NAME, claimAccessor.getClaims())
            );
        } else {
            authorizationBuilder.accessToken(accessToken);
        }

        // refresh_token
        OAuth2RefreshToken refreshToken = null;
        if (registeredClient.getAuthorizationGrantTypes().contains(AuthorizationGrantType.REFRESH_TOKEN)) {
            DefaultOAuth2TokenContext refreshTokenContext = tokenBuild.tokenType(REFRESH_TOKEN).build();
            OAuth2Token generatedRefreshToken = Optional.ofNullable(tokenGenerator.generate(refreshTokenContext))
                    .orElseThrow(() -> new GlobalException(GlobalCodeEnum.GC_800003));
            refreshToken = (OAuth2RefreshToken) generatedRefreshToken;
            authorizationBuilder.refreshToken(refreshToken);
        }


        // ----- ID token -----
        OidcIdToken idToken;
        if (registeredClient.getScopes().contains(OidcScopes.OPENID)) {
            // @formatter:off
            tokenContext = tokenBuild
                    .tokenType(ID_TOKEN_TOKEN_TYPE)
                    .authorization(authorizationBuilder.build())	// ID token customizer may need access to the access token and/or refresh token
                    .build();
            // @formatter:on
            OAuth2Token generatedIdToken = this.tokenGenerator.generate(tokenContext);
            if (!(generatedIdToken instanceof Jwt)) {
                throw new GlobalException(GlobalCodeEnum.GC_800003);
            }

            idToken = new OidcIdToken(generatedIdToken.getTokenValue(), generatedIdToken.getIssuedAt(),
                    generatedIdToken.getExpiresAt(), ((Jwt) generatedIdToken).getClaims());
            authorizationBuilder.token(idToken, (metadata) ->
                    metadata.put(OAuth2Authorization.Token.CLAIMS_METADATA_NAME, idToken.getClaims()));
        } else {
            idToken = null;
        }

        OAuth2Authorization authorization = authorizationBuilder.build();

        this.authorizationService.save(authorization);

        Map<String, Object> additionalParameters = new HashMap<>();
        additionalParameters.put("key", key);
        if (idToken != null) {
            additionalParameters.put(OidcParameterNames.ID_TOKEN, idToken.getTokenValue());
        }


        return new OAuth2AccessTokenAuthenticationToken(registeredClient, authentication, accessToken, refreshToken, additionalParameters);
    }


    private OAuth2ClientAuthenticationToken getOAuth2ClientAuthenticationToken(Authentication authentication) {
        OAuth2ClientAuthenticationToken clientPrincipal = null;
        if (OAuth2ClientAuthenticationToken.class.isAssignableFrom(authentication.getPrincipal().getClass())) {
            clientPrincipal = (OAuth2ClientAuthenticationToken) authentication.getPrincipal();
        }
        if (Objects.nonNull(clientPrincipal) && clientPrincipal.isAuthenticated()) {
            return clientPrincipal;
        }
        throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_REQUEST);
    }


    private AuthorizationGrantType getGrantType() {
        return new AuthorizationGrantType(OauthConstants.AGENT_TYPE_PASSWORD);
    }


    private static String generateKey(String clientId, String userId) {
        Map<String, Object> values = new HashMap<>();
        values.put("user_id", userId);
        values.put("client_id", clientId);
        return EncryptionUtil.encryptMD5(values.toString());
    }


    private static String createKey() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replace(SpecialCharacters.MINUS_SIGN, "");
    }

}
