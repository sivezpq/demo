package com.dubbo.provider.service;

import com.dubbo.api.entity.User;
import com.dubbo.api.service.UserDubboService;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * TODO
 *
 * @author lxy
 * @version 1.0
 * @date 2021/6/3 14:58
 */
@DubboService
public class UserDobboServiceImpl implements UserDubboService {
    @Override
    public User getUser(Long id) {
        User user = new User();
        user.setId(1L);
        user.setAge(6);
        user.setName("晴天");
        return user;
    }
}
