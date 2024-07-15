package com.brenden.cloud.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.server.resource.introspection.ReactiveOpaqueTokenIntrospector;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

/**
 * <p>
 *
 * </p>
 *
 * @author brenden
 * @since 2023/7/13
 */
@Configuration
@EnableWebSecurity
public class OAuth2ResourceServerAutoConfig {

    public static String[] STATIC_ANT_MATCHERS = new String[] { "/error","/swagger-ui.html", "/swagger-resources/**",
            "/v3/**", "/webjars/**", "*.js", "*.css", "/favicon.ico", "/doc.html", "/v3/api-docs/*", "/auth/*", "/auth/oauth2/token"};

    /**
     *  认证 Spring Security 资源过滤器链
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http, ReactiveOpaqueTokenIntrospector opaqueTokenIntrospector,
                                                      ServerAccessDeniedHandler accessDeniedHandler,
                                                      ServerAuthenticationEntryPoint authenticationEntryPoint) throws Exception {
        /*http.headers().frameOptions().disable();
        http.csrf(AbstractHttpConfigurer::disable).cors(AbstractHttpConfigurer::disable);
        // 基于token，关闭session
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        // TODO 需要放开的请求（后期添加）
        http.authorizeHttpRequests((authorize) -> (authorize.requestMatchers(STATIC_ANT_MATCHERS)).permitAll()
                        .anyRequest().authenticated()).formLogin(Customizer.withDefaults());
        http.exceptionHandling().authenticationEntryPoint((request, response, authException) ->
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED));*/
        http
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers(STATIC_ANT_MATCHERS).permitAll()
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .opaqueToken(token -> token.introspector(opaqueTokenIntrospector))
                        // 资源验证失败(无权限)
                        .accessDeniedHandler(accessDeniedHandler)
                        // token令牌失效或无效令牌
                        .authenticationEntryPoint(authenticationEntryPoint)
                )
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .requestCache(ServerHttpSecurity.RequestCacheSpec::disable)
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance());

        return http.build();

    }

}
