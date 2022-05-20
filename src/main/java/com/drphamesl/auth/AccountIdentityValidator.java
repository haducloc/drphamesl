package com.drphamesl.auth;

import com.appslandia.common.base.Out;
import com.appslandia.common.utils.ExceptionUtils;
import com.appslandia.plum.base.AuthFailureResult;
import com.appslandia.plum.base.IdentityValidator;
import com.appslandia.plum.base.PrincipalGroups;
import com.drphamesl.entities.Account;
import com.drphamesl.services.AccountService;
import com.drphamesl.utils.AccountUtils;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
@ApplicationScoped
public class AccountIdentityValidator implements IdentityValidator {

	@Inject
	protected AccountService accountService;

	@Override
	public PrincipalGroups validate(String identity, Out<String> failureCode) {
		Account user = null;
		try {
			user = accountService.findByEmail(identity);
		} catch (Exception ex) {
			throw ExceptionUtils.toUncheckedException(ex);
		}

		if (user == null) {
			failureCode.value = AuthFailureResult.CREDENTIAL_INVALID.getFailureCode();
			return null;
		}

		if (user.getStatus() == AccountUtils.ACCOUNT_PENDING) {
			failureCode.value = AuthFailureResult.CREDENTIAL_NOT_ACTIVATED.getFailureCode();
			return null;
		}

		if (user.getStatus() == AccountUtils.ACCOUNT_BANNED) {
			failureCode.value = AuthFailureResult.CREDENTIAL_SUSPENDED.getFailureCode();
			return null;
		}
		return new PrincipalGroups(new AccountPrincipal(user.getAccountId(), user.getEmail(), user.getDisplayName()), null);
	}
}
