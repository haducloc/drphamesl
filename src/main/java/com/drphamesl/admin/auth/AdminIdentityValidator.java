package com.drphamesl.admin.auth;

import com.appslandia.common.base.Out;
import com.appslandia.common.utils.ExceptionUtils;
import com.appslandia.plum.base.AuthFailureResult;
import com.appslandia.plum.base.IdentityValidator;
import com.appslandia.plum.base.PrincipalGroups;
import com.drphamesl.entities.AdUser;
import com.drphamesl.services.AdUserService;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
@ApplicationScoped
public class AdminIdentityValidator implements IdentityValidator {

	@Inject
	protected AdUserService userService;

	@Override
	public PrincipalGroups validate(String identity, Out<String> failureCode) {
		AdUser user = null;
		try {
			user = userService.findByName(identity);
		} catch (Exception ex) {
			throw ExceptionUtils.toUncheckedException(ex);
		}

		if (user == null) {
			failureCode.value = AuthFailureResult.CREDENTIAL_INVALID.getFailureCode();
			return null;
		}
		return new PrincipalGroups(new AdminPrincipal(user.getUserId(), user.getUserName()), null);
	}
}
