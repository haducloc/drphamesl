package com.drphamesl.admin.auth;

import com.appslandia.plum.base.UserPrincipal;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class AdminPrincipal extends UserPrincipal {

	final int userId;

	public AdminPrincipal(int userId, String userName) {
		super(userName);

		this.userId = userId;
	}

	@Override
	public int getUserId() {
		return this.userId;
	}
}
