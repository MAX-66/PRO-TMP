package com.brenden.cloud.auth.authentication;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

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
public class PasswordAuthenticationToken extends UsernamePasswordAuthenticationToken {

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
}
