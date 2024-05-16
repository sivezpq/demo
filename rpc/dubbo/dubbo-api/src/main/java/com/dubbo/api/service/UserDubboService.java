package com.dubbo.api.service;

import com.dubbo.api.entity.User;

/**
 * TODO
 *
 * @author lxy
 * @version 1.0
 * @date 2021/6/3 14:56
 */
public interface UserDubboService {
    User getUser(Long id);
}
