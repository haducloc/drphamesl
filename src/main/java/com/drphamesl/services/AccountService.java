package com.drphamesl.services;

import com.appslandia.common.base.UUIDGenerator;
import com.appslandia.common.jpa.EntityManagerImpl;
import com.appslandia.common.utils.AssertUtils;
import com.appslandia.common.utils.ModelUtils;
import com.drphamesl.entities.Account;
import com.drphamesl.entities.Signup;
import com.drphamesl.utils.AccountUtils;
import com.drphamesl.utils.NewsTypes;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
@ApplicationScoped
public class AccountService {

	@Inject
	protected EntityManagerImpl em;

	public Account findByPk(int accountId) throws Exception {
		return em.find(Account.class, accountId);
	}

	public Account findByEmail(String email) throws Exception {
		return em.createNamedQuery("Account.findByEmail", Account.class).setParameter("email", email).getSingleOrNull();
	}

	public Account findBySid(String sid) throws Exception {
		return em.createNamedQuery("Account.findBySid", Account.class).setParameter("sid", sid).getSingleOrNull();
	}

	@Transactional
	public void save(Account account) throws Exception {
		if (account.getPk() == null) {
			em.insert(account);
		} else {

			Account managed = em.find(Account.class, account.getPk());
			AssertUtils.assertNotNull(managed);

			ModelUtils.copy(managed, account, "firstName", "lastName");
		}
	}

	@Transactional
	public synchronized void activate(String email) throws Exception {
		Account account = findByEmail(email);
		AssertUtils.assertNotNull(account);

		if (account.getStatus() == AccountUtils.ACCOUNT_ACTIVE) {
			return;
		}
		account.setStatus(AccountUtils.ACCOUNT_ACTIVE);

		// Add SignUp
		Signup signup = em.find(Signup.class, email);
		if (signup == null) {
			signup = new Signup();

			signup.setEmail(email);
			signup.setSid(UUIDGenerator.INSTANCE.generate());
			signup.setNewsMask(NewsTypes.NEWS_ALL);

			em.insert(signup);
		} else {
			signup.setNewsMask(NewsTypes.NEWS_ALL);
		}
	}

	@Transactional
	public void resetPassword(String email, String newHashPassword) throws Exception {
		Account account = findByEmail(email);
		AssertUtils.assertNotNull(account);

		account.setPassword(newHashPassword);

		if (account.getStatus() == AccountUtils.ACCOUNT_PENDING) {
			account.setStatus(AccountUtils.ACCOUNT_ACTIVE);
		}
	}

	@Transactional
	public void changePassword(int accountId, String newHashPassword) throws Exception {
		Account account = em.find(Account.class, accountId);
		AssertUtils.assertNotNull(account);

		account.setPassword(newHashPassword);
	}
}
