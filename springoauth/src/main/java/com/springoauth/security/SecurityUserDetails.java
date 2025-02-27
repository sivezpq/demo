package com.springoauth.security;

import com.springoauth.entity.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class SecurityUserDetails implements UserDetails {

    private UserEntity user;
    private Collection<? extends GrantedAuthority> authorities;

    public SecurityUserDetails(UserEntity userEntity, Collection<? extends GrantedAuthority> authorities){
        super();
        //password = new BCryptPasswordEncoder().encode("123456");
        this.user = userEntity;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // // 权限信息
        // List<SimpleGrantedAuthority> collect = userDTO.getPermissions().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        // // 添加角色信息
        // userDTO.getRoles().stream().forEach(item->collect.add(new SimpleGrantedAuthority("ROLE_"+item)));
        // return collect;
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

    /**
     * 账户是否过期
     * @return
     */
    @Override
    public boolean isAccountNonExpired() {
        // return this.user.getState().equals(SecurityConstant.STATE_ACCOUNTEXPIRED);
        return true;
    }

    /**
     * 是否禁用
     * @return
     */
    @Override
    public boolean isAccountNonLocked() {
        return this.user.getStatus() == 0;
    }

    /**
     * 密码是否过期
     * @return
     */
    @Override
    public boolean isCredentialsNonExpired() {
        // return this.user.getState().equals(SecurityConstant.STATE_TOKENEXPIRED);
        return true;
    }

    /**
     * 是否启用
     * @return
     */
    @Override
    public boolean isEnabled() {
        // return this.user.getState().equals(SecurityConstant.STATE_NO_NORMAL);
        return this.user.getStatus() == 0;
    }

}
