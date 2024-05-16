package com.website.application.mapper;

import com.website.application.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select("SELECT id,userid,username,state FROM USER")
    List<User> getAllUser();

    User getUserById(String userid);
}
