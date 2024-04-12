package com.brenden.cloud.auth.model;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 授权确认信息实体
 * </p>
 *
 * @author lxq
 * @since 2024/4/11
 */
@Data
public class OAuth2AuthorizationConsent implements Serializable {

    /**
     * 额外提供的主键
     */
    private String id;

    /**
     * 当前授权确认的客户端id
     */
    private String registeredClientId;

    /**
     * 当前授权确认用户的 username
     */
    private String principalName;

    /**
     * 授权确认的scope
     */
    private String authorities;
}
