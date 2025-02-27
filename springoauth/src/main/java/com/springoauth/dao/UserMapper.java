package com.springoauth.dao;

import com.springoauth.entity.UserEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {
    /**
     * 获取车型当前排序
     *
     * @param id 车型id
     * @return int
     */
    UserEntity getUserByName(String username);

}
