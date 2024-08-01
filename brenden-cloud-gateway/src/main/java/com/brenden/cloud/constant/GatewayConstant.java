package com.brenden.cloud.constant;

/**
 * <p>
 *
 * </p>
 *
 * @author lxq
 * @since 2024/7/17
 */
public class GatewayConstant {

    public static String[] STATIC_ANT_MATCHERS = new String[] { "/error", "/auth/oauth2/*", "/swagger-ui.html", "/swagger-resources/**", "/**/v3/api-docs",
            "/v3/**", "/webjars/**", "*.js", "*.css", "/favicon.ico", "/doc.html", "/v3/api-docs/*"};


    public static final String DATA_ID = "gateway-router.json";

    public static final String ROUTER_KEY = "route:definition";

}
