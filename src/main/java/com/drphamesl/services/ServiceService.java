package com.drphamesl.services;

import java.util.List;

import com.appslandia.common.jpa.EntityManagerImpl;
import com.appslandia.common.utils.AssertUtils;
import com.appslandia.common.utils.ModelUtils;
import com.appslandia.plum.caching.CacheChangeEvent;
import com.drphamesl.caching.Caches;
import com.drphamesl.entities.Service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
@ApplicationScoped
public class ServiceService {

	@Inject
	protected EntityManagerImpl em;

	@Inject
	protected CacheChangeEvent cacheChangeEvent;

	public Service findByPk(int pk) {
		return em.find(Service.class, pk);
	}

	public List<Service> queryAll() {
		return em.createNamedQuery("Service.queryAll", Service.class).asReadonly().getResultList();
	}

	@Transactional
	public void save(Service obj) throws Exception {
		if (obj.getPk() == null) {
			em.insert(obj);

		} else {
			Service managed = em.find(Service.class, obj.getPk());
			AssertUtils.assertNotNull(managed);

			ModelUtils.copy(managed, obj, "serviceCatId", "titleText", "descText", "tuitionText", "active", "dispPos");
		}

		// Notify change
		cacheChangeEvent.fireAsync(Caches.DEFAULT, ServiceCatService.CACHE_KEY_SERVICES);
	}
}
