package com.drphamesl.auth;

import com.appslandia.common.base.Out;
import com.appslandia.common.base.TextGenerator;
import com.appslandia.common.base.TokenGenerator;
import com.appslandia.common.base.UUIDGenerator;
import com.appslandia.common.crypto.DigesterImpl;
import com.appslandia.common.crypto.PasswordDigester;
import com.appslandia.common.crypto.TextDigester;
import com.appslandia.common.utils.EmailUtils;
import com.appslandia.plum.base.AuthFailureResult;
import com.appslandia.plum.base.Modules;
import com.appslandia.plum.base.PrincipalGroups;
import com.appslandia.plum.base.RemMeIdentityStore;
import com.drphamesl.admin.auth.AdminIdentityValidator;
import com.drphamesl.utils.AccountUtils;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
@ApplicationScoped
public class RemMeIdentityStoreImpl extends RemMeIdentityStore {

	final TextGenerator tokenGenerator = new TokenGenerator(64);
	final TextDigester tokenDigester = new PasswordDigester();
	final TextDigester identityDigester = new TextDigester(new DigesterImpl("SHA-256"));

	@Inject
	protected AccountIdentityValidator identityValidator;

	@Inject
	protected AdminIdentityValidator adminIdentityValidator;

	@Override
	protected TextGenerator getSeriesGenerator() {
		return UUIDGenerator.INSTANCE;
	}

	@Override
	protected TextGenerator getTokenGenerator() {
		return this.tokenGenerator;
	}

	@Override
	protected TextDigester getTokenDigester() {
		return this.tokenDigester;
	}

	@Override
	protected TextDigester getIdentityDigester() {
		return this.identityDigester;
	}

	@Override
	protected PrincipalGroups doValidate(String module, String identity, Out<String> failureCode) {
		// AdminUsers
		if (AccountUtils.isAdminUser(identity)) {

			if (EmailUtils.isValid(identity)) {
				return identityValidator.validate(identity, failureCode);
			}

			PrincipalGroups result = adminIdentityValidator.validate(identity, failureCode);
			if (result != null) {
				return result;
			}
		}

		if (Modules.DEFAULT.equalsIgnoreCase(module)) {
			return identityValidator.validate(identity, failureCode);
		}

		if (Modules.ADMIN.equalsIgnoreCase(module)) {
			return adminIdentityValidator.validate(identity, failureCode);
		}

		failureCode.value = AuthFailureResult.CREDENTIAL_INVALID.getFailureCode();
		return null;
	}
}
