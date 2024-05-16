package com.website.application.repository;

import com.website.application.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class UserDAO {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public User getUser(String id) {
        String sql = "select * from USER WHERE ID = ?";
        return jdbcTemplate.queryForObject(sql, new UserRowMapper(), id);
    }

    public int updateUsesr(User user) {
        String sql = "update USER set name=? where id=?";
        return jdbcTemplate.update(sql, user.getUsername(), user.getId());
    }

    public List<User> getAllUser() {
        String sql = "select * from USER";
        return jdbcTemplate.query(sql, new UserRowMapper());
    }

    static class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getString("id"));
            user.setUsername(rs.getString("name"));
            return user;
        }
    }
}
