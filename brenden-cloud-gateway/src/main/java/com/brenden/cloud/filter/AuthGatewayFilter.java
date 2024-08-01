package com.brenden.cloud.filter;

import com.brenden.cloud.base.entity.ResultEntity;
import com.brenden.cloud.base.error.GlobalCodeEnum;
import com.brenden.cloud.utils.ReactiveResponseUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static com.brenden.cloud.constant.GatewayConstant.STATIC_ANT_MATCHERS;

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

    private static final PathMatcher pathMatcher = new AntPathMatcher();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 排除忽略的请求
        if (isUriMatched(exchange.getRequest().getPath().value())) {
            return chain.filter(exchange);
        }
        // 获取token
        ServerHttpRequest request = exchange.getRequest();
        String authorizationValue = getParamValue(request, HttpHeaders.AUTHORIZATION);
        if (StringUtils.isNotBlank(authorizationValue)) {
            return chain
                    .filter(exchange.mutate().request(request.mutate().header(HttpHeaders.AUTHORIZATION, authorizationValue).build()).build());
        }
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return ReactiveResponseUtil.response(exchange,  ResultEntity.fail(GlobalCodeEnum.GC_800004));
    }

    @Override
    public int getOrder() {
        return -1;
    }



    private static boolean isUriMatched(String uri) {
        for (String pattern : STATIC_ANT_MATCHERS) {
            if (pathMatcher.match(pattern, uri)) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        // 测试示例
        String testUri1 = "/v3/api-docs";
        System.out.println(pathMatcher.match("/**/v3/api-docs", testUri1));
    }

    private static String getParamValue(ServerHttpRequest request, String paramName) {
        String paramValue = request.getHeaders().getFirst(paramName);
        if (StringUtils.isBlank(paramValue)) {
            paramValue = request.getQueryParams().getFirst(paramName);
        }
        return paramValue;
    }

}
