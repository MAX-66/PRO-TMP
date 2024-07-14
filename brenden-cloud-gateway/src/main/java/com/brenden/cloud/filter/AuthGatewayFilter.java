package com.brenden.cloud.filter;

import com.brenden.cloud.error.GlobalCodeEnum;
import com.brenden.cloud.error.GlobalException;
import org.apache.http.HttpHeaders;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * <p>
 * 鉴权过滤器
 * </p>
 *
 * @author lxq
 * @since 2024/7/14
 */
@Component
public class AuthGatewayFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 排除忽略的请求

        // 获取token
        ServerHttpRequest request = exchange.getRequest();
        String authorizationValue = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (!StringUtils.hasLength(authorizationValue)) {
            return null;
        }

        // 验证有效性
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        throw new GlobalException(GlobalCodeEnum.GC_800004);
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
