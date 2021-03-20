package com.drphamesl.services;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import com.appslandia.common.jpa.EntityManagerAccessor;
import com.appslandia.common.utils.AssertUtils;
import com.drphamesl.entities.AdUser;
import com.drphamesl.utils.AccountUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
@ApplicationScoped
public class AdUserService {

	@Inject
	protected EntityManagerAccessor em;

	public AdUser findByPk(int userId) throws Exception {
		return em.find(AdUser.class, userId);
	}

	public AdUser findByName(String userName) throws Exception {
		return em.createNamedQuery("AdUser.findByName", AdUser.class).setParameter("userName", userName).getSingleOrNull();
	}

	@Transactional
	public void changePassword(int userId, String newPassword) throws Exception {
		AdUser user = em.find(AdUser.class, userId);
		AssertUtils.assertNotNull(user);

		user.setPassword(AccountUtils.hashPassword(newPassword));
	}
}
