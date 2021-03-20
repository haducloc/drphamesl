package com.drphamesl.utils;

import java.util.Set;

import com.appslandia.common.crypto.PasswordDigester;
import com.appslandia.common.utils.CollectionUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class AccountUtils {

	public static final int ACCOUNT_PENDING = 0;
	public static final int ACCOUNT_ACTIVE = 1;
	public static final int ACCOUNT_BANNED = 2;

	public static final int ID_PROVIDER_APP = 1;
	public static final int ID_PROVIDER_FB = 2;

	public static String hashPassword(String password) {
		return getPasswordDigester().digest(password);
	}

	public static boolean verifyPassword(String password, String hash) {
		return getPasswordDigester().verify(password, hash);
	}

	static PasswordDigester getPasswordDigester() {
		return new PasswordDigester();
	}

	static final Set<String> ADMIN_USERS = CollectionUtils.unmodifiableSet("drphamesl@gmail.com", "haducloc13@gmail.com", "drphamesl");

	public static boolean isAdminUser(String username) {
		return ADMIN_USERS.contains(username);
	}

	public static boolean isAdminEmail(String username) {
		return ADMIN_USERS.contains(username) && !username.equals("drphamesl");
	}
}
