package com.brenden.cloud.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.SecurityWebFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * <p>
 * 接口文档拦截
 * </p>
 *
 * @author lxq
 * @since 2024/8/1
 */
@Configuration
@EnableWebFluxSecurity
public class SecurityConfiguration {
    @Bean
    public SecurityWebFilterChain webFluxSecurityFilterChain(ServerHttpSecurity http) {
        http.authorizeExchange((authorize) -> authorize
                .pathMatchers("/doc.html","/swagger-ui.html", "/v3/api-docs/**", "/swagger-ui/**").authenticated()
                .anyExchange().permitAll()
        ).formLogin(withDefaults()).httpBasic(withDefaults()).csrf(withDefaults()).logout(withDefaults());
        return http.build();
    }

}
