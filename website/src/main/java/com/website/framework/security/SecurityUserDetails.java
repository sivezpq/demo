package com.website.framework.security;

import com.website.framework.security.entity.SecurityUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * 自定义用户身份信息
 * */
public class SecurityUserDetails implements UserDetails {
    private static final long serialVersionUID = 1L;
    // 用户信息
    private SecurityUser user;
    // 用户角色
    private Collection<? extends GrantedAuthority> authorities;

    public SecurityUserDetails(SecurityUser user, Collection<? extends GrantedAuthority> authorities) {
        super();
        this.user = user;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.user.getState().equals(SecurityConstant.STATE_ACCOUNTEXPIRED);
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.user.getState().equals(SecurityConstant.STATE_LOCK);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.user.getState().equals(SecurityConstant.STATE_TOKENEXPIRED);
    }

    @Override
    public boolean isEnabled() {
        return this.user.getState().equals(SecurityConstant.STATE_NO_NORMAL);
    }

}
