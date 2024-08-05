package com.brenden.cloud.auth.handler;

import com.brenden.cloud.base.entity.ResultEntity;
import com.brenden.cloud.base.error.GlobalCodeEnum;
import com.brenden.cloud.core.utils.JacksonUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * <p>
 * 身份验证未通过调用
 * </p>
 *
 * @author lxq
 * @since 2024/7/11
 */
@Component
@Slf4j
public class CustomizedAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.error("", authException);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");

        ResultEntity<Object> fail = ResultEntity.fail(GlobalCodeEnum.GC_800004);
        response.getWriter().write(JacksonUtil.toJson(fail));
    }
}
