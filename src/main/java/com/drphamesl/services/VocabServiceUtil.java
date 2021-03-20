package com.drphamesl.services;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.appslandia.common.jpa.EntityManagerAccessor;
import com.appslandia.common.utils.TagUtils;
import com.appslandia.plum.caching.CacheChangeEvent;
import com.appslandia.plum.caching.CacheResult;
import com.appslandia.plum.caching.CacheUtils;
import com.drphamesl.caching.Caches;
import com.drphamesl.entities.Vocab;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
@ApplicationScoped
public class VocabServiceUtil {

	static final String CACHE_KEY_VOCABS = "vocabs-${0}";

	@Inject
	protected EntityManagerAccessor em;

	@Inject
	protected CacheChangeEvent cacheChangeEvent;

	@CacheResult(cacheName = Caches.DEFAULT, key = CACHE_KEY_VOCABS)
	public List<Vocab> getVocabs(String tag) {
		List<Vocab> list = em.createNamedQuery("Vocab.queryByTag", Vocab.class).setLikeTag("wtag", tag).getResultList();
		return Collections.unmodifiableList(list);
	}

	public void fireAsync(String... changedDbTags) {
		Set<String> keys = new HashSet<>();
		for (String dbTags : changedDbTags) {

			for (String tag : TagUtils.toTags(dbTags)) {
				keys.add(CacheUtils.format(CACHE_KEY_VOCABS, tag));
			}
		}

		// Notify change
		cacheChangeEvent.fireAsync(Caches.DEFAULT, keys);
	}
}
