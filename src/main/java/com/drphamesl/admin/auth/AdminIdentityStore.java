package com.drphamesl.admin.auth;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.security.enterprise.credential.Credential;

import com.appslandia.common.base.Out;
import com.appslandia.plum.base.AuthFailureResult;
import com.appslandia.plum.base.IdentityStoreBase;
import com.appslandia.plum.base.PrincipalGroups;
import com.drphamesl.entities.AdUser;
import com.drphamesl.services.AdUserService;
import com.drphamesl.utils.AccountUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
@ApplicationScoped
public class AdminIdentityStore extends IdentityStoreBase {

	@Inject
	protected AdUserService userService;

	@Override
	public Class<? extends Credential> getCredentialType() {
		return AdminCredential.class;
	}

	@Override
	protected PrincipalGroups doValidate(Credential credential, Out<String> failureCode) {
		AdminCredential loginCredential = (AdminCredential) credential;

		AdUser user = null;
		try {
			user = userService.findByName(loginCredential.getCaller());
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

		return new PrincipalGroups(new AdminPrincipal(user.getUserId(), user.getUserName()), null);
	}
}
