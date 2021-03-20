package com.drphamesl.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;

import com.appslandia.common.jpa.EntityBase;
import com.appslandia.common.validators.BitMask;
import com.appslandia.common.validators.Email;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
@Entity
@NamedQuery(name = "Signup.queryAll", query = "SELECT e FROM Signup e WHERE e.newsMask > 0")
public class Signup extends EntityBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Email
	@Column(length = 255)
	private String email;

	// NewsTypes
	@BitMask(3)
	private int newsMask;

	@NotNull
	@Column(length = 32)
	private String sid;

	@Override
	public Serializable getPk() {
		return this.email;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getNewsMask() {
		return newsMask;
	}

	public void setNewsMask(int newsMask) {
		this.newsMask = newsMask;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}
}
