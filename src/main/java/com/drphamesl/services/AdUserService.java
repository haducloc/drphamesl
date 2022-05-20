package com.drphamesl.services;

import com.appslandia.common.jpa.EntityManagerImpl;
import com.appslandia.common.utils.AssertUtils;
import com.drphamesl.entities.AdUser;
import com.drphamesl.utils.AccountUtils;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
@ApplicationScoped
public class AdUserService {

	@Inject
	protected EntityManagerImpl em;

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
