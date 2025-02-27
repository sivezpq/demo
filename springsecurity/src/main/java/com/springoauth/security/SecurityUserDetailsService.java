package com.springoauth.security;

import com.springoauth.dao.UserMapper;
import com.springoauth.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SecurityUserDetailsService implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //TODO 根据username条件访问数据库得到用户信息
        UserEntity user = userMapper.getUserByName(username);
        if (user==null){//没有找到该用户
            throw new UsernameNotFoundException("账户或密码错误");
        }else if (user.getStatus() == 1){//该用户的Status状态为1（停用）
            throw new UserSuspendException("账户已经停用，请联系管理员！");//自定义异常
        }
        //TODO 封装权限信息，从数据库中获取角色、菜单权限信息，给SimpleGrantedAuthority设置角色时前面加ROLE_
        // RBAC模型: 用户 --->  角色  --->  权限
        List<GrantedAuthority> authorityList = new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority("ROLE_USER"));
        return new SecurityUserDetails(user, authorityList);
    }
}
