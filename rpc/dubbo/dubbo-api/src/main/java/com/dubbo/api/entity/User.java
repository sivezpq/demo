package com.dubbo.api.entity;

import java.io.Serializable;

/**
 * TODO
 *
 * @author lxy
 * @version 1.0
 * @date 2021/6/3 14:56
 */
public class User implements Serializable {
    private Long id;
    private Integer age;
    private String name;

    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
