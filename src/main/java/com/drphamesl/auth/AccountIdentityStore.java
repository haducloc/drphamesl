package com.drphamesl.auth;

import com.appslandia.common.base.Out;
import com.appslandia.plum.base.AuthFailureResult;
import com.appslandia.plum.base.IdentityStoreBase;
import com.appslandia.plum.base.PrincipalGroups;
import com.drphamesl.entities.Account;
import com.drphamesl.services.AccountService;
import com.drphamesl.utils.AccountUtils;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.credential.Credential;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
@ApplicationScoped
public class AccountIdentityStore extends IdentityStoreBase {

	@Inject
	protected AccountService accountService;

	@Override
	public Class<? extends Credential> getCredentialType() {
		return AccountCredential.class;
	}

	@Override
	protected PrincipalGroups doValidate(Credential credential, Out<String> failureCode) {
		AccountCredential loginCredential = (AccountCredential) credential;

		Account user = null;
		try {
			user = accountService.findByEmail(loginCredential.getCaller());
		} catch (Exception ex) {
			failureCode.value = AuthFailureResult.ID_STORE_EXCEPTION.getFailureCode();
			return null;
		}

		if (user == null) {
			failureCode.value = AuthFailureResult.CREDENTIAL_INVALID.getFailureCode();
			return null;
		}

		// Password
		if (!AccountUtils.verifyPassword(loginCredential.getPasswordAsString(), user.getPassword())) {
			failureCode.value = AuthFailureResult.CREDENTIAL_INVALID.getFailureCode();
			return null;
		}

		// Status
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
