package com.website.framework.security.mapper;

import com.website.framework.security.entity.SecurityUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SecurityUserMapper {
    SecurityUser findByUsername(String username);
}
