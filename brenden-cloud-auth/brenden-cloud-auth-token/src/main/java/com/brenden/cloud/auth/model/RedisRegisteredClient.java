package com.brenden.cloud.auth.model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

/**
 * <p>
 * 客户端实体
 * </p>
 *
 * @author lxq
 * @since 2024/4/11
 */
@Data
public class RedisRegisteredClient implements Serializable {

    @Serial
    private static final long serialVersionUID = 7334929548548691445L;
    /**
     * 主键
     */
    private String id;

    /**
     * 客户端id
     */
    private String clientId;

    /**
     * 客户端id签发时间
     */
    private Instant clientIdIssuedAt;

    /**
     * 客户端秘钥
     */
    private String clientSecret;

    /**
     * 客户端秘钥过期时间
     */
    private Instant clientSecretExpiresAt;

    /**
     * 客户端名称
     */
    private String clientName;

    /**
     * 客户端支持的认证方式
     */
    private String clientAuthenticationMethods;

    /**
     * 客户端支持的授权申请方式
     */
    private String authorizationGrantTypes;

    /**
     * 回调地址
     */
    private String redirectUris;

    /**
     * 登出回调地址
     */
    private String postLogoutRedirectUris;

    /**
     * 客户端拥有的scope
     */
    private String scopes;

    /**
     * 客户端配置
     */
    private String clientSettings;

    /**
     * 通过该客户端签发的access token设置
     */
    private String tokenSettings;
}
