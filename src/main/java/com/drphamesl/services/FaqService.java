package com.drphamesl.services;

import java.util.Collections;
import java.util.List;

import com.appslandia.common.jpa.EntityManagerImpl;
import com.appslandia.common.utils.AssertUtils;
import com.appslandia.common.utils.ModelUtils;
import com.appslandia.plum.caching.CacheChangeEvent;
import com.appslandia.plum.caching.CacheResult;
import com.drphamesl.caching.Caches;
import com.drphamesl.entities.Faq;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
@ApplicationScoped
public class FaqService {

	static final String CACHE_KEY_FAQS = "faqs";

	@Inject
	protected EntityManagerImpl em;

	@Inject
	protected CacheChangeEvent cacheChangeEvent;

	public Faq findByPk(int pk) {
		return em.find(Faq.class, pk);
	}

	public List<Faq> queryAll() {
		return em.createNamedQuery("Faq.queryAll", Faq.class).setParameter("active", 0).asReadonly().getResultList();
	}

	@Transactional
	public void save(Faq obj) throws Exception {
		if (obj.getPk() == null) {
			em.insert(obj);

		} else {
			Faq managed = em.find(Faq.class, obj.getPk());
			AssertUtils.assertNotNull(managed);

			ModelUtils.copy(managed, obj, "questText", "ansText", "active", "dispPos");
		}

		// Notify change
		cacheChangeEvent.fireAsync(Caches.DEFAULT, CACHE_KEY_FAQS);
	}

	@CacheResult(cacheName = Caches.DEFAULT, key = CACHE_KEY_FAQS)
	public List<Faq> getFaqs() {
		List<Faq> list = em.createNamedQuery("Faq.queryAll", Faq.class).setParameter("active", 1).asReadonly().getResultList();
		return Collections.unmodifiableList(list);
	}
}
