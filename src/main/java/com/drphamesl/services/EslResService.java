package com.drphamesl.services;

import java.util.Collections;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import com.appslandia.common.jpa.EntityManagerAccessor;
import com.appslandia.common.utils.AssertUtils;
import com.appslandia.common.utils.ModelUtils;
import com.appslandia.plum.caching.CacheChangeEvent;
import com.appslandia.plum.caching.CacheResult;
import com.drphamesl.caching.Caches;
import com.drphamesl.entities.EslRes;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
@ApplicationScoped
public class EslResService {

	static final String CACHE_KEY_ESL_RESOURCES = "esl-resources";

	@Inject
	protected EntityManagerAccessor em;

	@Inject
	protected CacheChangeEvent cacheChangeEvent;

	public EslRes findByPk(int pk) {
		return em.find(EslRes.class, pk);
	}

	public List<EslRes> queryAll() {
		return em.createNamedQuery("EslRes.queryAll", EslRes.class).setParameter("active", 0).asReadonly().getResultList();
	}

	@Transactional
	public void save(EslRes obj) throws Exception {
		if (obj.getPk() == null) {
			em.insert(obj);

		} else {
			EslRes managed = em.find(EslRes.class, obj.getPk());
			AssertUtils.assertNotNull(managed);

			ModelUtils.copy(managed, obj, "titleText", "descText", "active", "dispPos");
		}

		// Notify change
		cacheChangeEvent.fireAsync(Caches.DEFAULT, CACHE_KEY_ESL_RESOURCES);
	}

	@CacheResult(cacheName = Caches.DEFAULT, key = CACHE_KEY_ESL_RESOURCES)
	public List<EslRes> getEslResources() {
		List<EslRes> list = em.createNamedQuery("EslRes.queryAll", EslRes.class).setParameter("active", 1).asReadonly().getResultList();
		return Collections.unmodifiableList(list);
	}
}
