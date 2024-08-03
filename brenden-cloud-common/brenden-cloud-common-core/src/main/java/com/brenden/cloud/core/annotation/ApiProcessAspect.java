package com.brenden.cloud.core.annotation;

import com.brenden.cloud.base.context.UserContextHolder;
import com.brenden.cloud.base.entity.BaseEntity;
import com.brenden.cloud.base.entity.UserContextPayload;
import com.brenden.cloud.base.error.GlobalCodeEnum;
import com.brenden.cloud.base.error.GlobalException;
import com.brenden.cloud.core.properties.DebugProperties;
import com.brenden.cloud.core.utils.JacksonUtil;
import com.brenden.cloud.core.utils.SignUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.HashMap;
import java.util.Map;
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
@RequiredArgsConstructor
public class ApiProcessAspect {


    private final DebugProperties debugProperties;

    @Around(value = "@annotation(apiRequest)")
    public Object aroundMethod(ProceedingJoinPoint joinPoint, ApiProcess apiRequest) throws Throwable {
        // 获取方法参数
        Object[] args = joinPoint.getArgs();
        Map<String, Object> params = getParams(args);
        UserContextPayload context = UserContextHolder.getContext();
        // 签名校验
        // todo feign 不需要校验
        if (apiRequest.checkSign()) {
            checkSign(params, context.getKey());
        }


        for (Object arg : args) {
            if (arg instanceof BaseEntity baseEntity) {
                baseEntity.setToken(context.getToken());
                baseEntity.setUserId(context.getUserId());
                baseEntity.setUsername(context.getUsername());
            }
        }

        return joinPoint.proceed();
    }


    private void checkSign(Map<String, Object> params, String key) {
        String signatureParam = MapUtils.getString(params, SignUtil.KEY_SIGN);
        if (StringUtils.isBlank(signatureParam)) {
            throw new GlobalException(GlobalCodeEnum.GC_800010);
        }
        String sign = SignUtil.sign(params, null);
        if (!sign.equals(signatureParam)) {
            StringBuilder msgSb = new StringBuilder(GlobalCodeEnum.GC_800010.getMsg());
            if (debugProperties.isDevOrTestEnv()) {
                msgSb.append("\n");
                msgSb.append("签名结果：").append(sign).append("\n");
                msgSb.append("接收参数：").append(JacksonUtil.toJson(params)).append("\n");
                msgSb.append("计算签名参数：").append(SignUtil.mapToStringAppendKey(params, key));
            }
            throw new GlobalException(GlobalCodeEnum.GC_800010.getCode(), msgSb.toString());
        }
    }

    private Map<String, Object> getParams(Object[] args) {
        Map<String, Object> params = new HashMap<>();
        for (Object arg : args) {
            params.putAll(JacksonUtil.toMap(JacksonUtil.toJson(arg)));
        }
        return params;
    }
}
