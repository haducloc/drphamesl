package com.drphamesl.services;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.appslandia.common.base.Out;
import com.appslandia.common.jpa.EntityManagerImpl;
import com.appslandia.common.utils.AssertUtils;
import com.appslandia.common.utils.BitBool;
import com.appslandia.common.utils.ModelUtils;
import com.appslandia.plum.caching.CacheChangeEvent;
import com.appslandia.plum.caching.CacheResult;
import com.drphamesl.caching.Caches;
import com.drphamesl.entities.VocabList;
import com.drphamesl.utils.TimeUtils;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
@ApplicationScoped
public class VocabListService {

	static final String CACHE_KEY_VOCABLISTS = "vocablists";

	@Inject
	protected EntityManagerImpl em;

	@Inject
	protected CacheChangeEvent cacheChangeEvent;

	public VocabList findByPk(int vocabListId) throws Exception {
		return em.find(VocabList.class, vocabListId);
	}

	@Transactional
	public void remove(int vocabListId) throws Exception {
		em.removeByPk(VocabList.class, vocabListId);
	}

	@Transactional
	public void save(VocabList vocabList) throws Exception {
		if (vocabList.getPk() == null) {
			em.insert(vocabList);
		} else {

			VocabList managed = em.find(VocabList.class, vocabList.getPk());
			AssertUtils.assertNotNull(managed);

			ModelUtils.copy(managed, vocabList, "titleText", "descText", "keywords", "imageUrl", "dispPos", "shareType");
		}

		// Notify change
		cacheChangeEvent.fireAsync(Caches.DEFAULT, CACHE_KEY_VOCABLISTS);
	}

	@Transactional
	public void markNotified(int vocabListId) throws Exception {
		VocabList managed = em.find(VocabList.class, vocabListId);
		AssertUtils.assertNotNull(managed);

		managed.setNotified(BitBool.TRUE);
		managed.setTimeCreated(TimeUtils.nowAtGMT7());
	}

	public List<VocabList> query(Integer shareType, String tag, int pageIndex, int pageSize, Out<Integer> recordCount) {
		if (recordCount.value == null || recordCount.value <= 0) {
			recordCount.value = em.createNamedQuery("VocabList.queryCount").setParameter("shareType", shareType).setParameter("tag", tag).getCount();
		}

		final int startPos = (pageIndex - 1) * pageSize;

		return em.createNamedQuery("VocabList.query", VocabList.class).setParameter("shareType", shareType).setParameter("tag", tag).setStartPos(startPos)
				.setMaxResults(pageSize).asReadonly().getResultList();
	}

	@CacheResult(cacheName = Caches.DEFAULT, key = CACHE_KEY_VOCABLISTS)
	public List<VocabList> getVocabLists() {
		List<VocabList> list = em.createNamedQuery("VocabList.queryList", VocabList.class).asReadonly().getResultList();

		Comparator<VocabList> comparator = Comparator.comparing(VocabList::getShareType).reversed().thenComparing(VocabList::isNew)
				.thenComparing(VocabList::getDispPos).thenComparing(VocabList::getTimeCreated).reversed();

		list.sort(comparator);
		return Collections.unmodifiableList(list);
	}
}
