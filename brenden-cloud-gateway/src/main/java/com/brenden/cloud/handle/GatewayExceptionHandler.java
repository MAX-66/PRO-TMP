package com.brenden.cloud.handle;

import com.brenden.cloud.base.entity.ResultEntity;
import com.brenden.cloud.base.error.GlobalCodeEnum;
import com.brenden.cloud.utils.ReactiveResponseUtil;
import io.micrometer.common.lang.NonNullApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * <p>
 * 异常统一处理
 * </p>
 *
 * @author lxq
 * @since 2024/7/18
 */
@Component
@Slf4j
@NonNullApi
public class GatewayExceptionHandler implements ErrorWebExceptionHandler, Ordered {
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable e) {
        if (e instanceof NotFoundException) {
            log.error("service is offline，error info：{}", e.getMessage(), e);
            return ReactiveResponseUtil.response(exchange, ResultEntity.fail(GlobalCodeEnum.GC_800005));
        }
        if (e instanceof ResponseStatusException responseStatusException) {
            int statusCode = responseStatusException.getStatusCode().value();
            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                log.error("response status code：{}，request URI: {} was not found，error info：{}", statusCode,
                        exchange.getRequest().getPath().pathWithinApplication().value(),
                        e.getMessage(), e);
                return ReactiveResponseUtil.response(exchange, ResultEntity.fail(GlobalCodeEnum.GC_800007));
            }
            else if (statusCode == HttpStatus.BAD_REQUEST.value()) {
                log.error("response status code：{}，bad request，error info：{}", statusCode, e.getMessage(), e);
                return ReactiveResponseUtil.response(exchange, ResultEntity.fail(GlobalCodeEnum.GC_800008));
            }
            else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                log.error("response status code：{}，internal server error，error info：{}", statusCode, e.getMessage(), e);
                return ReactiveResponseUtil.response(exchange,  ResultEntity.fail(GlobalCodeEnum.GC_800006));
            }
        }
        log.error("bad gateway，error info：{}", e.getMessage(), e);
        return ReactiveResponseUtil.response(exchange, ResultEntity.fail(GlobalCodeEnum.GC_800009));
    }

    @Override
    public int getOrder() {
        return -2;
    }
}
