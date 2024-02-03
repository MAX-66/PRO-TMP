package com.brenden.cloud.auth.token;

import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;

/**
 * <p>
 *
 * </p>
 *
 * @author lxq
 * @since 2023/9/8
 */
public class RedisOAuth2AuthorizationService implements OAuth2AuthorizationService {

/*    private RedisUtil redisUtil;

    private RegisteredClientRepository registeredClientRepository;

    public RedisOAuth2AuthorizationService(RedisUtil redisUtil, RegisteredClientRepository registeredClientRepository) {
        this.redisUtil = redisUtil;
        this.registeredClientRepository = registeredClientRepository;
    }*/

    @Override
    public void save(OAuth2Authorization authorization) {

    }

    @Override
    public void remove(OAuth2Authorization authorization) {

    }

    @Override
    public OAuth2Authorization findById(String id) {
        return null;
    }

    @Override
    public OAuth2Authorization findByToken(String token, OAuth2TokenType tokenType) {
        return null;
    }
}
