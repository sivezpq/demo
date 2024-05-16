package com.website.framework.security.mapper;

import com.website.framework.security.entity.SecurityUser;
import com.website.framework.security.entity.SecurityUserRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SecurityUserRoleMapper {
    List<SecurityUserRole> findByUserId(String userid);
}
