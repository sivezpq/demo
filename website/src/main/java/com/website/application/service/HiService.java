package com.website.application.service;

import com.website.application.mapper.UserMapper;
import com.website.application.repository.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.website.application.entity.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class HiService {
    @Autowired
    UserDAO userDAO;

    @Autowired
    UserMapper userMapper;

    @Autowired
    RestTemplateBuilder restTemplateBuilder;

    @Transactional
    @Cacheable("usercache")
    public User getUser(String id){
        return userMapper.getUserById(id);
    }

    @Transactional
    public List<User> getAllUser(){
        return userDAO.getAllUser();
    }

}
