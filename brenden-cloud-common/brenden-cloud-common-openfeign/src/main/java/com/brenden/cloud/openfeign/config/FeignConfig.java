package com.brenden.cloud.openfeign.config;

import com.brenden.cloud.base.constant.Constant;
import com.brenden.cloud.core.utils.RequestUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

import java.util.Collection;

/**
 * <p>
 *
 * </p>
 *
 * @author lxq
 * @since 2024/8/4
 */
@Configuration
public class FeignConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            requestTemplate.header(HttpHeaders.AUTHORIZATION, String.format("%s %s", Constant.BEARER_TOKEN, getToken(requestTemplate)));
            requestTemplate.header(Constant.REQUEST_FEIGN_HEADER, "true");
        };
    }


    private String getToken(RequestTemplate requestTemplate) {
        String token = null;
        Collection<String> tokenCollection = requestTemplate.queries().get("token");
        if (CollectionUtils.isNotEmpty(tokenCollection)) {
            token = IterableUtils.first(tokenCollection);
            return token;
        }
        HttpServletRequest httpServletRequest = RequestUtil.getHttpServletRequest();
        token = RequestUtil.getAuthorizationValue(httpServletRequest);
        return token;
    }
}
