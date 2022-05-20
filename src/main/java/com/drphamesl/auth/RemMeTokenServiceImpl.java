package com.drphamesl.auth;

import com.appslandia.common.jpa.EntityManagerImpl;
import com.appslandia.common.logging.AppLogger;
import com.appslandia.common.utils.AssertUtils;
import com.appslandia.plum.base.RemMeToken;
import com.appslandia.plum.base.RemMeTokenManager;

import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import jakarta.inject.Inject;
import jakarta.interceptor.Interceptor;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
@ApplicationScoped
@Alternative
@Priority(Interceptor.Priority.APPLICATION)
public class RemMeTokenServiceImpl implements RemMeTokenManager {

	@Inject
	protected EntityManagerImpl em;

	@Inject
	protected AppLogger logger;

	@Override
	@Transactional
	public void save(RemMeToken token) {
		this.em.persist(token);
	}

	@Override
	public RemMeToken load(String series) {
		return this.em.find(RemMeToken.class, series);
	}

	@Override
	@Transactional
	public void reissue(String series, String token, long expiresAt, long issuedAt) {
		RemMeToken obj = this.em.find(RemMeToken.class, series);
		AssertUtils.assertNotNull(obj);

		obj.setToken(token);
		obj.setExpiresAt(expiresAt);
		obj.setIssuedAt(issuedAt);
	}

	@Override
	@Transactional
	public void remove(String series) {
		this.logger.error(() -> this.em.removeByPk(RemMeToken.class, series));
	}

	@Override
	@Transactional
	public void removeAll(String hashIdentity) {
		Query nq = this.em.createNativeQuery("DELETE FROM RemMeToken WHERE hashIdentity=:hashIdentity");
		nq.setParameter("hashIdentity", hashIdentity).executeUpdate();
	}
}
