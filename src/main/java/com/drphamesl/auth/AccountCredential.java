package com.drphamesl.auth;

import jakarta.security.enterprise.credential.UsernamePasswordCredential;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class AccountCredential extends UsernamePasswordCredential {

	public AccountCredential(String email, String password) {
		super(email, password);
	}
}
