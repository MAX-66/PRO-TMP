package com.brenden.cloud.auth.core;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

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
            "/v2/**", "/webjars/**", "*.js", "*.css","/favicon.ico","/doc.html", "/v3/api-docs/*", "/auth/*", "/login"};

    /**
     *  认证 Spring Security 过滤器链
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    @Order(2)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
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
                        .anyRequest().authenticated()
                )
                // Form login handles the redirect to the login page from the
                // authorization server filter chain
                .formLogin(Customizer.withDefaults());
        return http.build();
    }

}
