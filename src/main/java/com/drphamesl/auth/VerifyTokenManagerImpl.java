package com.drphamesl.auth;

import com.appslandia.common.jpa.EntityManagerImpl;
import com.appslandia.common.logging.AppLogger;
import com.appslandia.plum.base.VerifyToken;
import com.appslandia.plum.base.VerifyTokenManager;

import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import jakarta.inject.Inject;
import jakarta.interceptor.Interceptor;
import jakarta.transaction.Transactional;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
@ApplicationScoped
@Alternative
@Priority(Interceptor.Priority.APPLICATION)
public class VerifyTokenManagerImpl implements VerifyTokenManager {

	@Inject
	protected EntityManagerImpl em;

	@Inject
	protected AppLogger logger;

	@Override
	@Transactional
	public void save(VerifyToken token) {
		this.em.persist(token);
	}

	@Override
	public VerifyToken load(String series) {
		return this.em.find(VerifyToken.class, series);
	}

	@Override
	@Transactional
	public void remove(String series) {
		this.logger.error(() -> this.em.removeByPk(VerifyToken.class, series));
	}
}
