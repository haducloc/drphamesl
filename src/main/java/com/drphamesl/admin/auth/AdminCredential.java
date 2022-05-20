package com.drphamesl.admin.auth;

import jakarta.security.enterprise.credential.UsernamePasswordCredential;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class AdminCredential extends UsernamePasswordCredential {

	public AdminCredential(String userName, String password) {
		super(userName, password);
	}
}
