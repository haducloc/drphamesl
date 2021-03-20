package com.drphamesl.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import com.appslandia.common.base.Out;
import com.appslandia.common.jpa.EntityManagerAccessor;
import com.appslandia.common.utils.AssertUtils;
import com.appslandia.common.utils.CollectionUtils;
import com.appslandia.common.utils.ModelUtils;
import com.appslandia.common.utils.RandomUtils;
import com.appslandia.common.utils.TagUtils;
import com.appslandia.plum.caching.CacheChangeEvent;
import com.appslandia.plum.caching.CacheResult;
import com.drphamesl.caching.Caches;
import com.drphamesl.entities.Vocab;
import com.drphamesl.models.FlashcardModel;
import com.drphamesl.models.VocabTestModel;
import com.drphamesl.utils.PreTagUtils;
import com.drphamesl.utils.TestOrders;
import com.drphamesl.utils.VocabOrders;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
@ApplicationScoped
public class VocabService {

	@Inject
	protected EntityManagerAccessor em;

	@Inject
	protected VocabServiceUtil vocabServiceUtil;

	@Inject
	protected CacheChangeEvent cacheChangeEvent;

	public Vocab findByPk(int vocabId) throws Exception {
		return em.find(Vocab.class, vocabId);
	}

	public Vocab findByWords(String words) throws Exception {
		return em.createNamedQuery("Vocab.findByWords", Vocab.class).setParameter("s_words", words).getSingleOrNull();
	}

	@Transactional
	public void remove(int vocabId) throws Exception {
		Vocab obj = em.find(Vocab.class, vocabId);
		em.remove(obj);

		// Notify change
		vocabServiceUtil.fireAsync(obj.getTags());
		cacheChangeEvent.fire(Caches.DEFAULT, CACHE_KEY_VOCAB_TAGS);
	}

	@Transactional
	public void save(Vocab vocab) throws Exception {
		if (vocab.getPk() == null) {
			em.insert(vocab);

			// Notify change
			vocabServiceUtil.fireAsync(vocab.getTags());
		} else {

			Vocab managed = em.find(Vocab.class, vocab.getPk());
			AssertUtils.assertNotNull(managed);

			String prevTags = managed.getTags();
			ModelUtils.copy(managed, vocab, "words", "tags", "definitions");

			// Notify change
			vocabServiceUtil.fireAsync(vocab.getTags(), prevTags);
		}

		// Notify change for tags
		cacheChangeEvent.fire(Caches.DEFAULT, CACHE_KEY_VOCAB_TAGS);
	}

	public List<Vocab> queryByTag(String tag) {
		return em.createNamedQuery("Vocab.queryByTag", Vocab.class).setLikeTag("wtag", tag).getResultList();
	}

	public boolean checkTag(String tag) {
		return em.createNamedQuery("Vocab.checkTag").setLikeTag("wtag", tag).getFirstOrNull() != null;
	}

	public List<Vocab> query(String query, int pageIndex, int pageSize, Out<Integer> recordCount) {
		if (recordCount.value == null || recordCount.value <= 0) {
			recordCount.value = em.createNamedQuery("Vocab.queryCount").setParameter("query", query).setLikeTag("wtag", query).setLikeStart("s_words", query)
					.getCount();
		}

		final int startPos = (pageIndex - 1) * pageSize;

		return em.createNamedQuery("Vocab.query", Vocab.class).setParameter("query", query).setLikeTag("wtag", query).setLikeStart("s_words", query)
				.setStartPos(startPos).setMaxResults(pageSize).asReadonly().getResultList();
	}

	public void generateCard(String tag, FlashcardModel model) throws Exception {
		List<Vocab> matchedVocabs = vocabServiceUtil.getVocabs(tag);

		model.setRecordCount(matchedVocabs.size());
		if (matchedVocabs.size() == 0)
			return;

		int vocabIdx = 0;
		final Random rd = new Random();

		if (model.getVocabOrder() == VocabOrders.RANDOM_ORDER) {
			vocabIdx = RandomUtils.nextInt(0, matchedVocabs.size() - 1, rd);

		} else {
			// index: 1+
			if (model.getIndex() > matchedVocabs.size()) {
				model.setIndex(1);
			}
			vocabIdx = model.getIndex() - 1;
		}

		Vocab answerVocab = matchedVocabs.get(vocabIdx);
		model.setVocab(answerVocab);
	}

	public void generateTest(String tag, VocabTestModel model) throws Exception {
		List<Vocab> matchedVocabs = vocabServiceUtil.getVocabs(tag);

		model.setRecordCount(matchedVocabs.size());
		if (matchedVocabs.size() < 4)
			return;

		int vocabIdx = 0;
		final Random rd = new Random();

		if (model.getTestOrder() == TestOrders.RANDOM_ORDER) {
			vocabIdx = RandomUtils.nextInt(0, matchedVocabs.size() - 1, rd);

		} else {
			// index: 1+
			if (model.getIndex() > matchedVocabs.size()) {
				model.setIndex(1);
			}
			vocabIdx = model.getIndex() - 1;
		}

		Vocab answerVocab = matchedVocabs.get(vocabIdx);
		List<Vocab> testVocabs = matchedVocabs.stream().filter(r -> r.getVocabId() != answerVocab.getVocabId()).sorted((r1, r2) -> RandomUtils.rdCompare(rd))
				.limit(3).collect(Collectors.toList());

		testVocabs.add(answerVocab);
		Collections.shuffle(testVocabs);

		model.setVocabId(answerVocab.getVocabId());
		model.setVocabs(testVocabs);
	}

	static final String CACHE_KEY_VOCAB_TAGS = "vocab-tags";

	@CacheResult(cacheName = Caches.DEFAULT, key = CACHE_KEY_VOCAB_TAGS)
	public List<String> getVocabTags() throws Exception {
		List<String> list = em.createNamedQuery("Vocab.queryDbTags").getList();
		Set<String> tags = new HashSet<>();

		for (String dbTags : list) {
			CollectionUtils.toSet(tags, TagUtils.toTags(dbTags));
		}

		List<String> sortedTags = new ArrayList<>(tags);
		Collections.sort(sortedTags,
				(t1, t2) -> Integer.compare(PreTagUtils.PRE_TAGS_ORDERS.getOrDefault(t1, 1000), PreTagUtils.PRE_TAGS_ORDERS.getOrDefault(t2, 2000)));

		return Collections.unmodifiableList(sortedTags);
	}
}
