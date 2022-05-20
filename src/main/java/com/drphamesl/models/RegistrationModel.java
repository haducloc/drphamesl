package com.drphamesl.models;

import com.appslandia.common.base.Bind;
import com.appslandia.common.formatters.Formatter;
import com.appslandia.common.validators.Email;
import com.appslandia.common.validators.MaxLength;
import com.appslandia.common.validators.Password;

import jakarta.validation.constraints.NotNull;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class RegistrationModel {

	@NotNull
	@MaxLength(value = 45)
	private String firstName;

	@NotNull
	@MaxLength(value = 45)
	private String lastName;

	@Bind(fmt = Formatter.STRING_ELC)
	@NotNull
	@Email
	private String email;

	@NotNull
	@Password
	private String password;

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
}
