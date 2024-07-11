package com.brenden.cloud.auth.authentication;

import com.brenden.cloud.auth.constants.OauthConstants;
import com.brenden.cloud.error.GlobalCodeEnum;
import com.brenden.cloud.error.GlobalException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Map;

/**
 * <p>
 *
 * </p>
 *
 * @author lxq
 * @since 2024/3/7
 */
public class PasswordAuthenticationConverter implements AuthenticationConverter {

    @Override
    public Authentication convert(HttpServletRequest request) {
        String grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);
        if (!OauthConstants.AGENT_TYPE_PASSWORD.equals(grantType)) {
            return null;
        }

        Authentication clientPrincipal = SecurityContextHolder.getContext().getAuthentication();

        MultiValueMap<String, String> parameters = getParameters(request);

        // USERNAME (REQUIRED)
        String username = parameters.getFirst(OAuth2ParameterNames.USERNAME);
        if (!StringUtils.hasText(username) || parameters.get(OAuth2ParameterNames.USERNAME).size() != 1) {
            throw new OAuth2AuthenticationException(new OAuth2Error(GlobalCodeEnum.GC_800000.getCode(), GlobalCodeEnum.GC_800000.getMsg(), null));
        }

        // PASSWORD (REQUIRED)
        String password = parameters.getFirst(OAuth2ParameterNames.PASSWORD);
        if (!StringUtils.hasText(password) || parameters.get(OAuth2ParameterNames.PASSWORD).size() != 1) {
            throw new OAuth2AuthenticationException(new OAuth2Error(GlobalCodeEnum.GC_800000.getCode(), GlobalCodeEnum.GC_800000.getMsg(), null));
        }

        return new PasswordAuthenticationToken(username, password, clientPrincipal, clientPrincipal.getCredentials());
    }


    private static MultiValueMap<String, String> getParameters(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>(parameterMap.size());
        parameterMap.forEach((key, values) -> parameters.addAll(key, Arrays.asList(values)));
        return parameters;
    }
}
