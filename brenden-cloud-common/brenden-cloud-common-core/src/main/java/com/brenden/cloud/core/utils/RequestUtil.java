package com.brenden.cloud.core.utils;

import com.brenden.cloud.base.constant.Constant;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

/**
 * <p>
 *
 * </p>
 *
 * @author lxq
 * @since 2024/8/3
 */
@Slf4j
public class RequestUtil {

    /**
     * 获取请求对象.
     * @return 请求对象
     */
    public static HttpServletRequest getHttpServletRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        Assert.notNull(requestAttributes, "requestAttributes not be null");
        return ((ServletRequestAttributes) requestAttributes).getRequest();
    }


    public static String getAuthorizationValue(HttpServletRequest request) {
        String token = request.getHeader(AUTHORIZATION);
        if (StringUtils.isBlank(token)) {
            log.debug("Token not found in headers. Trying request parameters.");
            token = request.getParameter(AUTHORIZATION);
            if (StringUtils.isBlank(token)) {
                log.debug("Token not found in request parameters.  Not an OAuth2 request.");
                return token;
            }
        }
        return token.substring(Constant.BEARER_TOKEN.length()).trim();
    }

}
