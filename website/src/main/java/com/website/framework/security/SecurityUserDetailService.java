package com.website.framework.security;

import com.website.framework.security.entity.SecurityUser;
import com.website.framework.security.entity.SecurityUserRole;
import com.website.framework.security.mapper.SecurityUserMapper;
import com.website.framework.security.mapper.SecurityUserRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
/**
 * 用户身份认证服务类
 * */
@Service("securityUserDetailService")
public class SecurityUserDetailService implements UserDetailsService {
    @Autowired
    private SecurityUserMapper securityUserMapper;
    @Autowired
    private SecurityUserRoleMapper securityUserRoleMapper;
    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        UserDetails userDetails = null;
        try {
            SecurityUser user = securityUserMapper.findByUsername(username);
            if(user != null) {
                List<SecurityUserRole> urs = securityUserRoleMapper.findByUserId(user.getId());
                Collection<GrantedAuthority> authorities = new ArrayList<>();
                for(SecurityUserRole ur : urs) {
                    String roleName = ur.getRole().getRoleid();
                    SimpleGrantedAuthority grant = new SimpleGrantedAuthority(roleName);
                    authorities.add(grant);
                }
                //封装自定义UserDetails类
                userDetails = new SecurityUserDetails(user, authorities);
            } else {
                throw new UsernameNotFoundException("该用户不存在！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userDetails;
    }
}
