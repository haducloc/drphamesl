package com.drphamesl.entities;

import java.io.Serializable;
import java.time.OffsetDateTime;

import com.appslandia.common.jpa.EntityBase;
import com.appslandia.common.utils.StringFormat;
import com.appslandia.common.validators.Email;
import com.appslandia.common.validators.MaxLength;
import com.appslandia.common.validators.ValidInts;
import com.drphamesl.utils.AccountUtils;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.validation.constraints.NotNull;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
@Entity
@NamedQuery(name = "Account.findByEmail", query = "SELECT a FROM Account a WHERE a.email=:email")
@NamedQuery(name = "Account.findBySid", query = "SELECT a FROM Account a WHERE a.sid=:sid")
public class Account extends EntityBase {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer accountId;

	@NotNull
	@Email
	@Column(length = 255, unique = true, updatable = false)
	private String email;

	@NotNull
	@MaxLength(255)
	@Column(length = 255)
	private String password;

	@NotNull
	@MaxLength(45)
	@Column(length = 45)
	private String firstName;

	@NotNull
	@MaxLength(45)
	@Column(length = 45)
	private String lastName;

	@ValidInts({ AccountUtils.ACCOUNT_PENDING, AccountUtils.ACCOUNT_ACTIVE, AccountUtils.ACCOUNT_BANNED })
	private int status;

	@ValidInts({ AccountUtils.ID_PROVIDER_APP, AccountUtils.ID_PROVIDER_FB })
	private int idProvider;

	@NotNull
	@Column(length = 32)
	private String sid;

	private OffsetDateTime timeCreated;

	@Override
	public Serializable getPk() {
		return this.accountId;
	}

	public String getDisplayName() {
		return StringFormat.fmt("{} {}", this.firstName, this.lastName);
	}

	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getIdProvider() {
		return idProvider;
	}

	public void setIdProvider(int idProvider) {
		this.idProvider = idProvider;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public OffsetDateTime getTimeCreated() {
		return timeCreated;
	}

	public void setTimeCreated(OffsetDateTime timeCreated) {
		this.timeCreated = timeCreated;
	}
}
