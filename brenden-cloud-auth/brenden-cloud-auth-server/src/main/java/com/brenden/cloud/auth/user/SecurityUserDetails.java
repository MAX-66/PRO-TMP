package com.brenden.cloud.auth.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.brenden.cloud.constant.Constant.YES;

/**
 * <p>
 *
 * </p>
 *
 * @author lxq
 * @since 2023/9/10
 */
@Data
@NoArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
public class SecurityUserDetails implements UserDetails, OAuth2AuthenticatedPrincipal, Serializable {

    @Serial
    private static final long serialVersionUID = 3957586021470480642L;

    private Long id;

    private String username;

    private String password;

    private Integer status;

    private List<String> roles;

    @Override
    @JsonIgnore
    public Map<String, Object> getAttributes() {
        return Collections.emptyMap();
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (CollectionUtils.isEmpty(roles)) {
            return Collections.emptyList();
        }
        return AuthorityUtils.createAuthorityList(roles);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return YES == status;
    }

    public SecurityUserDetails(Long id, String username, String password, Integer status, List<String> roles) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.status = status;
        this.roles = roles;
    }

    @Override
    @JsonIgnore
    public String getName() {
        return this.getUsername();
    }
}
