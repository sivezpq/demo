package com.springoauth.entity;

import java.io.Serializable;

public class UserEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 主键
	 */
	private Long id;
	private String username;
	private String password;
	private Integer status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
}
