package com.brenden.cloud.auth.introspector;

import com.brenden.cloud.auth.model.SecurityUserDetails;
import com.brenden.cloud.base.context.UserContextHolder;
import com.brenden.cloud.base.entity.BaseEntity;
import com.brenden.cloud.base.entity.UserContextPayload;
import io.micrometer.common.lang.NonNullApi;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.security.oauth2.core.OAuth2AccessToken.TokenType.BEARER;

/**
 * <p>
 * 用户信息拦截器
 * </p>
 *
 * @author lxq
 * @since 2024/7/29
 */

@NonNullApi
@Slf4j
public class UserContextInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        SecurityUserDetails userDetails = getSecurityUserDetails();
        UserContextHolder.setContext(convert(userDetails, request));
        return true;
    }

    /**
     * 用户没有登录情况下的处理
     * @return SecurityUserDetails
     */
    private static SecurityUserDetails getSecurityUserDetails() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            return (SecurityUserDetails) authentication.getPrincipal();
        } catch (Exception e) {
            log.debug("No user login information was retrieved in SecurityContextHolder.");
            return new SecurityUserDetails();
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) {
        UserContextHolder.clearContext();
    }


    /**
     * 转换User对象
     * @param userDetails 用户认证信息
     * @param request 请求
     * @return User
     */
    private static UserContextPayload convert(SecurityUserDetails userDetails, HttpServletRequest request) {
        return new UserContextPayload(userDetails.getId(), userDetails.getUsername(), userDetails.getKey(), getParamValue(request));
    }


    /**
     * 获取token
     * @param request 请求
     * @return token
     */
    private static String getParamValue(HttpServletRequest request) {
        String token = request.getHeader(AUTHORIZATION);
        if (StringUtils.isBlank(token)) {
            log.debug("Token not found in headers. Trying request parameters.");
            token = request.getParameter(AUTHORIZATION);
            if (StringUtils.isBlank(token)) {
                log.debug("Token not found in request parameters.  Not an OAuth2 request.");
                return token;
            }
        }
        return token.substring(BEARER.getValue().length()).trim();
    }

}
