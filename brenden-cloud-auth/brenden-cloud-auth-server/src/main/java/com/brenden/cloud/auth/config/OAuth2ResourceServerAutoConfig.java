package com.brenden.cloud.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.keygen.Base64StringKeyGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2AccessTokenGenerator;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Base64;

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
            "/v3/**", "/webjars/**", "*.js", "*.css", "/favicon.ico", "/doc.html", "/v3/api-docs/*", "/auth/*"};

    /**
     *  认证 Spring Security 资源过滤器链
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, OpaqueTokenIntrospector opaqueTokenIntrospector) throws Exception {
        /*http.headers().frameOptions().disable();
        http.csrf(AbstractHttpConfigurer::disable).cors(AbstractHttpConfigurer::disable);
        // 基于token，关闭session
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        // TODO 需要放开的请求（后期添加）
        http.authorizeHttpRequests((authorize) -> (authorize.requestMatchers(STATIC_ANT_MATCHERS)).permitAll()
                        .anyRequest().authenticated()).formLogin(Customizer.withDefaults());
        http.exceptionHandling().authenticationEntryPoint((request, response, authException) ->
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED));*/
        http.authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(STATIC_ANT_MATCHERS).permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.opaqueToken(token -> token.introspector(opaqueTokenIntrospector)))
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .requestCache(AbstractHttpConfigurer::disable)
                .sessionManagement(AbstractHttpConfigurer::disable)
                .securityContext(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

}
