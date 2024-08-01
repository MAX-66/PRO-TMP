package com.brenden.cloud.doc.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import java.io.IOException;
import java.util.Base64;

/**
 * <p>
 *
 * </p>
 *
 * @author lxq
 * @since 2024/8/1
 */
@Data
public class JakartaBasicFilter implements Filter {

    private boolean enable = false;

    private String username;

    private String password;

    private static final PathMatcher pathMatcher = new AntPathMatcher();

    public static String[] STATIC_ANT_MATCHERS = new String[] {"/swagger-ui.html", "/swagger-resources/**", "/**/v3/api-docs", "/favicon.ico", "/doc.html", "/v3/api-docs/*"};



    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String url = request.getRequestURI();
        if (!isUriMatched(url)) {
            filterChain.doFilter(request, response);
            return;
        }
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Basic ")) {
            // 解码并验证用户名和密码
            String base64Credentials = authHeader.substring("Basic ".length());
            String credentials = new String(Base64.getDecoder().decode(base64Credentials));
            String[] values = credentials.split(":", 2);

            if (values.length == 2 && username.equals(values[0]) && password.equals(values[1])) {
                filterChain.doFilter(request, response); // 认证成功，继续处理请求
                return;
            }
        }
        response.setHeader("WWW-Authenticate", "Basic realm=\"Protected\"");
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }

    private static boolean isUriMatched(String uri) {
        for (String pattern : STATIC_ANT_MATCHERS) {
            if (pathMatcher.match(pattern, uri)) {
                return true;
            }
        }
        return false;
    }

}
