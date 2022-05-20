package com.drphamesl.services;

import java.util.List;

import com.appslandia.common.base.UUIDGenerator;
import com.appslandia.common.jpa.EntityManagerImpl;
import com.appslandia.common.logging.AppLogger;
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
public class SignupService {

	@Inject
	protected AppLogger appLogger;

	@Inject
	protected EntityManagerImpl em;

	public List<Signup> queryAll() {
		return em.createNamedQuery("Signup.queryAll", Signup.class).asReadonly().getResultList();
	}

	@Transactional
	public synchronized boolean checkEmail(String email) throws Exception {
		Signup managed = em.find(Signup.class, email);

		if (managed == null) {
			return false;

		} else {
			managed.setNewsMask(NewsTypes.NEWS_ALL);
			return true;
		}
	}

	@Transactional
	public synchronized void activate(String email) throws Exception {
		Signup managed = em.find(Signup.class, email);

		if (managed == null) {
			Signup obj = new Signup();

			obj.setEmail(email);
			obj.setSid(UUIDGenerator.INSTANCE.generate());
			obj.setNewsMask(NewsTypes.NEWS_ALL);

			em.insert(obj);

		} else {
			managed.setNewsMask(NewsTypes.NEWS_ALL);
		}

		// Activate
		Account account = em.createNamedQuery("Account.findByEmail", Account.class).setParameter("email", email).getSingleOrNull();

		if (account != null && account.getStatus() == AccountUtils.ACCOUNT_PENDING) {
			account.setStatus(AccountUtils.ACCOUNT_ACTIVE);
		}
	}

	@Transactional
	public boolean remove(String email, String sid, int type) throws Exception {
		Signup managed = em.find(Signup.class, email);

		if (managed != null && managed.getSid().equals(sid)) {

			if ((managed.getNewsMask() & type) == type) {
				managed.setNewsMask(managed.getNewsMask() - type);
			}
			return true;
		}
		return false;
	}
}
