package com.brenden.cloud.auth.handler;

import com.brenden.cloud.entity.ResultEntity;
import com.brenden.cloud.error.GlobalCodeEnum;
import com.brenden.cloud.utils.JacksonUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * <p>
 * 后处理程序，用于处理 OAuth2AuthenticationException 并返回 OAuth2Error 响应。
 * </p>
 *
 * @author lxq
 * @since 2024/6/11
 */
@Component
public class CustomizedAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        if (exception instanceof OAuth2AuthenticationException oAuth2Exception) {
            OAuth2Error error = oAuth2Exception.getError();

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");

            ResultEntity<Object> fail = ResultEntity.fail(error.getErrorCode(), error.getDescription());
            response.getWriter().write(JacksonUtil.toJson(fail));
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication Failed");
        }
    }
}
