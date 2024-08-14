package com.brenden.cloud.sentinel.handler;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import com.brenden.cloud.base.entity.ResultEntity;
import com.brenden.cloud.base.error.GlobalCodeEnum;
import com.brenden.cloud.core.utils.ResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 *
 * </p>
 *
 * @author lxq
 * @since 2024/8/14
 */
@Slf4j
public class SentinelBlockExceptionHandler implements BlockExceptionHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, BlockException ex) throws Exception {
        log.error("request uri: {}, request params: {},", request.getRequestURI(), request.getParameterMap());
        if (ex instanceof FlowException flowException) {
            log.error("限流 -> {}", flowException.getMessage());
            ResponseUtil.response(response,  ResultEntity.fail(GlobalCodeEnum.GC_800014));
        } else if (ex instanceof DegradeException degradeException) {
            log.error("降级 -> {}", degradeException.getMessage());
            ResponseUtil.response(response,  ResultEntity.fail(GlobalCodeEnum.GC_800015));
        } else if (ex instanceof ParamFlowException paramFlowException){
            log.error("热点参数限流 -> {}", paramFlowException.getMessage());
            ResponseUtil.response(response,  ResultEntity.fail(GlobalCodeEnum.GC_800016));
        } else if (ex instanceof SystemBlockException systemBlockException) {
            log.error("系统规则 -> {}", systemBlockException.getMessage());
            ResponseUtil.response(response,  ResultEntity.fail(GlobalCodeEnum.GC_800017));
        } else if (ex instanceof AuthorityException authorityException) {
            log.error("授权规则 -> {}", authorityException.getMessage());
            ResponseUtil.response(response,  ResultEntity.fail(GlobalCodeEnum.GC_800018));
        }
    }
}
