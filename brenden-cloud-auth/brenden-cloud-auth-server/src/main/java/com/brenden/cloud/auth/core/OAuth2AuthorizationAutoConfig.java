package com.brenden.cloud.auth.core;

import com.brenden.cloud.auth.authentication.PasswordAuthenticationConverter;
import com.brenden.cloud.auth.authentication.PasswordAuthenticationProvider;
import com.brenden.cloud.auth.repository.RedisOAuth2AuthorizationRepository;
import com.brenden.cloud.auth.token.RedisOAuth2AuthorizationService;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.DelegatingOAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.JwtGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2AccessTokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2RefreshTokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.web.authentication.DelegatingAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;

import javax.sql.DataSource;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.List;
import java.util.UUID;

import static org.springframework.data.redis.core.RedisKeyValueAdapter.EnableKeyspaceEvents.OFF;

/**
 * <p>
 *
 * </p>
 *
 * @author brenden
 * @since 2023/7/13
 */
@AutoConfiguration
//@AutoConfiguration(before = { JdbcTemplateAutoConfiguration.class })
@EnableRedisRepositories(enableKeyspaceEvents = OFF,  basePackages = { "com.brenden.cloud.auth.repository"})
@ConditionalOnClass({ DataSource.class })
@EnableWebSecurity
public class OAuth2AuthorizationAutoConfig {

    /**
     * 管理客户端
     * @author lxq
     * @date 2023/9/10 21:51
     * @param jdbcTemplate
     * @return {@link RegisteredClientRepository}
     */
    @Bean
    @ConditionalOnMissingBean(RegisteredClientRepository.class)
    public RegisteredClientRepository registeredClientRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcRegisteredClientRepository(jdbcTemplate);
    }


    @Bean
    public OAuth2AuthorizationService oauth2AuthorizationService(RedisOAuth2AuthorizationRepository redisOAuth2AuthorizationRepository,
                                                                 RegisteredClientRepository registeredClientRepository) {
        return new RedisOAuth2AuthorizationService(redisOAuth2AuthorizationRepository, registeredClientRepository);
    }


    @Bean
    public OAuth2AuthorizationConsentService authorizationConsentService(JdbcTemplate jdbcTemplate, RegisteredClientRepository registeredClientRepository) {
        return new JdbcOAuth2AuthorizationConsentService(jdbcTemplate, registeredClientRepository);
    }

    /**
     * 用于 协议端点  的 Spring Security 过滤器链。
     * @param http
     * @param authorizationService
     * @return
     * @throws Exception
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http,
                                                                      OAuth2AuthorizationService authorizationService,
                                                                      AuthenticationProvider passwordAuthenticationProvider,
                                                                      AuthenticationFailureHandler authenticationFailureHandler,
                                                                      AuthenticationSuccessHandler authenticationSuccessHandler)
            throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer =
                http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                .oidc(Customizer.withDefaults())
                .authorizationService(authorizationService);
        authorizationServerConfigurer.tokenEndpoint(tokenEndpoint ->
                tokenEndpoint.accessTokenRequestConverter(new DelegatingAuthenticationConverter(List.of(new PasswordAuthenticationConverter())))
                        .authenticationProvider(passwordAuthenticationProvider)
                        .accessTokenResponseHandler(authenticationSuccessHandler)
                        .errorResponseHandler(authenticationFailureHandler));
        http.httpBasic(AbstractHttpConfigurer::disable);
        http.exceptionHandling((exceptions) ->
                exceptions.defaultAuthenticationEntryPointFor(new LoginUrlAuthenticationEntryPoint("/login"),
                        new MediaTypeRequestMatcher(MediaType.TEXT_HTML)));
        http.apply(authorizationServerConfigurer);

        return http.build();
    }

    @Bean
    public AuthenticationProvider passwordAuthenticationProvider(PasswordEncoder passwordEncoder,
                                                                 UserDetailsService userDetailsService,
                                                                 OAuth2TokenGenerator<OAuth2Token> tokenGenerator,
                                                                 OAuth2AuthorizationService authorizationService) {
        return new PasswordAuthenticationProvider(passwordEncoder, userDetailsService, tokenGenerator, authorizationService);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
        return new NimbusJwtEncoder(jwkSource);
    }

    @Bean
    public OAuth2TokenGenerator<OAuth2Token> tokenGenerator(JwtEncoder jwtEncoder) {
        JwtGenerator generator = new JwtGenerator(jwtEncoder);
        return new DelegatingOAuth2TokenGenerator(generator, new OAuth2AccessTokenGenerator(),
                new OAuth2RefreshTokenGenerator());
    }


    /**
     * 配置Spring授权服务器
     * @return
     */
    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().build();
    }

    /**
     * 解码签名访问令牌
     * @param jwkSource
     * @return
     */
    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    /**
     * 签署访问令牌
     * @return
     */
    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        KeyPair keyPair = generateRsaKey();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(jwkSet);
    }

    private static KeyPair generateRsaKey() {
        KeyPair keyPair;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        }
        catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
        return keyPair;
    }

}
