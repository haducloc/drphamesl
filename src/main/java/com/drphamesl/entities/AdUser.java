package com.drphamesl.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;

import com.appslandia.common.jpa.EntityBase;
import com.appslandia.common.validators.MaxLength;
import com.appslandia.common.validators.UserName;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
@Entity
@NamedQuery(name = "AdUser.findByName", query = "SELECT e FROM AdUser e WHERE e.userName=:userName")
public class AdUser extends EntityBase {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer userId;

	@NotNull
	@UserName
	@Column(length = 255, unique = true, updatable = false)
	private String userName;

	@NotNull
	@MaxLength(255)
	@Column(length = 255)
	private String password;

	@Override
	public Serializable getPk() {
		return this.userId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
