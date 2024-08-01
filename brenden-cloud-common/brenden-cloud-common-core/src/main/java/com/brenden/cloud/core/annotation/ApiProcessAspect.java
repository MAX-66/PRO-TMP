package com.brenden.cloud.core.annotation;

import com.brenden.cloud.base.context.UserContextHolder;
import com.brenden.cloud.base.entity.BaseEntity;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * <p>
 *
 * </p>
 *
 * @author lxq
 * @since 2024/7/29
 */
@Aspect
@Component
public class ApiProcessAspect {

    @Around(value = "@annotation(apiRequest)")
    public Object aroundMethod(ProceedingJoinPoint joinPoint, ApiProcess apiRequest) throws Throwable {
        // 获取方法参数
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof BaseEntity baseEntity) {
                BaseEntity context = UserContextHolder.getContext();
                baseEntity.setToken(context.getToken());
                baseEntity.setUserId(context.getUserId());
                baseEntity.setUsername(context.getUsername());
            }
        }
        // 参数缩写处理逻辑
        // 自动注入参数逻辑
        // 参数签名验证逻辑

        // 在这里实现你的具体逻辑
        return joinPoint.proceed();
    }
}
