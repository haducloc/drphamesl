package com.drphamesl.services;

import java.util.Collections;
import java.util.List;

import com.appslandia.common.jpa.EntityManagerImpl;
import com.appslandia.common.utils.AssertUtils;
import com.appslandia.common.utils.BitBool;
import com.appslandia.common.utils.ModelUtils;
import com.appslandia.plum.caching.CacheChangeEvent;
import com.appslandia.plum.caching.CacheResult;
import com.drphamesl.caching.Caches;
import com.drphamesl.entities.ServiceCat;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
@ApplicationScoped
public class ServiceCatService {

	static final String CACHE_KEY_SERVICES = "services";

	@Inject
	protected EntityManagerImpl em;

	@Inject
	protected CacheChangeEvent cacheChangeEvent;

	public ServiceCat findByPk(int pk) {
		return em.find(ServiceCat.class, pk);
	}

	public List<ServiceCat> queryAll() {
		return em.createNamedQuery("ServiceCat.query", ServiceCat.class).setParameter("active", 0).asReadonly().getResultList();
	}

	@Transactional
	public void save(ServiceCat obj) throws Exception {
		if (obj.getPk() == null) {
			em.insert(obj);

		} else {
			ServiceCat managed = em.find(ServiceCat.class, obj.getPk());
			AssertUtils.assertNotNull(managed);

			ModelUtils.copy(managed, obj, "titleText", "active", "dispPos");
		}

		// Notify change
		cacheChangeEvent.fireAsync(Caches.DEFAULT, CACHE_KEY_SERVICES);
	}

	@CacheResult(cacheName = Caches.DEFAULT, key = CACHE_KEY_SERVICES)
	public List<ServiceCat> getServiceCats() {
		List<ServiceCat> list = em.createNamedQueryFetch("ServiceCat.query", ServiceCat.class, "ServiceCatGraph.services").setParameter("active", 1)
				.asReadonly().getResultList();

		list.stream().forEach(c -> c.getServices().removeIf(s -> s.getActive() == BitBool.FALSE));
		return Collections.unmodifiableList(list);
	}
}
