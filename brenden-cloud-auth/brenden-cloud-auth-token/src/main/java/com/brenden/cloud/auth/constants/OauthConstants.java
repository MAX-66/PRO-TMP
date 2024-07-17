package com.brenden.cloud.auth.constants;

/**
 * <p>
 *  Oauth 常量类
 * </p>
 *
 * @author lxq
 * @since 2024/3/7
 */
public final class OauthConstants {


    /** 授权类型：密码模式  */
    public static final String AGENT_TYPE_PASSWORD = "password";

    /** oauth2 授权信息 redis存储前缀 */
    public static final String OAUTH2_AUTHORIZATION_PREFIX = "oauth2:authorization";

    /** oauth2 access token redis存储前缀 */
    public static final String OAUTH2_ACCESS_TOKEN_PREFIX = OAUTH2_AUTHORIZATION_PREFIX + ":accessTokenValue:";

    /** oauth2 refresh token redis存储前缀 */
    public static final String OAUTH2_REFRESH_TOKEN_PREFIX = OAUTH2_AUTHORIZATION_PREFIX + ":refreshTokenValue:";

    /** oauth2 授权信息 redis 索引后缀 */
    public static final String OAUTH2_AUTHORIZATION_ID_SUFFIX = ":idx";

    public static String[] STATIC_ANT_MATCHERS = new String[] { "/error", "/auth/oauth2/*", "/swagger-ui.html", "/swagger-resources/**",
            "/v3/**", "/webjars/**", "*.js", "*.css", "/favicon.ico", "/doc.html", "/v3/api-docs/*"};

}
