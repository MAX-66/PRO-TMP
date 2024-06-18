package com.brenden.cloud.auth.authentication;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;

import java.io.Serial;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * <p>
 *
 * </p>
 *
 * @author lxq
 * @since 2024/3/8
 */
@Getter
@Setter
public class PasswordAuthenticationToken extends UsernamePasswordAuthenticationToken implements OAuth2AuthenticatedPrincipal {

    @Serial
    private static final long serialVersionUID = 607280695175978849L;

    private String username;

    private String password;

    public PasswordAuthenticationToken() {
        super(null, null);
    }

    public PasswordAuthenticationToken(String username, String password, Object principal, Object credentials) {
        super(principal, credentials);
        this.password = password;
        this.username = username;
    }

    public PasswordAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }

    @Override
    public String getName() {
        return this.username;
    }

    @Override
    public Object getCredentials() {
        return this.password;
    }

    @Override
    public <A> A getAttribute(String name) {
        return OAuth2AuthenticatedPrincipal.super.getAttribute(name);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return Collections.emptyMap();
    }
}
