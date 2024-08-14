package com.brenden.cloud.handle;

import com.alibaba.csp.sentinel.adapter.spring.webflux.exception.SentinelBlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import com.brenden.cloud.base.entity.ResultEntity;
import com.brenden.cloud.base.error.GlobalCodeEnum;
import com.brenden.cloud.utils.ReactiveResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.result.view.ViewResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author lxq
 * @since 2024/8/14
 */
@Component
@Slf4j
//@NonNullApi
public class SentinelGatewayExceptionHandler extends SentinelBlockExceptionHandler implements Ordered {

    public SentinelGatewayExceptionHandler(List<ViewResolver> viewResolvers, ServerCodecConfigurer serverCodecConfigurer) {
        super(viewResolvers, serverCodecConfigurer);
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ServerHttpRequest request = exchange.getRequest();
        log.error("request uri: {}, request params: {},", request.getURI(), request.getQueryParams());
        if (ex instanceof FlowException flowException) {
            log.error("限流 -> {}", flowException.getMessage());
            return ReactiveResponseUtil.response(exchange,  ResultEntity.fail(GlobalCodeEnum.GC_800014));
        } else if (ex instanceof DegradeException degradeException) {
            log.error("降级 -> {}", degradeException.getMessage());
            return ReactiveResponseUtil.response(exchange,  ResultEntity.fail(GlobalCodeEnum.GC_800015));
        } else if (ex instanceof ParamFlowException paramFlowException){
            log.error("热点参数限流 -> {}", paramFlowException.getMessage());
            return ReactiveResponseUtil.response(exchange,  ResultEntity.fail(GlobalCodeEnum.GC_800016));
        } else if (ex instanceof SystemBlockException systemBlockException) {
            log.error("系统规则 -> {}", systemBlockException.getMessage());
            return ReactiveResponseUtil.response(exchange,  ResultEntity.fail(GlobalCodeEnum.GC_800017));
        } else if (ex instanceof AuthorityException authorityException) {
            log.error("授权规则 -> {}", authorityException.getMessage());
            return ReactiveResponseUtil.response(exchange,  ResultEntity.fail(GlobalCodeEnum.GC_800018));
        }
        return Mono.error(ex);
    }

    @Override
    public int getOrder() {
        return -3;
    }
}
